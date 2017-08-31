package eu.laramartin.medsreminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.model.Med;

public class MedsAdapter extends RecyclerView.Adapter<MedsAdapter.MedViewHolder>{

    private List<Med> meds = new ArrayList<>();

    public void add(Med med) {
        meds.add(med);
        notifyItemInserted(meds.size() -1);
    }

    public class MedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.med_icon)
        ImageView medIcon;
        @BindView(R.id.med_name)
        TextView medName;
        @BindView(R.id.med_time) TextView medTime;
        @BindView(R.id.med_days) TextView medDays;
        @BindView(R.id.med_notes) TextView medNotes;
        // TODO: 31.08.17 Lara: show next date to take medicine
        @BindView(R.id.med_date)
        TextView medDate;
        // TODO: 31.08.17 Lara: show current time status (on time, soon, it' time)
        @BindView(R.id.med_time_status) TextView medTimeStatus;

        public MedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
        Med med = meds.get(position);
        holder.medName.setText(med.getName());
        holder.medIcon.setImageResource(MedsUtility.getMedIcon(med.getDosage()));
        // TODO: 31.08.17 Lara: handle set date
        holder.medDate.setText("07/07/2017");
        holder.medTime.setText(med.getTime());
        // TODO: 31.08.17 Lara: format days like "Days: Mo, Tu"
        holder.medDays.setText(med.getDays());
        // TODO: 31.08.17 Lara: format notes like "Notes: blabla..." 
        if (med.getNotes() != null && !med.getNotes().isEmpty()) {
            holder.medNotes.setText(med.getNotes());
        } else {
            holder.medNotes.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (meds != null) {
            return meds.size();
        }
        return 0;
    }
}
