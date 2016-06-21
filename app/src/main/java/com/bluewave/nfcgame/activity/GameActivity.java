package com.bluewave.nfcgame.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluewave.nfcgame.R;
import com.bluewave.nfcgame.base.BaseActivity;
import com.bluewave.nfcgame.common.Const;
import com.bluewave.nfcgame.common.Global;
import com.bluewave.nfcgame.model.Player;
import com.bluewave.nfcgame.model.Room;
import com.bluewave.nfcgame.net.Client;
import com.bluewave.nfcgame.net.GameClient;
import com.bluewave.nfcgame.net.RoomClient;
import com.bluewave.nfcgame.service.GameService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends BaseActivity implements OnMapReadyCallback, GameService.Callback, NfcAdapter.CreateNdefMessageCallback {

    @BindView(R.id.tv_info)
    TextView tvInfo;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.btn_start)
    Button btnStart;

    @BindView(R.id.tv_state)
    TextView tvState;

    @BindView(R.id.root)
    RelativeLayout root;

    @BindView(R.id.root_popup)
    View popup;

    @BindView(R.id.tv_msg)
    TextView tvMsg;

    @OnClick(R.id.btn_start)
    void onClickStart() {
        if (mRoom.join_count < 2) {
            showToast("최소 2명 이상 시작가능.");
            return;
        }
        GameClient.startGame(mRoom.uid, new Client.Handler() {
            @Override
            public void onSuccess(Object object) {
                showToast(R.string.success_start_game);
                btnStart.setVisibility(View.GONE);
                tvState.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFail() {

            }
        }, getProgressDialog());
    }

    @OnClick(R.id.root_popup)
    void onClickPopup()
    {
        popup.setVisibility(View.GONE);
    }

    private GoogleMap mMap;
    private Room mRoom;
    private GameService mService;
    private Timer mTimer;
    private Timer mMapTimer;
    private Intent gameService;
    private boolean isFirst = true;
    private NfcAdapter nfcAdapter;
    private ArrayList<Player> mPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mRoom = (Room) getIntent().getSerializableExtra(Const.EXTRA_ROOM);

        gameService = new Intent(this, GameService.class);
        startService(gameService);

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestGameInfo();
                requestMapInfo();
            }
        }, 1000, 1000);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            showToast("NFC 를 지원하지 않습니다.");
            finish();
            return;
        }

        nfcAdapter.setNdefPushMessageCallback(this, this);

        setComponents();
    }

    @Override
    public void finish() {
        super.finish();
        mTimer.cancel();
        RoomClient.exit(Global.getLoginUser().uid, mRoom.uid, new Client.Handler() {
            @Override
            public void onSuccess(Object object) {
               // showToast(R.string.success_exit_room);
            }

            @Override
            public void onFail() {
              //  showToast(R.string.fail_exit_room);
            }
        });
    }

    private void setComponents() {
        if (mRoom != null && mRoom.tagger_uid != Global.getLoginUser().uid) {
            btnStart.setVisibility(View.GONE);
            tvState.setVisibility(View.VISIBLE);
            tvState.setText("대기중...");
        }
    }

    private void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);

        NdefMessage msg = (NdefMessage) rawMsgs[0];
        int sender_uid = Integer.parseInt(new String(msg.getRecords()[0].getPayload()));

        onTagging();
    }

    private void onTagging() {
        if(mRoom.state != Room.STATE_GAME) return;

        if (mRoom.tagger_uid == Global.getLoginUser().uid)
        {
            tvMsg.setText("잡았다");
        }
        else
        {
            tvMsg.setText("잡혔다");

            if(mRoom != null) {
                GameClient.updateType(Global.getLoginUser().uid, mRoom.uid, Player.TYPE_END_RUNNGER, new Client.Handler() {
                    @Override
                    public void onSuccess(Object object) {

                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        }

        runOnUiThread(new TimerTask() {
            @Override
            public void run() {
                popup.setVisibility(View.VISIBLE);
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showToast(R.string.need_permission);
            finish();
            return;
        }
        mService.enableGPS();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(gameService, mConnection, BIND_AUTO_CREATE);
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            processIntent(getIntent());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void requestMapInfo()
    {
        GameClient.getInfo(mRoom.uid, new Client.Handler() {
            @Override
            public void onSuccess(Object object) {
                mPlayers = (ArrayList<Player>) object;
                updateMap();
            }

            @Override
            public void onFail() {
              //  showToast(R.string.finished_game);
               // finish();
            }
        });
    }

    private void requestGameInfo() {
        runOnUiThread(new TimerTask() {
            @Override
            public void run() {

                RoomClient.getInfo(mRoom.uid, new Client.Handler() {
                    @Override
                    public void onSuccess(Object object) {
                        if(object != null)
                            mRoom = (Room) object;
                        tvInfo.setText("현재 인원 : " + mRoom.getCountString() + "\n" + "상태 : " + mRoom.getStateString());
                        if (mRoom.state == Room.STATE_GAME) {
                            int elapsedSec = mRoom.getElapedSeconds();
                            if (elapsedSec < 30) {
                                tvState.setText(String.format("%d 초후 게임이 시작됩니다.", 30 - elapsedSec));
                            } else {
                                int remainingSec = 60 * 20 - elapsedSec + 30;

                                tvTime.setText(String.format("남은 시간 : %02d:%02d", remainingSec / 60, remainingSec % 60));

                                int runnerCount = mPlayers.size() - 1;
                                int endRunnerCount = 0;

                                for(Player p: mPlayers)
                                {
                                    if(p.type == Player.TYPE_END_RUNNGER)
                                        endRunnerCount++;
                                }

                                if(mRoom.tagger_uid == Global.getLoginUser().uid)
                                {
                                    if(remainingSec <= 0) {
                                        GameClient.endGame(mRoom.uid, new Client.Handler() {
                                            @Override
                                            public void onSuccess(Object object) {
                                                tvState.setText("게임 종료");
                                            }

                                            @Override
                                            public void onFail() {

                                            }
                                        });
                                    }
                                    else if(runnerCount == endRunnerCount)
                                    {
                                        GameClient.endGame(mRoom.uid, new Client.Handler() {
                                            @Override
                                            public void onSuccess(Object object) {
                                                tvState.setText("게임 종료");
                                            }

                                            @Override
                                            public void onFail() {

                                            }
                                        });
                                    }
                                }

                                tvState.setText(String.format("잡은수 %d/%d",endRunnerCount, runnerCount));
                            }
                        }
                        else if(mRoom.state == Room.STATE_END)
                        {

                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
            }
        });
    }

    private void drawMap()
    {
        for (int i = 0; i < mPlayers.size(); ++i) {
            Player player = mPlayers.get(i);
            if (player.isValidLatLng()) {
                MarkerOptions options = player.getMarker();
                mMap.addMarker(options);

                if (isFirst && player.user_uid == Global.getLoginUser().uid) {
                    isFirst = false;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(options.getPosition(), 12));
                }
            }
        }
    }
    private void updateMap() {
        if(mRoom.state == Room.STATE_WAIT || mRoom.state == Room.STATE_END) {
            mMap.clear();
            drawMap();
        }
        else if(mRoom.state == Room.STATE_GAME)
        {
            if(getMyType() == Player.TYPE_TAGGER)
            {
                Date now = new Date();
                if(now.getTime() - preDate.getTime() > 20 * 1000)
                {
                    preDate = now;
                    mMap.clear();
                    drawMap();
                    Handler handler = new Handler();
                    handler.postDelayed(new TimerTask() {
                        @Override
                        public void run() {
                            mMap.clear();
                        }
                    },5000);
                }
            }
            else if(getMyType() == Player.TYPE_RUNNER)
            {
                mMap.clear();
            }
            else if(getMyType() == Player.TYPE_END_RUNNGER)
            {
                mMap.clear();
                drawMap();
            }
        }
    }

    private int getMyType()
    {
        for(Player p : mPlayers)
        {
            if(p.user_uid == Global.getLoginUser().uid)
            {
                return p.type;
            }
        }

        return Player.TYPE_RUNNER;
    }

    private Date preDate = new Date();


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
    }

    @Override
    public void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Const.REQUEST_PERMISSION_LOCATION);
    }

    @Override
    public void onChangeLocation() {

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(Const.TAG, "onServiceConnected");
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            mService = binder.getService();
            mService.setRoom(mRoom);
            mService.setCallback(GameActivity.this);
            mService.enableGPS();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = Global.getLoginUser().uid + "";
        NdefMessage msg = new NdefMessage(
                new NdefRecord[]{NdefRecord.createMime(
                        "application/com.bluewave.nfcgame.beam", text.getBytes())
                });
        onTagging();
        return msg;
    }
}
