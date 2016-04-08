import java.io.*;
import java.util.*;

import javax.naming.spi.ObjectFactoryBuilder;

import java.net.Socket;
import java.net.StandardSocketOptions;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Client extends Thread{

	LinkedList<ExpressionResult> expression_results;
	List<String> records = new ArrayList<String>();
	Socket connection;

	//Constructor
	public Client(String IP,int port,LinkedList<ExpressionResult> r_exp) throws InterruptedException, IOException{
		expression_results = r_exp;
		this.connect(IP, port);
		this.send_series(this.connection);
	}
	
	public void run(){
		while(!this.connection.isClosed()){
			try{
				InputStream input_stream = this.connection.getInputStream();
				ObjectInputStream input_data = new ObjectInputStream(input_stream);
				ExpressionResult exp_r = (ExpressionResult)input_data.readObject();
				expression_results.add(exp_r);
				System.out.println(exp_r.result);
				
			}catch (IOException | ClassNotFoundException e) {
				try {
					this.connection.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	public void connect(String IP,int port){	
		try{
			this.connection = new Socket(IP,port);
			//this.send_series(this.connection);
			
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public LinkedList<ExpressionResult> get_expression_result(){
		return this.expression_results;
	}
	
	public void send_expressions(Expression exp) throws IOException, InterruptedException{
		System.out.println("Transfering Data");
		OutputStream out = this.connection.getOutputStream();
		ObjectOutputStream out_object = new ObjectOutputStream(out);
		out_object.writeObject(exp); 
		out_object.flush();
	}
	
	private void send_series(Socket con){
		try{
			String path;
			//for(int i=1;i<4;i++){
			byte[] b;
			b = Files.readAllBytes(Paths.get("D:\\Serie1.csv"));
			String serie = new String(b,StandardCharsets.UTF_8);
			records.add(serie);
			
			
			//Send the list of string series to the server
			OutputStream out = con.getOutputStream();
			ObjectOutputStream out_object = new ObjectOutputStream(out);
			out_object.writeObject(records); out_object.flush();
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}	
