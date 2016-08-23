package com.example.nemus.seoulbusapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SelectPath extends AppCompatActivity {

    Intent input;
    ListView mListView;
    TextView nullPage;
    ArrayList<PathInfoData> pathData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.search_result));
        setContentView(R.layout.activity_select_path);

        input = getIntent();
        final SelectPath main = this;

        mListView = (ListView)findViewById(R.id.path_list);
        nullPage = (TextView)findViewById(R.id.search_null);

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
        PathListAdapter adapter = new PathListAdapter(this,android.R.layout.simple_expandable_list_item_1,pathData);

        mListView.setAdapter(adapter);
        mListView.setEmptyView(nullPage);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int item, long l) {
                PathInfoData temp = pathData.get(item);
                Intent output = new Intent();
                output.putExtra("distance",temp.distance);
                for(int i=0;i<temp.paths.size();i++) {
                    output.putExtra("pathStartNm" + i, temp.paths.get(i).startBusStop);
                    output.putExtra("pathStartX" + i, temp.paths.get(i).startBusStopCoorX);
                    output.putExtra("pathStartY" + i, temp.paths.get(i).startBusStopCoorY);
                    output.putExtra("pathEndNm" + i, temp.paths.get(i).endBusStop);
                    output.putExtra("pathEndX" + i, temp.paths.get(i).endBusStopCoorX);
                    output.putExtra("pathEndY" + i, temp.paths.get(i).endBusStopCoorY);
                    output.putExtra("pathBusNm" + i, temp.paths.get(i).busNum);
                }
                output.putExtra("pathsize",temp.paths.size());
                setResult(RESULT_OK,output);
                SelectPath.this.finish();
            }
        });
    }
}
