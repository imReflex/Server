package org.endeavor.engine.network;

/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.endeavor.engine.LoginThread;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.player.PlayerDetails;
import org.endeavor.game.entity.player.net.Client;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 
 * @author Stuart Murphy
 * 
 */
public class ChannelHandler extends SimpleChannelHandler {

	private Client client = null;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		// e.getCause().printStackTrace();
		ctx.getChannel().close();
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		try {
			if (!e.getChannel().isConnected()) {
				return;
			} else if (e.getMessage() instanceof Client) {
				client = (Client) e.getMessage();

				if (!ClientMap.allow(client)) {
					StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
					resp.writeByte(Misc.LOGIN_RESPONSE_LOGIN_LIMIT_EXCEEDED);
					resp.writeByte(0); 
					resp.writeByte(0);
					client.send(resp.getBuffer());
					ctx.getChannel().close();
				} else {
					final PlayerDetails p = client.getDetails();
					
					LoginThread.queueLogin(p);
				}

			} else if (e.getMessage() instanceof ReceivedPacket) {
				client.queueIncomingPacket((ReceivedPacket) e.getMessage());
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		if (client != null) {
			/*client.getPlayer().logout(true);*/
			client.disconnect();
			client = null;
		}
	}
}
