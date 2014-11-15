
/**
 * 
 * @author Allen
 *
 */
public abstract class ScreenComponent {

	protected int x;
	protected int y;
	protected String title;
	
	public ScreenComponent(int x, int y, String title) {
		this.x = x;
		this.y = y;
		this.title = title;
	}
	
	public abstract void render();
	
	public abstract void click(int clickX, int clickY);
	
	public abstract void doAction();
	
}
