package com.cse.dlibtest.util;

import android.util.Log;

import com.cse.dlibtest.model.Face;

/**
 * Created by sy081 on 2018-01-04.
 */

public class LandmarkComparator {
    final private int LANDMARK_SIZE = 68;
    final private int NOSE_AND_CHIN_RATIO_SIZE = 1;
    final private int GLABELLA_RATIO_SIZE = 1;
    final private int FACE_AREA_RATIO_SIZE = 6;
    final private int CHIN_AND_LIPS_RATIO_SIZE = 4;
    final private int EYEBROWS_RATIO_SIZE = 5;
    final private int CHIN_AND_SLACK_RATIO_SIZE = 8;
    private int weight_index;
    /*final private double[] weigths = {
            39.256147488724935, 2.3564919952317607, 6.35574882158455 , 4.2697048503738815, 13.18855678994133,
            15.420939922623575, 18.21105914497969, 28.026246464898996, 35.96370846034475, 8.078563200010176,
            9.019133651765777, 36.904278912102704, 23.87597247767618, 31.322442093848405, 6.9284819825516735,
            0.354569176999296, 41.43306200274139
    };*/
    final private double[] weigths = {
            35.66212124722465, 24.310844856903064, 19.631565152283734, 32.519216178502354, 46.82162133360625, 53.41328821018676, 60.59823235455841, 55.36158999270871, 16.022657418852432, 6.55062555415481, 9.191791422820428, 13.322315085764881, 42.0854951105421, 37.02884005875519, 38.45849771527334, 26.445362971258596, 19.41402355696748
    };
    public double compare(Face comp, Face face){
        double result = 100;
        this.weight_index = 0;
        //총 6가지 파트, 총 25개의 비율 비교
        //1. 코와 턱 비율 비교
        //2. 면적 비교
        //3. 미간 비교(눈과 눈사이, 따로 표현할 단어가 떠오르지 않았음)
        //4. 턱과 아랫입술 비교
        //5. 눈썹과 눈썹사이 비교
        result -= checkNoseAndChinRatio(comp, face);
        result -= checkFaceAreaRatio(comp, face);
        result -= checkGlabellaRatio(comp, face);
        result -= checkChinAndLipsRatio(comp, face);
        result -= checkEyebrowsRatio(comp, face);
        return result;
    }
    public double checkNoseAndChinRatio(Face comp, Face face){
        double error = 0;
        double[] compRatio;
        double[] faceRatio;
        compRatio = comp.getNoseAndChinRatio();
        faceRatio = face.getNoseAndChinRatio();
        for(int i = 0; i < NOSE_AND_CHIN_RATIO_SIZE; i++){
            error += weigths[weight_index++]*Math.abs(compRatio[i] - faceRatio[i]);
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
            error += weigths[weight_index++]*Math.abs(compRatio[i] - faceRatio[i]);
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
            error += weigths[weight_index++]*Math.abs(compRatio[i] - faceRatio[i]);
        }
        return error;
    }

    public double checkChinAndLipsRatio(Face comp, Face face){
        double error = 0;
        double[] compRatio;
        double[] faceRatio;
        compRatio = comp.getChinAndLipsRatio();
        faceRatio = face.getChinAndLipsRatio();
        for(int i = 0; i < CHIN_AND_LIPS_RATIO_SIZE; i++){
            error += weigths[weight_index++]*Math.abs(compRatio[i] - faceRatio[i]);
        }
        return error;
    }

    public double checkEyebrowsRatio(Face comp, Face face){
        double error = 0;
        double[] compRatio;
        double[] faceRatio;
        compRatio = comp.getEyebrowsRatio();
        faceRatio = face.getEyebrowsRatio();
        for(int i = 0; i < EYEBROWS_RATIO_SIZE; i++){
            Log.d("DEBUG", Double.toString(weigths[weight_index]));
            error += weigths[weight_index++]*Math.abs(compRatio[i] - faceRatio[i]);
        }
        return error;
    }
}
