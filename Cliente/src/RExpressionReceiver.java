import java.util.concurrent.ArrayBlockingQueue;
import java.io.*;
import java.net.*;

import StandardObjects.ExpressionResult;


public class RExpressionReceiver extends Thread{
	ArrayBlockingQueue<ExpressionResult> results;
	Socket connection;
	
	public RExpressionReceiver(ArrayBlockingQueue<ExpressionResult> er,String ip, int port) throws UnknownHostException, IOException{
		this.results = er;
		this.connection = new Socket(ip,port);
	}
	
	public void run(){
		//code to receive the expression results
		while(!this.connection.isClosed()){
			InputStream input_stream;
			try {
				input_stream = connection.getInputStream();
				ObjectInputStream in_data =  new ObjectInputStream(input_stream);
				ExpressionResult er= (ExpressionResult)in_data.readObject();
				this.results.add(er);
				System.out.println("Exp ID: " + er.id + "-->"+er.result);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
