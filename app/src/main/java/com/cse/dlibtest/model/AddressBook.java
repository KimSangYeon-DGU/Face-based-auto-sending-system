package com.cse.dlibtest.model;

import android.graphics.Bitmap;

/**
 * Created by sy081 on 2018-01-12.
 */

public class AddressBook {
    private int id;
    private String name;
    private String phoneNumber;
    private String similarity;
    private Face face = new Face();
    private Bitmap predictedIcon;

    public AddressBook(){}
    public AddressBook(int _id, String _name, String _phoneNumber, Bitmap _image, Bitmap _icon, String strLandmark){
        this.id = _id;
        this.name = _name;
        this.phoneNumber = _phoneNumber;
        this.face.setImage(_image);
        this.face.setIcon(_icon);
        this.face.setStrLandmark(strLandmark);
    }

    public void setPredictedIcon(Bitmap predictedIcon) {
        this.predictedIcon = predictedIcon;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public void setId(int _id) {
        this.id = _id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return this.id;
    }

    public Face getFace() {
        return face;
    }

    public Bitmap getPredictedIcon() {
        return predictedIcon;
    }

    public String getSimilarity() {
        return similarity;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
}
