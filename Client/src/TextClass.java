

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

import sign.signlink;

public final class TextClass {

	public static long longForName(String s) {
		long l = 0L;
		for (int i = 0; i < s.length() && i < 12; i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}

		for (; l % 37L == 0L && l != 0L; l /= 37L)
			;
		return l;
	}

	public static String nameForLong(long l) {
		try {
			if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
				return "invalid_name";
			if (l % 37L == 0L)
				return "invalid_name";
			int i = 0;
			char ac[] = new char[12];
			while (l != 0L) {
				long l1 = l;
				l /= 37L;
				ac[11 - i++] = validChars[(int) (l1 - l * 37L)];
			}
			return new String(ac, 12 - i, i);
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("81570, " + l + ", " + (byte) -99 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}
	
	public static String stringForLong(long l) {
		try {
			if (l <= 0L || l >= 0x5b5b57f8a98a5dd1L)
				return "";
			if (l % 37L == 0L)
				return "";
			int i = 0;
			char ac[] = new char[12];
			while (l != 0L) {
				long l1 = l;
				l /= 37L;
				ac[11 - i++] = validChars[(int) (l1 - l * 37L)];
			}
			return new String(ac, 12 - i, i);
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("81570, " + l + ", " + (byte) -99 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public static long method585(String s) {
		s = s.toUpperCase();
		long l = 0L;
		for (int i = 0; i < s.length(); i++) {
			l = (l * 61L + (long) s.charAt(i)) - 32L;
			l = l + (l >> 56) & 0xffffffffffffffL;
		}
		return l;
	}

	public static String method586(int i) {
		return (i >> 24 & 0xff) + "." + (i >> 16 & 0xff) + "." + (i >> 8 & 0xff) + "." + (i & 0xff);
	}

	public static String fixName(String s) {
		if (s == null)
			return "";
		if (s.length() > 0) {
			char ac[] = s.toCharArray();
			for (int j = 0; j < ac.length; j++)
				if (ac[j] == '_') {
					ac[j] = ' ';
					if (j + 1 < ac.length && ac[j + 1] >= 'a' && ac[j + 1] <= 'z')
						ac[j + 1] = (char) ((ac[j + 1] + 65) - 97);
				}

			if (ac[0] >= 'a' && ac[0] <= 'z')
				ac[0] = (char) ((ac[0] + 65) - 97);
			return new String(ac);
		} else {
			return s;
		}
	}

	/**
	 * Cuts a string into more than one line if it exceeds the specified max
	 * width.
	 * 
	 * @param font
	 * @param prefix
	 * @param string
	 * @param maxWidth
	 * @param ranked
	 * @return
	 */
	public static String[] splitString(TypeFace font, String prefix, String string, int maxWidth, boolean ranked) {
		maxWidth -= font.getStringEffectsWidth(prefix) + (ranked ? 14 : 0);
		if (font.getStringEffectsWidth(prefix + string) + (ranked ? 14 : 0) <= maxWidth) {
			return new String[] { string };
		}
		String line = "";
		String[] cut = new String[2];
		boolean split = false;
		char[] characters = string.toCharArray();
		int space = -1;
		for (int index = 0; index < characters.length; index++) {
			char c = characters[index];
			line += c;
			if (c == ' ') {
				space = index;
			}
			if (!split) {
				if (font.getStringEffectsWidth(line) + 10 > maxWidth) {
					if (space != -1 && characters[index - 1] != ' ') {
						cut[0] = line.substring(0, space);
						line = line.substring(space);
					} else {
						cut[0] = line;
						line = "";
					}
					split = true;
				}
			}
		}
		if (line.length() > 0) {
			cut[1] = line;
		}
		return cut;
	}

	public static boolean isAlphanumeric(String string) {
		if (!string.matches("[A-Za-z0-9 ]+")) {
			return false;
		}
		return true;
	}

	public static boolean isValidName(String name) {
		if (name.contains("  ") || name.contains("   ") || name.contains("    ") || name.contains("     ") || name.contains("      ") || name.contains("       ") || name.contains("        ") || name.contains("         ") || name.contains("          ") || name.contains("           ") || name.contains("            ")) {
			return false;
		}
		if (!name.matches("[A-Za-z0-9 ]+")) {
			return false;
		}
		return true;
	}

	public static boolean isValidEmail(String email) {
		if (email.matches(".+@.+\\.[a-z]+")) {
			return true;
		}
		return false;
	}

	public static String passwordAsterisks(String s) {
		if (s == null)
			return "";
		StringBuffer stringbuffer = new StringBuffer();
		for (int j = 0; j < s.length(); j++)
			stringbuffer.append("*");
		return stringbuffer.toString();
	}

	private static final char[] validChars = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

}
