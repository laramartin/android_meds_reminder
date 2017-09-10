package eu.laramartin.medsreminder.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.common.CalendarUtility.getMedDays;
import static eu.laramartin.medsreminder.common.CalendarUtility.getMedTimeSplit;
import static eu.laramartin.medsreminder.common.CalendarUtility.timeToNextGivenDay;

public class RemindersUtility {

    private static final String LOG_TAG = RemindersUtility.class.getCanonicalName();

    public static void scheduleMedReminder(Context context, Med med) {

        // days of the week when med has to be taken
        // "MoTu" would be [2, 3]
        List<Integer> calendarDays = getMedDays(med);
        String[] timeSplit = getMedTimeSplit(med);
        int hours = Integer.valueOf(timeSplit[0]);
        int minutes = Integer.valueOf(timeSplit[1]);
        List<Integer> reminderPendingIntentIds = new ArrayList<>();

        for (Integer calendarDay : calendarDays) {
            long diffInMillis = timeToNextGivenDay(calendarDay, hours, minutes);

            // https://developer.android.com/training/scheduling/alarms.html
            // Wake up the device to fire a one-time (non-repeating) alarm in 5 seconds:
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReminderReceiver.class);
            int pendingIntentId = (int) System.currentTimeMillis();
            reminderPendingIntentIds.add(pendingIntentId);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, pendingIntentId, intent, 0);

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() +
                            diffInMillis, alarmIntent);
            Log.i(LOG_TAG, "created alarm for: " + calendarDay +
                    " diff in millis: " + diffInMillis);
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

        med.setReminderPendingIntentIds(reminderPendingIntentIds);
        FirebaseUtility.updateMedOnDb(med);

        Log.i(LOG_TAG, "created alarm for: " + med.getKey());
    }

    @NonNull
    public static String buildSwitchReminderKey(Med med) {
        return "reminder".concat(med.getKey());
    }

    public static void cancelMedReminder(Context context, List<Integer> reminderPendingIntentIds) {
        for (int pendingIntentId : reminderPendingIntentIds) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                Intent intent = new Intent(context, ReminderReceiver.class);
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, pendingIntentId, intent, 0);
                alarmManager.cancel(alarmIntent);
            }
        }
    }
}
