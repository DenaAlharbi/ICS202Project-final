




import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDatabase {
    public static List<Student> readCSV(String path) {
        List<Student> studentsGiven = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String id = values[0];
                String firstName = values[2];
                String lastName = values[1];
                String birth = values[3];
                String level = values[4];
                studentsGiven.add(new Student(id, lastName, firstName, birth, level));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentsGiven;
    }
    public static void writeCSV(String path, List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Student student : students) {
                bw.write(student.getId() + "," + student.getFirstName() + "," + student.getLastName() + "," + student.getBirth() + "," + student.getLevel());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<Student> students = readCSV("C:\\Users\\denaa\\JavaProjects231\\ICS202PPFF\\src\\students-details.csv");
        System.out.println(students);
        Btrees database = new Btrees();
        for (Student student : students) {
            database.addStudent(student);
        }
        database.ToMainMenu(database);
    }
}