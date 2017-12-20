package com.fryarludwig.dulynoted;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoteCardViewHolder> {
    private List<NoteCard> mNoteCards;
    private OnSaveEventListener mSaveListener;
    private OnCardPreviewToggle mPreviewToggleListener;
    private LinearLayout.LayoutParams buttonLayout;

    RVAdapter(List<NoteCard> notecards) {
        mNoteCards = notecards;
    }

    RVAdapter(List<NoteCard> notecards, OnSaveEventListener saveListener, OnCardPreviewToggle previewToggle) {
        mNoteCards = notecards;
        mSaveListener = saveListener;
        mPreviewToggleListener = previewToggle;

        buttonLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayout.weight = 1;
    }

    public static class NoteCardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        LinearLayout buttonLayout;
        ArrayList<Button> buttonList;
        LinearLayout recordsPreviewLayout;
        ImageButton expandPreviewButton;
        Button visualizeRecordsButton;
        ImageButton edit_card_button;
        Context mContext;
        boolean isCollapsed;

        NoteCardViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            cv = itemView.findViewById(R.id.note_card_view);
            title = itemView.findViewById(R.id.title_text_view);
            buttonLayout = itemView.findViewById(R.id.notecard_button_layout);
            buttonList = new ArrayList<>();
            recordsPreviewLayout = itemView.findViewById(R.id.records_preview_layout);
            visualizeRecordsButton = itemView.findViewById(R.id.visualize_records_button);
            expandPreviewButton = itemView.findViewById(R.id.expand_card_button);
            edit_card_button = itemView.findViewById(R.id.edit_card_button);
            isCollapsed = false;
        }
    }

    @Override
    public int getItemCount() {
        return mNoteCards.size();
    }

    @Override
    public NoteCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_card_layout, viewGroup, false);
        return new NoteCardViewHolder(v, viewGroup.getContext());
    }

    @Override
    public void onBindViewHolder(NoteCardViewHolder noteCardViewHolder, final int index) {
        NoteCard noteCard = mNoteCards.get(index);
        noteCardViewHolder.title.setText(noteCard.mTitle);
        noteCardViewHolder.buttonLayout.removeAllViews();
        noteCardViewHolder.recordsPreviewLayout.removeAllViews();

        for (int i = 0; i < noteCard.getNumberOfOptions(); i++) {
            Button newButton = getNoteCardButton(noteCardViewHolder.mContext,
                    noteCard.getRecordNames().get(i), i);
            noteCardViewHolder.buttonList.add(newButton);
            noteCardViewHolder.buttonLayout.addView(newButton);
            TextView previewTextView = new TextView(noteCardViewHolder.mContext);
            int visibility = (noteCard.mIsCollapsed && i > 0) ? View.GONE : View.VISIBLE;

            if (noteCard.mIsCollapsed) {
                previewTextView.setText(noteCard.getLatestRecord().toString());
            } else {
                previewTextView.setText(noteCard.getRecordByIndex(i).toString());
            }

            previewTextView.setTextColor(Color.parseColor("#ffd6d7d7"));
            previewTextView.setVisibility(visibility);
            noteCardViewHolder.recordsPreviewLayout.addView(previewTextView);
        }

        noteCardViewHolder.expandPreviewButton.setTag(index);
        if (!noteCardViewHolder.expandPreviewButton.hasOnClickListeners()) {
            noteCardViewHolder.expandPreviewButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPreviewToggleListener != null) {
                        mPreviewToggleListener.onEvent(view, (int) view.getTag());
                    }
                }
            });
        }

        if (noteCard.mIsCollapsed) {
            noteCardViewHolder.expandPreviewButton.setImageDrawable(ContextCompat.getDrawable(
                    noteCardViewHolder.expandPreviewButton.getContext(), R.drawable.ic_expand_less));
        } else {
            noteCardViewHolder.expandPreviewButton.setImageDrawable(ContextCompat.getDrawable(
                    noteCardViewHolder.expandPreviewButton.getContext(), R.drawable.ic_expand_more_dark));
        }

        noteCardViewHolder.visualizeRecordsButton.setVisibility((noteCard.mIsCollapsed) ? View.GONE : View.VISIBLE);
        noteCardViewHolder.recordsPreviewLayout.addView(noteCardViewHolder.visualizeRecordsButton);
        noteCardViewHolder.buttonLayout.setTag(index);
        noteCardViewHolder.edit_card_button.setTag(index);
    }

    private Button getNoteCardButton(Context context, String buttonText, int tag) {
        Button newButton = new Button(context);
        newButton.setLayoutParams(buttonLayout);
        newButton.setText(buttonText);
        newButton.setWidth(0);
        newButton.setHeight(0);
        newButton.setTag(tag);
        newButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                int buttonPosition = Integer.valueOf(view.getTag().toString());
                int cardIndex = Integer.valueOf(((LinearLayout) view.getParent()).getTag().toString());

                if (mSaveListener != null) {
                    mSaveListener.onEvent(view, cardIndex, buttonPosition);
                }
            }
        });
        return newButton;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setSaveEventListener(OnSaveEventListener listener) {
        mSaveListener = listener;
    }
}
