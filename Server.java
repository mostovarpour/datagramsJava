/*
 * Matthew Ostovarpour
 * Daniel Durazp
 * 11/19/15
 * Datagram server in Java
 */

//Required imports
import java.net.*;
import java.io.*;
import java.util.*;


public class Server{
	//The port that we will connect on
	protected static int port = 23657;

	//Default buffer size
	protected static int buflen = 512;

	//This is our main function
	public static void main(String[] args) throws Exception{

		//Here we create the server socket
		DatagramSocket serverSocket = new DatagramSocket(port);
		
		//This will be used to point to each new datagram packet that arrives
		DatagramPacket recPacket;

		//Here we define our buffer for receiving what to do
		byte[] recBuff;
		

		//This while loop is for the server to always continue processing requests
		while (true){
			
			//Server side message to show waiting for data to arrive
			System.out.println("Waiting for data...");
			
			//Clear out the buffer before use each time
			//Arrays.fill(recBuff, (byte)0);
			recBuff = new byte[buflen];

			//First need to receive datagram to know what service was requested
			recPacket = new DatagramPacket(recBuff, recBuff.length);
			
			//Attempt to receive datagram from the server socket and store in receive packet
			serverSocket.receive(recPacket);
			
			
			//recBuff[0] should now contain the requested service
			// 1 = Echo the next datagram message
			// 2 = DNS lookup (URL to IP)
			// 3 = Request server's local time
			
			if (recBuff[0] == '1'){
				// Echo service requested
				echoServer(serverSocket);
				
				
			} else if(recBuff[0] == '2'){
				// DNS lookup requested
				dnsLookup(serverSocket);
				
			} else {
				// Must be in case 3, request the local time
				serverTime(serverSocket);
			}

		} //End server while loop

	} //End server main
	

	//This is our echo server method which takes in the serverSocket to get the next datagram
	//There is a chance of an exception, pass it back to main method if something fails
	public static void echoServer(DatagramSocket serverSocket) throws IOException{
		
		//Local buffer for reading message to echo back
		byte[] buf = new byte[buflen];
		
		//This will clear our buffer to get it ready for the message
		Arrays.fill(buf, (byte)0);

		//Create the datagram packet object and use the local buffer
		DatagramPacket recPacket = new DatagramPacket(buf, buf.length);

		//Attempt to receive datagram from the server socket and store in receive packet
		serverSocket.receive(recPacket);
		
		//Get the IP address to send a reply back to client
		InetAddress ipAddress = recPacket.getAddress();
		
		//Get the port to send back to
		int sendPort = recPacket.getPort();
		
		//Extract the message to echo back and store in temporary string
		String echoMe = new String(recPacket.getData());
		
		//Display this on server side to show what will be echo'd
		System.out.println("Client echo request: " + echoMe);
		
		//Clear out the buffer to hold the message bytes to send back to client
		Arrays.fill(buf, (byte)0);
		buf = echoMe.getBytes();
		
		//Create the datagram packet to send back to client and then send it
		DatagramPacket sendMe = new DatagramPacket(buf, buf.length, ipAddress, sendPort);
		serverSocket.send(sendMe);
		
	}
	
	
	public static void dnsLookup(DatagramSocket clientSocket) throws IOException{
		//This is local buffer for receiving and sending
		byte[] buf = new byte[buflen];

		//Clearing the buffer before we use it
		Arrays.fill(buf, (byte)0);

	}
	public static void serverTime(DatagramSocket clientSocket) throws IOException{

	}
}
