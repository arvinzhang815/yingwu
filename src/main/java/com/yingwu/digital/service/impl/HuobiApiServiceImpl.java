package com.yingwu.digital.service.impl;

import com.yingwu.digital.base.ApiResponse;
import com.yingwu.digital.base.DigitalException;
import com.yingwu.digital.service.HuobiApiService;
import com.yingwu.digital.utils.webSocketClient.WebSocketClient;
import org.springframework.stereotype.Service;

@Service
public class HuobiApiServiceImpl implements HuobiApiService {
    private WebSocketClient client = null;

    @Override
    public ApiResponse connectWebSocket(String json) throws DigitalException {
        client = new WebSocketClient(json);
        return new ApiResponse();
    }

    @Override
    public ApiResponse requestMsg(String json) throws DigitalException {
//        JSONObject sub = new JSONObject();
//
//        sub.put("sub", "market.btcusdt.kline.1min");
//        sub.put("id", "id1000");
        client.sendText(json);
        return new ApiResponse();
    }

}
