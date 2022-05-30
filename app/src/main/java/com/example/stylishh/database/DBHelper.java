package com.example.stylishh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import androidx.annotation.Nullable;

import com.example.stylishh.adapter.LoginDatabaseAdapter;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";
    public static final String DBNAME = "Stylist.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DBNAME, null, DATABASE_VERSION);
        Log.d("table", CreateServicesTable);
    }
    //Columns for services
    public static final String services_table = "Services";
    public static final String servicesId = "Id";
    public static final String serviceName = "serviceName";
    public static final String postcode = "postcode";
    //foreign key to get availability


    //columns for availability
    public static final String availability_table = "availability";
    public static final String availability_Id = servicesId;
    public static final String dateTime = "dateTime";
    public static final String status = "status";
    public static final String appointment_servicesName = serviceName;
    public static final String services_availability_Id = "servicesId";

    //create services table
    private String CreateServicesTable = "create table " + services_table + "(" +
            servicesId + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            serviceName + " TEXT UNIQUE," + postcode + " TEXT"
        +")";
    //create availability table
    private String CreateAvailabilityTable = "create table " + availability_table + "(" +
            availability_Id + " INTEGER primary key AUTOINCREMENT," +
            dateTime + " text," +
            status + " text,"  +
            services_availability_Id +  " INTEGER NOT NULL "
        +")";
    @Override
    public void onCreate(SQLiteDatabase myDB) {
        myDB.execSQL(LoginDatabaseAdapter.DATABASE_CREATE);
        myDB.execSQL(CreateServicesTable);
        myDB.execSQL(CreateAvailabilityTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDB, int _oldVersion, int _newVersion) {
        Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");

        myDB.execSQL("DROP TABLE IF EXISTS " + services_table);
        myDB.execSQL("DROP TABLE IF EXISTS " + availability_table);
        onCreate(myDB);
    }

    public Cursor searchServices(String text){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + services_table + "WHERE " + serviceName+" Like '%"+text+"%'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    /*public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBNAME, factory, DATABASE_VERSION);
    }*/
}

