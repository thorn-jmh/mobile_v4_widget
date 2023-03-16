package com.qsc_mobile.widget.data.entry;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;


public class DataEntry implements Comparable<DataEntry>{


    public String name;
    public String place;
    public DateTime start;
    public DateTime end;
    public EntryType category;

    protected DataEntry(String name, String place, DateTime start, DateTime end, EntryType category) {
        this.name = name;
        this.place = place;
        this.start = start;
        this.end = end;
        this.category = category;
    }


    public static DataEntry fromJson(JSONObject json) throws JSONException {
        String name = json.getString("name");
        String place = json.getString("place");
        EntryType category = EntryType.fromName(json.getString("category"));

        // parse time
        DateTime start = DateTime.now();
        DateTime end = DateTime.now();
        try {
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ").withZone(DateTimeZone.forOffsetHours(8));
            start = fmt.parseDateTime(json.getString("start"));
            end = fmt.parseDateTime(json.getString("end"));
        } catch (UnsupportedOperationException | IllegalArgumentException e){
            Log.d("fromJson","error parsing time: " + e.getMessage());
            e.printStackTrace();
        }

        return new DataEntry(name, place, start, end,category);
    }

    public static DataEntry getDayHeader(DateTime date) {
        DateTime day = date.withTimeAtStartOfDay();
        return new DataEntry("", "", day,day, EntryType.DayHeader);
    }

    @Override
    public int compareTo(DataEntry dataEntry) {
        return start.compareTo(dataEntry.start);
    }
}
