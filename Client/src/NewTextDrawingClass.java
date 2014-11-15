

import java.util.ArrayList;

public class NewTextDrawingClass extends DrawingArea {
	
	String smileys[] = new String[]
			{
				":)", ":(", ":s", "xd"
			};
	
    public int baseCharacterHeight = 0;
    public int anInt4142;
    public int anInt4144;
    public int[] characterDrawYOffsets;
    public int[] characterHeights;
    public int[] characterDrawXOffsets;
    public int[] characterWidths;
    public int[] iconWidths;
    public byte[] aByteArray4151;
    public byte[][] fontPixels;
    public int[] characterScreenWidths;
    public Sprite[] chatImages;
    public static String rsString;
    public static String startTransparency;
    public static String startDefaultShadow;
    public static String endShadow = "/shad";
    public static String endEffect;
    public static String aRSString_4143;
    public static String endStrikethrough = "/str";
    public static String aRSString_4147;
    public static String startColor;
    public static String lineBreak;
    public static String startStrikethrough;
    public static String endColor;
    public static String startImage;
    public static String endUnderline;
    public static String defaultStrikethrough;
    public static String startShadow;
    public static String startEffect;
    public static String aRSString_4162;
    public static String aRSString_4163;
    public static String endTransparency;
    public static String aRSString_4165;
    public static String startUnderline;
    public static String startDefaultUnderline;
    public static String aRSString_4169;
    public static String[] splitTextStrings;
    public static int defaultColor;
    public static int textShadowColor;
    public static int strikethroughColor;
    public static int defaultTransparency;
    public static int anInt4175;
    public static int underlineColor;
    public static int defaultShadow;
    public static int anInt4178;
    public static int transparency;
    public static int textColor;

    public NewTextDrawingClass(boolean TypeFont, String s, CacheArchive archive) {
        fontPixels = new byte[256][];
        characterWidths = new int[256];
        characterHeights = new int[256];
        characterDrawXOffsets = new int[256];
        characterDrawYOffsets = new int[256];
        characterScreenWidths = new int[256];
        Stream stream = new Stream(archive.getDataForName(s + ".dat"));
        Stream stream_1 = new Stream(archive.getDataForName("index.dat"));
        stream_1.currentOffset = stream.readUnsignedWord() + 4;
        int k = stream_1.readUnsignedByte();
        if (k > 0) {
            stream_1.currentOffset += 3 * (k - 1);
        }
        for (int l = 0; l < 256; l++) {
            characterDrawXOffsets[l] = stream_1.readUnsignedByte();
            characterDrawYOffsets[l] = stream_1.readUnsignedByte();
            int i1 = characterWidths[l] = stream_1.readUnsignedWord();
            int j1 = characterHeights[l] = stream_1.readUnsignedWord();
            int k1 = stream_1.readUnsignedByte();
            int l1 = i1 * j1;
            fontPixels[l] = new byte[l1];
            if (k1 == 0) {
                for (int i2 = 0; i2 < l1; i2++) {
                    fontPixels[l][i2] = stream.readByte();
                }

            } else if (k1 == 1) {
                for (int j2 = 0; j2 < i1; j2++) {
                    for (int l2 = 0; l2 < j1; l2++) {
                        fontPixels[l][j2 + l2 * i1] = stream.readByte();
                    }

                }

            }
            if (j1 > baseCharacterHeight && l < 128) {
                baseCharacterHeight = j1;
            }
            characterDrawXOffsets[l] = 1;
            characterScreenWidths[l] = i1 + 2;
            int k2 = 0;
            for (int i3 = j1 / 7; i3 < j1; i3++) {
                k2 += fontPixels[l][i3 * i1];
            }

            if (k2 <= j1 / 7) {
                characterScreenWidths[l]--;
                characterDrawXOffsets[l] = 0;
            }
            k2 = 0;
            for (int j3 = j1 / 7; j3 < j1; j3++) {
                k2 += fontPixels[l][(i1 - 1) + j3 * i1];
            }

            if (k2 <= j1 / 7) {
                characterScreenWidths[l]--;
            }
        }

        if (TypeFont) {
            characterScreenWidths[32] = characterScreenWidths[73];
        } else {
            characterScreenWidths[32] = characterScreenWidths[105];
        }
    }

