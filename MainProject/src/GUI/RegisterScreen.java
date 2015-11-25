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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import library.FontLibrary;
import library.ImageLibrary;

public class RegisterScreen extends PaintedPanel{

	JLabel logo;
	JLabel welcome;
	JLabel welcome2;
	JTextField userName;
	JLabel nameLabel;
	JLabel passLabel;
	JTextField passWord;
	PaintedButton Register;
	JPanel namePanel;
	JPanel passPanel;
	JLabel namePic;
	JLabel passPic;
	private static final long serialVersionUID = 1L;
	
	public RegisterScreen(Image image, final ClientPanel clientPanel){
		super(image,true);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		logo = new JLabel();
		logo.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/logo_small.png")));
		add(logo,gbc);
		welcome = new JLabel("Welcome to MapChat new chatter!");
		welcome.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		welcome.setForeground(Color.WHITE);
		gbc.gridy = 1;
		gbc.weighty = 0.01;
		add(welcome,gbc);
		welcome2 = new JLabel("Let's set up your credential as fast as possibe, so you can go MapChatting.");
		welcome2.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		welcome2.setForeground(Color.WHITE);
		gbc.gridy = 2;
		add(welcome2,gbc);
		nameLabel = new JLabel("Please enter a username");
		nameLabel.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		nameLabel.setForeground(Color.WHITE);
		gbc.gridy = 3;
		gbc.weighty = 0;
		add(nameLabel,gbc);
		userName= new JTextField();
		userName.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		userName.setHorizontalAlignment(SwingConstants.CENTER);
		Border rounded =new LineBorder(new Color(220,220,220), 4, true);
		userName.setBorder(rounded);
		userName.setPreferredSize(new Dimension(400,30));
		namePic = new JLabel();
		namePic.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/user.png")));
		namePic.setText("     ");
		namePanel = new JPanel();
		namePanel.setOpaque(false);
		namePanel.add(namePic);
		namePanel.add(userName);
		namePanel.setPreferredSize(new Dimension(500, 70));
		gbc.gridy = 4;
		gbc.weighty = 0.005;
		add(namePanel, gbc);
		passLabel = new JLabel("Please enter a password");
		passLabel.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		passLabel.setForeground(Color.WHITE);
		gbc.gridy = 5;
		gbc.weighty = 0;
		add(passLabel,gbc);
		passWord = new JTextField();
		passWord.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		passWord.setHorizontalAlignment(SwingConstants.CENTER);
		passWord.setBorder(rounded);
		passWord.setPreferredSize(new Dimension(400,30));
		passPic = new JLabel();
		passPic.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/pass.png")));
		passPic.setText("     ");
		passPanel = new JPanel();
		passPanel.setOpaque(false);
		passPanel.setPreferredSize(new Dimension(500, 70));
		passPanel.add(passPic);
		passPanel.add(passWord);
		gbc.gridy = 6;
		gbc.weighty = 0.005;
		add(passPanel, gbc);
		gbc.gridy = 7;
		Register = new PaintedButton("Register",ImageLibrary.getImage("Assets/button.png"),
				ImageLibrary.getImage("Assets/button_down.png"),16);
		Register.setPreferredSize(new Dimension(200,30));
		add(Register,gbc);
		
		Register.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(userName.getText().isEmpty() == true || passWord.getText().isEmpty() == true){
					JOptionPane.showMessageDialog(null, "Username or password field is empty", "Register Error", JOptionPane.NO_OPTION);
				}
				else {
					String concatenate = "CHECKUSERNAME: " + userName.getText() + "|" + passWord.getText();
					clientPanel.sendMessage(concatenate);
				}
			}
			
		});	
	}
}