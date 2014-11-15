package org.endeavor.engine.network.jaggrab;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 
 * @author Joshua Barry
 * 
 */
public class JaggrabHandler extends SimpleChannelHandler implements ChannelHandler {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		StringBuilder builder = new StringBuilder();

		while (buffer.readable()) {
			builder.append((char) (buffer.readByte() & 0xFF));
		}

		((JaggrabSession) ctx.getAttachment()).parse(builder.toString());
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		ctx.setAttachment(new JaggrabSession(ctx));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getCause().printStackTrace();
	}
}
