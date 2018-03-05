package controller;

import java.awt.event.ActionListener;

import javax.swing.JFrame;

import model.Client;
import model.Main;
import view.MenuView;
import view.Name;

public class MainController {

	JFrame frame;
	Client client;
	ActionListener menu_listener;
	
	public MainController() 
	{
		menu_listener = new MenuListener(this);
		frame = new MenuView(menu_listener);
		
	}
	
	protected void connect(int port) 
	{
		client = new Client(port);
		frame.dispose();
		frame = new Name(menu_listener);
	}
	
	protected void join(String name)
	{
		if(!name.isEmpty())
		{
			try{
			client.join(name);
			}catch(Exception e )
			{
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) {
		Main server = new Main();
		MainController controller = new MainController();
	}

}
