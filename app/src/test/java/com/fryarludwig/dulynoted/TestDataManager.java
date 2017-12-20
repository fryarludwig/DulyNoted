package com.fryarludwig.dulynoted;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestDataManager {

    @Test
    public void testSharedPreferences() throws Exception {
        ArrayList<NoteCard> noteCardList = new ArrayList<>();
        noteCardList.add(new NoteCard("Emma Wilson", new ArrayList<String>(Arrays.asList("A", "B", "C"))));
        noteCardList.add(new NoteCard("Lavery Maiss", new ArrayList<String>(Arrays.asList("Parmesan", "Ricotta", "Fontina", "Cheddar"))));
        noteCardList.add(new NoteCard("Lillie Watts", new ArrayList<String>(Arrays.asList("Peanut Butter", "Butter", "Peanut"))));


        Gson myGson = new Gson();

        for (NoteCard noteCard : noteCardList) {
            noteCard.mRecordsList.get(0).logEvent();
            noteCard.mRecordsList.get(0).logEvent();
            noteCard.mRecordsList.get(2).logEvent();
            noteCard.mRecordsList.get(1).logEvent();
            System.out.println(noteCard.toString());
//            String jsonString = new String(DataManager.serializeNoteCard(noteCard));
        }

        String noteCardListAsJson = DataManager.serializeNoteCard(noteCardList);
        System.out.println(noteCardListAsJson);
        System.out.println();

        ArrayList<NoteCard> deserialized = DataManager.deserializeNoteCard(noteCardListAsJson);
        System.out.println(deserialized);
        System.out.println();
    }
}
