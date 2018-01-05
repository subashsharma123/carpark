import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class UserManager {
	/**
	 * Load user information from saved document 
	 */
	public void loadUsers(String userfile) {
		File file = new File(userfile);
		allUsers.clear();
        try {
        	BufferedReader reader = new BufferedReader(new FileReader(file));
            String tmpString = null;
            while ((tmpString = reader.readLine()) != null) {
            	String[] elems = tmpString.split("\t");
            	if (elems.length != 3) {	// Invalid line, just ignore it
            		continue;
            	}

            	int userid = Integer.parseInt(elems[0]);
            	boolean permanent = (Integer.parseInt(elems[1]) == 1);
            	String usertype = elems[2];
            	allUsers.add(new User(userid, permanent, usertype));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Save user information into file to persistence
	 */
	public void saveUsers(String userfile) {
		File file = new File(userfile);
        try {
        	BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        	for (User user : allUsers) {
        		String tmpUser = "" + user.getUserID() + "\t" + (user.getPermanent() ? "1" : "0") + "\t" + user.getUserType() + "\n";
        		writer.write(tmpUser);
        	}
        	writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }		
	}
	
	public User findUser(int userid) {
		for (User user : allUsers) {
			if (user.getUserID() == userid)
				return user;
		}
		return null;
	}
	
	public User addNewUser(int userid, String type) { 
		// The newly added user is not permanent 
		if (findUser(userid) != null)	// there is already such user 
			return null;
		User user = new User(userid, false, type);
		allUsers.add(user);
		return user;
	}
	
	private Vector<User> allUsers = new Vector<User>();
}
