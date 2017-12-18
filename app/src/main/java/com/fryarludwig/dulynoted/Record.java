package com.fryarludwig.dulynoted;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Record implements Parcelable {
    public String mRecordName;
    public ArrayList<Long> mTimestampsList;

    public Record()
    {
        mRecordName = "";
        mTimestampsList = new ArrayList<>();
    }

    public Record(String recordName)
    {
        mRecordName = recordName;
        mTimestampsList = new ArrayList<>();
    }

    public Record(Record record)
    {
        mRecordName = record.mRecordName;
        mTimestampsList = new ArrayList<>(record.mTimestampsList);
    }

    protected Record(Parcel in) {
        mRecordName = in.readString();
        mTimestampsList = new ArrayList<>();
        in.readList(mTimestampsList, Long.class.getClassLoader());
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    public void logEvent()
    {
        mTimestampsList.add(System.currentTimeMillis());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mRecordName);
        dest.writeList(mTimestampsList);
    }
}
