package org.example;
//hh
import java.time.LocalDate;

public class CampsiteSystem {
    private myList<Campsite> campsites = new myList<>();
    private myList<Guest> guests = new myList<>();
    private int nextGuestId = 1;
    private int nextBookingId = 1;

    // ---------- Campsites ----------
    public void addCampsite(Campsite campsite) {
        if (findCampsite(campsite.getName()) != null) {
            throw new IllegalArgumentException("Campsite already exists: " + campsite.getName());
        }
        campsites.add(campsite);
    }

    public Campsite findCampsite(String name) {
        for (Campsite c : campsites) {
            if (c.getName().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    // ---------- Guests ----------
    public Guest addGuest(String name, LocalDate dob, String address,
                          String phone, String email, String notes) {
        Guest g = new Guest(nextGuestId, name, dob, address, phone, email, notes);
        nextGuestId++;
        guests.add(g);
        return g;
    }

    public Guest findGuest(int id) {
        for (Guest g : guests) {
            if (g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    // ---------- bookings -----
    public Booking makeBooking(CampingArea area, BookingCheck request) {
        if (findGuest(request.getGuestId()) == null) {
            throw new IllegalArgumentException("No guest with id " + request.getGuestId());
        }
        for (Plot plot : area.getPlots()) {
            if (plot.matches(request)) {
                Booking b = new Booking(nextBookingId, request.getGuestId(),
                        request.getStart(), request.getEnd(),
                        request.getNumGuests(), plot);
                nextBookingId++;
                plot.addBooking(b);
                return b;
            }
        }
        return null;
    }

    // ---------- reset ----------
    public void reset() {
        campsites.clear();
        guests.clear();
        nextGuestId = 1;
        nextBookingId = 1;
    }

    public myList<Campsite> getCampsites() {
        return campsites; }
    public myList<Guest> getGuests() {
        return guests; }
}
