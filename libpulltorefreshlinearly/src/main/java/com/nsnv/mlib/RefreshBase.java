package com.nsnv.mlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by ccSun on 11/18/16.
 * 刷新header和加载footer的抽象类
 */

public abstract class RefreshBase extends FrameLayout implements RefreshStateI{

    private RefreshStateI.State stateCurrent;
    public RefreshStateI.State getState() {
        return stateCurrent;
    }

    public RefreshBase(Context context) {
        super(context);
    }

    public RefreshBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshBase(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setStatePullDownI() {
        stateCurrent = State.PullDown;
        setStatePullDown();
    }

    public abstract void setStatePullDown();

    @Override
    public void setStatePullUpI() {
        stateCurrent = State.PullUp;
        setStatePullUp();
    }

    public abstract void setStatePullUp();

    @Override
    public void setStateRefreshIngI() {
        stateCurrent = State.RefreshIng;
        setStateRefreshIng();
    }

    public abstract void setStateRefreshIng();

    @Override
    public void setStateRefreshSuccessI() {
        stateCurrent = State.RefreshSuccess;
        setStateRefreshSuccess();
    }

    public abstract void setStateRefreshSuccess();

    @Override
    public void setStateRefreshFailI() {
        stateCurrent = State.RefreshFail;
        setStateRefreshFail();
    }

    public abstract void setStateRefreshFail();
}