    public void drawStringMoveY(String string, int drawX, int drawY, int color,
            int shadow, int randomMod, int randomMod2) {
        if (string != null) {
            setColorAndShadow(color, shadow);
            double d = 7.0 - (double) randomMod2 / 8.0;
            if (d < 0.0) {
                d = 0.0;
            }
            int[] yOffset = new int[string.length()];
            for (int index = 0; index < string.length(); index++) {
                yOffset[index] = (int) (Math.sin((double) index / 1.5 + (double) randomMod) * d);
            }
            drawBaseStringMoveXY(string, drawX - getTextWidth(string) / 2, drawY, null, yOffset);
        }
    }

    public int getCharacterWidth(int i) {
        return characterScreenWidths[i & 0xff];
    }

    public void setDefaultTextEffectValues(int color, int shadow, int trans) {
        strikethroughColor = -1;
        underlineColor = -1;
        textShadowColor = defaultShadow = shadow;
        textColor = defaultColor = color;
        transparency = defaultTransparency = trans;
        anInt4178 = 0;
        anInt4175 = 0;
    }

    public static int method1014(byte[][] is, byte[][] is_27_, int[] is_28_,
            int[] is_29_, int[] is_30_, int i,
            int i_31_) {
        int i_32_ = is_28_[i];
        int i_33_ = i_32_ + is_30_[i];
        int i_34_ = is_28_[i_31_];
        int i_35_ = i_34_ + is_30_[i_31_];
        int i_36_ = i_32_;
        if (i_34_ > i_32_) {
            i_36_ = i_34_;
        }
        int i_37_ = i_33_;
        if (i_35_ < i_33_) {
            i_37_ = i_35_;
        }
        int i_38_ = is_29_[i];
        if (is_29_[i_31_] < i_38_) {
            i_38_ = is_29_[i_31_];
        }
        byte[] is_39_ = is_27_[i];
        byte[] is_40_ = is[i_31_];
        int i_41_ = i_36_ - i_32_;
        int i_42_ = i_36_ - i_34_;
        for (int i_43_ = i_36_; i_43_ < i_37_; i_43_++) {
            int i_44_ = is_39_[i_41_++] + is_40_[i_42_++];
            if (i_44_ < i_38_) {
                i_38_ = i_44_;
            }
        }
        return -i_38_;
    }

    public void drawCenteredStringMoveXY(String string, int drawX, int drawY, int color,
            int shadow, int randomMod) {
        if (string != null) {
            setColorAndShadow(color, shadow);
            int[] xMods = new int[string.length()];
            int[] yMods = new int[string.length()];
            for (int index = 0; index < string.length(); index++) {
                xMods[index] = (int) (Math.sin((double) index / 5.0 + (double) randomMod / 5.0) * 5.0);
                yMods[index] = (int) (Math.sin((double) index / 3.0 + (double) randomMod / 5.0) * 5.0);
            }
            drawBaseStringMoveXY(string, drawX - getTextWidth(string) / 2, drawY, xMods,
                                 yMods);
        }
    }

    public void drawCenteredStringMoveY(String class100, int drawX, int drawY, int color,
            int shadow, int i_54_) {
        if (class100 != null) {
            setColorAndShadow(color, shadow);
            int[] yOffset = new int[class100.length()];
            for (int index = 0; index < class100.length(); index++) {
                yOffset[index] = (int) (Math.sin((double) index / 2.0 + (double) i_54_ / 5.0) * 5.0);
            }
            drawBaseStringMoveXY(class100, drawX - getTextWidth(class100) / 2, drawY, null,
                                 yOffset);
        }
    }

