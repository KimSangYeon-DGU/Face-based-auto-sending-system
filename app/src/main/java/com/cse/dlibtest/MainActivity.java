/*
*  Copyright (C) 2015-present TzuTaLin
*/

package com.cse.dlibtest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.cse.dlibtest.model.AddressBook;
import com.cse.dlibtest.adapter.ContactsAdapter;
import com.cse.dlibtest.model.Face;
import com.cse.dlibtest.ui.SendActivity;
import com.cse.dlibtest.util.FileUtils;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hugo.weaving.DebugLog;
import timber.log.Timber;

import static com.cse.dlibtest.util.Util.extractAddressBookLandmarks;
import static com.cse.dlibtest.util.Util.getResizedBitmap;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private static final int PICK_PHONE_DATA = 3;
    final private int ICON_IMAGE_WIDTH = 128;
    final private int ICON_IMAGE_HEIGHT = 128;
    private Bitmap bm;
    private static final String TAG = "MainActivity";
    private ArrayList<AddressBook> addressBooks = new ArrayList<>();
    private ArrayList<Face> faces = new ArrayList<>();

    private ContactsAdapter mAdapter;
    // Storage Permissions
    private static String[] PERMISSIONS_REQ = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    protected String mTestImgPath;
    // UI
    @ViewById(R.id.rv_contacts)
    protected RecyclerView mAddressView;
    @ViewById(R.id.material_listview)
    protected MaterialListView mListView;
    @ViewById(R.id.fab)
    protected FloatingActionButton mFabActionBt;
    @ViewById(R.id.fab_process)
    protected FloatingActionButton mFabProcessActionBt;
    @ViewById(R.id.fab_insert)
    protected FloatingActionButton mFabInsertActionBt;

    FaceDet mFaceDet;

    //CONTACTS DATA SET
    static final String[] field = new String[] {
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        mAdapter = new ContactsAdapter(this, addressBooks);
        init();
    }
    public void insertUser(String name, String phoneNumber, Bitmap image){
        int size = addressBooks.size();
        Bitmap icon = getResizedBitmap(image, ICON_IMAGE_WIDTH, ICON_IMAGE_HEIGHT);
        addressBooks.add(new AddressBook(size+1, name, phoneNumber, image, icon,""));
        mAdapter.notifyDataSetChanged();
    }
    public boolean deleteUser(int id){
        try{
            addressBooks.remove(id);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void init(){
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, field, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

        ContentResolver cr = getContentResolver();
        InputStream input = null;

        int idx = 1;
        //한 행씩 이동
        while(c.moveToNext()) {
            //연락처 ID이 여기 위치
            String number = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            //연락처 대표 이름이 여기 위치
            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            int contactId_idx = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            Long contactId = c.getLong(contactId_idx);

            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);

            input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
            Bitmap contactPhoto = BitmapFactory.decodeStream(input);
            //contactPhoto is NULL...... should fix

            if(input != null) {
                Bitmap icon = getResizedBitmap(contactPhoto, ICON_IMAGE_WIDTH, ICON_IMAGE_HEIGHT);
                addressBooks.add(new AddressBook(idx++, name, number, contactPhoto, icon, ""));
            }
        }
        mAdapter.swapData(addressBooks);
    }

    @AfterViews
    protected void setupUI() {
        Toast.makeText(MainActivity.this, getString(R.string.description_info), Toast.LENGTH_LONG).show();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mAddressView.setHasFixedSize(true);
        mAddressView.setLayoutManager(linearLayoutManager);

        mAddressView.setAdapter(mAdapter);
    }

    @Click({R.id.fab})
    protected void launchGallery() {
        Toast.makeText(MainActivity.this, "Pick one image", Toast.LENGTH_SHORT).show();
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Click({R.id.fab_process}) //process button
    protected void searchPeople() {
        runExtractAsync();
    }

    @Click({R.id.fab_insert})
    protected void launchAddressList(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, PICK_PHONE_DATA);
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
            } else if(resultCode == RESULT_OK)
            {
                switch (requestCode){
                    case PICK_PHONE_DATA:
                        Cursor cursor = getContentResolver().query(data.getData(),
                                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI}, null, null, null);
                        cursor.moveToFirst();
                        String name = cursor.getString(0);     //0은 이름을 얻어온다.
                        String phone = cursor.getString(1);   //1은 번호를 받아온다.
                        String image_uri = cursor.getString(2);
                        if(image_uri != null) {
                            Bitmap contactPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(image_uri));
                            cursor.close();
                            insertUser(name, phone, contactPhoto);
                        } else{
                            Toast.makeText(this, "Cannot add the contact, Because the photo do not exist", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                }
            }
            else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    // ==========================================================
    // Tasks inner class
    // ==========================================================
    private ProgressDialog mDialog, mExtractDialog;
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

    @Background
    protected  void runExtractAsync(){
        showExtractDialog();
        int AdrressBookSize = addressBooks.size();
        for(int i = 0; i < AdrressBookSize; i++){
            String extractedLandmark = extractAddressBookLandmarks(addressBooks.get(i).getFace().getIcon());
            addressBooks.get(i).getFace().setStrLandmark(extractedLandmark);
        }
        /*addressBooks.get(0).getFace().setStrLandmark("14,47,15,55,17,63,19,70,23,77,28,83,34,88,40,92,47,93,55,91,61,86,66,80,71,74,74,66,75,58,76,50,76,41,18,40,22,35,27,33,33,33,39,35,49,34,55,32,60,30,66,31,70,36,45,44,45,50,46,57,46,63,39,66,43,67,46,68,50,66,53,65,26,46,29,44,33,44,36,46,33,47,29,47,54,45,57,42,60,42,64,43,61,45,57,45,35,73,39,72,43,72,46,72,49,72,52,72,57,73,52,75,49,77,46,77,43,77,39,75,37,73,43,73,46,74,49,73,55,73,49,74,46,74,43,74,");
        addressBooks.get(1).getFace().setStrLandmark("11,49,11,57,12,65,13,73,15,81,20,87,26,92,33,96,42,96,50,95,57,92,64,87,69,81,72,74,73,66,74,58,74,50,18,43,21,39,27,37,32,37,38,39,48,39,53,37,59,37,64,39,67,43,43,47,43,53,43,58,43,64,37,68,40,69,43,70,46,69,49,68,24,48,28,46,32,46,35,49,31,49,27,49,51,49,54,46,58,46,61,48,58,49,54,49,31,76,36,75,40,74,43,74,46,74,50,75,54,76,50,78,46,79,43,80,40,80,36,79,32,76,40,76,43,77,46,76,53,76,46,76,43,77,40,76,");
        addressBooks.get(2).getFace().setStrLandmark("9,43,9,52,10,61,12,69,15,77,20,84,27,90,36,93,45,94,54,92,61,88,67,83,71,76,73,69,74,61,75,52,75,44,18,31,22,27,28,25,34,26,40,28,49,29,54,26,60,26,66,28,69,32,45,36,45,40,45,45,45,50,39,57,42,57,45,58,47,57,50,57,24,38,28,36,32,36,35,38,32,38,28,38,52,38,56,36,60,37,63,38,60,39,56,39,32,70,37,66,42,64,45,65,48,64,52,66,56,69,52,71,48,72,45,72,42,72,37,72,34,69,42,68,45,68,48,68,54,69,48,68,45,69,42,69,");*/
        dismissExtractDialog();
    }
    @UiThread
    protected void addCardListView(List<Card> cardrets) {
        for (Card each : cardrets) {
            mListView.add(each);
        }
    }

    @UiThread
    protected void showDiaglog() {
        mDialog = ProgressDialog.show(MainActivity.this, "Wait", "Detecting faces", true);
    }

    @UiThread
    protected void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @UiThread
    protected void showExtractDialog(){
        mExtractDialog = ProgressDialog.show(MainActivity.this, "Wait", "Extracting Landmarks", true);
    }

    @UiThread
    protected void dismissExtractDialog(){
        if(mExtractDialog != null){
            Intent sendIntent = new Intent(MainActivity.this, SendActivity.class);
            sendIntent.putExtra("FaceSize", faces.size());
            for(int i = 0; i < faces.size(); i++) {
                sendIntent.putExtra("FaceLandmarks"+Integer.toString(i), faces.get(i).getStrLandmark());//사진 속 얼굴 랜드마크 전송
                sendIntent.putExtra("FaceIcon"+Integer.toString(i), convertBitmapToByteArray(faces.get(i).getIcon())); //사진 속 얼굴 아이콘 전송
            }

            sendIntent.putExtra("AddrBookSize", addressBooks.size());
            for(int i = 0; i < addressBooks.size(); i++) {
                sendIntent.putExtra("AddrBookID"+Integer.toString(i), Integer.toString(addressBooks.get(i).getId()));
                sendIntent.putExtra("AddrBookName"+Integer.toString(i), addressBooks.get(i).getName());
                sendIntent.putExtra("AddrBookPhoneNumber"+Integer.toString(i), addressBooks.get(i).getPhoneNumber());
                sendIntent.putExtra("AddrBookLandmark"+Integer.toString(i), addressBooks.get(i).getFace().getStrLandmark());
                sendIntent.putExtra("AddrBookImage"+Integer.toString(i), convertBitmapToByteArray(getResizedBitmap(addressBooks.get(i).getFace().getImage(),ICON_IMAGE_WIDTH, ICON_IMAGE_HEIGHT))); //사진 속 얼굴 아이콘 전송
            }

            sendIntent.putExtra("Image", convertBitmapToByteArray(bm)); //사용자가 선택한 사진 전송
            startActivity(sendIntent);
            mExtractDialog.dismiss();
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
            //canvas.drawRect(bounds, paint); //얼굴 사각형 그리기
            // Get landmark
            ArrayList<Point> landmarks = ret.getFaceLandmarks();
            String tempLandmark = "";
            for (Point point : landmarks) {
                int pointX = (int) (point.x * resizeRatio);
                int pointY = (int) (point.y * resizeRatio);
                tempLandmark += Integer.toString(pointX);
                tempLandmark += ",";
                tempLandmark += Integer.toString(pointY);
                tempLandmark += ",";
                //canvas.drawCircle(pointX, pointY, 2, paint); //랜드마크 그리기
            }
            Face face = new Face();
            face.setIcon(icon);
            face.setStrLandmark(tempLandmark);
            faces.add(face);
        }
        return new BitmapDrawable(getResources(), bm);
    }


    public byte[] convertBitmapToByteArray(Bitmap bitmp){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmp.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        return bStream.toByteArray();
    }
}
