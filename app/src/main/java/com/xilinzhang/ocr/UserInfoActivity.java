package com.xilinzhang.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    AppCompatTextView username;
    AppCompatTextView exp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
    }

    private void initView() {
        findViewById(R.id.my_history).setOnClickListener(this);
        findViewById(R.id.my_answer).setOnClickListener(this);
        findViewById(R.id.my_question).setOnClickListener(this);
        username = findViewById(R.id.username);
        exp = findViewById(R.id.exp);

        username.setText(MyApplication.userName);
        exp.setText("0/9999999");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_history:
                Intent intent1 = new Intent(UserInfoActivity.this, ShowHistoryActivity.class);
                startActivity(intent1);
                break;
            case R.id.my_answer:
                Intent intent2 = new Intent(UserInfoActivity.this, MyAnswerActivity.class);
                startActivity(intent2);
                break;
            case R.id.my_question:
                Intent intent3 = new Intent(UserInfoActivity.this, MyQuestionActicity.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }
}
