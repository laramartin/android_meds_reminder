package eu.laramartin.medsreminder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import eu.laramartin.medsreminder.permissions.PermissionsActivity;
import eu.laramartin.medsreminder.reminders.RemindersActivity;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.signOut;

public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_bar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                signOut(getContext());
                return true;
            case R.id.menu_reminders:
                Intent remindersIntent = new Intent(getActivity(), RemindersActivity.class);
                startActivity(remindersIntent);
                return true;
            case R.id.menu_permissions:
                Intent permissionsIntent = new Intent(getActivity(), PermissionsActivity.class);
                startActivity(permissionsIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
