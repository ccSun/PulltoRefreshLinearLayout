package com.nsnv.mlib;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

/**
 *
 */
public abstract class BaseRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter{

    protected List listData;

    public BaseRecyclerAdapter(List listData) {
        this.listData = listData;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setItemAnimationSet(holder);
        onBindViewHolderChild((VH)holder, position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    /**
     * Bind data to view from list.
     * @param holder
     * @param position
     */
    public abstract void onBindViewHolderChild(VH holder, int position);

    /**
     * Override this to realize your animation.
     * @param holder
     */
    public void setItemAnimationSet(RecyclerView.ViewHolder holder){
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(holder.itemView, "rotationY", 30, -20, 15, -10, 5, -3, 0),
                ObjectAnimator.ofFloat(holder.itemView, "alpha", 0.5f, 1f)
        );
        animatorSet.setDuration(1500);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
    }
}
