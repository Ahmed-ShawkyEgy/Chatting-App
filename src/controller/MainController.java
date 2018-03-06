package controller;

import java.awt.event.ActionListener;

import javax.swing.JFrame;

import listeners.AppListener;
import listeners.MenuListener;
import model.Client;
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
		client = new Client(port,this);
		frame.dispose();
		frame = new Name(menu_listener);
	}
	
	public void join(String name)
	{
		name = name.trim();
		if(!name.isEmpty() && name.split(" ").length==1)
		{
			try{
			if(!client.join(name))
				return;
			frame.dispose();
			app_listener = new AppListener(this);
			frame = new AppView(app_listener,name);
			try{
				
			getMembers();
			}catch(Throwable e)
			{
				System.out.println(e.getMessage());
			}
			System.out.println("Joined Successfully: "+name);
			}catch(Exception e )
			{
				System.out.println("Failed to join");
				System.out.println(e.getMessage());
				System.out.println(e.getMessage());
			}
		}
		else
		{
			System.out.println("Usernames can't be empty nor have empty spaces");
		}
	}

	public void chat(String name,String message,String TTL) {
		if(name!=null)
		{
			try{
				client.send(name, message, Integer.parseInt(TTL));
			}
			catch(Throwable e)
			{
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void getMembers()
	{
		try {
			client.get_members();
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void populate(String[] member)
	{
		try{
		((AppView)frame).clearMembers();
		for (int i = 0; i < member.length; i++) {
			((AppView)frame).addMember(member[i]);
		}
		print("Refreshed!");
		}
		catch(Throwable E)
		{
			System.out.println(E.getMessage());
		}
		
	}
	
	public void print(String s)
	{
		((AppView)frame).print(s);
	}
	
	public void quit()
	{
		try{
		client.quit();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainController controller = new MainController();
		
	}

}
