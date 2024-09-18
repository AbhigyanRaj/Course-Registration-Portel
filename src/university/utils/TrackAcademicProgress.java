package university.utils;

import java.io.*;
import java.util.*;

public class TrackAcademicProgress {

    public static void trackProgress(String studentEmail) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Track Academic Progress ---");
        System.out.println("1. View Previous Marks");
        System.out.println("2. Calculate CGPA");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                viewPreviousMarks(studentEmail);
                break;
            case 2:
                calculateCGPA(studentEmail);
                break;
            default:
                System.out.println("Invalid choice. Returning to Student Portal...");
        }
    }

    private static void viewPreviousMarks(String studentEmail) {
        File file = new File("src/resources/data/student_grades.txt");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("\n--- Your Previous Marks ---");
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(studentEmail)) {
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

    private static void calculateCGPA(String studentEmail) {
        File file = new File("src/resources/data/student_grades.txt");
        List<Double> allGrades = new ArrayList<>();
        int totalCourses = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                if (details[0].equals(studentEmail)) {
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
