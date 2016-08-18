package com.example.nemus.seoulbusapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class BusStopInfo extends AppCompatActivity {
    ListView mBusInfo;
    TextView BusStopName;
    TextView BusStopInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_info);

        Intent firstInfo = getIntent();

        BusStopName = (TextView)findViewById(R.id.busstopname);
        BusStopInfo = (TextView)findViewById(R.id.busstopinfo);
        mBusInfo = (ListView)findViewById(R.id.buslist);

        ArrayList<String> businfo = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,businfo);
        mBusInfo.setAdapter(adapter);
    }
}
