package eu.laramartin.medsreminder.reminders;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class ReminderFirebaseJobService extends JobService {

    private AsyncTask backgroundTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        backgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                ReminderTasks.executeTask(
                        ReminderFirebaseJobService.this,
                        ReminderTasks.ACTION_MED_REMINDER,
                        jobParameters.getExtras());
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                jobFinished(jobParameters, false);
            }
        };
        backgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (backgroundTask != null) {
            backgroundTask.cancel(true);
        }
        return true;
    }
}
