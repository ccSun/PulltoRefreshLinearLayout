package com.nsnv.pulltorefreshlinearlayout;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nsnv.refreshlinearly.RefreshLinearly;

import java.util.ArrayList;

public class ActMain extends AppCompatActivity {

    private RecyclerView recycler_test;
    private RefreshLinearly refresh_linely;
    private MRecyclerAdapter adapter;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        recycler_test = (RecyclerView) findViewById(R.id.recycler_test);

        list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("Item_" + i);
        }

        adapter = new MRecyclerAdapter(list);

        recycler_test.setAdapter(adapter);
        recycler_test.setLayoutManager(new LinearLayoutManager(this));

        refresh_linely = (RefreshLinearly) findViewById(R.id.refresh_linely);
        refresh_linely.setOnRefreshListener(new RefreshLinearly.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        list.add(0, "ADD_" + list.size());
                        adapter.notifyItemInserted(0);
                        refresh_linely.stopRefreshSuccess();
                    }
                }, 1700);
            }
        });

        refresh_linely.setEnableLoadmore(true);
        refresh_linely.setOnLoadMoreListener(new RefreshLinearly.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.add(list.size(), "LOADMORE" + list.size());
                        adapter.notifyItemInserted(list.size());
                        refresh_linely.stopLoadMoreSuccess();
//                        refresh_linely.stopLoadMoreFail();
                    }
                }, 2000);
            }
        });
    }
}
