import java.io.*;
import java.util.*;

import javax.naming.spi.ObjectFactoryBuilder;

import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class control_client extends Thread{

	LinkedList<ExpressionResult> expression_results;
	List<record> records;
	Socket connection; //implementar com lista circular

	//Constructor
	public control_client(String root_path,String IP,int port,LinkedList<ExpressionResult> r_exp){
		String path;
		expression_results = r_exp;
//		for(int i=1;i<4;i++){
//			path = root_path + Integer.toString(i) + ".csv";
//			record record_table = new record(i,path);
//			records.add(record_table);
//		}
		this.connect(IP, port);
	}
	
	public LinkedList<ExpressionResult> get_expression_result(){
		return this.expression_results;
	}
	
	public void send_expressions(Expression exp) throws IOException{
		this.send(exp,this.connection);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	public void connect(String IP,int port){	
		try{
			this.connection = new Socket(IP,port);
			System.out.println(IP);
			//this.send_series(this.connection);
			
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void send(Expression exp,Socket con) throws IOException{
		OutputStream out = con.getOutputStream();
		ObjectOutputStream out_object = new ObjectOutputStream(out);
		out_object.writeObject(exp); out_object.flush();
	}
	
	private void send_series(Socket con){
		try{
			OutputStream out = con.getOutputStream();
			ObjectOutputStream out_object = new ObjectOutputStream(out);
			//Send all the series to the server
			out_object.writeObject(records); out_object.flush();
			
//			while(records.iterator().hasNext()){
//				record csv_serie = records.iterator().next();
//				out_object.writeObject(csv_serie); out_object.flush();
//			}
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}	
