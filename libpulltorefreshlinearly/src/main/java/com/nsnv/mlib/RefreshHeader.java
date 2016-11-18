package com.nsnv.mlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by ccSun on 11/18/16.
 * header抽象类
 */

public abstract class RefreshHeader extends FrameLayout implements RefreshStateI{

    protected RefreshStateI.State stateCurrent;
    public RefreshStateI.State getState() {
        return stateCurrent;
    }


    public RefreshHeader(Context context) {
        super(context);
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
