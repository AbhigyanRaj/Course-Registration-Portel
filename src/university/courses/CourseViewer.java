package university.courses;

import java.io.*;
import java.util.*;

public class CourseViewer {

    public static List<String[]> getAvailableCourses(String branch, int semester) {
        List<String[]> availableCourses = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/courses.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }

                String[] courseDetails = line.split(",");
                if (courseDetails.length < 8) {
                    System.out.println("Error: Incomplete course data. Skipping entry.");
                    continue;
                }

                String courseBranch = courseDetails[2];
                int courseSemester = Integer.parseInt(courseDetails[3]);

                // Check branch and semester match the filters
                if (courseBranch.equalsIgnoreCase(branch) && courseSemester == semester) {
                    availableCourses.add(courseDetails);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing semester value: " + e.getMessage());
        }

        return availableCourses;
    }

    // Method to display available courses in a tabular format
    public static void displayAvailableCourses(List<String[]> courses, String branch, int semester) {
        if (courses.isEmpty()) {
            System.out.println("No available courses for " + branch + " in semester " + semester);
            return;
        }

        System.out.println("\nAvailable Courses for " + branch + " - Semester " + semester + ":");
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.printf("%-12s | %-30s | %-15s | %-10s | %-7s | %-15s%n",
                "Course ID", "Course Name", "Professor", "Timings", "Credits", "Prerequisites");
        System.out.println("-------------------------------------------------------------------------------------");

        for (String[] course : courses) {
            // Ensuring that we have the expected number of fields before printing
            if (course.length >= 8) {
                System.out.printf("%-12s | %-30s | %-15s | %-10s | %-7s | %-15s%n",
                        course[0], course[1], course[6], course[7], course[4], course[5]);
            } else {
                System.out.println("Skipping incomplete course data.");
            }
        }
        System.out.println("-------------------------------------------------------------------------------------");
    }
}

//code by abhigyann:) : Visual inchancer code and Previwer.