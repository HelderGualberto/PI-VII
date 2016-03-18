import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.util.*;

public class DiscoverListener extends Thread{

	LinkedList<ServerInstance> instances;
	
	public LinkedList<ServerInstance> get_instances(){
		return this.instances;
	}
	
	public void run(){
				
		try {
		      int port = 4445;
		      // Create a socket to listen on the port.
		      DatagramSocket dsocket = new DatagramSocket(port);
	
		      // Create a buffer to read datagrams into. If a
		      // packet is larger than this buffer, the
		      // excess will simply be discarded!
		      byte[] buffer = new byte[512];
	
		      // Create a packet to receive data into the buffer
		      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
	
		      // Now loop forever, waiting to receive packets and printing them.
		      while (true) {
		        // Wait to receive a datagram
		    	
		    	  dsocket.receive(packet);
		    	  String server_id = new String(buffer);
		          ServerInstance instance = new ServerInstance(server_id, packet.getAddress());
		          
		          this.instances.add(instance);
		          
		        //dsocket.getLocalAddress();
		        // Convert the contents to a string, and display them
		        //String msg = new String(buffer, 0, packet.getLength());
		        //System.out.println(packet.getAddress().getHostName() + ": "+ msg);
	
		        // Reset the length of the packet before reusing it.
		        //packet.setLength(buffer.length);
		      }
		    } catch (Exception e) {
		      System.err.println(e);
		    }
		 
	}
	
}
