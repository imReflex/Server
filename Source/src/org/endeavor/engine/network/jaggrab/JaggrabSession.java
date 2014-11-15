package org.endeavor.engine.network.jaggrab;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author Joshua Barry
 * 
 */
public class JaggrabSession {

	private Channel channel;
	private JaggrabRequest request;

	public JaggrabSession(ChannelHandlerContext ctx) {
		this.channel = ctx.getChannel();
	}

	public void parse(String string) throws IOException {
		if (string.startsWith(JaggrabConstants.PREFIX)) {
			this.request = new JaggrabRequest(string.substring(JaggrabConstants.PREFIX.length()).trim());
		} else {
			channel.close();
		}

		if (request == null || request.getIndex() == 0) {
			channel.close();
			return;
		}

		Cache cache = Jaggrab.getCache();
		if (cache == null) {
			channel.close();
			return;
		}

		byte[] response = null;
		if (request.getIndex() == -2) {
			response = Jaggrab.getCrcTable();
		} else {
			response = cache.get(0, request.getIndex());
		}

		if (response == null || response.length == 0) {
			channel.close();
			return;
		}

		channel.write(ChannelBuffers.wrappedBuffer(response)).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				channel.close();
			}
		});
	}
}
