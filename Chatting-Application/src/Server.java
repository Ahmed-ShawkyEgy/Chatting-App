import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server{ 
	
	static ArrayList<Connection> member_list;
	static HashMap<String, Integer>  name_to_index;
	static StringBuilder allMembers;
	
	public static void main(String argv[]) throws Exception 
	{  
	
		ServerSocket welcomeSocket = new ServerSocket(6789); 
	    member_list = new ArrayList<Connection>();
		name_to_index = new HashMap<String, Integer>();
		allMembers = new StringBuilder();
		int index = 0;
		
		while(true)
		{			
			try{
				Socket connectionSocket = welcomeSocket.accept(); 
				
				Connection c = new Connection(connectionSocket,index++);
				
				member_list.add(c);
				
				c.start();
				
				}catch(Exception e)
				{
					System.err.println(e.getMessage());
					break;
				}
				
			}
			welcomeSocket.close();
	    } 
	
static class Connection extends Thread
{
	private BufferedReader inFromClient ; 
	private DataOutputStream  outToClient;
	private Socket connectionSocket;
	
	public String user_name;
	public int index;
	public boolean isConnected;
	
	public Connection(Socket connectionSocket,int i) {
		this.connectionSocket = connectionSocket;
		index = i;
		user_name = null;
		isConnected = false;
	}
	
	
	
	public void run()
	{
		String clientSentence , capitalizedSentence;
		
		try{
		inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
		
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		
		loop: while(true)
		{			
			
			clientSentence = inFromClient.readLine(); 
			capitalizedSentence = clientSentence.toUpperCase(); 
			
			switch (capitalizedSentence)
			{
			case "QUIT":
				outToClient.writeBytes("bye\n");
				inFromClient.readLine(); 
				isConnected = false;
				break loop;

			case "GET-MEMBERS":
				outToClient.writeBytes("Server: "+allMembers.toString()+"\n");
				break;
				
			case "JOIN":
				if(isConnected)
					outToClient.writeBytes("Server: You are already connected with the name "+user_name+"\n");
				else
					joinServer();
				break;
				
			case "CHAT":
				if(!isConnected)
					outToClient.writeBytes("Server: You must join the server first\n");
				else
					chat();
				break;
				
			default:
				outToClient.writeBytes("Server-Echo: "+capitalizedSentence+"\n"); 
				break;
			}
			
		}
		inFromClient.close();
		outToClient.close();
		connectionSocket.close();
		
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private void joinServer() throws IOException
	{
		if(isConnected)
		{
			outToClient.writeBytes("Server: Already connected by the name "+user_name+"\n"); 
			return;
		}
		else
		{
			outToClient.writeBytes("Server: Enter username:\n"); 
			String in= inFromClient.readLine();
			if(name_to_index.containsKey(in) && member_list.get(name_to_index.get(in)).isConnected)
			{
				outToClient.writeBytes("Server: This name is Already taken :(\n"); 
				return;
			}
			user_name = in;
			allMembers.append(user_name+" ");
			isConnected = true;
			
			name_to_index.put(user_name, this.index);
			
			outToClient.writeBytes("Server: Welcome "+user_name+"\n"); 
		}
	}
	
	private void chat() throws IOException
	{
		outToClient.writeBytes("Server: Enter username: \n"); 
		String reciever_name = inFromClient.readLine();
		
		if(!name_to_index.containsKey(reciever_name))
		{
			outToClient.writeBytes("Server: Invalid username\n"); 
			return;
		}
		
		outToClient.writeBytes("Server: Enter Message: \n"); 
		String message = inFromClient.readLine();
		
		sendTo(reciever_name, message);
	}
	
	private void sendTo(String reciever_name,String message) throws IOException
	{
		int idx = name_to_index.get(reciever_name);
		boolean recieved = member_list.get(idx).recieve(user_name+": "+message);
		if(recieved)
			outToClient.writeBytes("Server: Message sent\n"); 
		else
		outToClient.writeBytes("Server: Failed Sending the message. This client is probably offline\n"); 
	}
	
	public boolean recieve(String message)
	{
		if(!isConnected)return false;
		try {
			outToClient.writeBytes(message+"\n");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}
}
}
