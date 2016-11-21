package com.nsnv.mlib;

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

/**
 *
 */
public class RefreshFooterMy extends RefreshBase {
    private Context cxt;
    private Resources res;
    private MProgressCircle progress_circle;
    private TextView txt_footer_state;

    public RefreshFooterMy(Context context) {
        super(context);
        init(context, null);
    }

    public RefreshFooterMy(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RefreshFooterMy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshFooterMy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        this.cxt = context;

        View view = LayoutInflater.from(cxt).inflate(R.layout.refresh_footer, null);
        addView(view);

        progress_circle = (MProgressCircle) view.findViewById(R.id.progress_footer);
        txt_footer_state = (TextView) view.findViewById(R.id.txt_footer_state);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLinearly);
        int color = typedArray.getColor(R.styleable.RefreshLinearly_colorShow, Color.BLUE);
        txt_footer_state.setTextColor(color);
        progress_circle.setColor(color);
        typedArray.recycle();

        res = context.getResources();
    }

    @Override
    public void setStatePullDown() {
        progress_circle.setVisibility(VISIBLE);
        progress_circle.setStatePullDownI();
        txt_footer_state.setVisibility(GONE);
        txt_footer_state.setText(res.getString(R.string.refresh_release));
    }

    @Override
    public void setStatePullUp() {
        progress_circle.setVisibility(VISIBLE);
        progress_circle.setStatePullUpI();
        txt_footer_state.setVisibility(GONE);
        txt_footer_state.setText(res.getString(R.string.refresh_footer_pullup));
    }

    @Override
    public void setStateRefreshIng() {
        progress_circle.setVisibility(VISIBLE);
        progress_circle.setStateRefreshIngI();
        txt_footer_state.setVisibility(GONE);
    }

    @Override
    public void setStateRefreshSuccess() {
        progress_circle.setVisibility(VISIBLE);
        progress_circle.setStateRefreshSuccessI();
        txt_footer_state.setVisibility(GONE);
    }

    @Override
    public void setStateRefreshFail() {
        progress_circle.setVisibility(VISIBLE);
        progress_circle.setStateRefreshFailI();
        txt_footer_state.setVisibility(GONE);
    }

}
