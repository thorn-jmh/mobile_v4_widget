package com.qsc_mobile.widget.data;

import android.content.Context;
import android.util.Log;

public class DataStore {
    public static final String PACKAGE = "com.qsc_mobile.widget";
    public static final String PREF_WIDGET_DATA = "widgetData";



    public static void saveData(Context context,String data) {
        context.getSharedPreferences(PACKAGE, Context.MODE_PRIVATE)
                .edit()
                .putString(PREF_WIDGET_DATA, data)
                .apply();
    }

    public static boolean restoreData(Context context) {
        String data = context.getSharedPreferences(PACKAGE, Context.MODE_PRIVATE)
                .getString(PREF_WIDGET_DATA, null);

        Log.d("DataStore", "restoreData: " + data);
        if (data != null) {
            DataChannel.parseJsonData(data);
        }

        return data == null;
    }


}
