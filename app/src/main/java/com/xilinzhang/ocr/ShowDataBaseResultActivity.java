package com.xilinzhang.ocr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.xilinzhang.ocr.utils.DataBaseUtils;
import com.xilinzhang.ocr.utils.NetworkUtils;
import com.xilinzhang.ocr.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShowDataBaseResultActivity extends AppCompatActivity {
    @Nullable
    private String data;

    private WebView show, answer, analysis;
    private FrameLayout failedView;

    Uri imgUri;

    private boolean isSuccess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_result);

        isSuccess = getIntent().getExtras().getBoolean(DataBaseUtils.SUCCESS_FLAG);
        imgUri = (Uri) getIntent().getExtras().get("imgUri");
        initView();
    }

    private void initView() {
        show = findViewById(R.id.shiti_show);
        answer = findViewById(R.id.shiti_answer);
        analysis = findViewById(R.id.shiti_analysis);

        failedView = findViewById(R.id.failed_view);
        if (isSuccess) {
            parseData();
        } else {
            failedView.setVisibility(View.VISIBLE);
            findViewById(R.id.new_question).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShowDataBaseResultActivity.this, NewQuestionActivity.class);
                    intent.putExtra("imgPath", getIntent().getExtras().getString("imgPath"));
                    intent.putExtra(NewQuestionActivity.OCR_RESULT, getIntent().getExtras().getString(NewQuestionActivity.OCR_RESULT));
                    intent.putExtra("imgUri", imgUri);
                    startActivity(intent);
                }
            });
        }
    }

    private void parseData() {
        show.loadData(getIntent().getExtras().getString(DataBaseUtils.SHITI_SHOW), "text/html", "utf-8");
        answer.loadData(getIntent().getExtras().getString(DataBaseUtils.SHITI_ANSWER), "text/html", "utf-8");
        WebSettings settings = analysis.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        analysis.loadData(getIntent().getExtras().getString(DataBaseUtils.SHITI_ANALYSIS), "text/html", "utf-8");

        Log.d("testlog", getIntent().getExtras().getString(DataBaseUtils.SHITI_SHOW));
        Log.d("testlog", getIntent().getExtras().getString(DataBaseUtils.SHITI_ANALYSIS));
        Log.d("testlog", getIntent().getExtras().getString(DataBaseUtils.SHITI_ANSWER));

        if(getIntent().getExtras().getBoolean("need_record")) {
            addHistory();
        }
    }

    private void addHistory() {
        Record record = new Record(
                imgUri.toString(),
                getIntent().getExtras().getString(DataBaseUtils.SHITI_SHOW),
                getIntent().getExtras().getString(DataBaseUtils.SHITI_ANSWER),
                getIntent().getExtras().getString(DataBaseUtils.SHITI_ANALYSIS)
        );
        //添加历史纪
        List<Record> old = (List<Record>) Utils.getLocalHistory();
        if(old == null) {
            old = new ArrayList<>();
        }
        for(Record item: old) {
            if(item.getImgUri().equals(record.getImgUri())) {
                return;
            }
        }
        old.add(record);
        Utils.setLocalHistory(old);
    }
}
