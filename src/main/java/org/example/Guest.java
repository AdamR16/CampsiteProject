package org.example;

import java.time.LocalDate;

public class Guest {
    private int id;
    private String name;
    private LocalDate dateOfBirth;
    private String address;
    private String phone;
    private String email;
    private String notes;

    public Guest(int id, String name, LocalDate dateOfBirth, String address,
                 String phone, String email, String notes) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.notes = notes;
    }

    public int getId() {
        return id; }
    public String getName() {
        return name; }
    public LocalDate getDateOfBirth() {
        return dateOfBirth; }
    public String getAddress() {
        return address; }
    public String getPhone() {
        return phone; }
    public String getEmail() {
        return email; }
    public String getNotes() {
        return notes; }

    public String toString() {
        return "Guest #" + id + ": " + name + ", " + email + ", " + phone;
    }
}