package com.example.torchapp;

public class TrochObject {

    private double longitude;
    private double latitude;
    private String nameOfTorch;
    private int torchId;
    private String message;
    private boolean state = false;

    public TrochObject(double longitude, double latitude, String nameOfTorch, int torchId, String message) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.nameOfTorch = nameOfTorch;
        this.torchId = torchId;
        this.message = message;
    }

    public TrochObject() {


    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getNameOfTorch() {
        return nameOfTorch;
    }

    public void setNameOfTorch(String nameOfTorch) {
        this.nameOfTorch = nameOfTorch;
    }

    public int getTorchId() {
        return torchId;
    }

    public void setTorchId(int torchId) {
        this.torchId = torchId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
