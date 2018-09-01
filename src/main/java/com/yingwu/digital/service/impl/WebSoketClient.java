package com.yingwu.digital.service.impl;


import com.yingwu.digital.service.WebSocketBase;
import com.yingwu.digital.service.WebSocketService;

/**
 * 通过继承WebSocketBase创建WebSocket客户端
 * @author okcoin
 *
 */
public class WebSoketClient extends WebSocketBase {
	public WebSoketClient(String url, WebSocketService service){
		super(url,service);
	}
}
