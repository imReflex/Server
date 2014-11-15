

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseWheelEvent;

@SuppressWarnings("all")
final class RSFrame extends Frame {

	public RSFrame(RSApplet rsapplet, int width, int height, boolean undecorative, boolean resizable) {
		rsApplet = rsapplet;
		setTitle(Configuration.CLIENT_NAME + " | " + GetDate.currentDate());
		setUndecorated(undecorative);
		setResizable(resizable);
		
		setVisible(true);
		Insets insets = this.getInsets();
		setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);// 28
		Client.getClient();
		setLocation((screenWidth - width) / 2, ((screenHeight - height) / 2) - screenHeight == Client.getMaxHeight() ? 0 : undecorative ? 0 : 70);
		requestFocus();
		toFront();
		this.setFocusTraversalKeysEnabled(false);
		setBackground(Color.BLACK);
	}
	public void mouseWheelMoved(MouseWheelEvent event) {
		rsApplet.mouseWheelMoved(event);
	}

	public Graphics getGraphics() {
		Graphics g = super.getGraphics();
		Insets insets = this.getInsets();
		g.translate(insets.left, insets.top);
		return g;
	}

	public int getFrameWidth() {
		Insets insets = this.getInsets();
		return getWidth() - (insets.left + insets.right);
	}

	public int getFrameHeight() {
		Insets insets = this.getInsets();
		return getHeight() - (insets.top + insets.bottom);
	}

	public void update(Graphics g) {
		rsApplet.update(g);
	}

	public void paint(Graphics g) {
		rsApplet.paint(g);
	}

	final RSApplet rsApplet;
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Dimension screenSize = toolkit.getScreenSize();
	int screenWidth = (int) screenSize.getWidth();
	int screenHeight = (int) screenSize.getHeight();
}