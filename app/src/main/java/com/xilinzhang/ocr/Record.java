package com.xilinzhang.ocr;

import android.net.Uri;

import java.io.Serializable;

public class Record implements Serializable {
    private static final long serialVersionUID = -2095916884810199532L;
    private String imgUri;

    private String shitiShow, shitiAnswer, shitiAnalysis;

    public Record(String uri, String show, String answer, String analysis) {
        imgUri = uri;
        shitiShow = show;
        shitiAnswer = answer;
        shitiAnalysis = analysis;
    }

    public String getImgUri() {
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

    @Override
    public String toString() {
        return "Record{" +
                "imgUri=" + imgUri +
                ", shitiShow='" + shitiShow + '\'' +
                ", shitiAnswer='" + shitiAnswer + '\'' +
                ", shitiAnalysis='" + shitiAnalysis + '\'' +
                '}';
    }
}
