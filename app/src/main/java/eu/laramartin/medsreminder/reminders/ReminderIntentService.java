package eu.laramartin.medsreminder.reminders;

import android.app.IntentService;
import android.content.Intent;

public class ReminderIntentService extends IntentService {

    public ReminderIntentService() {
        super("ReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}
