package kalpesh.mac.com.raandroid_header.GoogleMaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import kalpesh.mac.com.raandroid_header.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int size;
    private String[] title;
    private double[] lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        lat = intent.getDoubleArrayExtra("latitude");
        lon = intent.getDoubleArrayExtra("longitude");
        title = intent.getStringArrayExtra("title");
        size = intent.getIntExtra("size", 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

            mMap.setMapType(1);
            mMap.setTrafficEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            System.out.println("Map size: " + size);
        System.out.println("Map lat: "+lat[0]);
        System.out.println("Map long: "+lon[0]);
        System.out.println("Map title: "+title[0]);
              int i = 0;
        LatLng locations;
        while (i < size) {
            locations = new LatLng(lat[i],lon[i]);
            mMap.addMarker(new MarkerOptions().position(locations).title(title[i]));
            System.out.println("MapActivity latitude: " + lat[i]);
            System.out.println("MapActivity longitude: "+lon[i]);
           // mMap.moveCamera(CameraUpdateFactory.newLatLng(locations));
            i++;
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat[0],
                lon[0]), 12.0f));
    }
}