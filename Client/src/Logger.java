



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Logger extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCommandsGoHere;
	public static JTextPane textPane;
	private JButton btnNewButton_1;
	static int i1 = 1;
	private JCheckBox enabled;

	public static void addLog(String type, String text, Color c, Color c2) {
		StyledDocument doc = textPane.getStyledDocument();
		Style style = textPane.addStyle("I'm a Style", null);
		try {
			doc.insertString(doc.getLength(), "[", style);
		} catch (BadLocationException e) {
		}
		StyleConstants.setForeground(style, c);
		try {
			doc.insertString(doc.getLength(), "" + type + "", style);
		} catch (BadLocationException e) {
		}
		StyleConstants.setForeground(style, Color.BLACK);
		try {
			doc.insertString(doc.getLength(), "] ", style);
		} catch (BadLocationException e) {
		}
		StyleConstants.setForeground(style, c2);
		try {
			doc.insertString(doc.getLength(), text + "\n", style);
		} catch (BadLocationException e) {
		}
	}

	public static void centreWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	/**
	 * Create the frame.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public Logger() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		super("Logger");
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		setBounds(100, 100, 616, 286);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		centreWindow(this);
		textPane = new JTextPane();
		textPane.setBounds(10, 11, 507, 188);
		textPane.setEditable(false);
		final JButton btnNewButton = new JButton("Send command");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Client.stream.createFrame(103);
				Client.stream.writeByte1(txtCommandsGoHere.getText().length() + 1);
				Client.stream.writeString(txtCommandsGoHere.getText());
				addLog("COMMAND", txtCommandsGoHere.getText(), Color.MAGENTA, Color.GRAY);
			}
		});
		btnNewButton.setBounds(249, 213, 105, 24);
		contentPane.add(btnNewButton);

		txtCommandsGoHere = new JTextField();
		txtCommandsGoHere.setText("Commands go here (Example, item 4151 1)");
		txtCommandsGoHere.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					btnNewButton.doClick();
			}
		});
		txtCommandsGoHere.setBounds(10, 213, 229, 24);
		contentPane.add(txtCommandsGoHere);
		txtCommandsGoHere.setColumns(10);

		JScrollPane scrollPane = new JScrollPane(textPane);
		scrollPane.setBounds(10, 11, 580, 188);
		contentPane.add(scrollPane);

		btnNewButton_1 = new JButton("Clear");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (javax.swing.JOptionPane.showConfirmDialog(null, "Are you sure you would like to clear?", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					textPane.setText("");
				}
			}
		});
		btnNewButton_1.setBounds(533, 214, 57, 23);
		contentPane.add(btnNewButton_1);

		final JCheckBox chckbxNewCheckBox = new JCheckBox("Editable");
		chckbxNewCheckBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textPane.setEditable(chckbxNewCheckBox.isSelected());
			}
		});
		chckbxNewCheckBox.setBounds(374, 214, 64, 23);
		contentPane.add(chckbxNewCheckBox);

		enabled = new JCheckBox("Enabled");
		enabled.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

			}
		});
		enabled.setBounds(451, 214, 72, 23);
		contentPane.add(enabled);
		setVisible(true);
	}

	public void readLog() {
		/*
		 * Scanner s = null; try { s = new Scanner(new
		 * File(signlink.findcachedir()+ "ConsoleLogger.txt")); } catch
		 * (FileNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } while(s.hasNextLine()) {
		 * if(s.nextLine().contains(".java:")) Logger.addLog("ERROR",
		 * s.nextLine(), Color.RED, Color.ORANGE); else { if(s.hasNextLine() &&
		 * s.nextLine() != null && s.hasNext()) Logger.addLog("INFO",
		 * s.nextLine(), Color.CYAN, Color.MAGENTA); } }
		 */
	}
}
