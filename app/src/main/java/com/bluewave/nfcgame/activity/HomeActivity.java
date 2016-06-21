package com.bluewave.nfcgame.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.bluewave.nfcgame.R;
import com.bluewave.nfcgame.adapter.RoomAdapter;
import com.bluewave.nfcgame.base.BaseActivity;
import com.bluewave.nfcgame.common.Const;
import com.bluewave.nfcgame.common.Global;
import com.bluewave.nfcgame.model.Room;
import com.bluewave.nfcgame.net.Client;
import com.bluewave.nfcgame.net.RoomClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Developer on 2016-06-17.
 */
public class HomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.lv_room)
    ListView lvRoom;

    RoomAdapter adapterRoom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        adapterRoom = new RoomAdapter(this, R.layout.row_room);
        lvRoom.setAdapter(adapterRoom);

        swipeContainer.setOnRefreshListener(this);

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);

                requestRoomList();
            }
        });
    }

    @OnClick(R.id.btn_create)
    void onClickCreate()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_create_room, null);

        builder.setTitle(getString(R.string.create_room))
                .setView(view).
                setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etName = (EditText) view.findViewById(R.id.et_name);
                EditText etLimit = (EditText) view.findViewById(R.id.et_limit);
                String name = etName.getText().toString();
                String limit = etLimit.getText().toString();
                if(TextUtils.isEmpty(name)){
                    showToast(R.string.input_room_name);
                    return;
                }
                if(TextUtils.isEmpty(limit))
                {
                    showToast(R.string.input_limit);
                    return;
                }
                dialog.dismiss();
                RoomClient.create(Global.getLoginUser().uid ,name, limit, new Client.Handler() {
                    @Override
                    public void onSuccess(Object object) {
                        showToast(R.string.success_create_room);
                        Room room = (Room)object;
                        enterRoom(room);
                    }

                    @Override
                    public void onFail() {
                        showToast(R.string.fail_create_room);
                    }
                },getProgressDialog());
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void enterRoom(final Room room)
    {
        RoomClient.enter(Global.getLoginUser().uid, room.uid, new Client.Handler() {
            @Override
            public void onSuccess(Object object) {
                startGameActivity(room);
                showToast(R.string.success_enter_room);
            }

            @Override
            public void onFail() {
                showToast(R.string.fail_enter_room);
            }
        },getProgressDialog());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Const.REQUEST_GAME)
        {
            requestRoomList();
        }
    }

    @OnClick(R.id.btn_enter)
    void onClickEnter()
    {
        int selectPos = lvRoom.getCheckedItemPosition();

        if(selectPos != AdapterView.INVALID_POSITION)
        {
            Room room = adapterRoom.getItem(selectPos);
            enterRoom(room);
        }
        else
        {
            showToast(R.string.empty_select_room);
        }
    }

    private void requestRoomList()
    {
        adapterRoom.clear();

        RoomClient.getList(new Client.Handler() {
            @Override
            public void onSuccess(Object object) {
                swipeContainer.setRefreshing(false);
                ArrayList<Room> result = (ArrayList<Room>)object;
                adapterRoom.addAll(result);
            }

            @Override
            public void onFail() {
                swipeContainer.setRefreshing(false);
            }
        }, getProgressDialog());
    }

    @Override
    public void onRefresh() {
        requestRoomList();
    }
}

