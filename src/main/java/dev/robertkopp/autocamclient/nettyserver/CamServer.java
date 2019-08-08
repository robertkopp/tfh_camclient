/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.nettyserver;

import com.google.inject.Inject;
import com.google.inject.Provider;
import dev.robertkopp.autocamclient.ClientConfig;
import dev.robertkopp.autocamclient.camera.ICameraManager;
import dev.robertkopp.autocamclient.httpsender.HttpPingSender;
import dev.robertkopp.autocamclient.httpsender.IHttpPingSender;
import dev.robertkopp.autocamclient.httpsender.IHttpRegistrationSender;
import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Discards any incoming data.
 */
public class CamServer {

    private int port;
    private final Provider<CamDataHandler> theAdapter;
    private final Provider<MessageDecoder> messageDecoder;
    private final ClientConfig config;
    private final IHttpRegistrationSender regsender;

    private static final Logger logger = Logger.getLogger(CamServer.class.getName());

    @Inject
    public CamServer(ClientConfig config, Provider<CamDataHandler> adapterProvider, Provider<MessageDecoder> decoderProvider, IHttpRegistrationSender regsender) {
        this.port = config.THIS_CLIENTS_PORT;
        this.theAdapter = adapterProvider;
        this.messageDecoder = decoderProvider;
        this.config = config;
        this.regsender = regsender;

        sendRegistration(regsender);
        //Send status when registration is done
        startWatchDog();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(messageDecoder.get(), theAdapter.get());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128) // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private void sendRegistration(IHttpRegistrationSender regsender) {
        try {
            regsender.sendHttpRequest();
            logger.log(Level.INFO, "registration successful.");
        } catch (Exception ex) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex1) {
                logger.log(Level.SEVERE, null, ex1);
            }
            final String errortext = String.format("Server not reachable at %s:%s .. retry", config.SERVER_IP, config.SERVER_PORT);
            logger.log(Level.INFO, errortext);
            sendRegistration(regsender);
        }
    }

    private void startWatchDog() {
        //check if the server is running every 5s, if not register the client again
        final IHttpPingSender sender = new HttpPingSender(config);
        Thread dog = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CamServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        logger.info("Watchdog sending ping");
                        sender.sendHttpRequest();
                        logger.info("ping ok.");
                    } catch (Exception e) {
                        logger.info("ping fail, registering again..");
                        sendRegistration(regsender);
                    }
                }
            }
        });
        logger.log(Level.INFO, "starting watchdog");
        dog.start();
    }
}
