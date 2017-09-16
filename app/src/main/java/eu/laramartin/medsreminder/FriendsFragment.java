package eu.laramartin.medsreminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.User;


public class FriendsFragment extends BaseFragment {

    private static final String LOG_TAG = FriendsFragment.class.getCanonicalName();
    private ValueEventListener valueEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Friends fragment selected");
        attachValueEventListener();
        getGrantedPermissionsByUser();
        return inflater.inflate(R.layout.fragment_friends, container, false);
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
}
