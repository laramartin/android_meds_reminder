/*
 * PROJECT LICENSE
 *
 * This project was submitted by Lara Martín as part of the Nanodegree At Udacity.
 *
 * As part of Udacity Honor code, your submissions must be your own work, hence
 * submitting this project as yours will cause you to break the Udacity Honor Code
 * and the suspension of your account.
 *
 * Me, the author of the project, allow you to check the code as a reference, but if
 * you submit it, it's your own responsibility if you get expelled.
 *
 * Copyright (c) 2017 Lara Martín
 *
 * Besides the above notice, the following license applies and this license notice
 * must be included in all works derived from this project.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package eu.laramartin.medsreminder.friends;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import eu.laramartin.medsreminder.BaseFragment;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.firebase.FirebaseUtility;
import eu.laramartin.medsreminder.model.User;


public class FriendsFragment extends BaseFragment {

    private static final java.lang.String RV_POS_INDEX = "recycler_position";
    private ValueEventListener valueEventListener;
    private Unbinder unbinder;
    private FriendsAdapter friendsAdapter;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.friends_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.friends_toolbar)
    Toolbar toolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        friendsAdapter = new FriendsAdapter();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(friendsAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        attachValueEventListener();
        getGrantedPermissionsByUser();

        if (savedInstanceState != null) {
            final int recyclerPositionIndex = savedInstanceState.getInt(RV_POS_INDEX);

            friendsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

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

    private void attachValueEventListener() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> usersDataSnapshot = dataSnapshot.getChildren();
                for (DataSnapshot currentSnapshot : usersDataSnapshot) {
                    DataSnapshot permissions = currentSnapshot.child("permissions");
                    if (permissions.exists()) {
                        for (DataSnapshot permission : permissions.getChildren()) {
                            addToAdapterIfPermissionGranted(currentSnapshot, permission);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public void addToAdapterIfPermissionGranted(DataSnapshot currentSnapshot, DataSnapshot permission) {
        if (permission.child("email").exists()) {
            if (isPermissionForCurrentUser(permission)) {
                addPermissionToAdapter(currentSnapshot);
            }
        }
    }

    public void addPermissionToAdapter(DataSnapshot currentSnapshot) {
        User userThatGavePermission = currentSnapshot.getValue(User.class);
        friendsAdapter.add(userThatGavePermission);
    }

    public boolean isPermissionForCurrentUser(DataSnapshot permission) {
        return permission.child("email").getValue().equals(FirebaseUtility.getCurrentUserEmail());
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
