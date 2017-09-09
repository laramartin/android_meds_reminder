package eu.laramartin.medsreminder.reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import eu.laramartin.medsreminder.MainActivity;
import eu.laramartin.medsreminder.R;

class NotificationUtils {

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private static final int MED_REMINDER_NOTIFICATION_ID = 857;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int MED_REMINDER_PENDING_INTENT_ID = 566;
    private static final int ACTION_TAKE_MED_PENDING_INTENT_ID = 1;
//    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 14;

//    public static void clearAllNotifications(Context context) {
//        NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancelAll();
//    }

    public static void remindUserToTakeMed(Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_capsule_128)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.its_time_reminder_title))
                .setContentText(context.getString(R.string.med_reminder_notification_text))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.med_reminder_notification_text)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(takeMedAction(context))
//                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        /* WATER_REMINDER_NOTIFICATION_ID allows you to update or cancel the notification later on */
        notificationManager.notify(MED_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

//    private static NotificationCompat.Action ignoreReminderAction(Context context) {
//        Intent ignoreReminderIntent = new Intent(context, MedReminderIntentService.class);
//        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
//        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
//                context,
//                ACTION_IGNORE_PENDING_INTENT_ID,
//                ignoreReminderIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px,
//                "No, thanks.",
//                ignoreReminderPendingIntent);
//        return ignoreReminderAction;
//    }

    static NotificationCompat.Action takeMedAction(Context context) {
        Intent takeMedIntent = new Intent(context, ReminderReceiver.class);
        PendingIntent takeMedPendingIntent = PendingIntent.getService(
                context,
                ACTION_TAKE_MED_PENDING_INTENT_ID,
                takeMedIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action takeMedAction = new NotificationCompat.Action(R.drawable.ic_capsule_128,
                "I did it!",
                takeMedPendingIntent);
        return takeMedAction;
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                MED_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, R.drawable.ic_capsule_128);
    }
}
