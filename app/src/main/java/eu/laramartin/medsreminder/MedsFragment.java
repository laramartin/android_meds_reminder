package eu.laramartin.medsreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.createUserIfDoesntExist;
import static eu.laramartin.medsreminder.firebase.FirebaseUtility.getMedsReference;
import static eu.laramartin.medsreminder.firebase.FirebaseUtility.signOut;


public class MedsFragment extends Fragment {

    private static final String LOG_TAG = MedsFragment.class.getCanonicalName();
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_meds)
    FloatingActionButton fab;
    // TODO: 20.08.17 Lara: remove this TextView from layout 
    @BindView(R.id.meds_text)
    TextView medsText;

    Unbinder unbinder;
    private ChildEventListener medsChildEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meds, container, false);
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        createUserIfDoesntExist();
        attachDatabaseReadListener();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddMedActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void attachDatabaseReadListener() {
        if (medsChildEventListener == null) {
            medsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Med snapshot = dataSnapshot.getValue(Med.class);
                    // TODO: 20.08.17 Lara: here add object to adapter
                    if (snapshot != null) {
                        medsText.setText(snapshot.toString());
                    }
                    Log.i(LOG_TAG, "snapshot: " + snapshot.toString());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            getMedsReference().addChildEventListener(medsChildEventListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getMedsReference().removeEventListener(medsChildEventListener);
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
