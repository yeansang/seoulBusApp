package com.example.nemus.seoulbusapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mainActivity extends FragmentActivity implements OnMapReadyCallback {

    public final static int MAPPOPUP = 0;
    public final static int BUSSTOPINFO = 1;
    public final static int BUSLINEPOP = 2;
    public final static int PATHSEARCH = 3;

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private PathInfoData path = new PathInfoData();

    private Marker startMarker = null;
    private Marker endMarker= null;

    double maxX=0;
    double minX=1000;
    double maxY=0;
    double minY=1000;

    JSONArray drawLine=null;
    Polyline polyline=null;
    Polyline pathLine = null;

    BitmapDescriptor busStopIcon=null;
    BitmapDescriptor startPointIcon=null;
    BitmapDescriptor endPointIcon=null;
    BitmapDescriptor transportIcon=null;

    int markerToggle = 0;
    Marker[] markersA = new Marker[30];
    Marker[] markersB = new Marker[30];
    Marker[] transport = new Marker[10];
    int rcount=0;

    private double startPoint[] = new double[]{37.558345, 126.994583};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("main", "Start");
        super.onCreate(savedInstanceState);

        MapsInitializer.initialize(getApplicationContext());

        busStopIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_bus_black_48dp);
        startPointIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_walk_black_48dp);
        endPointIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_add_location_black_48dp);
        transportIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_transfer_within_a_station_black_48dp);

        int permissionCheck = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] requstPermission = new String[]{"",""};
            permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                requstPermission[0] = android.Manifest.permission.ACCESS_FINE_LOCATION;
            }
            permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                requstPermission[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            }
            requestPermissions(requstPermission, 0);
        }
