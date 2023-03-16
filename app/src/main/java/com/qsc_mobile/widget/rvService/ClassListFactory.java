package com.qsc_mobile.widget.rvService;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import androidx.annotation.ColorInt;

import com.qsc_mobile.widget.R;
import com.qsc_mobile.widget.data.DataProvider;
import com.qsc_mobile.widget.data.entry.DataEntry;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ClassListFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = ClassListFactory.class.getSimpleName();

    private final int widgetId;
    private final Context context;
    private List<DataEntry> widgetEntries = new ArrayList<>();

    public static final String[] COLORS = {
            "#3342ACEF", "#3343C4D7", "#33F3A735", "#33F1805F", "#33F879C5"
    };


    public ClassListFactory(int widgetId, Context context) {
        this.widgetId = widgetId;
        this.context = context;
    }



    // ---------------------------------------------------
    // RemoteViewsFactory methods

    @Override
    public void onCreate() {}
    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        if (widgetEntries.isEmpty()) {
            reloadData();
        }
        return widgetEntries.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position < widgetEntries.size()) {
            DataEntry entry = widgetEntries.get(position);

            switch (entry.category){
                case Course:
                    RemoteViews course = new RemoteViews(context.getPackageName(), R.layout.widget_class_list_item);
                    configureBackground(course, COLORS[position % COLORS.length]);
                    configureItemText( course, entry, COLORS[position % COLORS.length]);
                    Log.d(TAG, "getViewAt: " + entry.category.toString() + " " + entry.name + " " + entry.start.toString("HH:mm") + " " + entry.end.toString("HH:mm") );
                    return course;
                case DayHeader:
                    RemoteViews dayHeader = new RemoteViews(context.getPackageName(), R.layout.widget_class_list_dayheader);
                    configureDayHeader(dayHeader, entry.start);
                    return dayHeader;
                default:
                    Log.d(TAG, "getViewAt: " + entry.category.toString());
                    throw new RuntimeException("Unsupported category: " + entry.category.name);
            }

        }
        return null;
    }

    @Override
    public void onDataSetChanged(){
        Log.d(TAG, "onDataSetChanged: ");
        reloadData();
        Log.d(TAG, "onDataSetChanged: " + widgetEntries.size());
    }

    @Override
    public int getViewTypeCount() {return 2;}

    @Override
    public long getItemId(int position) {
        if (position < widgetEntries.size()) {
            return position + 1;
        }
        return 0;
    }

    // using default loading view
    @Override
    public RemoteViews getLoadingView() {return null;}
    // do not reuse views
    @Override
    public boolean hasStableIds() {return false;}




    // -------------------------------------------
    // Data manipulation

    private void reloadData() {
        widgetEntries = DataProvider.getDataWithDayHeader(null,null);
    }




    // -------------------------------------------
    // View configuration

    private void configureBackground(RemoteViews rv, String colorStr){

        @ColorInt int color = Color.parseColor(colorStr);

        // card background & right edge
        // using light color
        rv.setInt(R.id.item_info_bar, "setBackgroundColor", color);

        rv.setImageViewResource(R.id.item_right_edge, R.drawable.right_rounded_card);
        rv.setInt(R.id.item_right_edge,"setColorFilter",getColorFromHSV(getHSVColor(color)));
        rv.setInt(R.id.item_right_edge,"setImageAlpha", getAlpha(color));

        // left edge
        // using dark color
        rv.setImageViewResource(R.id.item_left_edge, R.drawable.left_rounded_card);
        rv.setInt(R.id.item_left_edge,"setColorFilter",getColorFromHSV(getHSVColor(color)));
    }

    private void configureItemText(RemoteViews rv, DataEntry dataEntry, String colorStr){
        // format time
        String startTime = dataEntry.start.toString("HH:mm");
        String endTime = dataEntry.end.toString("HH:mm");
        String timeStr = startTime + " - " + endTime;

        // set text & textStyle
        @ColorInt int color = getColorWithOutAlpha(Color.parseColor(colorStr));
        rv.setTextViewText(R.id.item_name, dataEntry.name);
        rv.setTextColor(R.id.item_name,color);
        rv.setTextViewText(R.id.item_time, timeStr);
        rv.setTextColor(R.id.item_time,color);
        rv.setTextViewText(R.id.item_place, dataEntry.place);
        rv.setTextColor(R.id.item_place,color);
    }

    private void configureDayHeader(RemoteViews rv, DateTime day){
        String dayStr = day.toString("M 月 d 日 | EEEE");
        rv.setTextViewText(R.id.day_header, dayStr);
        rv.setTextColor(R.id.day_header, Color.BLACK);
    }



    //---------------------------------------------------
    // Color manipulation

    private static int getAlpha(int color) {
        return Color.alpha(color);
    }
    private static float[] getHSVColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return hsv;
    }
    private static int getColorFromHSV(float[] hsv) {
        return Color.HSVToColor(hsv);
    }
    private static int getColorWithOutAlpha(int color) {
        return Color.argb(255, Color.red(color), Color.green(color), Color.blue(color));
    }


}

