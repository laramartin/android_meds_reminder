package eu.laramartin.medsreminder.firebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
import eu.laramartin.medsreminder.model.Med;
import eu.laramartin.medsreminder.model.User;

import static android.support.v4.content.ContextCompat.startActivity;


public class FirebaseUtility {

    private static final String LOG_TAG = FirebaseUtility.class.getCanonicalName();

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
                } else {
                    User currentUser = dataSnapshot.getValue(User.class);
                    Log.v(LOG_TAG, "User exists: " + currentUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v(LOG_TAG, "onCancelled");
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

    public static void writeMedOnDb(final Med newMed) {
        final Med med = new Med();
        med.setName(newMed.getName());
        med.setTime(newMed.getTime());
        med.setDays(newMed.getDays());
        med.setDosage(newMed.getDosage());
        if (!newMed.getNotes().isEmpty()) {
            med.setNotes(newMed.getNotes());
        }
        // Adds a new med directly
        getCurrentUserReference()
                .child("meds")
                .push()
                .setValue(med);
    }

    public static DatabaseReference getMedsReference() {
        return getCurrentUserReference().child("meds");

    }
}
