package com.cse.dlibtest;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by sy081 on 2018-01-03.
 */

public class SendActivity extends AppCompatActivity {
    final private int LANDMARK_SIZE = 68;
    final private int ICON_IMAGE_WIDTH = 128;
    final private int ICON_IMAGE_HEIGHT = 128;
    Face[] face;
    LandmarkComparator comparator = new LandmarkComparator();
    ArrayList<AdressBook> candidate = new ArrayList<AdressBook>();
    ArrayList<String> totalLandmarks;
    ArrayList<String> addrBookLandmarks;
    String mImagePath;
    Bitmap bmp;

    ListView listview ;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        getDataFromMainActivity(); //메인 액티비티에서 값 넘겨받기
        startSearchPeople(); //사진속에 사람을 찾음
        showListView(); //찾은 사람들을 리스트 뷰에 뿌려줌

        //뒤로 가기 버튼 -> 홈 화면으로 이동
        Button mBackButton = (Button)findViewById(R.id.btn_back);
        Button mSendButton = (Button)findViewById(R.id.btn_send);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToMainIntent = new Intent(SendActivity.this, MainActivity_.class); //SendActivity에서 MainActivity로 이동
                moveToMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //액티비티간 이동중에 스택 중간에 저장되어있는 액티비티를 지움
                moveToMainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //띄우려는 액티비티가 스택 맨위에 이미 실행 중이면 재사용함.
                startActivity(moveToMainIntent); //Activity 시작
            }
        });

        //Send버튼 눌렀을 때의 동작
        mSendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                Object obj = new Object();
                int count = adapter.getCount();
                boolean isThere = false;
                String phoneNumber = "";
                for(int i =  0; i < count; i++){
                    ListViewItem lv = new ListViewItem();
                    if(checkedItems.get(i)) {
                        lv = (ListViewItem) adapter.getItem(i);
                        if (!isThere) {
                            phoneNumber += lv.getPhoneNumber().replaceAll("-", "");
                        } else {
                            phoneNumber += ";";
                            phoneNumber += lv.getPhoneNumber().replaceAll("-", "");
                        }
                        isThere = true;
                    }
                }
                if(isThere)
                    sendMMS(phoneNumber, "함께 찍은 사진보냅니다.", bmp);
            }
        });
    }
    public void getDataFromMainActivity(){
        totalLandmarks = getIntent().getExtras().getStringArrayList("TotalLandmarks");
        addrBookLandmarks = getIntent().getExtras().getStringArrayList("AddrBookLandmarks");
        mImagePath = getIntent().getStringExtra("ImagePath");
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    public void showListView(){
        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.lv_list);
        listview.setAdapter(adapter);
        int mCandidateSize = candidate.size();
        for(int i = 0; i < mCandidateSize; i++) {
            // 첫 번째 아이템 추가.
            Bitmap icon = Bitmap.createScaledBitmap(candidate.get(i).getImage(), ICON_IMAGE_WIDTH, ICON_IMAGE_HEIGHT, true);
            adapter.addItem(icon, candidate.get(i).getName() , candidate.get(i).getPhoneNumber(), false);
        }
    }

    public void startSearchPeople(){

        String[] name = new String[3];
        String[] phoneNumber = new String[3];
        Bitmap[] images = new Bitmap[3];
        int totalSize = totalLandmarks.size();
        int addrBookSize = addrBookLandmarks.size();

        setAddressInfo(name, phoneNumber, images); //가상 주소록 정보 저장
        face = new Face[totalSize];
        for(int i = 0; i < totalSize; i++){
            face[i] = new Face();
        }
        //주소록
        AdressBook[] adressBook = new AdressBook[addrBookSize];
        for(int i = 0; i< addrBookSize; i++){
            adressBook[i] = new AdressBook(i, name[i], phoneNumber[i], images[i]);
        }

        //주소록 사진의 랜드마크를 저장할 객체 생성
        Face[] addrBookFace = new Face[addrBookSize];
        for(int i = 0; i < addrBookSize; i++){
            addrBookFace[i] = new Face();
        }
        saveUserLandmark(face, totalLandmarks, totalSize); //현재 사진 속 인물의 랜드마크 저장
        saveUserLandmark(addrBookFace, addrBookLandmarks, addrBookSize); //주소록에 있는 랜드마크 저장
        for(int i = 0; i < totalSize; i++){
            face[i].setFaceRatio();
        }
        for(int i = 0; i < addrBookSize; i++){
            addrBookFace[i].setFaceRatio();
        }
        //선택한 사진(m)과 주소록 사진(n)의 랜드마크 비교(O(mxn)) 진행
        double temp = 0, results = 0;
        int personA, personB;
        for(int i = 0; i < totalSize; i++){
            personA = -1;
            personB = -1;
            for(int j = 0; j < addrBookSize; j++) {
                temp = comparator.compare(addrBookFace[j], face[i]);
                if(temp >= results){
                    results = temp;
                    personA = i;
                    personB = j;
                }
            }
            if(personB != -1) {
                candidate.add(adressBook[personB]);
            }
            System.out.println(personA + "와 " + adressBook[personB].getName() +"님의 닮음도는 "+ results + "입니다.");
        }
    }
    //MMS 전송
    public void sendMMS(String phoneNumber, String msg, Bitmap image){
        String pathOfBitmap = MediaStore.Images.Media.insertImage(getContentResolver(), image,"title", null);
        Uri bmpUri = Uri.parse(pathOfBitmap);
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra("address", phoneNumber);
        emailIntent.putExtra("sms_body", msg);
        emailIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        emailIntent.setType("image/png");
        emailIntent.setAction(Intent.ACTION_SEND);
        startActivity(emailIntent);
        finish();
    }
    //가상 주소록 정보 저장
    public void setAddressInfo(String[] name, String[] phoneNumber, Bitmap[] images){
        name[0] = "김상연";
        name[1] = "나선엽";
        name[2] = "강백진";
        phoneNumber[0] = "010-7940-5173";
        phoneNumber[1] = "010-3333-3323";
        phoneNumber[2] = "010-5555-3212";
        images[0] = BitmapFactory.decodeResource(getResources(), R.drawable.kim);
        images[1] = BitmapFactory.decodeResource(getResources(), R.drawable.na);
        images[2] = BitmapFactory.decodeResource(getResources(), R.drawable.kang);
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
