package com.cse.dlibtest;

import android.graphics.Bitmap;

/**
 * Created by sy081 on 2018-01-15.
 */

public class Prediction {
    private Bitmap icon;
    private String name;
    private String phoneNumber;
    private String similarity;
    private Bitmap predictedIcon;

    public Prediction(){}
    public Prediction(Bitmap _icon, String _name, String _phoneNumber, Bitmap _predictedIcon,String _similarity){
        this.icon = _icon;
        this.name = _name;
        this.phoneNumber = _phoneNumber;
        this.predictedIcon = _predictedIcon;
        this.similarity = _similarity;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public void setPredictedIcon(Bitmap predictedIcon) {
        this.predictedIcon = predictedIcon;
    }

    public Bitmap getPredictedIcon() {
        return predictedIcon;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getSimilarity() {
        return similarity;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
