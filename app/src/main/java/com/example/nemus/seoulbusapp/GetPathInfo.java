package com.example.nemus.seoulbusapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nemus on 2016-08-22.
 */
public class GetPathInfo extends AsyncTask<Void,Void,ArrayList<PathInfoData>> {

    private final String INFO_URL = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoByBus";
    private final String SERVICEKEY = "JVRMM1Qh0dzIbgQXxSABGHaAJH9jIF0uJZxBYnL/QtzRpM0WNWKpS5+5mDiE6V6vXVhxY15X42Iq3MQpSQ7c+A==";
    private final String STARTX = "?startX=";
    private final String STARTY = "&startY=";
    private final String ENDX = "&endX=";
    private final String ENDY = "&endY=";
    private final String OTHER_OPTION = "&numOfRows=99&pageSize=99&pageNo=1&startPage=1";

    private URL sendUrl;


    public void setData(double inStartY,double inStartX,double inEndY,double inEndX){
        try {
            sendUrl = new URL(INFO_URL+STARTX+inStartX+STARTY+inStartY+ENDX+inEndX+ENDY+inEndY+OTHER_OPTION);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("URL",sendUrl.toString());
    }

    @Override
    protected ArrayList<PathInfoData> doInBackground(Void... voids) {
        XmlPullParser xpp = null;
        ArrayList<PathInfoData> out = new ArrayList<PathInfoData>();
        PathInfoData temp = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) sendUrl.openConnection();
            conn.setRequestProperty("serviceKey",SERVICEKEY);
            conn.setRequestMethod("GET");

            conn.setConnectTimeout(10000);

            int rescode = conn.getResponseCode();
            Log.d("rescode",rescode+"");

            if (rescode == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                XmlPullParserFactory factory;
                try {
                    factory= XmlPullParserFactory.newInstance();
                    xpp= factory.newPullParser();
                    xpp.setInput(new InputStreamReader(in));
                }catch (XmlPullParserException e){
                    e.printStackTrace();
                }
            } else {
                return null;
            }
            try {
                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch(xpp.getEventType()) {
                        case XmlPullParser.START_TAG:{
                            String catName = xpp.getName();
                            if (catName.equals("itemList")) {
                                temp = new PathInfoData();
                                xpp.next();
                                xpp.next();
                                temp.distance = Integer.getInteger(xpp.getText());
                            }
                            if (catName.equals("pathList")) {
                                EachPath tempPath = new EachPath();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                tempPath.startBusStop = xpp.getText();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                tempPath.startBusStopCoorX = Double.parseDouble(xpp.getText());
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                tempPath.startBusStopCoorY = Double.parseDouble(xpp.getText());
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                tempPath.busNum = xpp.getText();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                tempPath.endBusStop = xpp.getText();
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                tempPath.endBusStopCoorX = Double.parseDouble(xpp.getText());
                                xpp.next();
                                xpp.next();
                                xpp.next();
                                tempPath.endBusStopCoorY = Double.parseDouble(xpp.getText());
                                temp.paths.add(tempPath);
                            }
                            break;
                        }
                        case XmlPullParser.END_TAG: {
                            String catName = xpp.getName();
                            if (catName.equals("itemList")){
                                out.add(temp);
                                temp = null;
                            }
                        }
                    }
                    xpp.next();
                }
                return out;
            }catch(XmlPullParserException e){
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
