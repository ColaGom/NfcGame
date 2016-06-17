package com.bluewave.nfcgame.net;

/**
 * Created by Developer on 2016-06-17.
 */
public class Client {
    protected final static String NAME_TAG = "tag";

    public interface Handler
    {
        void onSuccess(Object object);
        void onFail();
    }
}
