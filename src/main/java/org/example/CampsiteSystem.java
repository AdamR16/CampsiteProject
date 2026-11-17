package org.example;
//hh
import java.time.LocalDate;

public class CampsiteSystem implements java.io.Serializable {
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


    // Pending bookings report. Pass one campsite for the campsite view, or null for the whole system.
    public String viewPendingBookings(Campsite only, LocalDate today) {
        String text = "";
        int count = 0;
        double cost = 0;
        for (Campsite c : campsites) {
            if (only != null && c != only) {
                continue;                                   // skip other campsites in the single view
            }
            text += "CAMPSITE: " + c.getName() + "\n";
            for (CampingArea a : c.getAreas()) {
                text += "  AREA: " + a.getId() + "\n";
                for (Plot p : a.getPlots()) {
                    text += "    PLOT: " + p.getId() + "\n";
                    for (Booking b : p.getBookings()) {
                        if (b.isPending(today)) {
                            Guest g = findGuest(b.getGuestId());
                            text += "      #" + b.getId() + " " + (g == null ? "unknown guest" : g.getName())
                                    + ", " + b.getStart() + " to " + b.getEnd()
                                    + ", " + b.getNumGuests() + " guest(s), " + money(b.getTotalCost()) + "\n";
                            count++;
                            cost += b.getTotalCost();
                        }
                    }
                }
            }
        }
        return text + (only == null ? "OVERALL TOTAL: " : "TOTAL: ")
                + count + " pending booking(s), " + money(cost) + "\n";
    }
    // Removes a plot, then tries to move its bookings
    public String removePlot(Campsite campsite, CampingArea area, Plot plot) {
        if (!area.removePlot(plot)) {
            throw new IllegalArgumentException("That plot is not in that area");
        }
        int moved = 0;
        String notMoved = "";
        while (plot.getBookings().size() > 0) {
            Booking b = plot.getBookings().get(0);
            plot.removeBooking(b);
            Plot replacement = findReplacement(campsite, plot, b);
            if (replacement == null) {
                notMoved += " #" + b.getId();
            } else {
                b.setPlot(replacement);
                replacement.addBooking(b);
                moved++;
            }
        }
        String message = "Plot " + plot.getId() + " removed. " + moved + " booking(s) moved.";
        if (!notMoved.isEmpty()) {
            message += " Could not move (cancelled):" + notMoved;
        }
        return message;
    }

    // Finds another plot in the campsite that suits this booking, or null
    private Plot findReplacement(Campsite campsite, Plot removed, Booking b) {
        for (CampingArea a : campsite.getAreas()) {
            for (Plot p : a.getPlots()) {
                boolean sameType = p.getType() == removed.getType();
                boolean enoughBeds = p.getType() == Plot.PlotType.EMPTY_PITCH || p.getBeds() >= b.getNumGuests();
                boolean powerOk = !removed.hasPower() || p.hasPower();
                if (sameType && enoughBeds && powerOk && p.isAvailable(b.getStart(), b.getEnd())) {
                    return p;
                }
            }
        }
        return null;
    }
    public String viewAllPendingBookings(LocalDate today) {
        return viewPendingBookings(null, today);
    }

    private String money(double amount) {
        return "€" + String.format(java.util.Locale.ENGLISH, "%.2f", amount);
    }


    public myList<Campsite> getCampsites() {
        return campsites; }
    public myList<Guest> getGuests() {
        return guests; }
}
