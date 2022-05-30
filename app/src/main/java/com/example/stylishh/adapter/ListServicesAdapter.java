package com.example.stylishh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.stylishh.R;
import com.example.stylishh.model.Availability;
import com.example.stylishh.model.Services;

import java.util.List;

public class ListServicesAdapter extends BaseAdapter {
    public static final String TAG = "ListServicesAdapter";

    private List<Services> mItems;
    private LayoutInflater mInflater;

    public ListServicesAdapter(Context context, List<Services> servicesList)
    {
        this.setItems(servicesList);
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount()
    {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().size() : 0;
    }
    @Override
    public Services getItem(int position){
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null;
    }
    @Override
    public long getItemId(int position)
    {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getsId() : position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        ViewHolder holder;
        if(v==null){
            v= mInflater.inflate(R.layout.list_item_services, parent, false);
            holder = new ViewHolder();
            holder.servicesNameTXT = (TextView) v.findViewById(R.id.txt_service_name);
            holder.postcodeTXT = (TextView) v.findViewById(R.id.txt_postcode);

            v.setTag(holder);
        }
        else{
            holder = (ViewHolder) v.getTag();
        }

        //fill row data
        Services currentItem = getItem(position);
        if(currentItem != null){
            holder.servicesNameTXT.setText(currentItem.getsName());
            holder.postcodeTXT.setText(currentItem.getsPostcode());
        }
        return v;
    }
    public List<Services> getItems()
    {return mItems;}
    public void setItems(List<Services> mItems)
    {
        this.mItems = mItems;
    }
    class ViewHolder{
        TextView servicesNameTXT;
        TextView postcodeTXT;
    }
}
