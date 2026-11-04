/**
 * File: Location.java
 * Author: Sanskriti Tiwari
 * Course: CS3345 Data Structures & Algorithm Analysis
 * Description: Represents a geographic location in the navigation system,
 * containing a unique integer ID, name, latitude, and longitude.
 * Serves as the primary data record for tree nodes and hash table entries.
 */
public class Location implements Comparable<Location> {
    private int locationID;
    private String name;
    private double latitude;
    private double longitude;

    /**
     * Constructs a Location with specified ID, name, latitude, and longitude.
     * @param locationID Unique integer identifier for the location.
     * @param name Name of the location.
     * @param latitude Geographic latitude coordinate.
     * @param longitude Geographic longitude coordinate.
     */
    public Location(int locationID, String name, double latitude, double longitude) {
        this.locationID = locationID;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Constructs a Location with default latitude (0.0) and longitude (0.0)
     * as specified by assignment requirements for dataset loading.
     * @param locationID Unique integer identifier.
     * @param name Name of the location (typically "Location " + locationID).
     */

    // Returns the integer key used for searching, hashing, and tree sorting.
    public Location(int locationID, String name) {
        this(locationID, name, 0.0, 0.0);
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Compares two location IDs to determine ordering in trees.
    @Override
    public int compareTo(Location other) {
        return Integer.compare(this.locationID, other.locationID);
    }

    @Override
    public String toString() {
        return "ID: " + locationID + ", Name: " + name + " (Latitude: " + latitude + ", Longitude: " + longitude + ")";
    }
}