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

package Networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import Backend.Attraction;
import Backend.DBCommunicator;
import Backend.User;

public class Server {
	private int port;
	//-----------Vectors-------------
	private Vector<ClientThread>	cts;
	private Vector<String> 			usernames;
	//---------Network Items---------
	private ServerSocket 	ss;
	private DBCommunicator 	dbc;
	//------------Maps---------------
	private Map<String, ClientThread> 	usernameClientThreadMap;
	private Map<String, String> 		partnerMap;
//------------------------------------------------//
//Constructor
	public Server(int port) {
		//-----Instantiate all private variables above-----//
		dbc = new DBCommunicator();
		ss = null;
		cts = new Vector<ClientThread>();
		usernames = new Vector<String>();
		//-----Instantiate Maps---------------------------//
		usernameClientThreadMap = new HashMap<String, ClientThread>();
		partnerMap = new HashMap<String, String>();
//----------------------------------------------------------------
		/* Here's how the server works. BUCKLE UP BOYS AND GIRLS
		 * Bind to the port (5555) should be the default, for what it's worth
		 * while (true) --> accept new connections
		 * IMMEDIATELY block with a buffered reader br.readline()
		 * ==>store out the username ("") means a guest user
		 * kick of a clientThread and pass in the username to associate them
		 * and just for kicks, map the username to its clientThread
		 */
		try {
			ss = new ServerSocket (port);
			System.out.println("Server is online and bound to port: " + port);
			//Continue to accept connections forever
			while (true){
				Socket s = ss.accept();
				System.out.println("Accepted connection from: " + s.getInetAddress() + ":" + s.getPort());
				
				ClientThread ct = new ClientThread(s, this, dbc);
				//Add the client thread to our collection and then kick off the thread
				cts.add(ct);
				ct.start();
			}
//----------------------------------------------------------------
		} catch (IOException ioe) {
			System.out.println("IOException in Server: " + ioe.getMessage());
		} finally {
			if (ss != null){
				try {
					ss.close();
					dbc.closeDBCommunicator();
				} catch (IOException ioe){
					System.out.println("IOE in closing server socket : " + ioe.getMessage());
				}
			}
		}
	}

	public static void main (String args[]){
		new Server(5555);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port){
		this.port = port;
	}

	public void addnewuserPreference(String userName, int outdoor, int nightlife, int hotel, int shopping, int food){
		this.dbc.addUserPreferencesToDB(userName, outdoor, nightlife, hotel, shopping, food);
	}
	
    public void addnewUser(String fname, String lname, String info, String userName, String passWord){
        this.dbc.addUserToDB(fname, lname, info, userName, passWord);
    }
	
	public void addRatingData(double longitude, double lattitude, int rating){
		this.dbc.addAttractionRating (longitude, lattitude, rating);
	}

	public void addTestmonialData(double longitude, double lattitude, String comment, String username){
	// database function not ready yet		
		this.dbc.addAttractionTestimonial(longitude, lattitude, username, comment);

	}
	
	// get average rating
	public int getAverageRating(String longitude, String lattitude) {
		Attraction A = this.dbc.getAttractionFromDB(Double.parseDouble(longitude), Double.parseDouble(lattitude));
		return A.getRating();
		
	}
	
	public void updateServerPack(DBCommunicator dbc){
		System.out.println("Updating server pack");
		this.dbc = dbc;
	}
	
	// Willa
	public void addModifiedAttractions(String name, String phone, String longitude, String lattitude, String address){
		this.dbc.addAttractionToDB(name, phone, Double.parseDouble(longitude), Double.parseDouble(lattitude), address);
	}
	
	public String preMessage(String in){
		return in.substring(0, in.indexOf(":"));
	}
	
	public String postMessage(String in){
		return in.substring(in.indexOf(":")+2, in.length());
	}
	
	public boolean checkCredentials(String username, String password, ClientThread ct) {
		if(dbc.isValidLoginInfo(username, password) == true) {
			
			User u = dbc.getUserFromDB(username, password);
			if (u.getUserPreferences() != null) {
				ct.sendMessage("USER_INFO: " + u.getFirstName() + "|" + u.getLastName() + "|" + u.getCityInfo() + "|" + u.getLocation()[0] + "|" + u.getLocation()[1] + "|"
						+ u.getUserPreferences().get(0) + "|"+ u.getUserPreferences().get(1)+ "|" + u.getUserPreferences().get(2)+ "|" + u.getUserPreferences().get(3)
						+ "|" + u.getUserPreferences().get(4) + "|" + username);
			}
			else {
				ct.sendMessage("USER_INFO2: " + u.getFirstName() + "|" + u.getLastName() + "|" + u.getCityInfo() + "|" + u.getLocation()[0] + "|" + u.getLocation()[1] + "|" + username);
			}
			return true;
		}
		else {
			return false;
		}
	}

	public Boolean checkUsername(String username) {
		if (dbc.isUsernameAvailable(username)) {
			return true;
		}
		else return false;
	}
	
