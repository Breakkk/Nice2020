package com.xilinzhang.ocr;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
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
                                        Log.d("[test log]", text);
                                        Log.d("[test log]", "***" + json.getString("QuestionID") + "***");
                                        text = DataBaseUtils.replaceImgURL(text, json.getString("FilePath"), json.getString("QuestionID").replaceAll(" ", ""));
                                        Log.d("[test log]", text);
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