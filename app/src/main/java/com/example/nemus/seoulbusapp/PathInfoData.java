package com.example.nemus.seoulbusapp;

import java.util.ArrayList;

/**
 * Created by nemus on 2016-08-22.
 */
public class PathInfoData {
    int distance;
    ArrayList<EachPath> paths = new ArrayList<EachPath>();
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
