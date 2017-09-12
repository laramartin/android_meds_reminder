package eu.laramartin.medsreminder.model;

public class Report {

    private final String medName;
    private final String timeTaken;

    public Report(String medName, String timeTaken) {
        this.medName = medName;
        this.timeTaken = timeTaken;
    }

    public String getMedName() {
        return medName;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    @Override
    public String toString() {
        return "Report{" +
                "medName='" + medName + '\'' +
                ", timeTaken='" + timeTaken + '\'' +
                '}';
    }
}
