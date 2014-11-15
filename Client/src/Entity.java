

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public class Entity extends Animable {

	public final void setPos(int i, int j, boolean flag) {
		if (anim != -1 && Animation.anims[anim].priority == 1)
			anim = -1;
		if (!flag) {
			int k = i - pathX[0];
			int l = j - pathY[0];
			if (k >= -8 && k <= 8 && l >= -8 && l <= 8) {
				if (pathLength < 9)
					pathLength++;
				for (int i1 = pathLength; i1 > 0; i1--) {
					pathX[i1] = pathX[i1 - 1];
					pathY[i1] = pathY[i1 - 1];
					aBooleanArray1553[i1] = aBooleanArray1553[i1 - 1];
				}

				pathX[0] = i;
				pathY[0] = j;
				aBooleanArray1553[0] = false;
				return;
			}
		}
		pathLength = 0;
		anInt1542 = 0;
		anInt1503 = 0;
		pathX[0] = i;
		pathY[0] = j;
		x = pathX[0] * 128 + boundDim * 64;
		y = pathY[0] * 128 + boundDim * 64;
	}

	public final void resetWalk() {
		pathLength = 0;
		anInt1542 = 0;
	}

	public int[] hitmarkMove = new int[4];
	public int[] moveTimer = new int[4];
	public int[] hitmarkTrans = new int[4];
	public int[] hitIcon = new int[4];
	public int[] soakDamage = new int[4];
	public int[] hitMarkPos = new int[4];

	public final void updateHitData(int markType, int damage, int l, int icon, int soak) {
		for (int i1 = 0; i1 < 4; i1++)
			if (hitsLoopCycle[i1] <= l) {
				hitIcon[i1] = icon;
				hitmarkMove[i1] = 5;
				moveTimer[i1] = 2;
				hitmarkTrans[i1] = 255;
				soakDamage[i1] = soak;
				hitArray[i1] = damage;
				hitMarkTypes[i1] = markType;
				hitsLoopCycle[i1] = l + 70;
				hitMarkPos[i1] = 0;
				return;
			}
	}

	public final void moveInDir(boolean flag, int dir) {
		int j = pathX[0];
		int k = pathY[0];
		if (dir == 0) {
			j--;
			k++;
		}
		if (dir == 1)
			k++;
		if (dir == 2) {
			j++;
			k++;
		}
		if (dir == 3)
			j--;
		if (dir == 4)
			j++;
		if (dir == 5) {
			j--;
			k--;
		}
		if (dir == 6)
			k--;
		if (dir == 7) {
			j++;
			k--;
		}
		if (anim != -1 && Animation.anims[anim].priority == 1)
			anim = -1;
		if (pathLength < 9)
			pathLength++;
		for (int l = pathLength; l > 0; l--) {
			pathX[l] = pathX[l - 1];
			pathY[l] = pathY[l - 1];
			aBooleanArray1553[l] = aBooleanArray1553[l - 1];
		}
		pathX[0] = j;
		pathY[0] = k;
		aBooleanArray1553[0] = flag;
	}

	public int entScreenX;
	public int entScreenY;
	public final int index = -1;

	public boolean isVisible() {
		return false;
	}

	Entity() {
		pathX = new int[10];
		pathY = new int[10];
		interactingEntity = -1;
		anInt1504 = 32;
		runAnimation = -1;
		height = 200;
		standAnimIndex = -1;
		nextRenderAnim = -1;
		hitArray = new int[4];
		hitMarkTypes = new int[4];
		hitsLoopCycle = new int[4];
		forcedAnimId = -1;
		gfxId = -1;
		anim = -1;
		loopCycleStatus = -1000;
		textCycle = 100;
		boundDim = 1;
		aBoolean1541 = false;
		aBooleanArray1553 = new boolean[10];
		walkAnimIndex = -1;
		turn180AnimIndex = -1;
		turn90CWAnimIndex = -1;
		turn90CCWAnimIndex = -1;
	}

	public final int[] pathX;
	public final int[] pathY;
	public int interactingEntity;
	int anInt1503;
	int anInt1504;
	int runAnimation;
	public String textSpoken;
	public int height;
	public int turnDirection;
	int standAnimIndex;
	int nextRenderAnim;
	int chatColor;
	final int[] hitArray;
	final int[] hitMarkTypes;
	final int[] hitsLoopCycle;
	int forcedAnimId;//17
	int currentForcedAnimFrame;//18
	int forcedAnimFrameCycle;
	int gfxId;//20
	int currentAnim;//21
	int animCycle;//22
	int graphicDelay;//23
	int graphicHeight;//24
	int pathLength;//25
	public int anim;//26
	int currentAnimFrame;//27
	int frameCycle;
	int animationDelay;//29
	int anInt1530;
	int chatEffect;
	public int loopCycleStatus;
	public int currentHealth;
	public int maxHealth;
	int textCycle;
	int loopCycle;
	int anInt1538;
	int anInt1539;
	int boundDim;
	boolean aBoolean1541;
	int anInt1542;
	int anInt1543;
	int anInt1544;
	int anInt1545;
	int anInt1546;
	int anInt1547;
	int anInt1548;
	int turnInfo;
	public int x;
	public int y;
	int anInt1552;
	final boolean[] aBooleanArray1553;
	int walkAnimIndex;
	int turn180AnimIndex;
	int turn90CWAnimIndex;
	int turn90CCWAnimIndex;
}
