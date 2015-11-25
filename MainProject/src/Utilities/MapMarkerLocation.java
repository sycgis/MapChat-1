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

import java.awt.Color;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

/**
 * A simple implementation of the {@link MapMarker} interface. Each map marker
 * is painted as a circle with a black border line and filled with a specified
 * color.
 *
 * @author Jan Peter Stotz
 *
 */
public class MapMarkerLocation extends MapMarkerCircle {
	private String name;

    public static final int DOT_RADIUS = 5;

    public MapMarkerLocation(Coordinate coord) {
        this(null, null, coord);
    }

    public MapMarkerLocation(String name, Coordinate coord) {
        this(null, name, coord);
    }

    public MapMarkerLocation(Layer layer, Coordinate coord) {
        this(layer, null, coord);
    }

    public MapMarkerLocation(Layer layer, String name, Coordinate coord) {
        this(layer, name, coord, getDefaultStyle());
    }

    public MapMarkerLocation(Layer layer, String name, double lat, double lon) {
        this(layer, name, new Coordinate(lat, lon), getDefaultStyle());
        this.name = name;
    }
    public String getName() { 
    	return this.name;
    }

    public MapMarkerLocation(Layer layer, String name, Coordinate coord, Style style) {
        super(layer, name, coord, DOT_RADIUS, STYLE.FIXED, style);
    }

    public static Style getDefaultStyle() {
        return new Style(Color.BLACK, Color.BLUE, null, getDefaultFont());
    }
}
