package org.endeavor.engine.network.jaggrab;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

/**
 * 
 * @author Joshua Barry
 * 
 */
public class Jaggrab {

	private static Cache cache;
	private static byte[] crcTable;
	private static ServerBootstrap bootstrap;

	public static final CRC32 crc = new CRC32();

	public static void initialise() throws IOException {
		cache = new Cache(JaggrabConstants.CACHE_PATH);
		crcTable = generateTable();
		bootstrap = new ServerBootstrap();
		bootstrap.setFactory(new NioServerSocketChannelFactory());
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			@Override
			public ChannelPipeline getPipeline() throws Exception {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("handler", new JaggrabHandler());
				return pipeline;
			}
		});
		InetSocketAddress address = new InetSocketAddress(JaggrabConstants.PORT);
		bootstrap.bind(address);
		System.out.println("JAGGRAB bound to address [addr=" + address + "]");
	}

	public static byte[] generateTable() throws IOException {
		int[] checksums = new int[9];

		checksums[0] = 317;

		for (int i = 1; i < checksums.length; i++) {
			byte[] data = cache.get(0, i);
			crc.reset();
			crc.update(data);
			checksums[i] = (int) crc.getValue();
		}

		int hash = 1234;

		for (int i = 0; i < 9; i++) {
			hash = (hash << 1) + checksums[i];
		}

		ByteBuffer buffer = ByteBuffer.allocate((checksums.length + 1) * 4);
		for (int i : checksums) {
			buffer.putInt(i);
		}
		buffer.putInt(hash);
		buffer.flip();

		return buffer.array();
	}

	public static byte[] getCrcTable() {
		return crcTable;
	}

	public static Cache getCache() {
		return cache;
	}
}
