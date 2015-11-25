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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;

import library.FontLibrary;
import library.ImageLibrary;

public class ChatScreen extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4735310093577905058L;

	
	private JPanel middleBar, bottomBar;
	private JLabel otherName, logo;
	private PaintedButton sendButton, disconnectButton;
	private JTextField jtf;
	private JScrollPane js;
	private PaintedPanel topBar;
	private ClientPanel cp;
	private JTextArea jta;
	
	public ChatScreen(String otherName, ClientPanel cp){
		this.cp = cp;
		initializeComponents(otherName);
		createGUI();
		addEvents();
		setSize(700,800);
		this.setLocationRelativeTo(cp);
		
	}
	
	private void initializeComponents(String otherName){
		topBar = new PaintedPanel(ImageLibrary.getImage("Assets/background.jpg"));
		middleBar = new JPanel();
		middleBar.setBackground(Color.white);
		bottomBar = new JPanel();
		this.otherName = new JLabel(otherName);
		this.otherName.setHorizontalAlignment(SwingConstants.CENTER);
		this.otherName.setForeground(Color.WHITE);
		this.otherName.setFont(new Font("Sans Serif", Font.PLAIN, 40));
		this.otherName.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 36));
		sendButton = new PaintedButton("Send",ImageLibrary.getImage("Assets/chatbutton.png"),
				ImageLibrary.getImage("Assets/chatbuttondown.png"),10);
		sendButton.setPreferredSize(new Dimension(70, 30));
		disconnectButton = new PaintedButton("Disconnect",ImageLibrary.getImage("Assets/chatbutton.png"),
				ImageLibrary.getImage("Assets/chatbuttondown.png"),10);
		disconnectButton.setPreferredSize(new Dimension(70, 30));
		jtf = new JTextField(30);
		jtf.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 18));
		jtf.setPreferredSize(new Dimension(140,50));
		Border rounded =new LineBorder(new Color(220,220,220), 4, true);
		jtf.setBorder(rounded);
		logo = new JLabel();
		logo.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/chatlogo.png")));
		jta = new JTextArea();
//		jta.setPreferredSize(new Dimension(600,600));
		jta.setEditable(false);
		jta.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 18));
		DefaultCaret caret = (DefaultCaret)jta.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
	}
	
	private void createGUI(){
		this.setLayout(new BorderLayout());
		createTop();
		createMiddle();
		createBottom();
		add(topBar, BorderLayout.NORTH);
		add(middleBar, BorderLayout.CENTER);
		add(bottomBar, BorderLayout.SOUTH);
	}
	
	private void createBottom() {
		bottomBar.setPreferredSize(new Dimension(0,75));
		Border rounded =new LineBorder(new Color(220,220,220), 4, true);
		bottomBar.setBorder(rounded);
		bottomBar.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		bottomBar.add(jtf, gbc);
		gbc.weightx = 0.3;
		bottomBar.add(sendButton, gbc);
		bottomBar.add(disconnectButton, gbc);
		
	}

	private void createMiddle() {
		js = new JScrollPane(jta);
		js.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		js.setPreferredSize(new Dimension(600,600));
		middleBar.add(js);
	}

	private void createTop() {
		topBar.setBackground(Color.BLUE);
		topBar.setLayout(new GridBagLayout());
		topBar.setPreferredSize(new Dimension(0,75));
		Border rounded =new LineBorder(new Color(220,220,220), 3, true);
		topBar.setBorder(rounded);
		topBar.setForeground(Color.WHITE);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.0;
		topBar.add(logo,gbc);
		gbc.weightx = 1.0;
		topBar.add(otherName,gbc);
	}

	private void addEvents(){
		sendButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String username = cp.getUser().getFirstName();
				cp.sendMessage("CHATMESSAGE: " + username + ": " + jtf.getText());
				jtf.setText("");
			}
			
		});
		disconnectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				String username = cp.getUser().getUsername();
				cp.sendMessage("DISCONNECTCHAT: " + username);
				ChatScreen.this.dispose();
			}
			
		});
		jtf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendButton.doClick();
                }
            }
        });
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	public void append(String message) {
		jta.append(message);
		jta.append("\n");
		jta.setCaretPosition(jta.getDocument().getLength());
		jta.repaint(); jta.revalidate();
	}
}
