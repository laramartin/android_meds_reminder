package eu.laramartin.medsreminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FriendsFragment extends Fragment {

    private static final String LOG_TAG = FriendsFragment.class.getCanonicalName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(LOG_TAG, "Friends fragment selected");
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }
}
