package com.cse.dlibtest.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.cse.dlibtest.R;

/**
 * Created by sy081 on 2018-01-13.
 */

public class CheckableLinearLayout extends LinearLayout implements Checkable{
    public CheckableLinearLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public boolean isChecked() {
        CheckBox cb = (CheckBox)findViewById(R.id.cb_select);
        return cb.isChecked();
    }

    @Override
    public void toggle() {
        CheckBox cb = (CheckBox)findViewById(R.id.cb_select);
        setChecked(cb.isChecked()? false: true);
    }

    @Override
    public void setChecked(boolean checked) {
        CheckBox cb = (CheckBox)findViewById(R.id.cb_select);
        if(cb.isChecked() != checked){
            cb.setChecked(checked);
        }
    }
}
