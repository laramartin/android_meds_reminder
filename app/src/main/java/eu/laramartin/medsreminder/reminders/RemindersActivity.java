package eu.laramartin.medsreminder.reminders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.common.Settings;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.Med;

public class RemindersActivity extends AppCompatActivity {

    private static final java.lang.String RV_POS_INDEX = "recycler_position";
    @BindView(R.id.reminder_recyclerview)
    RecyclerView recyclerView;

    private RemindersAdapter remindersAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        ButterKnife.bind(this);

        remindersAdapter = new RemindersAdapter(new Settings(this));
        recyclerView.setAdapter(remindersAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setAdapterData();

        if (savedInstanceState != null) {
            final int recyclerPositionIndex = savedInstanceState.getInt(RV_POS_INDEX);

            remindersAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

                public void onItemRangeInserted(int positionStart, int itemCount) {
                    linearLayoutManager.scrollToPosition(recyclerPositionIndex);
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int recyclerPositionIndex = linearLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(RV_POS_INDEX, recyclerPositionIndex);
    }

    private void setAdapterData() {
        DatabaseReference medsReference = FirebaseUtility.getMedsReference();
        medsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
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
