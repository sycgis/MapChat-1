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

import Backend.Attraction;
import Backend.DBCommunicator;

public class ClientThread extends Thread {
	private Server server;
	private BufferedReader br; 
	private PrintWriter pw;
	private String username, partner;
	private Socket s;
	
	public ClientThread(Socket s, Server server, DBCommunicator dbc) {
		this.server = server;
		this.s = s;
		try{
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public String preMessage(String in){
		return in.substring(0, in.indexOf(":"));
	}
	
	public String postMessage(String in){
		return in.substring(in.indexOf(":")+2, in.length());
	}
	
	public void sendMessage(String inMessage){
		if (pw != null){
			//System.out.println("INSIDE SENDMESSAGE: " + inMessage);
			pw.println(inMessage);
			pw.flush();
		}	
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPartner(String otherUsername){
		partner = otherUsername;
	}
	public String getPartner(){
		return partner;
	}
	public void run(){
		try {
			while (true) {
				String message = br.readLine();
				if (message == null) {
					break;
				}
				
				if (message != null) {
					 //LOGIN
					if (preMessage(message).equals("LOGIN")) {
						String credentials = postMessage(message);
						String[] split = credentials.split("\\|");
						String username = split[0];
						String password = split[1];
						Boolean isValid = server.checkCredentials(username, password, this);
						if (isValid == true) {
							if (server.isUserLoggedIn(username)) {
								sendMessage("ALREADYLOGGED: ");
							}
							else {
								server.addUsername(username, this);
								sendMessage("LOGIN_CHECK: " + isValid.toString());
							}
						}
						else {
							sendMessage("LOGIN_CHECK: " + isValid.toString());
						}
						
					}
					else if(preMessage(message).equals("NEWUSER")){
						String credentials = postMessage(message);
						String[] split = credentials.split("\\|");
						String fname = split[0];
						String lname = split[1];
						String info = split[2];
						String userName = split[3];
						String passWord = split[4];
						server.addnewUser(fname, lname, info, userName, passWord);
					}
					else if(preMessage(message).equals("NEWUSER1")){
						String credentials = postMessage(message);
						String[] split = credentials.split("\\|");
						String userName = split[0];
						int outdoor = Integer.parseInt(split[1]);
						int food = Integer.parseInt(split[2]);
						int nightlife = Integer.parseInt(split[3]);
						int shopping = Integer.parseInt(split[4]);
						server.addnewuserPreference(userName,outdoor,nightlife,0,shopping,food );
					}
					else if (preMessage(message).equals("FINDCHAT")) {
						String s = postMessage(message);
						String[] split = s.split("\\|");
						String username = split[0];
						String city = split[1];
						String other = server.matchingAlgorithm(username, city, this);
						String[] split2 = other.split("\\|");
						other = split2[0];
						server.startChat(username, other);
					}
					//If somebody on the client side is sending a chat-message
					else if(preMessage(message).equals("CHATMESSAGE")){
						//sendMessage("FINDCHATRESPONSE: " + server.matchingAlgorithm(username, city, this));
						server.sendPrivateChat(username, partner, postMessage(message));
					}
					else if(preMessage(message).equals("RATING")){
						String ratingData = postMessage(message);
						String[] split = ratingData.split("\\|");
						String longitudeStr = split[0];
						String latitudeStr = split[1];						
						int rating = Integer.parseInt(split[2]);
						String newComment = split[3];
						String username = split[4];
						String name = split[5];
						String address = split[6];
						String phone = split[7];
						Boolean isModified = server.checkIfAlreadyModified(latitudeStr, longitudeStr);
						if (!isModified) {
							server.addModifiedAttractions(name,phone,longitudeStr, latitudeStr, address);
						}
						server.addRatingData(Double.parseDouble(longitudeStr), Double.parseDouble(latitudeStr), rating);
						if (!newComment.equals("Please enter your testimonial here...")) {
							server.addTestmonialData(Double.parseDouble(longitudeStr), Double.parseDouble(latitudeStr), newComment, username);
						}
						
					}					
					else if (preMessage(message).equals("CHECKUSERNAME")) {
						String response = postMessage(message);
						String[] split = response.split("\\|");
						String username = split[0];
						String password = split[1];
						Boolean isUsed = server.checkUsername(username);
						sendMessage("USERNAMECHECK: " + isUsed.toString() + "|" + username + "|" + password); 
					}

					else if(preMessage(message).equals("PARTNER?")){
						sendMessage("PARTNER: " + partner );
					}
					else if (preMessage(message).equals("GETATTRACTION")) {
						String response = postMessage(message);
						String[] split = response.split("\\|");
						String latitude = split[0];
						String longitude = split[1];
						String pos = split[2];
						Attraction a = server.getAttraction(latitude, longitude);
						if (a != null) {
							String m = "MODIFIEDATTRACTION: " + a.getTestimonial() + "|" + a.getRating() + "|" + pos;
							sendMessage(m);
							//server.sendMessagetoAllClients("CHECK_FOR_UPDATES: ", this); 
							//Let them know it's updated, but other clients have to iterate
							//Problem with the above call was that it made other clients check
							//for updated attractions before they even had the vector set up
						}
					}
					else if (preMessage(message).equals("NOTIFY_ABOUT_MODIFY")) {
						server.sendMessagetoAllClients(message, this);
					}
					else if(preMessage(message).equals("SELFDESTRUCT")){
						String usernametoremove = postMessage(message);
						server.removeUser(usernametoremove);

					}
					else if(preMessage(message).equals("CHATRESPONSE")){
						String response = postMessage(message);
						if(preMessage(response).equals("YES")){
							String initiator = postMessage(response);
							server.acceptChat(true, initiator, username);
						}
						else{
							
						}
					}
					else if (preMessage(message).equals("CLICKCHAT")){
						String other = postMessage(message);
						server.startChat(username, other);
					}
					else if (preMessage(message).equals("DISCONNECTCHAT")){
						server.disconnectChat(postMessage(message));
					}
					else {
						server.sendMessagetoAllClients(message, this);
					}
				}
			}
		} catch (IOException ioe) {
			System.out.println("ioe: in Thread close out: " + ioe.getMessage());
			server.removeUser(username);
		}
 	}

	public void startChatScreen(String initiator){
		sendMessage("RECEIVECHAT: " + initiator);
	}
	public Socket getSocket() {
		return s;
	}

	public void sendPrivateMessageToClient(String message) {
		sendMessage("CHATMESSAGE: " + message);
	}
}
			


