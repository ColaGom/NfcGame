package com.bluewave.nfcgame.common;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Developer on 2016-06-17.
 */
public class Dialoger {

    public static SweetAlertDialog createProgressDialog(Context c, boolean cancel)
    {
        SweetAlertDialog dialog = new SweetAlertDialog(c, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setCancelable(cancel);

        return dialog;
    }
}
