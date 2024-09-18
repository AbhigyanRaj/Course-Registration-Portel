package university.users;

import java.io.*;
import java.util.*;

import university.courses.CourseViewer;


public class StudentPortal {

    public void viewAvailableCourses(String branch, int semester) {
        List<String[]> availableCourses = CourseViewer.getAvailableCourses(branch, semester);
        if (availableCourses.isEmpty()) {
            System.out.println("No available courses for " + branch + " in semester " + semester);
        } else {
            CourseViewer.displayAvailableCourses(availableCourses, branch, semester);
        }
    }

    //regiistration
    public void registerForCourses(String email, String branch, int semester) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\n*** Register for Courses ***");
            System.out.println("1. View Current Registered Courses");
            System.out.println("2. Add New Courses");
            System.out.println("3. Exit to Student Menu");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // To consume the rest of the line

            switch (choice) {
                case 1:
                    viewRegisteredCourses(email);
                    break;
                case 2:
                    addCourses(email, branch, semester);
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting Register for Courses menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void viewRegisteredCourses(String email) {
        List<String> registeredCourses = getRegisteredCoursesForStudent(email);
        if (registeredCourses.isEmpty()) {
            System.out.println("You are not registered for any courses.");
            return;
        }

        System.out.println("\n*** Current Registered Courses ***");
        for (String course : registeredCourses) {
            System.out.println(course);
        }
    }

    //add couress
    public void addCourses(String email, String branch, int semester) {
        List<String[]> availableCourses = CourseViewer.getAvailableCourses(branch, semester);
        if (availableCourses.isEmpty()) {
            System.out.println("No available courses to add.");
            return;
        }

        CourseViewer.displayAvailableCourses(availableCourses, branch, semester);

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Course IDs you want to add (separate by commas): ");
        String courseIds = sc.nextLine();
        String[] coursesToAdd = courseIds.split(",");

        List<String> validCourses = new ArrayList<>();
        for (String courseId : coursesToAdd) {
            courseId = courseId.trim();
            boolean courseExists = false;

            for (String[] course : availableCourses) {
                if (course[0].equalsIgnoreCase(courseId)) {
                    courseExists = true;
                    break;
                }
            }

            if (courseExists) {
                validCourses.add(courseId);
            } else {
                System.out.println("Course ID " + courseId + " is invalid or does not exist.");
            }
        }

        if (!validCourses.isEmpty()) {
            for (String courseId : validCourses) {
                updateRegisteredCourses(email, courseId);
            }
            System.out.println("The following courses have been successfully added: " + String.join(", ", validCourses));
        } else {
            System.out.println("No valid courses were added. Please try again.");
        }
    }

    public void updateRegisteredCourses(String email, String courseId) {
        File file = new File("src/resources/data/registeredCourses.txt");
        Map<String, List<String>> registeredCourses = new HashMap<>();

        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] studentCourses = line.split(";");
                    if (studentCourses.length > 1) {
                        String studentEmail = studentCourses[0];
                        List<String> courses = new ArrayList<>(Arrays.asList(studentCourses[1].split(",")));
                        registeredCourses.put(studentEmail, courses);
                    }
                }
                reader.close();
            }

            List<String> courses = registeredCourses.getOrDefault(email, new ArrayList<>());
            if (!courses.contains(courseId)) {
                courses.add(courseId);
                registeredCourses.put(email, courses);

                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                for (Map.Entry<String, List<String>> entry : registeredCourses.entrySet()) {
                    if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                        writer.write(entry.getKey() + ";" + String.join(",", entry.getValue()) + "\n");
                    }
                }
                writer.close();
            } else {
                System.out.println("You have already registered for the course: " + courseId);
            }

        } catch (IOException e) {
            System.out.println("Error updating registered courses file: " + e.getMessage());
        }
    }

    public void submitComplaint(String email) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the description of your complaint: ");
        String description = sc.nextLine();

        File file = new File("src/resources/data/complaints.txt");

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(email + "," + description + ",Pending\n");
            writer.close();
            System.out.println("Complaint submitted successfully!");
        } catch (IOException e) {
            System.out.println("Error submitting complaint: " + e.getMessage());
        }
    }

    //complaint
    public void viewComplaintStatus(String email) {
        File file = new File("src/resources/data/complaints.txt");
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] complaintDetails = line.split(",");
                if (complaintDetails[0].equals(email)) {
                    System.out.println("Complaint: " + complaintDetails[1] + " | Status: " + complaintDetails[2]);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No complaints found.");
            }
        } catch (IOException e) {
            System.out.println("Error reading complaints: " + e.getMessage());
        }
    }

    public void viewSchedule(String email) {
        List<String> registeredCourses = getRegisteredCoursesForStudent(email);
        if (registeredCourses.isEmpty()) {
            System.out.println("You have not registered for any courses.");
            return;
        }

        Map<String, List<String>> weeklySchedule = new LinkedHashMap<>();
        weeklySchedule.put("Monday", new ArrayList<>());
        weeklySchedule.put("Tuesday", new ArrayList<>());
        weeklySchedule.put("Wednesday", new ArrayList<>());
        weeklySchedule.put("Thursday", new ArrayList<>());
        weeklySchedule.put("Friday", new ArrayList<>());

        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/courses.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;
                }

                String[] courseDetails = line.split(",");
                if (courseDetails.length < 9) {

                    continue;
                }

                if (registeredCourses.contains(courseDetails[0])) {
                    String day = getDayFromTime(courseDetails[7]);
                    String scheduleEntry = courseDetails[0] + " - " + courseDetails[1] + " | " +
                            "Prof: " + courseDetails[6] + " | " +
                            "Time: " + courseDetails[7] + " | " +
                            "Location: " + courseDetails[8];

                    weeklySchedule.get(day).add(scheduleEntry);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading courses file: " + e.getMessage());
        }

        System.out.println("\n*** Your Weekly Course Schedule ***");
        for (Map.Entry<String, List<String>> entry : weeklySchedule.entrySet()) {
            System.out.println("\n" + entry.getKey() + ":");
            if (entry.getValue().isEmpty()) {
                System.out.println("No classes.");
            } else {
                for (String course : entry.getValue()) {
                    System.out.println(course);
                }
            }
        }
    }

    private String getDayFromTime(String time) {
        if (time.startsWith("Mon")) {
            return "Monday";
        } else if (time.startsWith("Tue")) {
            return "Tuesday";
        } else if (time.startsWith("Wed")) {
            return "Wednesday";
        } else if (time.startsWith("Thu")) {
            return "Thursday";
        } else if (time.startsWith("Fri")) {
            return "Friday";
        }
        return "Unknown";
    }

    private List<String> getRegisteredCoursesForStudent(String email) {
        List<String> registeredCourses = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/resources/data/registeredCourses.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] studentCourses = line.split(";");
                if (studentCourses[0].equals(email)) {
                    registeredCourses = Arrays.asList(studentCourses[1].split(","));
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading registered courses file: " + e.getMessage());
        }
        return registeredCourses;
    }
}


//code by abhigyann:) : this code dealing with all feature related to students.