package eu.laramartin.medsreminder.reminders;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class ReminderIntentService extends IntentService {

    public ReminderIntentService() {
        super("ReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        ReminderTasks.executeTask(this, action, bundle);
    }
}
