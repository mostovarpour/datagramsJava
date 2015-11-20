/*
 * Matthew Ostovarpour
 * Daniel Durazo
 * 11/19/15
 * Datagram client in Java
 */

import java.net.*;
import java.io.*;

public class Client{
	//The port that we will connect on
	protected static int port = 23657;
	protected static int buflen = 512;

	//This is our main function
	//Network code can throw an exception
	public static void main(String[] args) throws IOException{

		//This is a BufferedReader to take input from the command line
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

		//Here we create the client socket 
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("localhost");

		//User will select from a menu, store it in this variable
		int select;

		//This while loop is for the client
		while (true){

			//This is a small options menu for the user
			System.out.println("+--------- Java Datagram Client ---------+");
			System.out.println("|     1. Echo Server                     |");
			System.out.println("|     2. DNS Lookup Service              |");
			System.out.println("|     3. Get server time                 |");
			System.out.println("|     4. Quit                            |");
			System.out.println("+----------------------------------------+");
			System.out.print("What would you like to do: ");

			//Getting the input from the user for their choice
			select = Integer.parseInt(userInput.readLine());

			//Call the appropriate method based on user selection
			//Values based on text menu above
			//Any number other than 1-4 will show user error message and ask again
			if (select == 1) {
				echoServer(clientSocket, IPAddress);
			} else if (select == 2) {
				dnsLookup(clientSocket, IPAddress);
			} else if (select == 3) {
				serverTime(clientSocket, IPAddress);
			} else if (select == 4) {
				System.exit(0);
			} else {
				System.out.println("You must enter 1, 2, 3, or 4.\n");
			}
		}

	}

	//Code that requests the echo service from server
	public static void echoServer(DatagramSocket clientSocket, InetAddress IPAddress) throws IOException{
		
		//Creating our BufferedReader and DatagramPacket
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		DatagramPacket sendPacket, recPacket;
		String incomingMesg;

		//Our buffer
		byte[] buf = new byte[buflen];

		//Setting the first char of the buffer to 1
		buf = ("1").trim().getBytes();

		//This will get the server ready to do what we want
		sendPacket = new DatagramPacket(buf, buf.length, IPAddress, port);
		clientSocket.send(sendPacket);

		//This will clear/reset our buffer to get it ready for the message
		buf = new byte[buflen];

		//Prompting the user for input
		System.out.print("\nEnter message to send: ");

		//Storing the user's input in buf
		buf = userInput.readLine().trim().getBytes();

		//Sending the user's input to the server
		sendPacket = new DatagramPacket(buf, buf.length, IPAddress, port);
		clientSocket.send(sendPacket);

		//Resetting the buffer for use again
		buf = new byte[buflen];

		//Receiving the input back from the server
		recPacket = new DatagramPacket(buf, buf.length);
		clientSocket.receive(recPacket);

		//Turning the bytes back into a string
		incomingMesg = new String(recPacket.getData());
		System.out.println(" Returned from server: " + incomingMesg + "\n");
	}
	
	//Code that requests the DNS lookup 
	public static void dnsLookup(DatagramSocket clientSocket, InetAddress IPAddress) throws IOException{
		
		//Creating our BufferedReader and DatagramPackets for sending and receiving
		BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
		DatagramPacket sendPacket, recPacket;
		String incomingMesg;

		//This is our buffer
		byte[] buf = new byte[buflen];

		//setting the first char of the buffer to 2 so the server knows what to do
		buf = ("2").trim().getBytes();

		//Sending the command to the server
		sendPacket = new DatagramPacket(buf, buf.length, IPAddress, port);
		clientSocket.send(sendPacket);

		//Clearing/resetting the buffer
		buf = new byte[buflen];

		//Prompting the user for input
		System.out.print("\nURL to look up: ");

		//Storing the user's input
		buf = userInput.readLine().trim().getBytes();

		//Sending the user's input to the server
		sendPacket = new DatagramPacket(buf, buf.length, IPAddress, port);
		clientSocket.send(sendPacket);

		//Clearing/resetting the buffer
		buf = new byte[buflen];

		//Receiving the packet from the server
		recPacket = new DatagramPacket(buf, buf.length);
		clientSocket.receive(recPacket);

		//Turning back into a string
		incomingMesg = new String(recPacket.getData());
		System.out.println(" The URL IP is: " + incomingMesg + "\n");

	}
	
	//Code that requests the server time
	public static void serverTime(DatagramSocket clientSocket, InetAddress IPAddress) throws IOException{
		
		//Need to send out a packet and receive from server
		DatagramPacket sendPacket, recPacket;
		String incomingMesg;

		//This is our buffer
		byte[] buf = new byte[buflen];

		//setting the first char of the buffer to 3 so the server knows what to do
		buf = ("3").trim().getBytes();

		//Sending the command to the server
		sendPacket = new DatagramPacket(buf, buf.length, IPAddress, port);
		clientSocket.send(sendPacket);

		//Reset buffer to clear it and have enough room to store time
		buf = new byte[buflen];

		//Receiving the packet from the server
		recPacket = new DatagramPacket(buf, buf.length);
		clientSocket.receive(recPacket);

		//Turning back into a string
		incomingMesg = new String(recPacket.getData());
		System.out.println("\nThe server time is: " + incomingMesg + "\n");
	}
}
