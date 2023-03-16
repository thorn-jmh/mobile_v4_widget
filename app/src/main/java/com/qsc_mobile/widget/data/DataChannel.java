package com.qsc_mobile.widget.data;

import android.content.Context;
import android.util.Log;

import com.qsc_mobile.widget.data.entry.DataEntry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class DataChannel {

    static String json = "{\n" +
            "      \"2023-02-26\": [\n" +
            "        {\n" +
            "          \"duration\": \"allDay\",\n" +
            "          \"category\": \"course\",\n" +
            "          \"tags\": [],\n" +
            "          \"name\": \"test1\",\n" +
            "          \"time\": \"\",\n" +
            "          \"place\": \"qsc\",\n" +
            "          \"start\": \"2023-02-26T12:00:00Z\",\n" +
            "          \"end\": \"2023-02-26T13:00:00Z\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"duration\": \"allDay\",\n" +
            "          \"category\": \"course\",\n" +
            "          \"tags\": [],\n" +
            "          \"name\": \"test2\",\n" +
            "          \"time\": \"\",\n" +
            "          \"place\": \"217\",\n" +
            "          \"start\": \"2023-02-26T14:00:00Z\",\n" +
            "          \"end\": \"2023-02-26T15:00:00Z\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"2023-02-27\": [\n" +
            "        {\n" +
            "            \"duration\": \"allDay\",\n" +
            "            \"category\": \"course\",\n" +
            "            \"tags\": [],\n" +
            "            \"name\": \"数字逻辑设计\",\n" +
            "            \"time\": \"\",\n" +
            "            \"place\": \"东四-409\",\n" +
            "            \"start\": \"2023-02-27T06:00:00Z\",\n" +
            "            \"end\": \"2023-02-26T08:00:00Z\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"duration\": \"allDay\",\n" +
            "          \"category\": \"course\",\n" +
            "          \"tags\": [],\n" +
            "          \"name\": \"面向对象程序设计\",\n" +
            "          \"time\": \"\",\n" +
            "          \"place\": \"紫金港机房\",\n" +
            "          \"start\": \"2023-02-27T00:00:00Z\",\n" +
            "          \"end\": \"2023-02-27T02:00:00Z\"\n" +
            "        }\n" +
            "      ]\n" +
            "}";


    // for test
    public static void getData(Context context){
        if(parseJsonData(json)){
            DataStore.saveData(context, json);
        } else {
            DataStore.restoreData(context);
        }
    }

    public static boolean parseJsonData(String jsonStr){
        try{
            Log.d("DataParser","parsing json data...");
            // clear old data
            DataProvider.clearData();

            // parse json
            JSONObject jsonData = new JSONObject(jsonStr);

            // iterate through json
            Iterator<String> iterator =  jsonData.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                DateTime date = DateTime.parse(key, DateTimeFormat.forPattern("yyyy-MM-dd"));

                // parse events
                JSONArray events = jsonData.getJSONArray(key);
                List<DataEntry> eventList =  parseEvents(events);

                // add to data provider
                DataProvider.addData(date, eventList);
            }
            return true;

        } catch (JSONException e) {
            Log.d("DataParser","JsonException: "+e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private static List<DataEntry> parseEvents(JSONArray events) throws JSONException {
        List<DataEntry> dataEntries = new ArrayList<>();
        for (int i = 0; i < events.length(); i++) {
            JSONObject event = events.getJSONObject(i);
            DataEntry dataEntry = DataEntry.fromJson(event);
            dataEntries.add(dataEntry);
        }
        return dataEntries;
    }
}
