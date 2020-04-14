package com.xilinzhang.ocr;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.xilinzhang.ocr.utils.LevelUtils;
import com.xilinzhang.ocr.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private AppCompatTextView signIn, signUp, my;
    private AppCompatImageView circle;

    private FrameLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.btn_camera);
        imageView.setOnClickListener(this);
        circle = findViewById(R.id.circle);
        root = findViewById(R.id.root_view);

        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        my = findViewById(R.id.my);

        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);
        my.setOnClickListener(this);
        findViewById(R.id.question_list).setOnClickListener(this);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
                circle,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.3f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.3f)
        );
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();

        initAccessToken();
    }

    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                final String token = accessToken.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "init error with lisense", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
                Utils.startCamera(MainActivity.this);
                break;
            case R.id.sign_in:
                Intent intent1 = new Intent(MainActivity.this, SignActivity.class);
                intent1.putExtra(SignActivity.SIGN_IN_KEY, true);
                startActivityForResult(intent1, SignActivity.SIGN_IN_REQUEST);
                break;
            case R.id.sign_up:
                Intent intent2 = new Intent(MainActivity.this, SignActivity.class);
                intent2.putExtra(SignActivity.SIGN_IN_KEY, false);
                startActivityForResult(intent2, SignActivity.SIGN_UP_REQUEST);
                break;
            case R.id.my:
                //enter my activity
                Intent intent3 = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent3);
                break;
            case R.id.question_list:
                Intent intent4 = new Intent(MainActivity.this, QuestionListActivity.class);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utils.PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, MyCameraActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "请开启摄像头权限", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SignActivity.SIGN_IN_REQUEST && resultCode == SignActivity.SIGN_IN_RESULT_SUCCESS) {
            //登陆成功
            Toast.makeText(this, "登陆成功", Toast.LENGTH_LONG).show();
            signIn.setVisibility(View.GONE);
            my.setVisibility(View.VISIBLE);
        }else if(requestCode == SignActivity.SIGN_UP_REQUEST && resultCode == SignActivity.SIGN_UP_RESULT_SUCCESS) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        }
    }
}