    public void unpackChatImages(Sprite[] icons) {
        chatImages = icons;
    }
    public ArrayList<Integer[]> findSmileys(String s)
    {
    	ArrayList<Integer[]> smileyz = new ArrayList<Integer[]>();
    	int startIndex = -1;
    	for(int i = 0; i < s.length(); i++)
    	{
    		for(String s1 : smileys)
    		{
    			if(s.equalsIgnoreCase(s1.substring(0,0)))
    			{
    				startIndex = i;
    			}
    			if(s.equalsIgnoreCase(s1.substring(s1.length(), s1.length())))
    			{
    				if(startIndex != -1)
    				{
    					String sub = s.substring(startIndex, i);
    					if(sub.equalsIgnoreCase(s1))
    						smileyz.add(new Integer[] {startIndex, i});
    				}
    			}
    		}
    	}
    	return smileyz;
    }
    public void drawBasicString(String string, int drawX, int drawY) {
        drawY -= baseCharacterHeight;
        int startIndex = -1;
        ArrayList<Integer[]> smileyz = findSmileys(string);
        for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
            int character = string.charAt(currentCharacter);
            Integer[] smiley = null;
            for(Integer i[] : smileyz)
            {
            	if(currentCharacter >= i[0].intValue() && currentCharacter <= i[1].intValue())
            	{
            		smiley = i;
            		break;
            	}
            }
            if (character > 255) {
                character = 32;
            }

            if (character == 60) {
                startIndex = currentCharacter;
            } else {
            	if(smiley != null)
            	{
            		String effectString = string.substring(smiley[0].intValue(), smiley[1].intValue());
            		for(String s2 : smileys) {
		            	if (effectString.equalsIgnoreCase(s2)) {
		            		int imageId = Integer.valueOf(effectString.substring(4));
		                	Sprite icon = chatImages[imageId];
		                	int iconModY = icon.myWidth;
		                	icon.drawSprite(drawX, (drawY + baseCharacterHeight - iconModY));
		                	drawX += icon.myWidth;
		                	break;
		            	}
            		}
            	}
                if (character == 62 && startIndex != -1) {
                    String effectString = string.substring(startIndex + 1, currentCharacter);
                    startIndex = -1;
                    if (effectString.equals(startEffect)) {
                        character = 60;
                    } else if (effectString.equals(endEffect)) {
                        character = 62;
                    } else if (effectString.equals(rsString)) {
                        character = 160;
                    } else if (effectString.equals(aRSString_4162)) {
                        character = 173;
                    } else if (effectString.equals(aRSString_4165)) {
                        character = 215;
                    } else if (effectString.equals(aRSString_4147)) {
                        character = 128;
                    } else if (effectString.equals(aRSString_4163)) {
                        character = 169;
                    } else if (effectString.equals(aRSString_4169)) {
                        character = 174;
                    } else {
                        setTextEffects(effectString);
                        continue;
                    }
                }
                if (startIndex == -1 && smiley == null) {
                    int width = characterWidths[character];
                    int height = characterHeights[character];
                    if (character != 32) {
                        if (transparency == 256) {
                            if (textShadowColor != -1) {
                                drawCharacter(character,
                                              drawX + characterDrawXOffsets[character] + 1,
                                              drawY + characterDrawYOffsets[character] + 1,
                                              width, height, textShadowColor, true);
                            }
                            drawCharacter(character, drawX + characterDrawXOffsets[character],
                                          drawY + characterDrawYOffsets[character], width,
                                          height, textColor, false);
                        } else {
                            if (textShadowColor != -1) {
                                drawTransparentCharacter(character,
                                                         drawX + characterDrawXOffsets[character] + 1,
                                                         drawY + characterDrawYOffsets[character] + 1,
                                                         width, height, textShadowColor, transparency,
                                                         true);
                            }
                            drawTransparentCharacter(character, drawX + characterDrawXOffsets[character],
                                                     drawY + characterDrawYOffsets[character], width,
                                                     height, textColor, transparency, false);
                        }
                    } else if (anInt4178 > 0) {
                        anInt4175 += anInt4178;
                        drawX += anInt4175 >> 8;
                        anInt4175 &= 0xff;
                    }
                    int lineWidth = characterScreenWidths[character];
                    drawX += lineWidth;
                }
            }
        }
    }

    public void drawRAString(String string, int drawX, int drawY, int color,
            int shadow) {
        if (string != null) {
            setColorAndShadow(color, shadow);
            drawBasicString(string, drawX - getTextWidth(string), drawY);
        }
    }

   public void drawBaseStringMoveXY(String string, int drawX, int drawY, int[] xModifier,
            int[] yModifier) {
        drawY -= baseCharacterHeight;
        int startIndex = -1;
        int modifierOffset = 0;
        ArrayList<Integer[]> smileyz = findSmileys(string);
        for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
            int character = string.charAt(currentCharacter);

            Integer[] smiley = null;
            for(Integer i[] : smileyz)
            {
            	if(currentCharacter >= i[0].intValue() && currentCharacter <= i[1].intValue())
            	{
            		smiley = i;
            		break;
            	}
            }
            if (character == 60) {
                startIndex = currentCharacter;
            } else {
                if (character == 62 && startIndex != -1) {
                    String effectString = string.substring(startIndex + 1, currentCharacter);
                    startIndex = -1;
                    if (effectString.equals(startEffect)) {
                        character = 60;
                    } else if (effectString.equals(endEffect)) {
                        character = 62;
                    } else if (effectString.equals(rsString)) {
                        character = 160;
                    } else if (effectString.equals(aRSString_4162)) {
                        character = 173;
                    } else if (effectString.equals(aRSString_4165)) {
                        character = 215;
                    } else if (effectString.equals(aRSString_4147)) {
                        character = 128;
                    } else if (effectString.equals(aRSString_4163)) {
                        character = 169;
                    } else if (effectString.equals(aRSString_4169)) {
                        character = 174;
                    } else {

                    	if(smiley != null)
                    	{
                    		String str = string.substring(smiley[0].intValue(), smiley[1].intValue());
                    		for(String s2 : smileys) {
        		            	if (str.equalsIgnoreCase(s2)) {

                                    int xModI;
                                    if (xModifier != null) {
                                        xModI = xModifier[modifierOffset];
                                    } else {
                                        xModI = 0;
                                    }
                                    int yMod;
                                    if (yModifier != null) {
                                        yMod = yModifier[modifierOffset];
                                    } else {
                                        yMod = 0;
                                    }
                                    modifierOffset++;
                                    Sprite sprite = chatImages[12];
                                    int iconOffsetY = sprite.myWidth;
                                    sprite.drawSprite(drawX + xModI, (drawY + baseCharacterHeight - iconOffsetY + yMod));
                                    drawX += sprite.myWidth;
                                    
        		                	break;
        		            	}
                    		}
                    	}
                        if (effectString.startsWith(startImage)) {
                            try {
                                int xModI;
                                if (xModifier != null) {
                                    xModI = xModifier[modifierOffset];
                                } else {
                                    xModI = 0;
                                }
                                int yMod;
                                if (yModifier != null) {
                                    yMod = yModifier[modifierOffset];
                                } else {
                                    yMod = 0;
                                }
                                modifierOffset++;
                                int iconId = Integer.valueOf(effectString.substring(4));
                                Sprite sprite = chatImages[iconId];
                                int iconOffsetY = sprite.myWidth;
                                    sprite.drawSprite(drawX + xModI, 16083,(drawY + baseCharacterHeight - iconOffsetY + yMod));
                                drawX += sprite.myWidth;
                            } catch (Exception exception) {
                                /* empty */
                            }
                        } else {
                            setTextEffects(effectString);
                        }
                        continue;
                    }
                }
                if (startIndex == -1 && smiley == null) {
                    int width = characterWidths[character];
                    int height = characterHeights[character];
                    int xOff;
                    if (xModifier != null) {
                        xOff = xModifier[modifierOffset];
                    } else {
                        xOff = 0;
                    }
                    int yOff;
                    if (yModifier != null) {
                        yOff = yModifier[modifierOffset];
                    } else {
                        yOff = 0;
                    }
                    modifierOffset++;
                    if (character != 32) {
                        if (transparency == 256) {
                            if (textShadowColor != -1) {
                                drawCharacter(character,
                                              (drawX + characterDrawXOffsets[character] + 1 + xOff),
                                              (drawY + characterDrawYOffsets[character] + 1 + yOff),
                                              width, height, textShadowColor, true);
                            }
                            drawCharacter(character,
                                          drawX + characterDrawXOffsets[character] + xOff,
                                          drawY + characterDrawYOffsets[character] + yOff,
                                          width, height, textColor, false);
                        } else {
                            if (textShadowColor != -1) {
                                drawTransparentCharacter(character,
                                                         (drawX + characterDrawXOffsets[character] + 1 + xOff),
                                                         (drawY + characterDrawYOffsets[character] + 1 + yOff),
                                                         width, height, textShadowColor,
                                                         transparency, true);
                            }
                            drawTransparentCharacter(character,
                                                     drawX + characterDrawXOffsets[character] + xOff,
                                                     drawY + characterDrawYOffsets[character] + yOff,
                                                     width, height, textColor, transparency,
                                                     false);
                        }
                    } else if (anInt4178 > 0) {
                        anInt4175 += anInt4178;
                        drawX += anInt4175 >> 8;
                        anInt4175 &= 0xff;
                    }
                    int i_109_ = characterScreenWidths[character];
                    if (strikethroughColor != -1) {
                        //rsDrawingArea.drawHorizontalLine(drawX,
                                                      //   drawY + (int) ((double) baseCharacterHeight * 0.7),
                                                      //   i_109_, strikethroughColor);
                    }
                    if (underlineColor != -1) {
                        //rsDrawingArea.drawHorizontalLine(drawX, drawY + baseCharacterHeight, i_109_,
                                                        // underlineColor);
                    }
                    drawX += i_109_;
                }
            }
        }
    }
    public void setTextEffects(String string) {
        do {
            try {
                if (string.startsWith(startColor)) {
                    textColor = Integer.valueOf(string.substring(4));
                } else if (string.startsWith("@whi@")) {
                    textColor = 0xFFFFFF;
                } else if (string.startsWith("@red@")) {
                    textColor = 0xFF0000;
                } else if (string.startsWith("@yel@")) {
                    textColor = 0xFFFF00;
                } else if (string.startsWith("@blu@")) {
                    textColor = 255;
                } else if (string.startsWith("@gre@")) {
                    textColor = 65280;
                } else if (string.startsWith("@cya@")) {
                    textColor = 65535;
                } else if (string.startsWith("@mag@")) {
                    textColor = 0xff00ff;
                } else if (string.startsWith("@lre@")) {
                    textColor = 0xff9040;
                } else if (string.equals(endColor)) {
                    textColor = defaultColor;
                } else if (string.startsWith(startTransparency)) {
                    transparency = Integer.valueOf(string.substring(6));
                } else if (string.equals(endTransparency)) {
                    transparency = defaultTransparency;
                } else if (string.startsWith(startStrikethrough)) {
                    strikethroughColor = Integer.valueOf(string.substring(4));
                } else if (string.equals(defaultStrikethrough)) {
                    strikethroughColor = 8388608;
                } else if (string.equals(endStrikethrough)) {
                    strikethroughColor = -1;
                } else if (string.startsWith(startUnderline)) {
                    underlineColor = Integer.valueOf(string.substring(2));
                } else if (string.equals(startDefaultUnderline)) {
                    underlineColor = 0;
                } else if (string.equals(endUnderline)) {
                    underlineColor = -1;
                } else if (string.startsWith(startShadow)) {
                    textShadowColor = Integer.valueOf(string.substring(5));
                } else if (string.equals(startDefaultShadow)) {
                    textShadowColor = 0;
                } else if (string.equals(endShadow)) {
                    textShadowColor = defaultShadow;
                } else {
                    if (!string.equals(lineBreak)) {
                        break;
                    }
                    setDefaultTextEffectValues(defaultColor, defaultShadow, defaultTransparency);
                }
            } catch (Exception exception) {
                break;
            }
            break;
        } while (false);
    }

    public void setColorAndShadow(int color, int shadow) {
        strikethroughColor = -1;
        underlineColor = -1;
        textShadowColor = defaultShadow = shadow;
        textColor = defaultColor = color;
        transparency = defaultTransparency = 256;
        anInt4178 = 0;
        anInt4175 = 0;
    }

    public int getTextWidth(String string) {
        if (string == null) {
            return 0;
        }
        int startIndex = -1;
        int finalWidth = 0;
        ArrayList<Integer[]> smileyz = findSmileys(string);
        for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
            int character = string.charAt(currentCharacter);
            Integer[] smiley = null;
            for(Integer i[] : smileyz)
            {
            	if(currentCharacter >= i[0].intValue() && currentCharacter <= i[1].intValue())
            	{
            		smiley = i;
            		break;
            	}
            }
            if (character > 255) {
                character = 32;
            }
            if (character == 60) {
                startIndex = currentCharacter;
            } else {
                if (character == 62 && startIndex != -1) {
                    String effectString = string.substring(startIndex + 1, currentCharacter);
                    startIndex = -1;
                    if (effectString.equals(startEffect)) {
                        character = 60;
                    } else if (effectString.equals(endEffect)) {
                        character = 62;
                    } else if (effectString.equals(rsString)) {
                        character = 160;
                    } else if (effectString.equals(aRSString_4162)) {
                        character = 173;
                    } else if (effectString.equals(aRSString_4165)) {
                        character = 215;
                    } else if (effectString.equals(aRSString_4147)) {
                        character = 128;
                    } else if (effectString.equals(aRSString_4163)) {
                        character = 169;
                    } else if (effectString.equals(aRSString_4169)) {
                        character = 174;
                    } else {
                    	if(smiley != null)
                    	{
                    		String str = string.substring(smiley[0].intValue(), smiley[1].intValue());
                    		for(String s2 : smileys) {
        		            	if (str.equalsIgnoreCase(s2)) {
                                    finalWidth += chatImages[12].myWidth;
        		                	break;
        		            	}
                    		}
                    	}
                        if (effectString.startsWith(startImage)) {
                            try {
                                int iconId = Integer.valueOf(effectString.substring(4));
                                finalWidth += chatImages[iconId].myWidth;
                            } catch (Exception exception) {
                            }
                        }
			
                        
                        continue;
                    }
                }
                if (startIndex == -1) {
                    finalWidth += characterScreenWidths[character];
                }
            }
        }
        return finalWidth;
    }

    public void drawBasicString(String string, int drawX, int drawY, int color,
            int shadow) {
        if (string != null) {
            setColorAndShadow(color, shadow);
            drawBasicString(string, drawX, drawY);
        }
    }

    public void drawCenteredString(String string, int drawX, int drawY, int color,
            int shadow) {
        if (string != null) {
            setColorAndShadow(color, shadow);
            drawBasicString(string, drawX - getTextWidth(string) / 2, drawY);
        }
    }

    public static void nullLoader() {
        startEffect = null;
        endEffect = null;
        rsString = null;
        aRSString_4162 = null;
        aRSString_4165 = null;
        aRSString_4147 = null;
        aRSString_4163 = null;
        aRSString_4169 = null;
        startImage = null;
        lineBreak = null;
        startColor = null;
        endColor = null;
        startTransparency = null;
        endTransparency = null;
        startUnderline = null;
        startDefaultUnderline = null;
        endUnderline = null;
        startShadow = null;
        startDefaultShadow = null;
        endShadow = null;
        startStrikethrough = null;
        defaultStrikethrough = null;
        endStrikethrough = null;
        aRSString_4143 = null;
        splitTextStrings = null;
    }

    public static void recolourAlpha(int[] ai, byte[] abyte0, int colour, int l,
            int j, int i1, int i, int k1,
            int j1, int alpha) {
        colour = ((colour & 0xff00ff) * alpha & ~0xff00ff) + ((colour & 0xff00) * alpha & 0xff0000) >> 8;
        alpha = 256 - alpha;
        for (int j2 = -i; j2 < 0; j2++) {
            for (int k2 = -i1; k2 < 0; k2++) {
                if (abyte0[l++] != 0) {
                    int l2 = ai[j];
                    ai[j++] = ((((l2 & 0xff00ff) * alpha & ~0xff00ff) + ((l2 & 0xff00) * alpha & 0xff0000)) >> 8) + colour;
                } else {
                    j++;
                }
            }
            j += k1;
            l += j1;
        }
    }

    public void drawTransparentCharacter(int i, int j, int l, int k, int i1,
            int pixels, int i3, boolean bool) {
        int k1 = j + l * DrawingArea.width;
        int l1 = DrawingArea.width - k;
        int i2 = 0;
        int j2 = 0;
        if (l < DrawingArea.topY) {
            int k2 = DrawingArea.topY - l;
            i1 -= k2;
            l = DrawingArea.topY;
            j2 += k2 * k;
            k1 += k2 * DrawingArea.width;
        }
        if (l + i1 > DrawingArea.bottomY) {
            i1 -= l + i1 - DrawingArea.bottomY;
        }
        if (j < DrawingArea.topX) {
            int l2 = DrawingArea.topX - j;
            k -= l2;
            j = DrawingArea.topX;
            j2 += l2;
            k1 += l2;
            i2 += l2;
            l1 += l2;
        }
        if (j + k > DrawingArea.bottomX) {
            int k2 = j + k - DrawingArea.bottomX;
            k -= k2;
            i2 += k2;
            l1 += k2;
        }
        if (k > 0 && i1 > 0) {
            recolourAlpha(DrawingArea.pixels, fontPixels[i], pixels,
                                             j2, k1, k, i1, l1, i2, i3);
        }
    }

    public static void colourPixels(int[] pixels, byte[] originalPixels, int colour, int j,
            int k, int l, int i1, int j1,
            int k1) {
        int l1 = -(l >> 2);
        l = -(l & 0x3);
        for (int i2 = -i1; i2 < 0; i2++) {
            for (int j2 = l1; j2 < 0; j2++) {
                if (originalPixels[j++] != 0) {
                    pixels[k++] = colour;
                } else {
                    k++;
                }
                if (originalPixels[j++] != 0) {
                    pixels[k++] = colour;
                } else {
                    k++;
                }
                if (originalPixels[j++] != 0) {
                    pixels[k++] = colour;
                } else {
                    k++;
                }
                if (originalPixels[j++] != 0) {
                    pixels[k++] = colour;
                } else {
                    k++;
                }
            }
            for (int k2 = l; k2 < 0; k2++) {
                if (originalPixels[j++] != 0) {
                    pixels[k++] = colour;
                } else {
                    k++;
                }
            }
            k += j1;
            j += k1;
        }
    }

    public void drawCharacter(int character, int j, int l, int k, int i1,
            int pixels, boolean bool) {
        int k1 = j + l * DrawingArea.width;
        int l1 = DrawingArea.width - k;
        int i2 = 0;
        int j2 = 0;
        if (l < DrawingArea.topY) {
            int k2 = DrawingArea.topY - l;
            i1 -= k2;
            l = DrawingArea.topY;
            j2 += k2 * k;
            k1 += k2 * DrawingArea.width;
        }
        if (l + i1 > DrawingArea.bottomY) {
            i1 -= l + i1 - DrawingArea.bottomY;
        }
        if (j < DrawingArea.topX) {
            int l2 = DrawingArea.topX - j;
            k -= l2;
            j = DrawingArea.topX;
            j2 += l2;
            k1 += l2;
            i2 += l2;
            l1 += l2;
        }
        if (j + k > DrawingArea.bottomX) {
            int i3 = j + k - DrawingArea.bottomX;
            k -= i3;
            i2 += i3;
            l1 += i3;
        }
        if (k > 0 && i1 > 0) {
            colourPixels(DrawingArea.pixels, fontPixels[character],
                                  pixels, j2, k1, k, i1, l1, i2);

        }
    }

    static {
        startTransparency = "trans=";
        startStrikethrough = "str=";
        startDefaultShadow = "shad";
        startColor = "col=";
        lineBreak = "br";
        defaultStrikethrough = "str";
        endUnderline = "/u";
        startImage = "img=";
        startShadow = "shad=";
        startUnderline = "u=";
        endColor = "/col";
        startDefaultUnderline = "u";
        endTransparency = "/trans";

        aRSString_4143 = Integer.toString(100);
        rsString = "nbsp";
        aRSString_4169 = "reg";
        aRSString_4165 = "times";
        aRSString_4162 = "shy";
        aRSString_4163 = "copy";
        endEffect = "gt";
        aRSString_4147 = "euro";
        startEffect = "lt";
        defaultTransparency = 256;
        defaultShadow = -1;
        anInt4175 = 0;
        textShadowColor = -1;
        textColor = 0;
        defaultColor = 0;
        strikethroughColor = -1;
        splitTextStrings = new String[100];
        underlineColor = -1;
        anInt4178 = 0;
        transparency = 256;
    }
}