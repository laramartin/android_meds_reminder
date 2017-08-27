package eu.laramartin.medsreminder;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.writeMedOnDb;

public class AddMedActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddMedActivity.class.getCanonicalName();
    @BindView(R.id.add_dosage_spinner)
    Spinner dosageSpinner;
    @BindView(R.id.add_name_input)
    EditText nameEditText;
    @BindView(R.id.add_name_input_text_layout)
    TextInputLayout nameTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setDosagePicker();

    }

    private boolean isNameEmpty() {
        return getMedName().isEmpty();
    }

    private void setDosagePicker() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dosage_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dosageSpinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_med_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_med:
                // TODO: 19.08.17 Lara: implement saving medication to db
                if (!isNameEmpty()) {
                    Log.v(LOG_TAG, "name is not empty!");
                    // TODO: 20.08.17 Lara: save medication to db
                    writeMedOnDb(getMedName());
                    Toast.makeText(this, "Saving med to DB!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                } else {
                    nameTextInputLayout.setError(getString(R.string.error_add_med_empty_name));
                    return false;
                }
        }
        return false;
    }

    private String getMedName() {
        return nameEditText.getText().toString();
    }
}
