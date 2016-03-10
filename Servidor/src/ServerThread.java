import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread{
	Socket socket;
	MathResponse calculadora = new MathResponse();
	public ServerThread(Socket socket) {
		this.socket = socket;
	}

    public void run(){
    	
    	InputStream is;
		try {
			is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			//String str = reader.readLine();
			//double testFormula = calculadora.testFormula(str);
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter outwriter = new OutputStreamWriter(out);
			PrintWriter prtWriter = new PrintWriter(outwriter);
			prtWriter.println("RESULTADO  asdasdasdasd");
			//prtWriter.println("RESULTADO  " + testFormula);
			prtWriter.flush();
			socket.close();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}	
    }
}
