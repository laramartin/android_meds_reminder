package eu.laramartin.medsreminder.reminders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.ArrayList;
import java.util.List;

import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.common.CalendarUtility.getMedDays;
import static eu.laramartin.medsreminder.common.CalendarUtility.millisToNextTargetDay;

public class RemindersUtility {

    private static final String LOG_TAG = RemindersUtility.class.getCanonicalName();
    private static final int SYNC_FLEXTIME_SECONDS = 10;

    public static void scheduleMedReminder(Context context, Med med) {

        // days of the week when med has to be taken
        // "MoTu" would be [2, 3]
        List<Integer> calendarDays = getMedDays(med);
        List<String> reminderJobTags = new ArrayList<>();
        String time = med.getTime();

        for (Integer calendarDay : calendarDays) {
            String reminderJobTag = String.valueOf(System.currentTimeMillis());
            reminderJobTags.add(reminderJobTag);
            scheduleAlarm(context, time, calendarDay, reminderJobTag);
        }

        med.setReminderJobTags(reminderJobTags);
        FirebaseUtility.updateMedOnDb(med);

        Log.i(LOG_TAG, "created alarm for: " + med.getKey());
    }

    public static void scheduleAlarm(Context context, String time, Integer calendarDay, String reminderJobTag) {
        Bundle medAlarmBundle = new Bundle();
        medAlarmBundle.putInt(context.getString(R.string.reminder_day_bundle_key), calendarDay);
        medAlarmBundle.putString(context.getString(R.string.reminder_time_bundle_key), time);
        medAlarmBundle.putString(context.getString(R.string.reminder_tag_bundle_key), reminderJobTag);

        String[] timeSplit = time.split(":");
        int hours = Integer.valueOf(timeSplit[0]);
        int minutes = Integer.valueOf(timeSplit[1]);

        int diffInSeconds = (int) (millisToNextTargetDay(calendarDay, hours, minutes) / 1000);

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job job = dispatcher.newJobBuilder()
                .setService(ReminderFirebaseJobService.class)
                .setTag(reminderJobTag)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(
                        diffInSeconds,
                        diffInSeconds + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .setExtras(medAlarmBundle)
                .build();
        dispatcher.schedule(job);

        Log.i(LOG_TAG, "created alarm for: " + calendarDay +
                " diff in seconds: " + diffInSeconds);
    }

    @NonNull
    public static String buildSwitchReminderKey(Med med) {
        return "reminder".concat(med.getKey());
    }

    public static void cancelMedReminder(Context context, List<String> reminderJobTags) {
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        for (String reminderJobTag : reminderJobTags) {
            dispatcher.cancel(reminderJobTag);
        }
    }
}
