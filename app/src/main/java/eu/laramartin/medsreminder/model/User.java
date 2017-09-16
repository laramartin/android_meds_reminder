package eu.laramartin.medsreminder.model;

public class User {

    private String id;
    private String email;
//    private List<Med> meds = new ArrayList<>();
//    private List<Report> reports = new ArrayList<>();
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

//    public List<Report> getReports() {
//        return reports;
//    }
//
//    public void setReports(List<Report> reports) {
//        this.reports = reports;
//    }
//
//    public List<Permission> getPermissions() {
//        return permissions;
//    }
//
//    public void setPermissions(List<Permission> permissions) {
//        this.permissions = permissions;
//    }
//
//    public List<Med> getMeds() {
//        return meds;
//    }
//
//    public void setMeds(List<Med> meds) {
//        this.meds = meds;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id='" + id + '\'' +
//                ", email='" + email + '\'' +
//                ", meds=" + meds +
//                ", reports=" + reports +
//                ", permissions=" + permissions +
//                '}';
//    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
