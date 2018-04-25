
package com.example.matt.locatecafets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by matt on 09/04/18.
 */

public class MyInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private int[] idImages = {
            R.drawable.aura,
            R.drawable.carelia,
            R.drawable.futura,
            R.drawable.louhi,
            R.drawable.puisto,
            R.drawable.forum,
            R.drawable.melta
    };

    public MyInfoWindow(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.info_window, null);
        final InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        TextView locationTitle = view.findViewById(R.id.location_title_window);
        locationTitle.setText(marker.getTitle());

        ImageView image = view.findViewById(R.id.image_view);
        image.setImageResource(idImages[infoWindowData.getId()]);

        /*TextView details_tv = view.findViewById(R.id.details);
        ImageView img = view.findViewById(R.id.pic);

        TextView hotel_tv = view.findViewById(R.id.hotels);
        TextView food_tv = view.findViewById(R.id.food);
        TextView transport_tv = view.findViewById(R.id.transport);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        int imageId = context.getResources().getIdentifier(infoWindowData.getImage().toLowerCase(),
                "drawable", context.getPackageName());
        img.setImageResource(imageId);

        hotel_tv.setText(infoWindowData.getHotel());
        food_tv.setText(infoWindowData.getFood());
        transport_tv.setText(infoWindowData.getTransport());
        */


        return view;
    }
}
