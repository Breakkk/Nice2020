package com.xilinzhang.ocr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.xilinzhang.ocr.utils.Utils;

public class DoQuestionActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO_RESULT = 1003;
    public static final int TAKE_PHOTO_FOR_ANSWER_REQUEST = 1001;
    public static final int TAKE_PHOTO_FOR_ANALYSIS_REQUEST = 1002;

    AppCompatImageView answer, analysis;
    AppCompatButton upload;

    String answerPath, analysisPath;
    Uri answerUri, analysisUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_question);

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == TAKE_PHOTO_RESULT) {
            if(requestCode == TAKE_PHOTO_FOR_ANSWER_REQUEST) {
                answerUri = data.getData();
                answerPath = data.getExtras().getString("path");
                if(answerUri != null) {
                    answer.setImageURI(answerUri);
                    answer.setEnabled(false);
                }
            } else if (requestCode == TAKE_PHOTO_FOR_ANALYSIS_REQUEST) {
                analysisUri = data.getData();
                analysisPath = data.getExtras().getString("path");
                if(analysisUri != null) {
                    analysis.setImageURI(analysisUri);
                    analysis.setEnabled(false);
                }
            }
        }
    }

    private void initView() {
        answer = findViewById(R.id.answer);
        analysis = findViewById(R.id.analysis);
        upload = findViewById(R.id.upload);

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startCameraForResult(DoQuestionActivity.this, TAKE_PHOTO_FOR_ANSWER_REQUEST);
            }
        });

        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startCameraForResult(DoQuestionActivity.this, TAKE_PHOTO_FOR_ANALYSIS_REQUEST);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
