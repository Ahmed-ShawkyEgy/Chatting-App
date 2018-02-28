import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Thread{ 
	
	ArrayList<Connection> member_list;
	HashMap<String, Integer>  name_to_index;
	StringBuilder allMembers;
	private Server other_server;
	public int port_number;
	
	public Server(int port_number)
	{
		this.port_number = port_number;
		
	}
	
	
	
	public void setOther_server(Server other_server) {
		this.other_server = other_server;
	}



	public void run()
	{
		try
		{
			ServerSocket welcomeSocket = new ServerSocket(port_number); 
		    member_list = new ArrayList<Connection>();
			name_to_index = new HashMap<String, Integer>();
			allMembers = new StringBuilder();
			int index = 0;
			
			while(true)
			{			
				try{
					Socket connectionSocket = welcomeSocket.accept(); 
					
					Connection c = new Connection(connectionSocket,index++,member_list,name_to_index,allMembers,this);
					
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
		if(TTL<=0)
			return false;
		if(name_to_index.containsKey(name))
		{
			int index = name_to_index.get(name);
			return member_list.get(index).recieve(message);
		}
		else if(other_server!=null)
		{
			return other_server.sendTo(name, message, TTL-1);
		}
		return false;
	}
}
