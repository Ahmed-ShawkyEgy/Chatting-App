package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.MainController;

public class AppListener implements ActionListener{

	MainController controller;
	
	public AppListener(MainController controller) {
		this.controller = controller;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if(cmd.equals("refresh"))
		{
			try{
			controller.getMembers();
			}catch(Throwable e1)
			{
				e1.printStackTrace();
			}
		}
		else
		{
			String[] request = cmd.split("\\|");
			controller.chat(request[0], request[1],request[2]);
		}
		
	}

}
