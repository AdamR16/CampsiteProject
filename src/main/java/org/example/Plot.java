package org.example;

public class Plot {

    public enum PlotType { STATIC_HOME, BUNGALOW, APARTMENT, EMPTY_PITCH }

    private String id;
    private PlotType type;
    private boolean hasPower;
    private int bedrooms;
    private int beds;
    private double sizeM2;
    private double costPerNight;
    private String photoUrl;
    private myList<String> facilities = new myList<>();
    private myList<Booking> bookings = new myList<>();

    public Plot(String id, PlotType type, boolean hasPower, int bedrooms, int beds,
                double sizeM2, double costPerNight, String photoUrl) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Plot id cannot be empty");
        }
        if (costPerNight <= 0) {
            throw new IllegalArgumentException("Cost must be more than 0");
        }
        this.id = id;
        this.type = type;
        this.bedrooms = bedrooms;
        this.beds = beds;
        this.sizeM2 = sizeM2;
        this.costPerNight = costPerNight;
        this.photoUrl = photoUrl;


        if (type == PlotType.EMPTY_PITCH) {
            this.hasPower = hasPower;
        } else {
            this.hasPower = true;
        }
    }

    public void addFacility(String facility) {
        facilities.add(facility);
    }


    public void addBooking(Booking b) { bookings.add(b); }
    public boolean removeBooking(Booking b) { return bookings.remove(b); }

    public String getId() {
        return id; }
    public PlotType getType() {
        return type; }
    public boolean hasPower() {
        return hasPower; }
    public int getBedrooms() {
        return bedrooms; }
    public int getBeds() {
        return beds; }
    public double getSizeM2() {
        return sizeM2; }
    public double getCostPerNight() {
        return costPerNight; }
    public String getPhotoUrl() {
        return photoUrl; }
    public myList<String> getFacilities() {
        return facilities; }
    public myList<Booking> getBookings() {
        return bookings; }

    public String toString() {
        return id + " (" + type + ", " + sizeM2 + "m2, €" + costPerNight + "/night)";
    }
}
