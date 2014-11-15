
public class Checkbox extends ScreenComponent {

	public boolean condition;
	private int width,height;
	
	public Checkbox(int x, int y, boolean condition, String title) {
		super(x, y, title);
		this.condition = condition;
	}

	@Override
	public void render() {
		int mouseX = Client.instance.mouseX;
		int mouseY = Client.instance.mouseY;
		width = SpriteCache.get(766).myWidth;
		height = SpriteCache.get(766).myHeight;
		if(!condition) {
			SpriteCache.get((mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) ? 767 : 766).drawAdvancedSprite(x, y);
		} else {
			SpriteCache.get((mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height) ? 769 : 768).drawAdvancedSprite(x, y);
		}
		int xoff = x + width + 2;
		int yoff = y + 12;
		Client.instance.arial[0].drawString(title, xoff, yoff, 0xffffff, true);
	}

	@Override
	public void click(int clickX, int clickY) {
		if((clickX > x && clickX < x + width && clickY > y && clickY < y + height)) {
			condition = !condition;
			doAction();
		}
	}

	@Override
	public void doAction() {
		
	}

}