	public Boolean isUserLoggedIn(String username) {
		for (int i = 0; i < usernames.size(); i++) {
			if (usernames.get(i).equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	//-----------------ChatServer Portion----------------------//

	public void sendMessagetoAllClients(String message, ClientThread clientThread) {
		for (ClientThread st : cts) {
			if (st != clientThread) {
				st.sendMessage(message);
			}
		}
	}

	
	public void addUsername(String username, ClientThread clientThread) {
		usernames.add(username);
		clientThread.setUsername(username);
		System.out.println("Username of connected client is: " + username);
		//Add this to the map of usernames and clientThreads
		usernameClientThreadMap.put(username, clientThread);
	}
	
	public void removeUser(String username) {
		System.out.println(username + " has disconnected.");
		usernames.remove(username);
		int x = -1;
		for (int i = 0; i < cts.size(); i++) {
			if (!cts.get(i).isAlive()) {
				x = i;
			}
		}
		if (x != -1) {
			cts.remove(x);
		}	
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
	    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ) {
	            return (o2.getValue()).compareTo( o1.getValue() ); //low to high?
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}
	
	public String matchingAlgorithm(String username, String city, ClientThread ct) {
		//Matches username with a live user whose hometown is the same as username's current location
		//If there are multiple then the local with the lowest "score" as defined by differences in preferences is chosen
		//Chat threads are created for both users(?) but the recipient has the option to accept/decline an invite to chat
		User currentUser = dbc.getUserFromDBNoPassword(username);
		Vector<Integer> currentPreferences = currentUser.getUserPreferences();
		Map<String, Integer> scores = new HashMap<String, Integer>();
		for (int i = 0; i < usernames.size(); i++) {
			if (!usernames.get(i).equals(username)) {
				User u = dbc.getUserFromDBNoPassword(usernames.get(i));
				String cityAndState = u.getCityInfo();
				String[] split = cityAndState.split(",");
				String uCity = split[0];
				if (city.contains(uCity)) {
					Vector<Integer> uPreferences = u.getUserPreferences();
					int score = 0;
					for (int j = 0; j < 5; j++) {
						score += Math.abs(currentPreferences.get(j) - uPreferences.get(j));
					}
					scores.put(u.getUsername(), score);
				}
			}
		}
		if (scores.isEmpty()) {
			return "No match";
		}
		else {
			Map<String, Integer> scoresSorted = sortByValue(scores);
			Iterator<String> iterator = scoresSorted.keySet().iterator();
			Vector<String> strings = new Vector<String>();
			while(iterator.hasNext()) {
				String s = iterator.next();
				
				int score = scoresSorted.get(s);
				int scorePercent = 100-(5*score);
				s += "|" + scorePercent;
				strings.add(s);
				
			}
			if (!strings.isEmpty()) {
				return strings.get(strings.size()-1);
			}
			else {
				return "Error";
			}
		}	
	}
	
	public Attraction getAttraction(String latitude, String longitude) {
		Vector<Attraction> modifiedAttractions = dbc.getModifiedAttractions();

		Attraction a = null;
		for (int i = 0; i < modifiedAttractions.size(); i++) {
			if (modifiedAttractions.get(i).getLocation()[0] == Double.parseDouble(longitude) && modifiedAttractions.get(i).getLocation()[1] == Double.parseDouble(latitude)) {
				a = modifiedAttractions.get(i);
			}
		}
		return a;
	}
	
	public Boolean checkIfAlreadyModified(String latitude, String longitude) {
		if (dbc.getAttractionFromDB(Double.parseDouble(longitude), Double.parseDouble(latitude)) != null) {
			return true;
		}
		else return false;
 	}
	
	//-------------------------------------------------------------------
	public void disconnectChat(String username){
		String partnerName = partnerMap.get(username);
		usernameClientThreadMap.get(partnerName).sendMessage("DISCONNECTCHAT: ");
		partnerMap.remove(partnerMap.get(username));
		partnerMap.remove(username);
		
		usernameClientThreadMap.get(username).setPartner(null);
		usernameClientThreadMap.get(partnerName).setPartner(null);
		
	}
	
	public void startChat(String initiator, String receiver){
		if (	initiator.equals("No match") 	|| receiver.equals("No match") || 
				initiator.equals("Error") 		|| receiver.equals("Error")){
			ClientThread initiatorThread = usernameClientThreadMap.get(initiator);
			initiatorThread.sendMessage("NOMATCH: ");
			//System.out.println("No match found!");
			return;
		}		
		if(usernameClientThreadMap.get(receiver).getPartner() == null){
			ClientThread receiverThread = usernameClientThreadMap.get(receiver);
			receiverThread.sendMessage("ACCEPT?: " + initiator);
		}
		else{
			ClientThread initiatorThread = usernameClientThreadMap.get(initiator);
			initiatorThread.sendMessage("OCCUPIED: " + receiver);
		}

	}
	
	
	public void acceptChat(boolean accept, String initiator, String receiver){
		ClientThread initiatorThread = usernameClientThreadMap.get(initiator);
		ClientThread receiverThread = usernameClientThreadMap.get(receiver);
		//Set each  other as partners
		receiverThread.setPartner(initiator);
		initiatorThread.setPartner(receiver);
		partnerMap.put(initiator, receiver);
		partnerMap.put(receiver, initiator);
		initiatorThread.startChatScreen(receiver);
		receiverThread.startChatScreen(initiator);
	}

	public void sendPrivateChat(String username, String partner, String message) {
		if (partner == null){
			return;
		}
		ClientThread ct1;
		ClientThread ct2;
		
		ct1 = usernameClientThreadMap.get(username);
		ct2 = usernameClientThreadMap.get(partner);
		
		ct1.sendPrivateMessageToClient(message);
		ct2.sendPrivateMessageToClient(message);
	}
}


