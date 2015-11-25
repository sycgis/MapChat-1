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

package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import library.FontLibrary;
import library.ImageLibrary;
import Backend.User;
import Networking.Client;

public class ClientPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private LoginScreen loginScreen;
	private User user;
	private Client client;
	private MapScreen ms;
	private String currentPartner;

	public ClientPanel(){
		client = null;
		user = null;
		createDialog();
		loginScreen = new LoginScreen(ImageLibrary.getImage("Assets/background.jpg"),this);
		setLayout(new BorderLayout());
		add(loginScreen);
		
	}
	public MapScreen getMap(){
		return ms;
	}
	public void checkUserCredentials(String username, String password) {
		if (username.isEmpty() == true || password.isEmpty() == true) {
			JOptionPane.showMessageDialog(null, "Username or password field is empty", "Login Error", JOptionPane.NO_OPTION);
		}
		else {
			String concatanate = "LOGIN: " + username + "|" + password;
			client.sendMessagetoServer(concatanate);
		}
	}
	
	public void successfulLogin() {
		ms = new MapScreen(this);
		removeAll();
		add(ms);
		ms.map().setVisible(true);
		revalidate();
	}
	
	public void shareLocation (String fullName, String latitude, String longitude) {
		String concatenate = "SHARE_LOC: " + fullName + "|" + latitude + "|" + longitude;
		client.sendMessagetoServer(concatenate);
		
	}
	public void setPartner(String partner){
		currentPartner = partner;
	}
	public void showChatMatch(String response) {
		if (response.equals("No match")) {
			JOptionPane.showMessageDialog(null, "Sorry, we could not find an active user whose hometown is where you are currently located.", "Matchmaking Error", JOptionPane.NO_OPTION);
		}
		else if (response.equals("Error")) {
			JOptionPane.showMessageDialog(null, "Unknown Error", "Matchmaking Error", JOptionPane.NO_OPTION);
		}
		else {
			String[] responseArray = response.split("\\|");
			String responseUser = responseArray[0];
			String responsePercent = responseArray[1];
			JOptionPane.showMessageDialog(null, "You matched " + responsePercent + "% with " + responseUser + "!", "Matchmade!", JOptionPane.NO_OPTION);
		}
	}
	
	private void createDialog() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
		final JDialog jd = new JDialog();
		jd.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		System.exit(0);
        	}
        });
		JPanel jp = new JPanel();
		JLabel jl = new JLabel("Enter the IP address of the server: ");
		jl.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf",Font.PLAIN, 14));
		final JTextField jtf = new JTextField(30);
		Border rounded = new LineBorder(new Color(220,220,220), 4, true);
		jtf.setPreferredSize(new Dimension(120, 25));
		jtf.setBorder(rounded);
		jtf.setHorizontalAlignment(SwingConstants.CENTER);
		jtf.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf",Font.PLAIN, 14));
		final PaintedButton okButton = new PaintedButton("OK",ImageLibrary.getImage("Assets/button.png"),
				ImageLibrary.getImage("Assets/button_down.png"),14);
		okButton.setPreferredSize(new Dimension(80,30));
		jtf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    okButton.doClick();
                }
            }
        });
		okButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent ae) {
        		client = new Client(jtf.getText(), 5555, ClientPanel.this);
        		jd.dispose();
        	}
        });
		jd.setSize(400, 130);
		jd.setLocation(screenWidth/2-200, screenHeight/2-65);
		jp.add(jl);
		jp.add(jtf);
		jp.add(okButton);
		jd.add(jp);
        jd.setModalityType(ModalityType.APPLICATION_MODAL);
        jd.setVisible(true);
	}
	
	public void sendMessage(String message){
		client.sendMessagetoServer(message);
	}
	
	public void setUser(User user) {
		this.user = user; 
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public Client getclient() {
		return this.client;
	}

	public void setNewLocation(String fullName, Double latitude, Double longitude) {
		// TODO Create map marker on other client screens
		ms.drawMarker(fullName, latitude, longitude);	
	}
	
	

	public void sendRatingDataToServer(String longiStr, String latiStr, int rating, String testimonial, String username, String name, String address, String phone) {
		if ( rating < 0 ) {
			JOptionPane.showMessageDialog(null, "Please select a rating.", "Error", JOptionPane.NO_OPTION);
		}
		else {
			String concatanate = "RATING: " + longiStr + "|" + latiStr + "|" + Integer.toString(rating) + "|" + testimonial + "|" + username + "|" + name + "|" + address + "|" + phone;
			client.sendMessagetoServer(concatanate);
		}
	}	
	
	public void checkUsername(String in, String username, String password) {
		if (in.equals("true")) {
			FirstTimeScreen fts = new FirstTimeScreen(ImageLibrary.getImage("Assets/background.jpg"), username, password, this);
			removeAll();
			add(fts);
			revalidate();
		}
		else {
			JOptionPane.showMessageDialog(null, "Username is taken. Please try again.", "Registration Error", JOptionPane.NO_OPTION);
		}
	}
	
	public void updateAttraction(String testimonial, int rating, int pos) {
		ms.updateAttraction(testimonial, rating, pos);
	}
	
	public void checkForUpdatedAttractions() {
		ms.checkForUpdatedAttractions();
	}
	
	public void notifyOtherClientsAboutModify() {
		client.sendMessagetoServer("NOTIFY_ABOUT_MODIFY: ");
	}

	public String getPartner() {
		return currentPartner;
	}
	
}
