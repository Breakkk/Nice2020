package com.xilinzhang.ocr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SignActivity extends AppCompatActivity {
    public static final String SIGN_IN_KEY = "is_sign_in";

    AppCompatTextView title;
    LinearLayout signInLayout, signUpLayout;
    AppCompatEditText signInUser, signInPwd;
    AppCompatEditText signUpUser, signUpPwdOnce, signUpPwdTwice;
    AppCompatButton button;

    boolean isSignIn = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        isSignIn = getIntent().getExtras().getBoolean(SIGN_IN_KEY);
        initView();
    }

    private void initView() {
        title = findViewById(R.id.title);
        signInLayout = findViewById(R.id.sign_in);
        signUpLayout = findViewById(R.id.sign_up);
        signInUser = findViewById(R.id.user_sign_in);
        signInPwd = findViewById(R.id.pwd_sign_in);
        signUpUser = findViewById(R.id.user_sign_up);
        signUpPwdOnce = findViewById(R.id.pwd_once);
        signUpPwdTwice = findViewById(R.id.pwd_twice);
        button = findViewById(R.id.button);

        title.setText(isSignIn ? "登录" : "注册");
        signInLayout.setVisibility(isSignIn ? View.VISIBLE : View.GONE);
        signUpLayout.setVisibility(!isSignIn ? View.VISIBLE : View.GONE);
        button.setText(isSignIn ? "登录" : "注册");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSignIn) {
                    String user = signInUser.getText().toString();
                    String pwd = signInPwd.getText().toString();
                    checkLegal(user, pwd);
                    Toast.makeText(SignActivity.this, user + " " + pwd, Toast.LENGTH_LONG).show();
                } else {
                    String user = signUpUser.getText().toString();
                    String pwdOnce = signUpPwdOnce.getText().toString();
                    String pwdTwice = signUpPwdTwice.getText().toString();
                    checkLegal(user, pwdOnce, pwdTwice);
                    Toast.makeText(SignActivity.this, user + " " + pwdOnce + " " + pwdTwice, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkLegal(String user, String pwd) {

        return false;
    }

    private boolean checkLegal(String user, String pwdOnce, String pwdTwice) {

        return false;
    }
}