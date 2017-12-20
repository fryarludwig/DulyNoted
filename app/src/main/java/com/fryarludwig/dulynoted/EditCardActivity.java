package com.fryarludwig.dulynoted;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import static android.support.design.widget.Snackbar.*;

public class EditCardActivity extends AppCompatActivity {
    public static final String EXTRA_NOTECARD = "EXTRA_NOTECARD";
    public static final String EXTRA_NOTECARD_POSITION = "EXTRA_NOTECARD_POSITION";
    public static final String EXTRA_NOTECARD_DELETE_ME = "EXTRA_NOTECARD_DELETE_ME";
    private int mNoteCardPosition = -1;

    ValueIterator mValueIterator;

    protected int[] mOptionIdArray = new int[]{
            R.id.card_item_text_1,
            R.id.card_item_text_2,
            R.id.card_item_text_3,
            R.id.card_item_text_4
    };

    private final int MIN_ITERATOR_VALUE = 1;
    private final int DEFAULT_ITERATOR_VALUE = 2;
    private final int MAX_ITERATOR_VALUE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent intent = getIntent();
        Integer position = intent.getIntExtra(MainActivity.EXTA_LIST_POSITION, -1);
        mNoteCardPosition = position;

        mValueIterator = findViewById(R.id.value_iterator);
        mValueIterator.setMinValue(MIN_ITERATOR_VALUE);
        mValueIterator.setMaxValue(MAX_ITERATOR_VALUE);
        mValueIterator.setValueChangeListener(new OnValueChangedListener() {
            @Override
            public void onEvent(View view) {
                EditCardActivity activity = (EditCardActivity) view.getContext();
                activity.updateVisibleFields();
            }
        });

        if (position >= 0) {
            NoteCard notecard = MainActivity.mNoteCards.get(position);
            mValueIterator.setCurrentValue(notecard.getNumberOfOptions());
            setValuesByNoteCard(notecard);
        } else {
            Button delete_button = findViewById(R.id.delete_card_button);
            delete_button.setVisibility(View.INVISIBLE);
            mValueIterator.setCurrentValue(DEFAULT_ITERATOR_VALUE);
        }
    }

    public NoteCard getValuesAsNoteCard() {
        NoteCard newNotecard = new NoteCard();
        EditText titleTextEdit = (EditText)this.findViewById(R.id.card_title_input);
        newNotecard.mTitle = titleTextEdit.getText().toString();
        for (int i = 0; i < mValueIterator.getCurrentValue(); i++) {
            String textValue = ((EditText) this.findViewById(mOptionIdArray[i])).getText().toString();
            if (textValue.length() > 0) {
                newNotecard.addNewRecord(textValue);
            }
        }

        if (newNotecard.mTitle.length() == 0) {
            titleTextEdit.setError("This card needs a title");
            return null;
        }
        if (newNotecard.getNumberOfOptions() == 0) {
            EditText firstTextField = findViewById(R.id.card_item_text_1);
            firstTextField.setError("You need to have at least one card filled");
            return null;
        }

        return newNotecard;
    }

    public void setValuesByNoteCard(NoteCard notecard) {
        mValueIterator.setCurrentValue(notecard.getNumberOfOptions());
        ((EditText) this.findViewById(R.id.card_title_input)).setText(notecard.mTitle);
        for (int i = 0; i < notecard.getNumberOfOptions(); i++) {
            ((EditText) this.findViewById(mOptionIdArray[i])).setText(notecard.getRecordName(i));
        }
    }

    public void updateVisibleFields() {
        int numFieldsVisible = mValueIterator.getCurrentValue();
        for (int i = 0; i < mOptionIdArray.length; i++) {
            EditText textField = findViewById(mOptionIdArray[i]);
            textField.setVisibility(i < numFieldsVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void onDelete(View view) {
        Intent newCardIntent = new Intent();
        newCardIntent.putExtra(EXTRA_NOTECARD_POSITION, mNoteCardPosition);
        newCardIntent.putExtra(EXTRA_NOTECARD_DELETE_ME, true);
        setResult(Activity.RESULT_OK, newCardIntent);
        finish();
    }

    public void onAccept(View view) {
        Intent newCardIntent = new Intent();
        NoteCard updatedNoteCard = getValuesAsNoteCard();
        if (updatedNoteCard != null) {
            newCardIntent.putExtra(EXTRA_NOTECARD, updatedNoteCard);
            newCardIntent.putExtra(EXTRA_NOTECARD_POSITION, mNoteCardPosition);
            setResult(Activity.RESULT_OK, newCardIntent);
            finish();
        }
    }

    public void onCancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

}
