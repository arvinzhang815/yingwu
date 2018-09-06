package com.yingwu.digital.utils.webSocketClient;

import com.alibaba.fastjson.JSONObject;
import com.yingwu.digital.base.DigitalException;
import com.yingwu.digital.bean.POJO.Depth;
import com.yingwu.digital.bean.POJO.KLine;
import com.yingwu.digital.bean.POJO.TradeDetail;
import com.yingwu.digital.bean.dto.KlineDto;
import com.yingwu.digital.bean.dto.Tick;
import com.yingwu.digital.bean.dto.TradeDataDto;
import com.yingwu.digital.bean.dto.TradeDetailDto;
import com.yingwu.digital.bean.dto.TradeDetailDto.*;
import com.yingwu.digital.dao.DepthMapper;
import com.yingwu.digital.dao.KLineMapper;
import com.yingwu.digital.dao.MarketDetailMapper;
import com.yingwu.digital.dao.TradeDetailMapper;
import com.yingwu.digital.utils.BaseBeanUtils;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.tools.Diagnostic;
import java.util.HashMap;
import java.util.Map;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    @Autowired
    private KLineMapper kLineMapper;

    @Autowired
    private DepthMapper depthMapper;

    @Autowired
    private MarketDetailMapper marketDetailMapper;

    @Autowired
    private TradeDetailMapper tradeDetailMapper;

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
//        saveData(str);
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
            klineDto.setSymbol(returnMap.get("symbol"));
            klineDto.setPeriod(returnMap.get("period"));
            int count = kLineMapper.insert(klineDto);
            if(count < 1){
                throw new DigitalException("新增Kline数据失败！" + jsonObject.getString("ch") + jsonObject.getString("ts"));
            }
        }else if(jsonObject.getString("ch").contains("depth")){
            Depth Depth = JSONObject.parseObject(jsonObject.getString("tick"),Depth.class);
            Map<String,String> returnMap = getSymbolByCh(jsonObject.getString("ch"));
            Depth.setChannel(jsonObject.getString("ch"));
            Depth.setTs(jsonObject.getString("ts"));
            Depth.setSymbol(returnMap.get("symbol"));
            int count = depthMapper.insert(Depth);
            if(count < 1){
                throw new DigitalException("新增depth数据失败！" + jsonObject.getString("ch") + jsonObject.getString("ts"));
            }
        }else if (jsonObject.getString("ch").contains("trade")){
            TradeDetailDto tradeDetailDto = JSONObject.parseObject(data,TradeDetailDto.class);
            Map<String,String> returnMap = getSymbolByCh(jsonObject.getString("ch"));
            if(tradeDetailDto.getTick() != null && tradeDetailDto.getTick().getData() != null &&
                    tradeDetailDto.getTick().getData().size() > 0){
                for (TradeDataDto tmp : tradeDetailDto.getTick().getData()){
                    TradeDetail tradeDetail = new TradeDetail();
                    tradeDetail.setChannel(tradeDetailDto.getCh());
                    tradeDetail.setSymbol(returnMap.get("symbol"));
                    BaseBeanUtils.copyPropertiesIgnoreNull(tradeDetailDto,tradeDetail);
                    BaseBeanUtils.copyPropertiesIgnoreNull(tmp,tradeDetail);
                    int count = tradeDetailMapper.insert(tradeDetail);
                    if(count < 1){
                        throw new DigitalException("新增tradeDetail数据失败！" + jsonObject.getString("ch") + jsonObject.getString("ts"));
                    }
                }
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
        String data = "{\"ch\":\"market.btcusdt.trade.detail\",\"ts\":1536219685504,\"tick\":{\"id\":18825105385,\"ts\":1536219685343,\"data\":[{\"amount\":2.184400000000000000,\"ts\":1536219685343,\"id\":1882510538511782201841,\"price\":6429.000000000000000000,\"direction\":\"sell\"},{\"amount\":2.184400000000000000,\"ts\":1536219685343,\"id\":1882510538511782201841,\"price\":6429.000000000000000000,\"direction\":\"sell\"}]}}";
        TradeDetailDto tradeDetailDto = JSONObject.parseObject(data,TradeDetailDto.class);
        Map<String,String> returnMap = getSymbolByCh("market.btcusdt.trade.detail");
        if(tradeDetailDto.getTick() != null && tradeDetailDto.getTick().getData() != null &&
                tradeDetailDto.getTick().getData().size() > 0){
            for (TradeDataDto tmp : tradeDetailDto.getTick().getData()){
                TradeDetail tradeDetail = new TradeDetail();
                tradeDetail.setChannel(tradeDetailDto.getCh());
                tradeDetail.setSymbol(returnMap.get("symbol"));
                BaseBeanUtils.copyPropertiesIgnoreNull(tmp,tradeDetail);

                System.out.println(tradeDetail);
            }
        }
    }
}