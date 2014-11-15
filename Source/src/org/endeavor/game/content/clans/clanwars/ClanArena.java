package org.endeavor.game.content.clans.clanwars;

/**
 * A ClanArena!
 * @author Allen K.
 *
 */
public enum ClanArena {

	/**
	 * South is requester, North is accepter -- always!
	 */
	
	CLASSIC("Classic", 3299, 3726, 3292, 3826, 3320, 3770, 3320, 3780, 28174, 10368),
	PLATEAU("Plateau", 2884, 5908, 2884, 5932, 2851, 5909, 2851, 5931, 38687, 10368),
	FORSAKEN_QUARRY("Forsaken Querry", 2891, 5518, 2931, 5554, 2908, 5536, 2916, 5536, 38685, 10369),
	BLASTED_FORREST("Blasted Forrest", 2890, 5651, 2933, 5678, 2893, 5661, 2929, 5667, 38689, 10370),
	TURRETS("Turrets", 2731, 5517, 2710, 5617, 2722, 5506, 2716, 5629, 38691, 10371);
	
	private String name;
	private int requesterSpawnX, accepterSpawnX;
	private int requesterSpawnY, accepterSpawnY;
	private int requesterViewingX, accepterViewingX;
	private int requesterViewingY, accepterViewingY;
	private int wallID;
	private int animationID;
	
	/**
	 * Lots of stuff right? 0_o
	 * @param name
	 * @param requesterSpawnX
	 * @param requesterSpawnY
	 * @param accepterSpawnX
	 * @param accepterSpawnY
	 * @param requesterViewingX
	 * @param requesterViewingY
	 * @param accepterViewingX
	 * @param accepterViewingY
	 */
	ClanArena(String name, int requesterSpawnX, int requesterSpawnY, int accepterSpawnX, int accepterSpawnY,
			int requesterViewingX, int requesterViewingY, int accepterViewingX, int accepterViewingY, int wallID, int animationID) {
		this.name = name;
		this.requesterSpawnX = requesterSpawnX;
		this.requesterSpawnY = requesterSpawnY;
		this.accepterSpawnX = accepterSpawnX;
		this.accepterSpawnY = accepterSpawnY;
		this.requesterViewingX = requesterViewingX;
		this.requesterViewingY = requesterViewingY;
		this.accepterSpawnX = accepterViewingX;
		this.accepterSpawnY = accepterSpawnY;
		this.wallID = wallID;
		this.animationID = animationID;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getRequesterSpawnX() {
		return this.requesterSpawnX;
	}
	
	public int getRequesterSpawnY() {
		return this.requesterSpawnY;
	}
	
	public int getAccepterSpawnX() {
		return this.accepterSpawnX;
	}
	
	public int getAccepterSpawnY() {
		return this.accepterSpawnY;
	}
	
	public int getRequesterViewingX() {
		return this.requesterViewingX;
	}
	
	public int getRequesterViewingY() {
		return this.requesterViewingY;
	}
	
	public int getAccepterViewingX() {
		return this.accepterViewingX;
	}
	
	public int getAccepterViewingY() {
		return this.accepterViewingY;
	}
	
	public int getWallID() {
		return wallID;
	}
	
	public int getAnimationID() {
		return animationID;
	}
	
}
