package main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

public class DemoJMap extends JFrame implements JMapViewerEventListener {

    private static final long serialVersionUID = 1L;

    private Vector<MapMarker> mapMarkerVector;
    private final JMapViewerTree treeMap;
    
    private JPanel northPanel;
	private JTextField jtf;
	private JButton goButton;
	private LongLatService lls;
	private PlacesSearch ps;
	private JLabel imageLabel;

    public DemoJMap() {
        super("JMapViewer Demo");
        setSize(400, 400);

        northPanel = new JPanel();
		jtf = new JTextField(50);
		jtf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					goButton.doClick();
				}
			}
		});
		goButton = new JButton("Go");
		imageLabel = new JLabel();
		northPanel.add(jtf);
		northPanel.add(goButton);
		add(imageLabel);
		
		lls = new LongLatService();
		ps = new PlacesSearch();
		mapMarkerVector = new Vector<MapMarker>();
        treeMap = new JMapViewerTree("Zones");

        // Listen to the map viewer for user operations so components will
        // receive events and update
        map().addJMVListener(this);

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        map().setTileSource((TileSource) new OsmTileSource.Mapnik());
        
        add(treeMap, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);
		
    	goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String s = jtf.getText();
				lls.getLongitudeLatitude(s);
				
	            String latitude = lls.getLatitude();
	            String longitude = lls.getLongitude();
	            
	            ps.setLatLong(latitude, longitude);
	            ps.getPlaces();
	            
	            LayerGroup losAngelesGroup = new LayerGroup("Los Angeles");
	            Layer losangeles = losAngelesGroup.addLayer("Los Angeles Main");
	            Vector<String> latitudes = ps.getPlaceLatitudes();
	            Vector<String> longitudes = ps.getPlaceLongitudes();
	            
	            mapMarkerVector.removeAllElements();
	            map().removeAllMapMarkers();
	            if (mapMarkerVector.isEmpty()) {
	            	 for (int i = 0; i < 10; i++) {
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
            	    int counter = 0;
            	    while (i.hasNext()) {
            	        MapMarkerDot mapMarker = (MapMarkerDot) i.next();
            	        Point MarkerPosition = map.getMapPosition(mapMarker.getLat(), mapMarker.getLon());

            	        if (MarkerPosition != null) {
            	            int centerX =  MarkerPosition.x;
            	            int centerY = MarkerPosition.y;

            	            // calculate the radius from the touch to the center of the dot
            	            double radCircle  = Math.sqrt( (((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));

            	            // if the radius is smaller then 23 (radius of a ball is 5), then it must be on the dot
            	            if (radCircle < 8){
            	            	createJDialog(counter, p);
            	            	//System.out.println(map.getPosition(e.getPoint()));
            	            }
            	        }
            	        counter++;
            	    }
            	}
            }

			private void createJDialog(int i, Point p) {
				JDialog jd = new JDialog();
				int pointX = p.x;
				int pointY = p.y;
				jd.setLocation(pointX-2, pointY-38);
				jd.setResizable(false);
				jd.setSize(400,100);
				jd.setTitle("Location Details");
				
				JPanel jp = new JPanel(new GridLayout(3,1));
	    		JLabel nameLabel = new JLabel(ps.getNames().get(i));
	    		nameLabel.setHorizontalAlignment(JLabel.CENTER);
	    		JLabel addressLabel = new JLabel(ps.getAddresses().get(i));
	    		addressLabel.setHorizontalAlignment(JLabel.CENTER);
	    		JLabel phoneLabel = new JLabel(ps.getPhones().get(i));
	    		phoneLabel.setHorizontalAlignment(JLabel.CENTER);
	    		jp.add(nameLabel);
	    		jp.add(addressLabel);
	    		jp.add(phoneLabel);
	    		jd.add(jp);
	    		jd.setModalityType(ModalityType.APPLICATION_MODAL);
				jd.setVisible(true);
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
    }

    private JMapViewer map() {
        return treeMap.getViewer();
    }

    /**
     * @param args Main program arguments
     */
    public static void main(String[] args) {
        new DemoJMap().setVisible(true);
    }

    @Override
    public void processCommand(JMVCommandEvent command) {
        if (command.getCommand().equals(JMVCommandEvent.COMMAND.ZOOM) ||
                command.getCommand().equals(JMVCommandEvent.COMMAND.MOVE)) {
            
        }
    }
}
