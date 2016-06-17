package com.bluewave.nfcgame.model;

import java.io.Serializable;

/**
 * Created by Developer on 2016-06-17.
 */
public class Room implements Serializable {
    public int uid;
    public String name;
    public int limit_count;
    public int join_count;

    public String getCountString()
    {
        return String.format("%d/%d", join_count,limit_count);
    }
}
