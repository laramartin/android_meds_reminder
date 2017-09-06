package eu.laramartin.medsreminder.reminders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
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
        DatabaseReference medsReference = FirebaseUtility.getMedsReference();
        medsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    Log.i("RemindersActivity", child.toString());
                    Med med = child.getValue(Med.class);
                    med.setKey(child.getKey());
                    remindersAdapter.add(med);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
