import java.applet.AppletContext;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.zip.CRC32;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import sign.signlink;

@SuppressWarnings("all")
public class Client extends RSApplet {

	public static int log_view_dist = 10;
	public static HashMap<String, Boolean> options = new HashMap<String, Boolean>();
	public static HashMap<String, Integer> settings = new HashMap<String, Integer>();

	public static int underlay_id = 593;
	public static int currentTileId = 0;
	
	public static boolean getOption(String s) {
		return options.get(s).booleanValue();
	}
	
	public static int getSetting(String s) {
		return settings.get(s);
	}

	private void handleAccountHeadRotation() {
		if (Account.accounts == null || Account.accounts.size() == 0)
			return;
		for (int i = 0; i < Account.accounts.size(); i++) {
			RSInterface rsi = RSInterface.interfaceCache[31002+(i*4)];
			int baseX = (clientWidth / 2) - (SpriteCache.spriteCache[694].myWidth / 2) + 75;
			int baseY = (clientHeight / 2) - (SpriteCache.spriteCache[694].myHeight / 2) + 75;
			int x = baseX + RSInterface.interfaceCache[31000].childX[1+(i*4)];
			int y = baseY + RSInterface.interfaceCache[31000].childY[1+(i*4)];
			int diffX = mouseX - x;
			int diffY = y - mouseY;
			if(mouseX <= 0 && mouseY <= 0)
				return;
			if(diffY <= -100)
				diffY = -100;
			if(diffY >= 100)
				diffY = 100;
			if (diffX < 0)
				diffX = (-diffX);
			else
				diffX = 2020 - diffX;
			if (diffY < 0)
				diffY = (-diffY);
			else
				diffY = 2020 - diffY;
			
			if (diffX >= 2020)
				diffX = 2020;
			if (diffX <= 0)
				diffX = 0;
			
			if(diffY >= 2020)
				diffY = 2020;
			if(diffY <= 0)
				diffY = 0;
			rsi.modelRotation2 = diffX;
			rsi.modelRotation1 = diffY;
		}
	}

	private void handleSettings() {

		if (getOption("hd_tex")) {
			int l2 = RSInterface.interfaceCache[35565].valueIndexArray[0][1];
			variousSettings[l2] = 1 - variousSettings[l2];
		}
		if (getOption("player_shadow")) {
			int l2 = RSInterface.interfaceCache[35568].valueIndexArray[0][1];
			variousSettings[l2] = 1 - variousSettings[l2];
		}
		if (getOption("tooltip_hover")) {
			int l2 = RSInterface.interfaceCache[35571].valueIndexArray[0][1];
			variousSettings[l2] = 1 - variousSettings[l2];
		}
		/*if (!musicEnabled)
			stopMidi();*/
	}

	public void loadSettings() {
		if (!FileOperations.FileExists(signlink.findcachedir()
				+ "preferences.set")) {
			options.put("hd_tex", false);
			options.put("player_shadow", true);
			options.put("tooltip_hover", false);
			options.put("new_sprites", false);
			options.put("damage_10", false);
			options.put("combat_icons", true);
			options.put("absorb", true);
			options.put("new_items", false);
			options.put("new_idk", false);
			options.put("new_hitbar", true);
			options.put("smooth_shade", false);
			options.put("tweening", false);
			options.put("orbs", true);
			options.put("xp_orb", true);
			options.put("xp_circle", true);
			options.put("menu_crown", true);
			settings.put("menu", 317);
			settings.put("hitsplats", 632);
			settings.put("gameframe", 614);
			settings.put("revision_load", 667);
			saveSettings();
			return;
		}
		try {
			RandomAccessFile in = new RandomAccessFile(signlink.findcachedir()
					+ "preferences.set", "rw");
			int music = in.readShort();
			int sound = in.readShort();
			options.put("hd_tex", in.readByte() == 1);
			options.put("player_shadow", in.readByte() == 1);
			options.put("tooltip_hover", in.readByte() == 1);
			options.put("new_sprites", in.readByte() == 1);
			options.put("damage_10", in.readByte() == 1);
			options.put("combat_icons", in.readByte() == 1);
			options.put("absorb", in.readByte() == 1);
			options.put("new_items", in.readByte() == 1);
			options.put("new_items", false);
			options.put("new_idk", in.readByte() == 1);
			options.put("new_hitbar", in.readByte() == 1);
			options.put("smooth_shade", in.readByte() == 1);
			options.put("tweening", in.readByte() == 1);
			options.put("orbs", in.readByte() == 1);
			options.put("xp_orb", in.readByte() == 1);
			options.put("xp_circle", in.readByte() == 1);
			options.put("menu_crown", in.readByte() == 1);
			settings.put("menu", in.readInt());
			settings.put("hitsplats", in.readInt());
			settings.put("gameframe", in.readInt());
			settings.put("revision_load", in.readInt());
			if (music == -1)
				musicEnabled = false;
			else
				musicEnabled = true;
			if (sound == -1)
				soundEnabled = false;
			else
				soundEnabled = true;
			in.close();
		} catch (IOException e) {
			options.put("hd_tex", false);
			options.put("player_shadow", true);
			options.put("tooltip_hover", false);
			options.put("new_sprites", false);
			options.put("damage_10", false);
			options.put("combat_icons", true);
			options.put("absorb", true);
			options.put("new_items", false);
			options.put("new_idk", false);
			options.put("new_hitbar", true);
			options.put("smooth_shade", false);
			options.put("tweening", false);
			options.put("orbs", true);
			options.put("xp_orb", true);
			options.put("xp_circle", true);
			options.put("menu_crown", true);
			settings.put("menu", 317);
			settings.put("hitsplats", 632);
			settings.put("gameframe", 614);
			settings.put("revision_load", 667);
			saveSettings();
			loadSettings();
			e.printStackTrace();
		}
	}
	
	public void resetSettings() {
		try {
			options.put("hd_tex", false);
			options.put("player_shadow", true);
			options.put("tooltip_hover", false);
			options.put("new_sprites", false);
			options.put("damage_10", false);
			options.put("combat_icons", true);
			options.put("absorb", true);
			options.put("new_items", false);
			options.put("new_idk", false);
			options.put("new_hitbar", true);
			options.put("smooth_shade", false);
			options.put("tweening", false);
			options.put("orbs", true);
			options.put("xp_orb", true);
			options.put("xp_circle", true);
			options.put("menu_crown", true);
			settings.put("menu", 317);
			settings.put("hitsplats", 632);
			settings.put("gameframe", 614);
			settings.put("revision_load", 667);
			saveSettings();
			loadSettings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveSettings() {
		try {
			RandomAccessFile out = new RandomAccessFile(signlink.findcachedir() + "preferences.set", "rw");
			out.writeShort(musicEnabled ? signlink.midiVolume : -1);
			out.writeShort(soundEnabled ? signlink.wavevol : -1);
			out.write(getOption("hd_tex") ? 1 : 0);
			out.write(getOption("player_shadow") ? 1 : 0);
			out.write(getOption("tooltip_hover") ? 1 : 0);
			out.write(getOption("new_sprites") ? 1 : 0);
			out.write(getOption("damage_10") ? 1 : 0);
			out.write(getOption("combat_icons") ? 1 : 0);
			out.write(getOption("absorb") ? 1 : 0);
			out.write(getOption("new_items") ? 1 : 0);
			out.write(getOption("new_idk") ? 1 : 0);
			out.write(getOption("new_hitbar") ? 1 : 0);
			out.write(getOption("smooth_shade") ? 1 : 0);
			out.write(getOption("tweening") ? 1 : 0);
			out.write(getOption("orbs") ? 1 : 0);
			out.write(getOption("xp_orb") ? 1 : 0);
			out.write(getOption("xp_circle") ? 1 : 0);
			out.write(getOption("menu_crown") ? 1 : 0);
			out.writeInt(getSetting("menu"));
			out.writeInt(getSetting("hitsplats"));
			out.writeInt(getSetting("gameframe"));
			out.writeInt(getSetting("revision_load"));

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private StatGraph fpsGraph = new StatGraph("Fps");
	private StatGraph memGraph = new StatGraph("Mem");
	private int screenOpacity = 255;
	/**
	 * 
	 */
	private final static int SHADOW_SPEED = 1;
	private int shadowDestination = 255;
	private boolean shadowProcessing;

	private void processShadow() {
		if (screenOpacity < shadowDestination) {
			screenOpacity += SHADOW_SPEED;
		} else if (screenOpacity > shadowDestination) {
			screenOpacity -= SHADOW_SPEED;
		} else {
			shadowProcessing = false;
		}
	}

	private boolean forcedShadow;
	private double currentShadow;
	private boolean resting = false;
	private final String[][] PLRCOMMANDS = new String[][] {
			{ "Enter character", "hangman" },
			{ "Enter house owner's name", "popE158" } };
	private boolean clickedHelp;
	private int playerCommand;
	public static boolean onWebClient = true;
	private static final long serialVersionUID = 461042660071422207L;

	private WorldMap wm;
	private boolean[] orbTextEnabled = new boolean[4];
	static boolean openedLogger = false;
	private boolean loggerEnabled = false;
	private static int loginFailures = 0;
	public String printedMessage;
	public Logger logger;

	public Graphics2D gfx;

	public int cycle = 0, maxCycle = 150;
	public Sprite[] banner = null;
	public int offset = 0;

	void mouseWheelDragged(int i, int j) {
		if (!mouseWheelDown)
			return;
		this.cameraYawTranslate += i * 3;
		this.cameraPitchTranslate += (j << 1);
	}

	private void formatNoteTab(String notes) {
		int width = 140;
		String finalString = "";
		String[] allNotes = notes.split("-");
		for (String s : allNotes) {
			String fixed = "";
			String[] parts = splitString(s, width);
			for (String s1 : parts) {
				fixed += s1 + "\\n";
			}
			if (parts == null || parts.length == 0) {
				finalString += s + "\\n";
			} else
				finalString += fixed + "\\n";
		}
		if (allNotes == null || allNotes.length == 0) {
			finalString = notes;
		}
		sendFrame126("", 17800);
		if (finalString == null || finalString.equals("null"))
			finalString = "";
		sendFrame126(finalString, 14001);
		RSInterface.interfaceCache[14000].scrollMax = (allNotes == null ? 0
				: allNotes.length * 40);

	} 

	public String[] splitString(String string, int maxWidth) { 
		int cutsNeeded = smallText.getStringEffectsWidth(string) / maxWidth + 1;
		int cuts = 0;
		String line = "";
		String[] cut = new String[cutsNeeded];
		char[] characters = string.toCharArray();
		int space = -1;
		for (int index = 0; index < characters.length; index++) {
			char c = characters[index];
			line += c;
			if (smallText.getStringEffectsWidth(line) > maxWidth) {
				cut[cuts] = line;
				line = "";
				cuts++;
			}
		}
		if (line != "") {
			cut[cuts] = line;
		}
		return cut;
	}

	public String getPrefix(int rights) {
		/*String prefix = "cr";
		if (rights > 10) {
			prefix = "c";
		}
		return "@" + prefix + rights + "@";*/
		//System.out.println("Rights: " + rights);
		if(rights < 0 || rights == 128)
			return "";
		return "<img=" + rights + ">";
	}

	public int getPrefixRightsNew(String prefix) {
		//System.out.println("Prefix2: " + prefix);
		int rights = 0;
		try {
			rights = Integer.parseInt(prefix.substring(prefix.indexOf("=") + 1, prefix.indexOf(">")));
			//System.out.println(rights);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rights;
	}
	
	public int getImageTagIDLength(String tag) {
		int content = 0;
		try {
			content = Integer.parseInt(tag.substring(tag.indexOf("="), tag.indexOf(">")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(content >= 10)
			return 3;
		else
			return 2;
		
	}
	
	public int getPrefixRights(String prefix) {
		int rights = 0;
		int start = 3;
		int end = 4;
		if (!prefix.contains("cr")) {
			start = 2;
		}
		try {
			rights = Integer.parseInt(prefix.substring(start, end));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rights;
	}

	/**
	 * Draws a black pane, used for quests
	 * 
	 * @param opacity
	 */
	public void drawBlackPane() {
		if (paneOpacity >= 255) {
			minus = true;
		}
		if (paneOpacity <= 0) {
			minus = false;
			timesLooped = true;
		}
		paneOpacity += (minus ? -2 : 2);
		DrawingArea474.drawAlphaFilledPixels(0, 0, getClientWidth(),
				getClientHeight(), 0, paneOpacity);
		if (timesLooped)
			drawPane = false;

	}

	private boolean timesLooped = false;
	private boolean minus = false;
	private int paneOpacity = 0;
	public static int paneClosed = 0;
	public static int clientSize = 0;
	public static int clientWidth = 765, clientHeight = 503;// 503 - original
	public static int chatAreaHeight = 0;
	private int gameAreaWidth = 512, gameAreaHeight = 334;
	public int appletWidth = 765, appletHeight = 503;
	private static final int resizableWidth = getMaxWidth() - 200;
	private static final int resizableHeight = getMaxHeight() - 200;

	public static int getMaxWidth() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}

	public static int getMaxHeight() {
		return (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}

	public void toggleSize(int size) {
		// if (clientSize != size) {
		clientSize = size;
		int width = 765;
		int height = 503;
		if (size == 0) {
			size = 0;
			width = 765;
			height = 503;
			showChat = true;
			showTab = true;
			log_view_dist = 9;
			clientZoom = 0;
		} else if (size == 1) {
			size = 1;
			width = isWebclient() ? getGameComponent().getWidth()
					: resizableWidth;
			height = isWebclient() ? getGameComponent().getHeight()
					: resizableHeight;
			log_view_dist = 10;
			clientZoom = 480;
		} else if (size == 2) {
			size = 2;
			width = getMaxWidth();
			height = getMaxHeight();
			log_view_dist = 10;
			clientZoom = 480;
		} else if (size == 3) {
			clientSize = 0;
			width = 765;
			height = 503;
			log_view_dist = 9;
			clientZoom = 0;
		}
		rebuildFrame(size, width, height);
		// updateGameArea();
		// }
	}

	public boolean isWebclient() {
		return mainFrame == null && Client.webclient || Client.swiftKit;
	}

	public void checkSize() {
		if (clientSize == 1) {
			boolean requireUpdate = false;
			if (clientWidth != (isWebclient() ? getGameComponent().getWidth()
					: mainFrame.getFrameWidth())) {
				clientWidth = (isWebclient() ? getGameComponent().getWidth()
						: mainFrame.getFrameWidth());
				gameAreaWidth = clientWidth;
				requireUpdate = true;
			}
			if (clientHeight != (isWebclient() ? getGameComponent().getHeight()
					: mainFrame.getFrameHeight())) {
				clientHeight = (isWebclient() ? getGameComponent().getHeight()
						: mainFrame.getFrameHeight());
				gameAreaHeight = clientHeight;
				requireUpdate = true;
			}
			if (requireUpdate)
				updateGameArea();
		}
	}

	public void rebuildFrame(int size, int width, int height) {
		try {

			gameAreaWidth = (size == 0) ? 512 : width;
			gameAreaHeight = (size == 0) ? 334 : height;
			clientWidth = width;
			clientHeight = height;
			instance.rebuildFrame(size == 2, width, height, size == 1, size == 2);
			updateGameArea();
			super.mouseX = super.mouseY = -1;
			particleNeedsRedraw = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateGameArea() {
		try {
			Rasterizer.setBounds(clientWidth, clientHeight);
			fullScreenTextureArray = Rasterizer.lineOffsets;
			Rasterizer.setBounds(
					clientSize == 0 ? (chatAreaIP != null ? chatAreaIP.width
							: 519) : clientWidth,
					clientSize == 0 ? (chatAreaIP != null ? chatAreaIP.height
							: 165) : clientHeight);
			chatOffsets = Rasterizer.lineOffsets;
			Rasterizer.setBounds(
					clientSize == 0 ? (tabAreaIP != null ? tabAreaIP.width
							: 250) : clientWidth,
					clientSize == 0 ? (tabAreaIP != null ? tabAreaIP.height
							: 335) : clientHeight);
			sidebarOffsets = Rasterizer.lineOffsets;
			Rasterizer.setBounds(!loggedIn ? clientWidth : gameAreaWidth,
					!loggedIn ? clientHeight : gameAreaHeight);
			viewportOffsets = Rasterizer.lineOffsets;
			int ai[] = new int[9];
			for (int i8 = 0; i8 < 9; i8++) {
				int k8 = 128 + i8 * 32 + 15;
				int l8 = 600 + k8 * 3;
				int i9 = Rasterizer.SINE[k8];
				ai[i8] = l8 * i9 >> 16;
			}
			WorldController.setupViewport(500, 800, gameAreaWidth,
					gameAreaHeight, ai);
			if (loggedIn) {
				gameScreenIP = new RSImageProducer(gameAreaWidth,
						gameAreaHeight, getGameComponent());
			} else {
				titleScreen = new RSImageProducer(clientWidth, clientHeight, getGameComponent());
			}
			DrawingArea.width = clientWidth;
			DrawingArea.height = clientHeight;
			DrawingArea.pixels = new int[clientWidth * clientHeight];
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drawUnfixedGame() {
		if (clientSize != 0) {
			drawChatArea();
			drawTabArea();
			drawMinimap();
		}
	}

	public void drawChatArea() {
		int offsetX = 0;
		int offsetY = clientSize != 0 ? clientHeight - 165 : 0;
		if (clientSize == 0) {
			chatAreaIP.initDrawingArea();
		}
		Rasterizer.lineOffsets = chatOffsets;
		TypeFace textDrawingArea = normalFont;
		if (showChat) {
			if (clientSize == 0) {
				cacheSprite[0].drawSprite(0 + offsetX, 0 + offsetY);
			} else {
				cacheSprite[88].drawARGBSprite(7 + offsetX, 7 + offsetY);
			}
		}
		drawChannelButtons(offsetX, offsetY);
		if (showInput) {
			cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
			newBoldFont.drawCenteredString(promptMessage, 259 + offsetX,
					60 + offsetY, 0, -1);
			newBoldFont.drawCenteredString(promptInput + "*", 259 + offsetX,
					80 + offsetY, 128, -1);
		} else if (inputDialogState == 1) {
			cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
			newBoldFont.drawCenteredString("Enter amount:", 259 + offsetX,
					60 + offsetY, 0, -1);
			newBoldFont.drawCenteredString(amountOrNameInput + "*",
					259 + offsetX, 80 + offsetY, 128, -1);
		} else if (inputDialogState == 2) {
			cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
			newBoldFont.drawCenteredString("Enter name:", 259 + offsetX,
					60 + offsetY, 0, -1);
			newBoldFont.drawCenteredString(amountOrNameInput + "*",
					259 + offsetX, 80 + offsetY, 128, -1);
		} else if (inputDialogState == 3) {

			displayItemSearch();
		} else if (inputDialogState == 5) {
			cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
			String s = "";
			if (playerCommand != 0) {
				s = PLRCOMMANDS[playerCommand - 1][0];
			}
			newBoldFont.drawCenteredString(s, 259 + offsetX, 60 + offsetY, 0,
					-1);
			newBoldFont.drawCenteredString(amountOrNameInput + "*",
					259 + offsetX, 80 + offsetY, 128, -1);
		} else if (notifyMessage != null) {
			cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
			newBoldFont.drawCenteredString(notifyMessage, 259 + offsetX,
					60 + offsetY, 0, -1);
			newBoldFont.drawCenteredString("Click to continue", 259 + offsetX,
					80 + offsetY, 128, -1);
		} else if (backDialogID != -1) {
			cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
			drawInterface(0, 20 + offsetX,
					RSInterface.interfaceCache[backDialogID], 20 + offsetY);
		} else if (dialogID != -1) {
			cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
			drawInterface(0, 20 + offsetX,
					RSInterface.interfaceCache[dialogID], 20 + offsetY);
		} else if (!quickChat && showChat) {
			int messageY = -3;
			int scrollPosition = 0;
			DrawingArea.setDrawingArea(121 + offsetY, 8 + offsetX, 512 + offsetX, 7 + offsetY);
			for (int index = 0; index < 500; index++)
				if (chatMessages[index] != null) {
					int chatType = chatTypes[index];
					int positionY = (70 - messageY * 14) + chatScrollAmount + 6;
					String name = chatNames[index];
					String prefixName = name;
					final String time = "[" + "" + "]";
					int playerRights = -1;
					if (name != null && name.indexOf("<img=") == 0) {
						//System.out.println("Name before: " + name);
						name = name.substring(7);
						//System.out.println("Name after: " + name);
						String name1 = prefixName.substring(5);
						playerRights = getPrefixRightsNew(prefixName.substring(0, prefixName.indexOf(name1) + 2));
					}

					boolean glow = false;
					int gloColor = 16722474;

					if (name != null && name.startsWith("@glo")) {
						gloColor = Integer.parseInt(name.substring(4, 6));
						name = name.substring(7);
						glow = true;
					}
					if (chatType == 0) {
						if (chatTypeView == 5 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210) {
								int xPos = 11;
								newRegularFont.drawBasicString(
										chatMessages[index], xPos + offsetX,
										positionY + offsetY,
										clientSize == 0 ? 0 : 0xffffff,
										clientSize == 0 ? -1 : 0);
							}
							scrollPosition++;
							messageY++;
						}
					}
					if ((chatType == 1 || chatType == 2)
							&& (chatType == 1 || publicChatMode == 0 || publicChatMode == 1
									&& isFriendOrSelf(name))) {
						if (chatTypeView == 1 || chatTypeView == 0
								|| (playerRights >= 0 && playerRights <= 4)) {
							if (positionY > 0 && positionY < 210) {
								int xPos = 11;
								if (timeStamp) {
									newRegularFont.drawBasicString(time, xPos
											+ offsetX, positionY + offsetY,
											clientSize == 0 ? 0 : 0xffffff,
											clientSize == 0 ? -1 : 0);
									xPos += newRegularFont.getTextWidth(time);
								}
								if (playerRights >= 0 && playerRights != 128) {
									modIcons[playerRights].drawSprite(xPos + 1 + offsetX, positionY - 11 + offsetY);
									xPos += 14;
								}
								if (gloColor == 10)
									gloColor = 5614335;
								if (gloColor == 11)
									gloColor = 2785279;
								if (gloColor == 12)
									gloColor = 006633;
								if (gloColor == 13)
									gloColor = 65280;
								if (gloColor == 14)
									gloColor = 11206612;
								if (gloColor == 15)
									gloColor = 418308;
								if (gloColor == 16)
									gloColor = 10909941;
								if (gloColor == 17)
									gloColor = 16755285;
								if (gloColor == 18)
									gloColor = 16486436;
								if (gloColor == 19)
									gloColor = 16776960;
								if (gloColor == 20)
									gloColor = 16722474;
								if (gloColor == 21)
									gloColor = 16733652;
								if (gloColor == 22)
									gloColor = 16722687;
								if (gloColor == 23)
									gloColor = 11141375;
								if (gloColor == 24)
									gloColor = 000000;
								if (gloColor == 25)
									gloColor = 16777215;
								if (gloColor == 26)
									gloColor = 13290186;
								if (gloColor == 27)
									gloColor = 5614335;
								if (glow) {
									String toRemove = "";
									try {
										toRemove = name.substring(
												name.indexOf("<col=13132800>"),
												name.indexOf("</col> "));
									} catch (Exception e) {
									}
									int tempX = newRegularFont.getTextWidth(toRemove);
									newRegularFont.drawBasicString(name.replaceAll(toRemove, ""), xPos
													- 1 + offsetX + tempX, positionY + 1 + offsetY, gloColor, -1);
								}
								newRegularFont.drawBasicString(name + ":", xPos
										+ offsetX, positionY + offsetY,
										clientSize == 0 ? 0 : 0xffffff,
										clientSize == 0 ? -1 : 0);
								xPos += newRegularFont.getTextWidth(name) + 7;
								newRegularFont.drawBasicString(
										chatMessages[index], xPos + offsetX,
										positionY + offsetY,
										clientSize == 0 ? 255 : 0x7FA9FF,
										clientSize == 0 ? -1 : 0);
							}
							scrollPosition++;
							messageY++;
						}
					}
					if ((chatType == 3 || chatType == 7)
							&& (splitPrivateChat == 0 || chatTypeView == 2)
							&& (chatType == 7 || privateChatMode == 0 || privateChatMode == 1
									&& isFriendOrSelf(name))) {
						if (chatTypeView == 2 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210) {
								int xPos = 11;
								if (timeStamp) {
									newRegularFont.drawBasicString(time, xPos
											+ offsetX, positionY + offsetY,
											clientSize == 0 ? 0 : 0xffffff,
											clientSize == 0 ? -1 : 0);
									xPos += newRegularFont.getTextWidth(time);
								}
								newRegularFont.drawBasicString("From", xPos
										+ offsetX, positionY + offsetY,
										clientSize == 0 ? 0 : 0xffffff,
										clientSize == 0 ? -1 : 0);
								xPos += newRegularFont.getTextWidth("From ");
								if (playerRights >= 0) {
									modIcons[playerRights].drawSprite(xPos + 1 + offsetX, positionY - 11 + offsetY);
									xPos += 14;
								}
								newRegularFont.drawBasicString(name + ":", xPos
										+ offsetX, positionY + offsetY,
										clientSize == 0 ? 0 : 0xffffff,
										clientSize == 0 ? -1 : 0);
								xPos += newRegularFont.getTextWidth(name) + 8;
								newRegularFont.drawBasicString(chatMessages[index], xPos + offsetX,
										positionY + offsetY,
										clientSize == 0 ? 0x800000 : 0xFF5256,
										clientSize == 0 ? -1 : 0);
							}
							scrollPosition++;
							messageY++;
						}
					}
					if (chatType == 4
							&& (tradeMode == 0 || tradeMode == 1
									&& isFriendOrSelf(name))) {
						if (chatTypeView == 3 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210) {
								newRegularFont.drawBasicString(name + " "
										+ chatMessages[index], 11 + offsetX,
										positionY + offsetY,
										clientSize == 0 ? 0x800080 : 0xFF00D4,
										clientSize == 0 ? -1 : 0);
							}
							scrollPosition++;
							messageY++;
						}
					}
					if (chatType == 5 && splitPrivateChat == 0
							&& privateChatMode < 2) {
						if (chatTypeView == 2 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210)
								newRegularFont.drawBasicString(
										chatMessages[index], 11 + offsetX,
										positionY + offsetY,
										clientSize == 0 ? 0x800000 : 0xFF5256,
										clientSize == 0 ? -1 : 0);
							scrollPosition++;
							messageY++;
						}
					}
					if (chatType == 6
							&& (splitPrivateChat == 0 || chatTypeView == 2)
							&& privateChatMode < 2) {
						if (chatTypeView == 2 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210) {
								newRegularFont.drawBasicString("To " + name
										+ ":", 11 + offsetX, positionY
										+ offsetY, clientSize == 0 ? 0
										: 0xffffff, clientSize == 0 ? -1 : 0);
								newRegularFont.drawBasicString(
										chatMessages[index],
										15
												+ newRegularFont
														.getTextWidth("To :"
																+ name)
												+ offsetX + offsetX, positionY
												+ offsetY,
										clientSize == 0 ? 0x800000 : 0xFF5256,
										clientSize == 0 ? -1 : 0);
							}
							scrollPosition++;
							messageY++;
						}
					}
					if (chatType == 8
							&& (tradeMode == 0 || tradeMode == 1
									&& isFriendOrSelf(name))) {
						if (chatTypeView == 3 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210)
								textDrawingArea.drawString(0x7e3200, name + " "
										+ chatMessages[index], positionY
										+ offsetY, 11 + offsetX);
							scrollPosition++;
							messageY++;
						}
					}
					if (chatType == 16) {
						if (chatTypeView == 11 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210) {
								int positionX = 11;
								String title = (clientSize == 0 ? "<col=0000FF>" : "<col=7FA9FF>") + clanname + "</col>";
								String username = (chatRights[index] > 0 ? "<img=" + (chatRights[index]) + ">" : "") + capitalize(chatNames[index]);
								String message = (clientSize == 0 ? "<col=800000>" : "<col=FF5256>") + chatMessages[index] + "</col>";
								//System.out.println(username + " - " + message);
								newRegularFont.drawBasicString("[" + title + "] " + username + ": " + message,
										positionX, positionY + offsetY,
										clientSize == 0 ? 0 : 0xffffff,
										clientSize == 0 ? -1 : 0);
							}
							scrollPosition++;
							messageY++;
						}
					}
					if (chatType == 17) {
						if (chatTypeView == 3 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210)
								textDrawingArea.drawString(0xFF7519, name + " " + chatMessages[index], positionY + offsetY, 11 + offsetX);
							scrollPosition++;
							messageY++;
						}
					}
					if (chatType == 18) {
						if (chatTypeView == 3 || chatTypeView == 0) {
							if (positionY > 0 && positionY < 210)
								textDrawingArea.drawString(0x8D1919, name + " " + chatMessages[index], positionY + offsetY, 11 + offsetX);
							scrollPosition++;
							messageY++;
						}
					}
				}
			DrawingArea.defaultDrawingAreaSize();
			chatScrollHeight = scrollPosition * 14 + 7 + 5;
			if (chatScrollHeight < 111) {
				chatScrollHeight = 111;
			}
			drawScrollbar(114, chatScrollHeight - chatScrollAmount - 113, 7 + offsetY, 496 + offsetX, chatScrollHeight, false, clientSize != 0);
			String name;
			if (myPlayer != null && myPlayer.name != null) {
				name = myPlayer.name;
			} else {
				name = TextClass.fixName(myUsername);
			}
			if (myCrown > 0) {
				modIcons[myCrown].drawSprite(12 + offsetX, 122 + offsetY);
				offsetX += 14;
			}
			if (muteReason.length() > 0) {
				textDrawingArea.drawShadowedString(clientSize == 0 ? false : true,
						11 + offsetX, clientSize == 0 ? 0 : 0xffffff,
						"You are currently muted. Reason: " + muteReason + ".",
						133 + offsetY);
			} else {
				textDrawingArea.drawShadowedString(clientSize == 0 ? false : true,
						11 + offsetX, clientSize == 0 ? 0 : 0xffffff, name, 133 + offsetY);
				cacheSprite[14].drawSprite(textDrawingArea.getStringEffectsWidth(name) + 11 + offsetX, 123 + offsetY);
				textDrawingArea.drawShadowedString(clientSize == 0 ? false : true,
						textDrawingArea.getStringEffectsWidth(name) + 24 + offsetX,
						clientSize == 0 ? 0 : 0xffffff, ": ", 133 + offsetY);
				// newRegularFont.drawRAString(inputString + "*", 24 +
				// newRegularFont.getTextWidth(s + ": ") + xPosOffset, 133 +
				// yPosOffset, clientSize == 0 ? 255 : 0x7FA9FF, clientSize == 0
				// ? -1 : 0);
				String toDraw = "";
				try {
					String loopSeperator = (((loopCycle % 40 < 20 ? 1 : 0) != 0) ? "|" : "");
					int width = newRegularFont.getTextWidth(inputString);
					toDraw = inputString.substring(width > 420 ? ((width - 420) / 5) : 0, inputStringPos) + "" + loopSeperator + "" + inputString.substring(inputStringPos);
					newRegularFont.drawBasicString(toDraw, 24
							+ newRegularFont.getTextWidth(name + ": ") + offsetX,
							133 + offsetY, clientSize == 0 ? 255 : 0x7FA9FF, clientSize == 0 ? -1 : 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (clientSize == 0)
				DrawingArea.drawHorizontalLine(121 + offsetY, clientSize == 0 ? 0x807660 : 0xffffff, 505, 7);
		} else if (quickChat) {
			cacheSprite[64].drawSprite(0 + offsetX, 0 + offsetY);
			displayQuickChat(offsetX, offsetY);
		}
		/** Texture Debugging **/
		if(textureImage != null) {
			//System.out.println("Debugging Texture.");
			textureImage.drawAdvancedSprite(332, 5);
		}
		if (menuOpen && menuScreenArea == 2) {
			drawMenu();
		}
		if (clientSize == 0) {
			chatAreaIP.drawGraphics(338, super.graphics, 0);
		}
		gameScreenIP.initDrawingArea();
		Rasterizer.lineOffsets = viewportOffsets;
	}

	public int channel;
	public boolean showChat = true;
	public boolean timeStamp = false;
	public String mutedBy = "";
	public String muteReason = "";
	public int chatColor = 0;
	public int chatEffect = 0;

	/**
	 * quickChat: is quick chat open? canTalk: can player submit text(type in
	 * the chatbox)? quickHoverY: hover position of the green box.
	 **/
	public boolean quickChat = false, canTalk = true, divideSelections = false,
			divideSelectedSelections = false;
	public int quickSelY = -1, quickSelY2 = -1, quickHoverY = -1,
			quickHoverY2 = -1, shownSelection = -1,
			shownSelectedSelection = -1;
	public String quickChatDir = "Quick Chat";
	public int quickHOffsetX = shownSelection != -1 ? 110 : 0;

	public void openQuickChat() {
		resetQuickChat();
		quickChat = true;
		canTalk = false;
		inputTaken = true;
	}

	public void resetQuickChat() {
		divideSelections = false;
		divideSelectedSelections = false;
		shownSelection = -1;
		shownSelectedSelection = -1;
		quickSelY = -1;
		quickSelY2 = -1;
		quickHoverY = -1;
		quickHoverY2 = -1;
	}

	/**
	 * Draws the quick chat interface.
	 **/
	public void displayQuickChat(int x, int y) {
		String[] shortcutKey = { "G", "T", "S", "E", "C", "M", "Enter" };
		String[] name = { "General", "Trade/Items", "Skills", "Group Events",
				"Clans", "Inter-game", "I'm muted." };
		cacheSprite[65].drawSprite(7 + x, 7 + y);
		if (cButtonHPos == 8) {
			cacheSprite[66].drawSprite(7 + x, 7 + y);
		}
		DrawingArea.drawPixels(2, 23 + y, 7 + x, 0x847963, 506);
		if (divideSelections) {
			DrawingArea.drawPixels(111, 25 + y, 116 + x, 0x847963, 2);
		}
		if (divideSelectedSelections) {
			DrawingArea.drawPixels(111, 25 + y, 116 + 158 + x, 0x847963, 2);
		}
		normalFont.drawShadowedString(false, 45 + x, 255, quickChatDir, 20 + y);
		if (quickHoverY != -1 && shownSelection == -1 && quickHOffsetX == 0) {
			DrawingArea.drawPixels(14, quickHoverY + y, 7 + x, 0x577E45, 109);
		} else if (quickHoverY != -1 && shownSelection != -1
				&& quickHOffsetX == 0) {
			DrawingArea.drawPixels(14, quickHoverY + y, 7 + x, 0x969777, 109);
		}
		/**
		 * Hovering over text on selected->selections.
		 **/
		if (quickHoverY2 != -1 && shownSelectedSelection == -1
				&& quickHOffsetX == 0) {
			DrawingArea.drawPixels(14, quickHoverY2 + y, 118 + 159 + x,
					0x577E45, 109);
		} else if (quickHoverY2 != -1 && shownSelectedSelection != -1
				&& quickHOffsetX == 0) {
			DrawingArea.drawPixels(14, quickHoverY2 + y, 118 + 159 + x,
					0x969777, 109);
		}
		if (quickSelY != -1) {
			DrawingArea.drawPixels(14, quickSelY + y, 7 + x, 0x969777, 109);
		}
		if (quickSelY2 != -1) {
			DrawingArea.drawPixels(14, quickSelY2 + y, 118 + x, 0x969777, 156);
		}
		for (int i1 = 0, y2 = 36; i1 < name.length; i1++, y2 += 14) {
			normalFont.drawShadowedString(false, 10 + x, 0x555555, shortcutKey[i1]
					+ ".", y + y2);
			if (i1 == name.length - 1)
				normalFont
						.drawShadowedString(
								false,
								12
										+ x
										+ normalFont
												.getStringEffectsWidth(shortcutKey[i1]
														+ "."), 0, name[i1], y
										+ y2);
			else
				normalFont
						.drawShadowedString(
								false,
								12
										+ x
										+ normalFont
												.getStringEffectsWidth(shortcutKey[i1]
														+ "."), 0, name[i1]
										+ " ->", y + y2);
		}
		if (shownSelection != -1) {
			showSelections(shownSelection, x, y);
		}
		if (shownSelectedSelection != -1) {
			showSelectedSelections(shownSelectedSelection, x, y);
		}
	}

	public void showSelections(int selections, int x, int y) {
		switch (selections) {
		case 0:
			String[] keys1 = { "R", "H", "G", "C", "S", "M", "B", "P" };
			String[] names1 = { "Responses", "Hello", "Goodbye", "Comments",
					"Smilies", "Mood", "Banter", "Player vs Player" };
			if (quickHoverY != -1 && quickHOffsetX == 110)
				DrawingArea.drawPixels(14, quickHoverY + y, 118 + x, 0x577E45,
						156);
			for (int i1 = 0, y2 = 36; i1 < names1.length; i1++, y2 += 14) {
				normalFont.drawShadowedString(false, 118 + x, 0x555555, keys1[i1]
						+ ".", y + y2);
				normalFont.drawShadowedString(false,
						120 + x + normalFont.getStringEffectsWidth(keys1[i1] + "."), 0,
						names1[i1] + " ->", y + y2);
			}
			break;
		case 1:
			String[] keys2 = { "T", "I" };
			String[] names2 = { "Trade", "Items" };
			if (quickHoverY != -1 && quickHoverY < 53 && quickHOffsetX == 110)
				DrawingArea.drawPixels(14, quickHoverY + y, 118 + x, 0x577E45,
						101);
			for (int i2 = 0, y2 = 36; i2 < names2.length; i2++, y2 += 14) {
				normalFont.drawShadowedString(false, 118 + x, 0x555555, keys2[i2]
						+ ".", y + y2);
				normalFont.drawShadowedString(false,
						120 + x + normalFont.getStringEffectsWidth(keys2[i2] + "."), 0,
						names2[i2] + " ->", y + y2);
			}
			break;

		default:
			break;
		}
	}

	public void showSelectedSelections(int selections, int x, int y) {
		switch (selections) {
		case 0:
			String[] keys1 = { "1", "2", "3", "4", "5" };
			String[] names1 = { "Hi.", "Hey!", "Sup?", "Hello.", "Yo dawg." };
			if (quickHoverY2 != -1 && quickHOffsetX == 269)
				DrawingArea.drawPixels(14, quickHoverY2 + y, 118 + 158 + x,
						0x577E45, 156);
			for (int i1 = 0, y2 = 36; i1 < names1.length; i1++, y2 += 14) {
				normalFont.drawShadowedString(false, 118 + 159 + x, 0x555555,
						keys1[i1] + ".", y + y2);
				normalFont.drawShadowedString(
						false,
						120 + 159 + x
								+ normalFont.getStringEffectsWidth(keys1[i1] + "."), 0,
						names1[i1], y + y2);
			}
			break;

		default:
			break;
		}
	}

	public void processQuickChatArea() {
		int y = clientHeight - 503;
		if (super.mouseX < 117 && super.mouseY > 363) {
			quickHOffsetX = 0;
			quickHoverY2 = -1;
		} else if (super.mouseX > 117 && super.mouseX < 117 + 158
				&& super.mouseY > 363) {
			quickHOffsetX = 110;
			quickHoverY2 = -1;
		} else {
			quickHOffsetX = 269;
			quickHoverY2 = quickHoverY;
		}
		if (super.mouseX >= 7 && super.mouseX <= 23 && super.mouseY >= y + 345
				&& super.mouseY <= y + 361) {
			cButtonHPos = 8;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 364 && super.mouseY <= y + 377) {
			quickHoverY = 25;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 378 && super.mouseY <= y + 391) {
			quickHoverY = 39;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 392 && super.mouseY <= y + 405) {
			quickHoverY = 53;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 406 && super.mouseY <= y + 419) {
			quickHoverY = 67;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 420 && super.mouseY <= y + 433) {
			quickHoverY = 81;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 434 && super.mouseY <= y + 447) {
			quickHoverY = 95;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 448 && super.mouseY <= y + 461) {
			quickHoverY = 109;
			inputTaken = true;
		} else if (super.mouseX >= 8 + quickHOffsetX
				&& super.mouseX <= 117 + quickHOffsetX
				&& super.mouseY >= y + 462 && super.mouseY <= y + 474
				&& shownSelection == 0) {
			quickHoverY = 123;
			inputTaken = true;
		} else {
			quickHoverY = -1;
			quickHoverY2 = -1;
			inputTaken = true;
		}
		if (super.clickMode3 == 1) {
			if (super.saveClickX >= 8 && super.saveClickX <= 117
					&& super.saveClickY >= y + 364
					&& super.saveClickY <= y + 377) {
				setSelection(25, "Quick Chat @bla@-> @blu@General", 0);
			} else if (super.saveClickX >= 8 && super.saveClickX <= 117
					&& super.saveClickY >= y + 378
					&& super.saveClickY <= y + 391) {
				setSelection(39, "Quick Chat @bla@-> @blu@Trade/Items", 1);
			} else if (clickInRegion(118, clientHeight - 126, 118 + 156,
					clientHeight - 113)) {
				if (shownSelection == 0) {
					setSelectedSelection(
							25,
							39,
							"Quick Chat @bla@-> @blu@General @bla@-> @blu@Hello",
							0);
				}
			} else if (clickInRegion(277, clientHeight - 140, 277 + 156,
					clientHeight - 126)) {
				if (shownSelectedSelection == 0) {
					quickSay("Hi.");
					return;
				}
			} else if (clickInRegion(277, clientHeight - 126, 277 + 156,
					clientHeight - 112)) {
				if (shownSelectedSelection == 0) {
					quickSay("Hey!");
					return;
				}
			} else if (clickInRegion(277, clientHeight - 112, 277 + 156,
					clientHeight - 98)) {
				if (shownSelectedSelection == 0) {
					quickSay("Sup?");
					return;
				}
			} else if (clickInRegion(277, clientHeight - 98, 277 + 156,
					clientHeight - 84)) {
				if (shownSelectedSelection == 0) {
					quickSay("Hello.");
					return;
				}
			} else if (clickInRegion(277, clientHeight - 84, 277 + 156,
					clientHeight - 70)) {
				if (shownSelectedSelection == 0) {
					quickSay("Yo dawg.");
					return;
				}
			} else if (clickInRegion(7, clientHeight - 56, 116,
					clientHeight - 42)) {
				quickSay("I'm muted and I can only use quick chat.");
				return;
			} else {
				inputTaken = true;
			}
		}
	}

	public void setSelection(int y, String directory, int selection) {
		quickSelY = y;
		quickSelY2 = -1;
		divideSelections = true;
		divideSelectedSelections = false;
		quickChatDir = directory;
		shownSelection = selection;
		shownSelectedSelection = -1;
		inputTaken = true;
	}

	public void setSelectedSelection(int y1, int y2, String directory,
			int selectedSelection) {
		divideSelections = true;
		divideSelectedSelections = true;
		quickSelY = y1;
		quickSelY2 = y2;
		quickChatDir = directory;
		shownSelectedSelection = selectedSelection;
		inputTaken = true;
	}

	public void quickSay(String text) {
		say(text, true);
		isQuickChat = true;
		resetQuickChat();
		quickChat = false;
		canTalk = true;
		inputTaken = true;
	}

	public boolean isQuickChat = false;

	public void say(String text, boolean isQuickSay) {
		isQuickChat = true;
		stream.createFrame(4);
		stream.writeByte(0);
		int j3 = stream.currentOffset;
		stream.writeByteS(chatEffect);
		stream.writeByteS(chatColor);
		textStream.currentOffset = 0;
		TextInput.appendToStream(text, textStream);
		stream.method441(0, textStream.buffer, textStream.currentOffset);
		stream.writeBytes(stream.currentOffset - j3);
		text = TextInput.processText(text);
		myPlayer.textSpoken = text;
		myPlayer.chatColor = chatColor;
		myPlayer.chatEffect = chatEffect;
		myPlayer.textCycle = 150;
		System.out.println("Your crown: " + myCrown);
		pushMessage(myPlayer.textSpoken, 2, getPrefix(myCrown)
				+ (glowColor != 0 ? "@glo" + glowColor + "@" : "")
				+ myPlayer.name + "" + (isQuickSay ? "<img=8>" : ""));
		if (publicChatMode == 2) {
			publicChatMode = 3;
			stream.createFrame(95);
			stream.writeByte(publicChatMode);
			stream.writeByte(privateChatMode);
			stream.writeByte(tradeMode);
		}
	}

	public boolean filterMessages = false;
	public String[] filteredMessages = { "You catch a",
			"You successfully cook the", "You accidentally burn the",
			"You manage to get", "You get some" };// add
													// more

	public void drawChannelButtons(int xPosOffset, int yPosOffset) {
		cacheSprite[5].drawSprite(5 + xPosOffset, 142 + yPosOffset);
		String text[] = { "On", "Friends", "Off", "Hide", "Filter", "All" };
		int textColor[] = { 65280, 0xffff00, 0xff0000, 65535, 0xffff00, 65280 };
		int[] x = { 5, 62, 119, 176, 233, 290, 347, 404 };
		switch (cButtonCPos) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
			cacheSprite[2].drawSprite(x[cButtonCPos] + xPosOffset,
					142 + yPosOffset);
			break;
		}
		if (cButtonHPos == cButtonCPos) {
			switch (cButtonHPos) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				cacheSprite[3].drawSprite(x[cButtonHPos] + xPosOffset,
						142 + yPosOffset);
				break;
			case 7:
				cacheSprite[4].drawSprite(x[cButtonHPos] + xPosOffset,
						142 + yPosOffset);
				break;
			}
		} else {
			switch (cButtonHPos) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				cacheSprite[1].drawSprite(x[cButtonHPos] + xPosOffset,
						142 + yPosOffset);
				break;
			case 7:
				cacheSprite[4].drawSprite(x[cButtonHPos] + xPosOffset,
						142 + yPosOffset);
				break;
			}
		}
		smallText.drawStringCenter(0xffffff, x[0] + 28 + xPosOffset, "All",
				157 + yPosOffset, true);
		smallText.drawStringCenter(0xffffff, x[1] + 28 + xPosOffset, "Game",
				152 + yPosOffset, true);
		smallText.drawStringCenter(0xffffff, x[2] + 28 + xPosOffset, "Public",
				152 + yPosOffset, true);
		smallText.drawStringCenter(0xffffff, x[3] + 28 + xPosOffset, "Private",
				152 + yPosOffset, true);
		smallText.drawStringCenter(0xffffff, x[4] + 28 + xPosOffset, "Clan",
				152 + yPosOffset, true);
		smallText.drawStringCenter(0xffffff, x[5] + 28 + xPosOffset, "Trade",
				152 + yPosOffset, true);
		smallText.drawStringCenter(0xffffff, x[6] + 28 + xPosOffset, "Yell",
				152 + yPosOffset, true);
		smallText.drawStringCenter(0xffffff, x[7] + 55 + xPosOffset,
				"Report A Bug", 157 + yPosOffset, true);
		smallText.drawStringCenter(textColor[gameChatMode],
				62 + 28 + xPosOffset, text[gameChatMode], 163 + yPosOffset,
				true);
		smallText.drawStringCenter(textColor[publicChatMode], x[2] + 28
				+ xPosOffset, text[publicChatMode], 163 + yPosOffset, true);
		smallText.drawStringCenter(textColor[privateChatMode], x[3] + 28
				+ xPosOffset, text[privateChatMode], 163 + yPosOffset, true);
		smallText.drawStringCenter(textColor[clanChatMode], x[4] + 28
				+ xPosOffset, text[clanChatMode], 163 + yPosOffset, true);
		smallText.drawStringCenter(textColor[tradeMode], x[5] + 28 + xPosOffset,
						text[tradeMode], 163 + yPosOffset, true);
		 smallText.drawStringCenter(textColor[yellMode], x[6] + 28 + xPosOffset, text[yellMode], 163 + yPosOffset, true);
	}

	private void processChatModeClick() {
		int[] x = { 5, 62, 119, 176, 233, 290, 347, 404 };
		if (super.mouseX >= x[0] && super.mouseX <= x[0] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 0;
			inputTaken = true;
		} else if (super.mouseX >= x[1] && super.mouseX <= x[1] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 1;
			inputTaken = true;
		} else if (super.mouseX >= x[2] && super.mouseX <= x[2] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 2;
			inputTaken = true;
		} else if (super.mouseX >= x[3] && super.mouseX <= x[3] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 3;
			inputTaken = true;
		} else if (super.mouseX >= x[4] && super.mouseX <= x[4] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 4;
			inputTaken = true;
		} else if (super.mouseX >= x[5] && super.mouseX <= x[5] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 5;
			inputTaken = true;
		} else if (super.mouseX >= x[6] && super.mouseX <= x[6] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 6;
			inputTaken = true;
		} else if (super.mouseX >= x[7] && super.mouseX <= x[7] + 111
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			cButtonHPos = 7;
			inputTaken = true;
		} else {
			cButtonHPos = -1;
			inputTaken = true;
		}
		if (super.clickMode3 == 1) {
			if (super.saveClickX >= x[0] && super.saveClickX <= x[0] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 0) {
						cButtonCPos = 0;
						chatTypeView = 0;
						inputTaken = true;
						channel = 0;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 0;
					chatTypeView = 0;
					inputTaken = true;
					channel = 0;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(privateChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[1]
					&& super.saveClickX <= x[1] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 1 && clientSize != 0) {
						cButtonCPos = 1;
						chatTypeView = 5;
						inputTaken = true;
						channel = 1;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 1;
					chatTypeView = 5;
					inputTaken = true;
					channel = 1;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(privateChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[2]
					&& super.saveClickX <= x[2] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 2 && clientSize != 0) {
						cButtonCPos = 2;
						chatTypeView = 1;
						inputTaken = true;
						channel = 2;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 2;
					chatTypeView = 1;
					inputTaken = true;
					channel = 2;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(privateChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[3]
					&& super.saveClickX <= x[3] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 3 && clientSize != 0) {
						cButtonCPos = 3;
						chatTypeView = 2;
						inputTaken = true;
						channel = 3;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 3;
					chatTypeView = 2;
					inputTaken = true;
					channel = 3;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(privateChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[4]
					&& super.saveClickX <= x[4] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 4 && clientSize != 0) {
						cButtonCPos = 4;
						chatTypeView = 11;
						inputTaken = true;
						channel = 4;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 4;
					chatTypeView = 11;
					inputTaken = true;
					channel = 4;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(privateChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[5]
					&& super.saveClickX <= x[5] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 5 && clientSize != 0) {
						cButtonCPos = 5;
						chatTypeView = 3;
						inputTaken = true;
						channel = 5;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 5;
					chatTypeView = 3;
					inputTaken = true;
					channel = 5;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(privateChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= x[6]
					&& super.saveClickX <= x[6] + 56
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				if (clientSize != 0) {
					if (channel != 6 && clientSize != 0) {
						cButtonCPos = 6;
						chatTypeView = 6;
						inputTaken = true;
						channel = 6;
					} else {
						showChat = showChat ? false : true;
					}
				} else {
					cButtonCPos = 6;
					chatTypeView = 6;
					inputTaken = true;
					channel = 6;
				}
				stream.createFrame(95);
				stream.writeByte(publicChatMode);
				stream.writeByte(privateChatMode);
				stream.writeByte(tradeMode);
			} else if (super.saveClickX >= 404 && super.saveClickX <= 515
					&& super.saveClickY >= clientHeight - 23
					&& super.saveClickY <= clientHeight) {
				/*if (openInterfaceID == -1) {
					clearTopInterfaces();
					// CustomUserInput.input = "";
					reportAbuseInput = "";
					canMute = false;
					for (int i = 0; i < RSInterface.interfaceCache.length; i++) {
						if (RSInterface.interfaceCache[i] == null
								|| RSInterface.interfaceCache[i].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i].parentID;
						break;
					}
				} else {
					pushMessage( "Please close the interface you have open before using 'report abuse'", 0, "");
				}*/
				launchURL("http://revolutionxpk.org/forum/index.php?/forum/124-report-a-glitchbug/");
			}
			if (!showChat) {
				cButtonCPos = -1;
			}
		}
	}

	private void rightClickChatButtons() {
		int y = clientHeight - 503;
		int[] x = { 5, 62, 119, 176, 233, 290, 347, 404 };
		if (super.mouseX >= 7 && super.mouseX <= 23 && super.mouseY >= y + 345
				&& super.mouseY <= y + 361) {
			if (quickChat) {
				menuActionName[1] = "Close";
				menuActionID[1] = 1004;
				menuActionRow = 2;
			}
		} else if (super.mouseX >= 7
				&& super.mouseX <= newRegularFont.getTextWidth(myUsername) + 24
				&& super.mouseY >= clientHeight - 43
				&& super.mouseY <= clientHeight - 31) {
			if (!quickChat) {
				menuActionName[1] = "Open quickchat";
				menuActionID[1] = 1005;
				menuActionRow = 2;
			}
		}
		if (super.mouseX >= x[0] && super.mouseX <= x[0] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "View All";
			menuActionID[1] = 999;
			menuActionRow = 2;
		} else if (super.mouseX >= x[1] && super.mouseX <= x[1] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Filter Game";
			menuActionID[1] = 798;
			menuActionName[2] = "All Game";
			menuActionID[2] = 797;
			menuActionName[3] = "View Game";
			menuActionID[3] = 998;
			menuActionRow = 4;
		} else if (super.mouseX >= x[2] && super.mouseX <= x[2] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Hide Public";
			menuActionID[1] = 997;
			menuActionName[2] = "Off Public";
			menuActionID[2] = 996;
			menuActionName[3] = "Friends Public";
			menuActionID[3] = 995;
			menuActionName[4] = "On Public";
			menuActionID[4] = 994;
			menuActionName[5] = "View Public";
			menuActionID[5] = 993;
			menuActionRow = 6;
		} else if (super.mouseX >= x[3] && super.mouseX <= x[3] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Off Private";
			menuActionID[1] = 992;
			menuActionName[2] = "Friends Private";
			menuActionID[2] = 991;
			menuActionName[3] = "On Private";
			menuActionID[3] = 990;
			menuActionName[4] = "View Private";
			menuActionID[4] = 989;
			menuActionRow = 5;
		} else if (super.mouseX >= x[4] && super.mouseX <= x[4] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Off Clan chat";
			menuActionID[1] = 1003;
			menuActionName[2] = "Friends Clan chat";
			menuActionID[2] = 1002;
			menuActionName[3] = "On Clan chat";
			menuActionID[3] = 1001;
			menuActionName[4] = "View Clan chat";
			menuActionID[4] = 1000;
			menuActionRow = 5;
		} else if (super.mouseX >= x[5] && super.mouseX <= x[5] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Off Trade";
			menuActionID[1] = 987;
			menuActionName[2] = "Friends Trade";
			menuActionID[2] = 986;
			menuActionName[3] = "On Trade";
			menuActionID[3] = 985;
			menuActionName[4] = "View Trade";
			menuActionID[4] = 984;
			menuActionRow = 5;
		} else if (super.mouseX >= x[6] && super.mouseX <= x[6] + 56
				&& super.mouseY >= clientHeight - 23
				&& super.mouseY <= clientHeight) {
			menuActionName[1] = "Hide Yell";
			menuActionID[1] = 983;
			menuActionName[2] = "Off Yell";
			menuActionID[2] = 982;
			menuActionName[3] = "Friends Yell";
			menuActionID[3] = 981;
			menuActionName[4] = "On Yell";
			menuActionID[4] = 980;
			menuActionName[5] = "View Yell";
			menuActionID[5] = 979;
			menuActionRow = 6;
		}
	}

	public boolean canClick() {
		if (mouseInRegion(clientWidth - (clientWidth < smallTabs ? 240 : 480),
				clientHeight - (clientWidth < smallTabs ? 74 : 37),
				clientWidth, clientHeight)) {
			return false;
		}
		if (showChat || backDialogID != -1) {
			if (super.mouseX > 0 && super.mouseX < 519
					&& super.mouseY > clientHeight - 165
					&& super.mouseY < clientHeight
					|| super.mouseX > clientWidth - 220
					&& super.mouseX < clientWidth && super.mouseY > 0
					&& super.mouseY < 165) {
				return false;
			}
		}
		if (mouseInRegion2(clientWidth - 216, clientWidth, 0, 172)) {
			return false;
		}
		if (showTab) {
			if (clientWidth >= smallTabs) {
				if (super.mouseX >= clientWidth - 420
						&& super.mouseX <= clientWidth
						&& super.mouseY >= clientHeight - 37
						&& super.mouseY <= clientHeight
						|| super.mouseX > clientWidth - 204
						&& super.mouseX < clientWidth
						&& super.mouseY > clientHeight - 37 - 274
						&& super.mouseY < clientHeight)
					return false;
			} else {
				if (super.mouseX >= clientWidth - 210
						&& super.mouseX <= clientWidth
						&& super.mouseY >= clientHeight - 74
						&& super.mouseY <= clientHeight
						|| super.mouseX > clientWidth - 204
						&& super.mouseX < clientWidth
						&& super.mouseY > clientHeight - 74 - 274
						&& super.mouseY < clientHeight)
					return false;
			}
		}
		return true;
	}

	public int getOrbX(int orb) {
		switch (orb) {
		case 0:
			return clientSize != 0 ? clientWidth - 212 : 172;
		case 1:
			return clientSize != 0 ? clientWidth - 215 : 188;
		case 2:
			return clientSize != 0 ? clientWidth - 203 : 188;
		case 3:
			return clientSize != 0 ? clientWidth - 180 : 172;
		}
		return 0;
	}

	public int getOrbY(int orb) {
		switch (orb) {
		case 0:
			return clientSize != 0 ? 39 : 15;
		case 1:
			return clientSize != 0 ? 73 : 54;
		case 2:
			return clientSize != 0 ? 107 : 93;
		case 3:
			return clientSize != 0 ? 141 : 128;
		}
		return 0;
	}

	public double fillHP;

	public void drawHPOrb() {
		int currentHp = 0;
		try {
			currentHp = /*Integer.parseInt(RSInterface.interfaceCache[19001].message)*/currentStats[3];
		} catch (Exception e) {
		}
		int health = (int) (((double) currentHp / (double) currentMaxStats[3]) * 100D);
		int x = getOrbX(0);
		int y = getOrbY(0);
		if (orbTextEnabled[0]) {
			orbs[clientSize == 0 ? 0 : 11].drawSprite(x, y);
			if (health >= 75) {
				newSmallFont.drawCenteredString(Integer.toString(currentHp), x
						+ (clientSize == 0 ? 42 : 15), y + 26, 65280, 0);
			} else if (health <= 74 && health >= 50) {
				newSmallFont.drawCenteredString(Integer.toString(currentHp), x
						+ (clientSize == 0 ? 42 : 15), y + 26, 0xffff00, 0);
			} else if (health <= 49 && health >= 25) {
				newSmallFont.drawCenteredString(Integer.toString(currentHp), x
						+ (clientSize == 0 ? 42 : 15), y + 26, 0xfca607, 0);
			} else if (health <= 24 && health >= 0) {
				newSmallFont.drawCenteredString(Integer.toString(currentHp), x
						+ (clientSize == 0 ? 42 : 15), y + 26, 0xf50d0d, 0);
			}

		}
		orbs[2].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		double percent = (health / 100D);
		fillHP = 27 * percent;
		int depleteFill = 27 - (int) fillHP;
		orbs[1].myHeight = depleteFill;
		orbs[1].height = depleteFill;
		orbs[1].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		orbs[3].drawSprite(x + (clientSize == 0 ? 9 : 33), y + 11);
	}

	public double fillPrayer;
	private boolean prayClicked;
	private String prayerBook;

	public void drawPrayerOrb() {
		int currentPray = 0;
		int maxPray = 0;
		try {
			currentPray = /*Integer.parseInt(RSInterface.interfaceCache[4012].message)*/currentStats[5];
			maxPray = /*Integer.parseInt(RSInterface.interfaceCache[4013].message)*/currentMaxStats[5];
		} catch (Exception e) {
		}
		int prayer = (int) (((double) currentPray / (double) maxPray) * 100D);
		int x = getOrbX(1);
		int y = getOrbY(1);
		if (orbTextEnabled[1]) {
			orbs[clientSize == 0 ? (hoverPos == 1 ? 12 : 0)
					: (hoverPos == 1 ? 13 : 11)].drawSprite(x, y);
			if (prayer <= 100 && prayer >= 75) {
				newSmallFont.drawCenteredString(Integer.toString(currentPray),
						x + (clientSize == 0 ? 42 : 15), y + 26, 65280, 0);
			} else if (prayer <= 74 && prayer >= 50) {
				newSmallFont.drawCenteredString(Integer.toString(currentPray),
						x + (clientSize == 0 ? 42 : 15), y + 26, 0xffff00, 0);
			} else if (prayer <= 49 && prayer >= 25) {
				newSmallFont.drawCenteredString(Integer.toString(currentPray),
						x + (clientSize == 0 ? 42 : 15), y + 26, 0xfca607, 0);
			} else if (prayer <= 24 && prayer >= 0) {
				newSmallFont.drawCenteredString(Integer.toString(currentPray),
						x + (clientSize == 0 ? 42 : 15), y + 26, 0xf50d0d, 0);
			}
		}
		orbs[prayClicked ? 10 : 4].drawSprite(x + (clientSize == 0 ? 3 : 27),
				y + 3);
		double percent = (prayer / 100D);
		fillPrayer = 27 * percent;
		int depleteFill = 27 - (int) fillPrayer;
		orbs[17].myHeight = depleteFill;
		orbs[17].height = depleteFill;
		orbs[17].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		orbs[5].drawSprite(x + (clientSize == 0 ? 7 : 31), y + 7);
	}

	public double fillRun;
	public boolean running;
	public int currentEnergy;

	public void drawRunOrb() {
		int run = (int) (((double) currentEnergy / (double) 100) * 100D);
		int x = getOrbX(2);
		int y = getOrbY(2);
		if (orbTextEnabled[2]) {
			orbs[clientSize == 0 ? (hoverPos == 2 ? 12 : 0)
					: (hoverPos == 2 ? 13 : 11)].drawSprite(x, y);
			if (run <= 100 && run >= 75) {
				newSmallFont
						.drawCenteredString(Integer.toString(currentEnergy), x
								+ (clientSize == 0 ? 42 : 15), y + 26, 65280, 0);
			} else if (run <= 74 && run >= 50) {
				newSmallFont.drawCenteredString(
						Integer.toString(currentEnergy), x
								+ (clientSize == 0 ? 42 : 15), y + 26,
						0xffff00, 0);
			} else if (run <= 49 && run >= 25) {
				newSmallFont.drawCenteredString(
						Integer.toString(currentEnergy), x
								+ (clientSize == 0 ? 42 : 15), y + 26,
						0xfca607, 0);
			} else if (run <= 24 && run >= 0) {
				newSmallFont.drawCenteredString(
						Integer.toString(currentEnergy), x
								+ (clientSize == 0 ? 42 : 15), y + 26,
						0xf50d0d, 0);
			}
		}
		orbs[!running ? 6 : 8]
				.drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		double percent = (run / 100D);
		fillRun = 27 * percent;
		int depleteFill = 27 - (int) fillRun;
		orbs[18].myHeight = depleteFill;
		orbs[18].height = depleteFill;
		orbs[18].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		orbs[!running ? 7 : 9].drawSprite(x + (clientSize == 0 ? 10 : 34), y + 7);
	}

	public double fillSummoning;

	public void drawSummoningOrb() {
		int summoning = (int) (((double) currentStats[21] / (double) currentMaxStats[21]) * 100D);
		int x = getOrbX(3);
		int y = getOrbY(3);
		if (orbTextEnabled[3]) {
			orbs[clientSize == 0 ? (hoverPos == 3 ? 12 : 0)
					: (hoverPos == 3 ? 13 : 11)].drawSprite(x, y);
			if (summoning <= 100 && summoning >= 75) {
				newSmallFont
						.drawCenteredString(Integer.toString(currentStats[21]),
								x + (clientSize == 0 ? 42 : 15), y + 26, 65280,
								0);
			} else if (summoning <= 74 && summoning >= 50) {
				newSmallFont.drawCenteredString(
						Integer.toString(currentStats[21]), x
								+ (clientSize == 0 ? 42 : 15), y + 26,
						0xffff00, 0);
			} else if (summoning <= 49 && summoning >= 25) {
				newSmallFont.drawCenteredString(
						Integer.toString(currentStats[21]), x
								+ (clientSize == 0 ? 42 : 15), y + 26,
						0xfca607, 0);
			} else if (summoning <= 24 && summoning >= 0) {
				newSmallFont.drawCenteredString(
						Integer.toString(currentStats[21]), x
								+ (clientSize == 0 ? 42 : 15), y + 26,
						0xf50d0d, 0);
			}
		}
		orbs[getFamiliar().isActive() ? 16 : 14].drawSprite(x
				+ (clientSize == 0 ? 3 : 27), y + 3);
		double percent = (summoning / 100D);
		fillSummoning = 27 * percent;
		int depleteFill = 27 - (int) fillSummoning;
		orbs[19].myHeight = depleteFill;
		orbs[19].height = depleteFill;
		orbs[19].drawSprite(x + (clientSize == 0 ? 3 : 27), y + 3);
		orbs[15].drawSprite(x + (clientSize == 0 ? 9 : 33), y + 9);
	}

	private NumberFormat format = NumberFormat.getInstance();
	public boolean showXP;
	public boolean showBonus;
	public int gainedExpY = 0;
	public static boolean xpGained = false, canGainXP = true;
	public static int totalXP = 0;

	private LinkedList<XPGain> gains = new LinkedList<XPGain>();

	public void addXP(int skillID, int xp) {
		if (xp != 0 && canGainXP) {
			gains.add(new XPGain(skillID, xp));
		}
	}

	public class XPGain {
		/**
		 * The skill which gained the xp
		 */
		private int skill;

		/**
		 * The XP Gained
		 */
		private int xp;
		private int y;
		private int alpha = 0;

		public XPGain(int skill, int xp) {
			this.skill = skill;
			this.xp = xp;
		}

		public void increaseY() {
			y++;
		}

		public int getSkill() {
			return skill;
		}

		public int getXP() {
			return xp;
		}

		public int getY() {
			return y;
		}

		public int getAlpha() {
			return alpha;
		}

		public void increaseAlpha() {
			alpha += alpha < 256 ? 30 : 0;
			alpha = alpha > 256 ? 256 : alpha;
		}

		public void decreaseAlpha() {
			alpha -= alpha > 0 ? 30 : 0;
			alpha = alpha > 256 ? 256 : alpha;
		}
	}

	public void displayXPCounter() {
		int x = clientSize == 0 ? 419 : clientWidth - 310;
		int y = clientSize == 0 ? 0 : -36;
		int currentIndex = 0;
		int offsetY = 0;
		int stop = 70;
		cacheSprite[40].drawSprite(x, clientSize == 0 ? 50 : 48 + y);
		normalFont.drawShadowedString(true, x + 1, 0xffffff,
				"XP:" + String.format("%, d", totalXP), (clientSize == 0 ? 63 : 61) + y);

		if (!gains.isEmpty()) {
			Iterator<XPGain> it$ = gains.iterator();
			while (it$.hasNext()) {
				XPGain gain = it$.next();
				if (gain.getY() < stop) {
					if (gain.getY() <= 10) {
						gain.increaseAlpha();
					}
					if (gain.getY() >= stop - 10) {
						gain.decreaseAlpha();
					}
					gain.increaseY();
				} else if (gain.getY() == stop) {
					it$.remove();
				}
				Sprite sprite = cacheSprite[gain.getSkill() + 41];
				if (gains.size() > 1) {
					offsetY = (clientSize == 0 ? 0 : -20) + (currentIndex * 28);
				}
				if (gain.getY() < stop) {
					sprite.drawSprite((x + 15) - (sprite.myWidth / 2),
							gain.getY() + offsetY + 66 - (sprite.myHeight / 2),
							gain.getAlpha());
					newSmallFont.drawBasicString("<trans=" + gain.getAlpha()
							+ ">+" + format.format(gain.getXP()) + "xp",
							x + 30, gain.getY() + offsetY + 70, 0xCC6600, 0);
				}
				currentIndex++;
			}
		}
	}

	public int hoverPos;

	public void processMapAreaMouse() {
		if (mouseInRegion(clientWidth - (clientSize == 0 ? 249 : 217),
				clientSize == 0 ? 46 : 3, clientWidth
						- (clientSize == 0 ? 249 : 217) + 34,
				(clientSize == 0 ? 46 : 3) + 34)) {
			hoverPos = 0;// xp counter
		} else if (mouseInRegion(clientSize == 0 ? clientWidth - 58
				: getOrbX(1), getOrbY(1), (clientSize == 0 ? clientWidth - 58
				: getOrbX(1)) + 57, getOrbY(1) + 34)) {
			hoverPos = 1;// prayer
		} else if (mouseInRegion(clientSize == 0 ? clientWidth - 58
				: getOrbX(2), getOrbY(2), (clientSize == 0 ? clientWidth - 58
				: getOrbX(2)) + 57, getOrbY(2) + 34)) {
			hoverPos = 2;// run
		} else if (mouseInRegion(clientSize == 0 ? clientWidth - 74
				: getOrbX(3), getOrbY(3), (clientSize == 0 ? clientWidth - 74
				: getOrbX(3)) + 57, getOrbY(3) + 34)) {
			hoverPos = 3;// summoning
		} else {
			hoverPos = -1;
		}
	}

	public boolean choosingLeftClick;
	public int leftClick;
	public String[] leftClickNames = { "Call Follower", "Dismiss", "Take BoB",
			"Renew Familiar", "Interact", "Attack", "Follower Details", "Cast"
	// "Follower Details", "Attack", "Interact", "Renew Familiar", "Take BoB",
	// "Dismiss", "Call Follower"
	};
	public int[] leftClickActions = { 1018, 1019, 1020, 1021, 1022, 1023, 1024,
			1026 };

	public void rightClickMapArea() {
		if (mouseInRegion(clientWidth - (clientSize == 0 ? 249 : 217),
				clientSize == 0 ? 46 : 3, clientWidth
						- (clientSize == 0 ? 249 : 217) + 34,
				(clientSize == 0 ? 46 : 3) + 34)) {
			menuActionName[1] = "Reset counter";
			menuActionID[1] = 1013;
			menuActionName[2] = showXP ? "Hide counter" : "Show counter";
			menuActionID[2] = 1006;
			menuActionRow = 3;
		}
		if (mouseInRegion(clientSize == 0 ? clientWidth - 58 : getOrbX(1),
				getOrbY(1),
				(clientSize == 0 ? clientWidth - 58 : getOrbX(1)) + 57,
				getOrbY(1) + 34)) {

			menuActionName[3] = prayClicked ? "Toggle Quick-" + prayerBook
					+ " off" : "Toggle Quick-" + prayerBook + " on";
			menuActionID[3] = 1500;
			menuActionName[2] = "Select quick " + prayerBook;
			menuActionID[2] = 1506;
			menuActionName[1] = orbTextEnabled[1] ? "Hide text" : "Show text";
			menuActionID[1] = 1509;
			menuActionRow = 4;
		}
		if (mouseInRegion(clientSize == 0 ? clientWidth - 58 : getOrbX(2),
				getOrbY(2),
				(clientSize == 0 ? clientWidth - 58 : getOrbX(2)) + 57,
				getOrbY(2) + 34)) {

			menuActionName[3] = running ? "Turn run off" : "Turn run on";
			menuActionID[3] = 1014;
			menuActionName[2] = resting ? "Turn rest off" : "Turn rest on";
			menuActionID[2] = 1016;
			menuActionName[1] = orbTextEnabled[2] ? "Hide text" : "Show text";
			menuActionID[1] = 1510;
			menuActionRow = 4;
		}
		if (mouseInRegion(clientSize == 0 ? clientWidth - 58 : getOrbX(0),
				getOrbY(0),
				(clientSize == 0 ? clientWidth - 58 : getOrbX(0)) + 57,
				getOrbY(0) + 34)) {
			menuActionName[1] = orbTextEnabled[0] ? "Hide text" : "Show text";
			menuActionID[1] = 1508;
			menuActionRow = 2;
		}
		int x = super.mouseX; // Face North on compass
		int y = super.mouseY;
		if (x >= 531 && x <= 557 && y >= 7 && y <= 40) {
			menuActionName[1] = "Face North";
			menuActionID[1] = 696;
			menuActionRow = 2;
		}
		if (mouseInRegion(clientSize == 0 ? clientWidth - 74 : getOrbX(3),
				getOrbY(3),
				(clientSize == 0 ? clientWidth - 74 : getOrbX(3)) + 57,
				getOrbY(3) + 34)) {
			if (leftClick != -1 && leftClick < 8) {
				menuActionName[1] = orbTextEnabled[3] ? "Hide text"
						: "Show text";
				menuActionID[1] = 1511;
				menuActionName[2] = "Select left-click option";
				menuActionID[2] = 1027;
				menuActionName[3] = leftClickNames[leftClick].equals("Cast") ? leftClickNames[leftClick]
						+ " @gre@" + getFamiliar().getSpecialAttack()
						: leftClickNames[leftClick];
				menuActionID[3] = leftClickActions[leftClick];
				menuActionRow = 4;
			} else if (choosingLeftClick) {
				menuActionName[1] = orbTextEnabled[3] ? "Hide text"
						: "Show text";
				menuActionID[1] = 1511;
				menuActionName[2] = "Select left-click option";
				menuActionID[2] = 1027;
				menuActionName[3] = "Call Follower";
				menuActionID[3] = 1018;
				menuActionName[4] = "Dismiss";
				menuActionID[4] = 1019;
				menuActionName[5] = "Take BoB";
				menuActionID[5] = 1020;
				menuActionName[6] = "Renew Familiar";
				menuActionID[6] = 1021;
				menuActionName[7] = "Interact";
				menuActionID[7] = 1022;
				menuActionName[8] = "Attack";
				menuActionID[8] = 1023;
				if (getFamiliar().isActive()
						&& getFamiliar().getSpecialAttack().length() > 0) {
					menuActionName[9] = "Cast @gre@"
							+ getFamiliar().getSpecialAttack();
					menuActionID[9] = 1026;
					menuActionName[10] = "Follower Details";
					menuActionID[10] = 1024;
					menuActionRow = 11;
				} else {
					menuActionName[9] = "Follower Details";
					menuActionID[9] = 1024;
					menuActionRow = 10;
				}
			} else {
				menuActionName[1] = orbTextEnabled[3] ? "Hide text"
						: "Show text";
				menuActionID[1] = 1511;
				menuActionName[2] = "Select left-click option";
				menuActionID[2] = 1027;
				menuActionRow = 3;
			}
		}
	}

	private void drawMinimap() {
		int xPosOffset = clientSize == 0 ? 0 : clientWidth - 246;
		if (clientSize == 0)
			mapAreaIP.initDrawingArea();
		if (minimapStatus == 2) {
			cacheSprite[67].drawSprite((clientSize == 0 ? 32 : clientWidth - 162), (clientSize == 0 ? 9 : 5));
			if (clientSize == 0) {
				int gf = settings.get("gameframe");
				if(gf == 474) {
					SpriteCache.get(791).drawAdvancedSprite(0 + xPosOffset, 0);
				} else if(gf == 525) {
					SpriteCache.get(792).drawAdvancedSprite(0 + xPosOffset, 0);
				} else
					cacheSprite[6].drawSprite(0 + xPosOffset, 0);
			} else {
				cacheSprite[36].drawSprite(clientWidth - 167, 0);
				cacheSprite[37].drawSprite(clientWidth - 172, 0);
			}
			if(options.get("xp_orb")) {
				cacheSprite[38].drawSprite(clientSize == 0 ? -1 : clientWidth - 188, clientSize == 0 ? 46 : 40);
				if (hoverPos == 0) {
					cacheSprite[39].drawSprite(clientSize == 0 ? -1 : clientWidth - 188, clientSize == 0 ? 46 : 40);
				}
			}
			cacheSprite[30].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
			if (tabHover != -1) {
				if (tabHover == 10) {
					cacheSprite[34].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
				}
			}
			if (tabInterfaceIDs[tabID] != -1) {
				if (tabID == 10) {
					cacheSprite[35].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
				}
			}
			if(options.get("orbs")) {
				drawHPOrb();
				drawPrayerOrb();
				drawRunOrb();
				drawSummoningOrb();
			}
			if(settings.get("gameframe") == 614)
				drawWorldMap();
			if(settings.get("gameframe") >= 508 || clientSize > 0)
				compass[0].rotate(33, viewRotation, mapbackOffsets1, 256, mapBackOffsets0, 25, (clientSize == 0 ? 8 : 5), 
						(clientSize == 0 ? 8 + xPosOffset : clientWidth - 167), 33, 25);
			else
				compass[0].rotate(33, viewRotation, mapbackOffsets1, 256, mapBackOffsets0, 25, 5, 17 + xPosOffset, 33, 25);
			gameScreenIP.initDrawingArea();
			return;
		}
		int rotation = viewRotation + minimapRotation & 0x7ff;
		int j = 48 + myPlayer.x / 32;
		int l2 = 464 - myPlayer.y / 32;
		if(settings.get("gameframe") >= 508 || clientSize > 0)
			miniMap.rotate(clientSize == 0 ? 156 : 156, rotation, minimapYPosArray,
					256 + minimapZoom, minimapXPosArray, l2, (clientSize == 0 ? 8
							: 4/* 5 */),
					(clientSize == 0 ? 32 : clientWidth - 162),
					clientSize == 0 ? 156 : 156, j);
		else
			miniMap.rotate(156, rotation, minimapYPosArray, 256 + minimapZoom, minimapXPosArray, l2, 5, 48, 156, j);
		for (int j5 = 0; j5 < mapFunctionsLoadedAmt; j5++) {
			int k = (mapFunctionTileX[j5] * 4 + 2) - myPlayer.x / 32;
			int i3 = (mapFunctionTileY[j5] * 4 + 2) - myPlayer.y / 32;
			try {
				markMinimap(currentMapFunctionSprites[j5], k, i3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int rX = 0; rX < 104; rX++) {
			for (int rY = 0; rY < 104; rY++) {
				Deque class19 = groundArray[plane][rX][rY];
				if (class19 != null) {
					int x = (rX * 4 + 2) - myPlayer.x / 32;
					int y = (rY * 4 + 2) - myPlayer.y / 32;
					markMinimap(mapDotItem, x, y);
				}
			}
		}
		for (int i6 = 0; i6 < npcCount; i6++) {
			NPC npc = npcArray[npcIndices[i6]];
			if (npc != null && npc.isVisible()) {
				NPCDef entityDef = npc.desc;// to review
				if (entityDef.childrenIDs != null)
					entityDef = entityDef.getAlteredNPCDef();
				if (entityDef != null && entityDef.drawMinimapDot
						&& entityDef.clickable) {
					int mapXPos = npc.x / 32 - myPlayer.x / 32;
					int mapYPos = npc.y / 32 - myPlayer.y / 32;
					markMinimap(mapDotNPC, mapXPos, mapYPos);
				}
			}
		}
		for (int j6 = 0; j6 < playerCount; j6++) {
			Player player = playerArray[playerIndices[j6]];
			if (player != null && player.isVisible()) {
				int mapXPos = player.x / 32 - myPlayer.x / 32;
				int mapYPos = player.y / 32 - myPlayer.y / 32;
				boolean flag1 = false;
				long l6 = TextClass.longForName(player.name);
				for (int k6 = 0; k6 < friendsCount; k6++) {
					if (l6 != friendsListAsLongs[k6] || friendsNodeIDs[k6] == 0)
						continue;
					flag1 = true;
					break;
				}
				boolean flag2 = false;
				if (myPlayer.team != 0 && player.team != 0
						&& myPlayer.team == player.team)
					flag2 = true;
				boolean flag3 = false;
				for (int j3 = 0; j3 < clanList.length; j3++) {
					if (clanList[j3] == null)
						continue;
					if (!clanList[j3].equalsIgnoreCase(player.name))
						continue;
					flag3 = true;
					break;
				}
				if (flag1)
					markMinimap(mapDotFriend, mapXPos, mapYPos);
				else if (flag2)
					markMinimap(mapDotTeam, mapXPos, mapYPos);
				else if(flag3)
					markMinimap(mapDotClan, mapXPos, mapYPos);
				else
					markMinimap(mapDotPlayer, mapXPos, mapYPos);
			}
		}
		if (markType != 0 && loopCycle % 20 < 10) {
			if (markType == 1 && markedNPC >= 0 && markedNPC < npcArray.length) {
				NPC npc = npcArray[markedNPC];
				if (npc != null) {
					int k1 = npc.x / 32 - myPlayer.x
							/ 32;
					int i4 = npc.y / 32 - myPlayer.y
							/ 32;
					drawTargetArrow(mapMarker, i4, k1);
				}
			}
			if (markType == 2) {
				int l1 = ((markedX - baseX) * 4 + 2) - myPlayer.x / 32;
				int j4 = ((markedY - baseY) * 4 + 2) - myPlayer.y / 32;
				drawTargetArrow(mapMarker, j4, l1);
			}
			if (markType == 10 && markedPlayer >= 0
					&& markedPlayer < playerArray.length) {
				Player plry = playerArray[markedPlayer];
				if (plry != null) {
					int i2 = plry.x / 32 - myPlayer.x / 32;
					int k4 = plry.y / 32 - myPlayer.y / 32;
					drawTargetArrow(mapMarker, k4, i2);
				}
			}
		}
		if (destX != 0) {
			int j2 = (destX * 4 + 2) - myPlayer.x / 32;
			int l4 = (destY * 4 + 2) - myPlayer.y / 32;
			markMinimap(mapFlag, j2, l4);
		}
		if(settings.get("gameframe") >= 508 || clientSize > 0)
			DrawingArea.drawPixels(3, (clientSize == 0 ? 84 : 80), (clientSize == 0 ? 107 + xPosOffset : clientWidth - 88), 0xffffff, 3);
		else
			DrawingArea.drawPixels(3, 82, 125 + xPosOffset, 0xffffff, 3);
		if (clientSize == 0) {
			int gf = settings.get("gameframe");
			if(gf == 474) {
				SpriteCache.get(791).drawAdvancedSprite(0 + xPosOffset, 0);
			} else if(gf == 525) {
				SpriteCache.get(792).drawAdvancedSprite(0 + xPosOffset, 0);
			} else
				cacheSprite[6].drawSprite(0 + xPosOffset, 0);
		} else {
			cacheSprite[36].drawSprite(clientWidth - 167, 0);
			cacheSprite[37].drawSprite(clientWidth - 172, 0);
		}
		if(options.get("xp_orb")) {
			cacheSprite[38].drawSprite(clientSize == 0 ? -1 : clientWidth - 217, clientSize == 0 ? 46 : 3);
			if (hoverPos == 0) {
				cacheSprite[39].drawSprite( clientSize == 0 ? -1 : clientWidth - 217, clientSize == 0 ? 46 : 3);
			}
		}
		cacheSprite[30].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
		if (tabHover != -1) {
			if (tabHover == 10) {
				cacheSprite[34].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
			}
		}
		if (tabInterfaceIDs[tabID] != -1) {
			if (tabID == 10) {
				cacheSprite[35].drawSprite((clientSize == 0 ? 246 : clientWidth) - 21, 0);
			}
		}
		if(options.get("orbs")) {
			drawHPOrb();
			drawPrayerOrb();
			drawRunOrb();
			drawSummoningOrb();
		}
		if(settings.get("gameframe") == 614)
			drawWorldMap();
		if(settings.get("gameframe") >= 508 || clientSize > 0)
			compass[0].rotate(33, viewRotation, mapbackOffsets1, 256, mapBackOffsets0,
					25, (clientSize == 0 ? 8 : 5), (clientSize == 0 ? 8 + xPosOffset : clientWidth - 167), 33, 25);
		else
			SpriteCache.get(793).rotate(33, viewRotation, mapbackOffsets1, 256, mapBackOffsets0, 25, 4, 29 + xPosOffset, 33, 25);
		if (menuOpen && menuScreenArea == 3)
			drawMenu();
		gameScreenIP.initDrawingArea();
	}

	public int tabHover = -1;
	public boolean showTab = true;
	private int smallTabs = 1000;

	public void drawTabHover(boolean fixed) {
		if (fixed) {
			if (tabHover != -1) {
				if (tabInterfaceIDs[tabHover] != -1) {
					int[] positionX = { 0, 30, 60, 120, 150, 180, 210, 90, 30,
							60, -1, 120, 150, 180, 90, 0, 210 };
					int[] positionY = { 0, 0, 0, 0, 0, 0, 0, 298, 298, 298, -1,
							298, 298, 298, 0, 298, 298 };
					if (tabHover != 10) {
						cacheSprite[16].drawSprite(3 + positionX[tabHover], positionY[tabHover]);
					}
				}
			}
		} else {
			if (tabHover != -1) {
				int[] tab = { 0, 1, 2, 14, 3, 4, 5, 6, 15, 8, 9, 7, 11, 12, 13,
						16 };
				int[] positionX = { 0, 30, 60, 90, 120, 150, 180, 210, 0, 30,
						60, 90, 120, 150, 180, 210 };
				int offsetX = 0;
				for (int index = 0; index < tab.length; index++) {
					if (tabInterfaceIDs[tab[index]] != -1) {
						if (tabHover == tab[index]) {
							offsetX = index > 7 && clientWidth >= smallTabs ? 240
									: 0;
							cacheSprite[16]
									.drawARGBSprite(
											(clientWidth - (clientWidth >= smallTabs ? 480
													: 240))
													+ positionX[index]
													+ offsetX,
											clientHeight
													- (clientWidth >= smallTabs ? 37
															: (index < 8 ? 74
																	: 37)));
						}
					}
				}
			}
		}
	}

	public void handleTabArea(boolean fixed) {
		if (fixed) {
			int gf = settings.get("gameframe");
			if(gf == 474) {
				SpriteCache.get(789).drawSprite(0, 0);
			} else if(gf == 525) {
				SpriteCache.get(790).drawSprite(0, 0);
			} else if(gf == 614) {
				cacheSprite[13].drawSprite(0, 0);
			}
		} else {
			if (clientWidth >= smallTabs) {
				for (int positionX = clientWidth - 480, positionY = clientHeight - 37, index = 0; positionX <= clientWidth - 30
						&& index < 16; positionX += 30, index++) {
					cacheSprite[15].drawSprite(positionX, positionY);
				}
				if (showTab) {
					cacheSprite[18].drawTransparentSprite(clientWidth - 197,
							clientHeight - 37 - 267, 150);
					cacheSprite[19].drawSprite(clientWidth - 204,
							clientHeight - 37 - 274);
				}
			} else {
				for (int positionX = clientWidth - 240, positionY = clientHeight - 74, index = 0; positionX <= clientWidth - 30
						&& index < 8; positionX += 30, index++) {
					cacheSprite[15].drawSprite(positionX, positionY);
				}
				for (int positionX = clientWidth - 240, positionY = clientHeight - 37, index = 0; positionX <= clientWidth - 30
						&& index < 8; positionX += 30, index++) {
					cacheSprite[15].drawSprite(positionX, positionY);
				}
				if (showTab) {
					cacheSprite[18].drawTransparentSprite(clientWidth - 197,
							clientHeight - 74 - 267, 150);
					cacheSprite[19].drawSprite(clientWidth - 204,
							clientHeight - 74 - 274);
				}
			}
		}
		if (invOverlayInterfaceID == -1) {
			if(settings.get("gameframe") == 614 || clientSize > 0)
				drawTabHover(fixed);
			if (showTab) {
				drawTabs(fixed);
			}
			drawSideIcons(fixed);
		}
	}

	public void drawTabs(boolean fixed) {
		if (fixed) {
			int xPos = 2;
			int yPos = 0;
			int spriteId = 12;
			if (tabID < tabInterfaceIDs.length && tabInterfaceIDs[tabID] != -1) {
				if(settings.get("gameframe") == 614) {
					switch (tabID) {
					case 0:
					case 1:
					case 2:
						xPos += tabID * 30;
						yPos = 0;
						break;
					case 3:
					case 4:
					case 5:
					case 6:
						xPos += (tabID + 1) * 30;
						yPos = 0;
						break;
					case 7:
						xPos = 2 + ((tabID - 4) * 30);
						yPos = 299;
						break;
					case 8:
					case 9:
					case 11:
					case 12:
					case 13:
						xPos = 2 + ((tabID - 7) * 30);
						yPos = 299;
						break;
					case 14:
						xPos = 92;
						yPos = 0;
						break;
					case 15:
						xPos = 2;
						yPos = 299;
						break;
					case 16:
						xPos = 212;
						yPos = 299;
						break;
					}
					if (tabID != 10) {
						cacheSprite[17].drawARGBSprite(xPos - 4, yPos);
					}
				} else {
					switch(tabID) {
					case 0:
						xPos = 0;
						yPos = 0;
						spriteId = 8;
						break;
					case 14:
						xPos = 38 + 33;
						yPos = 0;
						break;
					case 2:
						xPos = 38;
						yPos = 0;
						spriteId = 12;
						break;
					case 3:
					case 4:
					case 5:
						xPos = ((tabID) * 33) + 5;
						yPos = 0;
						spriteId = 12;
						break;
					case 6:
						xPos = 38 + (5 * 33);
						yPos = 0;
						spriteId = 9;
						break;
					case 15:
						xPos = 1;
						yPos = 299;
						spriteId = 10;
						break;
					case 13:
						xPos = 1 + 38 + (33 * 5);
						yPos = 299;
						spriteId = 11;
						break;
					case 8:
					case 9:
					case 11:
					case 12:
						xPos =  6 + (tabID - 7) * 33;
						yPos = 299;
						break;
					case 7:
						xPos = 6 + (tabID - 4) * 33;
						yPos = 299;
						break;
					}
					cacheSprite[spriteId].drawARGBSprite(xPos + 5, yPos);
				}
			}
		} else {
			int[] tab = { 0, 1, 2, 14, 3, 4, 5, 6, 15, 8, 9, 7, 11, 12, 13, 16 };
			int[] positionX = { 0, 30, 60, 90, 120, 150, 180, 210, 0, 30, 60,
					90, 120, 150, 180, 210 };
			for (int index = 0; index < tab.length; index++) {
				int offsetX = clientWidth >= smallTabs ? 481 : 241;
				if (offsetX == 481 && index > 7) {
					offsetX -= 240;
				}
				int offsetY = clientWidth >= smallTabs ? 37 : (index > 7 ? 37
						: 74);
				if (tabID == tab[index] && tabInterfaceIDs[tab[index]] != -1) {
					cacheSprite[17].drawARGBSprite((clientWidth - offsetX - 4)
							+ positionX[index], (clientHeight - offsetY) + 0);
				}
			}
		}
	}

	public void drawSideIcons(boolean fixed) {
		if (fixed) {
			if(settings.get("gameframe") == 614) {
				int[] id = { 20, 89, 21, 22, 23, 24, 25, 26, 95, 28, 29, 27, 31,
						32, 33, 90, 149 };
				int[] tab = { 0, 1, 2, 14, 3, 4, 5, 6, 15, 8, 9, 7, 11, 12, 13, 16 };
				int[] positionX = { 8, 37, 67, 97, 127, 159, 187, 217, 7, 38, 69,
						97, 127, 157, 187, 217 };
				int[] positionY = { 9, 9, 8, 8, 8, 8, 8, 8, 307, 306, 306, 307,
						306, 306, 306, 308 };
				for (int index = 0; index < 16; index++) {
					if (tabInterfaceIDs[tab[index]] != -1) {
						if (id[index] != -1) {
							cacheSprite[id[((tabInterfaceIDs[14] == 26224 || tabInterfaceIDs[14] == 27224) && index == 3) ? 16
									: index]].drawSprite(positionX[index],
									positionY[index]);
						}
					}
				}
			} else {
				int[] id = { 0, 1, 2, 2, 3, 4, 5, 6, 14, 8, 9, 7, 11, 12, 13, 15, 16 };
				int[] tab = { 0, 1, 2, 14, 3, 4, 5, 6, 15, 8, 9, 7, 11, 12, 13, 16 };
				int[] positionX = { 11, 46, 48, 78, 114, 144, 178, 211, 13, 49, 82, 115, 148, 183, 217, 217 };
				int[] positionY = { 5, 4, 8, 3, 6, 1, 1, 4, 302, 304, 304, 304, 305, 302, 304, 308 };
				for (int index = 0; index < 16; index++) {
					if (tabInterfaceIDs[tab[index]] != -1) { 
						if (id[index] != -1) {
							if(tab[index] == 2 || tab[index] == 16)
								continue;
							if((tabInterfaceIDs[14] == 26224 || tabInterfaceIDs[14] == 27224) && index == 3)
								cacheSprite[149].drawSprite(positionX[index], positionY[index]);
							else
								sideIcons[id[index]].drawSprite(positionX[index], positionY[index]);
						}
					}
				}
			}
		} else {
			int[] id = { 20, 89, 21, 22, 23, 24, 25, 26, 95, 28, 29, 27, 31,
					32, 33, 90, 149 };
			int[] tab = { 0, 1, 2, 14, 3, 4, 5, 6, 15, 8, 9, 7, 11, 12, 13, 16 };
			int[] positionX = { 8, 37, 67, 97, 127, 159, 187, 217, 7, 38, 69,
					97, 127, 157, 187, 217 };
			int[] positionY = { 9, 9, 8, 8, 8, 8, 8, 8, /* second row */8, 8,
					8, 9, 8, 8, 8, 9 };
			for (int index = 0; index < tab.length; index++) {
				int offsetX = clientWidth >= smallTabs ? 482 : 242;
				if (offsetX == 482 && index > 7) {
					offsetX -= 240;
				}
				int offsetY = clientWidth >= smallTabs ? 37 : (index > 7 ? 37
						: 74);
				if (tabInterfaceIDs[tab[index]] != -1) {
					if (id[index] != -1) {
						cacheSprite[id[((tabInterfaceIDs[14] == 26224 || tabInterfaceIDs[14] == 27224) && index == 3) ? 16
								: index]].drawSprite((clientWidth - offsetX)
								+ positionX[index], (clientHeight - offsetY)
								+ positionY[index]);
					}
				}
			}
		}
	}

	private void drawTabArea() {
		if (clientSize == 0) {
			tabAreaIP.initDrawingArea();
		}
		Rasterizer.lineOffsets = sidebarOffsets;
		handleTabArea(clientSize == 0);
		int y = clientWidth >= smallTabs ? 37 : 74;
		if (showTab) {
			if (invOverlayInterfaceID != -1) {
				drawInterface(0, (clientSize == 0 ? 28 : clientWidth - 197),
						RSInterface.interfaceCache[invOverlayInterfaceID],
						(clientSize == 0 ? 37 : clientHeight - y - 267));
			} else if (tabInterfaceIDs[tabID] != -1) {
				drawInterface(0, (clientSize == 0 ? 28 : clientWidth - 197),
						RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
						(clientSize == 0 ? 37 : clientHeight - y - 267));
			}
			if (menuOpen && menuScreenArea == 1) {
				drawMenu();
			}
		}
		if (clientSize == 0)
			tabAreaIP.drawGraphics(168, super.graphics, 519);
		gameScreenIP.initDrawingArea();
		Rasterizer.lineOffsets = viewportOffsets;
	}

	private void processTabAreaTooltips(int TabHoverId) {
		String[] tooltipString = { "Combat Styles", "Task List", "Stats",
				"Inventory", "Worn Equipment", "Prayer List",
				"Magic Spellbook", "Clan Chat", "Friends List", "Ignore List",
				"Logout", "Options", "Emotes", "Music", "Quest Journals",
				"Summoning", "Notes" };
		menuActionName[1] = tooltipString[TabHoverId];
		menuActionID[1] = 1076;
		menuActionRow = 2;
	}

	private void processTabAreaHovers() {
		if (clientSize == 0) {
			int positionX = clientWidth - 244;
			int positionY = 169, positionY2 = clientHeight - 36;
			if (mouseInRegion(clientWidth - 21, 0, clientWidth, 21)) {
				tabHover = 10;
				processTabAreaTooltips(tabHover);
			} else if (mouseInRegion(positionX, positionY, positionX + 30,
					positionY + 36)) {
				needDrawTabArea = true;
				tabHover = 0;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 30, positionY, positionX + 60,
					positionY + 36)) {
				needDrawTabArea = true;
				tabHover = 1;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 60, positionY, positionX + 90,
					positionY + 36)) {
				needDrawTabArea = true;
				tabHover = 2;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 90, positionY,
					positionX + 120, positionY + 36)) {
				needDrawTabArea = true;
				tabHover = 14;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 120, positionY,
					positionX + 150, positionY + 36)) {
				needDrawTabArea = true;
				tabHover = 3;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 150, positionY,
					positionX + 180, positionY + 36)) {
				needDrawTabArea = true;
				tabHover = 4;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 180, positionY,
					positionX + 210, positionY + 36)) {
				needDrawTabArea = true;
				tabHover = 5;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 210, positionY,
					positionX + 240, positionY + 36)) {
				needDrawTabArea = true;
				tabHover = 6;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX, positionY2, positionX + 30,
					positionY2 + 36)) {
				needDrawTabArea = true;
				tabHover = 15;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 30, positionY2,
					positionX + 60, positionY2 + 36)) {
				needDrawTabArea = true;
				tabHover = 8;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 60, positionY2,
					positionX + 90, positionY2 + 36)) {
				needDrawTabArea = true;
				tabHover = 9;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 90, positionY2,
					positionX + 120, positionY2 + 36)) {
				needDrawTabArea = true;
				tabHover = 7;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 120, positionY2,
					positionX + 150, positionY2 + 36)) {
				needDrawTabArea = true;
				tabHover = 11;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 150, positionY2,
					positionX + 180, positionY2 + 36)) {
				needDrawTabArea = true;
				tabHover = 12;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 180, positionY2,
					positionX + 210, positionY2 + 36)) {
				needDrawTabArea = true;
				tabHover = 13;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX + 210, positionY2,
					positionX + 240, positionY2 + 36)) {
				needDrawTabArea = true;
				tabHover = 16;
				processTabAreaTooltips(tabHover);
				tabAreaAltered = true;
			} else {
				needDrawTabArea = true;
				tabHover = -1;
				tabAreaAltered = true;
			}
		} else {
			int[] positionX = { 0, 30, 60, 90, 120, 150, 180, 210, 0, 30, 60,
					90, 120, 150, 180, 210 };
			int[] tab = { 0, 1, 2, 14, 3, 4, 5, 6, 15, 8, 9, 7, 11, 12, 13, 16 };
			int offsetX = (clientWidth >= smallTabs ? clientWidth - 480
					: clientWidth - 240);
			int positionY = (clientWidth >= smallTabs ? clientHeight - 37
					: clientHeight - 74);
			int secondPositionY = clientHeight - 37;
			int secondOffsetX = clientWidth >= smallTabs ? 240 : 0;
			if (mouseInRegion(clientWidth - 21, 0, clientWidth, 21)) {
				tabHover = 10;
			} else if (mouseInRegion(positionX[0] + offsetX, positionY,
					positionX[0] + offsetX + 30, positionY + 37)) {
				tabHover = tab[0];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[1] + offsetX, positionY,
					positionX[1] + offsetX + 30, positionY + 37)) {
				tabHover = tab[1];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[2] + offsetX, positionY,
					positionX[2] + offsetX + 30, positionY + 37)) {
				tabHover = tab[2];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[3] + offsetX, positionY,
					positionX[3] + offsetX + 30, positionY + 37)) {
				tabHover = tab[3];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[4] + offsetX, positionY,
					positionX[4] + offsetX + 30, positionY + 37)) {
				tabHover = tab[4];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[5] + offsetX, positionY,
					positionX[5] + offsetX + 30, positionY + 37)) {
				tabHover = tab[5];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[6] + offsetX, positionY,
					positionX[6] + offsetX + 30, positionY + 37)) {
				tabHover = tab[6];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[7] + offsetX, positionY,
					positionX[7] + offsetX + 30, positionY + 37)) {
				tabHover = tab[7];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[8] + offsetX + secondOffsetX,
					secondPositionY, positionX[8] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				tabHover = tab[8];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[9] + offsetX + secondOffsetX,
					secondPositionY, positionX[9] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				tabHover = tab[9];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[10] + offsetX + secondOffsetX,
					secondPositionY, positionX[10] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				tabHover = tab[10];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[11] + offsetX + secondOffsetX,
					secondPositionY, positionX[11] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				tabHover = tab[11];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[12] + offsetX + secondOffsetX,
					secondPositionY, positionX[12] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				tabHover = tab[12];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[13] + offsetX + secondOffsetX,
					secondPositionY, positionX[13] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				tabHover = tab[13];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[14] + offsetX + secondOffsetX,
					secondPositionY, positionX[14] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				tabHover = tab[14];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else if (mouseInRegion(positionX[15] + offsetX + secondOffsetX,
					secondPositionY, positionX[15] + offsetX + secondOffsetX
							+ 30, secondPositionY + 37)) {
				tabHover = tab[15];
				processTabAreaTooltips(tabHover);
				needDrawTabArea = true;
				tabAreaAltered = true;
			} else {
				tabHover = -1;
				needDrawTabArea = true;
				tabAreaAltered = true;
			}
		}
	}

	private void processTabAreaClick() {
		if (clientSize == 0) {
			int positionX = clientWidth - 244;
			int positionY = 169;
			if (super.clickMode3 == 1  && settings.get("gameframe") != 474) {
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[0] != -1) {
					needDrawTabArea = true;
					tabID = 0;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[1] != -1) {
					needDrawTabArea = true;
					tabID = 1;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[2] != -1) {
					needDrawTabArea = true;
					tabID = 2;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[14] != -1) {
					needDrawTabArea = true;
					tabID = 14;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[3] != -1) {
					needDrawTabArea = true;
					tabID = 3;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[4] != -1) {
					needDrawTabArea = true;
					tabID = 4;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[5] != -1) {
					needDrawTabArea = true;
					tabID = 5;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[6] != -1) {
					needDrawTabArea = true;
					tabID = 6;
					tabAreaAltered = true;
				}
				positionX = clientWidth - 244;
				positionY = clientHeight - 36;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[15] != -1) {
					needDrawTabArea = true;
					tabID = 15;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[8] != -1) {
					needDrawTabArea = true;
					tabID = 8;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[9] != -1) {
					needDrawTabArea = true;
					tabID = 9;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[7] != -1) {
					needDrawTabArea = true;
					tabID = 7;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(clientWidth - 21, 0, clientWidth, 21)
						&& tabInterfaceIDs[10] != -1) {
					needDrawTabArea = true;
					tabID = 10;
					tabAreaAltered = true;
				}

				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[11] != -1) {
					needDrawTabArea = true;
					tabID = 11;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[12] != -1) {
					needDrawTabArea = true;
					tabID = 12;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[13] != -1) {
					needDrawTabArea = true;
					tabID = 13;
					tabAreaAltered = true;
				}
				positionX += 30;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[16] != -1) {
					needDrawTabArea = true;
					tabID = 16;
					tabAreaAltered = true;
				}
			} else if (super.clickMode3 == 1  && settings.get("gameframe") == 474) {
				positionX = clientWidth - 242;
				positionY = 169;
				if (clickInRegion(positionX, positionY, positionX + 38, positionY + 36) && tabInterfaceIDs[0] != -1) {
					needDrawTabArea = true;
					tabID = 0;
					tabAreaAltered = true;
				}
				positionX += 38;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[2] != -1) {
					needDrawTabArea = true;
					tabID = 2;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[14] != -1) {
					needDrawTabArea = true;
					tabID = 14;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[3] != -1) {
					needDrawTabArea = true;
					tabID = 3;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[4] != -1) {
					needDrawTabArea = true;
					tabID = 4;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[5] != -1) {
					needDrawTabArea = true;
					tabID = 5;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[6] != -1) {
					needDrawTabArea = true;
					tabID = 6;
					tabAreaAltered = true;
				}
				positionX = clientWidth - 242;
				positionY = clientHeight - 36;
				if (clickInRegion(positionX, positionY, positionX + 38,
						positionY + 36) && tabInterfaceIDs[15] != -1) {
					needDrawTabArea = true;
					tabID = 15;
					tabAreaAltered = true;
				}
				positionX += 38;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[8] != -1) {
					needDrawTabArea = true;
					tabID = 8;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[9] != -1) {
					needDrawTabArea = true;
					tabID = 9;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[7] != -1) {
					needDrawTabArea = true;
					tabID = 7;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(clientWidth - 21, 0, clientWidth, 21)
						&& tabInterfaceIDs[10] != -1) {
					needDrawTabArea = true;
					tabID = 10;
					tabAreaAltered = true;
				}

				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[11] != -1) {
					needDrawTabArea = true;
					tabID = 11;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 30,
						positionY + 36) && tabInterfaceIDs[12] != -1) {
					needDrawTabArea = true;
					tabID = 12;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[13] != -1) {
					needDrawTabArea = true;
					tabID = 13;
					tabAreaAltered = true;
				}
				positionX += 33;
				if (clickInRegion(positionX, positionY, positionX + 33,
						positionY + 36) && tabInterfaceIDs[16] != -1) {
					needDrawTabArea = true;
					tabID = 16;
					tabAreaAltered = true;
				}
			}
		} else {
			int[] positionX = { 0, 30, 60, 90, 120, 150, 180, 210, 0, 30, 60,
					90, 120, 150, 180, 210 };
			int[] tab = { 0, 1, 2, 14, 3, 4, 5, 6, 15, 8, 9, 7, 11, 12, 13, 16 };
			int offsetX = (clientWidth >= smallTabs ? clientWidth - 480
					: clientWidth - 240);
			int positionY = (clientWidth >= smallTabs ? clientHeight - 37
					: clientHeight - 74);
			int secondPositionY = clientHeight - 37;
			int secondOffsetX = clientWidth >= smallTabs ? 240 : 0;
			if (super.clickMode3 == 1) {
				if (clickInRegion(positionX[0] + offsetX, positionY,
						positionX[0] + offsetX + 30, positionY + 37)
						&& tabInterfaceIDs[tab[0]] != -1) {
					if (tabID == tab[0]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[0];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[1] + offsetX, positionY,
						positionX[1] + offsetX + 30, positionY + 37)
						&& tabInterfaceIDs[tab[1]] != -1) {
					if (tabID == tab[1]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[1];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[2] + offsetX, positionY,
						positionX[2] + offsetX + 30, positionY + 37)
						&& tabInterfaceIDs[tab[2]] != -1) {
					if (tabID == tab[2]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[2];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[3] + offsetX, positionY,
						positionX[3] + offsetX + 30, positionY + 37)
						&& tabInterfaceIDs[tab[3]] != -1) {
					if (tabID == tab[3]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[3];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[4] + offsetX, positionY,
						positionX[4] + offsetX + 30, positionY + 37)
						&& tabInterfaceIDs[tab[4]] != -1) {
					if (tabID == tab[4]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[4];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[5] + offsetX, positionY,
						positionX[5] + offsetX + 30, positionY + 37)
						&& tabInterfaceIDs[tab[5]] != -1) {
					if (tabID == tab[5]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[5];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[6] + offsetX, positionY,
						positionX[6] + offsetX + 30, positionY + 37)
						&& tabInterfaceIDs[tab[6]] != -1) {
					if (tabID == tab[6]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[6];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[7] + offsetX, positionY,
						positionX[7] + offsetX + 30, positionY + 37)
						&& tabInterfaceIDs[tab[7]] != -1) {
					if (tabID == tab[7]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[7];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(
						positionX[8] + offsetX + secondOffsetX,
						secondPositionY, positionX[8] + offsetX + secondOffsetX
								+ 30, secondPositionY + 37)) {
					if (tabID == tab[8]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[8];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(
						positionX[9] + offsetX + secondOffsetX,
						secondPositionY, positionX[9] + offsetX + secondOffsetX
								+ 30, secondPositionY + 37)) {
					if (tabID == tab[9]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[9];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[10] + offsetX
						+ secondOffsetX, secondPositionY, positionX[10]
						+ offsetX + secondOffsetX + 30, secondPositionY + 37)) {
					if (tabID == tab[10]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[10];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[11] + offsetX
						+ secondOffsetX, secondPositionY, positionX[11]
						+ offsetX + secondOffsetX + 30, secondPositionY + 37)) {
					if (tabID == tab[11]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[11];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[12] + offsetX
						+ secondOffsetX, secondPositionY, positionX[12]
						+ offsetX + secondOffsetX + 30, secondPositionY + 37)) {
					if (tabID == tab[12]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[12];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[13] + offsetX
						+ secondOffsetX, secondPositionY, positionX[13]
						+ offsetX + secondOffsetX + 30, secondPositionY + 37)) {
					if (tabID == tab[13]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[13];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[14] + offsetX
						+ secondOffsetX, secondPositionY, positionX[14]
						+ offsetX + secondOffsetX + 30, secondPositionY + 37)) {
					if (tabID == tab[14]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[14];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(positionX[15] + offsetX
						+ secondOffsetX, secondPositionY, positionX[15]
						+ offsetX + secondOffsetX + 30, secondPositionY + 37)) {
					if (tabID == tab[15]) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = tab[15];
					needDrawTabArea = true;
					tabAreaAltered = true;
				} else if (clickInRegion(clientWidth - 21, 0, clientWidth, 21)) {
					if (tabID == 10) {
						showTab = !showTab;
					} else {
						showTab = true;
					}
					tabID = 10;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
			}
		}
	}

	public int MapX, MapY;
	public static int spellID = 0;
	public static boolean newDamage;
	public Sprite magicAuto;
	public boolean Autocast = false;
	public boolean xpLock;
	public int xpCounter;
	public int autocastId = 0;
	public int followPlayer = 0;
	public int followNPC = 0;
	public int followDistance = 1;
	public boolean downloading = false;
	public String clanName = "";
	public boolean buttonclicked = false;
	public String TalkingFix = "";
	public int GEItemId = 0;
	public Sprite Search;
	public Sprite search;

	public void magicOnItem(int id) {
		spellSelected = 1;
		spellID = id;
		selectedSpellId = id;
		spellUsableOn = 16;
		itemSelected = 0;
		needDrawTabArea = true;
		spellTooltip = "Cast on";
		needDrawTabArea = true;
		tabID = 3;
		tabAreaAltered = true;
	}

	public final String formatNumberToLetter(int j) {
		if (j >= 0 && j < 10000)
			return String.valueOf(j);
		if (j >= 10000 && j < 10000000)
			return j / 1000 + "K";
		if (j >= 10000000 && j < 999999999)
			return j / 1000000 + "M";
		if (j >= 999999999)
			return "*";
		else
			return "?";
	}

	public static final byte[] ReadFile(String s) {
		try {
			byte abyte0[];
			File file = new File(s);
			int i = (int) file.length();
			abyte0 = new byte[i];
			DataInputStream datainputstream = new DataInputStream(
					new BufferedInputStream(new FileInputStream(s)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			return abyte0;
		} catch (Exception e) {
			System.out.println((new StringBuilder()).append("Read Error: ")
					.append(s).toString());
			return null;
		}
	}

	public static final int MAP_IDX = 4;

	public static final int MODEL_IDX = 1, CONFIG_IDX = 0;

	public static boolean displayScrollbar;

	public void displayItemSearch() {
		int yPosOffset = (clientSize > 0) ? clientHeight - 165 : 0;
		int xPosOffset = 0;
		try {
			quickChat = false;
			if (amountOrNameInput != "") {
				itemSearch(amountOrNameInput);
			}
			cacheSprite[64].drawSprite(0 + xPosOffset, 0 + yPosOffset);
			DrawingArea.setDrawingArea(121 + yPosOffset, 8, 512, 7);
			SpriteCache.spriteCache[678].drawSprite(18, 18 + yPosOffset);
			for (int j = 0; j < totalItemResults; j++) {
				int x = super.mouseX;
				int y = super.mouseY;
				final int yPos = 21 + j * 14 - itemResultScrollPos;
				if (yPos > 0 && yPos < 210) {
					String n = itemResultNames[j];
					for (int i = 0; i <= 20; i++)
						if (n.contains("<img=" + i + ">"))
							n = n.replaceAll("<img=" + i + ">", "");
					chatTextDrawingArea.drawStringNormal(capitalizeFirstChar(n), 78,
							0xA05A00, yPos + yPosOffset
									+ (totalItemResults < 8 ? 6 : 0));
					if (x > 74
							&& x < 495
							&& y > ((clientSize == 0) ? 338
									: clientHeight - 165)
									+ yPos
									- 13
									+ (totalItemResults < 8 ? 6 : 0)
							&& y < ((clientSize == 0) ? 338
									: clientHeight - 165)
									+ yPos
									+ 2
									+ (totalItemResults < 8 ? 6 : 0)) {
						DrawingArea.fillRectangle(0x807660, yPos - 12
								+ yPosOffset + (totalItemResults < 8 ? 6 : 0),
								424, 15, 60, 75);
						Sprite itemImg = ItemDef.getSprite(itemResultIDs[j], 1, 0);
						if (itemImg != null)
							itemImg.drawSprite(22, 20 + yPosOffset);
						GEItemId = itemResultIDs[j];
					}
				}
			}
			DrawingArea.drawPixels(113, 8 + yPosOffset, 74, 0x807660, 2);
			DrawingArea.defaultDrawingAreaSize();
			if (totalItemResults > 8) {
				displayScrollbar = true;
				drawScrollbar(114, itemResultScrollPos, 8 + yPosOffset,
						496 + xPosOffset, totalItemResults * 14, false, false);
				// drawScrollbar(112, itemResultScrollPos, 8, 496,
				// totalItemResults * 14 + 12, 0);
			} else {
				displayScrollbar = false;
			}
			boolean showMatches = true;
			showMatches = true;
			if (amountOrNameInput.length() == 0) {
				chatTextDrawingArea.drawStringCenter(0xA05A00, 259,
						"Grand Exchange Item Search", 30 + yPosOffset, false);
				smallText
						.drawStringCenter(
								0xA05A00,
								259,
								"To search for an item, start by typing part of it's name.",
								80 + yPosOffset, false);
				smallText
						.drawStringCenter(
								0xA05A00,
								259,
								"Then, simply select the item you want from the results on the display.",
								80 + 15 + yPosOffset, false);
				// chatTextDrawingArea.drawText(0xffffff, amountOrNameInput +
				// "*", 32, 133);
				showMatches = false;
			}
			if (totalItemResults == 0 && showMatches) {
				smallText.drawStringCenter(0xA05A00, 259,
						"No matching items found", 80 + yPosOffset, false);
			}
			DrawingArea.fillRectangle(0x807660, 121 + yPosOffset, 506, 15, 120,
					7);// box
			// chatTextDrawingArea.drawText(0, "<img=8>", 133, 12);
			chatTextDrawingArea.drawStringNormal(amountOrNameInput + "*",
					28 + xPosOffset, 0xffffff, 133 + yPosOffset);
			// chatTextDrawingArea.drawText(0xffffff, amountOrNameInput + "*",
			// 133, 122);
			DrawingArea.drawLine(121 + yPosOffset, 0x807660, 506, 7);// line
			// drawClose(496, 122, 496, 345 + 112, 496 + 19, 361 + 112);
			// drawClose(496, 122, 496, 345 + 112, 496 + 19, 361 + 112);
			// drawClose(496, 122, 496, 345 + 112, 496 + 19, 361 + 112);

			SpriteCache.spriteCache[679].drawSprite(11, 122 + yPosOffset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drawClose(int x, int y, int x2, int y2, int x3, int y3) {
		cacheSprite[31].drawSprite(x, y);
		if (super.mouseX >= x2 && super.mouseX <= x3 && super.mouseY >= y2
				&& super.mouseY <= y3) {
			cacheSprite[32].drawSprite(x, y);
		}
	}

	public int interfaceButtonAction = 0;

	void sendPacket(int packet) {
		if (packet == 103) {
			stream.createFrame(103);
			stream.writeByte1(inputString.length() - 1);
			stream.writeString(inputString.substring(2));
			inputString = "";
			inputStringPos = 0;
			promptInput = "";
			interfaceButtonAction = 0;
		}
		if (packet == 1003) {
			stream.createFrame(103);
			inputString = "::" + inputString;
			stream.writeByte1(inputString.length() - 1);
			stream.writeString(inputString.substring(2));
			inputString = "";
			inputStringPos = 0;
			promptInput = "";
			interfaceButtonAction = 0;
		}
	}

	public void playSong(int id) {
		if (currentSong != id) {
			nextSong = id;
			songChanging = true;
			onDemandFetcher.requestFileData(2, nextSong);
			currentSong = id;
		}
	}

	/*public void stopMidi() {
		signlink.fadeMidi = 0;
		signlink.midi = "stop";
		try {
			signlink.music.stop();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}*/

	/*private void adjustVolume(boolean updateMidi, int volume) {
		try {
			signlink.setVolume(volume);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		if (updateMidi) {
			signlink.midi = "voladjust";
		}
	}*/

	private boolean menuHasAddFriend(int j) {
		if (j < 0)
			return false;
		int k = menuActionID[j];
		if (k >= 2000)
			k -= 2000;
		return k == 337;
	}

	private Sprite min;
	private int minY = 0, timer = 0;
	private boolean fin = true, up = false;

	private void resetAnAnim(int w) {
		minY = 0;
		minY -= 140;
		timer = 0;
		fin = false;
		up = false;
	}

	public void processAnAnim() {
		if (!fin) {
			if (minY <= 10 && !up) {
				minY += 2;
			} else {
				up = true;
				timer++;
				if (timer > 30)
					minY -= 2;
			}
			if (minY == -145) {
				fin = true;
			}
			SpriteCache.fetchIfNeeded(676);
			if (min == null)
				min = SpriteCache.spriteCache[676];
			if (min != null)
				min.drawSprite(290, minY);
		}
	}

	public int getXPForLevel(int level) {
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

	public static int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430) {
			return 99;
		}
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public String[] skillNames = { "Attack", "Hitpoints", "Mining", "Strength",
			"Agility", "Smithing", "Defence", "Herblore", "Fishing", "Range",
			"Thieving", "Cooking", "Prayer", "Crafting", "Firemaking", "Magic",
			"Fletching", "Woodcutting", "Runecrafting", "Slayer", "Farming",
			"Construction", "Hunter", "Summoning", "Dungeoneering" };

	public String setMessage(int level) {
		String[] messages = new String[4];
		String message = "";
		int[] stuff = { 0, 3, 14, 2, 16, 13, 1, 15, 10, 4, 17, 7, 5, 12, 11, 6,
				9, 8, 20, 18, 19, 21, 22, 23, 24 };
		messages[0] = skillNames[level] + ": " + currentStats[stuff[level]]
				+ "/" + currentMaxStats[stuff[level]] + "\\n";
		messages[1] = "Current XP: "
				+ String.format("%, d", currentExp[stuff[level]]) + "\\n";
		messages[2] = "Remainder: "
				+ String.format(
						"%, d",
						(getXPForLevel(currentMaxStats[stuff[level]] + 1) - currentExp[stuff[level]]))
				+ "\\n";
		messages[3] = "Next level: "
				+ String.format("%, d",
						getXPForLevel(currentMaxStats[stuff[level]] + 1));
		message = messages[0] + messages[1] + messages[2] + messages[3];
		if (currentMaxStats[stuff[level]] >= 99) {
			message = messages[0] + messages[1];
		}
		return message;
	}

	public static boolean swiftKit = false, webclient = false;;

	public void init() {
		try {
			webclient = true;
			nodeID = 10;
			portOff = 0;
			setHighMem();
			isMembers = true;
			signlink.storeid = 32;
			signlink.startpriv(InetAddress.getLocalHost());
			initClientFrame(765, 503);
			instance = this;

		} catch (Exception exception) {
			return;
		}
	}

	public void startRunnable(Runnable runnable, int i) {
		if (i > 10)
			i = 10;
		if (signlink.mainapp != null) {
			signlink.startthread(runnable, i);
		} else {
			super.startRunnable(runnable, i);
		}
	}

	public Socket openSocket(int port, String server) throws IOException {
		return new Socket(InetAddress.getByName(server), port);
	}

	public String indexLocation(int cacheIndex, int index) {
		return signlink.findcachedir() + "index" + cacheIndex + "/"
				+ (index != -1 ? index + ".gz" : "");
	}

	public void repackCacheIndex(int cacheIndex) {
		System.out.println("Started repacking index " + cacheIndex + ".");
		int indexLength = new File(indexLocation(cacheIndex, -1)).listFiles().length;
		File[] file = new File(indexLocation(cacheIndex, -1)).listFiles();
		try {
			for (int index = 0; index < indexLength; index++) {
				int fileIndex = Integer
						.parseInt(getFileNameWithoutExtension(file[index]
								.toString()));
				byte[] data = fileToByteArray(cacheIndex, fileIndex);
				if (data != null && data.length > 0) {
					cacheIndices[cacheIndex].put(data.length, data, fileIndex);
					System.out.println("Repacked " + fileIndex + ".");
				} else {
					System.out.println("Unable to locate index " + fileIndex
							+ ".");
				}
			}
		} catch (Exception e) {
			System.out.println("Error packing cache index " + cacheIndex + ".");
		}
		System.out.println("Finished repacking " + cacheIndex + ".");
	}

	public byte[] fileToByteArray(int cacheIndex, int index) {
		try {
			if (indexLocation(cacheIndex, index).length() <= 0
					|| indexLocation(cacheIndex, index) == null) {
				return null;
			}
			File file = new File(indexLocation(cacheIndex, index));
			byte[] fileData = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(fileData);
			fis.close();
			return fileData;
		} catch (Exception e) {
			return null;
		}
	}

	private boolean processMenuClick() {
		if (activeInterfaceType != 0)
			return false;
		int j = super.clickMode3;
		if (spellSelected == 1 && super.saveClickX >= 516
				&& super.saveClickY >= 160 && super.saveClickX <= 765
				&& super.saveClickY <= 205)
			j = 0;
		if (menuOpen) {
			if (j != 1) {
				int k = super.mouseX;
				int j1 = super.mouseY;
				if (menuScreenArea == 0) {
					k -= clientSize == 0 ? 4 : 0;
					j1 -= clientSize == 0 ? 4 : 0;
				}
				if (menuScreenArea == 1) {
					k -= 519;
					j1 -= 168;
				}
				if (menuScreenArea == 2) {
					k -= 17;
					j1 -= 338;
				}
				if (menuScreenArea == 3) {
					k -= 519;
					j1 -= 0;
				}
				if (k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10
						|| j1 < menuOffsetY - 10
						|| j1 > menuOffsetY + menuHeight + 10) {
					menuOpen = false;
					if (menuScreenArea == 1)
						needDrawTabArea = true;
					if (menuScreenArea == 2)
						inputTaken = true;
				}
			}
			if (j == 1) {
				int l = menuOffsetX;
				int k1 = menuOffsetY;
				int i2 = menuWidth;
				int k2 = super.saveClickX;
				int l2 = super.saveClickY;
				if (menuScreenArea == 0) {
					k2 -= clientSize == 0 ? 4 : 0;
					l2 -= clientSize == 0 ? 4 : 0;
				}
				if (menuScreenArea == 1) {
					k2 -= 519;
					l2 -= 168;
				}
				if (menuScreenArea == 2) {
					k2 -= 17;
					l2 -= 338;
				}
				if (menuScreenArea == 3) {
					k2 -= 519;
					l2 -= 0;
				}
				int i3 = -1;
				for (int j3 = 0; j3 < menuActionRow; j3++) {
					int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
					if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3)
						i3 = j3;
				}
				// System.out.println(i3);
				if (i3 != -1)
					doAction(i3);
				menuOpen = false;
				if (menuScreenArea == 1)
					needDrawTabArea = true;
				if (menuScreenArea == 2) {
					inputTaken = true;
				}
			}
			return true;
		} else {
			if (j == 1 && menuActionRow > 0) {
				int i1 = menuActionID[menuActionRow - 1];
				if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53
						|| i1 == 74 || i1 == 454 || i1 == 539 || i1 == 493
						|| i1 == 847 || i1 == 447 || i1 == 1125) {
					int l1 = menuActionCmd2[menuActionRow - 1];
					int j2 = menuActionCmd3[menuActionRow - 1];
					RSInterface rsi = RSInterface.interfaceCache[j2];
					if (rsi.deleteOnDrag2 || rsi.dragDeletes) {
						isDragging = false;
						dragCycle = 0;
						focusedDragWidget = j2;
						dragFromSlot = l1;
						activeInterfaceType = 2;
						pressX = super.saveClickX;
						pressY = super.saveClickY;
						if (RSInterface.interfaceCache[j2].parentID == openInterfaceID)
							activeInterfaceType = 1;
						if (RSInterface.interfaceCache[j2].parentID == backDialogID)
							activeInterfaceType = 3;
						return true;
					}
				}
			}
			if (j == 1
					&& (useOneMouseButton == 1 || menuHasAddFriend(menuActionRow - 1))
					&& menuActionRow > 2)
				j = 2;
			if (j == 1 && menuActionRow > 0)
				doAction(menuActionRow - 1);
			if (j == 2 && menuActionRow > 0)
				determineMenuSize();
			return false;
		}
	}

	public static int totalRead = 0;

	public static String getFileNameWithoutExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}

	private void saveMidi(boolean flag, byte abyte0[]) {
		signlink.fadeMidi = flag ? 1 : 0;
		signlink.midisave(abyte0, abyte0.length);
	}

	private void loadRegion() {
		try {
			lastKnownPlane = -1;
			highestAmtToLoad = 0;
			stillGraphicDeque.clear();
			projectileDeque.clear();
			TextureLoader317.clearTextureCache();
			TextureLoader667.clearTextureCache();
			clearMemoryCaches();
			worldController.initToNull();
			System.gc();
			for (int i = 0; i < 4; i++)
				clippingPlanes[i].setDefault();

			for (int l = 0; l < 4; l++) {
				for (int k1 = 0; k1 < 104; k1++) {
					for (int j2 = 0; j2 < 104; j2++)
						tileSettingBits[l][k1][j2] = 0;

				}

			}

			objectManager = new Region(tileSettingBits, intGroundArray);
			int k2 = terrainData.length;
			if (loggedIn)
				stream.createFrame(0);
			if (!requestMapReconstruct) {
				for (int i3 = 0; i3 < k2; i3++) {
					int i4 = (mapCoordinates[i3] >> 8) * 64 - baseX;
					int k5 = (mapCoordinates[i3] & 0xff) * 64 - baseY;
					byte abyte0[] = terrainData[i3];
					if (abyte0 != null)
						objectManager.method180(abyte0, k5, i4,
								(currentRegionX - 6) * 8,
								(currentRegionY - 6) * 8, clippingPlanes);
				}

				for (int j4 = 0; j4 < k2; j4++) {
					int l5 = (mapCoordinates[j4] >> 8) * 64 - baseX;
					int k7 = (mapCoordinates[j4] & 0xff) * 64 - baseY;
					byte abyte2[] = terrainData[j4];
					if (abyte2 == null && currentRegionY < 800)
						objectManager.initiateVertexHeights(k7, 64, 64, l5);
				}

				anInt1097++;
				if (anInt1097 > 160) {
					anInt1097 = 0;
					stream.createFrame(238);
					stream.writeByte1(96);
				}
				if (loggedIn)
					stream.createFrame(0);
				for (int i6 = 0; i6 < k2; i6++) {
					byte abyte1[] = objectData[i6];
					if (abyte1 != null) {
						// System.out.println("Object maps: "+anIntArray1236[i6]);
						int l8 = (mapCoordinates[i6] >> 8) * 64 - baseX;
						int k9 = (mapCoordinates[i6] & 0xff) * 64 - baseY;
						objectManager.loadObjects(l8, clippingPlanes, k9,
								worldController, abyte1);
					}
				}

			}
			if (requestMapReconstruct) {
				for (int plane = 0; plane < 4; plane++) {
					for (int x = 0; x < 13; x++) {
						for (int y = 0; y < 13; y++) {
							int chunkBits = constructRegionData[plane][x][y];
							if (chunkBits != -1) {
								int z = chunkBits >> 24 & 3;
								int rotation = chunkBits >> 1 & 3;
								int xCoord = chunkBits >> 14 & 0x3ff;
								int yCoord = chunkBits >> 3 & 0x7ff;
								int mapRegion = (xCoord / 8 << 8) + yCoord / 8;
								for (int idx = 0; idx < mapCoordinates.length; idx++) {
									if (mapCoordinates[idx] != mapRegion
											|| terrainData[idx] == null)
										continue;
									objectManager.loadMapChunk(z, rotation,
											clippingPlanes, x * 8,
											(xCoord & 7) * 8, terrainData[idx],
											(yCoord & 7) * 8, plane, y * 8);
									break;
								}

							}
						}

					}

				}

				for (int xChunk = 0; xChunk < 13; xChunk++) {
					for (int yChunk = 0; yChunk < 13; yChunk++) {
						int tileBits = constructRegionData[0][xChunk][yChunk];
						if (tileBits == -1)
							objectManager.initiateVertexHeights(yChunk * 8, 8, 8,
									xChunk * 8);
					}

				}
				if (loggedIn)
					stream.createFrame(0);
				for (int chunkZ = 0; chunkZ < 4; chunkZ++) {
					for (int chunkX = 0; chunkX < 13; chunkX++) {
						for (int chunkY = 0; chunkY < 13; chunkY++) {
							int tileBits = constructRegionData[chunkZ][chunkX][chunkY];
							if (tileBits != -1) {
								int plane = tileBits >> 24 & 3;
								int rotation = tileBits >> 1 & 3;
								int coordX = tileBits >> 14 & 0x3ff;
								int coordY = tileBits >> 3 & 0x7ff;
								int mapRegion = (coordX / 8 << 8) + coordY / 8;
								for (int idx = 0; idx < mapCoordinates.length; idx++) {
									if (mapCoordinates[idx] != mapRegion
											|| objectData[idx] == null)
										continue;
									objectManager.readObjectMap(clippingPlanes,
											worldController, plane, chunkX * 8,
											(coordY & 7) * 8, chunkZ,
											objectData[idx], (coordX & 7) * 8,
											rotation, chunkY * 8);
									break;
								}
							}
						}
					}
				}
			}
			if (loggedIn)
				stream.createFrame(0);
			objectManager.createRegionScene(clippingPlanes, worldController);
			if (loggedIn)
				gameScreenIP.initDrawingArea();
			if (loggedIn)
				stream.createFrame(0);
			int k3 = Region.highestPlane;
			if (k3 > plane)
				k3 = plane;
			if (k3 < plane - 1)
				k3 = plane - 1;
			if (lowMem)
				worldController.initTiles(Region.highestPlane);
			else
				worldController.initTiles(0);
			for (int i5 = 0; i5 < 104; i5++) {
				for (int i7 = 0; i7 < 104; i7++)
					spawnGroundItem(i5, i7);

			}

			anInt1051++;
			if (anInt1051 > 98) {
				anInt1051 = 0;
				stream.createFrame(150);
			}
			clearObjectSpawnRequests();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		ObjectDef.modelCache.clear();
		ObjectDef.completedModelCache.clear();
		if (super.mainFrame != null && loggedIn) {
			stream.createFrame(210);
			stream.writeDWord(0x3f008edd);
		}
		System.gc();
		TextureLoader317.resetTextures();
		onDemandFetcher.clearExtraFilesList();
		int startRegionX = (currentRegionX - 6) / 8 - 1;
		int endRegionX = (currentRegionX + 6) / 8 + 1;
		int startRegionY = (currentRegionY - 6) / 8 - 1;
		int endRegionY = (currentRegionY + 6) / 8 + 1;
		for (int regionX = startRegionX; regionX <= endRegionX; regionX++) {
			for (int regionY = startRegionY; regionY <= endRegionY; regionY++)
				if (regionX == startRegionX || regionX == endRegionX
						|| regionY == startRegionY || regionY == endRegionY) {
					int floorMapId = onDemandFetcher.getMapIdForRegions(0,
							regionY, regionX);
					if (floorMapId != -1) {
						onDemandFetcher.insertExtraFilesRequest(floorMapId, 3);
					}
					int objectMapId = onDemandFetcher.getMapIdForRegions(1,
							regionY, regionX);
					if (objectMapId != -1) {
						onDemandFetcher.insertExtraFilesRequest(objectMapId, 3);
					}
				}

		}

	}

	public void clearMemoryCaches() {
		ObjectDef.modelCache.clear();
		ObjectDef.completedModelCache.clear();
		NPCDef.modelCache.clear();
		ItemDef.modelCache.clear();
		ItemDef.spriteCache.clear();
		NewItemDef.modelCache.clear();
		NewItemDef.spriteCache.clear();
		Player.modelCache.clear();
		SpotAnim.modelCache.clear();
	}

	public void renderedMapScene(int z) {
		int mapPixels[] = miniMap.myPixels;
		int mapLength = mapPixels.length;
		for (int k = 0; k < mapLength; k++)
			mapPixels[k] = 0;

		for (int y = 1; y < 103; y++) {
			int i1 = 24628 + (103 - y) * 512 * 4;
			for (int x = 1; x < 103; x++) {
				if ((tileSettingBits[z][x][y] & 0x18) == 0)
					worldController.drawTileMinimap(mapPixels, i1, z, x, y);
				if (z < 3 && (tileSettingBits[z + 1][x][y] & 8) != 0)
					worldController.drawTileMinimap(mapPixels, i1, z + 1, x, y);
				i1 += 4;
			}

		}

		int primaryColor = ((238 + (int) (Math.random() * 20D)) - 10 << 16)
				+ ((238 + (int) (Math.random() * 20D)) - 10 << 8) 
				+ ((238 + (int) (Math.random() * 20D)) - 10);
		int secondaryColor = (238 + (int) (Math.random() * 20D)) - 10 << 16;
		if (loggedIn)
			miniMap.method343();
		for (int y = 1; y < 103; y++) {
			for (int x = 1; x < 103; x++) {
				if ((tileSettingBits[z][x][y] & 0x18) == 0)
					drawMapScenes(y, primaryColor, x, secondaryColor, z);
				if (z < 3 && (tileSettingBits[z + 1][x][y] & 8) != 0)
					drawMapScenes(y, primaryColor, x, secondaryColor, z + 1);
			}

		}
		if (loggedIn)
			gameScreenIP.initDrawingArea();
		mapFunctionsLoadedAmt = 0;
		for (int xTile = 0; xTile < 104; xTile++) {
			for (int yTIle = 0; yTIle < 104; yTIle++) {
				int uid = worldController.fetchGroundDecorationNewUID(plane, xTile, yTIle);
				if (uid != 0) {
					// uid = uid >> 14 & 0x7fff;
					int functionId = ObjectDef.forID(uid).mapFunctionID;
					if (functionId >= 0) {
						int xClip = xTile;
						int yClip = yTIle;
						if (functionId != 22 && functionId != 29
								&& functionId != 34 && functionId != 36
								&& functionId != 46 && functionId != 47 && functionId != 48) {
							byte xMax = 104;
							byte yMax = 104;
							int clipData[][] = clippingPlanes[plane].clipData;
							for (int i4 = 0; i4 < 10; i4++) {
								int j4 = (int) (Math.random() * 4D);
								if (j4 == 0 && xClip > 0 && xClip > xTile - 3 && (clipData[xClip - 1][yClip] & 0x1280108) == 0)
									xClip--;
								if (j4 == 1 && xClip < xMax - 1 && xClip < xTile + 3 && (clipData[xClip + 1][yClip] & 0x1280180) == 0)
									xClip++;
								if (j4 == 2 && yClip > 0 && yClip > yTIle - 3 && (clipData[xClip][yClip - 1] & 0x1280102) == 0)
									yClip--;
								if (j4 == 3 && yClip < yMax - 1 && yClip < yTIle + 3 && (clipData[xClip][yClip + 1] & 0x1280120) == 0)
									yClip++;
							}

						}
						currentMapFunctionSprites[mapFunctionsLoadedAmt] = mapFunctions[functionId];
						mapFunctionTileX[mapFunctionsLoadedAmt] = xClip;
						mapFunctionTileY[mapFunctionsLoadedAmt] = yClip;
						mapFunctionsLoadedAmt++;
					}
				}
			}

		}

	}

	private void spawnGroundItem(int i, int j) {
		Deque itemDeque = groundArray[plane][i][j];
		if (itemDeque == null) {
			worldController.removeGroundItemFromTIle(plane, i, j);
			return;
		}
		int k = 0xfa0a1f01;
		Object toSpawn = null;
		for (Item item = (Item) itemDeque.getFront(); item != null; item = (Item) itemDeque
				.getNext()) {
			ItemDef itemDef = ItemDef.forID(item.ID);
			int l = itemDef.value;
			if (itemDef.stackable)
				l *= item.amount + 1;
			if (l > k) {
				k = l;
				toSpawn = item;
			}
		}

		itemDeque.insertFront(((Node) (toSpawn)));
		Object firstItem = null;
		Object secondItem = null;
		for (Item item_1 = (Item) itemDeque.getFront(); item_1 != null; item_1 = (Item) itemDeque
				.getNext()) {
			if (item_1.ID != ((Item) (toSpawn)).ID && firstItem == null)
				firstItem = item_1;
			if (item_1.ID != ((Item) (toSpawn)).ID
					&& item_1.ID != ((Item) (firstItem)).ID
					&& secondItem == null)
				secondItem = item_1;
		}

		int i1 = i + (j << 7) + 0x60000000;
		worldController.addGroundItemTile(i, i1, ((Animable) (firstItem)),
				getFloorDrawHeight(plane, j * 128 + 64, i * 128 + 64),
				((Animable) (secondItem)), ((Animable) (toSpawn)), plane, j);
	}

	private void processNpcs(boolean flag) {
		for (int j = 0; j < npcCount; j++) {
			NPC npc = npcArray[npcIndices[j]];
			int k = 0x20000000 + (npcIndices[j] << 14);
			if (npc == null || !npc.isVisible()
					|| npc.desc.hasRenderPriority != flag)
				continue;
			int l = npc.x >> 7;
			int i1 = npc.y >> 7;
			if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104)
				continue;
			if (npc.boundDim == 1 && (npc.x & 0x7f) == 64
					&& (npc.y & 0x7f) == 64) {
				if (tileCycleMap[l][i1] == sceneCycle)
					continue;
				tileCycleMap[l][i1] = sceneCycle;
			}
			if (!npc.desc.clickable)
				k += 0x80000000;
			worldController.addMutipleTileEntity(plane, npc.anInt1552,
					getFloorDrawHeight(plane, npc.y, npc.x), k, npc.y,
					(npc.boundDim - 1) * 64 + 60, npc.x, npc, npc.aBoolean1541);
		}
	}

	private void loadError() {
		String s = "ondemand";// was a constant parameter
		System.out.println(s);
		try {
			getAppletContext().showDocument(
					new URL(getCodeBase(), "loaderror_" + s + ".html"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		do
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		while (true);
	}

	public void drawHoverBox2(int xPos, int yPos, String text) {
		String[] results = text.split("\n");
		int height = (results.length * 16) + 6;
		int width;
		width = chatTextDrawingArea.getStringEffectsWidth(results[0]) + 6;
		for (int i = 1; i < results.length; i++)
			if (width <= chatTextDrawingArea.getStringEffectsWidth(results[i]) + 6)
				width = chatTextDrawingArea.getStringEffectsWidth(results[i]) + 6;
		DrawingArea.drawPixels(height + 2, yPos - 1, xPos - 1, 0xFFFFFF,
				width + 2);
		DrawingArea.drawPixels(height, yPos, xPos, 0x1E1F1F, width);
		yPos += 14;
		for (int i = 0; i < results.length; i++) {
			drawingArea.drawShadowedString(false, xPos + 15, 0xFFFFFF, results[i],
					yPos + 1);
			yPos += 16;
		}
	}

	public void drawTooltip(int x, int y, String[] text, boolean error, int info) {
		//boolean verified = getRegister().verified[info];
		if (text != null/* || (error && verified)*/) {
			if (error/* && verified*/) {
				text = new String[] { "This field is valid." };
			}
			int width = 0;
			for (int index = 0; index < text.length; index++) {
				if (newSmallFont.getTextWidth(text[index]) > width) {
					width = newSmallFont.getTextWidth(text[index]) + 10;
				}
			}
			int height = (text.length * 15) + 8;
			DrawingArea474.drawRoundedRectangle(x, y, width, height, 0, 150,
					true, false);
			for (int index = 0; index < text.length; index++) {
				newSmallFont.drawBasicString(text[index], x + 5, y + 15
						+ (15 * index), 0xffffff, 0);
			}
		}
	}

	public void drawHoverBox(int xPos, int yPos, String text) {
		if(text == null || text == "")
			return;
		String[] results = text.split("\n");
		int height = (results.length * 16) + 6;
		int width;
		width = newRegularFont.getTextWidth(results[0]) + 6;
		for (int i = 1; i < results.length; i++)
			if (width <= newRegularFont.getTextWidth(results[i]) + 6)
				width = newRegularFont.getTextWidth(results[i]) + 6;
		DrawingArea.drawPixels(height, yPos, xPos, 0xFFFFA0, width);
		DrawingArea.fillPixels(xPos, width, height, 0, yPos);
		yPos += 14;
		for (int i = 0; i < results.length; i++) {
			//drawingArea.drawShadowedString(false, xPos + 15, 0, results[i], yPos + 1);
			//newRegularFont.drawShadowedStringNormal(0xffffff, xPos + 15, results[i], yPos + 1, false);
			newRegularFont.drawBasicString(results[i], xPos + 15, yPos + 1, 0xffffff, 0x000000);
			yPos += 16;
		}
	}

	public void drawHoverBox(int xPos, int yPos, int color, int color2, String text) {
		String[] results = text.split("\n");
		int height = (results.length * 16) + 6;
		int width;
		//width = chatTextDrawingArea.getStringEffectsWidth(results[0]) + 6;
		width = newRegularFont.getTextWidth(results[0]) + 6;
		for (int i = 1; i < results.length; i++)
			if (width <= newRegularFont.getTextWidth(results[i]) + 6)
				width = newRegularFont.getTextWidth(results[i]) + 6;
		if (xPos + width > clientWidth)
			xPos -= width + 10;
		if (yPos + height > clientHeight)
			yPos -= height + 10;
		DrawingArea.drawPixels(height, yPos, xPos, color, width);
		DrawingArea.fillPixels(xPos, width, height, color2, yPos);
		yPos += 14;
		for (int i = 0; i < results.length; i++) {
			//drawingArea.drawShadowedString(false, xPos + 5, color2, results[i], yPos + 1);
			newRegularFont.drawBasicString(results[i], xPos + 5, yPos + 1, color2, 0x000000);
			yPos += 16;
		}
	}

	private int hoverSpriteId = -1;

	private void buildInterfaceMenu(int interfaceX, RSInterface class9,
			int mouseX, int interfaceY, int mouseY, int scrollOffset) {
		if (class9.type != 0 || class9.children == null
				|| class9.interfaceShown)
			return;
		if (mouseX < interfaceX || mouseY < interfaceY
				|| mouseX > interfaceX + class9.width
				|| mouseY > interfaceY + class9.height)
			return;
		int totalChildrens = class9.children.length;
		for (int frameID = 0; frameID < totalChildrens; frameID++) {
			int childX = class9.childX[frameID] + interfaceX;
			int childY = (class9.childY[frameID] + interfaceY) - scrollOffset;
			RSInterface child = RSInterface.interfaceCache[class9.children[frameID]];
			childX += child.xOffset;
			childY += child.yOffset;
			if(super.clickMode3 != 0) {
				mouseX = super.clickX;
				mouseY = super.clickY;
			}
			if ((child.hoverType >= 0 || child.disabledMouseOverColor != 0)
					&& mouseX >= childX && mouseY >= childY
					&& mouseX < childX + child.width
					&& mouseY < childY + child.height) {
				if (child.hoverType >= 0) {
					hoveredInterface = child.hoverType;
					hoverSpriteId = hoveredInterface;
				} else {
					hoveredInterface = child.id;
					hoverSpriteId = hoveredInterface;
				}
			}
			if (child.type == 8 || child.type == 9 || child.type == 10
					&& mouseX >= childX && mouseY >= childY
					&& mouseX < childX + child.width
					&& mouseY < childY + child.height) {
				anInt1315 = child.id;
			}
			if (child.type == 0) {
				buildInterfaceMenu(childX, child, mouseX, childY, mouseY,
						child.scrollPosition);
				if (child.scrollMax > child.height)
					scrollInterface(childX + child.width, child.height, mouseX,
							mouseY, child, childY, true, child.scrollMax);
			} else {
				if (child.atActionType == 1 && mouseX >= childX
						&& mouseY >= childY && mouseX < childX + child.width
						&& mouseY < childY + child.height) {
					boolean flag = false;
					boolean flag1 = false;
					if (child.contentType != 0)
						flag = buildFriendsListMenu(child);
					if (child.tooltip.startsWith("[CC]") || child.tooltip.startsWith("[GE]")) {
						flag1 = true;
						if (!child.tooltip.startsWith("[GE]"))
							clanName = RSInterface.interfaceCache[child.id - 800].message;
					}
					if (!flag && !flag1) {
						String tooltip = child.tooltip;
						if (myRights > 1) {
							tooltip += "Id: " + child.id + " img1: "
									+ child.disabledSpriteId + " img2:"
									+ child.enabledSpriteId;
						}
						menuActionName[menuActionRow] = tooltip;
						menuActionID[menuActionRow] = 315;
						menuActionCmd3[menuActionRow] = child.id;
						menuActionRow++;
					}
					if (flag1 && !child.tooltip.startsWith("[GE]")) {
						if (RSInterface.interfaceCache[child.id - 800].message != "") {
							menuActionName[menuActionRow] = "General";
							menuActionID[menuActionRow] = 1321;
							menuActionCmd3[menuActionRow] = child.id;
							menuActionRow++;
							menuActionName[menuActionRow] = "Captain";
							menuActionID[menuActionRow] = 1320;
							menuActionCmd3[menuActionRow] = child.id;
							menuActionRow++;
							menuActionName[menuActionRow] = "Lieutenant";
							menuActionID[menuActionRow] = 1319;
							menuActionCmd3[menuActionRow] = child.id;
							menuActionRow++;
							menuActionName[menuActionRow] = "Sergeant";
							menuActionID[menuActionRow] = 1318;
							menuActionCmd3[menuActionRow] = child.id;
							menuActionRow++;
							menuActionName[menuActionRow] = "Corporal";
							menuActionID[menuActionRow] = 1317;
							menuActionCmd3[menuActionRow] = child.id;
							menuActionRow++;
							menuActionName[menuActionRow] = "Recruit";
							menuActionID[menuActionRow] = 1316;
							menuActionCmd3[menuActionRow] = child.id;
							menuActionRow++;
							menuActionName[menuActionRow] = "Not ranked";
							menuActionID[menuActionRow] = 1315;
							menuActionCmd3[menuActionRow] = child.id;
							menuActionRow++;
						}
					}
				}
				if (child.atActionType == 2 && spellSelected == 0
						&& mouseX >= childX && mouseY >= childY
						&& mouseX < childX + child.width
						&& mouseY < childY + child.height) {
					String s = child.selectedActionName;
					if (s.indexOf(" ") != -1)
						s = s.substring(0, s.indexOf(" "));
					if (child.spellName.endsWith("Rush")
							|| child.spellName.endsWith("Burst")
							|| child.spellName.endsWith("Blitz")
							|| child.spellName.endsWith("Barrage")
							|| child.spellName.endsWith("strike")
							|| child.spellName.endsWith("bolt")
							|| child.spellName.equals("Crumble undead")
							|| child.spellName.endsWith("blast")
							|| child.spellName.endsWith("wave")
							|| child.spellName.equals("Claws of Guthix")
							|| child.spellName.equals("Flames of Zamorak")
							|| child.spellName.equals("Magic Dart")) {
						menuActionName[menuActionRow] = "Autocast @gre@"
								+ child.spellName;
						menuActionID[menuActionRow] = 104;
						menuActionCmd3[menuActionRow] = child.id;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = s + " @gre@"
							+ child.spellName;
					menuActionID[menuActionRow] = 626;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
				}
				if (child.atActionType == 3 && mouseX >= childX
						&& mouseY >= childY && mouseX < childX + child.width
						&& mouseY < childY + child.height) {
					menuActionName[menuActionRow] = "Close";
					menuActionID[menuActionRow] = 200;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
				}
				if (child.atActionType == 4 && mouseX >= childX
						&& mouseY >= childY && mouseX < childX + child.width
						&& mouseY < childY + child.height) {
					// System.out.println("2"+class9_1.tooltip + ", " +
					// class9_1.interfaceID);
					menuActionName[menuActionRow] = child.tooltip + ", "
							+ child.id;
					menuActionID[menuActionRow] = 169;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
					if (child.hoverText != null) {
						// drawHoverBox(k, l, class9_1.hoverText);
						// System.out.println("DRAWING INTERFACE: " +
						// class9_1.hoverText);
					}
				}
				if (child.atActionType == 5 && mouseX >= childX
						&& mouseY >= childY && mouseX < childX + child.width
						&& mouseY < childY + child.height) {
					menuActionName[menuActionRow] = child.tooltip
							+ ((myRights != 0) ? ", @gre@(@whi@" + child.id + "@gre@)" : "");
					menuActionID[menuActionRow] = 646;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
				}
				if (child.atActionType == 6 && !dialogueOptionsShowing
						&& mouseX >= childX && mouseY >= childY
						&& mouseX < childX + child.width
						&& mouseY < childY + child.height) {
					menuActionName[menuActionRow] = child.tooltip + ", "
							+ child.id;
					menuActionID[menuActionRow] = 679;
					menuActionCmd3[menuActionRow] = child.id;
					menuActionRow++;
				}
				if (child.type == 2) {
					int ptr = 0;
					for (int height = 0; height < child.height; height++) {
						for (int width = 0; width < child.width; width++) {
							int itemX = childX + width
									* (32 + child.invSpritePadX);
							int itemY = childY + height
									* (32 + child.invSpritePadY);
							if (ptr < 20 || child.id > 20000) {
								itemX += child.spritesX[ptr];
								itemY += child.spritesY[ptr];
							}
							if (mouseX >= itemX && mouseY >= itemY
									&& mouseX < itemX + 32
									&& mouseY < itemY + 32) {
								mouseInvInterfaceIndex = ptr;
								lastActiveInvInterface = child.id;
								if (child.inv[ptr] > 0) {
									ItemDef itemDef = ItemDef
											.forID(child.inv[ptr] - 1);
									if (itemSelected == 1
											&& child.isInventoryInterface) {
										if (child.id != lastItemSelectedInterface
												|| ptr != lastItemSelectedSlot) {
											menuActionName[menuActionRow] = "Use "
													+ selectedItemName
													+ " with @lre@"
													+ itemDef.name;
											menuActionID[menuActionRow] = 870;
											menuActionCmd1[menuActionRow] = itemDef.ID;
											menuActionCmd2[menuActionRow] = ptr;
											menuActionCmd3[menuActionRow] = child.id;
											menuActionRow++;
										}
									} else if (spellSelected == 1
											&& child.isInventoryInterface) {
										if ((spellUsableOn & 0x10) == 16) {
											menuActionName[menuActionRow] = spellTooltip
													+ " @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 543;
											menuActionCmd1[menuActionRow] = itemDef.ID;
											menuActionCmd2[menuActionRow] = ptr;
											menuActionCmd3[menuActionRow] = child.id;
											menuActionRow++;
										}
									} else {
										if (child.isInventoryInterface) {
											for (int l3 = 4; l3 >= 3; l3--)
												if (itemDef.actions != null
														&& itemDef.actions[l3] != null) {
													if (openInterfaceID != 24700) {
														menuActionName[menuActionRow] = itemDef.actions[l3]
																+ " @lre@"
																+ itemDef.name;
														if (l3 == 3)
															menuActionID[menuActionRow] = 493;
														if (l3 == 4)
															menuActionID[menuActionRow] = 847;
														menuActionCmd1[menuActionRow] = itemDef.ID;
														menuActionCmd2[menuActionRow] = ptr;
														menuActionCmd3[menuActionRow] = child.id;
														menuActionRow++;
													}
												} else if (l3 == 4) {
													if (openInterfaceID != 24700) {
														menuActionName[menuActionRow] = "Drop @lre@"
																+ itemDef.name;
														menuActionID[menuActionRow] = 847;
														menuActionCmd1[menuActionRow] = itemDef.ID;
														menuActionCmd2[menuActionRow] = ptr;
														menuActionCmd3[menuActionRow] = child.id;
														menuActionRow++;
													}
												}

										}
										if (child.usableItemInterface) {
											if (openInterfaceID == 24700) {
												menuActionName[menuActionRow] = "Offer @lre@"
														+ itemDef.name;
												menuActionID[menuActionRow] = 454;
												menuActionCmd1[menuActionRow] = itemDef.ID;
											} else {
												menuActionName[menuActionRow] = "Use @lre@"
														+ itemDef.name;
												menuActionID[menuActionRow] = 447;
												menuActionCmd1[menuActionRow] = itemDef.ID;
											}
											// k2 = inventory spot
											// System.out.println(k2);
											menuActionCmd2[menuActionRow] = ptr;
											menuActionCmd3[menuActionRow] = child.id;
											menuActionRow++;
										}
										if (child.isInventoryInterface
												&& itemDef.actions != null) {
											for (int i4 = 2; i4 >= 0; i4--)
												if (itemDef.actions[i4] != null) {
													if (openInterfaceID != 24700) {
														menuActionName[menuActionRow] = itemDef.actions[i4]
																+ " @lre@"
																+ itemDef.name;
														if (i4 == 0)
															menuActionID[menuActionRow] = 74;
														if (i4 == 1)
															menuActionID[menuActionRow] = 454;
														if (i4 == 2)
															menuActionID[menuActionRow] = 539;
														menuActionCmd1[menuActionRow] = itemDef.ID;
														menuActionCmd2[menuActionRow] = ptr;
														menuActionCmd3[menuActionRow] = child.id;
														menuActionRow++;
													}
												}

										}
										boolean m = false;
										m = false;
										if (child.actions != null) {
											for (int j4 = 4; j4 >= 0; j4--)
												if (child.actions[j4] != null) {
													if (child.actions[j4] != "[GE]") {
														if (child.actions[j4] != "[ITEM]Collect"
																&& child.actions[j4] != "[COINS]Collect") {
															menuActionName[menuActionRow] = child.actions[j4]
																	+ " @lre@"
																	+ itemDef.name;
															if (j4 == 0)
																menuActionID[menuActionRow] = 632;
															if (j4 == 1)
																menuActionID[menuActionRow] = 78;
															if (j4 == 2)
																menuActionID[menuActionRow] = 867;
															if (j4 == 3)
																menuActionID[menuActionRow] = 431;
															if (j4 == 4)
																menuActionID[menuActionRow] = 53;
															menuActionCmd1[menuActionRow] = itemDef.ID;
															menuActionCmd2[menuActionRow] = ptr;
															menuActionCmd3[menuActionRow] = child.id;
															menuActionRow++;
														} else {
															menuActionName[menuActionRow] = "Collect @lre@"
																	+ itemDef.name;

															if (openInterfaceID == 54700) {
																if (child.actions[j4]
																		.startsWith("[ITEM]")) {
																	menuActionID[menuActionRow] = 889;
																} else if (child.actions[j4]
																		.startsWith("[COINS]")) {
																	menuActionID[menuActionRow] = 888;
																}
															}
															if (openInterfaceID == 53700) {
																if (child.actions[j4]
																		.startsWith("[ITEM]")) {
																	menuActionID[menuActionRow] = 890;
																} else if (child.actions[j4]
																		.startsWith("[COINS]")) {
																	menuActionID[menuActionRow] = 891;
																}
															}
															menuActionName[menuActionRow] = "Collect @lre@"
																	+ itemDef.name;
															menuActionCmd1[menuActionRow] = itemDef.ID;
															menuActionCmd2[menuActionRow] = ptr;
															menuActionCmd3[menuActionRow] = child.id;
															menuActionRow++;
														}
													} else {
														m = true;
													}
												}

										}
										if (m != true) {
											if (child.id == 38274)
												menuActionName[menuActionRow] = "Build @lre@"
														+ itemDef.name;
											else
												menuActionName[menuActionRow] = "Examine @lre@"
														+ itemDef.name;
											menuActionID[menuActionRow] = 1125;
											menuActionCmd1[menuActionRow] = itemDef.ID;
											menuActionCmd2[menuActionRow] = ptr;
											menuActionCmd3[menuActionRow] = child.id;
											menuActionRow++;
										}
									}
								}
							}
							ptr++;
						}

					}

				}
			}
		}

	}

	private void drawScrollbar(int barHeight, int scrollPos, int yPos,
			int xPos, int contentHeight, boolean newScroller,
			boolean isTransparent) {
		int backingAmount = (barHeight - 32) / 5;
		int scrollPartHeight = ((barHeight - 32) * barHeight) / contentHeight;
		int scrollerID;
		if (newScroller) {
			scrollerID = 4;
		} else if (isTransparent) {
			scrollerID = 8;
		} else {
			scrollerID = 0;
		}
		if (scrollPartHeight < 10)
			scrollPartHeight = 10;
		int scrollPartAmount = (scrollPartHeight / 5) - 2;
		int scrollPartPos = ((barHeight - 32 - scrollPartHeight) * scrollPos)
				/ (contentHeight - barHeight) + 16 + yPos;
		/* Bar fill */
		for (int i = 0, yyPos = yPos + 16; i <= backingAmount; i++, yyPos += 5) {
			scrollPart[scrollerID + 1].drawSprite(xPos, yyPos);
		}
		/* Top of bar */
		scrollPart[scrollerID + 2].drawSprite(xPos, scrollPartPos);
		scrollPartPos += 5;
		/* Middle of bar */
		for (int i = 0; i <= scrollPartAmount; i++) {
			scrollPart[scrollerID + 3].drawSprite(xPos, scrollPartPos);
			scrollPartPos += 5;
		}
		scrollPartPos = ((barHeight - 32 - scrollPartHeight) * scrollPos)
				/ (contentHeight - barHeight) + 16 + yPos
				+ (scrollPartHeight - 5);
		/* Bottom of bar */
		scrollPart[scrollerID].drawSprite(xPos, scrollPartPos);
		/* Arrows */
		if (newScroller) {
			scrollBar[2].drawSprite(xPos, yPos);
			scrollBar[3].drawSprite(xPos, (yPos + barHeight) - 16);
		} else if (isTransparent) {
			scrollBar[4].drawSprite(xPos, yPos);
			scrollBar[5].drawSprite(xPos, (yPos + barHeight) - 16);
		} else {
			scrollBar[0].drawSprite(xPos, yPos);
			scrollBar[1].drawSprite(xPos, (yPos + barHeight) - 16);
		}
	}

	private void updateNPCs(Stream stream, int i) {
		entityUpdateCount = 0;
		playersToUpdateCount = 0;
		updateNPCAmount(stream);
		updateNPCMovement(i, stream);
		readNPCUpdateMask(stream);
		for (int ptr = 0; ptr < entityUpdateCount; ptr++) {
			int l = anIntArray840[ptr];
			if (npcArray[l] == null)
				continue;
			if (npcArray[l].loopCycle != loopCycle) {
				npcArray[l].desc = null;
				npcArray[l] = null;
			}
		}

		if (stream.currentOffset != i) {
			signlink.reporterror(myUsername
					+ " size mismatch in getnpcpos - pos:"
					+ stream.currentOffset + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for (int i1 = 0; i1 < npcCount; i1++)
			if (npcArray[npcIndices[i1]] == null) {
				signlink.reporterror(myUsername
						+ " null entry in npc list - pos:" + i1 + " size:"
						+ npcCount);
				throw new RuntimeException("eek");
			}

	}

	private int cButtonHPos;
	private int cButtonCPos;
	private boolean menuToggle = false;

	private void handleActions(int configID) {
		int action = 0;
		if (configID < Varp.cache.length)
			action = Varp.cache[configID].usage;
		if (action == 0)
			return;
		int config = variousSettings[configID];
		if (action == 1) {
			if (!forcedShadow) {
				if (config == 1) {
					Rasterizer.calculatePalette(0.90000000000000002D, true);
					currentShadow = 0.90000000000000002D;
				}
				if (config == 2) {
					Rasterizer.calculatePalette(0.80000000000000004D, true);
					currentShadow = 0.80000000000000004D;
				}
				if (config == 3) {
					Rasterizer.calculatePalette(0.69999999999999996D, true);
					currentShadow = 0.69999999999999996D;
				}
				if (config == 4) {
					Rasterizer.calculatePalette(0.59999999999999998D, true);
					currentShadow = 0.59999999999999998D;
				}
				ItemDef.spriteCache.clear();
				welcomeScreenRaised = true;
			}
		}
		/*if (action == 3) {
			boolean music = musicEnabled;
			if (config == 0) {
				adjustVolume(musicEnabled, 500);
				musicEnabled = true;
			}
			if (config == 1) {
				adjustVolume(musicEnabled, 300);
				musicEnabled = true;
			}
			if (config == 2) {
				adjustVolume(musicEnabled, 100);
				musicEnabled = true;
			}
			if (config == 3) {
				adjustVolume(musicEnabled, 0);
				musicEnabled = true;
			}
			if (config == 4) {
				musicEnabled = false;
			}
			if (musicEnabled != music) {
				if (musicEnabled) {
					nextSong = currentSong;
					songChanging = true;
					onDemandFetcher.requestFileData(2, nextSong);
				} else {
					stopMidi();
				}
				prevSong = 0;
			}
		}*/
		if (action == 4) {
			SoundPlayer.setVolume(config);
			if (config == 0) {
				soundEnabled = true;
				setWaveVolume(0);
			}
			if (config == 1) {
				soundEnabled = true;
				setWaveVolume(-400);
			}
			if (config == 2) {
				soundEnabled = true;
				setWaveVolume(-800);
			}
			if (config == 3) {
				soundEnabled = true;
				setWaveVolume(-1200);
			}
			if (config == 4) {
				soundEnabled = false;
			}
		}
		if (action == 5)
			useOneMouseButton = config;
		if (action == 6)
			showSpokenEffects = config;
		if (action == 7)
			running = !running;
		if (action == 8) {
			splitPrivateChat = config;
			inputTaken = true;
		}
		if (action == 9)
			anInt913 = config;
	}

	private void updateEntities() {
		try {
			int currentSpeakIndex = 0;
			for (int j = -1; j < playerCount + npcCount; j++) {
				Object entity;
				if (j == -1)
					entity = myPlayer;
				else if (j < playerCount)
					entity = playerArray[playerIndices[j]];
				else
					entity = npcArray[npcIndices[j - playerCount]];
				if (entity == null || !((Entity) (entity)).isVisible())
					continue;
				if (entity instanceof NPC) {
					NPCDef entityDef = ((NPC) entity).desc;
					if (entityDef.childrenIDs != null)
						entityDef = entityDef.getAlteredNPCDef();
					if (entityDef == null)
						continue;
				}
				if (j < playerCount) {
					int l = 30;
					Player player = (Player) entity;
					if (player.headIcon >= 0) {
						npcScreenPos(((Entity) (entity)),
								((Entity) (entity)).height + 15);
						if (spriteDrawX > -1) {
							if (player.skullIcon < 2) {
								skullIcons[player.skullIcon].drawSprite(
										spriteDrawX - 12, spriteDrawY - l);
								l += 24;
							}
							if (player.headIcon < 20) {
								headIcons[player.headIcon].drawSprite(
										spriteDrawX - 12, spriteDrawY - l);
								l += 18;
							}
						}
					}
					if (j >= 0 && markType == 10
							&& markedPlayer == playerIndices[j]) {
						npcScreenPos(((Entity) (entity)),
								((Entity) (entity)).height + 15);
						if (spriteDrawX > -1)
							headIconsHint[player.hintIcon].drawSprite(
									spriteDrawX - 12, spriteDrawY - l);
					}
				} else {
					NPCDef entityDef_1 = ((NPC) entity).desc;
					if (entityDef_1.headIcon >= 0 && entityDef_1.headIcon < headIcons.length) {
						npcScreenPos(((Entity) (entity)), ((Entity) (entity)).height + 15);
						if (spriteDrawX > -1)
							headIcons[entityDef_1.headIcon].drawSprite(spriteDrawX - 12, spriteDrawY - 30);
					}
					if (markType == 1 && markedNPC == npcIndices[j - playerCount] && loopCycle % 20 < 10) {
						npcScreenPos(((Entity) (entity)), ((Entity) (entity)).height + 15);
						if (spriteDrawX > -1)
							headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
					}
				}
				if (((Entity) (entity)).textSpoken != null && (j >= playerCount || publicChatMode == 0 || publicChatMode == 3 || publicChatMode == 1
								&& isFriendOrSelf(((Player) entity).name))) {
					npcScreenPos(((Entity) (entity)), ((Entity) (entity)).height);
					if (spriteDrawX > -1 && currentSpeakIndex < speakAmountsOnScreen) {
						spokenOffsetX[currentSpeakIndex] = chatTextDrawingArea.getStringWidth(((Entity) (entity)).textSpoken) / 2;
						spokenOffsetY[currentSpeakIndex] = chatTextDrawingArea.characterDefaultHeight;
						spokenX[currentSpeakIndex] = spriteDrawX;
						spokenY[currentSpeakIndex] = spriteDrawY;
						spokenColor[currentSpeakIndex] = ((Entity) (entity)).chatColor;
						spokenEffect[currentSpeakIndex] = ((Entity) (entity)).chatEffect;
						spokenCycle[currentSpeakIndex] = ((Entity) (entity)).textCycle;
						spokenMessage[currentSpeakIndex++] = ((Entity) (entity)).textSpoken;
						if (showSpokenEffects == 0 && ((Entity) (entity)).chatEffect >= 1 && ((Entity) (entity)).chatEffect <= 3) {
							spokenOffsetY[currentSpeakIndex] += 10;
							spokenY[currentSpeakIndex] += 5;
						}
						if (showSpokenEffects == 0 && ((Entity) (entity)).chatEffect == 4)
							spokenOffsetX[currentSpeakIndex] = 60;
						if (showSpokenEffects == 0 && ((Entity) (entity)).chatEffect == 5)
							spokenOffsetY[currentSpeakIndex] += 5;
					}
				}
				if (((Entity) (entity)).loopCycleStatus > loopCycle) {
					try {
						npcScreenPos(((Entity) (entity)),
								((Entity) (entity)).height + 15);
						if (spriteDrawX > -1) {
							int i1 = (((Entity) (entity)).currentHealth * 30) / ((Entity) (entity)).maxHealth;
							if (i1 > 30)
								i1 = 30;
							int hpPercent = (((Entity) (entity)).currentHealth * 90)
									/ ((Entity) (entity)).maxHealth;
							if (hpPercent > 90)
								hpPercent = 90;
							int HpPercent = (((Entity) (entity)).currentHealth * 56) / ((Entity) (entity)).maxHealth;
							if (HpPercent > 56)
								HpPercent = 56;
							if(options.get("new_hitbar")) {
								SpriteCache.spriteCache[34].drawSprite(spriteDrawX - 28, spriteDrawY - 5);
								Sprite s = Sprite.getCutted(SpriteCache.spriteCache[33], HpPercent, SpriteCache.spriteCache[33].myHeight);
								s.drawSprite(spriteDrawX - 28, spriteDrawY - 5);
							} else {
								if(hpPercent > 30)
									hpPercent = 30;
								DrawingArea.drawPixels(5, spriteDrawY - 5, spriteDrawX - 15, 0xFF0000, 30);
								DrawingArea.drawPixels(5, spriteDrawY - 5, spriteDrawX - 15, 0x00FF00, i1);
							}
							/*
							 * if (obj instanceof NPC && ((NPC) obj).maxHealth
							 * >= 2500) {
							 * SpriteCache.spriteCache[35].drawSprite(
							 * spriteDrawX - 44, spriteDrawY - 5);
							 * SpriteCache.spriteCache[33] =
							 * Sprite.getResizedSprite
							 * (SpriteCache.spriteCache[33], HpPercent-2,
							 * SpriteCache.spriteCache[33].myHeight);
							 * SpriteCache
							 * .spriteCache[33].drawSprite(spriteDrawX - 44,
							 * spriteDrawY - 5); }
							 */
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (int j1 = 0; j1 < 4; j1++)
					if (((Entity) (entity)).hitsLoopCycle[j1] > loopCycle) {
						npcScreenPos(((Entity) (entity)), ((Entity) (entity)).height / 2);
						if (spriteDrawX > -1) {
							Entity e = ((Entity) (entity));
							if (e.moveTimer[j1] == 0) {
								if (e.hitmarkMove[j1] > -14)
									e.hitmarkMove[j1]--;
								e.moveTimer[j1] = 2;
							} else {
								e.moveTimer[j1]--;
							}
							if (e.hitmarkMove[j1] <= -14)
								e.hitmarkTrans[j1] -= 10;
							hitmarkDraw(e, String.valueOf(e.hitArray[j1]).length(), e.hitMarkTypes[j1],
									e.hitIcon[j1], e.hitArray[j1],
									e.soakDamage[j1], e.hitmarkMove[j1],
									e.hitmarkTrans[j1], j1, (entity instanceof Player) ? (((Player) entity) == myPlayer) : false);
						}
					}
			}
			for (int speakIndex = 0; speakIndex < currentSpeakIndex; speakIndex++) {
				int speakX = spokenX[speakIndex];
				int speakY = spokenY[speakIndex];
				int speakXOffset = spokenOffsetX[speakIndex];
				int speakYOffset = spokenOffsetY[speakIndex];
				boolean flag = true;
				while (flag) {
					flag = false;
					for (int speak = 0; speak < speakIndex; speak++)
						if (speakY + 2 > spokenY[speak] - spokenOffsetY[speak] && speakY - speakYOffset < spokenY[speak] + 2
								&& speakX - speakXOffset < spokenX[speak] + spokenOffsetX[speak]
								&& speakX + speakXOffset > spokenX[speak] - spokenOffsetX[speak]
								&& spokenY[speak] - spokenOffsetY[speak] < speakY) {
							speakY = spokenY[speak] - spokenOffsetY[speak];
							flag = true;
						}

				}
				spriteDrawX = spokenX[speakIndex];
				spriteDrawY = spokenY[speakIndex] = speakY;
				String s = spokenMessage[speakIndex];
				if (showSpokenEffects == 0) {
					int i3 = 0xffff00;
					if (spokenColor[speakIndex] < 6)
						i3 = spoken_palette[spokenColor[speakIndex]];
					if (spokenColor[speakIndex] == 6)
						i3 = sceneCycle % 20 >= 10 ? 0xffff00 : 0xff0000;
					if (spokenColor[speakIndex] == 7)
						i3 = sceneCycle % 20 >= 10 ? 65535 : 255;
					if (spokenColor[speakIndex] == 8)
						i3 = sceneCycle % 20 >= 10 ? 0x80ff80 : 45056;
					if (spokenColor[speakIndex] == 9) {
						int j3 = 150 - spokenCycle[speakIndex];
						if (j3 < 50)
							i3 = 0xff0000 + 1280 * j3;
						else if (j3 < 100)
							i3 = 0xffff00 - 0x50000 * (j3 - 50);
						else if (j3 < 150)
							i3 = 65280 + 5 * (j3 - 100);
					}
					if (spokenColor[speakIndex] == 10) {
						int k3 = 150 - spokenCycle[speakIndex];
						if (k3 < 50)
							i3 = 0xff0000 + 5 * k3;
						else if (k3 < 100)
							i3 = 0xff00ff - 0x50000 * (k3 - 50);
						else if (k3 < 150)
							i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
					}
					if (spokenColor[speakIndex] == 11) {
						int l3 = 150 - spokenCycle[speakIndex];
						if (l3 < 50)
							i3 = 0xffffff - 0x50005 * l3;
						else if (l3 < 100)
							i3 = 65280 + 0x50005 * (l3 - 50);
						else if (l3 < 150)
							i3 = 0xffffff - 0x50000 * (l3 - 100);
					}
					if (spokenEffect[speakIndex] == 0) {
						chatTextDrawingArea.drawStringCenter(0, s, spriteDrawY + 1, spriteDrawX);
						chatTextDrawingArea.drawStringCenter(i3, s, spriteDrawY, spriteDrawX);
						//newBoldFont.drawCenteredString(s, spriteDrawX, spriteDrawY, i3, 0);
					}
					if (spokenEffect[speakIndex] == 1) {
						chatTextDrawingArea.drawCenteredStringWaveY(0, s, spriteDrawX, sceneCycle, spriteDrawY + 1);
						chatTextDrawingArea.drawCenteredStringWaveY(i3, s, spriteDrawX, sceneCycle, spriteDrawY);
						//newBoldFont.drawCenteredStringMoveXY(s, spriteDrawX, spriteDrawY, i3, 0, sceneCycle);
					}
					if (spokenEffect[speakIndex] == 2) {
						chatTextDrawingArea.drawCeneteredStringWaveXY(spriteDrawX, s, sceneCycle, spriteDrawY + 1, 0);
						chatTextDrawingArea.drawCeneteredStringWaveXY(spriteDrawX, s, sceneCycle, spriteDrawY, i3);
					}
					if (spokenEffect[speakIndex] == 3) {
						chatTextDrawingArea.drawCenteredStringWaveXYMove(150 - spokenCycle[speakIndex], s, sceneCycle, spriteDrawY + 1, spriteDrawX, 0);
						chatTextDrawingArea.drawCenteredStringWaveXYMove(150 - spokenCycle[speakIndex], s, sceneCycle, spriteDrawY, spriteDrawX, i3);
					}
					if (spokenEffect[speakIndex] == 4) {
						int i4 = chatTextDrawingArea.getStringWidth(s);
						int k4 = ((150 - spokenCycle[speakIndex]) * (i4 + 100)) / 150;
						DrawingArea.setDrawingArea(334, spriteDrawX - 50, spriteDrawX + 50, 0);
						chatTextDrawingArea.drawString(0, s, spriteDrawY + 1, (spriteDrawX + 50) - k4);
						chatTextDrawingArea.drawString(i3, s, spriteDrawY, (spriteDrawX + 50) - k4);
						DrawingArea.defaultDrawingAreaSize();
					}
					if (spokenEffect[speakIndex] == 5) {
						int j4 = 150 - spokenCycle[speakIndex];
						int l4 = 0;
						if (j4 < 25)
							l4 = j4 - 25;
						else if (j4 > 125)
							l4 = j4 - 125;
						DrawingArea
								.setDrawingArea(spriteDrawY + 5, 0, 512,
										spriteDrawY
												- chatTextDrawingArea.characterDefaultHeight
												- 1);
						chatTextDrawingArea.drawStringCenter(0, s,
								spriteDrawY + 1 + l4, spriteDrawX);
						chatTextDrawingArea.drawStringCenter(i3, s, spriteDrawY + l4,
								spriteDrawX);
						DrawingArea.defaultDrawingAreaSize();
					}
				} else {
					chatTextDrawingArea.drawStringCenter(0, s, spriteDrawY + 1,
							spriteDrawX);
					chatTextDrawingArea.drawStringCenter(0xffff00, s, spriteDrawY,
							spriteDrawX);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void delFriend(long playerID) {
		try {
			if (playerID == 0L)
				return;
			for (int i = 0; i < friendsCount; i++) {
				if (friendsListAsLongs[i] != playerID)
					continue;
				friendsCount--;
				needDrawTabArea = true;
				inputString = "[DFR]" + friendsList[i];
				for (int j = i; j < friendsCount; j++) {
					friendsList[j] = friendsList[j + 1];
					friendsNodeIDs[j] = friendsNodeIDs[j + 1];
					friendsListAsLongs[j] = friendsListAsLongs[j + 1];
				}
				sendPacket(1003);
				stream.createFrame(215);
				stream.writeQWord(playerID);
				break;
			}
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("18622, " + false + ", " + playerID + ", "
					+ runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	private void processTextCycles() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = myPlayerIndex;
			else
				j = playerIndices[i];
			Player player = playerArray[j];
			if (player != null && player.textCycle > 0) {
				player.textCycle--;
				if (player.textCycle == 0)
					player.textSpoken = null;
			}
		}
		for (int k = 0; k < npcCount; k++) {
			int l = npcIndices[k];
			NPC npc = npcArray[l];
			if (npc != null && npc.textCycle > 0) {
				npc.textCycle--;
				if (npc.textCycle == 0)
					npc.textSpoken = null;
			}
		}
	}

	private void calcCameraPos() {
		int maxXPos = cutsceneLocalX * 128 + 64;
		int maxYPos = cutsceneLocalY * 128 + 64;
		int height = getFloorDrawHeight(plane, maxYPos, maxXPos) - cutsceneLocalZ;
		if (xCameraPos < maxXPos) {
			xCameraPos += cutsceneSpeed + ((maxXPos - xCameraPos) * cutsceneSpeedMul) / 1000;
			if (xCameraPos > maxXPos)
				xCameraPos = maxXPos;
		}
		if (xCameraPos > maxXPos) {
			xCameraPos -= cutsceneSpeed + ((xCameraPos - maxXPos) * cutsceneSpeedMul) / 1000;
			if (xCameraPos < maxXPos)
				xCameraPos = maxXPos;
		}
		if (zCameraPos < height) {
			zCameraPos += cutsceneSpeed + ((height - zCameraPos) * cutsceneSpeedMul) / 1000;
			if (zCameraPos > height)
				zCameraPos = height;
		}
		if (zCameraPos > height) {
			zCameraPos -= cutsceneSpeed + ((zCameraPos - height) * cutsceneSpeedMul) / 1000;
			if (zCameraPos < height)
				zCameraPos = height;
		}
		if (yCameraPos < maxYPos) {
			yCameraPos += cutsceneSpeed + ((maxYPos - yCameraPos) * cutsceneSpeedMul) / 1000;
			if (yCameraPos > maxYPos)
				yCameraPos = maxYPos;
		}
		if (yCameraPos > maxYPos) {
			yCameraPos -= cutsceneSpeed + ((yCameraPos - maxYPos) * cutsceneSpeedMul) / 1000;
			if (yCameraPos < maxYPos)
				yCameraPos = maxYPos;
		}
		maxXPos = cutsceneFocusLocalX * 128 + 64;
		maxYPos = cutsceneFocusLocalY * 128 + 64;
		height = getFloorDrawHeight(plane, maxYPos, maxXPos) - cutsceneFocusLocalZ;
		int xDiff = maxXPos - xCameraPos;
		int zDiff = height - zCameraPos;
		int yDiff = maxYPos - yCameraPos;
		int k1 = (int) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		int yCamCurve = (int) (Math.atan2(zDiff, k1) * 325.94900000000001D) & 0x7ff;
		int xCamCurve = (int) (Math.atan2(xDiff, yDiff) * -325.94900000000001D) & 0x7ff;
		if (yCamCurve < 128)
			yCamCurve = 128;
		if (yCamCurve > 383)
			yCamCurve = 383;
		if (yCameraCurve < yCamCurve) {
			yCameraCurve += cutsceneRotateSpeed + ((yCamCurve - yCameraCurve) * cutsceneRotateMul) / 1000;
			if (yCameraCurve > yCamCurve)
				yCameraCurve = yCamCurve;
		}
		if (yCameraCurve > yCamCurve) {
			yCameraCurve -= cutsceneRotateSpeed + ((yCameraCurve - yCamCurve) * cutsceneRotateMul) / 1000;
			if (yCameraCurve < yCamCurve)
				yCameraCurve = yCamCurve;
		}
		int xCamDiff = xCamCurve - xCameraCurve;
		if (xCamDiff > 1024)
			xCamDiff -= 2048;
		if (xCamDiff < -1024)
			xCamDiff += 2048;
		if (xCamDiff > 0) {
			xCameraCurve += cutsceneRotateSpeed + (xCamDiff * cutsceneRotateMul) / 1000;
			xCameraCurve &= 0x7ff;
		}
		if (xCamDiff < 0) {
			xCameraCurve -= cutsceneRotateSpeed + (-xCamDiff * cutsceneRotateMul) / 1000;
			xCameraCurve &= 0x7ff;
		}
		int xCamDiff_ = xCamCurve - xCameraCurve;
		if (xCamDiff_ > 1024)
			xCamDiff_ -= 2048;
		if (xCamDiff_ < -1024)
			xCamDiff_ += 2048;
		if (xCamDiff_ < 0 && xCamDiff > 0 || xCamDiff_ > 0 && xCamDiff < 0)
			xCameraCurve = xCamCurve;
	}

	private void drawMenu() {
		int menuXOffset = menuOffsetX;
		int menuYOffset = menuOffsetY;
		int width = menuWidth;
		int mouseXPos = super.mouseX;
		int mouseYPos = super.mouseY;
		int height = menuHeight + 1;
		int color = 0x5d5447;
		if (menuScreenArea == 1 && (clientSize > 0)) {
			menuXOffset += 519;// +extraWidth;
			menuYOffset += 168;// +extraHeight;
		}
		if (menuScreenArea == 2 && (clientSize > 0)) {
			menuYOffset += 338;
		}
		if (menuScreenArea == 3 && (clientSize > 0)) {
			menuXOffset += 515;
			menuYOffset += 0;
		}
		if (menuScreenArea == 0) {
			mouseXPos -= 4;
			mouseYPos -= 4;
		}
		if (menuScreenArea == 1) {
			if (!(clientSize > 0)) {
				mouseXPos -= 519;
				mouseYPos -= 168;
			}
		}
		if (menuScreenArea == 2) {
			if (!(clientSize > 0)) {
				mouseXPos -= 17;
				mouseYPos -= 338;
			}
		}
		if (menuScreenArea == 3 && !(clientSize > 0)) {
			mouseXPos -= 515;
			mouseYPos -= 0;
		}
		if (getSetting("menu") == 317) {
			DrawingArea.fillRectangle(color, menuYOffset, width, height, 150, menuXOffset);
			DrawingArea.fillRectangle(0, menuYOffset + 1, width - 2, 16, 150, menuXOffset + 1);
			DrawingArea.fillPixels(menuXOffset + 1, width - 2, height - 19, 0, menuYOffset + 18);
			DrawingArea.drawRectangle(menuYOffset + 18, height - 19, 150, 0, width - 2, menuXOffset + 1);
			//newBoldFont.drawString(0xc6b895, "Choose Option", menuYOffset + 14, menuXOffset + 3);
			newBoldFont.drawBasicString("Choose Option", menuXOffset + 3, menuYOffset + 14, 0xc6b895, 0x000000);
			for (int menuIndex = 0; menuIndex < menuActionRow; menuIndex++) {
				int yPos = menuYOffset + 31 + (menuActionRow - 1 - menuIndex) * 15;
				int menuEntreeColor = 0xffffff;
				if (mouseXPos > menuXOffset && mouseXPos < menuXOffset + width && mouseYPos > yPos - 13 && mouseYPos < yPos + 3)
					menuEntreeColor = 0xffff00;
				//boldFont.drawShadowedString(true, menuXOffset + 3, menuEntreeColor, menuActionName[menuIndex], yPos);
				newBoldFont.drawBasicString(menuActionName[menuIndex], menuXOffset + 3, yPos, menuEntreeColor, 0x000000);
			}
		} else if (getSetting("menu") == 562) {
			// DrawingArea.drawPixels(height, yPos, xPos, color, width);
			// DrawingArea.fillPixels(xPos, width, height, color, yPos);
			DrawingArea.drawPixels(height - 4, menuYOffset + 2, menuXOffset, 0x706a5e, width);
			DrawingArea.drawPixels(height - 2, menuYOffset + 1, menuXOffset + 1, 0x706a5e, width - 2);
			DrawingArea.drawPixels(height, menuYOffset, menuXOffset + 2, 0x706a5e, width - 4);
			DrawingArea.drawPixels(height - 2, menuYOffset + 1, menuXOffset + 3, 0x2d2822, width - 6);
			DrawingArea.drawPixels(height - 4, menuYOffset + 2, menuXOffset + 2, 0x2d2822, width - 4);
			DrawingArea.drawPixels(height - 6, menuYOffset + 3, menuXOffset + 1, 0x2d2822, width - 2);
			DrawingArea.drawPixels(height - 22, menuYOffset + 19, menuXOffset + 2, 0x524a3d, width - 4);
			DrawingArea.drawPixels(height - 22, menuYOffset + 20, menuXOffset + 3, 0x524a3d, width - 6);
			DrawingArea.drawPixels(height - 23, menuYOffset + 20, menuXOffset + 3, 0x2b271c, width - 6);
			DrawingArea.fillPixels(menuXOffset + 3, width - 6, 1, 0x2a291b, menuYOffset + 2);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x2a261b, menuYOffset + 3);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x252116, menuYOffset + 4);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x211e15, menuYOffset + 5);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x1e1b12, menuYOffset + 6);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x1a170e, menuYOffset + 7);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 2, 0x15120b, menuYOffset + 8);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x100d08, menuYOffset + 10);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x090a04, menuYOffset + 11);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x080703, menuYOffset + 12);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x090a04, menuYOffset + 13);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x070802, menuYOffset + 14);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x090a04, menuYOffset + 15);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x070802, menuYOffset + 16);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x090a04, menuYOffset + 17);
			DrawingArea.fillPixels(menuXOffset + 2, width - 4, 1, 0x2a291b, menuYOffset + 18);
			DrawingArea.fillPixels(menuXOffset + 3, width - 6, 1, 0x564943, menuYOffset + 19);
			//boldFont.drawString(0xc6b895, "Choose Option", menuYOffset + 14, menuXOffset + 3);
			newBoldFont.drawBasicString("Choose Option", menuXOffset + 3, menuYOffset + 14, 0xc6b895, 0x000000);
			for (int menuIndex = 0; menuIndex < menuActionRow; menuIndex++) {
				int yPos = menuYOffset + 31 + (menuActionRow - 1 - menuIndex) * 15;
				int menuEntreeColor = 0xc6b895;
				if (mouseXPos > menuXOffset && mouseXPos < menuXOffset + width && mouseYPos > yPos - 13 && mouseYPos < yPos + 3) {
					DrawingArea.drawPixels(15, yPos - 11, menuXOffset + 3, 0x6f695d, menuWidth - 6);
					menuEntreeColor = 0xeee5c6;
				}
				//boldFont.drawShadowedString(true, menuXOffset + 4, menuEntreeColor, menuActionName[menuIndex], yPos + 1);
				newBoldFont.drawBasicString(menuActionName[menuIndex], menuXOffset + 4, yPos + 1, menuEntreeColor, 0x000000);
			}
		} else if(getSetting("menu") == 718) {
			
		}
	}

	private void addFriend(long l) {
		try {
			if (l == 0L)
				return;
			if (friendsCount >= 100 && anInt1046 != 1) {
				pushMessage(
						"Your friendlist is full. Max of 100 for free users, and 200 for members",
						0, "");
				return;
			}
			if (friendsCount >= 200) {
				pushMessage(
						"Your friendlist is full. Max of 100 for free users, and 200 for members",
						0, "");
				return;
			}
			String s = TextClass.fixName(TextClass.nameForLong(l));
			for (int i = 0; i < friendsCount; i++)
				if (friendsListAsLongs[i] == l) {
					pushMessage(s + " is already on your friend list", 0, "");
					return;
				}
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					pushMessage("Please remove " + s
							+ " from your ignore list first", 0, "");
					return;
				}
			if (s.equals(myPlayer.name))
				return;
			else {
				friendsList[friendsCount] = s;
				friendsListAsLongs[friendsCount] = l;
				friendsNodeIDs[friendsCount] = 0;
				friendsCount++;
				needDrawTabArea = true;
				stream.createFrame(188);
				stream.writeQWord(l);
				inputString = "[FRI]" + s;
				sendPacket(1003);
				int slot = 44001;
				for (int a = 44001; a <= 44200; a++) {
					sendFrame126("", slot);
					slot++;
				}
				slot = 44801;
				for (int d = 44801; d <= 45000; d++) {
					sendFrame126("", slot);
					slot++;
				}
				return;
			}
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("15283, " + (byte) 68 + ", " + l + ", "
					+ runtimeexception.toString());
		}
		// throw new RuntimeException();
	}

	private int getFloorDrawHeight(int i, int j, int k) {
		int l = k >> 7;
		int i1 = j >> 7;
		if (l < 0 || i1 < 0 || l > 103 || i1 > 103)
			return 0;
		int j1 = i;
		if (j1 < 3 && (tileSettingBits[1][l][i1] & 2) == 2)
			j1++;
		int k1 = k & 0x7f;
		int l1 = j & 0x7f;
		int i2 = intGroundArray[j1][l][i1] * (128 - k1)
				+ intGroundArray[j1][l + 1][i1] * k1 >> 7;
		int j2 = intGroundArray[j1][l][i1 + 1] * (128 - k1)
				+ intGroundArray[j1][l + 1][i1 + 1] * k1 >> 7;
		return i2 * (128 - l1) + j2 * l1 >> 7;
	}
	
	/**private int getFloorDrawHeight(int plane, int y, int x) {
		int regionX = x >> 7;
		int regionY = y >> 7;
		if (regionX < 0 || regionY < 0 || regionX > 103 || regionY > 103)
			return 0;
		int regionPlane = plane;
		if (regionPlane < 3 && (tileSettingBits[1][regionX][regionY] & 2) == 2)
			regionPlane++;
		int k1 = x & 0x7f;
		int l1 = y & 0x7f;
		int i2 = intGroundArray[regionPlane][regionX][regionY] * (128 - k1) + intGroundArray[regionPlane][regionX + 1][regionY] * k1 >> 7;
		int j2 = intGroundArray[regionPlane][regionX][regionY + 1] * (128 - k1) + intGroundArray[regionPlane][regionX + 1][regionY + 1] * k1 >> 7;
		return (i2 * (128 - l1)) + (j2 * l1 >> 7);
	}**/

	private static String intToKOrMil(int j) {
		if (j < 0x186a0)
			return String.valueOf(j);
		if (j < 0x989680)
			return j / 1000 + "K";
		else
			return j / 0xf4240 + "M";
	}

	private void resetLogout() {
		saveSettings();
		loginRedraw = true;
		if (!dontRememberMe) {
			Account a_ = null;
			for (Account a : Account.accounts) {
				if (a.getUsername().equals(myUsername)) {
					a_ = a;
					a_.setClientSize(clientSize);
					a_.setIDKHead(myPlayer.equipment[0] < 1164 ? myAppearance[0] == 0 ? myHeadAndJaw[0]
							: myAppearance[0]
							: 0);
					a_.setHelmet(myPlayer.equipment[0] >= 1164 ? myPlayer.equipment[0] - 512
							: 0);
					a_.setJaw(myHeadAndJaw[1]);
					a_.setGender(myPlayer.myGender);
					a_.setPassword(myPassword);
				}
			}
			if (Account.accounts.size() < 4) {
				if (a_ == null) {
					a_ = new Account(
							myUsername,
							myPassword,
							clientSize,
							myPlayer.equipment[0] >= 1164 ? myPlayer.equipment[0] - 512
									: 0,
							myPlayer.equipment[0] < 1164 ? myAppearance[0] == 0 ? myHeadAndJaw[0]
									: myAppearance[0]
									: 0, myAppearance[1] == 0 ? myHeadAndJaw[1]
									: myAppearance[1], myPlayer.myGender);
					Account.accounts.add(a_);
				}
			} 
			if (a_ != null)
				a_.save();
			RSInterface.buildPlayerMenu(Account.accounts);
		}
		clientSize = 0;
		if (clientSize == 0)
			toggleSize(3); 
		prayClicked = false;
		followPlayer = 0;
		followNPC = 0;
		followDistance = 1;
		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		socketStream = null;
		loggedIn = false;
		previousScreenState = 0;
		loginScreenState = 0;
		if (logger != null) {
			logger.setVisible(false);
			logger = null;
		}
		circle = 0;
		loginCode = 0;
		clearMemoryCaches();
		worldController.initToNull();
		for (int i = 0; i < 4; i++)
			clippingPlanes[i].setDefault();
		System.gc();
		//stopMidi();
		currentSong = -1;
		nextSong = -1;
		prevSong = 0;
	}

	private void setMyAppearance() {
		updateCharacterCreation = true;
		for (int j = 0; j < 7; j++) {
			myAppearance[j] = -1;
			for (int index = 0; index < IDK.cache.length; index++) {
				if (IDK.cache[index].notSelectable
						|| IDK.cache[index].bodyPartID != j + (isMale ? 0 : 7))
					continue;
				myAppearance[j] = index;
				break;
			}
		}
	}

	private void updateNPCMovement(int i, Stream stream) {
		while (stream.bitPosition + 21 < i * 8) {
			int k = stream.readBits(14);
			if (k == 16383)
				break;
			if (npcArray[k] == null)
				npcArray[k] = new NPC();
			NPC npc = npcArray[k];
			npcIndices[npcCount++] = k;
			npc.loopCycle = loopCycle;
			int l = stream.readBits(5);
			if (l > 15)
				l -= 32;
			int i1 = stream.readBits(5);
			if (i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(1);
			npc.desc = NPCDef.forID(stream.readBits(Configuration.NPC_BITS));
			int k1 = stream.readBits(1);
			if (k1 == 1)
				playersToUpdate[playersToUpdateCount++] = k;
			npc.boundDim = npc.desc.squaresNeeded;
			npc.anInt1504 = npc.desc.degreesToTurn;
			npc.walkAnimIndex = npc.desc.walkAnim;
			npc.runAnimation = npc.desc.runAnim;
			npc.turn180AnimIndex = npc.desc.turn180AnimIndex;
			npc.turn90CWAnimIndex = npc.desc.turn90CWAnimIndex;
			npc.turn90CCWAnimIndex = npc.desc.turn90CCWAnimIndex;
			npc.standAnimIndex = npc.desc.standAnim;
			npc.setPos(myPlayer.pathX[0] + i1, myPlayer.pathY[0] + l, j1 == 1);
		}
		stream.finishBitAccess();
	}

	public void processGameLoop() {
		if (rsAlreadyLoaded || loadingError || genericLoadingError)
			return;
		loopCycle++;
		checkSize();
		if (!loggedIn)
			try {
				processLoginScreenInput();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		else {
			try {
				mainGameProcessor();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		}
		processOnDemandQueue();
	}

	private void processPlayers(boolean local) {
		if (myPlayer.x >> 7 == destX && myPlayer.y >> 7 == destY)
			destX = 0;
		int playerIndexLength = playerCount;
		if (local)
			playerIndexLength = 1;
		for (int playerIndex = 0; playerIndex < playerIndexLength; playerIndex++) {
			Player player;
			int playerUID;
			if (local) {
				player = myPlayer;
				playerUID = myPlayerIndex << 14;
			} else {
				player = playerArray[playerIndices[playerIndex]];
				playerUID = playerIndices[playerIndex] << 14;
			}
			if (player == null || !player.isVisible())
				continue;
			player.aBoolean1699 = (lowMem && playerCount > 50 || playerCount > 200) && !local && player.forcedAnimId == player.standAnimIndex;
			int regionX = player.x >> 7;
			int regionY = player.y >> 7;
			if (regionX < 0 || regionX >= 104 || regionY < 0 || regionY >= 104)
				continue;
			if (player.tranformIntoModel != null
					&& loopCycle >= player.startTimeTransform
					&& loopCycle < player.transformedTimer) {
				player.aBoolean1699 = false;
				player.z = getFloorDrawHeight(plane, player.y, player.x);
				worldController.addSingleTileEntity(plane, player.y, player,
						player.anInt1552, player.extendedYMax, player.x,
						player.z, player.extendedXMin, player.extendedXMax, playerUID,
						player.extendedYMin);
				continue;
			}
			if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
				if (tileCycleMap[regionX][regionY] == sceneCycle)
					continue;
				tileCycleMap[regionX][regionY] = sceneCycle;
			}
			player.z = getFloorDrawHeight(plane, player.y, player.x);
			worldController.addMutipleTileEntity(plane, player.anInt1552,
					player.z, playerUID, player.y, 60, player.x, player,
					player.aBoolean1541);
		}
	}

	private boolean promptUserForInput(RSInterface rsi) {
		int type = rsi.contentType;
		if (inputTextType == 2) {
			if (type == 201) {
				inputTaken = true;
				inputDialogState = 0;
				showInput = true;
				promptInput = "";
				friendsListAction = 1;
				promptMessage = "Enter name of friend to add to list";
			}
			if (type == 202) {
				inputTaken = true;
				inputDialogState = 0;
				showInput = true;
				promptInput = "";
				friendsListAction = 2;
				promptMessage = "Enter name of friend to delete from list";
			}
		}
		if(type == 250)
			return true;
		if (type == 7712) {
			doGEAction(721);
		}
		if (type == 7713) {
			doGEAction(722);
		}
		if (type == 7714) {
			doGEAction(723);
		}
		if (type == 7715) {
			doGEAction(724);
		}
		if (type == 22222) {
			inputTaken = true;
			showInput = true;
			amountOrNameInput = "";
			promptInput = "";
			inputDialogState = 0;
			interfaceButtonAction = 6199;
			promptMessage = "Enter a name for the clan chat.";
		}
		if (type == 677) {
			inputTaken = true;
			showInput = true;
			amountOrNameInput = "";
			promptInput = "";
			inputDialogState = 0;
			interfaceButtonAction = 6200;
			promptMessage = "Enter name of the player you would like kicked.";
		}
		if (type == 205) {
			logoutCycle = 250;
			return true;
		}
		if (type == 6650) {
			inputTaken = true;
			showInput = true;
			amountOrNameInput = "";
			inputDialogState = -1;
			interfaceButtonAction = 6651;
			promptMessage = "Enter the note you would like to save";
		}
		if (type == 501) {
			inputTaken = true;
			inputDialogState = 0;
			showInput = true;
			promptInput = "";
			friendsListAction = 4;
			promptMessage = "Enter name of player to add to list";
		}
		if (type == 502) {
			inputTaken = true;
			inputDialogState = 0;
			showInput = true;
			promptInput = "";
			friendsListAction = 5;
			promptMessage = "Enter name of player to delete from list";
		}
		if (type == 550) {
			if (RSInterface.interfaceCache[18135].message.startsWith("Join")) {
				inputTaken = true;
				inputDialogState = 0;
				showInput = true;
				promptInput = "";
				friendsListAction = 6;
				promptMessage = "Enter the name of the chat you wish to join";
			} else {
				stream.createFrame(185);
				stream.writeWord(49627);
			}
		}
		if (type >= 300 && type <= 313) {
			int k = (type - 300) / 2;
			int j1 = type & 1;
			int i2 = myAppearance[k];
			if (i2 != -1) {
				do {
					if (j1 == 0 && --i2 < 0)
						i2 = IDK.cache.length - 1;
					if (j1 == 1 && ++i2 >= IDK.cache.length)
						i2 = 0;
				} while (IDK.cache[i2].notSelectable || IDK.cache[i2].bodyPartID != k + (isMale ? 0 : 7));
				myAppearance[k] = i2;
				updateCharacterCreation = true;
			}
		}
		if (type >= 314 && type <= 323) {
			int l = (type - 314) / 2;
			int k1 = type & 1;
			int j2 = myAppearanceColors[l];
			if (k1 == 0 && --j2 < 0)
				j2 = anIntArrayArray1003[l].length - 1;
			if (k1 == 1 && ++j2 >= anIntArrayArray1003[l].length)
				j2 = 0;
			myAppearanceColors[l] = j2;
			updateCharacterCreation = true;
		}
		if (type == 324 && !isMale) {
			isMale = true;
			setMyAppearance();
		}
		if (type == 325 && isMale) {
			isMale = false;
			setMyAppearance();
		}
		if (type == 326) {
			stream.createFrame(101);
			stream.writeByte1(isMale ? 0 : 1);
			for (int i1 = 0; i1 < 7; i1++)
				stream.writeWord(myAppearance[i1]);

			for (int l1 = 0; l1 < 5; l1++)
				stream.writeByte1(myAppearanceColors[l1]);

			return true;
		}
		if (type == 613)
			canMute = !canMute;
		if (type >= 601 && type <= 612) {
			clearTopInterfaces();
			if (reportAbuseInput.length() > 0) {
				stream.createFrame(218);
				stream.writeQWord(TextClass.longForName(reportAbuseInput));
				stream.writeByte1(type - 601);
				stream.writeByte1(canMute ? 1 : 0);
			}
		}
		return false;
	}

	private void processPlayerUpdating(Stream stream) {
		for (int index = 0; index < playersToUpdateCount; index++) {
			int length = playersToUpdate[index];
			Player player = playerArray[length];
			int updateFlag = stream.readUnsignedByte();
			if ((updateFlag & 0x40) != 0)
				updateFlag += stream.readUnsignedByte() << 8;
			readPlayerUpdateMask(updateFlag, length, stream, player);
		}
	}

	public void drawMapScenes(int y, int primaryColor, int x, int secondaryColor, int z) {
		int uid = worldController.getWallObjectUID(z, x, y);
		int newUID = worldController.fetchWallObjectNewUID(z, x, y);
		if ((uid ^ 0xffffffffffffffffL) != -1L || uid != 0) {
			int resourceTag = worldController.getIDTagForXYZ(z, x, y, uid);
			int direction = resourceTag >> 6 & 3;// direction
			int type = resourceTag & 0x1f;// type
			int color = primaryColor;// color
			if (uid > 0)
				color = secondaryColor;
			int mapPixels[] = miniMap.myPixels;
			int pixel = 24624 + x * 4 + (103 - y) * 512 * 4;
			int objectId = worldController.fetchWallDecorationNewUID(z, x, y);
			ObjectDef objDef = ObjectDef.forID(objectId);
			if (objDef.mapSceneID != -1) {
				Background scene = mapScenes[objDef.mapSceneID];
				if (scene != null) {
					int scene_x = (objDef.sizeX * 4 - scene.imgWidth) / 2;
					int scene_y = (objDef.sizeY * 4 - scene.imgHeight) / 2;
					scene.drawBackground(48 + x * 4 + scene_x, 48 + (104 - y - objDef.sizeY) * 4 + scene_y);
				}
			} else {
				if ((objDef.mapSceneID ^ 0xffffffff) == 0) {
					if (type == 0 || type == 2)
						if (direction == 0) {
							mapPixels[pixel] = color;
							mapPixels[pixel + 512] = color;
							mapPixels[1024 + pixel] = color;
							mapPixels[1536 + pixel] = color;
						} else if ((direction ^ 0xffffffff) == -2
								|| direction == 1) {
							mapPixels[pixel] = color;
							mapPixels[pixel + 1] = color;
							mapPixels[pixel + 2] = color;
							mapPixels[3 + pixel] = color;
						} else if (direction == 2) {
							mapPixels[pixel - -3] = color;
							mapPixels[3 + (pixel + 512)] = color;
							mapPixels[3 + (pixel + 1024)] = color;
							mapPixels[1536 + (pixel - -3)] = color;
						} else if (direction == 3) {
							mapPixels[pixel + 1536] = color;
							mapPixels[pixel + 1536 + 1] = color;
							mapPixels[2 + pixel + 1536] = color;
							mapPixels[pixel + 1536] = color;
						}
					if (type == 3)
						if (direction == 0)
							mapPixels[pixel] = color;
						else if (direction == 1)
							mapPixels[pixel + 3] = color;
						else if (direction == 2)
							mapPixels[pixel + 3 + 1536] = color;
						else if (direction == 3)
							mapPixels[pixel + 1536] = color;
					if (type == 2)
						if (direction == 3) {
							mapPixels[pixel] = color;
							mapPixels[pixel + 512] = color;
							mapPixels[pixel + 1024] = color;
							mapPixels[pixel + 1536] = color;
						} else if (direction == 0) {
							mapPixels[pixel] = color;
							mapPixels[pixel + 1] = color;
							mapPixels[pixel + 2] = color;
							mapPixels[pixel + 3] = color;
						} else if (direction == 1) {
							mapPixels[pixel + 3] = color;
							mapPixels[pixel + 3 + 512] = color;
							mapPixels[pixel + 3 + 1024] = color;
							mapPixels[pixel + 3 + 1536] = color;
						} else if (direction == 2) {
							mapPixels[pixel + 1536] = color;
							mapPixels[pixel + 1536 + 1] = color;
							mapPixels[pixel + 1536 + 2] = color;
							mapPixels[pixel + 1536 + 3] = color;
						}
				}
			}
		}
		uid = worldController.getInteractableObjectUID(z, x, y);
		newUID = worldController.fetchObjectMeshNewUID(z, x, y);
		if (uid != 0) {
			int resourceTag = worldController.getIDTagForXYZ(z, x, y, uid);
			int direction = resourceTag >> 6 & 3;
			int type = resourceTag & 0x1f;
			int objectId = worldController.fetchObjectMeshNewUID(z, x, y);

			ObjectDef objDef = ObjectDef.forID(objectId);
			if (objDef.mapSceneID != -1) {
				Background scene = mapScenes[objDef.mapSceneID];
				if (scene != null) {
					int sceneX = (objDef.sizeX * 4 - scene.imgWidth) / 2;
					int sceneY = (objDef.sizeY * 4 - scene.imgHeight) / 2;
					scene.drawBackground(48 + x * 4 + sceneX, 48 + (104 - y - objDef.sizeY) * 4 + sceneY);
				}
			} else if (type == 9) {
				int color = 0xeeeeee;
				if (uid > 0)
					color = 0xee0000;
				int mapPixels[] = miniMap.myPixels;
				int pixel = 24624 + x * 4 + (103 - y) * 512 * 4;
				if (direction == 0 || direction == 2) {
					mapPixels[pixel + 1536] = color;
					mapPixels[pixel + 1024 + 1] = color;
					mapPixels[pixel + 512 + 2] = color;
					mapPixels[pixel + 3] = color;
				} else {
					mapPixels[pixel] = color;
					mapPixels[pixel + 512 + 1] = color;
					mapPixels[pixel + 1024 + 2] = color;
					mapPixels[pixel + 1536 + 3] = color;
				}
			}
		}
		uid = worldController.fetchGroundDecorationNewUID(z, x, y);
		if (uid > 0 || uid != 0) {
			ObjectDef objDef = ObjectDef.forID(uid);
			if (objDef.mapSceneID != -1) {
				Background scene = mapScenes[objDef.mapSceneID];
				if (scene != null) {
					int sceneX = (objDef.sizeX * 4 - scene.imgWidth) / 2;
					int sceneY = (objDef.sizeY * 4 - scene.imgHeight) / 2;
					scene.drawBackground(48 + x * 4 + sceneX, 48 + (104 - y - objDef.sizeY) * 4 + sceneY);
				}
			}
		}
	}

	private void loadTitleScreen() {
		anIntArray828 = new int[32768];
		anIntArray829 = new int[32768];

	}

	private static void setHighMem() {
		WorldController.lowMem = false;
		Rasterizer.lowMem = false;
		lowMem = false;
		Region.lowMem = false;
		ObjectDef.lowMem = false;
	}

	public int canWalkDelay = 0;

	public int getDis(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = coordX2 - coordX1;
		int deltaY = coordY2 - coordY1;
		return ((int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)));
	}

	public int random(int range) {
		return (int) (Math.random() * range);
	}

	public boolean withinDistance(int x1, int y1, int x2, int y2, int dis) {
		for (int i = 0; i <= dis; i++) {
			try {
				if ((x1 + i) == x2
						&& ((y1 + i) == y2 || (y1 - i) == y2 || y1 == y2))
					return true;
				else if ((x1 - i) == x2
						&& ((x1 + i) == y2 || (y1 - i) == y2 || y1 == y2))
					return true;
				else if (x1 == x2
						&& ((x1 + i) == y2 || (y1 - i) == y2 || y1 == y2))
					return true;
			} catch (Exception ex) {
				System.out
						.println("Exception in following, method : WithingDistance");
			}
		}
		return false;
	}

	public static boolean onFullPage = false;
	
	public static void main(String args[]) {
		try {
			/*boolean useKit = JOptionPane.showConfirmDialog(null,
					"Would you like to use Swiftkit Beta Client?") == JOptionPane.YES_OPTION;
			swiftKit = useKit;
			if (useKit) {
				SwiftKitFrame.main(args);
				return;
			}*/
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("full")) {
					onFullPage = true;
				}
			}
			onWebClient = false;
			nodeID = 10;
			portOff = 0;
			setHighMem();
			isMembers = true;
			signlink.storeid = 32;
			signlink.startpriv(InetAddress.getLocalHost());
			clientSize = 0;
			instance = new Client();
			instance.createClientFrame(clientWidth, clientHeight);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static Client instance;

	private int highestAmtToLoad = 0;

	private void loadingStages() {
		try {
			if (lowMem && loadingStage == 2 && Region.onBuildTimePlane != plane) {

				gameScreenIP.initDrawingArea();
				SpriteCache.spriteCache[31].drawSprite(8, 9);
				int todo = onDemandFetcher.getRemaining();
				normalFont.drawShadowedString(false, clientSize == 0 ? 14 : 10,
						0xFFFFFF, "Remaining:" + todo, clientSize == 0 ? 14
								: 10);

				gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0,
						super.graphics, clientSize == 0 ? 4 : 0);
				loadingStage = 1;
				mapLoadingTime = System.currentTimeMillis();
			}
			if (loadingStage == 1) {
				SpriteCache.spriteCache[31].drawSprite(8, 9);
				int todo = onDemandFetcher.getRemaining();
				if (todo > highestAmtToLoad)
					highestAmtToLoad = todo;
				double percentage = (((double) todo / (double) highestAmtToLoad) * 100D);
				normalFont.drawShadowedString(false, 180 - 36, 0xc8c8c8, "("
						+ (100 - (int) percentage) + "%)", 30);

				gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0,
						super.graphics, clientSize == 0 ? 4 : 0);
				int j = getMapLoadingState();
				if (j != 0
						&& System.currentTimeMillis() - mapLoadingTime > 0x57e40L) {
					signlink.reporterror(myUsername + " glcfb " + serverSeed
							+ "," + j + "," + lowMem + ","
							+ cacheIndices[CONFIG_IDX] + ","
							+ onDemandFetcher.getRemaining() + "," + plane
							+ "," + currentRegionX + "," + currentRegionY);
					mapLoadingTime = System.currentTimeMillis();
				}
			}
			if (loadingStage == 2 && plane != lastKnownPlane) {
				lastKnownPlane = plane;
				renderedMapScene(plane);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String objectMaps = "";
	private String floorMaps = "";

	private int getMapLoadingState() {
		if (!floorMaps.equals("") || !objectMaps.equals("")) {
			floorMaps = "";
			objectMaps = "";
		}
		for (int i = 0; i < terrainData.length; i++) {
			floorMaps += "  " + terrainIndices[i];
			objectMaps += "  " + objectIndices[i];
			if (terrainData[i] == null && terrainIndices[i] != -1)
				return -1;
			if (objectData[i] == null && objectIndices[i] != -1)
				return -2;
		}
		boolean flag = true;
		for (int j = 0; j < terrainData.length; j++) {
			byte obData[] = objectData[j];
			if (obData != null) {
				int k = (mapCoordinates[j] >> 8) * 64 - baseX;
				int l = (mapCoordinates[j] & 0xff) * 64 - baseY;
				if (requestMapReconstruct) {
					k = 10;
					l = 10;
				}
				flag &= Region.allObjectsLoaded(k, obData, l);
			}
		}
		if (!flag)
			return -3;
		if (loadingMap) {
			return -4;
		} else {
			loadingStage = 2;
			Region.onBuildTimePlane = plane;
			loadRegion();
			if (loggedIn)
				stream.createFrame(121);
			return 0;
		}
	}

	private void processProjectiles() {
		for (Projectile projectile = (Projectile) projectileDeque.getFront(); projectile != null; projectile = (Projectile) projectileDeque
				.getNext())
			if (projectile.plane != plane || loopCycle > projectile.speed)
				projectile.unlink();
			else if (loopCycle >= projectile.startTime) {
				if (projectile.lockOn > 0) {
					NPC npc = npcArray[projectile.lockOn - 1];
					if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.y >= 0 && npc.y < 13312)
						projectile.calculateTracking(loopCycle,npc.y,
								getFloorDrawHeight(projectile.plane, npc.y,npc.x) - projectile.endHeight, npc.x);
				}
				if (projectile.lockOn < 0) {
					int pID = -projectile.lockOn - 1;
					Player player;
					if (pID == playerId)
						player = myPlayer;
					else
						player = playerArray[pID];
					if (player != null && player.x >= 0 && player.x < 13312
							&& player.y >= 0 && player.y < 13312)
						projectile.calculateTracking(loopCycle, player.y,
								getFloorDrawHeight(projectile.plane, player.y,player.x) - projectile.endHeight, player.x);
				}
				projectile.processMovement(cycleTimer);
				worldController.addMutipleTileEntity(plane, projectile.rotationY,
						(int) projectile.currentPositionZ, -1,
						(int) projectile.currentPositionY, 60,
						(int) projectile.currentPositionX, projectile, false);
			}

	}

	public AppletContext getAppletContext() {
		if (signlink.mainapp != null)
			return signlink.mainapp.getAppletContext();
		else
			return super.getAppletContext();
	}

	public static final int 
		ANIM_IDX = 2, 
		AUDIO_IDX = 3, 
		IMAGE_IDX = 5, 
		TEXTURE_IDX = 6
	;

	public synchronized void processOnDemandQueue() {
		do {
			OnDemandRequest onDemandData;
			do {
				onDemandData = onDemandFetcher.getNextNode();
				if (onDemandData == null)
					return;
				/** Models Loading **/
				if (onDemandData.dataType == MODEL_IDX - 1) {
					Model.readFirstModelData(onDemandData.buffer,
							onDemandData.id);
					needDrawTabArea = true;

				}
				/** Animations Loading **/
				if (onDemandData.dataType == ANIM_IDX - 1) {
					FrameReader.load(onDemandData.id, onDemandData.buffer);
				}
				/** Sounds Loading **/
				if (onDemandData.dataType == AUDIO_IDX - 1
						&& onDemandData.id == nextSong
						&& onDemandData.buffer != null)
					saveMidi(songChanging, onDemandData.buffer);
				/** Maps Loading **/

				if (onDemandData.dataType == IMAGE_IDX - 1) {
					if (onDemandData.id == 681) {
						super.setCursor(onDemandData.buffer);
					}
					SpriteCache.spriteCache[onDemandData.id] = new Sprite(
							onDemandData.buffer);
				}
				if (onDemandData.dataType == TEXTURE_IDX - 1) {
					Texture.load(onDemandData.id, onDemandData.buffer);
				}
				if (onDemandData.dataType == MAP_IDX - 1 && loadingStage == 1) {

					for (int i = 0; i < terrainData.length; i++) {
						if (terrainIndices[i] == onDemandData.id) {
							terrainData[i] = onDemandData.buffer;
							if (onDemandData.buffer == null)
								terrainIndices[i] = -1;
							break;
						}
						if (objectIndices[i] != onDemandData.id)
							continue;
						objectData[i] = onDemandData.buffer;
						if (onDemandData.buffer == null)
							objectIndices[i] = -1;
						break;
					}

				}
			} while (onDemandData.dataType != 93
					|| !onDemandFetcher.mapIsObjectMap(onDemandData.id));
			Region.passiveRequestGameObjectModels(new Stream(onDemandData.buffer),
					onDemandFetcher);
		} while (true);
	}

	private void resetInterfaceAnimation(int i) {
		RSInterface rsInterface = RSInterface.interfaceCache[i];
		if(rsInterface == null)
			return;
		for (int j = 0; j < rsInterface.children.length; j++) {
			if (rsInterface.children[j] == -1)
				break;
			RSInterface child = RSInterface.interfaceCache[rsInterface.children[j]];
			if (child == null)
				System.out.println(rsInterface.children[j]);
			if (child.type == 1)
				resetInterfaceAnimation(child.id);
			child.currentFrame = 0;
			child.frameTimer = 0;
		}
	}

	private void doGEAction(int l) {
		if (l == 721) {
			inputTaken = true;
			amountOrNameInput = "";
			inputDialogState = 1;
			interfaceButtonAction = 1557;
		}
		if (l == 722) {
			inputTaken = true;
			amountOrNameInput = "";
			inputDialogState = 1;
			interfaceButtonAction = 1557;
		}
		if (l == 723) {
			inputTaken = true;
			amountOrNameInput = "";
			inputDialogState = 1;
			interfaceButtonAction = 1558;
		}
		if (l == 724) {
			inputTaken = true;
			amountOrNameInput = "";
			inputDialogState = 1;
			interfaceButtonAction = 1558;
		}
	}

	private void drawHeadIcon() {
		if (markType != 2)
			return;
		calcEntityScreenPos((markedX - baseX << 7) + anInt937, anInt936 * 2, (markedY - baseY << 7) + anInt938);
		if (spriteDrawX > -1 && loopCycle % 20 < 10)
			headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
	}

	public int otherPlayerId = 0, otherPlayerX = 0, otherPlayerY = 0;
	private int lastPercent;

	private void mainGameProcessor() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		if (openInterfaceID == 24600 && buttonclicked && interfaceButtonAction != 1558 && interfaceButtonAction != 1557) {
			inputDialogState = 3;
		}
		if (openInterfaceID == 24600 && interfaceButtonAction == 1558 || openInterfaceID == 24600 && interfaceButtonAction == 1557) {
			inputDialogState = 1;
		}
		if (openInterfaceID == 24600 && !buttonclicked && interfaceButtonAction != 1558 && interfaceButtonAction != 1557) {
			inputDialogState = 0;
		}
		if (updateMinutes > 1)
			updateMinutes--;
		if (logoutCycle > 0)
			logoutCycle--;
		for (int attempt = 0; attempt < 5; attempt++)
			if (!parsePacket())
				break;

		if (!loggedIn)
			return;
		synchronized (mouseDetection.syncObject) {
			if (loggedIn && otherPlayerId > 0) {
				Player player = playerArray[otherPlayerId];
				int xCOORD = 0;
				int yCOORD = 0;
				boolean doStuff = false;
				if (playerArray[otherPlayerId] != null) {
					xCOORD = player.pathX[0] + (player.x - 6 >> 7);
					yCOORD = player.pathY[0] + (player.y - 6 >> 7);
					if (xCOORD == otherPlayerX && yCOORD == otherPlayerY)
						doStuff = true;
				}
				if (playerArray[otherPlayerId] != null && !doStuff) {
					doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
							player.pathY[0], myPlayer.pathX[0], false,
							player.pathX[0]);
				}
				if (playerArray[otherPlayerId] != null) {
					otherPlayerY = yCOORD;
					otherPlayerX = xCOORD;
				}
				if (playerArray[otherPlayerId] == null) {
					otherPlayerId = 0;
				}
			}
			if (flagged) {
				if (super.clickMode3 != 0 || mouseDetection.coordsIndex >= 40) {

					stream.createFrame(45);
					stream.writeByte1(0);
					int j2 = stream.currentOffset;
					int j3 = 0;
					for (int j4 = 0; j4 < mouseDetection.coordsIndex; j4++) {
						if (j2 - stream.currentOffset >= 240)
							break;
						j3++;
						int l4 = mouseDetection.coordsY[j4];
						if (l4 < 0)
							l4 = 0;
						else if (l4 > 502)
							l4 = 502;
						int k5 = mouseDetection.coordsX[j4];
						if (k5 < 0)
							k5 = 0;
						else if (k5 > 764)
							k5 = 764;
						int i6 = l4 * 765 + k5;
						if (mouseDetection.coordsY[j4] == -1
								&& mouseDetection.coordsX[j4] == -1) {
							k5 = -1;
							l4 = -1;
							i6 = 0x7ffff;
						}
						if (k5 == mouseRecorderLastX && l4 == mouseRecorderLastY) {
							if (mouseRecorderIdleCycles < 2047)
								mouseRecorderIdleCycles++;
						} else {
							int j6 = k5 - mouseRecorderLastX;
							mouseRecorderLastX = k5;
							int k6 = l4 - mouseRecorderLastY;
							mouseRecorderLastY = l4;
							if (mouseRecorderIdleCycles < 8 && j6 >= -32 && j6 <= 31
									&& k6 >= -32 && k6 <= 31) {
								j6 += 32;
								k6 += 32;
								stream.writeWord((mouseRecorderIdleCycles << 12) + (j6 << 6)
										+ k6);
								mouseRecorderIdleCycles = 0;
							} else if (mouseRecorderIdleCycles < 8) {
								stream.writeDWordBigEndian(0x800000
										+ (mouseRecorderIdleCycles << 19) + i6);
								mouseRecorderIdleCycles = 0;
							} else {
								stream.writeDWord(0xc0000000
										+ (mouseRecorderIdleCycles << 19) + i6);
								mouseRecorderIdleCycles = 0;
							}
						}
					}

					stream.writeBytes(stream.currentOffset - j2);
					if (j3 >= mouseDetection.coordsIndex) {
						mouseDetection.coordsIndex = 0;
					} else {
						mouseDetection.coordsIndex -= j3;
						for (int i5 = 0; i5 < mouseDetection.coordsIndex; i5++) {
							mouseDetection.coordsX[i5] = mouseDetection.coordsX[i5
									+ j3];
							mouseDetection.coordsY[i5] = mouseDetection.coordsY[i5
									+ j3];
						}

					}
				}
			} else {
				mouseDetection.coordsIndex = 0;
			}
		}
		if (super.clickMode3 != 0) {
			long l = (super.aLong29 - lastClickTime) / 50L;
			if (l > 4095L)
				l = 4095L;
			lastClickTime = super.aLong29;
			int k2 = super.saveClickY;
			if (k2 < 0)
				k2 = 0;
			else if (k2 > 502)
				k2 = 502;
			int k3 = super.saveClickX;
			if (k3 < 0)
				k3 = 0;
			else if (k3 > 764)
				k3 = 764;
			int k4 = k2 * 765 + k3;
			int j5 = 0;
			if (super.clickMode3 == 2)
				j5 = 1;
			int l5 = (int) l;
			stream.createFrame(241);
			stream.writeDWord((l5 << 20) + (j5 << 19) + k4);
		}
		processShadow();
		if (sendCameraInfoCycle > 0)
			sendCameraInfoCycle--;
		if (super.keyArray[1] == 1 || super.keyArray[2] == 1
				|| super.keyArray[3] == 1 || super.keyArray[4] == 1)
			cameraSendingInfo = true;
		if (cameraSendingInfo && sendCameraInfoCycle <= 0) {
			sendCameraInfoCycle = 20;
			cameraSendingInfo = false;
			stream.createFrame(86);
			stream.writeWord(chaseCameraPitch);
			stream.writeUnsignedWordA(viewRotation);
		}
		if (super.awtFocus && !isFocused) {
			isFocused = true;
			stream.createFrame(3);
			stream.writeByte1(1);
		}
		if (!super.awtFocus && isFocused) {
			isFocused = false;
			stream.createFrame(3);
			stream.writeByte1(0);
		}
		loadingStages();
		updateSpawnedObjects();
		processRequestedAudio();
		netIdleCycles++;
		if (netIdleCycles > 750)
			dropClient();
		updatePlayerInstances();
		readNPCUpdateBlockForced();
		processTextCycles();
		cycleTimer++;
		if (crossType != 0) {
			crossIndex += 20;
			if (crossIndex >= 400)
				crossType = 0;
		}
		if (atInventoryInterfaceType != 0) {
			atInventoryLoopCycle++;
			if (atInventoryLoopCycle >= 15) {
				if (atInventoryInterfaceType == 2)
					needDrawTabArea = true;
				if (atInventoryInterfaceType == 3)
					inputTaken = true;
				atInventoryInterfaceType = 0;
			}
		}
		if (activeInterfaceType != 0) {
			dragCycle++;
			if (super.mouseX > pressX + 5 || super.mouseX < pressX - 5
					|| super.mouseY > pressY + 5
					|| super.mouseY < pressY - 5)
				isDragging = true;
			if (super.clickMode2 == 0) {
				if (activeInterfaceType == 2)
					needDrawTabArea = true;
				if (activeInterfaceType == 3)
					inputTaken = true;
				activeInterfaceType = 0;
				if (isDragging && dragCycle >= 10) {
					lastActiveInvInterface = -1;
					processRightClick();
					if (lastActiveInvInterface == focusedDragWidget
							&& mouseInvInterfaceIndex != dragFromSlot) {
						RSInterface inter = RSInterface.interfaceCache[focusedDragWidget];
						int j1 = 0;
						if (anInt913 == 1 && inter.contentType == 206)
							j1 = 1;
						if (inter.inv[mouseInvInterfaceIndex] <= 0)
							j1 = 0;
						if (inter.dragDeletes) {
							int l2 = dragFromSlot;
							int l3 = mouseInvInterfaceIndex;
							inter.inv[l3] = inter.inv[l2];
							inter.invStackSizes[l3] = inter.invStackSizes[l2];
							inter.inv[l2] = -1;
							inter.invStackSizes[l2] = 0;
						} else if (j1 == 1) {
							int i3 = dragFromSlot;
							for (int i4 = mouseInvInterfaceIndex; i3 != i4;)
								if (i3 > i4) {
									inter.swapInventoryItems(i3, i3 - 1);
									i3--;
								} else if (i3 < i4) {
									inter.swapInventoryItems(i3, i3 + 1);
									i3++;
								}

						} else {
							inter.swapInventoryItems(dragFromSlot,
									mouseInvInterfaceIndex);
						}
						stream.createFrame(214);
						stream.writeSignedBigEndian(focusedDragWidget);
						stream.writeByteC(j1);
						stream.writeSignedBigEndian(dragFromSlot);
						stream.writeUnsignedWordBigEndian(mouseInvInterfaceIndex);
					}
				} else if ((useOneMouseButton == 1 || menuHasAddFriend(menuActionRow - 1))
						&& menuActionRow > 2)
					determineMenuSize();
				else if (menuActionRow > 0)
					doAction(menuActionRow - 1);
				atInventoryLoopCycle = 10;
				super.clickMode3 = 0;
			}
		}
		if (WorldController.clickedTileX != -1) {
			int k = WorldController.clickedTileX;
			int k1 = WorldController.clickedTileY;

			boolean flag = doWalkTo(0, 0, 0, 0, myPlayer.pathY[0], 0, 0, k1,
					myPlayer.pathX[0], true, k);
			WorldController.clickedTileX = -1;
			if (flag) {
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 1;
				crossIndex = 0;
			}
		}
		if (super.clickMode3 == 1 && notifyMessage != null) {
			notifyMessage = null;
			inputTaken = true;
			super.clickMode3 = 0;
		}
		if (!processMenuClick()) {
			processMainScreenClick();
			processTabAreaClick();
			processChatModeClick();
			if (quickChat)
				processQuickChatArea();
			processMapAreaMouse();
		}
		if (super.clickMode3 == 1) {
			if (super.saveClickX >= 522 && super.saveClickX <= 558
					&& super.saveClickY >= 124 && super.saveClickY < 161) {
				worldMap[0] = !worldMap[0];
				// WorldMap.clientInstance = this;
				// wm = new WorldMap();

			}
		}
		if (super.clickMode2 == 1 || super.clickMode3 == 1)
			clickCycle++;
		if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
			if (anInt1501 < 50 && !menuOpen) {
				anInt1501++;
				if (anInt1501 == 50) {
					if (anInt1500 != 0) {
						inputTaken = true;
					}
					if (anInt1044 != 0) {
						needDrawTabArea = true;
					}
				}
			}
		} else if (anInt1501 > 0) {
			anInt1501--;
		}
		if (loadingStage == 2)
			handleArrowKeys();
		if (loadingStage == 2 && inCutScene)
			calcCameraPos();
		for (int i1 = 0; i1 < 5; i1++)
			cameraEffectCycles[i1]++;

		manageTextInput();
		super.idleTime++;
		if (super.idleTime > 15000) {
			logoutCycle = 250;
			super.idleTime -= 500;
			stream.createFrame(202);
		}
		cameraACCycle0++;
		if (cameraACCycle0 > 500) {
			cameraACCycle0 = 0;
			int l1 = (int) (Math.random() * 8D);
			if ((l1 & 1) == 1)
				cameraOffsetX += cameraACTranslateX;
			if ((l1 & 2) == 2)
				cameraOffsetY += anInt1132;
			if ((l1 & 4) == 4)
				viewRotationOffset += cameraACTranslatePitch;
		}
		if (cameraOffsetX < -50)
			cameraACTranslateX = 2;
		if (cameraOffsetX > 50)
			cameraACTranslateX = -2;
		if (cameraOffsetY < -55)
			anInt1132 = 2;
		if (cameraOffsetY > 55)
			anInt1132 = -2;
		if (viewRotationOffset < -40)
			cameraACTranslatePitch = 1;
		if (viewRotationOffset > 40)
			cameraACTranslatePitch = -1;
		cameraACCycle1++;
		if (cameraACCycle1 > 500) {
			cameraACCycle1 = 0;
			int i2 = (int) (Math.random() * 8D);
			if ((i2 & 1) == 1)
				minimapRotation += cameraACTranslateYaw;
			if ((i2 & 2) == 2)
				minimapZoom += minimapACTranslateZoom;
		}
		if (minimapRotation < -60)
			cameraACTranslateYaw = 2;
		if (minimapRotation > 60)
			cameraACTranslateYaw = -2;
		if (minimapZoom < -20)
			minimapACTranslateZoom = 1;
		if (minimapZoom > 10)
			minimapACTranslateZoom = -1;
		heartBeatCycle++;
		if (heartBeatCycle > 50)
			stream.createFrame(0);
		try {
			if (socketStream != null && stream.currentOffset > 0) {
				socketStream.queueBytes(stream.currentOffset, stream.buffer);
				stream.currentOffset = 0;
				heartBeatCycle = 0;
			}
		} catch (IOException _ex) {
			dropClient();
			//_ex.printStackTrace();
		} catch (Exception exception) {
			resetLogout();
			exception.printStackTrace();
		}
	}

	private void clearObjectSpawnRequests() {
		GameObjectSpawnRequest request = (GameObjectSpawnRequest) objectSpawnDeque
				.getFront();
		for (; request != null; request = (GameObjectSpawnRequest) objectSpawnDeque
				.getNext())
			if (request.removeTime == -1) {
				request.spawnTime = 0;
				assignOldValuesToNewRequest(request);
			} else {
				request.unlink();
			}

	}

	private void resetImageProducers() {
		if (GraphicsBuffer_1107 != null)
			return;
		super.fullGameScreen = null;
		chatAreaIP = null;
		mapAreaIP = null;
		tabAreaIP = null;
		gameScreenIP = null;
		GraphicsBuffer_1125 = null;
		GraphicsBuffer_1107 = new RSImageProducer(509, 171, getGameComponent());
		DrawingArea.resetImage();
		new RSImageProducer(360, 132, getGameComponent());
		DrawingArea.resetImage();
		titleScreen = new RSImageProducer(getClientWidth(), getClientHeight(), getGameComponent());
		DrawingArea.resetImage();
		new RSImageProducer(202, 238, getGameComponent());
		DrawingArea.resetImage();
		new RSImageProducer(203, 238, getGameComponent());
		DrawingArea.resetImage();
		new RSImageProducer(74, 94, getGameComponent());
		DrawingArea.resetImage();
		new RSImageProducer(75, 94, getGameComponent());
		DrawingArea.resetImage();
		if (titleStreamLoader != null) {
			loadTitleScreen();
		}
		// System.gc();
		welcomeScreenRaised = true;
	}

	public void drawSmoothLoading(int i, String s) {
		// checkSize();
		lastPercent = i;
		loadingStepText = s;
		// for (int f = lastPercent; f <= i; f++)
		// drawLoadingText(i, s);
	}

	void drawLoadingText(int i, String s) {
		/*if (SpriteCache.initialised()) {
			resetImageProducers();
			loadingScreen.initDrawingArea();
			DrawingArea474.drawFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0x000000);
			SpriteCache.get(36).drawAdvancedSprite(0, 0);
			//SpriteCache.get(36).rotate(50, 26.0, 50);
			loadingScreen.drawGraphics(0, super.graphics, 0);
			return;
		} else {*/
			super.drawLoadingText(i, s);
			super.shouldClearScreen = true;
			return;
		//}
	}

	public DrawingArea Dinstance = new DrawingArea();

	public DrawingArea getDraw() {
		return Dinstance;
	}

	private void resetImage() {
		DrawingArea.resetImage();
	}

	private void scrollInterface(int maxWidth, int height, int mouseX, int mouseY, RSInterface rsi, int childYPos, boolean tabRedraw, int scrollItemsLength) {
		int anInt992;
		if (aBoolean972)
			anInt992 = 32;
		else
			anInt992 = 0;
		aBoolean972 = false;
		if (mouseX >= maxWidth && mouseX < maxWidth + 16 && mouseY >= childYPos && mouseY < childYPos + 16) {
			rsi.scrollPosition -= clickCycle * 4;
			if (tabRedraw)
				needDrawTabArea = true;
		} else if (mouseX >= maxWidth && mouseX < maxWidth + 16 && mouseY >= (childYPos + height) - 16 && mouseY < childYPos + height) {
			rsi.scrollPosition += clickCycle * 4;
			if (tabRedraw)
				needDrawTabArea = true;
		} else if (mouseX >= maxWidth - anInt992 && mouseX < maxWidth + 16 + anInt992 
				&& mouseY >= childYPos + 16 && mouseY < (childYPos + height) - 16 && clickCycle > 0) {
			int l1 = ((height - 32) * height) / scrollItemsLength;
			if (l1 < 8)
				l1 = 8;
			int i2 = mouseY - childYPos - 16 - l1 / 2;
			int j2 = height - 32 - l1;
			rsi.scrollPosition = ((scrollItemsLength - height) * i2) / j2;
			if (tabRedraw)
				needDrawTabArea = true;
			aBoolean972 = true;
		}
	}

	private boolean reachedClickedObject(int uid, int tileY, int tileX, int id) {
		int objectBits = worldController.getIDTagForXYZ(plane, tileX, tileY,
				uid);
		if (uid == -1)
			return false;
		int objectType = objectBits & 0x1f;
		int objectRotation = objectBits >> 6 & 3;
		if (objectType == 10 || objectType == 11 || objectType == 22) {
			ObjectDef objectDef = ObjectDef.forID(id);
			int sizeX;
			int sizeY;
			if (objectRotation == 0 || objectRotation == 2) {
				sizeX = objectDef.sizeX;
				sizeY = objectDef.sizeY;
			} else {
				sizeX = objectDef.sizeY;
				sizeY = objectDef.sizeX;
			}
			int reqPlane = objectDef.plane;
			if (objectRotation != 0)
				reqPlane = (reqPlane << objectRotation & 0xf)
						+ (reqPlane >> 4 - objectRotation);
			doWalkTo(2, 0, sizeY, 0, myPlayer.pathY[0], sizeX, reqPlane, tileY, myPlayer.pathX[0], false, tileX);
		} else {
			doWalkTo(2, objectRotation, 0, objectType + 1, myPlayer.pathY[0],
					0, 0, tileY, myPlayer.pathX[0], false, tileX);
		}
		crossX = super.saveClickX;
		crossY = super.saveClickY;
		crossType = 2;
		crossIndex = 0;
		return true;
	}

	public final CRC32 aCRC32_930 = new CRC32();

	private CacheArchive streamLoaderForName(int i, String s, String s1, int j,
			int k) {
		byte abyte0[] = null;
		int l = 5;
		try {
			if (cacheIndices[CONFIG_IDX] != null)
				abyte0 = cacheIndices[CONFIG_IDX].get(i);
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}

		if (abyte0 != null) {
			if (Configuration.JAGCACHED_ENABLED) {
				aCRC32_930.reset();
				aCRC32_930.update(abyte0);
				int i1 = (int) aCRC32_930.getValue();
				if (i1 != j)
					abyte0 = null;
			}
		}

		if (abyte0 != null) {
			CacheArchive streamLoader = new CacheArchive(abyte0);
			return streamLoader;
		}
		int j1 = 0;
		while (abyte0 == null) {
			String s2 = "Unknown error";
			drawLoadingText(k, "Requesting " + s);
			try {
				int k1 = 0;
				DataInputStream datainputstream = openJagGrabInputStream(s1 + j);
				byte abyte1[] = new byte[6];
				datainputstream.readFully(abyte1, 0, 6);
				Stream stream = new Stream(abyte1);
				stream.currentOffset = 3;
				int i2 = stream.read3Bytes() + 6;
				int j2 = 6;
				abyte0 = new byte[i2];
				System.arraycopy(abyte1, 0, abyte0, 0, 6);

				while (j2 < i2) {
					int l2 = i2 - j2;
					if (l2 > 1000)
						l2 = 1000;
					int j3 = datainputstream.read(abyte0, j2, l2);
					if (j3 < 0) {
						s2 = "Length error: " + j2 + "/" + i2;
						throw new IOException("EOF");
					}
					j2 += j3;
					int k3 = (j2 * 100) / i2;
					if (k3 != k1) {
						drawSmoothLoading(k, "Loading " + s + " - " + k3 + "%");
					}
					k1 = k3;
				}
				datainputstream.close();
				try {
					if (cacheIndices[CONFIG_IDX] != null)
						cacheIndices[CONFIG_IDX].put(abyte0.length, abyte0, i);
				} catch (Exception _ex) {
					cacheIndices[0] = null;
					_ex.printStackTrace();
				}

				if (abyte0 != null) {
					if (Configuration.JAGCACHED_ENABLED) {
						aCRC32_930.reset();
						aCRC32_930.update(abyte0);
						int i3 = (int) aCRC32_930.getValue();
						if (i3 != j) {
							abyte0 = null;
							j1++;
							s2 = "Checksum error: " + i3;
						}
					}
				}

			} catch (IOException ioexception) {
				if (s2.equals("Unknown error"))
					s2 = "Connection error";
				abyte0 = null;
				ioexception.printStackTrace();
			} catch (NullPointerException _ex) {
				s2 = "Null error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
				_ex.printStackTrace();
			} catch (ArrayIndexOutOfBoundsException _ex) {
				s2 = "Bounds error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
				_ex.printStackTrace();
			} catch (Exception _ex) {
				s2 = "Unexpected error";
				abyte0 = null;
				if (!signlink.reporterror)
					return null;
				_ex.printStackTrace();
			}
			if (abyte0 == null) {
				for (int l1 = l; l1 > 0; l1--) {
					if (j1 >= 3) {
						drawLoadingText(k, "Game updated - please reload page");
						l1 = 10;
					} else {
						drawLoadingText(k, s2 + " - Retrying in " + l1);
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				l *= 2;
				if (l > 60)
					l = 60;

			}

		}

		CacheArchive streamLoader_1 = new CacheArchive(abyte0);
		return streamLoader_1;
	}

	private void dropClient() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		if (logoutCycle > 0) {
			resetLogout();
			return;
		}
		gameScreenIP.initDrawingArea();
		SpriteCache.spriteCache[32].drawSprite(7, 4);
		gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0, super.graphics, clientSize == 0 ? 4 : 0);
		minimapStatus = 0;
		destX = 0;
		RSSocket rsSocket = socketStream;
		loggedIn = false;
		loginFailures = 0;
		if (logger != null) {
			logger.setVisible(false);
			logger = null;
		}
		login(myUsername, myPassword, true);
		if (!loggedIn)
			resetLogout();
		try {
			rsSocket.close();
		} catch (Exception _ex) {
		}
	}

	private void doAction(int menuIndex) {
		if (menuIndex < 0)
			return;
		if (inputDialogState != 0 && inputDialogState != 3) {
			inputDialogState = 0;
			inputTaken = true;
		}
		int slot = menuActionCmd2[menuIndex];
		int interfaceId = menuActionCmd3[menuIndex];
		int cmd4 = menuActionCmd4[menuIndex];
		int actionId = menuActionID[menuIndex];
		int nodeId = menuActionCmd1[menuIndex];
		int x = slot;
		int y = interfaceId;
		int id = (nodeId > 32767 ? cmd4 : nodeId >> 14 & 0x7fff);
		System.out.println("ActionID: " + menuIndex + " MenuActionID: " + actionId);
		if (actionId >= 2000)
			actionId -= 2000;
		if (actionId == 104) {
			RSInterface rsi = RSInterface.interfaceCache[interfaceId];
			spellID = rsi.id;
			if (!Autocast) {
				Autocast = true;
				autocastId = rsi.id;
				stream.createFrame(185);
				stream.writeWord(rsi.id);
			} else if (autocastId == rsi.id) {
				Autocast = false;
				autocastId = 0;
				stream.createFrame(185);
				stream.writeWord(rsi.id);
			} else if (autocastId != rsi.id) {
				Autocast = true;
				autocastId = rsi.id;
				stream.createFrame(185);
				stream.writeWord(rsi.id);
			}
		}
		if (actionId == 696) {
			viewRotation = 0;
			chaseCameraPitch = 120;

		}
		if (actionId == 1251) {
			buttonclicked = false;
			inputString = "[A]" + GEItemId;
			sendPacket(1003);
		}
		if (actionId == 1007) {
			canGainXP = canGainXP ? false : true;
		}
		if (actionId == 1006 && !showBonus) {
			if (!gains.isEmpty()) {
				gains.removeAll(gains);
			}
			showXP = showXP ? false : true;
		}
		if (actionId == 1030 && !showXP) {
			showBonus = showBonus ? false : true;
		}
		if (actionId == 1005) {
			openQuickChat();
		}
		if (actionId == 1004) {
			quickChat = false;
			canTalk = true;
			inputTaken = true;
		}
		if (actionId == 1014) {
			running = !running;
			sendPacket185(19158);
		}
		if (actionId == 1016) {
			resting = !resting;
			sendPacket185(26500);
		}
		if (actionId == 1013) {// xp counter reset
			sendPacket185(18222);
		}
		if (actionId == 1076) {
			if (menuOpen) {
				needDrawTabArea = true;
				tabID = tabHover;
				tabAreaAltered = true;
			}
		}
		if (actionId == 1026) {// Cast Special Attack
			if (choosingLeftClick) {
				leftClick = 7;
				choosingLeftClick = false;
			} else
				sendPacket185(15660);
		}
		if (actionId == 1025) {
			if (choosingLeftClick) {
				leftClick = -1;
				choosingLeftClick = false;
			} else {
				leftClick = -1;
				choosingLeftClick = true;
			}
		}
		if (actionId == 1024) {// Follower Details
			if (choosingLeftClick) {
				leftClick = 6;
				choosingLeftClick = false;
			} else
				sendPacket185(15661);
		}
		if (actionId == 1023) {// Attack
			if (choosingLeftClick) {
				leftClick = 5;
				choosingLeftClick = false;
			} else
				sendPacket185(15662);
		}
		if (actionId == 1022) {// Interact
			if (choosingLeftClick) {
				leftClick = 4;
				choosingLeftClick = false;
			} else
				sendPacket185(15663);
		}
		if (actionId == 1021) {// Renew Familiar
			if (choosingLeftClick) {
				leftClick = 3;
				choosingLeftClick = false;
			} else
				sendPacket185(15664);
		}
		if (actionId == 1020) {// Tale BoB
			if (choosingLeftClick) {
				leftClick = 2;
				choosingLeftClick = false;
			} else
				sendPacket185(15665);
		}
		if (actionId == 1019) {// Dismiss
			if (choosingLeftClick) {
				leftClick = 1;
				choosingLeftClick = false;
			} else
				sendPacket185(15666);
		}
		if (actionId == 1027) {// Dismiss
			leftClick = -1;
			choosingLeftClick = true;
		}
		if (actionId == 1018) {// Call Follower
			if (choosingLeftClick) {
				leftClick = 0;
				choosingLeftClick = false;
			} else
				sendPacket185(15667);
		}
		if (actionId == 13003) {
			stream.createFrame(185);
			stream.writeWord(menuActionName[menuIndex].contains("Cast") ? 15004 : 15003);
		}
		if (actionId == 13004) {
			stream.createFrame(185);
			stream.writeWord(15005);
		}
		if (actionId == 13005) {
			stream.createFrame(185);
			stream.writeWord(15006);
		}
		if (actionId == 13006) {
			stream.createFrame(185);
			stream.writeWord(15007);
		}
		if (actionId == 13007) {
			stream.createFrame(185);
			stream.writeWord(15008);
		}
		if (actionId == 582) {
			NPC npc = npcArray[nodeId];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0, npc.pathY[0],
						myPlayer.pathX[0], false, npc.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(57);
				stream.writeUnsignedWordA(selectedItemId);
				stream.writeUnsignedWordA(nodeId);
				stream.writeUnsignedWordBigEndian(lastItemSelectedSlot);
				stream.writeUnsignedWordA(lastItemSelectedInterface);
			}
		}
		if (actionId == 234) {
			boolean flag1 = doWalkTo(2, 0, 0, 0, myPlayer.pathY[0], 0, 0,
					interfaceId, myPlayer.pathX[0], false, slot);
			if (!flag1)
				flag1 = doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						interfaceId, myPlayer.pathX[0], false, slot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(236);
			stream.writeUnsignedWordBigEndian(interfaceId + baseY);
			stream.writeWord(nodeId);
			stream.writeUnsignedWordBigEndian(slot + baseX);
		}
		if (actionId == 62 && reachedClickedObject(nodeId, y, x, id)) {
			stream.createFrame(192);
			stream.writeWord(lastItemSelectedInterface);
			stream.writeWord(id);
			stream.writeSignedBigEndian(y + baseY);
			stream.writeUnsignedWordBigEndian(lastItemSelectedSlot);
			stream.writeSignedBigEndian(x + baseX);
			stream.writeWord(selectedItemId);
		}
		if (actionId == 511) {
			boolean flag2 = doWalkTo(2, 0, 0, 0, myPlayer.pathY[0], 0, 0,
					interfaceId, myPlayer.pathX[0], false, slot);
			if (!flag2)
				flag2 = doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						interfaceId, myPlayer.pathX[0], false, slot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(25);
			stream.writeUnsignedWordBigEndian(lastItemSelectedInterface);
			stream.writeUnsignedWordA(selectedItemId);
			stream.writeWord(nodeId);
			stream.writeUnsignedWordA(interfaceId + baseY);
			stream.writeSignedBigEndian(lastItemSelectedSlot);
			stream.writeWord(slot + baseX);
		}
		if (actionId == 74) {
			stream.createFrame(122);
			stream.writeWord(interfaceId);
			stream.writeWord(slot);
			stream.writeWord(nodeId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 315) {
			RSInterface class9 = RSInterface.interfaceCache[interfaceId];
			System.out.println("Interface: " + interfaceId);
			boolean flag8 = true;
			if (class9.contentType > 0)
				flag8 = promptUserForInput(class9);
			if (flag8) {

				switch (interfaceId) {
				case 19144:
					sendFrame248(15106, 3213);
					resetInterfaceAnimation(15106);
					inputTaken = true;
					break;
				default:
					stream.createFrame(185);
					stream.writeWord(interfaceId);
					break;

				}
			}
		}
		switch (actionId) {
		case 1508:
			orbTextEnabled[0] = !orbTextEnabled[0];
			break;
		case 1509:
			orbTextEnabled[1] = !orbTextEnabled[1];
			break;
		case 1510:
			orbTextEnabled[2] = !orbTextEnabled[2];
			break;
		case 1511:
			orbTextEnabled[3] = !orbTextEnabled[3];
			break;
		case 1500: // Toggle quick prayers
			try {
				int currentPray;
				currentPray = currentStats[5];
				if (currentPray != 0)
					prayClicked = !prayClicked;
				else {
					prayClicked = false;
					pushMessage("You have run out of prayer points!", 0, "");
				}
				stream.createFrame(185);
				stream.writeWord(6000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case 1506: // Select quick prayers
			stream.createFrame(185);
			stream.writeWord(6001);
			break;
		}
		if (actionId == 561) {
			Player player = playerArray[nodeId];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0, player.pathY[0],
						myPlayer.pathX[0], false, player.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1188 += nodeId;
				if (anInt1188 >= 90) {
					stream.createFrame(136);
					anInt1188 = 0;
				}
				stream.createFrame(128);
				stream.writeWord(nodeId);
			}
		}
		if (actionId == 20) {
			NPC npc = npcArray[nodeId];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0, npc.pathY[0],
						myPlayer.pathX[0], false, npc.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(155);
				stream.writeUnsignedWordBigEndian(nodeId);
			}
		}
		if (actionId == 779) {
			Player plr = playerArray[nodeId];
			if (plr != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0, plr.pathY[0],
						myPlayer.pathX[0], false, plr.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(153);
				stream.writeUnsignedWordBigEndian(nodeId);
			}
		}
		if (actionId == 516)
			if (!menuOpen)
				worldController.request2DTrace(super.saveClickY - 4,
						super.saveClickX - 4);
			else
				worldController.request2DTrace(interfaceId - 4, slot - 4);
		if (actionId == 1062) {
			anInt924 += baseX;
			if (anInt924 >= 113) {
				stream.createFrame(183);
				stream.writeDWordBigEndian(0xe63271);
				anInt924 = 0;
			}
			reachedClickedObject(nodeId, y, x, id);
			stream.createFrame(228);
			stream.writeUnsignedWordA(id);
			stream.writeUnsignedWordA(y + baseY);
			stream.writeWord(x + baseX);
		}
		if (actionId == 679 && !dialogueOptionsShowing) {
			stream.createFrame(40);
			stream.writeWord(interfaceId);
			dialogueOptionsShowing = true;
		}
		if (actionId == 431) {
			stream.createFrame(129);
			stream.writeUnsignedWordA(slot);
			stream.writeWord(interfaceId);
			stream.writeUnsignedWordA(nodeId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 337 || actionId == 42 || actionId == 792 || actionId == 322) {
			String s = menuActionName[menuIndex];
			int startIndex = s.indexOf("<col=0xffffff>");
			if (startIndex != -1) {
				try {
					String name = s.substring(startIndex + 14);
					long nameID = TextClass.longForName(s.substring(startIndex + 14 + (name.indexOf("<img=") == 0 ? 7 : 0)).trim());
					if (actionId == 337)
						addFriend(nameID);
					if (actionId == 42)
						addIgnore(nameID);
					if (actionId == 792)
						delFriend(nameID);
					if (actionId == 322)
						delIgnore(nameID);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (actionId == 1315) {
			inputString = "/[FRI]" +  " " + clanName;
			sendPacket(1003);
		}
		if (actionId == 1316) {
			inputString = "/[REC]" +  " " + clanName;
			System.out.println(inputString);
			sendPacket(1003);
		}
		if (actionId == 1317) {
			inputString = "/[COR]" +  " " + clanName;
			sendPacket(1003);
		}
		if (actionId == 1318) {
			inputString = "/[SER]" +  " " + clanName;
			sendPacket(1003);
		}
		if (actionId == 1319) {
			inputString = "/[LIE]" +  " " + clanName;
			sendPacket(1003);
		}
		if (actionId == 1320) {
			inputString = "/[CAP]" +  " " + clanName;
			sendPacket(1003);
		}
		if (actionId == 1321) {
			inputString = "/[GEN]" +  " " + clanName;
			sendPacket(1003);
		}
		if (actionId == 53) {
			stream.createFrame(135);
			stream.writeUnsignedWordBigEndian(slot);
			stream.writeUnsignedWordA(interfaceId);
			stream.writeUnsignedWordBigEndian(nodeId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 539) {
			stream.createFrame(16);
			stream.writeUnsignedWordA(nodeId);
			stream.writeSignedBigEndian(slot);
			stream.writeSignedBigEndian(interfaceId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 484 || actionId == 6) {
			String rawUsername = menuActionName[menuIndex];
			int l1 = rawUsername.indexOf("<col=0xffffff>");
			if (l1 != -1) {
				rawUsername = rawUsername.substring(l1 + 14).trim();
				String playerUsername = TextClass.fixName(TextClass.nameForLong(TextClass.longForName(rawUsername)));
				boolean found = false;
				for (int playerIndex = 0; playerIndex < playerCount; playerIndex++) {
					Player player = playerArray[playerIndices[playerIndex]];
					if (player == null || player.name == null || !player.name.equalsIgnoreCase(playerUsername))
						continue;
					doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0, player.pathY[0], myPlayer.pathX[0], false, player.pathX[0]);
					if (actionId == 484) {
						stream.createFrame(39);
						stream.writeUnsignedWordBigEndian(playerIndices[playerIndex]);
					}
					if (actionId == 6) {
						anInt1188 += nodeId;
						if (anInt1188 >= 90) {
							stream.createFrame(136);
							anInt1188 = 0;
						}
						stream.createFrame(128);
						stream.writeWord(playerIndices[playerIndex]);
					}
					found = true;
					break;
				}

				if (!found)
					pushMessage("Unable to find " + playerUsername, 0, "");
			}
		}
		if (actionId == 870) {
			stream.createFrame(53);
			stream.writeWord(slot);
			stream.writeUnsignedWordA(lastItemSelectedSlot);
			stream.writeSignedBigEndian(nodeId);
			stream.writeWord(lastItemSelectedInterface);
			stream.writeUnsignedWordBigEndian(selectedItemId);
			stream.writeWord(interfaceId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		switch (interfaceId) {
		//toggle size client
		case 40049:
			if(isWebclient() && !onFullPage) {
				pushMessage("Please open the fullscreen webclient to enable fullscreen((http://rxclient.net/fullscreen) or download.", 0, "");
				return;
			}
			toggleSize(2);
			break;
		case 40046:
			if(isWebclient() && !onFullPage) {
				pushMessage("Please open the fullscreen webclient to enable resizeable(http://rxclient.net/fullscreen) or download.", 0, "");
				return;
			}
			toggleSize(1); 
			break;
		case 40043:
			toggleSize(0);
			break;
		case 40039:
			clearTopInterfaces();
			break;

		}
		if (actionId == 847) {
			stream.createFrame(87);
			stream.writeUnsignedWordA(nodeId);
			stream.writeWord(interfaceId);
			stream.writeUnsignedWordA(slot);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 626) {
			RSInterface class9_1 = RSInterface.interfaceCache[interfaceId];
			spellSelected = 1;
			spellID = class9_1.id;
			selectedSpellId = interfaceId;
			spellUsableOn = class9_1.spellUsableOn;
			itemSelected = 0;
			needDrawTabArea = true;
			spellID = class9_1.id;
			String s4 = class9_1.selectedActionName;
			if (s4.indexOf(" ") != -1)
				s4 = s4.substring(0, s4.indexOf(" "));
			String s8 = class9_1.selectedActionName;
			if (s8.indexOf(" ") != -1)
				s8 = s8.substring(s8.indexOf(" ") + 1);
			spellTooltip = s4 + " " + class9_1.spellName + " " + s8;
			if (spellUsableOn == 16) {
				needDrawTabArea = true;
				tabID = 3;
				tabAreaAltered = true;
			}
			return;
		}
		if (actionId == 78) {
			stream.createFrame(117);
			stream.writeSignedBigEndian(interfaceId);
			stream.writeSignedBigEndian(nodeId);
			stream.writeUnsignedWordBigEndian(slot);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 27) {
			Player player = playerArray[nodeId];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						player.pathY[0],
						myPlayer.pathX[0], false,
						player.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt986 += nodeId;
				if (anInt986 >= 54) {
					stream.createFrame(189);
					stream.writeByte1(234);
					anInt986 = 0;
				}
				stream.createFrame(73);
				stream.writeUnsignedWordBigEndian(nodeId);
			}
		}
		if (actionId == 213) {
			boolean flag3 = doWalkTo(2, 0, 0, 0, myPlayer.pathY[0], 0, 0,
					interfaceId, myPlayer.pathX[0], false, slot);
			if (!flag3)
				flag3 = doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						interfaceId, myPlayer.pathX[0], false, slot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(79);
			stream.writeUnsignedWordBigEndian(interfaceId + baseY);
			stream.writeWord(nodeId);
			stream.writeUnsignedWordA(slot + baseX);
		}
		if (actionId == 632) {
			stream.createFrame(145);
			stream.writeUnsignedWordA(interfaceId);
			stream.writeUnsignedWordA(slot);
			stream.writeUnsignedWordA(nodeId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 888) {
			inputString = "[BS1]";
			sendPacket(1003);
		}
		if (actionId == 889) {
			inputString = "[BS2]";
			sendPacket(1003);
		}
		if (actionId == 890) {
			inputString = "[BB1]";
			sendPacket(1003);
		}
		if (actionId == 891) {
			inputString = "[BB2]";
			sendPacket(1003);
		}

		if (actionId == 1004) {
			if (tabInterfaceIDs[10] != -1) {
				needDrawTabArea = true;
				tabID = 10;
				tabAreaAltered = true;
			}
		}
		if (actionId == 1003) {
			clanChatMode = 2;
			inputTaken = true;
		}
		if (actionId == 1002) {
			clanChatMode = 1;
			inputTaken = true;
		}
		if (actionId == 1001) {
			clanChatMode = 0;
			inputTaken = true;
		}
		if (actionId == 1000) {
			cButtonCPos = 4;
			chatTypeView = 11;
			inputTaken = true;
		}
		if (actionId == 999) {
			cButtonCPos = 0;
			chatTypeView = 0;
			inputTaken = true;
		}
		if (actionId == 998) {
			cButtonCPos = 1;
			chatTypeView = 5;
			inputTaken = true;
		}
		if (actionId == 997) {
			publicChatMode = 3;
			inputTaken = true;
		}
		if (actionId == 996) {
			publicChatMode = 2;
			inputTaken = true;
		}
		if (actionId == 995) {
			publicChatMode = 1;
			inputTaken = true;
		}
		if (actionId == 994) {
			publicChatMode = 0;
			inputTaken = true;
		}
		if (actionId == 993) {
			cButtonCPos = 2;
			chatTypeView = 1;
			inputTaken = true;
		}
		if (actionId == 992) {
			privateChatMode = 2;
			inputTaken = true;
		}
		if (actionId == 991) {
			privateChatMode = 1;
			inputTaken = true;
		}
		if (actionId == 990) {
			privateChatMode = 0;
			inputTaken = true;
		}
		if (actionId == 989) {
			cButtonCPos = 3;
			chatTypeView = 2;
			inputTaken = true;
		}
		if (actionId == 987) {
			tradeMode = 2;
			inputTaken = true;
		}
		if (actionId == 986) {
			tradeMode = 1;
			inputTaken = true;
		}
		if (actionId == 985) {
			tradeMode = 0;
			inputTaken = true;
		}
		if (actionId == 984) {
			cButtonCPos = 5;
			chatTypeView = 3;
			inputTaken = true;
		}
		if (actionId == 983) {
			duelMode = 2;
			yellMode = 3;
			inputTaken = true;
		}
		if (actionId == 982) {
			duelMode = 1;
			yellMode = 2;
			inputTaken = true;
		}
		if (actionId == 981) {
			duelMode = 0;
			yellMode = 1;
			inputTaken = true;
		}
		if (actionId == 980) {
			duelMode = 0;
			yellMode = 0;
			inputTaken = true;
		}
		if (actionId == 979) {
			cButtonCPos = 6;
			chatTypeView = 4;
			inputTaken = true;
		}
		if (actionId == 798) {
			gameChatMode = 4;
			inputTaken = true;
			filterMessages = true;
			inputTaken = true;
		}
		if (actionId == 797) {
			gameChatMode = 5;
			inputTaken = true;
			filterMessages = false;
			inputTaken = true;
		}
		if (actionId == 493) {
			stream.createFrame(75);
			stream.writeSignedBigEndian(interfaceId);
			stream.writeUnsignedWordBigEndian(slot);
			stream.writeUnsignedWordA(nodeId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 652) {
			boolean flag4 = doWalkTo(2, 0, 0, 0, myPlayer.pathY[0], 0, 0,
					interfaceId, myPlayer.pathX[0], false, slot);
			if (!flag4)
				flag4 = doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						interfaceId, myPlayer.pathX[0], false, slot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(156);
			stream.writeUnsignedWordA(slot + baseX);
			stream.writeUnsignedWordBigEndian(interfaceId + baseY);
			stream.writeSignedBigEndian(nodeId);
		}
		if (actionId == 94) {
			boolean flag5 = doWalkTo(2, 0, 0, 0, myPlayer.pathY[0], 0, 0,
					interfaceId, myPlayer.pathX[0], false, slot);
			if (!flag5)
				flag5 = doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						interfaceId, myPlayer.pathX[0], false, slot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(181);
			stream.writeUnsignedWordBigEndian(interfaceId + baseY);
			stream.writeWord(nodeId);
			stream.writeUnsignedWordBigEndian(slot + baseX);
			stream.writeUnsignedWordA(selectedSpellId);
		}
		if (actionId == 646) {
			stream.createFrame(185);
			stream.writeWord(interfaceId);
			RSInterface class9_2 = RSInterface.interfaceCache[interfaceId];
			if (class9_2.valueIndexArray != null
					&& class9_2.valueIndexArray[0][0] == 5) {
				int i2 = class9_2.valueIndexArray[0][1];
				if (variousSettings[i2] != class9_2.requiredValues[0]) {
					variousSettings[i2] = class9_2.requiredValues[0];
					handleActions(i2);
					needDrawTabArea = true;
				}
			}
		}
		if (actionId == 225) {
			NPC npc = npcArray[nodeId];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						npc.pathY[0],
						myPlayer.pathX[0], false,
						npc.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1226 += nodeId;
				if (anInt1226 >= 85) {
					/*stream.createFrame(230);
					stream.writeWordBigEndian(239);*/
					anInt1226 = 0;
				}
				stream.createFrame(17);
				stream.writeSignedBigEndian(nodeId);
			}
		}
		if (actionId == 965) {
			NPC npc = npcArray[nodeId];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						npc.pathY[0],
						myPlayer.pathX[0], false,
						npc.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1134++;
				if (anInt1134 >= 96) {
					stream.createFrame(152);
					stream.writeByte1(88);
					anInt1134 = 0;
				}
				stream.createFrame(21);
				stream.writeWord(nodeId);
			}
		}
		if (actionId == 413) {
			NPC npc = npcArray[nodeId];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						npc.pathY[0],
						myPlayer.pathX[0], false,
						npc.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(131);
				stream.writeSignedBigEndian(nodeId);
				stream.writeUnsignedWordA(selectedSpellId);
			}
		}
		if (actionId == 200)
			clearTopInterfaces();
		if (actionId == 1025) {
			NPC npc = npcArray[nodeId];
			if (npc != null) {
				NPCDef entityDef = npc.desc;
				if (entityDef.childrenIDs != null)
					entityDef = entityDef.getAlteredNPCDef();
				if (entityDef != null) {
					String s9;
					if (entityDef.description != null)
						s9 = new String(entityDef.description);
					else
						s9 = "You don't find anything interesting about the "
								+ entityDef.name + ".";
					pushMessage(s9, 0, "");
				}
			}
		}
		if (actionId == 900) {
			reachedClickedObject(nodeId, y, x, id);
			stream.createFrame(252);
			stream.writeSignedBigEndian(id);
			stream.writeUnsignedWordBigEndian(y + baseY);
			stream.writeUnsignedWordA(x + baseX);
		}
		if (actionId == 412) {
			NPC npc = npcArray[nodeId];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						npc.pathY[0],
						myPlayer.pathX[0], false,
						npc.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(72);
				stream.writeUnsignedWordA(nodeId);
			}
		}
		if (actionId == 365) {
			Player player = playerArray[nodeId];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						player.pathY[0],
						myPlayer.pathX[0], false,
						player.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(249);
				stream.writeUnsignedWordA(nodeId);
				stream.writeUnsignedWordBigEndian(selectedSpellId);
			}
		}
		if (actionId == 729) {
			Player player = playerArray[nodeId];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						player.pathY[0],
						myPlayer.pathX[0], false,
						player.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(39);
				stream.writeUnsignedWordBigEndian(nodeId);
			}
		}
		if(actionId == 1200) {
			Player player = playerArray[nodeId];
			String inp = "";
			inp = inputString;
			inputString = "/invite " + player.name;
			sendPacket(1003);
			inputString = inp;
		}
		if(actionId == 1201) {
			String rawUsername = menuActionName[menuIndex];
			int l1 = rawUsername.indexOf("<col=0xffffff>");
			if (l1 != -1) {
				rawUsername = rawUsername.substring(l1 + 14).trim();
				String playerUsername = TextClass.fixName(TextClass.nameForLong(TextClass.longForName(rawUsername)));
				boolean found = false;
				for (int playerIndex = 0; playerIndex < playerCount; playerIndex++) {
					Player player = playerArray[playerIndices[playerIndex]];
					if (player == null || player.name == null || !player.name.equalsIgnoreCase(playerUsername))
						continue;
					doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0, player.pathY[0], myPlayer.pathX[0], false, player.pathX[0]);
					String inp = "";
					inp = inputString;
					inputString = "::accept " + playerUsername;
					sendPacket(103);
					inputString = inp;
					found = true;
					break;
				}

				if (!found)
					pushMessage("Unable to find " + playerUsername, 0, "");
			}
		}
		if(actionId == 1202) {
			String rawUsername = menuActionName[menuIndex];
			int l1 = rawUsername.indexOf("<col=0xffffff>");
			if (l1 != -1) {
				rawUsername = rawUsername.substring(l1 + 14).trim();
				String playerUsername = TextClass.fixName(TextClass.nameForLong(TextClass.longForName(rawUsername)));
				boolean found = false;
				for (int playerIndex = 0; playerIndex < playerCount; playerIndex++) {
					Player player = playerArray[playerIndices[playerIndex]];
					if (player == null || player.name == null || !player.name.equalsIgnoreCase(playerUsername))
						continue;
					doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0, player.pathY[0], myPlayer.pathX[0], false, player.pathX[0]);
					String inp = "";
					inp = inputString;
					inputString = "::acceptwar " + playerUsername;
					sendPacket(103);
					inputString = inp;
					found = true;
					break;
				}

				if (!found)
					pushMessage("Unable to find " + playerUsername, 0, "");
			}
		}
		if (actionId == 577) {
			Player player = playerArray[nodeId];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						player.pathY[0],
						myPlayer.pathX[0], false,
						player.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(139);
				stream.writeUnsignedWordBigEndian(nodeId);
			}
		}
		if (actionId == 956 && reachedClickedObject(nodeId, y, x, id)) {
			stream.createFrame(35);
			stream.writeUnsignedWordBigEndian(x + baseX);
			stream.writeUnsignedWordA(selectedSpellId);
			stream.writeUnsignedWordA(y + baseY);
			stream.writeUnsignedWordBigEndian(id);
		}
		if (actionId == 567) {
			boolean flag6 = doWalkTo(2, 0, 0, 0, myPlayer.pathY[0], 0, 0,
					interfaceId, myPlayer.pathX[0], false, slot);
			if (!flag6)
				flag6 = doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						interfaceId, myPlayer.pathX[0], false, slot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(23);
			stream.writeUnsignedWordBigEndian(interfaceId + baseY);
			stream.writeUnsignedWordBigEndian(nodeId);
			stream.writeUnsignedWordBigEndian(slot + baseX);
		}
		if (actionId == 867) {
			if ((nodeId & 3) == 0)
				anInt1175++;
			if (anInt1175 >= 59) {
				stream.createFrame(200);
				stream.writeWord(25501);
				anInt1175 = 0;
			}
			stream.createFrame(43);
			stream.writeUnsignedWordBigEndian(interfaceId);
			stream.writeUnsignedWordA(nodeId);
			stream.writeUnsignedWordA(slot);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 543) {
			stream.createFrame(237);
			stream.writeWord(slot);
			stream.writeUnsignedWordA(nodeId);
			stream.writeWord(interfaceId);
			stream.writeUnsignedWordA(selectedSpellId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 606) {
			String toReport = menuActionName[menuIndex];
			try {
				do {
					toReport = toReport.substring(toReport.indexOf(">") + 1);
				} while(toReport.indexOf(">") != -1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (openInterfaceID == -1) {
				clearTopInterfaces();
				reportAbuseInput = toReport.trim();
				canMute = false;
				for (int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++) {
					if (RSInterface.interfaceCache[i3] == null
							|| RSInterface.interfaceCache[i3].contentType != 600)
						continue;
					reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i3].parentID;
					break;
				}

			} else {
				pushMessage(
						"Please close the interface you have open before using 'report abuse'",
						0, "");
			}
		}
		if (actionId == 491) {
			Player player = playerArray[nodeId];
			if (player != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						player.pathY[0], myPlayer.pathX[0], false, player.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(14);
				stream.writeUnsignedWordA(lastItemSelectedInterface);
				stream.writeWord(nodeId);
				stream.writeWord(selectedItemId);
				stream.writeUnsignedWordBigEndian(lastItemSelectedSlot);
			}
		}
		if (actionId == 639) {
			String menuName = menuActionName[menuIndex];
			try {
				do {
					menuName = menuName.substring(menuName.indexOf(">") + 1);
				} while(menuName.indexOf(">") != -1);
			} catch(Exception e) {
				e.printStackTrace();
			}
			//System.out.println("Message To: " + menuName.trim());
			long userLong = TextClass.longForName(menuName.trim());
			int friendIndex = -1;
			for (int index = 0; index < friendsCount; index++) {
				if (friendsListAsLongs[index] != userLong)
					continue;
				friendIndex = index;
				break;
			}

			if (friendIndex != -1 && friendsNodeIDs[friendIndex] > 0) {
				inputTaken = true;
				inputDialogState = 0;
				showInput = true;
				promptInput = "";
				friendsListAction = 3;
				aLong953 = friendsListAsLongs[friendIndex];
				promptMessage = "Enter message to send to " + friendsList[friendIndex];
			}
		}
		if (actionId == 454) {
			stream.createFrame(41);
			stream.writeWord(nodeId);
			stream.writeUnsignedWordA(slot);
			stream.writeUnsignedWordA(interfaceId);
			atInventoryLoopCycle = 0;
			atInventoryInterface = interfaceId;
			atInventoryIndex = slot;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[interfaceId].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[interfaceId].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (actionId == 478) {
			NPC npc = npcArray[nodeId];
			if (npc != null) {
				doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0, npc.pathY[0], myPlayer.pathX[0], false, npc.pathX[0]);
				crossX = super.saveClickX;
				crossY = super.saveClickY;
				crossType = 2;
				crossIndex = 0;
				if ((nodeId & 3) == 0)
					anInt1155++;
				if (anInt1155 >= 53) {
					stream.createFrame(85);
					stream.writeByte1(66);
					anInt1155 = 0;
				}
				stream.createFrame(18);
				stream.writeUnsignedWordBigEndian(nodeId);
			}
		}
		if (actionId == 113) {
			reachedClickedObject(nodeId, y, x, id);
			stream.createFrame(70);
			stream.writeUnsignedWordBigEndian(x + baseX);
			stream.writeWord(y + baseY);
			stream.writeSignedBigEndian(id);
		}
		if (actionId == 872) {
			reachedClickedObject(nodeId, y, x, id);
			stream.createFrame(234);
			stream.writeSignedBigEndian(x + baseX);
			stream.writeUnsignedWordA(id);
			stream.writeSignedBigEndian(y + baseY);
		}
		if (actionId == 502) {
			reachedClickedObject(nodeId, y, x, id);
			stream.createFrame(132);
			stream.writeSignedBigEndian(x + baseX);
			stream.writeWord(id);
			stream.writeUnsignedWordA(y + baseY);
		}
		if (actionId == 1125) {
			ItemDef itemDef = ItemDef.forID(nodeId);
			RSInterface rsi = RSInterface.interfaceCache[interfaceId];
			if (interfaceId == 38274) {
				stream.createFrame(122);
				stream.writeWord(interfaceId);
				stream.writeWord(slot);
				stream.writeWord(nodeId);

			} else {
				String examine;
				if (rsi != null && rsi.invStackSizes[slot] >= 0x186a0)
					examine = rsi.invStackSizes[slot] + " x " + itemDef.name;
				else
					examine = itemDef.description;
				if (examine.contains("<")) {
					examine = "It's a " + itemDef.name + ".";
				}
				pushMessage(examine, 0, "");
			}
		}
		if (actionId == 169) {
			stream.createFrame(185);
			stream.writeWord(interfaceId);
			RSInterface rsi = RSInterface.interfaceCache[interfaceId];
			if (rsi.valueIndexArray != null
					&& rsi.valueIndexArray[0][0] == 5) {
				int l2 = rsi.valueIndexArray[0][1];
				variousSettings[l2] = 1 - variousSettings[l2];
				handleActions(l2);
				needDrawTabArea = true;
			}
			switch (interfaceId) {
			case 35565:// hd tex
				options.put("hd_tex", !getOption("hd_tex"));
				OverLayFlo317.unpackConfig(streamLoaderForName(2, "config", "config", expectedCRCs[2], 30));
				loadRegion();
				break;
			case 35568:// player shadow
				options.put("player_shadow", !getOption("player_shadow"));
				Player.modelCache.clear();
				break;
			case 35571:// tooltip shadow
				options.put("tooltip_hover", !getOption("tooltip_hover"));
				break;
			}
		}
		if (actionId == 447) {
			itemSelected = 1;
			lastItemSelectedSlot = slot;
			lastItemSelectedInterface = interfaceId;
			selectedItemId = nodeId;
			selectedItemName = ItemDef.forID(nodeId).name;
			spellSelected = 0;
			needDrawTabArea = true;
			return;
		}
		if (actionId == 1226) {
			int j1 = nodeId >> 14 & 0x7fff;
			ObjectDef class46 = ObjectDef.forID(j1);
			String s10;
			if (class46.description != null)
				s10 = new String(class46.description);
			else
				s10 = "You don't find anything interesting about the "
						+ class46.name + ".";
			pushMessage(s10, 0, "");
		}
		if (actionId == 244) {
			boolean flag7 = doWalkTo(2, 0, 0, 0, myPlayer.pathY[0], 0, 0,
					interfaceId, myPlayer.pathX[0], false, slot);
			if (!flag7)
				flag7 = doWalkTo(2, 0, 1, 0, myPlayer.pathY[0], 1, 0,
						interfaceId, myPlayer.pathX[0], false, slot);
			crossX = super.saveClickX;
			crossY = super.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(253);
			stream.writeUnsignedWordBigEndian(slot + baseX);
			stream.writeSignedBigEndian(interfaceId + baseY);
			stream.writeUnsignedWordA(nodeId);
		}
		if (actionId == 1448) {
			ItemDef itemDef_1 = ItemDef.forID(nodeId);
			String s6;
			s6 = itemDef_1.description;
			if (s6.contains("<")) {
				s6 = "It's a " + itemDef_1.name + ".";
			}
			pushMessage(s6, 0, "");
		}
		itemSelected = 0;
		spellSelected = 0;
		needDrawTabArea = true;

	}

	private void checkTutorialIsland() {
		isOnTutorialIsland = 0;
		int j = (myPlayer.x >> 7) + baseX;
		int k = (myPlayer.y >> 7) + baseY;
		if (j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136)
			isOnTutorialIsland = 1;
		if (j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535)
			isOnTutorialIsland = 1;
		if (isOnTutorialIsland == 1 && j >= 3139 && j <= 3199 && k >= 3008
				&& k <= 3062)
			isOnTutorialIsland = 0;
	}

	public void run() {
		super.run();
	}

	private void build3dScreenMenu() {
		if (itemSelected == 0 && spellSelected == 0) {
			menuActionName[menuActionRow] = "Walk here";
			menuActionID[menuActionRow] = 516;
			menuActionCmd2[menuActionRow] = super.mouseX;
			menuActionCmd3[menuActionRow] = super.mouseY;
			menuActionRow++;
		}
		int lastUID = -1;
		for (int index = 0; index < Model.objectsRendered; index++) {
			int uid = Model.objectsInCurrentRegion[index];
			int x = uid & 0x7f;
			int y = uid >> 7 & 0x7f;
			int resourceType = uid >> 29 & 3; // k1
			int resourceId = uid >> 14 & 0x7fff;
			if (uid == lastUID)
				continue;
			lastUID = uid;
			if (resourceType == 2 && worldController.getIDTagForXYZ(plane, x, y, uid) >= 0) {
				if (resourceId != 1814)
					resourceId = Model.mapObjectIds[index];
				ObjectDef object = ObjectDef.forID(resourceId);
				if (object.configObjectIDs != null)
					object = object.getTransformedObject();
				if (object == null || object.name == null || object.name == "null")
					continue;
				if (itemSelected == 1) {
					menuActionName[menuActionRow] = "Use " + selectedItemName
							+ " -> @cya@" + object.name;
					menuActionID[menuActionRow] = 62;
					menuActionCmd1[menuActionRow] = uid;
					menuActionCmd2[menuActionRow] = x;
					menuActionCmd3[menuActionRow] = y;
					menuActionCmd4[menuActionRow] = resourceId;
					menuActionRow++;
				} else if (spellSelected == 1) {
					if ((spellUsableOn & 4) == 4) {
						menuActionName[menuActionRow] = spellTooltip + " @cya@"
								+ object.name;
						menuActionID[menuActionRow] = 956;
						menuActionCmd1[menuActionRow] = uid;
						menuActionCmd2[menuActionRow] = x;
						menuActionCmd3[menuActionRow] = y;
						menuActionCmd4[menuActionRow] = resourceId;
						menuActionRow++;
					}
				} else {
					if (object.actions != null) {
						for (int i2 = 4; i2 >= 0; i2--)
							if (object.actions[i2] != null) {
								if (variousSettings[8000] == 1) {
									if (object.actions[i2].equals("Remove")
											|| object.actions[i2]
													.equals("Upgrade")
											|| object.actions[i2]
													.equals("Remove-room"))
										continue;
								}
								menuActionName[menuActionRow] = object.actions[i2]
										+ " @cya@" + object.name;
								if (i2 == 0)
									menuActionID[menuActionRow] = 502;
								if (i2 == 1)
									menuActionID[menuActionRow] = 900;
								if (i2 == 2)
									menuActionID[menuActionRow] = 113;
								if (i2 == 3)
									menuActionID[menuActionRow] = 872;
								if (i2 == 4)
									menuActionID[menuActionRow] = 1062;
								menuActionCmd1[menuActionRow] = uid;
								menuActionCmd2[menuActionRow] = x;
								menuActionCmd3[menuActionRow] = y;
								menuActionCmd4[menuActionRow] = resourceId;
								menuActionRow++;
							}
					}
					
					String modelIds = "";
					for(int mid = 0; mid < object.objectModelIDs.length; mid++) {
						modelIds = modelIds + "" + object.objectModelIDs[mid] + ",";
					}
					/*
					 * right click ids
					 */
					menuActionName[menuActionRow] = "Examine @cya@"
							+ object.name + " @gre@(@whi@" + object.type
							+ "@gre@) (@whi@" + (x + baseX) + "," + (y + baseY)
							+ "@gre@) (@whi@" + modelIds + "@gre@)";
					menuActionID[menuActionRow] = 1226;
					menuActionCmd1[menuActionRow] = object.type << 14;
					menuActionCmd2[menuActionRow] = x;
					menuActionCmd3[menuActionRow] = y;
					menuActionCmd4[menuActionRow] = resourceId;
					menuActionRow++;
				}
			}
			if (resourceType == 1) {
				NPC npc = npcArray[resourceId];
				if (npc.desc.squaresNeeded == 1 && (npc.x & 0x7f) == 64
						&& (npc.y & 0x7f) == 64) {
					for (int j2 = 0; j2 < npcCount; j2++) {
						NPC npc2 = npcArray[npcIndices[j2]];
						if (npc2 != null && npc2 != npc
								&& npc2.desc.squaresNeeded == 1
								&& npc2.x == npc.x && npc2.y == npc.y)
							buildAtNPCMenu(npc2.desc, npcIndices[j2], y, x);
					}

					for (int l2 = 0; l2 < playerCount; l2++) {
						Player player = playerArray[playerIndices[l2]];
						if (player != null && player.x == npc.x
								&& player.y == npc.y)
							buildAtPlayerMenu(x, playerIndices[l2], player, y);
					}

				}
				buildAtNPCMenu(npc.desc, resourceId, y, x);
			}
			if (resourceType == 0) {
				Player player = playerArray[resourceId];
				if ((player.x & 0x7f) == 64 && (player.y & 0x7f) == 64) {
					for (int k2 = 0; k2 < npcCount; k2++) {
						NPC npc = npcArray[npcIndices[k2]];
						try {
							if (npc != null && npc.desc.squaresNeeded == 1 && npc.x == player.x && npc.y == player.y)
								buildAtNPCMenu(npc.desc, npcIndices[k2], y, x);
						} catch (Exception _ex) {
							_ex.printStackTrace();
						}
					}

					for (int i3 = 0; i3 < playerCount; i3++) {
						Player plyr = playerArray[playerIndices[i3]];
						if (plyr != null
								&& plyr != player
								&& plyr.x == player.x
								&& plyr.y == player.y)
							buildAtPlayerMenu(x, playerIndices[i3],
									plyr, y);
					}

				}
				buildAtPlayerMenu(x, resourceId, player, y);
			}
			if (resourceType == 3) {
				Deque class19 = groundArray[plane][x][y];
				if (class19 != null) {
					for (Item item = (Item) class19.getBack(); item != null; item = (Item) class19.getPrevious()) {
						ItemDef itemDef = ItemDef.forID(item.ID);
						if (itemSelected == 1) {
							menuActionName[menuActionRow] = "Use "
									+ selectedItemName + " with @lre@"
									+ itemDef.name;
							menuActionID[menuActionRow] = 511;
							menuActionCmd1[menuActionRow] = item.ID;
							menuActionCmd2[menuActionRow] = x;
							menuActionCmd3[menuActionRow] = y;
							menuActionRow++;
						} else if (spellSelected == 1) {
							if ((spellUsableOn & 1) == 1) {
								menuActionName[menuActionRow] = spellTooltip
										+ " @lre@" + itemDef.name;
								menuActionID[menuActionRow] = 94;
								menuActionCmd1[menuActionRow] = item.ID;
								menuActionCmd2[menuActionRow] = x;
								menuActionCmd3[menuActionRow] = y;
								menuActionRow++;
							}
						} else {
							for (int j3 = 4; j3 >= 0; j3--)
								if (itemDef.groundActions != null
										&& itemDef.groundActions[j3] != null) {
									menuActionName[menuActionRow] = itemDef.groundActions[j3]
											+ " @lre@" + itemDef.name;
									if (j3 == 0)
										menuActionID[menuActionRow] = 652;
									if (j3 == 1)
										menuActionID[menuActionRow] = 567;
									if (j3 == 2)
										menuActionID[menuActionRow] = 234;
									if (j3 == 3)
										menuActionID[menuActionRow] = 244;
									if (j3 == 4)
										menuActionID[menuActionRow] = 213;
									menuActionCmd1[menuActionRow] = item.ID;
									menuActionCmd2[menuActionRow] = x;
									menuActionCmd3[menuActionRow] = y;
									menuActionRow++;
								} else if (j3 == 2) {
									menuActionName[menuActionRow] = "Take @lre@"
											+ itemDef.name;
									menuActionID[menuActionRow] = 234;
									menuActionCmd1[menuActionRow] = item.ID;
									menuActionCmd2[menuActionRow] = x;
									menuActionCmd3[menuActionRow] = y;
									menuActionRow++;
								}
							menuActionName[menuActionRow] = "Examine @lre@"
									+ itemDef.name;
							menuActionID[menuActionRow] = 1448;
							menuActionCmd1[menuActionRow] = item.ID;
							menuActionCmd2[menuActionRow] = x;
							menuActionCmd3[menuActionRow] = y;
							menuActionRow++;
						}
					}

				}
			}
		}
	}

	public void cleanUpForQuit() {
		signlink.reporterror = false;
		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		socketStream = null;
		//stopMidi();
		if (mouseDetection != null)
			mouseDetection.running = false;
		mouseDetection = null;
		onDemandFetcher.dispose();
		onDemandFetcher = null;
		textStream = null;
		prayClicked = false;
		stream = null;
		aStream_847 = null;
		inStream = null;
		mapCoordinates = null;
		terrainData = null;
		objectData = null;
		terrainIndices = null;
		objectIndices = null;
		intGroundArray = null;
		tileSettingBits = null;
		worldController = null;
		clippingPlanes = null;
		pathwayPoint = null;
		anIntArrayArray825 = null;
		bigX = null;
		bigY = null;
		tabAreaIP = null;
		leftFrame = null;
		topFrame = null;
		rightFrame = null;
		mapAreaIP = null;
		gameScreenIP = null;
		chatAreaIP = null;
		GraphicsBuffer_1125 = null;
		backgroundFix = null;
		candle1 = null;
		candle2 = null;
		fire1 = null;
		fire2 = null;
		fire3 = null;
		registerButton = null;
		loginBackButton = null;
		loginButton = null;
		scroll = null;
		scrollBottom = null;
		/* Null pointers for custom sprites */
		loadingPleaseWait = null;
		reestablish = null;
		WorldOrb = null;
		HPBarFull = null;
		HPBarEmpty = null;
		HPBarBigEmpty = null;
		newMapBack = null;
		orbs = null;
		LOGOUT = null;
		/**/
		sideIcons = null;
		compass = null;
		headIcons = null;
		skullIcons = null;
		headIconsHint = null;
		crosses = null;
		mapDotItem = null;
		mapDotNPC = null;
		mapDotPlayer = null;
		mapDotFriend = null;
		mapDotTeam = null;
		mapScenes = null;
		mapFunctions = null;
		tileCycleMap = null;
		playerArray = null;
		playerIndices = null;
		playersToUpdate = null;
		aStreamArray895s = null;
		anIntArray840 = null;
		npcArray = null;
		npcIndices = null;
		groundArray = null;
		objectSpawnDeque = null;
		projectileDeque = null;
		stillGraphicDeque = null;
		menuActionCmd2 = null;
		menuActionCmd3 = null;
		menuActionCmd4 = null;
		menuActionID = null;
		menuActionCmd1 = null;
		menuActionName = null;
		variousSettings = null;
		mapFunctionTileX = null;
		mapFunctionTileY = null;
		currentMapFunctionSprites = null;
		miniMap = null;
		friendsList = null;
		friendsListAsLongs = null;
		friendsNodeIDs = null;
		GraphicsBuffer_1107 = null;
		titleScreen = null;
		multiOverlay = null;
		nullLoader();
		ObjectDef.nullLoader();
		NPCDef.nullLoader();
		ItemDef.nullLoader();
		Flo.cache = null;
		IDK.cache = null;
		RSInterface.interfaceCache = null;
		DummyClass.cache = null;
		Animation.anims = null;
		SpotAnim.cache = null;
		SpotAnim.modelCache = null;
		Varp.cache = null;
		super.fullGameScreen = null;
		Player.modelCache = null;
		Rasterizer.clearCache();
		WorldController.nullLoader();
		Model.nullLoader();
		FrameReader.nullLoader();
		System.gc();
	}

	void printDebug() {
		System.out.println("============");
		System.out.println("flame-cycle:" + flameCycle);
		if (onDemandFetcher != null)
			System.out.println("Od-cycle:" + onDemandFetcher.onDemandCycle);
		System.out.println("loop-cycle:" + loopCycle);
		System.out.println("draw-cycle:" + drawCycle);
		System.out.println("ptype:" + opCode);
		System.out.println("psize:" + pktSize);
		if (socketStream != null)
			socketStream.printDebug();
		super.shouldDebug = true;
	}

	Component getGameComponent() {
		if (signlink.mainapp != null)
			return signlink.mainapp;
		if (super.mainFrame != null)
			return super.mainFrame;
		else
			return this;
	}

	private void manageTextInput() {
		do {
			int key = readChar(-796);
			if (key == -1)
				break;
			if (backDialogID == 310 || backDialogID == 306
					|| backDialogID == 315 || backDialogID == 321
					|| backDialogID == 4887 || backDialogID == 4900
					|| backDialogID == 6179 || backDialogID == 356
					|| backDialogID == 4882 || backDialogID == 356
					|| backDialogID == 359 || backDialogID == 363
					|| backDialogID == 368 || backDialogID == 374
					|| backDialogID == 4882 || backDialogID == 4887
					|| backDialogID == 4893 || backDialogID == 4900
					|| backDialogID == 968 || backDialogID == 973
					|| backDialogID == 979 || backDialogID == 986) {
				if (key == KeyEvent.VK_SPACE) {
					stream.createFrame(40);
					stream.writeWord(0);
				}
			}
			if (openInterfaceID != -1
					&& openInterfaceID == reportAbuseInterfaceID) {
				if (key == 8 && reportAbuseInput.length() > 0)
					reportAbuseInput = reportAbuseInput.substring(0,
							reportAbuseInput.length() - 1);
				if ((key >= 97 && key <= 122 || key >= 65 && key <= 90
						|| key >= 48 && key <= 57 || key == 32)
						&& reportAbuseInput.length() < 12)
					reportAbuseInput += (char) key;
			} else if (showInput) {
				if (key >= 32 && key <= 122 && promptInput.length() < 80) {
					promptInput += (char) key;
					inputTaken = true;
				}
				if (key == 8 && promptInput.length() > 0) {
					promptInput = promptInput.substring(0,
							promptInput.length() - 1);
					inputTaken = true;
				}
				if (key == 13 || key == 10) {
					showInput = false;
					inputTaken = true;
					if (interfaceButtonAction == 6651
							&& promptInput.length() > 0) {
						stream.createFrame(103);
						stream.writeByte1(promptInput.length() + 5);
						stream.writeString("note" + promptInput);
						promptInput = "";
						interfaceButtonAction = -1;
					}
					if (friendsListAction == 1) {
						long l = TextClass.longForName(promptInput);
						addFriend(l);
					}
					if (interfaceButtonAction == 6199
							&& promptInput.length() > 0) {
						String inp = "";
						inp = inputString;
						inputString = "/name " + promptInput;
						sendPacket(1003);
						inputString = inp;
					}
					if (interfaceButtonAction == 6200
							&& promptInput.length() > 0) {
						String inp = "";
						inp = inputString;
						inputString = "/kick " + promptInput;
						sendPacket(1003);
						inputString = inp;
					}
					if (friendsListAction == 2 && friendsCount > 0) {
						long l1 = TextClass.longForName(promptInput);
						delFriend(l1);
					}
					if (friendsListAction == 3 && promptInput.length() > 0) {
						stream.createFrame(126);
						stream.writeByte1(0);
						int k = stream.currentOffset;
						stream.writeQWord(aLong953);
						TextInput.appendToStream(promptInput, stream);
						stream.writeBytes(stream.currentOffset - k);
						promptInput = TextInput.processText(promptInput);
						pushMessage(promptInput, 6, TextClass.fixName(TextClass
								.nameForLong(aLong953)));
						if (privateChatMode == 2) {
							privateChatMode = 1;
							stream.createFrame(95);
							stream.writeByte1(publicChatMode);
							stream.writeByte1(privateChatMode);
							stream.writeByte1(tradeMode);
						}
					}
					if (friendsListAction == 4 && ignoreCount < 100) {
						long l2 = TextClass.longForName(promptInput);
						addIgnore(l2);
					}
					if (friendsListAction == 5 && ignoreCount > 0) {
						long l3 = TextClass.longForName(promptInput);
						delIgnore(l3);
					}
					if (friendsListAction == 6) {
						long l3 = TextClass.longForName(promptInput);
						chatJoin(l3);
					}
				}
			} else if (inputDialogState == 1) {
				if (key >= 48 && key <= 57 && amountOrNameInput.length() < 10) {
					amountOrNameInput += (char) key;
					inputTaken = true;
					long l = Long.valueOf(amountOrNameInput);
					if (l == 0) {
						amountOrNameInput = "";
						inputTaken = true;
					}
				}
				if ((amountOrNameInput.length() > 0
						&& !amountOrNameInput.toLowerCase().contains("k")
						&& !amountOrNameInput.toLowerCase().contains("m") && !amountOrNameInput
						.toLowerCase().contains("b"))
						&& (key == 107 || key == 109 || key == 98)) {
					int am = 0;
					boolean addChar = true;
					long l = Long.valueOf(amountOrNameInput);
					if (l > 2147483647) {
						amountOrNameInput = "2147483647";
						inputTaken = true;
						addChar = false;
					} else {
						am = Integer.parseInt(amountOrNameInput);
					}
					if (key == 107 && am > 2147000) {
						addChar = false;
					}
					if (key == 109 && am > 2147) {
						addChar = false;
					}
					if (key == 98 && am > 2) {
						addChar = false;
					}

					if (addChar && am > 0) {
						amountOrNameInput += (char) key;
						inputTaken = true;
					}
				}
				if (key == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (key == 13 || key == 10) {
					if (amountOrNameInput.length() > 0) {
						if (amountOrNameInput.toLowerCase().contains("k")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"k", "000");
						} else if (amountOrNameInput.toLowerCase()
								.contains("m")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"m", "000000");
						} else if (amountOrNameInput.toLowerCase()
								.contains("b")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"b", "000000000");
						}
						long l = Long.valueOf(amountOrNameInput);

						if (l > 2147483647) {
							amountOrNameInput = "2147483647";
						}
						int amount = 0;
						amount = Integer.parseInt(amountOrNameInput);
						stream.createFrame(208);
						stream.writeDWord(amount);
					}
					inputDialogState = 0;
					inputTaken = true;
				}
				if (key == 13 || key == 10) {
					if (interfaceButtonAction == 1557
							&& amountOrNameInput.length() > 0) {
						if (amountOrNameInput.toLowerCase().contains("k")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"k", "000");
						} else if (amountOrNameInput.toLowerCase()
								.contains("m")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"m", "000000");
						} else if (amountOrNameInput.toLowerCase()
								.contains("b")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"b", "000000000");
						}
						long l = Long.valueOf(amountOrNameInput);

						if (l > 2147483647) {
							amountOrNameInput = "2147483647";
						}
						TalkingFix = inputString;
						inputString = "::[L]" + amountOrNameInput;
						sendPacket(103);
						inputString = TalkingFix;
						inputTaken = true;
					}
					if (interfaceButtonAction == 1558
							&& amountOrNameInput.length() > 0) {
						if (amountOrNameInput.toLowerCase().contains("k")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"k", "000");
						} else if (amountOrNameInput.toLowerCase()
								.contains("m")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"m", "000000");
						} else if (amountOrNameInput.toLowerCase()
								.contains("b")) {
							amountOrNameInput = amountOrNameInput.replaceAll(
									"b", "000000000");
						}
						long l = Long.valueOf(amountOrNameInput);

						if (l > 2147483647) {
							amountOrNameInput = "2147483647";
						}
						TalkingFix = inputString;
						inputString = "::[E]" + amountOrNameInput;
						sendPacket(103);
						inputString = TalkingFix;
						inputTaken = true;
					}
					if (interfaceButtonAction == 1557
							&& amountOrNameInput.length() == 0
							|| interfaceButtonAction == 1558
							&& amountOrNameInput.length() == 0) {
						interfaceButtonAction = 0;
					}
				}
			} else if (inputDialogState == 3) {
				if (key == 10) {
					totalItemResults = 0;
					amountOrNameInput = "";
					inputDialogState = 0;
					inputTaken = true;
				}
				if (key == 13 || key == 10) {
					if (amountOrNameInput.length() == 0) {
						buttonclicked = false;
						interfaceButtonAction = 0;
					}
				}
				if (key >= 32 && key <= 122 && amountOrNameInput.length() < 40) {
					amountOrNameInput += (char) key;
					inputTaken = true;
				}
				if (key == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
				}
			} else if (inputDialogState == 2) {
				if (key >= 32 && key <= 122 && amountOrNameInput.length() < 12) {
					amountOrNameInput += (char) key;
					inputTaken = true;
				}
				if (key == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (key == 13 || key == 10) {
					if (amountOrNameInput.length() > 0) {
						stream.createFrame(60);
						stream.writeQWord(TextClass
								.longForName(amountOrNameInput));
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (inputDialogState == 5) {
				int max = 12;
				if (playerCommand == 1)
					max = 1;
				if (key >= 32 && key <= 122 && amountOrNameInput.length() < max) {
					amountOrNameInput += (char) key;
					inputTaken = true;
				}
				if (key == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0,
							amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (key == 13 || key == 10) {
					if (amountOrNameInput.length() > 0) {
						if (playerCommand != 0) {
							stream.createFrame(103);
							stream.writeByte1(amountOrNameInput
									.length()
									+ PLRCOMMANDS[playerCommand - 1][1]
											.length() + 1);
							stream.writeString(PLRCOMMANDS[playerCommand - 1][1]
									+ amountOrNameInput);
						}
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (backDialogID == -1) {
				if (key == 9)
					tabToReplyPm();
				if (key >= 32 && key <= 122 && inputString.length() < /*80*/250) {
					inputString = inputString.substring(0, inputStringPos) + ((char) key) + inputString.substring(inputStringPos);
					//inputString += (char) key;
					inputStringPos += 1;
					inputTaken = true;
				}
				if (key == 8 && inputString.length() > 0) {
					if(inputStringPos > 0 && inputStringPos < inputString.length())
						inputString = inputString.substring(0, inputStringPos - 1) + inputString.substring(inputStringPos);
					else
						inputString = inputString.substring(0, inputString.length() - 1);
					if(inputStringPos > 0)
						inputStringPos -= 1;
					inputTaken = true;
				}
				if ((key == 13 || key == 10) && inputString.length() > 0) {
					if(inputString.equals("::items")) {
						/*for(int id = 0; id < NewItemDef.totalItems; id++) {
							//System.out.println("Loading: " + id);
							NewItemDef.forID(id);
						}*/
						options.put("new_items", !getOption("new_items"));
						System.out.println("New items: " + getOption("new_items"));
						//ItemDef.modelCache.clear();
						//NewItemDef.modelCache.clear();
						Player.modelCache.clear();
					}
					if(inputString.equals("::idks")) {
						options.put("new_idk", !getOption("new_idk"));
						IDK.changeIDK(getOption("new_idk"));
						Player.modelCache.clear();
					}
					if (myRights >= 2 && myRights <= 4 || Configuration.server.equals("127.0.0.1")) {
						if (inputString.startsWith("::object ")) {
							try {
								int id = Integer.parseInt(inputString.substring(9));
								ObjectDef o = ObjectDef.forID(id);
								pushMessage("Name: " + o.name + " ,HasActions: " + o.hasActions + ", SizeX:" + o.sizeX + ", SizeY:" + o.sizeY, 0, "");
								if (o.objectModelIDs != null) {
									for (int model = 0; model < o.objectModelIDs.length; model++)
										pushMessage("Models: "
												+ o.objectModelIDs[model] + "("
												+ o.objectModelTypes[model]
												+ ")", 0, "");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (inputString.startsWith("::3234")) {
							int id = Integer.parseInt(inputString.substring(6));
							NPCDef npc = NPCDef.forID(id);
							if (npc.npcModels != null) {
								for (int modelid = 0; modelid < npc.npcModels.length; modelid++)
									pushMessage("Name: " + npc.name
											+ " ,Models: "
											+ npc.npcModels[modelid], 0, "");
							}
						}

						if (inputString.equals("::lag"))
							printDebug();
						if (inputString.equals("::fpson"))
							fpsOn = true;
						if (inputString.equals("::exp"))
							new ExperienceCalculator();
						if (inputString.equals("::fpsoff"))
							fpsOn = false;
						if (inputString.equals("::dataon"))
							clientData = true;
						if(inputString.equals("::objects")) {
							/*for(int id = 0; id < ObjectDef.totalObjects667; id++) {
								System.out.println("Loading: " + id);
								ObjectDef.forID(id);
							}*/
						}
						if(inputString.equalsIgnoreCase("::ge"))
							inputDialogState = 3;
						if(inputString.equalsIgnoreCase("::loaditems")) {
							for(int id = 0; id < NewItemDef.totalItems; id++) {
								System.out.println("Loading Item ID: " + id);
								ItemDef.forID(id);
							}
						}
						if(inputString.equals("::icons")) {
							options.put("combat_icons", !getOption("combat_icons"));
							pushMessage("Combat Icons: " + getOption("combat_icons"), 0 ,"");
						}
						if(inputString.equals("::hits")) {
							if(getSetting("hitsplats") == 632)
								settings.put("hitsplats", 317);
							else if(getSetting("hitsplats") == 562)
								settings.put("hitsplats", 632);
							else if(getSetting("hitsplats") == 317)
								settings.put("hitsplats", 562);
							else
								settings.put("hitsplats", 317);
						}
						if(inputString.equals("::hitbar")) {
							options.put("new_hitbar", !getOption("new_hitbar"));
							pushMessage("New Hit Bar: " + getOption("new_hitbar"), 0 ,"");
						}
						if(inputString.equals("::tweening")) {
							options.put("tweening", !getOption("tweening"));
							pushMessage("Tweening: " + getOption("tweening"), 0 ,"");
						}
						if(inputString.equals("::damage")) {
							options.put("damage_10", !getOption("damage_10"));
							pushMessage("Damage x10: " + getOption("damage_10"), 0, "");
						}
						if(inputString.equals("::absorb") || inputString.equals("::soak")) {
							options.put("absorb", !getOption("absorb"));
							pushMessage("Absorb/Soak: " + getOption("absorb"), 0, "");
						}
						if(inputString.equals("::shading")) {
							options.put("smooth_shade", !getOption("smooth_shade"));
							pushMessage("Smooth Shading: " + getOption("smooth_shade"), 0, "");
						}
						if(inputString.startsWith("::texture")) {
							int id = Integer.parseInt(inputString.substring(10));
							Texture texture = Texture.get(id);
							if(texture == null)
								pushMessage("Texture " + id + " is null", 0 , "");
							else
								pushMessage("Texture: " + id + " Width: " + texture.width + " Height: " + texture.height, 0, "");
							if(TextureLoader667.getTexturePixels(id) != null && texture != null) {
								System.out.println("TextureImage is set!");
								textureImage = new Sprite(TextureLoader667.getTexturePixels(id), texture.width, texture.height);
							}
						}
						if(inputString.startsWith("::floorid")) {
							int id = Integer.parseInt(inputString.substring(10));
							if(id >= 51 && id <= 1418)
								underlay_id = id;
						}
						if (inputString.equals("::resettexture"))
							textureImage = null;
						if (inputString.equals("::lowmem"))
							setLowMem();
						if (inputString.equals("::highmem"))
							setHighMem();
						if (inputString.equals("::dataoff"))
							clientData = false;
						if (inputString.equals("::noclip")) {
							for (int k1 = 0; k1 < 4; k1++) {
								for (int i2 = 1; i2 < 103; i2++) {
									for (int k2 = 1; k2 < 103; k2++)
										clippingPlanes[k1].clipData[i2][k2] = 0;

								}
							}
						}
					}
					if (inputString.startsWith("/"))
						inputString = "::" + inputString;
					if (inputString.startsWith("::")
							&& !inputString.startsWith("::[")) {
						stream.createFrame(103);
						stream.writeByte1(inputString.length() - 1);
						stream.writeString(inputString.substring(2));
					} else {
						String s = inputString.toLowerCase();
						int color = 0;
						if (s.startsWith("yellow:")) {
							color = 0;
							inputString = inputString.substring(7);
						} else if (s.startsWith("red:")) {
							color = 1;
							inputString = inputString.substring(4);
						} else if (s.startsWith("green:")) {
							color = 2;
							inputString = inputString.substring(6);
						} else if (s.startsWith("cyan:")) {
							color = 3;
							inputString = inputString.substring(5);
						} else if (s.startsWith("purple:")) {
							color = 4;
							inputString = inputString.substring(7);
						} else if (s.startsWith("white:")) {
							color = 5;
							inputString = inputString.substring(6);
						} else if (s.startsWith("flash1:")) {
							color = 6;
							inputString = inputString.substring(7);
						} else if (s.startsWith("flash2:")) {
							color = 7;
							inputString = inputString.substring(7);
						} else if (s.startsWith("flash3:")) {
							color = 8;
							inputString = inputString.substring(7);
						} else if (s.startsWith("glow1:")) {
							color = 9;
							inputString = inputString.substring(6);
						} else if (s.startsWith("glow2:")) {
							color = 10;
							inputString = inputString.substring(6);
						} else if (s.startsWith("glow3:")) {
							color = 11;
							inputString = inputString.substring(6);
						}
						s = inputString.toLowerCase();
						int effect = 0;
						if (s.startsWith("wave:")) {
							effect = 1;
							inputString = inputString.substring(5);
						} else if (s.startsWith("wave2:")) {
							effect = 2;
							inputString = inputString.substring(6);
						} else if (s.startsWith("shake:")) {
							effect = 3;
							inputString = inputString.substring(6);
						} else if (s.startsWith("scroll:")) {
							effect = 4;
							inputString = inputString.substring(7);
						} else if (s.startsWith("slide:")) {
							effect = 5;
							inputString = inputString.substring(6);
						}
						if (s.startsWith("opacity")) {
							int nullilify = Integer.parseInt(inputString
									.substring(8));
							RSInterface.interfaceCache[nullilify].type = -1;
							RSInterface rsi = RSInterface.interfaceCache[962];
							int id = 0;
							if (rsi.children != null) {
								for (int ii : rsi.children) {
									RSInterface rsi_ = RSInterface.interfaceCache[ii];
									if (rsi_.type == 5)
										System.out.println(rsi_.id + " "
												+ rsi.childX[id] + " "
												+ rsi.childY[id]);
									id++;
								}
							}
						}
						stream.createFrame(4);
						stream.writeByte1(0);
						int j3 = stream.currentOffset;
						stream.writeByteS(effect);
						stream.writeByteS(color);
						textStream.currentOffset = 0;
						TextInput.appendToStream(inputString, textStream);
						stream.method441(0, textStream.buffer,
								textStream.currentOffset);
						stream.writeBytes(stream.currentOffset - j3);
						inputString = TextInput.processText(inputString);
						myPlayer.textSpoken = inputString;
						myPlayer.chatColor = color;
						myPlayer.chatEffect = effect;
						myPlayer.textCycle = 150;
						System.out.println("Your crown: " + myCrown);
						pushMessage(myPlayer.textSpoken, 2, getPrefix(myCrown) + myPlayer.name);
						if (publicChatMode == 2) {
							publicChatMode = 3;
							stream.createFrame(95);
							stream.writeByte1(publicChatMode);
							stream.writeByte1(privateChatMode);
							stream.writeByte1(tradeMode);
						}
					}
					inputString = "";
					inputStringPos = 0;
					inputTaken = true;
				}
			}
		} while (true);
	}

	private void buildPublicChat(int j) {
		int l = 0;
		for (int index = 0; index < 500; index++) {
			if (chatMessages[index] == null)
				continue;
			if (chatTypeView != 1)
				continue;
			int type = chatTypes[index];
			String name = chatNames[index];
			// String message = chatMessages[index];
			int positionY = (70 - l * 14 + 42) + chatScrollAmount + 4 + 5;
			if (positionY < -23)
				break;
			if (name != null && name.indexOf("<col=0xffffff>") == 0) {
				name = name.substring(14);
			}
			if ((type == 1 || type == 2)
					&& (type == 1 || publicChatMode == 0 || publicChatMode == 1
							&& isFriendOrSelf(name))) {
				if (j > positionY - 14 && j <= positionY
						&& !name.equals(myPlayer.name)) {
					if (myRights >= 1) {
						menuActionName[menuActionRow] = "Report abuse <col=0xffffff>"
								+ name;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					if (!isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Add ignore <col=0xffffff>"
								+ name;
						menuActionID[menuActionRow] = 42;
						menuActionRow++;
						menuActionName[menuActionRow] = "Add friend <col=0xffffff>"
								+ name;
						menuActionID[menuActionRow] = 337;
						menuActionRow++;
					}
					if (isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Message <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
					}
				}
				l++;
			}
		}
	}

	private void buildFriendChat(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 2)
				continue;
			int type = chatTypes[i1];
			String name = chatNames[i1];
			// String message = chatMessages[i1];
			int k1 = (70 - l * 14 + 42) + chatScrollAmount + 4 + 5;
			if (k1 < -23)
				break;
			if (name != null && name.indexOf("<col=0xffffff>") == 0) {
				name = name.substring(14);
			}
			if ((type == 5 || type == 6)
					&& (splitPrivateChat == 0 || chatTypeView == 2)
					&& (type == 6 || privateChatMode == 0 || privateChatMode == 1
							&& isFriendOrSelf(name)))
				l++;
			if ((type == 3 || type == 7)
					&& (splitPrivateChat == 0 || chatTypeView == 2)
					&& (type == 7 || privateChatMode == 0 || privateChatMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					if (myRights >= 1) {
						menuActionName[menuActionRow] = "Report abuse <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					if (!isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Add ignore <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 42;
						menuActionRow++;
						menuActionName[menuActionRow] = "Add friend <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 337;
						menuActionRow++;
					}
					if (isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Message <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
					}
				}
				l++;
			}
		}
	}

	private void buildDuelorTrade(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 3 && chatTypeView != 4)
				continue;
			int j1 = chatTypes[i1];
			String name = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + chatScrollAmount + 4 + 5;
			if (k1 < -23)
				break;
			if (name != null && name.indexOf("<col=0xffffff>") == 0) {
				name = name.substring(14);
			}
			if (chatTypeView == 3
					&& j1 == 4
					&& (tradeMode == 0 || tradeMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade <col=0xffffff>" + name;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
				l++;
			}
			if (chatTypeView == 4
					&& j1 == 8
					&& (tradeMode == 0 || tradeMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge <col=0xffffff>" + name;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
				l++;
			}
			if (j1 == 12) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Go-to @blu@" + name;
					menuActionID[menuActionRow] = 915;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	private void buildChatAreaMenu(int j) {
		int l = 0;
		for (int chatIndex = 0; chatIndex < 500; chatIndex++) {
			if (chatMessages[chatIndex] == null)
				continue;
			int type = chatTypes[chatIndex];
			int k1 = (70 - l * 14 + 42) + chatScrollAmount + 4 + 5;
			if (k1 < -23)
				break;
			String name = chatNames[chatIndex];
			if (chatTypeView == 1) {
				buildPublicChat(j);
				break;
			}
			if (chatTypeView == 2) {
				buildFriendChat(j);
				break;
			}
			if (chatTypeView == 3 || chatTypeView == 4) {
				buildDuelorTrade(j);
				break;
			}
			if (inputDialogState == 3) {
				buildItemSearch(j);
				break;
			}
			if (chatTypeView == 5) {
				break;
			}
			if (name != null && name.indexOf("<col=0xffffff>") == 0) {
				name = name.substring(14);
			}
			if (type == 0)
				l++;
			if ((type == 1 || type == 2)
					&& (type == 1 || publicChatMode == 0 || publicChatMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1 && !name.equals(myPlayer.name)) {
					if (myRights >= 1) {
						menuActionName[menuActionRow] = "Report abuse <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					if (!isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Add ignore <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 42;
						menuActionRow++;
						menuActionName[menuActionRow] = "Add friend <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 337;
						menuActionRow++;
					}
					if (isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Message <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
					}
				}
				l++;
			}
			if ((type == 3 || type == 7)
					&& splitPrivateChat == 0
					&& (type == 7 || privateChatMode == 0 || privateChatMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					if (myRights >= 1) {
						menuActionName[menuActionRow] = "Report abuse <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					if (!isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Add ignore <col=0xffffff>"
								+ name;
						menuActionID[menuActionRow] = 42;
						menuActionRow++;
						menuActionName[menuActionRow] = "Add friend <col=0xffffff>"
								+ name;
						menuActionID[menuActionRow] = 337;
						menuActionRow++;
					}
					if (isFriendOrSelf(name)) {
						menuActionName[menuActionRow] = "Message <col=0xffffff>" + name;
						menuActionID[menuActionRow] = 639;
						menuActionRow++;
					}
				}
				l++;
			}
			if (type == 4
					&& (tradeMode == 0 || tradeMode == 1
							&& isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade <col=0xffffff>" + name;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
				l++;
			}
			if ((type == 5 || type == 6) && splitPrivateChat == 0
					&& privateChatMode < 2)
				l++;
			if (type == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(name))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge <col=0xffffff>" + name;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
				l++;
			}
			if (type == 17) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept Invite <col=0xffffff>" + name;
					menuActionID[menuActionRow] = 1201;
					menuActionRow++;
				}
				l++;
			}
			if (type == 18) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept Challenge <col=0xffffff>" + name;
					menuActionID[menuActionRow] = 1202;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	public static int totalItemResults;
	public String itemResultNames[] = new String[100];
	public int itemResultIDs[] = new int[100];
	public static int itemResultScrollPos;

	public void itemSearch(String n) {
		if (n == null || n.length() == 0) {
			totalItemResults = 0;
			return;
		}
		String searchName = n;
		String searchParts[] = new String[100];
		int totalResults = 0;
		do {
			int k = searchName.indexOf(" ");
			if (k == -1)
				break;
			String part = searchName.substring(0, k).trim();
			if (part.length() > 0)
				searchParts[totalResults++] = part.toLowerCase();
			searchName = searchName.substring(k + 1);
		} while (true);
		searchName = searchName.trim();
		if (searchName.length() > 0)
			searchParts[totalResults++] = searchName.toLowerCase();
		totalItemResults = 0;
		label0: for (int id = 0; id < ItemDef.totalItems; id++) {
			ItemDef item = ItemDef.forID(id);
			if (item.certTemplateID != -1 || item.lentItemID != -1
					|| item.name == null || item.name == "Picture"
					|| item.certID == 18786 || item.name == "Null")
				continue;
			String result = item.name.toLowerCase();
			for (int idx = 0; idx < totalResults; idx++)
				if (result.indexOf(searchParts[idx]) == -1)
					continue label0;

			// int value = getItemShopValue(id);

			if (item.value != 0) {
				itemResultNames[totalItemResults] = result;
				itemResultIDs[totalItemResults] = id;
				totalItemResults++;
			}
			if (totalItemResults >= itemResultNames.length)
				return;
		}
	}

	private void buildItemSearch(int mouseY) {
		int y = 0;
		for (int idx = 0; idx < 100; idx++) {
			if (amountOrNameInput.length() == 0)
				return;
			else if (totalItemResults == 0)
				return;
			if (amountOrNameInput == "")
				return;
			String name = capitalizeFirstChar(itemResultNames[idx]);
			for (int i = 0; i <= 20; i++)
				if (name.contains(" <img=" + i + ">"))
					name = name.replaceAll(" <img=" + i + ">", "");
			int textY = (21 + y * 14) - itemResultScrollPos;
			if (mouseY > textY - 14 && mouseY <= textY && super.mouseX > 74
					&& super.mouseX < 495) {
				menuActionName[menuActionRow] = "" + name;
				menuActionID[menuActionRow] = 1251;
				menuActionRow++;
			}
			y++;
		}
	}

	private void drawFriendsListOrWelcomeScreen(RSInterface rsi) {
		int j = rsi.contentType;
		if (j >= 205 && j <= 205 + 25) {
			j -= 205;
			rsi.message = setMessage(j);
			return;
		}
		if (j >= 1 && j <= 100 || j >= 701 && j <= 800) {
			if (j == 1 && inputTextType == 0) {
				rsi.message = "Loading friend list";
				rsi.atActionType = 0;
				return;
			}
			if (j == 1 && inputTextType == 1) {
				rsi.message = "Connecting to friendserver";
				rsi.atActionType = 0;
				return;
			}
			if (j == 2 && inputTextType != 2) {
				rsi.message = "Please wait...";
				rsi.atActionType = 0;
				return;
			}
			int k = friendsCount;
			if (inputTextType != 2)
				k = 0;
			if (j > 700)
				j -= 601;
			else
				j--;
			if (j >= k) {
				rsi.message = "";
				rsi.atActionType = 0;
				return;
			} else {
				rsi.message = friendsList[j];
				rsi.atActionType = 1;
				return;
			}
		}
		if (j == 901) {
			rsi.message = friendsCount + " / 200";
			return;
		}
		if (j == 902) {
			rsi.message = ignoreCount + " / 100";
			return;
		}
		if (j >= 101 && j <= 200 || j >= 801 && j <= 900) {
			int l = friendsCount;
			if (inputTextType != 2)
				l = 0;
			if (j > 800)
				j -= 701;
			else
				j -= 101;
			if (j >= l) {
				rsi.message = "";
				rsi.atActionType = 0;
				return;
			}
			if (friendsNodeIDs[j] == 0)
				rsi.message = "@red@Offline";
			else if (friendsNodeIDs[j] == nodeID)
				rsi.message = "@gre@Online"/* + (friendsNodeIDs[j] - 9) */;
			else
				rsi.message = "@red@Offline"/* + (friendsNodeIDs[j] - 9) */;
			rsi.atActionType = 1;
			return;
		}
		if (j == 203) {
			int i1 = friendsCount;
			if (inputTextType != 2)
				i1 = 0;
			rsi.scrollMax = i1 * 15 + 20;
			if (rsi.scrollMax <= rsi.height)
				rsi.scrollMax = rsi.height + 1;
			return;
		}
		if (j >= 401 && j <= 500) {
			if ((j -= 401) == 0 && inputTextType == 0) {
				rsi.message = "Loading ignore list";
				rsi.atActionType = 0;
				return;
			}
			if (j == 1 && inputTextType == 0) {
				rsi.message = "Please wait...";
				rsi.atActionType = 0;
				return;
			}
			int j1 = ignoreCount;
			if (inputTextType == 0)
				j1 = 0;
			if (j >= j1) {
				rsi.message = "";
				rsi.atActionType = 0;
				return;
			} else {
				rsi.message = TextClass.fixName(TextClass
						.nameForLong(ignoreListAsLongs[j]));
				rsi.atActionType = 1;
				return;
			}
		}
		if (j == 503) {
			rsi.scrollMax = ignoreCount * 15 + 20;
			if (rsi.scrollMax <= rsi.height)
				rsi.scrollMax = rsi.height + 1;
			return;
		}
		if (j == 327) {
			rsi.modelRotation1 = 150;
			rsi.modelRotation2 = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
			if (updateCharacterCreation) {
				for (int k1 = 0; k1 < 7; k1++) {
					int l1 = myAppearance[k1];
					if (l1 >= 0 && !IDK.cache[l1].bodyModelIsFetched())
						return;
				}

				updateCharacterCreation = false;
				Model aclass30_sub2_sub4_sub6s[] = new Model[7];
				int i2 = 0;
				for (int j2 = 0; j2 < 7; j2++) {
					int k2 = myAppearance[j2];
					if (k2 >= 0)
						aclass30_sub2_sub4_sub6s[i2++] = IDK.cache[k2]
								.fetchBodyModel();
				}

				Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
				for (int l2 = 0; l2 < 5; l2++)
					if (myAppearanceColors[l2] != 0) {
						model.recolour(anIntArrayArray1003[l2][0],
								anIntArrayArray1003[l2][myAppearanceColors[l2]]);
						if (l2 == 1)
							model.recolour(anIntArray1204[0],
									anIntArray1204[myAppearanceColors[l2]]);
					}
				model.createBones();
				model.applyTransform(Animation.anims[myPlayer.standAnimIndex].frameIDs[0]);
				model.light(84, 1000, -90, -580, -90, true);
				rsi.mediaType = 5;
				rsi.mediaID = 0;
				RSInterface.clearModelCache(aBoolean994, model);
			}
			return;
		}
		if (j == 328) {
			RSInterface rsInterface = rsi;
			int verticleTilt = 150;
			int animationSpeed = (int) (Math.sin((double) loopCycle / 40D) * 256D) & 0x7ff;
			rsInterface.modelRotation1 = verticleTilt;
			rsInterface.modelRotation2 = animationSpeed;
			if (updateCharacterCreation) {
				Model characterDisplay = myPlayer.getRotatedModel();
				for (int l2 = 0; l2 < 5; l2++)
					if (myAppearanceColors[l2] != 0) {
						characterDisplay
								.recolour(
										anIntArrayArray1003[l2][0],
										anIntArrayArray1003[l2][myAppearanceColors[l2]]);
						if (l2 == 1)
							characterDisplay.recolour(anIntArray1204[0],
									anIntArray1204[myAppearanceColors[l2]]);
					}
				int staticFrame = myPlayer.standAnimIndex;
				characterDisplay.createBones();
				characterDisplay.applyTransform(Animation.anims[staticFrame].frameIDs[0]);
				//characterDisplay.interpolate();
				rsInterface.mediaType = 5;
				rsInterface.mediaID = 0;
				RSInterface.clearModelCache(aBoolean994, characterDisplay);
			}
			return;
		}
		if (j == 324) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = rsi.disabledSprite;
				aClass30_Sub2_Sub1_Sub1_932 = rsi.enabledSprite;
			}
			if (isMale) {
				rsi.disabledSprite = aClass30_Sub2_Sub1_Sub1_932;
				return;
			} else {
				rsi.disabledSprite = aClass30_Sub2_Sub1_Sub1_931;
				return;
			}
		}
		if (j == 325) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = rsi.disabledSprite;
				aClass30_Sub2_Sub1_Sub1_932 = rsi.enabledSprite;
			}
			if (isMale) {
				rsi.disabledSprite = aClass30_Sub2_Sub1_Sub1_931;
				return;
			} else {
				rsi.disabledSprite = aClass30_Sub2_Sub1_Sub1_932;
				return;
			}
		}
		if (j == 600) {
			rsi.message = reportAbuseInput;
			if (loopCycle % 20 < 10) {
				rsi.message += "|";
				return;
			} else {
				rsi.message += " ";
				return;
			}
		}
		if (j == 613)
			if (myRights >= 1) {
				if (canMute) {
					rsi.disabledColor = 0xff0000;
					rsi.message = "Moderator option: Mute player for 48 hours: <ON>";
				} else {
					rsi.disabledColor = 0xffffff;
					rsi.message = "Moderator option: Mute player for 48 hours: <OFF>";
				}
			} else {
				rsi.message = "";
			}
		if (j == 650 || j == 655)
			if (anInt1193 != 0) {
				String s;
				if (daysSinceLastLogin == 0)
					s = "earlier today";
				else if (daysSinceLastLogin == 1)
					s = "yesterday";
				else
					s = daysSinceLastLogin + " days ago";
				rsi.message = "You last logged in " + s + " from: "
						+ signlink.dns;
			} else {
				rsi.message = "";
			}
		if (j == 651) {
			if (unreadMessages == 0) {
				rsi.message = "0 unread messages";
				rsi.disabledColor = 0xffff00;
			}
			if (unreadMessages == 1) {
				rsi.message = "1 unread message";
				rsi.disabledColor = 65280;
			}
			if (unreadMessages > 1) {
				rsi.message = unreadMessages + " unread messages";
				rsi.disabledColor = 65280;
			}
		}
		if (j == 652)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					rsi.message = "@yel@This is a non-members world: @whi@Since you are a member we";
				else
					rsi.message = "";
			} else if (daysSinceRecovChange == 200) {
				rsi.message = "You have not yet set any password recovery questions.";
			} else {
				String s1;
				if (daysSinceRecovChange == 0)
					s1 = "Earlier today";
				else if (daysSinceRecovChange == 1)
					s1 = "Yesterday";
				else
					s1 = daysSinceRecovChange + " days ago";
				rsi.message = s1 + " you changed your recovery questions";
			}
		if (j == 653)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					rsi.message = "@whi@recommend you use a members world instead. You may use";
				else
					rsi.message = "";
			} else if (daysSinceRecovChange == 200)
				rsi.message = "We strongly recommend you do so now to secure your account.";
			else
				rsi.message = "If you do not remember making this change then cancel it immediately";
		if (j == 654) {
			if (daysSinceRecovChange == 201)
				if (membersInt == 1) {
					rsi.message = "@whi@this world but member benefits are unavailable whilst here.";
					return;
				} else {
					rsi.message = "";
					return;
				}
			if (daysSinceRecovChange == 200) {
				rsi.message = "Do this from the 'account management' area on our front webpage";
				return;
			}
			rsi.message = "Do this from the 'account management' area on our front webpage";
		}
	}

	private void drawSplitPrivateChat() {
		if (splitPrivateChat == 0)
			return;
		TypeFace textDrawingArea = normalFont;
		int i = 0;
		if (updateMinutes != 0)
			i = 1;
		for (int j = 0; j < 100; j++)
			if (chatMessages[j] != null) {
				int type = chatTypes[j];
				String name = chatNames[j];
				String prefixName = name;
				int rights = 0;
				if (name != null && name.indexOf("<img=") == 0) {
					name = name.substring(7);
				}
				if ((type == 3 || type == 7)
						&& (type == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(name))) {
					int l = (clientHeight - 174) - i * 13;
					int k1 = 4;
					textDrawingArea.drawString(0, "From", l, k1);
					textDrawingArea.drawString(65535, "From", l - 1, k1);
					k1 += textDrawingArea.getStringEffectsWidth("From ");
					if (prefixName != null && prefixName.indexOf("<img=") == 0)
						rights = getPrefixRightsNew(prefixName.substring(0, prefixName.indexOf(name) + 2));
					if (rights != 0) {
						modIcons[rights].drawSprite(k1, l - 12);
						k1 += 12;
					}
					textDrawingArea.drawString(0, name + ": " + chatMessages[j],
							l, k1);
					textDrawingArea.drawString(65535, name + ": "
							+ chatMessages[j], l - 1, k1);
					if (++i >= 5)
						return;
				}
				if (type == 5 && privateChatMode < 2) {
					int i1 = (clientHeight - 174) - i * 13;
					textDrawingArea.drawString(0, chatMessages[j], i1, 4);
					textDrawingArea
							.drawString(65535, chatMessages[j], i1 - 1, 4);
					if (++i >= 5)
						return;
				}
				if (type == 6 && privateChatMode < 2) {
					int j1 = (clientHeight - 174) - i * 13;
					int k1 = 4;
					textDrawingArea.drawString(0, "To " + name + ": "
							+ chatMessages[j], j1, k1);
					textDrawingArea.drawString(65535, "To " + name + ": "
							+ chatMessages[j], j1 - 1, k1);
					if (++i >= 5)
						return;
				}
			}
	}

	public void pushMessage(String message, int chatType, String name) {
		for (int Fmessage = 0; Fmessage < filteredMessages.length; Fmessage++) {
			if (message.startsWith(filteredMessages[Fmessage]) && filterMessages && name != myPlayer.name)
				return;
		}
		if (chatType == 0 && dialogID != -1) {
			notifyMessage = message;
			super.clickMode3 = 0;
		}
		if (backDialogID == -1)
			inputTaken = true;
		try {
			String cutoff = getCutoffString(message, 420);
			if(!cutoff.equalsIgnoreCase(message) && (chatType == 2 || chatType == 0)) {
				String original = cutoff.trim();
				//System.out.println("Cut: " + original);
				//System.out.println("Ori: " + message);
				String extra = message.substring(original.length());
				//System.out.println("Extra: " + extra);
				for (int next = 499; next > 0; next--) {
					if(next - 2 < 0)
						continue;
					chatTypes[next] = chatTypes[next - 2];
					chatNames[next] = chatNames[next - 2];
					chatMessages[next] = chatMessages[next - 2];
					chatRights[next] = chatRights[next - 2];
				}
				message = RSFontSystem.handleOldSyntax(message);
				name = RSFontSystem.handleOldSyntax(name);
				chatTypes[1] = chatType;
				chatNames[1] = name;
				chatMessages[1] = original;
				chatRights[1] = rights;
				chatTypes[0] = chatType;
				chatNames[0] = name;
				chatMessages[0] = extra.trim();
				chatRights[0] = rights;
			} else {
				for (int next = 499; next > 0; next--) {
					chatTypes[next] = chatTypes[next - 1];
					chatNames[next] = chatNames[next - 1];
					chatMessages[next] = chatMessages[next - 1];
					chatRights[next] = chatRights[next - 1];
				}
				message = RSFontSystem.handleOldSyntax(message);
				name = RSFontSystem.handleOldSyntax(name);
				chatTypes[0] = chatType;
				chatNames[0] = name;
				chatMessages[0] = message;
				chatRights[0] = rights;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private String getCutoffString(String text, int width) {
		if(newRegularFont.getTextWidth(text) < width)
			return text;
		String[] words = text.split(" ");
		int textWidth = 0;
		String toReturn = "";
		for(String word : words) {
			textWidth += newRegularFont.getTextWidth(word);
			textWidth += newRegularFont.getTextWidth(" ");
			if(textWidth < width)
				toReturn += " " + word;
			else
				return toReturn;
		}
		return toReturn;
	}
	
	public static void setTab(int id) {
		needDrawTabArea = true;
		tabID = id;
		tabAreaAltered = true;
	}

	private void resetImageProducers2() {
		if (chatAreaIP != null)
			return;
		nullLoader();
		super.fullGameScreen = null;
		GraphicsBuffer_1107 = null;
		titleScreen = null;
		chatAreaIP = new RSImageProducer(519, 165, getGameComponent());
		mapAreaIP = new RSImageProducer(249, 168, getGameComponent());
		DrawingArea.resetImage();
		//cacheSprite[6].drawSprite(0, 0);
		int gf = settings.get("gameframe");
		if(gf == 474) {
			SpriteCache.get(791).drawAdvancedSprite(0, 0);
		} else if(gf == 525) {
			SpriteCache.get(792).drawAdvancedSprite(0, 0);
		} else
			cacheSprite[6].drawSprite(0, 0);
		tabAreaIP = new RSImageProducer(250, 335, getGameComponent());
		gameScreenIP = new RSImageProducer(clientSize == 0 ? 512 : clientWidth, clientSize == 0 ? 334 : clientHeight, getGameComponent());
		DrawingArea.resetImage();
		new RSImageProducer(269, 37, getGameComponent());
		GraphicsBuffer_1125 = new RSImageProducer(249, 45, getGameComponent());
		welcomeScreenRaised = true;
	}

	public String getDocumentBaseHost() {
		if (signlink.mainapp != null) {
			return signlink.mainapp.getDocumentBase().getHost().toLowerCase();
		}
		if (super.mainFrame != null) {
			return ""; // runescape.com <- removed for Jframe to work
		} else {
			return ""; // super.getDocumentBase().getHost().toLowerCase() <-
			// removed for Jframe to work
		}
	}

	private void drawTargetArrow(Sprite sprite, int j, int k) {
		int l = k * k + j * j;
		if (l > 4225 && l < 0x15f90) {
			int i1 = viewRotation + minimapRotation & 0x7ff;
			int j1 = Model.SINE[i1];
			int k1 = Model.COSINE[i1];
			j1 = (j1 * 256) / (minimapZoom + 256);
			k1 = (k1 * 256) / (minimapZoom + 256);
			int l1 = j * j1 + k * k1 >> 16;
			int i2 = j * k1 - k * j1 >> 16;
			double d = Math.atan2(l1, i2);
			int j2 = (int) (Math.sin(d) * 63D);
			int k2 = (int) (Math.cos(d) * 57D);
			mapEdge.rotate(83 - k2 - 20, d, (94 + j2 + 4) - 10);
		} else {
			markMinimap(sprite, k, j);
		}
	}

	public static String capitalize(String s) {
		if (s == null)
			return "";
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
						s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1),
							Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s;
	}

	public boolean isChatInterface, displayChat;

	public boolean canClickScreen() {
		if (super.mouseX > 0
				&& super.mouseY > clientHeight - 165
				&& super.mouseX < 519
				&& super.mouseY < clientHeight
				&& displayChat
				|| super.mouseX > clientWidth - 246
				&& super.mouseY > clientHeight - 335
				&& super.mouseX < clientWidth
				&& super.mouseY < clientHeight
				|| super.mouseX > clientWidth - 220
				&& super.mouseY > 0
				&& super.mouseX < clientWidth
				&& super.mouseY < 164
				|| (super.mouseX > 247 && super.mouseX < 260
						&& super.mouseY > clientHeight - 173
						&& super.mouseY < clientHeight - 166 || super.mouseY > clientHeight - 15)
				|| super.mouseX > clientWidth - 462
				&& super.mouseY > clientHeight - 36
				&& super.mouseX < clientWidth && super.mouseY < clientHeight)
			return false;
		else
			return true;
	}

	public void processRightClick() {
		if (activeInterfaceType != 0) {
			return;
		}
		menuActionName[0] = "Cancel";
		menuActionID[0] = 1107;
		menuActionRow = 1;
		if (clientSize >= 1) {
			if (fullscreenInterfaceID != -1) {
				hoveredInterface = 0;
				anInt1315 = 0;
				buildInterfaceMenu((clientWidth / 2) - 765 / 2,
						RSInterface.interfaceCache[fullscreenInterfaceID],
						super.mouseX, (clientHeight / 2) - 503 / 2,
						super.mouseY, 0);
				if (hoveredInterface != focusedViewportWidget) {
					focusedViewportWidget = hoveredInterface;
				}
				if (anInt1315 != anInt1129) {
					anInt1129 = anInt1315;
				}
				return;
			}
		}
		if (showChat)
			buildSplitPrivateChatMenu();
		hoveredInterface = 0;
		anInt1315 = 0;
		if (clientSize == 0) {
			if (super.mouseX > 0 && super.mouseY > 0 && super.mouseX < 516
					&& super.mouseY < 338) {
				if (openInterfaceID != -1) {
					buildInterfaceMenu(4,
							RSInterface.interfaceCache[openInterfaceID],
							super.mouseX, 4, super.mouseY, 0);
				} else {
					build3dScreenMenu();
				}
			}
		} else if (clientSize >= 1) {
			if (canClick())
				if (super.mouseX > (clientWidth / 2) - 256
						&& super.mouseY > (clientHeight / 2) - 167
						&& super.mouseX < ((clientWidth / 2) + 256)
						&& super.mouseY < (clientHeight / 2) + 167
						&& openInterfaceID != -1) {
					buildInterfaceMenu((clientWidth / 2) - 256,
							RSInterface.interfaceCache[openInterfaceID],
							super.mouseX, (clientHeight / 2) - 167,
							super.mouseY, 0);
				} else {
					build3dScreenMenu();
				}
		}
		if (hoveredInterface != focusedViewportWidget) {
			focusedViewportWidget = hoveredInterface;
		}
		if (anInt1315 != anInt1129) {
			anInt1129 = anInt1315;
		}
		hoveredInterface = 0;
		anInt1315 = 0;
		if (clientSize == 0) {
			if (super.mouseX > 516 && super.mouseY > 205 && super.mouseX < 765
					&& super.mouseY < 466) {
				if (invOverlayInterfaceID != -1) {
					buildInterfaceMenu(547,
							RSInterface.interfaceCache[invOverlayInterfaceID],
							super.mouseX, 205, super.mouseY, 0);
				} else if (tabInterfaceIDs[tabID] != -1) {
					buildInterfaceMenu(547,
							RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
							super.mouseX, 205, super.mouseY, 0);
				}
			}
		} else {
			int y = clientWidth >= smallTabs ? 46 : 82;
			if (super.mouseX > clientWidth - 197
					&& super.mouseY > clientHeight - y - 245
					&& super.mouseX < clientWidth - 7
					&& super.mouseY < clientHeight - y + 10 && showTab) {
				if (invOverlayInterfaceID != -1) {
					buildInterfaceMenu(clientWidth - 197,
							RSInterface.interfaceCache[invOverlayInterfaceID],
							super.mouseX, clientHeight - y - 256, super.mouseY,
							0);
				} else if (tabInterfaceIDs[tabID] != -1) {
					buildInterfaceMenu(clientWidth - 197,
							RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
							super.mouseX, clientHeight - y - 256, super.mouseY,
							0);
				}
			}
		}
		if (hoveredInterface != focusedSidebarWidget) {
			needDrawTabArea = true;
			tabAreaAltered = true;
			focusedSidebarWidget = hoveredInterface;
		}
		if (anInt1315 != anInt1044) {
			needDrawTabArea = true;
			tabAreaAltered = true;
			anInt1044 = anInt1315;
		}
		hoveredInterface = 0;
		anInt1315 = 0;
		if (super.mouseX > 0
				&& super.mouseY > (clientSize == 0 ? 338 : clientHeight - 165)
				&& super.mouseX < 490
				&& super.mouseY < (clientSize == 0 ? 463 : clientHeight - 40)
				&& (showChat || backDialogID != -1)) {
			if (backDialogID != -1) {
				buildInterfaceMenu(20,
						RSInterface.interfaceCache[backDialogID], super.mouseX,
						(clientSize == 0 ? 358 : clientHeight - 145),
						super.mouseY, 0);
			} else if (super.mouseY < (clientSize == 0 ? 463
					: clientHeight - 40) && super.mouseX < 490) {
				buildChatAreaMenu(super.mouseY
						- (clientSize == 0 ? 338 : clientHeight - 165));
			}
		}
		if (backDialogID != -1 && hoveredInterface != focusChatWidget) {
			inputTaken = true;
			focusChatWidget = hoveredInterface;
		}
		if (backDialogID != -1 && anInt1315 != anInt1500) {
			inputTaken = true;
			anInt1500 = anInt1315;
		}
		/* Enable custom right click areas */
		if (super.mouseX > 0 && super.mouseY > clientHeight - 165
				&& super.mouseX < 519 && super.mouseY < clientHeight)
			rightClickChatButtons();
		if (super.mouseX > clientWidth - 249 && super.mouseY < 168)
			rightClickMapArea();
		processTabAreaHovers();
		/**/
		boolean flag = false;
		while (!flag) {
			flag = true;
			for (int j = 0; j < menuActionRow - 1; j++) {
				if (menuActionID[j] < 1000 && menuActionID[j + 1] > 1000) {
					String s = menuActionName[j];
					menuActionName[j] = menuActionName[j + 1];
					menuActionName[j + 1] = s;
					int k = menuActionID[j];
					menuActionID[j] = menuActionID[j + 1];
					menuActionID[j + 1] = k;
					k = menuActionCmd2[j];
					menuActionCmd2[j] = menuActionCmd2[j + 1];
					menuActionCmd2[j + 1] = k;
					k = menuActionCmd3[j];
					menuActionCmd3[j] = menuActionCmd3[j + 1];
					menuActionCmd3[j + 1] = k;
					k = menuActionCmd1[j];
					menuActionCmd1[j] = menuActionCmd1[j + 1];
					menuActionCmd1[j + 1] = k;
					k = menuActionCmd4[j];
					menuActionCmd4[j] = menuActionCmd4[j + 1];
					menuActionCmd4[j + 1] = k;
					flag = false;
				}
			}
		}
	}

	private int loginCode;

	public void login(String username, String password, boolean flag) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		username = TextClass.fixName(username);
		signlink.errorname = username;
		// setCursor(0);
		try {
			if (!flag) {
				try {
					drawLoginScreen(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			socketStream = new RSSocket(this, openSocket(43594 + portOff, Configuration.server));
			long l = TextClass.longForName(username);
			int i = (int) (l >> 16 & 31L);
			stream.currentOffset = 0;
			stream.writeByte1(14);
			stream.writeByte1(i);
			socketStream.queueBytes(2, stream.buffer);
			for (int j = 0; j < 8; j++)
				socketStream.read();
			loginCode = socketStream.read();
			int i1 = loginCode;
			if (loginCode == 0) {
				/*new IdentityPunishment().createIdentity();
				try {
					new IdentityPunishment().loadIdentity();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}*/
				socketStream.flushInputStream(inStream.buffer, 8);
				inStream.currentOffset = 0;
				serverSeed = inStream.readQWord();
				int ai[] = new int[4];
				ai[0] = (int) (Math.random() * 99999999D);
				ai[1] = (int) (Math.random() * 99999999D);
				ai[2] = (int) (serverSeed >> 32);
				ai[3] = (int) serverSeed;
				stream.currentOffset = 0;
				stream.writeByte1(100);
				stream.writeDWord(ai[0]);
				stream.writeDWord(ai[1]);
				stream.writeDWord(ai[2]);
				stream.writeDWord(ai[3]);
				stream.writeDWord(signlink.uid);
				stream.writeString(username);
				stream.writeString(password);
				//stream.writeByte(previousScreenState == 3 ? 1 : 0);
				/*if (previousScreenState == 3) {
					stream.writeString(register.getEmail());
					stream.writeString(register.getReferrer() == null ? ""
							: register.getReferrer());
				}
				stream.writeString(IdentityPunishment.getIndentity());*/
				stream.doKeys();
				aStream_847.currentOffset = 0;
				if (flag)
					aStream_847.writeByte1(18);
				else
					aStream_847.writeByte1(16);
				aStream_847.writeByte1(stream.currentOffset + 36 + 1 + 1 + 2);
				aStream_847.writeByte1(255);
				aStream_847.writeWord(16);//client version
				aStream_847.writeByte1(lowMem ? 1 : 0);
				for (int l1 = 0; l1 < 9; l1++)
					aStream_847.writeDWord(expectedCRCs[l1]);

				aStream_847.writeBytes(stream.buffer, stream.currentOffset, 0);
				stream.encryption = new ISAACRandomGen(ai);
				for (int j2 = 0; j2 < 4; j2++)
					ai[j2] += 50;

				encryption = new ISAACRandomGen(ai);
				socketStream.queueBytes(aStream_847.currentOffset, aStream_847.buffer);
				loginCode = socketStream.read();
			}
			if (loginCode == 1) {
				try {
					Thread.sleep(2000L);
				} catch (Exception _ex) {
				}
				login(username, password, flag);
				return;
			}
			if (loginCode == 2) {
				myRights = socketStream.read();
				System.out.println("Rights: " + myRights);
				myCrown = socketStream.read();
				if(myCrown == 128)
					myCrown = -1;
				System.out.println("Crown: " + myCrown);
				//glowColor = socketStream.read();
				flagged = socketStream.read() == 1;
				//System.out.println("Rights: " + myRights + " Glow: " + glowColor + " Flagged: " + flagged);
				lastClickTime = 0L;
				mouseRecorderIdleCycles = 0;
				mouseDetection.coordsIndex = 0;
				super.awtFocus = true;
				isFocused = true;
				loggedIn = true;
				stream.currentOffset = 0;
				inStream.currentOffset = 0;
				opCode = -1;
				anInt841 = -1;
				opcode_last = -1;
				opcode_second = -1;
				pktSize = 0;
				netIdleCycles = 0;
				updateMinutes = 0;
				logoutCycle = 0;
				markType = 0;
				menuActionRow = 0;
				menuOpen = false;
				super.idleTime = 0;
				for (int j1 = 0; j1 < 100; j1++)
					chatMessages[j1] = null;
				itemSelected = 0;
				spellSelected = 0;
				loadingStage = 0;
				currentSound = 0;
				cameraOffsetX = (int) (Math.random() * 100D) - 50;
				cameraOffsetY = (int) (Math.random() * 110D) - 55;
				viewRotationOffset = (int) (Math.random() * 80D) - 40;
				minimapRotation = (int) (Math.random() * 120D) - 60;
				minimapZoom = (int) (Math.random() * 30D) - 20;
				viewRotation = (int) (Math.random() * 20D) - 10 & 0x7ff;
				scriptManager = null;
				titleScreenOffsets = null;
				minimapStatus = 0;
				lastKnownPlane = -1;
				destX = 0;
				destY = 0;
				playerCount = 0;
				npcCount = 0;
				for (int i2 = 0; i2 < maxPlayers; i2++) {
					playerArray[i2] = null;
					aStreamArray895s[i2] = null;
				}

				for (int k2 = 0; k2 < 16384; k2++)
					npcArray[k2] = null;

				myPlayer = playerArray[myPlayerIndex] = new Player();
				projectileDeque.clear();
				stillGraphicDeque.clear();
				for (int l2 = 0; l2 < 4; l2++) {
					for (int i3 = 0; i3 < 104; i3++) {
						for (int k3 = 0; k3 < 104; k3++)
							groundArray[l2][i3][k3] = null;

					}

				}
				objectSpawnDeque = new Deque();
				fullscreenInterfaceID = -1;
				inputTextType = 0;
				friendsCount = 0;
				dialogID = -1;
				backDialogID = -1;
				openInterfaceID = -1;
				invOverlayInterfaceID = -1;
				walkableInterfaceId = -1;
				dialogueOptionsShowing = false;
				tabID = 3;
				inputDialogState = 0;
				menuOpen = false;
				showInput = false;
				notifyMessage = null;
				drawMultiwayIcon = 0;
				flashingSidebarTab = -1;
				isMale = true;
				setMyAppearance();
				for (int j3 = 0; j3 < 5; j3++)
					myAppearanceColors[j3] = 0;

				for (int l3 = 0; l3 < 6; l3++) {
					atPlayerActions[l3] = null;
					atPlayerArray[l3] = false;
				}

				anInt1175 = 0;
				anInt1134 = 0;
				anInt986 = 0;
				anInt1288 = 0;
				anInt924 = 0;
				anInt1188 = 0;
				anInt1155 = 0;
				anInt1226 = 0;
				resetImageProducers2();
				clientHeight += 1;
				clientHeight -= 1;
				int slot = 44001;
				for (int a = 44001; a <= 44200; a++) {
					sendFrame126("", slot);
					slot++;
				}
				slot = 44801;
				for (int d = 44801; d <= 45000; d++) {
					sendFrame126("", slot);
					slot++;
				}
				chatMessages = new String[500];
				updateGameArea();
				/*if (musicEnabled)
					stopMidi();*/
				/*Account a = Account.getAccountForName(myUsername);
				if (a != null && a.getClientSize() != 0) {
					toggleSize(a.getClientSize());
				} else if (clientSize == 0)
					toggleSize(0);*/
				if(onFullPage)
					toggleSize(1);
				else
					toggleSize(0);

				return;
			}
			if (loginCode == 3) {
				loginMessage1 = "";
				loginMessage2 = "Invalid username or password.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 4) {
				loginMessage1 = "Your account has been disabled.";
				loginMessage2 = "Please check your message-center for details.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 5) {
				loginMessage1 = "Your account is already logged in.";
				loginMessage2 = "Try again in 60 secs...";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 6) {
				loginMessage1 = "RevolutionX has been updated!";
				loginMessage2 = "Please reload this page.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 7) {
				loginMessage1 = "This world is full.";
				loginMessage2 = "Please use a different world.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 8) {
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Login server offline.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 9) {
				loginMessage1 = "Login limit exceeded.";
				loginMessage2 = "Too many connections from your address.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 10) {
				loginMessage1 = "Unable to connect.";
				loginMessage2 = "Bad session id.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 11) {
				loginMessage2 = "Login server rejected session.";
				loginMessage2 = "Please try again.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 12) {
				loginMessage1 = "You need a members account to login to this world.";
				loginMessage2 = "Please subscribe, or use a different world.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 13) {
				loginMessage1 = "Could not complete login.";
				loginMessage2 = "Please try using a different world.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 14) {
				loginMessage1 = "The server is being updated.";
				loginMessage2 = "Please wait 1 minute and try again.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 15) {
				loggedIn = true;
				stream.currentOffset = 0;
				inStream.currentOffset = 0;
				opCode = -1;
				anInt841 = -1;
				opcode_last = -1;
				opcode_second = -1;
				pktSize = 0;
				netIdleCycles = 0;
				updateMinutes = 0;
				menuActionRow = 0;
				menuOpen = false;
				mapLoadingTime = System.currentTimeMillis();
				return;
			}
			if (loginCode == 16) {
				loginMessage1 = "Login attempts exceeded.";
				loginMessage2 = "Please wait 1 minute and try again.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 17) {
				loginMessage1 = "You are standing in a members-only area.";
				loginMessage2 = "To play on this world move to a free area first";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 20) {
				loginMessage1 = "Invalid loginserver requested";
				loginMessage2 = "Please try using a different world.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 21) {
				for (int k1 = socketStream.read(); k1 >= 0; k1--) {
					loginMessage1 = "You have only just left another world";
					loginMessage2 = "Your profile will be transferred in: "
							+ k1 + " seconds";
					loginScreenState = 2;
					drawLoginScreen(true);
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
						_ex.printStackTrace();
					}
				}

				login(username, password, flag);
				return;
			}
			if (loginCode == 22) {
				//getRegister().verified[0] = false;
				//getRegister().usernameMessage = new String[] { "This username is not available." };
				loginMessage1 = "This username is not available.";
				loginMessage2 = "Please try using a different name.";
				return;
			}
			if (loginCode == 23) {
				loginMessage1 = "Invalid login credentials.";
				loginMessage2 = "Please, click here to register.";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 24) {
				loginMessage1 = "Account successfully registered.";
				loginMessage2 = "You may now log in.";
				previousScreenState = 0;
				loginScreenState = 2;
				return;
			}
			if (loginCode == 25) {
				loginMessage1 = "This username is already in use.";
				loginMessage2 = "Please try a different one";
				loginScreenState = 2;
				return;
			}
			if (loginCode == 26) {
				loginMessage1 = "This email-address is already in use.";
				loginMessage2 = "Please try a different one";
				loginScreenState = 2;
				return;
			}
			if (loginCode == -1) {
				if (i1 == -1) {// TODO remove this original value: 0
					if (loginFailures < 6) {
						try {
							Thread.sleep(2000L);
						} catch (Exception _ex) {
							_ex.printStackTrace();
						}
						loginFailures++;
						login(username, password, flag);
						return;
					} else {
						// loginMessage1 = "No response from loginserver";
						// loginMessage2 =
						// "Please wait 1 minute and try again.";
						loginMessage1 = "You have entered the wrong password 3 times.";
						loginMessage2 = "Please wait 1 minute and try again.";
						loginScreenState = 2;
						return;
					}
				} else {
					loginMessage1 = "No response from server";
					loginMessage2 = "Please try using a different world.";
					loginScreenState = 2;
					return;
				}
			} else {
				System.out.println("response:" + loginCode);
				loginMessage1 = "Unexpected server response";
				loginMessage2 = "Please try using a different world.";
				loginScreenState = 2;
				return;
			}
		} catch (IOException _ex) {
			loginMessage1 = "";
		}
		loginMessage2 = "Error connecting to server.";
		loginScreenState = 2;
	}

	private boolean doWalkTo(int i, int j, int k, int i1, int j1, int k1,
			int l1, int i2, int j2, boolean flag, int k2) {
		try {
			byte byte0 = 104;
			byte byte1 = 104;
			for (int l2 = 0; l2 < byte0; l2++) {
				for (int i3 = 0; i3 < byte1; i3++) {
					pathwayPoint[l2][i3] = 0;
					anIntArrayArray825[l2][i3] = 0x5f5e0ff;
				}
			}
			int j3 = j2;
			int k3 = j1;
			pathwayPoint[j2][j1] = 99;
			anIntArrayArray825[j2][j1] = 0;
			int l3 = 0;
			int i4 = 0;
			bigX[l3] = j2;
			bigY[l3++] = j1;
			boolean flag1 = false;
			int j4 = bigX.length;
			int ai[][] = clippingPlanes[plane].clipData;
			while (i4 != l3) {
				j3 = bigX[i4];
				k3 = bigY[i4];
				i4 = (i4 + 1) % j4;
				if (j3 == k2 && k3 == i2) {
					flag1 = true;
					break;
				}
				if (i1 != 0) {
					if ((i1 < 5 || i1 == 10)
							&& clippingPlanes[plane].checkWallClipping(k2, j3,
									k3, j, i1 - 1, i2)) {
						flag1 = true;
						break;
					}
					if (i1 < 10
							&& clippingPlanes[plane]
									.checkWallDecorationClipping(k2, i2, k3,
											i1 - 1, j, j3)) {
						flag1 = true;
						break;
					}
				}
				if (k1 != 0
						&& k != 0
						&& clippingPlanes[plane].canWalkToEntity(i2, k2, j3, k,
								l1, k1, k3)) {
					flag1 = true;
					break;
				}
				int l4 = anIntArrayArray825[j3][k3] + 1;
				if (j3 > 0 && pathwayPoint[j3 - 1][k3] == 0
						&& (ai[j3 - 1][k3] & 0x1280108) == 0) {
					bigX[l3] = j3 - 1;
					bigY[l3] = k3;
					l3 = (l3 + 1) % j4;
					pathwayPoint[j3 - 1][k3] = 2;
					anIntArrayArray825[j3 - 1][k3] = l4;
				}
				if (j3 < byte0 - 1 && pathwayPoint[j3 + 1][k3] == 0
						&& (ai[j3 + 1][k3] & 0x1280180) == 0) {
					bigX[l3] = j3 + 1;
					bigY[l3] = k3;
					l3 = (l3 + 1) % j4;
					pathwayPoint[j3 + 1][k3] = 8;
					anIntArrayArray825[j3 + 1][k3] = l4;
				}
				if (k3 > 0 && pathwayPoint[j3][k3 - 1] == 0
						&& (ai[j3][k3 - 1] & 0x1280102) == 0) {
					bigX[l3] = j3;
					bigY[l3] = k3 - 1;
					l3 = (l3 + 1) % j4;
					pathwayPoint[j3][k3 - 1] = 1;
					anIntArrayArray825[j3][k3 - 1] = l4;
				}
				if (k3 < byte1 - 1 && pathwayPoint[j3][k3 + 1] == 0
						&& (ai[j3][k3 + 1] & 0x1280120) == 0) {
					bigX[l3] = j3;
					bigY[l3] = k3 + 1;
					l3 = (l3 + 1) % j4;
					pathwayPoint[j3][k3 + 1] = 4;
					anIntArrayArray825[j3][k3 + 1] = l4;
				}
				if (j3 > 0 && k3 > 0 && pathwayPoint[j3 - 1][k3 - 1] == 0
						&& (ai[j3 - 1][k3 - 1] & 0x128010e) == 0
						&& (ai[j3 - 1][k3] & 0x1280108) == 0
						&& (ai[j3][k3 - 1] & 0x1280102) == 0) {
					bigX[l3] = j3 - 1;
					bigY[l3] = k3 - 1;
					l3 = (l3 + 1) % j4;
					pathwayPoint[j3 - 1][k3 - 1] = 3;
					anIntArrayArray825[j3 - 1][k3 - 1] = l4;
				}
				if (j3 < byte0 - 1 && k3 > 0
						&& pathwayPoint[j3 + 1][k3 - 1] == 0
						&& (ai[j3 + 1][k3 - 1] & 0x1280183) == 0
						&& (ai[j3 + 1][k3] & 0x1280180) == 0
						&& (ai[j3][k3 - 1] & 0x1280102) == 0) {
					bigX[l3] = j3 + 1;
					bigY[l3] = k3 - 1;
					l3 = (l3 + 1) % j4;
					pathwayPoint[j3 + 1][k3 - 1] = 9;
					anIntArrayArray825[j3 + 1][k3 - 1] = l4;
				}
				if (j3 > 0 && k3 < byte1 - 1
						&& pathwayPoint[j3 - 1][k3 + 1] == 0
						&& (ai[j3 - 1][k3 + 1] & 0x1280138) == 0
						&& (ai[j3 - 1][k3] & 0x1280108) == 0
						&& (ai[j3][k3 + 1] & 0x1280120) == 0) {
					bigX[l3] = j3 - 1;
					bigY[l3] = k3 + 1;
					l3 = (l3 + 1) % j4;
					pathwayPoint[j3 - 1][k3 + 1] = 6;
					anIntArrayArray825[j3 - 1][k3 + 1] = l4;
				}
				if (j3 < byte0 - 1 && k3 < byte1 - 1
						&& pathwayPoint[j3 + 1][k3 + 1] == 0
						&& (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0
						&& (ai[j3 + 1][k3] & 0x1280180) == 0
						&& (ai[j3][k3 + 1] & 0x1280120) == 0) {
					bigX[l3] = j3 + 1;
					bigY[l3] = k3 + 1;
					l3 = (l3 + 1) % j4;
					pathwayPoint[j3 + 1][k3 + 1] = 12;
					anIntArrayArray825[j3 + 1][k3 + 1] = l4;
				}
			}
			anInt1264 = 0;
			if (!flag1) {
				if (flag) {
					int i5 = 100;
					for (int k5 = 1; k5 < 2; k5++) {
						for (int i6 = k2 - k5; i6 <= k2 + k5; i6++) {
							for (int l6 = i2 - k5; l6 <= i2 + k5; l6++)
								if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104
										&& anIntArrayArray825[i6][l6] < i5) {
									i5 = anIntArrayArray825[i6][l6];
									j3 = i6;
									k3 = l6;
									anInt1264 = 1;
									flag1 = true;
								}

						}

						if (flag1)
							break;
					}

				}
				if (!flag1)
					return false;
			}
			i4 = 0;
			bigX[i4] = j3;
			bigY[i4++] = k3;
			int l5;
			for (int j5 = l5 = pathwayPoint[j3][k3]; j3 != j2 || k3 != j1; j5 = pathwayPoint[j3][k3]) {
				if (j5 != l5) {
					l5 = j5;
					bigX[i4] = j3;
					bigY[i4++] = k3;
				}
				if ((j5 & 2) != 0)
					j3++;
				else if ((j5 & 8) != 0)
					j3--;
				if ((j5 & 1) != 0)
					k3++;
				else if ((j5 & 4) != 0)
					k3--;
			}
			// if(cancelWalk) { return i4 > 0; }

			if (i4 > 0) {
				int k4 = i4;
				if (k4 > 25)
					k4 = 25;
				i4--;
				int k6 = bigX[i4];
				int i7 = bigY[i4];
				anInt1288 += k4;
				if (anInt1288 >= 92) {
					stream.createFrame(36);
					stream.writeDWord(0);
					anInt1288 = 0;
				}
				if (i == 0) {
					stream.createFrame(164);
					stream.writeByte1(k4 + k4 + 3);
				}
				if (i == 1) {
					stream.createFrame(248);
					stream.writeByte1(k4 + k4 + 3 + 14);
				}
				if (i == 2) {
					stream.createFrame(98);
					stream.writeByte1(k4 + k4 + 3);
				}
				stream.writeSignedBigEndian(k6 + baseX);
				destX = bigX[0];
				destY = bigY[0];
				for (int j7 = 1; j7 < k4; j7++) {
					i4--;
					stream.writeByte1(bigX[i4] - k6);
					stream.writeByte1(bigY[i4] - i7);
				}

				stream.writeUnsignedWordBigEndian(i7 + baseY);
				stream.writeByteC(super.keyArray[5] != 1 ? 0 : 1);
				return true;
			}
			return i != 1;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @param stream
	 */
	private void readNPCUpdateMask(Stream stream) {
		for (int j = 0; j < playersToUpdateCount; j++) {
			int k = playersToUpdate[j];
			NPC npc = npcArray[k];
			int l = stream.readUnsignedByte();
			if ((l & 0x10) != 0) {
				int requestAnim = stream.readLEShort();
				if (requestAnim == 65535)
					requestAnim = -1;
				int i2 = stream.readUnsignedByte();
				if (requestAnim == npc.anim && requestAnim != -1) {
					int l2 = Animation.anims[requestAnim].delayType;
					if (l2 == 1) {
						npc.currentAnimFrame = 0;
						npc.frameCycle = 0;
						npc.animationDelay = i2;
						npc.anInt1530 = 0;
					}
					if (l2 == 2)
						npc.anInt1530 = 0;
				} else if (requestAnim == -1
						|| npc.anim == -1
						|| Animation.anims[requestAnim].forcedPriority >= Animation.anims[npc.anim].forcedPriority) {
					npc.anim = requestAnim;
					npc.currentAnimFrame = 0;
					npc.frameCycle = 0;
					npc.animationDelay = i2;
					npc.anInt1530 = 0;
					npc.anInt1542 = npc.pathLength;
					try {
						if (FrameReader.animationlist[Integer.parseInt(Integer.toHexString(Animation.anims[requestAnim].frameIDs[0]).substring(0,
														Integer.toHexString(Animation.anims[requestAnim].frameIDs[0]).length() - 4), 16)].length == 0)
							onDemandFetcher.requestFileData(1, Integer.parseInt(Integer.toHexString(Animation.anims[requestAnim].frameIDs[0]).substring(0,
														Integer.toHexString(Animation.anims[requestAnim].frameIDs[0]).length() - 4), 16));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if ((l & 8) != 0) {
				int damage = inStream.readShortA();
				int hitmark = stream.readByteC();
				int icon = stream.readUnsignedByte();
				npc.updateHitData(hitmark, damage, loopCycle, icon, 0);
				npc.loopCycleStatus = loopCycle + 300;
				npc.currentHealth = inStream.readShortA();
				npc.maxHealth = inStream.readShortA();
			}
			if ((l & 0x80) != 0) {
				npc.gfxId = stream.readUnsignedWord();
				int k1 = stream.readDWord();
				npc.graphicHeight = k1 >> 16;
				npc.graphicDelay = loopCycle + (k1 & 0xffff);
				npc.currentAnim = 0;
				npc.animCycle = 0;
				if (npc.graphicDelay > loopCycle)
					npc.currentAnim = -1;
				if (npc.gfxId == 65535)
					npc.gfxId = -1;
				try {
					if (FrameReader.animationlist[Integer.parseInt(Integer.toHexString(SpotAnim.cache[npc.gfxId].animation.frameIDs[0]).substring(0,
							Integer.toHexString(SpotAnim.cache[npc.gfxId].animation.frameIDs[0]).length() - 4), 16)].length == 0)
						onDemandFetcher.requestFileData(1, Integer.parseInt(Integer.toHexString(SpotAnim.cache[npc.gfxId].animation.frameIDs[0])
														.substring(0,Integer.toHexString(SpotAnim.cache[npc.gfxId].animation.frameIDs[0]).length() - 4), 16));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if ((l & 0x20) != 0) {
				npc.interactingEntity = stream.readUnsignedWord();
				if (npc.interactingEntity == 65535)
					npc.interactingEntity = -1;
			}
			if ((l & 1) != 0) {
				npc.textSpoken = stream.readString();
				npc.textCycle = 100;

			}
			if ((l & 0x40) != 0) {
				int damage = inStream.readShortA();
				int hitmark = stream.readByteS();
				int icon = stream.readUnsignedByte();
				npc.updateHitData(hitmark, damage, loopCycle, icon, 0);
				npc.loopCycleStatus = loopCycle + 300;
				npc.currentHealth = inStream.readShortA();
				npc.maxHealth = inStream.readShortA();
			}
			if ((l & 2) != 0) {
				npc.desc = NPCDef.forID(stream.readWordBigEndian());
				npc.boundDim = npc.desc.squaresNeeded;
				npc.anInt1504 = npc.desc.degreesToTurn;
				npc.walkAnimIndex = npc.desc.walkAnim;
				npc.runAnimation = npc.desc.runAnim;
				npc.turn180AnimIndex = npc.desc.turn180AnimIndex;
				npc.turn90CWAnimIndex = npc.desc.turn90CWAnimIndex;
				npc.turn90CCWAnimIndex = npc.desc.turn90CCWAnimIndex;
				npc.standAnimIndex = npc.desc.standAnim;
			}
			if ((l & 4) != 0) {
				npc.anInt1538 = stream.readLEShort();
				npc.anInt1539 = stream.readLEShort();
			}
		}
	}

	private void buildAtNPCMenu(NPCDef entityDef, int i, int j, int k) {
		if (menuActionRow >= 400)
			return;
		if (entityDef.childrenIDs != null)
			entityDef = entityDef.getAlteredNPCDef();
		if (entityDef == null)
			return;
		if (!entityDef.clickable)
			return;
		String s = entityDef.name;
		if (entityDef.combatLevel != 0)
			s = s + combatDiffColor(myPlayer.combatLevel, entityDef.combatLevel) + " (level: " + entityDef.combatLevel + ")";
		if (itemSelected == 1) {
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @yel@" + s;
			menuActionID[menuActionRow] = 582;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
			return;
		}
		if (spellSelected == 1) {
			if ((spellUsableOn & 2) == 2) {
				menuActionName[menuActionRow] = spellTooltip + " @yel@" + s;
				menuActionID[menuActionRow] = 413;
				menuActionCmd1[menuActionRow] = i;
				menuActionCmd2[menuActionRow] = k;
				menuActionCmd3[menuActionRow] = j;
				menuActionRow++;
			}
		} else {
			if (entityDef.actions != null) {
				for (int l = 4; l >= 0; l--)
					if (entityDef.actions[l] != null
							&& !entityDef.actions[l].equalsIgnoreCase("attack")) {
						menuActionName[menuActionRow] = entityDef.actions[l] + " @yel@" + s;
						if (l == 0)
							menuActionID[menuActionRow] = 20;
						if (l == 1)
							menuActionID[menuActionRow] = 412;
						if (l == 2)
							menuActionID[menuActionRow] = 225;
						if (l == 3)
							menuActionID[menuActionRow] = 965;
						if (l == 4)
							menuActionID[menuActionRow] = 478;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}
			if (entityDef.actions != null) {
				for (int i1 = 4; i1 >= 0; i1--)
					if (entityDef.actions[i1] != null
							&& entityDef.actions[i1].equalsIgnoreCase("attack")) {
						char c = '\0';
						if (entityDef.combatLevel > myPlayer.combatLevel)
							c = '\u07D0';
						menuActionName[menuActionRow] = entityDef.actions[i1] + " @yel@" + s;
						if (i1 == 0)
							menuActionID[menuActionRow] = 20 + c;
						if (i1 == 1)
							menuActionID[menuActionRow] = 412 + c;
						if (i1 == 2)
							menuActionID[menuActionRow] = 225 + c;
						if (i1 == 3)
							menuActionID[menuActionRow] = 965 + c;
						if (i1 == 4)
							menuActionID[menuActionRow] = 478 + c;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}
			// menuActionName[menuActionRow] = "Examine @yel@" + s +
			// " @gre@(@whi@" + entityDef.type + "@gre@)";
			menuActionName[menuActionRow] = "Examine @yel@" + s;
			menuActionID[menuActionRow] = 1025;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
		}
	}

	public String pvpCombatLevel(int combatLevel, int summoningLevel) {
		double i = summoningLevel / 8;
		int s = 1;
		s *= i;
		return i >= 1 ? "(level: " + (combatLevel - s) + "+" + s + ")" : "(level: " + combatLevel + ")";
	}

	public int SummoningLevel = 0;

	private void buildAtPlayerMenu(int i, int j, Player player, int k) {
		if (player == myPlayer)
			return;
		if (menuActionRow >= 400)
			return;
		String s;
		if (player.skill == 0) {
			s = (player.title != "" ? "<col=" + player.colorTitle + ">" + player.title + "</col> " : "") + player.name
					+ combatDiffColor(myPlayer.combatLevel, player.combatLevel)
					+ " "
					+ pvpCombatLevel(player.combatLevel, player.SummonLevel)
					+ "";
		} else {
			s = (player.title != "" ? "<col=" + player.colorTitle + ">" + player.title + "</col> " : "") + player.name
					+ combatDiffColor(myPlayer.combatLevel, player.combatLevel)
					+ " "
					+ pvpCombatLevel(player.combatLevel, player.SummonLevel)
					+ "";
		}
		// System.out.println(getRank(player.skill));
		if(player.skill >= 0 && player.skill != 128 && getOption("menu_crown")) {
			s = "<img=" + player.skill + ">" + s;
		}
		if (itemSelected == 1) {
			menuActionName[menuActionRow] = "Use " + selectedItemName
					+ " with <col=0xffffff>" + s;
			menuActionID[menuActionRow] = 491;
			menuActionCmd1[menuActionRow] = j;
			menuActionCmd2[menuActionRow] = i;
			menuActionCmd3[menuActionRow] = k;
			menuActionRow++;
		} else if (spellSelected == 1) {
			if ((spellUsableOn & 8) == 8) {
				menuActionName[menuActionRow] = spellTooltip + " <col=0xffffff>" + s;
				menuActionID[menuActionRow] = 365;
				menuActionCmd1[menuActionRow] = j;
				menuActionCmd2[menuActionRow] = i;
				menuActionCmd3[menuActionRow] = k;
				menuActionRow++;
			}
		} else {
			for (int l = 5; l >= 0; l--)
				if (atPlayerActions[l] != null) {
					menuActionName[menuActionRow] = atPlayerActions[l]
							+ " <col=0xffffff>" + s;
					char c = '\0';
					if (atPlayerActions[l].equalsIgnoreCase("attack")) {
						if (player.combatLevel > myPlayer.combatLevel)
							c = '\u07D0';
						if (myPlayer.team != 0 && player.team != 0)
							if (myPlayer.team == player.team)
								c = '\u07D0';
							else
								c = '\0';
					} else if (atPlayerArray[l])
						c = '\u07D0';
					if (l == 0)
						menuActionID[menuActionRow] = 561 + c;
					if (l == 1)
						menuActionID[menuActionRow] = 779 + c;
					if (l == 2)
						menuActionID[menuActionRow] = 27 + c;
					if (l == 3)
						menuActionID[menuActionRow] = 577 + c;
					if (l == 4)
						menuActionID[menuActionRow] = 729 + c;
					if (l == 5)
						menuActionID[menuActionRow] = 1200 + c;
					menuActionCmd1[menuActionRow] = j;
					menuActionCmd2[menuActionRow] = i;
					menuActionCmd3[menuActionRow] = k;
					menuActionRow++;
				}
		}
		for (int i1 = 0; i1 < menuActionRow; i1++) {
			if (menuActionID[i1] == 516) {
				menuActionName[i1] = "Walk here <col=0xffffff>" + s;
				return;
			}
		}
	}

	private void assignOldValuesToNewRequest(GameObjectSpawnRequest request) {
		int uid = 0;
		int objectId = 0;
		int j = -1;
		int type = 0;
		int face = 0;
		if (request.objectType == 0) {
			uid = worldController.getWallObjectUID(request.plane, request.tileX, request.tileY);
			objectId = worldController.fetchWallObjectNewUID(request.plane, request.tileX, request.tileY);
		}
		if (request.objectType == 1) {
			uid = worldController.getWallDecorationUID(request.plane, request.tileX, request.tileY);
			objectId = worldController.fetchWallDecorationNewUID(request.plane, request.tileX, request.tileY);
		}
		if (request.objectType == 2) {
			uid = worldController.getInteractableObjectUID(request.plane, request.tileX, request.tileY);
			objectId = worldController.fetchObjectMeshNewUID(request.plane, request.tileX, request.tileY);
		}
		if (request.objectType == 3) {
			uid = worldController.getGroundDecorationUID(request.plane, request.tileX, request.tileY);
			objectId = worldController.fetchGroundDecorationNewUID(request.plane, request.tileX, request.tileY);
		}
		if (uid != 0) {
			int i1 = worldController.getIDTagForXYZ(request.plane,
					request.tileX, request.tileY, uid);
			j = objectId;
			type = i1 & 0x1f;
			face = i1 >> 6;
		}
		request.objectId = j;
		request.type = type;
		request.face = face;
	}

	private void processRequestedAudio() {
		for (int index = 0; index < currentSound; index++) {
			boolean flag1 = false;
			try {
				Stream stream = Sounds.fetchSoundData(soundType[index],
						sound[index]);
				new SoundPlayer((InputStream) new ByteArrayInputStream(
						stream.buffer, 0, stream.currentOffset),
						soundVolume[index], soundDelay[index]);
				if (System.currentTimeMillis()
						+ (long) (stream.currentOffset / 22) > lastSoundTime
						+ (long) (lastSoundPosition / 22)) {
					lastSoundPosition = stream.currentOffset;
					lastSoundTime = System.currentTimeMillis();
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			if (!flag1 || soundDelay[index] == -5) {
				currentSound--;
				for (int j = index; j < currentSound; j++) {
					sound[j] = sound[j + 1];
					soundType[j] = soundType[j + 1];
					soundDelay[j] = soundDelay[j + 1];
					soundVolume[j] = soundVolume[j + 1];
				}
				index--;
			} else {
				soundDelay[index] = -5;
			}
		}

		if (prevSong > 0) {
			prevSong -= 20;
			if (prevSong < 0)
				prevSong = 0;
			if (prevSong == 0 && musicEnabled) {
				nextSong = currentSong;
				songChanging = true;
				onDemandFetcher.requestFileData(2, nextSong);
			}
		}
	}

	public void playSound(int id, int type, int delay, int volume) {
		sound[currentSound] = id;
		soundType[currentSound] = type;
		soundDelay[currentSound] = delay + Sounds.anIntArray326[id];
		soundVolume[currentSound] = volume;
		currentSound++;
	}

	/*public static void playMusic() {
		try {
			Sequencer sequencer = MidiSystem.getSequencer();
			if (sequencer == null)
				throw new MidiUnavailableException();
			sequencer.open();
			FileInputStream is = new FileInputStream(signlink.findcachedir()
					+ "0.mid");
			Sequence mySeq = MidiSystem.getSequence(is);
			sequencer.setSequence(mySeq);
			sequencer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/**
	 * Load everything and prep for gameplay!
	 */
	void startUp() {

		
		
		isLoading = true;
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (isLoading) {
					drawLoadingText(lastPercent, loadingStepText);
					if (onDemandFetcher != null)
						processOnDemandQueue();
				}
			}
		});
		t.setPriority(10);
		t.start();

		drawSmoothLoading(20, "Starting up");
		if (!Configuration.JAGCACHED_ENABLED)
			new CacheDownloader(this).downloadCache();

		if (signlink.sunjava)
			super.minDelay = 5;

		getDocumentBaseHost();

		if (signlink.cache_dat != null) {
			for (int i = 0; i < 7; i++)
				cacheIndices[i] = new Decompressor(signlink.cache_dat,
						signlink.cache_idx[i], i + 1);
		}

		try {
			if (Configuration.JAGCACHED_ENABLED) {
				connectToUpdateServer();
			}
			
			/** Loading Fonts **/
			drawSmoothLoading(40, "Loading fonts");
			titleStreamLoader = streamLoaderForName(1, "title screen", "title", expectedCRCs[1], 25);
			smallText = new TypeFace(false, "p11_full", titleStreamLoader);
			smallHit = new TypeFace(false, "hit_full", titleStreamLoader);
			bigHit = new TypeFace(true, "critical_full", titleStreamLoader);
			drawingArea = new TypeFace(false, "p12_full", titleStreamLoader);
			chatTextDrawingArea = new TypeFace(false, "b12_full", titleStreamLoader);
			// newFont = new TextDrawingArea("Images.dat", titleStreamLoader);
			normalFont = new TypeFace(false, "p12_full", titleStreamLoader);
			boldFont = new TypeFace(false, "b12_full", titleStreamLoader);
			fancyText = new TypeFace(true, "q8_full", titleStreamLoader);
			newSmallFont = new RSFontSystem(false, "p11_full", titleStreamLoader);
			newRegularFont = new RSFontSystem(false, "p12_full", titleStreamLoader);
			newBoldFont = new RSFontSystem(false, "b12_full", titleStreamLoader);
			newFancyFont = new RSFontSystem(true, "q8_full", titleStreamLoader);
			TypeFace aTextDrawingArea_1273 = new TypeFace(true, "q8_full", titleStreamLoader);
			
			/** Render Title Screen **/
			resetImageProducers();
			loadTitleScreen();
			
			/** Unpack Archives from the Config Indice **/
			drawSmoothLoading(50, "Loading archives");
			CacheArchive configArchive = streamLoaderForName(2, "config", "config", expectedCRCs[2], 30);
			CacheArchive interfaceArchive = streamLoaderForName(3, "interface", "interface", expectedCRCs[3], 35);
			CacheArchive mediaArchive = streamLoaderForName(4, "2d graphics", "media", expectedCRCs[4], 40);
			CacheArchive textureArchive = streamLoaderForName(6, "textures", "textures", expectedCRCs[6], 45);
			CacheArchive censorArchive = streamLoaderForName(7, "chat system", "wordenc", expectedCRCs[7], 50);
			CacheArchive soundArchive = streamLoaderForName(8, "sound effects", "sounds", expectedCRCs[8], 55);
			
			tileSettingBits = new byte[4][104][104];
			intGroundArray = new int[4][105][105];
			worldController = new WorldController(intGroundArray);
			for (int j = 0; j < 4; j++)
				clippingPlanes[j] = new CollisionDetection();

			miniMap = new Sprite(512, 512);
			
			/** Setup On Demand! **/
			CacheArchive updateListArchive = streamLoaderForName(5, "update list", "versionlist", expectedCRCs[5], 60);
			drawSmoothLoading(60, "Connecting to File Server");
			onDemandFetcher = new OnDemandFetcher();
			onDemandFetcher.start(updateListArchive, this);
			drawSmoothLoading(65, "Loading Animations");
			FrameReader.initialise(onDemandFetcher.getAnimCount());
			drawSmoothLoading(70, "Loading Models");
			Model.initialise(onDemandFetcher.getModelCount(), onDemandFetcher);
			SpriteCache.initialise(onDemandFetcher.getImageCount(), onDemandFetcher);

			/** Load preferences.dat **/
			loadSettings();
			
			/** Load Music **/
			if (!lowMem && musicEnabled) {
				drawSmoothLoading(75, "Requesting music");
				nextSong = 0;
				onDemandFetcher.requestFileData(2, nextSong);
			}
			
			/**
			 * Unpack Sprite Archive(sprites.dat/idx)
			 * TODO: Remove and move images to sprite indice. 
			 **/
			drawSmoothLoading(80, "Unpacking Media");
			try {
				SpriteLoader.loadSprites(configArchive);
				cacheSprite = SpriteLoader.sprites;
			} catch (Exception e) {
				e.printStackTrace();
			}

			/** Preload Images from Sprite Indice **/
			drawSmoothLoading(81, "Unpacking Media");
			SpriteCache.preloadedPriorityImages(this);
			
			/** Load from media archive(317 sprite cache) **/
			multiOverlay = new Sprite(mediaArchive, "overlay_multiway", 0);
			try {
				for (int index = 0; index < 20; index++) {
					if (index < 17) {
						orbs[index] = new Sprite(mediaArchive, "orbs", index);
					} else {
						orbs[index] = new Sprite(mediaArchive, "orbs", 1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			SkillOrbHandler.loadImages();
			mapBack = new Background(mediaArchive, "mapback", 0);
			for (int sideIconIndex = 0; sideIconIndex <= 14; sideIconIndex++)
				sideIcons[sideIconIndex] = new Sprite(mediaArchive, "sideicons", sideIconIndex);
			for (int compassIndex = 0; compassIndex < 2; compassIndex++)
				compass[compassIndex] = new Sprite(mediaArchive, "compass", compassIndex);
			mapEdge = new Sprite(mediaArchive, "mapedge", 0);
			mapEdge.method345();
			for (int hitmarkIndex = 0; hitmarkIndex < 5; hitmarkIndex++)
				hitMarks[hitmarkIndex] = new Sprite(mediaArchive, "hitmarks", hitmarkIndex);
			for (int scrollPartIndex = 0; scrollPartIndex < 12; scrollPartIndex++)
				scrollPart[scrollPartIndex] = new Sprite(mediaArchive, "scrollpart", scrollPartIndex);
			for (int scrollBarIndex = 0; scrollBarIndex < 6; scrollBarIndex++)
				scrollBar[scrollBarIndex] = new Sprite(mediaArchive, "scrollbar", scrollBarIndex);
			for (int mapSceneIndex = 0; mapSceneIndex < 80; mapSceneIndex++) {
				try {
					mapScenes[mapSceneIndex] = new Background(mediaArchive, "mapscene", mapSceneIndex);
				} catch (Exception _ex) {
					System.out.println("Error loading mapscene " + mapSceneIndex);
				}
			}
			for (int mapFunctionIndex = 0; mapFunctionIndex < 70; mapFunctionIndex++) {
				try {
					mapFunctions[mapFunctionIndex] = new Sprite(mediaArchive, "mapfunction",
							mapFunctionIndex);
				} catch (Exception _ex) {
					System.out.println("Error loading mapscene " + mapFunctionIndex);
				}
			}
			try {
				for (int hintIconIndex = 0; hintIconIndex < 6; hintIconIndex++)
					headIconsHint[hintIconIndex] = new Sprite(mediaArchive, "headicons_hint", hintIconIndex);
			} catch (Exception _ex) {
				_ex.printStackTrace();
			}
			for (int headIconIndex = 0; headIconIndex < 18; headIconIndex++) {
				try {
					headIcons[headIconIndex] = new Sprite(mediaArchive, "headicons_prayer", headIconIndex);
				} catch (Exception _ex) {
					_ex.printStackTrace();
				}
			}
			drawSmoothLoading(82, "Unpacking Media");
			for (int skullIconIndex = 0; skullIconIndex < 3; skullIconIndex++)
				skullIcons[skullIconIndex] = new Sprite(mediaArchive, "headicons_pk", skullIconIndex);
			mapFlag = new Sprite(mediaArchive, "mapmarker", 0);
			mapMarker = new Sprite(mediaArchive, "mapmarker", 1);
			for (int crossesIndex = 0; crossesIndex < 8; crossesIndex++)
				crosses[crossesIndex] = new Sprite(mediaArchive, "cross", crossesIndex);
			mapDotItem = new Sprite(mediaArchive, "mapdots", 0);
			mapDotNPC = new Sprite(mediaArchive, "mapdots", 1);
			mapDotPlayer = new Sprite(mediaArchive, "mapdots", 2);
			mapDotFriend = new Sprite(mediaArchive, "mapdots", 3);
			mapDotTeam = new Sprite(mediaArchive, "mapdots", 4);
			mapDotClan = new Sprite(mediaArchive, "mapdots", 5);
			for (int modIconIndex = 0; modIconIndex < 8; modIconIndex++)
				modIcons[modIconIndex] = new Sprite(mediaArchive, "mod_icons", modIconIndex);
			modIcons[8] = cacheSprite[14];
			newSmallFont.unpackChatImages(modIcons);
			newRegularFont.unpackChatImages(modIcons);
			newBoldFont.unpackChatImages(modIcons);
			newFancyFont.unpackChatImages(modIcons);
			Sprite sprite = new Sprite(mediaArchive, "screenframe", 0);
			leftFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight, getGameComponent());
			sprite.method346(0, 0);
			sprite = new Sprite(mediaArchive, "screenframe", 1);
			topFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight, getGameComponent());
			sprite.method346(0, 0);
			sprite = new Sprite(mediaArchive, "screenframe", 2);
			rightFrame = new RSImageProducer(sprite.myWidth, sprite.myHeight, getGameComponent());
			sprite.method346(0, 0);
			sprite = new Sprite(mediaArchive, "mapedge", 0);
			new RSImageProducer(sprite.myWidth, sprite.myHeight, getGameComponent());
			sprite.method346(0, 0);

			/** Map Stuff? **/
			int i5 = (int) (Math.random() * 21D) - 10;
			int j5 = (int) (Math.random() * 21D) - 10;
			int k5 = (int) (Math.random() * 21D) - 10;
			int l5 = (int) (Math.random() * 41D) - 20;
			for (int i6 = 0; i6 < 100; i6++) {
				if (mapFunctions[i6] != null)
					mapFunctions[i6].decodePalette(i5 + l5, j5 + l5, k5 + l5);
				if (mapScenes[i6] != null)
					mapScenes[i6].decodePalette(i5 + l5, j5 + l5, k5 + l5);
			}
			
			/** Unpack Texture Archive **/
			drawSmoothLoading(83, "Unpacking Textures");
			TextureLoader317.unpackTextures(textureArchive);
			Rasterizer.calculatePalette(0.80000000000000004D, true);
			TextureLoader317.resetTextures();
			
			/** Unpack Configurations! **/
			drawSmoothLoading(86, "Unpacking Config");
			Animation.unpackConfig(configArchive);
			ObjectDef.unpackConfig(configArchive, soundArchive);
			OverLayFlo317.unpackConfig(configArchive);
			Flo.unpackConfig(configArchive);
			ItemDef.unpackConfig(configArchive);
			//FontArchive.unpackConfig(configArchive);
			NewItemDef.unpackConfig(configArchive);
			NPCDef.unpackConfig(configArchive);
			IDK.unpackConfig(configArchive);
			SpotAnim.unpackConfig(configArchive);
			Varp.unpackConfig(configArchive);
			VarBit.unpackConfig(configArchive);
			TextureDef.unpackConfig(configArchive);
			TextureLoader667.initTextures(1419, onDemandFetcher);
			ItemDef.isMembers = isMembers;
			
			//drawSmoothLoading(85, "Pre-Loading Item Models");
			/*for(int id = 0; id < NewItemDef.totalItems; id++) {
				//ItemDef.forID(id);
				try {
					ItemDef def = ItemDef.forID(id);
					def.getItemModel(1);
					def.getEquipModel(0);
				} catch (Exception e) {
				}
			}
			System.gc();*/
			
			/** Unpack Sound Archive **/
			drawSmoothLoading(87, "Unpacking Sounds");
			if (!lowMem) {
				byte abyte0[] = soundArchive.getDataForName("sounds.dat");
				Stream stream = new Stream(abyte0);
				Sounds.unpack(stream);
			}
			
			/** Unpack Widgets/Interfaces **/
			drawSmoothLoading(88, "Unpacking Interfaces");
			TypeFace fonts[] = { smallText, drawingArea, chatTextDrawingArea, aTextDrawingArea_1273 };
			RSInterface.unpack(interfaceArchive, fonts, mediaArchive);
			handleSettings();

			clearMemoryCaches();
			drawSmoothLoading(90, "Preparing Game Engine");
			try {
				for (int j6 = 0; j6 < 33; j6++) {
					int k6 = 999;
					int i7 = 0;
					for (int k7 = 0; k7 < 34; k7++) {
						if (mapBack.imgPixels[k7 + j6 * mapBack.imgWidth] == 0) {
							if (k6 == 999)
								k6 = k7;
							continue;
						}
						if (k6 == 999)
							continue;
						i7 = k7;
						break;
					}

					mapBackOffsets0[j6] = k6;
					mapbackOffsets1[j6] = i7 - k6;
				}
				for (int l6 = 3; l6 < 160; l6++) {
					int j7 = 999;
					int l7 = 0;
					for (int j8 = 20; j8 < 172; j8++) {
						try {
							if (mapBack.imgPixels[j8 + l6 * mapBack.imgWidth] == 0
									&& (j8 > 34 || l6 > 34)) {
								if (j7 == 999)
									j7 = j8;
								continue;
							}

							if (j7 == 999)
								continue;
							l7 = j8;
							break;
						} catch (Exception e) {
							//e.printStackTrace();
						}
					}
					if (minimapXPosArray.length > (l6 - 3))
						minimapXPosArray[l6 - 3] = j7 - 20;
					if (minimapYPosArray.length > (l6 - 3)) {
						minimapYPosArray[l6 - 3] = l7 - j7;
						if (minimapYPosArray[l6 - 3] == -20)
							minimapYPosArray[l6 - 3] = 154;
					}
				}
			} catch (Exception _ex) {
				//_ex.printStackTrace();
			}
			updateGameArea();
			
			/** Unpack Censor Archive **/
			Censor.loadConfig(censorArchive);
			
			mouseDetection = new MouseDetection(this);
			startRunnable(mouseDetection, 10);
			ObjectOnTile.clientInstance = this;
			ObjectDef.clientInstance = this;
			NPCDef.clientInstance = this;

			int loaded = 0;
			while (loaded < 10) {
				loaded = 0;
				for (int i = 36; i < 46; i++) {
					if (SpriteCache.spriteCache[i] != null)
						loaded++;
				}
				for (int i = 53; i < 57; i++) {
					if (SpriteCache.spriteCache[i] != null)
						loaded++;
				}
				drawSmoothLoading(90 + loaded > 100 ? 100 : 90 + loaded, "Fetching login graphics.");
			}
			
			/** Preload Priority images from sprite indice. **/
			SpriteCache.preloadLowPriorityImages();
			// onDemandFetcher.fetchMaps(true);
			loginScreenCursorPos = 0;
			isLoading = false;
			Account.load();
			ArrayList<Account> accounts = Account.accounts;
			RSInterface.buildPlayerMenu(accounts);
			return;
		} catch (Exception exception) {
			signlink.reporterror("loaderror " + loadingStepText + " " + lastPercent);
			exception.printStackTrace();
		}
		loadingError = true;
	}

	private void updatePlayerMovement(Stream stream, int i) {
		while (stream.bitPosition + 10 < i * 8) {
			int j = stream.readBits(11);
			if (j == 2047)
				break;
			if (playerArray[j] == null) {
				playerArray[j] = new Player();
				if (aStreamArray895s[j] != null)
					playerArray[j].updatePlayerAppearance(aStreamArray895s[j]);
			}
			playerIndices[playerCount++] = j;
			Player player = playerArray[j];
			player.loopCycle = loopCycle;
			int k = stream.readBits(1);
			if (k == 1)
				playersToUpdate[playersToUpdateCount++] = j;
			int l = stream.readBits(1);
			int i1 = stream.readBits(5);
			if (i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(5);
			if (j1 > 15)
				j1 -= 32;
			player.setPos(myPlayer.pathX[0] + j1, myPlayer.pathY[0] + i1,
					l == 1);
		}
		stream.finishBitAccess();
	}

	public boolean inCircle(int circleX, int circleY, int clickX, int clickY, int radius) {
		return Math.pow((circleX + radius - clickX), 2) + Math.pow((circleY + radius - clickY), 2) < Math.pow(radius, 2);
	}

	private void processMainScreenClick() {
		if (minimapStatus != 0) {
			return;
		}
		if (super.clickMode3 == 1) {
			int clickX = super.saveClickX - 3 - ((settings.get("gameframe") == 474 && clientSize == 0) ? (clientWidth - 195) : (clientSize == 0 ? clientWidth - 214 : clientWidth - 163));
			int clickY = super.saveClickY - ((settings.get("gameframe") == 474 && clientSize == 0) ? 7 : (clientSize == 0 ? 9 : 6));
			// if (i >= 0 && j >= 0 && i < 152 && j < 152 && canClickMap()) {
			if (inCircle(0, 0, clickX, clickY, 76)) {
				clickX -= 73;
				clickY -= 75;
				int k = viewRotation + minimapRotation & 0x7ff;
				int i1 = Rasterizer.SINE[k];
				int j1 = Rasterizer.COSINE[k];
				i1 = i1 * (minimapZoom + 256) >> 8;
				j1 = j1 * (minimapZoom + 256) >> 8;
				int k1 = clickY * i1 + clickX * j1 >> 11;
				int l1 = clickY * j1 - clickX * i1 >> 11;
				int i2 = myPlayer.x + k1 >> 7;
				int j2 = myPlayer.y - l1 >> 7;
				boolean flag1 = doWalkTo(1, 0, 0, 0, myPlayer.pathY[0], 0, 0, j2, myPlayer.pathX[0], true, i2);
				if (flag1) {
					stream.writeByte(clickX);
					stream.writeByte(clickY);
					stream.writeWord(viewRotation);
					stream.writeByte1(57);
					stream.writeByte1(minimapRotation);
					stream.writeByte1(minimapZoom);
					stream.writeByte(89);
					stream.writeWord(myPlayer.x);
					stream.writeWord(myPlayer.y);
					stream.writeByte(anInt1264);
					stream.writeByte(63);
				}
			}
			anInt1117++;
			if (anInt1117 > 1151) {
				anInt1117 = 0;
				stream.createFrame(246);
				stream.writeByte(0);
				int l = stream.currentOffset;
				if ((int) (Math.random() * 2D) == 0)
					stream.writeByte(101);
				stream.writeByte(197);
				stream.writeWord((int) (Math.random() * 65536D));
				stream.writeByte((int) (Math.random() * 256D));
				stream.writeByte(67);
				stream.writeWord(14214);
				if ((int) (Math.random() * 2D) == 0)
					stream.writeWord(29487);
				stream.writeWord((int) (Math.random() * 65536D));
				if ((int) (Math.random() * 2D) == 0)
					stream.writeByte(220);
				stream.writeByte(180);
				stream.writeBytes(stream.currentOffset - l);
			}
		}
	}

	private String interfaceIntToString(int j) {
		if (j < 0x3b9ac9ff)
			return String.valueOf(j);
		else
			return "*";
	}

	private void showErrorScreen() {
		Graphics g = getGameComponent().getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 765, 503);
		setFPS(1);
		if (loadingError) {
			g.setFont(new Font("Helvetica", 1, 16));
			g.setColor(Color.yellow);
			int k = 35;
			g.drawString(
					"Sorry, an error has occured whilst loading RevolutionX", 30,
					k);
			k += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, k);
			k += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString(
					"1: Try closing ALL open web-browser windows, and reloading",
					30, k);
			k += 30;
			g.drawString(
					"2: Try clearing your web-browsers cache from tools->internet options",
					30, k);
			k += 30;
			g.drawString("3: Try using a different game-world", 30, k);
			k += 30;
			g.drawString("4: Try rebooting your computer", 30, k);
			k += 30;
			g.drawString(
					"5: Try selecting a different version of Java from the play-game menu",
					30, k);
		}
		if (genericLoadingError) {
			g.setFont(new Font("Helvetica", 1, 20));
			g.setColor(Color.white);
			g.drawString("Error - unable to load game!", 50, 50);
			g.drawString("To play RevolutionX make sure you play from", 50, 100);
			g.drawString("http://www.RevolutionX.net", 50, 150);
		}
		if (rsAlreadyLoaded) {
			g.setColor(Color.yellow);
			int l = 35;
			g.drawString(
					"Error a copy of RevolutionX already appears to be loaded",
					30, l);
			l += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, l);
			l += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString(
					"1: Try closing ALL open web-browser windows, and reloading",
					30, l);
			l += 30;
			g.drawString("2: Try rebooting your computer, and reloading", 30, l);
			l += 30;
		}
	}

	public URL getCodeBase() {
		try {
			return new URL(Configuration.server + ":" + (80 + portOff));
		} catch (Exception _ex) {
		}
		return null;
	}

	private void readNPCUpdateBlockForced() {
		for (int j = 0; j < npcCount; j++) {
			int k = npcIndices[j];
			NPC npc = npcArray[k];
			if (npc != null)
				entityUpdateBlock(npc);
		}
	}

	private void entityUpdateBlock(Entity entity) {
		if (entity.x < 128 || entity.y < 128 || entity.x >= 13184
				|| entity.y >= 13184) {
			entity.anim = -1;
			entity.gfxId = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.pathX[0] * 128 + entity.boundDim * 64;
			entity.y = entity.pathY[0] * 128 + entity.boundDim * 64;
			entity.resetWalk();
		}
		if (entity == myPlayer
				&& (entity.x < 1536 || entity.y < 1536 || entity.x >= 11776 || entity.y >= 11776)) {
			entity.anim = -1;
			entity.gfxId = -1;
			entity.anInt1547 = 0;
			entity.anInt1548 = 0;
			entity.x = entity.pathX[0] * 128 + entity.boundDim * 64;
			entity.y = entity.pathY[0] * 128 + entity.boundDim * 64;
			entity.resetWalk();
		}
		if (entity.anInt1547 > loopCycle)
			updateEntityPos(entity);
		else if (entity.anInt1548 >= loopCycle)
			updateEntityFace(entity);
		else
			processWalkingStep(entity);
		appendFocusDest(entity);
		appendAnimation(entity);
	}

	private void updateEntityPos(Entity entity) {
		int i = entity.anInt1547 - loopCycle;
		int j = entity.anInt1543 * 128 + entity.boundDim * 64;
		int k = entity.anInt1545 * 128 + entity.boundDim * 64;
		entity.x += (j - entity.x) / i;
		entity.y += (k - entity.y) / i;
		entity.anInt1503 = 0;
		if (entity.turnInfo == 0)
			entity.turnDirection = 1024;
		if (entity.turnInfo == 1)
			entity.turnDirection = 1536;
		if (entity.turnInfo == 2)
			entity.turnDirection = 0;
		if (entity.turnInfo == 3)
			entity.turnDirection = 512;
	}

	private void updateEntityFace(Entity entity) {
		if (entity.anInt1548 == loopCycle
				|| entity.anim == -1
				|| entity.animationDelay != 0
				|| entity.frameCycle + 1 > Animation.anims[entity.anim]
						.getFrameLength(entity.currentAnimFrame)) {
			int i = entity.anInt1548 - entity.anInt1547;
			int j = loopCycle - entity.anInt1547;
			int k = entity.anInt1543 * 128 + entity.boundDim * 64;
			int l = entity.anInt1545 * 128 + entity.boundDim * 64;
			int i1 = entity.anInt1544 * 128 + entity.boundDim * 64;
			int j1 = entity.anInt1546 * 128 + entity.boundDim * 64;
			entity.x = (k * (i - j) + i1 * j) / i;
			entity.y = (l * (i - j) + j1 * j) / i;
		}
		entity.anInt1503 = 0;
		if (entity.turnInfo == 0)
			entity.turnDirection = 1024;
		if (entity.turnInfo == 1)
			entity.turnDirection = 1536;
		if (entity.turnInfo == 2)
			entity.turnDirection = 0;
		if (entity.turnInfo == 3)
			entity.turnDirection = 512;
		entity.anInt1552 = entity.turnDirection;
	}

	private void processWalkingStep(Entity entity) {
		entity.forcedAnimId = entity.standAnimIndex;
		if (entity.pathLength == 0) {
			entity.anInt1503 = 0;
			return;
		}
		if (entity.anim != -1 && entity.animationDelay == 0) {
			Animation animation = Animation.anims[entity.anim];
			if (entity.anInt1542 > 0 && animation.resetWhenWalk == 0) {
				entity.anInt1503++;
				return;
			}
			if (entity.anInt1542 <= 0 && animation.priority == 0) {
				entity.anInt1503++;
				return;
			}
		}
		int currentX = entity.x;
		int currentY = entity.y;
		int nextX = entity.pathX[entity.pathLength - 1] * 128 + entity.boundDim
				* 64;
		int nextY = entity.pathY[entity.pathLength - 1] * 128 + entity.boundDim
				* 64;
		if (nextX - currentX > 256 || nextX - currentX < -256
				|| nextY - currentY > 256 || nextY - currentY < -256) {
			entity.x = nextX;
			entity.y = nextY;
			return;
		}
		if (currentX < nextX) {
			if (currentY < nextY)
				entity.turnDirection = 1280;
			else if (currentY > nextY)
				entity.turnDirection = 1792;
			else
				entity.turnDirection = 1536;
		} else if (currentX > nextX) {
			if (currentY < nextY)
				entity.turnDirection = 768;
			else if (currentY > nextY)
				entity.turnDirection = 256;
			else
				entity.turnDirection = 512;
		} else if (currentY < nextY)
			entity.turnDirection = 1024;
		else
			entity.turnDirection = 0;
		int i1 = entity.turnDirection - entity.anInt1552 & 0x7ff;
		if (i1 > 1024)
			i1 -= 2048;
		int j1 = entity.turn180AnimIndex;
		if (i1 >= -256 && i1 <= 256)
			j1 = entity.walkAnimIndex;
		else if (i1 >= 256 && i1 < 768)
			j1 = entity.turn90CCWAnimIndex;
		else if (i1 >= -768 && i1 <= -256)
			j1 = entity.turn90CWAnimIndex;
		if (j1 == -1)
			j1 = entity.walkAnimIndex;
		entity.forcedAnimId = j1;
		int k1 = 4;
		if (entity.anInt1552 != entity.turnDirection
				&& entity.interactingEntity == -1 && entity.anInt1504 != 0)
			k1 = 2;
		if (entity.pathLength > 2)
			k1 = 6;
		if (entity.pathLength > 3)
			k1 = 8;
		if (entity.anInt1503 > 0 && entity.pathLength > 1) {
			k1 = 8;
			entity.anInt1503--;
		}
		if (entity.aBooleanArray1553[entity.pathLength - 1])
			k1 <<= 1;
		if (k1 >= 8 && entity.forcedAnimId == entity.walkAnimIndex
				&& entity.runAnimation != -1)
			entity.forcedAnimId = entity.runAnimation;
		if (currentX < nextX) {
			entity.x += k1;
			if (entity.x > nextX)
				entity.x = nextX;
		} else if (currentX > nextX) {
			entity.x -= k1;
			if (entity.x < nextX)
				entity.x = nextX;
		}
		if (currentY < nextY) {
			entity.y += k1;
			if (entity.y > nextY)
				entity.y = nextY;
		} else if (currentY > nextY) {
			entity.y -= k1;
			if (entity.y < nextY)
				entity.y = nextY;
		}
		if (entity.x == nextX && entity.y == nextY) {
			entity.pathLength--;
			if (entity.anInt1542 > 0)
				entity.anInt1542--;
		}
	}

	private void appendFocusDest(Entity entity) {
		if (entity.anInt1504 == 0)
			return;
		if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
			try {
				NPC npc = npcArray[entity.interactingEntity];
				if (npc != null) {
					int i1 = entity.x - npc.x;
					int k1 = entity.y - npc.y;
					if (i1 != 0 || k1 != 0)
						entity.turnDirection = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		if (entity.interactingEntity >= 32768) {
			int j = entity.interactingEntity - 32768;
			if (j == playerId)
				j = myPlayerIndex;
			Player player = playerArray[j];
			if (player != null) {
				int l1 = entity.x - player.x;
				int i2 = entity.y - player.y;
				if (l1 != 0 || i2 != 0)
					entity.turnDirection = (int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff;
			}
		}
		if ((entity.anInt1538 != 0 || entity.anInt1539 != 0)
				&& (entity.pathLength == 0 || entity.anInt1503 > 0)) {
			int k = entity.x - (entity.anInt1538 - baseX - baseX) * 64;
			int j1 = entity.y - (entity.anInt1539 - baseY - baseY) * 64;
			if (k != 0 || j1 != 0)
				entity.turnDirection = (int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff;
			entity.anInt1538 = 0;
			entity.anInt1539 = 0;
		}
		int l = entity.turnDirection - entity.anInt1552 & 0x7ff;
		if (l != 0) {
			if (l < entity.anInt1504 || l > 2048 - entity.anInt1504)
				entity.anInt1552 = entity.turnDirection;
			else if (l > 1024)
				entity.anInt1552 -= entity.anInt1504;
			else
				entity.anInt1552 += entity.anInt1504;
			entity.anInt1552 &= 0x7ff;
			if (entity.forcedAnimId == entity.standAnimIndex
					&& entity.anInt1552 != entity.turnDirection) {
				if (entity.nextRenderAnim != -1) {
					entity.forcedAnimId = entity.nextRenderAnim;
					return;
				}
				entity.forcedAnimId = entity.walkAnimIndex;
			}
		}
	}

	private void appendAnimation(Entity entity) {
		entity.aBoolean1541 = false;
		if (entity.forcedAnimId != -1) {
			Animation animation = Animation.anims[entity.forcedAnimId];
			entity.forcedAnimFrameCycle++;
			if (entity.currentForcedAnimFrame < animation.frameCount
					&& entity.forcedAnimFrameCycle > animation.getFrameLength(entity.currentForcedAnimFrame)) {
				entity.forcedAnimFrameCycle = 1;// this is the frame delay. 0 is what it's // normally at. higher number = faster // animations.
				entity.currentForcedAnimFrame++;
				entity.next_idle_frame++;
			}
			entity.next_idle_frame = entity.currentForcedAnimFrame + 1;
			if(entity.next_idle_frame >= animation.frameCount) {
				if(entity.next_idle_frame >= animation.frameCount)
					entity.next_idle_frame = 0;
			}
			if (entity.currentForcedAnimFrame >= animation.frameCount) {
				entity.forcedAnimFrameCycle = 1;
				entity.currentForcedAnimFrame = 0;
				entity.next_idle_frame = 0;
			}
		}
		if (entity.gfxId != -1 && loopCycle >= entity.graphicDelay) {
			if (entity.currentAnim < 0)
				entity.currentAnim = 0;
			Animation animation_1 = SpotAnim.cache[entity.gfxId].animation;
			if (animation_1 != null) {
				for (entity.animCycle++; entity.currentAnim < animation_1.frameCount 
						&& entity.animCycle > animation_1 .getFrameLength(entity.currentAnim); entity.currentAnim++)
					entity.animCycle -= animation_1 .getFrameLength(entity.currentAnim);

				if (entity.currentAnim >= animation_1.frameCount && (entity.currentAnim < 0 || entity.currentAnim >= animation_1.frameCount))
					entity.gfxId = -1;
				entity.next_graphics_frame = entity.currentAnim + 1;
				if(entity.next_graphics_frame >= animation_1.frameCount) {
					if(entity.next_graphics_frame < 0 || entity.next_graphics_frame >= animation_1.frameCount)
						entity.gfxId = -1;
				}
			}
		}
		if (entity.anim != -1 && entity.animationDelay <= 1) {
			if (Animation.anims.length <= entity.anim) {
				return;
			}
			Animation animation_2 = Animation.anims[entity.anim];
			if (animation_2.resetWhenWalk == 1 && entity.anInt1542 > 0
					&& entity.anInt1547 <= loopCycle
					&& entity.anInt1548 < loopCycle) {
				entity.animationDelay = 1;
				return;
			}
		}
		try {
			if (entity.anim != -1 && entity.animationDelay == 0) {
				Animation animation_3 = Animation.anims[entity.anim];
				for (entity.frameCycle++; entity.currentAnimFrame < animation_3.frameCount
						&& entity.frameCycle > animation_3.getFrameLength(entity.currentAnimFrame); entity.currentAnimFrame++)
					entity.frameCycle -= animation_3.getFrameLength(entity.currentAnimFrame);

				if (entity.currentAnimFrame >= animation_3.frameCount) {
					entity.currentAnimFrame -= animation_3.loopDelay;
					entity.anInt1530++;
					if (entity.anInt1530 >= animation_3.frameStep)
						entity.anim = -1;
					if (entity.currentAnimFrame < 0 || entity.currentAnimFrame >= animation_3.frameCount) {
						entity.anim = -1;
						entity.currentAnimFrame = 0;
					}
				}
				entity.next_animation_frame = entity.currentAnimFrame + 1;
				if(entity.next_animation_frame >= animation_3.frameCount) {
					if(entity.anInt1530 >= animation_3.frameStep) {
						//entity.anim = -1;
						entity.next_animation_frame = entity.currentAnimFrame + 1;
					}
					if(entity.next_animation_frame < 0 || entity.next_animation_frame >= animation_3.frameCount) {
						//entity.anim = -1;
						entity.next_animation_frame = entity.currentAnimFrame;
					}
				}
				entity.aBoolean1541 = animation_3.oneSquareAnimation;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (entity.animationDelay > 0)
			entity.animationDelay--;
	}

	private void drawGameScreen() {
		if (fullscreenInterfaceID != -1
				&& (loadingStage == 2 || super.fullGameScreen != null)) {
			if (loadingStage == 2) {

				processInterfaceAnimation(cycleTimer, fullscreenInterfaceID);
				if (openInterfaceID != -1) {
					processInterfaceAnimation(cycleTimer, openInterfaceID);
				}
				cycleTimer = 0;
				resetAllImageProducers();
				super.fullGameScreen.initDrawingArea();
				Rasterizer.lineOffsets = fullScreenTextureArray;
				welcomeScreenRaised = true;
				if (openInterfaceID != -1) {
					RSInterface rsInterface_1 = RSInterface.interfaceCache[openInterfaceID];
					if (rsInterface_1.width == 512
							&& rsInterface_1.height == 334
							&& rsInterface_1.type == 0) {
						rsInterface_1.width = (clientSize == 0 ? 765
								: clientWidth);
						rsInterface_1.height = (clientSize == 0 ? 503
								: clientHeight);
					}
					drawInterface(0, clientSize == 0 ? 0
							: (clientWidth / 2) - 765 / 2, rsInterface_1,
							clientSize == 0 ? 8 : (clientHeight / 2) - 503 / 2);
				}
				RSInterface rsInterface = RSInterface.interfaceCache[fullscreenInterfaceID];
				if (rsInterface.width == 512 && rsInterface.height == 334
						&& rsInterface.type == 0) {
					rsInterface.width = (clientSize == 0 ? 765 : clientWidth);
					rsInterface.height = (clientSize == 0 ? 503 : clientHeight);
				}
				drawInterface(0, clientSize == 0 ? 0
						: (clientWidth / 2) - 765 / 2, rsInterface,
						clientSize == 0 ? 8 : (clientHeight / 2) - 503 / 2);
				
				if (!menuOpen) {
					processRightClick();
					drawTooltip();
				} else {
					drawMenu();
				}
			}
			drawCount++;
			super.fullGameScreen.drawGraphics(0, super.graphics, 0);

			return;
		} else {
			if (drawCount != 0) {
				resetImageProducers2();
			}
		}
		if (welcomeScreenRaised) {
			welcomeScreenRaised = false;
			if (clientSize == 0) {
				topFrame.drawGraphics(0, super.graphics, 0);
				leftFrame.drawGraphics(4, super.graphics, 0);
				rightFrame.drawGraphics(3, super.graphics, 516);
			}
			needDrawTabArea = true;
			inputTaken = true;
			tabAreaAltered = true;
			if (loadingStage != 2) {
				if (gameScreenIP != null)
					gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0,
							super.graphics, clientSize == 0 ? 4 : 0);
				if (clientSize == 0)
					if (mapAreaIP != null)
						mapAreaIP.drawGraphics(0, super.graphics, 516);
			}
		}

		if (menuOpen && menuScreenArea == 1)
			needDrawTabArea = true;
		if (invOverlayInterfaceID != -1) {
			boolean flag1 = processInterfaceAnimation(cycleTimer,
					invOverlayInterfaceID);
			if (flag1)
				needDrawTabArea = true;
		}
		if (atInventoryInterfaceType == 2)
			needDrawTabArea = true;
		if (activeInterfaceType == 2)
			needDrawTabArea = true;
		if (needDrawTabArea) {
			if (clientSize == 0) {
				drawTabArea();
			}
			needDrawTabArea = false;
		}
		if (backDialogID == -1 && inputDialogState == 3) {
			int position = totalItemResults * 14 + 7;
			chatInterface.scrollPosition = itemResultScrollPos;
			if (super.mouseX > 478 && super.mouseX < 580
					&& super.mouseY > (clientHeight - 161)) {
				scrollInterface(494, 110, super.mouseX - 0, super.mouseY
						- (clientHeight - 155), chatInterface, 0, false,
						totalItemResults);
			}
			int scrollPosition = chatInterface.scrollPosition;
			if (scrollPosition < 0) {
				scrollPosition = 0;
			}
			if (scrollPosition > position - 110) {
				scrollPosition = position - 110;
			}
			if (itemResultScrollPos != scrollPosition) {
				itemResultScrollPos = scrollPosition;
				inputTaken = true;
			}
		}
		if (backDialogID == -1 && inputDialogState != 3) {
			chatInterface.scrollPosition = chatScrollHeight - chatScrollAmount - 110;
			if (super.mouseX > 478 && super.mouseX < 580
					&& super.mouseY > (clientHeight - 161))
				scrollInterface(494, 110, super.mouseX - 0, super.mouseY
						- (clientHeight - 155), chatInterface, 0, false,
						chatScrollHeight);
			int i = chatScrollHeight - 110 - chatInterface.scrollPosition;
			if (i < 0)
				i = 0;
			if (i > chatScrollHeight - 110)
				i = chatScrollHeight - 110;
			if (chatScrollAmount != i) {
				chatScrollAmount = i;
				inputTaken = true;
			}
		}
		if (backDialogID != -1) {
			boolean flag2 = processInterfaceAnimation(cycleTimer, backDialogID);
			if (flag2)
				inputTaken = true;
		}
		if (atInventoryInterfaceType == 3)
			inputTaken = true;
		if (activeInterfaceType == 3)
			inputTaken = true;
		if (notifyMessage != null)
			inputTaken = true;
		if (menuOpen && menuScreenArea == 2)
			inputTaken = true;
		if (inputTaken) {
			if (clientSize == 0) {
				drawChatArea();
				gameScreenIP.initDrawingArea();
			}
			inputTaken = false;
		}
		if (loadingStage == 2)
			try {
				renderWorld();
			} catch (Exception e) {
				e.printStackTrace();
			}
		if (loadingStage == 2) {
			if (clientSize == 0) {
				drawMinimap();
				mapAreaIP.drawGraphics(0, super.graphics, 765 - 246);
			}
		}
		if (flashingSidebarTab != -1)
			tabAreaAltered = true;
		if (tabAreaAltered) {
			if (flashingSidebarTab != -1 && flashingSidebarTab == tabID) {
				flashingSidebarTab = -1;
				stream.createFrame(120);
				stream.writeByte1(tabID);
			}
			tabAreaAltered = false;
			if (clientSize == 0)
				rightFrame.drawGraphics(3, super.graphics, 516);
			GraphicsBuffer_1125.initDrawingArea();
			gameScreenIP.initDrawingArea();
		}

		cycleTimer = 0;
	}

	private boolean buildFriendsListMenu(RSInterface class9) {
		int i = class9.contentType;
		if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
			if (i >= 801)
				i -= 701;
			else if (i >= 701)
				i -= 601;
			else if (i >= 101)
				i -= 101;
			else
				i--;
			menuActionName[menuActionRow] = "Remove <col=0xffffff>" + friendsList[i];
			menuActionID[menuActionRow] = 792;
			menuActionRow++;
			menuActionName[menuActionRow] = "Message <col=0xffffff>" + friendsList[i];
			menuActionID[menuActionRow] = 639;
			menuActionRow++;
			return true;
		}
		if (i >= 401 && i <= 500) {
			menuActionName[menuActionRow] = "Remove <col=0xffffff>" + class9.message;
			menuActionID[menuActionRow] = 322;
			menuActionRow++;
			return true;
		} else {
			return false;
		}
	}

	private void processStillGraphic() {
		StillGraphic stillGraphic = (StillGraphic) stillGraphicDeque.getFront();
		for (; stillGraphic != null; stillGraphic = (StillGraphic) stillGraphicDeque.getNext())
			if (stillGraphic.plane != plane || stillGraphic.animFinished)
				stillGraphic.unlink();
			else if (loopCycle >= stillGraphic.startTime) {
				stillGraphic.processAnimation(cycleTimer);
				if (stillGraphic.animFinished)
					stillGraphic.unlink();
				else
					worldController.addMutipleTileEntity(stillGraphic.plane, 0,
							stillGraphic.drawHeight, -1, stillGraphic.yTile,
							60, stillGraphic.xTile, stillGraphic, false);
			}

	}

	public void drawBlackBox(int xPos, int yPos) {
		DrawingArea.drawPixels(71, yPos - 1, xPos - 2, 0x726451, 1);
		DrawingArea.drawPixels(69, yPos, xPos + 174, 0x726451, 1);
		DrawingArea.drawPixels(1, yPos - 2, xPos - 2, 0x726451, 178);
		DrawingArea.drawPixels(1, yPos + 68, xPos, 0x726451, 174);
		DrawingArea.drawPixels(71, yPos - 1, xPos - 1, 0x2E2B23, 1);
		DrawingArea.drawPixels(71, yPos - 1, xPos + 175, 0x2E2B23, 1);
		DrawingArea.drawPixels(1, yPos - 1, xPos, 0x2E2B23, 175);
		DrawingArea.drawPixels(1, yPos + 69, xPos, 0x2E2B23, 175);
		DrawingArea.fillRectangle(0, yPos, 174, 68, 220, xPos);
	}

	private void drawInterface(int scrollOffset, int interfaceX, RSInterface rsInterface, int interfaceY) {
		if (rsInterface.type != 0 || rsInterface.children == null)
			return;
		if (rsInterface.interfaceShown && focusedViewportWidget != rsInterface.id
				&& focusedSidebarWidget != rsInterface.id && focusChatWidget != rsInterface.id
				&& rsInterface.id != 35555)
			return;
		int origTopX = DrawingArea.topX;
		int origTopY = DrawingArea.topY;
		int origBottomX = DrawingArea.bottomX;
		int origBottomY = DrawingArea.bottomY;
		DrawingArea.setDrawingArea(interfaceY + rsInterface.height, interfaceX,
				interfaceX + rsInterface.width, interfaceY);

		int totalChildrens = rsInterface.children.length;
		for (int childID = 0; childID < totalChildrens; childID++) {
			int childX = rsInterface.childX[childID] + interfaceX;
			int childY = (rsInterface.childY[childID] + interfaceY)
					- scrollOffset;
			RSInterface child = RSInterface.interfaceCache[rsInterface.children[childID]];
			childX += child.xOffset;
			childY += child.yOffset;
			if (child.contentType > 0)
				drawFriendsListOrWelcomeScreen(child);
			// here
			int[] IDs = { 1196, 1199, 1206, 1215, 1224, 1231, 1240, 1249, 1258,
					1267, 1274, 1283, 1573, 1290, 1299, 1308, 1315, 1324, 1333,
					1340, 1349, 1358, 1367, 1374, 1381, 1388, 1397, 1404, 1583,
					12038, 1414, 1421, 1430, 1437, 1446, 1453, 1460, 1469,
					15878, 1602, 1613, 1624, 7456, 1478, 1485, 1494, 1503,
					1512, 1521, 1530, 1544, 1553, 1563, 1593, 1635, 12426,
					12436, 12446, 12456, 6004, 18471,
					/* Ancients */
					12940, 12988, 13036, 12902, 12862, 13046, 12964, 13012,
					13054, 12920, 12882, 13062, 12952, 13000, 13070, 12912,
					12872, 13080, 12976, 13024, 13088, 12930, 12892, 13096 };
			for (int m5 = 0; m5 < IDs.length; m5++) {
				if (child.id == IDs[m5] + 1) {
					if (m5 > 61)
						drawBlackBox(childX + 1, childY);
					else
						drawBlackBox(childX, childY + 1);
				}
			}
			int[] runeChildren = { 1202, 1203, 1209, 1210, 1211, 1218, 1219,
					1220, 1227, 1228, 1234, 1235, 1236, 1243, 1244, 1245, 1252,
					1253, 1254, 1261, 1262, 1263, 1270, 1271, 1277, 1278, 1279,
					1286, 1287, 1293, 1294, 1295, 1302, 1303, 1304, 1311, 1312,
					1318, 1319, 1320, 1327, 1328, 1329, 1336, 1337, 1343, 1344,
					1345, 1352, 1353, 1354, 1361, 1362, 1363, 1370, 1371, 1377,
					1378, 1384, 1385, 1391, 1392, 1393, 1400, 1401, 1407, 1408,
					1410, 1417, 1418, 1424, 1425, 1426, 1433, 1434, 1440, 1441,
					1442, 1449, 1450, 1456, 1457, 1463, 1464, 1465, 1472, 1473,
					1474, 1481, 1482, 1488, 1489, 1490, 1497, 1498, 1499, 1506,
					1507, 1508, 1515, 1516, 1517, 1524, 1525, 1526, 1533, 1534,
					1535, 1547, 1548, 1549, 1556, 1557, 1558, 1566, 1567, 1568,
					1576, 1577, 1578, 1586, 1587, 1588, 1596, 1597, 1598, 1605,
					1606, 1607, 1616, 1617, 1618, 1627, 1628, 1629, 1638, 1639,
					1640, 6007, 6008, 6011, 8673, 8674, 12041, 12042, 12429,
					12430, 12431, 12439, 12440, 12441, 12449, 12450, 12451,
					12459, 12460, 15881, 15882, 15885, 18474, 18475, 18478 };
			for (int r = 0; r < runeChildren.length; r++)
				if (child.id == runeChildren[r])
					child.modelZoom = 775;
			if (child.type == 0) {
				if (child.scrollPosition > child.scrollMax - child.height)
					child.scrollPosition = child.scrollMax - child.height;
				if (child.scrollPosition < 0)
					child.scrollPosition = 0;
				drawInterface(child.scrollPosition, childX, child, childY);
				if (child.scrollMax > child.height)
					drawScrollbar(child.height, child.scrollPosition, childY,
							childX + child.width, child.scrollMax, false, false);
			} else if (child.type != 1)
				if (child.type == 2) {
					int spriteIndex = 0;
					for (int height = 0; height < child.height; height++) {
						for (int width = 0; width < child.width; width++) {
							int itemSpriteX = childX + width
									* (32 + child.invSpritePadX);
							int itemSpriteY = childY + height
									* (32 + child.invSpritePadY);
							if (spriteIndex < 20 || child.id > 20000) {
								itemSpriteX += child.spritesX[spriteIndex];
								itemSpriteY += child.spritesY[spriteIndex];
							}
							if (child.inv[spriteIndex] > 0) {
								int k6 = 0;
								int j7 = 0;
								int itemId = child.inv[spriteIndex] - 1;
								if (itemSpriteX > DrawingArea.topX - 32
										&& itemSpriteX < DrawingArea.bottomX
										&& itemSpriteY > DrawingArea.topY - 32
										&& itemSpriteY < DrawingArea.bottomY
										|| activeInterfaceType != 0
										&& dragFromSlot == spriteIndex) {
									int selectedColour = 0;
									if (itemSelected == 1
											&& lastItemSelectedSlot == spriteIndex
											&& lastItemSelectedInterface == child.id)
										selectedColour = 0xffffff;
									Sprite sprite_2 = ItemDef.getSprite(itemId, child.invStackSizes[spriteIndex], selectedColour);
									if (sprite_2 != null) {
										if (activeInterfaceType != 0
												&& dragFromSlot == spriteIndex
												&& focusedDragWidget == child.id) {
											k6 = super.mouseX - pressX;
											j7 = super.mouseY - pressY;
											if (k6 < 5 && k6 > -5)
												k6 = 0;
											if (j7 < 5 && j7 > -5)
												j7 = 0;
											if (dragCycle < 10) {
												k6 = 0;
												j7 = 0;
											}
											sprite_2.drawSprite1(itemSpriteX
													+ k6, itemSpriteY + j7);
											if (itemSpriteY + j7 < DrawingArea.topY
													&& rsInterface.scrollPosition > 0) {
												int i10 = (cycleTimer * (DrawingArea.topY
														- itemSpriteY - j7)) / 3;
												if (i10 > cycleTimer * 10)
													i10 = cycleTimer * 10;
												if (i10 > rsInterface.scrollPosition)
													i10 = rsInterface.scrollPosition;
												rsInterface.scrollPosition -= i10;
												pressY += i10;
											}
											if (itemSpriteY + j7 + 32 > DrawingArea.bottomY
													&& rsInterface.scrollPosition < rsInterface.scrollMax
															- rsInterface.height) {
												int j10 = (cycleTimer * ((itemSpriteY
														+ j7 + 32) - DrawingArea.bottomY)) / 3;
												if (j10 > cycleTimer * 10)
													j10 = cycleTimer * 10;
												if (j10 > rsInterface.scrollMax
														- rsInterface.height
														- rsInterface.scrollPosition)
													j10 = rsInterface.scrollMax
															- rsInterface.height
															- rsInterface.scrollPosition;
												rsInterface.scrollPosition += j10;
												pressY -= j10;
											}
										} else if (atInventoryInterfaceType != 0
												&& atInventoryIndex == spriteIndex
												&& atInventoryInterface == child.id)
											sprite_2.drawSprite1(itemSpriteX,
													itemSpriteY);
										else
											sprite_2.drawSprite(itemSpriteX,
													itemSpriteY);
										if (sprite_2.maxWidth == 33
												|| child.invStackSizes[spriteIndex] != 1) {
											int k10 = child.invStackSizes[spriteIndex];
											if (child.actions[0] != "[GE]"
													&& child.actions[1] != "[GE]"
													&& child.actions[2] != "[GE]"
													&& child.actions[3] != "[GE]"
													&& child.actions[4] != "[GE]") {
												smallText.drawString(0,
														intToKOrMil(k10),
														itemSpriteY + 10 + j7,
														itemSpriteX + 1 + k6);
												if (k10 > 99999
														&& k10 < 10000000)
													smallText.drawString(
															0xFFFFFF,
															intToKOrMil(k10),
															itemSpriteY + 9
																	+ j7,
															itemSpriteX + k6);
												else if (k10 > 9999999)
													smallText.drawString(
															0x00ff80,
															intToKOrMil(k10),
															itemSpriteY + 9
																	+ j7,
															itemSpriteX + k6);
												else
													smallText.drawString(
															0xFFFF00,
															intToKOrMil(k10),
															itemSpriteY + 9
																	+ j7,
															itemSpriteX + k6);
											}
										}
									}
								}
							} else if (child.sprites != null
									&& spriteIndex < 20) {
								Sprite sprite_1 = child.sprites[spriteIndex];
								if (sprite_1 != null)
									sprite_1.drawSprite(itemSpriteX,
											itemSpriteY);
							}
							spriteIndex++;
						}
					}
				} else if (child.type == 3) {
					boolean hover = false;
					if (focusChatWidget == child.id || focusedSidebarWidget == child.id
							|| focusedViewportWidget == child.id)
						hover = true;
					int color;
					if (interfaceIsSelected(child)) {
						color = child.enabledColor;
						if (hover && child.enabledMouseOverColor != 0)
							color = child.enabledMouseOverColor;
					} else {
						color = child.disabledColor;
						if (hover && child.disabledMouseOverColor != 0)
							color = child.disabledMouseOverColor;
					}
					if (child.opacity == 0) {
						if (child.filled)
							DrawingArea.drawPixels(child.height, childY,
									childX, color, child.width);
						else
							DrawingArea.fillPixels(childX, child.width,
									child.height, color, childY);
					} else if (child.filled)
						DrawingArea.fillRectangle(color, childY, child.width,
								child.height, 256 - (child.opacity & 0xff),
								childX);
					else
						DrawingArea.drawRectangle(childY, child.height,
								256 - (child.opacity & 0xff), color,
								child.width, childX);
				} else if (child.type == 4) {
					TypeFace textDrawingArea = child.textDrawingAreas;
					String s = child.message;
					boolean hovered = false;
					if (focusChatWidget == child.id || focusedSidebarWidget == child.id
							|| focusedViewportWidget == child.id)
						hovered = true;
					int color;
					if (interfaceIsSelected(child)) {
						color = child.enabledColor;
						if (hovered && child.enabledMouseOverColor != 0)
							color = child.enabledMouseOverColor;
						if (child.enabledMessage.length() > 0)
							s = child.enabledMessage;
					} else {
						color = child.disabledColor;
						if (hovered && child.disabledMouseOverColor != 0)
							color = child.disabledMouseOverColor;
					}
					if (child.atActionType == 6 && dialogueOptionsShowing) {
						s = "Please wait...";
						color = child.disabledColor;
					}
					if (chatAreaIP == null
							|| DrawingArea.width == chatAreaIP.width
							|| rsInterface.id == backDialogID) {
						if (color == 0xffff00)
							color = 255;
						if (color == 49152)
							color = 0xffffff;
					}
					if ((child.parentID == 1151) || (child.parentID == 12855)) {
						switch (color) {
						case 16773120:
							color = 0xFE981F;
							break;
						case 7040819:
							color = 0xAF6A1A;
							break;
						}
					}
					for (int l6 = childY + textDrawingArea.characterDefaultHeight; s
							.length() > 0; l6 += textDrawingArea.characterDefaultHeight) {
						if (s.indexOf("%") != -1) {
							do {
								int k7 = s.indexOf("%1");
								if (k7 == -1)
									break;
								if (child.id < 4000 || child.id > 5000
										&& child.id != 13921
										&& child.id != 13922
										&& child.id != 12171
										&& child.id != 12172)
									s = s.substring(0, k7)
											+ formatNumberToLetter(extractInterfaceValues(
													child, 0))
											+ s.substring(k7 + 2);
								else
									s = s.substring(0, k7)
											+ interfaceIntToString(extractInterfaceValues(
													child, 0))
											+ s.substring(k7 + 2);
							} while (true);
							do {
								int l7 = s.indexOf("%2");
								if (l7 == -1)
									break;
								s = s.substring(0, l7)
										+ interfaceIntToString(extractInterfaceValues(
												child, 1))
										+ s.substring(l7 + 2);
							} while (true);
							do {
								int i8 = s.indexOf("%3");
								if (i8 == -1)
									break;
								s = s.substring(0, i8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 2))
										+ s.substring(i8 + 2);
							} while (true);
							do {
								int j8 = s.indexOf("%4");
								if (j8 == -1)
									break;
								s = s.substring(0, j8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 3))
										+ s.substring(j8 + 2);
							} while (true);
							do {
								int k8 = s.indexOf("%5");
								if (k8 == -1)
									break;
								s = s.substring(0, k8)
										+ interfaceIntToString(extractInterfaceValues(
												child, 4))
										+ s.substring(k8 + 2);
							} while (true);
						}
						int l8 = s.indexOf("\\n");
						String s1;
						if (l8 != -1) {
							s1 = s.substring(0, l8);
							s = s.substring(l8 + 2);
						} else {
							s1 = s;
							s = "";
						}
						if (child.centerText)
							textDrawingArea.drawStringCenter(color, childX
									+ child.width / 2, s1, l6, child.shadowed);
						else
							textDrawingArea.drawShadowedString(child.shadowed,
									childX, color, s1, l6);
					}
				} else if (child.type == 5) {
					Sprite sprite;
					if (child.enabledSpriteId != -1 && child.enabledSprite == null
							&& SpriteCache.spriteCache[child.enabledSpriteId] == null) {
						onDemandFetcher.requestFileData(IMAGE_IDX - 1, child.enabledSpriteId);
					}
					if (child.disabledSpriteId != -1 && child.disabledSprite == null
							&& SpriteCache.spriteCache[child.disabledSpriteId] == null) {
						onDemandFetcher.requestFileData(IMAGE_IDX - 1, child.disabledSpriteId);
					}
					if (child.itemSpriteId1 != -1
							&& child.disabledSprite == null
							&& child.disabledSpriteId == -1
							&& child.enabledSpriteId == -1) {
						child.disabledSprite = ItemDef.getSprite(
								child.itemSpriteId1, 1,
								(child.itemSpriteZoom1 == -1) ? 0 : -1,
								child.itemSpriteZoom1);
						child.enabledSprite = ItemDef.getSprite(
								child.itemSpriteId2, 1,
								(child.itemSpriteZoom2 == -1) ? 0 : -1,
								child.itemSpriteZoom2);
						if (child.greyScale) {
							child.disabledSprite.greyScale();
						}
					}
					if (interfaceIsSelected(child) || hoverSpriteId == child.id)
						if (child.enabledSpriteId != -1 && child.enabledSprite == null
								&& SpriteCache.spriteCache[child.enabledSpriteId] != null)
							sprite = SpriteCache.spriteCache[child.enabledSpriteId];
						else
							sprite = child.enabledSprite;
					else if (child.disabledSpriteId != -1 && child.disabledSprite == null
							&& SpriteCache.spriteCache[child.disabledSpriteId] != null)
						sprite = SpriteCache.spriteCache[child.disabledSpriteId];
					else
						sprite = child.disabledSprite;
					if (child.id == 1164 || child.id == 1167
							|| child.id == 1170 || child.id == 1174
							|| child.id == 1540 || child.id == 1541
							|| child.id == 7455 || child.id == 18470
							|| child.id == 13035 || child.id == 13045
							|| child.id == 13053 || child.id == 13061
							|| child.id == 13069 || child.id == 13079
							|| child.id == 30064 || child.id == 30075
							|| child.id == 30083 || child.id == 30106
							|| child.id == 30114 || child.id == 30106
							|| child.id == 30170 || child.id == 13087
							|| child.id == 30162 || child.id == 13095)
						if (child.enabledSpriteId != -1 && child.enabledSprite == null
								&& SpriteCache.spriteCache[child.enabledSpriteId] != null)
							sprite = SpriteCache.spriteCache[child.enabledSpriteId];
						else
							sprite = child.enabledSprite;
					if (spellSelected == 1 && child.id == spellID
							&& spellID != 0 && sprite != null) {
						sprite.drawSprite(childX, childY, 0xffffff);
					} else {
						if (sprite != null) {
							if (child.type == 5) {
								if (child.advancedSprite) {
									sprite.drawAdvancedSprite(childX, childY);
								} else {
									sprite.drawSprite(childX, childY);
								}
							} else {
								sprite.drawSprite1(childX, childY,
										child.opacity);
							}
						}
					}
					if (Autocast && child.id == autocastId)
						SpriteCache.spriteCache[47].drawSprite(childX - 3,
								childY - 2);
				} else if (child.type == 6) {
					int k3 = Rasterizer.center_x;
					int j4 = Rasterizer.center_y;
					Rasterizer.center_x = childX + child.width / 2;
					Rasterizer.center_y = childY + child.height / 2;
					int i5 = Rasterizer.SINE[child.modelRotation1]
							* child.modelZoom >> 16;
					int l5 = Rasterizer.COSINE[child.modelRotation1]
							* child.modelZoom >> 16;
					boolean selected = interfaceIsSelected(child);
					int animId;
					if (selected)
						animId = child.enabledAnimationId;
					else
						animId = child.disabledAnimationId;
					Model model;
					if (animId == -1) {
						model = child.getAnimatedModel(-1, -1, selected);
					} else {
						Animation animation = Animation.anims[animId];
						model = child.getAnimatedModel(
								animation.frameIDs2[child.currentFrame],
								animation.frameIDs[child.currentFrame],
								selected);
					}
					if (model != null)
						model.renderSingle(child.modelRotation2, 0,
								child.modelRotation1, 0, i5, l5);
					// model.reset();
					// model = null;
					Rasterizer.center_x = k3;
					Rasterizer.center_y = j4;
				} else if (child.type == 7) {
					TypeFace textDrawingArea_1 = child.textDrawingAreas;
					int k4 = 0;
					for (int y = 0; y < child.height; y++) {
						for (int x = 0; x < child.width; x++) {
							if (child.inv[k4] > 0) {
								ItemDef itemDef = ItemDef
										.forID(child.inv[k4] - 1);
								String s2 = itemDef.name;
								if (itemDef.stackable
										|| child.invStackSizes[k4] != 1)
									s2 = s2
											+ " x"
											+ intToKOrMilLongName(child.invStackSizes[k4]);
								int i9 = childX + x
										* (115 + child.invSpritePadX);
								int k9 = childY + y
										* (12 + child.invSpritePadY);
								if (child.centerText)
									textDrawingArea_1.drawStringCenter(
											child.disabledColor, i9
													+ child.width / 2, s2, k9,
											child.shadowed);
								else
									textDrawingArea_1.drawShadowedString(
											child.shadowed, i9,
											child.disabledColor, s2, k9);
							}
							k4++;
						}
					}
				} else if (child.type == 8) {
					// if (interfaceIsSelected(child)) {
					// } else
					try {
						drawHoverBox(childX, childY, child.popupString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (child.type == 9) {
					Sprite sprite;
					if (interfaceIsSelected(child)) {
						sprite = child.enabledSprite;
					} else {
						sprite = child.disabledSprite;
					}
					if (sprite != null) {
						sprite.drawSpriteWithOpacity(childX, childY,
								child.opacity);
					}
				} else if (child.type == 10
						&& (anInt1500 == child.id || anInt1044 == child.id || anInt1129 == child.id)
						&& !menuOpen) {
					int boxWidth = 0;
					int boxHeight = 0;
					TypeFace textDrawingArea_2 = drawingArea;
					for (String s1 = child.message; s1.length() > 0;) {
						int l7 = s1.indexOf("\\n");
						String s4;
						if (l7 != -1) {
							s4 = s1.substring(0, l7);
							s1 = s1.substring(l7 + 2);
						} else {
							s4 = s1;
							s1 = "";
						}
						int j10 = textDrawingArea_2.getStringEffectsWidth(s4);
						if (j10 > boxWidth) {
							boxWidth = j10;
						}
						boxHeight += textDrawingArea_2.characterDefaultHeight + 1;
					}
					boxWidth += 6;
					boxHeight += 7;
					int xPos = (childX + child.width) - 5 - boxWidth;
					int yPos = childY + child.height + 5;
					if (xPos < childX + 5) {
						xPos = childX + 5;
					}
					if (xPos + boxWidth > interfaceX + rsInterface.width) {
						xPos = (interfaceX + rsInterface.width) - boxWidth;
					}
					if (yPos + boxHeight > interfaceY + rsInterface.height) {
						yPos = (interfaceY + rsInterface.height) - boxHeight;
					}
					if (clientSize != 0) {
						if (childX == clientWidth - 69
								|| childX == clientWidth - 131) {
							xPos -= (childX == clientWidth - 69) ? 100 : 20;
						}
						if (childY == clientHeight
								- (clientWidth <= smallTabs ? 112 : 75)) {
							yPos -= 100;
						}
						// System.out.println("ChildX = "+
						// childX+", childY = "+childY+", clientWidth = "+clientWidth+" , clientHeight = "+clientHeight);
					} else {
						if (child.inventoryHover) {
							if (xPos + boxWidth + interfaceX > 249) {
								xPos = 251 - boxWidth - interfaceX;
							}
							if (yPos + boxHeight + interfaceY > 261) {
								yPos = 245 - boxHeight - interfaceY;
							}
						}
					}
					DrawingArea.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0,
							boxWidth);
					DrawingArea.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
					String s2 = child.message;
					for (int j11 = yPos + textDrawingArea_2.characterDefaultHeight + 2; s2
							.length() > 0; j11 += textDrawingArea_2.characterDefaultHeight + 1) {
						int l11 = s2.indexOf("\\n");
						String s5;
						if (l11 != -1) {
							s5 = s2.substring(0, l11);
							s2 = s2.substring(l11 + 2);
						} else {
							s5 = s2;
							s2 = "";
						}
						textDrawingArea_2.drawShadowedString(false, xPos + 3, 0,
								s5, j11);
					}
				} else if (child.type == 11) {
					DrawingArea.fillRectangle(child.disabledColor, 0,
							clientWidth, clientHeight,
							256 - (child.opacity & 0xff), 0);
				}
		}
		DrawingArea
				.setDrawingArea(origBottomY, origTopX, origBottomX, origTopY);
	}

	private void readPlayerUpdateMask(int updateFlag, int j, Stream stream, Player player) {
		/*
		 * Player updating method
		 */
		if ((updateFlag & 0x400) != 0) {
			player.anInt1543 = stream.readByteS();
			player.anInt1545 = stream.readByteS();
			player.anInt1544 = stream.readByteS();
			player.anInt1546 = stream.readByteS();
			player.anInt1547 = stream.readWordBigEndian() + loopCycle;
			player.anInt1548 = stream.readShortA() + loopCycle;
			player.turnInfo = stream.readByteS();
			player.resetWalk();
		}
		if ((updateFlag & 0x100) != 0) {
			player.gfxId = stream.readLEShort();
			int k = stream.readDWord();
			player.graphicHeight = k >> 16;
			player.graphicDelay = loopCycle + (k & 0xffff);
			player.currentAnim = 0;
			player.animCycle = 0;
			if (player.graphicDelay > loopCycle)
				player.currentAnim = -1;
			if (player.gfxId == 65535)
				player.gfxId = -1;
			try {
				if (FrameReader.animationlist[Integer
						.parseInt(
								Integer.toHexString(
										SpotAnim.cache[player.gfxId].animation.frameIDs[0])
										.substring(
												0,
												Integer.toHexString(
														SpotAnim.cache[player.gfxId].animation.frameIDs[0])
														.length() - 4), 16)].length == 0)
					onDemandFetcher
							.requestFileData(
									1,
									Integer.parseInt(
											Integer.toHexString(
													SpotAnim.cache[player.gfxId].animation.frameIDs[0])
													.substring(
															0,
															Integer.toHexString(
																	SpotAnim.cache[player.gfxId].animation.frameIDs[0])
																	.length() - 4),
											16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if ((updateFlag & 8) != 0) {
			int requestAnim = stream.readLEShort();
			if (requestAnim == 65535)
				requestAnim = -1;
			int i2 = stream.readByteC();
			if (requestAnim == player.anim && requestAnim != -1) {
				int i3 = Animation.anims[requestAnim].delayType;
				if (i3 == 1) {
					player.currentAnimFrame = 0;
					player.frameCycle = 0;
					player.animationDelay = i2;
					player.anInt1530 = 0;
				}
				if (i3 == 2)
					player.anInt1530 = 0;
			} else if (requestAnim == -1
					|| player.anim == -1
					|| Animation.anims[requestAnim].forcedPriority >= Animation.anims[player.anim].forcedPriority) {
				player.anim = requestAnim;
				player.currentAnimFrame = 0;
				player.frameCycle = 0;
				player.animationDelay = i2;
				player.anInt1530 = 0;
				player.anInt1542 = player.pathLength;

				try {
					if (FrameReader.animationlist[Integer
							.parseInt(
									Integer.toHexString(
											Animation.anims[requestAnim].frameIDs[0])
											.substring(
													0,
													Integer.toHexString(
															Animation.anims[requestAnim].frameIDs[0])
															.length() - 4), 16)].length == 0)
						onDemandFetcher
								.requestFileData(
										1,
										Integer.parseInt(
												Integer.toHexString(
														Animation.anims[requestAnim].frameIDs[0])
														.substring(
																0,
																Integer.toHexString(
																		Animation.anims[requestAnim].frameIDs[0])
																		.length() - 4),
												16));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if ((updateFlag & 4) != 0) {
			player.textSpoken = stream.readString();
			if (player.textSpoken.charAt(0) == '~') {
				player.textSpoken = player.textSpoken.substring(1);
				pushMessage(player.textSpoken, 2, player.name);
			} else if (player == myPlayer)
				pushMessage(player.textSpoken, 2, player.name);
			player.chatColor = 0;
			player.chatEffect = 0;
			player.textCycle = 150;
		}
		if ((updateFlag & 0x80) != 0) {
			int effects = stream.readLEShort();
			int rights = stream.readUnsignedByte();
			int gloColor = stream.readUnsignedByte();
			int chatTextSize = stream.readByteC();
			int currentOffset = stream.currentOffset;
			if (player.name != null && player.visible) {
				long l3 = TextClass.longForName(player.name);
				boolean flag = false;
				if (rights <= 1) {
					for (int i4 = 0; i4 < ignoreCount; i4++) {
						if (ignoreListAsLongs[i4] != l3)
							continue;
						flag = true;
						break;
					}

				}
				if (!flag && isOnTutorialIsland == 0)
					try {
						textStream.currentOffset = 0;
						stream.readBytes_reverse(chatTextSize, 0, textStream.buffer);
						textStream.currentOffset = 0;
						String message = TextInput.decodeToString(chatTextSize, textStream);
						player.textSpoken = message;
						player.chatColor = effects >> 8;
						player.rights = rights;
						player.chatEffect = effects & 0xff;
						player.textCycle = 150;
						//System.out.println(rights + " - " + message);
						if(rights == 128)
							rights = -1;
						pushMessage(message, 2, getPrefix(rights) + (gloColor != 0 ? "@glo" + gloColor + "@" : "") + player.name);
					} catch (Exception exception) {
						signlink.reporterror("cde2");
					}
			}
			stream.currentOffset = currentOffset + chatTextSize;
		}
		if ((updateFlag & 1) != 0) {
			player.interactingEntity = stream.readLEShort();
			if (player.interactingEntity == 65535)
				player.interactingEntity = -1;
		}
		if ((updateFlag & 0x10) != 0) {
			int j1 = stream.readByteC();
			byte abyte0[] = new byte[j1];
			Stream stream_1 = new Stream(abyte0);
			stream.readBytes(j1, 0, abyte0);
			aStreamArray895s[j] = stream_1;
			player.updatePlayerAppearance(stream_1);
		}
		if ((updateFlag & 2) != 0) {
			player.anInt1538 = stream.readWordBigEndian();
			player.anInt1539 = stream.readLEShort();
		}
		if ((updateFlag & 0x20) != 0) {
			int hitdamage = stream.readUnsignedByte();
			if(getOption("damage_10"))
				hitdamage = hitdamage * 10;
			int hitmask = stream.readByteA();
			int icon = stream.readUnsignedByte();
			int soakDamage = stream.readUnsignedByte();
			player.updateHitData(hitmask, hitdamage, loopCycle, icon, soakDamage);
			player.loopCycleStatus = loopCycle + 300;
			player.constitution = player.currentHealth = stream.readByteC();
			player.maxConstitution = player.maxHealth = stream.readUnsignedByte();
			//System.out.println("Damage: " + hitdamage + " Type: " + hitmask + " Icon: " + icon + " Soak: " + soakDamage + " HP: " + player.currentHealth + " Max HP: " + player.maxHealth);
		}
		if ((updateFlag & 0x200) != 0) {
			int hitdamage = stream.readUnsignedByte();
			if(getOption("damage_10"))
				hitdamage = hitdamage * 10;
			int hitmask = stream.readByteS();
			int icon = stream.readUnsignedByte();
			int soakDamage = stream.readUnsignedByte();
			player.updateHitData(hitmask, hitdamage, loopCycle, icon, soakDamage);
			player.loopCycleStatus = loopCycle + 300; 
			player.constitution = player.currentHealth = stream.readUnsignedByte();
			player.maxConstitution = player.maxHealth = stream.readByteC();
			//System.out.println("Damage2: " + hitdamage + " Type: " + hitmask + " Icon: " + icon + " Soak: " + soakDamage + " HP: " + player.currentHealth + " Max HP: " + player.maxHealth);
		}
	}

	private void handleArrowKeys() {
		try {
			int j = myPlayer.x + cameraOffsetX;
			int k = myPlayer.y + cameraOffsetY;
			if (chaseCameraX - j < -500 || chaseCameraX - j > 500
					|| chaseCameraY - k < -500 || chaseCameraY - k > 500) {
				chaseCameraX = j;
				chaseCameraY = k;
			}
			if (chaseCameraX != j)
				chaseCameraX += (j - chaseCameraX) / 16;
			if (chaseCameraY != k)
				chaseCameraY += (k - chaseCameraY) / 16;
			if (super.keyArray[1] == 1)
				cameraYawTranslate += (-24 - cameraYawTranslate) / 2;
			else if (super.keyArray[2] == 1)
				cameraYawTranslate += (24 - cameraYawTranslate) / 2;
			else
				cameraYawTranslate /= 2;
			if (super.keyArray[3] == 1)
				cameraPitchTranslate += (12 - cameraPitchTranslate) / 2;
			else if (super.keyArray[4] == 1)
				cameraPitchTranslate += (-12 - cameraPitchTranslate) / 2;
			else
				cameraPitchTranslate /= 2;
			viewRotation = viewRotation + cameraYawTranslate / 2 & 0x7ff;
			chaseCameraPitch += cameraPitchTranslate / 2;
			if (chaseCameraPitch < 128)
				chaseCameraPitch = 128;
			if (chaseCameraPitch > 383)
				chaseCameraPitch = 383;
			int l = chaseCameraX >> 7;
			int i1 = chaseCameraY >> 7;
			int j1 = getFloorDrawHeight(plane, chaseCameraY, chaseCameraX);
			int k1 = 0;
			if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
				for (int l1 = l - 4; l1 <= l + 4; l1++) {
					for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
						int l2 = plane;
						if (l2 < 3 && (tileSettingBits[1][l1][k2] & 2) == 2)
							l2++;
						int i3 = j1 - intGroundArray[l2][l1][k2];
						if (i3 > k1)
							k1 = i3;
					}

				}

			}
			anInt1005++;
			if (anInt1005 > 1512) {
				anInt1005 = 0;
				stream.createFrame(77);
				stream.writeByte1(0);
				int i2 = stream.currentOffset;
				stream.writeByte1((int) (Math.random() * 256D));
				stream.writeByte1(101);
				stream.writeByte1(233);
				stream.writeWord(45092);
				if ((int) (Math.random() * 2D) == 0)
					stream.writeWord(35784);
				stream.writeByte1((int) (Math.random() * 256D));
				stream.writeByte1(64);
				stream.writeByte1(38);
				stream.writeWord((int) (Math.random() * 65536D));
				stream.writeWord((int) (Math.random() * 65536D));
				stream.writeBytes(stream.currentOffset - i2);
			}
			int j2 = k1 * 192;
			if (j2 > 0x17f00)
				j2 = 0x17f00;
			if (j2 < 32768)
				j2 = 32768;
			if (j2 > minCameraPitch) {
				minCameraPitch += (j2 - minCameraPitch) / 24;
				return;
			}
			if (j2 < minCameraPitch) {
				minCameraPitch += (j2 - minCameraPitch) / 80;
			}
		} catch (Exception _ex) {
			signlink.reporterror("glfc_ex " + myPlayer.x + "," + myPlayer.y
					+ "," + chaseCameraX + "," + chaseCameraY + "," + currentRegionX
					+ "," + currentRegionY + "," + baseX + "," + baseY);
			throw new RuntimeException("eek");
		}
	}

	public void processDrawing() {
		if (rsAlreadyLoaded || loadingError || genericLoadingError) {
			showErrorScreen();
			return;
		}
		drawCycle++;
		if (!loggedIn) {
			drawLoginScreen(false);
		} else {
			drawGameScreen();
		}
		clickCycle = 0;
	}

	private boolean isFriendOrSelf(String s) {
		if (s == null)
			return false;
		s = s.substring(s.indexOf(">") + 1);
		for (int i = 0; i < friendsCount; i++)
			if (s.equalsIgnoreCase(friendsList[i]))
				return true;
		return s.equalsIgnoreCase(myPlayer.name);
	}

	private static String combatDiffColor(int i, int j) {
		int k = i - j;
		if (k < -9)
			return "@red@";
		if (k < -6)
			return "@or3@";
		if (k < -3)
			return "@or2@";
		if (k < 0)
			return "@or1@";
		if (k > 9)
			return "@gre@";
		if (k > 6)
			return "@gr3@";
		if (k > 3)
			return "@gr2@";
		if (k > 0)
			return "@gr1@";
		else
			return "@yel@";
	}

	private void setWaveVolume(int i) {
		signlink.wavevol = i;
	}

	private boolean drawPane = false;

	private void draw3dScreen() {

		if (screenOpacity != 0 && screenOpacity != 255) {
			RSInterface.interfaceCache[35556].width = clientWidth;
			RSInterface.interfaceCache[35556].height = clientHeight;
			RSInterface.interfaceCache[35556].opacity = (byte) screenOpacity;
			drawInterface(0, 0, RSInterface.interfaceCache[35555], 0);
			drawInterface(0, 512, RSInterface.interfaceCache[35555], 0);
			drawInterface(0, 512 * 2, RSInterface.interfaceCache[35555], 0);
			drawInterface(0, 512 * 3, RSInterface.interfaceCache[35555], 0);
			drawInterface(0, 0, RSInterface.interfaceCache[35555], 700);
			drawInterface(0, 512, RSInterface.interfaceCache[35555], 700);
			drawInterface(0, 512 * 2, RSInterface.interfaceCache[35555], 700);
			drawInterface(0, 512 * 3, RSInterface.interfaceCache[35555], 700);
		}
		processAnAnim();
		if (showChat)
			drawSplitPrivateChat();
		if (crossType == 1) {
			crosses[crossIndex / 100]
					.drawSprite(crossX - 8 - 4, crossY - 8 - 4);
			anInt1142++;
			if (anInt1142 > 67) {
				anInt1142 = 0;
				stream.createFrame(78);
			}
		}
		if (crossType == 2)
			crosses[4 + crossIndex / 100].drawSprite(crossX - 8 - 4,
					crossY - 8 - 4);
		if (clientSize != 0 && (walkableInterfaceId == 21119 || walkableInterfaceId == 21100)) {
			processInterfaceAnimation(cycleTimer, walkableInterfaceId);
			drawInterface(0, 0, RSInterface.interfaceCache[walkableInterfaceId], 0);
		} else if (walkableInterfaceId != -1) {
			processInterfaceAnimation(cycleTimer, walkableInterfaceId);
			drawInterface(0, clientSize == 0 ? 0 : (clientWidth / 2) - 256, RSInterface.interfaceCache[walkableInterfaceId], 
					clientSize == 0 ? 0 : (clientHeight / 2) - 167);
		}
		if (openInterfaceID != -1) {
			processInterfaceAnimation(cycleTimer, openInterfaceID);
			drawInterface(0, clientSize == 0 ? 0 : (clientWidth / 2) - 256,
					RSInterface.interfaceCache[openInterfaceID],
					clientSize == 0 ? 0 : (clientHeight / 2) - 167);
		}
		checkTutorialIsland();
		drawGrandExchange();
		if (!menuOpen) {
			processRightClick();
			drawTooltip();
		} else if (menuScreenArea == 0)
			drawMenu();
		if (drawMultiwayIcon == 1) {
			multiOverlay.drawSprite(clientSize == 0 ? 472 : clientWidth - 230,
					clientSize == 0 ? 296 : clientHeight - 100);
		}
		if (fpsOn) {
			char c = '\u01FB';
			int k = 20;
			int i1 = 0xffff00;
			if (super.fps < 15)
				i1 = 0xff0000;
			drawingArea.drawStringLeft("Fps:" + super.fps, c, i1, k);
			k += 15;
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			i1 = 0xffff00;
			if (j1 > 0x2000000 && lowMem)
				i1 = 0xff0000;
			drawingArea.drawStringLeft("Mem:" + j1 + "k", c, 0xffff00, k);
			k += 15;
			drawingArea.drawString(0xffff00, "Mouse X: " + super.mouseX + " , Mouse Y: " + super.mouseY, 314, 5);
		}
		int x = baseX + (myPlayer.x - 6 >> 7);
		int y = baseY + (myPlayer.y - 6 >> 7);
		if (clientData) {
			int minus = 45;
			if (super.fps < 15) {

			}
			// memGraph.draw(30, 50);
			// fpsGraph.addValue(super.fps);
			// fpsGraph.draw(30, 350);

			drawingArea.drawString(0xffff00, "Input Length: " + inputString.length(), 230 - minus, 5);
			drawingArea.drawString(0xffff00, "Tab ID: " + tabID, 255 - minus, 5);
			drawingArea.drawString(0xffff00, "Floor Tile: " + currentTileId, 270 - minus, 5);
			drawingArea.drawString(0xffff00, "Fps: " + super.fps, 285 - minus, 5);
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);

			drawingArea.drawString(0xffff00, "Mem: " + j1 + "k", 299 - minus, 5);
			drawingArea.drawString(0xffff00, "Mouse X: " + super.mouseX
					+ " , Mouse Y: " + super.mouseY, 314 - minus, 5);
			drawingArea.drawString(0xffff00, "Coords: " + x + ", " + y + " "
					+ myPlayer.x + " " + myPlayer.y, 329 - minus, 5);
			drawingArea.drawString(0xffff00, "Client resolution: " + clientWidth
					+ "x" + clientHeight, 344 - minus, 5);
			drawingArea.drawString(0xffff00, "Object Maps: " + objectMaps, 359 - minus, 5);
			drawingArea.drawString(0xffff00, "Floor Maps: " + floorMaps, 374 - minus, 5);
		}
		if (updateMinutes != 0) {
			int j = updateMinutes / 50;
			int l = j / 60;
			j %= 60;
			if (j < 10)
				drawingArea.drawString(0xffff00, "System update in: " + l + ":0" + j, 329, 4);
			else
				drawingArea.drawString(0xffff00, "System update in: " + l + ":" + j, 329, 4);
			systemUpdateCycle++;
			if (systemUpdateCycle > 75) {
				systemUpdateCycle = 0;
				stream.createFrame(148);
			}
		}
	}

	private void addIgnore(long l) {
		try {
			if (l == 0L)
				return;
			if (ignoreCount >= 100) {
				pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			String s = TextClass.fixName(TextClass.nameForLong(l));
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					pushMessage(s + " is already on your ignore list", 0, "");
					return;
				}
			for (int k = 0; k < friendsCount; k++)
				if (friendsListAsLongs[k] == l) {
					pushMessage("Please remove " + s
							+ " from your friend list first", 0, "");
					return;
				}

			ignoreListAsLongs[ignoreCount++] = l;
			needDrawTabArea = true;
			stream.createFrame(133);
			stream.writeQWord(l);
			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("45688, " + l + ", " + 4 + ", "
					+ runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void updatePlayerInstances() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = myPlayerIndex;
			else
				j = playerIndices[i];
			Player player = playerArray[j];
			if (player != null)
				entityUpdateBlock(player);
		}

	}

	private void updateSpawnedObjects() {
		if (loadingStage == 2) {
			for (GameObjectSpawnRequest request = (GameObjectSpawnRequest) objectSpawnDeque
					.getFront(); request != null; request = (GameObjectSpawnRequest) objectSpawnDeque
					.getNext()) {
				if (request.removeTime > 0)
					request.removeTime--;
				if (request.removeTime == 0) {
					if (request.objectId < 0 || Region.isObjectModelCached(request.objectId, request.type)) {
						addRequestedObject(request.tileY, request.plane,
								request.face, request.type, request.tileX, request.objectType, request.objectId);
						request.unlink();
					}
				} else {
					if (request.spawnTime > 0)
						request.spawnTime--;
					if (request.spawnTime == 0
							&& request.tileX >= 1
							&& request.tileY >= 1
							&& request.tileX <= 102
							&& request.tileY <= 102
							&& (request.currentIDRequested < 0 || Region.isObjectModelCached(request.currentIDRequested, request.currentTypeRequested))) {
						addRequestedObject(request.tileY, request.plane, request.currentFaceRequested,
								request.currentTypeRequested, request.tileX, request.objectType, request.currentIDRequested);
						request.spawnTime = -1;
						if (request.currentIDRequested == request.objectId && request.objectId == -1)
							request.unlink();
						else if (request.currentIDRequested == request.objectId && request.currentFaceRequested == request.face && request.currentTypeRequested == request.type)
							request.unlink();
					}
				}
			}

		}
	}

	private void determineMenuSize() {
		int i = newBoldFont/*.getStringEffectsWidth("Choose Option")*/.getTextWidth("Choose Option");
		for (int j = 0; j < menuActionRow; j++) {
			int k = newBoldFont./*boldFont.getStringEffectsWidth(menuActionName[j])*/getTextWidth(menuActionName[j]);
			//System.out.println(menuActionName[j] + " -- width: " + k);
			if (k > i)
				i = k;
		}
		i += 8;
		int l = 15 * menuActionRow + 21;
		if (clientSize == 0) {
			if (super.saveClickX > 4 && super.saveClickY > 4
					&& super.saveClickX < 516 && super.saveClickY < 338) {
				int i1 = super.saveClickX - 4 - i / 2;
				if (i1 + i > 512)
					i1 = 512 - i;
				if (i1 < 0)
					i1 = 0;
				int l1 = super.saveClickY - 4;
				if (l1 + l > 334)
					l1 = 334 - l;
				if (l1 < 0)
					l1 = 0;
				menuOpen = true;
				menuScreenArea = 0;
				menuOffsetX = i1;
				menuOffsetY = l1;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
			if (super.saveClickX > 519 && super.saveClickY > 168
					&& super.saveClickX < 765 && super.saveClickY < 503) {
				int j1 = super.saveClickX - 519 - i / 2;
				if (j1 < 0)
					j1 = 0;
				else if (j1 + i > 245)
					j1 = 245 - i;
				int i2 = super.saveClickY - 168;
				if (i2 < 0)
					i2 = 0;
				else if (i2 + l > 333)
					i2 = 333 - l;
				menuOpen = true;
				menuScreenArea = 1;
				menuOffsetX = j1;
				menuOffsetY = i2;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
			if (super.saveClickX > 0 && super.saveClickY > 338
					&& super.saveClickX < 516 && super.saveClickY < 503) {
				int k1 = super.saveClickX - 0 - i / 2;
				if (k1 < 0)
					k1 = 0;
				else if (k1 + i > 516)
					k1 = 516 - i;
				int j2 = super.saveClickY - 338;
				if (j2 < 0)
					j2 = 0;
				else if (j2 + l > 165)
					j2 = 165 - l;
				menuOpen = true;
				menuScreenArea = 2;
				menuOffsetX = k1;
				menuOffsetY = j2;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
			// if(super.saveClickX > 0 && super.saveClickY > 338 &&
			// super.saveClickX < 516 && super.saveClickY < 503) {
			if (super.saveClickX > 519 && super.saveClickY > 0
					&& super.saveClickX < 765 && super.saveClickY < 168) {
				int j1 = super.saveClickX - 519 - i / 2;
				if (j1 < 0)
					j1 = 0;
				else if (j1 + i > 245)
					j1 = 245 - i;
				int i2 = super.saveClickY - 0;
				if (i2 < 0)
					i2 = 0;
				else if (i2 + l > 168)
					i2 = 168 - l;
				menuOpen = true;
				menuScreenArea = 3;
				menuOffsetX = j1;
				menuOffsetY = i2;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
		} else {
			if (super.saveClickX > 0 && super.saveClickY > 0
					&& super.saveClickX < clientWidth
					&& super.saveClickY < clientHeight) {
				int i1 = super.saveClickX - 0 - i / 2;
				if (i1 + i > clientWidth)
					i1 = clientWidth - i;
				if (i1 < 0)
					i1 = 0;
				int l1 = super.saveClickY - 0;
				if (l1 + l > clientHeight)
					l1 = clientHeight - l;
				if (l1 < 0)
					l1 = 0;
				menuOpen = true;
				menuScreenArea = 0;
				menuOffsetX = i1;
				menuOffsetY = l1;
				menuWidth = i;
				menuHeight = 15 * menuActionRow + 22;
			}
		}
	}

	private void updatePlayerMovement(Stream stream) {
		stream.initBitAccess();
		int j = stream.readBits(1);
		if (j == 0)
			return;
		int k = stream.readBits(2);
		if (k == 0) {
			playersToUpdate[playersToUpdateCount++] = myPlayerIndex;
			return;
		}
		if (k == 1) {
			int l = stream.readBits(3);
			myPlayer.moveInDir(false, l);
			int k1 = stream.readBits(1);
			if (k1 == 1)
				playersToUpdate[playersToUpdateCount++] = myPlayerIndex;
			return;
		}
		if (k == 2) {
			int i1 = stream.readBits(3);
			myPlayer.moveInDir(true, i1);
			int l1 = stream.readBits(3);
			myPlayer.moveInDir(true, l1);
			int j2 = stream.readBits(1);
			if (j2 == 1)
				playersToUpdate[playersToUpdateCount++] = myPlayerIndex;
			return;
		}
		if (k == 3) {
			plane = stream.readBits(2);
			int j1 = stream.readBits(1);
			int i2 = stream.readBits(1);
			if (i2 == 1)
				playersToUpdate[playersToUpdateCount++] = myPlayerIndex;
			int k2 = stream.readBits(7);
			int l2 = stream.readBits(7);
			myPlayer.setPos(l2, k2, j1 == 1);
		}
	}

	private void nullLoader() {
	}

	private boolean processInterfaceAnimation(int i, int interfaceId) {
		boolean flag1 = false;
		RSInterface rsi = RSInterface.interfaceCache[interfaceId];
		if (rsi == null)
			return false;
		if (rsi.children == null)
			return false;
		for (int childId = 0; childId < rsi.children.length; childId++) {
			if (rsi.children[childId] == -1)
				break;
			RSInterface child = RSInterface.interfaceCache[rsi.children[childId]];
			if (child.type == 1)
				flag1 |= processInterfaceAnimation(i, child.id);
			if (child.type == 6
					&& (child.disabledAnimationId != -1 || child.enabledAnimationId != -1)) {
				boolean flag2 = interfaceIsSelected(child);
				int l;
				if (flag2)
					l = child.enabledAnimationId;
				else
					l = child.disabledAnimationId;
				if (l != -1) {
					Animation animation = Animation.anims[l];
					for (child.frameTimer += i; child.frameTimer > animation
							.getFrameLength(child.currentFrame);) {
						child.frameTimer -= animation
								.getFrameLength(child.currentFrame) + 1;
						child.currentFrame++;
						if (child.currentFrame >= animation.frameCount) {
							child.currentFrame -= animation.loopDelay;
							if (child.currentFrame < 0
									|| child.currentFrame >= animation.frameCount)
								child.currentFrame = 0;
						}
						flag1 = true;
					}

				}
			}
		}

		return flag1;
	}

	private int getCameraHeight() {
		int j = 3;
		if (yCameraCurve < 310) {
			int k = xCameraPos >> 7;
			int l = yCameraPos >> 7;
			int i1 = myPlayer.x >> 7;
			int j1 = myPlayer.y >> 7;
			if ((tileSettingBits[plane][k][l] & 4) != 0)
				j = plane;
			int k1;
			if (i1 > k)
				k1 = i1 - k;
			else
				k1 = k - i1;
			int l1;
			if (j1 > l)
				l1 = j1 - l;
			else
				l1 = l - j1;
			if (k1 > l1) {
				int i2 = (l1 * 0x10000) / k1;
				int k2 = 32768;
				while (k != i1) {
					if (k < i1)
						k++;
					else if (k > i1)
						k--;
					if ((tileSettingBits[plane][k][l] & 4) != 0)
						j = plane;
					k2 += i2;
					if (k2 >= 0x10000) {
						k2 -= 0x10000;
						if (l < j1)
							l++;
						else if (l > j1)
							l--;
						if ((tileSettingBits[plane][k][l] & 4) != 0)
							j = plane;
					}
				}
			} else {
				int j2 = (k1 * 0x10000) / l1;
				int l2 = 32768;
				while (l != j1) {
					if (l < j1)
						l++;
					else if (l > j1)
						l--;
					if ((tileSettingBits[plane][k][l] & 4) != 0)
						j = plane;
					l2 += j2;
					if (l2 >= 0x10000) {
						l2 -= 0x10000;
						if (k < i1)
							k++;
						else if (k > i1)
							k--;
						if ((tileSettingBits[plane][k][l] & 4) != 0)
							j = plane;
					}
				}
			}
		}
		if ((tileSettingBits[plane][myPlayer.x >> 7][myPlayer.y >> 7] & 4) != 0)
			j = plane;
		return j;
	}

	private int getCamHeight() {
		int j = getFloorDrawHeight(plane, yCameraPos, xCameraPos);
		if (j - zCameraPos < 800
				&& (tileSettingBits[plane][xCameraPos >> 7][yCameraPos >> 7] & 4) != 0)
			return plane;
		else
			return 3;
	}

	private void delIgnore(long l) {
		try {
			if (l == 0L)
				return;
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					ignoreCount--;
					needDrawTabArea = true;
					System.arraycopy(ignoreListAsLongs, j + 1,
							ignoreListAsLongs, j, ignoreCount - j);

					stream.createFrame(74);
					stream.writeQWord(l);
					return;
				}

			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("47229, " + 3 + ", " + l + ", "
					+ runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private void chatJoin(long l) {
		try {
			if (l == 0L)
				return;
			stream.createFrame(60);
			stream.writeQWord(l);
			return;
		} catch (RuntimeException runtimeexception) {
			signlink.reporterror("47229, " + 3 + ", " + l + ", "
					+ runtimeexception.toString());
		}
		throw new RuntimeException();

	}

	public String getParameter(String s) {
		if (signlink.mainapp != null)
			return signlink.mainapp.getParameter(s);
		else
			return super.getParameter(s);
	}

	private int extractInterfaceValues(RSInterface rsi, int j) {
		if (rsi.valueIndexArray == null || j >= rsi.valueIndexArray.length)
			return -2;
		try {
			int myValues[] = rsi.valueIndexArray[j];
			int k = 0;
			int valueIdx = 0;
			int i1 = 0;
			do {
				int checkType = myValues[valueIdx++];
				int returnValue = 0;
				byte byte0 = 0;
				if (checkType == 0)
					return k;
				if (checkType == 1)
					returnValue = currentStats[myValues[valueIdx++]];
				if (checkType == 2)
					returnValue = currentMaxStats[myValues[valueIdx++]];
				if (checkType == 3)
					returnValue = currentExp[myValues[valueIdx++]];
				if (checkType == 4) {
					RSInterface interfaceToCheckOn = RSInterface.interfaceCache[myValues[valueIdx++]];
					int itemId = myValues[valueIdx++];
					for (int j3 = 0; j3 < interfaceToCheckOn.inv.length; j3++)
						if (interfaceToCheckOn.inv[j3] == itemId + 1)
							returnValue += interfaceToCheckOn.invStackSizes[j3];
				}
				/**
				 * Returns corresponding varsettings
				 */
				if (checkType == 5)
					returnValue = variousSettings[myValues[valueIdx++]];
				/**
				 * Returns your xp in given skill
				 */
				if (checkType == 6)
					returnValue = levelXPs[currentMaxStats[myValues[valueIdx++]] - 1];
				/**
				 * Checks if something is higher than 46874
				 */
				if (checkType == 7)
					returnValue = (variousSettings[myValues[valueIdx++]] * 100) / 46875;
				/**
				 * Gets combat level
				 */
				if (checkType == 8)
					returnValue = myPlayer.combatLevel;
				/**
				 * Gets total level
				 */
				if (checkType == 9) {
					for (int l1 = 0; l1 < Skills.skillsCount; l1++)
						if (Skills.skillEnabled[l1])
							returnValue += currentMaxStats[l1];

				}
				/**
				 * Checks if the itemid is along the items shown in the
				 * interface
				 */
				if (checkType == 10) {
					RSInterface itemInterface = RSInterface.interfaceCache[myValues[valueIdx++]];
					int itemId = myValues[valueIdx++] + 1;
					for (int k3 = 0; k3 < itemInterface.inv.length; k3++) {
						if (itemInterface.inv[k3] != itemId)
							continue;
						returnValue = 0x3b9ac9ff;
						break;
					}
				}
				/**
				 * Looks up run energy
				 */
				if (checkType == 11)
					returnValue = currentEnergy;
				/**
				 * Looks up weight
				 */
				if (checkType == 12)
					returnValue = weight;
				if (checkType == 13) {
					int required = variousSettings[myValues[valueIdx++]];
					int current = myValues[valueIdx++];
					returnValue = (required & 1 << current) == 0 ? 0 : 1;
				}
				if (checkType == 14) {
					int j2 = myValues[valueIdx++];
					VarBit varBit = VarBit.cache[j2];
					int l3 = varBit.configId;
					int i4 = varBit.leastSignificantBit;
					int j4 = varBit.mostSignificantBit;
					int k4 = bit_mask[j4 - i4];
					returnValue = variousSettings[l3] >> i4 & k4;
				}
				if (checkType == 15)
					byte0 = 1;
				if (checkType == 16)
					byte0 = 2;
				if (checkType == 17)
					byte0 = 3;
				if (checkType == 18)
					returnValue = (myPlayer.x >> 7) + baseX;
				if (checkType == 19)
					returnValue = (myPlayer.y >> 7) + baseY;
				if (checkType == 20)
					returnValue = myValues[valueIdx++];
				if (byte0 == 0) {
					if (i1 == 0)
						k += returnValue;
					if (i1 == 1)
						k -= returnValue;
					if (i1 == 2 && returnValue != 0)
						k /= returnValue;
					if (i1 == 3)
						k *= returnValue;
					i1 = 0;
				} else {
					i1 = byte0;
				}
			} while (true);
		} catch (Exception _ex) {
			return -1;
		}
	}

	public void drawTooltip() {
		if (menuActionRow < 2 && itemSelected == 0 && spellSelected == 0) {
			return;
		}
		String s;
		if (itemSelected == 1 && menuActionRow < 2)
			s = "Use " + selectedItemName + " with...";
		else if (spellSelected == 1 && menuActionRow < 2)
			s = spellTooltip + "...";
		else
			s = menuActionName[menuActionRow - 1];
		if (!s.contains("Walk here") && getOption("tooltip_hover")) {
			drawHoverBox(super.mouseX + 10, super.mouseY - 10, 0x101010, 0xFFFFFF, s);
		}
		if (menuActionRow > 2)
			s = s + "@whi@ / " + (menuActionRow - 2) + " more options";
		//chatTextDrawingArea.drawShadowedSeededAlphaString(4, 0xffffff, s, loopCycle / 1000, 15);
		//chatTextDrawingArea.drawShadowedSeededAlphaString(x, j, s, k, y);
		newBoldFont.drawBasicString(s, 4, 15, 0xffffff, 0x000000);
	}

	private void npcScreenPos(Entity entity, int i) {
		calcEntityScreenPos(entity.x, i, entity.y);
	}

	private void calcEntityScreenPos(int x, int j, int y) {
		if (x < 128 || y < 128 || x > 13056 || y > 13056) {
			spriteDrawX = -1;
			spriteDrawY = -1;
			return;
		}
		int height = getFloorDrawHeight(plane, y, x) - j;
		x -= xCameraPos;
		height -= zCameraPos;
		y -= yCameraPos;
		int j1 = Model.SINE[yCameraCurve];
		int k1 = Model.COSINE[yCameraCurve];
		int l1 = Model.SINE[xCameraCurve];
		int i2 = Model.COSINE[xCameraCurve];
		int j2 = y * l1 + x * i2 >> 16;
		y = y * i2 - x * l1 >> 16;
		x = j2;
		j2 = height * k1 - y * j1 >> 16;
		y = height * j1 + y * k1 >> 16;
		height = j2;
		if (y >= 50) {
			spriteDrawX = Rasterizer.center_x + (x << Client.log_view_dist) / y;
			spriteDrawY = Rasterizer.center_y + (height << Client.log_view_dist)
					/ y;
		} else {
			spriteDrawX = -1;
			spriteDrawY = -1;
		}
	}
	
	public int objectDrawX = 0;
	public int objectDrawY = 0;
	public  void calcObjectScreenPos(int x, int height, int y) {
		if (x < 128 || y < 128 || x > 13056 || y > 13056) {
			objectDrawX = -1;
			objectDrawY = -1;
			return;
		}
		int z = getFloorDrawHeight(plane, y, x) - height;
		x -= xCameraPos;
		z -= zCameraPos;
		y -= yCameraPos;
		int j1 = Model.SINE[yCameraCurve];
		int k1 = Model.COSINE[yCameraCurve];
		int l1 = Model.SINE[xCameraCurve];
		int i2 = Model.COSINE[xCameraCurve];
		int j2 = y * l1 + x * i2 >> 16;
		y = y * i2 - x * l1 >> 16;
		x = j2;
		j2 = z * k1 - y * j1 >> 16;
		y = z * j1 + y * k1 >> 16;
		z = j2;
		if (y >= 50) {
			objectDrawX = Rasterizer.center_x + (x << Client.log_view_dist) / y;
			objectDrawY = Rasterizer.center_y + (z << Client.log_view_dist)
					/ y;
		} else {
			objectDrawX = -1;
			objectDrawY = -1;
		}
	}

	private void buildSplitPrivateChatMenu() {
		if (splitPrivateChat == 0)
			return;
		int i = 0;
		if (updateMinutes != 0)
			i = 1;
		for (int chatIndex = 0; chatIndex < 100; chatIndex++)
			if (chatMessages[chatIndex] != null) {
				int type = chatTypes[chatIndex];
				String name = chatNames[chatIndex];
				if (name != null && name.indexOf("<col=0xffffff>") == 0) {
					name = name.substring(14);
				}
				if ((type == 3 || type == 7)
						&& (type == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(name))) {
					int height = (clientHeight - 174) - i * 13;
					if (super.mouseX > (clientSize == 0 ? 4 : 0) 
							&& super.mouseY - (clientSize == 0 ? 4 : 0) > height - 10
							&& super.mouseY - (clientSize == 0 ? 4 : 0) <= height + 3) {
						int width = normalFont.getStringEffectsWidth("From:  " + name + chatMessages[chatIndex]) + 25;
						if (width > 450)
							width = 450;
						if (super.mouseX < (clientSize == 0 ? 4 : 0) + width) {
							if (myRights >= 1) {
								menuActionName[menuActionRow] = "Report abuse <col=0xffffff>" + name;
								menuActionID[menuActionRow] = 2606;
								menuActionRow++;
							}
							if (!isFriendOrSelf(name)) {
								menuActionName[menuActionRow] = "Add ignore <col=0xffffff>" + name;
								menuActionID[menuActionRow] = 2042;
								menuActionRow++;
								menuActionName[menuActionRow] = "Add friend <col=0xffffff>" + name;
								menuActionID[menuActionRow] = 2337;
								menuActionRow++;
							}
							if (isFriendOrSelf(name)) {
								menuActionName[menuActionRow] = "Message <col=0xffffff>" + name;
								menuActionID[menuActionRow] = 2639;
								menuActionRow++;
							}
						}
					}
					if (++i >= 5)
						return;
				}
				if ((type == 5 || type == 6) && privateChatMode < 2 && ++i >= 5)
					return;
			}

	}

	private void createObjectSpawnRequest(int j, int objectId, int l, int i1,
			int j1, int k1, int l1, int i2, int j2) {
		GameObjectSpawnRequest newRequest = null;
		for (GameObjectSpawnRequest request = (GameObjectSpawnRequest) objectSpawnDeque
				.getFront(); request != null; request = (GameObjectSpawnRequest) objectSpawnDeque
				.getNext()) {
			if (request.plane != l1 || request.tileX != i2
					|| request.tileY != j1 || request.objectType != i1)
				continue;
			newRequest = request;
			break;
		}

		if (newRequest == null) {
			newRequest = new GameObjectSpawnRequest();
			newRequest.plane = l1;
			newRequest.objectType = i1;
			newRequest.tileX = i2;
			newRequest.tileY = j1;
			assignOldValuesToNewRequest(newRequest);
			objectSpawnDeque.insertBack(newRequest);
		}
		newRequest.currentIDRequested = objectId;
		newRequest.currentTypeRequested = k1;
		newRequest.currentFaceRequested = l;
		newRequest.spawnTime = j2;
		newRequest.removeTime = j;
	}

	private boolean interfaceIsSelected(RSInterface rsInterface) {
		if (rsInterface.valueCompareType == null)
			return false;
		for (int i = 0; i < rsInterface.valueCompareType.length; i++) {
			int myValue = extractInterfaceValues(rsInterface, i);
			int minValue = rsInterface.requiredValues[i];
			if (rsInterface.valueCompareType[i] == 2) {
				if (myValue >= minValue)
					return false;
			} else if (rsInterface.valueCompareType[i] == 3) {
				if (myValue <= minValue)
					return false;
			} else if (rsInterface.valueCompareType[i] == 4) {
				if (myValue == minValue)
					return false;
			} else if (rsInterface.valueCompareType[i] == 10) {
				if (myValue < minValue)
					return false;
			} else if (myValue != minValue)
				return false;
		}

		return true;
	}

	private void connectToUpdateServer() {
		int j = 5;
		expectedCRCs[8] = 0;
		int k = 0;
		while (expectedCRCs[8] == 0) {
			String s = "Unknown problem";
			drawSmoothLoading(20, "Connecting to web server");
			try {
				DataInputStream datainputstream = openJagGrabInputStream("crc" + (int) (Math.random() * 99999999D) + "-" + 317);
				Stream stream = new Stream(new byte[40]);
				datainputstream.readFully(stream.buffer, 0, 40);
				datainputstream.close();
				for (int crcIndex = 0; crcIndex < 9; crcIndex++)
					expectedCRCs[crcIndex] = stream.readInt();

				int j1 = stream.readInt();
				int k1 = 1234;
				for (int l1 = 0; l1 < 9; l1++)
					k1 = (k1 << 1) + expectedCRCs[l1];

				if (j1 != k1) {
					s = "checksum problem";
					expectedCRCs[8] = 0;
				}
			} catch (EOFException _ex) {
				s = "EOF problem";
				expectedCRCs[8] = 0;
			} catch (IOException _ex) {
				s = "connection problem";
				expectedCRCs[8] = 0;
			} catch (Exception _ex) {
				s = "logic problem";
				expectedCRCs[8] = 0;
				if (!signlink.reporterror)
					return;
			}
			if (expectedCRCs[8] == 0) {
				k++;
				for (int l = j; l > 0; l--) {
					if (k >= 10) {
						drawLoadingText(10, "Game updated - please reload page");
						l = 10;
					} else {
						drawLoadingText(10, s + " - Will retry in " + l
								+ " secs.");
					}
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				j *= 2;
				if (j > 60)
					j = 60;
			}
		}
	}

	private DataInputStream openJagGrabInputStream(String s) throws IOException {
		// if(!aBoolean872)
		// if(signlink.mainapp != null)
		// return signlink.openurl(s);
		// else
		// return new DataInputStream((new URL(getCodeBase(), s)).openStream());
		if (jagGrabSocket != null) {
			try {
				jagGrabSocket.close();
			} catch (Exception _ex) {
			}
			jagGrabSocket = null;
		}
		jagGrabSocket = openSocket(43595, Configuration.jaggrab);
		jagGrabSocket.setSoTimeout(50000);
		java.io.InputStream inputstream = jagGrabSocket.getInputStream();
		OutputStream outputstream = jagGrabSocket.getOutputStream();
		outputstream.write(("JAGGRAB /" + s + "\n\n").getBytes());
		return new DataInputStream(inputstream);
	}

	private void updatePlayer(Stream stream) {
		int j = stream.readBits(8);
		//System.out.println("PC Update Count: " + j);
		if (j < playerCount) {
			for (int k = j; k < playerCount; k++)
				anIntArray840[entityUpdateCount++] = playerIndices[k];

		}
		if (j > playerCount) {
			signlink.reporterror(myUsername + " Too many players");
			throw new RuntimeException("eek");
		}
		playerCount = 0;
		for (int l = 0; l < j; l++) {
			int i1 = playerIndices[l];
			Player player = playerArray[i1];
			int j1 = stream.readBits(1);
			if (j1 == 0) {
				playerIndices[playerCount++] = i1;
				player.loopCycle = loopCycle;
			} else {
				int k1 = stream.readBits(2);
				if (k1 == 0) {
					playerIndices[playerCount++] = i1;
					player.loopCycle = loopCycle;
					playersToUpdate[playersToUpdateCount++] = i1;
				} else if (k1 == 1) {
					playerIndices[playerCount++] = i1;
					player.loopCycle = loopCycle;
					int l1 = stream.readBits(3);
					player.moveInDir(false, l1);
					int j2 = stream.readBits(1);
					if (j2 == 1)
						playersToUpdate[playersToUpdateCount++] = i1;
				} else if (k1 == 2) {
					playerIndices[playerCount++] = i1;
					player.loopCycle = loopCycle;
					int i2 = stream.readBits(3);
					player.moveInDir(true, i2);
					int k2 = stream.readBits(3);
					player.moveInDir(true, k2);
					int l2 = stream.readBits(1);
					if (l2 == 1)
						playersToUpdate[playersToUpdateCount++] = i1;
				} else if (k1 == 3)
					anIntArray840[entityUpdateCount++] = i1;
			}
		}
	}

	public int terrainRegionX;
	public int terrainRegionY;
	public int[] titleScreenOffsets = null;
	public int titleWidth = -1;
	public int titleHeight = -1;
	public ScriptManager scriptManager;

	public void generateWorld(int x, int y) {
		terrainRegionX = x;
		terrainRegionY = y;
		requestMapReconstruct = false;
		if (currentRegionX == x && currentRegionY == y && loadingStage == 2) {
			return;
		}
		currentRegionX = x;
		currentRegionY = y;
		baseX = (currentRegionX - 6) * 8;
		baseY = (currentRegionY - 6) * 8;
		inTutorialIsland = (currentRegionX / 8 == 48 || currentRegionX / 8 == 49)
				&& currentRegionY / 8 == 48;
		if (currentRegionX / 8 == 48 && currentRegionY / 8 == 148)
			inTutorialIsland = true;
		loadingStage = 1;
		mapLoadingTime = System.currentTimeMillis();
		int indexLength = 0;
		for (int i21 = (currentRegionX - 6) / 8; i21 <= (currentRegionX + 6) / 8; i21++) {
			for (int k23 = (currentRegionY - 6) / 8; k23 <= (currentRegionY + 6) / 8; k23++)
				indexLength++;
		}
		terrainData = new byte[indexLength][];
		objectData = new byte[indexLength][];
		mapCoordinates = new int[indexLength];
		terrainIndices = new int[indexLength];
		objectIndices = new int[indexLength];
		int index = 0;
		for (int xTile = (currentRegionX - 6) / 8; xTile <= (currentRegionX + 6) / 8; xTile++) {
			for (int yTile = (currentRegionY - 6) / 8; yTile <= (currentRegionY + 6) / 8; yTile++) {
				mapCoordinates[index] = (xTile << 8) + yTile;
				if (inTutorialIsland
						&& (yTile == 49 || yTile == 149 || yTile == 147 || xTile == 50 || xTile == 49
								&& yTile == 47)) {
					terrainIndices[index] = -1;
					objectIndices[index] = -1;
					index++;
				} else {
					int terrainId = terrainIndices[index] = onDemandFetcher.getMapIdForRegions(0, yTile, xTile);
					if (terrainId != -1)
						onDemandFetcher.requestFileData(3, terrainId);
					int landscapeID = objectIndices[index] = onDemandFetcher.getMapIdForRegions(1, yTile, xTile);
					if (landscapeID != -1)
						onDemandFetcher.requestFileData(3, landscapeID);
					index++;
				}
			}
		}
		int i17 = baseX - anInt1036;
		int j21 = baseY - anInt1037;
		anInt1036 = baseX;
		anInt1037 = baseY;
		for (int j24 = 0; j24 < 16384; j24++) {
			NPC npc = npcArray[j24];
			if (npc != null) {
				for (int j29 = 0; j29 < 10; j29++) {
					npc.pathX[j29] -= i17;
					npc.pathY[j29] -= j21;
				}
				npc.x -= i17 * 128;
				npc.y -= j21 * 128;
			}
		}
		for (int i27 = 0; i27 < maxPlayers; i27++) {
			Player player = playerArray[i27];
			if (player != null) {
				for (int i31 = 0; i31 < 10; i31++) {
					player.pathX[i31] -= i17;
					player.pathY[i31] -= j21;
				}
				player.x -= i17 * 128;
				player.y -= j21 * 128;
			}
		}
		loadingMap = true;
		byte byte1 = 0;
		byte byte2 = 104;
		byte byte3 = 1;
		if (i17 < 0) {
			byte1 = 103;
			byte2 = -1;
			byte3 = -1;
		}
		byte byte4 = 0;
		byte byte5 = 104;
		byte byte6 = 1;
		if (j21 < 0) {
			byte4 = 103;
			byte5 = -1;
			byte6 = -1;
		}
		for (int k33 = byte1; k33 != byte2; k33 += byte3) {
			for (int l33 = byte4; l33 != byte5; l33 += byte6) {
				int i34 = k33 + i17;
				int j34 = l33 + j21;
				for (int k34 = 0; k34 < 4; k34++)
					if (i34 >= 0 && j34 >= 0 && i34 < 104 && j34 < 104)
						groundArray[k34][k33][l33] = groundArray[k34][i34][j34];
					else
						groundArray[k34][k33][l33] = null;
			}
		}
		for (GameObjectSpawnRequest spawnReq = (GameObjectSpawnRequest) objectSpawnDeque
				.getFront(); spawnReq != null; spawnReq = (GameObjectSpawnRequest) objectSpawnDeque
				.getNext()) {
			spawnReq.tileX -= i17;
			spawnReq.tileY -= j21;
			if (spawnReq.tileX < 0 || spawnReq.tileY < 0
					|| spawnReq.tileX >= 104 || spawnReq.tileY >= 104)
				spawnReq.unlink();
		}
		if (destX != 0) {
			destX -= i17;
			destY -= j21;
		}
		inCutScene = false;
	}

	public void resetWorld(int stage) {
		if (stage == 0) {
			currentSound = 0;
			cameraOffsetX = (int) (Math.random() * 100D) - 50;
			cameraOffsetY = (int) (Math.random() * 110D) - 55;
			viewRotationOffset = (int) (Math.random() * 80D) - 40;
			minimapRotation = (int) (Math.random() * 120D) - 60;
			minimapZoom = (int) (Math.random() * 30D) - 20;
			viewRotation = (int) (Math.random() * 20D) - 10 & 0x7ff;
			minimapStatus = 0;
			loadingStage = 1;
		} else if (stage == 1) {
			loadingMap = false;
		}
	}

	void loginScreenBG(boolean b) {
		xCameraPos = 6100;
		yCameraPos = 6867;
		zCameraPos = -750;
		xCameraCurve = 2040;
		yCameraCurve = 383;
		resetWorld(0);
		if (b || scriptManager == null) {
			scriptManager = new ScriptManager(this);
		} else {
			scriptManager.update();
		}
		plane = scriptManager.regionPlane;
		generateWorld(scriptManager.terrainRegionX,
				scriptManager.terrainRegionY);
		resetWorld(1);
	}

	public void drawAnimatedWorldBackground(boolean display) {
		if (display) {
			int centerX = clientWidth / 2;
			int centerY = clientHeight / 2;
			if (scriptManager == null) {
				loginScreenBG(true);
			}
			int canvasCenterX = Rasterizer.center_x;
			int canvasCenterY = Rasterizer.center_y;
			int canvasPixels[] = Rasterizer.lineOffsets;
			if (titleScreenOffsets != null
					&& (titleWidth != clientWidth || titleHeight != clientHeight)) {
				titleScreenOffsets = null;
			}
			if (titleScreenOffsets == null) {
				titleWidth = clientWidth;
				titleHeight = clientHeight;
				titleScreenOffsets = Rasterizer.getOffsets(titleWidth,
						titleHeight);
			}
			Rasterizer.center_x = centerX;
			Rasterizer.center_y = centerY;
			Rasterizer.lineOffsets = titleScreenOffsets;
			if (loadingStage == 2 && Region.onBuildTimePlane != plane)
				loadingStage = 1;

			if (!loggedIn && loadingStage == 1) {
				// getMapLoadingState();
			}
			if (!loggedIn && loadingStage == 2 && plane != lastKnownPlane) {
				lastKnownPlane = plane;
				renderedMapScene(plane);
			}
			if (loadingStage == 2) {
				// Texture.triangles = 0;
				try {
					worldController.render(xCameraPos, yCameraPos,
							xCameraCurve, zCameraPos, getCamHeight(),
							yCameraCurve);
					DrawingArea.resetImage();
					// public static int clientWidth = 765, clientHeight =
					// 563;//503
					DrawingArea.drawPixels(765, 0, 0, dayTimeShades(), 503);
					worldController.clearInteractableObjects();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (scriptManager != null && loadingStage == 2
					&& plane == lastKnownPlane && !loggedIn) {
				scriptManager.cycle();
			}
			Rasterizer.center_x = canvasCenterX;
			Rasterizer.center_y = canvasCenterY;
			Rasterizer.lineOffsets = canvasPixels;
		}
	}

	private boolean animationDone = false;

	private void animateLoginScreen() {
		// main box
		SpriteCache.spriteCache[53].drawMovingSprite(-1, (clientHeight / 2)
				- (SpriteCache.spriteCache[53].myHeight / 2), (clientWidth / 2)
				- (SpriteCache.spriteCache[53].myWidth / 2), 0, 4, 'y', true);
		// username path
		SpriteCache.spriteCache[53 + 5].drawMovingSprite(
				(clientWidth / 2 - 75), 0, 0, (clientHeight / 2 - 34), 5, 'x',
				true);
		// password path
		SpriteCache.spriteCache[53 + 6].drawMovingSprite(
				(clientWidth / 2 - 75), 0, clientWidth,
				(clientHeight / 2 + 12), (int) -5.8, 'x', true);
		if (SpriteCache.spriteCache[53 + 6].movedEnough)
			animationDone = true;
	}
	
	private long circleDelay;
	private int circle = 0;

	private ParticleEmitter loader;
	
	private void drawLoadingMenu() {
		if(loader == null)
			loader = new ParticleEmitter(600, 600, 10, 0, 0, new ParticleDefinition(0.00001f, 0.00001f, 6.0f, 1.0f, 0.2, -0.1, 0.2, -0.1, true));
		DrawingArea474.drawAlphaFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0, 200);
		if (System.currentTimeMillis() - circleDelay > 157) {
			circle += (circle == 7) ? -7 : 1;
			circleDelay = System.currentTimeMillis();
		}
		int baseX = (clientWidth / 2) - (SpriteCache.spriteCache[694].myWidth / 2);
		int baseY = (clientHeight / 2) - (SpriteCache.spriteCache[694].myHeight / 2);
		SpriteCache.spriteCache[786].drawAdvancedSprite((clientWidth / 2) - (SpriteCache.spriteCache[786].myWidth / 2),
				(clientHeight / 2) - (SpriteCache.spriteCache[786].myHeight / 2));
		SpriteCache.spriteCache[788].drawAdvancedSprite(baseX + 325, baseY + 210);
		int lc = this.loopCycle;
		double x = baseX + 375 + (50 * Math.cos(lc));
		double y = baseY + 260 + (50 * Math.sin(lc));
		loader.setOrigin(new ParticleVector((float)x, (float)y));
		loader.cycle();
		normalFont.drawStringCenter(16777215, clientWidth / 2, "Connecting to the server...", clientHeight / 2 + 65, true);
	}

	private void drawWarningMenu() {
		DrawingArea474.drawAlphaFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0, 200);
		int baseX = (clientWidth / 2) - (SpriteCache.spriteCache[694].myWidth / 2);
		int baseY = (clientHeight / 2) - (SpriteCache.spriteCache[694].myHeight / 2);
		
		SpriteCache.spriteCache[786].drawAdvancedSprite((clientWidth / 2) - (SpriteCache.spriteCache[786].myWidth / 2),
				(clientHeight / 2) - (SpriteCache.spriteCache[786].myHeight / 2));
		SpriteCache.spriteCache[785].drawAdvancedSprite(baseX + 345, baseY + 210);
		normalFont.drawStringCenter(0x000000, clientWidth / 2, loginMessage1, clientHeight / 2 + 10, false);
		normalFont.drawStringCenter(0x000000, clientWidth / 2, loginMessage2, clientHeight / 2 + 30, false);
		if(warningBackButton != null)
			warningBackButton.render();
	}

	private int opacity = 256;

	private void loopLayerTransparency(int xPos, int YPos, int width,
			int height, int speed) {
		if (opacity >= 0)
			opacity -= speed;
		else
			opacity = 0;
		DrawingArea474.drawAlphaFilledPixels(xPos, YPos, width, height, 0,
				opacity);
	}

	private Checkbox hdTexturesCheck;
	private Checkbox playerShadowCheck;
	private Checkbox tooltipHoversCheck;
	private Checkbox newSpritesCheck;
	private Checkbox x10DamageCheck;
	private Checkbox combatIconsCheck;
	private Checkbox absorbCheck;
	private Checkbox newItemsCheck;
	private Checkbox newIDKCheck;
	private Checkbox newHitbarCheck;
	private Checkbox smoothShadeCheck;
	private Checkbox tweeningCheck;
	private Checkbox orbsCheck;
	private Checkbox xpOrbCheck;
	private Checkbox xpCircleCheck;
	private Checkbox menuCrownsCheck;
	private SelectionToggle menuSelection;
	private SelectionToggle hitsplatsSelection;
	private SelectionToggle gameframeSelection;
	private Button saveSettings;
	private Button resetSettings;
	
	public void resetSettingElements() {
		hdTexturesCheck = null;
		playerShadowCheck = null;
		tooltipHoversCheck = null;
		newSpritesCheck = null;
		x10DamageCheck = null;
		combatIconsCheck = null;
		absorbCheck = null;
		newItemsCheck = null;
		newIDKCheck = null;
		newHitbarCheck = null;
		smoothShadeCheck = null;
		tweeningCheck = null;
		menuSelection = null;
		hitsplatsSelection = null;
		gameframeSelection = null;
		saveSettings = null;
		resetSettings = null;
	}
	
	private void setupClientSettingMenu(int baseX, int baseY) {
		if(hdTexturesCheck == null)
			hdTexturesCheck = new Checkbox(baseX + 243, baseY + 142, getOption("hd_tex"), "HD Ground Textures") {
				@Override
				public void doAction() {
					options.put("hd_tex", !getOption("hd_tex"));
				}
			};
		if(playerShadowCheck == null)
			playerShadowCheck = new Checkbox(baseX + 243, baseY + 162, getOption("player_shadow"), "Player Shadows") {
				@Override
				public void doAction() {
					options.put("player_shadow", !getOption("player_shadow"));
				}
			};
		if(tooltipHoversCheck == null)
			tooltipHoversCheck = new Checkbox(baseX + 243, baseY + 182, getOption("tooltip_hover"), "Tooltips") {
				@Override
				public void doAction() {
					options.put("tooltip_hover", !getOption("tooltip_hover"));
				}
			};
		if(newSpritesCheck == null)
			newSpritesCheck = new Checkbox(baseX + 243, baseY + 202, getOption("new_sprites"), "New Sprites") {
				@Override
				public void doAction() {
					options.put("new_sprites", !getOption("new_sprites"));
				}
			};
		if(x10DamageCheck == null)
			x10DamageCheck = new Checkbox(baseX + 243, baseY + 222, getOption("damage_10"), "x10 Damage") {
				@Override
				public void doAction() {
					options.put("damage_10", !getOption("damage_10"));
				}
			};
		if(combatIconsCheck == null)
			combatIconsCheck = new Checkbox(baseX + 243, baseY + 242, getOption("combat_icons"), "Show Combat Icons") {
				@Override
				public void doAction() {
					options.put("combat_icons", !getOption("combat_icons"));
				}
			};
		if(absorbCheck == null)
			absorbCheck = new Checkbox(baseX + 243, baseY + 262, getOption("absorb"), "Show Absorb/Soak") {
				@Override
				public void doAction() {
					options.put("absorb", !getOption("absorb"));
				}
			};
		if(newItemsCheck == null)
			newItemsCheck = new Checkbox(baseX + 243, baseY + 282, getOption("new_items"), "New Items") {
				@Override
				public void doAction() {
					options.put("new_items", !getOption("new_items"));
					Player.modelCache.clear();
				}
			};
		if(newIDKCheck == null)
			newIDKCheck = new Checkbox(baseX + 243, baseY + 302, getOption("new_idk"), "Old Character Appearances") {
				@Override
				public void doAction() {
					options.put("new_idk", !getOption("new_idk"));
				}
			};
		if(newHitbarCheck == null)
			newHitbarCheck = new Checkbox(baseX + 243, baseY + 322, getOption("new_hitbar"), "New Hitbar") {
				@Override
				public void doAction() {
					options.put("new_hitbar", !getOption("new_hitbar"));
				}
			};
		if(smoothShadeCheck == null)
			smoothShadeCheck = new Checkbox(baseX + 243, baseY + 342, getOption("smooth_shade"), "Smooth Shading") {
				@Override
				public void doAction() {
					options.put("smooth_shade", !getOption("smooth_shade"));
				}
			};
		if(tweeningCheck == null)
			tweeningCheck = new Checkbox(baseX + 430, baseY + 342, getOption("tweening"), "Smooth Animation") {
				@Override
				public void doAction() {
					options.put("tweening", !getOption("tweening"));
				}
			};
		if(orbsCheck == null)
			orbsCheck = new Checkbox(baseX + 430, baseY + 282, getOption("orbs"), "Show Orbs") {
				@Override
				public void doAction() {
					options.put("orbs", !getOption("orbs"));
				}
			};
		if(xpOrbCheck == null)
			xpOrbCheck = new Checkbox(baseX + 430, baseY + 302, getOption("xp_orb"), "XP Orb") {
				@Override
				public void doAction() {
					options.put("xp_orb", !getOption("xp_orb"));
				}
			};
		if(xpCircleCheck == null)
			xpCircleCheck = new Checkbox(baseX + 430, baseY + 322, getOption("xp_circle"), "Skill Circles") {
				@Override
				public void doAction() {
					options.put("xp_circle", !getOption("xp_circle"));
				}
			};
		if(menuCrownsCheck == null)
			menuCrownsCheck = new Checkbox(baseX + 243, baseY + 362, getOption("menu_crown"), "Crowns in Menus") {
				@Override
				public void doAction() {
					options.put("menu_crown", !getOption("menu_crown"));
				}
			};
		if(menuSelection == null)
			menuSelection = new SelectionToggle(baseX + 480, baseY + 142, "Context Menu:", new int[]{317, 562/*, 718*/}, settings.get("menu")) {
				@Override
				public void doAction() {
					settings.put("menu", this.currentOption);
				}
			};
		if(hitsplatsSelection == null)
			hitsplatsSelection = new SelectionToggle(baseX + 480, baseY + 182, "Hitsplats:", new int[]{317, 562, 632}, settings.get("hitsplats")) {
				@Override
				public void doAction() {
					settings.put("hitsplats", this.currentOption);
				}
			};
		if(gameframeSelection == null)
			gameframeSelection = new SelectionToggle(baseX + 480, baseY + 222, "Game Frame:", new int[]{474, 525, 614}, settings.get("gameframe")) {
				@Override
				public void doAction() {
					settings.put("gameframe", this.currentOption);
				}
			};
		if(saveSettings == null)
			saveSettings = new Button(baseX + 313, baseY + 402, 7, "Save Settings"){
				@Override
				public void doAction() {
					saveSettings();
					resetSettingElements();
					loginScreenState = 0;
				}
			};
		if(resetSettings == null)
			resetSettings = new Button(baseX + 313, baseY + 432, 7, "Reset Settings"){
				@Override
				public void doAction() {
					resetSettings();
					resetSettingElements();
				}
			};
	}
	
	private void drawClientSettings() {
		int baseX = (clientWidth / 2) - (SpriteCache.spriteCache[694].myWidth / 2);
		int baseY = (clientHeight / 2) - (SpriteCache.spriteCache[694].myHeight / 2);
		SpriteCache.get(756).drawAdvancedSprite(baseX + ((SpriteCache.get(694).myWidth / 2) - (SpriteCache.get(756).myWidth / 2)),
				baseY + ((SpriteCache.get(694).myHeight / 2) - (SpriteCache.get(756).myHeight / 2)));
		setupClientSettingMenu(baseX, baseY);
		hdTexturesCheck.render();
		playerShadowCheck.render();
		tooltipHoversCheck.render();
		newSpritesCheck.render();
		x10DamageCheck.render();
		combatIconsCheck.render();
		absorbCheck.render();
		newItemsCheck.render();
		newIDKCheck.render();
		newHitbarCheck.render();
		smoothShadeCheck.render();
		tweeningCheck.render();
		orbsCheck.render();
		xpOrbCheck.render();
		xpCircleCheck.render();
		menuCrownsCheck.render();
		menuSelection.render();
		hitsplatsSelection.render();
		gameframeSelection.render();
		saveSettings.render();
		resetSettings.render();
	}
	
	public void processClientSettingsInput(int baseX, int baseY) {
		if(hdTexturesCheck == null)
			return;
		hdTexturesCheck.click(baseX, baseY);
		playerShadowCheck.click(baseX, baseY);
		tooltipHoversCheck.click(baseX, baseY);
		newSpritesCheck.click(baseX, baseY);
		x10DamageCheck.click(baseX, baseY);
		combatIconsCheck.click(baseX, baseY);
		absorbCheck.click(baseX, baseY);
		newItemsCheck.click(baseX, baseY);
		newIDKCheck.click(baseX, baseY);
		newHitbarCheck.click(baseX, baseY);
		smoothShadeCheck.click(baseX, baseY);
		tweeningCheck.click(baseX, baseY);
		orbsCheck.click(baseX, baseY);
		xpOrbCheck.click(baseX, baseY);
		xpCircleCheck.click(baseX, baseY);
		menuCrownsCheck.click(baseX, baseY);
		menuSelection.click(baseX, baseY);
		hitsplatsSelection.click(baseX, baseY);
		gameframeSelection.click(baseX, baseY);
		saveSettings.click(baseX, baseY);
		if(resetSettings != null)
			resetSettings.click(baseX, baseY);
	}
	
	private boolean bgCheck = true;
	private boolean loginRedraw = false;

	public RealFont[] arial = { new RealFont(this, "Arial", 0, 10, true),
			new RealFont(this, "Arial", 0, 12, true),
			new RealFont(this, "Arial", 0, 14, true) };

	public void setupLogin(int baseX, int baseY) {
		if(emitter == null || particleNeedsRedraw) {
			emitter = new ParticleEmitter(baseX + 600, baseY + 355,10,15,15, new ParticleDefinition(0.05f, 0.05f, 5.0f, 1.0f, 2.5, -1.5, 2.5, -1.5, true));
			particleNeedsRedraw = false;
		}
		if(loginScreenButton == null || loginRedraw) // does the login button need to be instantiated?
			loginScreenButton = new Button(baseX + 285, baseY + 385, 5, "Login") {
				@Override
				public void doAction() {
					if ((myUsername.length() > 0) && (myPassword.length() > 0)) {
						loginFailures = 0;
						previousScreenState = 0;
						loginScreenState = 1;
					} else {
						loginScreenState = 2;
						loginScreenCursorPos = 0;
						loginMessage1 = "Username & Password";
						loginMessage2 = "Must be more than 1 character";
					}
					if (loggedIn)
						return;
				}
			};
		if(registerScreenButton == null || loginRedraw) // does the login button need to be instantiated?
			registerScreenButton = new Button(baseX + 383, baseY + 385, 5, "Register") {
				@Override
				public void doAction() {
					launchURL("http://revolutionxpk.org/forum/index.php?app=core&module=global&section=register");
				}
			};
		if(settingsScreenButton == null || loginRedraw) // does the register button need to be instantiated?
			settingsScreenButton = new Button(baseX + 316, baseY + 413, 8, "Client Settings") {
				@Override
				public void doAction() {
					loginScreenState = 4;
				}
			};
		if(warningBackButton == null || loginRedraw) // does the register button need to be instantiated?
			warningBackButton = new Button(baseX + 343, baseY + 330, 4, "Back") {
				@Override
				public void doAction() {
					loginScreenState = 0;
				}
			};
		if(rememberCheck == null || loginRedraw)
			rememberCheck = new Checkbox(baseX + 335, baseY + 445, !dontRememberMe, "Remember me") {
				@Override
				public void doAction() {
					dontRememberMe = !dontRememberMe;
				}
			};
		/*if(musicCheck == null || loginRedraw)
			musicCheck = new Checkbox(baseX + 418, baseY + 445, musicEnabled, "Music") {
				@Override
				public void doAction() {
					musicEnabled = !musicEnabled;
					saveSettings();
					if (musicEnabled) {
						nextSong = currentSong;
						songChanging = true;
						onDemandFetcher.requestFileData(2, nextSong);
					} else {
						stopMidi();
					}
				}
			};*/
		if(usernameBox == null || loginRedraw)
			usernameBox = new InputBox(baseX + 318, baseY + 208, 16, 0, myUsername, "Username:") {
				@Override
				public void doAction() {
					loginScreenCursorPos = 0;
				}
			};
		if(passwordBox == null || loginRedraw)
			passwordBox = new InputBox(baseX + 318, baseY + 243, 16, 1, TextClass.passwordAsterisks(myPassword), "Password:") {
				@Override
				public void doAction() {
					loginScreenCursorPos = 1;
				}
			};
	}
	
	/**
	 * loginScreenState
	 * 0 - login
	 * 1 - logging in
	 * 2 - warning
	 * 3 - register
	 * 4 - 
	 * 5 - title page login/register
	 * @param flag
	 */
	
	private static ParticleEmitter emitter;
	private Button loginScreenButton;
	private Button registerScreenButton;
	private Button settingsScreenButton;
	private Button warningBackButton;
	private Checkbox rememberCheck;
	//private Checkbox musicCheck;
	private InputBox usernameBox;
	private InputBox passwordBox;
	
	public boolean particleNeedsRedraw = false;
	
	public void drawLoginScreen(boolean flag) {
		try {
			resetImageProducers();
			titleScreen.initDrawingArea();
			DrawingArea474.drawFilledPixels(0, 0, getClientWidth(), getClientHeight(), 0x000000);

			/**
			 * Setup the login screen rendering!
			 */
			int baseX = (clientWidth / 2) - (SpriteCache.spriteCache[694].myWidth / 2);
			int baseY = (clientHeight / 2) - (SpriteCache.spriteCache[694].myHeight / 2);
			setupLogin(baseX, baseY);
			SpriteCache.spriteCache[694].drawSprite(baseX, baseY);
			/*drawingArea.drawString(0xffffff, "MouseX: " + mouseX, baseY + 15, baseX + 5);
			drawingArea.drawString(0xffffff, "MouseY: " + mouseY, baseY + 30, baseX + 5);
			drawingArea.drawString(0xffffff, "BaseX: " + baseX, baseY + 45, baseX + 5);
			drawingArea.drawString(0xffffff, "BaseY: " + baseY, baseY + 60, baseX + 5);
			drawingArea.drawString(0xffffff, "State: " + loginScreenState, baseY + 75, baseX + 5);
			drawingArea.drawString(0xffffff, "Loop Cycle: " + this.loopCycle, baseY + 90, baseX + 5);
			drawingArea.drawString(0xffffff, "GameFrame: " + settings.get("gameframe"), baseY + 105, baseX + 5);
			drawingArea.drawString(0xffffff, "Menu: " + settings.get("gameframe"), baseY + 120, baseX + 5);*/
			
			if(emitter != null) {
				emitter.cycle();
			}
			char c = '\u0168';
			if (loginScreenState == 0) {
				int i = 100;
				drawingArea.drawStringCenter(0x75a9a9, c / 190, onDemandFetcher.statusString, i, true);
				SpriteCache.get(756).drawAdvancedSprite(baseX + ((SpriteCache.get(694).myWidth / 2) - (SpriteCache.get(756).myWidth / 2)),
						baseY + ((SpriteCache.get(694).myHeight / 2) - (SpriteCache.get(756).myHeight / 2)));
				SpriteCache.get(784).drawAdvancedSprite(baseX + 210, baseY + 90);
				
				newRegularFont.drawBasicString("Please use your forum login details!", baseX + 278, baseY + 190, 0xffffff, 0x000000);
				
				usernameBox.render();
				usernameBox.setString(myUsername);
				passwordBox.render();
				passwordBox.setString(TextClass.passwordAsterisks(myPassword));

				drawInterface(0, baseX + 65, RSInterface.interfaceCache[31000], baseY - 15);
				handleAccountHeadRotation();
				processInterfaceAnimation(1, 31000);
				int size = (Account.accounts.size() > 3) ? 3 : Account.accounts.size();
				for (int ii = 0; ii < size; ii++) {
					if (mouseX >= baseX + 158 + 65 + (Configuration.CHARACTERS_SEPARATOR_WIDTH * ii)
							&& mouseY >= baseY + 290
							&& mouseX <= baseX + 257 + 55 + (Configuration.CHARACTERS_SEPARATOR_WIDTH * ii)
							&& mouseY <= baseY + 373) {
						//RSInterface.interfaceCache[31001 + (ii * 4)].opacity = (byte) 200;
						RSInterface.interfaceCache[31001 + (ii * 4)].disabledSpriteId = 783;
					} else {
						//RSInterface.interfaceCache[31001 + (ii * 4)].opacity = (byte) 100;
						RSInterface.interfaceCache[31001 + (ii * 4)].disabledSpriteId = 782;
					}

				}
				if(size == 0)
					newRegularFont.drawCenteredString("You have no saved accounts!", baseX + 383, baseY + 326, 0xffffff, 0x000000);
				
				loginScreenButton.render();
				registerScreenButton.render();
				settingsScreenButton.render();
				
				rememberCheck.render();
				//musicCheck.render();
			} else if (loginScreenState == 1) {
				drawLoadingMenu();
			} else if (loginScreenState == 2) {
				drawWarningMenu();
			} else if (loginScreenState == 3) {
				getRegister().render();
			} else if (loginScreenState == 4) {
				drawClientSettings();
			} else if(loginScreenState == 6) {
				drawLoadingMenu();
			}
			titleScreen.drawGraphics(0, super.graphics, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean dontRememberMe;

	private void processLoginScreenInput() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		try {
			int baseX = (clientWidth / 2) - (SpriteCache.spriteCache[694].myWidth / 2);
			int baseY = (clientHeight / 2) - (SpriteCache.spriteCache[694].myHeight / 2);
			
			//main login screen
			if (this.loginScreenState == 0) {
				resetImage();
				if(super.clickMode3 == 1 && loginScreenButton != null)
					loginScreenButton.click(mouseX, mouseY);
				if(super.clickMode3 == 1 && registerScreenButton != null)
					registerScreenButton.click(mouseX, mouseY);
				if(super.clickMode3 == 1 && settingsScreenButton != null)
					settingsScreenButton.click(mouseX, mouseY);
				/*if(super.clickMode3 == 1 && musicCheck != null)
					musicCheck.click(mouseX, mouseY);*/
				if(super.clickMode3 == 1 && rememberCheck != null)
					rememberCheck.click(mouseX, mouseY);
				if(super.clickMode3 == 1 && usernameBox != null)
					usernameBox.click(mouseX, mouseY);
				if(super.clickMode3 == 1 && passwordBox != null)
					passwordBox.click(mouseX, mouseY);
				
				if (super.clickMode3 == 1) {
					for (int ii = 0; ii < Account.accounts.size(); ii++) {

						if (saveClickX >= baseX + 297 + (Configuration.CHARACTERS_SEPARATOR_WIDTH * ii)
								&& saveClickY >= baseY + 288
								&& saveClickX <= baseX + 314 + (Configuration.CHARACTERS_SEPARATOR_WIDTH * ii)
								&& saveClickY <= baseY + 303) {
							Account a = Account.accounts.get(ii);
							a.delete();
							Account.accounts.remove(a);
							RSInterface.buildPlayerMenu(Account.accounts);
						} else if (saveClickX >= baseX + 158 + 65 + (Configuration.CHARACTERS_SEPARATOR_WIDTH * ii)
								&& saveClickY >= baseY + 290
								&& saveClickX <= baseX + 257 + 55 + (Configuration.CHARACTERS_SEPARATOR_WIDTH * ii)
								&& saveClickY <= baseY + 373) {
							loginFailures = 0;
							previousScreenState = 0;
							loginScreenState = 1;
							myUsername = Account.accounts.get(ii).getUsername();
							myPassword = Account.accounts.get(ii).getPassword();
						}
					}
				}
				if (super.clickMode3 == 1
						&& ((this.saveClickX >= baseX + 282)
								&& (saveClickX <= baseX + 459)
								&& (saveClickY >= baseY + 492) && (saveClickY <= baseY + 539))) {
					if ((myUsername.length() > 0) && (myPassword.length() > 0)) {
						loginFailures = 0;
						previousScreenState = 0;
						loginScreenState = 1;
					} else {
						this.loginScreenCursorPos = 0;
						this.loginMessage1 = "Username & Password";
						this.loginMessage2 = "Must be more than 1 character";
					}
					if (loggedIn)
						return;
				}
				do {
					int keyPressed = readChar(-796);
					if (keyPressed == -1)
						break;
					boolean validKey = false;
					for (int i2 = 0; i2 < validUserPassChars.length(); i2++) {
						if (keyPressed != validUserPassChars.charAt(i2))
							continue;
						validKey = true;
						break;
					}

					if (loginScreenCursorPos == 0) {
						if (keyPressed == 8 && myUsername.length() > 0)
							myUsername = myUsername.substring(0, myUsername.length() - 1);
						if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13)
							loginScreenCursorPos = 1;

						if (validKey)
							myUsername += (char) keyPressed;
						if (myUsername.length() > 15)
							myUsername = myUsername.substring(0, 15);
					} else if (loginScreenCursorPos == 1) {
						if (keyPressed == 8 && myPassword.length() > 0)
							myPassword = myPassword.substring(0, myPassword.length() - 1);
						if (keyPressed == 9 || keyPressed == 10 || keyPressed == 13) {
							if (myPassword.length() > 0) {
								loginFailures = 0;
								loginScreenState = 1;
							} else {
								loginScreenCursorPos = 0;
							}
						}
						if (validKey)
							myPassword += (char) keyPressed;
						if (myPassword.length() > 21)
							myPassword = myPassword.substring(0, 21);
					}
				} while (true);
				return;
			}
			
			if (loginScreenState == 1) {
				if (circle == 4 + getRandom(3, true)) {
					login(previousScreenState == 3 ? getRegister().username : myUsername, previousScreenState == 3 ? getRegister().password : myPassword, false);
					circle = 0;
				}
			}
			if (loginScreenState == 2 && super.clickMode3 == 1) {
				warningBackButton.click(mouseX, mouseY);
			}
			if (loginScreenState == 3) {
				getRegister().processInput();
			}
			if(loginScreenState == 4 && super.clickMode3 == 1) {
				processClientSettingsInput(mouseX, mouseY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String capitalizeFirstChar(String s) {
		try {
			if (s != "")
				return (s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase()).trim();
		} catch (Exception e) {
		}
		return s;
	}

	private int[] anIntArray828;
	private int[] anIntArray829;
	private int anInt1275;

	private int anInt1040;
	private int anInt1041;
	private final int[] anIntArray969;

	public void raiseWelcomeScreen() {
		welcomeScreenRaised = true;
	}

	private void parseEntityPacket(Stream stream, int j) {
		if (j == 84) {
			int k = stream.readUnsignedByte();
			int j3 = bigRegionX + (k >> 4 & 7);
			int i6 = bigRegionY + (k & 7);
			int l8 = stream.readUnsignedWord();
			int k11 = stream.readUnsignedWord();
			int l13 = stream.readUnsignedWord();
			if (j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104) {
				Deque class19_1 = groundArray[plane][j3][i6];
				if (class19_1 != null) {
					for (Item class30_sub2_sub4_sub2_3 = (Item) class19_1
							.getFront(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (Item) class19_1
							.getNext()) {
						if (class30_sub2_sub4_sub2_3.ID != (l8 & 0x7fff)
								|| class30_sub2_sub4_sub2_3.amount != k11)
							continue;
						class30_sub2_sub4_sub2_3.amount = l13;
						break;
					}

					spawnGroundItem(j3, i6);
				}
			}
			return;
		}
		if (j == 105) {
			int l = stream.readUnsignedByte();
			int k3 = bigRegionX + (l >> 4 & 7);
			int j6 = bigRegionY + (l & 7);
			int i9 = stream.readUnsignedWord();
			int l11 = stream.readUnsignedByte();
			int i14 = l11 >> 4 & 0xf;
			int i16 = l11 & 7;
			if (myPlayer.pathX[0] >= k3 - i14 && myPlayer.pathX[0] <= k3 + i14
					&& myPlayer.pathY[0] >= j6 - i14
					&& myPlayer.pathY[0] <= j6 + i14 && soundEnabled && !lowMem
					&& currentSound < 50) {
				sound[currentSound] = i9;
				soundType[currentSound] = i16;
				soundDelay[currentSound] = Sounds.anIntArray326[i9];
				currentSound++;
			}
		}
		if (j == 215) {
			int i1 = stream.readShortA();
			int l3 = stream.readByteS();
			int k6 = bigRegionX + (l3 >> 4 & 7);
			int j9 = bigRegionY + (l3 & 7);
			int i12 = stream.readShortA();
			int j14 = stream.readUnsignedWord();
			if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != playerId) {
				Item class30_sub2_sub4_sub2_2 = new Item();
				class30_sub2_sub4_sub2_2.ID = i1;
				class30_sub2_sub4_sub2_2.amount = j14;
				if (groundArray[plane][k6][j9] == null)
					groundArray[plane][k6][j9] = new Deque();
				groundArray[plane][k6][j9].insertBack(class30_sub2_sub4_sub2_2);
				spawnGroundItem(k6, j9);
			}
			return;
		}
		if (j == 156) {
			int j1 = stream.readByteA();
			int regionX = bigRegionX + (j1 >> 4 & 7);
			int regionY = bigRegionY + (j1 & 7);
			int k9 = stream.readUnsignedWord();
			if (regionX >= 0 && regionY >= 0 && regionX < 104 && regionY < 104) {
				Deque deque = groundArray[plane][regionX][regionY];
				if (deque != null) {
					for (Item item = (Item) deque.getFront(); item != null; item = (Item) deque.getNext()) {
						if (item.ID != (k9 & 0x7fff))
							continue;
						item.unlink();
						break;
					}

					if (deque.getFront() == null)
						groundArray[plane][regionX][regionY] = null;
					spawnGroundItem(regionX, regionY);
				}
			}
			return;
		}
		if (j == 160) {
			int k1 = stream.readByteS();
			int j4 = bigRegionX + (k1 >> 4 & 7);
			int i7 = bigRegionY + (k1 & 7);
			int l9 = stream.readByteS();
			int j12 = l9 >> 2;
			int k14 = l9 & 3;
			int j16 = anIntArray1177[j12];
			int j17 = stream.readShortA();
			if (j4 >= 0 && i7 >= 0 && j4 < 103 && i7 < 103) {
				int j18 = intGroundArray[plane][j4][i7];
				int i19 = intGroundArray[plane][j4 + 1][i7];
				int l19 = intGroundArray[plane][j4 + 1][i7 + 1];
				int k20 = intGroundArray[plane][j4][i7 + 1];
				if (j16 == 0) {
					WallObject class10 = worldController.getWallObject(plane,
							j4, i7);
					if (class10 != null) {
						int k21 = class10.wallObjUID;
						if (j12 == 2) {
							class10.node1 = new ObjectOnTile(k21, 4 + k14, 2,
									i19, l19, j18, k20, j17, false);
							class10.node2 = new ObjectOnTile(k21, k14 + 1 & 3,
									2, i19, l19, j18, k20, j17, false);
						} else {
							class10.node1 = new ObjectOnTile(k21, k14, j12,
									i19, l19, j18, k20, j17, false);
						}
					}
				}
				if (j16 == 1) {
					WallDecoration class26 = worldController.getWallDecoration(
							j4, i7, plane);
					if (class26 != null)
						class26.node = new ObjectOnTile(class26.wallDecorUID,
								0, 4, i19, l19, j18, k20, j17, false);
				}
				if (j16 == 2) {
					InteractableObject class28 = worldController
							.getInteractableObject(j4, i7, plane);
					if (j12 == 11)
						j12 = 10;
					if (class28 != null)
						class28.node = new ObjectOnTile(
								class28.interactiveObjUID, k14, j12, i19, l19,
								j18, k20, j17, false);
				}
				if (j16 == 3) {
					GroundDecoration class49 = worldController.getGroundDecoration(i7, j4, plane);
					if (class49 != null)
						class49.node = new ObjectOnTile(class49.groundDecorUID,
								k14, 22, i19, l19, j18, k20, j17, false);
				}
			}
			return;
		}
		if (j == 147) {
			int tileBits = stream.readByteS();
			int x = bigRegionX + (tileBits >> 4 & 7);
			int y = bigRegionY + (tileBits & 7);
			int plrId = stream.readUnsignedWord();
			byte xMax = stream.method430();
			int startTime = stream.readLEShort();
			byte yMax = stream.method429();
			int endTime = stream.readUnsignedWord();
			int objectBits = stream.readByteS();
			int objectType = objectBits >> 2;
			int objectFace = objectBits & 3;
			int typeDecoded = anIntArray1177[objectType];
			byte xMin = stream.readSignedByte();
			int objectId = stream.readUnsignedWord();
			byte yMin = stream.method429();
			Player player;
			if (plrId == playerId)
				player = myPlayer;
			else
				player = playerArray[plrId];
			if (player != null) {
				ObjectDef objectDef = ObjectDef.forID(objectId);
				int mine = intGroundArray[plane][x][y];
				int right = intGroundArray[plane][x + 1][y];
				int upperRight = intGroundArray[plane][x + 1][y + 1];
				int up = intGroundArray[plane][x][y + 1];
				Model model = objectDef.renderObject(objectType, objectFace,
						mine, right, upperRight, up, null, -1);
				if (model != null) {
					createObjectSpawnRequest(endTime + 1, -1, 0, typeDecoded,
							y, 0, plane, x, startTime + 1);
					player.startTimeTransform = startTime + loopCycle;
					player.transformedTimer = endTime + loopCycle;
					player.tranformIntoModel = model;
					int addedSizeY = objectDef.sizeX;
					int addedSizeX = objectDef.sizeY;
					if (objectFace == 1 || objectFace == 3) {
						addedSizeY = objectDef.sizeY;
						addedSizeX = objectDef.sizeX;
					}
					player.resizeX = x * 128 + addedSizeY * 64;
					player.resizeY = y * 128 + addedSizeX * 64;
					player.resizeZ = getFloorDrawHeight(plane, player.resizeY,
							player.resizeX);
					if (xMin > xMax) {
						byte oldMin = xMin;
						xMin = xMax;
						xMax = oldMin;
					}
					if (yMin > yMax) {
						byte oldMin = yMin;
						yMin = yMax;
						yMax = oldMin;
					}
					player.extendedXMin = x + xMin;
					player.extendedXMax = x + xMax;
					player.extendedYMin = y + yMin;
					player.extendedYMax = y + yMax;
				}
			}
		}
		if (j == 151) {
			int tileData = stream.readByteA();
			int x = bigRegionX + (tileData >> 4 & 7);
			int y = bigRegionY + (tileData & 7);
			int objID = stream.readLEShort();
			int typeAndFaceBits = stream.readByteS();
			int obType = typeAndFaceBits >> 2;
			int obFace = typeAndFaceBits & 3;
			int l17 = anIntArray1177[obType];
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				//System.out.println("Object Spawn REQ: " + objID + " - " + l4 + " - " + k7);
				createObjectSpawnRequest(-1, objID, obFace, l17, y, obType, plane, x, 0);
			} else {
				//System.out.println("Cannot spawn object.");
			}
			return;
		}
		if (j == 152) {
			int tileData = stream.readByteA();
			int tileX = bigRegionX + (tileData >> 4 & 7);
			int tileY = bigRegionY + (tileData & 7);
			int objID = stream.readLEShort();
			int typeAndFaceBits = stream.readByteS();
			int obType = typeAndFaceBits >> 2;
			int obFace = typeAndFaceBits & 3;
			int toPlane = stream.readByte();
			int l17 = anIntArray1177[obType];
			if (tileX >= 0 && tileY >= 0 && tileX < 104 && tileY < 104)
				createObjectSpawnRequest(-1, objID, obFace, l17, tileY, obType,
						toPlane, tileX, 0);
			return;
		}

		if (j == 153) {
			int chunkX = stream.readUnsignedByte();
			int chunkY = stream.readUnsignedByte();
			int toPlane = stream.readByte();
			if (toPlane == 10) {
				clearObjectSpawnRequests();
				return;
			}
			GameObjectSpawnRequest request = (GameObjectSpawnRequest) objectSpawnDeque
					.getFront();
			for (; request != null; request = (GameObjectSpawnRequest) objectSpawnDeque
					.getNext()) {
				if (request.tileX >= chunkX * 8
						&& request.tileX <= (chunkX * 8) + 7
						&& request.tileY >= chunkY * 8
						&& request.tileY <= (chunkY * 8) + 7
						&& request.plane == toPlane)
					request.unlink();
			}

			return;
		}
		if (j == 4) {
			int tileBits = stream.readUnsignedByte();
			int x = bigRegionX + (tileBits >> 4 & 7);
			int y = bigRegionY + (tileBits & 7);
			int id = stream.readUnsignedWord();
			int height = stream.readUnsignedByte();
			int time = stream.readUnsignedWord();
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				x = x * 128 + 64;
				y = y * 128 + 64;
				StillGraphic stillGraphi = new StillGraphic(plane, loopCycle,
						time, id, getFloorDrawHeight(plane, y, x) - height, y,
						x);
				stillGraphicDeque.insertBack(stillGraphi);
			}
			return;
		}
		if (j == 44) {
			int k2 = stream.readWordBigEndian();
			int j5 = stream.readUnsignedWord();
			int i8 = stream.readUnsignedByte();
			int x = bigRegionX + (i8 >> 4 & 7);
			int y = bigRegionY + (i8 & 7);
			if (x >= 0 && y >= 0 && x < 104 && y < 104) {
				Item item = new Item();
				item.ID = k2;
				item.amount = j5;
				if (groundArray[plane][x][y] == null)
					groundArray[plane][x][y] = new Deque();
				groundArray[plane][x][y].insertBack(item);
				spawnGroundItem(x, y);
			}
			return;
		}
		if (j == 101) {
			int l2 = stream.readByteC();
			int k5 = l2 >> 2;
			int j8 = l2 & 3;
			if (k5 >= anIntArray1177.length)
				return;
			int i11 = anIntArray1177[k5];
			int j13 = stream.readUnsignedByte();
			int k15 = bigRegionX + (j13 >> 4 & 7);
			int l16 = bigRegionY + (j13 & 7);
			if (k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104)
				createObjectSpawnRequest(-1, -1, j8, i11, l16, k5, plane, k15,
						0);
			return;
		}
		if (j == 117) {
			int tileBits = stream.readUnsignedByte();
			int xTile = bigRegionX + (tileBits >> 4 & 7);
			int yTile = bigRegionY + (tileBits & 7);
			int xToGo = xTile + stream.readSignedByte();
			int yToGo = yTile + stream.readSignedByte();
			int lockOn = stream.readSignedWord();
			int gfxId = stream.readUnsignedWord();
			int startHeight = stream.readUnsignedByte() * 4;
			int endHeight = stream.readUnsignedByte() * 4;
			int time = stream.readUnsignedWord();
			int speed = stream.readUnsignedWord();
			int slopeHeight = stream.readUnsignedByte();
			int radius = stream.readUnsignedByte();
			if (xTile >= 0 && yTile >= 0 && xTile < 104 && yTile < 104
					&& xToGo >= 0 && yToGo >= 0 && xToGo < 104 && yToGo < 104
					&& gfxId != 65535) {
				xTile = xTile * 128 + 64;
				yTile = yTile * 128 + 64;
				xToGo = xToGo * 128 + 64;
				yToGo = yToGo * 128 + 64;
				Projectile projectile = new Projectile(slopeHeight, endHeight,
						time + loopCycle, speed + loopCycle, radius, plane,
						getFloorDrawHeight(plane, yTile, xTile) - startHeight,
						yTile, xTile, lockOn, gfxId);
				projectile.calculateTracking(time + loopCycle, yToGo,
						getFloorDrawHeight(plane, yToGo, xToGo) - endHeight,
						xToGo);
				projectileDeque.insertBack(projectile);
			}
		}
	}

	private static void setLowMem() {
		WorldController.lowMem = true;
		Rasterizer.lowMem = true;
		lowMem = true;
		Region.lowMem = true;
		ObjectDef.lowMem = true;
	}

	private void updateNPCAmount(Stream stream) {
		stream.initBitAccess();
		int npcAmt = stream.readBits(8);
		if (npcAmt < npcCount) {
			for (int l = npcAmt; l < npcCount; l++)
				anIntArray840[entityUpdateCount++] = npcIndices[l];

		}
		if (npcAmt > npcCount) {
			signlink.reporterror(myUsername + " Too many npcs");
			throw new RuntimeException("eek");
		}
		npcCount = 0;
		for (int i1 = 0; i1 < npcAmt; i1++) {
			int j1 = npcIndices[i1];
			NPC npc = npcArray[j1];
			int k1 = stream.readBits(1);
			if (k1 == 0) {
				npcIndices[npcCount++] = j1;
				npc.loopCycle = loopCycle;
			} else {
				int l1 = stream.readBits(2);
				if (l1 == 0) {
					npcIndices[npcCount++] = j1;
					npc.loopCycle = loopCycle;
					playersToUpdate[playersToUpdateCount++] = j1;
				} else if (l1 == 1) {
					npcIndices[npcCount++] = j1;
					npc.loopCycle = loopCycle;
					int i2 = stream.readBits(3);
					npc.moveInDir(false, i2);
					int k2 = stream.readBits(1);
					if (k2 == 1)
						playersToUpdate[playersToUpdateCount++] = j1;
				} else if (l1 == 2) {
					npcIndices[npcCount++] = j1;
					npc.loopCycle = loopCycle;
					int j2 = stream.readBits(3);
					npc.moveInDir(true, j2);
					int l2 = stream.readBits(3);
					npc.moveInDir(true, l2);
					if (npc.desc.type == 13447) {
						npc.moveInDir(true, l2);
						npc.moveInDir(true, l2);
						npc.moveInDir(true, l2);
					}
					int i3 = stream.readBits(1);
					if (i3 == 1)
						playersToUpdate[playersToUpdateCount++] = j1;
				} else if (l1 == 3)
					anIntArray840[entityUpdateCount++] = j1;
			}
		}

	}

	public static int getRandom(int number, boolean greaterThan0) {
		Random random = new Random();
		int randomNr = random.nextInt(number) + (greaterThan0 ? 1 : 0);
		return randomNr;
	}

	private void markMinimap(Sprite sprite, int x, int y) {
		if (sprite == null)
			return;
		try {
			int offX = settings.get("gameframe") == 474 ? 18 : clientSize == 0 ? 0 : clientWidth - 249;
			int angle = viewRotation + minimapRotation & 0x7ff;
			int l = x * x + y * y;
			if (l > 6400) {
				return;
			}
			int i1 = Model.SINE[angle];
			int j1 = Model.COSINE[angle];
			i1 = (i1 * 256) / (minimapZoom + 256);
			j1 = (j1 * 256) / (minimapZoom + 256);
			int k1 = y * i1 + x * j1 >> 16;
			int l1 = y * j1 - x * i1 >> 16;
			if (clientSize == 0)
				sprite.drawSprite(
						((105 + k1) - sprite.maxWidth / 2) + 4 + offX, 88 - l1
								- sprite.maxHeight / 2 - 4);
			else
				sprite.drawSprite(((77 + k1) - sprite.maxWidth / 2) + 4
						+ (clientWidth - 167), 85 - l1 - sprite.maxHeight / 2
						- 4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addRequestedObject(int yTile, int z, int objectFace,
			int requestType, int xTile, int objectType, int objectId) {
		if (xTile >= 1 && yTile >= 1 && xTile <= 102 && yTile <= 102) {
			if (lowMem && z != plane)
				return;
			int uid = 0;
			int obId = 0;
			if (objectType == 0) {
				uid = worldController.getWallObjectUID(z, xTile, yTile);
				obId = worldController.fetchWallObjectNewUID(z, xTile, yTile);
			}
			if (objectType == 1) {
				uid = worldController.getWallDecorationUID(z, xTile, yTile);
				obId = worldController.fetchWallDecorationNewUID(z, xTile,
						yTile);
			}
			if (objectType == 2) {
				uid = worldController.getInteractableObjectUID(z, xTile, yTile);
				obId = worldController.fetchObjectMeshNewUID(z, xTile, yTile);
			}
			if (objectType == 3) {
				obId = worldController.fetchGroundDecorationNewUID(z, xTile,
						yTile);
				uid = worldController.getGroundDecorationUID(z, xTile, yTile);
			}
			if (uid != 0) {
				int uidTag = worldController.getIDTagForXYZ(z, xTile, yTile,
						uid);
				int objectId_1 = obId;
				int k2 = uidTag & 0x1f;
				int l2 = uidTag >> 6;
				if (objectType == 0) {
					worldController.removeWallObject(xTile, z, yTile);
					ObjectDef objectDef = ObjectDef.forID(objectId_1);
					if (objectDef.isUnwalkable)
						clippingPlanes[z].addClip(l2, k2,
								objectDef.walkable, xTile, yTile);
				}
				if (objectType == 1)
					worldController.removeWallDecoration(yTile, z, xTile);
				if (objectType == 2) {
					worldController.removeInteractableObject(z, xTile, yTile);
					ObjectDef objectDef_1 = ObjectDef.forID(objectId_1);
					if (xTile + objectDef_1.sizeX > 103
							|| yTile + objectDef_1.sizeX > 103
							|| xTile + objectDef_1.sizeY > 103
							|| yTile + objectDef_1.sizeY > 103)
						return;
					if (objectDef_1.isUnwalkable)
						clippingPlanes[z].addInteractableObjectClip(l2,
								objectDef_1.sizeX, xTile, yTile,
								objectDef_1.sizeY, objectDef_1.walkable);
				}
				if (objectType == 3) {
					worldController.removeGroundDecoration(z, yTile, xTile);
					ObjectDef objectDef_2 = ObjectDef.forID(objectId_1);
					if (objectDef_2.isUnwalkable && objectDef_2.hasActions)
						clippingPlanes[z].addGroundDecClip(yTile, xTile);
				}
			}
			if (objectId >= 0) {
				int j3 = z;
				if (j3 < 3 && (tileSettingBits[1][xTile][yTile] & 2) == 2)
					j3++;
				Region.addObject(worldController, objectFace, yTile,
						requestType, j3, clippingPlanes[z], intGroundArray,
						xTile, objectId, z);
			}
		}
	}

	private void updatePlayers(int i, Stream stream) {
		entityUpdateCount = 0;
		playersToUpdateCount = 0;
		updatePlayerMovement(stream);
		updatePlayer(stream);
		updatePlayerMovement(stream, i);
		processPlayerUpdating(stream);
		for (int k = 0; k < entityUpdateCount; k++) {
			int l = anIntArray840[k];
			if (playerArray[l].loopCycle != loopCycle)
				playerArray[l] = null;
		}

		if (stream.currentOffset != i) {
			signlink.reporterror("Error packet size mismatch in getplayer pos:"
					+ stream.currentOffset + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for (int i1 = 0; i1 < playerCount; i1++)
			if (playerArray[playerIndices[i1]] == null) {
				signlink.reporterror(myUsername
						+ " null entry in pl list - pos:" + i1 + " size:"
						+ playerCount);
				throw new RuntimeException("eek");
			}

	}

	private void setCameraPos(int j, int k, int l, int i1, int j1, int k1) {
		int l1 = 2048 - k & 0x7ff;
		int i2 = 2048 - j1 & 0x7ff;
		int j2 = 0;
		int k2 = 0;
		int l2 = j;
		if (l1 != 0) {
			int i3 = Model.SINE[l1];
			int k3 = Model.COSINE[l1];
			int i4 = k2 * k3 - l2 * i3 >> 16;
			l2 = k2 * i3 + l2 * k3 >> 16;
			k2 = i4;
		}
		if (i2 != 0) {
			/*
			 * xxx if(cameratoggle){ if(zoom == 0) zoom = k2; if(lftrit == 0)
			 * lftrit = j2; if(fwdbwd == 0) fwdbwd = l2; k2 = zoom; j2 = lftrit;
			 * l2 = fwdbwd; }
			 */
			int j3 = Model.SINE[i2];
			int l3 = Model.COSINE[i2];
			int j4 = l2 * j3 + j2 * l3 >> 16;
			l2 = l2 * l3 - j2 * j3 >> 16;
			j2 = j4;
		}
		xCameraPos = l - j2;
		zCameraPos = i1 - k2;
		yCameraPos = k1 - l2;
		yCameraCurve = k;
		xCameraCurve = j1;
	}

	public void updateStrings(String str, int i) {
		switch (i) {
		case 1675:
			sendFrame126(str, 17508);
			break;// Stab
		case 1676:
			sendFrame126(str, 17509);
			break;// Slash
		case 1677:
			sendFrame126(str, 17510);
			break;// Cursh
		case 1678:
			sendFrame126(str, 17511);
			break;// Magic
		case 1679:
			sendFrame126(str, 17512);
			break;// Range
		case 1680:
			sendFrame126(str, 17513);
			break;// Stab
		case 1681:
			sendFrame126(str, 17514);
			break;// Slash
		case 1682:
			sendFrame126(str, 17515);
			break;// Crush
		case 1683:
			sendFrame126(str, 17516);
			break;// Magic
		case 1684:
			sendFrame126(str, 17517);
			break;// Range
		case 1686:
			sendFrame126(str, 17518);
			break;// Strength
		case 1687:
			sendFrame126(str, 17519);
			break;// Prayer
		}
	}

	public void sendFrame126(String str, int i) {
		if (RSInterface.interfaceCache[i] == null)
			return;
		RSInterface.interfaceCache[i].message = str;
		if (RSInterface.interfaceCache[i].parentID == tabInterfaceIDs[tabID])
			needDrawTabArea = true;
	}

	public void sendPacket185(int buttonID) {
		stream.createFrame(185);
		stream.writeWord(buttonID);
		RSInterface rsi = RSInterface.interfaceCache[buttonID];
		if (rsi == null)
			return;
		if (rsi.valueIndexArray != null && rsi.valueIndexArray[0][0] == 5) {
			int configID = rsi.valueIndexArray[0][1];
			variousSettings[configID] = 1 - variousSettings[configID];
			handleActions(configID);
			needDrawTabArea = true;
		}
	}

	public void sendPacket185(int button, int toggle, int type) {
		switch (type) {
		case 135:
			RSInterface class9 = RSInterface.interfaceCache[button];
			boolean flag8 = true;
			if (class9.contentType > 0)
				flag8 = promptUserForInput(class9);
			if (flag8) {
				stream.createFrame(185);
				stream.writeWord(button);
			}
			break;
		case 646:
			stream.createFrame(185);
			stream.writeWord(button);
			RSInterface class9_2 = RSInterface.interfaceCache[button];
			if (class9_2.valueIndexArray != null
					&& class9_2.valueIndexArray[0][0] == 5) {
				if (variousSettings[toggle] != class9_2.requiredValues[0]) {
					variousSettings[toggle] = class9_2.requiredValues[0];
					handleActions(toggle);
					needDrawTabArea = true;
				}
			}
			break;
		case 169:
			stream.createFrame(185);
			stream.writeWord(button);
			RSInterface clickedInterface = RSInterface.interfaceCache[button];
			if (clickedInterface.valueIndexArray != null
					&& clickedInterface.valueIndexArray[0][0] == 5) {
				variousSettings[toggle] = 1 - variousSettings[toggle];
				handleActions(toggle);
				needDrawTabArea = true;
			}
			switch (button) {
			case 19136:
				if (toggle == 0)
					sendFrame36(173, toggle);
				if (toggle == 1)
					sendPacket185(153, 173, 646);
				break;
			}
			break;
		}
	}

	public void sendFrame36(int id, int state) {
		varbitConfigs[id] = state;
		if (variousSettings[id] != state) {
			variousSettings[id] = state;
			handleActions(id);
			needDrawTabArea = true;
			if (dialogID != -1)
				inputTaken = true;
		}
	}

	public void sendFrame219() {
		if (invOverlayInterfaceID != -1) {
			invOverlayInterfaceID = -1;
			needDrawTabArea = true;
			tabAreaAltered = true;
		}
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
		}
		if (inputDialogState != 0) {
			inputDialogState = 0;
			inputTaken = true;
		}
		openInterfaceID = -1;
		dialogueOptionsShowing = false;
	}

	public void sendFrame248(int interfaceID, int sideInterfaceID) {
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
		}
		if (inputDialogState != 0) {
			inputDialogState = 0;
			inputTaken = true;
		}
		openInterfaceID = interfaceID;
		invOverlayInterfaceID = sideInterfaceID;
		needDrawTabArea = true;
		tabAreaAltered = true;
		dialogueOptionsShowing = false;
	}

	private boolean parsePacket() {
		//System.out.println("Parsing packet...");
		if (socketStream == null)
			return false;
		try {
			int i = socketStream.available();
			if (i == 0)
				return false;
			if (opCode == -1) {
				socketStream.flushInputStream(inStream.buffer, 1);
				opCode = inStream.buffer[0] & 0xff;
				if (encryption != null)
					opCode = opCode - encryption.getNextKey() & 0xff;
				pktSize = SizeConstants.packetSizes[opCode];
				i--;
			}
			if (pktSize == -1)
				if (i > 0) {
					socketStream.flushInputStream(inStream.buffer, 1);
					pktSize = inStream.buffer[0] & 0xff;
					i--;
				} else {
					return false;
				}
			if (pktSize == -2)
				if (i > 1) {
					socketStream.flushInputStream(inStream.buffer, 2);
					inStream.currentOffset = 0;
					pktSize = inStream.readUnsignedWord();
					i -= 2;
				} else {
					return false;
				}
			if (i < pktSize)
				return false;
			inStream.currentOffset = 0;
			socketStream.flushInputStream(inStream.buffer, pktSize);
			netIdleCycles = 0;
			opcode_second = opcode_last;
			opcode_last = anInt841;
			anInt841 = opCode;
			//System.out.println("Income packet: " + opCode);
			switch (opCode) {
			case 81:
				//System.out.println("Packet 81 Size: " + pktSize);
				updatePlayers(pktSize, inStream);
				loadingMap = false;
				opCode = -1;
				return true;

			case 179:
				glowColor = inStream.readUnsignedByte();
				opCode = -1;
				return true;
			case 172:
				try {
					boolean active = inStream.readByte() == 1;
					String special = "";
					if (active)
						special = inStream.readString();
					getFamiliar().setActive(active);
					getFamiliar().setFamiliar("", special);
				} catch (Exception e) {
					e.printStackTrace();
				}
				opCode = -1;
				return true;
			case 161:
				int frameId = inStream.readUnsignedWord();
				RSInterface rsi = RSInterface.interfaceCache[frameId];
				int color = inStream.readUnsignedByte();
				int height = inStream.readUnsignedWord();
				if (color == 0) {
					color = 0xFF2323;
				}
				if (color == 1) {
					color = 0x3366FF;
				}
				if (color == 2) {
					color = 0xF0F0F0;
				}
				if (color != 10)
					rsi.disabledColor = color;
				rsi.height = height;
				opCode = -1;
				return true;
			case 198:
				printedMessage = inStream.readString();
				if (loggerEnabled) {
					if (printedMessage.contains(".java:"))
						Logger.addLog("Error", printedMessage, Color.RED,
								Color.ORANGE);
					else
						Logger.addLog("Info", printedMessage, Color.GREEN,
								Color.CYAN);
				}
				System.out.println(printedMessage);
				opCode = -1;
				return true;

			case 176:
				daysSinceRecovChange = inStream.readByteC();
				unreadMessages = inStream.readShortA();
				membersInt = inStream.readUnsignedByte();
				anInt1193 = inStream.method440();
				daysSinceLastLogin = inStream.readUnsignedWord();
				if (anInt1193 != 0 && openInterfaceID == -1) {
					signlink.dnslookup(TextClass.method586(anInt1193));
					clearTopInterfaces();
					char c = '\u028A';
					if (daysSinceRecovChange != 201 || membersInt == 1)
						c = '\u028F';
					reportAbuseInput = "";
					canMute = false;
					for (int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++) {
						if (RSInterface.interfaceCache[k9] == null
								|| RSInterface.interfaceCache[k9].contentType != c)
							continue;
						openInterfaceID = RSInterface.interfaceCache[k9].parentID;

					}
				}
				opCode = -1;
				return true;

			case 64:
				bigRegionX = inStream.readByteC();
				bigRegionY = inStream.readByteS();
				for (int j = bigRegionX; j < bigRegionX + 8; j++) {
					for (int l9 = bigRegionY; l9 < bigRegionY + 8; l9++)
						if (groundArray[plane][j][l9] != null) {
							groundArray[plane][j][l9] = null;
							spawnGroundItem(j, l9);
						}
				}
				for (GameObjectSpawnRequest class30_sub1 = (GameObjectSpawnRequest) objectSpawnDeque
						.getFront(); class30_sub1 != null; class30_sub1 = (GameObjectSpawnRequest) objectSpawnDeque
						.getNext())
					if (class30_sub1.tileX >= bigRegionX
							&& class30_sub1.tileX < bigRegionX + 8
							&& class30_sub1.tileY >= bigRegionY
							&& class30_sub1.tileY < bigRegionY + 8
							&& class30_sub1.plane == plane)
						class30_sub1.removeTime = 0;
				opCode = -1;
				return true;
				
			/*case 133:
				int skillID = inStream.readShort();
				int gainedXP = inStream.readDWord();
				this.addXP(skillID, gainedXP);
				opCode = -1;
				return true;*/

			case 185:
				int k = inStream.readWordBigEndian();
				RSInterface.interfaceCache[k].mediaType = 3;
				if (myPlayer.desc == null)
					RSInterface.interfaceCache[k].mediaID = (myPlayer.anIntArray1700[0] << 25)
							+ (myPlayer.anIntArray1700[4] << 20)
							+ (myPlayer.equipment[0] << 15)
							+ (myPlayer.equipment[8] << 10)
							+ (myPlayer.equipment[11] << 5)
							+ myPlayer.equipment[1];
				else
					RSInterface.interfaceCache[k].mediaID = (int) (0x12345678L + myPlayer.desc.type);
				opCode = -1;
				return true;

				/* Clan chat packet */
			case 217:
				try {
					System.out.println("packet 217");
					name = inStream.readString();
					message = inStream.readString();
					clanname = inStream.readString();
					rights = inStream.readUnsignedWord();
					message = TextInput.processText(message);
					message = Censor.doCensor(message);
					// System.out.println(clanname);
					pushMessage(message, 16, name);
				} catch (Exception e) {
					e.printStackTrace();
				}
				opCode = -1;
				return true;

			case 107:
				inCutScene = false;
				for (int l = 0; l < 5; l++)
					cameraEffectEnabled[l] = false;
				xpCounter = 0;
				opCode = -1;
				return true;

			case 72:
				int i1 = inStream.readLEShort();
				RSInterface class9 = RSInterface.interfaceCache[i1];
				if (class9.inv == null) {
					opCode = -1;
					return true;
				}
				for (int k15 = 0; k15 < class9.inv.length; k15++) {
					class9.inv[k15] = -1;
					class9.inv[k15] = 0;
				}
				opCode = -1;
				return true;

			case 124:
				int skillID = inStream.readShort();
				int gainedXP = inStream.readDWord();
				int totalEXP = inStream.readDWord();
				addXP(skillID, gainedXP);
				totalXP = totalEXP;
				opCode = -1;
				return true;

			case 214:
				ignoreCount = pktSize / 8;
				for (int j1 = 0; j1 < ignoreCount; j1++)
					ignoreListAsLongs[j1] = inStream.readQWord();
				opCode = -1;
				return true;

			case 166:
				inStream.readUnsignedByte();
				int type = inStream.readUnsignedByte();
				int slot = inStream.readUnsignedByte();
				if (type == 1) {
					slotColor[slot] = inStream.readUnsignedByte();
				} else if (type == 2) {
					slotColorPercent[slot] = inStream.readUnsignedByte();
				} else if (type == 3) {
					int lololol = inStream.readUnsignedByte();
					if (lololol == 1) {
						slotAborted[slot] = true;
					} else {
						slotAborted[slot] = false;
					}
				} else if (type == 4) {
					int thing = inStream.readUnsignedByte();
					if (thing == 1) {
						buttonclicked = false;
						interfaceButtonAction = 0;
					} else if (thing == 2) {
						slotSelected = slot;
					} else if (thing == 3) {
						slots[slot] = "";
						Slots[slot] = 0;
					}
				} else if (type == 5) {
					int thing1 = inStream.readUnsignedByte();
					if (thing1 == 1) {
						slotUsing = slot;
						slots[slot] = "Sell";
						Slots[slot] = 1;
					} else if (thing1 == 2) {
						slotUsing = slot;
						slots[slot] = "Buy";
						Slots[slot] = 1;
					} else if (thing1 == 3) {
						Slots[slot] = 2;
						slots[slot] = "Sell";
					} else if (thing1 == 4) {
						Slots[slot] = 2;
						slots[slot] = "Buy";
					} else if (thing1 == 5) {
						Slots[slot] = 3;
						slots[slot] = "Sell";
					} else if (thing1 == 6) {
						Slots[slot] = 3;
						slots[slot] = "Buy";
					}
				} else if (type == 6) {
					inStream.readUnsignedByte();
					buttonclicked = true;
					amountOrNameInput = "";
					totalItemResults = 0;
				} else if (type == 7) {
					int anInt1308 = inStream.readUnsignedByte();
					resetAnAnim(anInt1308);
				} else {
					inStream.readUnsignedByte();
				}
				inStream.readUnsignedByte();
				inStream.readUnsignedByte();
				opCode = -1;
				return true;

			case 134:
				needDrawTabArea = true;
				int statId = inStream.readUnsignedByte();
				int newXP = inStream.method439();
				int currentLevel = inStream.readUnsignedByte();
				currentExp[statId] = newXP;
				currentStats[statId] = currentLevel;
				currentMaxStats[statId] = 1;
				for (int lvlIdx = 0; lvlIdx < 98; lvlIdx++)
					if (newXP >= levelXPs[lvlIdx])
						currentMaxStats[statId] = lvlIdx + 2;
				opCode = -1;
				return true;

			case 175:
				int soundId = inStream.readShort();
				int sType = inStream.readUByte();
				int delay = inStream.readShort();
				int volume = inStream.readShort();
				sound[currentSound] = soundId;
				soundType[currentSound] = sType;
				soundDelay[currentSound] = delay
						+ Sounds.anIntArray326[soundId];
				soundVolume[currentSound] = volume;
				currentSound++;
				opCode = -1;
				return true;
			case 71:
				int l1 = inStream.readUnsignedWord();
				int j10 = inStream.readByteA();
				if (l1 == 65535)
					l1 = -1;
				tabInterfaceIDs[j10] = l1;
				needDrawTabArea = true;
				tabAreaAltered = true;
				opCode = -1;
				return true;

			case 74:
				int songID = inStream.readLEShort();
				if (songID == 65535) {
					songID = -1;
				}
				if (songID != currentSong && musicEnabled && !lowMem
						&& prevSong == 0) {
					nextSong = songID;
					songChanging = true;
					onDemandFetcher.requestFileData(2, nextSong);
				}
				currentSong = songID;
				opCode = -1;
				return true;

			case 121:
				int j2 = inStream.readWordBigEndian();
				int k10 = inStream.readShortA();
				if (musicEnabled && !lowMem) {
					nextSong = j2;
					songChanging = false;
					onDemandFetcher.requestFileData(2, nextSong);
					prevSong = k10;
				}
				opCode = -1;
				return true;

			case 109:
				resetLogout();
				opCode = -1;
				return false;

			case 70:
				int x_off = inStream.readSignedWord();
				int y_off = inStream.readLEShort();
				int interface_id = inStream.readLEShort();
				RSInterface rsi_select = RSInterface.interfaceCache[interface_id];
				if(rsi_select == null) {
					opCode = -1;
					return true;
				}
				rsi_select.xOffset = x_off;
				rsi_select.yOffset = y_off;
				opCode = -1;
				return true;

			case 73:
			case 241:
				int regionX = currentRegionX;
				int regionY = currentRegionY;
				if (opCode == 73) {
					regionX = inStream.readShortA();
					regionY = inStream.readUnsignedWord();
					requestMapReconstruct = false;
				}
				if (opCode == 241) {
					regionY = inStream.readShortA();
					inStream.initBitAccess();
					for (int z = 0; z < 4; z++) {
						for (int x = 0; x < 13; x++) {
							for (int y = 0; y < 13; y++) {
								int i26 = inStream.readBits(1);
								if (i26 == 1)
									constructRegionData[z][x][y] = inStream.readBits(26);
								else
									constructRegionData[z][x][y] = -1;
							}
						}
					}
					inStream.finishBitAccess();
					regionX = inStream.readUnsignedWord();
					requestMapReconstruct = true;
				}
				if (opCode != 241 && currentRegionX == regionX
						&& currentRegionY == regionY && loadingStage == 2) {
					opCode = -1;
					return true;
				}
				currentRegionX = regionX;
				currentRegionY = regionY;
				baseX = (currentRegionX - 6) * 8;
				baseY = (currentRegionY - 6) * 8;
				inTutorialIsland = (currentRegionX / 8 == 48 || currentRegionX / 8 == 49)
						&& currentRegionY / 8 == 48;
				if (currentRegionX / 8 == 48 && currentRegionY / 8 == 148)
					inTutorialIsland = true;
				loadingStage = 1;
				mapLoadingTime = System.currentTimeMillis();
				gameScreenIP.initDrawingArea();
				if (opCode == 73) {
					int k16 = 0;
					for (int i21 = (currentRegionX - 6) / 8; i21 <= (currentRegionX + 6) / 8; i21++) {
						for (int k23 = (currentRegionY - 6) / 8; k23 <= (currentRegionY + 6) / 8; k23++)
							k16++;
					}
					terrainData = new byte[k16][];
					objectData = new byte[k16][];
					mapCoordinates = new int[k16];
					terrainIndices = new int[k16];
					objectIndices = new int[k16];
					k16 = 0;
					for (int x_region = (currentRegionX - 6) / 8; x_region <= (currentRegionX + 6) / 8; x_region++) {
						for (int y_region = (currentRegionY - 6) / 8; y_region <= (currentRegionY + 6) / 8; y_region++) {
							mapCoordinates[k16] = (x_region << 8) + y_region;
							if (inTutorialIsland && (y_region == 49 || y_region == 149 || y_region == 147 || x_region == 50 || x_region == 49 && y_region == 47)) {
								terrainIndices[k16] = -1;
								objectIndices[k16] = -1;
								k16++;
							} else {
								int terrain_id = terrainIndices[k16] = onDemandFetcher.getMapIdForRegions(0, y_region, x_region);
								if (terrain_id != -1)
									onDemandFetcher.requestFileData(3, terrain_id);
								int landscape_id = objectIndices[k16] = onDemandFetcher.getMapIdForRegions(1, y_region, x_region);
								if (landscape_id != -1)
									onDemandFetcher.requestFileData(3, landscape_id);
								k16++;
							}
						}
					}
				}
				if (opCode == 241) {
					int totalLegitChunks = 0;
					int totalChunks[] = new int[676];
					for (int z = 0; z < 4; z++) {
						for (int x = 0; x < 13; x++) {
							for (int y = 0; y < 13; y++) {
								int tileBits = constructRegionData[z][x][y];
								if (tileBits != -1) {
									int xCoord = tileBits >> 14 & 0x3ff;
									int yCoord = tileBits >> 3 & 0x7ff;
									int mapRegion = (xCoord / 8 << 8) + yCoord
											/ 8;
									for (int idx = 0; idx < totalLegitChunks; idx++) {
										if (totalChunks[idx] != mapRegion)
											continue;
										mapRegion = -1;

									}
									if (mapRegion != -1)
										totalChunks[totalLegitChunks++] = mapRegion;
								}
							}
						}
					}
					terrainData = new byte[totalLegitChunks][];
					objectData = new byte[totalLegitChunks][];
					mapCoordinates = new int[totalLegitChunks];
					terrainIndices = new int[totalLegitChunks];
					objectIndices = new int[totalLegitChunks];
					for (int idx = 0; idx < totalLegitChunks; idx++) {
						int region = mapCoordinates[idx] = totalChunks[idx];
						int region_x = region >> 8 & 0xff;
						int region_y = region & 0xff;
						int terrainMapId = terrainIndices[idx] = onDemandFetcher.getMapIdForRegions(0, region_y, region_x);
						if (terrainMapId != -1)
							onDemandFetcher.requestFileData(3, terrainMapId);
						int objectMapId = objectIndices[idx] = onDemandFetcher.getMapIdForRegions(1, region_y, region_x);
						if (objectMapId != -1)
							onDemandFetcher.requestFileData(3, objectMapId);
					}
				}
				int i17 = baseX - anInt1036;
				int j21 = baseY - anInt1037;
				anInt1036 = baseX;
				anInt1037 = baseY;
				for (int npcIdx = 0; npcIdx < 16384; npcIdx++) {
					NPC npc = npcArray[npcIdx];
					if (npc != null) {
						for (int j29 = 0; j29 < 10; j29++) {
							npc.pathX[j29] -= i17;
							npc.pathY[j29] -= j21;
						}
						npc.x -= i17 * 128;
						npc.y -= j21 * 128;
					}
				}
				for (int plrIdx = 0; plrIdx < maxPlayers; plrIdx++) {
					Player player = playerArray[plrIdx];
					if (player != null) {
						for (int i31 = 0; i31 < 10; i31++) {
							player.pathX[i31] -= i17;
							player.pathY[i31] -= j21;
						}
						player.x -= i17 * 128;
						player.y -= j21 * 128;
					}
				}
				loadingMap = true;
				byte minX = 0;
				byte endX = 104;
				byte incrementX = 1;
				if (i17 < 0) {
					minX = 103;
					endX = -1;
					incrementX = -1;
				}
				byte minY = 0;
				byte endY = 104;
				byte incrementY = 1;
				if (j21 < 0) {
					minY = 103;
					endY = -1;
					incrementY = -1;
				}
				for (int x = minX; x != endX; x += incrementX) {
					for (int y = minY; y != endY; y += incrementY) {
						int xTile = x + i17;
						int yTile = y + j21;
						for (int plane = 0; plane < 4; plane++)
							if (xTile >= 0 && yTile >= 0 && xTile < 104 && yTile < 104)
								groundArray[plane][x][y] = groundArray[plane][xTile][yTile];
							else
								groundArray[plane][x][y] = null;
					}
				}
				for (GameObjectSpawnRequest request = (GameObjectSpawnRequest) objectSpawnDeque.getFront(); request != null;
						request = (GameObjectSpawnRequest) objectSpawnDeque.getNext()) {
					request.tileX -= i17;
					request.tileY -= j21;
					if (request.tileX < 0 || request.tileY < 0 || request.tileX >= 104 || request.tileY >= 104)
						request.unlink();
				}
				if (destX != 0) {
					destX -= i17;
					destY -= j21;
				}
				inCutScene = false;
				opCode = -1;
				return true;

			case 208:
				int i3 = inStream.method437();
				if (i3 >= 0)
					resetInterfaceAnimation(i3);
				walkableInterfaceId = i3;
				opCode = -1;
				return true;

			case 99:
				minimapStatus = inStream.readUnsignedByte();
				opCode = -1;
				return true;

			case 75:
				int mediaId = inStream.readWordBigEndian();
				int j11 = inStream.readWordBigEndian();
				RSInterface.interfaceCache[j11].mediaType = 2;
				RSInterface.interfaceCache[j11].mediaID = mediaId;
				opCode = -1;
				return true;

			case 114:
				updateMinutes = inStream.readLEShort() * 30;
				opCode = -1;
				return true;

			case 60:
				bigRegionY = inStream.readUnsignedByte();
				bigRegionX = inStream.readByteC();
				while (inStream.currentOffset < pktSize) {
					int k3 = inStream.readUnsignedByte();
					parseEntityPacket(inStream, k3);
				}
				opCode = -1;
				return true;

			case 35:
				int screenStateIdx = inStream.readUnsignedByte();
				int k11 = inStream.readUnsignedByte();
				int j17 = inStream.readUnsignedByte();
				int k21 = inStream.readUnsignedByte();
				cameraEffectEnabled[screenStateIdx] = true;
				anIntArray873[screenStateIdx] = k11;
				anIntArray1203[screenStateIdx] = j17;
				anIntArray928[screenStateIdx] = k21;
				cameraEffectCycles[screenStateIdx] = 0;
				opCode = -1;
				return true;

			case 174:
				followPlayer = 0;
				followNPC = 0;
				int l11z = inStream.readUnsignedWord();
				int iq = inStream.readUnsignedByte();
				followDistance = inStream.readUnsignedWord();
				if (iq == 0) {
					followNPC = l11z;
				} else if (iq == 1) {
					followPlayer = l11z;
				}
				opCode = -1;
				return true;
			case 178:
				boolean active = inStream.readByte() == 1;
				drawPane = active;
				opCode = -1;
				return true;

			case 104:
				int optionId = inStream.readByteC();
				int i12 = inStream.readByteA();
				String ptionContent = inStream.readString();
				if (optionId >= 1 && optionId <= 6) {
					if (ptionContent.equalsIgnoreCase("null"))
						ptionContent = null;
					atPlayerActions[optionId - 1] = ptionContent;
					atPlayerArray[optionId - 1] = i12 == 0;
				}
				opCode = -1;
				return true;

			case 78:
				destX = 0;
				opCode = -1;
				return true;

			case 253:
				String s = inStream.readString();
				if (s.endsWith(":tradereq:")) {
					String s3 = s.substring(0, s.indexOf(":"));
					long l17 = TextClass.longForName(s3);
					boolean flag2 = false;
					for (int j27 = 0; j27 < ignoreCount; j27++) {
						if (ignoreListAsLongs[j27] != l17)
							continue;
						flag2 = true;

					}
					if (!flag2 && isOnTutorialIsland == 0)
						pushMessage("wishes to trade with you.", 4, s3);
				} else if (s.endsWith(":clanreq:")) {
					String name = s.substring(0, s.indexOf(":"));
					long nameLong = TextClass.longForName(name);
					boolean ignore = false;
					for (int ignoreIndex = 0; ignoreIndex < ignoreCount; ignoreIndex++) {
						if (ignoreListAsLongs[ignoreIndex] != nameLong)
							continue;
						ignore = true;

					}
					if (!ignore && isOnTutorialIsland == 0)
						pushMessage("has invited you to their clan.", 17, name);
				} else if (s.endsWith(":warreq:")) {
					String name = s.substring(0, s.indexOf(":"));
					long nameLong = TextClass.longForName(name);
					boolean ignore = false;
					for (int ignoreIndex = 0; ignoreIndex < ignoreCount; ignoreIndex++) {
						if (ignoreListAsLongs[ignoreIndex] != nameLong)
							continue;
						ignore = true;

					}
					if (!ignore && isOnTutorialIsland == 0)
						pushMessage("has challenged you to a clan war.", 18, name);
				} else if (s.endsWith("::")) {
					String s4 = s.substring(0, s.indexOf(":"));
					TextClass.longForName(s4);
					pushMessage("Clan: ", 8, s4);
				} else if (s.endsWith("#url#")) {
					String link = s.substring(0, s.indexOf("#"));
					pushMessage("Join us at: ", 9, link);
				} else if (s.endsWith(":duelreq:")) {
					String s4 = s.substring(0, s.indexOf(":"));
					long l18 = TextClass.longForName(s4);
					boolean flag3 = false;
					for (int k27 = 0; k27 < ignoreCount; k27++) {
						if (ignoreListAsLongs[k27] != l18)
							continue;
						flag3 = true;

					}
					if (!flag3 && isOnTutorialIsland == 0)
						pushMessage("wishes to duel with you.", 8, s4);
				} else if (s.endsWith(":chalreq:")) {
					String s5 = s.substring(0, s.indexOf(":"));
					long l19 = TextClass.longForName(s5);
					boolean flag4 = false;
					for (int l27 = 0; l27 < ignoreCount; l27++) {
						if (ignoreListAsLongs[l27] != l19)
							continue;
						flag4 = true;

					}
					if (!flag4 && isOnTutorialIsland == 0) {
						String s8 = s.substring(s.indexOf(":") + 1,
								s.length() - 9);
						pushMessage(s8, 8, s5);
					}
				} else if (s.endsWith(":prayer:curses")) {
					prayerBook = "Curses";
				} else if (s.endsWith(":prayer:prayers")) {
					prayerBook = "Prayers";
				} else if (s.endsWith(":quicks:off")) {
					prayClicked = false;
				} else if (s.endsWith(":quicks:on")) {
					prayClicked = true;
				} else if (s.endsWith(":resetautocast:")) {
					Autocast = false;
					autocastId = 0;
					SpriteCache.spriteCache[47].drawSprite(1000, 1000);
				} else {
					pushMessage(s, 0, "");
				}
				opCode = -1;
				return true;

			case 1:
				for (int k4 = 0; k4 < playerArray.length; k4++)
					if (playerArray[k4] != null)
						playerArray[k4].anim = -1;
				for (int j12 = 0; j12 < npcArray.length; j12++)
					if (npcArray[j12] != null)
						npcArray[j12].anim = -1;
				opCode = -1;
				return true;

			case 50:
				long l4 = inStream.readQWord();
				int i18 = inStream.readUnsignedByte();
				String s7 = TextClass.fixName(TextClass.nameForLong(l4));
				for (int k24 = 0; k24 < friendsCount; k24++) {
					if (l4 != friendsListAsLongs[k24])
						continue;
					if (friendsNodeIDs[k24] != i18) {
						friendsNodeIDs[k24] = i18;
						needDrawTabArea = true;
						if (i18 >= 2) {
							pushMessage(s7 + " has logged in.", 5, "");
						}
						if (i18 <= 1) {
							pushMessage(s7 + " has logged out.", 5, "");
						}
					}
					s7 = null;

				}
				if (s7 != null && friendsCount < 200) {
					friendsListAsLongs[friendsCount] = l4;
					friendsList[friendsCount] = s7;
					friendsNodeIDs[friendsCount] = i18;
					friendsCount++;
					needDrawTabArea = true;
				}
				for (boolean flag6 = false; !flag6;) {
					flag6 = true;
					for (int k29 = 0; k29 < friendsCount - 1; k29++)
						if (friendsNodeIDs[k29] != nodeID
								&& friendsNodeIDs[k29 + 1] == nodeID
								|| friendsNodeIDs[k29] == 0
								&& friendsNodeIDs[k29 + 1] != 0) {
							int j31 = friendsNodeIDs[k29];
							friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
							friendsNodeIDs[k29 + 1] = j31;
							String s10 = friendsList[k29];
							friendsList[k29] = friendsList[k29 + 1];
							friendsList[k29 + 1] = s10;
							long l32 = friendsListAsLongs[k29];
							friendsListAsLongs[k29] = friendsListAsLongs[k29 + 1];
							friendsListAsLongs[k29 + 1] = l32;
							needDrawTabArea = true;
							flag6 = false;
						}
				}
				opCode = -1;
				return true;

			case 110:
				if (tabID == 12)
					needDrawTabArea = true;
				currentEnergy = inStream.readUByte();
				opCode = -1;
				return true;

			case 254:
				markType = inStream.readUnsignedByte();
				if (markType == 1)
					markedNPC = inStream.readUnsignedWord();
				if (markType >= 2 && markType <= 6) {
					if (markType == 2) {
						anInt937 = 64;
						anInt938 = 64;
					}
					if (markType == 3) {
						anInt937 = 0;
						anInt938 = 64;
					}
					if (markType == 4) {
						anInt937 = 128;
						anInt938 = 64;
					}
					if (markType == 5) {
						anInt937 = 64;
						anInt938 = 0;
					}
					if (markType == 6) {
						anInt937 = 64;
						anInt938 = 128;
					}
					markType = 2;
					markedX = inStream.readUnsignedWord();
					markedY = inStream.readUnsignedWord();
					anInt936 = inStream.readUnsignedByte();
				}
				if (markType == 10)
					markedPlayer = inStream.readUnsignedWord();
				opCode = -1;
				return true;

			case 248:
				int i5 = inStream.readShortA();
				int k12 = inStream.readUnsignedWord();
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					inputTaken = true;
				}
				openInterfaceID = i5;
				invOverlayInterfaceID = k12;
				needDrawTabArea = true;
				tabAreaAltered = true;
				dialogueOptionsShowing = false;
				opCode = -1;
				return true;

			case 79:
				int j5 = inStream.readLEShort();
				int l12 = inStream.readShortA();
				RSInterface class9_3 = RSInterface.interfaceCache[j5];
				if (class9_3 != null && class9_3.type == 0) {
					if (l12 < 0)
						l12 = 0;
					if (l12 > class9_3.scrollMax - class9_3.height)
						l12 = class9_3.scrollMax - class9_3.height;
					class9_3.scrollPosition = l12;
				}
				opCode = -1;
				return true;

			case 68:
				for (int k5 = 0; k5 < variousSettings.length; k5++)
					if (variousSettings[k5] != varbitConfigs[k5]) {
						variousSettings[k5] = varbitConfigs[k5];
						handleActions(k5);
						needDrawTabArea = true;
					}
				opCode = -1;
				return true;

			case 196:
				long l5 = inStream.readQWord();
				inStream.readDWord();
				int playerRights = inStream.readUnsignedByte();
				boolean flag5 = false;
				if (playerRights <= 1) {
					for (int l29 = 0; l29 < ignoreCount; l29++) {
						if (ignoreListAsLongs[l29] != l5)
							continue;
						flag5 = true;

					}
				}
				if (!flag5 && isOnTutorialIsland == 0)
					try {
						String message = TextInput.decodeToString(pktSize - 13,
								inStream);
						if (playerRights != 0) {
							pushMessage(
									message,
									7,
									getPrefix(playerRights)
											+ TextClass.fixName(TextClass
													.nameForLong(l5)));
						} else {
							pushMessage(message, 3, TextClass.fixName(TextClass
									.nameForLong(l5)));
						}
					} catch (Exception exception1) {
						signlink.reporterror("cde1");
					}
				opCode = -1;
				return true;
			case 85:
				bigRegionY = inStream.readByteC();
				bigRegionX = inStream.readByteC();
				opCode = -1;
				return true;

			case 24:
				flashingSidebarTab = inStream.readByteS();
				if (flashingSidebarTab == tabID) {
					if (flashingSidebarTab == 3)
						tabID = 1;
					else
						tabID = 3;
					needDrawTabArea = true;
				}
				opCode = -1;
				return true;

			case 28:
				playerCommand = inStream.readUnsignedByte();
				showInput = false;
				inputDialogState = 5;
				amountOrNameInput = "";
				inputTaken = true;
				opCode = -1;
				return true;
			case 29:
				int value = inStream.readUnsignedByte();
				shadowProcessing = true;
				if (value == 250 || value == 0)
					value = 255;
				shadowDestination = value;
				opCode = -1;
				return true;
			case 246:
				int i6 = inStream.readLEShort();
				int i13 = inStream.readUnsignedWord();
				int k18 = inStream.readUnsignedWord();
				if (k18 == 65535) {
					RSInterface.interfaceCache[i6].mediaType = 0;
					opCode = -1;
					return true;
				} else {
					ItemDef itemDef = ItemDef.forID(k18);
					RSInterface.interfaceCache[i6].mediaType = 4;
					RSInterface.interfaceCache[i6].mediaID = k18;
					RSInterface.interfaceCache[i6].modelRotation1 = itemDef.rotationY;
					RSInterface.interfaceCache[i6].modelRotation2 = itemDef.rotationX;
					RSInterface.interfaceCache[i6].modelZoom = (itemDef.modelZoom * 100)
							/ i13;
					opCode = -1;
					return true;
				}

			case 171:
				boolean flag1 = inStream.readUnsignedByte() == 1;
				int j13 = inStream.readUnsignedWord();
				RSInterface.interfaceCache[j13].interfaceShown = flag1;
				opCode = -1;
				return true;

			case 142:
				int j6 = inStream.readLEShort();
				resetInterfaceAnimation(j6);
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					inputTaken = true;
				}
				invOverlayInterfaceID = j6;
				needDrawTabArea = true;
				tabAreaAltered = true;
				openInterfaceID = -1;
				dialogueOptionsShowing = false;
				opCode = -1;
				return true;

			case 126:

				String text = inStream.readString();
				int frame = inStream.readShortA();
				//System.out.println("TEXT: " + text);
				if (frame == 0 && text.equals("resting"))
					resting = false;
				if (frame == 17800) {
					formatNoteTab(text);
					opCode = -1;
					return true;
				}
				if (text.startsWith("http://")) {
					launchURL(text);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[REG]")) {
					text = text.substring(4);
					//System.out.println("Frame: " + frame);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[0]*/SpriteCache.get(327);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[OWN]")) {
					text = text.substring(4);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[1]*/SpriteCache.get(328);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[MOD]")) {
					text = text.substring(4);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[2]*/SpriteCache.get(329);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[REC]")) {
					text = text.substring(4);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[3]*/SpriteCache.get(330);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[COR]")) {
					text = text.substring(4);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[4]*/SpriteCache.get(331);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[SER]")) {
					text = text.substring(4);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[5]*/SpriteCache.get(332);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[LIE]")) {
					text = text.substring(4);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[6]*/SpriteCache.get(333);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[CAP]")) {
					text = text.substring(4);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[7]*/SpriteCache.get(334);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[GEN]")) {
					text = text.substring(3);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[8]*/SpriteCache.get(335);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (text.startsWith("[FRI]")) {
					text = text.substring(4);
					RSInterface.interfaceCache[frame + 20000].disabledSprite = /*RSInterface.interfaceCache[frame + 20000].savedSprite[9]*/SpriteCache.get(336);
					updateStrings(text, frame);
					sendFrame126(text, frame);
					opCode = -1;
					return true;
				}
				if (frame == 47997) {
					Recruits = "";
					Recruits = text;
					opCode = -1;
					return true;
				}
				if (frame == 47996) {
					Corporals = "";
					Corporals = text;
					opCode = -1;
					return true;
				}
				if (frame == 47995) {
					Sergeants = "";
					Sergeants = text;
					opCode = -1;
					return true;
				}
				if (frame == 47994) {
					Lieutenants = "";
					Lieutenants = text;
					opCode = -1;
					return true;
				}
				if (frame == 47993) {
					Captains = "";
					Captains = text;
					opCode = -1;
					return true;
				}
				if (frame == 47992) {
					Generals = "";
					Generals = text;
					opCode = -1;
					return true;
				}
				if (text.startsWith("[UPDATE]")) {
					slot = 44001;
					for (int a = 0; a < friendsCount; a++) {
						if (isRecruit("" + friendsList[a] + ""))
							sendFrame126("Recruit", slot + 800);
						else if (isCorporal("" + friendsList[a] + ""))
							sendFrame126("Corporal", slot + 800);
						else if (isSergeant("" + friendsList[a] + ""))
							sendFrame126("Sergeant", slot + 800);
						else if (isLieutenant("" + friendsList[a] + ""))
							sendFrame126("Lieutenant", slot + 800);
						else if (isCaptain("" + friendsList[a] + ""))
							sendFrame126("Captain", slot + 800);
						else if (isGeneral("" + friendsList[a] + ""))
							sendFrame126("General", slot + 800);
						else
							sendFrame126("Not ranked", slot + 800);
						sendFrame126(friendsList[a], slot);
						slot++;
					}
					opCode = -1;
					return true;
				}
				if (text.startsWith("[FI]")) {
					text = text.substring(4);
					otherPlayerId = Integer.parseInt(text);
					opCode = -1;
					return true;
				}
				updateStrings(text, frame);
				sendFrame126(text, frame);
				if (frame >= 18144 && frame <= 18244) {
					clanList[frame - 18144] = text;
				}
				opCode = -1;
				return true;

			case 206:
				publicChatMode = inStream.readUnsignedByte();
				privateChatMode = inStream.readUnsignedByte();
				tradeMode = inStream.readUnsignedByte();
				inputTaken = true;
				opCode = -1;
				return true;

			case 240:
				if (tabID == 12)
					needDrawTabArea = true;
				weight = inStream.readSignedWord();
				opCode = -1;
				return true;

			case 8:
				int k6 = inStream.readWordBigEndian();
				int l13 = inStream.readUnsignedWord();
				RSInterface.interfaceCache[k6].mediaType = 1;
				RSInterface.interfaceCache[k6].mediaID = l13;
				opCode = -1;
				return true;

			case 122:
				int rsi_id = inStream.readWordBigEndian();
				int i14 = inStream.readWordBigEndian();
				int i19 = i14 >> 10 & 0x1f;
				int i22 = i14 >> 5 & 0x1f;
				int l24 = i14 & 0x1f;
				RSInterface.interfaceCache[rsi_id].disabledColor = (i19 << 19) + (i22 << 11) + (l24 << 3);
				opCode = -1;
				return true;

			case 53:
				needDrawTabArea = true;
				int rsi_frame = inStream.readUnsignedWord();
				RSInterface rsi_1 = RSInterface.interfaceCache[rsi_frame];
				int totalItems = inStream.readUnsignedWord();
				for (int idx = 0; idx < totalItems; idx++) {
					int itemAmt = inStream.readUnsignedByte();
					if (itemAmt == 255)
						itemAmt = inStream.method440();
					rsi_1.inv[idx] = inStream.readShort();
					rsi_1.invStackSizes[idx] = itemAmt;
				}
				for (int idx = totalItems; idx < rsi_1.inv.length; idx++) {
					rsi_1.inv[idx] = 0;
					rsi_1.invStackSizes[idx] = 0;
				}
				opCode = -1;
				return true;

			case 230:
				int j7 = inStream.readShortA();
				int j14 = inStream.readUnsignedWord();
				int k19 = inStream.readUnsignedWord();
				int k22 = inStream.readWordBigEndian();
				RSInterface.interfaceCache[j14].modelRotation1 = k19;
				RSInterface.interfaceCache[j14].modelRotation2 = k22;
				RSInterface.interfaceCache[j14].modelZoom = j7;
				opCode = -1;
				return true;

			case 221:
				inputTextType = inStream.readUnsignedByte();
				needDrawTabArea = true;
				opCode = -1;
				return true;

			case 177:
				inCutScene = true;
				cutsceneFocusLocalX = inStream.readUnsignedByte();
				cutsceneFocusLocalY = inStream.readUnsignedByte();
				cutsceneFocusLocalZ = inStream.readUnsignedWord();
				cutsceneRotateSpeed = inStream.readUnsignedByte();
				cutsceneRotateMul = inStream.readUnsignedByte();
				if (cutsceneRotateMul >= 100) {
					int k7 = cutsceneFocusLocalX * 128 + 64;
					int k14 = cutsceneFocusLocalY * 128 + 64;
					int i20 = getFloorDrawHeight(plane, k14, k7) - cutsceneFocusLocalZ;
					int l22 = k7 - xCameraPos;
					int k25 = i20 - zCameraPos;
					int j28 = k14 - yCameraPos;
					int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
					yCameraCurve = (int) (Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
					xCameraCurve = (int) (Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
					if (yCameraCurve < 128)
						yCameraCurve = 128;
					if (yCameraCurve > 383)
						yCameraCurve = 383;
				}
				opCode = -1;
				return true;

			case 249:
				anInt1046 = inStream.readByteA();
				playerId = inStream.readWordBigEndian();
				opCode = -1;
				return true;

			case 65:
				updateNPCs(inStream, pktSize);
				opCode = -1;
				return true;

			case 27:
				showInput = false;
				inputDialogState = 1;
				amountOrNameInput = "";
				inputTaken = true;
				opCode = -1;
				return true;

			case 187:
				showInput = false;
				inputDialogState = 2;
				amountOrNameInput = "";
				inputTaken = true;
				opCode = -1;
				return true;

			case 97:
				try {
					int interfaceID = inStream.readUnsignedWord();
					resetInterfaceAnimation(interfaceID);
					if (invOverlayInterfaceID != -1) {
						invOverlayInterfaceID = -1;
						needDrawTabArea = true;
						tabAreaAltered = true;
					}
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					if (interfaceID == 24600) {
						buttonclicked = false;
						interfaceButtonAction = 0;
					}
					openInterfaceID = interfaceID;
					dialogueOptionsShowing = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
				opCode = -1;
				return true;

			case 218:
				int i8 = inStream.method438();
				dialogID = i8;
				inputTaken = true;
				opCode = -1;
				return true;

			case 87:
				int settingIdx = inStream.readLEShort();
				int settingValue = inStream.method439();
				varbitConfigs[settingIdx] = settingValue;
				if (variousSettings[settingIdx] != settingValue) {
					variousSettings[settingIdx] = settingValue;
					handleActions(settingIdx);
					needDrawTabArea = true;
					if (dialogID != -1)
						inputTaken = true;
				}
				opCode = -1;
				return true;

			case 36:
				int k8 = inStream.readLEShort();
				byte byte0 = inStream.readSignedByte();
				varbitConfigs[k8] = byte0;
				if (variousSettings[k8] != byte0) {
					variousSettings[k8] = byte0;
					handleActions(k8);
					needDrawTabArea = true;
					if (dialogID != -1)
						inputTaken = true;
				}
				opCode = -1;
				return true;

			case 61:
				drawMultiwayIcon = inStream.readUnsignedByte();
				opCode = -1;
				return true;

			case 200:
				int l8 = inStream.readUnsignedWord();
				int i15 = inStream.readSignedWord();
				RSInterface class9_4 = RSInterface.interfaceCache[l8];
				class9_4.disabledAnimationId = i15;
				if (i15 == -1) {
					class9_4.currentFrame = 0;
					class9_4.frameTimer = 0;
				}
				class9_4.modelZoom = 1600;
				opCode = -1;
				return true;

			case 219:
				if (invOverlayInterfaceID != -1) {
					invOverlayInterfaceID = -1;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
				if (backDialogID != -1) {
					backDialogID = -1;
					inputTaken = true;
				}
				if (inputDialogState != 0) {
					inputDialogState = 0;
					inputTaken = true;
				}
				openInterfaceID = -1;
				dialogueOptionsShowing = false;
				opCode = -1;
				return true;

			case 34:
				needDrawTabArea = true;
				int interfaceID = inStream.readUnsignedWord();
				RSInterface equipRSI = RSInterface.interfaceCache[interfaceID];
				while (inStream.currentOffset < pktSize) {
					int equipSlot = inStream.readSmart();
					int itemId = inStream.readUnsignedWord();
					int itemAmount = inStream.readUnsignedByte();
					if (itemAmount == 255)
						itemAmount = inStream.readDWord();
					if (equipSlot >= 0 && equipSlot < equipRSI.inv.length) {
						equipRSI.inv[equipSlot] = itemId;
						equipRSI.invStackSizes[equipSlot] = itemAmount;
					}
				}
				opCode = -1;
				return true;

			case 4:
			case 44:
			case 84:
			case 101:
			case 105:
			case 117:
			case 147:
			case 151:
			case 152:
			case 153:
			case 156:
			case 160:
			case 215:
				parseEntityPacket(inStream, opCode);
				opCode = -1;
				return true;

			case 106:
				tabID = inStream.readByteC();
				needDrawTabArea = true;
				tabAreaAltered = true;
				opCode = -1;
				return true;

			case 164:
				int j9 = inStream.readLEShort();
				resetInterfaceAnimation(j9);
				if (invOverlayInterfaceID != -1) {
					invOverlayInterfaceID = -1;
					needDrawTabArea = true;
					tabAreaAltered = true;
				}
				backDialogID = j9;
				inputTaken = true;
				openInterfaceID = -1;
				dialogueOptionsShowing = false;
				opCode = -1;
				return true;

			}
			signlink.reporterror("Packet not handled: " + opCode + ","
					+ pktSize + " - " + opcode_last + "," + opcode_second);
			// resetLogout();
		} catch (IOException _ex) {
			try {
				dropClient();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			}
		} catch (Exception exception) {
			String s2 = "Packet received but exception occurred: " + opCode
					+ "," + opcode_last + "," + opcode_second + " - " + pktSize
					+ "," + (baseX + myPlayer.pathX[0]) + ","
					+ (baseY + myPlayer.pathY[0]) + " - ";
			for (int j15 = 0; j15 < pktSize && j15 < 50; j15++)
				s2 = s2 + inStream.buffer[j15] + ",";
			exception.printStackTrace();
			// resetLogout();
		}
		opCode = -1;
		return true;
	}

	public static int clientZoom = 0;

	private void renderWorld() {
		sceneCycle++;
		int j = 0;
		int camera_x_pos = xCameraPos;
		int camera_z_pos = zCameraPos;
		int camera_y_pos = yCameraPos;
		int camera_y_curve = yCameraCurve;
		int camera_x_curve = xCameraCurve;
		if (loggedIn) {
			processPlayers(true);
			processNpcs(true);
			processPlayers(false);
			processNpcs(false);
			processProjectiles();
			processStillGraphic();
			if (!inCutScene) {
				int changeOffset = chaseCameraPitch;
				if (minCameraPitch / 256 > changeOffset)
					changeOffset = minCameraPitch / 256;
				if (cameraEffectEnabled[4] && anIntArray1203[4] + 128 > changeOffset)
					changeOffset = anIntArray1203[4] + 128;
				int k = viewRotation + viewRotationOffset & 0x7ff;
				int zoom = (600 + (changeOffset * clientHeight / 400) + clientZoom);
				setCameraPos(clientSize == 0 ? (600 + changeOffset * 3) + clientZoom : zoom, changeOffset, chaseCameraX,
						getFloorDrawHeight(plane, myPlayer.y, myPlayer.x) - 50, k, chaseCameraY);
			}
			if (!inCutScene)
				j = getCameraHeight();
			else
				j = getCamHeight();
			for (int cameraEffect = 0; cameraEffect < 5; cameraEffect++)
				if (cameraEffectEnabled[cameraEffect]) {
					int effect = (int) ((Math.random() * (double) (anIntArray873[cameraEffect] * 2 + 1) - 
							(double) anIntArray873[cameraEffect]) + Math.sin((double) cameraEffectCycles[cameraEffect] * 
							((double) anIntArray928[cameraEffect] / 100D)) * (double) anIntArray1203[cameraEffect]);
					if (cameraEffect == 0)
						xCameraPos += effect;
					if (cameraEffect == 1)
						zCameraPos += effect;
					if (cameraEffect == 2)
						yCameraPos += effect;
					if (cameraEffect == 3)
						xCameraCurve = xCameraCurve + effect & 0x7ff;
					if (cameraEffect == 4) {
						yCameraCurve += effect;
						if (yCameraCurve < 128)
							yCameraCurve = 128;
						if (yCameraCurve > 383)
							yCameraCurve = 383;
					}
				}
		}
		Model.objectExists = true;
		Model.objectsRendered = 0;
		Model.currentCursorX = super.mouseX - 4;
		Model.currentCursorY = super.mouseY - 4;
		DrawingArea.resetImage();
		if (Configuration.fog) {
			if (loggedIn)
				DrawingArea.drawPixels(clientHeight, 0, 0, 0xC8C0A8, clientWidth);
		}
		if (loggedIn) {
			worldController.render(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, j, yCameraCurve);
			worldController.clearInteractableObjects();
		}
		updateEntities();
		drawHeadIcon();
		TextureAnimating.animateTexture();
		if (drawPane)
			drawBlackPane();
		if (loggedIn) {
			drawUnfixedGame();
			draw3dScreen();
		}
		if (showXP && loggedIn) {
			displayXPCounter();
		}
		if(loggedIn) {
			SkillOrbHandler.drawOrbs();
		}
		if (loggedIn) {
			gameScreenIP.drawGraphics(clientSize == 0 ? 4 : 0, super.graphics, clientSize == 0 ? 4 : 0);
			xCameraPos = camera_x_pos;
			zCameraPos = camera_z_pos;
			yCameraPos = camera_y_pos;
			yCameraCurve = camera_y_curve;
			xCameraCurve = camera_x_curve;
		}

	}

	public void setNorth() {
		cameraOffsetX = 0;
		cameraOffsetY = 0;
		viewRotationOffset = 0;
		viewRotation = 0;
		minimapRotation = 0;
		minimapZoom = 0;
	}

	public void clearTopInterfaces() {
		stream.createFrame(130);
		if (invOverlayInterfaceID != -1) {
			invOverlayInterfaceID = -1;
			needDrawTabArea = true;
			dialogueOptionsShowing = false;
			tabAreaAltered = true;
		}
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
			dialogueOptionsShowing = false;
		}
		openInterfaceID = -1;
		fullscreenInterfaceID = -1;
	}

	public Client() {

		StatGraph.Client = this;
		fpsGraph.addValue(super.fps);
		memGraph.addValue((int) ((Runtime.getRuntime().totalMemory() - Runtime
				.getRuntime().freeMemory()) / 1024L));
		for (int i = 0; i < orbTextEnabled.length; i++)
			orbTextEnabled[i] = true;
		anIntArray969 = new int[256];
		xpLock = false;
		xpCounter = 0;
		familiarHandler = new FamiliarHandler();
		register = new Register(this);
		choosingLeftClick = false;
		leftClick = -1;
		fullscreenInterfaceID = -1;
		chatRights = new int[500];
		displayChat = true;
		chatTypeView = 0;
		clanChatMode = 0;
		cButtonHPos = -1;
		cButtonCPos = 0;
		anIntArrayArray825 = new int[104][104];
		friendsNodeIDs = new int[200];
		groundArray = new Deque[4][104][104];
		textStream = new Stream(new byte[5000]);
		npcArray = new NPC[50000];
		npcIndices = new int[50000];
		anIntArray840 = new int[1000];
		aStream_847 = Stream.create();
		soundEnabled = true;
		openInterfaceID = -1;
		currentExp = new int[Skills.skillsCount];

		anIntArray873 = new int[5];
		cameraEffectEnabled = new boolean[5];
		reportAbuseInput = "";
		playerId = -1;
		menuOpen = false;
		inputString = "";
		inputStringPos = 0;
		maxPlayers = 2048;
		myPlayerIndex = 2047;
		playerArray = new Player[maxPlayers];
		playerIndices = new int[maxPlayers];
		playersToUpdate = new int[maxPlayers];
		aStreamArray895s = new Stream[maxPlayers];
		cameraACTranslatePitch = 1;
		pathwayPoint = new int[104][104];
		currentStats = new int[Skills.skillsCount];
		ignoreListAsLongs = new long[100];
		loadingError = false;
		anIntArray928 = new int[5];
		tileCycleMap = new int[104][104];
		chatTypes = new int[500];
		chatNames = new String[500];
		chatMessages = new String[500];
		backButton = new Sprite[2];
		titleBox = new Sprite[17];
		loadCircle = new Sprite[8];
		sideIcons = new Sprite[15];
		isFocused = true;
		friendsListAsLongs = new long[200];
		currentSong = -1;
		spriteDrawX = -1;
		spriteDrawY = -1;
		mapBackOffsets0 = new int[33];
		cacheIndices = new Decompressor[8];
		variousSettings = new int[9000];
		aBoolean972 = false;
		speakAmountsOnScreen = 50;
		spokenX = new int[speakAmountsOnScreen];
		spokenY = new int[speakAmountsOnScreen];
		spokenOffsetY = new int[speakAmountsOnScreen];
		spokenOffsetX = new int[speakAmountsOnScreen];
		spokenColor = new int[speakAmountsOnScreen];
		spokenEffect = new int[speakAmountsOnScreen];
		spokenCycle = new int[speakAmountsOnScreen];
		spokenMessage = new String[speakAmountsOnScreen];
		lastKnownPlane = -1;
		compass = new Sprite[2];
		hitMark = new Sprite[61];
		hitIcon = new Sprite[20];
		hitMarks = new Sprite[5];
		myAppearanceColors = new int[5];
		scrollPart = new Sprite[12];
		scrollBar = new Sprite[6];
		aBoolean994 = false;
		amountOrNameInput = "";
		projectileDeque = new Deque();
		cameraSendingInfo = false;
		walkableInterfaceId = -1;
		cameraEffectCycles = new int[5];
		updateCharacterCreation = false;
		mapFunctions = new Sprite[100];
		dialogID = -1;
		currentMaxStats = new int[Skills.skillsCount];
		varbitConfigs = new int[9000];
		isMale = true;
		minimapXPosArray = new int[158];
		flashingSidebarTab = -1;
		stillGraphicDeque = new Deque();
		mapbackOffsets1 = new int[33];
		chatInterface = new RSInterface();
		mapScenes = new Background[100];
		myAppearance = new int[7];
		mapFunctionTileX = new int[1000];
		mapFunctionTileY = new int[1000];
		loadingMap = false;
		friendsList = new String[200];
		inStream = Stream.create();
		expectedCRCs = new int[9];
		menuActionCmd2 = new int[500];
		menuActionCmd3 = new int[500];
		menuActionCmd4 = new int[500];
		menuActionID = new int[500];
		menuActionCmd1 = new int[500];
		headIcons = new Sprite[20];
		skullIcons = new Sprite[20];
		headIconsHint = new Sprite[20];
		tabAreaAltered = false;
		promptMessage = "";
		atPlayerActions = new String[6];
		atPlayerArray = new boolean[6];
		constructRegionData = new int[4][13][13];
		anInt1132 = 2;
		currentMapFunctionSprites = new Sprite[1000];
		inTutorialIsland = false;
		dialogueOptionsShowing = false;
		crosses = new Sprite[8];
		musicEnabled = true;
		needDrawTabArea = false;
		loggedIn = false;
		canMute = false;
		requestMapReconstruct = false;
		inCutScene = false;
		minimapACTranslateZoom = 1;
		myUsername = "";
		myPassword = "";
		genericLoadingError = false;
		reportAbuseInterfaceID = -1;
		objectSpawnDeque = new Deque();
		chaseCameraPitch = 128;
		invOverlayInterfaceID = -1;
		stream = Stream.create();
		menuActionName = new String[500];
		anIntArray1203 = new int[5];
		sound = new int[50];
		cameraACTranslateYaw = 2;
		chatScrollHeight = 78;
		promptInput = "";
		modIcons = new Sprite[11];
		newCrowns = new Sprite[10];
		tabID = 3;
		inputTaken = false;
		songChanging = true;
		minimapYPosArray = new int[158];
		clippingPlanes = new CollisionDetection[4];
		soundType = new int[50];
		isDragging = false;
		soundDelay = new int[50];
		soundVolume = new int[50];
		rsAlreadyLoaded = false;
		welcomeScreenRaised = false;
		showInput = false;
		loginMessage1 = "";
		loginMessage2 = "";
		backDialogID = -1;
		cameraACTranslateX = 2;
		bigX = new int[4000];
		bigY = new int[4000];
	}

	public int rights;
	public Sprite backgroundFix;
	public Sprite[] titleBox;
	public Sprite candle1, candle2, loginButton, registerButton,
			loginBackButton, scroll, scrollBottom;
	public Sprite fire1, fire2, fire3;
	public Sprite[] loadCircle;
	public String name;
	public String message;
	public String clanname;
	private final int[] chatRights;
	public int chatTypeView;
	public int clanChatMode;
	public int duelMode;
	/* Declare custom sprites */
	public Sprite[] backButton;
	public Sprite loadingPleaseWait;
	public Sprite reestablish;
	public Sprite HPBarFull, HPBarEmpty, HPBarBigEmpty;
	/**/
	private RSImageProducer leftFrame;
	private RSImageProducer topFrame;
	private RSImageProducer rightFrame;
	private int ignoreCount;
	private long mapLoadingTime;
	private int[][] anIntArrayArray825;
	private int[] friendsNodeIDs;
	private Deque[][][] groundArray;
	private Socket jagGrabSocket;
	public int loginScreenState = 0;
	public int previousScreenState;
	private Stream textStream;
	private NPC[] npcArray;
	private int npcCount;
	private int[] npcIndices;
	private int entityUpdateCount;
	private int[] anIntArray840;
	private int anInt841;
	private int opcode_last;
	private int opcode_second;
	private String notifyMessage;
	private int privateChatMode;
	private int gameChatMode;
	private Stream aStream_847;
	private boolean soundEnabled;
	private static int systemUpdateCycle;
	private int markType;
	public static int openInterfaceID;
	public int xCameraPos;
	public int zCameraPos;
	public int yCameraPos;
	public int yCameraCurve;
	public int xCameraCurve;
	private int myRights;
	private int myCrown;
	private int glowColor;
	public final int[] currentExp;
	private Sprite mapFlag;
	private Sprite mapMarker;
	private Sprite textureImage;
	private final int[] anIntArray873;
	private final boolean[] cameraEffectEnabled;
	private int weight;
	private MouseDetection mouseDetection;
	private String reportAbuseInput;
	private int playerId;
	private boolean menuOpen;
	private int hoveredInterface;
	public String inputString;
	public int inputStringPos;
	private final int maxPlayers;
	private final int myPlayerIndex;
	private Player[] playerArray;
	private int playerCount;
	private int[] playerIndices;
	private int playersToUpdateCount;
	private int[] playersToUpdate;
	private Stream[] aStreamArray895s;
	private int viewRotationOffset;
	private int cameraACTranslatePitch;
	private int friendsCount;
	private int inputTextType;
	private int[][] pathwayPoint;
	private int anInt913;
	private int crossX;
	private int crossY;
	private int crossIndex;
	private int crossType;
	public int plane;
	public final int[] currentStats;
	private static int anInt924;
	private final long[] ignoreListAsLongs;
	private boolean loadingError;
	private final int[] anIntArray928;
	private int[][] tileCycleMap;
	private Sprite aClass30_Sub2_Sub1_Sub1_931;
	private Sprite aClass30_Sub2_Sub1_Sub1_932;
	private int markedPlayer;
	private int markedX;
	private int markedY;
	private int anInt936;
	private int anInt937;
	private int anInt938;
	private final int[] chatTypes;
	private final String[] chatNames;
	private String[] chatMessages;
	public int cycleTimer;
	public WorldController worldController;
	private Sprite[] sideIcons;
	private int menuScreenArea;
	private int menuOffsetX;
	private int menuOffsetY;
	private int menuWidth;
	private int menuHeight;
	private long aLong953;
	private boolean isFocused;
	private long[] friendsListAsLongs;
	private String[] clanList = new String[100];
	private int currentSong;
	private static int nodeID = 10;
	static int portOff;
	public static boolean clientData;
	private static boolean isMembers = true;
	private static boolean lowMem;
	private int spriteDrawX;
	private int spriteDrawY;
	private final int[] spoken_palette = { 0xffff00, 0xff0000, 65280, 65535,
			0xff00ff, 0xffffff };
	private final int[] mapBackOffsets0;
	final Decompressor[] cacheIndices;
	public int variousSettings[];
	private boolean aBoolean972;
	private final int speakAmountsOnScreen;
	private final int[] spokenX;
	private final int[] spokenY;
	private final int[] spokenOffsetY;
	private final int[] spokenOffsetX;
	private final int[] spokenColor;
	private final int[] spokenEffect;
	private final int[] spokenCycle;
	private final String[] spokenMessage;
	private int minCameraPitch;
	private int lastKnownPlane;
	private static int anInt986;
	public Sprite[] hitMark;
	public Sprite[] hitMarks;
	public Sprite[] hitIcon;
	private Sprite[] scrollBar;
	public Sprite[] scrollPart;
	private int cameraACCycle0;
	private int dragCycle;
	private final int[] myAppearanceColors;
	private final boolean aBoolean994;
	private int cutsceneFocusLocalX;
	public static int[] myHeadAndJaw = new int[2];
	private int cutsceneFocusLocalY;
	private int cutsceneFocusLocalZ;
	private Region objectManager;
	private int cutsceneRotateSpeed;
	private int cutsceneRotateMul;
	private ISAACRandomGen encryption;
	private Sprite mapEdge;
	private Sprite multiOverlay;
	static final int[][] anIntArrayArray1003 = {
			{ 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, 33697, 22433, 2983,
					54193 },
			{ 8741, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094, 10153,
					56621, 4783, 1341, 16578, 35003, 25239 },
			{ 25238, 8742, 12, 64030, 43162, 7735, 8404, 1701, 38430, 24094,
					10153, 56621, 4783, 1341, 16578, 35003 },
			{ 4626, 11146, 6439, 12, 4758, 10270 },
			{ 4550, 20165, 43678, 16895, 28416, 12231, 947, 60359, 32433 } };
	private String amountOrNameInput;
	private static int anInt1005;
	private int daysSinceLastLogin;
	private int pktSize;
	private int opCode;
	private int netIdleCycles;
	private int heartBeatCycle;
	private int logoutCycle;
	private Deque projectileDeque;
	private int chaseCameraX;
	private int chaseCameraY;
	private int sendCameraInfoCycle;
	private boolean cameraSendingInfo;
	private int walkableInterfaceId;
	private static final int[] levelXPs;
	private int minimapStatus;
	private int mouseRecorderIdleCycles;
	private int loadingStage;
	private int focusedViewportWidget;
	private final int[] cameraEffectCycles;
	private boolean updateCharacterCreation;
	public Sprite[] mapFunctions;
	private int baseX;
	private int baseY;
	private int anInt1036;
	private int anInt1037;
	private int focusChatWidget;
	private int dialogID;
	private final int[] currentMaxStats;
	private final int[] varbitConfigs;
	private int anInt1046;
	private boolean isMale;
	private int focusedSidebarWidget;
	private String loadingStepText;
	private static int anInt1051;
	private final int[] minimapXPosArray;
	private CacheArchive titleStreamLoader;
	private int flashingSidebarTab;
	private int drawMultiwayIcon;
	private Deque stillGraphicDeque;
	private final int[] mapbackOffsets1;
	public final RSInterface chatInterface;
	public Background[] mapScenes;
	private static int drawCycle;
	private int currentSound;
	private int friendsListAction;
	private final int[] myAppearance;
	private int mouseInvInterfaceIndex;
	private int lastActiveInvInterface;
	public OnDemandFetcher onDemandFetcher;
	private int currentRegionX;
	private int currentRegionY;
	private int mapFunctionsLoadedAmt;
	private int[] mapFunctionTileX;
	private int[] mapFunctionTileY;
	private Sprite mapDotItem;
	private Sprite mapDotNPC;
	private Sprite mapDotPlayer;
	private Sprite mapDotFriend;
	private Sprite mapDotTeam;
	private Sprite mapDotClan;
	private boolean loadingMap;
	private String[] friendsList;
	private Stream inStream;
	private int focusedDragWidget;
	private int dragFromSlot;
	private int activeInterfaceType;
	private int pressX;
	private int pressY;
	public static int chatScrollAmount;
	private final int[] expectedCRCs;
	private int[] menuActionCmd2;
	private int[] menuActionCmd3;
	public int[] menuActionCmd4;
	private int[] menuActionID;
	private int[] menuActionCmd1;
	private Sprite[] headIcons;
	private Sprite[] skullIcons;
	private Sprite[] headIconsHint;
	private static int anInt1097;
	private int cutsceneLocalX;
	private int cutsceneLocalY;
	private int cutsceneLocalZ;
	private int cutsceneSpeed;
	private int cutsceneSpeedMul;
	public static boolean tabAreaAltered;
	private int updateMinutes;
	public static FamiliarHandler familiarHandler;
	public Register register;
	private RSImageProducer GraphicsBuffer_1107;
	private RSImageProducer titleScreen;
	private RSImageProducer loadingScreen;
	private static int anInt1117;
	private int membersInt;
	private String promptMessage;
	private Sprite[] compass;
	private RSImageProducer GraphicsBuffer_1125;
	public static Player myPlayer;
	private final String[] atPlayerActions;
	private final boolean[] atPlayerArray;
	private final int[][][] constructRegionData;
	public final int[] tabInterfaceIDs = { -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1 };
	private int cameraOffsetY;
	private int anInt1132;
	private int menuActionRow;
	private static int anInt1134;
	private int spellSelected;
	private int selectedSpellId;
	private int spellUsableOn;
	private String spellTooltip;
	private Sprite[] currentMapFunctionSprites;
	private boolean inTutorialIsland;
	private static int anInt1142;
	private boolean dialogueOptionsShowing;
	private Sprite[] crosses;
	private boolean musicEnabled;
	private Background[] aBackgroundArray1152s;
	public static boolean needDrawTabArea;
	private int unreadMessages;
	private static int anInt1155;
	public static boolean fpsOn;
	public boolean loggedIn;
	private boolean canMute;
	private boolean requestMapReconstruct;
	private boolean inCutScene;
	public static int loopCycle;
	public static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
	private RSImageProducer tabAreaIP;
	private RSImageProducer mapAreaIP;
	private RSImageProducer gameScreenIP;
	private RSImageProducer chatAreaIP;
	private int daysSinceRecovChange;
	private RSSocket socketStream;
	private int minimapZoom;
	private int minimapACTranslateZoom;
	private long lastSoundTime;
	public String myUsername;
	public String myPassword;
	private static int anInt1175;
	private boolean genericLoadingError;
	private final int[] anIntArray1177 = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	private int reportAbuseInterfaceID;
	private Deque objectSpawnDeque;
	private int[] chatOffsets;
	private int[] sidebarOffsets;
	private int[] viewportOffsets;
	public Sprite[] cacheSprite;
	private byte[][] terrainData;
	private int chaseCameraPitch;
	private int viewRotation;
	private int cameraYawTranslate;
	private int cameraPitchTranslate;
	private static int anInt1188;
	private int invOverlayInterfaceID;
	static Stream stream;
	private int anInt1193;
	private int splitPrivateChat;
	private Background mapBack;
	/* Gameframe update */
	public Sprite newMapBack;
	private String[] menuActionName;
	private final int[] anIntArray1203;
	static final int[] anIntArray1204 = { 9104, 10275, 7595, 3610, 7975, 8526,
			918, 38802, 24466, 10145, 58654, 5027, 1457, 16565, 34991, 25486 };
	private static boolean flagged;
	private final int[] sound;
	private int flameCycle;
	private int minimapRotation;
	private int cameraACTranslateYaw;
	public static int chatScrollHeight;
	private String promptInput;
	private int clickCycle;
	private int[][][] intGroundArray;
	private long serverSeed;
	public int loginScreenCursorPos;
	public final Sprite[] modIcons;
	public Sprite[] newCrowns;
	private long lastClickTime;
	public static int tabID;
	private int markedNPC;
	public static boolean inputTaken;
	private int inputDialogState;
	private static int anInt1226;
	private int nextSong;
	private boolean songChanging;
	private final int[] minimapYPosArray;
	static CollisionDetection[] clippingPlanes;
	public static int bit_mask[];
	private int[] mapCoordinates;
	private int[] terrainIndices;
	private int[] objectIndices;
	private int mouseRecorderLastX;
	private int mouseRecorderLastY;
	public final int anInt1239 = 100;
	private final int[] soundType;
	private boolean isDragging;
	private int atInventoryLoopCycle;
	private int atInventoryInterface;
	private int atInventoryIndex;
	private int atInventoryInterfaceType;
	private byte[][] objectData;
	private int tradeMode;
	private int showSpokenEffects;
	private final int[] soundDelay;
	private final int[] soundVolume;
	private int isOnTutorialIsland;
	private final boolean rsAlreadyLoaded;
	private int useOneMouseButton;
	private int cameraACCycle1;
	private boolean welcomeScreenRaised;
	private boolean showInput;
	private int lastSoundPosition;
	private byte[][][] tileSettingBits;
	private int prevSong;
	private int destX;
	private int destY;
	private Sprite miniMap;
	private int anInt1264;
	private int sceneCycle;
	private String loginMessage1;
	private String loginMessage2;
	private int bigRegionX;
	private int bigRegionY;
	public RSFontSystem newSmallFont, newRegularFont, newBoldFont,
			newFancyFont, regularHitFont, bigHitFont;
	public static TypeFace normalFont;
	public static TypeFace boldFont;
	public static TypeFace fancyText;
	public TypeFace smallText;
	private TypeFace smallHit;
	private TypeFace bigHit;
	public TypeFace drawingArea;
	private TypeFace chatTextDrawingArea;
	private int backDialogID;
	private int cameraOffsetX;
	private int cameraACTranslateX;
	private int[] bigX;
	private int[] bigY;
	private int itemSelected;
	private int lastItemSelectedSlot;
	private int lastItemSelectedInterface;
	private int selectedItemId;
	private String selectedItemName;
	private int publicChatMode;
	private int yellMode;
	private static int anInt1288;
	public static int anticheat_9;
	public int drawCount;
	public int fullscreenInterfaceID;
	public int anInt1044;
	public int anInt1129;
	public int anInt1315;
	public int anInt1500;
	public int anInt1501;
	public int[] fullScreenTextureArray;
	public String Recruits = "";
	public String Corporals = "";
	public String Sergeants = "";
	public String Lieutenants = "";
	public String Captains = "";
	public String Generals = "";
	public int Slots[] = new int[7];
	public String slots[] = new String[7];
	public int slotItemId[] = new int[7];
	public int slotColor[] = new int[7];
	public int slotColorPercent[] = new int[7];
	public boolean slotAborted[] = new boolean[7];
	public int slotUsing = 0;
	public int slotSelected;
	public Sprite Sell;
	public Sprite Buy;
	public Sprite SubmitBuy;
	public Sprite SubmitSell;

	public boolean isRecruit(String name) {
		name = name.toLowerCase();
		if (Recruits.contains(" " + name + ",")) {
			return true;
		}
		if (Recruits.contains(", " + name + "]")) {
			return true;
		}
		if (Recruits.contains("[" + name + ",")) {
			return true;
		}
		if (Recruits.contains("[" + name + "]")) {
			return true;
		}
		return false;
	}

	public boolean isCorporal(String name) {
		name = name.toLowerCase();
		if (Corporals.contains(" " + name + ",")) {
			return true;
		}
		if (Corporals.contains(", " + name + "]")) {
			return true;
		}
		if (Corporals.contains("[" + name + ",")) {
			return true;
		}
		if (Corporals.contains("[" + name + "]")) {
			return true;
		}
		return false;
	}

	public boolean isSergeant(String name) {
		name = name.toLowerCase();
		if (Sergeants.contains(" " + name + ",")) {
			return true;
		}
		if (Sergeants.contains(", " + name + "]")) {
			return true;
		}
		if (Sergeants.contains("[" + name + ",")) {
			return true;
		}
		if (Sergeants.contains("[" + name + "]")) {
			return true;
		}
		return false;
	}

	public boolean isLieutenant(String name) {
		name = name.toLowerCase();
		if (Lieutenants.contains(" " + name + ",")) {
			return true;
		}
		if (Lieutenants.contains(", " + name + "]")) {
			return true;
		}
		if (Lieutenants.contains("[" + name + ",")) {
			return true;
		}
		if (Lieutenants.contains("[" + name + "]")) {
			return true;
		}
		return false;
	}

	public boolean isCaptain(String name) {
		name = name.toLowerCase();
		if (Captains.contains(" " + name + ",")) {
			return true;
		}
		if (Captains.contains(", " + name + "]")) {
			return true;
		}
		if (Captains.contains("[" + name + ",")) {
			return true;
		}
		if (Captains.contains("[" + name + "]")) {
			return true;
		}
		return false;
	}

	public boolean isGeneral(String name) {
		name = name.toLowerCase();
		if (Generals.contains(" " + name + ",")) {
			return true;
		}
		if (Generals.contains(", " + name + "]")) {
			return true;
		}
		if (Generals.contains("[" + name + ",")) {
			return true;
		}
		if (Generals.contains("[" + name + "]")) {
			return true;
		}
		return false;
	}

	public void resetAllImageProducers() {
		if (super.fullGameScreen != null) {
			return;
		}
		chatAreaIP = null;
		mapAreaIP = null;
		tabAreaIP = null;
		gameScreenIP = null;
		GraphicsBuffer_1125 = null;
		GraphicsBuffer_1107 = null;
		titleScreen = null;
		loadingScreen = null;
		super.fullGameScreen = new RSImageProducer(765, 503, getGameComponent());
		welcomeScreenRaised = true;
	}

	public String getRank(int i) {
		switch (i) {
		case 1:
			return "Lord";
		case 2:
			return "Sir";
		case 3:
			return "Lionheart";
		case 4:
			return "Desperado";
		case 5:
			return "Bandito";
		case 6:
			return "King";
		case 7:
			return "Big Cheese";
		case 8:
			return "Wunderkind";
		case 9:
			return "Crusader";
		case 10:
			return "Overlord";
		case 11:
			return "Bigwig";
		case 12:
			return "Count";
		case 13:
			return "Duderino";
		case 14:
			return "Hell Raiser";
		case 15:
			return "Baron";
		case 16:
			return "Duke";
		case 17:
			return "Lady";
		case 18:
			return "Dame";
		case 19:
			return "Dudette";
		case 20:
			return "Baroness";
		case 21:
			return "Countess";
		case 22:
			return "Overlordess";
		case 23:
			return "Duchess";
		case 24:
			return "Queen";
		case 25:
			return "Mod";
		case 26:
			return "Admin";
		case 27:
			return "Head Admin";
		case 28:
			return "Main Owner";
		case 29:
			return "Most Respected";
		case 30:
			return "Top Pker";
		case 31:
			return "Veteran";
		}
		return "";
	}

	public void drawGrandExchange() {
		if (openInterfaceID != 24500 && openInterfaceID != 54700
				&& openInterfaceID != 53700) {
			return;
		}
		if (openInterfaceID == 24500) {
			for (int i = 1; i < Slots.length; i++) {
				if (Slots[i] == 0) {
					drawUpdate(i, "Regular");
				}
				if (Slots[i] == 1 && slots[i] == "Sell") {
					drawUpdate(i, "Submit Sell");
				}
				if (Slots[i] == 1 && slots[i] == "Buy") {
					drawUpdate(i, "Submit Buy");
				}
				if (Slots[i] == 2 && slots[i] == "Sell") {
					drawUpdate(i, "Sell");
				}
				if (Slots[i] == 2 && slots[i] == "Buy") {
					drawUpdate(i, "Buy");
				}
				if (Slots[i] == 3 && slots[i] == "Sell") {
					drawUpdate(i, "Finished Selling");
				}
				if (Slots[i] == 3 && slots[i] == "Buy") {
					drawUpdate(i, "Finished Buying");
				}
			}
		}
		int x = 0;
		int y = 0;
		x = (clientSize == 0) ? 71 : (71 + (clientWidth / 2 - 256));
		y = (clientSize == 0) ? 303 : (clientHeight / 2 + 136);
		if (openInterfaceID == 54700) {
			SpriteCache.fetchIfNeeded(647);
			SpriteCache.fetchIfNeeded(648);
			SpriteCache.fetchIfNeeded(649);
			SpriteCache.fetchIfNeeded(651);
			per4 = SpriteCache.spriteCache[647];
			per5 = SpriteCache.spriteCache[648];
			per6 = SpriteCache.spriteCache[649];
			abort2 = SpriteCache.spriteCache[651];
			if (slotColorPercent[slotSelected] == 100
					|| slotAborted[slotSelected]) {
				RSInterface.interfaceCache[54800].tooltip = "[GE]";
			} else {
				RSInterface.interfaceCache[54800].tooltip = "Abort offer";
			}
			if (slotSelected <= 6) {
				if (!slotAborted[slotSelected]) {
					for (int k9 = 1; k9 < slotColorPercent[slotSelected]; k9++) {
						if (slotColorPercent[slotSelected] > 0) {
							if (k9 == 1) {
								if (per4 != null)
									per4.drawSprite(x, y);
								x += 3;
							} else if (k9 == 99) {
								if (per6 != null)
									per6.drawSprite(x, y);
								x += 4;
							} else {
								if (per5 != null)
									per5.drawSprite(x, y);
								x += 3;
							}
						}
					}
				} else {
					if (abort2 != null)
						abort2.drawSprite(x, y);
				}
			}
		}
		x = (clientSize == 0) ? 71 : (71 + (clientWidth / 2 - 256));
		y = (clientSize == 0) ? 303 : (clientHeight / 2 + 136);
		if (openInterfaceID == 53700) {
			SpriteCache.fetchIfNeeded(647);
			SpriteCache.fetchIfNeeded(648);
			SpriteCache.fetchIfNeeded(649);
			per4 = SpriteCache.spriteCache[647];
			per5 = SpriteCache.spriteCache[648];
			per6 = SpriteCache.spriteCache[649];
			if (slotColorPercent[slotSelected] == 100
					|| slotAborted[slotSelected]) {
				RSInterface.interfaceCache[53800].tooltip = "[GE]";
			} else {
				RSInterface.interfaceCache[53800].tooltip = "Abort offer";
			}
			if (slotSelected <= 6) {
				if (!slotAborted[slotSelected]) {
					for (int k9 = 1; k9 < slotColorPercent[slotSelected]; k9++) {
						if (slotColorPercent[slotSelected] > 0) {
							if (k9 == 1) {
								if (per4 != null)
									per4.drawSprite(x, y);
								x += 3;
							} else if (k9 == 99) {
								if (per6 != null)
									per6.drawSprite(x, y);
								x += 4;
							} else {
								if (per5 != null)
									per5.drawSprite(x, y);
								x += 3;
							}
						}
					}
				} else {
					if (abort2 != null)
						abort2.drawSprite(x, y);
				}
			}
		}
	}

	public void drawUpdate(int id, String type) {
		int x = 0;
		int y = 0;
		int x2 = 0;
		int y2 = 0;
		int x3 = 0;
		int y3 = 0;
		boolean fixed = (clientSize == 0);
		switch (id) {
		case 1:
			x = fixed ? 30 : (clientWidth / 2 - 226);
			x2 = fixed ? 80 : (clientWidth / 2 - 226 + 50);
			x3 = fixed ? 40 : (clientWidth / 2 - 226 + 10);
			y = fixed ? 74 : (clientHeight / 2 - 93);
			y2 = fixed ? 136 : (clientHeight / 2 - 93 + 62);
			y3 = fixed ? 115 : (clientHeight / 2 - 93 + 41);
			break;
		case 2:
			x = fixed ? 186 : (clientWidth / 2 - 70);
			x2 = fixed ? 80 + 156 : (clientWidth / 2 - 70 + 50);
			x3 = fixed ? 40 + 156 : (clientWidth / 2 - 70 + 10);
			y = fixed ? 74 : (clientHeight / 2 - 93);
			y2 = fixed ? 136 : (clientHeight / 2 - 93 + 62);
			y3 = fixed ? 115 : (clientHeight / 2 - 93 + 41);
			break;
		case 3:
			x = fixed ? 342 : (clientWidth / 2 + 86);
			x2 = fixed ? 80 + 156 + 156 : (clientWidth / 2 + 86 + 50);
			x3 = fixed ? 40 + 156 + 156 : (clientWidth / 2 + 86 + 10);
			y = fixed ? 74 : (clientHeight / 2 - 93);
			y2 = fixed ? 136 : (clientHeight / 2 - 93 + 62);
			y3 = fixed ? 115 : (clientHeight / 2 - 93 + 41);
			break;
		case 4:
			x = fixed ? 30 : (clientWidth / 2 - 226);
			x2 = fixed ? 80 : (clientWidth / 2 - 226 + 50);
			x3 = fixed ? 40 : (clientWidth / 2 - 226 + 10);
			y = fixed ? 194 : (clientHeight / 2 + 27);
			y2 = fixed ? 256 : (clientHeight / 2 + 27 + 62);
			y3 = fixed ? 235 : (clientHeight / 2 + 27 + 41);
			break;
		case 5:
			x = fixed ? 186 : (clientWidth / 2 - 70);
			x2 = fixed ? 80 + 156 : (clientWidth / 2 - 70 + 50);
			x3 = fixed ? 40 + 156 : (clientWidth / 2 - 70 + 10);
			y = fixed ? 194 : (clientHeight / 2 + 27);
			y2 = fixed ? 256 : (clientHeight / 2 + 27 + 62);
			y3 = fixed ? 235 : (clientHeight / 2 + 27 + 41);
			break;
		case 6:
			x = fixed ? 342 : (clientWidth / 2 + 86);
			x2 = fixed ? 80 + 156 + 156 : (clientWidth / 2 + 86 + 50);
			x3 = fixed ? 40 + 156 + 156 : (clientWidth / 2 + 86 + 10);
			y = fixed ? 194 : (clientHeight / 2 + 27);
			y2 = fixed ? 256 : (clientHeight / 2 + 27 + 62);
			y3 = fixed ? 235 : (clientHeight / 2 + 27 + 41);
			break;
		}
		x -= 2;
		x2 -= 2;
		x3 -= 2;
		int minus = 20;
		if (type == "Sell") {
			if (super.mouseX >= x && super.mouseX <= x + 140
					&& super.mouseY >= y && super.mouseY <= y + 110
					&& !menuOpen) {
				SpriteCache.fetchIfNeeded(655);
				SellHover = SpriteCache.spriteCache[655];
				if (SellHover != null)
					SellHover.drawSprite(x, y);
			} else {
				SpriteCache.spriteCache[30].drawSprite(x, y);
			}
			setGrandExchange(id, false);
			if (slotAborted[id] || slotColorPercent[id] == 100) {
				changeSet(id, true, false);
			} else {
				changeSet(id, true, true);
			}
			drawPercentage(id);
			smallText.drawShadowedStringNormal(0xCC9900, x2,
					RSInterface.interfaceCache[32000 + id].message, y2 - minus,
					true);
			smallText.drawShadowedStringNormal(0xBDBB5B, x2,
					RSInterface.interfaceCache[33000 + id].message, y2, true);
			smallText.drawShadowedStringNormal(0xFFFF00, x3,
					RSInterface.interfaceCache[33100 + id].message, y3, true);
			setHovers(id, false);
		} else if (type == "Buy") {
			if (super.mouseX >= x && super.mouseX <= x + 140
					&& super.mouseY >= y && super.mouseY <= y + 110
					&& !menuOpen) {
				SpriteCache.fetchIfNeeded(654);
				BuyHover = SpriteCache.spriteCache[654];
				if (BuyHover != null)
					BuyHover.drawSprite(x, y);
			} else {
				SpriteCache.spriteCache[29].drawSprite(x, y);
			}
			setGrandExchange(id, false);
			if (slotAborted[id] || slotColorPercent[id] == 100) {
				changeSet(id, true, false);
			} else {
				changeSet(id, true, true);
			}
			drawPercentage(id);
			smallText.drawShadowedStringNormal(0xCC9900, x2,
					RSInterface.interfaceCache[32000 + id].message, y2 - minus,
					true);
			smallText.drawShadowedStringNormal(0xBDBB5B, x2,
					RSInterface.interfaceCache[33000 + id].message, y2, true);
			smallText.drawShadowedStringNormal(0xFFFF00, x3,
					RSInterface.interfaceCache[33100 + id].message, y3, true);
			setHovers(id, false);
		} else if (type == "Submit Buy") {
			if (super.mouseX >= x && super.mouseX <= x + 140
					&& super.mouseY >= y && super.mouseY <= y + 110
					&& !menuOpen) {
				SpriteCache.fetchIfNeeded(656);
				buySubmitHover = SpriteCache.spriteCache[656];
				if (buySubmitHover != null)
					buySubmitHover.drawSprite(x, y);
			} else {
				SpriteCache.spriteCache[27].drawSprite(x, y);
			}
			setGrandExchange(id, false);
			changeSet(id, false, false);
			smallText.drawShadowedStringNormal(0xCC9900, x2,
					RSInterface.interfaceCache[32000 + id].message, y2 - minus,
					true);
			smallText.drawShadowedStringNormal(0xBDBB5B, x2,
					RSInterface.interfaceCache[33000 + id].message, y2, true);
			smallText.drawShadowedStringNormal(0xFFFF00, x3,
					RSInterface.interfaceCache[33100 + id].message, y3, true);
			setHovers(id, false);
		} else if (type == "Submit Sell") {
			if (super.mouseX >= x && super.mouseX <= x + 140
					&& super.mouseY >= y && super.mouseY <= y + 110
					&& !menuOpen) {
				SpriteCache.fetchIfNeeded(657);
				sellSubmitHover = SpriteCache.spriteCache[657];
				if (sellSubmitHover != null)
					sellSubmitHover.drawSprite(x, y);
			} else {
				SpriteCache.spriteCache[28].drawSprite(x, y);
			}
			setGrandExchange(id, false);
			changeSet(id, false, false);
			smallText.drawShadowedStringNormal(0xCC9900, x2,
					RSInterface.interfaceCache[32000 + id].message, y2 - minus,
					true);
			smallText.drawShadowedStringNormal(0xBDBB5B, x2,
					RSInterface.interfaceCache[33000 + id].message, y2, true);
			smallText.drawShadowedStringNormal(0xFFFF00, x3,
					RSInterface.interfaceCache[33100 + id].message, y3, true);
			setHovers(id, false);
		} else if (type == "Regular") {
			setGrandExchange(id, true);
			setHovers(id, true);
		} else if (type == "Finished Selling") {
			if (super.mouseX >= x && super.mouseX <= x + 140
					&& super.mouseY >= y && super.mouseY <= y + 110
					&& !menuOpen) {
				SpriteCache.fetchIfNeeded(655);
				SellHover = SpriteCache.spriteCache[655];
				if (SellHover != null)
					SellHover.drawSprite(x, y);
			} else {
				SpriteCache.spriteCache[30].drawSprite(x, y);
			}
			setGrandExchange(id, false);
			changeSet(id, true, false);
			drawPercentage(id);
			smallText.drawShadowedStringNormal(0xCC9900, x2,
					RSInterface.interfaceCache[32000 + id].message, y2 - minus,
					true);
			smallText.drawShadowedStringNormal(0xBDBB5B, x2,
					RSInterface.interfaceCache[33000 + id].message, y2, true);
			smallText.drawShadowedStringNormal(0xFFFF00, x3,
					RSInterface.interfaceCache[33100 + id].message, y3, true);
			setHovers(id, false);
		} else if (type == "Finished Buying") {
			if (super.mouseX >= x && super.mouseX <= x + 140
					&& super.mouseY >= y && super.mouseY <= y + 110
					&& !menuOpen) {
				SpriteCache.fetchIfNeeded(656);
				BuyHover = SpriteCache.spriteCache[656];
				if (BuyHover != null)
					BuyHover.drawSprite(x, y);
			} else {
				SpriteCache.spriteCache[29].drawSprite(x, y);
			}
			setGrandExchange(id, false);
			changeSet(id, true, false);
			drawPercentage(id);
			smallText.drawShadowedStringNormal(0xCC9900, x2,
					RSInterface.interfaceCache[32000 + id].message, y2 - minus,
					true);
			smallText.drawShadowedStringNormal(0xBDBB5B, x2,
					RSInterface.interfaceCache[33000 + id].message, y2, true);
			smallText.drawShadowedStringNormal(0xFFFF00, x3,
					RSInterface.interfaceCache[33100 + id].message, y3, true);
			setHovers(id, false);
		}
	}

	public Sprite per0;
	public Sprite per1;
	public Sprite per2;
	public Sprite per3;
	public Sprite per4;
	public Sprite per5;
	public Sprite per6;
	public Sprite abort1;
	public Sprite abort2;
	public Sprite SellHover;
	public Sprite BuyHover;
	public Sprite sellSubmitHover;
	public Sprite buySubmitHover;

	public void drawPercentage(int id) {
		SpriteCache.fetchIfNeeded(643);
		SpriteCache.fetchIfNeeded(644);
		SpriteCache.fetchIfNeeded(645);
		SpriteCache.fetchIfNeeded(646);
		SpriteCache.fetchIfNeeded(650);
		per0 = SpriteCache.spriteCache[643];
		per1 = SpriteCache.spriteCache[644];
		per2 = SpriteCache.spriteCache[645];
		per3 = SpriteCache.spriteCache[646];
		abort1 = SpriteCache.spriteCache[650];
		int x = 0;
		int y = 0;
		boolean fixed = (clientSize == 0);
		switch (id) {
		case 1:
			x = fixed ? 30 + 8 : (clientWidth / 2 - 226 + 8);
			y = fixed ? 74 + 81 : (clientHeight / 2 - 93 + 81);
			break;
		case 2:
			x = fixed ? 186 + 8 : (clientWidth / 2 - 70 + 8);
			y = fixed ? 74 + 81 : (clientHeight / 2 - 93 + 81);
			break;
		case 3:
			x = fixed ? 342 + 8 : (clientWidth / 2 + 86 + 8);
			y = fixed ? 74 + 81 : (clientHeight / 2 - 93 + 81);
			break;
		case 4:
			x = fixed ? 30 + 8 : (clientWidth / 2 - 226 + 8);
			y = fixed ? 194 + 81 : (clientHeight / 2 + 27 + 81);
			break;
		case 5:
			x = fixed ? 186 + 8 : (clientWidth / 2 - 70 + 8);
			y = fixed ? 194 + 81 : (clientHeight / 2 + 27 + 81);
			break;
		case 6:
			x = fixed ? 342 + 8 : (clientWidth / 2 + 86 + 8);
			y = fixed ? 194 + 81 : (clientHeight / 2 + 27 + 81);
			break;
		}
		x -= 2;
		if (slotColorPercent[id] > 100) {
			slotColorPercent[id] = 100;
		}
		int s = 0;
		if (!slotAborted[id]) {
			for (int k9 = 1; k9 < slotColorPercent[id]; k9++) {
				if (slotColorPercent[id] > 0) {
					if (k9 == 1) {
						if (per0 != null)
							per0.drawSprite(x, y);
						x += 2;
					} else if (k9 == 2) {
						if (per1 != null)
							per1.drawSprite(x, y);
						x += 2;
					} else if (k9 >= 6 && k9 <= 14) {
						if (per3 != null)
							per3.drawSprite(x, y);
						x += 1;
					} else if (k9 >= 56 && k9 <= 65) {
						if (per3 != null)
							per3.drawSprite(x, y);
						x += 1;
					} else if (k9 >= 76 && k9 <= 82) {
						if (per3 != null)
							per3.drawSprite(x, y);
						x += 1;
					} else {
						if (s == 0) {
							if (per2 != null)
								per2.drawSprite(x, y);
							x += 2;
							s += 1;
						} else if (s == 1) {
							if (per3 != null)
								per3.drawSprite(x, y);
							x += 1;
							s += 1;
						} else if (s == 2) {
							if (per3 != null)
								per3.drawSprite(x, y);
							x += 1;
							s = 0;
						} else if (s == 4) {
							if (per3 != null)
								per3.drawSprite(x, y);
							x += 1;
							s = 0;
						}
					}
				}
			}
		} else {
			if (abort1 != null)
				abort1.drawSprite(x, y);
		}
	}

	public void setGrandExchange(int id, boolean on) {
		switch (id) {
		case 1:
			if (on) {
				RSInterface.interfaceCache[24505].tooltip = "Buy";
				RSInterface.interfaceCache[24511].tooltip = "Sell";
				changeSet(id, false, false);
			} else {
				RSInterface.interfaceCache[24505].tooltip = "[GE]";
				RSInterface.interfaceCache[24511].tooltip = "[GE]";
			}
			break;
		case 2:
			if (on) {
				RSInterface.interfaceCache[24523].tooltip = "Buy";
				RSInterface.interfaceCache[24526].tooltip = "Sell";
				changeSet(id, false, false);
			} else {
				RSInterface.interfaceCache[24523].tooltip = "[GE]";
				RSInterface.interfaceCache[24526].tooltip = "[GE]";
			}
			break;
		case 3:
			if (on) {
				RSInterface.interfaceCache[24514].tooltip = "Buy";
				RSInterface.interfaceCache[24529].tooltip = "Sell";
				changeSet(id, false, false);
			} else {
				RSInterface.interfaceCache[24514].tooltip = "[GE]";
				RSInterface.interfaceCache[24529].tooltip = "[GE]";
			}
			break;
		case 4:
			if (on) {
				RSInterface.interfaceCache[24508].tooltip = "Buy";
				RSInterface.interfaceCache[24532].tooltip = "Sell";
				changeSet(id, false, false);
			} else {
				RSInterface.interfaceCache[24508].tooltip = "[GE]";
				RSInterface.interfaceCache[24532].tooltip = "[GE]";
			}
			break;
		case 5:
			if (on) {
				RSInterface.interfaceCache[24517].tooltip = "Buy";
				RSInterface.interfaceCache[24535].tooltip = "Sell";
				changeSet(id, false, false);
			} else {
				RSInterface.interfaceCache[24517].tooltip = "[GE]";
				RSInterface.interfaceCache[24535].tooltip = "[GE]";
			}
			break;
		case 6:
			if (on) {
				RSInterface.interfaceCache[24520].tooltip = "Buy";
				RSInterface.interfaceCache[24538].tooltip = "Sell";
				changeSet(id, false, false);
			} else {
				RSInterface.interfaceCache[24520].tooltip = "[GE]";
				RSInterface.interfaceCache[24538].tooltip = "[GE]";
			}
			break;
		}
	}

	public void changeSet(int id, boolean view, boolean abort) {
		switch (id) {
		case 1:
			if (view) {
				RSInterface.interfaceCache[24543].tooltip = "View offer";
			} else {
				RSInterface.interfaceCache[24543].tooltip = "[GE]";
			}
			if (abort) {
				RSInterface.interfaceCache[24541].tooltip = "Abort offer";
			} else {
				RSInterface.interfaceCache[24541].tooltip = "[GE]";
			}
			break;
		case 2:
			if (view) {
				RSInterface.interfaceCache[24547].tooltip = "View offer";
			} else {
				RSInterface.interfaceCache[24547].tooltip = "[GE]";
			}
			if (abort) {
				RSInterface.interfaceCache[24545].tooltip = "Abort offer";
			} else {
				RSInterface.interfaceCache[24545].tooltip = "[GE]";
			}
			break;
		case 3:
			if (view) {
				RSInterface.interfaceCache[24551].tooltip = "View offer";
			} else {
				RSInterface.interfaceCache[24551].tooltip = "[GE]";
			}
			if (abort) {
				RSInterface.interfaceCache[24549].tooltip = "Abort offer";
			} else {
				RSInterface.interfaceCache[24549].tooltip = "[GE]";
			}
			break;
		case 4:
			if (view) {
				RSInterface.interfaceCache[24555].tooltip = "View offer";
			} else {
				RSInterface.interfaceCache[24555].tooltip = "[GE]";
			}
			if (abort) {
				RSInterface.interfaceCache[24553].tooltip = "Abort offer";
			} else {
				RSInterface.interfaceCache[24553].tooltip = "[GE]";
			}
			break;
		case 5:
			if (view) {
				RSInterface.interfaceCache[24559].tooltip = "View offer";
			} else {
				RSInterface.interfaceCache[24559].tooltip = "[GE]";
			}
			if (abort) {
				RSInterface.interfaceCache[24557].tooltip = "Abort offer";
			} else {
				RSInterface.interfaceCache[24557].tooltip = "[GE]";
			}
			break;
		case 6:
			if (view) {
				RSInterface.interfaceCache[24563].tooltip = "View offer";
			} else {
				RSInterface.interfaceCache[24563].tooltip = "[GE]";
			}
			if (abort) {
				RSInterface.interfaceCache[24561].tooltip = "Abort offer";
			} else {
				RSInterface.interfaceCache[24561].tooltip = "[GE]";
			}
			break;
		}
	}

	public void setHovers(int id, boolean on) {
		switch (id) {
		case 1:
			if (!on) {
			} else {
			}
			break;
		case 2:
			if (!on) {
			} else {
			}
			break;
		case 3:
			if (!on) {
			} else {
			}
			break;
		case 4:
			if (!on) {
			} else {
			}
			break;
		case 5:
			if (!on) {
			} else {
			}
			break;
		case 6:
			if (!on) {
			} else {
			}
			break;
		}
	}

	public void tabToReplyPm() {
		String name = null;
		for (int j = 0; j < 100; j++)
			if (chatMessages[j] != null) {
				int chatType = chatTypes[j];
				if (chatType == 3 || chatType == 7) {
					name = chatNames[j];
					break;
				}
			}
		if (name != null) {
			try {
				do {
					name = name.substring(name.indexOf(">") + 1);
				} while (name.indexOf(">") != -1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (name == null)
			pushMessage("You have not recieved any messages.", 0, "");
		try {
			if (name != null) {
				/*long namel = TextClass.longForName(name.trim());
				int node = -1;
				for (int count = 0; count < friendsCount; count++) {
					if (friendsListAsLongs[count] != namel)
						continue;
					node = count;
					break;
				}
				if (node != -1 && friendsNodeIDs[node] > 0) {
					inputTaken = true;
					inputDialogState = 0;
					showChat = true;
					promptInput = "";
					friendsListAction = 3;
					aLong953 = friendsListAsLongs[node];
					promptMessage = "Enter message to send to " + friendsList[node];
				} else {
					pushMessage(capitalize(name) + " is currently offline.", 0, "");
				}*/
				long userLong = TextClass.longForName(name.trim());
				int friendIndex = -1;
				for (int index = 0; index < friendsCount; index++) {
					if (friendsListAsLongs[index] != userLong)
						continue;
					friendIndex = index;
					break;
				}
				
				if (friendIndex != -1 && friendsNodeIDs[friendIndex] > 0) {
					System.out.println("input taken!");
					inputTaken = true;
					inputDialogState = 0;
					showInput = true;
					promptInput = "";
					friendsListAction = 3;
					aLong953 = friendsListAsLongs[friendIndex];
					promptMessage = "Enter message to send to " + friendsList[friendIndex];
				} else {
					pushMessage(capitalize(name) + " is currently offline.", 0, "");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getClientWidth() {
		return clientWidth;
	}

	public int getClientHeight() {
		return clientHeight;
	}

	public static Client getClient() {
		return instance;
	}

	public void launchURL(String url) {
		//System.out.println("Launching URL: " + url);
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
				Method openURL = fileMgr.getDeclaredMethod("openURL",
						new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows"))
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			else {
				String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape", "safari" };
				String browser = null;
				for (int count = 0; count < browsers.length && browser == null; count++)
					if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
						browser = browsers[count];
				if (browser == null) {
					throw new Exception("Could not find web browser");
				} else
					Runtime.getRuntime().exec(new String[] { browser, url });
			}
		} catch (Exception e) {
			pushMessage("Failed to open URL.", 0, "");
		}
	}

	static {
		levelXPs = new int[99];
		int i = 0;
		for (int j = 0; j < 99; j++) {
			int l = j + 1;
			int i1 = (int) ((double) l + 300D * Math.pow(2D, (double) l / 7D));
			i += i1;
			levelXPs[j] = i / 4;
		}
		bit_mask = new int[32];
		i = 2;
		for (int k = 0; k < 32; k++) {
			bit_mask[k] = i - 1;
			i += i;
		}
	}

	public Sprite[] WorldOrb = new Sprite[2];
	public boolean[] worldMap = new boolean[2];

	public void loadOrbs() {
		drawAdv();
		drawWorldMap();
	}

	public void drawAdv() {
		ADVISOR[!advisorHover ? 0 : 1].drawSprite(207, 0);
		if (super.clickMode2 == 1 && super.mouseX > 724 && super.mouseX < 743
				&& super.mouseY > 1 && super.mouseY < 20) {
			ADVISOR[2].drawSprite(207, 0);
		}
	}

	public void drawWorldMap() {
		if (clientSize == 0) {
			if (super.mouseX >= 522 && super.mouseX <= 558
					&& super.mouseY >= 124 && super.mouseY < 161)
				SpriteCache.spriteCache[52].drawSprite(7, 123);
			else
				SpriteCache.spriteCache[51].drawSprite(7, 123);
		} else {
			cacheSprite[37].drawSprite(clientWidth - 45, 129);
			if (super.mouseX >= clientWidth - 48
					&& super.mouseX <= clientWidth - 5 && super.mouseY >= 121
					&& super.mouseY <= 171)
				SpriteCache.spriteCache[52].drawSprite(clientWidth - 41, 133);
			else
				SpriteCache.spriteCache[51].drawSprite(clientWidth - 41, 133);
		}

	}

	public int logIconHPos = 0;

	public static FamiliarHandler getFamiliar() {
		return familiarHandler;
	}

	public Register getRegister() {
		return register;
	}

	public boolean mouseInRegion2(int x1, int x2, int y1, int y2) {
		if (super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1
				&& super.mouseY <= y2) {
			return true;
		}
		return false;
	}

	public boolean clickInRegion2(int x1, int x2, int y1, int y2) {
		if (super.saveClickX >= x1 && super.saveClickX <= x2
				&& super.saveClickY >= y1 && super.saveClickY <= y2) {
			return true;
		}
		return false;
	}

	public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
		if (super.mouseX >= x1 && super.mouseX <= x2 && super.mouseY >= y1
				&& super.mouseY <= y2)
			return true;
		return false;
	}

	public boolean clickInRegion(int x1, int y1, int x2, int y2) {
		if (super.saveClickX >= x1 && super.saveClickX <= x2
				&& super.saveClickY >= y1 && super.saveClickY <= y2)
			return true;
		return false;
	}

	public boolean logHover = false;
	public boolean advisorHover = false;
	public Sprite[] ADVISOR = new Sprite[5];
	public Sprite[] orbs = new Sprite[20];
	public Sprite[] LOGOUT = new Sprite[5];

	private static String intToKOrMilLongName(int i) {
		String s = String.valueOf(i);
		for (int k = s.length() - 3; k > 0; k -= 3)
			s = s.substring(0, k) + "," + s.substring(k);
		if (s.length() > 8)
			s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@("
					+ s + ")";
		else if (s.length() > 4)
			s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
		return " " + s;
	}

	public void hitmarkDraw(Entity e, int hitLength, int type, int icon, int damage, int soak, int move, int opacity, int mask, boolean local) {
		int drawPos = 0;
		boolean damage10 = getOption("damage_10");
		if(getSetting("hitsplats") == 632) {
			if(!getOption("absorb"))
				soak = 0;
			if(local) {
				switch(type) {
					case 0:
						type = 5;
						break;
					case 2:
						type = 7;
						break;
					case 3:
						type = 8;
						break;
					case 4:
						type = 9;
						break;
				}
			}
			if (mask == 0) {
				e.hitMarkPos[0] = spriteDrawY + move;
				drawPos = e.hitMarkPos[0];
			}
			if (mask != 0) {
				e.hitMarkPos[mask] = e.hitMarkPos[0] + (19 * mask);
				drawPos = e.hitMarkPos[mask];
			}
			if (damage > 0) {
				Sprite end1 = null, middle = null, end2 = null;
				int x = 0;
				switch (hitLength) {
				/* Trial and error shit, terrible hardcoding :( */
				case 1:
					x = 8;
					break;
				case 2:
					x = 4;
					break;
				case 3:
					x = 1;
					break;
				}
				if (soak > 0)
					x -= 16;
				end1 = SpriteCache.spriteCache[81 + (type * 3)];
				middle = SpriteCache.spriteCache[81 + (type * 3) + 1];
				end2 = SpriteCache.spriteCache[81 + (type * 3) + 2];
				if (icon != 255 && getOption("combat_icons")) {
					SpriteCache.spriteCache[114 + icon].drawSprite3(spriteDrawX
							- 34 + x, drawPos - 14, opacity);
				}
				int iconXOff = (icon == 255) ? -5 : 0;
				end1.drawSprite3(spriteDrawX - 12 + x + iconXOff, drawPos - 12, opacity);
				x += 4;
				for (int i = 0; i < hitLength * 2; i++) {
					middle.drawSprite3(spriteDrawX - 12 + x + iconXOff, drawPos - 12, opacity);
					x += 4;
				}
				end2.drawSprite3(spriteDrawX - 12 + x + iconXOff, drawPos - 12, opacity);
				(type == 1 ? bigHit : smallHit).drawOpacityText(0xffffff, String.valueOf(damage), drawPos + (type == 1 ? 2 : 32),
						spriteDrawX + 4 + iconXOff + (soak > 0 ? -16 : 0), opacity);
				if (soak > 0)
					drawSoak(soak, opacity, drawPos, x);
			} else {
				SpriteCache.fetchIfNeeded(673);
				Sprite block = SpriteCache.spriteCache[673];
				if (block != null)
					block.drawSprite3(spriteDrawX - 12, drawPos - 14, opacity);
			}
		} else if(getSetting("hitsplats") == 562) {
			if(damage > 0 ) {
				switch(type) {
				case 3:
					type = 4;
					break;
				case 4:
					type = 8;
					break;
				}
				int spriteId = 683 + type + ((damage10 && type != 8) ? 1 : 0);
				SpriteCache.get(spriteId).drawAdvancedSprite(spriteDrawX - (damage > 20 ? 10 : 12), spriteDrawY - 12);
				smallText.drawString(0xffffff, String.valueOf(damage), spriteDrawY + 5, spriteDrawX - 4);
			} else {
				Sprite block = SpriteCache.get(682);
				if (block != null) {
					block.drawAdvancedSprite(spriteDrawX - 12, spriteDrawY - 14);
					smallText.drawString(0xffffff, "0", spriteDrawY + 2, spriteDrawX - 3);
				}
			}
		} else {
			if(damage > 0 ) {
				if(type > 3)
					return;
				int spriteId = type + (type == 0 ? 1 : 0);
				hitMarks[spriteId].drawSprite(spriteDrawX - (damage > 20 ? 10 : 12), spriteDrawY - 12);
				smallText.drawString(0xffffff, String.valueOf(damage), spriteDrawY + 5, spriteDrawX - 4);
			} else {
				Sprite block = hitMarks[0];
				if (block != null) {
					block.drawSprite(spriteDrawX - 12, spriteDrawY - 14);
					smallText.drawString(0xffffff, "0", spriteDrawY + 2, spriteDrawX - 3);
				}
			}
		}
	}

	public int dayTimeShades() {
		Calendar calendar = new GregorianCalendar();
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		long time = min + (sec / 60);
		if (time > 30 && time <= 57.5)
			return 0x000000;
		else if (time > 57.5 && time < 61)
			return 0x669999;
		else if (time > 0 && time <= 2.5)
			return 0x99CCCC;
		else if (time > 2.5 && time <= 5)
			return 0x66CCCC;
		else if (time > 5 && time <= 7.5)
			return 0x99FFFF;
		else if (time > 7.5 && time <= 10)
			return 0xCCFFCC;
		else if (time > 10 && time <= 17.5)
			return 0xFFFF66;
		else if (time > 17.5 && time <= 25)
			return 0xFFCC33;
		else if (time > 25 && time <= 27.5)
			return 0xFF9900;
		else if (time > 27.5 && time <= 30)
			return 0xCC3300;
		return 0xCC3300;
	}

	public void drawSoak(int damage, int opacity, int drawPos, int x) {
		x -= 12;
		int soakLength = String.valueOf(damage).length();
		SpriteCache.spriteCache[114 + 5].drawSprite3(spriteDrawX + x,
				drawPos - 12, opacity);
		x += 20;
		SpriteCache.spriteCache[81 + 30].drawSprite3(spriteDrawX + x,
				drawPos - 12, opacity);
		x += 4;
		for (int i = 0; i < soakLength * 2; i++) {
			SpriteCache.spriteCache[81 + 31].drawSprite3(spriteDrawX + x,
					drawPos - 12, opacity);
			x += 4;
		}
		SpriteCache.spriteCache[81 + 32].drawSprite3(spriteDrawX + x,
				drawPos - 10, opacity);
		smallHit.drawOpacityText(0xffffff, String.valueOf(damage),
				drawPos + 32, spriteDrawX - 8 + x + (soakLength == 1 ? 5 : 0),
				opacity);
	}
}