/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.bookstore.model;

/**
 *
 * @author ppc
 */
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    // public User() {}

    public User(int id, String firstName, String lastName, String email) {
        this.id = id; 
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        if (firstName == null && lastName == null) return "";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return java.util.Objects.equals(id, user.id) &&
               java.util.Objects.equals(firstName, user.firstName) &&
               java.util.Objects.equals(lastName, user.lastName) &&
               java.util.Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, firstName, lastName, email);
    }

}
