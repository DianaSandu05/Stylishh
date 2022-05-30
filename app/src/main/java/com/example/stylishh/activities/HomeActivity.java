package com.example.stylishh.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.stylishh.R;
import com.example.stylishh.model.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    Button btnSearch, btnGetAllServices,getAllAvailability, btnAddAvailability;
    private static final String DB_NAME = "stylist.db";
    private static final int DB_VERSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getAllAvailability = (Button) findViewById(R.id.buttonGetAvailability);
        btnGetAllServices = (Button) findViewById(R.id.buttonGetServices);
        btnAddAvailability = (Button) findViewById(R.id.btn_add_availability);

        getAllAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListAvailabilityActivity.class);
                startActivity(intent);
            }
        });
        btnGetAllServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListServicesActivity.class);
                startActivity(intent);
            }
        });
        btnAddAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAvailabilityActivity.class);
                startActivity(intent);
            }
        });
    }

}