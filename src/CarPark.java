import java.util.Random;
import java.util.Vector;

public class CarPark {
	
	/**
	 * Assume there are 64 park space in the car park
	 */
	public static int PARK_SPACE_NUMBER = 32;
	/**
	 * Assume the park fee for 1st 30 minutes is free, and for the 2nd-12th 30 minutes is 1usd each, and for the 13th-36th is 2usd 
	 * each, for the 37th-48th is 3usd each. Still, if the park time exceed one day, there will be 10usd per day extra charge.
	 */
	public static int [] HALF_HOUR_FEE = {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 
		                                  2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
		                                  2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
		                                  3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
	public static int WHOLE_DAY_EXTRA_FEE = 10;
	
	/**
	 * If the car park is roof-based, will charge 3usd when the card exit the park  
	 */
	public static int ROOF_BASED_FEE = 3;
	
	public static int INFORM_AHEAD_MINUTES = 10;
	
	public static Random rand = new Random();
	
	CarPark(int parkID) {
		this.parkID = parkID;
		roofBased = rand.nextBoolean();	
	}
	
	/**
	 * Check if the car arrive at given time can park in this car-park
	 * This is just used for suggestion, the car is not parked when call this 
	 */
	public boolean canPark() { 
		for (int i = 0; i < PARK_SPACE_NUMBER; ++ i) {
			if (spaceUsage[i] == null) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Park the car in this car-park 
	 */
	public void park(int userid, int curMinute, int parktime) { 
		int iSlot = 0;
		while (iSlot < PARK_SPACE_NUMBER && spaceUsage[iSlot] != null) {
			iSlot ++;
		}
		
		spaceUsage[iSlot] = new ParkSpaceUsage(userid, curMinute, parktime);
		User curUser = CarParkSystem.carParkSys.findUser(userid);
		curUser.notifyUser("Enter park " + parkID + " " + (roofBased ? "with roof." : "without roof."));
	}
	
	/**
	 * Check all the cars parked here, and inform the user if less than 10 minutes to leave. Also will let the car exit the park when time is up.
	 */
	public void checkCars(int curMinute) { 
		for (int i = 0; i < PARK_SPACE_NUMBER; ++ i) {
			if (spaceUsage[i] == null) {
				continue;
			}
			int endtime = spaceUsage[i].getLeaveTime();
			if (endtime - curMinute > INFORM_AHEAD_MINUTES) {
				continue;
			}
			
			User curUser = CarParkSystem.carParkSys.findUser(spaceUsage[i].getUserID());
			// check if the car should exit now 
			if (endtime <= curMinute) {
				int price = calcPrice(spaceUsage[i].getParkTime());
				curUser.notifyUser("Leave park " + parkID + " and pay " + price + " USD.");
				spaceUsage[i] = null;
			} else {
				// check if the park should inform him the time is closing 
				if (! spaceUsage[i].getInformed()) {
					curUser.notifyUser(" Should get your car out of park " + parkID + " in 10 minutes!");
					spaceUsage[i].setInformed();
				}
			}
		}
	}
	
	public int getParkID() {
		return parkID;
	}
	
	public boolean isRoofBased() {
		return roofBased;
	}
	
	/**
	 * Fee calculate when the car exit the park 
	 */
	public int calcPrice(int parktime) {
		int total = 0; 
		int halfhrs = (parktime + 29) / 30;
		
		// the fee for a whole day  
		int feeWholeDay = 0;
		for (int i = 0; i < 48; ++ i) 
			feeWholeDay += HALF_HOUR_FEE[i];
		feeWholeDay += WHOLE_DAY_EXTRA_FEE;
		
		while (halfhrs > 48) {
			total += feeWholeDay;
			halfhrs -= 48;
		}
		
		for (int i = 0; i < halfhrs; ++ i)
			total += HALF_HOUR_FEE[i];
		
		if (roofBased)
			total += ROOF_BASED_FEE;
		return total;
	}
	
	/**
	 * Get the vacant space in current park 
	 */
	public Vector<Integer> getVacantSpaces() {
		Vector<Integer> vacantList = new Vector<Integer> ();
		for (int i = 0; i < PARK_SPACE_NUMBER; ++ i) {
			if (spaceUsage[i] == null) 
				vacantList.add(i + 1);
		}
		return vacantList;
	}
	
	public boolean hasCar() {
		for (int i = 0; i < PARK_SPACE_NUMBER; ++ i) {
			if (spaceUsage[i] != null) 
				return true;
		}		
		return false;
	}
	
	private int parkID;
	private boolean roofBased;
	
	private ParkSpaceUsage [] spaceUsage= new ParkSpaceUsage[PARK_SPACE_NUMBER];
}
