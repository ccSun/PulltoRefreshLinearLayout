package com.nsnv.mlib;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * 下拉刷新布局，可以内嵌RecyclerView和ScrollView
 */
public class RefreshLinearly extends LinearLayout {

    public interface OnRefreshListener{
        void onRefresh();
    }
    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    private int HEIGHT_HEADER_FOOTER;
    private RefreshBase header;
    private RefreshBase footer;

    private int yLastRecord = -1;
    private Scroller mScroller;

    private final float OFFSET_RATIO = 1.4f;

    private final int DURATION_SCROLL = 300;

    private final int MOVE_SHAKE = 20;
    private boolean enableRefresh = false;
    private boolean enableLoadmore = false;
    private OnRefreshListener refreshListener;
    private OnLoadMoreListener loadmoreListener;

    private enum STATE{
        Normal,
        PullDown,
        PullUp
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

        mScroller = new Scroller(context);

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if(!enableRefresh && !enableLoadmore)
            return false;

        if(null == header && null == footer)
            return false;

        if(enableRefresh && null != header &&
                ((header.getState() == RefreshStateI.State.RefreshIng
                        || header.getState() == RefreshStateI.State.RefreshSuccess
                        || header.getState() == RefreshStateI.State.RefreshFail))
                ){
            return false;
        }

        if(enableLoadmore && null != footer &&
                ((footer.getState() == RefreshStateI.State.RefreshIng
                        || footer.getState() == RefreshStateI.State.RefreshSuccess
                        || footer.getState() == RefreshStateI.State.RefreshFail))
                ){
            return false;
        }

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                yLastRecord = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                int yDiff = (int) ((event.getRawY() - yLastRecord)/OFFSET_RATIO);
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

    private boolean headerAlreadyTurnUp = false;

    private boolean footerAlreadyTrunDown = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!enableRefresh && !enableLoadmore)
            return false;

        if(null == header && null == footer)
            return false;

        if(enableRefresh && null != header &&
                ((header.getState() == RefreshStateI.State.RefreshIng
                        || header.getState() == RefreshStateI.State.RefreshSuccess
                        || header.getState() == RefreshStateI.State.RefreshFail))
                ){
            return false;
        }

