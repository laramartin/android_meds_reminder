package eu.laramartin.medsreminder.meds;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.common.DialogsUtility;
import eu.laramartin.medsreminder.common.MedsUtility;
import eu.laramartin.medsreminder.model.Med;
import eu.laramartin.medsreminder.model.Report;

import static eu.laramartin.medsreminder.common.MedsUtility.getReportFromMed;
import static eu.laramartin.medsreminder.common.Utils.runVibration;
import static eu.laramartin.medsreminder.firebase.FirebaseUtility.writeReportOnDb;

public class MedsAdapter extends RecyclerView.Adapter<MedsAdapter.MedViewHolder> {

    private List<Med> meds = new ArrayList<>();

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

    public class MedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.med_icon)
        ImageView medIcon;
        @BindView(R.id.med_name)
        TextView medName;
        @BindView(R.id.med_time)
        TextView medTime;
        @BindView(R.id.med_days)
        TextView medDays;
        @BindView(R.id.med_notes)
        TextView medNotes;
        // TODO: 31.08.17 Lara: show next date to take medicine
        @BindView(R.id.med_date)
        TextView medDate;
        // TODO: 31.08.17 Lara: show current time status (on time, soon, it' time)
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
            takeMedLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Report report = getReportFromMed(medsAdapterItem.getMed());
                    writeReportOnDb(report);
                    Toast.makeText(itemView.getContext(), medName.getText() + " taken!", Toast.LENGTH_SHORT).show();
                    Log.i("MedsAdapter", "report: " + report.toString());
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

            medName.setText(med.getName());
            medIcon.setImageResource(MedsUtility.getMedIcon(med.getDosage()));
            // TODO: 31.08.17 Lara: handle set date
            medDate.setText("07/07/2017");
            medTime.setText(med.getTime());
            // TODO: 31.08.17 Lara: handle different time status
            medTimeStatus.setText("On time");
            // TODO: 31.08.17 Lara: format days like "Days: Mo, Tu"
            medDays.setText(med.getDays());
            // TODO: 31.08.17 Lara: format notes like "Notes: blabla..."
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
