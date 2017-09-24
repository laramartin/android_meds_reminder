package eu.laramartin.medsreminder.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import eu.laramartin.medsreminder.MainActivity;
import eu.laramartin.medsreminder.R;

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_details);

            Intent remoteAdapterIntent = new Intent(context, WidgetService.class);
            Intent mainActivityIntent = new Intent(context, MainActivity.class);
            PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_header, mainActivityPendingIntent);

            remoteViews.setRemoteAdapter(R.id.widget_list_view, remoteAdapterIntent);
            remoteViews.setEmptyView(R.id.widget_list_view, R.id.widget_empty_view);

            Intent clickItemIntent = new Intent(context, MainActivity.class);
            PendingIntent clickItemPendingItent = PendingIntent.getActivity(context, 0, clickItemIntent, 0);
            remoteViews.setPendingIntentTemplate(R.id.widget_list_view, clickItemPendingItent);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
    }
}