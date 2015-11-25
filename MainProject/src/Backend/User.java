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
import java.util.Vector;

public class User implements Serializable {
	@Override
	public String toString() {
		return "User [firstname=" + firstname + ", lastname=" + lastname
				+ ", username=" + username + ", cityInfo=" + cityInfo + ", lattitude=" + lattitude
				+ ", longitude=" + longitude + ", preferences=" + preferences
				+ ", isReg=" + isReg + "]";
	}

	public static final long serialVersionUID = 1; 

	
	private String firstname; 
	private String lastname; 
	private String cityInfo; 
	private double lattitude; 
	private double longitude; 
	private Vector <Integer> preferences; 
	private boolean isReg; 
	private String username; 
	
	public User (boolean isReg) {
		this.isReg = isReg; 
	}
	
	public User (boolean isReg, String firstname, String lastname, double longitude, double lattitude, String cityinfo) {
		this.isReg = isReg; 
		this.firstname = firstname; 
		this.lastname = lastname; 
		this.lattitude = lattitude; 
		this.longitude = longitude; 
		this.cityInfo = cityinfo; 
		this.username = null; 
		preferences = new Vector<Integer>();
	}
	
	public User (boolean isReg, String firstname, String lastname, double longitude, double lattitude, String cityinfo, String username) {
		this.isReg = isReg; 
		this.firstname = firstname; 
		this.lastname = lastname; 
		this.lattitude = lattitude; 
		this.longitude = longitude; 
		this.cityInfo = cityinfo; 
		this.username = username; 
		preferences = new Vector<Integer>();
	}
	
	public User (boolean isReg, String firstname, String lastname, String cityinfo) {
		this.isReg = isReg; 
		this.firstname = firstname; 
		this.lastname = lastname; 
		this.cityInfo = cityinfo; 
		this.username = null; 
		preferences = new Vector<Integer>();
	}
	
	public User (boolean isReg, String firstname, String lastname, String cityinfo, String username) {
		this.isReg = isReg; 
		this.firstname = firstname; 
		this.lastname = lastname; 
		this.cityInfo = cityinfo; 
		this.username = username; 
		preferences = new Vector<Integer>();
	}
	
	public double [] getLocation () {
		double [] result = new double[2]; 
		result[0] = longitude;
		result[1] = lattitude; 
		return result; 
	}
	
	public String getFirstName () {
		return firstname; 
	}
	
	public String getUsername () {
		return username; 
	}
	
	//preference1:outdoors;
	//preferecne2: nightlife;
	//preference3:  hotels;
	//preference4: shopping;
	//preference5: restaurant;
	public void setUserPreferences (int outdoorPref, int nightlifePref, int hotelPref, int shoppingPref, int restaurantPref) {
		preferences.clear(); 
		preferences.add(outdoorPref); 
		preferences.add(nightlifePref); 
		preferences.add(hotelPref); 
		preferences.add(shoppingPref); 
		preferences.add(restaurantPref); 
	}
	
	public Vector<Integer> getUserPreferences () {
		return preferences; 
	}
	
	public Integer [] getUserPreferencesArray () {
		Integer [] result = new Integer [preferences.size()]; 
		
		for (int i = 0; i < preferences.size(); i++) {
			result[i] = preferences.get(i); 
		}
		
		return result; 
	}
	
	public String getLastName () {
		return lastname; 
	}
	
	public String getCityInfo () {
		return cityInfo; 
	}
	
	public void setUsername (String username) {
		this.username = username; 
	}
	
	public void setName (String firstname, String lastname) {
		this.firstname = firstname; 
		this.lastname = lastname; 
	}
	
	public void setCityInfo (String cityinfo) {
		this.cityInfo = cityinfo; 
	}
	
	public void setLocation (double longitude, double lattitude) {
		this.longitude = longitude; 
		this.lattitude = lattitude; 
	}
	
	public boolean isRegistered () {
		return isReg; 
	}
	
	public void setReg (boolean reg) {
		isReg = reg; 
	}
}
