import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import javax.print.DocFlavor.INPUT_STREAM;

import StandardObjects.Expression;

import java.io.*;

public class SEReceiver extends Thread{
	
	Socket socket;
	LinkedList<Expression> expressions;
	
	public SEReceiver(Socket socket,LinkedList<Expression> exps) {
		this.socket = socket;
		this.expressions = exps;
	}
	
	private void close_connection(){
		try {
			this.socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
    public void run(){
    	  
		//Code to receive the expression
    	while(!this.socket.isClosed()){	
    		try {
				InputStream input_stream = this.socket.getInputStream();
				ObjectInputStream input_data = new ObjectInputStream(input_stream);
				Expression exp = (Expression)input_data.readObject();
				expressions.add(exp);
				//System.out.println("Input Data: " + exp.expression);
				
			} catch (IOException e) {
				e.printStackTrace();
				this.close_connection();
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				this.close_connection();
			}
    		
    	}

    }
}
