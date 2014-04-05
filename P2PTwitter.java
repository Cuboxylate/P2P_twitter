/**
 * Class to store main and set up server and client threads.
 * When running program, supply a string key as args[0] to
 * identify the local user.
 *
 * Sebastian Dunn 2013
 */

import java.net.*;
import java.io.*;

public class P2PTwitter {
	
	public static void main (String[] args) {
		int port = 7014;

		//Builds a Communication object for all threads to use. Acts as
		//shared memory for communication between threads.
		Communication storage = new Communication(args[0]);

		//And a datagram socket for the client and server to share
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.out.println("Error creating datagram socket.");
			e.printStackTrace();
		}

		//This thread continually asks for the user's status and stores it
		//in their profile through the communication object
		Thread updater = new Thread(new StatusUpdater(args[0], storage));
		updater.start();

		//Server thread takes incoming requests and stores the remote user's
		//statuses in their profiles through the communication object
		Thread server = new Thread(new P2PServer(storage, socket));
		server.start();

		//Client periodically sends the user's current status to all peers in
		//the network.
		Thread client = new Thread(new P2PClient(storage, port, args[0], socket));
		client.start();
	}
}