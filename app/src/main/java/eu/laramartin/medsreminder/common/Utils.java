package eu.laramartin.medsreminder.common;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.view.View;

import static android.content.Context.VIBRATOR_SERVICE;

public class Utils {

    public static void runVibration(View view) {
        AudioManager am = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_VIBRATE:
            case AudioManager.RINGER_MODE_NORMAL:
                ((Vibrator) view.getContext().getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                break;
        }
    }
}
