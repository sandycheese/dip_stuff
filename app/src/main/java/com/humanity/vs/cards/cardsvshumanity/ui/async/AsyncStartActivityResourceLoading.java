package com.humanity.vs.cards.cardsvshumanity.ui.async;

import android.os.AsyncTask;
import android.os.RecoverySystem;

import com.humanity.vs.cards.cardsvshumanity.logic.repositories.CardsRepository;
import com.humanity.vs.cards.cardsvshumanity.ui.activities.StartActivity;

/**
 * Created by robot on 15.11.15.
 */
public class AsyncStartActivityResourceLoading extends AsyncTask<Void, Integer, Void> {

    StartActivity activity;

    public AsyncStartActivityResourceLoading(StartActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... params) {
        CardsRepository.getAllCards(activity, new RecoverySystem.ProgressListener() {
            @Override
            public void onProgress(int progress) {
                publishProgress(progress);
            }
        });

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        activity.setProgressBarValue(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        activity.checkedMainActivityStart();
    }
}
