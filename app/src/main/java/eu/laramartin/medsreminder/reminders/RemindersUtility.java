package eu.laramartin.medsreminder.reminders;

import android.content.Context;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eu.laramartin.medsreminder.model.Med;

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

    private static List<Integer> getMedDays(Med med) {
        String medDays = med.getDays();
        List<Integer> weekDays = getDaysOfWeek(medDays);
        return weekDays;
    }

    private static List<Integer> getDaysOfWeek(String medDays) {
        List<Integer> weekDays = new ArrayList<>();
        if (medDays.contains("Mo")) {
            weekDays.add(Calendar.MONDAY);
        }
        if (medDays.contains("Tu")) {
            weekDays.add(Calendar.TUESDAY);
        }
        if (medDays.contains("We")) {
            weekDays.add(Calendar.WEDNESDAY);
        }
        if (medDays.contains("Th")) {
            weekDays.add(Calendar.THURSDAY);
        }
        if (medDays.contains("Fr")) {
            weekDays.add(Calendar.FRIDAY);
        }
        if (medDays.contains("Sa")) {
            weekDays.add(Calendar.SATURDAY);
        }
        if (medDays.contains("Su")) {
            weekDays.add(Calendar.SUNDAY);
        }
        return weekDays;
    }

    private static String[] getMedTimeSplit(Med med) {
        String time = med.getTime();
        String[] timeParts = time.split(":");
        return timeParts;
    }

    public static long timeToNextGivenDay(int targetDoW, int targetHoW, int targetMoW) {
        Calendar now = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        int nowDoW = now.get(Calendar.DAY_OF_WEEK);
        int addDays = (nowDoW + targetDoW) % 7;
        next.add(Calendar.DAY_OF_YEAR, addDays);
        next.set(Calendar.HOUR_OF_DAY, targetHoW);
        next.set(Calendar.MINUTE, targetMoW);

        long date = next.getTimeInMillis();
        long diff = date - now.getTimeInMillis();

        // diff is in the past
        if (diff < 0) {
            next.add(Calendar.DAY_OF_YEAR, 7);
            date = next.getTimeInMillis();
            diff = date - now.getTimeInMillis();
        }

        return diff;
    }

}
