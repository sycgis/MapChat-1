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
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import library.FontLibrary;
import library.ImageLibrary;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.DefaultMapController;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.JMapViewerTree;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.LayerGroup;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent;
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource;
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource;

import Backend.Attraction;
import Backend.User;
import Utilities.LongLatService;
import Utilities.MapMarkerLocation;
import Utilities.PlacesSearch;

public class MapScreen extends JPanel implements JMapViewerEventListener, ItemListener, Runnable{

    private static final long serialVersionUID = 1L;

    private Vector<MapMarker> mapMarkerVector;
    private final JMapViewerTree treeMap;
    private LongLatService longLatService;
    private PlacesSearch placesSearch;
    
    private PaintedPanel northPanel;
    private PaintedButton chatButton, profileInfoButton,goButton;
    private JCheckBox outdoors;
    private JCheckBox nightlife;
    private JCheckBox hotels;
    private JCheckBox shopping;
    private JCheckBox restaurant;

    int chatCounter = 0;
    private String userLocation;
    private int searchRadius;
    private String keywordType = "";
    private int screenWidth, screenHeight;
    private User currentUser;
    
    private ClientPanel clientPanel;
    private LocationRatingDialog locationRatingDialog;
    
    private Vector<Attraction> attractions;
    // Storing custom map markers
    private Vector<MapMarkerLocation> MarkerVector;
    
