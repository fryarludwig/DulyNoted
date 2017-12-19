package com.fryarludwig.dulynoted;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String EXTA_LIST_POSITION = "LIST_POSITION";
    public static final int CREATE_CARD_REQUEST = 1;
    public static final int EDIT_CARD_REQUEST = 2;

    public static ArrayList<NoteCard> mNoteCards;
    private RVAdapter mRvAdapter;

    private void initializeData(){
        mNoteCards = new ArrayList<>();
        mNoteCards.add(new NoteCard("Emma Wilson", new ArrayList<String>(Arrays.asList("A", "B", "C"))));
        mNoteCards.add(new NoteCard("Lavery Maiss", new ArrayList<String>(Arrays.asList(
                "Parmesan",
                "Ricotta",
                "Fontina",
                "Cheddar"
        ))));
        mNoteCards.add(new NoteCard("Lillie Watts", new ArrayList<String>(Arrays.asList("Peanut Butter", "Peanut Butter", "Peanut Butter"))));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeData();

        LinearLayoutManager llm = new LinearLayoutManager(this);

        ConstraintLayout mainLayout = findViewById(R.id.main_activity_layout);
        mainLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeLeft() {
                Toast.makeText(mContext, "left", Toast.LENGTH_SHORT).show();
                ((MainActivity)mContext).onOpenVisualizeActivity();
            }
        });
        RecyclerView rv = this.findViewById(R.id.card_recycle_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(llm);

        mRvAdapter = new RVAdapter(mNoteCards, new OnSaveEventListener() {
            @Override
            public void onEvent(View view, int cardPosition, int buttonPosition) {
                NoteCard noteCard = mNoteCards.get(cardPosition);
                Record record = noteCard.mRecordsList.get(buttonPosition);
                record.logEvent();
                String message = String.format("Event saved: %s; %s; %d", noteCard.mTitle,
                        noteCard.getRecordName(buttonPosition), record.mTimestampsList.size());
                mRvAdapter.notifyItemChanged(cardPosition);
                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_activity_layout), message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }, new OnCardPreviewToggle() {
            @Override
            public void onEvent(View view, int cardPosition) {
                boolean isCollapsed = mNoteCards.get(cardPosition).mIsCollapsed;
                mNoteCards.get(cardPosition).mIsCollapsed = !isCollapsed;
                mRvAdapter.notifyItemChanged(cardPosition);
            }
        });
        rv.setAdapter(mRvAdapter);
    }

    public void onOpenVisualizeActivity(){
        Intent intent = new Intent(this, VisualizeActivity.class);
        startActivity(intent);
    }

    public void onAddClicked(View view)
    {
        Intent intent = new Intent(this, EditCardActivity.class);
        startActivityForResult(intent, CREATE_CARD_REQUEST);
    }

    public void onEditCardClicked(View view)
    {
        Integer position = Integer.valueOf(view.getTag().toString());
        Intent intent = new Intent(view.getContext(), EditCardActivity.class);
        intent.putExtra(EXTA_LIST_POSITION, position);
        startActivityForResult(intent, EDIT_CARD_REQUEST);
    }

    public void onSaveEventNotify(View view, int cardPosition, int buttonPosition)
    {
        String message = String.format("Save event: Card %# button %s", cardPosition, buttonPosition);
        Snackbar.make(view, message, Snackbar.LENGTH_LONG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        switch (requestCode)
        {
            case CREATE_CARD_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    handleUserCreateCard(resultData);
                }
                break;
            case EDIT_CARD_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    handleUserEditCard(resultData);
                }
                break;
            default:
                break;
//            case SOME_OTHER_REQUEST:
//                handleSomethingElse(aData);
//                break;
        }
        super.onActivityResult(requestCode, resultCode, resultData);
    }


    protected void handleUserEditCard(Intent resultData)
    {
        int position = resultData.getIntExtra(EditCardActivity.EXTRA_NOTECARD_POSITION, -1);

        if (resultData.getBooleanExtra(EditCardActivity.EXTRA_NOTECARD_DELETE_ME, false ))
        {
            mNoteCards.remove(position);
            mRvAdapter.notifyItemRemoved(position);
        }
        else
        {
            NoteCard noteCard = (NoteCard)resultData.getParcelableExtra(EditCardActivity.EXTRA_NOTECARD);
            mNoteCards.get(position).updateNoteCardValues(noteCard);
//            mNoteCards.set(position, noteCard);
            mRvAdapter.notifyItemChanged(position);
        }
    }

    protected void handleUserCreateCard(Intent resultData)
    {
        mNoteCards.add((NoteCard)resultData.getParcelableExtra(EditCardActivity.EXTRA_NOTECARD));
        mRvAdapter.notifyItemInserted(mNoteCards.size() - 1);
    }
}
