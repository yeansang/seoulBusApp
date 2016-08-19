package com.example.nemus.seoulbusapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class BusLinePop extends Activity {
    TextView busNum;
    Button onMap;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent recived = getIntent();
        setContentView(R.layout.activity_map_popup);
        setContentView(R.layout.activity_bus_line_pop);
        busNum = (TextView)findViewById(R.id.busnum);
        onMap = (Button)findViewById(R.id.onmap);
        cancel = (Button)findViewById(R.id.cancel);

        String busid = recived.getStringExtra("busId");

        busNum.setText(busid);

        final GetBusInfo gi = new GetBusInfo();
        gi.setData(GetBusInfo.DATA_BUSLINE,0,0,0,busid);

        final BusLinePop main = this;

        onMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gi.execute();
                try {
                    recived.putExtra("lineData",gi.get().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK,recived);
                main.finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.finish();
            }
        });
    }
}
