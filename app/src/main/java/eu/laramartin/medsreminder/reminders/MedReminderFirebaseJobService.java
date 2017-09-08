package eu.laramartin.medsreminder.reminders;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MedReminderFirebaseJobService extends JobService {

    private AsyncTask backgroundTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        backgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                ReminderTasks.executeTask(MedReminderFirebaseJobService.this, ReminderTasks.ACTION_MED_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
//                the job was successful therefore we pass false as 2nd parameter
                jobFinished(jobParameters, false);
            }
        };
        backgroundTask.execute();
        return true;
    }

    //    called when any of the constraints isn't met anymore
    @Override
    public boolean onStopJob(JobParameters job) {
        if (backgroundTask != null) {
            backgroundTask.cancel(true);
        }
//        as soon as constraints are met again, retry job
        return true;
    }
}
