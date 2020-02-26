package com.xilinzhang.ocr;

import android.app.ProgressDialog;
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
import com.xilinzhang.ocr.utils.NetworkUtils;

import java.util.HashMap;
import java.util.Map;

public class ShowAnswerActivity extends AppCompatActivity {
    private static final String TAG = "ShowAnswerActivity";

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

        imageView =  findViewById(R.id.image);
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
                        Log.d("keyword", text + text.indexOf("(") + text.indexOf(")"));
                         String keyword = text.substring(text.indexOf("("), text.indexOf(")") + 1);
                        Log.d("keyword", keyword);
                        map.put("keyword", keyword);
                        map.put("test", "test");
                        final String ans = NetworkUtils.sendPost("http://192.168.3.14:5000/search", map);
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(ans);
                            }
                        });
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
            BaiduOCRUtils.recognizeAccurateBasic(ShowAnswerActivity.this, imgPath, new BaiduOCRUtils.OCRCallBack<GeneralResult>() {
                @Override
                public void succeed(GeneralResult data) {
                    String str = "";
                    for(WordSimple word : data.getWordList()) {
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
