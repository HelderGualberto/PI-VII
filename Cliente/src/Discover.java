

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.*;

// see https://github.com/brunoapimentel/chat.git

public class Discover extends Thread{

	List<InetAddress> ips;
	
	private LinkedList<InetAddress> get_broadcast_address(){
		LinkedList<InetAddress> broadcast_addresses = new LinkedList<InetAddress>();
		
		try{
			//Obtém as interfaces de rede, possibilitando obter os endereços de broadcast
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback())
					continue; // Don't want to broadcast to the loopback interface
				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcastTmp = interfaceAddress.getBroadcast();
					if (broadcastTmp == null)
						continue;
					else{
						broadcast_addresses.add(broadcastTmp);
					}
				}		
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		return broadcast_addresses;
	}
	
	public void start_broadcast(){
		//Get the list of broadcast addresses
		LinkedList<InetAddress> broadcast = get_broadcast_address();
			
		//Start looping the broadcast addresses
		while(!broadcast.isEmpty()){
				
			InetAddress broadcast_ip = broadcast.iterator().next();
			try{
				//Alocate a new datagram comunication
				DatagramSocket socket = new DatagramSocket();
				socket.setBroadcast(true);
				// send request with a message
				String message = "IP request";
				byte[] buf = message.getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length,broadcast_ip, 4445);
				socket.send(packet);
				socket.close();
				
			}catch(SocketException e){
				e.printStackTrace();
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
	}
	
	public void run(){
		
		DiscoverListener listener = new DiscoverListener();
		listener.start();
		
		this.start_broadcast();
		
		
	}
}
