package com.example.torchapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String USER_ID= "user_id";

    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }


    public static void setUserId(Context ctx, String userId)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public static String getUserId(Context ctx)
    {
        return getSharedPreferences(ctx).getString(USER_ID, "");
    }
}