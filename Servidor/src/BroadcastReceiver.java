import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class BroadcastReceiver {
	
	public void bonjuor() throws IOException {
		// TODO Auto-generated method stub
		int port = 4445;
		
	DatagramSocket socket = new DatagramSocket(port);
		

      // Create a buffer to read datagrams into. If a
      // packe is larger than this buffer, the
      // excess will simply be discarded!
      byte[] buffer = new byte[10];
      String address = InetAddress.getLocalHost().getHostName();
      byte[] out_buffer = address.getBytes();
      
      
      // Create a packet to receive data into the buffer
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      // Now loop forever, waiting to receive packets and printing them.
      while (true) {
        // Wait to receive a datagram
    	  socket.receive(packet);
    	  InetAddress connected_ip = packet.getAddress();
    	  String in_data = new String(packet.getData(),StandardCharsets.UTF_8);
    	  
    	  System.out.println("IP: " + connected_ip + " said "+ in_data);
    	  
    	  if(in_data.equals("IP request")){
    	      DatagramPacket send_packet = new DatagramPacket(out_buffer,out_buffer.length,connected_ip,4446);
    		  socket.send(send_packet);
    		  break;
    	  }  	  
      }
      socket.close();
	}
}
