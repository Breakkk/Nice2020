package com.xilinzhang.ocr;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;

import com.xilinzhang.ocr.utils.NetworkUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class
NewQuestionActivity extends AppCompatActivity {
    public static final String OCR_RESULT = "ocr_result";

    Uri imgUri;
    String imgPath;
    String text;

    AppCompatImageView imgView;
    AppCompatEditText ocrView;

    AppCompatButton upload;

    AppCompatSpinner subject, type;

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
        subject = findViewById(R.id.subject);
        type = findViewById(R.id.type);

        imgUri = (Uri) getIntent().getExtras().get("imgUri");
        imgPath = getIntent().getExtras().getString("imgPath");
        text = getIntent().getExtras().getString(OCR_RESULT);

        imgView.setImageURI(imgUri);
        ocrView.setText(text);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(NewQuestionActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("正在上传...");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = new HashMap<>();
                        map.put("SubjectName", subject.getSelectedItem().toString());
                        map.put("TypeName", type.getSelectedItem().toString());
                        map.put("ShiTiShow", ocrView.getText().toString());
                        NetworkUtils.uploadFileWithJson(NetworkUtils.hostAddr + "uploadfile", "media", new File(imgPath), map);
                        dialog.dismiss();
                    }
                }).start();
            }
        });

    }
}
