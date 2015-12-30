package com.nsnv.mlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 *
 */
public class RefreshFooter extends FrameLayout{
    private Context cxt;
    private Resources res;
    private MProgressCircle progress_circle;
    private TextView txt_footer_state;
    private STATE mState = STATE.PULL_UP;


    public STATE getState() {
        return mState;
    }

    public enum STATE{
        PULL_UP(1),
        RELEASE_TO_LOAD(2),
        LOADING(3),
        SUCESS(4),
        FAIL(5);

        int VALUE;

        STATE(int i) {
            VALUE = i;
        }
    }

    public RefreshFooter(Context context) {
        super(context);
        init(context);
    }

    public RefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshFooter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.cxt = context;

        View view = LayoutInflater.from(cxt).inflate(R.layout.refresh_footer, null);
        addView(view);

        progress_circle = (MProgressCircle) view.findViewById(R.id.progress_footer);
        txt_footer_state = (TextView) view.findViewById(R.id.txt_footer_state);
        txt_footer_state.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (STATE.PULL_UP == mState)
                    setState(STATE.LOADING);
            }
        });

        res = context.getResources();
    }

    public void setState(RefreshFooter.STATE state){
        mState = state;
        switch (state){
            case PULL_UP:

                progress_circle.setVisibility(GONE);
                txt_footer_state.setVisibility(VISIBLE);
                txt_footer_state.setText(res.getString(R.string.refresh_footer_pullup));

                break;
            case RELEASE_TO_LOAD:

                progress_circle.setVisibility(GONE);
                txt_footer_state.setVisibility(VISIBLE);
                txt_footer_state.setText(res.getString(R.string.refresh_release));

                break;
            case LOADING:

                progress_circle.setVisibility(VISIBLE);
                txt_footer_state.setVisibility(GONE);
                progress_circle.setStateLoading();

                break;
            case SUCESS:

                progress_circle.setVisibility(VISIBLE);
                txt_footer_state.setVisibility(GONE);

                break;
            case FAIL:

                progress_circle.setVisibility(VISIBLE);
                txt_footer_state.setVisibility(GONE);

                break;
            default:
                Log.e(this.toString(), "RefreshFooter setState no case matched.");
        }
    }

}
