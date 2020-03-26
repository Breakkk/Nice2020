package com.xilinzhang.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.xilinzhang.ocr.utils.DataBaseUtils;
import com.xilinzhang.ocr.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QuestionListActivity extends AppCompatActivity {
    AppCompatButton search;

    AppCompatSpinner subject, type;

    LinearLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        initView();
    }

    private void initView() {
        search = findViewById(R.id.search);
        subject = findViewById(R.id.subject);
        type = findViewById(R.id.type);
        container = findViewById(R.id.container);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container.removeAllViews();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = new HashMap<>();
                        map.put("SubjectName", subject.getSelectedItem().toString());
                        map.put("TypeName", type.getSelectedItem().toString());
                        final JSONObject json;
                        try {
                            json = new JSONObject(NetworkUtils.sendPost(NetworkUtils.hostAddr + "unresovle", map));
                            new Handler(getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    WebView webView = new WebView(QuestionListActivity.this);
                                    try {
                                        String text = json.getString("ShiTiShow");
                                        Log.d("[uploadFileWithJson log]", text);
                                        Log.d("[uploadFileWithJson log]", "***" + json.getString("QuestionID") + "***");
                                        text = DataBaseUtils.replaceImgURL(text, json.getString("FilePath"), json.getString("QuestionID").replaceAll(" ", ""));
                                        Log.d("[uploadFileWithJson log]", text);
                                        webView.loadData(text, "text/html", "utf-8");
                                        WebSettings settings = webView.getSettings();
                                        settings.setUseWideViewPort(true);
                                        settings.setLoadWithOverviewMode(true);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    container.addView(webView);
                                    LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) webView.getLayoutParams();
                                    lllp.setMargins(20, 20, 20, 20);
                                    webView.setLayoutParams(lllp);

                                    webView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d("[uploadFileWithJson log]", "onClick");
                                            Intent intent = new Intent(QuestionListActivity.this, DoQuestionActivity.class);
                                            startActivity(intent);
                                        }
                                    });

                                    webView.setOnTouchListener(new View.OnTouchListener() {
                                        boolean isMove = false;
                                        float x, y;
                                        @Override
                                        public boolean onTouch(View view, MotionEvent motionEvent) {
                                            Log.d("[uploadFileWithJson log]", isMove + motionEvent.toString());
                                            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                                isMove = false;
                                                x = motionEvent.getX();
                                                y = motionEvent.getY();
                                                return false;
                                            }

                                            if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                                                if(Math.abs(motionEvent.getY() - y) >= 10) {
                                                    isMove = true;
                                                    return false;
                                                }
                                            }

                                            if(isMove) {
                                                return  false;
                                            }

                                            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                                Log.d("[uploadFileWithJson log]", "onTouch");
                                                Intent intent = new Intent(QuestionListActivity.this, DoQuestionActivity.class);
                                                try {
                                                    intent.putExtra("QuestionID", json.getString("QuestionID"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                startActivity(intent);
                                            }
                                            return false;
                                        }
                                    });
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}