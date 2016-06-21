package com.bluewave.nfcgame.base;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bluewave.nfcgame.activity.GameActivity;
import com.bluewave.nfcgame.activity.HomeActivity;
import com.bluewave.nfcgame.activity.MainActivity;
import com.bluewave.nfcgame.common.Const;
import com.bluewave.nfcgame.model.Room;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Developer on 2016-06-17.
 */
public class BaseActivity extends AppCompatActivity {


    protected void startMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void startHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    protected void startGameActivity(Room room)
    {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(Const.EXTRA_ROOM, room);
        startActivityForResult(intent, Const.REQUEST_GAME);
    }

    protected SweetAlertDialog getProgressDialog()
    {
        SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setCancelable(false);
        return dialog;
    }

    protected void hideActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    protected void showToast(int id) {
        showToast(getString(id));
    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
