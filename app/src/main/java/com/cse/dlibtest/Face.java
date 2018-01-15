package com.cse.dlibtest;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * Created by sy081 on 2018-01-04.
 */

public class Face{
    private static final long serialVersionUID = 1L;
    final private int LANDMARK_SIZE = 68;
    final private int NOSE_AND_CHIN_RATIO_SIZE = 2;
    final private int GLABELLA_RATIO_SIZE = 1;
    final private int FACE_AREA_RATIO_SIZE = 7;
    private Point[] landmarks = new Point[LANDMARK_SIZE];
    private double verticalDist; // 8-27
    private double horizontalDist;
    private double[] noseAndChinDist = new double[NOSE_AND_CHIN_RATIO_SIZE]; //8-33, 39-42
    private double[] faceAreaDist = new double[FACE_AREA_RATIO_SIZE]; //0-16, 1-15, 2-14, 3-13, 4-12, 5-11, 6-10
    private double[] glabellaDist = new double[GLABELLA_RATIO_SIZE];
    private double[] noseAndChinRatio = new double[NOSE_AND_CHIN_RATIO_SIZE];
    private double[] glabellaRatio = new double[GLABELLA_RATIO_SIZE];
    private double[] faceAreaRatio = new double[FACE_AREA_RATIO_SIZE];
    private boolean ratioReady;
    private String strLandmark;
    private Bitmap image;
    private Bitmap icon;
    private int predictedId;

    public Face(){
        for(int i = 0; i < 68; i++){
            landmarks[i] = new Point();
        }
        verticalDist = 0;
        horizontalDist = 0;
        noseAndChinDist[0] = 0;
        noseAndChinDist[1] = 0;
        this.ratioReady = false;
    }

    public void setPredictedId(int predictedId) {
        this.predictedId = predictedId;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setStrLandmark(String strLandmark) {
        this.strLandmark = strLandmark;
    }

    //얼굴 비율 세팅
    public void setFaceRatio(){
        if(!isRatioSet()) {
            setDistance(this); //비율을 구하기 위해 사전에 필요한 거리를 계산해놓는다.
            this.noseAndChinRatio = calculateRatio(noseAndChinDist, verticalDist, NOSE_AND_CHIN_RATIO_SIZE);
            this.glabellaRatio = calculateRatio(glabellaDist, horizontalDist, GLABELLA_RATIO_SIZE);
            this.faceAreaRatio = calculateRatio(faceAreaDist, horizontalDist, FACE_AREA_RATIO_SIZE);
            this.ratioReady = true;
        }
    }
    //필요한 거리값 저장
    public void setDistance(Face face){
        //코와 턱 길이의 총합
        this.verticalDist = getDistance(face.getFaceLandmarks(8), face.getFaceLandmarks(27));

        //코 길이 합
        this.noseAndChinDist[0] = getDistance(face.getFaceLandmarks(27), face.getFaceLandmarks(28));
        this.noseAndChinDist[0] += getDistance(face.getFaceLandmarks(28), face.getFaceLandmarks(29));
        this.noseAndChinDist[0] += getDistance(face.getFaceLandmarks(29), face.getFaceLandmarks(30));
        this.noseAndChinDist[0] += getDistance(face.getFaceLandmarks(30), face.getFaceLandmarks(33));

        //턱길이의 합
        this.noseAndChinDist[1] = getDistance(face.getFaceLandmarks(8), face.getFaceLandmarks(33));

        //얼굴 가로선 기준(0과 16의 거리)
        for(int i = 0; i < 7; i++){
            this.faceAreaDist[i] = getDistance(face.getFaceLandmarks(i), face.getFaceLandmarks(16-i));
        }
        horizontalDist = this.faceAreaDist[0];
        //미간 길이
        this.glabellaDist[0] = getDistance(face.getFaceLandmarks(39), face.getFaceLandmarks(42));
    }

    public boolean isRatioSet(){
        if(this.ratioReady == true)
            return true;
        else
            return false;
    }

    //거리 구하기
    public double getDistance(Point point1, Point point2){
        return Math.sqrt(Math.pow((point2.x - point1.x),2) + Math.pow((point2.y - point1.y),2));
    }

    public int getPredictedId() {
        return predictedId;
    }

    public double[] getNoseAndChinRatio(){
        return this.noseAndChinRatio;
    }

    public double[] getFaceAreaRatio(){
        return this.faceAreaRatio;
    }

    public double[] getGlabellaRatio(){
        return this.glabellaRatio;
    }

    //랜드마크 반환
    public Point getFaceLandmarks(int index){
        return this.landmarks[index];
    }

    //랜드마크 설정
    public void setFaceLandmarks(int index, Point point){
        this.landmarks[index].x = point.x;
        this.landmarks[index].y = point.y;
    }

    public String getStrLandmark() {
        return this.strLandmark;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getIcon() {
        return icon;
    }

    //비율 구하기
    public double[] calculateRatio(double[] value, double sum, int size){
        double[] result = new double[size];
        for(int i = 0; i < size; i++){
            result[i] = value[i] / sum;
        }
        return result;
    }
}
