/**
 * 
 * This class is used for abstraction of the car owners
 *
 */

public class User {
	
	public User(int userID, boolean permanent, String usertype) {
		this.userID = userID;
		this.permanent = permanent;
		this.userType = usertype;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public boolean getPermanent() {
		return permanent;
	}
	
	public String getUserType() {
		return userType;
	}
	
	public void notifyUser(String message) { 
		System.out.println("[User:" + userID + "] " + message);
	}
	
	private int userID;
	private boolean permanent;
	private String userType;
	
}
