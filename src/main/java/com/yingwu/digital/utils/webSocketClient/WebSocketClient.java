package com.yingwu.digital.utils.webSocketClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;

public class WebSocketClient {

    private final URI uri;
    private Channel ch;
    private static final EventLoopGroup group = new NioEventLoopGroup();
    private ChannelFuture channelFuture;

    public WebSocketClient(final String uri) {
        this.uri = URI.create(uri);
    }

    public void open() throws Exception {
        Bootstrap b = new Bootstrap();
        String protocol = uri.getScheme();
        if (!"wss".equals(protocol)) {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }

        final WebSocketClientHandler handler = new WebSocketClientHandler();
        handler.setHandShaker(uri);

        MychannelInitializer myChannelInitializer = new MychannelInitializer(handler);
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(myChannelInitializer);
        channelFuture = b.connect(uri.getHost(),443).sync();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接：{}成功" + uri.toString());
                } else {
                    System.out.println("连接：{}失败" + uri.toString());
                }
            }
        });
        ch = channelFuture.channel();
        handler.handshakeFuture().sync();
    }

    /**
     * 发送文本
     *
     * @param text
     */
    public void sendText(final String text){
        ch.writeAndFlush(new TextWebSocketFrame(text));
    }

    /**
     * 发送二进制数据
     *
     * @param bytes
     */
    public void sendBytes(byte[] bytes) {
        ByteBuf buf = Unpooled.buffer(bytes.length);
        buf.writeBytes(bytes);
        ch.writeAndFlush(new BinaryWebSocketFrame(buf));
    }
}