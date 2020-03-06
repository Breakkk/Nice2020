package com.xilinzhang.ocr.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.xilinzhang.ocr.MyApplication;
import com.xilinzhang.ocr.Record;
import com.xilinzhang.ocr.SharedPerferenceHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;
import java.util.Set;


/**
 * 工具类
 */
public class Utils {

    public static DisplayMetrics getScreenWH(Context context) {
        DisplayMetrics dMetrics = new DisplayMetrics();
        dMetrics = context.getResources().getDisplayMetrics();
        return dMetrics;
    }

    public static int getHeightInPx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getWidthInPx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static Rect calculateTapArea(int focusWidth, int focusHeight,
                                        float areaMultiple, float x, float y, int previewleft,
                                        int previewRight, int previewTop, int previewBottom) {
        int areaWidth = (int) (focusWidth * areaMultiple);
        int areaHeight = (int) (focusHeight * areaMultiple);
        int centerX = (previewleft + previewRight) / 2;
        int centerY = (previewTop + previewBottom) / 2;
        double unitx = ((double) previewRight - (double) previewleft) / 2000;
        double unity = ((double) previewBottom - (double) previewTop) / 2000;

        int left = clamp((int) (((x - areaWidth / 2) - centerX) / unitx),
            -1000, 1000);
        int top = clamp((int) (((y - areaHeight / 2) - centerY) / unity),
            -1000, 1000);
        int right = clamp((int) (left + areaWidth / unitx), -1000, 1000);
        int bottom = clamp((int) (top + areaHeight / unity), -1000, 1000);

        return new Rect(left, top, right, bottom);
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    public static boolean checkCameraHardware(Context context) {
        if (context != null && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static final String LOCAL_HISTORY_KEY = "history_";

    public static List<?> getLocalHistory() {
        String str = SharedPerferenceHelper.getValue(LOCAL_HISTORY_KEY, null);
        try {
            return stringToList(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void setLocalHistory(List<?> recordList) {
        String str = null;
        try {
            str = listToString(recordList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPerferenceHelper.putValue(LOCAL_HISTORY_KEY, str);
    }

    public static String listToString(List<?> list)throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(list);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String listString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return listString;
    }

    @SuppressWarnings("unchecked")
    public static List<?> stringToList(String listString) throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(listString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List<?> WeatherList = (List<?>) objectInputStream
                .readObject();
        objectInputStream.close();
        return WeatherList;
    }

    public static String testGet() {
        return SharedPerferenceHelper.getValue("test_string", "");
    }
    public static void testSet() {
        SharedPerferenceHelper.putValue("test_string", testGet()+"1");
    }
}
