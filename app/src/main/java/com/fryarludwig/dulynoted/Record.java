package com.fryarludwig.dulynoted;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Record implements Parcelable, Comparable<Record> {
    public String mRecordName;
    public ArrayList<Long> mTimestampsList;
    public transient SimpleDateFormat mDateFormat;
    private static transient String mDateFormatString = "hh:mma, MM/dd/yy";

    public Record()
    {
        mRecordName = "";
        mTimestampsList = new ArrayList<>();
        mDateFormat = new SimpleDateFormat(Record.mDateFormatString);
    }

    public Record(String recordName)
    {
        mRecordName = recordName;
        mTimestampsList = new ArrayList<>();
        mDateFormat = new SimpleDateFormat(Record.mDateFormatString);
    }

    public Record(Record record)
    {
        mRecordName = record.mRecordName;
        mTimestampsList = new ArrayList<>(record.mTimestampsList);
        mDateFormat = new SimpleDateFormat(Record.mDateFormatString);
    }

    protected Record(Parcel in) {
        mRecordName = in.readString();
        mTimestampsList = new ArrayList<>();
        in.readList(mTimestampsList, Long.class.getClassLoader());
        mDateFormat = new SimpleDateFormat(Record.mDateFormatString);
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

    public long getNewestEntry(){
        if (this.mTimestampsList.size() > 0)
        {
            return this.mTimestampsList.get(this.mTimestampsList.size() - 1);
        }
        else
        {
            return -1;
        }
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

    @Override
    public int compareTo(@NonNull Record otherRecord) {
        long newestOther = otherRecord.getNewestEntry();
        long newestThis = this.getNewestEntry();

        if (newestThis == newestOther)
        {
            return 0;
        }
        else if (newestThis > newestOther)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public String toString(){
        String resultText = String.format("No records for %s", mRecordName);
        long newestEntry = getNewestEntry();
        if (newestEntry != -1){
            resultText = String.format("Latest: %s at %s", mRecordName, mDateFormat.format(new Date(newestEntry)));
        }
        return resultText;
    }
}
