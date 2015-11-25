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

package Backend;

import java.io.Serializable;
import java.util.Vector;

public class Attraction implements Serializable {
	@Override
	public String toString() {
		return "Attraction [name=" + name + ", currTestimonial="
				+ currTestimonial + ", longitude=" + longitude + ", lattitude="
				+ lattitude + ", averageRating=" + averageRating + "]";
	}

	public static final long serialVersionUID = 1; 
 
	private String name;
	private String phone; 
	private Vector <String> keywords; 
	private Vector <String> testimonials; 
	private String currTestimonial; 
	private String address; 
	private double longitude; 
	private double lattitude; 
	private Vector <Integer> ratings; 
	private int averageRating; 
	private boolean shown; 
	
	public Attraction (String name, String address, double longitude, double lattitude) {
		this.name = name; 
		this.address = address; 
		this.longitude = longitude; 
		this.lattitude = lattitude; 
		keywords = new Vector<String>();
		testimonials = new Vector<String>();
		ratings = new Vector<Integer>();
	}
	public void setRandomImage () {
		
	}
	
	public void setPhone (String phone) {
		this.phone = phone;
	}
	
	public String getPhone () {
		return phone; 
	}
	
	public String getName () {
		return name; 
	}
	
	public Vector <String> getKeywords () {
		return keywords; 
	}
	
	public String getTestimonial () {
		if (testimonials.size() > 0) {
			String s = testimonials.get(testimonials.size()-1);
			
			return s;
		}
		else return null; 
	}
	
	public void setTestimonials(Vector<String> s) {
		testimonials = s;
	}
	
	public String getAddress () {
		return address; 
	}
	
	public int getRating () {
		return averageRating; 
	}
	
	public void addRating (int newestRating) {
		ratings.add(newestRating); 
	}
	public void setAvgRating(int newRating) {
		averageRating = newRating;
	}
	
	public void addTestimonial (String testimonial) {
		testimonials.add(testimonial); 
	}
	
	public void updateAttraction (double longitude, double lattitude) {
		this.longitude = longitude; 
		this.lattitude = lattitude; 
	}
	
	public void setShown (boolean shown) {
		this.shown = shown; 
	}
	
	public boolean isShown () {
		return shown; 
	}
	
	public double [] getLocation () {
		double [] result = new double[2]; 
		result[0] = longitude; 
		result[1] = lattitude; 
		return result; 
	}
}
