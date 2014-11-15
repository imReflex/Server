

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.InputMismatchException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

public class Conditions extends JFrame implements ActionListener {

	static final long serialVersionUID = 1L;

	private ArrayList<Rule> rules;

	private ArrayList<Rule> setRules() {
		rules = new ArrayList<Rule>();

		rules.add(new Rule("Rule 1: Offensive Language/Excessive Profanities.", "You are not permitted to use offensive, obscene, racist, foul, stereotypical, abusive, or derogatory language."));
		rules.add(new Rule("Rule 2: Flaming.", "You are not allowed to say things directed towards other members that are meant to offend, hurt, or aggravate them."));
		rules.add(new Rule("Rule 3: Trolling/Aggravation.", "You are not permitted to make remarks that are meant to provoke a negative and/or emotional response from other members"));
		rules.add(new Rule("Rule 4: Respect Staff & Members.", "Respect Staff & Members "));
		rules.add(new Rule("Rule 5: Spamming.", "Spamming is not allowed."));
		rules.add(new Rule("Rule 6:", null));
		rules.add(new Rule("Rule 7", null));
		rules.add(new Rule("Rule 8:", null));
		rules.add(new Rule("Rule 9:", null));
		rules.add(new Rule("Rule 10:", null));
		rules.add(new Rule("Rule 12:", null));
		rules.add(new Rule("Rule 13:", null));
		rules.add(new Rule("Rule 14:", null));
		rules.add(new Rule("Rule 15:", null));
		rules.add(new Rule("Rule 16:", null));
		rules.add(new Rule("Rule 17:", null));
		rules.add(new Rule("Rule 18:", null));

		return rules;
	}

	public Conditions() {
		super("Terms and Conditions");

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setResizable(false);

		setContentPane(new MainPanel());

		setLayout(new BorderLayout());

		setUndecorated(true);
		setAlwaysOnTop(true);

		// AWTUtilities.setWindowShape(this, new RoundRectangle2D.Float(0, 0,
		// getWidth(), getHeight(), 13, 13));
		RulesHolder rulesHolder = new RulesHolder(setRules());

		JScrollPane scrollPane = new JScrollPane(rulesHolder, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setPreferredSize(new Dimension(400, 200));
		add(scrollPane, BorderLayout.NORTH);

		JLabel message = new JLabel("You must agree with all rules to continue.");
		message.setPreferredSize(new Dimension(400, 15));
		message.setFont(message.getFont().deriveFont(1));
		message.setForeground(Color.red);
		add(message, BorderLayout.CENTER);

		JPanel buttonHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonHolder.setOpaque(false);
		buttonHolder.setPreferredSize(new Dimension(400, 30));
		CustomButton button = new CustomButton("Accept", new Color(0, 120, 255, 0), Color.white);
		button.addActionListener(this);
		buttonHolder.add(button);
		button = new CustomButton("Decline", new Color(0, 120, 255, 0), Color.white);
		button.setToolTipText("You can not play without accept the rules.");
		button.addActionListener(this);
		buttonHolder.add(button);
		add(buttonHolder, BorderLayout.SOUTH);
		setMaximumSize(new Dimension(400, 300));
		pack();
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				setSystemTheme();
				configureToolTip();
				new Conditions().setVisible(true);
			}
		});
	}

	private static void configureToolTip() {
		UIManager.put("ToolTip.foreground", new ColorUIResource(0xffffff));
		UIManager.put("ToolTip.background", new ColorUIResource(0x262626));
		Border border = BorderFactory.createLineBorder(Color.white);
		UIManager.put("ToolTip.border", border);
		ToolTipManager.sharedInstance().setInitialDelay(0);
	}

	private static void setSystemTheme() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (actionCommand.equals("Accept")) {
			startClient();
		} else if (actionCommand.equals("Decline")) {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}

	private void startClient() {

	}
}

class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public MainPanel() {
		setBorder(new EmptyBorder(2, 2, 2, 2));
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.white);
		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint gp = new GradientPaint(0, 0, new Color(0x262626), getWidth() / 2, getHeight() / 2, new Color(0x333333), true);
		g2.setPaint(gp);
		g2.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setPaint(null);
		g2.setColor(Color.black);
		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		g2.setColor(Color.white);
		g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
	}
}

class RulesHolder extends JPanel {

	private static final long serialVersionUID = 1L;

	private ArrayList<Rule> rules;

	public RulesHolder(ArrayList<Rule> rules) {
		if (!(rules instanceof ArrayList) || rules.size() == 0)
			throw new InputMismatchException("There's no rules to be displayed");
		setOpaque(false);
		setLayout(new GridLayout(rules.size(), 1));
		setSize(new Dimension(390, rules.size() * 15));
		setBorder(new EmptyBorder(10, 3, 10, 3));
		this.rules = rules;
		displayRules();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(255, 255, 255, 30));
		g.fillRoundRect(5, 5, getWidth() - 11, getHeight() - 11, 8, 8);
	}

	private void displayRules() {
		for (Rule rule : rules) {
			add(new RuleDisplay(rule));
		}
	}
}

class RuleDisplay extends JPanel implements MouseListener {

	private static final long serialVersionUID = 1L;

	private Rule rule;

	public RuleDisplay(Rule rule) {
		if (!(rule instanceof Rule)) {
			throw new InputMismatchException("Invalid Rule");
		}
		addMouseListener(this);

		setOpaque(false);

		setPreferredSize(new Dimension(400, 15));

		if (rule.getTip() instanceof String)
			setToolTipText(rule.getTip());
		this.rule = rule;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.black);
		g2.drawString(rule.getSentence(), 11, (getHeight() / 2) + 5);
		g2.setColor(Color.white);
		g2.drawString(rule.getSentence(), 10, (getHeight() / 2) + 4);

		if (isHovered) {
			g2.setColor(new Color(0, 0, 0, 50));
			g2.fillRect(5, 0, getWidth() - 20, getHeight());
		}
	}

	private boolean isHovered;

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		isHovered = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		isHovered = false;
		repaint();
	}
}

class CustomButton extends JButton {

	private static final long serialVersionUID = 1L;

	public CustomButton(String buttonText, Color background, Color foreground) {
		setText(buttonText);
		setBackground(background);
		setForeground(foreground);
		/** Remove Button UI background **/
		setContentAreaFilled(false);
		/** Remove Button Border **/
		setBorder(null);
		/** Remove the Line around the button when it's focused **/
		setFocusPainted(false);
		/** Set button size based on text size **/
		setPreferredSize(new Dimension(getTextWidth() + 12, getTextHeight() + 6));
	}

	private static Color changeAlpha(Color src, int alpha) {
		return new Color(src.getRed(), src.getGreen(), src.getBlue(), alpha);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		GradientPaint paint;

		if (getModel().isRollover()) {
			paint = new GradientPaint(getWidth() / 2, 0, changeAlpha(getBackground(), 30), getWidth() / 2, getHeight(), changeAlpha(getBackground(), 180));
		} else {
			paint = new GradientPaint(getWidth() / 2, 0, changeAlpha(getBackground(), 0), getWidth() / 2, getHeight(), changeAlpha(getBackground(), 120));
		}
		g2.setPaint(paint);
		g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 5, 5);
	}

	private int getTextWidth() {
		return getFontMetrics(getFont()).stringWidth(getText());
	}

	private int getTextHeight() {
		return getFontMetrics(getFont()).getHeight();
	}
}

class Rule {

	private String sentence;
	private String tip;

	public Rule(String sentence, String tip) {
		setSentence(sentence);
		setTip(tip);
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
}