package eu.laramartin.medsreminder.meds;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.laramartin.medsreminder.BaseFragment;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.createUserIfDoesntExist;
import static eu.laramartin.medsreminder.firebase.FirebaseUtility.getMedsReference;


public class MedsFragment extends BaseFragment {

    private static final java.lang.String RV_POS_INDEX = "recycler_position";
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.meds_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_meds)
    FloatingActionButton fab;
    @BindView(R.id.meds_recyclerview)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private ChildEventListener medsChildEventListener;
    private MedsAdapter medsAdapter;
    private DatabaseReference medsReference;
    private FirebaseAnalytics firebaseAnalytics;
    private LinearLayoutManager linearLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meds, container, false);
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        medsAdapter = new MedsAdapter(firebaseAnalytics);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(medsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddMedActivity.class);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity()).toBundle());
            }
        });

        createUserIfDoesntExist();
        attachDatabaseReadListener();

        if (savedInstanceState != null) {
            final int recyclerPositionIndex = savedInstanceState.getInt(RV_POS_INDEX);

            medsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

                public void onItemRangeInserted(int positionStart, int itemCount) {
                    linearLayoutManager.scrollToPosition(recyclerPositionIndex);
                }
            });
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int recyclerPositionIndex = linearLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(RV_POS_INDEX, recyclerPositionIndex);
    }

    private void attachDatabaseReadListener() {
        medsReference = getMedsReference();
        if (medsChildEventListener == null) {
            medsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Med snapshot = dataSnapshot.getValue(Med.class);
                    snapshot.setKey(dataSnapshot.getKey());
                    if (snapshot != null) {
                        medsAdapter.add(snapshot);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Med med = dataSnapshot.getValue(Med.class);
                    med.setKey(dataSnapshot.getKey());
                    medsAdapter.remove(med);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            medsReference.addChildEventListener(medsChildEventListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        medsReference.removeEventListener(medsChildEventListener);
    }
}
