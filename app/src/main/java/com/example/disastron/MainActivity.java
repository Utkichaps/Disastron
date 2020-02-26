package com.example.disastron;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    int myItemId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        myItemId = item.getItemId();

        switch(myItemId)
        {
            case R.id.home :
                setContentView(R.layout.activity_main);
                return true;
            case R.id.facebook:
                setContentView(R.layout.activity_facebook_share);
                return true;
            case R.id.contact:
                setContentView(R.layout.activity_emergency_contact);
                return true;
            case R.id.firstaid:
                setContentView(R.layout.activity_first_aid);
                return true;
            case R.id.settings:
                setContentView(R.layout.activity_settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
