import java.net.*;
import java.io.*;
import java.util.*;

import StandardObjects.ExpressionResult;


public class ResultReceiver extends Thread{

	List<ExpressionResult> results;
	public Socket connection;
	
	public ResultReceiver(Socket connection, List<ExpressionResult> results){
		this.results = results;
		this.connection = connection;
	}
	
	public void run(){
		System.out.println("Start receiving Expression result");
		while(!connection.isClosed()){
			InputStream input_stream;
			ObjectInputStream data_in;
			try {
				input_stream = this.connection.getInputStream();
				data_in = new ObjectInputStream(input_stream);
				ExpressionResult r = (ExpressionResult)data_in.readObject();
				this.results.add(r);
			} catch (IOException e) {
				try {
					this.connection.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
}
