package com.example.stylishh.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.HeaderViewListAdapter;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stylishh.R;
import com.example.stylishh.model.Availability;

public class ListAvailabilityAdapter extends BaseAdapter {
    public static final String TAG = "ListAvailabilityAdapter";

    private List<Availability> mItems;
    private LayoutInflater mInflater;

    public ListAvailabilityAdapter(Context context, List<Availability> servicesList)
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
    public Availability getItem(int position){
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position) : null;
    }
    @Override
    public long getItemId(int position)
    {
        return (getItems() != null && !getItems().isEmpty()) ? getItems().get(position).getaId() : position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        ViewHolder holder;
        if(v==null){
            v= mInflater.inflate(R.layout.list_item_availability, parent, false);
            holder = new ViewHolder();
            holder.dateTimeTXT = (TextView) v.findViewById(R.id.dateTime);
            holder.statusTXT = (TextView) v.findViewById(R.id.status);
            holder.servicesNameTXT = (TextView) v.findViewById(R.id.txt_service_name);
            v.setTag(holder);
        }
        else{
            holder = (ViewHolder) v.getTag();
        }

        //fill row data
        Availability currentItem = getItem(position);
        if(currentItem != null){
            holder.dateTimeTXT.setText(currentItem.getaDateTime());
            holder.statusTXT.setText(currentItem.getStatus());
            holder.servicesNameTXT.setText(currentItem.getServices().getsName());
        }
        return v;
    }
    public List<Availability> getItems()
    {return mItems;}

    public void setItems(List<Availability> mItems)
    {
        this.mItems = mItems;
    }

    class ViewHolder{
        TextView dateTimeTXT;
        TextView statusTXT;
        TextView servicesNameTXT;
    }
}
