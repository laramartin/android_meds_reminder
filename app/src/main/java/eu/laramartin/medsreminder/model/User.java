package eu.laramartin.medsreminder.model;

import java.util.Map;

public class User {

    private String id;
    private String email;
//    private List<Med> meds = new ArrayList<>();
    private Map<String, Report> reports;
//    private List<Permission> permissions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Report> getReports() {
        return reports;
    }

    public void setReports(Map<String, Report> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", reports=" + reports +
                '}';
    }
}
