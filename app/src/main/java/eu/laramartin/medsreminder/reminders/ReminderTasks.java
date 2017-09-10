package eu.laramartin.medsreminder.reminders;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.laramartin.medsreminder.R;

import static eu.laramartin.medsreminder.reminders.RemindersUtility.scheduleAlarm;

public class ReminderTasks {

    static final String ACTION_MED_REMINDER = "med_reminder";
    private static final String LOG_TAG = ReminderTasks.class.getCanonicalName();

    public static void executeTask(Context context, String action, Bundle medBundle) {
        if (ACTION_MED_REMINDER.equals(action)) {
            createMedReminder(context, medBundle);
        }
    }

    private static void createMedReminder(Context context, Bundle bundleMed) {

        // TODO: 10.09.17 Lara: schedule a job for same med in 7 days
        int calendarDay = bundleMed.getInt(context.getString(R.string.reminder_day_bundle_key));
        String time = bundleMed.getString(context.getString(R.string.reminder_time_bundle_key));
        String reminderJobTag = bundleMed.getString(context.getString(R.string.reminder_tag_bundle_key));
        Log.i(LOG_TAG, "calendar day:" + calendarDay + " time: " + time);
        scheduleAlarm(context, time, calendarDay, reminderJobTag);
        NotificationUtils.remindUserToTakeMed(context);
    }
}
