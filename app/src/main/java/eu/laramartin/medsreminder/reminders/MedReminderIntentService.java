package eu.laramartin.medsreminder.reminders;

import android.app.IntentService;
import android.content.Intent;

public class MedReminderIntentService extends IntentService {

    public MedReminderIntentService() {
        super("MedReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}
