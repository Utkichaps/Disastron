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
    DatabaseHelper myDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
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
                //setContentView(R.layout.activity_main);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.facebook:
                //setContentView(R.layout.activity_facebook_share);
                Intent intent1 = new Intent(this,FacebookShare.class);
                startActivity(intent1);
                break;
            case R.id.contact:
                //setContentView(R.layout.activity_emergency_contact);
                Intent intent2 = new Intent(this,EmergencyContact.class);
                startActivity(intent2);
                break;
            case R.id.firstaid:
                //setContentView(R.layout.activity_first_aid);
                Intent intent3 = new Intent(this,FirstAid.class);
                startActivity(intent3);
                break;
            case R.id.settings:
                //setContentView(R.layout.activity_settings);
                Intent intent4 = new Intent(this,Settings.class);
                startActivity(intent4);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
