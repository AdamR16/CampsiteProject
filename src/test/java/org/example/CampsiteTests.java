package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class CampsiteTests {

    private CampsiteSystem system;
    private CampingArea area;
    private Guest guest;

    @BeforeEach
    void setUp() {
        system = new CampsiteSystem();
        Campsite campsite = new Campsite("Sunny Shores", "Tramore", "051123", "a@b.ie", 4);
        area = new CampingArea("Lake View 1", "By the lake");
        area.addPlot(new Plot("P1", Plot.PlotType.BUNGALOW, true, 2, 4, 40, 100.0, "http://x"));
        campsite.addArea(area);
        system.addCampsite(campsite);
        guest = system.addGuest("Ann Murphy", LocalDate.of(1990, 1, 1), "Cork", "087", "ann@x.ie", "");
    }

    private BookingCheck request(String start, String end) {
        return new BookingCheck(guest.getId(), LocalDate.parse(start), LocalDate.parse(end),
                2, Plot.PlotType.BUNGALOW, true, 20, 1, 150);
    }

    @Test
    void listRemoveWorks() {
        myList<String> list = new myList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.remove("B");
        assertEquals(2, list.size());
        assertEquals("C", list.get(1));
    }

    @Test
    void duplicateAreaIdIsRejected() {
        Campsite c = system.findCampsite("Sunny Shores");
        assertThrows(IllegalArgumentException.class,
                () -> c.addArea(new CampingArea("Lake View 1", "Copy")));
    }

    @Test
    void bookingGetsPlotAndCorrectCost() {
        Booking b = system.makeBooking(area, request("2026-07-01", "2026-07-05"));
        assertNotNull(b);
        assertEquals("P1", b.getPlot().getId());
        assertEquals(400.0, b.getTotalCost());
    }

    @Test
    void overlappingBookingIsRefused() {
        system.makeBooking(area, request("2026-07-01", "2026-07-05"));
        assertNull(system.makeBooking(area, request("2026-07-03", "2026-07-08")));
        assertNotNull(system.makeBooking(area, request("2026-07-05", "2026-07-08")));
    }

    @Test
    void cancelRemovesBooking() {
        Booking b = system.makeBooking(area, request("2026-07-01", "2026-07-05"));
        assertTrue(system.cancelBooking(b.getId()));
        assertEquals(0, area.findPlot("P1").getBookings().size());
    }

    @Test
    void guestRecordShowsPastBooking() {
        system.makeBooking(area, request("2026-07-01", "2026-07-05"));
        String record = system.viewGuestRecord(guest.getId(), LocalDate.of(2026, 8, 1));
        assertTrue(record.contains("Ann Murphy"));
        assertTrue(record.indexOf("2026-07-01") > record.indexOf("PAST BOOKINGS"));
    }
}
