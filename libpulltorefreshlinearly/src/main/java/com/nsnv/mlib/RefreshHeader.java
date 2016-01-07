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
public class RefreshHeader extends FrameLayout {

    private MProgressCircle progress_header;
    private TextView txt_header_state;
    private TextView txt_header_time;
    private Resources res;
    private TextView txt_header_last_upate;

    public STATE getState() {
        return state;
    }

    private STATE state;

    public enum STATE{
        ARROW_DOWN(0),
        ARROW_UP(1),
        REFRESHING(2),
        SUCCESS(3),
        FAIL(4);

        int VALUE;
        STATE(int i) {
            this.VALUE = i;
        }
    }

    public RefreshHeader(Context context) {
        super(context);
        init(context, null);
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {

        View view = LayoutInflater.from(context).inflate(R.layout.refresh_header, null);
        addView(view);

        progress_header = (MProgressCircle) findViewById(R.id.progress_header);
        txt_header_state = (TextView) findViewById(R.id.txt_header_state);
        txt_header_time = (TextView) findViewById(R.id.txt_header_time);
        txt_header_last_upate = (TextView) findViewById(R.id.txt_header_last_upate);

        txt_header_time.setText(new SimpleDateFormat().format(new Date()));

        TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.RefreshLinearly);
        int color = typedArray.getColor(R.styleable.RefreshLinearly_colorShow, Color.BLUE);
        txt_header_state.setTextColor(color);
        txt_header_time.setTextColor(color);
        txt_header_last_upate.setTextColor(color);
        typedArray.recycle();

        res = context.getResources();
    }

    public void setState(STATE state){

        this.state = state;

        switch (state){

            case ARROW_DOWN:

                progress_header.setStateArrowDown();
                txt_header_state.setText(res.getString(R.string.refresh_header_pulldown));

                break;
            case ARROW_UP:

                progress_header.setStateArrowUp();
                txt_header_state.setText(res.getString(R.string.refresh_release));

                break;
            case REFRESHING:

                progress_header.setStateLoading();
                txt_header_state.setText(res.getString(R.string.refresh_refreshing));

                break;
            case SUCCESS:

                progress_header.setStateSuccess();
                txt_header_state.setText(res.getString(R.string.refresh_sucess));
                txt_header_time.setText(new SimpleDateFormat().format(new Date()));

                break;
            case FAIL:

                progress_header.setStateFail();
                txt_header_state.setText(res.getString(R.string.refresh_fail));

                break;
            default:
                Log.e(this.toString(), "setState no case matched.");
        }
    }

}
