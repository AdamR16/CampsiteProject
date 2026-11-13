package org.example;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.time.LocalDate;
import javafx.scene.control.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

public class MainController {

    @FXML
    private ComboBox<Campsite> areaCampsiteBox;
    @FXML
    private TextField areaDesc;
    @FXML
    private TextField areaID;
    @FXML
    private TextField bedrooms;
    @FXML
    private TextField beds;
    @FXML
    private ComboBox<CampingArea> bookArea;
    @FXML
    private ComboBox<Campsite> bookCampsiteBox;
    @FXML
    private TextField bookGuestID;
    @FXML
    private CheckBox bookPower;
    @FXML
    private ComboBox<Plot.PlotType> bookType;
    @FXML
    private TextField campsiteAddress;
    @FXML
    private TextField campsiteEmail;
    @FXML
    private TextField campsiteFacilities;
    @FXML
    private TextField campsiteName;
    @FXML
    private TextField campsitePhone;

    @FXML
    private TextField cancelId;

    @FXML
    private TextField cost;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private DatePicker endPicker;
    @FXML
    private TextField guestAddress;
    @FXML
    private TextField guestEmail;
    @FXML
    private TextField guestLookup;
    @FXML
    private TextField guestName;
    @FXML
    private TextField guestNotes;
    @FXML
    private TextField guestPhone;
    @FXML
    private TextArea guestRecord;

    @FXML
    private TextField minBedrooms;
    @FXML
    private TextField minSize;
    @FXML
    private TextField numGuests;
    @FXML
    private TextField photo;

    @FXML
    private ComboBox<CampingArea> plotAreaBox;
    @FXML
    private ComboBox<Campsite> plotCampsiteBox;
    @FXML
    private TextField plotFacilities;
    @FXML
    private TextField plotId;
    @FXML
    private ComboBox<Plot.PlotType> plotTypeBox;

    @FXML
    private CheckBox powerCheck;
    @FXML
    private TextField size;
    @FXML
    private ComboBox<Integer> starsBox;
    @FXML
    private DatePicker startPicker;
    @FXML
    private TreeView<Object> tree;
    @FXML
    private TextField budget;

    @FXML
    void BookCampsiteChsen(ActionEvent event) {
        loadAreas(bookArea, bookCampsiteBox);
    }

    @FXML
    void onAddArea(ActionEvent event) {
        areaCampsiteBox.getValue().addArea(new CampingArea(areaID.getText(), areaDesc.getText()));
        refresh();
    }

    @FXML
    void onAddCampsite(ActionEvent event) {
        Campsite c = new Campsite(campsiteName.getText(), campsiteAddress.getText(),
                campsitePhone.getText(), campsiteEmail.getText(), starsBox.getValue());
        addFacilities(c.getFacilities(), campsiteFacilities.getText());
        system.addCampsite(c);
        refresh();
    }

    @FXML
    void onAddGuest(ActionEvent event) {
        Guest g = system.addGuest(guestName.getText(), dateOfBirthPicker.getValue(),
                guestAddress.getText(), guestPhone.getText(),
                guestEmail.getText(), guestNotes.getText());
        show("Guest added with id " + g.getId());
    }

    @FXML
    void onAddPlot(ActionEvent event) {
        Plot plot = new Plot(plotId.getText(), plotTypeBox.getValue(), powerCheck.isSelected(),
                Integer.parseInt(bedrooms.getText()), Integer.parseInt(beds.getText()),
                Double.parseDouble(size.getText()), Double.parseDouble(cost.getText()),
                photo.getText());
        addFacilities(plot.getFacilities(), plotFacilities.getText());
        plotAreaBox.getValue().addPlot(plot);
        refreshTree();
    }

    @FXML
    void onCancelBooking(ActionEvent event) {
            show(system.cancelBooking(Integer.parseInt(cancelId.getText()))
                    ? "Booking cancelled" : "No booking with that id");
            refreshTree();
    }

    @FXML
    void onMakeBooking(ActionEvent event) {
        BookingCheck request = new BookingCheck(Integer.parseInt(bookGuestID.getText()),
                startPicker.getValue(), endPicker.getValue(),
                Integer.parseInt(numGuests.getText()), bookType.getValue(),
                bookPower.isSelected(), Double.parseDouble(minSize.getText()),
                Integer.parseInt(minBedrooms.getText()), Double.parseDouble(budget.getText()));
        Booking b = system.makeBooking(bookArea.getValue(), request);
        if (b == null) {
            show("No suitable plot is available in that area for those dates.");
        } else {
            show("Booked plot " + b.getPlot().getId() + "\nBooking id: " + b.getId()
                    + "\nTotal: €" + b.getTotalCost());
            refreshTree();
        }
    }

    @FXML
    void onPlotCampsiteChosen(ActionEvent event) {
        loadAreas(plotAreaBox, plotCampsiteBox);
    }

    @FXML
    void onReset(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "This deletes ALL data. Continue?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES) {
            system.reset();
            refreshTree();
        }
    }

    @FXML
    void onViewGuest(ActionEvent event) {
        guestRecord.setText(system.viewGuestRecord(
                Integer.parseInt(guestLookup.getText()), LocalDate.now()));
    }
//---------------------SceneBuilder Skeleton completed, further methods below---
    // All the data lives here, not in the JavaFX controls
    private CampsiteSystem system = new CampsiteSystem();

    @FXML
    void initialize() {
        for (int i = 1; i <= 5; i++) {
            starsBox.getItems().add(i);
        }
        for (Plot.PlotType t : Plot.PlotType.values()) {
            plotTypeBox.getItems().add(t);
            bookType.getItems().add(t);
        }
        refresh();
    }

    private void refreshTree() {
        TreeItem<Object> root = new TreeItem<>("Campsites");
        for (Campsite c : system.getCampsites()) {
            TreeItem<Object> campsiteItem = new TreeItem<>(c);
            for (CampingArea a : c.getAreas()) {
                TreeItem<Object> areaItem = new TreeItem<>(a);
                for (Plot p : a.getPlots()) {
                    TreeItem<Object> plotItem = new TreeItem<>(p);
                    for (Booking b : p.getBookings()) {
                        plotItem.getChildren().add(new TreeItem<Object>(b));
                    }
                    areaItem.getChildren().add(plotItem);
                }
                campsiteItem.getChildren().add(areaItem);
            }
            root.getChildren().add(campsiteItem);
        }
        root.setExpanded(true);
        tree.setRoot(root);
    }

    private void refresh() {
        refreshTree();
        loadCampsites(areaCampsiteBox);
        loadCampsites(plotCampsiteBox);
        loadCampsites(bookCampsiteBox);
    }

    private void show(String message) {
        new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
    }
    private void addFacilities(myList<String> list, String text) {
        String current = "";
        for (int i = 0; i <= text.length(); i++) {
            if (i == text.length() || text.charAt(i) == ',') {
                if (!current.isBlank()) {
                    list.add(current.trim());
                }
                current = "";
            } else {
                current += text.charAt(i);
            }
        }
    }
    private void loadCampsites(ComboBox<Campsite> box) {
        box.setValue(null);
        box.getItems().clear();
        for (Campsite c : system.getCampsites()) {
            box.getItems().add(c);
        }
    }
    private void loadAreas(ComboBox<CampingArea> areaBox, ComboBox<Campsite> campsiteBox) {
        areaBox.setValue(null);
        areaBox.getItems().clear();
        if (campsiteBox.getValue() != null) {
            for (CampingArea a : campsiteBox.getValue().getAreas()) {
                areaBox.getItems().add(a);
            }
        }
    }
}
