package org.endeavor.game.content.skill.crafting;

public enum BoltTipData {
	RUBY(0, 0, 0, 0.0D);

	public final int gem;
	public final int tips;
	public final int level;
	public final double exp;

	private BoltTipData(int gem, int tips, int level, double exp) {
		this.gem = gem;
		this.tips = tips;
		this.level = level;
		this.exp = exp;
	}
}
