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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.Receiver;

import java.lang.*;
import java.io.File;
import java.time.*;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import Broadcast.Discover;

import java.net.*;

import StandardObjects.*;
import GEInterface.*;


/*
 * PORT MAPPING
 * 
 * 10000 - TCP CONNECTION WITH THE SLAVES
 * 4445 - UDP BROADCAST SENDER
 * 4446 - UDP BROADCAST RECEIVER
 * 10010 - TCP CONNECTION WITH EXRPRESSION GENERATOR SERVER
 * 
 * */



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
		
		ArrayBlockingQueue<ExpressionResult> result_exp = new ArrayBlockingQueue<ExpressionResult>(200000);
	
		ArrayBlockingQueue<Expression> exp_list = new ArrayBlockingQueue<Expression>(200000);//Synchronize the list with the Expression receiver thread
		
		List<Client> c_control = new LinkedList<Client>();		
		//-----------------------------------------------------------------------------

		//Create a connection with the expression generator server
		ExpressionReceiver expression_receiver;
		do{
			expression_receiver = new ExpressionReceiver(exp_list,"192.168.0.8");
		}while(!expression_receiver.isConnected());
		expression_receiver.start();
		OutputStream out_stream;
		ObjectOutputStream data_out;
		//-----------------------------------------------------------------------------
		
		//Initiate the broadcast system. The sender system as well as IP receiver
		Discover server_discover = new Discover();
		server_discover.start();
		
		//-----------------------------------------------------------------------------
		
		//Get the available servers to connect		
		servers_available = server_discover.get_available_servers();
		while(servers_available.isEmpty()){
			servers_available = server_discover.get_available_servers();			
		}

		//-----------------------------------------------------------------------------

		ServerInstance tmp;
		//Initiate connection with all available servers
		while(!servers_available.isEmpty()){
			tmp = servers_available.remove(0);
			//Send the parameters: CSV path, Host IP, Port, List of result expressions
			Client client = new Client(tmp.ip_address.getHostAddress().toString(),10000,result_exp);
			c_control.add(client);
			client.start();
		}
		
		//-----------------------------------------------------------------------------
		Iterator<Client> i_control = c_control.iterator();
		while(true){
			/*
			 * *
			 * *
			 */
			//implementar algum tipo de BLOCK
			
			//Send the expressions to the server calculatorg
			while(!exp_list.isEmpty()){
				
				//send the expressions for all servers (slaves) connected, like a circular list
				if(i_control.hasNext()){
					Client c = i_control.next();
					if(c.isAlive())
						c.send_expressions(exp_list.remove());
					else{
						c_control.remove(c);
						break;
					}
				}
				else
					i_control = c_control.iterator();
				
				if(!result_exp.isEmpty()){
					out_stream = expression_receiver.connection.getOutputStream();
					data_out = new ObjectOutputStream(out_stream);
					data_out.writeObject(result_exp.remove());data_out.flush();
				}
			}
			if(c_control.isEmpty()){
				break;
			}
			if(!result_exp.isEmpty()){
				out_stream = expression_receiver.connection.getOutputStream();
				data_out = new ObjectOutputStream(out_stream);
				data_out.writeObject(result_exp.remove());data_out.flush();
			}			
		}
	}
}
