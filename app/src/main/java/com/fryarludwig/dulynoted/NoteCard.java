package com.fryarludwig.dulynoted;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.res.TypedArrayUtils;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class NoteCard implements Parcelable {
    public String mTitle;
    public ArrayAdapter<String> mNoteCardAdapter;
    public ArrayList<Record> mRecordsList;

    protected NoteCard(Parcel in) {
        mTitle = in.readString();
        mRecordsList = new ArrayList<>();
        in.readTypedList(mRecordsList, Record.CREATOR);
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

    public NoteCard()
    {
        mTitle = "Empty title";
        mRecordsList = new ArrayList<>();
    }

    public NoteCard(NoteCard noteCard)
    {
        mTitle = noteCard.mTitle;
        mRecordsList = new ArrayList<>(noteCard.mRecordsList.size());
        for (int i = 0; i < noteCard.mRecordsList.size(); i++)
        {
            mRecordsList.add(new Record(noteCard.mRecordsList.get(i)));
        }
    }

    public NoteCard(String title, ArrayList<String> array)
    {
        mTitle = title;
        mRecordsList = new ArrayList<>(array.size());
        for (int i = 0; i < array.size(); i++)
        {
            mRecordsList.add(new Record(array.get(i)));
        }
    }

    public void logPressEvent(int index){
        if (mRecordsList.size() > index)
        {
            mRecordsList.get(index).logEvent();
        }
    }

    public String getRecordName(int index){
        if (mRecordsList.size() > index)
        {
            return mRecordsList.get(index).mRecordName;
        }
        return null;
    }

    public ArrayList<Long> getRecordsByIndex(int index){
        if (mRecordsList.size() > index)
        {
            return mRecordsList.get(index).mTimestampsList;
        }
        return null;
    }

    public ArrayList<String> getRecordNames() {
        ArrayList<String> stringList = new ArrayList<>();
        for (int i = 0; i < mRecordsList.size(); i++)
        {
            stringList.add(mRecordsList.get(i).mRecordName);
        }
        return stringList;
    }

    public void addNewRecord(String recordName){
        mRecordsList.add(new Record(recordName));
    }

    public void setRecordName(int index, String newName)
    {
        if (mRecordsList.size() > index)
        {
            mRecordsList.get(index).mRecordName = newName;
        }
    }

    public int getNumberOfOptions()
    {
        return mRecordsList.size();
    }
}
