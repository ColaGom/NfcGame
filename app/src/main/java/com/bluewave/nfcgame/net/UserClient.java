package com.bluewave.nfcgame.net;

import android.os.Handler;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bluewave.nfcgame.model.User;
import com.google.gson.Gson;
import com.navercorp.volleyextensions.volleyer.Volleyer;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Developer on 2016-06-17.
 */
public class UserClient extends Client {
    private final static String TAG = "UserClient";
    private final static String URL = "http://shindnjswn.cafe24.com/api/user.php";

    private final static String TAG_LOGIN = "login";

    public static void login(String nickname, final Handler handler, final SweetAlertDialog dialog)
    {
        dialog.setTitleText("로그인...");
        dialog.show();

        Volleyer.volleyer().post(URL)
                .addStringPart(NAME_TAG, TAG_LOGIN)
                .addStringPart("nickname", nickname)
                .withListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, TAG_LOGIN + " response : " + response);
                        dialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("error")){
                                handler.onFail();
                            }else{
                                User user = new Gson().fromJson(jsonObject.getString("user"), User.class);
                                handler.onSuccess(user);
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
                        Log.d(TAG, TAG_LOGIN + " error");
                    }
                })
                .execute();
    }
}
