package com.kritsit.casetracker.shared.domain.model;

import java.io.Serializable;
import java.util.Objects;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 10L;
    private String registration;
    private String make;
    private String colour;
    private boolean isTrailer;

    public Vehicle(String registration, String make, String colour, boolean isTrailer) {
        this.registration = registration;
        this.make = make;
        this.colour = colour;
        this.isTrailer = isTrailer;
    }

    // Accessor methods:
    public String getRegistration() {
        return registration;
    }

    public String getMake() {
        return make;
    }

    public String getColour() {
        return colour;
    }

    public boolean isTrailer() {
        return isTrailer;
    }

    // Mutator methods:
    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public void setTrailer(boolean isTrailer) {
        this.isTrailer = isTrailer;
    }

    @Override
    public int hashCode() {
        Boolean t = Boolean.valueOf(isTrailer);
        return ((registration + make + colour).hashCode() + t.hashCode()) / 3;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return obj.hashCode() == hashCode();
    }

    @Override
    public String toString() {
        String result = "Vehicle: ";
        result += colour + " ";
        result += make + " ";
        result += "(" + registration + ")";
        return result;
    }
}
