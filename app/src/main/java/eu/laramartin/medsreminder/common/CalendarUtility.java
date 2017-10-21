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

package eu.laramartin.medsreminder.common;

import android.content.Context;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Med;

public class CalendarUtility {

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

    public static long millisToNextTargetDay(int targetDayOfWeek, int targetHour, int targetMinute) {
        Calendar now = Calendar.getInstance();
        Calendar next = getNextCalendarDay(targetDayOfWeek, targetHour, targetMinute);

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

    @NonNull
    public static Calendar getNextCalendarDay(int targetDayOfWeek, int targetHour, int targetMinute) {
        Calendar now = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        int nowDoW = now.get(Calendar.DAY_OF_WEEK);
        int addDays = (targetDayOfWeek - nowDoW) % 7;
        next.add(Calendar.DAY_OF_YEAR, addDays);
        next.set(Calendar.HOUR_OF_DAY, targetHour);
        next.set(Calendar.MINUTE, targetMinute);
        return next;
    }

    public static List<Integer> getMedDays(Med med) {
        String medDays = med.getDays();
        return getDaysOfWeek(medDays);
    }

    public static String getFormattedCurrentDateWithHour() {
        return getFormattedDateWithHour(new Date());
    }

    public static String getFormattedDateWithHour(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm");
        return dateFormat.format(date);
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    public static Date getNextTake(Med med) {
        List<Integer> days = CalendarUtility.getMedDays(med);
        long diffInMillisToNextDay = Long.MAX_VALUE;
        for (int day : days) {
            String[] timeSplit = med.getTime().split(":");
            int hours = Integer.valueOf(timeSplit[0]);
            int minutes = Integer.valueOf(timeSplit[1]);
            long diff = CalendarUtility.millisToNextTargetDay(day, hours, minutes);
            diffInMillisToNextDay = Math.min(diff, diffInMillisToNextDay);
        }
        return new Date(diffInMillisToNextDay + System.currentTimeMillis());
    }

    public static String getFormattedDaysOfWeek(Context context, String medDays) {
        StringBuilder builder = new StringBuilder();
        if (medDays.contains("Mo")) {
            builder.append(context.getString(R.string.meds_monday));
            builder.append(" ");
        }
        if (medDays.contains("Tu")) {
            builder.append(context.getString(R.string.meds_tuesday));
            builder.append(" ");
        }
        if (medDays.contains("We")) {
            builder.append(context.getString(R.string.meds_wednesday));
            builder.append(" ");
        }
        if (medDays.contains("Th")) {
            builder.append(context.getString(R.string.meds_thursday));
            builder.append(" ");
        }
        if (medDays.contains("Fr")) {
            builder.append(context.getString(R.string.meds_friday));
            builder.append(" ");
        }
        if (medDays.contains("Sa")) {
            builder.append(context.getString(R.string.meds_saturday));
            builder.append(" ");
        }
        if (medDays.contains("Su")) {
            builder.append(context.getString(R.string.meds_sunday));
        }
        return builder.toString();
    }
}
