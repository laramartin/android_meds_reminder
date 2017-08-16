package eu.laramartin.medsreminder.model;

public class Take {

    private String medName;
    private String timeTaken;
    private String dayTaken;

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getDayTaken() {
        return dayTaken;
    }

    public void setDayTaken(String dayTaken) {
        this.dayTaken = dayTaken;
    }

    @Override
    public String toString() {
        return "Take{" +
                "medName='" + medName + '\'' +
                ", timeTaken='" + timeTaken + '\'' +
                ", dayTaken='" + dayTaken + '\'' +
                '}';
    }
}
