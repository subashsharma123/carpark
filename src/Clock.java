/**
 * 
 * This class will simulate the clock system 
 *
 */
public class Clock {

	public static int MINUTES_PER_DAY = 1440;
	public static int MINUTES_PER_HOUR = 60;
	
	/**
	 * Calculate the time-stamp by day and the minute in that day 
	 */
	public static int getTimeStamp(int day, int hour, int minute) {
		return day * MINUTES_PER_DAY + hour * MINUTES_PER_HOUR + minute;
	}
	
	public Clock() {
		this.minute = 0;
	}
	
	public void tickPerMin() {
		minute ++; 
	}
	
	public void tickQuicker() {
		minute += 5;
	}
	
	public void print() {
		int timestamp = minute;
		int day = timestamp / MINUTES_PER_DAY;
		timestamp %= MINUTES_PER_DAY;
		int hour = timestamp / MINUTES_PER_HOUR;
		timestamp %= MINUTES_PER_HOUR;
		
		String daymsg = "" + day + "D";
		if (day < 10) {
			daymsg = "0" + daymsg;
		}
		String hourmsg = "" + hour + "H";
		if (hour < 10) {
			hourmsg = "0" + hourmsg;
		}
		String minmsg = "" + timestamp + "M";
		if (timestamp < 10) {
			minmsg = "0" + minmsg;
		}
		System.out.println("Current time is: " + daymsg + "-" + hourmsg + "-" + minmsg);
	}
	
	int getMinute() {
		return minute;
	}
	
	private int minute;

}
