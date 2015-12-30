package com.nsnv.mlib;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Default do pulling down refreshing.
 * You should set enableRefresh false if you dont want to refresh.
 * <p/>
 * You must register an OnRefreshListener.
 * <p/>
 * You can stop refreshing by calling stopRefreshSucess() and stopRefreshFail().
 */
public class RefreshLinearly extends LinearLayout{

    private Context cxt;
    private int headerHeight;
    private RefreshHeader header;

    private int yLastRecord = -1;
    private Scroller mScroller;

    private final int PULL_DOWN_HEIGHT = 20;
    private final int MOVE_SHAKE = 20;
    private OnRefreshListener refreshListener;
    private boolean enableRefresh = true;

    public RefreshLinearly(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RefreshLinearly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.cxt = context;

        header = new RefreshHeader(cxt);
        LayoutParams lp = new LayoutParams(cxt, attrs);
        lp.gravity = Gravity.CENTER;
        headerHeight= cxt.getResources().getDimensionPixelSize(R.dimen.refresh_header_footer_size);
        lp.topMargin = -headerHeight;
        addView(header, lp);

        mScroller = new Scroller(cxt);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if(!enableRefresh)
            return false;

        if(header.getState() == RefreshHeader.STATE.REFRESHING.VALUE){
            return true;
        }

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                yLastRecord = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                Log.e(this.toString(), "======getRawY=" + event.getRawY());
                Log.e(this.toString(), "======yLastRecord=" + yLastRecord);
                int yDiff = (int) (event.getRawY() - yLastRecord);
                if( yDiff > MOVE_SHAKE && !canChildViewScroll()) {
                    return true;
                }

                break;
        }
        return  false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!enableRefresh)
            return false;

        if(header.getState() == RefreshHeader.STATE.REFRESHING.VALUE){
            return false;
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                yLastRecord = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                int yDiff = (int) (event.getRawY() - yLastRecord);

                LayoutParams lp = (LayoutParams) header.getLayoutParams();
                int tmpTopMargin = lp.topMargin + yDiff;

                lp.topMargin = Math.max(tmpTopMargin, -headerHeight);
                header.setLayoutParams(lp);
                this.invalidate();

                if(lp.topMargin > PULL_DOWN_HEIGHT) {
                    header.setState(RefreshHeader.STATE.RELEASE_TOREFRESH);
                }else{
                    header.setState(RefreshHeader.STATE.PULL_DOWN);
                }
                yLastRecord = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_UP:

                LayoutParams lp1 = (LayoutParams) header.getLayoutParams();
                if(lp1.topMargin > PULL_DOWN_HEIGHT){

                    int yScroll = lp1.topMargin;
                    mScroller.startScroll(0, yScroll, 0, -yScroll);
                    this.invalidate();

                    header.setState(RefreshHeader.STATE.REFRESHING);
                    if(null == refreshListener)
                        throw new NullPointerException(this.getClass().toString() + " - you havent register an OnRefreshListener.");
                    refreshListener.onRefresh();

                }else{

                    int yScrollDiff = lp1.topMargin + headerHeight;
                    mScroller.startScroll(0, lp1.topMargin, 0, -yScrollDiff);
                    this.invalidate();
                }

                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            int i = mScroller.getCurrY();
            LayoutParams lp = (LayoutParams) header.getLayoutParams();
            lp.topMargin = Math.max(i, -headerHeight);
            header.setLayoutParams(lp);
            this.invalidate();
        }
    }

    private boolean canChildViewScroll(){
        View viewChild;
        if(getChildCount()>1) {
            viewChild = this.getChildAt(1);
            if(viewChild instanceof RecyclerView) {

                int scrollOffset = ((RecyclerView) viewChild).computeVerticalScrollOffset();
                int scrollRange = ((RecyclerView) viewChild).computeVerticalScrollRange();
                int scrollExtent = ((RecyclerView) viewChild).computeVerticalScrollExtent();
                if(0 == scrollOffset)
                    return false;
                else if((scrollRange+scrollExtent) == scrollOffset)
                    return false;
            }else if(viewChild instanceof ScrollView){
                if(viewChild.getScrollY() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private void resetHeader(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                LayoutParams lp = (LayoutParams) header.getLayoutParams();
                int yScroll = lp.topMargin;
                mScroller.startScroll(0, yScroll, 0, -headerHeight);
                invalidate();
                header.setState(RefreshHeader.STATE.PULL_DOWN);
            }
        },1000);
    }

    /**
     * Call this func to stop refreshing when you get data successfully.
     */
    public void stopRefreshSucess(){
        LayoutParams lp = (LayoutParams) header.getLayoutParams();
        if(0 == lp.topMargin && enableRefresh){

            header.setState(RefreshHeader.STATE.SUCESS);
            resetHeader();
        }else {

            Log.e(this.toString(), this.getClass().toString() + " - Header is not refreshing now. Cant stopRefreshSuccess.");
        }
    }

    /**
     * Call this func to stop refreshing when you fail to get data.
     */
    public void stopRefreshFail(){
        LayoutParams lp = (LayoutParams) header.getLayoutParams();
        if(0 == lp.topMargin && enableRefresh){

            header.setState(RefreshHeader.STATE.FAIL);
            resetHeader();
        }else {

            Log.e(this.toString(), this.getClass().toString() + " - Header is not refreshing now. Cant stopRefreshFail.");
        }
    }

    public interface OnRefreshListener{
        void onRefresh();
    }

    /**
     * @param listener Register an OnRefreshListener.
     */
    public void setOnRefreshListener(OnRefreshListener listener){
        this.refreshListener = listener;
    }

    /**
     * @param enable Enable refresh or not.
     */
    public void setRefreshEnabled(boolean enable){
        this.enableRefresh = enable;
    }
}
