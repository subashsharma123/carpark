import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class CarParkSystem {

	/**
	 * Assume there are 4 pars in the system 
	 */
	private final static int PARK_NUM = 4;

	private CarParkSystem() {
		parks = new CarPark[PARK_NUM];
		for (int iPark = 0; iPark < PARK_NUM; ++ iPark)
			parks[iPark] = new CarPark(iPark + 1);
	}
	
	// using in singleton mode
	public static CarParkSystem carParkSys = new CarParkSystem();
	
	/**
	 * The following interface will operate on user manager 
	 */
	public User findUser(int userid) {
		return userMan.findUser(userid);
	}
	
	public User addUser(int userid, String type) { 
		return userMan.addNewUser(userid, type);
	}
	
	private void drivenByInput() {

		Scanner scan = new Scanner(System.in);

		while (true) {
			clock.print();
			for (int i = 0; i < PARK_NUM; ++ i)
				parks[i].checkCars(clock.getMinute());
			
			System.out.print("Please select ([L]Login [C]Clock forward [E]Exit):");
			String str = scan.next().toLowerCase();
			if (str.startsWith("e")) 
				break;
			if (str.startsWith("c")) {	// just let the clock move forward 
				clock.tickQuicker();
				continue;
			}
			
			// then the user need to login to query the system 
			System.out.print("Please input your user id:");
			int userid = scan.nextInt();
			User user = this.findUser(userid);
			if (user == null) {
				// try to create a credit-card user here 
				String type = "";
				while (true) {
					System.out.print("Welcome, new user! Please select your identify(student faculty staff):");
					type = scan.next().toLowerCase();
					if (type.equals("student") || type.equals("faculty") || type.equals("staff"))
						break;
					System.out.println("Input wrong, please re-input!");
				}
				user = addUser(userid, type);
			}

			int sel = -1;
			while (sel != 0) { 
				System.out.println("[0] Logout!");
				System.out.println("[1] View the vacant in all parks");
				System.out.println("[2] Park my car!");
				System.out.println("[3] Consult the fee of parking!");
				System.out.print("Please select:");
				
				sel = scan.nextInt();
				int parkid = 0, parktime = 0;

				switch (sel) {
				case 0:
					System.out.println("User logout successfully!"); 
					break;
				case 1:
					for (int i = 0; i < PARK_NUM; ++ i) {
						System.out.println("Vacant space in Park " + parks[i].getParkID() + ":");
						Vector<Integer> vacants = parks[i].getVacantSpaces();
						for (Integer islot : vacants)
							System.out.print(islot + " ");
						System.out.println();
					}
					break;
				case 2:
					while (parkid <= 0 || parkid > 4) {
						System.out.print("Please select park [1-4]:");
						parkid = scan.nextInt();
					}
					while (parktime < 30) {
						System.out.print("Please input the park time(at least 30 minutes):");
						parktime = scan.nextInt();
					}
					if (! parks[parkid - 1].canPark()) 
						System.out.println("Sorry, Park " + parkid + " can not park!");
					else 
						parks[parkid - 1].park(userid, clock.getMinute(), parktime);
					break;
				case 3:
					while (parkid <= 0 || parkid > 4) {
						System.out.print("Please select park [1-4]:");
						parkid = scan.nextInt();
					}
					while (parktime < 30) {
						System.out.print("Please input the park time(at least 30 minutes):");
						parktime = scan.nextInt();
					}
					int price = parks[parkid - 1].calcPrice(parktime);
					System.out.println("The fee will be " + price + " USD.");
				}
			}
		}
		scan.close();
	}
	
	private void drivenByTestData(Vector<UserTestData> testdat) {

		while (true) {
			int curmin = clock.getMinute();
			for (int i = 0; i < PARK_NUM; ++ i) 
				parks[i].checkCars(curmin);				
			
			// Check if there is new car arrived 
			if (testdat.size() > 0 && curmin >= testdat.elementAt(0).arriveMin) {
				UserTestData head = testdat.elementAt(0);
				User user = this.findUser(head.userID);
				if (user == null)  // try to create a credit-card user here 
					user = addUser(head.userID, head.userType);
				
				Vector<Integer> vacantParks = new Vector<Integer> ();
				for (int iPark = 0; iPark < PARK_NUM; ++ iPark) {
					if (parks[iPark].canPark())
						vacantParks.add(iPark);
				}
				// we will randomly select a car-park to park 
				if (vacantParks.size() > 0) {
					int parkInd = vacantParks.elementAt(rand.nextInt(vacantParks.size()));
					parks[parkInd].park(head.userID, curmin, head.parkTime);
				} else {
					System.out.println("User id: " + head.userID + " park car failed!");
				}
				testdat.remove(0);				
			}
			
			clock.tickPerMin();
			
			// if no more car arrived and still there is not any car parked in the parks, just exit 
			if (testdat.size() > 0)
				continue;
			boolean carparked = false;
			for (int i = 0; i < PARK_NUM; ++ i) {
				if (parks[i].hasCar())
					carparked = true;
			}
			if (!carparked)
				break;
		}
	}
	
	public void simulate() {
		
		userMan.loadUsers("user.txt");
		drivenByInput();
		userMan.saveUsers("user.txt");
	}
	
	/**
	 * Test the car-park system using the data to driven it 
	 */
	public void test(String testfile) {
		// read all the test case in the file 
		Vector<UserTestData> testdat = UserTestData.loadTestData(testfile);
		Comparator<UserTestData> ct = new UserTestComp();
		Collections.sort(testdat, ct);
		userMan.loadUsers("user.txt");
		drivenByTestData(testdat);
		userMan.saveUsers("user.txt");
				
	}
	
	private UserManager userMan = new UserManager();
	private CarPark [] parks;
	private Clock clock = new Clock();		// the clock system for this car park system 
	private Random rand = new Random(47);
	
	public static void main(String args[]) {
		if (args.length == 1 && args[0].equals("test"))
			carParkSys.test("testdat.txt");
		else 
			carParkSys.simulate();
	}
}
