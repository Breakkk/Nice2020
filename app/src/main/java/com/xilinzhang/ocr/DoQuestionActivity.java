package com.xilinzhang.ocr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.xilinzhang.ocr.utils.LevelUtils;
import com.xilinzhang.ocr.utils.NetworkUtils;
import com.xilinzhang.ocr.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoQuestionActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO_RESULT = 1003;
    public static final int TAKE_PHOTO_FOR_ANSWER_REQUEST = 1001;
    public static final int TAKE_PHOTO_FOR_ANALYSIS_REQUEST = 1002;

    AppCompatImageView answer, analysis;
    AppCompatButton upload;

    String answerPath, analysisPath;
    Uri answerUri, analysisUri;

    FrameLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_question);

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == TAKE_PHOTO_RESULT) {
            if (requestCode == TAKE_PHOTO_FOR_ANSWER_REQUEST) {
                answerUri = data.getData();
                answerPath = data.getExtras().getString("path");
                if (answerUri != null) {
                    answer.setImageURI(answerUri);
                    answer.setEnabled(false);
                }
            } else if (requestCode == TAKE_PHOTO_FOR_ANALYSIS_REQUEST) {
                analysisUri = data.getData();
                analysisPath = data.getExtras().getString("path");
                if (analysisUri != null) {
                    analysis.setImageURI(analysisUri);
                    analysis.setEnabled(false);
                }
            }
        }
    }

    private void initView() {
        root = findViewById(R.id.root_view);
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
                if (answerPath == null || analysisPath == null) {
                    return;
                }

                final List<FileItem> fileList = new ArrayList<>();
                fileList.add(new FileItem(new File(answerPath), "answer"));
                fileList.add(new FileItem(new File(analysisPath), "analysis"));
                final ProgressDialog dialog = new ProgressDialog(DoQuestionActivity.this);
                dialog.setCancelable(false);
                dialog.setMessage("正在上传...");
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = new HashMap<>();
                        map.put("QuestionID", getIntent().getExtras().getString("QuestionID").trim());
                        if(MyApplication.isSignIned) {
                            map.put("username", MyApplication.userName);
                        }
                        NetworkUtils.test(NetworkUtils.hostAddr + "handle_new_answer", fileList, map);
                        dialog.dismiss();
                        //TODO get exp
                        LevelUtils.addExpWhenDoQuestion(DoQuestionActivity.this, root);
                    }
                }).start();
            }
        });
    }

    public static class FileItem {
        private File file;
        private String name;
        public FileItem(File file, String name) {
            this.file =  file;
            this.name = name;
        }

        public File getFile() {
            return file;
        }

        public String getName() {
            return name;
        }
    }
}
