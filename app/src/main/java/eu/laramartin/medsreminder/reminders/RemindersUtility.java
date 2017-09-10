package eu.laramartin.medsreminder.reminders;

import android.content.Context;
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

import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.common.CalendarUtility.getMedDays;
import static eu.laramartin.medsreminder.common.CalendarUtility.getMedTimeSplit;
import static eu.laramartin.medsreminder.common.CalendarUtility.timeToNextGivenDay;

public class RemindersUtility {

    private static final String LOG_TAG = RemindersUtility.class.getCanonicalName();
    private static final int SYNC_FLEXTIME_SECONDS = 30;

    public static void scheduleMedReminder(Context context, Med med) {

        // days of the week when med has to be taken
        // "MoTu" would be [2, 3]
        List<Integer> calendarDays = getMedDays(med);
        String[] timeSplit = getMedTimeSplit(med);
        int hours = Integer.valueOf(timeSplit[0]);
        int minutes = Integer.valueOf(timeSplit[1]);
        List<String> reminderJobTags = new ArrayList<>();

        for (Integer calendarDay : calendarDays) {
            int diffInSeconds = (int) (timeToNextGivenDay(calendarDay, hours, minutes) / 1000);

            Driver driver = new GooglePlayDriver(context);
            FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
            String reminderJobTag = String.valueOf(System.currentTimeMillis());
            reminderJobTags.add(reminderJobTag);

            Job job = dispatcher.newJobBuilder()
                    .setService(ReminderFirebaseJobService.class)
                    .setTag(reminderJobTag)
                    .setLifetime(Lifetime.FOREVER)
                    .setTrigger(Trigger.executionWindow(
                            diffInSeconds,
                            diffInSeconds + SYNC_FLEXTIME_SECONDS))
                    .setReplaceCurrent(true)
                    .build();
            dispatcher.schedule(job);

            Log.i(LOG_TAG, "created alarm for: " + calendarDay +
                    " diff in seconds: " + diffInSeconds);
        }

        med.setReminderJobTags(reminderJobTags);
        FirebaseUtility.updateMedOnDb(med);

        Log.i(LOG_TAG, "created alarm for: " + med.getKey());
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
