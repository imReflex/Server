import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class ExperienceCalculator implements ActionListener {

	private JTextField txtTargetXp, txtXp;
	private JLabel lblRemainder;
	private JProgressBar progressBar;
	private JFrame Frame;
	private NumberFormat format = NumberFormat.getInstance(Locale.CANADA);

	public ExperienceCalculator() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		Frame = new JFrame("Experience Calculator");
		Frame.setDefaultCloseOperation(2);
		Frame.getContentPane().setLayout(new BorderLayout());
		Frame.setBounds(100, 100, 337, 322);
		Frame.setResizable(false);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		Frame.getContentPane().add(panel, BorderLayout.CENTER);

		JLabel lblEnterTargetXp = new JLabel("Enter the target XP:");
		lblEnterTargetXp.setBounds(10, 78, 301, 14);
		panel.add(lblEnterTargetXp);

		txtTargetXp = new JTextField();
		txtTargetXp.setColumns(10);
		txtTargetXp.setBounds(10, 103, 301, 20);
		panel.add(txtTargetXp);
		txtTargetXp.addActionListener(this);

		JLabel lblEnterXp = new JLabel("Enter your current XP:");
		lblEnterXp.setBounds(10, 11, 301, 14);
		panel.add(lblEnterXp);

		txtXp = new JTextField();
		txtXp.setColumns(10);
		txtXp.setBounds(10, 36, 301, 20);
		panel.add(txtXp);
		txtXp.addActionListener(this);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 198, 301, 20);
		panel.add(progressBar);

		lblRemainder = new JLabel("XP Left:");
		lblRemainder.setBounds(10, 152, 301, 20);
		panel.add(lblRemainder);

		JButton btnCalculate = new JButton("Calculate");
		btnCalculate.setBounds(10, 251, 135, 23);
		panel.add(btnCalculate);
		btnCalculate.addActionListener(this);

		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(176, 251, 135, 23);
		panel.add(btnClear);
		btnClear.addActionListener(this);

		Frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand().toLowerCase();
		if(action.equalsIgnoreCase("calculate")) {
			if (txtXp.getText() != null && txtTargetXp.getText() != null) {
				if (Double.valueOf(txtXp.getText()) > 200000000 || Double.valueOf(txtTargetXp.getText()) > 200000000) {
					JOptionPane.showMessageDialog(Frame, "You can't have over 200,000,000 experience", "To much experience!", 0);
				} else if (Double.valueOf(txtXp.getText()) > Double.valueOf(txtTargetXp.getText())) {
					JOptionPane.showMessageDialog(Frame, "You can't have more experience than your target experience.", "", 0);
				} else {
					double currentXp = Double.valueOf(txtXp.getText());
					double targetXp = Double.valueOf(txtTargetXp.getText());

					double remainder = (targetXp - currentXp);
					double percent = ((currentXp / targetXp) * 100);

					lblRemainder.setText("XP Left: "+format.format(remainder));
					progressBar.setValue((int)percent);
				}
			}
		}
		if(action.equals("clear")) {
			txtXp.setText(null);
			txtTargetXp.setText(null);
			lblRemainder.setText("XP Left: ");
			progressBar.setValue(0);
		}
	}
}