package com.example.disastron;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        myItemId = item.getItemId();

        switch(myItemId)
        {
            case R.id.home :
                //setContentView(R.layout.activity_main);
                Intent intent1 = new Intent(this,MainActivity.class);
                this.startActivity(intent1);
                break;
            case R.id.mapopt:
                //setContentView(R.layout.activity_map);
                Intent intent6 = new Intent(this,MapActivity.class);
                this.startActivity(intent6);
                break;
            case R.id.facebook:
                //setContentView(R.layout.activity_facebook_share);
                Intent intent2 = new Intent(this,FacebookShare.class);
                this.startActivity(intent2);
                break;
            case R.id.contact:
                //setContentView(R.layout.activity_emergency_contact);
                Intent intent3 = new Intent(this,EmergencyContact.class);
                this.startActivity(intent3);
                break;
            case R.id.firstaid:
                //setContentView(R.layout.activity_first_aid);
                Intent intent4 = new Intent(this,FirstAid.class);
                this.startActivity(intent4);
                break;
            case R.id.settings:
                //setContentView(R.layout.activity_settings);
                Intent intent5 = new Intent(this,Settings.class);
                this.startActivity(intent5);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
