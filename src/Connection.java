import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

	
class Connection extends Thread
{
	private BufferedReader inFromClient ; 
	private DataOutputStream  outToClient;
	private Socket connectionSocket;
	
	private String user_name;
	private int index;
	private boolean isConnected;
	
	private Server my_server;
	
	ArrayList<Connection> member_list;
	HashMap<String, Integer>  name_to_index;
	StringBuilder allMembers;
	
	public Connection(Socket connectionSocket,int i,
			ArrayList<Connection> member_list,HashMap<String, Integer>map,
			StringBuilder allMembers , Server my_server) {
		this.connectionSocket = connectionSocket;
		index = i;
		user_name = null;
		isConnected = false;
		
		this.member_list = member_list;
		this.name_to_index = map;
		this.allMembers = allMembers;
		
		this.my_server = my_server;
		
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
				quit();
				break loop;

			case "GET-MEMBERS":
				getMembers();
				break;
				
			case "JOIN":
				joinServer();
				break;
				
			case "CHAT":
				chat();
				break;
				
			default:
				outToClient.writeBytes("Server-Echo: "+capitalizedSentence+"\n"); 
				break;
			}
			
		}
		terminate();
		
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void quit() throws IOException
	{
		outToClient.writeBytes("bye\n");
		inFromClient.readLine(); 
		isConnected = false;
	}
	
	private void getMembers() throws IOException
	{
		outToClient.writeBytes("Server: "+allMembers.toString()+"\n");
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
		if(!isConnected)
		{
			outToClient.writeBytes("Server: You must join the server first\n");
			return;
		}
		
		outToClient.writeBytes("Server: Enter username: \n"); 
		String reciever_name = inFromClient.readLine();

		outToClient.writeBytes("Server: Enter Message: \n"); 
		String message = inFromClient.readLine();
		
		outToClient.writeBytes("Server: Enter TTL: \n"); 
		
		int TTL ;
		while(true)
		{
			try{
				TTL = Integer.parseInt(inFromClient.readLine());	
				break;
			}catch(Exception e)
			{
				System.out.println("Please enter a valid number");
			}
		}
		
		if(my_server.sendTo(reciever_name, message, TTL))
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
	
	private void terminate() throws IOException {
		inFromClient.close();
		outToClient.close();
		connectionSocket.close();
	}

}
