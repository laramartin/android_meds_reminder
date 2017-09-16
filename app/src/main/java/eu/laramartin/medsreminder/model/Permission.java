package eu.laramartin.medsreminder.model;

public class Permission {

    public String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "email='" + email + '\'' +
                '}';
    }
}
