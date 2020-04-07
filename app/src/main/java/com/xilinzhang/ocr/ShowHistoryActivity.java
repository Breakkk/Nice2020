package com.xilinzhang.ocr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.xilinzhang.ocr.utils.DataBaseUtils;
import com.xilinzhang.ocr.utils.Utils;

import java.util.List;

public class ShowHistoryActivity extends AppCompatActivity {
    LinearLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initView();
        parseData();
    }

    private void initView() {
        root = findViewById(R.id.container);
    }

    private void parseData() {
        List<Record> recordList = (List<Record>) Utils.getLocalHistory();
        if(recordList != null) {
            for(final Record record : recordList) {
                AppCompatImageView imgView = new AppCompatImageView(this);
                imgView.setBackgroundResource(R.drawable.view_back);
                imgView.setImageURI(Uri.parse(record.getImgUri()));
                root.addView(imgView);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgView.getLayoutParams();
                lp.setMargins(30, 30, 30, 30);
                imgView.setLayoutParams(lp);

                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShowHistoryActivity.this, ShowDataBaseResultActivity.class);
                        intent.putExtra("imgUri", Uri.parse(record.getImgUri()));
                        intent.putExtra(DataBaseUtils.SUCCESS_FLAG, true);
                        intent.putExtra(DataBaseUtils.SHITI_SHOW, record.getShitiShow());
                        intent.putExtra(DataBaseUtils.SHITI_ANSWER, record.getShitiAnswer());
                        intent.putExtra(DataBaseUtils.SHITI_ANALYSIS, record.getShitiAnalysis());
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
