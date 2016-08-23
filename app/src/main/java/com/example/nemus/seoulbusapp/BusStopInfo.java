package com.example.nemus.seoulbusapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BusStopInfo extends AppCompatActivity {
    ListView mBusInfo;
    TextView BusStopName;
    TextView BusStopInfo;

    JSONArray busStopData;

    Intent firstInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_info);

        firstInfo = getIntent();

        BusStopName = (TextView)findViewById(R.id.busstopname);
        BusStopInfo = (TextView)findViewById(R.id.busstopinfo);
        mBusInfo = (ListView)findViewById(R.id.buslist);

        final ArrayList<JSONObject> businfo = new ArrayList<JSONObject>();

        GetBusInfo gi = new GetBusInfo();

        Log.d("bus info",firstInfo.getStringExtra("arsId"));

        gi.setData(GetBusInfo.DATA_BUSSTOPINFO,0,0,0,firstInfo.getStringExtra("arsId"));
        gi.execute();

        try {
            busStopData = gi.get();
            String title = busStopData.getJSONObject(0).getString("stNm");
            BusStopName.setText(title);
            setTitle(title);
            for(int i=0;i<busStopData.length();i++) {
                businfo.add(busStopData.getJSONObject(i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),android.R.layout.simple_expandable_list_item_1,businfo);
        mBusInfo.setAdapter(adapter);
        mBusInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent callPop = new Intent(BusStopInfo.this,BusLinePop.class);
                try {
                    callPop.putExtra("busId",businfo.get(i).getString("busRouteId"));
                    callPop.putExtra("busnum",businfo.get(i).getString("rtNm"));
                    callPop.putExtra("businfo",businfo.get(i).getString("sectNm"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivityForResult(callPop,mainActivity.BUSLINEPOP);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("data","catch");
        //Log.d("data",data.getStringExtra("lineData"));
        Log.d("code",resultCode+"");
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case mainActivity.BUSLINEPOP:
                    Log.d("data",data.getStringExtra("lineData"));
                    firstInfo.putExtras(data);
                    setResult(RESULT_OK,firstInfo);
                    this.finish();
            }
        }
    }
}
