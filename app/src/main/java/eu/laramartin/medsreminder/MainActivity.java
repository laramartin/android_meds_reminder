package eu.laramartin.medsreminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.getLoginIntent;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 498;
    private static final String LOG_TAG = MainActivity.class.getCanonicalName();
    @BindView(R.id.bottom_navigation_view)
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            // not signed in
            startLoginActivity();
        } else {
            loadContentWhenSuccessfulLogin(savedInstanceState);
        }
    }

    private void startLoginActivity() {
        startActivityForResult(
                getLoginIntent(),
                RC_SIGN_IN);
    }

    private void loadContentWhenSuccessfulLogin(Bundle savedInstanceState) {
        Toast.makeText(this, "You're signed in!", Toast.LENGTH_SHORT).show();
        if (savedInstanceState == null) {
            openFragment(new MedsFragment());
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_meds:
                        openFragment(new MedsFragment());
                        return true;
                    case R.id.menu_friends:
                        openFragment(new FriendsFragment());
                        return true;
                    case R.id.menu_report:
                        openFragment(new ReportFragment());
                        return true;
                }
                return false;
            }
        });
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // don't do anything :)
            }
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                loadContentWhenSuccessfulLogin(null);
                return;
            } else {
                // Sign in failed
                Log.e(LOG_TAG, "error in sign in");
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, R.string.sign_in_cancelled, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this, R.string.unknown_sign_in_response, Toast.LENGTH_SHORT).show();
        }
    }
}
