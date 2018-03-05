package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MenuView extends JFrame {

	private static final long serialVersionUID = 1L;

	
	public MenuView(ActionListener listener){
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		content = frame.getContentPane();
//		this.listener = listener;
		
		super("Connect");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel panel = new JPanel();
		
		JButton button1 = new JButton("Server 1");
		JButton button2 = new JButton("Server 2");
		
		
		button1.addActionListener(listener);
		button2.addActionListener(listener);
		
		button1.setActionCommand("s1");
		button2.setActionCommand("s2");
		
		int side = 200;
		button1.setPreferredSize(new Dimension(side,side));
		button2.setPreferredSize(new Dimension(side,side));
		
		panel.add(button1);
		panel.add(button2);
		
		
		add(panel);
		
		setSize(520,270);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		setVisible(true);
		
		
	}	

}
