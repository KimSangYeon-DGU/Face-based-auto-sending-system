package com.cse.dlibtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by sy081 on 2018-01-03.
 */

public class SendActivity extends AppCompatActivity {
    final private int LANDMARK_SIZE = 68;
    final private int ICON_IMAGE_WIDTH = 128;
    final private int ICON_IMAGE_HEIGHT = 128;
    private LandmarkComparator comparator = new LandmarkComparator();
    private ArrayList<Prediction> candidate = new ArrayList<>();
    private ArrayList<AddressBook> addressBooks = new ArrayList<>();
    private ArrayList<Face> faces = new ArrayList<>();
    /*
    private ArrayList<String> faceLandmarks;
    private ArrayList<String> addrBookLandmarks;
    private ArrayList<Bitmap> iconBitmaps = new ArrayList<Bitmap>();
    */
    private Bitmap bmp;

    private ListView listview ;
    private ListViewAdapter adapter;

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
                    sendMMS(phoneNumber, "함께 찍은 사진입니다.", bmp);
            }
        });
    }
    public void getDataFromMainActivity(){
        //totalLandmarks = getIntent().getExtras().getStringArrayList("TotalLandmarks");
        //addrBookLandmarks = getIntent().getExtras().getStringArrayList("AddrBookLandmarks");
        int faceSize = getIntent().getIntExtra("FaceSize", 1);
        for(int i = 0; i < faceSize; i++){ //사진 속 얼굴에 대한 정보 수신
            String tempStrLandmark = getIntent().getStringExtra("FaceLandmarks"+Integer.toString(i));
            byte[] tempByteArray = getIntent().getByteArrayExtra("FaceIcon"+Integer.toString(i));
            Bitmap icon  = BitmapFactory.decodeByteArray(tempByteArray, 0, tempByteArray.length);//선택 사진 비트맵으로 변환 후 bmp에 저장
            Face face = new Face();
            face.setStrLandmark(tempStrLandmark);
            face.setIcon(icon);
            faces.add(face);
        }
        int addrBookSize = getIntent().getIntExtra("AddrBookSize", 1);
        for(int i = 0; i < addrBookSize; i++){
            int tempAddrBookId = getIntent().getIntExtra("AddrBookID",1);
            String tempAddrBookName = getIntent().getStringExtra("AddrBookName"+Integer.toString(i));
            String tempAddrBookPhoneNumber = getIntent().getStringExtra("AddrBookPhoneNumber"+Integer.toString(i));
            String tempAddrBookLandmark = getIntent().getStringExtra("AddrBookLandmark"+Integer.toString(i));
            byte[] tempAddrBookByteArray = getIntent().getByteArrayExtra("AddrBookIcon"+Integer.toString(i));
            Bitmap icon  = BitmapFactory.decodeByteArray(tempAddrBookByteArray, 0, tempAddrBookByteArray.length);//주소록 비트맵으로 변환 후 bmp에 저장
            AddressBook addressBook = new AddressBook(tempAddrBookId, tempAddrBookName, tempAddrBookPhoneNumber,icon,tempAddrBookLandmark);
            addressBooks.add(addressBook);
        }
        byte[] byteArray = getIntent().getByteArrayExtra("Image"); //사용자 선택 사진 수신
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);//선택 사진 비트맵으로 변환 후 bmp에 저장
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
            Bitmap icon = candidate.get(i).getIcon(); //주소록 아이콘
            String name = candidate.get(i).getName();
            String phoneNumber = candidate.get(i).getPhoneNumber();
            String similarity = candidate.get(i).getSimilarity();
            Bitmap predictedIcon = candidate.get(i).getPredictedIcon();
            adapter.addItem(icon, name, phoneNumber, predictedIcon, similarity+"%");
        }
    }

    public void startSearchPeople(){
        int faceSize = faces.size();
        int addrBookSize = addressBooks.size();

        for(int i = 0; i < faceSize; i++) { //현재 사진 속 인물의 랜드마크 저장
            saveUserLandmark(faces.get(i), faces.get(i).getStrLandmark());
        }
        for(int i = 0; i < addrBookSize; i++) { //주소록에 있는 랜드마크 저장
            saveUserLandmark(addressBooks.get(i).getFace(), addressBooks.get(i).getFace().getStrLandmark());
        }

        for(int i = 0; i < faceSize; i++){
            faces.get(i).setFaceRatio();
        }
        for(int i = 0; i < addrBookSize; i++){
            addressBooks.get(i).getFace().setFaceRatio();
        }

        //선택한 사진(m)과 주소록 사진(n)의 랜드마크 비교(O(mxn)) 진행
        double probability = 0, max = 0;
        boolean predicted;
        for(int i = 0; i < faceSize; i++){
            Prediction prediction = new Prediction();
            predicted = false;
            for(int j = 0; j < addrBookSize; j++) {
                probability = comparator.compare(addressBooks.get(j).getFace(), faces.get(i));
                if(probability >= max){
                    predicted = true;
                    max = probability;
                    Bitmap icon = addressBooks.get(j).getFace().getIcon();
                    String name = addressBooks.get(j).getName();
                    String phoneNumber = addressBooks.get(j).getPhoneNumber();
                    Bitmap predictedIcon = faces.get(i).getIcon();
                    String similarity = Integer.toString((int)max);
                    prediction = new Prediction(icon, name, phoneNumber, predictedIcon, similarity);
                }
            }
            if(predicted) {
                candidate.add(prediction);
            }
        }
    }
    //MMS 전송
    public void sendMMS(String phoneNumber, String msg, Bitmap image){
        String pathOfBitmap = MediaStore.Images.Media.insertImage(getContentResolver(), image,"title", null);
        Uri bmpUri = Uri.parse(pathOfBitmap);
        final Intent messageIntent = new Intent(android.content.Intent.ACTION_SEND);
        messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        messageIntent.putExtra("address", phoneNumber);
        messageIntent.putExtra("sms_body", msg);
        messageIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        messageIntent.setType("image/png");
        messageIntent.setAction(Intent.ACTION_SEND);
        startActivity(messageIntent);
    }

    //Face객체에 랜드마크 정보 저장
    protected void saveUserLandmark(Face _faces, String _faceLandmarks){
        Point[] point;
        point = convertStringToPoint(_faces.getStrLandmark());
        for(int j = 0; j < LANDMARK_SIZE; j++){
            _faces.setFaceLandmarks(j, point[j]);
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
