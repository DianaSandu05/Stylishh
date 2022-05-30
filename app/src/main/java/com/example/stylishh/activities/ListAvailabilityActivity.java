package com.example.stylishh.activities;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.AdapterView;

import com.example.stylishh.R;
import com.example.stylishh.model.Availability;
import com.example.stylishh.adapter.ListAvailabilityAdapter;
import com.example.stylishh.database.AvailabilityDAO;
import com.example.stylishh.model.Services;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ListAvailabilityActivity extends Activity implements OnItemLongClickListener, OnItemClickListener, OnClickListener {

    public static final String TAG = "ListAvailabilityActivity";
    public static final int REQUEST_CODE_ADD_AVAILABILITY = 40;
    public static final String EXTRA_ADDED_AVAILABILITY = "extra_key_added_availability";
    public static final String EXTRA_SELECTED_SERVICES_ID = "extra_key_selected_services_id";

    public ListView mListViewAvailability;
    public TextView mTxtEmptyListAvailability;
    public ImageButton mBtnAddAvailability;

    public ListAvailabilityAdapter mAdapter;
    public List<Availability> mListAvailability;
    public AvailabilityDAO mAvailabilityDAO;

    private long mServiceId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_availability);
        //initialize views
        initViews();
        //get services id from extras
        mAvailabilityDAO = new AvailabilityDAO(this);
       // Intent intent = getIntent();
      /*  if(intent != null){
            this.mServiceId = intent.getLongExtra(EXTRA_SELECTED_SERVICES_ID, -1);
       }
        if(mServiceId != -1)
      {*/
            mListAvailability = mAvailabilityDAO.getAllAvailability();
            //fill the list view
            if(mListAvailability != null && !mListAvailability.isEmpty()){
                mAdapter = new ListAvailabilityAdapter(this, mListAvailability);
                mListViewAvailability.setAdapter(mAdapter);
            }
            else{
                mTxtEmptyListAvailability.setVisibility(View.VISIBLE);
                mListViewAvailability.setVisibility(View.GONE);
            }
        }
  /*  }*/
    private void initViews(){
        this.mListViewAvailability = (ListView) findViewById(R.id.list_availability);
        this.mTxtEmptyListAvailability = (TextView) findViewById(R.id.txt_empty_list_availability);
        this.mBtnAddAvailability = (ImageButton) findViewById(R.id.btn_add_availability);
        this.mListViewAvailability.setOnItemClickListener(this);
        this.mListViewAvailability.setOnItemLongClickListener(this);
        this.mBtnAddAvailability.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.btn_add_availability:
                    Intent intent = new Intent(this, AddAvailabilityActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_ADD_AVAILABILITY);
                    break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_ADD_AVAILABILITY)
        {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    Availability createdAvailability = (Availability) data.getSerializableExtra(EXTRA_ADDED_AVAILABILITY);
                    //refresh the listView
                    if(mListAvailability == null)
                        mListAvailability = new ArrayList<Availability>();
                       mListAvailability.add(createdAvailability);


                    if (mListAvailability == null || !mListAvailability.isEmpty()) {
                        mListAvailability = new ArrayList<Availability>();
                    }
                    if (mAvailabilityDAO == null)
                        mAvailabilityDAO = new AvailabilityDAO(this);
                    mListAvailability = mAvailabilityDAO.getAvailabilityOfServices(mServiceId);
                    if (mAdapter == null) {
                        mAdapter = new ListAvailabilityAdapter(this, mListAvailability);
                        mListViewAvailability.setAdapter(mAdapter);
                        if (mListViewAvailability.getVisibility() != View.VISIBLE) {
                            mTxtEmptyListAvailability.setVisibility(View.GONE);
                            mListViewAvailability.setVisibility(View.VISIBLE);
                        }
                    }
                }
               /* else{
                    mAdapter.setItems(mListAvailability);
                    mAdapter.notifyDataSetChanged();
                }*/
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mAvailabilityDAO.close();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Availability clickedAvailability = mAdapter.getItem(position);
        Log.d(TAG, "clickedItem : "+  clickedAvailability.getaDateTime()+" "
                +clickedAvailability.getStatus());
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
        Availability clickedAvailability = mAdapter.getItem(position);
        Log.d(TAG, "longClickedItem : "+clickedAvailability.getaDateTime());
        showDeleteDialogConfirmation(clickedAvailability);
        return true;
    }
    private void showDeleteDialogConfirmation(final Availability availability){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete");
            alertDialogBuilder.setMessage("Are you sure you want to delete the availability record?"
            + availability.getaDateTime());
    // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // delete the availability and refresh the list
            if(mAvailabilityDAO != null) {
                mAvailabilityDAO.deleteAvailability(availability);

                //refresh the listView
                mListAvailability.remove(availability);
                if(mListAvailability.isEmpty()) {
                    mListViewAvailability.setVisibility(View.GONE);
                    mTxtEmptyListAvailability.setVisibility(View.VISIBLE);
                }

                mAdapter.setItems(mListAvailability);
                mAdapter.notifyDataSetChanged();
            }

            dialog.dismiss();
            Toast.makeText(ListAvailabilityActivity.this,
                    "Record deleted successfully", Toast.LENGTH_SHORT).show();

        }
    });

    // set neutral button OK
        alertDialogBuilder.setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // Dismiss the dialog
            dialog.dismiss();
        }
    });

    AlertDialog alertDialog = alertDialogBuilder.create();
    // show alert
        alertDialog.show();
}
}
