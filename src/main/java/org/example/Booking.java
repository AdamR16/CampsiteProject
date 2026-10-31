package org.example;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Booking {
    private int id;
    private int guestId;
    private LocalDate start;
    private LocalDate end;
    private int numGuests;
    private Plot plot;

    public Booking(int id, int guestId, LocalDate start, LocalDate end,
                   int numGuests, Plot plot) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        this.id = id;
        this.guestId = guestId;
        this.start = start;
        this.end = end;
        this.numGuests = numGuests;
        this.plot = plot;
    }

    public long getNights() {
        return ChronoUnit.DAYS.between(start, end);
    }

    public double getTotalCost() {
        return getNights() * plot.getCostPerNight();
    }

    public int getId() {
        return id; }
    public int getGuestId() {
        return guestId; }
    public LocalDate getStart() {
        return start; }
    public LocalDate getEnd() {
        return end; }
    public int getNumGuests() {
        return numGuests; }
    public Plot getPlot() {
        return plot; }
    public void setPlot(Plot plot) {
        this.plot = plot; }

    public String toString() {
        return "Booking #" + id + ": guest " + guestId + ", plot " + plot.getId()
                + ", " + start + " to " + end + ", €" + getTotalCost();
    }
    public boolean isPending(LocalDate today) {
        return !end.isBefore(today);
    }
}
