package eu.laramartin.medsreminder.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String email;
    private List<Take> takes = new ArrayList<>();
    private List<String> allowedUsersPerEmail = new ArrayList<>();

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
                ", takes=" + takes +
                ", allowedUsersPerEmail=" + allowedUsersPerEmail +
                '}';
    }
}
