package com.example.stylishh.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import java.util.List;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stylishh.R;
import com.example.stylishh.adapter.ListServicesAdapter;
import com.example.stylishh.model.Services;
import com.example.stylishh.database.ServicesDAO;
import com.example.stylishh.activities.ListServicesActivity;

public class ListServicesActivity extends Activity implements OnItemLongClickListener, OnItemClickListener, OnClickListener {

    public static final String TAG = "ListServicesActivity";
    public static final int REQUEST_CODE_ADD_SERVICE = 40;
    public static final String EXTRA_ADDED_SERVICE = "extra_key_added_service";

    private ListView mListViewServices;
    private TextView xTxtEmptyListServices;
    private ImageButton mbtnAddServices;

    private ListServicesAdapter mAdapter;
    private List<Services> mListServices;
    private ServicesDAO mServicesDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_services);
        // initialize views
        initViews();

        // fill the listView
        mServicesDao = new ServicesDAO(this);
        mListServices = mServicesDao.getAllServices();
        if(mListServices != null && !mListServices.isEmpty()) {
            mAdapter = new ListServicesAdapter(this, mListServices);
            mListViewServices.setAdapter(mAdapter);
        }
        else {
            xTxtEmptyListServices.setVisibility(View.VISIBLE);
            mListViewServices.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        this.mListViewServices = (ListView) findViewById(R.id.list_services);
        this.xTxtEmptyListServices = (TextView) findViewById(R.id.txt_empty_list_services);
        this.mbtnAddServices = (ImageButton) findViewById(R.id.btn_add_services);
        this.mListViewServices.setOnItemClickListener(this);
        this.mListViewServices.setOnItemLongClickListener(this);
        this.mbtnAddServices.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_services:
                Intent intent = new Intent(this, AddServicesActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_SERVICE);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_ADD_SERVICE) {
            if(resultCode == RESULT_OK) {
                // add the added services to the listServices and refresh the listView
                if(data != null) {
                    Services createdCompany = (Services) data.getSerializableExtra(EXTRA_ADDED_SERVICE);
                    if(createdCompany != null) {
                        if(mListServices == null)
                            mListServices = new ArrayList<Services>();
                        mListServices.add(createdCompany);

                        if(mAdapter == null) {
                            if(mListViewServices.getVisibility() != View.VISIBLE) {
                                mListViewServices.setVisibility(View.VISIBLE);
                                xTxtEmptyListServices.setVisibility(View.GONE);
                            }

                            mAdapter = new ListServicesAdapter(this, mListServices);
                            mListViewServices.setAdapter(mAdapter);
                        }
                        else {
                            mAdapter.setItems(mListServices);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServicesDao.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Services clickedService = mAdapter.getItem(position);
        Log.d(TAG, "clickedItem : "+clickedService.getsName());
        Intent intent = new Intent(this, ListServicesActivity.class);
        intent.putExtra(ListAvailabilityActivity.EXTRA_SELECTED_SERVICES_ID, clickedService.getsId());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Services clickedService = mAdapter.getItem(position);
        Log.d(TAG, "longClickedItem : "+clickedService.getsName());
        showDeleteDialogConfirmation(clickedService);
        return true;
    }

    private void showDeleteDialogConfirmation(final Services clickedServices) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Delete");
        alertDialogBuilder.setMessage("Are you sure you want to delete the \""+clickedServices.getsName()+"\" company ?");

        // set positive button YES message
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // delete the services and refresh the list
                if(mServicesDao != null) {
                    mServicesDao.deleteServices(clickedServices);
                    mListServices.remove(clickedServices);

                    //refresh the listView
                    if(mListServices.isEmpty()) {
                        mListViewServices.setVisibility(View.GONE);
                        xTxtEmptyListServices.setVisibility(View.VISIBLE);
                    }
                    mAdapter.setItems(mListServices);
                    mAdapter.notifyDataSetChanged();
                }

                dialog.dismiss();
                Toast.makeText(ListServicesActivity.this, "Record deleted successfully", Toast.LENGTH_SHORT).show();
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
