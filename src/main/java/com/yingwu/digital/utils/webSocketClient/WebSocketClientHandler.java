package com.yingwu.digital.utils.webSocketClient;

import com.alibaba.fastjson.JSONObject;
import com.yingwu.digital.base.DigitalException;
import com.yingwu.digital.bean.POJO.Depth;
import com.yingwu.digital.bean.POJO.KLine;
import com.yingwu.digital.bean.POJO.TradeDetail;
import com.yingwu.digital.bean.dto.Tick;
import com.yingwu.digital.bean.dto.TradeDataDto;
import com.yingwu.digital.bean.dto.TradeDetailDto;
import com.yingwu.digital.dao.DepthMapper;
import com.yingwu.digital.dao.KLineMapper;
import com.yingwu.digital.dao.MarketDetailMapper;
import com.yingwu.digital.dao.TradeDetailMapper;
import com.yingwu.digital.service.HuobiApiService;
import com.yingwu.digital.utils.BaseBeanUtils;
import com.yingwu.digital.utils.GZipUtil;
import com.yingwu.digital.utils.GameUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
@Component
@ChannelHandler.Sharable
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

//    @Autowired
//    private KLineMapper kLineMapper;

    @Autowired
    private HuobiApiService huobiApiService;
//    private DepthMapper depthMapper;
//
//    @Autowired
//    private MarketDetailMapper marketDetailMapper;
//
//    @Autowired
//    private TradeDetailMapper tradeDetailMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private WebSocketClientHandshaker handShaker;
    private ChannelPromise handshakeFuture;

    public void setHandShaker(URI uri) {
        this.handShaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, false, HttpHeaders.EMPTY_HEADERS, 1280000);
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
        if(jsonObject.containsKey("ch")) {
            System.out.println(data);
            if (jsonObject.getString("ch").contains("kline")) {
                KLine klineDto = JSONObject.parseObject(jsonObject.getString("tick"), KLine.class);
                klineDto.setKlinId(klineDto.getId().toString());
                klineDto.setId(null);
                Map<String, String> returnMap = getSymbolByCh(jsonObject.getString("ch"));
                klineDto.setChannel(jsonObject.getString("ch"));
                klineDto.setTs(jsonObject.getString("ts"));
                klineDto.setSymbol(returnMap.get("symbol"));
                klineDto.setPeriod(returnMap.get("period"));
                int count = 1/*kLineMapper.insert(klineDto)*/;
                if (count < 1) {
                    throw new DigitalException("新增Kline数据失败！" + jsonObject.getString("ch") + jsonObject.getString("ts"));
                }
            } else if (jsonObject.getString("ch").contains("depth")) {
                Depth Depth = JSONObject.parseObject(jsonObject.getString("tick"), Depth.class);
                Map<String, String> returnMap = getSymbolByCh(jsonObject.getString("ch"));
                Depth.setChannel(jsonObject.getString("ch"));
                Depth.setTs(jsonObject.getString("ts"));
                Depth.setSymbol(returnMap.get("symbol"));
                int count = 1/*depthMapper.insert(Depth)*/;
                if (count < 1) {
                    throw new DigitalException("新增depth数据失败！" + jsonObject.getString("ch") + jsonObject.getString("ts"));
                }
            } else if (jsonObject.getString("ch").contains("trade")) {
                TradeDetailDto tradeDetailDto = JSONObject.parseObject(data, TradeDetailDto.class);
                Map<String, String> returnMap = getSymbolByCh(jsonObject.getString("ch"));
                if (tradeDetailDto.getTick() != null && tradeDetailDto.getTick().getData() != null &&
                        tradeDetailDto.getTick().getData().size() > 0) {
                    Tick tick = tradeDetailDto.getTick();
                    for (TradeDataDto tmp : tradeDetailDto.getTick().getData()) {
                        TradeDetail tradeDetail = new TradeDetail();
                        tradeDetail.setChannel(tradeDetailDto.getCh());
                        tradeDetail.setSymbol(returnMap.get("symbol"));
                        BaseBeanUtils.copyPropertiesIgnoreNull(tmp, tradeDetail);
                        tradeDetail.setTickId(tick.getId());
                        tradeDetail.setTradeTs(tick.getTs());
                        tradeDetail.setMsgId(tmp.getId());
                        tradeDetail.setTradeTime(tmp.getTime());
                        int count = 0;
                        try {
//                            count = tradeDetailMapper.insertSelective(tradeDetail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (count < 1) {
                            throw new DigitalException("新增tradeDetail数据失败！" + jsonObject.getString("ch") + jsonObject.getString("ts"));
                        }
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

}