    private ChatScreen cs;
    
    
    public MapScreen(final ClientPanel ow) {
    	attractions = new Vector<Attraction>();
    	MarkerVector = new Vector<MapMarkerLocation>();
        currentUser = ow.getUser();
        clientPanel = ow;
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth();
        screenHeight = (int) screenSize.getHeight();

        northPanel = new PaintedPanel(ImageLibrary.getImage("Assets/theme.jpg"));
        northPanel.setPreferredSize(new Dimension(1800, 35));
		chatButton = new PaintedButton("",ImageLibrary.getImage("Assets/phonebutton.png"),
				ImageLibrary.getImage("Assets/phonebutton.png"),16);
		chatButton.setPreferredSize(new Dimension(70,25));
        chatButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent ae) {
        		if (placesSearch.getPlaceVicinities().size() > 0) {
        			if (currentUser == null) {
        				JOptionPane.showMessageDialog(
                                null, 
                                "Sorry, chatting is for registered users only!", 
                                "Error", 
                                JOptionPane.NO_OPTION
                            );
        			}
        			else{
        				clientPanel.sendMessage("FINDCHAT: " + currentUser.getUsername() + "|" + placesSearch.getPlaceVicinities().get(0));
        			}
        		}
        	}
        });
		profileInfoButton = new PaintedButton("",ImageLibrary.getImage("Assets/profile.png"),
				ImageLibrary.getImage("Assets/profile.png"),16);
		profileInfoButton.setPreferredSize(new Dimension(70,25));
        profileInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFrame f = new JFrame();
                f.setVisible(true);
                f.setSize(500, 500);
                f.setLocation(screenWidth/2-250, screenHeight/2-250);
                ProfileAboutScreen panel = new ProfileAboutScreen(ow);
                f.add(panel);
                
             // setup ProfileAboutScreen user info from db   // setup ProfileAboutScreen user info from db
                String userName;
                String userTown;
                String userUsername = "";
                
                if( currentUser != null) {
                    String userFirst = currentUser.getFirstName();
                    String userLast = currentUser.getLastName();
                    userUsername = currentUser.getUsername();
                    userTown = currentUser.getCityInfo();
                    userName = userFirst + " " + userLast;
                }
                else {
                    userName = "Guest User";
                    userTown = "";
                }
                panel.setUserInfo(userName, userUsername, userTown); 
            }   
        });
        northPanel.add(chatButton);
        northPanel.add(profileInfoButton);
        outdoors = new JCheckBox("Outdoors");
        outdoors.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/noSelected.png")));
        outdoors.setSelectedIcon(new ImageIcon(ImageLibrary.getImage("Assets/Selected.png")));
        outdoors.setOpaque(false);
        outdoors.addItemListener(this);
        outdoors.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
        nightlife = new JCheckBox("Nightlife");
        nightlife.setOpaque(false);
        nightlife.addItemListener(this);
        nightlife.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
        nightlife.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/noSelected.png")));
        nightlife.setSelectedIcon(new ImageIcon(ImageLibrary.getImage("Assets/Selected.png")));
        shopping = new JCheckBox("Shopping");
        shopping.addItemListener(this);
        shopping.setOpaque(false);
        shopping.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
        shopping.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/noSelected.png")));
        shopping.setSelectedIcon(new ImageIcon(ImageLibrary.getImage("Assets/Selected.png")));
        restaurant = new JCheckBox("Restaurants");
        restaurant.addItemListener(this);
        restaurant.setOpaque(false);
        restaurant.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
        restaurant.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/noSelected.png")));
        restaurant.setSelectedIcon(new ImageIcon(ImageLibrary.getImage("Assets/Selected.png")));
        hotels = new JCheckBox("Lodging");
        hotels.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
        hotels.addItemListener(this);
        hotels.setOpaque(false);
        hotels.setIcon(new ImageIcon(ImageLibrary.getImage("Assets/noSelected.png")));
        hotels.setSelectedIcon(new ImageIcon(ImageLibrary.getImage("Assets/Selected.png")));
        NoneSelectedButtonGroup bg = new NoneSelectedButtonGroup();
        bg.add(outdoors);
        bg.add(nightlife);
        bg.add(shopping);
        bg.add(restaurant);
        bg.add(hotels);
        northPanel.add(outdoors);
        northPanel.add(nightlife);
        northPanel.add(shopping);
        northPanel.add(restaurant);
        northPanel.add(hotels);
        
		goButton = new PaintedButton("Go",ImageLibrary.getImage("Assets/button.png"),
				ImageLibrary.getImage("Assets/button_down.png"),16);
		goButton.setPreferredSize(new Dimension(55,25));
        northPanel.add(goButton);
        
        longLatService = new LongLatService();
        placesSearch = new PlacesSearch();
        mapMarkerVector = new Vector<MapMarker>();
        treeMap = new JMapViewerTree("Zones");

        // Listen to the map viewer for user operations so components will
        // receive events and update
        map().addJMVListener(this);

        setLayout(new BorderLayout());
        map().setTileSource((TileSource) new OsmTileSource.Mapnik());
        
        add(treeMap, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);
        
        goButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                longLatService.getLongitudeLatitude(userLocation);
                
                String latitude = longLatService.getLatitude();
                String longitude = longLatService.getLongitude();
                
                placesSearch.setLatLong(latitude, longitude);
                placesSearch.getPlaces(keywordType, searchRadius);
                
                LayerGroup losAngelesGroup = new LayerGroup("Los Angeles");
                Layer losangeles = losAngelesGroup.addLayer("Los Angeles Main");
                Vector<String> latitudes = placesSearch.getPlaceLatitudes();
                Vector<String> longitudes = placesSearch.getPlaceLongitudes();
                
                // ADD to attraction vector
                if (!attractions.isEmpty()) {
                	attractions.removeAllElements();
                }
                int x = 0;
                if (latitudes.size() > 10) {
                	x = 10;
                }
                else {
                	x = latitudes.size();
                }
                for (int i = 0; i < x; i++) {
                	String name = placesSearch.getNames().get(i);
                	String address = placesSearch.getAddresses().get(i);
                	String phone = placesSearch.getPhones().get(i);
                	Attraction a = new Attraction(name, address, Double.parseDouble(longitudes.get(i)), Double.parseDouble(latitudes.get(i)));
                	a.setPhone(phone);
                	attractions.add(a);
                }
                checkForUpdatedAttractions();
                
                mapMarkerVector.removeAllElements();
                map().removeAllMapMarkers();
                if (mapMarkerVector.isEmpty()) {
                    int z = 10;
                    if (latitudes.size() < 10) {
                        z = latitudes.size();
                    }
                     for (int i = 0; i < z; i++) {
                        MapMarkerDot eberstadt = new MapMarkerDot(losangeles, Integer.toString(i+1), Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
                        map().addMapMarker(eberstadt);
                        treeMap.addLayer(losangeles);
                        mapMarkerVector.add(eberstadt);
                    }
                     map().setDisplayToFitMapMarkers();
                }       
            }
        });
        
        new DefaultMapController(map()){
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println(map.getPosition(e.getPoint()));
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Point p = e.getPoint();
                    int X = p.x+3;
                    int Y = p.y+3;
                    Iterator<MapMarker> i = mapMarkerVector.iterator();
                    Iterator<MapMarkerLocation> i2= MarkerVector.iterator();
                    int counter = 0;
                    while (i.hasNext()) {
                    	MapMarkerDot mapMarker = (MapMarkerDot) i.next();
                        Point MarkerPosition = map.getMapPosition(mapMarker.getLat(), mapMarker.getLon());
                       

                        if (MarkerPosition != null) {
                            int centerX =  MarkerPosition.x;
                            int centerY = MarkerPosition.y;

                            // calculate the radius from the touch to the center of the dot
                            double radCircle  = Math.sqrt((((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));

                            // if the radius is smaller then 23 (radius of a ball is 5), then it must be on the dot
                            if (radCircle < 8){
                                createJDialog(counter, p);
                                //System.out.println(map.getPosition(e.getPoint()));
                            }
                        }
                        counter++;
                        }
                        
                    while (i2.hasNext()) {
                    	MapMarkerLocation locationMarker = (MapMarkerLocation) i2.next();
                    	Point MarkerPositionNew = map.getMapPosition(locationMarker.getLat(), locationMarker.getLon());
                    	  if (MarkerPositionNew != null) {
                          	int centerX =  MarkerPositionNew.x;
                              int centerY = MarkerPositionNew.y;

                              // calculate the radius from the touch to the center of the dot
                              double radCircle  = Math.sqrt((((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));

                              // if the radius is smaller then 23 (radius of a ball is 5), then it must be on the dot
                              if (radCircle < 8){
                              	String userToChatWith = locationMarker.getName();
                              	if (!(userToChatWith.equalsIgnoreCase("You are here"))){
                              		clientPanel.sendMessage("CLICKCHAT: " + userToChatWith);
                              	}
                              }
                    }
                }
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // create a new popup menu
                    JPopupMenu jpm = new JPopupMenu();
                    JMenuItem changeLocation = new JMenuItem("Change Location");
                    changeLocation.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent ae) {
                            setLocation();
                        }
                    });
                    jpm.add(changeLocation);
                    JMenuItem shareLocation = new JMenuItem("Share Location");
                    final Coordinate sharePoint = (Coordinate) map.getPosition(e.getPoint());
                    shareLocation.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            int x = -1;
                              if (!MarkerVector.isEmpty()) {
                            	  
                            	  for (int i = 0; i < MarkerVector.size(); i++) {
                            		  if (MarkerVector.get(i).getName().equals("You are here")) {
                            			  map().removeMapMarker(MarkerVector.get(i));
                            			  x = i;
                                	  }
                            		
                            	  }
                            	  
                              } 
                              if (x != -1) {
                            	  MarkerVector.remove(x);
                              }
                              // WE NEED LATITUDE, LONGITUDE, + USERNAME
                              drawMarker("You are here", sharePoint.getLat(), sharePoint.getLon());
                              ow.shareLocation(ow.getUser().getUsername()
                            		  ,
                            		  Double.toString(sharePoint.getLat()), Double.toString(sharePoint.getLon()));
                              
                        }
                        
                    });
                    jpm.add(shareLocation);
    
                    jpm.show(e.getComponent(), e.getX(), e.getY());
                }
                
            }
 
            
            private void createJDialog(int i, Point p) {
                            	
            	String latitudeStr = Double.toString(attractions.get(i).getLocation()[1]);
                String longitudeStr = Double.toString(attractions.get(i).getLocation()[0]);
            	String theName = attractions.get(i).getName();
                String theAddress = attractions.get(i).getAddress();
                String thePhone = attractions.get(i).getPhone();
                String addressAndphone = theAddress + "\n" + thePhone;
               
               
                if (locationRatingDialog != null) {
                	locationRatingDialog.dispose();
                }
                locationRatingDialog = new LocationRatingDialog(theName, theAddress, thePhone, longitudeStr, latitudeStr, clientPanel);
                locationRatingDialog.setLocationAddressPhone(theName, addressAndphone);
                if (attractions.get(i).getTestimonial() != null) {
               	 	int rating = attractions.get(i).getRating();
                    String testimonial = attractions.get(i).getTestimonial();
                    locationRatingDialog.setLocationStar(rating);
                    locationRatingDialog.setExistingTestimonialMsg(testimonial);	
               }	
            }
        };  
       
        map().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    map().getAttribution().handleAttribution(e.getPoint(), true);
                }
            }
        });

        map().addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                boolean cursorHand = map().getAttribution().handleAttributionCursor(p);
                if (cursorHand) {
                    map().setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    map().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
               
            }
        });
        
        Thread t1 = new Thread(MapScreen.this);
        t1.start();
        map().setVisible(true);
    }

    JMapViewer map() {
        return treeMap.getViewer();
    }
    
    public void updateAttraction(String testimonial, int rating, int pos) {
    	if (!attractions.isEmpty()) {
    		attractions.get(pos).addTestimonial(testimonial);
    		attractions.get(pos).setAvgRating(rating);
    	}
    }
    
    public void checkForUpdatedAttractions() {
    	if (!attractions.isEmpty()) {
    		for (int i = 0; i < attractions.size(); i++) {
	        	clientPanel.sendMessage("GETATTRACTION: " + attractions.get(i).getLocation()[1] + "|" + attractions.get(i).getLocation()[0] + "|" + i);
	        }
    	}
    }
    
    private void setLocation() {
        //Dialog that allows users to set their current physical location
        final JDialog setLocationDialog = new JDialog();
        setLocationDialog.setSize(400, 160);
        setLocationDialog.setLocation(screenWidth/2-200, screenHeight/2-80);
        setLocationDialog.setResizable(false);
        Border rounded =new LineBorder(new Color(220,220,220), 4, true);
        JPanel setLocationPanel = new JPanel();
        setLocationPanel.setBackground(Color.LIGHT_GRAY);
        JLabel setLocationLabel = new JLabel("Enter your current location below:");
		setLocationLabel.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
		setLocationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel setRadiusLabel = new JLabel("Radius (in kilometers) to search:");
		setRadiusLabel.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 14));
		setRadiusLabel.setHorizontalAlignment(SwingConstants.CENTER);        
        final JTextField setLocationField = new JTextField(30);
        setLocationField.setBorder(rounded);
        setLocationField.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 12));
        setLocationField.setHorizontalAlignment(SwingConstants.CENTER);
        final JTextField setRadiusField = new JTextField(30);
        setRadiusField.setBorder(rounded);
        setRadiusField.setFont(FontLibrary.getFont("Assets/MANIFESTO.ttf", Font.PLAIN, 12));
        setRadiusField.setHorizontalAlignment(SwingConstants.CENTER);
        final PaintedButton setLocationButton = new PaintedButton("Go",ImageLibrary.getImage("Assets/button.png"),
				ImageLibrary.getImage("Assets/button_down.png"),16);
        setLocationButton.setPreferredSize(new Dimension(80,25));
        setLocationPanel.add(setLocationLabel);
        setLocationLabel.setHorizontalTextPosition(JLabel.CENTER);
        setLocationPanel.add(setLocationField);
        setLocationPanel.add(setRadiusLabel);
        setRadiusLabel.setHorizontalTextPosition(JLabel.CENTER);
        setRadiusField.setText("1");
        setLocationPanel.add(setRadiusField);
        setLocationPanel.add(setLocationButton);
        setLocationField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    setLocationButton.doClick();
                }
            }
        });
        setRadiusField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                    setLocationButton.doClick();
                }
            }
        });
        setLocationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String searchRadiusString = setRadiusField.getText();
                try {
                    searchRadius = Integer.parseInt(searchRadiusString);
                    searchRadius *= 1000;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Please enter an integer for radius. Defaulting to 1 kilometer.", 
                        "Error", 
                        JOptionPane.NO_OPTION
                    );
                    searchRadius = 1000;
                }
                userLocation = setLocationField.getText();
                longLatService.getLongitudeLatitude(userLocation);
                
                String latitude = longLatService.getLatitude();
                String longitude = longLatService.getLongitude();
                
                if (latitude == null || longitude == null) {
                	JOptionPane.showMessageDialog(null, "Invalid location entered", "Location Error", JOptionPane.NO_OPTION);
                }
                else {
                	 placesSearch.setLatLong(latitude, longitude);
                	 String s = "";
	                 placesSearch.getPlaces(s, searchRadius);
	                 
	                 LayerGroup losAngelesGroup = new LayerGroup("Los Angeles");
	                 Layer losangeles = losAngelesGroup.addLayer("Los Angeles Main");
	                 Vector<String> latitudes = placesSearch.getPlaceLatitudes();
	                 Vector<String> longitudes = placesSearch.getPlaceLongitudes();
	                 
	                 //Add to attractions vector
	                 if (!attractions.isEmpty()) {
	                 	attractions.removeAllElements();
	                 }
	                 int x = 10;
	                 if (latitudes.size() < 10) {
	                 	x = latitudes.size();
	                 }
	                 for (int i = 0; i < x; i++) {
	                 	String name = placesSearch.getNames().get(i);
	                 	String address = placesSearch.getAddresses().get(i);
	                 	String phone = placesSearch.getPhones().get(i);
	                 	Attraction a = new Attraction(name, address, Double.parseDouble(longitudes.get(i)), Double.parseDouble(latitudes.get(i)));
	                 	a.setPhone(phone);
	                 	attractions.add(a);
	                 }
	                 checkForUpdatedAttractions();
	                 	
	                 mapMarkerVector.removeAllElements();
	                 map().removeAllMapMarkers();
	                 if (mapMarkerVector.isEmpty()) {
	                     int z = 10;
	                     if (latitudes.size() < 10) {
	                         z = latitudes.size();
	                     }
	                      for (int i = 0; i < z; i++) {
	                         MapMarkerDot eberstadt = new MapMarkerDot(losangeles, Integer.toString(i+1), Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i)));
	                         map().addMapMarker(eberstadt);
	                         treeMap.addLayer(losangeles);
	                         mapMarkerVector.add(eberstadt);
	                     }
	                      map().setDisplayToFitMapMarkers();
	                 }
	                 setLocationDialog.dispose();
                }     
            }
        });
        setLocationDialog.add(setLocationPanel);
        setLocationDialog.setModalityType(ModalityType.APPLICATION_MODAL);
        setLocationDialog.setVisible(true); 
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
                command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
        }
    }
  
    public class NoneSelectedButtonGroup extends ButtonGroup {
        private static final long serialVersionUID = 1L;

        @Override
        public void setSelected(ButtonModel model, boolean selected) {
            if (selected) {
              super.setSelected(model, selected);
            } else {
              clearSelection();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if (source == outdoors) {
            keywordType = "&types=park";
        } else if (source == nightlife) {
            keywordType = "&types=night_club";
        } else if (source == shopping) {
            keywordType = "&types=store";
        } else if (source == restaurant) {
            keywordType = "&types=restaurant";
        } else if (source == hotels) {
            keywordType = "&types=lodging";
        } 

        if (e.getStateChange() == ItemEvent.DESELECTED) keywordType = "";
    }
    public void drawMarker(String title, double latitude, double longitude) {
    
    	LayerGroup losAngelesGroup = new LayerGroup("Los Angeles");
        Layer losangeles = losAngelesGroup.addLayer("Los Angeles Main");
        MapMarkerLocation sharedLocationMarker = new MapMarkerLocation(losangeles, title, latitude, longitude);
        
        // check if this exists in the vector
        int x  = -1;
        if (!MarkerVector.isEmpty()) {
	        for (int i = 0; i < MarkerVector.size(); i++) {
	        	if (MarkerVector.get(i).getName().equals(sharedLocationMarker.getName())) {
	        		 map().removeMapMarker(MarkerVector.get(i));
	        		x = i;
	        	}
	        }
	        if (x != -1) { 
	        	MarkerVector.remove(x);
	        }
        }
        
        MarkerVector.add(sharedLocationMarker);
        
        map().addMapMarker(sharedLocationMarker);
    	
    }
    @Override
    public void run() {
        setLocation();
        
    }

    public void popChatScreen(String partner){
		ChatScreen cScreen = new ChatScreen(partner, clientPanel);
		cs = cScreen;
		cs.setVisible(true);

    }
    

	public ChatScreen getChatScreen() {
		return cs;
	}    
}

