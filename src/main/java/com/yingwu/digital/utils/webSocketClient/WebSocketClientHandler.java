package com.yingwu.digital.utils.webSocketClient;

import com.alibaba.fastjson.JSONObject;
import com.yingwu.digital.base.DigitalException;
import com.yingwu.digital.bean.POJO.KLine;
import com.yingwu.digital.bean.dto.KlineDto;
import com.yingwu.digital.dao.KLineMapper;
import com.yingwu.digital.utils.GZipUtil;
import com.yingwu.digital.utils.GameUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    @Autowired
    private KLineMapper kLineMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private final WebSocketClientHandshaker handShaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(final WebSocketClientHandshaker handShaker) {
        this.handShaker = handShaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        handShaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        System.out.println("WebSocket Client disconnected!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Channel ch = ctx.channel();
        if (!handShaker.isHandshakeComplete()) {
            handShaker.finishHandshake(ch, (FullHttpResponse) msg);
            handshakeFuture.setSuccess();
            return;
        }

        final WebSocketFrame frame = (WebSocketFrame) msg;
        ByteBuf buf = frame.content();

        // ## 需要解压的二进制数据
        String str = GZipUtil.deToString(GameUtil.toBytes(buf));
        saveData(str);
        System.out.println(str);

        JSONObject json = JSONObject.parseObject(str);
        if (json.containsKey("ping")) {
            JSONObject pong = new JSONObject();
            pong.put("pong", json.get("ping"));

            System.out.println(pong);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(pong.toString()));
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        cause.printStackTrace();

        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }

        ctx.close();
    }

    private void saveData(String data) throws DigitalException{
        JSONObject jsonObject = JSONObject.parseObject(data);
        if(jsonObject.getString("ch").contains("kline")){
            KLine klineDto = JSONObject.parseObject(jsonObject.getString("tick"),KLine.class);
            klineDto.setKlinId(klineDto.getId().toString());
            klineDto.setId(null);
            Map<String,String> returnMap = getSymbolByCh(jsonObject.getString("ch"));
            klineDto.setChannel(jsonObject.getString("ch"));
            klineDto.setTs(jsonObject.getString("ts"));
            klineDto.setsymbol(returnMap.get("symbol"));
            klineDto.setPeriod(returnMap.get("period"));
            int count = kLineMapper.insert(klineDto);
            if(count < 1){
                throw new DigitalException("新增数据失败！" + jsonObject.getString("ch"));
            }
        }else if(jsonObject.getString("ch").contains("depth")){
            KLine klineDto = JSONObject.parseObject(jsonObject.getString("tick"),KLine.class);
            klineDto.setKlinId(klineDto.getId().toString());
            klineDto.setId(null);
            Map<String,String> returnMap = getSymbolByCh(jsonObject.getString("ch"));
            klineDto.setChannel(jsonObject.getString("ch"));
            klineDto.setTs(jsonObject.getString("ts"));
            klineDto.setsymbol(returnMap.get("symbol"));
            klineDto.setPeriod(returnMap.get("period"));
            int count = kLineMapper.insert(klineDto);
            if(count < 1){
                throw new DigitalException("新增数据失败！" + jsonObject.getString("ch"));
            }
        }
    }
    private static Map<String,String> getSymbolByCh(String ch){
        String[] beginIndex = ch.split("\\.");
        Map<String,String> returnMap = new HashMap<>();
        returnMap.put("symbol",beginIndex[1]);
        returnMap.put("period",beginIndex[3]);
        return returnMap;
    }

    public static void main(String[] args) {
//        saveData("");
    }
}