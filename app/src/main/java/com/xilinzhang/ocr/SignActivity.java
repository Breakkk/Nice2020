package com.xilinzhang.ocr;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xilinzhang.ocr.utils.UserDataBaseUtils;

import java.util.HashMap;
import java.util.Map;

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
                if (isSignIn) {
                    String user = signInUser.getText().toString();
                    String pwd = signInPwd.getText().toString();
                    if (checkLegal(user, pwd)) {
                        final Map<String, Object> map = new HashMap<>();
                        map.put(UserDataBaseUtils.DATABASE_KEY_USER, user);
                        map.put(UserDataBaseUtils.DATABASE_KEY_PWD, pwd);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                if (UserDataBaseUtils.signIn(map)) {
                                    Toast.makeText(SignActivity.this, "success", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignActivity.this, "failed", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).start();
                    } else {

                    }
                    Toast.makeText(SignActivity.this, user + " " + pwd, Toast.LENGTH_LONG).show();
                } else {
                    String user = signUpUser.getText().toString();
                    String pwdOnce = signUpPwdOnce.getText().toString();
                    String pwdTwice = signUpPwdTwice.getText().toString();
                    if (checkLegal(user, pwdOnce, pwdTwice)) {

                    } else {

                    }
                    Toast.makeText(SignActivity.this, user + " " + pwdOnce + " " + pwdTwice, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkLegal(String user, String pwd) {

        return true;
    }

    private boolean checkLegal(String user, String pwdOnce, String pwdTwice) {

        return true;
    }
}
