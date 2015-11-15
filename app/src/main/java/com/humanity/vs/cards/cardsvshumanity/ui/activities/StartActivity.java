package com.humanity.vs.cards.cardsvshumanity.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.ui.async.AsyncStartActivityResourceLoading;
import com.humanity.vs.cards.cardsvshumanity.utils.WiFiDirectChecker;

/**
 * Created by robot on 15.11.15.
 */
public class StartActivity extends Activity {

    ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findComponents();
        loadResources();
    }

    void findComponents() {
        progressBar = (ProgressBar) findViewById(R.id.pbLoading);
    }

    void loadResources() {
        new AsyncStartActivityResourceLoading(this).execute();
    }

    public void setProgressBarValue(int progress) {
        if (progressBar != null)
            progressBar.setProgress(progress);
    }

    public void loadMainActivity() {

        if (!WiFiDirectChecker.isWifiDirectSupported(this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.text_sorry));
            builder.setMessage(getString(R.string.msg_widi_unsupported_device));
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.text_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            builder.show();
        } else {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
