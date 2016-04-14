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
import java.nio.charset.StandardCharsets;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;
import java.lang.*;
import java.io.File;
import java.time.*;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.net.*;

public class MasterControler {
	
	//Funcao para simular a expressão do servidor de expressoes, atualmente a entrada e pelo terminal
	static public LinkedList<Expression> get_expression() throws IOException{
		
		LinkedList<Expression> exp_list = new LinkedList<Expression>();
		byte[] buffer = new byte[500];
		
		System.in.read(buffer, 0, 499);
		String a = new String(buffer,StandardCharsets.UTF_8);
		a = a.trim();
		Expression exp = new Expression(a,1);
		exp_list.add(exp);
		return exp_list;
	}
	
	
	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException{
		
		List<ServerInstance> servers_available; 
		List<ExpressionResult> r_exp = new LinkedList<ExpressionResult>();
		r_exp = Collections.synchronizedList(r_exp);//Syncronize the list with other threads
		
		LinkedList<Expression> exp_list;// = new LinkedList<Expression>();//get list from server
		List<Client> c_control = new LinkedList<Client>();		
		
		//Initiate the broadcast system. The sender system as well as IP receiver
		Discover server_discover = new Discover();
		server_discover.start();
		
		//Get the available servers to connect		
		
		servers_available = server_discover.get_available_servers();
		while(servers_available.isEmpty()){
			servers_available = server_discover.get_available_servers();			
		}

		ServerInstance tmp;
		//Initiate connection with all available servers
		while(!servers_available.isEmpty()){
			tmp = servers_available.remove(0);
			//Send the parameters: CSV path, Host IP, Port, List of result expressions
			Client client = new Client(tmp.ip_address.getHostAddress().toString(),10000,r_exp);
			c_control.add(client);
			client.start();
		}
		exp_list = get_expression();
		
		Iterator<Client> i_control = c_control.iterator();
		while(true){
			//Send the expressions to the server calculator
			while(!exp_list.isEmpty()){

				if(i_control.hasNext()){
					Client c = i_control.next();
					if(c.isAlive())
						c.send_expressions(exp_list.removeFirst());
					else{
						c_control.remove(c);
						break;
					}
				}
				else
					i_control = c_control.iterator();
				exp_list = get_expression();
			}
			if(c_control.isEmpty()){
				break;
			}
			// SEND RESULT EXPRESSIONS TO EXPRESSION CLIENT
			// UPDATE EXPRESSIONS FROM EXPRESSION CLIENT
			
		}
	}
}
