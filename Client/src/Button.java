
/**
 * @author Allen
 */
public class Button extends ScreenComponent {

	private int width;
	
	public Button(int x, int y, int width, String title) {
		super(x, y, title);
		this.width = width;
	}

	@Override
	public void render() {
		int mouseX = Client.instance.mouseX;
		int mouseY = Client.instance.mouseY;
		SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(770).myWidth * (width + 3))) && mouseY > y && mouseY <= (y + SpriteCache.get(770).myHeight)) ? 773 : 770).drawAdvancedSprite(x, y);
		for(int i = 1; i <= width + 1; i++) {
			int xpos = x + (SpriteCache.get(771).myWidth * i);
			int ypos = y;
			SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(771).myWidth * (width + 3))) && mouseY > y && mouseY <= (ypos + SpriteCache.get(771).myHeight)) ? 774 : 771)
				.drawAdvancedSprite(xpos, ypos);
		}
		SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(772).myWidth * (width + 3))) && mouseY > y && mouseY <= (y + SpriteCache.get(772).myHeight)) ? 775 : 772).drawAdvancedSprite(x + (SpriteCache.get(772).myWidth * (width + 2)), y);
		int xoff = (SpriteCache.get(771).myWidth * (width + 3));
		Client.instance.newRegularFont.drawCenteredString(title, x + (xoff / 2), y + 16, 0xFEC949, 0x000000);
	}

	@Override
	public void click(int clickX, int clickY) {
		if(clickX >= x && clickX <= (x + (SpriteCache.get(772).myWidth * (width + 3))) && clickY >= y && clickY <= (y + SpriteCache.get(772).myHeight))
			doAction();
	}

	@Override
	public void doAction() {
		
	}

}
