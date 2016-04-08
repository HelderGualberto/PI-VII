import java.util.*;
import java.io.*;
import java.lang.*;
import java.net.*;


public class RExpressionReceiver extends Thread{
	ArrayList<GENode> results;
	Socket connection;
	
	public RExpressionReceiver(ArrayList<GENode> er,Socket connection){
		this.results = er;
		this.connection = connection;
	}
	
	public void run(){

		while(!this.connection.isClosed()){
			InputStream input_stream;
			try {
				input_stream = connection.getInputStream();
				ObjectInputStream in_data =  new ObjectInputStream(input_stream);
				GENode er= (GENode)in_data.readObject();
				this.results.add(er);
				System.out.println("Result: " + er.result);
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
