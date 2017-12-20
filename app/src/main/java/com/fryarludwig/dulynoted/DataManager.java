package com.fryarludwig.dulynoted;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * Created by fryar on 12/17/2017.
 */

public class DataManager {
    private static final String STRING_KEY = "NOTE_CARD_ARRAY_LIST";
    private static final int NUM_FRUIT = 6;

    public static void saveData(Context context, ArrayList<NoteCard> noteCardArrayList){
        String serializedData = serializeNoteCard(noteCardArrayList);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(DataManager.STRING_KEY, serializedData).apply();
    }

    public static ArrayList<NoteCard> loadData(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(DataManager.STRING_KEY, "[]");
        return deserializeNoteCard(json);
    }

    public static String serializeNoteCard(ArrayList<NoteCard> noteCardArrayList) {
        Gson gson = new Gson();
        String[] noteCardJsonList = new String[noteCardArrayList.size()];
        for (int i = 0; i < noteCardArrayList.size(); i++) {
            noteCardJsonList[i] = gson.toJson(noteCardArrayList.get(i));
        }

        return gson.toJson(noteCardJsonList);
    }

    public static ArrayList<NoteCard> deserializeNoteCard(String noteCardSerializedList) {
        Gson gson = new Gson();
        String[] serializedNoteCardList = gson.fromJson(noteCardSerializedList, String[].class);
        ArrayList<NoteCard> resultList = new ArrayList<>(serializedNoteCardList.length);
        for (int i = 0; i < serializedNoteCardList.length; i++) {
            resultList.add(gson.fromJson(serializedNoteCardList[i], NoteCard.class));
        }
        return resultList;
    }

    public static List<String> getSavedStrings(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString(DataManager.STRING_KEY, "[]");
        Gson gson = new Gson();
        ArrayList<String> saved_strings = gson.fromJson(json, ArrayList.class);
        return saved_strings;
    }

    public static String getSavedString(Context context, int index) {
        List<String> strings = getSavedStrings(context);
        if (index < strings.size()) {
            return strings.get(index);
        }
        return null;
    }

    public static void addString(Context context, String string) {
        List<String> saved_strings = getSavedStrings(context);
        saved_strings.add(string);
        String json = new Gson().toJson(saved_strings);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(DataManager.STRING_KEY, json).apply();
    }

    public static String getSavedB64(Context context, int index) {
        String image_key = getSavedString(context, index);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String b64 = sharedPreferences.getString(image_key, "");
        return b64;
    }

    public static Bitmap getSavedBitmap(Context context, int index) {
        int s_index = index;
        if (s_index < NUM_FRUIT) {
            return null; // We don't save images for fruit
        } else {
            s_index -= NUM_FRUIT;
        }
        String b64 = getSavedB64(context, s_index);
        if (b64.isEmpty()) {
            return null;
        }
        byte[] decoded = Base64.decode(b64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        return bitmap;
    }

    public static void addImageB64(Context context, String key, String b64) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(key, b64).apply();
        addString(context, key);
    }

    public static void addImageUri(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            InputStream stream = contentResolver.openInputStream(uri);
            String base64 = getImageBase64(stream);
            addImageB64(context, uri.toString(), base64);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private static String getImageBase64(InputStream stream) {
        Bitmap realImage = BitmapFactory.decodeStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }
}


