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

package Utilities;

import java.awt.GridLayout;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class PlacesSearch {
	
	private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/xml?";
	private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/xml?";
    private static HttpClient httpClient = HttpClients.createDefault();
    private String latitude;
    private String longitude;
    private Vector<String> placeNames = new Vector<String>();
    private Vector<String> placeIDs = new Vector<String>();
    private Vector<String> placeAddresses = new Vector<String>();
    private Vector<String> placePhones = new Vector<String>();
    private Vector<String> placeLatitudes = new Vector<String>();
    private Vector<String> placeLongitudes = new Vector<String>();
    private Vector<String> placeVicinities = new Vector<String>();

    public Vector<String> getPlaceLatitudes() {
    	return placeLatitudes;
    }
    
    public Vector<String> getPlaceLongitudes() {
    	return placeLongitudes;
    }
    
    public Vector<String> getNames() {
    	return placeNames;
    }
    
    public Vector<String> getIDs() {
    	return placeIDs;
    }
    
    public Vector<String> getAddresses() {
    	return placeAddresses;
    }
    
    public Vector<String> getPhones() {
    	return placePhones;
    }
    
    public Vector<String> getPlaceVicinities() {
    	return placeVicinities;
    }
    
    public void setLatLong(String latitude, String longitude) {
    	this.latitude = latitude;
    	this.longitude = longitude;
    }
   
    public void getPlaces(String type, int radius) {
        try {
        	placeNames.clear();
        	placeIDs.clear();
        	placeAddresses.clear();
        	placePhones.clear();
        	placeLatitudes.clear();
        	placeLongitudes.clear();
        	placeVicinities.clear();
        	
            StringBuilder urlBuilder = new StringBuilder(PLACES_SEARCH_URL);
            urlBuilder.append("&location=").append(URLEncoder.encode(latitude, "UTF-8"));
            urlBuilder.append(",").append(URLEncoder.encode(longitude, "UTF-8"));
            urlBuilder.append("&radius=" + radius); //radius in meters, default rank is prominence
            urlBuilder.append(type);
            //append &type to include types of places like food, night_club, shopping_mall
            //https://developers.google.com/places/supported_types
            urlBuilder.append("&key=AIzaSyBW2mm-Zap88-4Fkfj6Ug0_cdQCzG2n49c"); //API key
 
            final HttpGet getMethod = new HttpGet(urlBuilder.toString());
            try {
            	HttpResponse response = httpClient.execute(getMethod);
            	HttpEntity entity = response.getEntity();
            	String result = EntityUtils.toString(entity, "UTF-8");
 
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(result));
                Document doc = db.parse(is);
             
                setXpathValuesToVector(doc, "//PlaceSearchResponse/result/name/text()", placeNames);
                setXpathValuesToVector(doc, "//PlaceSearchResponse/result/place_id/text()", placeIDs);
                setXpathValuesToVector(doc, "//PlaceSearchResponse/result/geometry/location/lat/text()", placeLatitudes);
                setXpathValuesToVector(doc, "//PlaceSearchResponse/result/geometry/location/lng/text()", placeLongitudes);
                
                for (int i = 0; i < placeIDs.size(); i++) {
                	try {
                        StringBuilder urlBuilder2 = new StringBuilder(PLACES_DETAILS_URL);
                        urlBuilder2.append("&placeid=").append(URLEncoder.encode(placeIDs.get(i), "UTF-8"));
                        urlBuilder2.append("&key=AIzaSyBW2mm-Zap88-4Fkfj6Ug0_cdQCzG2n49c"); //API key
             
                        final HttpGet getMethod2 = new HttpGet(urlBuilder2.toString());
                        try {
                        	HttpResponse response2 = httpClient.execute(getMethod2);
                        	HttpEntity entity2 = response2.getEntity();
                        	String result2 = EntityUtils.toString(entity2, "UTF-8");
             
                            DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
                            DocumentBuilder db2 = dbf2.newDocumentBuilder();
                            InputSource is2 = new InputSource();
                            is2.setCharacterStream(new StringReader(result2));
                            Document doc2 = db2.parse(is2);
                         
                            setXpathValuesToVector(doc2, "//PlaceDetailsResponse/result/formatted_address/text()", placeAddresses);
                            setXpathValuesToVector(doc2, "//PlaceDetailsResponse/result/formatted_phone_number/text()", placePhones);
                            setXpathValuesToVector(doc2, "//PlaceDetailsResponse/result/vicinity/text()", placeVicinities);
                            
                        } finally {
                            getMethod.releaseConnection();
                        }
                    } catch (Exception e) {
                         e.printStackTrace();
                    }
                }
                
            } finally {
                getMethod.releaseConnection();
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    private void setXpathValuesToVector(Document doc, String strXpath, Vector<String> vector) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xPath.compile(strXpath);
        String resultData = "N/A";
        Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result4;
        if (nodes.getLength() == 0) {
        	vector.add(resultData);
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            resultData = nodes.item(i).getNodeValue();
            vector.add(resultData);
        }
    }
    
    public JTabbedPane getLocationsPane() {
    	JTabbedPane jtp = new JTabbedPane(JTabbedPane.TOP);
    	for (int i = 0; i < 10; i++) {
    		JPanel jp = new JPanel(new GridLayout(3,1));
    		JLabel nameLabel = new JLabel(placeNames.get(i));
    		nameLabel.setHorizontalAlignment(JLabel.CENTER);
    		JLabel addressLabel = new JLabel(placeAddresses.get(i));
    		addressLabel.setHorizontalAlignment(JLabel.CENTER);
    		JLabel phoneLabel = new JLabel(placePhones.get(i));
    		phoneLabel.setHorizontalAlignment(JLabel.CENTER);
    		jp.add(nameLabel);
    		jp.add(addressLabel);
    		jp.add(phoneLabel);
    		jtp.add(jp);
    		jtp.setTitleAt(i, "" + i);
    	}
    	return jtp;
    }
}
