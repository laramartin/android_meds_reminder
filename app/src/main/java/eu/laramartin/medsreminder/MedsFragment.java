package eu.laramartin.medsreminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;


public class MedsFragment extends Fragment {

    private static final String LOG_TAG = MedsFragment.class.getCanonicalName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(LOG_TAG, "meds fragment selected");
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
//        if (appCompatActivity.getSupportActionBar() != null) {
////            appCompatActivity.getSupportActionBar().setTitle("title");
////            appCompatActivity.getSupportActionBar().setSubtitle("subtitle");
//        }
        return inflater.inflate(R.layout.fragment_meds, container, false);
    }
}
