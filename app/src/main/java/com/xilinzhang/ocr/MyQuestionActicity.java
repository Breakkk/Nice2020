package com.xilinzhang.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xilinzhang.ocr.listeners.WebViewClickListener;
import com.xilinzhang.ocr.utils.DataBaseUtils;
import com.xilinzhang.ocr.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyQuestionActicity extends AppCompatActivity {
    LinearLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);

        initView();
    }

    private void initView() {
        container = findViewById(R.id.container);

        parseData();
    }

    private void parseData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("username", MyApplication.userName);
                String str = NetworkUtils.sendPost(NetworkUtils.hostAddr + "userquestion", map).trim();
                String[] idListMulti = str.split(";");
                final List<String> idList = new ArrayList<>();
                Log.d("testlog", "******************");
                for (String id : idListMulti) {
                    if (!idList.contains(id)) {
                        Log.d("testlog", id);
                        idList.add(id);
                    }
                }
                Log.d("testlog", "******************");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (String id : idList) {
                            addItemView(id);
                        }
                    }
                }).start();
            }
        }).start();
    }

    private void addItemView(final String questionID) {
        Map<String, Object> map = new HashMap<>();
        map.put("QuestionID", questionID);
        try {
            final JSONObject jsonObject = new JSONObject(NetworkUtils.sendPost(NetworkUtils.hostAddr + "getQuestionUseId", map));
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    FrameLayout frameLayout = new FrameLayout(MyQuestionActicity.this);
                    WebView webView = new WebView(MyQuestionActicity.this);
                    frameLayout.setBackgroundResource(R.drawable.view_back_round);
                    frameLayout.setPadding(30, 30, 30, 30);
                    String text = null;
                    try {
                        text = jsonObject.getString("ShiTiShow");
                        text = DataBaseUtils.replaceImgURL(text, jsonObject.getString("FilePath"), jsonObject.getString("QuestionID").trim());
                        webView.loadData(text, "text/html", "utf-8");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    WebSettings settings = webView.getSettings();
                    settings.setUseWideViewPort(true);
                    settings.setLoadWithOverviewMode(true);

                    frameLayout.addView(webView);
                    container.addView(frameLayout);
                    LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) frameLayout.getLayoutParams();
                    lllp.setMargins(20, 20, 20, 20);
                    frameLayout.setLayoutParams(lllp);

                    webView.setOnTouchListener(new WebViewClickListener(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MyQuestionActicity.this, ShowDataBaseResultActivity.class);
                            intent.putExtra(DataBaseUtils.SUCCESS_FLAG, true);
                            try {
                                DataBaseUtils.intentProcesser(intent, jsonObject);
                                intent.putExtra("need_record", false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                        }
                    }));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
