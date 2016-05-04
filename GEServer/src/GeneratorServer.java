import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import StandardObjects.Expression;
import StandardObjects.ExpressionResult;


public class GeneratorServer {
	
	static public void get_expression(List<Expression> expressions) throws IOException{
		
		/*
		//CODIGO PARA SIMULAR A RECEPCAO DAS SERIES A PARTIRA DA ENTRADA VIA TERMINAL
		byte[] buffer = new byte[500];
		System.out.print("Enter the expression: ");
		System.in.read(buffer, 0, 499);
		String a = new String(buffer,StandardCharsets.UTF_8);
		a = a.trim();
		Expression exp = new Expression(a,1);
		expressions.add(exp);
		*/
		
		 // CODIGO PARA SIMULAR A RECEPCAO DAS EXP A PARTIR DE UM ARQUIVO TEXTO
			try (BufferedReader br = new BufferedReader(new FileReader("c:/users/helder/Downloads/filename.txt"))) {
			    String line;
			    String a= "teste";
			    while ((line = br.readLine()) != null) {
			    	line = line.trim();
				    Expression exp = new Expression(line,a);
					expressions.add(exp);
			    }
			}
			
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket con_receiver = new ServerSocket(10010);
		List<Expression> expressions = new ArrayList<Expression>();
		List<ExpressionResult> r_exp = new ArrayList<ExpressionResult>();
		r_exp = Collections.synchronizedList(r_exp);
		
		ResultReceiver receiver;
		
		while(true){
			System.out.println("Waiting connection");
			Socket connection = con_receiver.accept();//Wait for a connection
			System.out.println("Connected with: "+connection.getInetAddress().getHostAddress().toString());
			OutputStream out_stream;
			ObjectOutputStream data_out;
			
			receiver = new ResultReceiver(connection, r_exp);
			receiver.start();
			get_expression(expressions);

			while(!connection.isClosed()){
				try{
					if(!expressions.isEmpty()){
							out_stream = connection.getOutputStream();
							data_out = new ObjectOutputStream(out_stream);
							data_out.writeObject(expressions.remove(0));
							data_out.flush();
					}
				}catch(SocketException e){
					e.printStackTrace();
					connection.close();
					break;
				}
			}
			con_receiver.close();
		}	
	}

}
