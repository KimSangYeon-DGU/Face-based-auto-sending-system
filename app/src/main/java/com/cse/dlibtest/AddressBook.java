package com.cse.dlibtest;

import android.graphics.Bitmap;

/**
 * Created by sy081 on 2018-01-12.
 */

public class AddressBook {
    private int number;
    private String name;
    private String phoneNumber;
    private Bitmap image;
    private Bitmap icon;
    private String similarity;

    public AddressBook(int _number, String _name, String _phoneNumber, Bitmap _image){
        this.number = _number;
        this.name = _name;
        this.phoneNumber = _phoneNumber;
        this.image = _image;
    }
    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int _number) {
        this.number = _number;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public void setImage(Bitmap _image) {
        this.image = _image;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSimilarity() {
        return similarity;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
}