/*        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100000,0,new MyLocationListener());*/
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng start = new LatLng(startPoint[0], startPoint[1]);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,15.5f));
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent mapData = new Intent(mainActivity.this,MapPopup.class);
                mapData.putExtra("y",latLng.latitude);
                mapData.putExtra("x",latLng.longitude);
                startActivityForResult(mapData,MAPPOPUP);
                float zoom = mMap.getCameraPosition().zoom;
                Log.d("zoom", zoom+"");
                zoomLevelCal();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                int mark = 0;
                if(m.getTitle().equals("Start")) mark=1;
                if(m.getTitle().equals("End")) mark=2;
                try {
                    if (Integer.parseInt(m.getTitle()) != 0) mark = 3;
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    mark=4;
                }

                Log.d("marker", mark+"");
                switch (mark){
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                            Intent busStopData = new Intent(mainActivity.this, BusStopInfo.class);
                            LatLng loc = m.getPosition();
                            busStopData.putExtra("x", loc.longitude);
                            busStopData.putExtra("y", loc.latitude);
                            busStopData.putExtra("arsId", m.getTitle());
                            Log.d("markerclick", m.getTitle());
                            startActivityForResult(busStopData, BUSSTOPINFO);
                            return true;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case MAPPOPUP:
                    int item = data.getIntExtra("ClickItem",2);
                    if(item == 0){
                        if(startMarker!=null){
                            startMarker.remove();
                        }
                        startMarker = mMap.addMarker(new MarkerOptions().title("Start").position(new LatLng(data.getDoubleExtra("y",0),data.getDoubleExtra("x",0))).icon(startPointIcon));
                        if((startMarker!=null)&&(endMarker!=null)){
                            //경로 불러오기& 경로 그리기
                            Intent busPath = new Intent(this,SelectPath.class);
                            LatLng s = startMarker.getPosition();
                            LatLng e = endMarker.getPosition();

                            busPath.putExtra("startx",s.longitude);
                            busPath.putExtra("starty",s.latitude);
                            busPath.putExtra("endx",e.longitude);
                            busPath.putExtra("endy",e.latitude);

                            startActivityForResult(busPath,PATHSEARCH);
                        }
                    }else if(item == 1) {
                        if(endMarker!=null){
                            endMarker.remove();
                        }
                        endMarker = mMap.addMarker(new MarkerOptions().title("End").position(new LatLng(data.getDoubleExtra("y",0),data.getDoubleExtra("x",0))).icon(endPointIcon));
                        if((startMarker!=null)&&(endMarker!=null)){
                            //경로 불러오기& 경로 그리기
                            if(pathLine!=null) pathLine.remove();
                            for(int i=0;i<10;i++){
                                if(transport[i]!=null) transport[i].remove();
                            }
                            Intent busPath = new Intent(this,SelectPath.class);
                            LatLng s = startMarker.getPosition();
                            LatLng e = endMarker.getPosition();

                            busPath.putExtra("startx",s.longitude);
                            busPath.putExtra("starty",s.latitude);
                            busPath.putExtra("endx",e.longitude);
                            busPath.putExtra("endy",e.latitude);

                            startActivityForResult(busPath,PATHSEARCH);
                        }
                    }else if(item == 2){
                        try {
                            JSONArray busStopData = new JSONArray(data.getStringExtra("busStopData"));
                            Marker[] markers;
                            if((markerToggle%2)==0) markers=markersA;
                            else markers=markersB;
                            markerToggle++;
                            int i=0;
                            for(;i<busStopData.length();i++){
                                JSONObject jo = busStopData.getJSONObject(i);
                                if(markers[i]!=null) markers[i].remove();
                                markers[i] = mMap.addMarker(new MarkerOptions().title(jo.getString("arsId")).position(new LatLng(jo.getDouble("gpsY"),jo.getDouble("gpsX"))).snippet(jo.getString("stationNm")).icon(busStopIcon));
                            }
                            for(;i<30;i++){
                                if(markers[i]!=null) markers[i].remove();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else if(item == 3){
                        if(data.getBooleanExtra("removeBusStop",false)){
                            if(polyline!=null)polyline.remove();
                            for(int i=0;i<30;i++){
                                if(markersA[i]!=null) markersA[i].remove();
                            }
                            for(int i=0;i<30;i++){
                                if(markersB[i]!=null) markersB[i].remove();
                            }
                        }
                    }

                    break;
                case BUSSTOPINFO:
                case BUSLINEPOP:
                    try {
                        drawLine = new JSONArray(data.getStringExtra("lineData"));
                        drawLine(drawLine, data.getStringExtra("busnum"));
                        centerOrder();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case PATHSEARCH:
                {
                    if(pathLine!=null) pathLine.remove();
                    for(int i=0;i<10;i++){if(transport[i]!=null) transport[i].remove();}

                    maxX=0;
                    minX=1000;
                    maxY=0;
                    minY=1000;

                    path.distance = data.getIntExtra("distance",0);
                    int pathSize = data.getIntExtra("pathsize",0);

                    PolylineOptions options = new PolylineOptions().color(Color.MAGENTA);
                    options.add(startMarker.getPosition());

                    for(int i=0;i<pathSize;i++) {
                        EachPath temp = new EachPath();
                        double x;
                        double y;

                        temp.startBusStop = data.getStringExtra("pathStartNm" + i);
                        temp.startBusStopCoorX = x = data.getDoubleExtra("pathStartX" + i, 0);
                        temp.startBusStopCoorY = y = data.getDoubleExtra("pathStartY" + i, 0);
                        options.add(new LatLng(temp.startBusStopCoorY,temp.startBusStopCoorX));
                        if(x>maxX) maxX = x;
                        if(x<minX) minX = x;
                        if(y>maxY) maxY = y;
                        if(y<minY) minY = y;

                        temp.endBusStop = data.getStringExtra("pathEndNm" + i);
                        temp.endBusStopCoorX = x = data.getDoubleExtra("pathEndX" + i, 0);
                        temp.endBusStopCoorY = y = data.getDoubleExtra("pathEndY" + i, 0);
                        options.add(new LatLng(temp.endBusStopCoorY,temp.endBusStopCoorX));
                        if(x>maxX) maxX = x;
                        if(x<minX) minX = x;
                        if(y>maxY) maxY = y;
                        if(y<minY) minY = y;

                        temp.busNum = data.getStringExtra("pathBusNm" + i);
                        transport[i+1]=mMap.addMarker(new MarkerOptions().position(new LatLng(temp.startBusStopCoorY,temp.startBusStopCoorX)).title(temp.busNum+getString(R.string.transport_to)).icon(transportIcon));
                        path.paths.add(temp);
                    }
                    transport[0] = mMap.addMarker(new MarkerOptions().position(new LatLng(data.getDoubleExtra("pathEndY" + (pathSize-1), 0),data.getDoubleExtra("pathEndX" + (pathSize-1), 0))).icon(transportIcon).title("하차"));
                    options.add(endMarker.getPosition());
                    pathLine = mMap.addPolyline(options);
                    Log.d("path",path.toString());
                    centerOrder();
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void drawLine(JSONArray lineList, String lineNum){
        if(polyline!=null) polyline.remove();
        maxX=0;
        minX=1000;
        maxY=0;
        minY=1000;
        try {
            PolylineOptions options = new PolylineOptions().color(Color.GREEN);
            for(int i=0;i<lineList.length();i++){
                JSONObject son = lineList.getJSONObject(i);
                double x = son.getDouble("x");
                double y = son.getDouble("y");
                Log.d("root",y+"/"+x);
                options.add(new LatLng(y,x));
                if(x>maxX) maxX = x;
                if(x<minX) minX = x;
                if(y>maxY) maxY = y;
                if(y<minY) minY = y;
            }
            polyline = mMap.addPolyline(options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public float zoomLevelCal(){
        float out=12.5f;
        if((maxX-minX)>(maxY-minY)){
            Log.d("zoomlv",(maxX-minX)+"");
            if((maxX-minX)<0.05) out = (float)(-15*((maxX-minX)*(maxX-minX))+14);
            else if((maxX-minX)>0.5) out = (float)(-9*((maxX-minX)*(maxX-minX))+12);
            else out = (float)(-9*((maxX-minX)*(maxX-minX))+11.5);
        }else{
            Log.d("zoomlv",(maxY-minY)+"");
            out = (float)(-9*((maxY-minY)*(maxY-minY))+12);
        }
        return out;
    }

    private int markerCount(){
        rcount++;
        return rcount%29;
    }

    private void centerOrder(){
        startPoint[1] = ((maxX-minX)/2)+minX;
        startPoint[0] = ((maxY-minY)/2)+minY;

        LatLng start = new LatLng(startPoint[0], startPoint[1]);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,zoomLevelCal()));
    }

    /*class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            startPoint[0] = location.getLatitude();
            startPoint[1] = location.getLongitude();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }*/
}
