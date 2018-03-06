package model;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client { 
	
	private int port;
	
	DataOutputStream outToServer;
	Socket clientSocket;
	BufferedReader inFromServer; 
	
	public Client(int port) {
		this.port = port;
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
//        Reciever reciever = new Reciever(clientSocket);
//        
//        sender.start();
//        reciever.start();
        
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes("CLIENT\n");
        
        inFromServer = 
        		new BufferedReader(new
        				InputStreamReader(clientSocket.getInputStream())); 
	}
	
	public String join(String name) throws Exception
	{
		outToServer.writeBytes("JOIN\n");
		System.out.println(inFromServer.readLine() + " Should enter: " + name);
		outToServer.writeBytes(name+"\n");
		return inFromServer.readLine();
	}
	
	public void send(String name,String message,int TTL)
	{
		
	}
	
	public String get_members() throws Throwable
	{
		String members = "";
		System.out.println("Get memebers");
		outToServer.writeBytes("GET-MEMBERS\n");
//		Thread.sleep(1);
		while(inFromServer.ready())
			members += inFromServer.readLine();
		return members;
	}
	
	public void quit() throws Exception
	{
		outToServer.writeBytes("QUIT\n");
		clientSocket.close();
	}
    
    static class Reciever extends Thread{
    	Socket clientSocket;
    	public Reciever(Socket s) {
    		clientSocket = s;
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
	    			if(recievedSentence.equals("bye"))
	    				break;
	    			System.out.println(recievedSentence); 
    			}
    			
    			outToServer.writeBytes("Terminate Connection\n");
    			inFromServer.close();
    			outToServer.close();
    			clientSocket.close();
    			System.out.println("Connection closed");
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
