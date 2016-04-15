import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class GeneratorServer {
	
	static public void get_expression(List<Expression> expressions) throws IOException{
		
		byte[] buffer = new byte[500];
		System.out.print("Enter the expression: ");
		System.in.read(buffer, 0, 499);
		String a = new String(buffer,StandardCharsets.UTF_8);
		a = a.trim();
		Expression exp = new Expression(a,1);
		expressions.add(exp);
	}
	
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
				get_expression(expressions);
				if(!expressions.isEmpty()){
						out_stream = connection.getOutputStream();
						data_out = new ObjectOutputStream(out_stream);
						data_out.writeObject(expressions.remove(0));
						data_out.flush();
				}
			}
			
		}	
	}

}