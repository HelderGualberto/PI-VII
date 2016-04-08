import java.util.*;
import java.io.*;
import java.lang.*;
import java.net.*;


public class RExpressionReceiver extends Thread{
	ArrayList<ExpressionResult> results;
	Socket connection;
	
	public RExpressionReceiver(ArrayList<ExpressionResult> er,Socket connection){
		this.results = er;
		this.connection = connection;
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
				System.out.println("Exp ID: " + er.id);
				System.out.println("ID ativo: " + er.active);
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
