import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;
import java.lang.*;

public class serverTest {
	@SuppressWarnings("deprecation")
	
	static public void open_series_list(List<File> series) throws FileNotFoundException,IOException{
		FileInputStream read_file;
		BufferedInputStream opened_file;
		DataInputStream input_data;
		
		for(File input_file: series){
			read_file = new FileInputStream(input_file);
			opened_file = new BufferedInputStream(read_file); 
			input_data = new DataInputStream(opened_file);
			
			if(input_file!=null){	
				while(input_data.available()!=0)
					System.out.println(input_data.readLine());
			}
			
			read_file.close();
			opened_file.close();
			input_data.close();
		}
	}
	
	public static void main(String arg[]) throws IOException, ClassNotFoundException {
		try{
			ServerSocket server = new ServerSocket(10000);
			InputStream reader;
			while(true){
				Socket socket = server.accept();
				reader = socket.getInputStream();
				ObjectInputStream read_object = new ObjectInputStream(reader);
				List<File> series = (List<File>)read_object.readObject();
				open_series_list(series);
				String expression = (String)read_object.readObject();
				System.out.println(expression);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	
}
