package eu.laramartin.medsreminder.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.getMedsReference;

public class WidgetService extends RemoteViewsService {

    private List<Med> meds = new ArrayList<>();
    private DatabaseReference medsReference;
    private ChildEventListener medsChildEventListener;
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private String LOG_TAG = WidgetService.class.getCanonicalName();

    private void attachDatabaseReadListener() {
        medsReference = getMedsReference();
        if (medsChildEventListener == null) {
            medsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Med snapshot = dataSnapshot.getValue(Med.class);
                    snapshot.setKey(dataSnapshot.getKey());
                    if (snapshot != null) {
                        meds.add(snapshot);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Med med = dataSnapshot.getValue(Med.class);
                    med.setKey(dataSnapshot.getKey());
                    meds.remove(med);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            medsReference.addChildEventListener(medsChildEventListener);
            countDownLatch.countDown();
        }
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        RemoteViewsFactory remoteViewsFactory = new RemoteViewsFactory() {

            @Override
            public void onCreate() {
//                attachDatabaseReadListener();
                Log.i(LOG_TAG, "onCreate called");

                Med med1 = new Med();
                Med med2 = new Med();
                Med med3 = new Med();
                med1.setName("medication 1");
                med1.setTime("01/09/2017 at 10:00");
                med2.setName("medication 2");
                med2.setTime("02/09/2017 at 10:00");
                med3.setName("medication 3");
                med3.setTime("03/09/2017 at 10:00");
                meds.add(med1);
                meds.add(med2);
                meds.add(med3);
            }

            @Override
            public void onDataSetChanged() {
                Log.i(LOG_TAG, "onDataSetChanged called");
//                countDownLatch = new CountDownLatch(1);
//                attachDatabaseReadListener();
//                try {
//                    countDownLatch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onDestroy() {
                Log.i(LOG_TAG, "onDestroy called");
//                medsReference.removeEventListener(medsChildEventListener);
            }

            @Override
            public int getCount() {
                return meds.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                final RemoteViews remoteView = new RemoteViews(
                        getApplicationContext().getPackageName(), R.layout.widget_list_item);
                if (meds != null || !meds.isEmpty()) {
                    Med med = meds.get(position);
                    remoteView.setTextViewText(R.id.widget_med_name_text, med.getName());
                    remoteView.setTextViewText(R.id.widget_med_time_text, med.getTime());
                }

//                String time = CalendarUtility.getFormattedDateWithHour(med.getTime());
//                remoteView.setTextViewText(R.id.widget_med_name_text, med.getName());
//                remoteView.setTextViewText(R.id.widget_med_time_text, time);

                return remoteView;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return meds.size();
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
        return remoteViewsFactory;
    }
}
