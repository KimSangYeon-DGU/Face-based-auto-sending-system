/*
*  Copyright (C) 2015-present TzuTaLin
*/

package com.cse.dlibtest;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BigImageCardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.cse.dlib.Constants;
import com.cse.dlib.FaceDet;
import com.cse.dlib.VisionDetRet;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;
import timber.log.Timber;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    final private int ICON_IMAGE_WIDTH = 128;
    final private int ICON_IMAGE_HEIGHT = 128;
    private Bitmap bm;
    private static final String TAG = "MainActivity";
    private ArrayList<String> totalLandmarks = new ArrayList<String>(); //사진 전체 랜드마크
    private ArrayList<String> testLandmarks = new ArrayList<String>(); //비교용(테스트) 랜드마크
    private ArrayList<byte[]> iconByteArrayList = new ArrayList<byte[]>();
    private int[] path = new int[100];
    // Storage Permissions
    private static String[] PERMISSIONS_REQ = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    protected String mTestImgPath;
    // UI
    @ViewById(R.id.material_listview)
    protected MaterialListView mListView;
    @ViewById(R.id.fab)
    protected FloatingActionButton mFabActionBt;
    @ViewById(R.id.fab_process)
    protected FloatingActionButton mFabProcessActionBt;
    @ViewById(R.id.toolbar)
    protected Toolbar mToolbar;

    FaceDet mFaceDet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        setSupportActionBar(mToolbar);
        // Just use hugo to print log
        isExternalStorageWritable();
        isExternalStorageReadable();

        // For API 23+ you need to request the read/write permissions even if they are already in your manifest.
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.M) {
            verifyPermissions(this);
        }
        extractTestDataLandmarks();
    }
    public void setDrawableImage(){
        path[0] = R.drawable.s1;
        path[1] = R.drawable.s2;
        path[2] = R.drawable.s3;
        path[3] = R.drawable.s4;
        path[4] = R.drawable.s5;
        path[5] = R.drawable.s6;
        path[6] = R.drawable.s7;
        path[7] = R.drawable.s8;
        path[8] = R.drawable.s9;
        path[9] = R.drawable.s10;

        path[10] = R.drawable.s11;
        path[11] = R.drawable.s12;
        path[12] = R.drawable.s13;
        path[13] = R.drawable.s14;
        path[14] = R.drawable.s15;
        path[15] = R.drawable.s16;
        path[16] = R.drawable.s17;
        path[17] = R.drawable.s18;
        path[18] = R.drawable.s19;
        path[19] = R.drawable.s20;

        path[20] = R.drawable.s21;
        path[21] = R.drawable.s22;
        path[22] = R.drawable.s23;
        path[23] = R.drawable.s24;
        path[24] = R.drawable.s25;
        path[25] = R.drawable.s26;
        path[26] = R.drawable.s27;
        path[27] = R.drawable.s28;
        path[28] = R.drawable.s29;
        path[29] = R.drawable.s30;

        path[30] = R.drawable.s31;
        path[31] = R.drawable.s32;
        path[32] = R.drawable.s33;
        path[33] = R.drawable.s34;
        path[34] = R.drawable.s35;
        path[35] = R.drawable.s36;
        path[36] = R.drawable.s37;
        path[37] = R.drawable.s38;
        path[38] = R.drawable.s39;
        path[39] = R.drawable.s40;

        path[40] = R.drawable.s41;
        path[41] = R.drawable.s42;
        path[42] = R.drawable.s43;
        path[43] = R.drawable.s44;
        path[44] = R.drawable.s45;
        path[45] = R.drawable.s46;
        path[46] = R.drawable.s47;
        path[47] = R.drawable.s48;
        path[48] = R.drawable.s49;
        path[49] = R.drawable.s50;

        path[50] = R.drawable.s51;
        path[51] = R.drawable.s52;
        path[52] = R.drawable.s53;
        path[53] = R.drawable.s54;
        path[54] = R.drawable.s55;
        path[55] = R.drawable.s56;
        path[56] = R.drawable.s57;
        path[57] = R.drawable.s58;
        path[58] = R.drawable.s59;
        path[59] = R.drawable.s60;

        path[60] = R.drawable.s61;
        path[61] = R.drawable.s62;
        path[62] = R.drawable.s63;
        path[63] = R.drawable.s64;
        path[64] = R.drawable.s65;
        path[65] = R.drawable.s66;
        path[66] = R.drawable.s67;
        path[67] = R.drawable.s68;
        path[68] = R.drawable.s69;
        path[69] = R.drawable.s70;

        path[70] = R.drawable.s71;
        path[71] = R.drawable.s72;
        path[72] = R.drawable.s73;
        path[73] = R.drawable.s74;
        path[74] = R.drawable.s75;
        path[75] = R.drawable.s76;
        path[76] = R.drawable.s77;
        path[77] = R.drawable.s78;
        path[78] = R.drawable.s79;
        path[79] = R.drawable.s80;

        path[80] = R.drawable.s81;
        path[81] = R.drawable.s82;
        path[82] = R.drawable.s83;
        path[83] = R.drawable.s84;
        path[84] = R.drawable.s85;
        path[85] = R.drawable.s86;
        path[86] = R.drawable.s87;
        path[87] = R.drawable.s88;
        path[88] = R.drawable.s89;
        path[89] = R.drawable.s90;

        path[90] = R.drawable.s91;
        path[91] = R.drawable.s92;
        path[92] = R.drawable.s93;
        path[93] = R.drawable.s94;
        path[94] = R.drawable.s95;
        path[95] = R.drawable.s96;
        path[96] = R.drawable.s97;
        path[97] = R.drawable.s98;
        path[98] = R.drawable.s99;
        path[99] = R.drawable.s100;

    }
    //가상 주소록 이미지 불러오기
    protected void extractTestDataLandmarks(){
        setDrawableImage();

        String testLandmark = "";
        for(int i = 0 ; i < path.length; i++) {
            testLandmark = extractAddressBookLandmarks(path[i]);
            Log.d("Report", Integer.toString(i)+": "+testLandmark);
            testLandmarks.add(testLandmark);
        }
        Log.d("Landmark", testLandmark);
    }
    //랜드마크 추출
    protected String extractAddressBookLandmarks(int id){
        FaceDet fDet = new FaceDet(Constants.getFaceShapeModelPath());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
        List<VisionDetRet> results = fDet.detect(bitmap);
        String tempTestLandmarks = "";
        for (final VisionDetRet ret : results) {
            // Get 68 landmark points
            ArrayList<Point> landmarks = ret.getFaceLandmarks();
            for (Point point : landmarks) {
                int pointX = point.x;
                int pointY = point.y;
                tempTestLandmarks += Integer.toString(pointX);
                tempTestLandmarks += ",";
                tempTestLandmarks += Integer.toString(pointY);
                tempTestLandmarks += ",";
            }
        }
        return tempTestLandmarks;
    }

    @AfterViews
    protected void setupUI() {
        mToolbar.setTitle(getString(R.string.app_name));
        Toast.makeText(MainActivity.this, getString(R.string.description_info), Toast.LENGTH_LONG).show();
    }

    @Click({R.id.fab})
    protected void launchGallery() {
        Toast.makeText(MainActivity.this, "Pick one image", Toast.LENGTH_SHORT).show();
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Click({R.id.fab_process}) //process button
    protected void searchPeople() {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();
        Intent sendIntent = new Intent(MainActivity.this, SendActivity.class);
        sendIntent.putStringArrayListExtra("TotalLandmarks", totalLandmarks);
        sendIntent.putStringArrayListExtra("AddrBookLandmarks", testLandmarks);
        sendIntent.putExtra("image", byteArray);
        int iconByteArraySize = iconByteArrayList.size();
        sendIntent.putExtra("ByteArraySize", iconByteArraySize);
        for(int i = 0; i < iconByteArraySize; i++) {
            sendIntent.putExtra("icon"+Integer.toString(i), iconByteArrayList.get(i));
        }
        startActivity(sendIntent);
        finish();
    }

    /**
     * Checks if the app has permission to write to device storage or open camera
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    @DebugLog
    private static boolean verifyPermissions(Activity activity) {
        // Check if we have write permission
        int write_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read_persmission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (write_permission != PackageManager.PERMISSION_GRANTED ||
                read_persmission != PackageManager.PERMISSION_GRANTED ||
                camera_permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_REQ,
                    REQUEST_CODE_PERMISSION
            );
            return false;
        } else {
            return true;
        }
    }

    /* Checks if external storage is available for read and write */
    @DebugLog
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    @DebugLog
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @DebugLog
    protected void demoStaticImage() {
        if (mTestImgPath != null) {
            Timber.tag(TAG).d("demoStaticImage() launch a task to det");
            runDetectAsync(mTestImgPath);
        } else {
            Timber.tag(TAG).d("demoStaticImage() mTestImgPath is null, go to gallery");
            Toast.makeText(MainActivity.this, "Pick an image to run algorithms", Toast.LENGTH_SHORT).show();
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            Toast.makeText(MainActivity.this, "Demo using static images", Toast.LENGTH_SHORT).show();
            demoStaticImage();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mTestImgPath = cursor.getString(columnIndex);
                cursor.close();
                if (mTestImgPath != null) {
                    runDetectAsync(mTestImgPath);
                    Toast.makeText(this, "Img Path:" + mTestImgPath, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    // ==========================================================
    // Tasks inner class
    // ==========================================================
    private ProgressDialog mDialog;

    @Background
    @NonNull
    protected void runDetectAsync(@NonNull String imgPath) {
        showDiaglog();

        final String targetPath = Constants.getFaceShapeModelPath();
        if (!new File(targetPath).exists()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Copy landmark model to " + targetPath, Toast.LENGTH_SHORT).show();
                }
            });
            FileUtils.copyFileFromRawToOthers(getApplicationContext(), R.raw.shape_predictor_68_face_landmarks, targetPath);
        }
        // Init
        if (mFaceDet == null) {
            mFaceDet = new FaceDet(Constants.getFaceShapeModelPath());
        }

        Timber.tag(TAG).d("Image path: " + imgPath);
        List<Card> cardrets = new ArrayList<>();
        List<VisionDetRet> faceList = mFaceDet.detect(imgPath);
        if (faceList.size() > 0) {
            Card card = new Card.Builder(MainActivity.this)
                    .withProvider(BigImageCardProvider.class)
                    .setDrawable(drawRect(imgPath, faceList, Color.GREEN))
                    .setTitle("Face det")
                    .endConfig()
                    .build();
            cardrets.add(card);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "No face", Toast.LENGTH_SHORT).show();
                }
            });
        }
        addCardListView(cardrets);
        dismissDialog();
    }

    @UiThread
    protected void addCardListView(List<Card> cardrets) {
        for (Card each : cardrets) {
            mListView.add(each);
        }
    }

    @UiThread
    protected void showDiaglog() {
        mDialog = ProgressDialog.show(MainActivity.this, "Wait", "Face detection", true);
    }

    @UiThread
    protected void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @DebugLog
    protected BitmapDrawable drawRect(String path, List<VisionDetRet> results, int color) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        bm = BitmapFactory.decodeFile(path, options);
        android.graphics.Bitmap.Config bitmapConfig = bm.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bm = bm.copy(bitmapConfig, true);
        int width = bm.getWidth();
        int height = bm.getHeight();
        // By ratio scale
        float aspectRatio = width / (float) height;

        final int MAX_SIZE = 512;
        int newWidth = MAX_SIZE;
        float resizeRatio = 1;
        int newHeight = Math.round(newWidth / aspectRatio);
        if (width > MAX_SIZE && height > MAX_SIZE) {
            Timber.tag(TAG).d("Resize Bitmap");
            bm = getResizedBitmap(bm, newWidth, newHeight);
            resizeRatio = (float) bm.getWidth() / (float) width;
            Timber.tag(TAG).d("resizeRatio " + resizeRatio);
        }

        // Create canvas to draw
        Canvas canvas = new Canvas(bm);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        // Loop result list
        Bitmap icon;
        for (VisionDetRet ret : results) {
            Rect bounds = new Rect();
            bounds.left = (int) (ret.getLeft() * resizeRatio);
            bounds.top = (int) (ret.getTop() * resizeRatio);
            bounds.right = (int) (ret.getRight() * resizeRatio);
            bounds.bottom = (int) (ret.getBottom() * resizeRatio);
            int imageWidth = bounds.right - bounds.left;
            int imageHeight = bounds.bottom - bounds.top;
            int x = bounds.left - (imageWidth/3);
            int y = (bounds.top - (imageHeight/3));
            int exWidth = imageWidth + (imageWidth/2);
            int exHeight = imageHeight + (imageWidth/2);
            if(x < 0) x = 0;
            if(y < 0) y = 0;
            if(exWidth > bm.getWidth()) exWidth = bm.getWidth();
            if(exHeight > bm.getHeight()) exHeight = bm.getHeight();
            icon = Bitmap.createBitmap(bm, x, y, exWidth, exHeight);
            icon = getResizedBitmap(icon, ICON_IMAGE_WIDTH, ICON_IMAGE_HEIGHT); //icon으로 재생성

            //비트맵인 icon을 byyeArray로 변환후 저장
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();
            iconByteArrayList.add(byteArray);

            //canvas.drawRect(bounds, paint); //얼굴 사각형 그리기
            // Get landmark
            ArrayList<Point> landmarks = ret.getFaceLandmarks();
            String tempLandmark = "";
            int index = 0;
            for (Point point : landmarks) {
                int pointX = (int) (point.x * resizeRatio);
                int pointY = (int) (point.y * resizeRatio);
                tempLandmark += Integer.toString(pointX);
                tempLandmark += ",";
                tempLandmark += Integer.toString(pointY);
                tempLandmark += ",";
                //canvas.drawCircle(pointX, pointY, 2, paint); //랜드마크 그리기
            }
            totalLandmarks.add(tempLandmark);
        }
        return new BitmapDrawable(getResources(), bm);
    }

    @DebugLog
    protected Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return resizedBitmap;
    }

    public byte[] convertBitmapToByteArray(Bitmap bitmp){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmp.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        return bStream.toByteArray();
    }
}
