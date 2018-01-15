package com.cse.dlibtest.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by sy081 on 2018-01-13.
 */

public class ListViewItem {
    private Bitmap imageBitmap;
    private Bitmap iconBitmap;
    private String similarity;
    private String name;
    private String phoneNumber;

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public String getSimilarity() {
        return similarity;
    }

    public String getName() {
        return this.name;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
}
