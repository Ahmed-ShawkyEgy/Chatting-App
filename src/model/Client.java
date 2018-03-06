package model;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import controller.MainController;

public class Client { 
	
	private int port;
	
	DataOutputStream outToServer;
	Socket clientSocket;
	BufferedReader inFromServer; 
	Reciever reciever;
	MainController controller;
	String my_name;
	boolean joined = false;
	
	public Client(int port,MainController controller) {
		this.port = port;
		this.controller = controller;
		try {
			connect();
		} catch (UnknownHostException e) {
			System.out.println(e.getMessage() + " "+port);
		} catch (IOException e) {
			System.out.println(e.getMessage() + " "+port);
		}
	}
	
	public void connect() throws UnknownHostException, IOException
	{
		clientSocket = new Socket("localhost",port);
    	System.out.println("Connection established Successfully!");
    	
        reciever = new Reciever(clientSocket,controller);
        
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("CLIENT\n");
        
        inFromServer = 
        		new BufferedReader(new
        				InputStreamReader(clientSocket.getInputStream())); 
	}
	
	public boolean join(String name) throws Exception
	{
		outToServer.writeBytes("JOIN\n");
		outToServer.writeBytes(name+"\n");
		String s = inFromServer.readLine();
		if(s.equals("false"))
			return false;
		reciever.start();
//		inFromServer.close();
		my_name = name;
		joined = true;
		return true;
	}
	
	public void send(String name,String message,int TTL)throws Throwable
	{
		outToServer.writeBytes("CHAT\n");
		outToServer.writeBytes(name+"\n");
		outToServer.writeBytes(my_name+": "+message+"\n");
		outToServer.writeBytes(TTL+"\n");
		
	}
	
	public void get_members() throws Throwable
	{
		outToServer.writeBytes("GET-MEMBERS\n");
	}
	
	public void quit() throws Exception
	{
		outToServer.writeBytes("QUIT\n");
		clientSocket.close();
	}
    
    class Reciever extends Thread{
    	Socket clientSocket;
    	MainController controller;
    	public Reciever(Socket s,MainController controller) {
    		clientSocket = s;
    		this.controller = controller;
		}
    	
    	public void run()
    	{
    		try
    		{
    			BufferedReader inFromServer = 
    	        		new BufferedReader(new
    	        				InputStreamReader(clientSocket.getInputStream())); 
    			
    			DataOutputStream outToServer = 
    	  				new DataOutputStream(clientSocket.getOutputStream());
    			
    			while(true)
    			{

	    			String recievedSentence = inFromServer.readLine();
	    			if(!joined)
		    			continue;
	    			if(recievedSentence.equals("@members"))
	    			{
	    				String members = "";
	    				while(inFromServer.ready())
	    					members += inFromServer.readLine();
	    				controller.populate(members.split(" "));
	    				continue;
	    			}
	    			else if(recievedSentence.equals("send-fail"))
	    			{
	    				controller.print("Server: Failed to send the message\n"
	    						+ "either the user is offline or the"
	    						+ "TTL was too short");
	    				continue;
	    			}
	    			else if(recievedSentence.equals("bye"))
	    				break;
	    			
	    			System.out.println(recievedSentence); 
	    			controller.print(recievedSentence);
    			}
    			
    			outToServer.writeBytes("Terminate Connection\n");
    			inFromServer.close();
    			outToServer.close();
    			clientSocket.close();
    			System.out.println("Connection closed");
    			controller.print("Connection closed");
    		}catch(Exception e)
    		{
    			System.out.println(e.getMessage());
    		}
    	}
    	
    }

} 
