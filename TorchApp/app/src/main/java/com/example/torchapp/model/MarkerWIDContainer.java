package com.example.torchapp.model;

import com.google.android.gms.maps.model.Marker;

public class MarkerWIDContainer {
    private Marker mMarker;
    private Integer mMakerID;

    public Marker getmMarker() {
        return mMarker;
    }

    public void setmMarker(Marker mMarker) {
        this.mMarker = mMarker;
    }

    public Integer getmMakerID() {
        return mMakerID;
    }

    public void setMmMakerID(Integer mmMakerID) {
        this.mMakerID = mmMakerID;
    }

    public MarkerWIDContainer(Marker marker, Integer markerID){
        mMarker = marker;
        mMakerID = markerID;
    }
}
