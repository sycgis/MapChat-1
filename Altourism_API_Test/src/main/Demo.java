package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
 
public class Demo extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel northPanel;
	private JTextField jtf;
	private JButton goButton, locationsButton;
	private LongLatService lls;
	private PlacesSearch ps;
	private JLabel imageLabel;

	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		setLocation(width/2-315, height/2-300);
		setResizable(false);
		setLayout(new BorderLayout());
		setTitle("API Demo");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(630,600);
		
		northPanel = new JPanel();
		jtf = new JTextField(42);
		jtf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					goButton.doClick();
				}
			}
		});
		goButton = new JButton("Go");
		locationsButton = new JButton("Details");
		imageLabel = new JLabel();
		northPanel.add(jtf);
		northPanel.add(goButton);
		northPanel.add(locationsButton);
		add(northPanel, BorderLayout.NORTH);
		add(imageLabel);
		
		lls = new LongLatService();
		ps = new PlacesSearch();
		
		locationsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JDialog jd = new JDialog();
				JTabbedPane jtp = ps.getLocationsPane();
				jd.add(jtp);
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = (int) screenSize.getWidth();
				int height = (int) screenSize.getHeight();
				jd.setLocation(width/2-175, height/2-100);
				jd.setResizable(false);
				jd.setSize(350,200);
				jd.setTitle("API Demo");
				jd.setVisible(true);
			}
		});
		
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String s = jtf.getText();
				lls.getLongitudeLatitude(s);
				
				try {
		            String latitude = lls.getLatitude();
		            String longitude = lls.getLongitude();
		            
		            ps.setLatLong(latitude, longitude);
		            ps.getPlaces();
		 
		            /*String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?center="
		                    + latitude
		                    + ","
		                    + longitude
		                    + "&zoom=15&size=612x612&scale=2&maptype=roadmap";*/
		            String imageUrl = "https://maps.googleapis.com/maps/api/staticmap?&size=612x612&scale=2&maptype=roadmap";
		            for (int i = 0; i < 10; i++) {
		            	imageUrl = imageUrl + "&markers=color:blue%7Clabel:" + i + "%7C" 
		            			+ ps.getPlaceLatitudes().get(i) + "," + ps.getPlaceLongitudes().get(i); 
		            }
		            String destinationFile = "image.jpg";
		            //if empty string then it shows Mumbai because of 'Null Bazar' lol
		 
		            // read the map image from Google
		            // then save it to a local file: image.jpg
		            URL url = new URL(imageUrl);
		            InputStream is = url.openStream();
		            OutputStream os = new FileOutputStream(destinationFile);
		 
		            byte[] b = new byte[2048];
		            int length;
		 
		            while ((length = is.read(b)) != -1) {
		                os.write(b, 0, length);
		            }
		 
		            is.close();
		            os.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		            System.exit(1);
		        }
		 
		        // create a GUI component that loads the image: image.jpg
		        ImageIcon imageIcon = new ImageIcon((new ImageIcon("image.jpg"))
		                .getImage().getScaledInstance(630, 600,
		                        java.awt.Image.SCALE_SMOOTH));
		        imageLabel.setIcon(imageIcon);
		        //repaint();
		        //pack();
			}
		});
	}
	
    public static void main(String[] args) { 
    	Demo d = new Demo();
    	d.setVisible(true);
    }
}
