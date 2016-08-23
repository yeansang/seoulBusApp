package com.example.nemus.seoulbusapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class BusLinePop extends Activity {
    TextView busNum;
    TextView busInfo;
    Button onMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Intent recived = getIntent();
        setContentView(R.layout.activity_map_popup);
        setContentView(R.layout.activity_bus_line_pop);
        busNum = (TextView)findViewById(R.id.busnum);
        busInfo = (TextView)findViewById(R.id.businfo);
        onMap = (Button)findViewById(R.id.onmap);

        String busid = recived.getStringExtra("busId");

        busInfo.setText(recived.getStringExtra("businfo"));
        busNum.setText(recived.getStringExtra("busnum"));

        final GetBusInfo gi = new GetBusInfo();
        gi.setData(GetBusInfo.DATA_BUSLINE,0,0,0,busid);

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
                BusLinePop.this.finish();
            }
        });

    }
}
