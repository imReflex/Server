import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ImageLink implements ActionListener {

	private JFrame frame;
	private String link;
	private JTextField txtDirectLink;
	private JButton btnCopyDirectLink, btnCopyFourmLink;
	private JTextField txtForumLink;

	public ImageLink(String link) {
		this.link = link;
		openFrame();
	}

	public void openFrame() {
		frame = new JFrame("Link for your image.");
		frame.setBounds(250, 250, 388, 223);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(2);

		JPanel contentPane = new JPanel();
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, 382, 195);
		contentPane.add(panel);

		JLabel lblDirectLink = new JLabel("Direct Link:");
		lblDirectLink.setBounds(10, 11, 362, 14);
		panel.add(lblDirectLink);

		txtDirectLink = new JTextField();
		txtDirectLink.setEditable(false);
		txtDirectLink.setBounds(10, 36, 263, 20);
		panel.add(txtDirectLink);
		txtDirectLink.setColumns(10);
		txtDirectLink.setText(link);

		btnCopyDirectLink = new JButton("Copy Link");
		btnCopyDirectLink.setBounds(283, 35, 89, 23);
		panel.add(btnCopyDirectLink);
		btnCopyDirectLink.addActionListener(this);

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(283, 165, 89, 23);
		panel.add(btnClose);

		btnCopyFourmLink = new JButton("Copy Link");
		btnCopyFourmLink.setBounds(283, 113, 89, 23);
		panel.add(btnCopyFourmLink);
		btnCopyFourmLink.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String link = txtForumLink.getText();
				StringSelection linkSelection = new StringSelection(link);

				Clipboard clipbrd = Toolkit.getDefaultToolkit().getSystemClipboard();

				clipbrd.setContents(linkSelection, linkSelection);
			}

		});

		txtForumLink = new JTextField();
		txtForumLink.setEditable(false);
		txtForumLink.setColumns(10);
		txtForumLink.setBounds(10, 114, 263, 20);
		txtForumLink.setText("[IMG]"+link+"[/IMG]");
		panel.add(txtForumLink);

		JLabel lblForumLink = new JLabel("Forum Link:");
		lblForumLink.setBounds(10, 88, 362, 14);
		panel.add(lblForumLink);
		btnClose.addActionListener(this);

		frame.setVisible(true);
	}

	@Override	
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand().toLowerCase();
		if(action.equals("copy link")) {
			String link = txtDirectLink.getText();
			StringSelection linkSelection = new StringSelection(link);

			Clipboard clipbrd = Toolkit.getDefaultToolkit().getSystemClipboard();

			clipbrd.setContents(linkSelection, linkSelection);
		}
		if(action.equals("close"))
			frame.dispose();
		
	}
	
}
