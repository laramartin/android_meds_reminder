package eu.laramartin.medsreminder.reminders;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import eu.laramartin.medsreminder.MainActivity;
import eu.laramartin.medsreminder.R;

class NotificationUtils {

    private static final int MED_REMINDER_NOTIFICATION_ID = 857;
    private static final int MED_REMINDER_PENDING_INTENT_ID = 566;
    private static final int ACTION_TAKE_MED_PENDING_INTENT_ID = 1;

    public static void remindUserToTakeMed(Context context) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_capsule_128)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.reminder_notification_title))
                .setContentText(context.getString(R.string.reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(takeMedAction(context))
                .setAutoCancel(true);

        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(MED_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    static NotificationCompat.Action takeMedAction(Context context) {
        Intent takeMedIntent = new Intent(context, ReminderIntentService.class);
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
