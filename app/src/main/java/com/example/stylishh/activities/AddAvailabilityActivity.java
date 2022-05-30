package com.example.stylishh.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.stylishh.adapter.SpinnerServicesAdapter;
import com.example.stylishh.database.AvailabilityDAO;
import com.example.stylishh.database.ServicesDAO;
import com.example.stylishh.model.Availability;
import com.example.stylishh.model.Services;
import com.example.stylishh.R;

import java.util.List;

public class AddAvailabilityActivity extends AppCompatActivity implements OnClickListener, OnItemSelectedListener {

    public static final String TAG = "AddAvailabilityActivity";

    private EditText mTxtDateTime;
    private EditText mTxtStatus;
    private Spinner mSpinnerServices;
    private Button mBtnAdd;

    private ServicesDAO mServicesDAO;
    private AvailabilityDAO mAvailabilityDAO;

    private Services mSelectedServices;
    private SpinnerServicesAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_availability);

        initViews();

        this.mServicesDAO = new ServicesDAO(this);
        this.mAvailabilityDAO = new AvailabilityDAO(this);

        //fill the spinner with services
        List<Services> listServices = mServicesDAO.getAllServices();
        if(listServices != null) {
            mAdapter = new SpinnerServicesAdapter(this, listServices);
            mSpinnerServices.setAdapter(mAdapter);
            mSpinnerServices.setOnItemSelectedListener(this);
        }
    }

    private void initViews() {
        this.mTxtDateTime = (EditText) findViewById(R.id.dateTime);
        this.mTxtStatus = (EditText) findViewById(R.id.status);
        this.mSpinnerServices = (Spinner) findViewById(R.id.spinner_companies);
        this.mBtnAdd = (Button) findViewById(R.id.btn_add);

        this.mBtnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Editable dateTime = mTxtDateTime.getText();
                Editable status = mTxtStatus.getText();
                mSelectedServices = (Services) mSpinnerServices.getSelectedItem();
                if (!TextUtils.isEmpty(dateTime) && !TextUtils.isEmpty(status)
                        ) {
                    // add the services to database
                    Availability createdAvailability = mAvailabilityDAO.createAvailability(dateTime.toString(),
                            status.toString(), mSelectedServices.getsId());

                    Log.d(TAG, "added availability : "+ createdAvailability.getaDateTime()+" "+createdAvailability.getStatus());
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    Toast.makeText(this, "Please fill all the details", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServicesDAO.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedServices = mAdapter.getItem(position);
        Log.d(TAG, "selectedServices : "+mSelectedServices.getsName());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
