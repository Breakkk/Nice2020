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

import com.xilinzhang.ocr.utils.NetworkUtils;
import com.xilinzhang.ocr.utils.UserDataBaseUtils;
import com.xilinzhang.ocr.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignActivity extends AppCompatActivity {
    public static final String SIGN_IN_KEY = "is_sign_in";

    public static final int SIGN_IN_RESULT_SUCCESS = 10001;
    public static final int SIGN_IN_REQUEST = 1000;
    public static final int SIGN_UP_RESULT_SUCCESS = 20001;
    public static final int SIGN_UP_REQUEST = 2000;

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
                    final String user = signInUser.getText().toString();
                    final String pwd = signInPwd.getText().toString();
                    if (checkLegal(user, pwd)) {
                        final Map<String, Object> map = new HashMap<>();
                        map.put(UserDataBaseUtils.DATABASE_KEY_USER, user);
                        map.put(UserDataBaseUtils.DATABASE_KEY_PWD, pwd);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                if (UserDataBaseUtils.signIn(map)) {
                                    //登陆成功
                                    MyApplication.isSignIned = true;
                                    MyApplication.userName = user;
                                    setResult(SIGN_IN_RESULT_SUCCESS);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            NetworkUtils.getExp();
                                        }
                                    }).start();
                                    finish();
                                } else {
                                    //登陆失败
                                }
                            }
                        }).start();
                    } else {

                    }
                } else {
                    String user = signUpUser.getText().toString();
                    String pwdOnce = signUpPwdOnce.getText().toString();
                    String pwdTwice = signUpPwdTwice.getText().toString();
                    if (checkLegal(user, pwdOnce, pwdTwice)) {
                        final Map<String, Object> map = new HashMap<>();
                        map.put(UserDataBaseUtils.DATABASE_KEY_USER, user);
                        map.put(UserDataBaseUtils.DATABASE_KEY_PWD, pwdOnce);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                if (UserDataBaseUtils.signUp(map)) {
                                    //注册成功
                                    setResult(SIGN_UP_RESULT_SUCCESS);
                                    finish();
                                } else {
                                    //注册失败
                                }
                            }
                        }).start();
                    } else {
                    }
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
