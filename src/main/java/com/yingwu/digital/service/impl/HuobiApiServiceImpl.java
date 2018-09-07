package com.yingwu.digital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yingwu.digital.base.ApiResponse;
import com.yingwu.digital.base.DigitalException;
import com.yingwu.digital.bean.POJO.KLine;
import com.yingwu.digital.dao.KLineMapper;
import com.yingwu.digital.service.HuobiApiService;
import com.yingwu.digital.utils.webSocketClient.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class HuobiApiServiceImpl implements HuobiApiService {
    private WebSocketClient client = null;

    @Autowired
    private KLineMapper kLineMapper;

    @Override
    public ApiResponse connectWebSocket(String json) throws DigitalException {
        json = "wss://www.hbg.com/-/s/pro/ws";
//        final String url = "wss://api.hadax.com/ws";
        client = new WebSocketClient(json);
        try {
            client.open();
            CountDownLatch latch = new CountDownLatch(1);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ApiResponse();
    }

    @Override
    public ApiResponse requestMsg(String json) throws DigitalException {
//        JSONObject sub = new JSONObject();
        JSONObject sub = new JSONObject();
        sub.put("sub", "market.btcusdt.trade.detail");
        sub.put("id", "id1000");
        client.sendText(sub.toString());

//
//        sub.put("sub", "market.btcusdt.kline.1min");
//        sub.put("id", "id1000");
        return new ApiResponse();
    }

    public void saveKlineDate(KLine date){
        System.out.println(date.toString());
        int count = kLineMapper.insert(date);
        System.out.println(count);
    }

}
