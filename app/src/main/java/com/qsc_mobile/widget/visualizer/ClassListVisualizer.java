package com.qsc_mobile.widget.visualizer;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;



import com.qsc_mobile.widget.R;
import com.qsc_mobile.widget.data.DataProvider;
import com.qsc_mobile.widget.rvService.RemoteViewsService;

import org.joda.time.DateTime;

public class ClassListVisualizer implements IWidgetVisualizer{

    private static final int bgColor = Color.parseColor("#FFFFFFFF");
    private static final int fontColor = Color.parseColor("#FF000000");



    @Override
    public void updateWidget(Context context, int[] widgetIds) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (appWidgetManager == null){
            return;
        }

        for (int widgetId : widgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_class_list);


            configureWidgetMainColor(context, rv);
            configureWidgetHeaderDate(context, rv);
            configureWidgetCollectionView(context,rv,widgetId);

            appWidgetManager.updateAppWidget(widgetId, rv);
            appWidgetManager.notifyAppWidgetViewDataChanged(new int[]{widgetId}, R.id.class_list);
        }
    }

    @Override
    public boolean handleAction(Context context, Intent intent,int[] widgetIds) {
        return false;
    }


    private void configureWidgetCollectionView(Context context, RemoteViews rv, int widgetId){
        // empty view
        rv.setTextColor(R.id.empty, fontColor);
        rv.setInt(R.id.empty, "setBackgroundColor", bgColor);
        rv.setEmptyView(R.id.class_list, R.id.empty);

        // expired view
        rv.setViewVisibility(R.id.class_list,View.VISIBLE);
        rv.setViewVisibility(R.id.expired,View.GONE);
        if (!DataProvider.checkUpdateTime()){
            rv.setTextColor(R.id.expired, fontColor);
            rv.setInt(R.id.expired, "setBackgroundColor", bgColor);
            rv.setViewVisibility(R.id.class_list,View.GONE);
            rv.setViewVisibility(R.id.expired,View.VISIBLE);
        }

        // set remote adapter
        Intent intent = new Intent(context, RemoteViewsService.class);
        intent
                .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        rv.setRemoteAdapter(R.id.class_list, intent);
    }

    private void configureWidgetMainColor(Context context, RemoteViews rv) {
        Log.d("ClassListVisualizer", "configureWidgetMainColor: ");
        rv.setInt(R.id.class_list_header, "setBackgroundColor", bgColor);
        rv.setInt(R.id.class_list,"setBackgroundColor", bgColor);

    }

    private void configureWidgetHeaderDate(Context context,RemoteViews rv){
        DateTime date = DateTime.now();
        String dateStr = date.toString("M 月 d 日");
        String weekdayStr = date.toString("EEEE");

        Log.d("ClassListVisualizer", "configureWidgetHeaderDate: " + dateStr + " " + weekdayStr);

        rv.setTextViewText(R.id.header_date, dateStr);
        rv.setTextColor(R.id.header_date, fontColor);
        rv.setTextViewText(R.id.header_weekday,weekdayStr);
        rv.setTextColor(R.id.header_weekday, fontColor);
    }

}
