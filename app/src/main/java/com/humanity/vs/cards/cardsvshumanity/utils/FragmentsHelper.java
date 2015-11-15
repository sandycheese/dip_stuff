package com.humanity.vs.cards.cardsvshumanity.utils;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by robot on 15.11.15.
 */
public class FragmentsHelper {
    public static void setFragment(Activity a, int layout, Fragment f) {
        FragmentTransaction transaction = a.getFragmentManager().beginTransaction();
        transaction.replace(layout, f, f.getClass().toString());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.commit();
    }
}
