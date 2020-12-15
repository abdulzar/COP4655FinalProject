package com.sense.it.wifi.iot.studentproject.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ResturantModel {
    private String id;
    private String name;
    private String image_url;
    private String address;
    private List<String> categories;
    private LatLng latLng;

    public ResturantModel() {
    }

    public ResturantModel(String id, String name, String image_url, String address, List<String> categories, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.address = address;
        this.categories = categories;
        this.latLng = latLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
