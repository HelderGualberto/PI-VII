import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Servidor {
	ServerSocket serverSocket;
	
	List<record> series;
	LinkedList<Expression> espressions = new LinkedList<Expression>();
	
	public Servidor() throws IOException{
		 serverSocket  = new ServerSocket(10000);
	}
	
	public void receive_series(Socket socket) throws IOException{
		InputStream input_stream = socket.getInputStream();
		ObjectInputStream input_data = new ObjectInputStream(input_stream);
		try{
			this.series = (List<record>)input_data.readObject();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String arg[]) throws IOException {
		Servidor servidor = new Servidor();
		
		BroadcastReceiver UDP_receiver = new BroadcastReceiver();
		
		
		while(true){
			System.out.println("Server UP!");
			System.out.println("Start broadcasting!");
			UDP_receiver.bonjuor();
			Socket socket = servidor.serverSocket.accept();
			ServerThread connection = new ServerThread(socket,servidor.espressions);
			connection.start();
			//servidor.receive_series(socket);
			
			while(!socket.isClosed()){
				
				//Pop from expression list
				//Use the math calculator to get the result
				//Send the result to client
			}
		
			
		}	
	}
}
