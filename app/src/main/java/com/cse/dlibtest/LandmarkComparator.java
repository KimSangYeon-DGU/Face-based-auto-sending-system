package com.cse.dlibtest;

import android.graphics.Point;

/**
 * Created by sy081 on 2018-01-04.
 */

public class LandmarkComparator {
        final private int LANDMARK_SIZE = 68;
        final private int OUTLINE_LANDMARK_SIZE = 17;
        private double[] outlines = new double[OUTLINE_LANDMARK_SIZE];
    public double compare(Face face1, Face face2){
        double prob = 0.0;
        double[] temp1 = new double[OUTLINE_LANDMARK_SIZE];
        double[] temp2 = new double[OUTLINE_LANDMARK_SIZE];
        double totalDistance = 0.0;

        //턱선에 대한 비율 구하기
        for(int i = 0; i < OUTLINE_LANDMARK_SIZE; i++){
            outlines[i] = getDistance(face1.getFaceLandmarks(i), face1.getFaceLandmarks(i+1));
            totalDistance += outlines[i];
        }
        temp1 = calculateRatio(outlines, totalDistance, OUTLINE_LANDMARK_SIZE);
        totalDistance = 0.0;

        for(int i = 0; i < OUTLINE_LANDMARK_SIZE; i++){
            outlines[i] = getDistance(face2.getFaceLandmarks(i), face2.getFaceLandmarks(i+1));
            totalDistance += outlines[i];
        }
        temp2 = calculateRatio(outlines, totalDistance, OUTLINE_LANDMARK_SIZE);
        prob = getMeanSquaredError(temp1, temp2, OUTLINE_LANDMARK_SIZE);
        return prob;
    }

    //평균제곱오차
    public double getMeanSquaredError(double[] ans, double[] comp, int size){
        double sum = 0.0;
        for(int i = 0; i < OUTLINE_LANDMARK_SIZE; i++){
            sum += Math.pow((ans[i] - comp[i]) , 2);
        }
        return sum / OUTLINE_LANDMARK_SIZE;
    }

    //비율 구하기
    public double[] calculateRatio(double[] value, double sum, int size){
        double[] result = new double[size];
        for(int i = 0; i < size; i++){
            result[i] = value[i] / sum;
        }
        return result;
    }

    //거리 구하기
    public double getDistance(Point point1, Point point2){
        return Math.sqrt(Math.pow((point2.x - point1.x),2) + Math.pow((point2.y - point1.y),2));
    }
}
