package com.cse.dlibtest.model;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by sy081 on 2018-01-04.
 */

public class Face{
    final private int LANDMARK_SIZE = 68;
    final private int NOSE_AND_CHIN_RATIO_SIZE = 1;
    final private int GLABELLA_RATIO_SIZE = 1;
    final private int FACE_AREA_RATIO_SIZE = 6;
    final private int CHIN_AND_LIPS_RATIO_SIZE = 4;
    final private int EYEBROWS_RATIO_SIZE = 5;
    private Point[] landmarks = new Point[LANDMARK_SIZE];
    //길이
    private double verticalDist; // 8-27
    private double horizontalDist;
    private double totalChinAndLipsDist; // 턱과 입술사이의 거리 길이의 총합
    private double[] noseAndChinDist = new double[NOSE_AND_CHIN_RATIO_SIZE]; //8-33, 39-42
    private double[] glabellaDist = new double[GLABELLA_RATIO_SIZE];
    private double[] faceAreaDist = new double[FACE_AREA_RATIO_SIZE]; //0-16, 1-15, 2-14, 3-13, 4-12, 5-11, 6-10
    private double[] chinAndLipsDist = new double[CHIN_AND_LIPS_RATIO_SIZE];
    private double[] eyebrowsDist = new double[EYEBROWS_RATIO_SIZE];

    //비율
    private double[] noseAndChinRatio = new double[NOSE_AND_CHIN_RATIO_SIZE];
    private double[] glabellaRatio = new double[GLABELLA_RATIO_SIZE];
    private double[] faceAreaRatio = new double[FACE_AREA_RATIO_SIZE];
    private double[] chinAndLipsRatio = new double[CHIN_AND_LIPS_RATIO_SIZE];
    private double[] eyebrowsRatio = new double[EYEBROWS_RATIO_SIZE];

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
        totalChinAndLipsDist = 0;
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
            this.chinAndLipsRatio = calculateRatio(chinAndLipsDist, totalChinAndLipsDist, CHIN_AND_LIPS_RATIO_SIZE);
            this.eyebrowsRatio = calculateRatio(eyebrowsDist, horizontalDist, EYEBROWS_RATIO_SIZE);
            this.ratioReady = true;
        }
    }
    //필요한 거리값 저장
    public void setDistance(Face face){
        //코 길이 합
        this.noseAndChinDist[0] = getDistance(face.getFaceLandmarks(27), face.getFaceLandmarks(28));
        this.noseAndChinDist[0] += getDistance(face.getFaceLandmarks(28), face.getFaceLandmarks(29));
        this.noseAndChinDist[0] += getDistance(face.getFaceLandmarks(29), face.getFaceLandmarks(30));
        this.noseAndChinDist[0] += getDistance(face.getFaceLandmarks(30), face.getFaceLandmarks(33));

        //턱길이의 합
        double chinDist = getDistance(face.getFaceLandmarks(8), face.getFaceLandmarks(33));

        //얼굴 수직선 길이
        this.verticalDist = this.noseAndChinDist[0] + chinDist;

        //얼굴 수평선 길이
        this.horizontalDist = getDistance(face.getFaceLandmarks(0), face.getFaceLandmarks(16));

        //얼굴 가로선 기준(0과 16의 거리)
        for(int i = 0; i < FACE_AREA_RATIO_SIZE; i++){
            this.faceAreaDist[i] = getDistance(face.getFaceLandmarks(i + 1), face.getFaceLandmarks(16 - i - 1));
        }

        //미간 길이
        this.glabellaDist[0] = getDistance(face.getFaceLandmarks(39), face.getFaceLandmarks(42));

        //턱과 아랫입까지의 거리
        this.totalChinAndLipsDist += this.chinAndLipsDist[0] = getDistance(face.getFaceLandmarks(11), face.getFaceLandmarks(56));
        this.totalChinAndLipsDist += this.chinAndLipsDist[1] = getDistance(face.getFaceLandmarks(12), face.getFaceLandmarks(56));
        this.totalChinAndLipsDist += this.chinAndLipsDist[2] = getDistance(face.getFaceLandmarks(5), face.getFaceLandmarks(58));
        this.totalChinAndLipsDist += this.chinAndLipsDist[3] = getDistance(face.getFaceLandmarks(4), face.getFaceLandmarks(58));

        //눈썹과 눈썹 거리
        this.eyebrowsDist[0] = getDistance(face.getFaceLandmarks(19), face.getFaceLandmarks(24));
        this.eyebrowsDist[1] = getDistance(face.getFaceLandmarks(18), face.getFaceLandmarks(25));
        this.eyebrowsDist[2] = getDistance(face.getFaceLandmarks(20), face.getFaceLandmarks(23));
        this.eyebrowsDist[3] = getDistance(face.getFaceLandmarks(21), face.getFaceLandmarks(22));
        this.eyebrowsDist[4] = getDistance(face.getFaceLandmarks(17), face.getFaceLandmarks(26));
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

    public double[] getEyebrowsRatio() {
        return eyebrowsRatio;
    }

    public double[] getChinAndLipsRatio() {
        return chinAndLipsRatio;
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
