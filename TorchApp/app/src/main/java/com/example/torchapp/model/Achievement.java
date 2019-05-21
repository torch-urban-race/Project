package com.example.torchapp.model;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

public class Achievement {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripttion() {
        return descripttion;
    }

    public void setDescripttion(String descripttion) {
        this.descripttion = descripttion;
    }

    public String getDateObtained() {
        return dateObtained;
    }

    public void setDateObtained(String dateObtained) {
        this.dateObtained = dateObtained;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean commplete) {
        this.complete = commplete;
    }
    private String title;
    private String descripttion;
    private String dateObtained;
    private Drawable icon;
    private boolean complete= false;



    public Achievement(){


    }
    /*
    float redValue= 255;
    float greenValue= 255;
    float blueValue= 255;

    float[] colorMatrix = {
            redValue, 0, 0, 0, 0,  //red
            0, greenValue, 0, 0, 0, //green
            0, 0, blueValue, 0, 0,  //blue
            0, 0, 0, 1, 0    //alpha
    };

     int asd = Color.GRAY;
    ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);

    public void setgray(){

        if(!complete){
            icon.setColorFilter(asd,PorterDuff.Mode.DST_OVER);
        }

    }
*/




}
