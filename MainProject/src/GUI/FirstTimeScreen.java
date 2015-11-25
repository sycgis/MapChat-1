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
import java.util.Scanner;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import library.FontLibrary;
import library.ImageLibrary;
import Utilities.LongLatService;
import Utilities.PlacesSearch;


public class FirstTimeScreen extends PaintedPanel{
	
	private JLabel namePic;
	private JPanel namePanel;
	private JPanel homePanel;
	private JLabel homePic;
	private JLabel outdoorPic;
	private JLabel outdoorPic2;
	private JPanel outdoorPanel;
	private JLabel foodPic;
	private JLabel foodPic2;
	private JPanel foodPanel;
	private JLabel nightlifePic;
	private JLabel nightlifePic2;
	private JPanel nightlifePanel;
	private JLabel shoppingPic;
	private JLabel shoppingPic2;
	private JPanel shoppingPanel;
	private JLabel logo;
	private JTextField hometownField;
	@SuppressWarnings("rawtypes")
	private JComboBox homeTown;
	private String Towns[] = {"Select your hometown", "Los Angeles", "Seattle", "San Francisco", "Denver", "Compton"};
	private JLabel message;
	private JTextField name;
	private PaintedButton submit;
	private JSlider Outdoor;
	private JLabel loveOutdoor;
	private JSlider Food;
	private JLabel loveFood;
	private JSlider Nightlife;
	private JLabel loveNightlife;
	private JSlider Shopping;
	private JLabel loveShopping;
	private PlacesSearch placesSearch;
	private LongLatService longlatService;

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FirstTimeScreen(Image image, final String userName, final String passWord, final ClientPanel clientPanel){
		super(image,true);
		placesSearch = new PlacesSearch();
		longlatService = new LongLatService();
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		logo = new JLabel();
		logo.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/logo_small.png")));
		add(logo,gbc);
		gbc.weighty = 0.01;
		gbc.gridy = 1;
		message = new JLabel("Hello there newbie! We want to get to know you better!");
		message.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		message.setForeground(Color.WHITE);
		add(message,gbc);
		namePic = new JLabel();
		namePic.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/name.png")));
		namePic.setText("                 ");
		name = new JTextField();
		name.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		name.setHorizontalAlignment(SwingConstants.CENTER);
		Border rounded =new LineBorder(new Color(220,220,220), 4, true);
		name.setBorder(rounded);
		name.setPreferredSize(new Dimension(400,30));
		gbc.gridy = 2;
		namePanel = new JPanel();
		namePanel.setOpaque(false);
		namePanel.add(namePic);
		namePanel.add(name);
		add(namePanel,gbc);
		homeTown = new JComboBox(Towns);
		homeTown.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf",Font.PLAIN , 22));
		homeTown.setPreferredSize(new Dimension(400,30));
		homeTown.setSelectedIndex(0);
		homePic = new JLabel();
		homePic.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/home.png")));
		homePic.setText("                   ");
		gbc.gridy = 3;
		homePanel = new JPanel();
		homePanel.setOpaque(false);
		homePanel.add(homePic);
		hometownField = new JTextField();
		hometownField.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		hometownField.setHorizontalAlignment(SwingConstants.CENTER);
		hometownField.setBorder(rounded);
		hometownField.setPreferredSize(new Dimension(400,30));
		homePanel.add(hometownField);
		//homePanel.add(homeTown);
		add(homePanel,gbc);
		loveOutdoor = new JLabel("How much do you enjoy the outdoor?");
		loveOutdoor.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		loveOutdoor.setForeground(Color.WHITE);
		gbc.gridy = 4;
		add(loveOutdoor,gbc);
		Outdoor = new JSlider(JSlider.HORIZONTAL);
		Outdoor.setMaximum(5);
		Outdoor.setPaintLabels(true);
		Outdoor.setPaintTicks(false);
		Outdoor.setMajorTickSpacing(1);
		Outdoor.setPreferredSize(new Dimension(400,30));
		Outdoor.setValue(3);
		Outdoor.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
		outdoorPic = new JLabel();
		outdoorPic.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/outdoor.png")));
		outdoorPic2 = new JLabel();
		outdoorPic2.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/outdoor 2.png")));
		outdoorPanel = new JPanel();
		outdoorPanel.setOpaque(false);
		outdoorPanel.add(outdoorPic2);
		outdoorPanel.add(Outdoor);
		outdoorPanel.add(outdoorPic);
		gbc.gridy = 5;
		add(outdoorPanel,gbc);
		loveFood = new JLabel("How much do you enjoy food?");
		loveFood.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		loveFood.setForeground(Color.WHITE);
		gbc.gridy = 6;
		add(loveFood, gbc);
		Food = new JSlider(JSlider.HORIZONTAL);
		Food.setMaximum(5);
		Food.setPaintLabels(true);
		Food.setPaintTicks(false);
		Food.setMajorTickSpacing(1);
		Food.setPreferredSize(new Dimension(400,30));
		Food.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
		Food.setValue(3);
		foodPic = new JLabel();
		foodPic.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/food.png")));
		foodPic2 = new JLabel();
		foodPic2.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/food2.png")));
		foodPanel = new JPanel();
		foodPanel.setOpaque(false);
		foodPanel.add(foodPic2);
		foodPanel.add(Food);
		foodPanel.add(foodPic);
		gbc.gridy = 7;
		add(foodPanel,gbc);
		loveNightlife = new JLabel("How much do you enjoy nightlife?");
		loveNightlife.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		loveNightlife.setForeground(Color.WHITE);
		gbc.gridy = 8;
		add(loveNightlife, gbc);
		Nightlife = new JSlider(JSlider.HORIZONTAL);
		Nightlife.setMaximum(5);
		Nightlife.setPaintLabels(true);
		Nightlife.setPaintTicks(false);
		Nightlife.setMajorTickSpacing(1);
		Nightlife.setPreferredSize(new Dimension(400,30));
		Nightlife.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
		Nightlife.setValue(3);
		nightlifePic = new JLabel();
		nightlifePic.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/nightlife.png")));
		nightlifePic2 = new JLabel();
		nightlifePic2.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/nightlife2.png")));
		nightlifePanel = new JPanel();
		nightlifePanel.setOpaque(false);
		nightlifePanel.add(nightlifePic2);
		nightlifePanel.add(Nightlife);
		nightlifePanel.add(nightlifePic);
		gbc.gridy = 9;
		add(nightlifePanel,gbc);
		loveShopping = new JLabel("How much do you enjoy Shopping");
		loveShopping.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 22));
		loveShopping.setForeground(Color.WHITE);
		gbc.gridy = 10;
		add(loveShopping, gbc);
		Shopping = new JSlider(JSlider.HORIZONTAL);
		Shopping.setMaximum(5);
		Shopping.setPaintLabels(true);
		Shopping.setPaintTicks(false);
		Shopping.setMajorTickSpacing(1);
		Shopping.setPreferredSize(new Dimension(400,30));
		Shopping.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
		Shopping.setValue(3);
		shoppingPic = new JLabel();
		shoppingPic.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/shopping.png")));
		shoppingPic2 = new JLabel();
		shoppingPic2.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/shopping2.png")));
		shoppingPanel = new JPanel();
		shoppingPanel.setOpaque(false);
		shoppingPanel.add(shoppingPic2);
		shoppingPanel.add(Shopping);
		shoppingPanel.add(shoppingPic);
		gbc.gridy = 11;
		add(shoppingPanel,gbc);		
		
		gbc.gridy = 12;
		submit = new PaintedButton("Submit",ImageLibrary.getImage("Assets/button.png"),
				ImageLibrary.getImage("Assets/button_down.png"),16);
		submit.setPreferredSize(new Dimension(200,30));
		add(submit,gbc);
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(name.getText().isEmpty() == true){
					JOptionPane.showMessageDialog(null, "Name field is empty. Please enter your name.", "Empty field Error", JOptionPane.NO_OPTION);
				}
				else if(hometownField.getText().isEmpty() == true){
					JOptionPane.showMessageDialog(null, "Hometown field is empty. Please enter an address or zip code of your hometown.", "Empty field Error", JOptionPane.NO_OPTION);
				}
				else{
					String fname;
					String lname = null;
					String info;
					Scanner sc = new Scanner(name.getText());
					fname = sc.next();
					if(sc.hasNext()){
						lname = sc.nextLine();
						lname = lname.substring(1);
					}
					sc.close();
					String hometownText = hometownField.getText();
					longlatService.getLongitudeLatitude(hometownText);
					String latitude = longlatService.getLatitude();
					String longitude = longlatService.getLongitude();
					if (latitude == null || longitude == null) {
						JOptionPane.showMessageDialog(null, "Please enter a more descriptive hometown location.", "Registration Error", JOptionPane.NO_OPTION);
	                }
					else {
						placesSearch.setLatLong(latitude, longitude);
						placesSearch.getPlaces("", 1000);
						Vector<String> vicinities = placesSearch.getPlaceVicinities();
						if (vicinities.size() == 0) {
							JOptionPane.showMessageDialog(null, "Please enter a more descriptive hometown location.", "Registration Error", JOptionPane.NO_OPTION);
						}
						else {
							String vicinity = vicinities.get(0);
							if (vicinity.equals("N/A")) {
								JOptionPane.showMessageDialog(null, "Please enter a more descriptive hometown location.", "Registration Error", JOptionPane.NO_OPTION);
							}
							else {
								if (vicinity.contains(",")) {
									String[] split = vicinity.split(",");
									info = split[1].substring(1);
								}
								else {
									info = vicinity;
								}
								
								String concatanate = "NEWUSER: " + fname + "|" + lname + "|" + info + "|" + userName + "|" + passWord;
								clientPanel.sendMessage(concatanate);
								String concatanate1 = "NEWUSER1: " + userName + "|" + Outdoor.getValue() + "|" + Food.getValue() + "|" + Nightlife.getValue() + "|" + Shopping.getValue();
								clientPanel.sendMessage(concatanate1);
							
								clientPanel.checkUserCredentials(userName, passWord);
							} 
						}	
					}
				}
			}
			
		});
	}


}
