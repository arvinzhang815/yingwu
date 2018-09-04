package com.yingwu.digital;

import com.alibaba.fastjson.JSONObject;
import com.yingwu.digital.utils.webSocketClient.WebSocketClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DigitalApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void connect() throws Exception {

        final String url = "wss://www.hbg.com/-/s/pro/ws";
//        final String url = "wss://api.hadax.com/ws";
        final WebSocketClient client = new WebSocketClient(url);
        client.open();

        // ## 发送string
        JSONObject sub = new JSONObject();
        sub.put("sub", "market.btcusdt.kline.1min");
        sub.put("id", "id1000");
        client.sendText(sub.toString());
        //订阅其他信息
        JSONObject sub1 = new JSONObject();
        sub1.put("sub", "market.ltcbtc.kline.1min");
        sub1.put("id", "id100");
        client.sendText(sub1.toString());

        // ## 阻塞住线程
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }
}
