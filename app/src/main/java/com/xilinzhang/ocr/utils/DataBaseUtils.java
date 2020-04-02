package com.xilinzhang.ocr.utils;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.util.Map;

public class DataBaseUtils {
    public static final String SERVER_POST_URL_SEARCH = "test";

    public static final String SUCCESS_FLAG = "success_flag";
    public static final String FILE_PATH = "FilePath";
    public static final String QUESTION_ID = "QuestionID";
    public static final String SHITI_SHOW = "ShiTiShow";
    public static final String SHITI_ANSWER = "ShiTiAnswer";
    public static final String SHITI_ANALYSIS = "ShiTiAnalysis";
    public static final String UNRESOVLE_URL = "_questionImageIP_questionImagePath_questionImageID";

    @Nullable
    public static JSONObject getDataUseKeyword(Map<String, Object> map) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(NetworkUtils.sendPost(NetworkUtils.hostAddr + SERVER_POST_URL_SEARCH, map));
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return jsonObject;
    }

    @Nullable
    public static String getFormatHTMLStr(JSONObject json) {
        try {
            final String tmpAns = json.getString("ShiTiShow")
                    + "\n"
                    + json.getString("ShiTiAnswer")
                    + "\n"
                    + json.getString("ShiTiAnalysis");
            String replace = NetworkUtils.hostAddr + "static/" + json.getString(FILE_PATH) + json.getString(QUESTION_ID);
            return tmpAns.replaceAll(UNRESOVLE_URL, replace);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return null;
    }

    public static String replaceImgURL(String origin, String filePath, String questionID) {
        String replace = NetworkUtils.hostAddr + "static/" + filePath + questionID;
        return origin.replaceAll(UNRESOVLE_URL, replace);
    }

    public static void intentProcesser(Intent intent, JSONObject json) throws Exception {
        String filePath = json.getString(FILE_PATH);
        String questionID = json.getString(QUESTION_ID).trim();
        intent.putExtra(FILE_PATH, json.getString(FILE_PATH));
        intent.putExtra(QUESTION_ID, json.getString(QUESTION_ID));
        intent.putExtra(SHITI_SHOW, replaceImgURL(json.getString(SHITI_SHOW), filePath, questionID));
        intent.putExtra(SHITI_ANSWER, replaceImgURL(json.getString(SHITI_ANSWER), filePath, questionID));
        intent.putExtra(SHITI_ANALYSIS, replaceImgURL(json.getString(SHITI_ANALYSIS), filePath, questionID));
    }
}
