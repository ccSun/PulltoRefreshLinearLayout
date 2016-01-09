package com.nsnv.mlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Default enable pulling down refreshing.
 * <p/>
 * You must register an OnRefreshListener.
 * <p/>
 * You can stop refreshing by calling stopRefreshSuccess() and stopRefreshFail().
 */
public class RefreshLinearly extends LinearLayout{

    private int HEIGHT_HEADER_FOOTER;
    private RefreshHeader header;
    private RefreshFooter footer;

    private int yLastRecord = -1;
    private Scroller mScroller;

    private final int MOVE_SHAKE = 20;
    private boolean enableRefresh = true;
    private boolean enableLoadmore = false;
    private OnRefreshListener refreshListener;
    private OnLoadMoreListener loadmoreListener;

    private enum STATE{
        Normal(0),
        PullDown(1),
        PullUp(2);

        int value;
        STATE(int i) {
            this.value = i;
        }
    }
    private STATE state;

    public RefreshLinearly(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RefreshLinearly(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        header = new RefreshHeader(context, attrs);
        LayoutParams lp = new LinearLayout.LayoutParams(context, attrs);
        lp.gravity = Gravity.CENTER;
        HEIGHT_HEADER_FOOTER = context.getResources().getDimensionPixelSize(R.dimen.refresh_header_footer_size);
        lp.topMargin = -HEIGHT_HEADER_FOOTER;
        addView(header, lp);

        footer = new RefreshFooter(context, attrs);

        mScroller = new Scroller(context);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {


        if(!enableRefresh && !enableLoadmore)
            return false;

        if(header.getState() == RefreshHeader.STATE.REFRESHING
                || header.getState() == RefreshHeader.STATE.SUCCESS
                || header.getState() == RefreshHeader.STATE.FAIL){
            return false;
        }

        if(footer.getState() == RefreshFooter.STATE.LOADING
                || footer.getState() == RefreshFooter.STATE.SUCCESS
                || footer.getState() == RefreshFooter.STATE.FAIL){
            return false;
        }

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                yLastRecord = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                int yDiff = (int) (event.getRawY() - yLastRecord);
                if( yDiff > MOVE_SHAKE && !canChildViewPullDown()) {
                    state = STATE.PullDown;
                    return true;
                }else if( yDiff < -MOVE_SHAKE && !canChildViewPullUp()){
                    state = STATE.PullUp;
                    return true;
                }

                break;
        }
        return  false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!enableRefresh && !enableLoadmore)
            return false;

        if(header.getState() == RefreshHeader.STATE.REFRESHING
                || header.getState() == RefreshHeader.STATE.SUCCESS
                || header.getState() == RefreshHeader.STATE.FAIL){
            return false;
        }

        if(footer.getState() == RefreshFooter.STATE.LOADING
                || footer.getState() == RefreshFooter.STATE.SUCCESS
                || footer.getState() == RefreshFooter.STATE.FAIL){
            return false;
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                yLastRecord = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                int yDiff = (int) (event.getRawY() - yLastRecord);

                if(STATE.PullDown == state){

                    LayoutParams lp = (LayoutParams) header.getLayoutParams();
                    int tmpTopMargin = lp.topMargin + yDiff;

//                    lp.topMargin = Math.max(tmpTopMargin, -HEIGHT_HEADER_FOOTER); // I forget why i coded this line
                    lp.topMargin = tmpTopMargin;

                    header.setLayoutParams(lp);

                    if(lp.topMargin > HEIGHT_HEADER_FOOTER) {
                        header.setState(RefreshHeader.STATE.ARROW_UP);
                    }else{
                        header.setState(RefreshHeader.STATE.ARROW_DOWN);
                    }

                }else if(STATE.PullUp == state){

                    LayoutParams lp = (LayoutParams) footer.getLayoutParams();
                    lp.topMargin = lp.topMargin + yDiff;
                    footer.setLayoutParams(lp);

                    if(-lp.topMargin > 2*HEIGHT_HEADER_FOOTER){
                        footer.setState(RefreshFooter.STATE.ARROW_DOWN);
                    }else{
                        footer.setState(RefreshFooter.STATE.ARROW_UP);
                    }

                    View view = this.getChildAt(this.getChildCount() - 2);
                    lp = (LayoutParams) view.getLayoutParams();
                    lp.bottomMargin = lp.bottomMargin - yDiff;
                    view.setLayoutParams(lp);
                    view.scrollBy(0, -yDiff);
                }


                yLastRecord = (int) event.getRawY();
                this.invalidate();

                break;
            case MotionEvent.ACTION_UP:

                if(STATE.PullDown == state){

                    LayoutParams lp = (LayoutParams) header.getLayoutParams();
                    if(lp.topMargin > HEIGHT_HEADER_FOOTER){

                        int yScroll = lp.topMargin;
                        mScroller.startScroll(0, yScroll, 0, -yScroll);

                        header.setState(RefreshHeader.STATE.REFRESHING);
                        if(null == refreshListener)
                            throw new NullPointerException(this.getClass().toString() + " - you havent register an OnRefreshListener.");
                        refreshListener.onRefresh();

                    }else{

                        int yScrollDiff = lp.topMargin + HEIGHT_HEADER_FOOTER;
                        mScroller.startScroll(0, lp.topMargin, 0, -yScrollDiff);
                    }

                }else if(STATE.PullUp == state){

                    LayoutParams lp = (LayoutParams) footer.getLayoutParams();

                    if(-lp.topMargin > 2*HEIGHT_HEADER_FOOTER){

                        int yScrollDiff = lp.topMargin;
                        mScroller.startScroll(0, yScrollDiff, 0, -yScrollDiff - HEIGHT_HEADER_FOOTER);

                        footer.setState(RefreshFooter.STATE.LOADING);
                        if(null == loadmoreListener)
                            throw new NullPointerException(this.getClass().toString() + " - you havent register an OnLoadMoreListener.");
                        loadmoreListener.onLoadMore();

                    }else{

                        int yScrollDiff = lp.topMargin;
                        mScroller.startScroll(0, yScrollDiff, 0, -yScrollDiff);
                    }
                }
                this.invalidate();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {

        if(mScroller.computeScrollOffset()){
            int i = mScroller.getCurrY();
            if( STATE.PullDown == state){

                LayoutParams lp = (LayoutParams) header.getLayoutParams();
                lp.topMargin = i;
                header.setLayoutParams(lp);
                if(lp.topMargin == -HEIGHT_HEADER_FOOTER){
                    state = STATE.Normal;
                }

            }else if( STATE.PullUp == state){

                LayoutParams lp = (LayoutParams) footer.getLayoutParams();
                lp.topMargin = i;
                footer.setLayoutParams(lp);
                if(lp.topMargin == 0){
                    state = STATE.Normal;
                }

                View view = this.getChildAt(this.getChildCount() - 2);
                lp = (LayoutParams) view.getLayoutParams();
                lp.bottomMargin = -i;
                view.setLayoutParams(lp);
            }
            this.invalidate();
        }
    }

    private boolean canChildViewPullDown(){
        View viewChild;
        if(getChildCount()>1) {
            viewChild = this.getChildAt(1);
            if(viewChild instanceof RecyclerView) {

                int scrollOffset = ((RecyclerView) viewChild).computeVerticalScrollOffset();
                if(0 == scrollOffset)
                    return false;
            }else if(viewChild instanceof ScrollView){
                if(viewChild.getScrollY() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canChildViewPullUp(){
        View viewChild;
        if(getChildCount()>2) {
            viewChild = this.getChildAt(this.getChildCount() - 2);
            if(viewChild instanceof RecyclerView) {

                RecyclerView recyclerView = (RecyclerView)viewChild;
                int scrollOffset = recyclerView.computeVerticalScrollOffset();
                int scrollRange = recyclerView.computeVerticalScrollRange();
                int scrollExtent = recyclerView.computeVerticalScrollExtent();
                if((scrollOffset+scrollExtent) >= scrollRange)
                    return false;
            }else if(viewChild instanceof ScrollView){

                ScrollView scrollView = (ScrollView)viewChild;
                if((scrollView.getChildAt(0).getMeasuredHeight()) <= (scrollView.getHeight() + scrollView.getScrollY()))
                    return false;
            }
        }
        return true;
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

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    /**
     * @param listener Register an OnLoadMoreListener.
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.loadmoreListener = listener;
    }

    /**
     * @param enable Enable refresh or not.
     */
    public void setEnableRefresh(boolean enable){
        this.enableRefresh = enable;
    }

    public void setEnableLoadmore(boolean enable){
        if(enable){

            LayoutParams lp = new LayoutParams(header.getLayoutParams());
            lp.topMargin = 0;
            footer.setLayoutParams(lp);
            addView(footer, lp);
        }

        this.enableLoadmore = enable;
    }

    private void resetHeader(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                LayoutParams lp = (LayoutParams) header.getLayoutParams();
                int yScroll = lp.topMargin;
                mScroller.startScroll(0, yScroll, 0, -HEIGHT_HEADER_FOOTER);
                header.setState(RefreshHeader.STATE.ARROW_DOWN);
                invalidate();
            }
        },1000);
    }

    private void resetFooter(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                LayoutParams lp = (LayoutParams) footer.getLayoutParams();
                int yScroll = lp.topMargin;
                mScroller.startScroll(0, yScroll, 0, -yScroll);
                footer.setState(RefreshFooter.STATE.ARROW_UP);
                invalidate();
            }
        },1000);
    }

    /**
     * Call this func to stop refreshing when you get data successfully.
     */
    public void stopRefreshSuccess(){
        LayoutParams lp = (LayoutParams) header.getLayoutParams();
        if(0 == lp.topMargin && enableRefresh){

            header.setState(RefreshHeader.STATE.SUCCESS);
            resetHeader();

            View view = this.getChildAt(1);
            if(view instanceof RecyclerView) {
                ((RecyclerView) view).scrollToPosition(0);
            }

        }else {

            MLog.e(this, "Header is not refreshing now. Cant stopRefreshSuccess.");
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
            MLog.e(this, "Header is not refreshing now. Cant stopRefreshFail.");
        }
    }

    /**
     * Call this func to stop loading more when you get data successfully.
     */
    public void stopLoadMoreSuccess(){
        LayoutParams lp = (LayoutParams) footer.getLayoutParams();
        if(-HEIGHT_HEADER_FOOTER == lp.topMargin && enableLoadmore){

            footer.setState(RefreshFooter.STATE.SUCCESS);
            resetFooter();
        }else {

            MLog.e(this, "Footer is not refreshing now. Cant stopLoadMoreSuccess.");
        }
    }

    /**
     * Call this func to stop refreshing when you fail to get data.
     */
    public void stopLoadMoreFail(){
        LayoutParams lp = (LayoutParams) footer.getLayoutParams();
        if(-HEIGHT_HEADER_FOOTER == lp.topMargin && enableLoadmore){

            footer.setState(RefreshFooter.STATE.FAIL);
            resetFooter();
        }else {
            MLog.e(this, "Footer is not refreshing now. Cant stopLoadMoreFail.");
        }
    }
}
