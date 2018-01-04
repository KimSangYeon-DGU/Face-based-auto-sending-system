package com.tzutalin.dlibtest;

import android.content.Intent;
import android.nfc.Tag;
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        ArrayList<String> totalLanmarks = getIntent().getExtras().getStringArrayList("TotalLandmarks");
        Log.d("landmark", totalLanmarks.get(0));

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
}
