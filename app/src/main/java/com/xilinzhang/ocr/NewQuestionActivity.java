package com.xilinzhang.ocr;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.xilinzhang.ocr.utils.NetworkUtils;

public class
NewQuestionActivity extends AppCompatActivity {
    public static final String OCR_RESULT = "ocr_result";

    Uri imgUri;
    String text;

    AppCompatImageView imgView;
    AppCompatEditText ocrView;

    AppCompatButton upload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_question);
        initView();
    }

    void initView() {
        imgView = findViewById(R.id.img);
        ocrView = findViewById(R.id.ocr_result);
        upload = findViewById(R.id.upload);

        imgUri = (Uri) getIntent().getExtras().get("imgUri");
        text = getIntent().getExtras().getString(OCR_RESULT);

        imgView.setImageURI(imgUri);
        ocrView.setText(text);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NetworkUtils.sendPost(NetworkUtils.hostAddr + );
                        NetworkUtils.uploadFile();
                    }
                }).start();
            }
        });

    }
}
