package com.example.kunal.disastron;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

public class map_fragment extends Fragment implements OnMapReadyCallback {

    View view;
    private GoogleMap mMap;
    double lat = 12.0,lon = 77.0;  //default values

    public map_fragment(double la, double lo) {
        lat = la;
        lon = lo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /*SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        view = inflater.inflate(R.layout.layout_map, container, false);
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("Location2:",lat+""+lon);
        LatLng loc = new LatLng(lat, lon);
        //mMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17));
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setCompassEnabled(true);
        mapUiSettings.setZoomControlsEnabled(true);
    }

}
