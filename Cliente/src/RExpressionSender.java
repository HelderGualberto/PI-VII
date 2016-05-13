import java.net.Socket;
import java.util.ArrayList;
import StandardObjects.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class RExpressionSender extends Thread{
	
	Socket connection;
	ArrayList<ExpressionResult> results;
	
	public RExpressionSender(Socket con,ArrayList<ExpressionResult> r){
		this.connection = con;
		this.results = r;
	}
	
	public void run(){
		
		while(!connection.isClosed()){
			try{
				OutputStream out = connection.getOutputStream();
				ObjectOutputStream out_data;
				
				if(!results.isEmpty()){
					out_data = new ObjectOutputStream(out);
					out_data.writeObject(results.get(0));
					out_data.flush();
				}
				
			}catch(IOException e){
				e.printStackTrace();
				try {
					System.out.println("Server: " + connection.getInetAddress().getHostName().toString()+ "is down");
					connection.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
	}
	
}
