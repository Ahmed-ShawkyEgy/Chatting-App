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
				
				Connection c = new Connection(connectionSocket,index++,member_list,name_to_index,allMembers);
				
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
}
