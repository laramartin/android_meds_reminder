package eu.laramartin.medsreminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ReportFragment extends Fragment {

    private static final String LOG_TAG = ReportFragment.class.getCanonicalName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(LOG_TAG, "report fragment selected");
        return inflater.inflate(R.layout.fragment_report, container, false);
    }
}
