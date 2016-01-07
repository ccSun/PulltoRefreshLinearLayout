package com.nsnv.refreshlinearly;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nsnv.mlib.R;

import org.w3c.dom.Attr;

/**
 *
 */
public class RefreshFooter extends FrameLayout{
    private Context cxt;
    private Resources res;
    private MProgressCircle progress_circle;
    private TextView txt_footer_state;
    private STATE mState = STATE.ARROW_UP;
    private AttributeSet attr;


    public STATE getState() {
        return mState;
    }

    public enum STATE{
        ARROW_UP(1),
        ARROW_DOWN(2),
        LOADING(3),
        SUCCESS(4),
        FAIL(5);

        int VALUE;

        STATE(int i) {
            VALUE = i;
        }
    }

    public RefreshFooter(Context context) {
        super(context);
        init(context, null);
    }

    public RefreshFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RefreshFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RefreshFooter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.cxt = context;
        this.attr = attrs;

        View view = LayoutInflater.from(cxt).inflate(R.layout.refresh_footer, null);
        addView(view);

        progress_circle = (MProgressCircle) view.findViewById(R.id.progress_footer);
        txt_footer_state = (TextView) view.findViewById(R.id.txt_footer_state);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshLinearly);
        int color = typedArray.getColor(R.styleable.RefreshLinearly_colorShow, Color.BLUE);
        txt_footer_state.setTextColor(color);
        typedArray.recycle();

        res = context.getResources();
    }

    public void setState(RefreshFooter.STATE state){
        mState = state;
        switch (state){
            case ARROW_UP:

                progress_circle.setVisibility(VISIBLE);
                progress_circle.setStateArrowUp();
                txt_footer_state.setVisibility(GONE);
                txt_footer_state.setText(res.getString(R.string.refresh_footer_pullup));

                break;
            case ARROW_DOWN:

                progress_circle.setVisibility(VISIBLE);
                progress_circle.setStateArrowDown();
                txt_footer_state.setVisibility(GONE);
                txt_footer_state.setText(res.getString(R.string.refresh_release));

                break;
            case LOADING:

                progress_circle.setVisibility(VISIBLE);
                progress_circle.setStateLoading();
                txt_footer_state.setVisibility(GONE);

                break;
            case SUCCESS:

                progress_circle.setVisibility(VISIBLE);
                progress_circle.setStateSuccess();
                txt_footer_state.setVisibility(GONE);

                break;
            case FAIL:

                progress_circle.setVisibility(VISIBLE);
                progress_circle.setStateFail();
                txt_footer_state.setVisibility(GONE);

                break;
            default:
                MLog.e(this, "RefreshFooter setState no case matched.");
        }
    }

}
