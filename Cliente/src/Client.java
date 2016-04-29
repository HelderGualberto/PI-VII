import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

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

import StandardObjects.Expression;
import StandardObjects.ExpressionResult;

public class Client extends Thread{

	ArrayBlockingQueue<ExpressionResult> expression_results;
	List<String> records = new ArrayList<String>();
	Socket connection;

	//-----------------------------------------Constructor-----------------------------------------
	public Client(String IP,int port,ArrayBlockingQueue<ExpressionResult> r_exp) throws InterruptedException, IOException{
		expression_results = r_exp;
		this.connect(IP, port);
		this.send_series(this.connection);
	}
	
	//----------------------Receive the result expressions from the slave server-----------------------------------------
	public void run(){
		while(!this.connection.isClosed()){
			InputStream input_stream;
			try {
				input_stream = connection.getInputStream();
				ObjectInputStream in_data =  new ObjectInputStream(input_stream);
				ExpressionResult er= (ExpressionResult)in_data.readObject();
				this.expression_results.add(er);
				System.out.println("Exp ID: " + er.id);
				System.out.println("ID ativo: " + er.active);
				System.out.println("Result: " + er.result);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					this.connection.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//	return;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					this.connection.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//	return;
			}
		}
		System.out.println("Server down!");
	}
	
	//---------------------------------------Initiate a connection with the slave server-------------------------------------------
	public void connect(String IP,int port){	
		try{
			this.connection = new Socket(IP,port);
			//this.send_series(this.connection);
			
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------Send the expressions to slave server---------------------------------
	public void send_expressions(Expression exp) throws IOException, InterruptedException{
		System.out.println("Transfering Data");
		OutputStream out = this.connection.getOutputStream();
		ObjectOutputStream out_object = new ObjectOutputStream(out);
		out_object.writeObject(exp); 
		out_object.flush();
	}
	//------------------------------------------Send the series to slave server (initialization)-------------------------------
	private void send_series(Socket con){
		try{
			
			String root_path = new File("..\\").getCanonicalPath()+"\\Series\\";
			File[] files = new File(root_path).listFiles();
			List<String> serie_names = new ArrayList<String>();
			
			for(File f:files){
				serie_names.add(f.getName());
				String path = root_path+f.getName();
				path = path.trim();
				//System.out.println(path);
				byte[] b;
				b = Files.readAllBytes(Paths.get(path));
				String serie = new String(b,StandardCharsets.UTF_8);
				records.add(serie);
			}
			//Send the list of string series to the server
			OutputStream out = con.getOutputStream();
			ObjectOutputStream out_object = new ObjectOutputStream(out);
			out_object.writeObject(records); out_object.flush();
			//Send the serie names to the server
			out = con.getOutputStream();
			out_object = new ObjectOutputStream(out);
			out_object.writeObject(serie_names); out_object.flush();
			
			
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("Problema ao enviar series");
		}
	}
}	
