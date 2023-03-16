package com.qsc_mobile.widget.rvService;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.util.Log;

public class RemoteViewsService extends android.widget.RemoteViewsService {
    private static final String TAG = RemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory, intent:" + intent);
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

        RemoteViewsFactory factory = new ClassListFactory(widgetId, this);
        return factory;
    }
}

