package Broadcast;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
// see https://github.com/brunoapimentel/chat.git

import StandardObjects.ServerInstance;

public class Discover extends Thread{

	List<ServerInstance> servers_available = new LinkedList<ServerInstance>();
	
	public Discover(){
		servers_available =  Collections.synchronizedList(servers_available);
	}
	
	//Initiate the broadcast address discover
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
	
	//Initiate the broadcast message sender
	public void start_broadcast() throws InterruptedException{
		//Get the list of broadcast addresses
		LinkedList<InetAddress> broadcast = get_broadcast_address();
			
		//Start looping the broadcast addresses
		Iterator<InetAddress> i = broadcast.iterator();
		InetAddress tmp;
		while(true){
			i = broadcast.iterator();
		
			while(i.hasNext()){
				try{
					tmp = i.next();
					//Alocate a new datagram comunication
					DatagramSocket socket = new DatagramSocket();
					socket.setBroadcast(true);
					// send request with a message
					String message = "IP request\0";
					byte[] buf = message.getBytes();
					DatagramPacket packet = new DatagramPacket(buf, buf.length,tmp, 4445);
					socket.send(packet);
					socket.close();
					
				}catch(SocketException e){
					e.printStackTrace();
				} catch (IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			sleep(20000);//Wait a minute to star the broadcast send again
		}
	}
	
	public List<ServerInstance> get_available_servers(){
		return this.servers_available;
	}
	
	public void run(){
		
		// Inicializa o sistema de listener para que o servidor possa receber as respostas dos outros IPs
		DiscoverListener listener = new DiscoverListener(this.servers_available); 
		listener.start();
		
		// Inicia o broadcast para todos os IPs de broadcast encontrados
		try {
			
			this.start_broadcast();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
