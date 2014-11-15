public class SkillOrbHandler {
 
    public static boolean DRAW_SKILL_ORBS = true;
    private static int ORB_DISPLAY_TIME = 384;
    private static int LEVEL_UP_DISPLAY_TIME = 340;
    private static int drawingLevelUp = -1;
    private static int[] savedExp = new int[24];
    private static SkillOrbHandler[] orbs = new SkillOrbHandler[24];
    public static boolean[] updated = new boolean[24];
    private static Sprite[] levelUpSkillIcons = new Sprite[24];
    private static Sprite[] skillIcons = new Sprite[24];
    private static Sprite background;
    private static Sprite fill;
    private static Sprite levelUp;
    private static Sprite[] number = new Sprite[10];
 
    public static void setSavedXP(int[] currentExp) {
    	savedExp = currentExp;
    }
    
    private static void checkForUpdates() {
        for (int i = 0; i < orbs.length; i++) {
            if (savedExp[i] != Client.instance.currentExp[i]) {
                if (orbs[i] != null) {
                    if (getLevelForExp(Client.instance.currentExp[i]) > getLevelForExp(savedExp[i])) {
                        orbs[i].levelUp(getLevelForExp(Client.instance.currentExp[i]));
                    }
                }
                savedExp[i] = Client.instance.currentExp[i];
                updated[i] = DRAW_SKILL_ORBS;
            }
            if (orbs[i] != null) {
                if ((int) getProgress(i) > (int) orbs[i].progress) {
                    orbs[i].progress++;
                } else {
                    orbs[i].progress = getProgress(i);
                }
            }
        }
    }
 
    private static void drawLevelUpOrb(int skill) {
        if (drawingLevelUp != -1 && drawingLevelUp != skill) {
            if (orbs[skill].levelUpTo > 0) {
                orbs[skill].levelUpCycle = Client.loopCycle;
            }
            return;
        }
        if (orbs[skill].levelUpTo > 0) {
            drawingLevelUp = skill;
            final int xPosition = /*DrawingArea.viewport_centerY - 30*/(DrawingArea.bottomX - DrawingArea.topX) / 2 - 27;
            int alpha = 256;
            if (Client.loopCycle - orbs[skill].levelUpCycle < 32) {
                alpha = 8 * (Client.loopCycle - orbs[skill].levelUpCycle);
            }
            if (Client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME - 32) {
                alpha = 256 - 8 * (Client.loopCycle - (orbs[skill].levelUpCycle + LEVEL_UP_DISPLAY_TIME - 32));
            }
            SpriteCache.get(697).drawSprite3(xPosition - 44, -2, alpha);
            int alpha2 = alpha;
            if (alpha2 == 256 && Client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2) {
                alpha2 = 8 * (Client.loopCycle - orbs[skill].levelUpCycle - LEVEL_UP_DISPLAY_TIME / 2);
            }
            if (alpha2 > 256) {
                alpha2 = 256;
            }
            SpriteCache.get(skill + 698).drawSprite3(xPosition + 30 - SpriteCache.get(skill + 698).myWidth / 2,
                    63 - SpriteCache.get(skill + 698).myHeight / 2,
                    Client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2 ? alpha - alpha2 / 3 * 2 : alpha);
            if (Client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME / 2) {
                if (orbs[skill].levelUpTo < 10) {
                    SpriteCache.get(orbs[skill].levelUpTo + 721).drawSprite3(xPosition + 30 - SpriteCache.get(orbs[skill].levelUpTo + 721).myWidth / 2,
                            63 - SpriteCache.get(orbs[skill].levelUpTo + 721).myHeight / 2, alpha2);
                } else {
                	
                	SpriteCache.get((orbs[skill].levelUpTo % 10) + 721).drawSprite3(xPosition + 26,
                            63 - SpriteCache.get((orbs[skill].levelUpTo % 10) + 721).myHeight / 2, alpha2);
                	SpriteCache.get(((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10) + 721).drawSprite3(xPosition + 31
                            - SpriteCache.get(((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10) + 721).myWidth,
                            63 - SpriteCache.get(((orbs[skill].levelUpTo - orbs[skill].levelUpTo % 10) / 10) + 721).myHeight / 2, alpha2);
                }
            }
            if (Client.loopCycle - orbs[skill].levelUpCycle > LEVEL_UP_DISPLAY_TIME) {
                orbs[skill].levelUpTo = -1;
                drawingLevelUp = -1;
            }
        }
    }
 
    private static void drawOrb(int skill) {
        if (orbs[skill] == null) {
            orbs[skill] = new SkillOrbHandler(skill);
        }
        if (updated[skill]) {
            orbs[skill].lastUpdateCycle = Client.loopCycle;
            updated[skill] = false;
        }
        if (Client.loopCycle - orbs[skill].lastUpdateCycle > ORB_DISPLAY_TIME) {
            orbs[skill] = null;
            return;
        }
        if (orbs[skill].levelUpTo > 0 && orbs[skill].levelUpCycle > 32) {
            for (int i = 0; i < orbs.length; i++) {
                if (orbs[i] != null) {
                    orbs[i].appearCycle = Client.loopCycle;
                    updated[i] = true;
                }
            }
        }
        int alpha = 256;
        if (Client.loopCycle - orbs[skill].appearCycle < 32) {
            alpha = 8 * (Client.loopCycle - orbs[skill].appearCycle);
        }
        if (Client.loopCycle - orbs[skill].lastUpdateCycle > ORB_DISPLAY_TIME - 32) {
            alpha = 256 - 8 * (Client.loopCycle - (orbs[skill].lastUpdateCycle + ORB_DISPLAY_TIME - 32));
        }
        if (orbs[skill].position == -1) {
            orbs[skill].position = getAvailablePosition();
        }
        int xPosition = (DrawingArea.bottomX - DrawingArea.topX) / 2 - 27;
        if (orbs[skill].position > 0) {
            if (orbs[skill].position % 2 != 0) {
                xPosition += orbs[skill].position / 2 * 54;
            } else {
                xPosition -= orbs[skill].position / 2 * 54;
            }
        }
        final int yPosition = -4;
        /*if(background == null) {
        	return;
        }*/
        SpriteCache.get(695).drawSprite3(xPosition, yPosition, alpha);
        DrawingArea.setDrawingArea(60 + yPosition, xPosition + 7, xPosition + 30, (int) (45 - orbs[skill].progress) + 5 + yPosition);
        SpriteCache.get(696).drawSprite3(xPosition + 7, 7 + yPosition, alpha);
        DrawingArea.setDrawingArea((int) (orbs[skill].progress - 38) + yPosition, xPosition + 30, xPosition + 52, 7 + yPosition);
        SpriteCache.get(696).drawSprite3(xPosition + 7, 7 + yPosition, alpha);
        DrawingArea.defaultDrawingAreaSize();
        SpriteCache.get(skill + 731).drawSprite3(xPosition + 30 - SpriteCache.get(skill + 731).myWidth / 2,
                28 - SpriteCache.get(skill + 731).myHeight / 2 + yPosition, alpha);
    }
 
    public static void drawOrbs() {
        checkForUpdates();
        if (!DRAW_SKILL_ORBS) {
            return;
        }
        for (int i = 0; i < orbs.length; i++) {
            if (orbs[i] != null || updated[i]) {
                drawOrb(i);
            }
        }
        for (int i = 0; i < orbs.length; i++) {
            if (orbs[i] != null) {
                drawLevelUpOrb(i);
            }
        }
    }
 
    private static int getAvailablePosition() {
        for (int i = 1; i < orbs.length + 1; i++) {
            boolean used = false;
            for (final SkillOrbHandler orb : orbs) {
                if (orb == null) {
                    continue;
                }
                if (orb.position == i) {
                    used = true;
                }
            }
            if (!used) {
                return i;
            } else {
                continue;
            }
        }
        return 0;
    }
 
    public static int getExpForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= level) {
                return output;
            }
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }
 
    public static int getLevelForExp(int exp) {
        int points = 0;
        int output = 0;
        if (exp > 13034430) {
            return 99;
        }
        for (int lvl = 1; lvl <= 99; lvl++) {
            points += Math.floor(lvl + 300.0
                    * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if (output >= exp) {
                return lvl;
            }
        }
        return 0;
    }
 
    private static float getProgress(int i) {
        final int currentLevel = getLevelForExp(savedExp[i]);
        final float expDifference = getExpForLevel(currentLevel + 1) - getExpForLevel(currentLevel);
        final float expEarnedAlready = savedExp[i] - getExpForLevel(currentLevel);
        return expEarnedAlready / expDifference * 90;
    }
 
    public static void loadImages() {
    	System.out.println("Loading skill orb images...");
        for (int i = 0; i < skillIcons.length; i++) {
            skillIcons[i] = SpriteCache.get(i + 695);
        }
        for (int i = 0; i < levelUpSkillIcons.length; i++) {
            levelUpSkillIcons[i] = SpriteCache.get(i + 731);
        }
        background = SpriteCache.get(695);
        fill = SpriteCache.get(696);
        levelUp = SpriteCache.get(697);
        for (int i = 0; i < number.length; i++) {
            number[i] = SpriteCache.get(i + 721);
        }
    }
    private int levelUpTo;
    private int levelUpCycle;
    private float progress;
    private int position;
    private int lastUpdateCycle;
    private int appearCycle;
 
    private SkillOrbHandler(int skill) {
        position = -1;
        appearCycle = Client.loopCycle;
        progress = getProgress(skill);
        levelUpTo = -1;
    }
 
    private void levelUp(int level) {
        levelUpCycle = Client.loopCycle;
        levelUpTo = level;
    }
}