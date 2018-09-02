package old;


/**
 * WebSocket API使用事例
 * 
 * @author okcoin
 * 
 */
public class Example {
	public static void main(String[] args) {

		// apiKey 为用户申请的apiKey
		String apiKey = "afddfeab-6110fb3b-d6961506-2e487";

		// secretKey为用户申请的secretKey
		String secretKey = "b0906f7a-62c0b211-b80abbaa-9dfea";

		// 国际站WebSocket地址 注意如果访问国内站 请将 real.okcoin.com 改为 real.okcoin.cn
        //Pro 站行情请求地址为：wss://api.huobi.pro/ws
        //HADAX 站行情请求地址为：wss://api.hadax.com/ws
		String url = "wss://api.huobi.pro/ws";

		// 订阅消息处理类,用于处理WebSocket服务返回的消息
		WebSocketService service = new BuissnesWebSocketServiceImpl();

		// WebSocket客户端
		WebSoketClient client = new WebSoketClient(url, service);

		// 启动客户端
		client.start();

		// 添加订阅
		client.addChannel("market.btcusdt.kline.1min");

		// 删除定订阅
		// client.removeChannel("ok_sub_spotusd_btc_ticker");

		// 合约下单交易
		// client.futureTrade(apiKey, secretKey, "btc_usd", "this_week", 2.3, 2,
		// 1, 0, 10);

		// 实时交易数据 apiKey
		// client.futureRealtrades(apiKey, secretKey);

		// 取消合约交易
		// client.cancelFutureOrder(apiKey, secretKey, "btc_usd", 123456L,
		// "this_week");

		// 现货下单交易
		// client.spotTrade(apiKey, secretKey, "btc_usd", 3.2, 2.3, "buy");

		// 现货交易数据
		// client.realTrades(apiKey, secretKey);

		// 现货取消订单
		// client.cancelOrder(apiKey, secretKey, "btc_usd", 123L);

		// 获取用户信息
		// client.getUserInfo(apiKey,secretKey);
	}
}
