package com.xilinzhang.ocr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.xilinzhang.ocr.utils.DataBaseUtils;

public class ShowDataBaseResultActivity extends AppCompatActivity {
    @Nullable
    private String data;

    private WebView show, answer, analysis;
    private FrameLayout failedView;

    private boolean isSuccess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_result);

        isSuccess = getIntent().getExtras().getBoolean(DataBaseUtils.SUCCESS_FLAG);
        initView();
    }

    private void initView() {
        show = findViewById(R.id.shiti_show);
        answer = findViewById(R.id.shiti_answer);
        analysis = findViewById(R.id.shiti_analysis);

        failedView = findViewById(R.id.failed_view);
        if(isSuccess) {
            parseData();
        } else {
            failedView.setVisibility(View.VISIBLE);
        }
    }

    private void parseData() {
        show.loadData(getIntent().getExtras().getString(DataBaseUtils.SHITI_SHOW), "text/html", "utf-8");
        answer.loadData(getIntent().getExtras().getString(DataBaseUtils.SHITI_ANSWER), "text/html", "utf-8");
        WebSettings settings = analysis.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        analysis.loadData(getIntent().getExtras().getString(DataBaseUtils.SHITI_ANALYSIS), "text/html", "utf-8");
    }
}
