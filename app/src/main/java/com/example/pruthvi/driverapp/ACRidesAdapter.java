package com.example.pruthvi.driverapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ACRidesAdapter extends ArrayAdapter<ACRides> implements View.OnClickListener{


    private ArrayList<ACRides> dataSet;
    Context mContext;


    private static class ViewHolder {
        TextView pName;
        TextView pDate;
        Button locButton;
        TextView pTime;
        TextView destination;
    }

    /**
     *
     * @param data
     * @param context
     */
    public ACRidesAdapter(ArrayList<ACRides> data, Context context) {
        super(context, R.layout.ac_rides, data);
        this.dataSet = data;
        this.mContext=context;

    }

    /**
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        int position=(Integer) view.getTag();
        Object object= getItem(position);
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return view of the Item row.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        // Get the data item for this position
        final ACRides ride = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {


            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.ac_rides, parent, false);
            viewHolder.pName = (TextView) convertView.findViewById(R.id.nameP);
            viewHolder.pDate = (TextView) convertView.findViewById(R.id.dateP);
            viewHolder.pTime= (TextView) convertView.findViewById(R.id.timeP);
            viewHolder.destination=(TextView)convertView.findViewById(R.id.destinationP);
            viewHolder.locButton=(Button)convertView.findViewById(R.id.getLocButton);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        final ViewGroup parentAc=parent;

        viewHolder.pName.setText(ride.getPassengerName());
        viewHolder.pDate.setText(ride.getDate());
        viewHolder.pTime.setText(ride.getTime());
        viewHolder.destination.setText(ride.getDestination());
        viewHolder.locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendMapIntent=new Intent(parentAc.getContext(),MapsActivity.class);
                sendMapIntent.putExtra("passengerNameM",ride.getPassengerName());
                parentAc.getContext().startActivity(sendMapIntent);
            }
        });
        // Return the completed view to render on screen


        return convertView;
    }
}
