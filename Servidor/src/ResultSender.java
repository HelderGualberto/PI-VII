import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

import StandardObjects.ExpressionResult;

public class ResultSender extends Thread{
	
	List<ExpressionResult> result_expressions;
	
	
	
	public ResultSender(List<ExpressionResult> result_expressions) {
		this.result_expressions = result_expressions;
	}

	public void run(){

		Socket connection;
		try {
			ServerSocket server_conn = new ServerSocket(7000);
			connection = server_conn.accept();
			
			while(true){
				if(!this.result_expressions.isEmpty()){
					ExpressionResult r = this.result_expressions.remove(0);
					OutputStream out_stream;
					try {
						out_stream = connection.getOutputStream();
						ObjectOutputStream out_data = new ObjectOutputStream(out_stream);
						out_data.writeObject(r);
						out_data.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Erro abrindo socket servidor");
		}
		System.out.println("Expressions sender server CONNECTED");
		
		
		
		
	}
	
}
