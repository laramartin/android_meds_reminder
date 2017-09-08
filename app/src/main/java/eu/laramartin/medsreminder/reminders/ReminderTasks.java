package eu.laramartin.medsreminder.reminders;

import android.content.Context;

public class ReminderTasks {

    static final String ACTION_MED_REMINDER = "med-reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_MED_REMINDER.equals(action)) {
            createMedReminder(context);
        }
    }

    private static void createMedReminder(Context context) {
        NotificationUtils.remindUserToTakeMed(context);
    }
}
