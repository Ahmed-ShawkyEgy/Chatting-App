import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client { 

    public static void main(String argv[]) throws Exception 
    { 
    	
//        Socket clientSocket = new Socket("192.168.1.100", 6789); 
    	Socket clientSocket;
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("Connect to a server");
    	System.out.println("Enter the port number:");
    	while(true)
    	{
    		try{
    			int port = Integer.parseInt(br.readLine());
    			clientSocket = new Socket("localhost",port);
    			break;
    		}catch(Exception e)
    		{
    			System.out.println(e.getMessage());
    			System.out.println("Please enter a valid port number");
    		}
    	}
    	System.out.println("Connection established Successfully!");
        
        Sender sender = new Sender(clientSocket);
        Reciever reciever = new Reciever(clientSocket);
        
        sender.start();
        reciever.start();
        
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
