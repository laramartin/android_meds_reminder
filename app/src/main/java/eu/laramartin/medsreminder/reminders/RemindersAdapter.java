package eu.laramartin.medsreminder.reminders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.common.Settings;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.reminders.RemindersUtility.buildSwitchReminderKey;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.RemindersViewHolder> {

    private static final String LOG_TAG = RemindersAdapter.class.getCanonicalName();
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
        return meds.size();
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

        public void bind(final Med med) {
            medName.setText(med.getName());

            String switchReminderKey = buildSwitchReminderKey(med);
            medSwitch.setOnCheckedChangeListener(null);
            medSwitch.setChecked(settings.getAlarmEnabled(switchReminderKey));
            medSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isReminderEnabled) {
                    if (!isReminderEnabled) {
                        // cancel reminder
                        RemindersUtility.cancelMedReminder(compoundButton.getContext(),
                                med.getReminderJobTags());
                        med.setReminderJobTags(new ArrayList<String>());
                        FirebaseUtility.updateMedOnDb(med);
                        Log.i(LOG_TAG, "alarm disabled for: " + med.getKey());
                    } else {
                        // activate reminder
                        RemindersUtility.scheduleMedReminder(compoundButton.getContext(), med);
                        Log.i(LOG_TAG, "alarm enabled for: " + med.getKey());
                    }
                    String switchReminderKey = buildSwitchReminderKey(med);
                    settings.setAlarmEnabled(switchReminderKey, isReminderEnabled);
                }
            });
        }
    }
}
