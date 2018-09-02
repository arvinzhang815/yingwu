package old;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订阅信息处理类需要实现WebSocketService接口
 * @author okcoin
 *
 */
public class BuissnesWebSocketServiceImpl implements WebSocketService {
    private static Logger log = LoggerFactory.getLogger(BuissnesWebSocketServiceImpl.class);

	@Override
	public void onReceive(String msg){
		
		log.info("WebSocket Client received message: " + msg);
	
	}
}
