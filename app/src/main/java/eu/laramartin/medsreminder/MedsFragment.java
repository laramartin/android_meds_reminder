package eu.laramartin.medsreminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.laramartin.medsreminder.model.User;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.signOut;


public class MedsFragment extends Fragment {

    private static final String LOG_TAG = MedsFragment.class.getCanonicalName();

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_text)
    EditText inputText;
    @BindView(R.id.button)
    Button sendButton;
    @BindView(R.id.text_display_database)
    TextView textDisplayed;

    Unbinder unbinder;
    private FirebaseDatabase database;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meds, container, false);
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        database = FirebaseDatabase.getInstance();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputText.getText().toString();
                if (!input.isEmpty()) {
                    createUser();
                    readFromDatabase();
                }
            }
        });
        return view;
    }

    private void createUser() {
        DatabaseReference databaseReference = database.getReference("user");
        User user = new User();
        user.setEmail("lala@blabla.com");
        user.setId("1234");
        databaseReference.child("1234")
                .setValue(user);
    }

    private void readFromDatabase() {
        // Read from the database
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                textDisplayed.setText(value);
//                Log.d(TAG, "Value is: " + value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                signOut(getContext());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
