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

package eu.laramartin.medsreminder.firebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import eu.laramartin.medsreminder.MainActivity;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.meds.MedsAdapterItem;
import eu.laramartin.medsreminder.model.Med;
import eu.laramartin.medsreminder.model.Permission;
import eu.laramartin.medsreminder.model.Report;
import eu.laramartin.medsreminder.model.User;

import static android.support.v4.content.ContextCompat.startActivity;


public class FirebaseUtility {

    public static Intent getLoginIntent() {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                        Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginTheme)
                .setLogo(R.drawable.ic_capsule_128)
                .build();
    }

    public static void signOut(final Context context) {
        final FragmentActivity fragmentActivity = (FragmentActivity) context;
        AuthUI.getInstance()
                .signOut(fragmentActivity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(context, new Intent(fragmentActivity, MainActivity.class), null);
                        fragmentActivity.finish();
                    }
                });
    }

    public static void createUserIfDoesntExist() {
        final DatabaseReference currentUserReference = getCurrentUserReference();
        currentUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    User user = new User();
                    FirebaseUser firebaseUser = getFirebaseUser();
                    user.setEmail(firebaseUser.getEmail());
                    user.setId(firebaseUser.getUid());
                    currentUserReference.setValue(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static DatabaseReference getCurrentUserReference() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseUserReference = database.getReference("user");
        final FirebaseUser firebaseUser = getFirebaseUser();
        String uid = firebaseUser.getUid();
        return databaseUserReference.child(uid);
    }

    @NonNull
    private static FirebaseUser getFirebaseUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            throw new IllegalStateException("User is not logged in");
        }
        return firebaseUser;
    }

    public static void writeMedOnDb(final Med med) {
        getCurrentUserReference()
                .child("meds")
                .push()
                .setValue(med);
    }
    public static void writeReportOnDb(final Report report) {
        getCurrentUserReference()
                .child("reports")
                .push()
                .setValue(report);
    }

    public static void writePermissionOnDb(final Permission permission) {
        String key = getCurrentUserReference()
                .child("permissions")
                .push()
                .getKey();
        permission.setKey(key);
        getCurrentUserReference()
                .child("permissions")
                .child(key)
                .setValue(permission);
    }

    public static void updateMedOnDb(final Med med) {
        getCurrentUserReference()
                .child("meds")
                .child(med.getKey())
                .setValue(med);
    }

    public static void updatePermissionOnDb(final Permission permission) {
        getCurrentUserReference()
                .child("permissions")
                .child(permission.getKey())
                .setValue(permission);
    }

    public static DatabaseReference getMedsReference() {
        return getCurrentUserReference().child("meds");
    }

    public static DatabaseReference getReportsReference() {
        return getCurrentUserReference().child("reports");
    }

    public static DatabaseReference getPermissionssReference() {
        return getCurrentUserReference().child("permissions");
    }

    public static void removeMed(MedsAdapterItem medsAdapterItem) {
        String key = medsAdapterItem.getMed().getKey();
        getMedsReference().child(key).removeValue();
    }

    public static DatabaseReference getUserRootDatabaseReference() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference("user");
    }

    public static String getCurrentUserEmail() {
        final FirebaseUser firebaseUser = getFirebaseUser();
        return firebaseUser.getEmail();
    }

    public static void removePermission(Permission permission) {
        String key = permission.getKey();
        getPermissionssReference().child(key).removeValue();
    }
}
