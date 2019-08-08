/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unit.netty;

import dev.robertkopp.autocamclient.nettyserver.Message;
import dev.robertkopp.autocamclient.nettyserver.MessageEncoder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.bootstrap.Bootstrap;

/**
 *
 * @author robert kopp
 */

public class NettyClient {

    public static void send(Message m) throws Exception {
        String host = "127.0.0.1";
        int port = 9999;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast("encoder", new MessageEncoder());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)
            if (m != null) {
                f.awaitUninterruptibly().channel().writeAndFlush(m);
            }

            // Wait until the connection is closed.
            f.channel().close();
            Thread.sleep(1000);

        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
