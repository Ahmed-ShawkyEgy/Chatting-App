import java.io.*; 
import java.net.*; 

public class Server { 

  public static void main(String argv[]) throws Exception 
    { 
      String clientSentence; 
      String capitalizedSentence; 

      ServerSocket welcomeSocket = new ServerSocket(6789); 
      
      	Socket connectionSocket = welcomeSocket.accept(); 
      	
		BufferedReader inFromClient =new BufferedReader(new
				  InputStreamReader(connectionSocket.getInputStream())); 
		
		DataOutputStream  outToClient = 
	             new DataOutputStream(connectionSocket.getOutputStream()); 
		
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
		welcomeSocket.close();
		connectionSocket.close();
    } 
} 
 
