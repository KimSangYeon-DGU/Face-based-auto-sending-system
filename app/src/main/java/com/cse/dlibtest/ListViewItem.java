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
    private boolean checkbox;

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
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

    public boolean getCheckbox(){
        return this.checkbox;
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
