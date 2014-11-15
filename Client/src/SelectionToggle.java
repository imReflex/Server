
public class SelectionToggle extends ScreenComponent {

	private int[] options;
	public int currentOption;
	
	public SelectionToggle(int x, int y, String title, int[] options, int currentOption) {
		super(x, y, title);
		this.options = options;
		this.currentOption = currentOption;
	}

	@Override
	public void render() {
		int mouseX = Client.instance.mouseX;
		int mouseY = Client.instance.mouseY;
		if(title != null && title != "")
			Client.instance.drawingArea.drawStringLeft(title, x - Client.instance.drawingArea.getStringWidth(title) / 8 - 5, 0xffffff, y + 16);
		SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(776).myWidth * (3))) && mouseY > y && mouseY <= (y + SpriteCache.get(776).myHeight)) ? 779 : 776).drawAdvancedSprite(x, y);
			SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(777).myWidth * (3))) && mouseY > y && mouseY <= (y + SpriteCache.get(777).myHeight)) ? 780 : 777)
				.drawAdvancedSprite(x + SpriteCache.get(777).myWidth, y);
		SpriteCache.get((mouseX >= x && mouseX <= (x + (SpriteCache.get(778).myWidth * (3))) && mouseY > y && mouseY <= (y + SpriteCache.get(778).myHeight)) ? 781 : 778).drawAdvancedSprite(x + (SpriteCache.get(777).myWidth * (2)), y);
		Client.instance.drawingArea.drawShadowedStringNormal(0xffffff, x + 4, String.valueOf(options[getIndexForOption(currentOption)]), y + 17, true);
	}

	@Override
	public void click(int clickX, int clickY) {
		if(clickX >= x && clickX <= x + (SpriteCache.get(778).myWidth * (3)) && clickY >= y && clickY <= (y + SpriteCache.get(778).myHeight)) {
			nextSelection();
			doAction();
		}
	}

	@Override
	public void doAction() {
		
	}
	
	private int getIndexForOption(int current) {
		for(int index = 0; index < options.length; index++)
			if(current == options[index])
				return index;
		return 0;
	}
	
	private void nextSelection() {
		int option = getIndexForOption(currentOption);
		if((option + 1) >= options.length)
			option = 0;
		else
			option += 1;
		currentOption = options[option];
	}

}
