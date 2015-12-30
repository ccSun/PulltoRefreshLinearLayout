package com.nsnv.pulltorefreshlinearlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ActMain extends AppCompatActivity {

    private RecyclerView recycler_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        recycler_test = (RecyclerView) findViewById(R.id.recycler_test);

        List list = new ArrayList<String>();
        for(int i = 0; i < 10; i++) {
            list.add("Item_" + i);
        }


        recycler_test.setAdapter(new MRecyclerAdapter(list));
        recycler_test.setLayoutManager(new LinearLayoutManager(this));

    }
}
