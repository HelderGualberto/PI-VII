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

public class Cliente {
	
	static public List<File> get_series() throws IOException{
		
		List<File> series_csv = new ArrayList<File>();
		File read_serie;
		String path;
		
		for(int i=1;i<4;i++){
			path = "d:\\bolsa\\Serie" + Integer.toString(i) + ".csv";
			read_serie = new File(path);
			//read_serie = new BufferedReader(new FileReader(path));		
			series_csv.add(read_serie);
		}
		
		return series_csv;
	}
	//Funcao para requisitar a expressão do servidor de expressoes
	static public LinkedList<String> get_expression(){
		
		LinkedList<String> exp_list = new LinkedList<String>();
		
		exp_list.add("Exp 1");
		exp_list.add("Exp 2");
		exp_list.add("Exp 3");
		exp_list.add("Exp 4");
		
		return exp_list; 
	}
	
	
	public static void main(String args[]) throws UnknownHostException, IOException{
		
		List<File> series_csv = get_series();
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
			out_object.writeObject(series_csv); out_object.flush();
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
