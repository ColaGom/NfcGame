package com.bluewave.nfcgame.common;

import com.bluewave.nfcgame.model.User;

/**
 * Created by Developer on 2016-06-18.
 */
public class Global {
    private static User loginUser;

    public static void setLoginUser(User user) {
        loginUser = user;
    }

    public static User getLoginUser() {
        return loginUser;
    }
}
