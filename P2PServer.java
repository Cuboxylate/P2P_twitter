/**
 * Server listens to the given datagram socket, and stores any messages it 
 * receives in the correct format in that peer's profile.
 *
 * Sebastian Dunn 2013
 */

import java.net.*;
import java.io.*;

public class P2PServer implements Runnable {
	private Communication storage;
	private DatagramSocket serverSocket;

	/**
	 * Contructor saves location of datagram socket and connects this
	 * object to the shared communication object.
	 */
	public P2PServer (Communication storage, DatagramSocket socket) {
		this.storage = storage;
		this.serverSocket = socket;
	}

	/**
	 * Executes when thread is started. LoopSs continuously waiting for requests
	 * through the socket, and storing them when they are received.
	 */
	public void run() {
		while (true) {
			//Buffer is refreshed with each loop to prevent errors from
			//overflowing. Given lots of room in case many escape characters
			//are used.
			byte[] buffer = new byte[500];
			DatagramPacket received = new DatagramPacket(buffer, buffer.length);
			try {
				serverSocket.receive(received);
				//Converts bytes to a String on the fly, with the required encoding
				storeMessage(new String(received.getData(), "ISO-8859-1"));
			} catch (UnsupportedEncodingException e) {
				System.out.println("ISO-8859-1 is not a supported encoding.");
			} catch (IOException e) {
				System.out.println("Error receiving packet through server socket.");
			}
		}
	}

	/**
	 * Stores messages received in that peer's profile. Splits message around first 
	 * instance of ':' - the unikey will never have a colon. Then strips escape
	 * characters from the resulting message text and sends it all through the 
	 * communication object.
	 */
	private void storeMessage(String message) {
		int index = message.indexOf(':'); //returns index of FIRST colon
		String unikey = message.substring(0, index);
		String status = message.substring(index + 1, message.length());
		status = status.replaceAll("\\\\:", ":");
		storage.setStatus(unikey, status);		
	}
}
