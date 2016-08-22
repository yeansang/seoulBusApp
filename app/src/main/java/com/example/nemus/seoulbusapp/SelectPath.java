package com.example.nemus.seoulbusapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SelectPath extends AppCompatActivity {

    Intent input;
    ListView mListView;
    ArrayList<PathInfoData> pathData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_path);

        mListView = (ListView)findViewById(R.id.path_list);

        input = getIntent();
        double sx = input.getDoubleExtra("startx",0);
        double sy = input.getDoubleExtra("starty",0);
        double ex = input.getDoubleExtra("endx",0);
        double ey = input.getDoubleExtra("endy",0);

        GetPathInfo gpi = new GetPathInfo();
        gpi.setData(sy,sx,ey,ex);
        gpi.execute();
        try {
            pathData = gpi.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
