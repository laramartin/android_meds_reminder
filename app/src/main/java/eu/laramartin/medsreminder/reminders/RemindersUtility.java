package eu.laramartin.medsreminder.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.view.View;

import java.util.List;

import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.common.CalendarUtility.getMedDays;
import static eu.laramartin.medsreminder.common.CalendarUtility.getMedTimeSplit;
import static eu.laramartin.medsreminder.common.CalendarUtility.timeToNextGivenDay;

public class RemindersUtility {

    public static void scheduleMedReminder(Context context, Med med) {

        // days of the week when med has to be taken
        // "MoTu" would be [2, 3]
        List<Integer> calendarDays = getMedDays(med);
        String[] timeSplit = getMedTimeSplit(med);
        int hours = Integer.valueOf(timeSplit[0]);
        int minutes = Integer.valueOf(timeSplit[1]);

        for (Integer calendarDay : calendarDays) {
            long diffInMillis = timeToNextGivenDay(calendarDay, hours, minutes);

            // https://developer.android.com/training/scheduling/alarms.html
            // Wake up the device to fire a one-time (non-repeating) alarm in 5 seconds:
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReminderReceiver.class);
            int pendingIntentId = (int) System.currentTimeMillis();
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, pendingIntentId, intent, 0);

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            diffInMillis, alarmIntent);
        }


        // TODO: 09.09.17 Lara: remove this!!! I kept it for testing
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, ReminderReceiver.class);
//        int id = (int) System.currentTimeMillis();
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, intent, 0);
//
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() +
//                        5 * 1000, alarmIntent);
    }

    public static boolean isSwitchActivated(View view) {
        SwitchCompat currentSwitch = (SwitchCompat) view;
        return currentSwitch.isChecked();
    }

    @NonNull
    public static String buildSwitchReminderKey(Med med) {
        return "reminder".concat(med.getKey());
    }
}
