package university.academic;

import java.io.*;
import java.util.*;

public class AcademicProgress {

    private String email;

    public AcademicProgress(String email) {
        this.email = email;
    }

    public void trackProgress() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Track Academic Progress ---");
        System.out.println("1. View Previous Marks");
        System.out.println("2. Calculate CGPA");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                viewPreviousMarks();
                break;
            case 2:
                calculateCGPA();
                break;
            default:
                System.out.println("Invalid choice. Returning to Student Portal...");
        }
    }

    private void viewPreviousMarks() {
        File file = new File("src/resources/data/student_grades.txt");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\n--- Your Previous Marks ---");
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(this.email)) {
                    System.out.println("Course ID: " + details[1] + " | Semester: " + details[2] + " | Grade: " + details[3]);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No previous marks found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading grades file: " + e.getMessage());
        }
    }

    private void calculateCGPA() {
        File file = new File("src/resources/data/student_grades.txt");
        List<Double> allGrades = new ArrayList<>();
        int totalCourses = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(this.email)) {
                    allGrades.add(Double.parseDouble(details[3]));
                    totalCourses++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading grades file: " + e.getMessage());
        }

        if (allGrades.isEmpty()) {
            System.out.println("No grades available to calculate CGPA.");
            return;
        }

        double sum = 0;
        for (double grade : allGrades) {
            sum += grade;
        }

        double cgpa = sum / totalCourses;
        System.out.printf("Your CGPA is: %.2f\n", cgpa);
    }
}


//code by abhigyann:) : this code dealing with all feature related to students - marks wala part gpa calculation.