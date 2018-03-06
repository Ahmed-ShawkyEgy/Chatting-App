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
			System.out.println(e.getMessage());
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
		outToClient.writeBytes("@members\n"+my_server.getMembers());
	}
	
	private void joinServer() throws IOException
	{
		if(isConnected)
		{
			outToClient.writeBytes("false\n"); 
			return;
		}
		else
		{
			String in= inFromClient.readLine().trim();
			if(in.isEmpty())
			{
				outToClient.writeBytes("false\n"); 
				return;
			}
			if(!my_server.add(in, index))
			{
				outToClient.writeBytes("false\n"); 
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
		
		String reciever_name = inFromClient.readLine();

		String message = inFromClient.readLine();
		
		int TTL = Integer.parseInt(inFromClient.readLine());
		
		if(my_server.sendTo(reciever_name, message, TTL-1))
			outToClient.writeBytes("Server: Message sent\n"); 
		else
			outToClient.writeBytes("send-fail\n"); 
	}
	
	public boolean recieve(String message,int TTL)
	{
		if(TTL<0)return false;
		if(!isConnected)return false;
		try {
			outToClient.writeBytes(message+"\n");
		} catch (IOException e) {
			System.out.println(e.getMessage());
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
