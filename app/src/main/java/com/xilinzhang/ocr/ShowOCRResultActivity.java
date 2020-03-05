package com.xilinzhang.ocr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.xilinzhang.ocr.utils.BaiduOCRUtils;
import com.xilinzhang.ocr.utils.DataBaseUtils;
import com.xilinzhang.ocr.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShowOCRResultActivity extends AppCompatActivity {
    private static final String TAG = "ShowOCRResultActivity";

    private ImageView imageView;
    private TextView textView;
    private AppCompatButton search;

    private Uri uri;
    private String imgPath;

    private Handler handler = new Handler();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_answer);

        Thread myThread = new Thread(runnable);
        dialog = new ProgressDialog(this);
        dialog.setMessage("正在识别...");
        dialog.setCancelable(false);
        dialog.show();

        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.text);

        search = findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Map<String, Object> map = new HashMap<>();
                        String text = textView.getText().toString();
                        String keyword = text.substring(text.indexOf("("), text.indexOf(")") + 1);
                        map.put("username", MyApplication.userName);
                        map.put("signed", MyApplication.isSignIned);
                        map.put("keyword", keyword);
                        final JSONObject json;
                        Intent intent = new Intent(ShowOCRResultActivity.this, ShowDataBaseResultActivity.class);
                        intent.putExtra("imgUri", uri);
                        try {
                            json = new JSONObject(NetworkUtils.sendPost(NetworkUtils.hostAddr + DataBaseUtils.SERVER_POST_URL_SEARCH, map));
//                            final String ans = DataBaseUtils.getFormatHTMLStr(json);
//                            intent.putExtra("data", ans);
                            int a = 1;
                            DataBaseUtils.intentProcesser(intent, json);
                            intent.putExtra(DataBaseUtils.SUCCESS_FLAG, true);
                        } catch (Exception e) {
                            Log.d("error", e.toString());
                            intent.putExtra(DataBaseUtils.SUCCESS_FLAG, false);
                        } finally {
                            startActivity(intent);
                        }
                    }
                }).start();
            }
        });

        uri = getIntent().getData();
        imgPath = getIntent().getExtras().getString("path");
        imageView.setImageURI(uri);
        myThread.start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            BaiduOCRUtils.recognizeAccurateBasic(ShowOCRResultActivity.this, imgPath, new BaiduOCRUtils.OCRCallBack<GeneralResult>() {
                @Override
                public void succeed(GeneralResult data) {
                    String str = "";
                    for (WordSimple word : data.getWordList()) {
                        str = str + word.getWords() + "\n";
                    }
                    final String test = str;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(test);
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void failed(final OCRError error) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(error.toString());
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    };
}
