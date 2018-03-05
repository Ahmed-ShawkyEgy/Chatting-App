package controller;

import java.awt.event.ActionListener;

import javax.swing.JFrame;

import listeners.AppListener;
import listeners.MenuListener;
import model.Client;
import model.Main;
import view.AppView;
import view.MenuView;
import view.Name;

public class MainController {

	JFrame frame;
	Client client;
	ActionListener menu_listener,app_listener;
	
	public MainController() 
	{
		menu_listener = new MenuListener(this);
		frame = new MenuView(menu_listener);
		
	}
	
	public void connect(int port) 
	{
		client = new Client(port);
		frame.dispose();
		frame = new Name(menu_listener);
	}
	
	public void join(String name)
	{
		name = name.trim();
		if(!name.isEmpty())
		{
			try{
			client.join(name);
			frame.dispose();
			app_listener = new AppListener(this);
			frame = new AppView(app_listener,name);
			System.out.println("Joined Successfully: "+name);
			}catch(Exception e )
			{
				System.out.println("Failed to join");
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("Empty String");
		}
	}

	public void chat(String name,String message,int TTL) {
		if(name!=null)
		{
			
		}
		System.out.println("Chat with :"+name+message+TTL);
	}
	
	public static void main(String[] args) {
		Main server = new Main();
		MainController controller = new MainController();
	}

}
