import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Thread{ 
	
	ArrayList<Connection> member_list;
	HashMap<String, Integer>  name_to_index;
	
	
	private DataOutputStream out_to_server;
	private BufferedReader in_from_server;
	
	public int port_number;
	
	public Server(int port_number)
	{
		this.port_number = port_number;
		
	}
	
	public void connect(int port_number){
		try {
			@SuppressWarnings("resource")
			Socket socket = new Socket("localhost",port_number);
			out_to_server = new DataOutputStream(socket.getOutputStream());
			out_to_server.writeBytes("SERVER\n");
			in_from_server = 
					new BufferedReader(new
							InputStreamReader(socket.getInputStream())); 
			
			System.out.println("Connected to other Server Successfully!");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	


	public void run()
	{
		try
		{
			ServerSocket welcomeSocket = new ServerSocket(port_number); 
		    member_list = new ArrayList<Connection>();
			name_to_index = new HashMap<String, Integer>();
			int index = 0;
			
			while(true)
			{			
				try{
					Socket connectionSocket = welcomeSocket.accept(); 
					
					Connection c = new Connection(connectionSocket,index++,this);
					
					member_list.add(c);
					
					c.start();
					
					}catch(Exception e)
					{
						System.err.println(e.getMessage());
						break;
					}
					
				}
				welcomeSocket.close();
		}catch(Exception e)
		{
			System.out.println();
		}
	}
	
	public boolean sendTo(String name,String message,int TTL)
	{
//		if(TTL<=0)
//			return false;
//		if(name_to_index.containsKey(name))
//		{
//			int index = name_to_index.get(name);
//			return member_list.get(index).recieve(message);
//		}
//		else if(other_server!=null)
//		{
//			return other_server.sendTo(name, message, TTL-1);
//		}
		return false;
	}
	
	public boolean exists(String name)
	{
		return name_to_index.containsKey(name);
	}
	
	public boolean isValidName(String name)
	{
		if(name_to_index.containsKey(name))
			return false;
		try{
		out_to_server.writeBytes("EXIST\n");
		out_to_server.writeBytes(name+"\n");
		String response = in_from_server.readLine();
		return response.equals("false");
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean add(String name,int index)
	{
		if(!isValidName(name))
			return false;
		name_to_index.put(name, index);
		return true;
	}
	
	public String getMembers()
	{
		String ret = myMembers();
		System.out.println("My members: "+ret);
		try{
			out_to_server.writeBytes("MEMBERS\n");
			while(true)
			{
				String response = in_from_server.readLine();
				System.out.println("Recieved |"+response+"|");
				if(response.isEmpty()){
					System.out.println("Reached Null");
					break;
				}
				ret += response +"\n";
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			ret += "SERVER: error occured while trying to retrieve all members!\n";
		}
		return ret;
	}
	
	public String myMembers()
	{
		String ret = "";
		for(Connection c : member_list)
			if(c.getUser_name()!=null)
				ret += c.getUser_name()+"\n";
		System.out.println("myMembers(): "+ret);
		return ret;
	}
	
}
