import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.io.*;

public class ServerThread extends Thread{
	
	Socket socket;
//	MathResponse calculadora = new MathResponse();
	LinkedList<Expression> expressions;
	
	
	
	public ServerThread(Socket socket,LinkedList<Expression> exps) {
		this.socket = socket;
		this.expressions = exps;
	}
	
    public void run(){
    	
    	while(!this.socket.isClosed()){	
    		try {
				InputStream input_stream = this.socket.getInputStream();
				ObjectInputStream input_data = new ObjectInputStream(input_stream);
	    		Expression exp = (Expression)input_data.readObject();
				expressions.add(exp);
				System.out.println(exp.expression);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
    		
    	}
    	
//    	InputStream is;
//		try {
//			is = socket.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//			//String str = reader.readLine();
//			//double testFormula = calculadora.testFormula(str);
//			OutputStream out = socket.getOutputStream();
//			OutputStreamWriter outwriter = new OutputStreamWriter(out);
//			PrintWriter prtWriter = new PrintWriter(outwriter);
//			prtWriter.println("RESULTADO  asdasdasdasd");
//			//prtWriter.println("RESULTADO  " + testFormula);
//			prtWriter.flush();
//			socket.close();
//
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//
//		}	
    }
}
