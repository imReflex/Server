

import java.util.Random;

public final class TypeFace extends DrawingArea {

	public TypeFace(boolean large, String s, CacheArchive streamLoader) {
		try {
			int length = (s.equals("hit_full") || s.equals("critical_full")) ? 58 : 256;
			characterPixels = new byte[length][];
			characterWidths = new int[length];
			characterHeights = new int[length];
			characterXOffsets = new int[length];
			characterYOffsets = new int[length];
			characterScreenWidths = new int[length];
			random = new Random();
			strikeThrough = false;
			Stream stream = new Stream(streamLoader.getDataForName(s + ".dat"));
			Stream stream_1 = new Stream(streamLoader.getDataForName("index.dat"));
			stream_1.currentOffset = stream.readUnsignedWord() + 4;
			int type = stream_1.readUnsignedByte();
			if (type > 0)
				stream_1.currentOffset += 3 * (type - 1);
			for (int character = 0; character < length; character++) {
				characterXOffsets[character] = stream_1.readUnsignedByte();
				characterYOffsets[character] = stream_1.readUnsignedByte();
				int characterWidth = characterWidths[character] = stream_1.readUnsignedWord();
				int characterHeight = characterHeights[character] = stream_1.readUnsignedWord();
				int characterType = stream_1.readUnsignedByte();
				int characterSize = characterWidth * characterHeight;
				characterPixels[character] = new byte[characterSize];
				if (characterType == 0) {
					for (int pixel = 0; pixel < characterSize; pixel++)
						characterPixels[character][pixel] = stream.readSignedByte();

				} else if (characterType == 1) {
					for (int characterX = 0; characterX < characterWidth; characterX++) {
						for (int characterY = 0; characterY < characterHeight; characterY++)
							characterPixels[character][characterX + characterY * characterWidth] = stream.readSignedByte();
					}
				}
				if (characterHeight > characterDefaultHeight && character < 128)
					characterDefaultHeight = characterHeight;
				characterXOffsets[character] = 1;
				characterScreenWidths[character] = characterWidth + 2;
				int pixelCount = 0;
				for (int i3 = characterHeight / 7; i3 < characterHeight; i3++)
					pixelCount += characterPixels[character][i3 * characterWidth];
				if (pixelCount <= characterHeight / 7) {
					characterScreenWidths[character]--;
					characterXOffsets[character] = 0;
				}
				pixelCount = 0;
				for (int j3 = characterHeight / 7; j3 < characterHeight; j3++)
					pixelCount += characterPixels[character][(characterWidth - 1) + j3 * characterWidth];
				if (pixelCount <= characterHeight / 7)
					characterScreenWidths[character]--;
			}
			if (large) {
				if(characterScreenWidths.length > 73)
					characterScreenWidths[32] = characterScreenWidths[73];
			} else {
				if(characterScreenWidths.length > 105)
					characterScreenWidths[32] = characterScreenWidths[105];
			}
		} catch (Exception _ex) {
			_ex.printStackTrace();
			System.out.println("Error loading font: "+s);
		}
	}

	public void method38(String s, int i, int j, int colour, boolean flag)
	{
		int l = getStringEffectsWidth(s) / 2;
		int i1 = method44();
		if(i - l > DrawingArea.bottomY)
			return;
		if(i + l < DrawingArea.topX)
			return;
		if(j - i1 > DrawingArea.bottomX)
			return;
		if(j < 0)
		{
			return;
		} else
		{
			drawStringCenter(colour, s, i-l, j);
			return;
		}
	}
	public int method40()
	{
		return characterScreenWidths[8] - 1;
	}
	public int method44()
	{
		return characterScreenWidths[6];
	}
	
	public void drawStringLeft(String string, int x, int color, int y) {
		drawString(color, string, y, x - getStringWidth(string));
	}

	public void drawStringCenter(int colour, String txt, int y, int x) {
		drawString(colour, txt, y, x - getStringWidth(txt) / 2);
	}

	public void drawStringNormal(String s, int x, int color, int y) {
		drawString(color, s, y, x);
	}

	public void drawShadowedStringNormal(int color, int x, String string, int y, boolean shadowed) {
		drawShadowedString(shadowed, x, color, string, y);
	}

