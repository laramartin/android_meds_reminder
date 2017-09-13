package eu.laramartin.medsreminder.reports;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.laramartin.medsreminder.BaseFragment;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.model.Report;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.getReportsReference;


public class ReportsFragment extends BaseFragment {

    private static final String LOG_TAG = ReportsFragment.class.getCanonicalName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.reports_recyclerview)
    RecyclerView recyclerView;

    private Unbinder unbinder;
    private ChildEventListener reportsChildEventListener;
    private ReportsAdapter reportsAdapter;
    private DatabaseReference reportsReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        reportsAdapter = new ReportsAdapter();
        recyclerView.setAdapter(reportsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        attachDatabaseReadListener();
        return view;
    }

    private void attachDatabaseReadListener() {
        reportsReference = getReportsReference();
        if (reportsChildEventListener == null) {
            reportsChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Report snapshot = dataSnapshot.getValue(Report.class);
                    if (snapshot != null) {
                        reportsAdapter.add(snapshot);
                    }
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
            reportsReference.addChildEventListener(reportsChildEventListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        reportsReference.removeEventListener(reportsChildEventListener);
    }
}
