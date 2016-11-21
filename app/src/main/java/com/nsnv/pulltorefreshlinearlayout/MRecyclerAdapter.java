package com.nsnv.pulltorefreshlinearlayout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 */
public class MRecyclerAdapter extends BaseRecyclerAdapter<MRecyclerAdapter.MyViewHolder>{

    public MRecyclerAdapter(List listData) {
        super(listData);
    }

    @Override
    public void onBindViewHolderChild(MyViewHolder holder, int position) {
        holder.txt_1.setText(listData.get(position).toString());
        holder.txt_2.setText(listData.get(position).toString());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView txt_1;
        public TextView txt_2;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_1 = (TextView) itemView.findViewById(R.id.txt_1);
            txt_2 = (TextView) itemView.findViewById(R.id.txt_2);
        }
    }
}