	public void setPixelsInfo(int width, int height, int transparancy, int newPixels[], byte oldPixels[], int newOffset, int oldOffset, int place1, int place2, int color) {
		color = ((color & 0xff00ff) * transparancy & 0xff00ff00) + ((color & 0xff00) * transparancy & 0xff0000) >> 8;
		transparancy = 256 - transparancy;
		for (int j2 = -height; j2 < 0; j2++) {
			for (int k2 = -width; k2 < 0; k2++) {
				if (oldPixels[oldOffset++] != 0) {
					int l2 = newPixels[newOffset];
					newPixels[newOffset++] = (((l2 & 0xff00ff) * transparancy & 0xff00ff00) + ((l2 & 0xff00) * transparancy & 0xff0000) >> 8) + color;
				} else {
					newOffset++;
				}
			}
			newOffset += place1;
			oldOffset += place2;
		}
	}

	private void drawChar(byte text[], int x, int y, int width, int height, int color, int opacity) {
		int newOffset = x + y * DrawingArea.width;
		int place1 = DrawingArea.width - width;
		int place2 = 0;
		int oldOffset = 0;
		if (y < DrawingArea.topY) {
			int Height2 = DrawingArea.topY - y;
			height -= Height2;
			y = DrawingArea.topY;
			oldOffset += Height2 * width;
			newOffset += Height2 * DrawingArea.width;
		}
		if (y + height >= DrawingArea.bottomY) {
			height -= ((y + height) - DrawingArea.bottomY);
		}
		if (x < DrawingArea.topX) {
			int toLeft = DrawingArea.topX - x;
			width -= toLeft;
			x = DrawingArea.topX;
			oldOffset += toLeft;
			newOffset += toLeft;
			place2 += toLeft;
			place1 += toLeft;
		}
		if (x + width >= DrawingArea.bottomX) {
			int toRight = ((x + width) - DrawingArea.bottomX);
			width -= toRight;
			place2 += toRight;
			place1 += toRight;
		}
		if ((width <= 0) || (height <= 0)) {
			return;
		}
		setPixelsInfo(width, height, opacity, DrawingArea.pixels, text, newOffset, oldOffset, place1, place2, color);
	}

	public void drawTransparentText(int i, String s, int j, int k, int l, int opacity) {
		if (s == null)
			return;
		j -= characterDefaultHeight;
		for (int i1 = 0; i1 < s.length(); i1++) {
			char c = s.charAt(i1);
			if (c != ' ')
				drawChar(characterPixels[c], l + characterXOffsets[c], j + characterYOffsets[c], characterWidths[c], characterHeights[c], i, opacity);
			l += characterScreenWidths[c];
		}
		k = 50 / k;
	}

	public void drawOpacityText(int color, String text, int yCoord, int xCoord, int alpha) {
		drawTransparentText(color, text, yCoord, 822, xCoord - getStringWidth(text) / 2, alpha);
	}

	public void drawStringCenter(int colour, int x, String text, int y, boolean shadowed) {
		drawShadowedString(shadowed, x - getStringEffectsWidth(text) / 2, colour, text, y);
	}

	public void drawChatInput(int color, int x, String string, int y, boolean shadowed) {
		drawShadowedString(shadowed, x, color, string, y);
	}

	public int getStringEffectsWidth(String s) {
		if (s == null)
			return 0;
		int width = 0;
		for (int character = 0; character < s.length(); character++)
			if (s.charAt(character) == '@' && character + 4 < s.length() && s.charAt(character + 4) == '@')
				character += 4;
			else
				width += characterScreenWidths[s.charAt(character)];
		return width;
	}

	public int charFor(int index, String s) {
		int character = 0;
		for (int k = 0; k < s.length(); k++) {
			if (s.charAt(k) == '@' && k + 4 < s.length() && s.charAt(k + 4) == '@')
				k += 4;
			else
				character += characterScreenWidths[s.charAt(k)];
			if (character >= index - 4 && character <= index + 4) {
				return character;
			}
		}
		return character;
	}

	public int getStringWidth(String s) {
		if (s == null)
			return 0;
		int width = 0;
		for (int character = 0; character < s.length(); character++)
			width += characterScreenWidths[s.charAt(character)];
		return width;
	}

	public void drawString(int color, String text, int y, int x) {
		if (text == null)
			return;
		y -= characterDefaultHeight;
		for (int character = 0; character < text.length(); character++) {
			char c = text.charAt(character);
			if (c != ' ')
				drawCharacter(characterPixels[c], x + characterXOffsets[c], y + characterYOffsets[c], characterWidths[c], characterHeights[c], color);
			x += characterScreenWidths[c];
		}
	}

