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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import library.FontLibrary;
import library.ImageLibrary;

public class LoginScreen extends PaintedPanel{
	
	private JLabel logoLabel;
	private JTextField usernameField;
	private PaintedButton loginButton;
	private PaintedButton registerButton;
	private JButton guestButton;
	private JPasswordField passwordField;
	private static final long serialVersionUID = 1L;

	public LoginScreen(Image image, final ClientPanel clientPanel) {
		super(image, true);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.5;
		logoLabel = new JLabel();
		logoLabel.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/Logo.png")));
		add(logoLabel,gbc);
		gbc.gridy = 1;
		gbc.weighty = 0.0;
		Border rounded =new LineBorder(new Color(220,220,220), 4, true);
		usernameField = new JTextField();
		usernameField.setPreferredSize(new Dimension(300,30));
		usernameField.setBorder(rounded);
		usernameField.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 18));
		usernameField.setHorizontalAlignment(SwingConstants.CENTER);
		add(usernameField,gbc);
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(300,30));
		passwordField.setBorder(rounded);
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.weighty = 0.1;
		gbc.gridy = 2;
		add(passwordField,gbc);
		loginButton = new PaintedButton("Login",ImageLibrary.getImage("Assets/button.png"),
				ImageLibrary.getImage("Assets/button_down.png"),16);
		loginButton.setPreferredSize(new Dimension(200,30));
		usernameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
		passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
		loginButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {	
				String passwordString = new String(passwordField.getPassword());
				clientPanel.checkUserCredentials(usernameField.getText(), passwordString);
			}
		});
		gbc.weighty = 0.02;
		gbc.gridy = 3;
		add(loginButton,gbc);
		registerButton = new PaintedButton("Register",ImageLibrary.getImage("Assets/button.png"),
				ImageLibrary.getImage("Assets/button_down.png"),16);
		registerButton.setPreferredSize(new Dimension(200,30));
		registerButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterScreen rs = new RegisterScreen(ImageLibrary.getImage("Assets/background.jpg"), clientPanel);
				clientPanel.removeAll();
				clientPanel.add(rs);
				clientPanel.revalidate();				
			}
			
		});
		gbc.weighty = 0.05;
		gbc.gridy = 4;
		add(registerButton, gbc);
		guestButton = new JButton("Try it out");
		guestButton.setPreferredSize(new Dimension(150,30));
		guestButton.setBackground(new Color(0,0,0,0));
		guestButton.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 16));
		guestButton.setOpaque(false);
		guestButton.addActionListener(new ActionListener(){

			// FOR GUEST
			@Override
			public void actionPerformed(ActionEvent e) {
				// user will be null in theory
				clientPanel.setUser(null);
				MapScreen ms = new MapScreen(clientPanel);
				clientPanel.removeAll();
				clientPanel.add(ms);
				clientPanel.revalidate();
			}
			
		});
		gbc.weighty = 0.5;
		gbc.gridy = 5;
		add(guestButton, gbc);
	}
}
