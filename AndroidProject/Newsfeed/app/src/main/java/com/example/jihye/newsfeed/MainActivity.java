package com.example.jihye.newsfeed;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ListView feeder;
        ListViewAdapter feederAdapter;

        feederAdapter = new ListViewAdapter();

        feeder = (ListView) findViewById(R.id.listview_newsfeed);
        feeder.setAdapter(feederAdapter);

        feederAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.ic_launcher_background),"Phantom","2018/12/01~2018/12/31");

    }

}
