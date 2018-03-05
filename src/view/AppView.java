package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppView extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel list , text_area, bottom , top;

	public AppView(ActionListener listener) 
	{

		super("Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(list,BorderLayout.WEST);
		add(text_area,BorderLayout.EAST);
		add(bottom,BorderLayout.SOUTH);
		add(top,BorderLayout.NORTH);
		
		setSize(520,520);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		setVisible(true);
		
	}
	
	
	
	
}
