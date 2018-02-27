
public class Main {
	
	
	public static void main(String[] args) {
		Server server1 = new Server(2000);
		server1.start();
		Server server2 = new Server(3000);
		server2.start();
		
		server1.setOther_server(server2);
		server2.setOther_server(server1);
	}

}
