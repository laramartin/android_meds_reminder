package eu.laramartin.medsreminder.permissions;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.common.DialogsUtility;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.Permission;

public class PermissionsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.fab_permissions)
    FloatingActionButton fab;
    @BindView(R.id.permissions_recyclerview)
    RecyclerView recyclerView;

    private DatabaseReference permissionsReference;
    private PermissionsAdapter permissionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        ButterKnife.bind(this);

        permissionsAdapter = new PermissionsAdapter();
        recyclerView.setAdapter(permissionsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(this);
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        permissionsReference = FirebaseUtility.getPermissionssReference();
        permissionsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Permission permission = dataSnapshot.getValue(Permission.class);
                permission.setKey(dataSnapshot.getKey());
                if (dataSnapshot != null) {
                    permissionsAdapter.add(permission);
                }
                FirebaseUtility.updatePermissionOnDb(permission);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Permission permission = dataSnapshot.getValue(Permission.class);
                permission.setKey(dataSnapshot.getKey());
                permissionsAdapter.remove(permission);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        DialogsUtility.showInviteFriendDialog(this);
    }
}
