/*
 * Developed by Team Crush
 * Tran Situ
 * Jason Wang
 * Vishnu Venkateswaran
 * Dana Thomas
 * Arad Margalit
 * Willa Zhao
 * USC CSCI 201 Fall 2015
 */

package Backend;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class DBCommunicator implements Serializable {
	public static final long serialVersionUID = 1; 
	
	private Connection conn; 
	private String mySQLPassword; 
	private Vector <User> liveUsers; 
	private Vector <Attraction> modifiedAttractions; 
	
	public DBCommunicator () {
		liveUsers = new Vector<User> ();
		modifiedAttractions = new Vector <Attraction>(); 
		this.mySQLPassword = ""; 
		
		conn = null; 
		try {
			Class.forName("com.mysql.jdbc.Driver"); //class named Driver w/i com package, mysql package in jdbc package
			String connectionString = "jdbc:mysql://localhost/ProjectDB?user=root&password=" + mySQLPassword; 
			conn = DriverManager.getConnection(connectionString);
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println ("CNFE in DBCommunicator constructor:");
			cnfe.printStackTrace(); 
			closeDBCommunicator(); 
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator constructor:");
			sqle.printStackTrace(); 
			closeDBCommunicator(); 
		}
		
		populateModifiedAttractions(); 
	}
		
	public DBCommunicator (String mySQLPassword) {
		liveUsers = new Vector<User> ();
		modifiedAttractions = new Vector <Attraction>(); 
		
		this.mySQLPassword = mySQLPassword; 
		conn = null; 
		try {
			Class.forName("com.mysql.jdbc.Driver"); //class named Driver w/i com package, mysql package in jdbc package
			String connectionString = "jdbc:mysql://localhost/ProjectDB?user=root&password=" + mySQLPassword; 
			conn = DriverManager.getConnection(connectionString);
		}
		catch (ClassNotFoundException cnfe) {
			System.out.println ("CNFE in DBCommunicator constructor:");
			cnfe.printStackTrace(); 
			closeDBCommunicator(); 
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator constructor:");
			sqle.printStackTrace(); 
			closeDBCommunicator(); 
		}
		
		populateModifiedAttractions(); 
	}
	
	//assumes all live users have already been added immediately after they enter their preference info! 
	//no longer needed - DB will not update users' locations
	
	public void populateModifiedAttractions () {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM attraction");
			ResultSet rs =  ps.executeQuery(); 	 
			while (rs.next()) {
			   modifiedAttractions.add(getAttractionFromDB (rs.getDouble("longitude"),
			    rs.getDouble("lattitude"))); 
			}
	   }
	    catch (SQLException sqle) {	
			System.out.println ("SQLE in DBCommunicator.getAttractionFromDB:");
			sqle.printStackTrace(); 
	    }
	}
	
	private int calculateSimilarities (int [] firstUser, int [] secondUser) {
		if (firstUser.length != 5 || secondUser.length != 5) return 1; 
		
		int diff_1 = Math.abs(firstUser[0] - secondUser[0]); 
		int diff_2 = Math.abs(firstUser[1] - secondUser[1]); 
		int diff_3 = Math.abs(firstUser[2] - secondUser[2]); 
		int diff_4 = Math.abs(firstUser[3] - secondUser[3]); 
		
		return 20 - (diff_1 + diff_2 + diff_3 + diff_4 ); 
	}

	public String findUsernameOfChatPartner (String username1) {
		String myHometown = getUserHometownFromDB(username1); 
		
		//compile list of all users with same hometown
		int max = 0; 
		String runningBest = null; //stores name of runningBest candidate for matching
		for (User u: liveUsers) {
			//if hometown matches
			if (getUserHometownFromDB(u.getUsername()).equals(myHometown) && !u.getUsername().equals(username1)) {
				//calculate the similarity, if it's the best similarity we've seen, set it to
				//runningBest (the username of the currentBest match) and max (best score we've seen)
				
				if (calculateSimilarities(getUserPreferencesFromDB(u.getUsername())
						,getUserPreferencesFromDB(username1)) > max){
					max = calculateSimilarities(getUserPreferencesFromDB(u.getUsername())
							,getUserPreferencesFromDB(username1)); 
					runningBest = u.getUsername(); 
				}
			}
		}
		
		//if there is no match (no one else online or no one has the same hometown, returns null) 
		return runningBest; 
	}
	
    public void closeDBCommunicator () {
		
		try {
			if (conn != null) conn.close(); 
			
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.closeDBCommunicator:");
			sqle.printStackTrace(); 
		}
	}
	
	public void addLiveUser (User newest) {
		//checks to see if all DB necessary components are there
		if (newest == null || (newest.getUsername() == null && newest.isRegistered())) return; 
		liveUsers.add(newest); 
	}
	
	public Vector <User> getLiveUsers () {
		return liveUsers; 
	}
	
	public Vector <Attraction> getModifiedAttractions () {
		return modifiedAttractions; 
	}
	
	public void addModifiedAttraction (Attraction newest) {
		//checks to double check all DB necessary components are there
		if (newest == null || newest.getName() == null || newest.getName() == "" || 
				newest.getLocation() == null) return; 
		modifiedAttractions.add(newest); 
		
	}
	
	public void addAttractionToDB (String name, String phone, double longitude, double lattitude, String address) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO attraction (name, phone, longitude, lattitude, "
					+ "address) VALUES (?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, phone);
			ps.setDouble(3, longitude);
			ps.setDouble(4, lattitude);
			ps.setString(5, address);
		    ps.execute(); 	  
		    
		    boolean has = false; 
		    for (Attraction a: modifiedAttractions) {
		    	double [] loc = a.getLocation(); 
		    	if (loc[0] == longitude && loc[1] == lattitude) {
		    		has = true; 
		    	}
		    }
		    
		    if (!has) {
		    	Attraction a = new Attraction (name, address, longitude, lattitude); 
		    	a.setPhone(phone);
		    	addModifiedAttraction(a); 
		    }
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.addAttractionToDB:");
			sqle.printStackTrace(); 
		}
	}
	
	public Vector<String> getAttractionTestimonialsFromDB (double longitude, double lattitude) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM attraction WHERE (longitude=? AND lattitude=?)");
			ps.setDouble(1, longitude);
			ps.setDouble(2, lattitude);
			ResultSet rs =  ps.executeQuery(); 
			int attractionID = 0; 
		    if (rs.next()) {
		    	attractionID = rs.getInt("id"); 	    	
		    }
		    else return null; 
		    		    
		    PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM testimonials WHERE (id=?)");
			ps2.setDouble(1, attractionID);
			ResultSet rs2 =  ps2.executeQuery(); 	
	    	Vector <String> testimonials = new Vector<String>(); 
	     
	    	while (rs2.next()) {
		    	testimonials.add(rs2.getString("testimonial")); 
		    }
		    
	    	if (testimonials.isEmpty()) return null; 
	    	else return testimonials; 
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.getAttractionFromDB:");
			sqle.printStackTrace(); 
		}
		
		return null; 
	}
	
	public Attraction getAttractionFromDB (double longitude, double lattitude) {
		Attraction a = null;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM attraction WHERE (longitude=? AND lattitude=?)");
			ps.setDouble(1, longitude);
			ps.setDouble(2, lattitude);
			ResultSet rs =  ps.executeQuery(); 	 
		    if (rs.next()) {
		    	a = new Attraction (rs.getString("name"), rs.getString("address"), longitude, lattitude); 
		    	a.setAvgRating(rs.getInt("avgrating"));
		    	if (getAttractionTestimonialsFromDB(longitude, lattitude) != null) {
		    		a.setTestimonials(getAttractionTestimonialsFromDB(longitude, lattitude));
		    	}
		    }
		    return a;
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.getAttractionFromDB:");
			sqle.printStackTrace(); 
		}
		
		return a;
	}
	
	
	
	public void addUserToDB (String fname, String lname, String info, String username, String password) {
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT INTO userinfo (firstname, lastname, cityinfo, username, "
					+ "password) VALUES (?,?,?,?,?)");
			ps.setString(1, fname);
			ps.setString(2, lname);
			ps.setString(3, info);
			ps.setString(4, username);
			ps.setString(5, password);
		    ps.execute(); 	
		    
		    addLiveUser(new User (true, fname, lname, info, username)); 
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.addUserToDB:");
			sqle.printStackTrace(); 
		}
		
	}
	
	  	public void addUserPreferencesToDB (String username, int pref1, int pref2, int pref3, int pref4, int pref5) {
		if (!username.equals("") && !username.equals(null)) {
			try {
			    PreparedStatement ps2 = conn.prepareStatement("INSERT INTO userinterest (username, preference_1, "
			    		+ "preference_2 , preference_3, preference_4, preference_5) VALUES (?,?,?,?,?,?) "); 
				ps2.setString(1, username);
				ps2.setDouble(2, pref1);
				ps2.setDouble(3, pref2);
				ps2.setDouble(4, pref3);
				ps2.setDouble(5, pref4);
				ps2.setDouble(6, pref5);
				ps2.execute(); 
			}
			catch (SQLException sqle) {
				System.out.println ("SQLE in DBCommunicator.addUserPreferencesToDB:");
				sqle.printStackTrace(); 
			}
		}
	}
	
	public int [] getUserPreferencesFromDB (String username) {
		int [] result = new int[5]; 
		if (!username.equals("") && !username.equals(null)) {
			try {
			    PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM userinterest WHERE username=?"); 
				ps2.setString(1, username);
				ResultSet rs = ps2.executeQuery(); 
				if (rs.next()) {
					result[0] = rs.getInt("preference_1"); 
					result[1] = rs.getInt("preference_2"); 
					result[2] = rs.getInt("preference_3"); 
					result[3] = rs.getInt("preference_4"); 
					result[4] = rs.getInt("preference_5"); 
				}
			}
			catch (SQLException sqle) {
				System.out.println ("SQLE in DBCommunicator.getUserPreferencesFromDB:");
				sqle.printStackTrace(); 
			}
		}
		
		return result; 
	}
	
	private String getUserHometownFromDB (String username) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM userinfo WHERE username=? ");
			ps.setString(1, username);
			ResultSet rs =  ps.executeQuery(); 	 
		    if (rs.next()) {  	
		    	return rs.getString("cityinfo"); 
		    }
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.getUserHometownFromDB:");
			sqle.printStackTrace(); 
		}
		return null; 
	}
	
	public User getUserFromDB (String username, String password) {
		try {
			User u = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM userinfo WHERE (username=? AND password=?)");
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs =  ps.executeQuery(); 	 
		    if (rs.next()) {
		    	u = new User (true, rs.getString("firstname"), rs.getString("lastname"), rs.getDouble("longitude"), rs.getDouble("lattitude"), rs.getString("cityinfo"), rs.getString("username"));
		    	/*return new User (true, rs.getString("firstname"), rs.getString("lastname"), 
		    		 rs.getString("cityinfo"));*/ 
		    }
		    int[] preferences = getUserPreferencesFromDB(username);
		    u.setUserPreferences(preferences[0], preferences[1], preferences[2], preferences[3], preferences[4]);
		    //System.out.println("getUser: " + u);
		    return u;
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.getUserFromDB:");
			sqle.printStackTrace(); 
		}
		return null; 
	}
	
	public User getUserFromDBNoPassword (String username) {
		try {
			User u = null;
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM userinfo WHERE username=?");
			ps.setString(1, username);
			ResultSet rs =  ps.executeQuery(); 	 
		    if (rs.next()) {
		    	u = new User (true, rs.getString("firstname"), rs.getString("lastname"), rs.getDouble("longitude"), rs.getDouble("lattitude"), rs.getString("cityinfo"), rs.getString("username"));
		    }
		    int[] preferences = getUserPreferencesFromDB(username);
		    u.setUserPreferences(preferences[0], preferences[1], preferences[2], preferences[3], preferences[4]);
		    return u;
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.getUserFromDB:");
			sqle.printStackTrace(); 
		}
		return null; 
	}
	
	public boolean isValidLoginInfo (String username, String password) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM userinfo WHERE (username=? AND password=?)");
			ps.setString(1, username);
			ps.setString(2, password);
		    ResultSet rs =  ps.executeQuery(); 	 
		    if (rs.next()) return true; 
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.isValidLoginInfo:");
			sqle.printStackTrace(); 
		}
		
		return false; 
	}
	
	public boolean isUsernameAvailable (String username) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM userinfo WHERE (username=?)");
			ps.setString(1, username);
		    ResultSet rs =  ps.executeQuery(); 	 
		    if (rs.next()) return false; 
		    else return true; 
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.isValidLoginInfo:");
			sqle.printStackTrace(); 
		}
		
		return false; 
	}
	
	
	public void addAttractionRating (double longitude, double lattitude, int rating) {
		
		int attractionID = 0;
		try {
			//get attraction ID of location entry with given longitude and lattitude
		    PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM attraction WHERE (longitude=? AND lattitude=?)"); 
			ps2.setDouble(1, longitude);
			ps2.setDouble(2, lattitude);
			ResultSet rs = ps2.executeQuery(); 
			if (rs.next()) attractionID = rs.getInt("id"); 
			else return; 
			
			//update ratings table 
		    PreparedStatement ps3 = conn.prepareStatement("INSERT INTO ratings (rating, id) VALUES (?, ?)");
		    ps3.setInt(1, rating);
		    ps3.setInt(2, attractionID);
		    ps3.execute(); 
		    
		    for (Attraction a : modifiedAttractions) {
		    	double [] loc = a.getLocation(); 
		    	if (loc[0] == longitude && loc[1] == lattitude) {
		    		a.addRating(rating);
		    	}
		    }
		    
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.addAttractionRating:");
			sqle.printStackTrace(); 
		}
		
		updateAverageAttractionRatingInDB (attractionID, longitude, lattitude); 
	}
	
	private void updateAverageAttractionRatingInDB (int attractionID, double longitude, double lattitude) {
		int totalRatings = 0; 
		int sum = 0; 
		try {
		    PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM ratings WHERE (id=?)"); 
			ps2.setInt(1, attractionID);
			ResultSet rs = ps2.executeQuery(); 
			while (rs.next()) {
				sum += rs.getInt("rating"); 
				totalRatings++; 
			}
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.updateAverageAttractionRating:");
			sqle.printStackTrace(); 
		}
		
		if (totalRatings != 0) {
			try {
			    PreparedStatement ps2 = conn.prepareStatement("UPDATE attraction SET avgRating=? WHERE (id=?)"); 
			    ps2.setInt(1, (int) (sum/totalRatings));
			    ps2.setInt(2, attractionID);
				ps2.execute(); 
				
				 for (Attraction a : modifiedAttractions) {
				    	double [] loc = a.getLocation(); 
				    	if (loc[0] == longitude && loc[1] == lattitude) {
				    		a.setAvgRating((int) (sum/totalRatings));
				    	}
				    }
			}
			catch (SQLException sqle) {
				System.out.println ("SQLE in DBCommunicator.updateAverageLocationRating:");
				sqle.printStackTrace(); 
			}
		}
	}
	
	public void addAttractionKeyword (double longitude, double lattitude, String keyword) {
		try {
			//get attraction ID of location entry with given longitude and lattitude
		    PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM attraction WHERE (longitude=? AND lattitude=?)"); 
			ps2.setDouble(1, longitude);
			ps2.setDouble(2, lattitude);
			ResultSet rs = ps2.executeQuery(); 
			int attractionID ; 
			if (!rs.next()) return; 
			else attractionID = rs.getInt("id"); 
			
			//update ratings table 
		    PreparedStatement ps3 = conn.prepareStatement("INSERT INTO keywords (keywords, id) VALUES (?, ?)");
			ps3.setString(1, keyword);
			ps3.setInt(2, attractionID);
		    ps3.execute(); 
		    
		}
		catch (SQLException sqle) {
			System.out.println ("SQLE in DBCommunicator.addAttractionKeyword:");
			sqle.printStackTrace(); 
		}
	}
	
	public void addAttractionKeyword (double longitude, double lattitude, String [] keyword) {
		for (String s: keyword) {
			addAttractionKeyword (longitude, lattitude, s); 
		}
	}
	
	public void addAttractionTestimonial (double longitude, double lattitude, String username, String testimonial) {
		try {
			//get attraction ID of foreign key for given location w/ lattitude and longitude
		    PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM attraction WHERE (longitude=? AND lattitude=?)"); 
			ps2.setDouble(1, longitude);
			ps2.setDouble(2, lattitude);
			ResultSet rs = ps2.executeQuery(); 
			int attractionID; 
			if (rs.next()) attractionID = rs.getInt("id"); 
			else return; 
			
			//get userID of foreign key for given location w/ fname and lname
		    PreparedStatement ps4 = conn.prepareStatement("SELECT * FROM userinfo WHERE (username=?)"); 
			ps4.setString(1, username);
			ResultSet rs2 = ps4.executeQuery(); 
			int userID = 0; 
			if (rs2.next()) userID = rs2.getInt("userid"); 
			else return; 
			
			//update ratings table 
		    PreparedStatement ps3 = conn.prepareStatement("INSERT INTO testimonials (testimonial, id, userid) VALUES (?,?,?)");
			ps3.setString(1, testimonial);
			ps3.setInt(2, attractionID);
			ps3.setInt(3, userID);
		    ps3.execute(); 
		    
		    for (Attraction a : modifiedAttractions) {
		    	double [] loc = a.getLocation(); 
		    	if (loc[0] == longitude && loc[1] == lattitude) {
		    		a.addTestimonial(testimonial);
		    	}
		    }
		}
		catch (SQLException sqle) {			
			System.out.println ("SQLE in DBCommunicator.addLocationTestimonial:");
			sqle.printStackTrace(); 
		}
	}
	
	
}
