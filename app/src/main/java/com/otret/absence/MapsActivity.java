package com.otret.absence;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double longitude;
    private Double latitude;
    Double officeLatitude = -7.7197619;
    Double officeLongitude = 110.3825028;
    float distance;
    private List<Marker> markers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        FusedLocationProviderClient mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocation.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    LatLng you = new LatLng(latitude,longitude);
                    markers.add(mMap.addMarker(new MarkerOptions().position(you)
                            .title(ConstantPreferences.YOUR_MARKER)
                            .icon(bitmapDescriptorFromVector(MapsActivity.this, R.drawable.ic_person_pin_circle_blue))));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(you, 19.0f));

                    officeMark();
                    cameraMarker(mMap);
                    distanceBetween(latitude, officeLatitude, longitude, officeLongitude);

                    if (distance<50){
                        ConstantPreferences.toastMessage(MapsActivity.this, ConstantPreferences.YOUR_MARKER);
                    } else {
                        ConstantPreferences.toastMessage(MapsActivity.this, ConstantPreferences.MARKER_OFFICE);
                    }
            }
        });
    }

    private void cameraMarker(GoogleMap googleMap){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 100; // offset from edges of the map in pixels

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }

    private void officeMark(){
        int radius = 50;
        LatLng office = new LatLng(officeLatitude,officeLongitude);
        mMap.addCircle(new CircleOptions()
                .center(office)
                .radius(radius)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT));
        markers.add(mMap.addMarker(new MarkerOptions().position(office).title(ConstantPreferences.MARKER_OFFICE)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(office, 19.0f));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void distanceBetween(Double latitude1, Double latitude2, Double longitude1, Double longitude2){
        Location loc1 = new Location("");
        loc1.setLatitude(latitude1);
        loc1.setLongitude(longitude1);
        Location loc2 = new Location("");
        loc2.setLatitude(latitude2);
        loc2.setLongitude(longitude2);
        distance = loc1.distanceTo(loc2);
    }
}
