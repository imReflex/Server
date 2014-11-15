package org.endeavor.game.entity.player.net.out.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.game.entity.player.Palette;
import org.endeavor.game.entity.player.Palette.PaletteTile;
import org.endeavor.game.entity.player.net.Client;
import org.endeavor.game.entity.player.net.out.OutgoingPacket;

/**
 * Created with Eclipse. Date: 19/08/2013 Time: 20:34:44
 * 
 * @author Sebastian <juan.2114@hotmail.com>
 * @see java.lang.Object
 */
public final class ConstructMap extends OutgoingPacket {

	private Palette palette;

	public ConstructMap(final Palette palette) {
		this.palette = palette;
	}

	public Palette getPalette() {
		return palette;
	}

	@Override
	public void execute(Client client) {

		 StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(20);
		 out.writeHeader(client.getEncryptor(), 241);
		 out.writeShort(client.getPlayer().getLocation().getRegionY() + 6);
		 //out.startBitAccess(); 
		 for (int z = 0; z < 4; z++) { 
			 for (int x = 0; x < 13; x++) { 
				 for (int y = 0; y < 13; y++) { 
					 PaletteTile tile = palette.getTile(x, y, z); 
					 out.writeBits(1, tile != null ? 1 : 0); 
					 if(tile != null) { 
						 out.writeBits(26, tile.getX() << 14 | tile.getY() << 3 | tile.getZ() << 24 | tile.getRotation() << 1); 
					 } 
				 } 
			 } 
		 }
		 //bldr.finishBitAccess();
		 out.writeShort(client.getPlayer().getLocation().getRegionX() + 6);
		 client.send(out.getBuffer());

	}

	@Override
	public int getOpcode() {
		return 241;
	}
}