        if(enableLoadmore && null != footer &&
                ((footer.getState() == RefreshStateI.State.RefreshIng
                        || footer.getState() == RefreshStateI.State.RefreshSuccess
                        || footer.getState() == RefreshStateI.State.RefreshFail))
                ){
            return false;
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                yLastRecord = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                int yDiff = (int) ((event.getRawY() - yLastRecord)/OFFSET_RATIO);

                if(STATE.PullDown == state && enableRefresh && null != header){

                    LayoutParams lp = (LayoutParams) header.getLayoutParams();
                    int tmpTopMargin = lp.topMargin + yDiff;

                    lp.topMargin = tmpTopMargin;

                    header.setLayoutParams(lp);

                    if(lp.topMargin > HEIGHT_HEADER_FOOTER && !headerAlreadyTurnUp) {
                        header.setStatePullUpI();
                        headerAlreadyTurnUp = true;
                    }else if(lp.topMargin < HEIGHT_HEADER_FOOTER && headerAlreadyTurnUp){
                        header.setStatePullDownI();
                        headerAlreadyTurnUp = false;
                    }

                }else if(STATE.PullUp == state && enableLoadmore && null != footer){

                    LayoutParams lp = (LayoutParams) footer.getLayoutParams();
                    lp.topMargin = lp.topMargin + yDiff;
                    footer.setLayoutParams(lp);

                    if(-lp.topMargin > 2*HEIGHT_HEADER_FOOTER && !footerAlreadyTrunDown){
                        footer.setStatePullDownI();
                        footerAlreadyTrunDown = true;
                    }else if(-lp.topMargin < 2*HEIGHT_HEADER_FOOTER && footerAlreadyTrunDown){
                        footer.setStatePullUpI();
                        footerAlreadyTrunDown = false;
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

                if(STATE.PullDown == state && enableRefresh && null != header){

                    LayoutParams lp = (LayoutParams) header.getLayoutParams();
                    if(lp.topMargin > HEIGHT_HEADER_FOOTER){

                        int yScroll = lp.topMargin;
                        mScroller.startScroll(0, yScroll, 0, -yScroll, DURATION_SCROLL);
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                header.setStateRefreshIngI();
                                if(null == refreshListener)
                                    throw new NullPointerException(this.getClass().toString() + " - you havent register an OnRefreshListener.");
                                else
                                    refreshListener.onRefresh();
                            }
                        }, DURATION_SCROLL);

                    }else{

                        int yScrollDiff = lp.topMargin + HEIGHT_HEADER_FOOTER;
                        mScroller.startScroll(0, lp.topMargin, 0, -yScrollDiff);
                    }

                }else if(STATE.PullUp == state && enableRefresh && null != footer){

                    LayoutParams lp = (LayoutParams) footer.getLayoutParams();

                    if(-lp.topMargin > 2*HEIGHT_HEADER_FOOTER){

                        int yScrollDiff = lp.topMargin;
                        mScroller.startScroll(0, yScrollDiff, 0, -yScrollDiff - HEIGHT_HEADER_FOOTER, DURATION_SCROLL);

                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                footer.setStateRefreshIngI();
                                if(null == loadmoreListener)
                                    throw new NullPointerException(this.getClass().toString() + " - you havent register an OnLoadMoreListener.");
                                else
                                    loadmoreListener.onLoadMore();
                            }
                        }, DURATION_SCROLL);

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
            if( STATE.PullDown == state && enableRefresh && null != header){

                LayoutParams lp = (LayoutParams) header.getLayoutParams();
                lp.topMargin = i;
                header.setLayoutParams(lp);
                if(lp.topMargin == -HEIGHT_HEADER_FOOTER){
                    state = STATE.Normal;
                }

            }else if( STATE.PullUp == state && enableLoadmore && null != footer){

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




    private void resetHeader(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                LayoutParams lp = (LayoutParams) header.getLayoutParams();
                int yScroll = lp.topMargin;
                mScroller.startScroll(0, yScroll, 0, -HEIGHT_HEADER_FOOTER);
                header.setStatePullDownI();
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
                footer.setStatePullUpI();
                invalidate();
            }
        },1000);
    }



    /**
     * @param enable Enable refresh or not.
     */
    public void setEnableRefresh(boolean enable){
        this.enableRefresh = enable;
    }

    /**
     * @param listener Register an OnRefreshListener.
     */
    public void setOnRefreshListener(OnRefreshListener listener){
        this.refreshListener = listener;
    }

    public void setHeader(Context context, RefreshBase header){
        if(null != header)
            removeView(header);

        LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        HEIGHT_HEADER_FOOTER = context.getResources().getDimensionPixelSize(R.dimen.refresh_header_footer_size);
        lp.topMargin = -HEIGHT_HEADER_FOOTER;
        addView(header, 0, lp);

        this.header = header;
    }

    /**
     * Call this func to stop refreshing when you get data successfully.
     */
    public void stopRefreshSuccess(){
        if(null == header)
            return;
        LayoutParams lp = (LayoutParams) header.getLayoutParams();
        if(0 == lp.topMargin && enableRefresh){

            header.setStateRefreshSuccessI();
            resetHeader();

            View view = this.getChildAt(1);
            if(view instanceof RecyclerView) {
                ((RecyclerView) view).scrollToPosition(0);
            }

        }else {

            android.util.Log.i(this.toString(), "Header is not refreshing now. Cant stopRefreshSuccess. :s" + lp.topMargin);
        }
    }

    /**
     * Call this func to stop refreshing when you fail to get data.
     */
    public void stopRefreshFail(){
        LayoutParams lp = (LayoutParams) header.getLayoutParams();
        if(0 == lp.topMargin && enableRefresh){

            header.setStateRefreshFailI();
            resetHeader();
        }else {
            android.util.Log.i(this.toString(), "Header is not refreshing now. Cant stopRefreshFail.");
        }
    }





    public void setEnableLoadmore(boolean enable){
        this.enableLoadmore = enable;
    }
    /**
     * @param listener Register an OnLoadMoreListener.
     */
    public void setOnLoadMoreListener(OnLoadMoreListener listener){

        this.loadmoreListener = listener;
    }

    public void setFooter(Context context, RefreshBase footer){

        if(null != footer)
            removeView(footer);

        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        lp.topMargin = 0;
        addView(footer, this.getChildCount(), lp);

        this.footer = footer;
    }

    /**
     * Call this func to stop loading more when you get data successfully.
     */
    public void stopLoadMoreSuccess(){
        LayoutParams lp = (LayoutParams) footer.getLayoutParams();
        if(-HEIGHT_HEADER_FOOTER == lp.topMargin && enableLoadmore){

            footer.setStateRefreshSuccessI();
            resetFooter();
        }else {

            android.util.Log.i(this.toString(), "Footer is not refreshing now. Cant stopLoadMoreSuccess.");
        }
    }

    /**
     * Call this func to stop refreshing when you fail to get data.
     */
    public void stopLoadMoreFail(){
        LayoutParams lp = (LayoutParams) footer.getLayoutParams();
        if(-HEIGHT_HEADER_FOOTER == lp.topMargin && enableLoadmore){

            footer.setStateRefreshFailI();
            resetFooter();
        }else {
            android.util.Log.e(this.toString(), "Footer is not refreshing now. Cant stopLoadMoreFail.");
        }
    }
}
