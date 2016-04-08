import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Slave {
	//------------------------------------------------GLOBAL VARIABLES-----------------------------------------------
	ServerSocket serverSocket;
	List<Record> series;
	LinkedList<Expression> expressions = new LinkedList<Expression>();
	ArrayList<ExpressionResult> results = new ArrayList<ExpressionResult>();
	
	//------------------------------------------------CONSTRUCTOR-----------------------------------------------
	public Slave() throws IOException{
		this.series = new ArrayList<Record>();
		serverSocket  = new ServerSocket(10000);
	}
	
	private Record get_csv_from_string(int id, String serie){
		Record r = new Record(id,serie);
		return r;
	}
	
	//------------------------------------------------RECEIVE SERIES-----------------------------------------------
	public void receive_series(Socket socket) throws IOException{
		System.out.println("Waiting series");
		InputStream input_stream = socket.getInputStream();
		ObjectInputStream input_data = new ObjectInputStream(input_stream);
		Record r;
		try{
			List<String> s_series = (List<String>)input_data.readObject();
			int id = 0;
			for(String s: s_series){
				r = get_csv_from_string(id, s);
				this.series.add(r);
				id++;
			}
		
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	//------------------------------------------------MAIN FUNCTION-----------------------------------------------
	public static void main(String arg[]) throws IOException {
		Slave servidor = new Slave();
		BroadcastReceiver UDP_receiver = new BroadcastReceiver();
		MathResponse calculator = new MathResponse();
		
		while(true){
			System.out.println("Server UP!");
			System.out.println("Start listening broadcast!");
			UDP_receiver.bonjuor();
			Socket socket = servidor.serverSocket.accept();
			ServerThread connection = new ServerThread(socket,servidor.expressions);
			servidor.receive_series(socket);
			
			connection.start();
			
			System.out.println("Number of series:" + servidor.series.size());
			/*
			int open = 1;
			for(Record r: servidor.series){
				for(int i=0;i<r.serie.size();i++){
					String s = r.serie.get(i).get(open).toString();
				}
			}
			*/
			while(connection.isAlive()){
				ExpressionResult exp_r;
				if(!servidor.expressions.isEmpty()){
					Expression exp = servidor.expressions.removeFirst();
					double result;
					for(Record r:servidor.series){
						calculator.setup(r);
						try{
							result = calculator.testFormula(exp.expression);
							exp_r = new ExpressionResult(result,exp.ID,r.id);
							servidor.results.add(exp_r);
							System.out.println("Resultado: " + result);
						}catch(Exception e){
							System.out.println("Error in MathResponse");
							e.printStackTrace();
						}
							
					}
				}
				//Condition to send the result array back
				if(servidor.expressions.size()<servidor.results.size() || servidor.results.size() > 50){
					OutputStream out_stream = socket.getOutputStream();
					ObjectOutputStream out_data = new ObjectOutputStream(out_stream);
					out_data.writeObject(servidor.results);
					out_data.flush();
					
				}
				//Pop from expression list
				//Use the math calculator to get the result
				//Send the result to client
			}	
			socket.close();
		}	
	}
}
