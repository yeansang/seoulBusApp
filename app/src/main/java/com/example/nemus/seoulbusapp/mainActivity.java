package com.example.nemus.seoulbusapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

    private GoogleMap mMap;
    private LocationManager mLocationManager;

    private Marker marker;
    private Marker startMarker = null;
    private Marker endMarker= null;

    private Intent intent;

    private double startPoint[] = new double[]{37.558345, 126.994583};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("main", "Start");
        super.onCreate(savedInstanceState);
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
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100000,0,new MyLocationListener());
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
        final mainActivity main = this;
        // Add a marker in Sydney and move the camera
        final LatLng start = new LatLng(startPoint[0], startPoint[1]);
        //marker = mMap.addMarker(new MarkerOptions().position(start).draggable(true).title("test").snippet("test String"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start,15.5f));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker m) {
                marker.setPosition(m.getPosition());
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent mapData = new Intent(main,MapPopup.class);
                mapData.putExtra("y",latLng.latitude);
                mapData.putExtra("x",latLng.longitude);
                startActivityForResult(mapData,MAPPOPUP);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
              if(startMarker!=null){
                if(startMarker.equals(m)) return true;
                }else if(endMarker!=null) {
                    if(endMarker.equals(m)) return true;
                }else{
                  Intent busStopData = new Intent(main,BusStopInfo.class);
                  LatLng loc = m.getPosition();
                  busStopData.putExtra("x",loc.longitude);
                  busStopData.putExtra("y",loc.latitude);
                  busStopData.putExtra("arsId",m.getTitle());
                  Log.d("markerclick",m.getTitle());
                  startActivityForResult(busStopData,BUSSTOPINFO);
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //marker.

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case MAPPOPUP:
                    int item = data.getIntExtra("ClickItem",2);
                    if(item == 0){
                        if(startMarker!=null){
                            startMarker.remove();
                        }
                        startMarker = mMap.addMarker(new MarkerOptions().title("Start").position(new LatLng(data.getDoubleExtra("y",0),data.getDoubleExtra("x",0))));
                    }else if(item == 1) {
                        if(endMarker!=null){
                            endMarker.remove();
                        }
                        endMarker = mMap.addMarker(new MarkerOptions().title("End").position(new LatLng(data.getDoubleExtra("y",0),data.getDoubleExtra("x",0))));
                    }else if(item == 2){
                        try {
                            JSONArray busStopData = new JSONArray(data.getStringExtra("busStopData"));
                            for(int i=0;i<busStopData.length();i++){
                                JSONObject jo = busStopData.getJSONObject(i);
                                mMap.addMarker(new MarkerOptions().title(jo.getString("arsId")).position(new LatLng(jo.getDouble("gpsY"),jo.getDouble("gpsX"))));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case BUSLINEPOP:
                    try {
                        JSONArray ja = new JSONArray(data.getStringExtra("lineData"));
                        drawLine(ja);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            if((startMarker!=null)&&(endMarker!=null)){
                //경로 불러오기& 경로 그리기
                JSONArray rootdata;
            }
        }
    }

    public void drawLine(JSONArray lineList){
        try {
            PolylineOptions options = new PolylineOptions().color(Color.CYAN);
            lineList = new JSONArray();
            for(int i=0;i<lineList.length();i++){
                JSONObject son = lineList.getJSONObject(i);
                Log.d("root",son.getDouble("y")+"/"+son.getDouble("x"));
                options.add(new LatLng(son.getDouble("y"),son.getDouble("x")));
            }
            options.color(Color.DKGRAY);
            Polyline polyline = mMap.addPolyline(options);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyLocationListener implements LocationListener {

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

    }
}
