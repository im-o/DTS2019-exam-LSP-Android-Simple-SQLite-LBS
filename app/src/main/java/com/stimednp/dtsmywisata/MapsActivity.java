package com.stimednp.dtsmywisata;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String EXTRA_WISATA = "extra_wisata";
    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle intent = getIntent().getExtras();
        Wisatas wisatas = getIntent().getParcelableExtra(EXTRA_WISATA);
        if (wisatas != null){
            String title = wisatas.getTitle();
            String lotitude = wisatas.getCoor_latitude();
            String longitude = wisatas.getCoor_longitude();
            Double lat = Double.valueOf(lotitude);
            Double lng = Double.valueOf(longitude);
            LatLng mylocation = new LatLng(lat, lng);
            Log.d(TAG, "onMapReady : " + mylocation);

            mMap.addMarker(new MarkerOptions().position(mylocation).title(title));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 14.f));
        }

    }
}
