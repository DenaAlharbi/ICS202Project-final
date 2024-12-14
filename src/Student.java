

public class Student {
    private String id;
    private String firstName;
    private String lastName;
    private String level;
    private String birth;

    // Constructor
    public Student(String id, String lastName, String firstName, String birth, String level) {
        this.birth = birth;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
    }

    public String getId() {
        return id;
    }
    public String getBirth() {
        return birth;
    }
    public void setBirth() {
        this.birth = birth;;
    }



    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    public String toCSV() {
        return String.join(",", id, firstName, lastName, birth, level);
    }
}
