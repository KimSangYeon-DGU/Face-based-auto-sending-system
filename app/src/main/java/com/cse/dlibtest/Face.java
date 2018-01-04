package com.cse.dlibtest;

import android.graphics.Point;

/**
 * Created by sy081 on 2018-01-04.
 */

public class Face {
    private Point[] landmarks = new Point[68];

    public Face(){
        for(int i = 0; i < 68; i++){
            landmarks[i] = new Point();
        }
    }

    public void setFaceLandmarks(int index, Point point){
        this.landmarks[index].x = point.x;
        this.landmarks[index].y = point.y;
    }
    public Point getFaceLandmarks(int index){
        return this.landmarks[index];
    }
}
