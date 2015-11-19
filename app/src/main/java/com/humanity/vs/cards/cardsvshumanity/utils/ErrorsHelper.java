package com.humanity.vs.cards.cardsvshumanity.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.humanity.vs.cards.cardsvshumanity.R;

/**
 * Created by robot on 17.11.15.
 */
public class ErrorsHelper {
    public static void commonError(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.text_sorry);
        builder.setMessage(R.string.error_message_app_error);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.text_ok, null);

        builder.show();
    }

}
