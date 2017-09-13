package eu.laramartin.medsreminder.model;

public class Report {

    public String medName;
    public String timeTaken;

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

    @Override
    public String toString() {
        return "Report{" +
                "medName='" + medName + '\'' +
                ", timeTaken='" + timeTaken + '\'' +
                '}';
    }
}
