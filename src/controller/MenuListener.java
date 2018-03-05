package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuListener implements ActionListener{

	MainController controller;
	
	public MenuListener(MainController controller)
	{
		this.controller = controller;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("s1"))
		{
			controller.connect(2000);
		}
		else
		{
			controller.connect(3000);
		}
	}

}
