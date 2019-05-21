package com.example.torchapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.torchapp.database.DatabaseFacade;
import com.example.torchapp.login.LoginActivity;
import com.example.torchapp.map.DrawerMapActivity;


public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseFacade.initializeServerConnection();

        Intent myIntent = null;

        //todo check connection first
        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
        {
            // call Login Activity
            myIntent = new Intent(this, LoginActivity.class);

        }
        else
        {
            // Stay at the current activity.
            myIntent = new Intent(this, DrawerMapActivity.class);
        }

        startActivity(myIntent);


    }
}
