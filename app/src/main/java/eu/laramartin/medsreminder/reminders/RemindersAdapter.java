package eu.laramartin.medsreminder.reminders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Med;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder> {

    private List<Med> meds = new ArrayList<>();

    // TODO: 06.09.17 Lara: handle add med
    public void add(Med med) {
        meds.add(med);
        notifyItemInserted(meds.size() - 1);
    }

    @Override
    public RemindersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_reminders_item, parent, false);
        return new RemindersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RemindersAdapter.RemindersViewHolder holder, int position) {
        holder.bind(meds.get(position));
    }

    @Override
    public int getItemCount() {
        if (meds != null) {
            return meds.size();
        }
        return 0;
    }

    public class RemindersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reminder_med_name)
        TextView medName;
        @BindView(R.id.reminder_switch)
        SwitchCompat medSwitch;

        public RemindersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Med med) {
            medName.setText(med.getName());
        }
    }
}
