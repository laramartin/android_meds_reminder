package eu.laramartin.medsreminder.model;

public class Permission {

    private String key;
    private String email;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "key='" + key + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
