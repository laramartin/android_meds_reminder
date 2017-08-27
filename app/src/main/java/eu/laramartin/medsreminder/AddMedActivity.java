package eu.laramartin.medsreminder;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.laramartin.medsreminder.model.Med;

import static eu.laramartin.medsreminder.firebase.FirebaseUtility.writeMedOnDb;

public class AddMedActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddMedActivity.class.getCanonicalName();
    @BindView(R.id.add_dosage_spinner)
    Spinner dosageSpinner;
    @BindView(R.id.add_name_input)
    EditText nameEditText;
    @BindView(R.id.add_name_input_text_layout)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.add_time_input)
    TextView timeText;
    @BindView(R.id.add_days_monday)
    TextView mondayText;
    @BindView(R.id.add_days_tuesday)
    TextView tuesdayText;
    @BindView(R.id.add_days_wednesday)
    TextView wednesdayText;
    @BindView(R.id.add_days_thursday)
    TextView thursdayText;
    @BindView(R.id.add_days_friday)
    TextView fridayText;
    @BindView(R.id.add_days_saturday)
    TextView saturdayText;
    @BindView(R.id.add_days_sunday)
    TextView sundayText;

    private List<TextView> daysTextView = new ArrayList<>();
    private String selectedDays = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimePicker();
            }
        });
        setDays();
        setDaysListener();
        setDosagePicker();
    }

    private void setDaysListener() {
        for (int i = 0; i < daysTextView.size(); i++) {
            daysTextView.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDaySelectedState(view);
                }
            });
        }
    }

    private void setDaySelectedState(View view) {
        TextView currentDayTextView = (TextView) view;
        String day = currentDayTextView.getText().toString();
        if (isDaySelected(day)) {
            selectedDays = selectedDays.replace(day, "");
            setDayViewBackground(currentDayTextView, false);
        } else {
            selectedDays = selectedDays.concat(day);
            setDayViewBackground(currentDayTextView, true);
        }
    }

    private void setDayViewBackground(TextView currentDayTextView, boolean paintSelected) {
        int colorId;
        if (paintSelected) {
            colorId = R.color.colorAccent;
        } else {
            colorId = R.color.colorSoftGrey;
        }
        currentDayTextView.setBackgroundColor(ContextCompat.getColor(this, colorId));
    }

    private boolean isDaySelected(String day) {
        return selectedDays.contains(day);
    }

    private void setDays() {
        daysTextView.add(mondayText);
        daysTextView.add(tuesdayText);
        daysTextView.add(wednesdayText);
        daysTextView.add(thursdayText);
        daysTextView.add(fridayText);
        daysTextView.add(saturdayText);
        daysTextView.add(sundayText);
    }

    private void setTimePicker() {
        String selectedTime = getTimeInput();
        String[] timeParts = selectedTime.split(":");
        int hour = Integer.valueOf(timeParts[0]);
        int minute = Integer.valueOf(timeParts[1]);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AddMedActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                timeText.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle(getString(R.string.add_time_picker_title));
        mTimePicker.show();
    }

    @NonNull
    private String getTimeInput() {
        return timeText.getText().toString();
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
                    writeMedOnDb(getMedInfo());
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

    private Med getMedInfo() {
        Med med = new Med();
        med.setName(getMedName());
        med.setTime(getTimeInput());
        med.setDays(selectedDays);
        med.setDosage(getDosage());
        return med;
    }

    private String getDosage() {
        return dosageSpinner.getSelectedItem().toString();
    }

    private String getMedName() {
        return nameEditText.getText().toString();
    }

}
