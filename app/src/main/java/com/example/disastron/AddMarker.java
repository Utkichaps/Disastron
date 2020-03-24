package com.example.disastron;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMarker extends Fragment {

    Button button, button2;
    View view;
    double lat,lon;
    private String UID;
    DatabaseReference dreff;
    EditText editText;
    Pinlocation pinloc;

    public AddMarker(double la, double lo,String ID) {
        lat = la;
        lon = lo;
        UID = ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dreff = FirebaseDatabase.getInstance().getReference().child("Pinlocation");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_marker, container, false);
        editText = view.findViewById(R.id.editText2);
        button = view.findViewById(R.id.button2);
        button2 = view.findViewById(R.id.button3);
        pinloc = new Pinlocation();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = editText.getText().toString().trim();
                if(desc.length() == 0) {
                    Toast.makeText(getContext(), "Please add a valid description", Toast.LENGTH_SHORT).show();
                }
                else {
                    pinloc.setDescription(desc);
                    pinloc.setLatitude(lat);
                    pinloc.setLongitude(lon);
                    pinloc.setUniqueID(UID);
                    dreff.push().setValue(pinloc);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        return view;
    }
}
