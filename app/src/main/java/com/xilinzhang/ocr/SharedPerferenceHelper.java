package com.xilinzhang.ocr;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

public class SharedPerferenceHelper {
    private static SharedPerferenceHelper helper;
    private SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor mEditor;

    private SharedPerferenceHelper(Context context, String baseKeyName) {
        mSharedPreferences = context.getSharedPreferences(baseKeyName, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public static SharedPerferenceHelper init(Context context, String baseKeyName) {
        if (helper == null) {
            helper = new SharedPerferenceHelper(context.getApplicationContext(), baseKeyName);
        }
        return helper;
    }

    public static SharedPerferenceHelper getHelper() {
        checkNotNull();
        return helper;
    }

    public static SharedPreferences.Editor getEditor() {
        checkNotNull();
        return mEditor;
    }

    public static SharedPerferenceHelper putValue(String key, String value) {
        getEditor().putString(key, value).commit();
        return getHelper();
    }

    public static SharedPerferenceHelper putValue(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
        return getHelper();
    }

    public static SharedPerferenceHelper putValue(String key, float value) {
        getEditor().putFloat(key, value).commit();
        return getHelper();
    }

    public static SharedPerferenceHelper putValue(String key, long value) {
        getEditor().putLong(key, value).commit();
        return getHelper();
    }

    public static SharedPerferenceHelper putValue(String key, int value) {
        getEditor().putInt(key, value).commit();
        return getHelper();
    }

    public static SharedPerferenceHelper putValue(String key, Set<String> value) {
        getEditor().putStringSet(key, value).commit();
        return getHelper();
    }

    public static <T> T getValue(String key, T defaultValue) {
        return getAll().get(key) == null ? defaultValue : (T) getAll().get(key);
    }

    public static boolean clearAll() {
        return getEditor().clear().commit();
    }

    public static boolean removeValue(String key) {
        return getEditor().remove(key).commit();
    }

    public static Set<String> getSet(String key, Set<String> defValues) {
        return getHelper().mSharedPreferences.getStringSet(key, defValues);
    }

    public static Map<String, ?> getAll() {
        return getHelper().mSharedPreferences.getAll();
    }

    private static void checkNotNull() {
        if (helper == null) {
            throw new ExceptionInInitializerError("Helper未初始化，请在Application中初始化");
        }
    }
}
