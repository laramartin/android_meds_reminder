package eu.laramartin.medsreminder.model;

public class Med {

    private String name;
    private String time;
    private String days;
    private String dosage;
    private String notes;

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

    @Override
    public String toString() {
        return "Med{" +
                "name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", days='" + days + '\'' +
                ", dosage='" + dosage + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
