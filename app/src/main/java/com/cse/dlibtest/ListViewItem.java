package com.cse.dlibtest;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by sy081 on 2018-01-13.
 */

public class ListViewItem {
    private Bitmap imageBitmap;
    private String name;
    private String phoneNumber;

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

    public String getName() {
        return this.name;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
}
