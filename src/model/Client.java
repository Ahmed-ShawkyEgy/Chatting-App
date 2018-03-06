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
        
//        Sender sender = new Sender(clientSocket);
        reciever = new Reciever(clientSocket,controller);
//        
//        sender.start();
//        reciever.start();
        
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("CLIENT\n");
        
        inFromServer = 
        		new BufferedReader(new
        				InputStreamReader(clientSocket.getInputStream())); 
	}
	
	public void join(String name) throws Exception
	{
		outToServer.writeBytes("JOIN\n");
		inFromServer.readLine();
		outToServer.writeBytes(name+"\n");
		reciever.start();
	}
	
	public void send(String name,String message,int TTL)throws Throwable
	{
		outToServer.writeBytes("CHAT\n");
		outToServer.writeBytes(name+"\n");
		outToServer.writeBytes(TTL+"\n");
		
	}
	
	public void get_members() throws Throwable
	{
		String members = "";
		System.out.println("Get memebers");
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
	    			if(recievedSentence.equals("@members"))
	    			{
	    				String members = "";
	    				while(inFromServer.ready())
	    					members += inFromServer.readLine();
	    				controller.populate(members.split(" "));
	    			}
	    			if(recievedSentence.equals("bye"))
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

    static class Sender extends Thread
    {
    	Socket clientSocket;
    	
    	public Sender(Socket s) {
		clientSocket = s;
		}
    	
    	public void run() {
    		try{
    			
    			BufferedReader inFromUser = 
  		          new BufferedReader(new InputStreamReader(System.in)); 
    			
		  		DataOutputStream outToServer = 
	  				new DataOutputStream(clientSocket.getOutputStream());
		  		
		  		String sentence;
		  		
		  		outToServer.writeBytes("CLIENT\n");
		  		
		  		while(true)
		  		{
		  			if(!inFromUser.ready())
		  			{
		  				Thread.sleep(100);
		  			}
			  		sentence = inFromUser.readLine();
					
		            outToServer.writeBytes(sentence + '\n'); 
		            if(sentence.toLowerCase().equals("quit"))
		            	break;
			  		
		  		}
		  		inFromUser.close();
    		}catch(Exception e)
    		{
    			System.out.println(e.getMessage());
    		}
    	
    	}
    }
} 
