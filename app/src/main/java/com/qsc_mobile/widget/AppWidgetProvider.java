package com.qsc_mobile.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.RemoteViews;

import com.qsc_mobile.widget.data.DataProvider;
import com.qsc_mobile.widget.visualizer.ClassListVisualizer;
import com.qsc_mobile.widget.visualizer.IWidgetVisualizer;

/**
 * Implementation of App Widget functionality.
 */
public abstract class AppWidgetProvider extends android.appwidget.AppWidgetProvider {
    private static final String TAG = AppWidgetProvider.class.getSimpleName();

    protected abstract IWidgetVisualizer getVisualizer();

    @Override
    public void onReceive(Context context, Intent intent) {
        int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        if (widgetIds == null || widgetIds.length == 0) {
            widgetIds = getWidgetIds(context);
        }
        String action = intent.getAction();
        Log.d(TAG, "onReceive, intent:" + intent + ", widgetIds:" + widgetIds + ", action:" + action);

        if ( !getVisualizer().handleAction(context, intent, widgetIds) ) {

            if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {
                getVisualizer().updateWidget(context, widgetIds);
            } else {
                super.onReceive(context, intent);
            }
        }
    }


    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled, context:" + context);
        DataProvider.ensureDataLoaded(context);
        super.onEnabled(context);
    }


    public static int[] getWidgetIds(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        return appWidgetManager == null
                ? new int[]{}
                : appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidgetProvider.class));
    }
}