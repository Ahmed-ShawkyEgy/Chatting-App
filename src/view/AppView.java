package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AppView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel members , text_area, bottom , top;
	private DefaultListModel<String>  list;

	public AppView(ActionListener listener,String name) 
	{

		super(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* INIT */
		members = new JPanel();
		text_area = new JPanel();
		bottom = new JPanel();
		top = new JPanel();
		
		/* TOP */
		list = new DefaultListModel<String>();
		JList<String> jlist = new JList<String>();
		
		members.add(jlist);
		
		/* Bottom */
		JLabel label = new JLabel("Message");
		JTextField text = new JTextField();
		JLabel label1 = new JLabel("TTL");
		JTextField text1 = new JTextField();
		JButton b = new JButton("Send");
		
		text.setPreferredSize(new Dimension(300,50));
		text1.setPreferredSize(new Dimension(75,50));
		
		b.setActionCommand(text.getText());
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String command = "chat "+jlist.getSelectedValue()+" "+text.getText().trim() + " " + text1.getText();
				listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,command));
			}
		});
		
		b.setPreferredSize(new Dimension(140,50));
		
		bottom.add(label);
		bottom.add(text);
		bottom.add(label1);
		bottom.add(text1);
		bottom.add(b);
		
		
		
		add(members,BorderLayout.WEST);
		add(text_area,BorderLayout.EAST);
		add(bottom,BorderLayout.SOUTH);
		add(top,BorderLayout.NORTH);
		
		setSize(720,720);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		setVisible(true);
		
	}
	
	
	public void addMember(String name)
	{
		list.addElement(name);
	}
	
	public void removeMember(String name)
	{
		list.removeElement(name);
	}
	
}
