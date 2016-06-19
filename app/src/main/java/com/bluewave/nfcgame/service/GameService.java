package com.bluewave.nfcgame.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.bluewave.nfcgame.common.Global;
import com.bluewave.nfcgame.model.Room;
import com.bluewave.nfcgame.net.Client;
import com.bluewave.nfcgame.net.GameClient;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Developer on 2016-06-18.
 */
public class GameService extends Service implements LocationListener {

    private IBinder mBinder = new LocalBinder();

    private Callback mCallback;
    private LocationManager mLocationManager;
    private Location mLastLocation;
    private Room mRoom;

    public GameService(Callback callback, Room room)
    {
        mCallback = callback;
        mRoom = room;
    }

    private void updateMyLocation(LatLng latlng)
    {
        GameClient.update(Global.getLoginUser().uid, mRoom.uid, latlng, new Client.Handler() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location)
    {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        updateMyLocation(latLng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(mCallback != null) {
                mCallback.requestPermission();
            }
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        mLastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateMyLocation(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

    public interface Callback
    {
        void requestPermission();
        void onChangeLocation();
    }
}
