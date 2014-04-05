/**
 * Simple storage class to hold information about a peer.
 *
 * Sebastian Dunn 2013
 */

import java.util.Date;

public class Profile {
	final private String unikey;
	final private String pseudo;
	final private String ip;
	final boolean isMe;
	private String status;
	private Date statusTime;

	/**
	 * Constructor saves all data about peer - it is final.
	 */
	public Profile(String unikey, String pseudo, String ip, boolean isMe){
		this.unikey = unikey;
		this.pseudo = pseudo;
		this.ip = ip;
		this.isMe = isMe;
	}

	/**
	 * Saves a new date object when status is set so the communication object
	 * can check if the status is idle when it prints them out.
	 */
	public void setStatus(String status) {
		this.status = status;
		statusTime = new Date();
	}

	public String getStatus() {
		return status;
	}

	public String getPseudo() {
		return pseudo;
	}

	public boolean isItMe() {
		return isMe;
	}

	public String getUnikey() {
		return unikey;
	}

	/**
	 * Returns the number of milliseconds since the last status was received. 
	 * Returns -1 is no messages received yet.
	 */
	public long timeSinceStatus() {
		if (statusTime == null) { return -1; }
		Date now = new Date();
		return now.getTime() - statusTime.getTime();
	}
}