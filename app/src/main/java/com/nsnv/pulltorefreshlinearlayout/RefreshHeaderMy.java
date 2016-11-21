package com.nsnv.pulltorefreshlinearlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nsnv.mlib.MProgressCircle;
import com.nsnv.mlib.RefreshBase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Change state by setState(?)
 */
public class RefreshHeaderMy extends RefreshBase {

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

        View view = LayoutInflater.from(context).inflate(com.nsnv.mlib.R.layout.refresh_header, null);
        addView(view);

        progress_header = (MProgressCircle) findViewById(com.nsnv.mlib.R.id.progress_header);
        txt_header_state = (TextView) findViewById(com.nsnv.mlib.R.id.txt_header_state);
        txt_header_time = (TextView) findViewById(com.nsnv.mlib.R.id.txt_header_time);
        TextView txt_header_last_upate = (TextView) findViewById(com.nsnv.mlib.R.id.txt_header_last_upate);

        txt_header_time.setText(new SimpleDateFormat().format(new Date()));

        TypedArray typedArray = context.obtainStyledAttributes(attr, com.nsnv.mlib.R.styleable.RefreshLinearly);
        int color = typedArray.getColor(com.nsnv.mlib.R.styleable.RefreshLinearly_colorShow, Color.BLUE);
        txt_header_state.setTextColor(color);
        txt_header_time.setTextColor(color);
        txt_header_last_upate.setTextColor(color);
        progress_header.setColor(color);
        typedArray.recycle();

        res = context.getResources();
    }


    @Override
    public void setStatePullDown() {
        progress_header.setStatePullDownI();
        txt_header_state.setText(res.getString(com.nsnv.mlib.R.string.refresh_header_pulldown));
    }

    @Override
    public void setStatePullUp() {
        progress_header.setStatePullUpI();
        txt_header_state.setText(res.getString(com.nsnv.mlib.R.string.refresh_release));
    }

    @Override
    public void setStateRefreshIng() {
        progress_header.setStateRefreshIngI();
        txt_header_state.setText(res.getString(com.nsnv.mlib.R.string.refresh_refreshing));
    }

    @Override
    public void setStateRefreshSuccess() {
        progress_header.setStateRefreshSuccessI();
        txt_header_state.setText(res.getString(com.nsnv.mlib.R.string.refresh_sucess));
        txt_header_time.setText(new SimpleDateFormat().format(new Date()));
    }

    @Override
    public void setStateRefreshFail() {
        progress_header.setStateRefreshFailI();
        txt_header_state.setText(res.getString(com.nsnv.mlib.R.string.refresh_fail));
    }
}
