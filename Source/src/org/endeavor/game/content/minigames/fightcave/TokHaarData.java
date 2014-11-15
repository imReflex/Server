package org.endeavor.game.content.minigames.fightcave;

public enum TokHaarData {
	WAVE_NULL(new short[0]), 
	WAVE_44(new short[] { 2743, 2631, 2631 }), 
	
	WAVE_45(new short[] { 2743, 2741 }), 
	
	WAVE_46(
	new short[] { 2743, 2741, 2627 }), 
	
	WAVE_47(new short[] { 2743, 2741, 2627, 2627 }), 
	
	WAVE_48(new short[] {
	2743, 2741, 2630 }), 
	
	WAVE_49(new short[] { 2743, 2741, 2630, 2627 }), 
	
	WAVE_50(new short[] { 2743, 2741,
	2630, 2627, 2627 }), 
	
	WAVE_51(new short[] { 2743, 2741, 2630, 2630 }), 
	
	WAVE_52(new short[] { 2743, 2741,
	2631 }), 
	
	WAVE_53(new short[] { 2743, 2741, 2631, 2627 }), 
	WAVE_54(new short[] { 2743, 2741, 2631, 2627,
	2627 }), 
	WAVE_55(new short[] { 2743, 2741, 2631, 2630 }), 
	WAVE_56(new short[] { 2743, 2741, 2631, 2630,
	2627 }), 
	WAVE_57(new short[] { 2743, 2741, 2631, 2630, 2627, 2627 }), 
	WAVE_58(new short[] { 2743, 2741,
	2631, 2630, 2630 }), 
	WAVE_59(new short[] { 2743, 2741, 2631, 2631 }), 
	WAVE_60(new short[] { 2743, 2741,
	2741 }), 
	WAVE_61(new short[] { 2743, 2743 }), 
	WAVE_62(new short[] { 2745 }),
	
	WAVE_63(new short[] { NPCS_DETAILS.AVATAR_OF_DESTRUCTION }),
	//end
	;
	
	private short[] npcs;

	private TokHaarData(short[] npcs) {
		this.npcs = npcs;
	}

	public short[] getNpcs() {
		return npcs;
	}

	public int toInteger() {
		return ordinal();
	}

	public static final class NPCS_DETAILS {
		public static final short TZ_KIH = 2627;
		public static final short TZ_KEK_SPAWN = 2738;
		public static final short TZ_KEK = 2630;
		public static final short TOK_XIL = 2631;
		public static final short YT_MEJKOT = 2741;
		public static final short KET_ZEK = 2743;
		public static final short TZTOK_JAD = 2745;
		public static final short AVATAR_OF_DESTRUCTION = 8596;
	}
}
