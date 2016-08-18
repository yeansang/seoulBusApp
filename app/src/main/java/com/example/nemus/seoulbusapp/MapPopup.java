package com.example.nemus.seoulbusapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MapPopup extends Activity {
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_popup);
        mListView = (ListView) findViewById(R.id.listView);

        final Intent intent = getIntent();

        ArrayList<String> select = new ArrayList<String>();
        select.add(getString(R.string.start_point));
        select.add(getString(R.string.end_point));
        select.add(getString(R.string.near_busstop));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,select);
        mListView.setAdapter(adapter);
        final MapPopup main = this;

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Log.d("mappopup","popup 1");
                        intent.putExtra("ClickItem",i);
                        setResult(RESULT_OK,intent);
                        main.finish();
                        break;
                    case 1:
                        Log.d("mappopup","popup 2");
                        intent.putExtra("ClickItem",i);
                        setResult(RESULT_OK,intent);
                        main.finish();
                        break;
                    case 2:
                        Log.d("mappopup","popup 3");
                        intent.putExtra("ClickItem",i);
                        setResult(RESULT_OK,intent);
                        main.finish();
                        break;
                    default:
                        Log.d("err","in MenuPopup");
                        main.finish();
                        break;
                }
            }
        });
    }
}
