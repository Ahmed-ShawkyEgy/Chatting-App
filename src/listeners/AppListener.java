package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.MainController;

public class AppListener implements ActionListener{

	MainController controller;
	
	public AppListener(MainController controller) {
		this.controller = controller;
		System.out.println("Listener Constructor");
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if(cmd.equals("refresh"))
		{
			try{
			controller.populate();
			}catch(Throwable e1)
			{
				e1.printStackTrace();
			}
		}
		else if(cmd.length()>4 && cmd.substring(0, 4).equals("chat"))
		{
			String[] request = cmd.split(" ");
			if(request[3].matches("\\d+"))
			{
				controller.chat(request[1], request[2], Integer.parseInt(request[3]));
			}
		}
		
	}

}
