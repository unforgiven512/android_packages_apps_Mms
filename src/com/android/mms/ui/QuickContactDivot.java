// Copyright 2011 Google Inc.
// All Rights Reserved.

package com.android.mms.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.QuickContactBadge;

import com.android.mms.R;

public class QuickContactDivot extends QuickContactBadge implements Divot{
    private Drawable mDrawable;
    private int mDrawableIntrinsicWidth;
    private int mDrawableIntrinsicHeight;
    private int mPosition;

    // The screen density.  Multiple this by dips to get pixels.
    private float mDensity;

    private boolean mBlackBackground;       // Option to switch background to black (from white)

    public QuickContactDivot(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(attrs);
    }

    public QuickContactDivot(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public QuickContactDivot(Context context) {
        super(context);
        initialize(null);
    }

    private void initialize(AttributeSet attrs) {
        if (attrs != null) {
            mPosition = attrs.getAttributeListValue(null, "position", sPositionChoices, -1);
        }

        Resources r = getContext().getResources();
        mDensity = r.getDisplayMetrics().density;

        setDrawable();
    }

    private void setDrawable() {
        Resources r = getContext().getResources();

        // Read preference for (optional) black background
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mBlackBackground = prefs.getBoolean(MessagingPreferenceActivity.BLACK_BACKGROUND, false);

        // Handle (optional) black background
        if (!mBlackBackground) {
            switch (mPosition) {
                case LEFT_UPPER:
                case LEFT_MIDDLE:
                case LEFT_LOWER:
                    mDrawable = r.getDrawable(R.drawable.msg_bubble_right);
                    break;

                case RIGHT_UPPER:
                case RIGHT_MIDDLE:
                case RIGHT_LOWER:
                    mDrawable = r.getDrawable(R.drawable.msg_bubble_left);
                    break;

//                case TOP_LEFT:
//                case TOP_MIDDLE:
//                case TOP_RIGHT:
//                    mDrawable = r.getDrawable(R.drawable.msg_bubble_bottom);
//                    break;
//
//                case BOTTOM_LEFT:
//                case BOTTOM_MIDDLE:
//                case BOTTOM_RIGHT:
//                    mDrawable = r.getDrawable(R.drawable.msg_bubble_top);
//                    break;
            }
        } else {
            switch (mPosition) {
                case LEFT_UPPER:
                case LEFT_MIDDLE:
                case LEFT_LOWER:
                    mDrawable = r.getDrawable(R.drawable.msg_bubble_right_dark);
                    break;

                case RIGHT_UPPER:
                case RIGHT_MIDDLE:
                case RIGHT_LOWER:
                    mDrawable = r.getDrawable(R.drawable.msg_bubble_left_dark);
                    break;

//                case TOP_LEFT:
//                case TOP_MIDDLE:
//                case TOP_RIGHT:
//                    mDrawable = r.getDrawable(R.drawable.msg_bubble_bottom);
//                    break;
//
//                case BOTTOM_LEFT:
//                case BOTTOM_MIDDLE:
//                case BOTTOM_RIGHT:
//                    mDrawable = r.getDrawable(R.drawable.msg_bubble_top);
//                    break;
            }
        }

        mDrawableIntrinsicWidth = mDrawable.getIntrinsicWidth();
        mDrawableIntrinsicHeight = mDrawable.getIntrinsicHeight();
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        c.save();
        computeBounds(c);
        mDrawable.draw(c);
        c.restore();
    }

    public void setPosition(int position) {
        mPosition = position;
        setDrawable();
        invalidate();
    }

    public int getPosition() {
        return mPosition;
    }

    public float getCloseOffset() {
        return CORNER_OFFSET * mDensity;  // multiply by density to get pixels
    }

    public ImageView asImageView() {
        return this;
    }

    public void assignContactFromEmail(String emailAddress) {
        assignContactFromEmail(emailAddress, true);
    }

    public float getFarOffset() {
        return getCloseOffset() + mDrawableIntrinsicHeight;
    }

    private void computeBounds(Canvas c) {
        final int left = 0;
        final int top = 0;
        final int right = getWidth();
        final int middle = right / 2;
        final int bottom = getHeight();

        final int cornerOffset = (int) getCloseOffset();

        switch (mPosition) {
            case RIGHT_UPPER:
                mDrawable.setBounds(
                        right - mDrawableIntrinsicWidth,
                        top + cornerOffset,
                        right,
                        top + cornerOffset + mDrawableIntrinsicHeight);
                break;

            case LEFT_UPPER:
                mDrawable.setBounds(
                        left,
                        top + cornerOffset,
                        left + mDrawableIntrinsicWidth,
                        top + cornerOffset + mDrawableIntrinsicHeight);
                break;

            case BOTTOM_MIDDLE:
                int halfWidth = mDrawableIntrinsicWidth / 2;
                mDrawable.setBounds(
                        (int)(middle - halfWidth),
                        (int)(bottom - mDrawableIntrinsicHeight),
                        (int)(middle + halfWidth),
                        (int)(bottom));

                break;
        }
    }

}
