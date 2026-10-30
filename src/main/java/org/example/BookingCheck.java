package org.example;

import java.time.LocalDate;

public class BookingCheck {
    private int guestId;
    private LocalDate start;
    private LocalDate end;
    private int numGuests;
    private Plot.PlotType type;      // null means any type
    private boolean needsPower;
    private double minSize;
    private int minBedrooms;
    private double maxCostPerNight;  // budget per night

    public BookingCheck(int guestId, LocalDate start, LocalDate end, int numGuests,
                          Plot.PlotType type, boolean needsPower, double minSize,
                          int minBedrooms, double maxCostPerNight) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (numGuests < 1) {
            throw new IllegalArgumentException("Need at least 1 guest");
        }
        this.guestId = guestId;
        this.start = start;
        this.end = end;
        this.numGuests = numGuests;
        this.type = type;
        this.needsPower = needsPower;
        this.minSize = minSize;
        this.minBedrooms = minBedrooms;
        this.maxCostPerNight = maxCostPerNight;
    }

    public int getGuestId() { return guestId; }
    public LocalDate getStart() { return start; }
    public LocalDate getEnd() { return end; }
    public int getNumGuests() { return numGuests; }
    public Plot.PlotType getType() { return type; }
    public boolean needsPower() { return needsPower; }
    public double getMinSize() { return minSize; }
    public int getMinBedrooms() { return minBedrooms; }
    public double getMaxCostPerNight() { return maxCostPerNight; }
}