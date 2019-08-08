/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.nettyserver;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handles a server-side channel.
 */
public class CamDataHandler extends ChannelInboundHandlerAdapter { 
    private final ICommandDispatcher cmdDispatch;
// (1)

    
    @Inject
    public CamDataHandler(ICommandDispatcher cmdDispatch){
        this.cmdDispatch = cmdDispatch;
    
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Message m = (Message) msg;
        cmdDispatch.dispatch(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
