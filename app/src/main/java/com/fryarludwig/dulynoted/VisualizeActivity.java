package com.fryarludwig.dulynoted;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fryar on 12/18/2017.
 */

public class VisualizeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualize_data_layout);

//        LinearLayoutManager llm = new LinearLayoutManager(this);

//        RecyclerView rv = this.findViewById(R.id.card_recycle_view);
//        rv.setHasFixedSize(true);
//        rv.setLayoutManager(llm);


    }

    public void onTabPress(View view)
    {
        int i = 0;
    }
}
