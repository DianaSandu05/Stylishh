package com.example.stylishh.database;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.SQLException;
import android.content.Context;
import android.util.Log;

import com.example.stylishh.model.Availability;
import com.example.stylishh.model.Services;

import java.util.ArrayList;
import java.util.List;

public class ServicesDAO {
    public static final String TAG = "ServicesDAO";
    static final String DBNAME = "Stylist.db";
    static final int DATABASE_VERSION = 1;
    //Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private Context mContext;
    private String[] mAllColumns ={DBHelper.servicesId,
    DBHelper.serviceName, DBHelper.postcode};

    public ServicesDAO(Context context){
        this.mContext = context;
        mDbHelper = new DBHelper(context);
        //open the database
        try{
            open();
        }
        catch (SQLException e){
            Log.e(TAG, "SQLException on opening database" + e.getMessage());
        }
    }
    public void open() throws SQLException{
        mDatabase = mDbHelper.getWritableDatabase();
    }
    public void close(){
        mDbHelper.close();
    }
    public Services createService(String serviceName, String postcode)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.serviceName, serviceName);
        values.put(DBHelper.postcode, postcode);
        long insertId = mDatabase.insert(DBHelper.services_table, null, values);
        Cursor cursor = mDatabase.query(DBHelper.services_table, mAllColumns,
                DBHelper.servicesId + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        Services newServices = cursorToServices(cursor);
        cursor.close();
        return newServices;
    }
    public void deleteServices(Services services)
    {
        long servicesId = services.getsId();
        //delete all availability slots for this company
        AvailabilityDAO availabilityDAO = new AvailabilityDAO(mContext);
        List<Availability> availabilityList = availabilityDAO.getAvailabilityOfServices(servicesId);
        if(availabilityList != null && !availabilityList.isEmpty())
        {
            for(Availability a : availabilityList)
            {
                availabilityDAO.deleteAvailability(a);
            }
        }
        System.out.println("the deleted service has the id:" + servicesId);
        mDatabase.delete(DBHelper.services_table, DBHelper.servicesId + " = " +servicesId, null);
    }
    public List<Services> getAllServices()
    {
        List<Services> servicesList = new ArrayList<Services>();
        Cursor cursor = mDatabase.query(DBHelper.services_table, mAllColumns,null, null, null,null, null);
        if(cursor != null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                Services services = cursorToServices(cursor);
                servicesList.add(services);
                cursor.moveToNext();
            }
            //make sure to close the cursor
            cursor.close();
        }
        return servicesList;
    }
    public Services getServicesById(long id){
        Cursor cursor = mDatabase.query(DBHelper.services_table, mAllColumns,
                DBHelper.servicesId + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        Services services = cursorToServices(cursor);
        return services;
    }
    protected Services cursorToServices(Cursor cursor)
    {
        Services services = new Services();
        services.setsId(cursor.getLong(0));
        services.setsName(cursor.getString(1));
        services.setsPostcode(cursor.getString(2));
        return services;
    }
}
