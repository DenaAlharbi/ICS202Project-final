

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Btrees {
    // B+ Tree instances for the three search criteria
    BPlusTree idIndex = new BPlusTree();
    BPlusTree lastNameIndex = new BPlusTree();
    BPlusTree firstNameIndex = new BPlusTree();
    BPlusTree levelIndex=new BPlusTree();
    List<Student> editedStudents = new ArrayList<>();

    // Add a student to all three B+ trees
    public void addStudent(Student student) {
        idIndex.insert(student.getId().hashCode(), student); // Index by ID
        lastNameIndex.insert(student.getLastName().hashCode(), student); // Index by last name
        firstNameIndex.insert(student.getFirstName().hashCode(), student); // Index by first name
        levelIndex.insert(student.getLevel().hashCode(),student);// Index by level
    }

    // Search by exact student ID
    public Student searchById(String id) {
        return idIndex.search(id.hashCode());
    }

    // Search by exact last name
    public List<Student> searchByLastName(String lastName) {
        return lastNameIndex.searchAll(lastName.hashCode());
    }

    // Search by exact first name
    public List<Student> searchByFirstName(String firstName) {
        return firstNameIndex.searchAll(firstName.hashCode());
    }
    public List<Student> searchByLevel(String level){
        return levelIndex.searchAll(level.hashCode());
    }

    // Implementation of the B+ Tree structure
    public class BPlusTree {
        private int m;
        private LeafNode firstLeaf;

        // Constructor
        public BPlusTree() {
            this.m = 4; // Degree of the tree, can be adjusted
        }

        public BPlusTree(int m) {
            this.m = m;
        }

        // Insert method
        public void insert(int key, Student value) {
            if (firstLeaf == null) {
                firstLeaf = new LeafNode();
                firstLeaf.insert(new DictionaryPair(key, value));
            } else {
                LeafNode targetLeaf = findLeafNode(key);
                if (!targetLeaf.insert(new DictionaryPair(key, value))) {
                    // Split logic if the leaf node is full
                }
            }
        }
        public void delete(int key, Student value) {
            if (firstLeaf != null) {
                LeafNode targetLeaf = findLeafNode(key);
                targetLeaf.delete(key, value);
            }
        }

        // Search for a single value by key
        public Student search(int key) {
            if (firstLeaf == null) return null;
            LeafNode targetLeaf = findLeafNode(key);
            return targetLeaf.search(key);
        }

        // Search for all students matching a key
        public List<Student> searchAll(int key) {
            if (firstLeaf == null) return new ArrayList<>();
            LeafNode targetLeaf = findLeafNode(key);
            if (targetLeaf == null) return new ArrayList<>();

            List<Student> students = targetLeaf.searchAll(key);
            if (students.isEmpty()) {
                System.out.println("No students found with the given key.");
            } else {
                int index = 1;
                Set<Integer> usedIndexes = new HashSet<>();
                for (Student student : students) {
                    while (usedIndexes.contains(index)) {
                        index++;
                    }
                    System.out.println(index + " - " + student.getId() + ", " + student.getFirstName() + ", " + student.getLastName() + ", " + student.getBirth() + ", " + student.getLevel());
                    usedIndexes.add(index);
                    index++;
                }
            }
            return students;
        }



        private LeafNode findLeafNode(int key) {
            return firstLeaf;
        }

        // Inner classes for the B+ tree
        private class LeafNode {
            private List<DictionaryPair> dictionary = new ArrayList<>();

            // Insert a key-value pair into the leaf node
            public boolean insert(DictionaryPair pair) {
                dictionary.add(pair);
                dictionary.sort(DictionaryPair::compareTo);
                return dictionary.size() <= m - 1; // Return false if full
            }
            public void delete(int key, Student value) {
                dictionary.removeIf(pair -> pair.key == key && pair.value.equals(value));
            }

            // Search for a single value by key
            public Student search(int key) {
                for (DictionaryPair pair : dictionary) {
                    if (pair.key == key) {
                        return pair.value;
                    }
                }
                return null;
            }

            // Search for all matching values by key
            public List<Student> searchAll(int key) {
                List<Student> result = new ArrayList<>();
                for (DictionaryPair pair : dictionary) {
                    if (pair.key == key) {
                        result.add(pair.value);
                    }
                }
                return result;
            }
        }

        private class DictionaryPair implements Comparable<DictionaryPair> {
            int key;
            Student value;

            public DictionaryPair(int key, Student value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public int compareTo(DictionaryPair o) {
                return Integer.compare(this.key, o.key);
            }
        }

        public List<Student> getAllStudents() {
            List<Student> allStudents = new ArrayList<>();
            LeafNode current = firstLeaf;
            while (current != null) {
                for (DictionaryPair pair : current.dictionary) {
                    allStudents.add(pair.value);
                } // Move to the next leaf node by finding the next node in the dictionary
                current = findNextLeaf(current);
            }
            return allStudents;
        }
        private LeafNode findNextLeaf(LeafNode current) {
            return current;

        }}


    //menues
    public void displayAndEditStudents(List<Student> students, Btrees database) {
        if (!students.isEmpty()) {
            int index = 1;

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the index number of the student you want to edit or type 'exit' to quit:");
            String input = scanner.nextLine();
            if (!input.equalsIgnoreCase("exit")) {
                try {
                    int selectedIndex = Integer.parseInt(input);
                    if (selectedIndex > 0 && selectedIndex <= students.size()) {
                        Student selectedStudent = students.get(selectedIndex - 1);
                        //System.out.println(selectedStudent.getFirstName());
                        System.out.println("Enter the number of the command you want:");
                        System.out.println("1-Edit student");
                        System.out.println("2-Delete student");
                        System.out.println("3-Return to main menu");
                        String input2 = scanner.nextLine();
                        if(Objects.equals(input2, "1"))
                            editStudent(selectedStudent, database);
                        else if (Objects.equals(input2, "2"))
                            deleteStudent(selectedStudent,database);
                        else if (Objects.equals(input2, "3"))
                            ToMainMenu(database);
                        else
                            System.out.println("Invalid input.");

                    } else {
                        System.out.println("Invalid index. Exiting.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Exiting.");
                }
            } else {
                System.out.println("Exiting.");
            }
        }else{
            ToMainMenu(database);

        }
    }

    public void displayAndEditStudents(Student student, Btrees database) {

        System.out.println(student.getId() + ", " + student.getFirstName() + ", " + student.getLastName() + ", " + student.getBirth() + ", " + student.getLevel());

        System.out.println("Enter the number of the command you want:");
        System.out.println("1-Edit student");
        System.out.println("2-Delete student");
        System.out.println("3-Return to main menu");
        Scanner scanner = new Scanner(System.in);
        String input2 = scanner.nextLine();
        if (Objects.equals(input2, "1"))
            editStudent(student,database);
        else if (Objects.equals(input2, "2"))
            deleteStudent(student, database);
        else if (Objects.equals(input2, "3"))
            ToMainMenu(database);
        else
            System.out.println("Invalid input.");


    }

    public void editStudent(Student student, Btrees database) {
        System.out.println(student.getId() + ", " + student.getFirstName() + ", " + student.getLastName() + ", " + student.getBirth() + ", " + student.getLevel());
        System.out.println("Enter the number of the command you want:");
        System.out.println("1-Edit student first name");
        System.out.println("2-Edit student last name");
        System.out.println("3-Edit student university level");
        Scanner scanner = new Scanner(System.in);
        String input3 = scanner.nextLine();
        System.out.println("What would you like the new edit to be?");
        String input4 = scanner.nextLine();

        //database.deleteStudent(student, database);

        if (Objects.equals(input3, "1")) {
            student.setFirstName(input4);
        } else if (Objects.equals(input3, "2")) {
            student.setLastName(input4);
        } else if (Objects.equals(input3, "3")) {
            student.setLevel(input4);
        } else {
            System.out.println("Invalid input.");
            ToMainMenu(database);
            return;
        }
        //database.addStudent(student);
        database.editedStudents.add(student);

        System.out.println(student.getId() + ", " + student.getFirstName() + ", " + student.getLastName() + ", " + student.getBirth() + ", " + student.getLevel());
        ToMainMenu(database);
    }


    public void deleteStudent(Student student, Btrees database) {
        idIndex.delete(student.getId().hashCode(), student); // Remove from ID index
        lastNameIndex.delete(student.getLastName().hashCode(), student); // Remove from last name index
        firstNameIndex.delete(student.getFirstName().hashCode(), student); // Remove from first name index
        levelIndex.delete(student.getLevel().hashCode(), student); // Remove from level index
        ToMainMenu(database);
    }


    public void ToMainMenu(Btrees database) {
        System.out.println("Enter the number of the command you want:");
        System.out.println("1-Search student");
        System.out.println("2-Add new student");
        System.out.println("3-Show students in academic level");
        System.out.println("4-Exit");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();


        if (Objects.equals(input, "1")) {
            //editStudent(selectedStudent);
            System.out.println("How would you like to search for a student");
            System.out.println("1- By first name");
            System.out.println("2- By last name");
            System.out.println("3- By exact student id");
            System.out.println("4- By exact student level");
            String input3 = scanner.nextLine();


            if (Objects.equals(input3, "1")) {
                System.out.println("What is the first name?");
                String inputAns = scanner.nextLine();
                //String input2 = scanner.nextLine();

                database.displayAndEditStudents(database.searchByFirstName(inputAns), database);


            } else if (Objects.equals(input3, "2")) {
                System.out.println("What is the last name?");
                String inputAns = scanner.nextLine();
                //String input2 = scanner.nextLine();

                database.displayAndEditStudents(database.searchByLastName(inputAns),database);


            } else if (Objects.equals(input3, "3")) {
                System.out.println("What is the Id?");
                String inputAns = scanner.nextLine();
                Student student = database.searchById(inputAns);
                displayAndEditStudents(student,database);


            }else if(Objects.equals(input3,"4")){
                System.out.println("What is the level ?");
                String inputAns = scanner.nextLine();
                database.displayAndEditStudents(database.searchByLevel(inputAns), database);

            }
            else {
                System.out.println("Invalid input.");
                ToMainMenu(database);
            }


        }else if (Objects.equals(input, "2")) {
            Scanner scanner1 = new Scanner(System.in);
            String id;
            do {// Get Student ID with validation
                System.out.println("\nEnter Student ID (5 digits): ");
                id = scanner1.next();
                if (!id.matches("\\d{5}")) {
                    System.out.println("Invalid ID. Please enter a 5-digit number.");
                }
            } while (!id.matches("\\d{5}"));
            System.out.println("Enter First Name: ");//first name
            String firstname = scanner1.next();

            System.out.println("Enter Last Name: ");//last name
            String lastName = scanner1.nextLine();

            scanner.nextLine(); // date of birth
            System.out.println("Enter Date of Birth (DD/MM/YYYY): ");
            String birth = scanner1.next();

            String level;// academic level
            do {
                System.out.println("Enter Academic Level (FR, SO, JR, SR): ");
                level = scanner1.next().toUpperCase();
                if (!level.matches("FR|SO|JR|SR")) {
                    System.out.println("Invalid level. Please enter one of the following: FR, SO, JR, SR.");
                }
            } while (!level.matches("FR|SO|JR|SR"));

            // Add student to the database
            Student newStudent = new Student(id, lastName, firstname, birth, level);
            database.addStudent(newStudent);
            System.out.println("Student added successfully!");
            System.out.println(newStudent.getId() + ", " + newStudent.getFirstName() + ", " + newStudent.getLastName() + ", " + newStudent.getBirth() + ", " + newStudent.getLevel());
            ToMainMenu(database);





        }else if (Objects.equals(input, "3")) {
            System.out.println("What is the level you want to search with");
            String input2 = scanner.nextLine();
            displayAndEditStudents(database.searchByLevel(input2),database);
        }else if (Objects.equals(input, "4")) {
            System.out.println("Exiting...");
            //writeCSV("C:\\Users\\denaa\\JavaProjects231\\ICS202PPFF\\src\\students-details.csv", database.idIndex.getAllStudents(), database.editedStudents);        }else{
            //System.out.println("Invalid input. Try again!");
            //ToMainMenu(database);}



    }
//    public void writeCSV(String filePath, List<Student> students, List<Student> editedStudents) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            for (Student student : students) {
//                writer.write(student.toCSV());
//                writer.newLine();
//            }
////            for (Student student : editedStudents) {
////                writer.write(student.toCSV());
////                writer.newLine();
////            }
//            System.out.println("CSV file written successfully!");
//        } catch (IOException e) {
//            System.out.println("Error writing CSV file: " + e.getMessage());
//        }
//    }





}}