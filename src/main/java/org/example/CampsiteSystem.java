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

    //--Cancel booking----
    public boolean cancelBooking(int bookingId) {
        for (Campsite c : campsites) {
            for (CampingArea a : c.getAreas()) {
                for (Plot p : a.getPlots()) {
                    for (Booking b : p.getBookings()) {
                        if (b.getId() == bookingId) {
                            return p.removeBooking(b);
                        }
                    }
                }
            }
        }
        return false;
    }
//--Guest record--
    public String viewGuestRecord(int guestId, LocalDate today) {
        Guest g = findGuest(guestId);
        if (g == null) {
            return "Guest not found";
        }
        String pending = "";
        String past = "";
        for (Campsite c : campsites) {
            for (CampingArea a : c.getAreas()) {
                for (Plot p : a.getPlots()) {
                    for (Booking b : p.getBookings()) {
                        if (b.getGuestId() == guestId) {
                            String line = "  #" + b.getId() + " " + c.getName() + " / " + a.getId()
                                    + " / " + p.getId() + ", " + b.getStart() + " to " + b.getEnd()
                                    + ", " + b.getNumGuests() + " guest(s), €" + b.getTotalCost() + "\n";
                            if (b.isPending(today)) {
                                pending += line;
                            } else {
                                past += line;
                            }
                        }
                    }
                }
            }
        }
        return "GUEST #" + g.getId() + "\nName: " + g.getName() + "\nDate of birth: " + g.getDateOfBirth()
                + "\nAddress: " + g.getAddress() + "\nPhone: " + g.getPhone() + "\nEmail: " + g.getEmail()
                + "\nNotes: " + g.getNotes()
                + "\n\nPENDING BOOKINGS\n" + (pending.isEmpty() ? "  none\n" : pending)
                + "\nPAST BOOKINGS\n" + (past.isEmpty() ? "  none\n" : past);
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
