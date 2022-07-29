package com.edgar.among_us_maker.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {
    private String fileSave = "AmongUs_GameInfo";
    private Context context;
    private String key;

    public LocalData(Context context, String key) {
        this.context = context;
        this.key = key;
    }

    public void setPos(int pos) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileSave, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (key) {
            case "background":
                editor.putInt("background", pos);
                break;
            case "body":
                editor.putInt("body", pos);
                break;
            case "clothes":
                editor.putInt("clothes", pos);
                break;
            case "hat":
                editor.putInt("hat", pos);
                break;
            case "pet":
                editor.putInt("pet", pos);
                break;
            case "tag":
                editor.putInt("tag", pos);
                break;
        }
        editor.apply();
    }

    public int getPos() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileSave, Context.MODE_PRIVATE);
        switch (key) {
            case "background":
                return sharedPreferences.getInt("background", -1);
            case "body":
                return sharedPreferences.getInt("body", -1);
            case "clothes":
                return sharedPreferences.getInt("clothes", -1);
            case "hat":
                return sharedPreferences.getInt("hat", -1);
            case "pet":
                return sharedPreferences.getInt("pet", -1);
            case "tag":
                return sharedPreferences.getInt("tag", -1);
            default:
                return -1;
        }
    }
}
