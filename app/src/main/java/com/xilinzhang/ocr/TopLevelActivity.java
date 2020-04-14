package com.xilinzhang.ocr;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;

import com.xilinzhang.ocr.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopLevelActivity extends AppCompatActivity {
    private LinearLayout topLevelList;
    private RelativeLayout root;
    private List<String> data = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_top_level);
        initView();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    private void initView() {
        root = findViewById(R.id.root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        topLevelList = findViewById(R.id.container);
        getData();
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String str = NetworkUtils.sendPost(NetworkUtils.hostAddr + "getTopLevel", new HashMap<String, Object>());
                    JSONObject jsonObject = new JSONObject(str);
                    JSONArray jsonArray = jsonObject.getJSONArray("res");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject item = jsonArray.getJSONObject(i);
                        data.add(String.format("%s      %s级", item.getString("username").trim(), item.getString("level").trim().replace("null", "0")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                });
            }
        }).start();
    }

    private void refreshData() {
        for (int i = 0; i < data.size(); i++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            AppCompatTextView actv = new AppCompatTextView(this);
            actv.setText(data.get(i));
            if (i == 0) {
                AppCompatImageView aciv = new AppCompatImageView(this);
                aciv.setImageResource(R.drawable.first_place);
                ll.addView(aciv);
                LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) aciv.getLayoutParams();
                lllp.width = 120;
                lllp.height = 120;
                aciv.setLayoutParams(lllp);
            } else if (i == 1) {
                AppCompatImageView aciv = new AppCompatImageView(this);
                aciv.setImageResource(R.drawable.second_place);
                ll.addView(aciv);
                LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) aciv.getLayoutParams();
                lllp.width = 120;
                lllp.height = 120;
                aciv.setLayoutParams(lllp);
            } else if (i == 2) {
                AppCompatImageView aciv = new AppCompatImageView(this);
                aciv.setImageResource(R.drawable.third_place);
                ll.addView(aciv);
                LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) aciv.getLayoutParams();
                lllp.width = 120;
                lllp.height = 120;
                aciv.setLayoutParams(lllp);
            } else {
                Space space = new Space(this);
                ll.addView(space);
                LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) space.getLayoutParams();
                lllp.width = 120;
                lllp.height = 120;
                space.setLayoutParams(lllp);
            }
            ll.addView(actv);
            topLevelList.addView(ll);
            LinearLayout.LayoutParams lllpText = (LinearLayout.LayoutParams) actv.getLayoutParams();
            lllpText.weight = 1;
            lllpText.gravity = Gravity.CENTER;
            actv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            actv.setLayoutParams(lllpText);
        }
    }
}
