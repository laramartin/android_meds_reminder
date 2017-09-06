package eu.laramartin.medsreminder.reminders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Med;

public class RemindersActivity extends AppCompatActivity {

    @BindView(R.id.reminder_recyclerview)
    RecyclerView recyclerView;

    private RemindersAdapter remindersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        ButterKnife.bind(this);

        remindersAdapter = new RemindersAdapter();
        recyclerView.setAdapter(remindersAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapterData();
    }

    private void setAdapterData() {
        // TODO: 06.09.17 Lara: read db

        Med med = new Med();
        med.setName("medication 1");
        remindersAdapter.add(med);
    }
}
