package university.users;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Professor implements User {
    private String name;
    private String email;
    private String password;
    private boolean isAuthenticated;

    public Professor() {
        this.isAuthenticated = false;
    }

    // Login functionality for the professor
    @Override
    public void login() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Professor Login ---");
        System.out.print("Enter your email: ");
        this.email = sc.nextLine();
        System.out.print("Enter your password: ");
        this.password = sc.nextLine();

        if (authenticateProfessor(this.email, this.password)) {
            System.out.println("Login successful!");
            this.isAuthenticated = true;
            showProfessorMenu();
        }
        else {
            System.out.println("Login failed. Incorrect email or password.");
        }
    }

    private boolean authenticateProfessor(String email, String password) {
        File file = new File("src/resources/data/professors.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] professorDetails = line.split(",");
                if (professorDetails.length == 3) {
                    String storedEmail = professorDetails[0];
                    String storedPassword = professorDetails[1];
                    if (storedEmail.equalsIgnoreCase(email) && storedPassword.equals(password)) {
                        this.name = professorDetails[2];  // Assign name after successful authentication
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading professors file: " + e.getMessage());
        }
        return false;
    }

    @Override
    public void signUp() {
        Scanner sc = new Scanner(System.in);

        System.out.println("\n--- Professor Sign Up ---");
        System.out.print("Enter your name: ");
        this.name = sc.nextLine();
        System.out.print("Enter your email: ");
        this.email = sc.nextLine();
        System.out.print("Enter your password: ");
        this.password = sc.nextLine();

        File file = new File("src/resources/data/professors.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(this.email + "," + this.password + "," + this.name + "\n");
            System.out.println("Sign-up successful! You can now log in.");
            this.isAuthenticated = true;
            showProfessorMenu();

        }
        catch (IOException e) {
            System.out.println("Error writing to professors file: " + e.getMessage());
        }
    }

    public void showProfessorMenu() {
        if (!isAuthenticated) {
            System.out.println("You need to log in first.");
            return;
        }

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n*** Professor Portal ***");
            System.out.println("1. Manage Courses");
            System.out.println("2. View Enrolled Students");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    viewManageCourses();
                    break;
                case 2:
                    viewEnrolledStudents();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting Professor Portal...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private void viewManageCourses() {
        System.out.println("\n--- Manage Courses ---");

        List<String[]> professorCourses = new ArrayList<>();
        File file = new File("src/resources/data/professor_courses.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseDetails = line.split(",");
                if (courseDetails[2].equals(this.email)) {  // Check if course belongs to the professor
                    professorCourses.add(courseDetails);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading professor courses file: " + e.getMessage());
        }

        if (professorCourses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("Your Courses:");
            for (String[] course : professorCourses) {
                System.out.println(course[0] + ": " + course[1] + " | Credits: " + course[3] + " | Timings: " + course[6] + " and " + course[7]);
            }

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the course ID to manage (or 0 to exit): ");
            String courseID = sc.nextLine();

            if (!courseID.equals("0")) {
                manageCourseDetails(courseID, professorCourses);
            } else {
                System.out.println("Exiting Manage Courses...");
            }
        }
    }

    private void manageCourseDetails(String courseID, List<String[]> professorCourses) {
        for (String[] course : professorCourses) {
            if (course[0].equalsIgnoreCase(courseID)) {
                System.out.println("\nEditing details for course: " + course[1]);
                Scanner sc = new Scanner(System.in);
                System.out.println("What would you like to update?");
                System.out.println("1. Syllabus");
                System.out.println("2. Class Timings");
                System.out.println("3. Credits");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter new syllabus: ");
                        course[6] = sc.nextLine();
                        break;
                    case 2:
                        System.out.print("Enter new class timings (format: Day Time): ");
                        course[6] = sc.nextLine();
                        break;
                    case 3:
                        System.out.print("Enter new credits: ");
                        course[3] = sc.nextLine();
                        break;
                    default:
                        System.out.println("Invalid choice.");
                        return;
                }

                updateCourseInFile(course);
                System.out.println("Course details updated successfully.");
                return;
            }
        }
        System.out.println("Invalid Course ID.");
    }

    private void updateCourseInFile(String[] updatedCourse) {
        File file = new File("src/resources/data/professor_courses.txt");
        List<String[]> allCourses = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseDetails = line.split(",");
                if (courseDetails[0].equalsIgnoreCase(updatedCourse[0])) {
                    allCourses.add(updatedCourse);
                } else {
                    allCourses.add(courseDetails);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading courses file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String[] course : allCourses) {
                writer.write(String.join(",", course) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating courses file: " + e.getMessage());
        }
    }


    private void viewEnrolledStudents() {
        System.out.println("\n--- View Enrolled Students ---");

        List<String[]> professorCourses = fetchCoursesTaughtByProfessor();

        if (professorCourses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
            return;
        }


        System.out.println("Your Courses:");
        professorCourses.forEach(course -> System.out.println("Course ID: " + course[0] + " | Course Name: " + course[1]));


        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter the Course ID to view enrolled students: ");
        String courseID = sc.nextLine();
        displayEnrolledStudents(courseID);
    }


    private List<String[]> fetchCoursesTaughtByProfessor() {
        List<String[]> courses = new ArrayList<>();
        File file = new File("src/resources/data/professor_courses.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length > 2 && details[2].equalsIgnoreCase(this.email)) {
                    courses.add(new String[]{details[0], details[1]});  // assuming index 0 is course ID, 1 is course name
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading professor courses file: " + e.getMessage());
        }
        return courses;
    }

    private void displayEnrolledStudents(String courseId) {
        File file = new File("src/resources/data/enrolled_students.txt");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\nEnrolled Students for Course ID: " + courseId + ":");
            System.out.printf("%-20s %-30s %-10s%n", "Student Name", "Student Email", "Grade");

            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length > 3 && details[0].equalsIgnoreCase(courseId)) {  // assuming index 0 is course ID
                    System.out.printf("%-20s %-30s %-10s%n", details[1], details[2], details[3]);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No students are currently enrolled in this course.");
            }
        } catch (IOException e) {
            System.out.println("Error reading enrolled students file: " + e.getMessage());
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
