package com.qsc_mobile.widget.data.entry;

public enum EntryType {
    Course("course"),
    DayHeader("dayHeader");

    public final String name;

    EntryType(String name){
        this.name = name;
    }

    public static EntryType fromName(String name){
        for(EntryType type : EntryType.values()){
            if(type.name.equals(name)){
                return type;
            }
        }
        return DayHeader;
    }
}
