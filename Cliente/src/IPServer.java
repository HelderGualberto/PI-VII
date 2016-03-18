import java.io.IOException;
import java.util.*;
import java.net.*;


public class IPServer extends Thread{
	private ServerSocket serverSocket;
	private List<InetAddress> ips;
	
	public IPServer() throws IOException{
		 serverSocket  = new ServerSocket(10001);
	}
	
	public List<InetAddress> get_server_ips(){
		return this.ips;
	}
	
	public void run() {
		
		try{
			IPServer servidor = new IPServer();
			while(true){
				Socket socket = servidor.serverSocket.accept();
				IPControler connection = new IPControler(socket);
				connection.start();
				ips.add(connection.get_server_ip());
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}


}
