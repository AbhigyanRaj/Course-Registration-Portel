package university.users;

import java.io.*;
import java.util.*;

public class Administrator implements User {
    private boolean isAuthenticated;

    public Administrator() {
        this.isAuthenticated = false;
    }

    @Override
    public void login() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Administrator Login ---");
        System.out.print("Enter your email: ");
        String email = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        if (authenticateAdmin(email, password)) {
            System.out.println("Login successful!");
            this.isAuthenticated = true;
            showAdminMenu();
        }
        else {
            System.out.println("Login failed. Incorrect email or password.");
        }
    }

    @Override
    public void signUp() {
        System.out.println("Admin sign-up is not required.");
    }

    private boolean authenticateAdmin(String email, String password) {
        return email.equals("admin@iiitd.ac.in") && password.equals("admin123");
    }

    public void showAdminMenu() {
        if (!isAuthenticated) {
            System.out.println("You need to log in first.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n*** Administrator Portal ***");
            System.out.println("1. Manage Course Catalog");
            System.out.println("2. Manage Student Records");
            System.out.println("3. Assign Professors to Courses");
            System.out.println("4. Handle Complaints");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    manageCourseCatalog();
                    break;
                case 2:
                    manageStudentRecords();
                    break;
                case 3:
                    assignProfessorsToCourses();
                    break;
                case 4:
                    handleComplaints();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting Administrator Portal...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void manageCourseCatalog() {
        System.out.println("\n--- Manage Course Catalog ---");
        System.out.println("1. View Courses");
        System.out.println("2. Add New Course");
        System.out.println("3. Delete Course");
        System.out.println("4. Exit to Admin Menu");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (choice) {
            case 1:
                viewCourses();
                break;
            case 2:
                addCourse();
                break;
            case 3:
                deleteCourse();
                break;
            case 4:
                System.out.println("Exiting Course Catalog Management...");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void viewCourses() {
        String filePath = "src/resources/data/courses.txt";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Courses file not found at " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseDetails = line.split(",");
                if (courseDetails.length >= 7) {
                    System.out.println("Course ID: " + courseDetails[0] + " | Name: " + courseDetails[1] + " | Professor: " + courseDetails[6]);
                } else {
                    System.out.println("Invalid course entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading courses file: " + e.getMessage());
        }
    }

    private void addCourse() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Add New Course ---");
        System.out.print("Enter Course ID: ");
        String courseId = sc.nextLine();
        System.out.print("Enter Course Name: ");
        String courseName = sc.nextLine();
        System.out.print("Enter Professor: ");
        String professor = sc.nextLine();
        System.out.print("Enter Credits: ");
        String credits = sc.nextLine();
        System.out.print("Enter Prerequisites: ");
        String prerequisites = sc.nextLine();
        System.out.print("Enter Timings: ");
        String timings = sc.nextLine();
        System.out.print("Enter Location: ");
        String location = sc.nextLine();

        String courseDetails = courseId + "," + courseName + "," + professor + "," + credits + "," + prerequisites + "," + timings + "," + location;

        String filePath = "src/resources/data/courses.txt";
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(courseDetails + "\n");
            System.out.println("Course added successfully.");
        } catch (IOException e) {
            System.out.println("Error adding course: " + e.getMessage());
        }
    }

    private void deleteCourse() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Delete Course ---");
        System.out.print("Enter Course ID to delete: ");
        String courseId = sc.nextLine();

        String filePath = "src/resources/data/courses.txt";
        File file = new File(filePath);
        List<String> courses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(courseId + ",")) {
                    courses.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading courses file: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String course : courses) {
                writer.write(course + "\n");
            }
            System.out.println("Course deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error deleting course: " + e.getMessage());
        }
    }

    private void manageStudentRecords() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Manage Student Records ---");
        System.out.println("1. View All Records");
        System.out.println("2. Update a Record");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                viewAllStudentRecords();
                break;
            case 2:
                updateStudentRecord();
                break;
            case 3:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    private void viewAllStudentRecords() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                String grades = fetchGradesForStudent(details[1]); // Assuming details[1] is email
                System.out.printf("Name: %s, Email: %s, Branch: %s, Semester: %s, Grades: %s\n", details[0], details[1], details[2], details[3], grades);
            }
        } catch (IOException e) {
            System.out.println("Failed to read student records: " + e.getMessage());
        }
    }

    private String fetchGradesForStudent(String email) {
        StringBuilder gradesBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/student_grades.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(email)) {
                    gradesBuilder.append(details[1]).append(": ").append(details[3]).append(", "); // Course: Grade
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read grades: " + e.getMessage());
        }
        return gradesBuilder.toString();
    }



    private void updateStudentRecord() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the email of the student you wish to update: ");
        String email = sc.nextLine();
        boolean found = false;

        List<String> records = new ArrayList<>();
        String newDetails = "";

        // Read all records from file
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[1].equals(email)) {
                    found = true;
                    System.out.println("Current record: " + String.join(",", details));
                    System.out.println("What would you like to update?");
                    System.out.println("1. Name");
                    System.out.println("2. Branch");
                    System.out.println("3. Semester");
                    int updateChoice = sc.nextInt();
                    sc.nextLine(); // Consume newline character

                    switch (updateChoice) {
                        case 1:
                            System.out.print("Enter new name: ");
                            details[0] = sc.nextLine();
                            break;
                        case 2:
                            System.out.print("Enter new branch: ");
                            details[2] = sc.nextLine();
                            break;
                        case 3:
                            System.out.print("Enter new semester: ");
                            details[3] = sc.nextLine();
                            break;
                        default:
                            System.out.println("Invalid option. Exiting update...");
                            return;
                    }
                    newDetails = String.join(",", details); // Update the details
                }
                records.add(line);
            }
        } catch (IOException e) {
            System.out.println("Failed to read student records: " + e.getMessage());
            return;
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/data/students.txt"))) {
                for (String record : records) {
                    if (record.contains(email)) {
                        writer.write(newDetails + "\n");
                    } else {
                        writer.write(record + "\n");
                    }
                }
                System.out.println("Record updated successfully.");
            } catch (IOException e) {
                System.out.println("Failed to write to student records: " + e.getMessage());
            }
        } else {
            System.out.println("No student found with the email: " + email);
        }
        showAdminMenu();
    }

    private void assignProfessorsToCourses() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Assign Professors to Courses ---");

        List<String[]> courses = readCourses();
        List<String[]> professors = readProfessors();

        System.out.println("Available Courses:");
        for (String[] course : courses) {
            if (course.length > 1) {
                System.out.println("Course ID: " + course[0] + ", Name: " + course[1]);
            }
        }

        System.out.print("Enter the Course ID to assign a professor: ");
        String courseId = sc.nextLine();
        String[] selectedCourse = courses.stream()
                .filter(c -> c[0].equals(courseId))
                .findFirst()
                .orElse(null);

        if (selectedCourse != null) {
            System.out.println("Available Professors:");
            for (String[] professor : professors) {
                if (professor.length > 2) {
                    System.out.println("Professor Email: " + professor[0] + ", Name: " + professor[2]);
                }
            }

            System.out.print("Enter the email of the professor to assign: ");
            String professorEmail = sc.nextLine();
            String[] selectedProfessor = professors.stream()
                    .filter(p -> p[0].equals(professorEmail))
                    .findFirst()
                    .orElse(null);

            if (selectedProfessor != null) {
                saveAssignment(selectedCourse[0], selectedProfessor[0], selectedProfessor[2]);
                System.out.println("Assigned Professor " + selectedProfessor[2] + " to Course " + selectedCourse[1]);
            } else {
                System.out.println("Invalid professor email.");
            }
        } else {
            System.out.println("Invalid course ID.");
        }

        showAdminMenu();
    }

    private void saveAssignment(String courseId, String professorEmail, String professorName) {
        String record = courseId + "," + professorEmail + "," + professorName + "\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/data/Assigned_proff.txt", true))) {
            writer.write(record);
        } catch (IOException e) {
            System.out.println("Error writing to Assigned_prof file: " + e.getMessage());
        }
    }

    public void viewAssignedProfessors() {
        System.out.println("\n--- View Assigned Professors ---");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/Assigned_proff.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                System.out.println("Course ID: " + details[0] + ", Professor Email: " + details[1] + ", Professor Name: " + details[2]);
            }
        } catch (IOException e) {
            System.out.println("Error reading Assigned_prof file: " + e.getMessage());
        }
    }

    private List<String[]> readCourses() {
        List<String[]> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/courses.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                courses.add(details);
            }
        } catch (IOException e) {
            System.out.println("Error reading courses file: " + e.getMessage());
        }
        return courses;
    }

    private List<String[]> readProfessors() {
        List<String[]> professors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/professors.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                professors.add(details);
            }
        } catch (IOException e) {
            System.out.println("Error reading professors file: " + e.getMessage());
        }
        return professors;
    }

    private void updateCourseProfessor(String courseId, String professorEmail) {
       //will do it (agar free time mila tho)
    }


    private void handleComplaints() {
        System.out.println("\n--- Handle Complaints ---");
        String filePath = "src/resources/data/complaints.txt";
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] complaintDetails = line.split(",");
                if (complaintDetails.length >= 3) {
                    System.out.println("Complaint from " + complaintDetails[0] + ": " + complaintDetails[1] + " | Status: " + complaintDetails[2]);
                }
            }

            System.out.println("\nEnter the email of the student whose complaint you want to resolve: ");
            Scanner sc = new Scanner(System.in);
            String studentEmail = sc.nextLine();
            List<String> complaints = new ArrayList<>();

            try (BufferedReader newReader = new BufferedReader(new FileReader(file))) {
                while ((line = newReader.readLine()) != null) {
                    String[] complaintDetails = line.split(",");
                    if (complaintDetails[0].equals(studentEmail)) {
                        complaints.add(complaintDetails[0] + "," + complaintDetails[1] + ",Resolved");
                    } else {
                        complaints.add(line);
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String complaint : complaints) {
                    writer.write(complaint + "\n");
                }
                System.out.println("Complaint resolved successfully!");
            } catch (IOException e) {
                System.out.println("Error writing to complaints file: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error reading complaints file: " + e.getMessage());
        }
    }

    @Override
    public void logout() {
        System.out.println("Logging out...");
        this.isAuthenticated = false;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }
}
