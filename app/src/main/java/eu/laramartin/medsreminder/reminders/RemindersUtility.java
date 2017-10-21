/*
 * PROJECT LICENSE
 *
 * This project was submitted by Lara Martín as part of the Nanodegree At Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * Me, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2017 Lara Martín
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package eu.laramartin.medsreminder.reminders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

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
