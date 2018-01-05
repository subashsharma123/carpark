import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Vector;

/**
 * This class stores the test data of arriving cars 
 */

public class UserTestData {
	
	public UserTestData(int userID, String type, int parktime, int arriveday, int arrivehr, int arrivemin) {
		this.userID = userID;
		this.userType = type;
		this.arriveMin = Clock.getTimeStamp(arriveday, arrivehr, arrivemin);
		this.parkTime = parktime;
	}
	
	public int userID;
	public String userType;
	public int arriveMin;
	public int parkTime;
	
	public static Vector<UserTestData> loadTestData(String testfile) {
		File file = new File(testfile);
		Vector<UserTestData> datas = new Vector<UserTestData> ();
        try {
        	BufferedReader reader = new BufferedReader(new FileReader(file));
            String tmpString = null;
            while ((tmpString = reader.readLine()) != null) {
            	String[] elems = tmpString.split("\t");
            	if (elems.length != 6) {	// Invalid line, just ignore it
            		continue;
            	}

            	int userid = Integer.parseInt(elems[0]);
            	String type = elems[1].toLowerCase();
            	if (! type.equals("student") && ! type.equals("faculty") && ! type.equals("staff"))
            		type = "student";	// Give a default type 
            	int parktime = Integer.parseInt(elems[2]);
            	int arrday = Integer.parseInt(elems[3]);
            	int arrhr = Integer.parseInt(elems[4]);
            	int arrmin = Integer.parseInt(elems[5]);
            	datas.add(new UserTestData(userid, type, parktime, arrday, arrhr, arrmin));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datas;
	}	
}

class UserTestComp implements Comparator<UserTestData>
{
	public int compare(UserTestData dat1, UserTestData dat2) {
		if (dat1.arriveMin < dat2.arriveMin)
			return -1;
		if (dat1.arriveMin > dat2.arriveMin)
			return 1;
		return 0;
	}
}
