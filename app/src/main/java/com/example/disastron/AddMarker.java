package com.example.disastron;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/**
 * FIGURE OUT A WAY TO STORE UNIQUE KEYS
 */

public class AddMarker extends Fragment {

    Button button, button2;
    View view;
    double lat,lon;
    private String UID;
    DatabaseReference dreff;
    EditText editText;
    Pinlocation pinloc;
    int flag;

    public AddMarker(double la, double lo,String ID) {
        lat = la;
        lon = lo;
        UID = ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dreff = FirebaseDatabase.getInstance().getReference().child("Pinlocation");
        flag = 0;
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
        dreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Pinlocation pl = childSnapshot.getValue(Pinlocation.class);
                    if(pl.getUniqueID().equals(UID)) {
                        flag = 1;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                    if(flag == 0) {
                        dreff.push().setValue(pinloc, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
                                } else {
                                    getFragmentManager().popBackStack();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(getContext(), "You have already placed a pin! Please remove your pin and" +
                                " try again.", Toast.LENGTH_SHORT).show();
                    }
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
