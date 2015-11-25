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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import library.ImageLibrary;


public class LocationRatingDialog extends JDialog implements ActionListener {
    private static final long serialVersionUID = 12L;

	private int selection = -1;
    private PaintedButton reviewSendBtn = null;
    private JLabel registertorate;
    private JLabel[] starLabel;
	private JTextArea siteAddressArea;
	private JTextArea siteNameArea;
	private ButtonGroup buttonGroup;
	private JRadioButton optionButtons[];
	private JTextArea existingReview;
	private JTextArea myReviewText;
	
	private ImageIcon starIcon;        
	private ImageIcon starGreyIcon;

	private String longitudeStr;
	private String latitudeStr;
	private String name;
	private String address;
	private String phone;
    private ClientPanel clientPanel;
    
	public LocationRatingDialog(String name, String addresss, String phone, String theLogi, String theLati, ClientPanel thePanel) {	
        super();
        longitudeStr = theLogi;
        latitudeStr = theLati;
        this.name = name;
        this.address = addresss;
        this.phone = phone;
        
        clientPanel = thePanel;
 
        starIcon = new ImageIcon(ImageLibrary.getImage("Assets/StarYellow.png"));        
        starGreyIcon = new ImageIcon(ImageLibrary.getImage("Assets/StarGrey.png"));        
				
		// center panel in the middle area
        JPanel centerPanel = new JPanel(); //default FlowLayout
        getContentPane().add(centerPanel);
        
        JPanel leftPanel = new JPanel();  // left center panel for address ...
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		siteAddressArea = new JTextArea("", 4, 15);
		siteAddressArea.setEditable(false);
		siteAddressArea.setBackground(new Color(238, 238, 238));
		siteNameArea = new JTextArea("", 1, 3);
		siteNameArea.setEditable(false);
		siteNameArea.setBackground(new Color(238, 238, 238));
		leftPanel.add(siteNameArea);
		leftPanel.add(siteAddressArea);
		
		leftPanel.add(new JLabel(" "));
		
		existingReview = new JTextArea("", 8, 20);
		existingReview.setLineWrap(true);
		existingReview.setWrapStyleWord(true);
		existingReview.setEditable(false);
		existingReview.setBackground(new Color(238, 238, 238));
		
		//JTextPane messagedisplay = new JTextPane();
		//JScrollPane reviewMsgScrollPane = new JScrollPane(existingReview);		
		//reviewMsgScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//messagedisplay.add(existingReview);
		leftPanel.add(existingReview);		
        centerPanel.add(leftPanel);
        
        JPanel rightPanel = new JPanel();  // right center panel for image
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.setAlignmentX(LEFT_ALIGNMENT);
		
        //star panel with 5 star labels
        JPanel starPanel = new JPanel();
		starLabel = new JLabel[5];        
        for (int i = 0; i<5; i++ ) {
        	starLabel[i] = new JLabel("");
			starLabel[i].setIcon(starGreyIcon);        	
        	starPanel.add(starLabel[i]);        	
        }
        rightPanel.add(starPanel);
        
        rightPanel.add(new JLabel(" "));
        
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));		
		buttonGroup = new ButtonGroup();
		optionButtons = new JRadioButton[5];
		for(int i = 0; i < 5; ++i) {
			JPanel ratingPanel = new JPanel();
			optionButtons[i] = new JRadioButton();
			final int buttonSelection = i+1;
			optionButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					selection = buttonSelection;
					for (int j = 0; j < 5; j++) {
						if (j < selection) {
							starLabel[j].setIcon(starIcon);   
						}
						else {
							starLabel[j].setIcon(starGreyIcon); 
						}
					}
				}
			});
			buttonGroup.add(optionButtons[i]);
			ratingPanel.add(optionButtons[i]);
			JLabel numLabel = new JLabel(""+buttonSelection);
			numLabel.setBackground(new Color(0,0,0,0));
			numLabel.setOpaque(true);
			ratingPanel.add(numLabel);			
			buttonPanel.add(ratingPanel);
		}
        rightPanel.add(buttonPanel);
        
        rightPanel.add(new JLabel(" "));
	
		/*myReviewText.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				myReviewText.setText("");
			}
			public void focusLost(FocusEvent e) {
				myReviewText.setText("Please enter your testimonial here...");
			}
		});*/
		//rightPanel.add(new JLabel("Type your testimonial here: "));				
        JPanel reviewEntryPanel = new JPanel (new FlowLayout());
		myReviewText = new JTextArea("Please enter your testimonial here...", 4, 20);
		myReviewText.setLineWrap(true);	
		reviewEntryPanel.add(myReviewText);
	
        rightPanel.add(new JLabel(" "));
		
        JPanel sendPanel = new JPanel();
        
