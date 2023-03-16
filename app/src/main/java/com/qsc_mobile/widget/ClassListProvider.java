package com.qsc_mobile.widget;

import com.qsc_mobile.widget.visualizer.ClassListVisualizer;
import com.qsc_mobile.widget.visualizer.IWidgetVisualizer;

public class ClassListProvider extends AppWidgetProvider {
    private static ClassListVisualizer visualizer;
    @Override
    protected IWidgetVisualizer getVisualizer() {
        if (visualizer == null) {
            synchronized (ClassListProvider.class) {
                if (visualizer == null) {
                    visualizer = new ClassListVisualizer();
                }
            }
        }
        return visualizer;
    }
}
