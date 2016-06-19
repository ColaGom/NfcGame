package com.bluewave.nfcgame.net;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bluewave.nfcgame.model.Room;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.navercorp.volleyextensions.volleyer.Volleyer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Developer on 2016-06-17.
 */
public class GameClient extends Client {
    private final static String TAG = "GameClient";
    private final static String URL = "http://shindnjswn.cafe24.com/api/game.php";

    private final static String TAG_UPDATE = "update";
    private final static String TAG_GET_INFO = "get_info";

    public static void getInfo(int room_uid, final Handler handler, final SweetAlertDialog dialog)
    {
        dialog.setTitleText("게임 정보 불러오는중...");
        dialog.show();

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_GET_INFO)
                .addStringPart("room_uid", room_uid + "")
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d(TAG, TAG_GET_INFO + " response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("error")){
                                handler.onFail();
                            }else{
                                handler.onSuccess(jsonObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.onFail();
                        }
                    }
                })
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handler.onFail();
                        dialog.dismiss();
                        Log.d(TAG, TAG_GET_INFO + " error");
                    }
                })
                .execute();
    }


    public static void update(int user_uid, int room_uid, LatLng latLng, final Handler handler)
    {
        String latLngString = latLng.latitude + "," + latLng.longitude;

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_UPDATE)
                .addStringPart("user_uid", user_uid + "")
                .addStringPart("room_uid", room_uid + "")
                .addStringPart("latlng", latLngString)
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_UPDATE + " response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("error")){
                                handler.onFail();
                            }else{
                                handler.onSuccess(jsonObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.onFail();
                        }

                    }
                })
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handler.onFail();
                        Log.d(TAG, TAG_UPDATE + " error");
                    }
                })
                .execute();
    }
}