//		reviewSendBtn = new JButton("Submit");
		reviewSendBtn = new PaintedButton("Submit",ImageLibrary.getImage("Assets/button1.png"),
				ImageLibrary.getImage("Assets/button_down.png"),16);
		reviewSendBtn.setPreferredSize(new Dimension(120,30));
		Border rounded =new LineBorder(new Color(220,220,220), 4, true);
		reviewSendBtn.setBorder(rounded);
		reviewSendBtn.addActionListener(this);		
		sendPanel.add(reviewSendBtn);
		JPanel noRatePanel = new JPanel();
		registertorate = new JLabel("Register to rate and add testimonial!");
		noRatePanel.add(registertorate);
		//reviewSendBtn.setHorizontalAlignment(JButton.CENTER);
		//registertorate.setHorizontalAlignment(JButton.CENTER);

		if(thePanel.getUser() == null){
			//registertorate.setHorizontalAlignment(JLabel.CENTER);
			rightPanel.add(noRatePanel);
		}
		else{
			//reviewSendBtn.setHorizontalAlignment(JButton.CENTER);
			rightPanel.add(reviewEntryPanel);
			rightPanel.add(sendPanel);
		}		
        
        centerPanel.add(rightPanel);   
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        setTitle ("Attraction Info");		
        pack();
        setSize(620, 400);
        setLocation(screenWidth/2-300, screenHeight/2-150);
        //setModalityType(ModalityType.APPLICATION_MODAL);
        setVisible(true);        
    }

    
    public void actionPerformed(ActionEvent e) {   	
        if(reviewSendBtn == e.getSource()) {	
			clientPanel.sendRatingDataToServer(longitudeStr, latitudeStr, selection, getUserTestimonialText(), clientPanel.getUser().getUsername(), name, address, phone);
			clientPanel.checkForUpdatedAttractions();
			clientPanel.notifyOtherClientsAboutModify();
			LocationRatingDialog.this.dispose();  	
        }       
    }

    // set address info
    public void setLocationAddressPhone(String theName, String addressAndphone) {
    	//Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
    	siteAddressArea.setBorder(new EmptyBorder(0, 0, 15, 5));
    	//siteAddressArea.setBorder(border);
    	siteNameArea.setBorder(new EmptyBorder(20,0,5,5));
    	siteNameArea.setFont(new Font("Serif", Font.BOLD, 20));
    	siteAddressArea.setFont(new Font("Serif", Font.PLAIN, 14));
    	siteNameArea.setText(theName);
    	siteAddressArea.setText(addressAndphone);
    }

    // set STAR number
    public void setLocationStar(int starNum) {
        for (int i = 0; i<starNum; i++ ) {
        	starLabel[i].setIcon(starIcon);
         }

    	for (int j = starNum; j < 5; j++){
        	starLabel[j].setIcon(starGreyIcon);    		
    	}
    }

    // set review message record
    public void setExistingTestimonialMsg(String reviewRecord) {
    	existingReview.setFont(new Font("Serif", Font.ITALIC, 15));
    	existingReview.setText(" \" " + reviewRecord + " \" " );
    	existingReview.setBorder(new EmptyBorder(20, 5, 15, 5));
    }
    
    // get your testimonial text
    public String getUserTestimonialText() {
    	return myReviewText.getText();
    }

    // get your rating
    public int getUserRating() {
    	return selection;
    }        
}
