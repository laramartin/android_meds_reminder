package eu.laramartin.medsreminder.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

    private static final boolean DEFAULT_SWITCH_CHECKED_STATE = false;
    private SharedPreferences preferences;

    public Settings(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAlarmEnabled(String reminderMedKey, boolean isEnabled) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(reminderMedKey, isEnabled);
        editor.apply();
    }

    public boolean getAlarmEnabled(String reminderMedKey) {
        return preferences.getBoolean(reminderMedKey, DEFAULT_SWITCH_CHECKED_STATE);
    }
}
