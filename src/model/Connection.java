package model;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

	
class Connection extends Thread
{
	private BufferedReader inFromClient ; 
	private DataOutputStream  outToClient;
	private Socket connectionSocket;
	
	private String user_name;
	private int index;
	private boolean isConnected , isClient;
	
	private Server my_server;
	
	public Connection(Socket connectionSocket,int i,Server my_server) {
		this.connectionSocket = connectionSocket;
		index = i;
		user_name = null;
		isConnected = false;
		
		this.my_server = my_server;
		
	}
	
	
	
	public void run()
	{
		String clientSentence , capitalizedSentence;
		
		try{
		inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); 
		
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		
		if(inFromClient.readLine().equals("CLIENT"))
			isClient = true;
		else
			isClient = false;
		
		loop: while(true)
		{			
			
			clientSentence = inFromClient.readLine(); 
			capitalizedSentence = clientSentence.toUpperCase(); 
			
			if(isClient)
			{
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
			else
			{
				switch (capitalizedSentence)
				{
					case "MEMBERS":
						String members = my_server.myMembers();
						outToClient.writeBytes(members+"\n");
						break;
					case "EXIST":
						String name = inFromClient.readLine();
						outToClient.writeBytes(my_server.exists(name)+"\n");
						break;
					case "CHAT":
						name = inFromClient.readLine();
						String message = inFromClient.readLine();
						int TTL = Integer.parseInt(inFromClient.readLine());
						outToClient.writeBytes(my_server.sendTo(name, message, TTL)+"\n");
						break;
					default:
						outToClient.writeBytes("ERROR: "+capitalizedSentence);
					
				}
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
		outToClient.writeBytes("Server: member list:\n"+my_server.getMembers());
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
			String in= inFromClient.readLine().trim();
			if(in.isEmpty())
			{
				outToClient.writeBytes("Server: Can't take empty name :(\n"); 
				return;
			}
			if(!my_server.add(in, index))
			{
				outToClient.writeBytes("Server: This name is Already taken :(\n"); 
				return;
			}
			user_name = in;
			isConnected = true;
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
				outToClient.writeBytes("Please enter a valid number\n");
			}
		}
		
		if(my_server.sendTo(reciever_name, message, TTL-1))
			outToClient.writeBytes("Server: Message sent\n"); 
		else
			outToClient.writeBytes("Server: Failed Sending the message."
					+ "Either this client is offline"
					+ " or the TTL you inserted wasn't long enough\n"); 
	}
	
	public boolean recieve(String message,int TTL)
	{
		if(TTL<0)return false;
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
	


	public String getUser_name() {
		return user_name;
	}

	
}
