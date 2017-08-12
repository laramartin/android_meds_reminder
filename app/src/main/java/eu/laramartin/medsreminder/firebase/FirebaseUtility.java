package eu.laramartin.medsreminder.firebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import eu.laramartin.medsreminder.MainActivity;
import eu.laramartin.medsreminder.R;

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
                .setLogo(R.drawable.ic_pill_capsule_128)
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
}
