package com.example.nemus.seoulbusapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by nemus on 2016-08-22.
 */
public class PathInfoData {
    int distance;
    ArrayList<EachPath> paths = new ArrayList<EachPath>();
    public String toString(){
        String out = "";
        out+=distance;
        for(int i=0;i<paths.size();i++){
            out+=", "+paths.get(i).busNum;
        }
        return out;
    }
}
class EachPath{
    String busNum;
    String startBusStop;
    String endBusStop;
    double startBusStopCoorX;
    double startBusStopCoorY;
    double endBusStopCoorX;
    double endBusStopCoorY;
}
