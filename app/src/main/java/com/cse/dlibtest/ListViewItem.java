package com.cse.dlibtest;

import android.graphics.drawable.Drawable;

/**
 * Created by sy081 on 2018-01-13.
 */

public class ListViewItem {
    private Drawable imageDrawable;
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

    public void setImageDrawable(Drawable imageDrawable) {
        this.imageDrawable = imageDrawable;
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

    public Drawable getImageDrawable() {
        return this.imageDrawable;
    }
}
