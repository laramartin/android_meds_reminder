package eu.laramartin.medsreminder.reminders;

import android.content.Context;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

public class RemindersUtility {

    public static int REMINDER_INTERVAL_SECONDS = 10;
    public static int SYNC_FLEXTIME_SECONDS;
    public static String REMINDER_JOB_TAG = "med_reminder_tag";
    private static boolean isInitialized;

    // synchronized to not have this method executed more than once at the time
    synchronized public static void scheduleMedReminder(Context context) {
        if (isInitialized) {
            return;
        }
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job job = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MedReminderFirebaseJobService.class)
                // uniquely identifies the job
                .setTag(REMINDER_JOB_TAG)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run when the device is charging
                        Constraint.DEVICE_CHARGING
                )
                // execute job forever even between reboots
                .setLifetime(Lifetime.FOREVER)
                // one-off job
                .setRecurring(true)
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