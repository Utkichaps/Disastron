package com.example.disastron;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class map_fragment extends Fragment implements OnMapReadyCallback {
    int myItemId;
    View view;
    private GoogleMap mMap;
    String Hospital = "hospital";
    LatLng loc;
    DatabaseReference dreff;
    private static String OWM_TILE_URL = "https://tile.openweathermap.org/map/%s/%d/%d/%d.png";
    double lat,lon;
    private String tileType = "clouds";
    private TileOverlay tileOver;
    private int PROXIMITY_RADIUS = 10000;
    private static String uniqueID = null;
    private String ID;
    int flag;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    public map_fragment(double la, double lo) {
        lat = la;
        lon = lo;
    }

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        ID = id(getContext());
        SupportMapFragment mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);
        view = inflater.inflate(R.layout.layout_map, container, false);

        dreff = FirebaseDatabase.getInstance().getReference().child("Pinlocation");

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

        dreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flag = 0;
                MarkerOptions markerOptions = new MarkerOptions();

                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Pinlocation pl = childSnapshot.getValue(Pinlocation.class);
                    LatLng latLng = new LatLng(pl.getLatitude(),pl.getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.title("Help");
                    markerOptions.snippet(pl.getDescription());
                    mMap.addMarker(markerOptions);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void HospitalClicked()
    {
        mMap.clear();
        String url = getUrl(lat, lon, Hospital);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(lat,lon);
        getNearbyPlacesData.execute(DataTransfer);
        Toast.makeText(getContext(),"Nearby Hospitals", Toast.LENGTH_LONG).show();
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCGebJhS00UuJTvtAGt-xIkPGVUy8nHY5k");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    public void MarkerAdd() {
        AddMarker addm = new AddMarker(lat,lon,ID);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame,addm,"findThisFrag")
                .addToBackStack(null)
                .commit();
    }

    public void MarkerDel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete your marker?");
        builder.setTitle("Alert");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DelMarker delm = new DelMarker(ID);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame,delm,"findThisFrag2")
                        .addToBackStack(null)
                        .commit();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        myItemId =item.getItemId();
        switch (myItemId)
        {
            case R.id.reset:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
                break;

            case R.id.del:
                MarkerDel();
                break;

            case R.id.add:
                MarkerAdd();
                break;

            case R.id.hosp:
                HospitalClicked();
                break;

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
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 17));
                break;

            case R.id.clou:
                tileType = "clouds_new";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.prec:
                tileType = "precipitation_new";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.temp:
                tileType = "temp_new";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.snow:
                tileType = "snow";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.rain:
                tileType = "rain";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.wind:
                tileType = "wind";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                break;

            case R.id.pres:
                tileType = "pressure_new";
                if(tileOver != null)
                    tileOver.remove();
                setUpMap();
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
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
