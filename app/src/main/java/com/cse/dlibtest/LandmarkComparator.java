package com.cse.dlibtest;

import android.graphics.Point;

/**
 * Created by sy081 on 2018-01-04.
 */

public class LandmarkComparator {
    final private int LANDMARK_SIZE = 68;
    final private int NOSE_AND_CHIN_RATIO_SIZE = 2;
    final private int GLABELLA_RATIO_SIZE = 1;
    final private int FACE_AREA_RATIO_SIZE = 7;

    public double compare(Face comp, Face face){
        double prob = 100;

        //총 3가지 비교
        //1. 코와 턱 비율 비교
        //2. 면적 비교
        //3. 미간 비교
        prob -= checkNoseAndChinRatio(comp, face);
        prob -= checkFaceAreaRatio(comp, face);
        prob -= checkGlabellaRatio(comp, face);
        return prob;
    }
    public double checkNoseAndChinRatio(Face comp, Face face){
        double error = 0;
        double[] compRatio;
        double[] faceRatio;
        compRatio = comp.getNoseAndChinRatio();
        faceRatio = face.getNoseAndChinRatio();
        for(int i = 0; i < NOSE_AND_CHIN_RATIO_SIZE; i++){
            error += 15*Math.abs(compRatio[i] - faceRatio[i]);
        }
        return error;
    }
    public double checkFaceAreaRatio(Face comp, Face face){
        double error = 0;
        double[] compRatio;
        double[] faceRatio;
        compRatio = comp.getFaceAreaRatio();
        faceRatio = face.getFaceAreaRatio();
        for(int i = 0; i < FACE_AREA_RATIO_SIZE; i++){
            error += 42*Math.abs(compRatio[i] - faceRatio[i]);
        }
        return error;
    }

    public double checkGlabellaRatio(Face comp, Face face){
        double error = 0;
        double[] compRatio;
        double[] faceRatio;
        compRatio = comp.getGlabellaRatio();
        faceRatio = face.getGlabellaRatio();
        for(int i = 0; i < GLABELLA_RATIO_SIZE; i++){
            error += Math.abs(compRatio[i] - faceRatio[i]);
        }
        return error;
    }
}
