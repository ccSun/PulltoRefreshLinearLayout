package com.nsnv.mlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Change state by setState(?)
 */
public class RefreshHeaderMy extends RefreshHeader {

    private MProgressCircle progress_header;
    private TextView txt_header_state;
    private TextView txt_header_time;
    private Resources res;

    public RefreshHeaderMy(Context context) {
        super(context);
        init(context, null);
    }

    public RefreshHeaderMy(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RefreshHeaderMy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshHeaderMy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {

        View view = LayoutInflater.from(context).inflate(R.layout.refresh_header, null);
        addView(view);

        progress_header = (MProgressCircle) findViewById(R.id.progress_header);
        txt_header_state = (TextView) findViewById(R.id.txt_header_state);
        txt_header_time = (TextView) findViewById(R.id.txt_header_time);
        TextView txt_header_last_upate = (TextView) findViewById(R.id.txt_header_last_upate);

        txt_header_time.setText(new SimpleDateFormat().format(new Date()));

        TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.RefreshLinearly);
        int color = typedArray.getColor(R.styleable.RefreshLinearly_colorShow, Color.BLUE);
        txt_header_state.setTextColor(color);
        txt_header_time.setTextColor(color);
        txt_header_last_upate.setTextColor(color);
        progress_header.setColor(color);
        typedArray.recycle();

        res = context.getResources();
    }


    @Override
    public void setStatePullDown() {
        stateCurrent = State.PullDown;
        progress_header.setStatePullDown();
        txt_header_state.setText(res.getString(R.string.refresh_header_pulldown));
    }

    @Override
    public void setStatePullUp() {
        stateCurrent = State.PullUp;
        progress_header.setStatePullUp();
        txt_header_state.setText(res.getString(R.string.refresh_release));
    }

    @Override
    public void setStateRefreshIng() {
        stateCurrent = State.RefreshIng;
        progress_header.setStateRefreshIng();
        txt_header_state.setText(res.getString(R.string.refresh_refreshing));
    }

    @Override
    public void setStateRefreshSuccess() {
        stateCurrent = State.RefreshSuccess;
        progress_header.setStateRefreshSuccess();
        txt_header_state.setText(res.getString(R.string.refresh_sucess));
        txt_header_time.setText(new SimpleDateFormat().format(new Date()));
    }

    @Override
    public void setStateRefreshFail() {
        stateCurrent = State.RefreshFail;
        progress_header.setStateRefreshFail();
        txt_header_state.setText(res.getString(R.string.refresh_fail));
    }
}
