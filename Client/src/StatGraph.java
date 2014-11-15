public class StatGraph {

	private String title;
	private int[] values = new int[75];
	private int max;
	public static Client Client;
	
	public StatGraph(String title) {
		this.title = title;
	}
	
	public void addValue(int value) {
		int high = 0;
		for (int i = this.values.length - 1; i > 0; i--) {
			this.values[i] = this.values[i-1];
		}
		this.values[0] = value;
		for (int i = 0; i < this.values.length; i++) {
			if (this.values[i] > high)
				high = this.values[i];
		}
		this.max = high;
	}
	
	public void draw(int x, int y) {
		int width = 150;
		int height = 110;
		Client.smallText.drawStringCenter(0xFFFFFF, x + 10 + width / 2, this.title, y, true);
		Client.smallText.drawStringCenter(0xFFFFFF, this.max + "", x+7, y+18);
		Client.smallText.drawStringCenter(0xABCDEF, values[0] + "", x + 7, y + 30);
		Client.smallText.drawStringCenter(0xFFFFFF, "0", x + 7, y + 11 + height);
		DrawingArea.drawVLine(x+10, y+10, height, 0xFFFFFF);
		DrawingArea.drawHLine(x+10, y+10+height, width, 0xFFFFFF);
		for (int i = 0; i < this.values.length - 1; i++)
			DrawingArea.drawDiagonalLine(x + 11 + ((width / values.length) * i), y + 9 + height - ((((values[i] * 90) / max) * height) / 100), x + 11 + ((width / values.length) * (i + 1)), y + 9 + height - ((((values[i + 1] * 90) / max) * height) / 100), 0xABCDEF);
	}
	
}