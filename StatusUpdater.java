/**
 * Class that loops, asking the user to input their status and saving
 * that status into their profile. Calls on the Communication object
 * to print out all the peers' statuses each time the user enters a new
 * status.
 *
 * Sebastian Dunn 2013
 */

import java.io.*;

public class StatusUpdater implements Runnable {
	final private String unikey;
	private Communication storage;

	/**
	 * Simple constructor links this object to the shared
	 * communications object.
	 */
	public StatusUpdater (String unikey, Communication storage) {
		this.unikey = unikey;
		this.storage = storage;
	}

	/**
	 * Run is called when thread is executed. Loops endlessly asking for a status
	 * and saving it to the user's profile, then prints out the statuses of all
	 * the peers.
	 */
	public void run() {
		BufferedReader input;

		try {
			input = new BufferedReader(new InputStreamReader(System.in));	
		} catch (Exception e) {
			System.out.println("Error setting up input stream reader.");
			return;
		}

		while (true) {
			System.out.print("Status: ");
			String status = null;

			try {
				status = input.readLine(); //Blocks here waiting for line
			} catch (IOException e) {
				System.out.println("Error reading through BufferedReader.");
			}

			//Checks that the status is between 1 and 140 characters long
			if (!checkLength(status)) { continue; }

			storage.setStatus(unikey, status);
			storage.printStatuses();
		}
	}

	/**
	 * Helper function to check that statuses are the correct length
	 */
	private boolean checkLength(String status) {
		if (status.length() > 140) {
			System.out.println("Status is too long, 140 characters max. Retry.");
			return false;
		}
		if (status.length() == 0) {
			System.out.println("Status is empty. Retry.");
			return false;
		}
		return true;
	}
}