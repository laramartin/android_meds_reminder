package eu.laramartin.medsreminder.permissions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.laramartin.medsreminder.BaseFragment;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.User;


public class FriendsFragment extends BaseFragment {

    private static final String LOG_TAG = FriendsFragment.class.getCanonicalName();
    private ValueEventListener valueEventListener;
    private List<User> usersThatGavePermission = new ArrayList<>();
    private Unbinder unbinder;
    private FriendsAdapter friendsAdapter;

    @BindView(R.id.friends_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        Log.i(LOG_TAG, "Friends fragment selected");
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        friendsAdapter = new FriendsAdapter();
        recyclerView.setAdapter(friendsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        attachValueEventListener();
        getGrantedPermissionsByUser();
        return view;
    }

    private void attachValueEventListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> usersDataSnapshot = dataSnapshot.getChildren();
                for (DataSnapshot currentSnapshot : usersDataSnapshot) {
                    DataSnapshot permissions = currentSnapshot.child("permissions");
                    if (permissions.exists()) {
                        for (DataSnapshot permission : permissions.getChildren()) {
                            if (permission.getValue().equals(FirebaseUtility.getCurrentUserEmail())) {
                                User userThatGavePermission = currentSnapshot.getValue(User.class);
                                usersThatGavePermission.add(userThatGavePermission);
                                Log.i(LOG_TAG, "User that gave permission: " + userThatGavePermission.toString());
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    private void getGrantedPermissionsByUser() {
        DatabaseReference databaseReference = FirebaseUtility.getUserRootDatabaseReference();
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
