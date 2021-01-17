package com.client.thegrocers.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsUtills {
    public static boolean saveFirstTimeUser(String onBoardIsOrNot, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString(Common.ONBOARD_SAVE,onBoardIsOrNot);
        prefsEditor.apply();
        return true;
    }
    public static String getUserStats(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Common.ONBOARD_SAVE,null);
    }

    public static boolean LogInStatus(String IsOrNot, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString(Common.LOGINSTATUS,IsOrNot);
        prefsEditor.apply();
        return true;
    }
    public static String getLoginStatus(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(Common.LOGINSTATUS,null);
    }
}
