import java.net.ServerSocket;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.net.*;


public class IPControler extends Thread{
	Socket connection;
	
	public IPControler(Socket connection){
		this.connection = connection;
	}
	
	public InetAddress get_server_ip() throws IOException{
				
		InetAddress ip;
		ip = this.connection.getInetAddress();
		this.connection.close();
		
		return ip;
	}
}
