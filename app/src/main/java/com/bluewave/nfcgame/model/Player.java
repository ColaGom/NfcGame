package com.bluewave.nfcgame.model;

import android.text.TextUtils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by Developer on 2016-06-20.
 */
public class Player {
    //도망자
    public static int TYPE_RUNNER = 0;
    //술래
    public static int TYPE_TAGGER = 1;
    //잡힘
    public static int TYPE_END_RUNNGER = 2;


    public int uid;
    public int room_uid;
    public int user_uid;
    public String user_name;
    public int type;
    public String latlng;

    private LatLng getLatLng()
    {
        if(TextUtils.isEmpty(latlng))
            return null;

        String [] arr = latlng.split(",");

        return new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]));
    }

    public boolean isValidLatLng()
    {
        return !TextUtils.isEmpty(latlng);
    }

    public MarkerOptions getMarker()
    {
        MarkerOptions options = new MarkerOptions().title(user_name).position(getLatLng());

        if(type == TYPE_RUNNER)
        {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
        else if(type == TYPE_TAGGER)
        {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        return options;
    }
}
