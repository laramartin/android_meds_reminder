package eu.laramartin.medsreminder.reminders;

import android.content.Context;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.List;

import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.common.CalendarUtility.getMedDays;
import static eu.laramartin.medsreminder.common.CalendarUtility.getMedTimeSplit;
import static eu.laramartin.medsreminder.common.CalendarUtility.timeToNextGivenDay;

public class RemindersUtility {

    public static int SYNC_FLEXTIME_SECONDS;
    public static String REMINDER_JOB_TAG = "med_reminder_tag";
    private static boolean isInitialized;

    // synchronized to not have this method executed more than once at the time
    synchronized public static void scheduleMedReminder(Context context, Med med) {
        if (isInitialized) {
            return;
        }
        int REMINDER_INTERVAL_SECONDS = 0;

        // days of the week when med has to be taken
        // "MoTu" would be [2, 3]
        List<Integer> calendarDays =  getMedDays(med);
        String[] timeSplitted = getMedTimeSplit(med);
        int hours = Integer.valueOf(timeSplitted[0]);
        int minutes = Integer.valueOf(timeSplitted[1]);

        // TODO: 09.09.17 Lara: for each day to take med, calculate day and hours difference in seconds
        // TODO: 09.09.17 Lara: and schedule a new job/alarm
        for (Integer calendarDay: calendarDays) {
            long diffInSeconds = timeToNextGivenDay(calendarDay, hours, minutes) / 1000;

        }

        // TODO: 09.09.17 Lara: create an alarm and move to inside for loop
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job job = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MedReminderFirebaseJobService.class)
                // uniquely identifies the job
                .setTag(REMINDER_JOB_TAG)
                // constraints that need to be satisfied for the job to run
                // execute job forever even between reboots
                .setLifetime(Lifetime.FOREVER)
                // one-off job
                .setRecurring(true)
                // TODO: 08.09.17 Lara: check this trigger interval
                // start between 0 and 15 minutes (900 seconds)
                .setTrigger(Trigger.executionWindow(
                        REMINDER_INTERVAL_SECONDS,
                        REMINDER_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                // overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                .build();
        dispatcher.schedule(job);
        isInitialized = true;
    }
}
