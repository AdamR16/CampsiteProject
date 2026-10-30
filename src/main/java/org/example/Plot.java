package org.example;
import java.time.LocalDate;

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


    public boolean isAvailable(LocalDate start, LocalDate end) {
        for (Booking b : bookings) {
            if (start.isBefore(b.getEnd()) && b.getStart().isBefore(end)) {
                return false;
            }
        }
        return true;
    }
    public boolean matches(BookingCheck r) {
        if (r.getType() != null && type != r.getType()) {
            return false;
        }
        if (r.needsPower() && !hasPower) {
            return false;
        }
        if (sizeM2 < r.getMinSize()) {
            return false;
        }
        if (bedrooms < r.getMinBedrooms()) {
            return false;
        }
        if (costPerNight > r.getMaxCostPerNight()) {
            return false;
        }
        if (type != PlotType.EMPTY_PITCH && beds < r.getNumGuests()) {
            return false;
        }
        return isAvailable(r.getStart(), r.getEnd());
    }

    public String toString() {
        return id + " (" + type + ", " + sizeM2 + "m2, €" + costPerNight + "/night)";
    }
}
