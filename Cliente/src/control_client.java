import java.io.*;
import java.util.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.lang.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class control_client {

	List<record> records;
	List<Socket> connection; //implementar com lista circular

	//Constructor
	public control_client(String root_path){
		String path;
		for(int i=0;i<3;i++){
			path = root_path + Integer.toString(i) + ".txt";
			record record_table = new record(0,path);
			records.add(record_table);
		}	
	}
	
	public void connect(String IP,int port){
		Socket c;
		try{
			c = new Socket(IP,port);
			this.send_series(c);
			connection.add(c);
			
		}catch (IOException e){
			e.printStackTrace();
		}
		
	}
	
	private void send_series(Socket con){
		try{
			OutputStream out = con.getOutputStream();
			ObjectOutputStream out_object = new ObjectOutputStream(out);
			while(records.iterator().hasNext()){
				record csv_serie = records.iterator().next();
				out_object.writeObject(csv_serie); out_object.flush();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}	
