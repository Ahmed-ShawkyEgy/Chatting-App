package model;

public class Main {
	
	public Main() {
		Server server1 = new Server(2000);
		server1.start();
		Server server2 = new Server(3000);
		server2.start();

		server1.connect(3000);
		server2.connect(2000);
	}

}
