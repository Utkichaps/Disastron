package com.example.kunal.disastron;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;

public class map_fragment extends Fragment implements OnMapReadyCallback {
    int myItemId;
    View view;
    private GoogleMap mMap;
    LatLng loc;
    private static String OWM_TILE_URL = "https://tile.openweathermap.org/map/%s/%d/%d/%d.png";
    double lat,lon;
    private String tileType = "clouds";
    private TileOverlay tileOver;

    public map_fragment(double la, double lo) {
        lat = la;
        lon = lo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        view = inflater.inflate(R.layout.layout_map, container, false);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.map_options, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.i("Location2:",lat+""+lon);
        loc = new LatLng(lat, lon);
        //mMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17));
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setCompassEnabled(true);
        mapUiSettings.setZoomControlsEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        myItemId =item.getItemId();
        switch (myItemId)
        {
            case R.id.norm:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;

            case R.id.sat:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.ter:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;

            case R.id.hyb:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            case R.id.none:
                if(tileOver != null)
                    tileOver.remove();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17));
                break;

            case R.id.clou:
                tileType = "clouds_new";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.prec:
                tileType = "precipitation_new";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.temp:
                tileType = "temp_new";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.snow:
                tileType = "snow";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.rain:
                tileType = "rain";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.wind:
                tileType = "wind";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.pres:
                tileType = "pressure_new";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;
        }
        return true;
    }

    private void setUpMap() {
        tileOver = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(createTransparentTileProvider()));
    }

    private TileProvider createTransparentTileProvider() {
        return new TransparentTileOWM(tileType);
    }
}
