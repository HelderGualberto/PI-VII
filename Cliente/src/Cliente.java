import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;
import java.io.File;
import java.util.LinkedList;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.net.*;

public class Cliente {
	
	//Funcao para simular a expressão do servidor de expressoes
	static public LinkedList<String> get_expression(){
		
		LinkedList<String> exp_list = new LinkedList<String>();
		
		exp_list.add("Exp 1");
		exp_list.add("Exp 2");
		exp_list.add("Exp 3");
		exp_list.add("Exp 4");
		
		return exp_list; 
	}
	
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		control_client c_control = new control_client("c:\\bolsa\\serie");
		IPServer ip_controler = new IPServer();
		List<InetAddress> server_ips = ip_controler.get_server_ips();
		
		int i = 0;
		
		while(!server_ips.isEmpty()){
			c_control.connect(server_ips.remove(i).toString(), 10000); //connect with the available servers
			i++;
		}			
		
		LinkedList<String> exp_list = get_expression();
		
		/* EFETUAR O BROADCAST PARA OBTER O IP DAS MÁQUINAS LIVRES E ENVIAR AS REQUISIÇÕES PARA
		 * AS REFERENTES MÁQUINAS*/
		
		Socket socket = new Socket("localhost", 10000);
		String expression;
		OutputStream out = socket.getOutputStream();
		ObjectOutputStream out_object = new ObjectOutputStream(out);
		System.out.println(exp_list.isEmpty());
		
		while(!exp_list.isEmpty()){
			expression = exp_list.pollFirst();
			System.out.println(expression);
			out_object.writeObject(expression); out_object.flush();
		}
		
		try{
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String str = reader.readLine();
			
			System.out.println(str);
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		socket.close();
	}
}
