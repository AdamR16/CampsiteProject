package org.example;

public class CampingArea {
    private String id;
    private String description;
    private myList<Plot> plots = new myList<>();

    public CampingArea(String id, String description) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Area id cannot be empty");
        }
        this.id = id;
        this.description = description;
    }

    public Plot findPlot(String plotId) {
        for (Plot p : plots) {
            if (p.getId().equalsIgnoreCase(plotId)) {
                return p;
            }
        }
        return null;
    }

    public void addPlot(Plot plot) {
        if (findPlot(plot.getId()) != null) {
            throw new IllegalArgumentException(
                    "Plot '" + plot.getId() + "' already exists in " + id);
        }
        plots.add(plot);
    }

    public boolean removePlot(Plot plot) {
        return plots.remove(plot);
    }

    public String getId() {
        return id; }
    public String getDescription() {
        return description; }
    public myList<Plot> getPlots() {
        return plots; }

    public String toString() {
        return id + " - " + description;
    }
}