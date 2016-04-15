import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.*;
import java.util.*;

import org.omg.CORBA.portable.OutputStream;


public class GeneratorServer {
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket con_receiver = new ServerSocket(10010);
		List<Expression> expressions = new ArrayList<Expression>();
		List<ExpressionResult> r_exp = new ArrayList<ExpressionResult>();
		r_exp = Collections.synchronizedList(r_exp);
		
		while(true){
			Socket connection = con_receiver.accept();//Wait for a connection
			OutputStream out_stream;
			ObjectOutputStream data_out;
			
			while(!connection.isClosed()){
				if(!expressions.isEmpty()){
						out_stream = (OutputStream) connection.getOutputStream();
						data_out = new ObjectOutputStream(out_stream);
						data_out.writeObject(expressions.remove(0));
						data_out.flush();
				}
			}
			
		}
		
		
		
	}

}
