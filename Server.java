/*
 * Matthew Ostovarpour
 * Daniel Durazo
 * 11/19/15
 * Datagram server in Java
 */

//Required imports
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
			System.out.println("Waiting for data...\n");

			//Clear out the buffer before use each time
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
				//DNS lookup requested
				dnsLookup(serverSocket);

			} else {
				//Must be in case 3, request the local time
				//This is a special case, need to pass in the address and port of client
				
				//Get the client's address
				InetAddress sendAddress = recPacket.getAddress();
				
				//Get the client's port
				int sendPort = recPacket.getPort();
				
				//Do the time lookup
				serverTime(serverSocket, sendAddress, sendPort);
			}

		} //End server while loop

	} //End server main


	//This is our echo server method which takes in the serverSocket to get the next datagram
	//There is a chance of an exception, pass it back to main method if something fails
	public static void echoServer(DatagramSocket serverSocket) throws IOException{

		//Local buffer for reading message to echo back
		byte[] buf = new byte[buflen];

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
		System.out.println("  Sending to client: " + echoMe + "\n");

		//Set buffer to hold the message bytes to send back to client
		buf = echoMe.getBytes();

		//Create the datagram packet to send back to client and then send it
		DatagramPacket sendMe = new DatagramPacket(buf, buf.length, ipAddress, sendPort);
		serverSocket.send(sendMe);

	}


	public static void dnsLookup(DatagramSocket serverSocket) throws IOException{

		//Local buffer for reading message to echo back
		byte[] buf = new byte[buflen];

		//Create the datagram packet object and use the local buffer
		DatagramPacket recPacket = new DatagramPacket(buf, buf.length);

		//Attempt to receive datagram from the server socket and store in receive packet
		serverSocket.receive(recPacket);

		//Get the IP address to send a reply back to client
		InetAddress ipAddress = recPacket.getAddress();

		//Get the port to send back to
		int sendPort = recPacket.getPort();

		//Extract the URL/host to lookup
		String theHost = new String(recPacket.getData());

		//Display this on server side to show what user requested
		System.out.println(" Client DNS lookup: " + theHost);

		//Look up the IP address by name
		InetAddress theAddress = InetAddress.getByName(theHost);

		//Store the IP address as a string
		theHost = theAddress.getHostAddress();

		//Print out what will be sent back
		System.out.println("Sending IP address: " + theHost + "\n");

		//Copy the message to send back into the buffer
		buf = theHost.getBytes();

		//Create the datagram packet to send back to client and then send it
		DatagramPacket sendMe = new DatagramPacket(buf, buf.length, ipAddress, sendPort);
		serverSocket.send(sendMe);

	}
	public static void serverTime(DatagramSocket serverSocket, InetAddress ipAddress, int sendPort) throws IOException{
		//Local buffer for writing message to client
		byte[] buf = new byte[buflen];

		//Display this on server side to show what user requested
		System.out.println("Client requesting local server time");

		//Looking to only show the time in hour:minute format
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		
		//Get the current date and time
		Date theDate = new Date();
		
		//Process the date/time to hour:minute string
		String theTime = timeFormat.format(theDate);
		
		//Print out what will be sent back
		System.out.println("Sending local time: " + theTime + "\n");

		//Copy the message to send back into the buffer
		buf = theTime.getBytes();

		//Create the datagram packet to send back to client and then send it
		DatagramPacket sendMe = new DatagramPacket(buf, buf.length, ipAddress, sendPort);
		serverSocket.send(sendMe);
	}
}
