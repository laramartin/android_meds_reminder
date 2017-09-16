package eu.laramartin.medsreminder.permissions;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.R;
import eu.laramartin.medsreminder.common.DialogsUtility;

public class PermissionsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.fab_permissions)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        ButterKnife.bind(this);

        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DialogsUtility.showInviteFriendDialog(this);
    }
}
