package com.fryarludwig.dulynoted;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.res.TypedArrayUtils;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;

public class NoteCard implements Parcelable {
    public String mTitle;
    public ArrayList<Record> mRecordsList;
    public transient boolean mIsCollapsed = true;

    protected NoteCard(Parcel in) {
        mTitle = in.readString();
        mRecordsList = new ArrayList<>();
        in.readTypedList(mRecordsList, Record.CREATOR);
    }

    public static String GetNoteCardJson(NoteCard noteCard){
        return "";
    }

    public static final Creator<NoteCard> CREATOR = new Creator<NoteCard>() {
        @Override
        public NoteCard createFromParcel(Parcel in) {
            return new NoteCard(in);
        }

        @Override
        public NoteCard[] newArray(int size) {
            return new NoteCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeTypedList(mRecordsList);
    }

    public NoteCard() {
        mTitle = "Empty title";
        mRecordsList = new ArrayList<>();
    }

    public NoteCard(NoteCard noteCard) {
        mTitle = noteCard.mTitle;
        mRecordsList = new ArrayList<>(noteCard.mRecordsList.size());
        for (int i = 0; i < noteCard.mRecordsList.size(); i++) {
            mRecordsList.add(new Record(noteCard.mRecordsList.get(i)));
        }
    }

    public NoteCard(String title, ArrayList<String> array) {
        mTitle = title;
        mRecordsList = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++) {
            mRecordsList.add(new Record(array.get(i)));
        }
    }

    public void updateNoteCardValues(NoteCard noteCard) {
        mTitle = noteCard.mTitle;
        for (int i = 0; i < noteCard.mRecordsList.size(); i++) {
            if (this.mRecordsList.size() > i) {
                this.mRecordsList.get(i).mRecordName = noteCard.mRecordsList.get(i).mRecordName;
            } else {
                this.mRecordsList.add(noteCard.mRecordsList.get(i));
            }
        }
        while (noteCard.mRecordsList.size() != this.mRecordsList.size()) {
            this.mRecordsList.remove(this.mRecordsList.size() - 1);
        }
    }

    public Record getLatestRecord() {
        long bestValue = -1;
        int bestIndex = 0;
        for (int i = 0; i < getNumberOfOptions(); i++) {
            if (mRecordsList.get(i).getNewestEntry() > bestValue) {
                bestValue = mRecordsList.get(i).getNewestEntry();
                bestIndex = i;
            }
        }

        return mRecordsList.get(bestIndex);
    }

    public void logPressEvent(int index) {
        if (mRecordsList.size() > index) {
            mRecordsList.get(index).logEvent();
        }
    }

    public String getRecordName(int index) {
        if (mRecordsList.size() > index) {
            return mRecordsList.get(index).mRecordName;
        }
        return null;
    }

    public ArrayList<Long> getRecordTimestampsByIndex(int index) {
        if (mRecordsList.size() > index) {
            return mRecordsList.get(index).mTimestampsList;
        }
        return null;
    }

    public Record getRecordByIndex(int index) {
        if (mRecordsList.size() > index) {
            return mRecordsList.get(index);
        }
        return null;
    }

    public ArrayList<String> getRecordNames() {
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i < mRecordsList.size(); i++) {
            stringList.add(mRecordsList.get(i).mRecordName);
        }
        return stringList;
    }

    public void addNewRecord(String recordName) {
        mRecordsList.add(new Record(recordName));
    }

    public void setRecordName(int index, String newName) {
        if (mRecordsList.size() > index) {
            mRecordsList.get(index).mRecordName = newName;
        }
    }

    public int getNumberOfOptions() {
        return mRecordsList.size();
    }
}
