/**
 * 
 * This class is used for abstraction of park space usage 
 * When recording the park space usage information, we need to remember the start time of using that park space,
 * we will ignore the second information
 */
public class ParkSpaceUsage {

	public ParkSpaceUsage(int userID, int startMinute, int parktime) {
		this.userID = userID;
		this.startMinute = startMinute;
		this.parkTime = parktime;
		this.informed = false;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public int getParkTime() {
		return parkTime;
	}
	
	public int getLeaveTime() { 
		return startMinute + parkTime;
	}
	
	public boolean getInformed() { 
		return informed;
	}
	
	public void setInformed() {
		informed = true;
	}
	
	private int userID;
	private int startMinute;
	private int parkTime;
	private boolean informed;	// have already informed when less than 10 minutes to leave 
	
}
