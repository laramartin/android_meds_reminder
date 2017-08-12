package eu.laramartin.medsreminder.firebase;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;

public class FirebaseUtility {

    public static Intent getLoginIntent() {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                        Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                .setIsSmartLockEnabled(false)
                .build();
    }

}
