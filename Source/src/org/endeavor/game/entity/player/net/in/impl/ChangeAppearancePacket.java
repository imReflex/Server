package org.endeavor.game.entity.player.net.in.impl;

import org.endeavor.engine.network.StreamBuffer;
import org.endeavor.engine.utility.Misc;
import org.endeavor.game.entity.player.Player;
import org.endeavor.game.entity.player.net.in.IncomingPacket;
import org.endeavor.game.entity.player.net.out.impl.SendMessage;

public class ChangeAppearancePacket extends IncomingPacket {
	private static final int[][] MALE_VALUES = { { 0, 8 }, // head
		{ 10, 17 }, // jaw
		{ 18, 25 }, // torso
		{ 26, 31 }, // arms
		{ 33, 34 }, // hands
		{ 36, 40 }, // legs
		{ 42, 43 }, // feet
	};
	
	private static final int[][] FEMALE_VALUES = { { 45, 54 }, // head
			{ -1, -1 }, // jaw
			{ 56, 60 }, // torso
			{ 61, 65 }, // arms
			{ 67, 68 }, // hands
			{ 70, 77 }, // legs
			{ 79, 80 }, // feet
	};
	
	private static final int[][] IDK_COLORS = {
		{
			6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 
			2983, 54193, 100
		}, {//hair^
			8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153, 56621,
			4783, 1341, 16578, 35003, 100, 4627, 11146, 6439, 4758, 10270, 25239
		}, {//shirt^
			25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 
			10153, 56621, 4783, 1341, 16578, 35003, 100, 4627, 11146, 6439, 4758, 10270
		}, {//legs^
			4626, 25239, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 
			10153, 56621, 4783, 1341, 16578, 35003, 100, 11146, 6439, 4758, 10270
		}, {//feet^
			4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574
		}//skin^
	};
	
	private static final int[][] ALLOWED_COLORS = { 
			{ 0, IDK_COLORS[0].length }, // hair color
			{ 0, IDK_COLORS[1].length }, // torso color
			{ 0, IDK_COLORS[2].length }, // legs color
			{ 0, IDK_COLORS[3].length }, // feet color
			{ 0, IDK_COLORS[4].length } // skin color
	};
	
	public static boolean validate(Player p) {
		int[] a = p.getAppearance();
		
		return validate(new int[] {a[0], a[6], a[1], a[2], a[3], a[4], a[5]}, 
				p.getColors(), p.getGender());
	}
	
	public static boolean validate(int[] app, byte[] col, byte gender) {
		if (gender == 0) {
			for (int i = 0; i < app.length; i++)
				if ((app[i] < MALE_VALUES[i][0]) || (app[i] > MALE_VALUES[i][1]))
					return false;
		} else if (gender == 1) {
			for (int i = 0; i < app.length; i++)
				if ((app[i] < FEMALE_VALUES[i][0]) || (app[i] > FEMALE_VALUES[i][1]))
					return false;
		} else {
			return false;
		}

		for (int i = 0; i < col.length; i++) {
			if ((col[i] < ALLOWED_COLORS[i][0]) || (col[i] > ALLOWED_COLORS[i][1]))
				return false;
		}
		return true;
	}

	public static void setToDefault(Player p) {
		p.setGender((byte) 0);
		p.getAppearance()[0] = 5;
		p.getAppearance()[1] = MALE_VALUES[2][0];
		p.getAppearance()[2] = MALE_VALUES[3][0];
		p.getAppearance()[3] = MALE_VALUES[4][0];
		p.getAppearance()[4] = MALE_VALUES[5][0];
		p.getAppearance()[5] = MALE_VALUES[6][0];
		p.getAppearance()[6] = MALE_VALUES[1][0];
	}

	@Override
	public void handle(Player player, StreamBuffer.InBuffer in, int opcode, int length) {
		byte gender = (byte) in.readByte();
		int head = in.readShort();
		int jaw = in.readShort();
		int torso = in.readShort();
		int arms = in.readShort();
		int hands = in.readShort();
		int legs = in.readShort();
		int feet = in.readShort();

		System.out.println("Torso: " + torso);
		
		byte[] col = { (byte) in.readByte(), (byte) in.readByte(), (byte) in.readByte(), (byte) in.readByte(),
				(byte) in.readByte() };
		
		int[] app = new int[player.getAppearance().length];

		app[0] = head;
		app[1] = torso;
		app[2] = arms;
		app[3] = hands;
		app[4] = legs;
		app[5] = feet;
		app[6] = jaw;
		
		/*if (!validate(new int[] {head, jaw, torso, arms, hands, legs, feet}, 
				col, gender)) {
			player.send(new SendMessage("Your appearance could not be validated."));
			return;
		}*/
		
		player.setGender(gender);
		player.setAppearance(app);
		player.setColors(col);

		player.setAppearanceUpdateRequired(true);
	}

	@Override
	public int getMaxDuplicates() {
		return 1;
	}
}
