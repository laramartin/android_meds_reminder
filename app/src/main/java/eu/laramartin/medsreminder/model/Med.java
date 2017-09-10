package eu.laramartin.medsreminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Med implements Parcelable {

    public Med() {
    }

    private String name;
    private String time;
    private String days;
    private String dosage;
    private String notes;
    private String key;
    private List<String> reminderJobTags = new ArrayList<>();

    protected Med(Parcel in) {
        name = in.readString();
        time = in.readString();
        days = in.readString();
        dosage = in.readString();
        notes = in.readString();
        key = in.readString();
        reminderJobTags = in.createStringArrayList();
    }

    public static final Creator<Med> CREATOR = new Creator<Med>() {
        @Override
        public Med createFromParcel(Parcel in) {
            return new Med(in);
        }

        @Override
        public Med[] newArray(int size) {
            return new Med[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(time);
        parcel.writeString(days);
        parcel.writeString(dosage);
        parcel.writeString(notes);
        parcel.writeString(key);
        parcel.writeStringList(reminderJobTags);
    }
}
