/*
 * Matthew Ostovarpour
 * Daniel Durazp
 * 11/19/15
 * Datagram client in Java
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class Client{
    //The port that we will connect on
    protected static int port = 23657;
    protected static int buflen = 512;

    //This is our main funciton
    public static void main(String[] args) throws Exception{
        //This is a BufferedReader to take input from the commadn line
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        //Here we create the client socket
        try (DatagramSocket clientSocket = new DatagramSocket()){
            InetAddress IPAddress = InetAddress.getByName("localhost");

            //Here we define our buffer
            byte[] recieveBuff = new byte[buflen];
            byte[] sendBuff = new byte[buflen];
            int stillGoing = 1, select;

            //This while loop is for the client
            while (stillGoing == 1){
                select = 0;

                //This is a small options menu for the user
                System.out.println("==========Datagram Client==========");
                System.out.println("+         1. Echo Server          +");
                System.out.println("+         2. DNS Lookup Service   +");
                System.out.println("+         3. Get server time      +");
                System.out.println("+         4. Quit                 +");
                System.out.println("===================================");
                System.out.print("What would you like to do: ");
                
                //Getting the input from the user
                select = Integer.parseInt(userInput.readLine());

                //This will tell the server what the user wants to do
                if (select == 1) echoServer(clientSocket, IPAddress);
                else if (select == 2) dnsLookup(clientSocket, IPAddress);
                else if (select == 3) serverTime(clientSocket, IPAddress);
                else if (select == 4) stillGoing = 0;
                else System.out.println("You must enter 1 or 2 or 3 or 4.");
            }
        }catch (IOException e){}
    }

    //This is our echo server method which takes in the clientSocket and IPAddress as parameters
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

        //This will clear our buffer to get it ready for the message
        Arrays.fill(buf, (byte)0);

        //Prompting the user for input
        System.out.print("Enter message: ");

        //Storing the user's input in buf
        buf = userInput.readLine().trim().getBytes();

        //Sending the user's input to the server
        sendPacket = new DatagramPacket(buf, buf.length, IPAddress, port);
        clientSocket.send(sendPacket);

        //Clearing the buffer
        Arrays.fill(buf, (byte)0);
        
        //Receiving the input back from ther server
        recPacket = new DatagramPacket(buf, buf.length);
        clientSocket.receive(recPacket);
        
        //Turning the bytes back into a string
        incomingMesg = new String(recPacket.getData());
        System.out.println("Received from the server: " + incomingMesg);
    }
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

        //Clearing the buffer
        Arrays.fill(buf, (byte)0);

        //Prompting the user for input
        System.out.print("Enter the domain you would like to look up: ");

        //Storing the user's input
        buf = userInput.readLine().trim().getBytes();

        //Sending the user's input to the server
        sendPacket = new DatagramPacket(buf, buf.length, IPAddress, port);
        clientSocket.send(sendPacket);

        //Clearing the buffer
        Arrays.fill(buf, (byte)0);

        //Receiving the packet from the server
        recPacket = new DatagramPacket(buf, buf.length);
        clientSocket.receive(recPacket);

        //Turning back into a string
        incomingMesg = new String(recPacket.getData());
        System.out.println("Your address resolved to " + incomingMesg);

    }
    public static void serverTime(DatagramSocket clientSocket, InetAddress IPAddress) throws IOException{
        //Creating our BufferedReader and DatagramPackets for sending and receiving
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        DatagramPacket sendPacket, recPacket;
        String incomingMesg;

        //This is our buffer
        byte[] buf = new byte[buflen];

        //setting the first char of the buffer to 3 so the server knows what to do
        buf = ("3").trim().getBytes();

        //Seding the command to the server
        sendPacket = new DatagramPacket(buf, buf.length, IPAddress, port);
        clientSocket.send(sendPacket);

        //Clearing the buffer
        Arrays.fill(buf, (byte)0);

        //Receiving the packet from the server
        recPacket = new DatagramPacket(buf, buf.length);
        clientSocket.receive(recPacket);

        //Turning back into a string
        incomingMesg = new String(recPacket.getData());
        System.out.println("The time is: " + incomingMesg);
    }
}
