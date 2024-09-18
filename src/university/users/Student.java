package university.users;

import university.courses.CourseViewer;
import university.utils.TrackAcademicProgress;
import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;


public class Student implements User {
    private String name;
    private String email;
    private String password;
    private String branch;
    private int currentSemester;
    private boolean isAuthenticated;

    public Student() {
        this.isAuthenticated = false;
    }

    @Override
    public void login() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Student Login ---");
        System.out.print("Enter your email: ");
        this.email = sc.nextLine().trim();
        System.out.print("Enter your password: ");
        this.password = sc.nextLine();
        if (authenticateStudent(this.email, this.password)) {
            System.out.println("Login successful!");
            this.isAuthenticated = true;
            showStudentMenu();
        } else {
            System.out.println("Login failed. Incorrect email or password.");
        }
    }

    private boolean authenticateStudent(String email, String password) {
        File file = new File("src/resources/data/students.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentDetails = line.split(",");
                if (studentDetails.length >= 5) {
                    String storedEmail = studentDetails[1].trim();
                    String storedPassword = studentDetails[2];
                    if (storedEmail.equalsIgnoreCase(email) && storedPassword.equals(password)) {
                        this.name = studentDetails[0];
                        this.branch = studentDetails[3];
                        this.currentSemester = Integer.parseInt(studentDetails[4]);
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading students file: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Student Sign Up ---");
        System.out.print("Enter your name: ");
        this.name = sc.nextLine();
        System.out.print("Enter your email: ");
        this.email = sc.nextLine();
        System.out.print("Enter your password: ");
        this.password = sc.nextLine();
        System.out.print("Enter your branch (CSE, ECE, etc.): ");
        this.branch = sc.nextLine();
        System.out.print("Enter your current semester: ");
        this.currentSemester = sc.nextInt();

        storeStudentDetails();

        System.out.println("Sign-up successful! You can now log in.");
        this.isAuthenticated = true;
        showStudentMenu();
    }

    private void storeStudentDetails() {
        File file = new File("src/resources/data/students.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(this.name + "," + this.email + "," + this.password + "," + this.branch + "," + this.currentSemester + "\n");
        } catch (IOException e) {
            System.out.println("Error storing student details: " + e.getMessage());
        }
    }

    public void showStudentMenu() {
        if (!isAuthenticated) {
            System.out.println("You need to log in first.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n*** Student Portal ***");
            System.out.println("1. View Available Courses");
            System.out.println("2. Register for Courses");
            System.out.println("3. View Schedule");
            System.out.println("4. Track Academic Progress");
            System.out.println("5. Submit Complaint");
            System.out.println("6. View Complaint Status");
            System.out.println("7. Drop Courses");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    List<String[]> availableCourses = CourseViewer.getAvailableCourses(this.branch, this.currentSemester);
                    CourseViewer.displayAvailableCourses(availableCourses, this.branch, this.currentSemester);
                    break;
                case 2:
                    StudentPortal portal = new StudentPortal();
                    portal.registerForCourses(this.email, this.branch, this.currentSemester);
                    break;
                case 3:
                    portal = new StudentPortal();
                    portal.viewSchedule(this.email);
                    break;
                case 4:
                    TrackAcademicProgress trackProgress = new TrackAcademicProgress();
                    trackProgress.trackProgress(this.email);
                    break;
                case 5:
                    submitComplaint();
                    break;
                case 6:
                    portal = new StudentPortal();
                    portal.viewComplaintStatus(this.email);
                    break;
                case 7:
                    dropCourses();
                    break;
                case 8:
                    exit = true;
                    System.out.println("Exiting Student Portal...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void dropCourses() {
        System.out.println("\n--- Drop Courses ---");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the course ID you want to drop: ");
        String courseIdToDrop = sc.nextLine().trim();

        File registeredCoursesFile = new File("src/resources/data/registeredCourses.txt");
        List<String> remainingCourses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(registeredCoursesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] registeredCourseDetails = line.split(",");
                if (registeredCourseDetails[0].equalsIgnoreCase(this.email) && !registeredCourseDetails[1].equalsIgnoreCase(courseIdToDrop)) {
                    remainingCourses.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading registered courses file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(registeredCoursesFile))) {
            for (String course : remainingCourses) {
                writer.write(course + "\n");
            }
            System.out.println("Course dropped successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to registered courses file: " + e.getMessage());
        }
    }

    private void submitComplaint() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Submit Complaint ---");
        System.out.print("Enter the description of your complaint: ");
        String complaintDescription = sc.nextLine();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/resources/data/complaints.txt", true))) {
            writer.write(this.email + "," + complaintDescription + ",Pending\n");
            System.out.println("Complaint submitted successfully!");
        } catch (IOException e) {
            System.out.println("Error submitting complaint: " + e.getMessage());
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

//code by abhigyann:) : this code dealing with all feature related to students.