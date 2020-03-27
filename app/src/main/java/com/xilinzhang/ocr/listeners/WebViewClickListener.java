package com.xilinzhang.ocr.listeners;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xilinzhang.ocr.DoQuestionActivity;
import com.xilinzhang.ocr.QuestionListActivity;

import org.json.JSONException;

public class WebViewClickListener implements View.OnTouchListener {
    boolean isMove = false;
    float x, y;
    Runnable runnable;

    public WebViewClickListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d("[uploadFileWithJson log]", isMove + motionEvent.toString());
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            isMove = false;
            x = motionEvent.getX();
            y = motionEvent.getY();
            return false;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(motionEvent.getY() - y) >= 10) {
                isMove = true;
                return false;
            }
        }

        if (isMove) {
            return false;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d("[uploadFileWithJson log]", "onTouch");
            runnable.run();
        }
        return false;
    }
}
