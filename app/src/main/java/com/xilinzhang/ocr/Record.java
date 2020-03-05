package com.xilinzhang.ocr;

import android.net.Uri;

public class Record {
    private Uri imgUri;
    private String shitiShow, shitiAnswer, shitiAnalysis;

    public Record(Uri uri, String show, String answer, String analysis) {
        imgUri = uri;
        shitiShow = show;
        shitiAnswer = answer;
        shitiAnalysis = analysis;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public String getShitiShow() {
        return shitiShow;
    }

    public String getShitiAnswer() {
        return shitiAnswer;
    }

    public String getShitiAnalysis() {
        return shitiAnalysis;
    }
}
