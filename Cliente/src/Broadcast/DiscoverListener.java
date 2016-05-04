package Broadcast;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
import java.util.*;

import StandardObjects.ServerInstance;

public class DiscoverListener extends Thread{

	List<ServerInstance> instances;
	private DatagramSocket dsocket;
	
	public DiscoverListener(List<ServerInstance> instances){
		this.instances = instances;
	}
	
	public void run(){
				
		try {
		      int port = 4446;
		      dsocket = new DatagramSocket(port);
	
		      // Create a buffer to read datagrams into. If a
		      // packet is larger than this buffer, the
		      // excess will simply be discarded!
		      byte[] buffer = new byte[512];
	
		      // Create a packet to receive data into the buffer
		      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	
		      // Now loop forever, waiting to receive packets and printing them.
		      int server_id = 0;
		      while (true) {
		        // Wait to receive a datagram
		    	  dsocket.receive(packet);
		    	  System.out.println("IP: " + packet.getAddress().toString()+" said "+ new String(packet.getData(),StandardCharsets.UTF_8));
		    	  //Create a server instance that keep the computer id and IP
		          ServerInstance instance = new ServerInstance(server_id, packet.getAddress());
		          // Add the instance in the ServerInstance list
		          this.instances.add(instance);
		          server_id++;
		          
		      }
		  } catch (Exception e) {
		      
		      System.err.println(e);
		    }
		
		 
	}
	
}
