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
        //주소록 사진의 랜드마크를 저장할 객체 생성
        Face[] addrBookFace = new Face[addrBookSize];
        for(int i = 0; i < addrBookSize; i++){
            addrBookFace[i] = new Face();
        }
        saveUserLandmark(face, totalLandmarks, totalSize); //현재 사진 속 인물의 랜드마크 저장
        saveUserLandmark(addrBookFace, addrBookLandmarks, addrBookSize); //주소록에 있는 랜드마크 저장

        //선택한 사진(m)과 주소록 사진(n)의 랜드마크 비교(O(mxn)) 진행
        for(int i = 0; i < totalSize; i++){
            for(int j = 0; j < addrBookSize; j++) {
                comparator.compare(face[i], addrBookFace[j]);
            }
        }
        //뒤로 가기 버튼 -> 홈 화면으로 이동
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

    //Face객체에 랜드마크 정보 저장
    protected void saveUserLandmark(Face[] face, ArrayList<String> totalLandmarks, int size){
        Point[] point = new Point[68];
        for(int i = 0; i < size; i++) {
            point = convertStringToPoint(totalLandmarks.get(i));
            for(int j = 0; j < LANDMARK_SIZE; j++){
                face[i].setFaceLandmarks(j, point[j]);
            }
        }
    }

    //스트링을 문법에 맞게 Parsing하여 Point 타입으로 변환
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
