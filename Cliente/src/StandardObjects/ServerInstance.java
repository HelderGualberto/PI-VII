package StandardObjects;
import java.net.*;


public class ServerInstance {

	public int id;
	public InetAddress ip_address;
	
	public ServerInstance(int id,InetAddress ip){
		this.id = id;
		this.ip_address = ip;
	}
	
}
