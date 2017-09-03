package com.example.ubuntu_master.tic_tac_toe2;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.GridLayout;

/** A RelativeLayout that will always be square -- same width and height,
 * where the height is based off the width. */
public class SquareGridLayout extends GridLayout {

    private boolean landscapeMode = false;

    public SquareGridLayout(Context context) {
        super(context);
    }

    public SquareGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareGridLayout(Context context, AttributeSet attrs,         int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        if(landscapeMode){
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
        else{
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }

    public void setLandscapeMode(boolean mode){
        landscapeMode = mode;
    }

}