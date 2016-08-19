package com.example.nemus.seoulbusapp;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nemus on 2016-08-18.
 */
public class GetBusInfo extends AsyncTask<Void,Void,JSONArray>{

    public static final int DATA_BUSLINE = 0;
    public static final int DATA_NEARBUSSTOP = 1;
    public static final int DATA_DESTINATION = 2;
    public static final int DATA_BUSSTOPINFO = 3;

    private int dataType;

    private final String GETBUSLINE = "http://topis.seoul.go.kr/renewal/ajaxData/getBusData.jsp?mode=routLine&rout_id=";
    private final String GETBUSSTOP = "http://m.bus.go.kr/mBus/bus/getStationByPos.bms";
    private final String GETBUSSTOPINFO = "http://m.bus.go.kr/mBus/bus/getStationByUid.bms?arsId=";
    private final String SETLONGITUDE = "?tmX=";
    private final String SETLATITUDE = "&tmY=";
    private final String SETRADIUS = "&radius=";
    private URL address;

    public void setData(int dataType, double latitude, double longitude, int radius, String id){

        this.dataType = dataType;
        latitude = cutDouble(latitude);
        longitude = cutDouble(longitude);
        try {
            switch (dataType){
                case DATA_BUSLINE:
                    address = new URL(GETBUSLINE+id);
                    break;
                case DATA_NEARBUSSTOP:
                    address = new URL(GETBUSSTOP+SETLONGITUDE+longitude+SETLATITUDE+latitude+SETRADIUS+radius);
                    break;
                case DATA_BUSSTOPINFO:
                    address = new URL(GETBUSSTOPINFO+id);
                    break;
                default:
                    break;
            }
            Log.d("URL",address.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        try {
            HttpURLConnection conn = (HttpURLConnection) address.openConnection();
            conn.setConnectTimeout(10000);

            int rescode = conn.getResponseCode();

            if (rescode == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                BufferedReader streamReader;
                if((dataType == DATA_NEARBUSSTOP)||(dataType == DATA_BUSSTOPINFO)) streamReader = new BufferedReader(new InputStreamReader(in, "EUC-KR"));
                else streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder responseStrBuilder = new StringBuilder();

                String inputStr;
                while ((inputStr = streamReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }
                JSONArray ja = null;
                Log.d("webtest",responseStrBuilder.toString());
                try {
                    switch (dataType) {
                        case DATA_BUSLINE:
                            ja = new JSONArray(responseStrBuilder.toString());
                            break;
                        case DATA_BUSSTOPINFO:
                        case DATA_NEARBUSSTOP:
                            JSONObject out = new JSONObject(responseStrBuilder.toString());
                            ja = out.getJSONArray("resultList");
                            break;
                    }
                }catch (JSONException e){
                    ja = new JSONArray();
                    e.printStackTrace();
                }

                return ja;
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    double cutDouble(double a){
        BigDecimal temp = new BigDecimal(a);
        temp.setScale(8,BigDecimal.ROUND_DOWN);
        return temp.doubleValue();
    }
}
