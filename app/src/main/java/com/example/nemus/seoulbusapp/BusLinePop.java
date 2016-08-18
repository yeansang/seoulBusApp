package com.example.nemus.seoulbusapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class BusLinePop extends Activity {
    TextView busNum;
    Button onMap;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_popup);
        setContentView(R.layout.activity_bus_line_pop);
        busNum = (TextView)findViewById(R.id.busnum);
        onMap = (Button)findViewById(R.id.onmap);
        cancel = (Button)findViewById(R.id.cancel);
    }
}
