package com.ugurckmkc.foursquareapivize;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivty extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private String venueID;
    private String venueName;
    private double venueLatitude;
    private double venueLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_activty);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle venue = getIntent().getExtras();

        venueID = venue.getString("ID");
        venueName = venue.getString("name");
        venueLatitude = venue.getDouble("latitude");
        venueLongitude = venue.getDouble("longitude");
        setTitle(venueName);

    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://foursquare.com/v/" + venueID));
        startActivity(browserIntent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng venue = new LatLng(venueLatitude, venueLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venue, 16));

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(venue)
                .title(venueName)
                .snippet("Foursquare'de Aç"));
        marker.showInfoWindow();
        mMap.setOnInfoWindowClickListener(this);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission
                .ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }
}