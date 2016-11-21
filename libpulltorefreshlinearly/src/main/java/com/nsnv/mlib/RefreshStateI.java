package com.nsnv.mlib;

/**
 * Created by ccSun on 11/18/16.
 * 下拉刷新header和上拉加载footer的状态
 */

public interface RefreshStateI {

    enum State{
        PullDown,
        PullUp,
        RefreshIng,
        RefreshSuccess,
        RefreshFail;
    }

    void setStatePullDownI();
    void setStatePullUpI();
    void setStateRefreshIngI();
    void setStateRefreshSuccessI();
    void setStateRefreshFailI();
}
