/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.robertkopp.autocamclient.nettyserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder { // (1)

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
        if (in.readableBytes() < 1) {
            return; // (3)
        }
        byte cmd = in.readByte();
        if (cmd == Commands.SendConfig || cmd== Commands.TakeShot || cmd==Commands.TakeSingleShot) {
            if (in.readableBytes()<5)
                return;
            int payload_length= in.readInt();
            if (in.readableBytes()<payload_length)
                return;
            byte[] stringbytes= new byte[payload_length];
             in.readBytes(stringbytes);
             String payload= new String(stringbytes);
             Message result= new Message(cmd, payload);
             out.add(result);
        }
        else{
            out.add(new Message(cmd, null));
        }
    }
}
