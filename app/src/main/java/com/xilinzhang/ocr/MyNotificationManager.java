package com.xilinzhang.ocr;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotificationManager {
    public static void notifyMessage(Context context) {
        @SuppressLint("ServiceCast")
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent clickIntent = new Intent(context, MyQuestionActicity.class);
        PendingIntent clickPI = PendingIntent.getActivity(context, 1, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_big_icon))
                .setContentTitle("好消息好消息！")
                .setContentText("您的提问已经被人回答了哦，快去看看~")
                .setContentIntent(clickPI)
                .setAutoCancel(true);
       manager.notify(1, builder.build());
    }
}
