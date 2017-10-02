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
