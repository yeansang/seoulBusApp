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

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        select.add(getString(R.string.data_delete));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,select);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent.putExtra("ClickItem",i);
                switch (i){
                    case 0:
                        Log.d("mappopup","popup 1");
                        setResult(RESULT_OK,intent);
                        finish();
                        break;
                    case 1:
                        Log.d("mappopup","popup 2");
                        setResult(RESULT_OK,intent);
                        MapPopup.this.finish();
                        break;
                    case 2:
                        Log.d("mappopup","popup 3");
                        setResult(RESULT_OK,intent);
                        GetBusInfo gi = new GetBusInfo();
                        gi.setData(GetBusInfo.DATA_NEARBUSSTOP,intent.getDoubleExtra("y",0),intent.getDoubleExtra("x",0),300,null);
                        gi.execute();
                        JSONArray temp = null;
                        try {
                            temp = gi.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra("busStopData",temp.toString());
                        setResult(RESULT_OK,intent);
                        MapPopup.this.finish();
                        break;
                    case 3:
                        intent.putExtra("removeBusStop",true);
                        setResult(RESULT_OK,intent);
                        MapPopup.this.finish();
                    default:
                        Log.d("err","in MenuPopup");
                        MapPopup.this.finish();
                        break;
                }
            }
        });
    }
}
