import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	ServerSocket serverSocket;
	public Servidor() throws IOException{
		 serverSocket  = new ServerSocket(10000);

	}
	
	public static void main(String arg[]) throws IOException {
		Servidor servidor = new Servidor();
		while(true){
			Socket socket = servidor.serverSocket.accept();
			ServerThread connection = new ServerThread(socket);
			connection.start();
		}	
	}
}
