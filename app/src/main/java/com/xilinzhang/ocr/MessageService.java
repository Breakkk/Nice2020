package com.xilinzhang.ocr;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xilinzhang.ocr.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

public class MessageService extends Service {
    private static final String TAG = "message_service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate:");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                        Log.d(TAG, "my service");
                        Map<String, Object> map = new HashMap<>();
                        map.put("username", MyApplication.userName);
                        String res = NetworkUtils.sendPost(NetworkUtils.hostAddr + "getNewMessage", map);
                        if(res.contains("1")) {
                            MyNotificationManager.notifyMessage(getApplicationContext());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand:");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy:");
        super.onDestroy();
    }
}
