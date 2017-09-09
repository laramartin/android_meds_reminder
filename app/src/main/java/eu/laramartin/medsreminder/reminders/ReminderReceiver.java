package eu.laramartin.medsreminder.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ReminderTasks.executeTask(context, ReminderTasks.ACTION_MED_REMINDER);
    }
}
