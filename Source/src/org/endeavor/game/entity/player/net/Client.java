package org.endeavor.game.entity.player.net;

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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.endeavor.engine.network.ReceivedPacket;
import org.endeavor.engine.network.security.ISAACCipher;
import org.endeavor.engine.utility.Misc;
import org.endeavor.engine.utility.Misc.Stopwatch;
import org.endeavor.game.entity.World;
import org.endeavor.game.entity.mob.Mob;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.PlayerDetails;
import org.endeavor.game.entity.player.net.in.PacketHandler;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

/**
 * The class behind a Player that handles all networking-related things.
 * 
 * @author blakeman8192
 */
public class Client implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3933755684470421943L;

	private Channel channel;

	private final List<Mob> mobs = new LinkedList<Mob>();

	/**
	 * Incoming packets
	 */
	private Queue<ReceivedPacket> incomingPackets = new ConcurrentLinkedQueue<ReceivedPacket>();
	/**
	 * Outgoing Packets
	 */
	private Queue<OutgoingPacket> outgoingPackets = new ConcurrentLinkedQueue<OutgoingPacket>();

	private final Misc.Stopwatch timeoutStopwatch = new Misc.Stopwatch();
	private Stages stage = Stages.LOGGING_IN;
	private ISAACCipher encryptor;
	private ISAACCipher decryptor;
	private PlayerDetails details;
	public PacketHandler packetHandler;
	private Player player;
	private String host;
	private long hostId = 0;

	private boolean logPlayer = false;

	private String enteredPassword = null;

	private String lastPlayerOption = "";

	private long lastPacketTime = World.getCycles();

	public void resetLastPacketReceived() {
		lastPacketTime = World.getCycles();
	}

	/**
	 * Creates a new Client.
	 */
	public Client(Channel channel) {
		try {
			this.channel = channel;

			// set host
			if (channel != null) {
				host = channel.getRemoteAddress().toString();
				host = host.substring(1, host.indexOf(":"));

				hostId = Misc.nameToLong(host);
			} else {
				host = "none";
				hostId = -1;
			}
			packetHandler = new PacketHandler(this);
			details = new PlayerDetails("", "", this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Disconnects the client.
	 */
	public void disconnect() {
		if (outgoingPackets != null) {
			synchronized (outgoingPackets) {
				outgoingPackets = null;
			}
		}
	}

	/**
	 * Resets the packet handler
	 */
	public void reset() {
		packetHandler.reset();
	}

	/**
	 * Sends the buffer to the socket.
	 * 
	 * @param buffer
	 *            the buffer
	 */
	public void send(ChannelBuffer buffer) {
		try {
			//if (channel == null) {
				//return;
			//}

			/**
			 * Synchronize to the channel to wait for modifications to complete
			 */
			//synchronized (channel) {
				if (channel == null || !channel.isConnected()) {
					return;
				}

				/**
				 * Synchronize to the outgoing packets just for added protection
				 * against sending packets at the exact same time
				 */
				synchronized (outgoingPackets) {
					channel.write(buffer);
				}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a packet to the queue
	 * 
	 * @param packet
	 */
	public void queueIncomingPacket(ReceivedPacket packet) {
		resetLastPacketReceived();
		
		synchronized (incomingPackets) {
			incomingPackets.offer(packet);
		}
	}

	/**
	 * Handles packets we have received
	 */
	public void processIncomingPackets() {
		ReceivedPacket p = null;

		try {
			if (outgoingPackets == null) {
				return;
			}

			/**
			 * Synchronize to the queue so we don't corrupt data
			 */
			synchronized (incomingPackets) {
				if (outgoingPackets == null) {
					return;
				}

				while ((p = incomingPackets.poll()) != null) {
					packetHandler.handlePacket(p);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

			/**
			 * Safely disconnect this player
			 */
			player.logout(true);
			return;
		}
	}

	/**
	 * Adds a packet to the outgoing queue
	 * 
	 * @param o
	 *            the OutGoingPacket object
	 */
	public void queueOutgoingPacket(OutgoingPacket o) {
		if (outgoingPackets == null) {
			return;
		}

		synchronized (outgoingPackets) {
			if (outgoingPackets == null) {
				return;
			}

			outgoingPackets.offer(o);
		}
	}

	/**
	 * Handles packets we are sending
	 */
	public void processOutgoingPackets() {
		if (channel == null || outgoingPackets == null) {
			return;
		}

		try {
			/**
			 * Synchronize to the channel to wait for modifications to complete
			 */
			synchronized (channel) {
				if (channel == null) {
					return;
				}

				/**
				 * Synchronize to the queue so we won't corrupt data
				 */
				synchronized (outgoingPackets) {
					if (outgoingPackets == null) {
						return;
					}
					/**
					 * Then process all the outgoing packets
					 */
					OutgoingPacket p = null;
					while ((p = outgoingPackets.poll()) != null) {
						// if (p.getOpcode() == -1 || p.getOpcode() == 73 ||
						// p.getOpcode() == 81) {
						p.execute(this);
						// }
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the outgoing packets.
	 */
	public Queue<OutgoingPacket> getOutgoingPackets() {
		return outgoingPackets;
	}

	/**
	 * Gets the remote host of the client.
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	public long getHostId() {
		return hostId;
	}

	/**
	 * Sets the encryptor.
	 * 
	 * @param encryptor
	 *            the encryptor
	 */
	public void setEncryptor(ISAACCipher encryptor) {
		this.encryptor = encryptor;
	}

	/**
	 * Gets the encryptor.
	 * 
	 * @return the encryptor
	 */
	public synchronized ISAACCipher getEncryptor() {
		return encryptor;
	}

	/**
	 * Sets the decryptor.
	 * 
	 * @param decryptor
	 *            the decryptor.
	 */
	public void setDecryptor(ISAACCipher decryptor) {
		this.decryptor = decryptor;
	}

	/**
	 * Gets the decryptor.
	 * 
	 * @return the decryptor
	 */
	public synchronized ISAACCipher getDecryptor() {
		return decryptor;
	}

	/**
	 * Gets the Player subclass implementation of this superclass.
	 * 
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public PacketHandler getPacketHandler() {
		return this.packetHandler;
	}
	
	public PlayerDetails getDetails() {
		return details;
	}

	public void setStage(Stages stage) {
		this.stage = stage;
	}

	public Stages getStage() {
		return stage;
	}

	public Stopwatch getTimeoutStopwatch() {
		return timeoutStopwatch;
	}

	public String getEnteredPassword() {
		return enteredPassword;
	}

	public void setEnteredPassword(String enteredPassword) {
		this.enteredPassword = enteredPassword;
	}

	public String getLastPlayerOption() {
		return lastPlayerOption;
	}

	public void setLastPlayerOption(String lastPlayerOption) {
		this.lastPlayerOption = lastPlayerOption;
	}

	public List<Mob> getNpcs() {
		return mobs;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isLogPlayer() {
		return logPlayer;
	}

	public void setLogPlayer(boolean logPlayer) {
		this.logPlayer = logPlayer;
	}

	public long getLastPacketTime() {
		return lastPacketTime;
	}

	public enum Stages {
		CONNECTED, LOGGING_IN, LOGGED_IN, LOGGED_OUT
	}
}
