package GEInterface;
import java.net.InetAddress;
import java.net.Socket;
import StandardObjects.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.*;
import java.net.*;

public class ExpressionReceiver extends Thread{
	public Socket connection;
	List<Expression> expressions;
	
	public ExpressionReceiver(List<Expression> exps,String ip){
		this.expressions = exps;
		
		try {
			Socket con = new Socket(ip,10010);
			this.connection = con;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public boolean isConnected(){
		return this.connection.isConnected();
	}
	
	public void run(){
		
		InputStream input_stream;
		
		while(!this.connection.isClosed()){
			try {
				input_stream = this.connection.getInputStream();
				ObjectInputStream data = new ObjectInputStream(input_stream);
				Expression exp = (Expression)data.readObject();
				this.expressions.add(exp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				try {
					this.connection.close();
					System.out.println("Server Down. Connection finished");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}
