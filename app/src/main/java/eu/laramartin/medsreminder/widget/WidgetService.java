/*
 * PROJECT LICENSE
 *
 * This project was submitted by Lara Martín as part of the Nanodegree At Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * Me, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2017 Lara Martín
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
