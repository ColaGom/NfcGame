package com.bluewave.nfcgame.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Developer on 2016-06-17.
 */
public class Room implements Serializable {
    public static int STATE_WAIT = 0;
    public static int STATE_GAME = 1;
    public static int STATE_END = 2;
    public int uid;
    public String name;
    public int state;
    public int limit_count;
    public int join_count;
    public int tagger_uid;
    public String start_at;
    public String insert_at;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public String getCountString() {
        return String.format("%d/%d", join_count, limit_count);
    }

    public String getStateString() {
        if (state == STATE_WAIT) {
            return "대기중";
        } else if (state == STATE_GAME) {
            return "게임 진행중";
        } else
            return "";
    }
    public Date getStartTime() {
        try {
            return format.parse(start_at);
        } catch (Exception e) {
           return new Date();
        }
    }

    public int getElapedSeconds()
    {
        return (int)((new Date().getTime() - getStartTime().getTime()) / 1000);
    }
}
