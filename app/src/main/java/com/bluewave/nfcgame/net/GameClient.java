package com.bluewave.nfcgame.net;

import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bluewave.nfcgame.model.Player;
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

    private final static String TAG_UPDATE_TYPE = "update_type";
    private final static String TAG_UPDATE_LATLNG = "update_latlng";
    private final static String TAG_GET_INFO = "get_info";
    private final static String TAG_START = "start";
    private final static String TAG_END = "end";

    public static void endGame(int room_uid, final Handler handler) {
        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_END)
                .addStringPart("room_uid", room_uid + "")
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_END + " response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                handler.onFail();
                            } else {
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
                        Log.d(TAG, TAG_END + " error");
                    }
                })
                .execute();
    }

    public static void startGame(int room_uid, final Handler handler, final SweetAlertDialog dialog) {
        dialog.setTitleText("게임 시작중..");
        dialog.show();

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_START)
                .addStringPart("room_uid", room_uid + "")
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.d(TAG, TAG_START + " response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                handler.onFail();
                            } else {
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
                        dialog.dismiss();
                        handler.onFail();
                        Log.d(TAG, TAG_START + " error");
                    }
                })
                .execute();
    }


    public static void getInfo(int room_uid, final Handler handler) {
        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_GET_INFO)
                .addStringPart("room_uid", room_uid + "")
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, TAG_GET_INFO + " response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                handler.onFail();
                            } else {
                                JSONArray jsonArray = jsonObject.getJSONArray("players");
                                Gson gson = new Gson();
                                ArrayList<Player> result = new ArrayList<Player>();

                                for (int i = 0; i < jsonArray.length(); ++i) {
                                    result.add(gson.fromJson(jsonArray.get(i).toString(), Player.class));
                                }

                                handler.onSuccess(result);
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
                        Log.d(TAG, TAG_GET_INFO + " error");
                    }
                })
                .execute();
    }

    public static void updateType(int user_uid, int room_uid, int type,final Handler handler)
    {
        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_UPDATE_TYPE)
                .addStringPart("user_uid", user_uid + "")
                .addStringPart("room_uid", room_uid + "")
                .addStringPart("type", type + "")
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_UPDATE_TYPE + " response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                handler.onFail();
                            } else {
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
                        Log.d(TAG, TAG_UPDATE_TYPE + " error");
                    }
                })
                .execute();
    }

    public static void updateLatLng(int user_uid, int room_uid, LatLng latLng, final Handler handler) {
        String latLngString = latLng.latitude + "," + latLng.longitude;

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_UPDATE_LATLNG)
                .addStringPart("user_uid", user_uid + "")
                .addStringPart("room_uid", room_uid + "")
                .addStringPart("latlng", latLngString)
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_UPDATE_LATLNG + " response : " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("error")) {
                                handler.onFail();
                            } else {
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
                        Log.d(TAG, TAG_UPDATE_LATLNG + " error");
                    }
                })
                .execute();
    }
}
