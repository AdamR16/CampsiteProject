package org.example;


public class Campsite {
    private String name;
    private String address;
    private String phone;
    private String email;
    private int stars;
    private myList<String> facilities = new myList<>();
    private myList<CampingArea> areas = new myList<>();

    public Campsite(String name, String address,
                 String phone, String email, int stars) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Stars must be between 1 and 5");
        }
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.stars = stars;
    }
    public void addFacility(String facility) {
        facilities.add(facility);
    }

    public boolean hasFacility(String facility) {
        for (String f : facilities) {
            if (f.equalsIgnoreCase(facility)) {
                return true;
            }
        }
        return false;
    }
    public CampingArea findArea(String areaId) {
        for (CampingArea area : areas) {
            if (area.getId().equalsIgnoreCase(areaId)) {
                return area;
            }
        }
        return null;
    }

    public void addArea(CampingArea area) {
        if (findArea(area.getId()) != null) {
            throw new IllegalArgumentException(
                    "Area '" + area.getId() + "' already exists in " + name);
        }
        areas.add(area);
    }

    public boolean removeArea(CampingArea area) {
        return areas.remove(area);
    }

    public String getName() {
        return name; }
    public String getAddress() {
        return address; }
    public String getPhone() {
        return phone; }
    public String getEmail() {
        return email; }
    public int getStars() {
        return stars; }
    public myList<String> getFacilities() {
        return facilities; }
    public myList<CampingArea> getAreas() {
        return areas; }

    public String toString() {
        return address + "," + name + ", " + email + ", " + phone + "," + stars;
    }
}
