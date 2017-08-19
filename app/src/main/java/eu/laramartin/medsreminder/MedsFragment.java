package eu.laramartin.medsreminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.createUserIfDoesntExist;
import static eu.laramartin.medsreminder.firebase.FirebaseUtility.signOut;


public class MedsFragment extends Fragment {

    private static final String LOG_TAG = MedsFragment.class.getCanonicalName();

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meds, container, false);
        unbinder = ButterKnife.bind(this, view);
        AppCompatActivity appCompatActivity = ((AppCompatActivity) getActivity());
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        createUserIfDoesntExist();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

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
        }
        return super.onOptionsItemSelected(item);
    }
}
