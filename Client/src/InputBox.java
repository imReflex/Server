
/**
 * 
 * @author Allen
 *
 */
public class InputBox extends ScreenComponent {

	private int width;
	private int loginPos;
	private String string;
	
	public InputBox(int x, int y, int width, int loginPos, String string, String title) {
		super(x, y, title);
		this.width = width;
		this.loginPos = loginPos;
		this.string = string;
	}

	@Override
	public void render() {
		int mouseX = Client.instance.mouseX;
		int mouseY = Client.instance.mouseY;
		if(title != null && title != "")
			Client.instance.drawingArea.drawStringLeft(title, x - Client.instance.drawingArea.getStringWidth(title) / 8 - 5, 0xffffff, y + 16);
		//FontArchive.cache[1].drawString(title, x - FontArchive.cache[0].stringWidth(title) - 5, y + 16, 0xffffff, true);
		SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(776).myWidth * (width + 3))) && mouseY > y && mouseY <= (y + SpriteCache.get(776).myHeight)) ? 779 : 776).drawAdvancedSprite(x, y);
		for(int i = 1; i <= width + 1; i++) {
			int xpos = x + (SpriteCache.get(777).myWidth * i);
			int ypos = y;
			SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(777).myWidth * (width + 3))) && mouseY > y && mouseY <= (ypos + SpriteCache.get(777).myHeight)) ? 780 : 777)
				.drawAdvancedSprite(xpos, ypos);
		}
		SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(778).myWidth * (width + 3))) && mouseY > y && mouseY <= (y + SpriteCache.get(778).myHeight)) ? 781 : 778).drawAdvancedSprite(x + (SpriteCache.get(777).myWidth * (width + 2)), y);
		Client.instance.drawingArea.drawShadowedString(false, x + 6, 16777215,
				new StringBuilder().append("").append(string).append(((Client.instance.loginScreenCursorPos == loginPos ? 1 : 0) 
						& (Client.instance.loopCycle % 40 < 20 ? 1 : 0)) != 0 ? "|" : "").toString(), y + 17);
	}

	@Override
	public void click(int clickX, int clickY) {
		if(clickX >= x && clickX <= x + (SpriteCache.get(778).myWidth * (this.width + 3)) && clickY >= y && clickY <= (y + SpriteCache.get(778).myHeight))
			doAction();
	}

	@Override
	public void doAction() {
		
	}
	
	/**
	 * Re-set the string.
	 * @param string
	 */
	public void setString(String string) {
		this.string = string;
	}

}
