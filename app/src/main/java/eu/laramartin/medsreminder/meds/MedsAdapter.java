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

package eu.laramartin.medsreminder.meds;

import android.content.Context;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.common.CalendarUtility;
import eu.laramartin.medsreminder.common.DialogsUtility;
import eu.laramartin.medsreminder.common.MedsUtility;
import eu.laramartin.medsreminder.firebase.AnalyticsUtility;
import eu.laramartin.medsreminder.model.Med;
import eu.laramartin.medsreminder.model.Report;

import static eu.laramartin.medsreminder.common.MedsUtility.getReportFromMed;
import static eu.laramartin.medsreminder.common.Utils.runVibration;
import static eu.laramartin.medsreminder.firebase.FirebaseUtility.writeReportOnDb;

public class MedsAdapter extends RecyclerView.Adapter<MedsAdapter.MedViewHolder> {

    private List<Med> meds = new ArrayList<>();
    private FirebaseAnalytics firebaseAnalytics;

    public MedsAdapter(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    public void add(Med med) {
        meds.add(med);
        notifyItemInserted(meds.size() - 1);
    }

    @Override
    public MedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_med_item, parent, false);
        return new MedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedViewHolder holder, int position) {
        holder.bind(meds.get(position));
    }

    @Override
    public int getItemCount() {
        if (meds != null) {
            return meds.size();
        }
        return 0;
    }

    public void remove(Med med) {
        for (int i = 0; i < meds.size(); i++) {
            if (med.getKey().equals(meds.get(i).getKey())) {
                meds.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    private String nextDate(Med med) {
        Date date = CalendarUtility.getNextTake(med);
        return CalendarUtility.getFormattedDate(date);
    }

    public class MedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.med_icon)
        ImageView medIcon;
        @BindView(R.id.med_name)
        TextView medName;
        @BindView(R.id.med_reminder_icon)
        ImageView medReminder;
        @BindView(R.id.med_time)
        TextView medTime;
        @BindView(R.id.med_days)
        TextView medDays;
        @BindView(R.id.med_notes)
        TextView medNotes;
        @BindView(R.id.meds_edit)
        TextView medEdit;
        @BindView(R.id.med_date)
        TextView medDate;
        @BindView(R.id.med_time_status)
        TextView medTimeStatus;
        @BindView(R.id.meds_constraint_layout_details)
        ConstraintLayout medsLayoutDetails;
        @BindView(R.id.icon_arrow_down)
        ImageView arrowDownIcon;
        @BindView(R.id.meds_take_label)
        TextView takeMedLabel;

        private MedsAdapterItem medsAdapterItem;

        public MedViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            medEdit.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            takeMedLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Report report = getReportFromMed(medsAdapterItem.getMed());
                    writeReportOnDb(report);
                    AnalyticsUtility.takeMed(firebaseAnalytics, medsAdapterItem.getMed());
                    Toast.makeText(itemView.getContext(), medName.getText() +
                            itemView.getContext().getString(R.string.meds_taken_feedback),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onClick(View view) {
            medsAdapterItem.setExpanded(!medsAdapterItem.isExpanded());
            setDetailsVisibility();
        }

        private void setDetailsVisibility() {
            if (medsAdapterItem.isExpanded()) {
                showDetails();
            } else {
                hideDetails();
            }
        }

        private void showDetails() {
            medsLayoutDetails.setVisibility(View.VISIBLE);
            arrowDownIcon.setVisibility(View.INVISIBLE);
        }

        private void hideDetails() {
            medsLayoutDetails.setVisibility(View.GONE);
            arrowDownIcon.setVisibility(View.VISIBLE);
        }

        public void bind(Med med) {
            MedsAdapterItem medsAdapterItem = new MedsAdapterItem();
            medsAdapterItem.setMed(med);
            this.medsAdapterItem = medsAdapterItem;

            medIcon.setImageResource(MedsUtility.getMedIcon(med.getDosage()));
            medName.setText(med.getName());
            if (med.getReminderJobTags() != null && !med.getReminderJobTags().isEmpty()) {
                medReminder.setVisibility(View.VISIBLE);
            } else {
                medReminder.setVisibility(View.INVISIBLE);
            }

            medDate.setText(nextDate(med));
            medTime.setText(med.getTime());
            // TODO: 31.08.17 Lara: handle different time status, hardcoded until feature is implemented
            medTimeStatus.setText("On time");

            medDays.setText(CalendarUtility.getFormattedDaysOfWeek(itemView.getContext(), med.getDays()));
            if (med.getNotes() != null && !med.getNotes().isEmpty()) {
                medNotes.setText(med.getNotes());
                medNotes.setVisibility(View.VISIBLE);
            } else {
                medNotes.setVisibility(View.INVISIBLE);
            }
            setDetailsVisibility();
        }

        @Override
        public boolean onLongClick(View view) {
            DialogsUtility.showRemoveMedDialog(view.getContext(), medsAdapterItem);
            runVibration(view);
            return true;
        }
    }
}
