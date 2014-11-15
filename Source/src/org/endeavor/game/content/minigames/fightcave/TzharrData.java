package org.endeavor.game.content.minigames.fightcave;

public enum TzharrData {
	WAVE_NULL(new short[0]), WAVE_0(new short[] { 2627 }), WAVE_1(new short[] { 2627, 2627 }), WAVE_2(
			new short[] { 2630 }), WAVE_3(new short[] { 2630, 2627 }), WAVE_4(new short[] { 2630, 2627, 2627 }), WAVE_5(
			new short[] { 2630, 2630 }), WAVE_6(new short[] { 2631 }), WAVE_7(new short[] { 2631, 2627 }), WAVE_8(
			new short[] { 2631, 2627, 2627 }), WAVE_9(new short[] { 2631, 2630 }), WAVE_10(new short[] { 2631, 2630,
			2627 }), WAVE_11(new short[] { 2631, 2630, 2627, 2627 }), WAVE_12(new short[] { 2631, 2630, 2630 }), WAVE_13(
			new short[] { 2631, 2631 }), WAVE_14(new short[] { 2741 }), WAVE_15(new short[] { 2741, 2627 }), WAVE_16(
			new short[] { 2741, 2627, 2627 }), WAVE_17(new short[] { 2741, 2630 }), WAVE_18(new short[] { 2741, 2630,
			2627 }), WAVE_19(new short[] { 2741, 2630, 2627, 2627 }), WAVE_20(new short[] { 2741, 2630, 2630 }), WAVE_21(
			new short[] { 2741, 2631 }), WAVE_22(new short[] { 2741, 2631, 2627 }), WAVE_23(new short[] { 2741, 2631,
			2627, 2627 }), WAVE_24(new short[] { 2741, 2631, 2630 }), WAVE_25(new short[] { 2741, 2631, 2630, 2627 }), WAVE_26(
			new short[] { 2741, 2631, 2630, 2627, 2627 }), WAVE_27(new short[] { 2741, 2631, 2630, 2630 }), WAVE_28(
			new short[] { 2741, 2631, 2631 }), WAVE_29(new short[] { 2741, 2741 }), WAVE_30(new short[] { 2743 }), WAVE_31(
			new short[] { 2743, 2627 }), WAVE_32(new short[] { 2743, 2627, 2627 }), WAVE_33(new short[] { 2743, 2630 }), WAVE_34(
			new short[] { 2743, 2630, 2627 }), WAVE_35(new short[] { 2743, 2630, 2627, 2627 }), WAVE_36(new short[] {
			2743, 2630, 2630 }), WAVE_37(new short[] { 2743, 2631 }), WAVE_38(new short[] { 2743, 2631, 2627 }), WAVE_39(
			new short[] { 2743, 2631, 2627, 2627 }), WAVE_40(new short[] { 2743, 2631, 2630 }), WAVE_41(new short[] {
			2743, 2631, 2630, 2627 }), WAVE_42(new short[] { 2743, 2631, 2630, 2627, 2627 }), WAVE_43(new short[] {
			2743, 2631, 2630, 2630 }),
			
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
			WAVE_62(new short[] { 2745 });

	private short[] npcs;

	private TzharrData(short[] npcs) {
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
	}
}
