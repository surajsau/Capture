package com.halfplatepoha.capture.models;

import io.realm.RealmObject;

/**
 * Created by surjo on 03/03/17.
 */

public class Location extends RealmObject {
    private String          city;
    private String          address;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
