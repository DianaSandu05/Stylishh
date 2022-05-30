package com.example.stylishh.adapter;


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stylishh.R;
import com.example.stylishh.model.Services;

import org.w3c.dom.Text;

public class SpinnerServicesAdapter extends BaseAdapter {

    public static final String TAG = "SpinnerServicesAdapter";

    private List<Services> mItems;
    private LayoutInflater mInflater;

    public SpinnerServicesAdapter(Context context, List<Services> servicesList) {
        this.setItems(servicesList);
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0 ;
    }

    @Override
    public Services getItem(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null ;
    }

    @Override
    public long getItemId(int position) {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getsId() : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if(v == null) {
            v = mInflater.inflate(R.layout.spinner_item_services, parent, false);
            holder = new ViewHolder();
            holder.txt_service_name = (TextView) v.findViewById(R.id.txt_service_name);
/*
            holder.txt_postcode = (TextView) v.findViewById(R.id.txt_postcode);
*/
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        // fill row data
        Services currentItem = getItem(position);
        if(currentItem != null) {
            holder.txt_service_name.setText(currentItem.getsName());
         /*   holder.txt_postcode.setText(currentItem.getsName());*/
        }

        return v;
    }
    public List<Services> getItems() {
        return mItems;
    }

    public void setItems(List<Services> mItems) {
        this.mItems = mItems;
    }

    class ViewHolder {
        TextView txt_service_name;
        TextView txt_postcode;
    }
}