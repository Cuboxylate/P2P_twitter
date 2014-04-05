/**
 * Class to store profiles of all users, connecting the profiles to the
 * client and server threads. Also fills the role of printing out the 
 * peer statuses.
 *
 * Sebastian Dunn 2013
 */

import java.io.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Date;
import java.util.ArrayList;

public class Communication {
	private HashMap<String, Profile> peers;
	private ArrayList<String> unikeys;
	private ArrayList<String> addresses;
	private final String myKey;

	/**
	 * Constuctor for Communication. Makes a hashmap and populates it
	 * with profiles. Profiles are constructed from the properties
	 * file and are hashed using their unikey as a key. Stores unikeys
	 * and ip addresses as their own arrays so they can be accessed and
	 * iterated through more easily.
	 */
	public Communication(String myKey) {
		peers = new HashMap<String, Profile>(); 
		unikeys = new ArrayList<String>();
		addresses = new ArrayList<String>();
		Properties userData = new Properties(); //has profile data
		this.myKey = myKey;

		try {
			userData.load(new FileInputStream("./participants.properties")); 
		} catch (IOException e) {
			System.out.println("Exception in loading properties' input stream.");
		}

		String[] usernames = userData.getProperty("participants").split(",");

		for (String username: usernames) {
			String unikey = userData.getProperty(username + ".unikey");
			String pseudo = userData.getProperty(username + ".pseudo");
			String ip = userData.getProperty(username + ".ip");
			//saves a boolean identifying this profile as the current user's profile
			boolean isMe = unikey.equals(myKey);

			//System.out.println("Creating user: " + unikey + ", " + pseudo + ", " + ip + ", " + isMe);//Debugging statement
			unikeys.add(unikey);
			addresses.add(ip);
			peers.put(unikey, new Profile(unikey, pseudo, ip, isMe));
		}
	}

	/**
	 * Returns the ip addresses to the client thread, so it can iterate
	 * through them easily to send out the status messages.
	 */
	public ArrayList<String> getIPAddresses() {
		return addresses;
	}

	/**
	 * Returns the current user's status to the client thread for sending
	 * to peers.
	 */
	public String getMyStatus() {
		return peers.get(myKey).getStatus();
	}

	/**
	 * Set the status of a given unikey user to the given string
	 */
	public void setStatus(String unikey, String status) {
		Profile sender = peers.get(unikey);
		//Stops if the profile doesn't exist, just in case
		if (sender == null) { return; }

		//System.out.println("Setting status of " + unikey + " to: " + status);//Debug statement
		sender.setStatus(status);
	}

	/**
	 * Prints all of the statuses of all of the peers. Checks for them being uninitialised 
	 * or idle. Prints out to stdout in the specified format.
	 */
	public void printStatuses() {
		System.out.println("### P2P tweets ###");
		for (String unikey: unikeys) {
			Profile current = peers.get(unikey);
			String status = current.getStatus();

			//If there is no status yet, peer is uninitialised. Handle in
			//separate function.
			if (status == null) {
				printUninitialised(current);
				continue;
			}

			//Check if the peer is idle. Move to next peer if idle for more than
			//30 seconds. If idle for between 10 and 30 seconds, handle in another
			//function.
			long delay = current.timeSinceStatus();
			if (delay >= 30000) { 
				continue; 
			}
			else if (delay >= 10000) { 
				printIdle(current); 
				continue;
			}

			//If initialised and active, construct and print output, taking into
			//consideration whether the current profile is me or not.
			String output = "# " + current.getPseudo() + " (";
			if (current.isItMe()) { output = output.concat("myself): " + status); }
			else { output = output.concat(unikey + "): " + status); }

			System.out.println(output);
		}

		System.out.println("### End tweets ###\n");
	}

	/**
	 * Function to print status is peer is uninitialised
	 */
	private void printUninitialised(Profile current) {
		System.out.println("# ["+current.getPseudo()+" ("+current.getUnikey()+"): not yet initialized]");
	}

	/**
	 * Function to print status is peer is idle
	 */
	private void printIdle(Profile current) {
		System.out.println("# ["+current.getPseudo()+" ("+current.getUnikey()+"): idle]");	
	}

}