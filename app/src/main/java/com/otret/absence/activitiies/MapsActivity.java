package com.otret.absence.activitiies;

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
import com.otret.absence.utilities.ConstantPreferences;
import com.otret.absence.models.ModelRumus;
import com.otret.absence.R;
import com.otret.absence.utilities.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double longitude;
    private Double latitude;
    float distance;
    private ModelRumus modelRumus = new ModelRumus();
    private List<Marker> markers = new ArrayList<>();
    private PreferenceHelper prefHelper;
    private Double officeLong, officeLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        prefHelper = PreferenceHelper.getInstance(MapsActivity.this);
        officeLong = Double.parseDouble(prefHelper.getString(ConstantPreferences.LONGITUDE, "0"));
        officeLat = Double.parseDouble(prefHelper.getString(ConstantPreferences.LATITUDE, "0"));
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

                officeMark();
                cameraMarker(mMap);
                distance = modelRumus.distanceBetween(latitude, longitude, officeLat, officeLong);

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
        LatLng office = new LatLng(officeLat,officeLong);
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
}
