package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.MainController;

public class MenuListener implements ActionListener{

	MainController controller;
	
	public MenuListener(MainController controller)
	{
		this.controller = controller;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String command = e.getActionCommand();
		if(command.equals("s1"))
		{
			controller.connect(2000);
		}
		else if(command.equals("s2"))
		{
			controller.connect(3000);
		}
		else
		{
			controller.join(command);
		}
	}

}
