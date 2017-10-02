package eu.laramartin.medsreminder.widget;

import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.laramartin.medsreminder.MainActivity;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.common.CalendarUtility;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.getMedsReference;

public class WidgetService extends RemoteViewsService {

    private List<Med> meds = new ArrayList<>();
    private DatabaseReference medsReference;
    private ValueEventListener medsValueEventListener;

    private void attachDatabaseReadListener() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            return;
        }
        medsReference = getMedsReference();
        if (medsValueEventListener == null) {
            medsValueEventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> medsDataSnapshot = dataSnapshot.getChildren();
                    for (DataSnapshot currentSnapshot : medsDataSnapshot) {
                        Med medSnapshot = currentSnapshot.getValue(Med.class);
                        medSnapshot.setKey(dataSnapshot.getKey());
                        if (medSnapshot != null) {
                            meds.add(medSnapshot);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            medsReference.addValueEventListener(medsValueEventListener);
        }
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        RemoteViewsFactory remoteViewsFactory = new RemoteViewsFactory() {

            @Override
            public void onCreate() {
                attachDatabaseReadListener();
            }

            @Override
            public void onDataSetChanged() {
                attachDatabaseReadListener();
            }

            @Override
            public void onDestroy() {
                if (medsValueEventListener != null) {
                    medsReference.removeEventListener(medsValueEventListener);
                }
            }

            @Override
            public int getCount() {
                return meds.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                final RemoteViews remoteView = new RemoteViews(
                        getApplicationContext().getPackageName(), R.layout.widget_details_item);
                if (meds != null || !meds.isEmpty()) {
                    Med med = meds.get(position);

                    Date date = CalendarUtility.getNextTake(med);
                    String formattedNextDate = CalendarUtility.getFormattedDateWithHour(date);
                    remoteView.setTextViewText(R.id.widget_med_name_text, med.getName());
                    remoteView.setTextViewText(R.id.widget_med_time_text, formattedNextDate);
                }
                Intent fillIntent = new Intent(getApplicationContext(), MainActivity.class);
                remoteView.setOnClickFillInIntent(R.id.widget_list_item, fillIntent);
                return remoteView;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_details_item);
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
