package main;

import java.io.StringReader;
import java.net.URLEncoder;
 
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
 
public class LongLatService {
     
    private static final String GEOCODE_REQUEST_URL = "https://maps.googleapis.com/maps/api/geocode/xml?";
    private static HttpClient httpClient = HttpClients.createDefault();
    private String latitude;
    private String longitude;
     
    public void getLongitudeLatitude(String address) {
        try {
            StringBuilder urlBuilder = new StringBuilder(GEOCODE_REQUEST_URL);
            if (!address.isEmpty()) {
            	urlBuilder.append("&address=").append(URLEncoder.encode(address, "UTF-8"));
                urlBuilder.append("&key=AIzaSyBW2mm-Zap88-4Fkfj6Ug0_cdQCzG2n49c"); //API key
            }
 
            final HttpGet getMethod = new HttpGet(urlBuilder.toString());
            try {
            	HttpResponse response = httpClient.execute(getMethod);
            	HttpEntity entity = response.getEntity();
            	String result = EntityUtils.toString(entity, "UTF-8");
            	//System.out.println(result);
 
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(result));
                Document doc = db.parse(is);
             
                String strLatitude = getXpathValue(doc, "//GeocodeResponse/result/geometry/location/lat/text()");
                System.out.println("Latitude: " + strLatitude);
                latitude = strLatitude;
                 
                String strLongitude = getXpathValue(doc,"//GeocodeResponse/result/geometry/location/lng/text()");
                System.out.println("Longitude: " + strLongitude + "\n");
                longitude = strLongitude;
                 
                 
            } finally {
                getMethod.releaseConnection();
            }
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    
    public String getLatitude() {
    	return latitude;
    }
    
    public String getLongitude() {
    	return longitude;
    }
 
    private String getXpathValue(Document doc, String strXpath) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xPath.compile(strXpath);
        String resultData = null;
        Object result4 = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result4;
        for (int i = 0; i < nodes.getLength(); i++) {
            resultData = nodes.item(i).getNodeValue();
        }
        return resultData;
    }
     
}
