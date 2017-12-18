package com.fryarludwig.dulynoted;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NoteCardViewHolder>{
    private List<NoteCard> mNoteCards;
    private OnSaveEventListener mSaveListener;

    RVAdapter(List<NoteCard> notecards){
        mNoteCards = notecards;
    }

    RVAdapter(List<NoteCard> notecards, OnSaveEventListener saveListener){
        mNoteCards = notecards;
        mSaveListener = saveListener;
    }

    public static class NoteCardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        LinearLayout buttonLayout;
        ArrayList<Button> buttonList;
        ImageButton edit_card_button;
        Context mContext;

        NoteCardViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            cv = itemView.findViewById(R.id.note_card_view);
            title = itemView.findViewById(R.id.title_text_view);
            buttonLayout = itemView.findViewById(R.id.notecard_button_layout);
            buttonList = new ArrayList<>();
            edit_card_button = itemView.findViewById(R.id. edit_card_button);
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
    public void onBindViewHolder(NoteCardViewHolder noteCardViewHolder, int index) {

        NoteCard noteCard = mNoteCards.get(index);
        noteCardViewHolder.title.setText(noteCard.mTitle);
        noteCardViewHolder.buttonLayout.removeAllViews();
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.weight = 1;
        for (int i = 0; i < noteCard.getNumberOfOptions(); i++)
        {
            Button newButton = new Button(noteCardViewHolder.mContext);
            newButton.setLayoutParams(p);
            newButton.setText(noteCard.getRecordNames().get(i));
            newButton.setWidth(0);
            newButton.setHeight(0);
            newButton.setTag(i);
//            newButton.setDuplicateParentStateEnabled(true);
            newButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int buttonPosition = Integer.valueOf(view.getTag().toString());
                    int cardIndex = Integer.valueOf(((LinearLayout)view.getParent()).getTag().toString());

                    if (mSaveListener != null)
                    {
                        mSaveListener.onEvent(view, cardIndex, buttonPosition);
                    }
                }
            });

            noteCardViewHolder.buttonList.add(newButton);
            noteCardViewHolder.buttonLayout.addView(newButton);
        }

        noteCardViewHolder.buttonLayout.setTag(index);
        noteCardViewHolder.edit_card_button.setTag(index);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setSaveEventListener(OnSaveEventListener listener)
    {
        mSaveListener = listener;
    }
}
