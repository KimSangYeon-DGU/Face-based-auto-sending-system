package com.cse.dlibtest;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by sy081 on 2018-01-03.
 */

public class SendActivity extends AppCompatActivity {
    Face[] face;
    final private int LANDMARK_SIZE = 68;
    LandmarkComparator comparator = new LandmarkComparator();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ArrayList<String> totalLandmarks = getIntent().getExtras().getStringArrayList("TotalLandmarks");
        ArrayList<String> addrBookLandmarks = getIntent().getExtras().getStringArrayList("AddrBookLandmarks");

        int totalSize = totalLandmarks.size();
        int addrBookSize = addrBookLandmarks.size();
        face = new Face[totalSize];
        for(int i = 0; i < totalSize; i++){
            face[i] = new Face();
        }
        saveUserLandmark(face, totalLandmarks, totalSize);

        //테스트 이미지
        Face[] addrBookFace = new Face[addrBookSize];
        for(int i = 0; i < addrBookSize; i++){
            addrBookFace[i] = new Face();
        }
        saveUserLandmark(addrBookFace, addrBookLandmarks, addrBookSize);

        //비교 진행
        for(int i = 0; i < totalSize; i++){
            for(int j = 0; j < addrBookSize; j++) {
                comparator.compare(face[i], addrBookFace[j]);
            }
        }
        Button mBackButton = (Button)findViewById(R.id.btn_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToMainIntent = new Intent(SendActivity.this, MainActivity_.class); //SendActivity에서 MainActivity로 이동
                moveToMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //액티비티간 이동중에 스택 중간에 저장되어있는 액티비티를 지움
                moveToMainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //띄우려는 액티비티가 스택 맨위에 이미 실행 중이면 재사용함.
                startActivity(moveToMainIntent); //Activity 시작
            }
        });
    }

    protected void saveUserLandmark(Face[] face, ArrayList<String> totalLandmarks, int size){
        Point[] point = new Point[68];
        for(int i = 0; i < size; i++) {
            point = convertStringToPoint(totalLandmarks.get(i));
            for(int j = 0; j < LANDMARK_SIZE; j++){
                face[i].setFaceLandmarks(j, point[j]);
            }
        }
    }
    protected Point[] convertStringToPoint(String str){
        Point[] point = new Point[LANDMARK_SIZE];
        for(int i = 0; i < point.length; i++){
            point[i] = new Point();
        }
        int index = 0;
        int pointX, pointY;
        for(int i = 0; i < LANDMARK_SIZE; i++) {
            String temp = "";
            pointX = 0;
            pointY = 0;
            while(str.charAt(index) != ','){
                temp += str.charAt(index);
                index++;
            }
            if(temp != "") {
                pointX = Integer.parseInt(temp);
                temp = "";
            }
            index++;
            while(str.charAt(index) != ','){
                temp += str.charAt(index);
                index++;
            }
            if(temp != "") {
                pointY = Integer.parseInt(temp);
            }
            index++;
            if(pointX != 0 && pointY != 0) {
                point[i].x = pointX;
                point[i].y = pointY;
            }
        }
        return point;
    }
}
