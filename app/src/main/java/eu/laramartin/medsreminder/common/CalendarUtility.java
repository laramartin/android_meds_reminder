package eu.laramartin.medsreminder.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eu.laramartin.medsreminder.model.Med;

public class CalendarUtility {

    public static List<Integer> getDaysOfWeek(String medDays) {
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

    public static String[] getMedTimeSplit(Med med) {
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


    public static List<Integer> getMedDays(Med med) {
        String medDays = med.getDays();
        List<Integer> weekDays = getDaysOfWeek(medDays);
        return weekDays;
    }
}
