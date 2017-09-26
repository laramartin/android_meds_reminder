package eu.laramartin.medsreminder.firebase;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import eu.laramartin.medsreminder.model.Med;
import eu.laramartin.medsreminder.model.Permission;

public class AnalyticsUtility {

    private static final String MED_TAKE = "med_take";
    private static final String FRIEND_INVITED = "friend_invited";

    public static void takeMed(FirebaseAnalytics firebaseAnalytics, Med med) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, med.getKey());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, med.getName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, med.getDosage());
        firebaseAnalytics.logEvent(MED_TAKE, bundle);
    }

    public static void friendInvited(FirebaseAnalytics firebaseAnalytics, Permission permission) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, permission.getEmail());
        firebaseAnalytics.logEvent(FRIEND_INVITED, bundle);
    }
}
