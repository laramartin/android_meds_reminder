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
import eu.laramartin.medsreminder.common.Settings;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.reminders.RemindersUtility.buildSwitchReminderKey;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder> {

    private List<Med> meds = new ArrayList<>();
    private Settings settings;

    public RemindersAdapter(Settings settings) {
        this.settings = settings;
    }

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

    public class RemindersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.reminder_med_name)
        TextView medName;
        @BindView(R.id.reminder_switch)
        SwitchCompat medSwitch;
        private Med med = null;

        public RemindersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            medSwitch.setOnClickListener(this);

        }

        public void bind(Med med) {
            medName.setText(med.getName());
            String switchReminderKey = buildSwitchReminderKey(med);
            medSwitch.setChecked(settings.getAlarmEnabled(switchReminderKey));
            this.med = med;
        }

        @Override
        public void onClick(View view) {
            RemindersUtility.scheduleMedReminder(view.getContext(), med);
            String switchReminderKey = buildSwitchReminderKey(med);
            boolean isSwitchActivated = RemindersUtility.isSwitchActivated(view);
            settings.setAlarmEnabled(switchReminderKey, isSwitchActivated);
        }
    }
}
