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
import java.util.concurrent.TimeUnit;
import java.lang.*;
import java.io.File;
import java.util.LinkedList;
import java.time.*;
import java.util.*;

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
	
	
	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException{
		
		
		LinkedList<ServerInstance> servers_available = new LinkedList<ServerInstance>(); 
		LinkedList<ExpressionResult> r_exp = new LinkedList<ExpressionResult>();
		LinkedList<Expression> exp_list = new LinkedList<Expression>();//get list from server
		List<control_client> c_control = new LinkedList<control_client>();		
		
		//Initiate the broadcast system. The sender system as well as IP receiver
		Discover server_discover = new Discover();
		server_discover.start();
		
		//Get the available servers to connect		
		while(servers_available.isEmpty()){
			servers_available = server_discover.get_available_servers();
//			try{
//				System.out.println(servers_available.size());
//			}catch(Exception e){
//				System.out.println("0");
//			}
		}
		
		//Create an iterator for the available servers list
		Iterator<ServerInstance> i = servers_available.iterator();
		ServerInstance tmp;
		
		//Initiate connection with all available servers
		while(i.hasNext()){
			tmp = i.next();
			//Send the parameters: CSV path, Host IP, Port, List of result expressions
			control_client client = new control_client("c:\\bolsa\\serie",tmp.toString(),10000,r_exp);
			client.start();
		}
		
		Iterator<control_client> i_control = c_control.iterator();
		while(true){
			while(!exp_list.isEmpty()){
				if(!i_control.hasNext())
					i_control = c_control.iterator();
				i_control.next().send_expressions(exp_list.removeFirst());
			}
			// SEND RESULT EXPRESSIONS TO EXPRESSION CLIENT
			// UPDATE EXPRESSIONS FROM EXPRESSION CLIENT
			
		}
	}
}
