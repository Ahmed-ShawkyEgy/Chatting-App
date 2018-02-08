import java.io.*; 
import java.net.*; 

public class Server{ 

  public static void main(String argv[]) throws Exception 
    {  

      ServerSocket welcomeSocket = new ServerSocket(6789); 
      
		
		while(true)
		{			
			try{
			Socket connectionSocket = welcomeSocket.accept(); 
			
			Connection c = new Connection(connectionSocket);
			c.start();
			
			}catch(Exception e)
			{
				System.err.println(e.getMessage());
				break;
			}
			
		}
		welcomeSocket.close();
    } 
} 

class Connection extends Thread
{
	private String clientSentence; 
	private String capitalizedSentence; 
	private BufferedReader inFromClient ; 
	private DataOutputStream  outToClient;
	private Socket connectionSocket;
	
	public Connection(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}
	
	public void run()
	{
		try{
		inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
		
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		
		while(true)
		{			
			
			clientSentence = inFromClient.readLine(); 
			if(clientSentence==null)break;
			
			capitalizedSentence = clientSentence.toUpperCase() + '\n'; 
			
			if(capitalizedSentence.equals("QUIT\n"))break;
			
			outToClient.writeBytes(capitalizedSentence); 
			
		}
		inFromClient.close();
		outToClient.close();
		connectionSocket.close();
		
		}catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}
