package com.bluewave.nfcgame.net;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bluewave.nfcgame.model.Room;
import com.bluewave.nfcgame.model.User;
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
public class RoomClient extends Client {
    private final static String TAG = "RoomClient";
    private final static String URL = "http://shindnjswn.cafe24.com/api/room.php";

    private final static String TAG_CREATE = "create";
    private final static String TAG_ENTER = "enter";
    private final static String TAG_EXIT = "exit";
    private final static String TAG_GET_LIST = "get_list";

    public static void enter(int user_uid, int room_uid, final Handler handler, final SweetAlertDialog dialog)
    {
        dialog.setTitleText("입장중...");
        dialog.show();

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_ENTER)
                .addStringPart("user_uid", user_uid + "")
                .addStringPart("room_uid", room_uid + "")
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_ENTER + " response : " + response);
                        dialog.dismiss();
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
                        dialog.dismiss();
                        handler.onFail();
                        Log.d(TAG, TAG_ENTER + " error");
                    }
                })
                .execute();
    }

    public static void exit(int user_uid, int room_uid, final Handler handler, final SweetAlertDialog dialog)
    {
        dialog.setTitleText("퇴장중...");
        dialog.show();

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_EXIT)
                .addStringPart("user_uid", user_uid + "")
                .addStringPart("room_uid", room_uid + "")
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_EXIT + " response : " + response);
                        dialog.dismiss();
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
                        dialog.dismiss();
                        handler.onFail();
                        Log.d(TAG, TAG_EXIT + " error");
                    }
                })
                .execute();
    }

    public static void create(String name,String limit, final Handler handler, final SweetAlertDialog dialog)
    {
        dialog.setTitleText("방 생성...");
        dialog.show();

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_CREATE)
                .addStringPart("name", name)
                .addStringPart("limit", limit)
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_CREATE + " response : " + response);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("error")){
                                handler.onFail();
                            }else{
                                handler.onSuccess(new Gson().fromJson(jsonObject.getString("room"), Room.class));
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
                        Log.d(TAG, TAG_CREATE + " error");
                    }
                })
                .execute();
    }

    public static void getList(final Handler handler, final SweetAlertDialog dialog)
    {
        dialog.setTitleText("정보 불러오는중...");
        dialog.show();

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_GET_LIST)
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_GET_LIST + " response : " + response);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("error")){
                                handler.onFail();
                            }else{
                                ArrayList<Room> result = new ArrayList<Room>();
                                JSONArray jsonArray = jsonObject.getJSONArray("rooms");
                                Gson gson = new Gson();

                                for(int i = 0 ; i < jsonArray.length(); ++i)
                                {
                                    result.add(gson.fromJson(jsonArray.get(i).toString(), Room.class));
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
                        dialog.dismiss();
                        handler.onFail();
                        Log.d(TAG, TAG_GET_LIST + " error");
                    }
                })
                .execute();
    }
}
