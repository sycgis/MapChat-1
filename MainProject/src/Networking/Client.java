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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JOptionPane;

import Backend.User;
import GUI.ClientPanel;

public class Client extends Thread {
	private BufferedReader br;
	private PrintWriter pw;
	private Socket s;
	
	public String isValid = null;
	
	private ClientPanel cp;
	
	public Client(String hostname, int port, ClientPanel cp) {
		this.cp = cp;
		try {
			s = new Socket(hostname, port);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe in Client constructor: " + ioe.getMessage());
			System.exit(0);
		}
	}
	public void sendMessagetoServer(String message) {
		if (pw != null) {
			pw.println(message);
			pw.flush();
		}
		
	}

	public String preMessage(String in){
		return in.substring(0, in.indexOf(":"));
	}
	
	public String postMessage(String in){
		return in.substring(in.indexOf(":")+2, in.length());
	}
	public void run() {
		try {
			while (true) {
				String message = br.readLine();
				if (message == null) {
					break;
				}
				
				if (message != null) {
					if (preMessage(message).equals("LOGIN_CHECK")) {
						isValid = postMessage(message);
						
						if (isValid.equals("true")) {
							cp.successfulLogin();
						} 
						else {
							JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Error", JOptionPane.NO_OPTION);
						}
					}
					else if (preMessage(message).equals("USER_INFO")) {
						// Parse all user info
						String userString = postMessage(message);
						//System.out.println(userString);
						String [] stringArray = userString.split("\\|");
						String firstName = stringArray[0];
						String lastName = stringArray[1];
						String city = stringArray[2];
						Double longitude = Double.parseDouble(stringArray[3]);
						Double latitude = Double.parseDouble(stringArray[4]);
						
						Vector<Integer> preferences = new Vector<Integer>();
						preferences.add(Integer.parseInt(stringArray[5]));
						preferences.add(Integer.parseInt(stringArray[6]));
						preferences.add(Integer.parseInt(stringArray[7]));
						preferences.add(Integer.parseInt(stringArray[8]));
						preferences.add(Integer.parseInt(stringArray[9]));
						
						String userName = stringArray[10];
						
						Boolean isReg = true;
						
						User u = new User(isReg, firstName, lastName, longitude, latitude, city, userName);
						u.setUserPreferences(preferences.get(0), preferences.get(1), preferences.get(2), preferences.get(3), preferences.get(4));
						cp.setUser(u);
						
					}
					else if (preMessage(message).equals("USER_INFO2")) {
						// parse through user info excluding preferences (if set to null)
						String userString = postMessage(message);
						//System.out.println(userString);
						String [] stringArray = userString.split("\\|");
						
						String firstName = stringArray[0];
						String lastName = stringArray[1];
						String city = stringArray[2];
						Double longitude = Double.parseDouble(stringArray[3]);
						Double latitude = Double.parseDouble(stringArray[4]);
						String userName = stringArray[5];
						User u = new User(true, firstName, lastName, longitude, latitude, city, userName);
						//System.out.println(u);
						cp.setUser(u);
					}
					else if (preMessage(message).equals("SHARE_LOC")) {
						
						String location = postMessage(message);
						//System.out.println(location);
						String [] split = location.split("\\|");
						String fullName = split[0];
						Double latitude = Double.parseDouble(split[1]);
						Double longitude = Double.parseDouble(split[2]);
						
						cp.setNewLocation(fullName, latitude, longitude);
						
					}
					else if (preMessage(message).equals("FINDCHATRESPONSE")) {
						String response = postMessage(message);
						cp.showChatMatch(response);
					}
					else if (preMessage(message).equals("USERNAMECHECK")) {
						String response = postMessage(message);
						String[] split = response.split("\\|");
						String in = split[0];
						String username = split[1];
						String password = split[2];
						cp.checkUsername(in, username, password);
					}
					else if (preMessage(message).equals("ALREADYLOGGED")) {
						JOptionPane.showMessageDialog(null, "This user is already logged in.", "Login Error", JOptionPane.NO_OPTION);
					} 
					else if (preMessage(message).equals("CHATMESSAGE")){
						//TODO send this to the chatscreen
						this.cp.getMap().getChatScreen().append(postMessage(message));
					}	
					else if (preMessage(message).equals("MODIFIEDATTRACTION")) {
						String response = postMessage(message);
						String split[] = response.split("\\|");
						String testimonial = split[0];
						int rating = Integer.parseInt(split[1]);
						int pos = Integer.parseInt(split[2]);
						cp.updateAttraction(testimonial, rating, pos);

					}
					else if(preMessage(message).equals("RECEIVECHAT")){
						cp.getMap().popChatScreen(postMessage(message));
					}
					else if(preMessage(message).equals("DISCONNECTCHAT")){
						cp.getMap().getChatScreen().dispose();
					}
					else if(preMessage(message).equals("ACCEPT?")){
						int selection = JOptionPane.showConfirmDialog(cp.getMap(), "Would you like to chat with " + postMessage(message) + "?", "Confirmation", JOptionPane.YES_NO_OPTION);
						 switch (selection) {
						 case JOptionPane.YES_OPTION: 
						 sendMessagetoServer("CHATRESPONSE: " + "YES: " + postMessage(message));
						 break;
						 default: 
							 sendMessagetoServer("CHATRESPONSE: " + "NO: ");
							 break;
						 } 
					}
					else if (preMessage(message).equals("NOTIFY_ABOUT_MODIFY")) {
						cp.checkForUpdatedAttractions();
					}
					else if (preMessage(message).equals("NOMATCH")) {
						JOptionPane.showMessageDialog(null, "Sorry, we could not match you with a user whose hometown is where you are currently located.", "Matchmaking Error", JOptionPane.NO_OPTION);
					}
					else if (preMessage(message).equals("OCCUPIED")){
						JOptionPane.showMessageDialog(null, "Sorry, your match " + postMessage(message) + " is occupied.", "Chat Error", JOptionPane.NO_OPTION);
					}
				}
				
			}
		}catch (IOException ioe) {
			System.out.println("ioe in ClientUser.run(): " + ioe.getMessage());
			
		}
	}
}