	public void drawCenteredStringWaveY(int color, String s, int x, int wave, int y) {
		if (s == null)
			return;
		x -= getStringWidth(s) / 2;
		y -= characterDefaultHeight;
		for (int character = 0; character < s.length(); character++) {
			char c = s.charAt(character);
			if (c != ' ')
				drawCharacter(characterPixels[c], x + characterXOffsets[c], y + characterYOffsets[c] + (int) (Math.sin((double) character / 2D + (double) wave / 5D) * 5D), characterWidths[c], characterHeights[c], color);
			x += characterScreenWidths[c];
		}
	}

	public void drawCeneteredStringWaveXY(int x, String string, int wave, int y, int color) {
		if (string == null)
			return;
		x -= getStringWidth(string) / 2;
		y -= characterDefaultHeight;
		for (int character = 0; character < string.length(); character++) {
			char c = string.charAt(character);
			if (c != ' ')
				drawCharacter(characterPixels[c], x + characterXOffsets[c] + (int) (Math.sin((double) character / 5D + (double) wave / 5D) * 5D), y + characterYOffsets[c] + (int) (Math.sin((double) character / 3D + (double) wave / 5D) * 5D), characterWidths[c], characterHeights[c], color);
			x += characterScreenWidths[c];
		}
	}

	public void drawCenteredStringWaveXYMove(int i, String string, int j, int y, int x, int i1) {
		if (string == null)
			return;
		double speed = 7D - (double) i / 8D;
		if (speed < 0.0D)
			speed = 0.0D;
		x -= getStringWidth(string) / 2;
		y -= characterDefaultHeight;
		for (int index = 0; index < string.length(); index++) {
			char c = string.charAt(index);
			if (c != ' ')
				drawCharacter(characterPixels[c], x + characterXOffsets[c], y + characterYOffsets[c] + (int) (Math.sin((double) index / 1.5D + (double) j) * speed), characterWidths[c], characterHeights[c], i1);
			x += characterScreenWidths[c];
		}
	}

	public void drawShadowedString(boolean shadow, int x, int j, String s, int y) {
		strikeThrough = false;
		int offsetX = x;
		if (s == null)
			return;
		y -= characterDefaultHeight;
		for (int character = 0; character < s.length(); character++)
			if (s.charAt(character) == '@' && character + 4 < s.length() && s.charAt(character + 4) == '@') {
				int stringColor = getColorByName(s.substring(character + 1, character + 4));
				if (stringColor != -1)
					j = stringColor;
				character += 4;
			} else {
				char c = s.charAt(character);
				if (c != ' ') {
					if (shadow)
						drawCharacter(characterPixels[c], x + characterXOffsets[c] + 1, y + characterYOffsets[c] + 1, characterWidths[c], characterHeights[c], 0);
					drawCharacter(characterPixels[c], x + characterXOffsets[c], y + characterYOffsets[c], characterWidths[c], characterHeights[c], j);
				}
				x += characterScreenWidths[c];
			}
		if (strikeThrough)
			DrawingArea.drawLine(y + (int) ((double) characterDefaultHeight * 0.69999999999999996D), 0x800000, x - offsetX, offsetX);
	}

	public void drawShadowedSeededAlphaString(int x, int j, String s, int k, int y) {
		if (s == null)
			return;
		random.setSeed(k);
		int alpha = 192 + (random.nextInt() & 0x1f);
		y -= characterDefaultHeight;
		for (int character = 0; character < s.length(); character++)
			if (s.charAt(character) == '@' && character + 4 < s.length() && s.charAt(character + 4) == '@') {
				int stringColor = getColorByName(s.substring(character + 1, character + 4));
				if (stringColor != -1)
					j = stringColor;
				character += 4;
			} else {
				char c = s.charAt(character);
				if (c != ' ') {
					method394(192, x + characterXOffsets[c] + 1, characterPixels[c], characterWidths[c], y + characterYOffsets[c] + 1, characterHeights[c], 0);
					method394(alpha, x + characterXOffsets[c], characterPixels[c], characterWidths[c], y + characterYOffsets[c], characterHeights[c], j);
				}
				x += characterScreenWidths[c];
				if ((random.nextInt() & 3) == 0)
					x++;
			}
	}

