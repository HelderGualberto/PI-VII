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
	List<ExpressionResult> results = new LinkedList<ExpressionResult>();
	
	//------------------------------------------------CONSTRUCTOR-----------------------------------------------
	public Slave() throws IOException{
		this.results = Collections.synchronizedList(results);
		this.series = new ArrayList<Record>();
		serverSocket  = new ServerSocket(10000);
	}
	
	private Record get_csv_from_string(String serie){
		Record r = new Record(serie);
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
			for(String s: s_series){
				r = get_csv_from_string(s);
				this.series.add(r);
			}
		
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		input_stream = socket.getInputStream();
		input_data = new ObjectInputStream(input_stream);
		try{
			List<String> serie_name = (List<String>)input_data.readObject();
			for(int i=0;i<serie_name.size();i++){
				System.out.println(serie_name.get(i));
				this.series.get(i).active_name = serie_name.get(i);
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
		
			while(connection.isAlive()){
				ExpressionResult exp_r;
				if(!servidor.expressions.isEmpty()){
					Expression exp = servidor.expressions.removeFirst();
					double result;
					for(Record r:servidor.series){
						calculator.setup(r);
						try{
							result = calculator.testFormula(exp.expression);
							exp_r = new ExpressionResult(result,r.active_name,exp.ID);
							servidor.results.add(exp_r);
							System.out.println("Resultado: " + result);
						}catch(Exception e){
							System.out.println("Error in MathResponse");
							e.printStackTrace();
						}
							
					}
				}
				//Condition to send the result array back
				if(!servidor.results.isEmpty()){
					ExpressionResult r = servidor.results.remove(0);
					OutputStream out_stream = socket.getOutputStream();
					ObjectOutputStream out_data = new ObjectOutputStream(out_stream);
					out_data.writeObject(r);
					out_data.flush();
				}
				//Pop from expression list
				//Use the math calculator to get the result
				//Send the result to client
			}	
			socket.close();
			servidor.series.clear();
			servidor.expressions.clear();
			servidor.results.clear();
		}	
	}
}
