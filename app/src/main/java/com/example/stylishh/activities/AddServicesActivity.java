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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylishh.database.ServicesDAO;
import com.example.stylishh.model.Services;
import com.example.stylishh.R;

import com.example.stylishh.R;

public class AddServicesActivity extends AppCompatActivity implements OnClickListener {

    public static final String TAG = "AddServicesActivity";

    private EditText mTxtServicesName;
    private EditText mTxtPostcode;
    private Button mBtnAdd;

    private ServicesDAO mServicesDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_services);

        initViews();

        this.mServicesDAO = new ServicesDAO(this);
    }

    private void initViews() {
        this.mTxtServicesName = (EditText) findViewById(R.id.txt_service_name);
        this.mTxtPostcode = (EditText) findViewById(R.id.txt_postcode);
        this.mBtnAdd = (Button) findViewById(R.id.btn_add);

        this.mBtnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Editable servicesName = mTxtServicesName.getText();
                Editable postcode = mTxtPostcode.getText();
                if (!TextUtils.isEmpty(servicesName) && !TextUtils.isEmpty(postcode)
                     ) {
                    // add the services to database
                    Services createdServices = mServicesDAO.createService(
                            servicesName.toString(),
                            postcode.toString());

                    Log.d(TAG, "added service : "+ createdServices.getsName());
                    Intent intent = new Intent();
                    intent.putExtra(ListServicesActivity.EXTRA_ADDED_SERVICE, createdServices);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
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
}
