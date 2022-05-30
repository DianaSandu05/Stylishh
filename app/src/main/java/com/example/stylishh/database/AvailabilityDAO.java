package com.example.stylishh.database;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.stylishh.model.Services;
import com.example.stylishh.model.Availability;

public class AvailabilityDAO {
    private static final String TAG ="AvailabilityDAO";
    private final Context context;
    static final int DATABASE_VERSION = 1;
    //Database fields
    private SQLiteDatabase mDatabase;
    private DBHelper mDbHelper;
    private String[] mAllColumns = {DBHelper.availability_Id,
                    DBHelper.dateTime,
                    DBHelper.status,
                    DBHelper.services_availability_Id};

    public AvailabilityDAO(Context context)
    {
        mDbHelper = new DBHelper(context);
        this.context = context;
        //open database
        try{
            open();
        }
        catch (SQLException e){
            Log.e(TAG, "SQLException on opening database" + e.getMessage());
            e.printStackTrace();
        }
    }
    public void open() throws SQLException{
        mDatabase = mDbHelper.getWritableDatabase();
    }
    public void close(){
        mDbHelper.close();
    }
    public Availability createAvailability(String aDateTime, String aStatus, long servicesId)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper.dateTime, aDateTime);
        values.put(DBHelper.status, aStatus);
        values.put(DBHelper.services_availability_Id, servicesId);
        long insertId = mDatabase.insert(DBHelper.availability_table, null, values);
        Cursor cursor = mDatabase.query(DBHelper.availability_table, mAllColumns,
                DBHelper.availability_Id + " = " + insertId, null, null,
        null, null);
        cursor.moveToFirst();
        Availability newAvailability = cursorToAvailability(cursor);
        cursor.close();
        return newAvailability;
    }
    public void deleteAvailability(Availability availability)
    {
        long id = availability.getaId();
        System.out.println("the availability slot has the id: " + id);
        mDatabase.delete(DBHelper.availability_table, DBHelper.availability_Id
                            + " = " + id, null );
    }
    public List<Availability> getAllAvailability(){
        List<Availability> availabilityList = new ArrayList<Availability>();
        Cursor cursor = mDatabase.query(DBHelper.availability_table, mAllColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            Availability availability = cursorToAvailability(cursor);
            availabilityList.add(availability);
            cursor.moveToNext();
        }
        cursor.close();
        return availabilityList;
    }
    public List<Availability> getAvailabilityOfServices(long servicesId)
    {
        List<Availability> availabilityList = new ArrayList<Availability>();
        Cursor cursor = mDatabase.query(DBHelper.availability_table, mAllColumns,
                DBHelper.servicesId + " = ?",
                new String[] {String.valueOf(servicesId)}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Availability availability = cursorToAvailability(cursor);
            availabilityList.add(availability);
            cursor.moveToNext();
        }
        //make sure to close the cursor
        cursor.close();
        return availabilityList;
    }
    private Availability cursorToAvailability(Cursor cursor)
    {
        Availability availability = new Availability();
        availability.setaId(cursor.getLong(0));
        availability.setaDateTime(cursor.getString(1));
        availability.setStatus(cursor.getString(2));
        //get the services by id
        long servicesId = cursor.getLong(3);
        ServicesDAO dao = new ServicesDAO(context);
        Services services = dao.getServicesById(servicesId);
        if(services != null)
            availability.setaServices(services);
        return availability;
    }
}
