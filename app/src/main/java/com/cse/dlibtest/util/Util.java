package com.cse.dlibtest.util;

import android.graphics.Bitmap;
import android.graphics.Point;

import com.cse.dlib.Constants;
import com.cse.dlib.FaceDet;
import com.cse.dlib.VisionDetRet;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by sy081 on 2018-01-16.
 */

public class Util {
    //주소록 랜드마크 추출
    public static String extractAddressBookLandmarks(Bitmap bitmap){
        FaceDet fDet = new FaceDet(Constants.getFaceShapeModelPath());
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
    @DebugLog
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
        return resizedBitmap;
    }
}
