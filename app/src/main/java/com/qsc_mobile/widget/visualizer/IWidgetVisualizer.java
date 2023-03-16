package com.qsc_mobile.widget.visualizer;



import android.content.Context;
import android.content.Intent;


public interface IWidgetVisualizer {
    void updateWidget(Context context, int[] widgetIds);
    boolean handleAction(Context context, Intent intent, int[] widgetIds);
}



