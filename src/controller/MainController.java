package controller;

import javax.swing.JFrame;

import model.Client;
import view.MenuView;

public class MainController {

	JFrame frame;
	Client client;
	
	public MainController() 
	{
		MenuListener menu_listener = new MenuListener(this);
		frame = new MenuView("Connect", menu_listener);
	}
	
	protected void connect(int port) 
	{
		client = new Client(port);
	}
	
	public static void main(String[] args) {
		
		
	}

}
