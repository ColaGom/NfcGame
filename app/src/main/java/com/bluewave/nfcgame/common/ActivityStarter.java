package com.bluewave.nfcgame.common;

import android.content.Context;
import android.content.Intent;

import com.bluewave.nfcgame.activity.GameActivity;
import com.bluewave.nfcgame.activity.HomeActivity;
import com.bluewave.nfcgame.activity.MainActivity;
import com.bluewave.nfcgame.model.Room;
import com.google.android.gms.games.Game;

/**
 * Created by Developer on 2016-06-17.
 */
public class ActivityStarter {

    public static void startMainActivity(Context c)
    {
        Intent intent = new Intent(c, MainActivity.class);
        c.startActivity(intent);
    }

    public static void startHomeActivity(Context c)
    {
        Intent intent = new Intent(c, HomeActivity.class);
        c.startActivity(intent);
    }

    public static void startGameActivity(Context c, Room room)
    {
        Intent intent = new Intent(c, GameActivity.class);
        intent.putExtra(Const.EXTRA_ROOM, room);
        c.startActivity(intent);
    }
}
