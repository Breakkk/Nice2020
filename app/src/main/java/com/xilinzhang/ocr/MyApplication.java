package com.xilinzhang.ocr;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.xilinzhang.ocr.utils.Utils;

/**
 * Application
 */
public class MyApplication extends Application {
    public static boolean isSignIned = false;
    public static String userName = "";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPerferenceHelper.init(this, "application");
    }
}
