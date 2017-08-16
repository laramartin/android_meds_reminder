package eu.laramartin.medsreminder.model;

import java.util.List;

public class User {

    private String id;
    private String email;
    private List<Med> meds;
    private List<Take> takes;
    private List<String> allowedUsersPerEmail;

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

    public List<Med> getMeds() {
        return meds;
    }

    public void setMeds(List<Med> meds) {
        this.meds = meds;
    }

    public List<Take> getTakes() {
        return takes;
    }

    public void setTakes(List<Take> takes) {
        this.takes = takes;
    }

    public List<String> getAllowedUsersPerEmail() {
        return allowedUsersPerEmail;
    }

    public void setAllowedUsersPerEmail(List<String> allowedUsersPerEmail) {
        this.allowedUsersPerEmail = allowedUsersPerEmail;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", meds=" + meds +
                ", takes=" + takes +
                ", allowedUsersPerEmail=" + allowedUsersPerEmail +
                '}';
    }
}
