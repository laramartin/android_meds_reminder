package eu.laramartin.medsreminder.model;

import java.util.ArrayList;
import java.util.List;

public class Med {

    private String name;
    private String time;
    private String days;
    private String dosage;
    private String notes;
    private String key;
    private List<String> reminderJobTags = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getReminderJobTags() {
        return reminderJobTags;
    }

    public void setReminderJobTags(List<String> reminderJobTags) {
        this.reminderJobTags = reminderJobTags;
    }

    @Override
    public String toString() {
        return "Med{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", days='" + days + '\'' +
                ", dosage='" + dosage + '\'' +
                ", notes='" + notes + '\'' +
                ", key='" + key + '\'' +
                ", reminderJobTags=" + reminderJobTags +
                '}';
    }
}
