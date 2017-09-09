package eu.laramartin.medsreminder.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.List;

import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.common.CalendarUtility.getMedDays;
import static eu.laramartin.medsreminder.common.CalendarUtility.getMedTimeSplit;
import static eu.laramartin.medsreminder.common.CalendarUtility.timeToNextGivenDay;

public class RemindersUtility {

    private static boolean isInitialized;

    // synchronized to not have this method executed more than once at the time
    synchronized public static void scheduleMedReminder(Context context, Med med) {
        if (isInitialized) {
            return;
        }
        int REMINDER_INTERVAL_SECONDS = 0;

        // days of the week when med has to be taken
        // "MoTu" would be [2, 3]
        List<Integer> calendarDays = getMedDays(med);
        String[] timeSplitted = getMedTimeSplit(med);
        int hours = Integer.valueOf(timeSplitted[0]);
        int minutes = Integer.valueOf(timeSplitted[1]);

        // TODO: 09.09.17 Lara: for each day to take med, calculate day and hours difference in seconds
        // TODO: 09.09.17 Lara: and schedule a new job/alarm
        for (Integer calendarDay : calendarDays) {
            long diffInSeconds = timeToNextGivenDay(calendarDay, hours, minutes) / 1000;

        }

        // https://developer.android.com/training/scheduling/alarms.html
        // Wake up the device to fire a one-time (non-repeating) alarm in 5 seconds:
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() +
                        5 * 1000, alarmIntent);

        isInitialized = true;
    }
}
