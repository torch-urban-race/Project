package com.example.torchapp.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.torchapp.R;
import com.example.torchapp.database.DatabaseFacade;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private static String torchTitle;
    private static String creator;
    private static String creationDate;
    private static String distanceTraveled;
    final View v;

    public CustomInfoWindowAdapter(Context context){
        mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.recyclevview_torch_item, null);

    }



    @Override
    public View getInfoWindow(Marker marker) {
       return null;
    }

    @Override
    public View getInfoContents(Marker marker) {

        DatabaseFacade.getTorchInformation((Integer)marker.getTag());

        TextView titleTextView = v.findViewById(R.id.torch_title);
        TextView creatorTextView = v.findViewById(R.id.torch_creator);
        TextView creationDateTextView = v.findViewById(R.id.torch_creation_date);
        TextView distanceTraveledTextView = v.findViewById(R.id.torch_distance_traveled);

        titleTextView.setText(torchTitle);
        creatorTextView.setText(creator);
        creationDateTextView.setText(creationDate);
        distanceTraveledTextView.setText(distanceTraveled);


        //TODO: add code to get the correct text for this marker



        return v;
    }


    public static void setInfoContents(String[] params){
        torchTitle = params[0];
        creator = params[1];
        creationDate = params[2];
        distanceTraveled = params[3];
    }
}
