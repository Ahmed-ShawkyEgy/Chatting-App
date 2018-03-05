package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Name extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public Name(ActionListener listener) {
		super("Name");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label = new JLabel("User name");
		JTextField text = new JTextField();
		JButton b = new JButton("Submit");
		
		text.setPreferredSize(new Dimension(300,50));
		
		b.setActionCommand(text.getText());
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,text.getText()));
			}
		});
		
		b.setPreferredSize(new Dimension(140,50));
		
		JPanel panel = new JPanel();
		panel.add(label);
		panel.add(text);
		panel.add(b);
		
		add(panel);
		
		setSize(600,130);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		setVisible(true);
	}

}