	private int getColorByName(String s) {
		if (s.equals("369"))// color code, use as @###@
			return 0x336699;// hex code
		if (s.equals("mon"))
			return 0x00ff80;
		if (s.equals("red"))
			return 0xff0000;
		if (s.equals("gre"))
			return 65280;
		if (s.equals("blu"))
			return 255;
		if (s.equals("yel"))
			return 0xffff00;
		if (s.equals("cya"))
			return 65535;
		if (s.equals("mag"))
			return 0xff00ff;
		if (s.equals("whi"))
			return 0xffffff;
		if (s.equals("bla"))
			return 0;
		if (s.equals("lre"))
			return 0xff9040;
		if (s.equals("dre"))
			return 0x800000;
		if (s.equals("dbl"))
			return 128;
		if (s.equals("or1"))
			return 0xffb000;
		if (s.equals("or2"))
			return 0xff7000;
		if (s.equals("or3"))
			return 0xff3000;
		if (s.equals("gr1"))
			return 0xc0ff00;
		if (s.equals("gr2"))
			return 0x80ff00;
		if (s.equals("gr3"))
			return 0x40ff00;
		if (s.equals("str"))
			strikeThrough = true;
		if (s.equals("end"))
			strikeThrough = false;
		return -1;
	}

	private void drawCharacter(byte abyte0[], int i, int j, int k, int l, int i1) {
		int j1 = i + j * DrawingArea.width;
		int k1 = DrawingArea.width - k;
		int l1 = 0;
		int i2 = 0;
		if (j < DrawingArea.topY) {
			int j2 = DrawingArea.topY - j;
			l -= j2;
			j = DrawingArea.topY;
			i2 += j2 * k;
			j1 += j2 * DrawingArea.width;
		}
		if (j + l >= DrawingArea.bottomY)
			l -= ((j + l) - DrawingArea.bottomY) + 1;
		if (i < DrawingArea.topX) {
			int k2 = DrawingArea.topX - i;
			k -= k2;
			i = DrawingArea.topX;
			i2 += k2;
			j1 += k2;
			l1 += k2;
			k1 += k2;
		}
		if (i + k >= DrawingArea.bottomX) {
			int l2 = ((i + k) - DrawingArea.bottomX) + 1;
			k -= l2;
			l1 += l2;
			k1 += l2;
		}
		if (!(k <= 0 || l <= 0)) {
			method393(DrawingArea.pixels, abyte0, i1, i2, j1, k, l, k1, l1);
		}
	}

	private void method393(int ai[], byte abyte0[], int i, int j, int k, int l, int i1, int j1, int k1) {
		int l1 = -(l >> 2);
		l = -(l & 3);
		for (int i2 = -i1; i2 < 0; i2++) {
			for (int j2 = l1; j2 < 0; j2++) {
				if (abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;
				if (abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;
				if (abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;
				if (abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;
			}
			for (int k2 = l; k2 < 0; k2++)
				if (abyte0[j++] != 0)
					ai[k++] = i;
				else
					k++;

			k += j1;
			j += k1;
		}
	}

	private void method394(int i, int j, byte abyte0[], int k, int l, int i1, int j1) {
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
		if (l + i1 >= DrawingArea.bottomY)
			i1 -= ((l + i1) - DrawingArea.bottomY) + 1;
		if (j < DrawingArea.topX) {
			int l2 = DrawingArea.topX - j;
			k -= l2;
			j = DrawingArea.topX;
			j2 += l2;
			k1 += l2;
			i2 += l2;
			l1 += l2;
		}
		if (j + k >= DrawingArea.bottomX) {
			int i3 = ((j + k) - DrawingArea.bottomX) + 1;
			k -= i3;
			i2 += i3;
			l1 += i3;
		}
		if (k <= 0 || i1 <= 0)
			return;
		method395(abyte0, i1, k1, DrawingArea.pixels, j2, k, i2, l1, j1, i);
	}

	private void method395(byte abyte0[], int i, int j, int ai[], int l, int i1, int j1, int k1, int l1, int i2) {
		l1 = ((l1 & 0xff00ff) * i2 & 0xff00ff00) + ((l1 & 0xff00) * i2 & 0xff0000) >> 8;
		i2 = 256 - i2;
		for (int j2 = -i; j2 < 0; j2++) {
			for (int k2 = -i1; k2 < 0; k2++)
				if (abyte0[l++] != 0) {
					int l2 = ai[j];
					ai[j++] = (((l2 & 0xff00ff) * i2 & 0xff00ff00) + ((l2 & 0xff00) * i2 & 0xff0000) >> 8) + l1;
				} else {
					j++;
				}
			j += k1;
			l += j1;
		}
	}

	public byte[][] characterPixels;
	public int[] characterWidths;
	public int[] characterHeights;
	public int[] characterXOffsets;
	public int[] characterYOffsets;
	public int[] characterScreenWidths;
	public int characterDefaultHeight;
	public Random random;
	public boolean strikeThrough;
}
