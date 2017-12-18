package com.fryarludwig.dulynoted;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ValueIterator extends LinearLayout {
    private int mMinValue = 1;
    private int mMaxValue = 10;
    private int mCurrentValue = 1;

    View mRootView;
    TextView mValueTextView;
    ImageButton mDecrementButton;
    ImageButton mIncrementButton;
    OnValueChangedListener mListener;


    public ValueIterator(Context context) {
        super(context);
        init(context);
    }

    public ValueIterator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ValueIterator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ValueIterator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mRootView = inflate(context, R.layout.value_iterator_layout, this);
        mValueTextView = (TextView) mRootView.findViewById(R.id.number_value);

        mDecrementButton = mRootView.findViewById(R.id.decrement_button);
        mIncrementButton = mRootView.findViewById(R.id.increment_button);

        mDecrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementValue();
            }
        });

        mIncrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementValue();
            }
        });
    }

    public void setValueChangeListener(OnValueChangedListener listener)
    {
        mListener = listener;
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int newValue) {
        if (newValue > mCurrentValue)
        {
            mCurrentValue = newValue;
            onViewUpdate();
        }

        this.mMinValue = newValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int newValue) {
        if (newValue < mCurrentValue)
        {
            mCurrentValue = newValue;
            onViewUpdate();
        }
        this.mMaxValue = newValue;
    }

    public int getCurrentValue() {
        return mCurrentValue;
    }

    public void setCurrentValue(int newValue) {
        if (newValue > mMaxValue)
        {
            newValue = mMaxValue;
        }
        if (newValue < mMinValue)
        {
            newValue = mMinValue;
        }

        this.mCurrentValue = newValue;
        onViewUpdate();
    }

    public void incrementValue()
    {
        if (mCurrentValue < mMaxValue)
        {
            mCurrentValue++;
            onViewUpdate();
        }
    }

    public void onIncrementValue(View view)
    {
        incrementValue();
    }

    public void decrementValue()
    {
        if (mCurrentValue > mMinValue)
        {
            mCurrentValue--;
            onViewUpdate();
        }
    }


    public void onDecrementValue(View view)
    {
        decrementValue();
    }

    private void onViewUpdate()
    {
        mValueTextView.setText(String.valueOf(mCurrentValue));
        if (mListener != null)
        {
            mListener.onEvent(this);
        }
    }
}
