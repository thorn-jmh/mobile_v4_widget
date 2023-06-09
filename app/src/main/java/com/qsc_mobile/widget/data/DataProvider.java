package com.qsc_mobile.widget.data;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.qsc_mobile.widget.data.entry.DataEntry;

import org.joda.time.DateTime;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;


public class DataProvider {
    private static volatile TreeSet<DateTime> dateList = new TreeSet<>();
    private static volatile HashMap<DateTime, List<DataEntry>> dataEntries = new HashMap<>();




    // multi-threaded safe data changer
    public static void addData(DateTime date, List<DataEntry> data) {
        synchronized (DataProvider.class) {
            dateList.add(date);
            dataEntries.put(date, data);
        }
    }
    public static void clearData() {
        synchronized (DataProvider.class) {
            dateList.clear();
            dataEntries.clear();
        }
    }

    // ------------------------------------------
    // data provider


    public static boolean checkUpdateTime(){
        return dateList.last().isAfter(DateTime.now().plusDays(3));
    }

    public static void ensureDataLoaded(Context context) {

        if (dateList.isEmpty()){
            synchronized (DataProvider.class) {
                if (dateList.isEmpty()) {
                    Log.d("DataProvider", "Data not loaded, loading...");
                    DataStore.restoreData(context);
                }
            }
        }
    }

    public static List<DataEntry> getAllData() {
        TreeSet<DataEntry> data = new TreeSet<>();
        Log.d("DataProvider", "getAllData: " + dateList.size());
        for (DateTime date : dateList) {
            Log.d("DataProvider", "getAllData: " + date.toString() + " " + dataEntries.get(date).size());
            data.addAll(Objects.requireNonNull(dataEntries.get(date)));
        }
        Log.d("DataProvider", "getAllData: " + data.size());

        return new ArrayList<>(data);
    }

    public static List<DataEntry> getDataWithDayHeader(DateTime from, DateTime to) {
        TreeSet<DataEntry> data = new TreeSet<>();
        DateTime startDate = from == null
                ? dateList.first().withTimeAtStartOfDay()
                : from.withTimeAtStartOfDay();
        DateTime endDate = to == null
                ? dateList.last().withTimeAtStartOfDay().plusDays(1)
                : to.withTimeAtStartOfDay().plusDays(1);

        for (DateTime date : dateList) {
            if (date.isAfter(endDate)) break;
            if (date.isBefore(startDate)) continue;

            List<DataEntry> entries = dataEntries.get(date);
            if (entries != null && !entries.isEmpty()) {
                data.add(DataEntry.getDayHeader(date));
                data.addAll(entries);
            }
        }
        return new ArrayList<>(data);

    }


}
