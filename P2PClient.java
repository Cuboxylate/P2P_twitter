/**
 * Client will loop continuously and send out the local user's status at random intervals
 * between 1 and 3 seconds.
 *
 * Sebastian Dunn 2013
 */

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class P2PClient implements Runnable {
	final private int port;
	final private String unikey;
	final private ArrayList<String> addresses;
	private Communication storage;
	private DatagramSocket clientSocket;

	/**
	 * Constructor connect client to shared communication object and the given datagram
	 * socket. It then requests a list of the peers' IP addresses from the communication
	 * object to store locally, so it can loop through them quickly.
	 */
	public P2PClient (Communication storage, int port, String unikey, DatagramSocket socket) {
		this.storage = storage;
		this.port = port;
		this.unikey = unikey;
		this.clientSocket = socket;
		addresses = storage.getIPAddresses();
	}

	/**
	 * Runs when thread executes. Loops endlessly, sleeping for random intervals
	 * between 1 and 3 seconds. Checks if there is a status saved for the local
	 * user at the beginning of the loop - this ensures it doesn't send anything
	 * until the first status is inputted.
	 */
	public void run() {
		Random rand = new Random();
		while (true) {
			if (storage.getMyStatus() == null) { continue; }
			try {
				Thread.sleep(1000 + rand.nextInt(2000));
			} catch (InterruptedException e) {
				System.out.println("Somebody woke up my client thread!");
			}
			//retieves status message from profile through Communication object
			sendMessage(storage.getMyStatus());
		}

	}
	
	/**
	 * Function to send the current status message to all peers. Constructs
	 * the transmission from the unikey and status, then converts it to bytes.
	 * Then for each peer it creates a datagram packet and sends it through the
	 * established datagram socket.
	 */
	private void sendMessage(String message) {
		DatagramPacket toSend;
		InetAddress sendTo;

		//Adds escape characters in front of any colons in the status
		String escaped = message.replaceAll(":", "\\\\:");	
		String outMessage = unikey + ":" + escaped;
		byte[] buffer = outMessage.getBytes();

		for (String address: addresses) {
			//System.out.println("Sending " + outMessage + " to " + address + "." + port);//Debugging statement
			try {	
				sendTo = InetAddress.getByName(address);
				toSend = new DatagramPacket(buffer, buffer.length, sendTo, port);	
				clientSocket.send(toSend);
			} catch (UnknownHostException e) {
				System.out.println(address + " not recognised as hostname.");
			} catch (IOException e) {
				System.out.println("Error sending Datagram through clientsocket");
			}
		}
	} 
}
