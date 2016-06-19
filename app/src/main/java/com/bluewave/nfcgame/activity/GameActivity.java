package com.bluewave.nfcgame.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.bluewave.nfcgame.R;
import com.bluewave.nfcgame.base.BaseActivity;
import com.bluewave.nfcgame.common.Const;
import com.bluewave.nfcgame.common.Dialoger;
import com.bluewave.nfcgame.model.Room;
import com.bluewave.nfcgame.net.Client;
import com.bluewave.nfcgame.net.GameClient;
import com.bluewave.nfcgame.service.GameService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends BaseActivity implements OnMapReadyCallback, GameService.Callback {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Room mRoom;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mRoom = (Room)getIntent().getSerializableExtra(Const.EXTRA_ROOM);
    }

    private void requestGameInfo()
    {
        GameClient.getInfo(mRoom.uid, new Client.Handler() {
            @Override
            public void onSuccess(Object object) {
            }

            @Override
            public void onFail() {

            }
        }, Dialoger.createProgressDialog(GameActivity.this, true));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestGameInfo();
            }
        },5000, 0);
    }

    @Override
    public void requestPermission() {
        ActivityCompat.requestPermissions(this ,new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }, Const.REQUEST_PERMISSION_LOCATION);
    }

    @Override
    public void onChangeLocation() {

    }
}
