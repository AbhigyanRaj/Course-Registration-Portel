package university;

import university.users.Student;
import university.users.Professor;
import university.users.Administrator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Main mainApp = new Main();
        mainApp.startApp();
    }

    public void startApp() {
        boolean exit = false;

        while (!exit) {
            clearScreen();
            System.out.println("=================================");
            System.out.println(" Welcome to the University System ");
            System.out.println("=================================");
            System.out.println("Please choose an option:");
            System.out.println("1. Student");
            System.out.println("2. Professor");
            System.out.println("3. Administrator (Login Only)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    studentLoginOrSignUp();
                    break;
                case 2:
                    professorLoginOrSignUp();
                    break;
                case 3:
                    adminLogin();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void studentLoginOrSignUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n1. Log in as Student");
        System.out.println("2. Sign up as Student");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        Student student = new Student();
        switch (choice) {
            case 1:
                student.login();
                break;
            case 2:
                student.signUp();
                break;
            default:
                System.out.println("Invalid option. Returning to main menu.");
        }
    }

    private void professorLoginOrSignUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n1. Log in as Professor");
        System.out.println("2. Sign up as Professor");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        Professor professor = new Professor();
        switch (choice) {
            case 1:
                professor.login();
                break;
            case 2:
                professor.signUp();
                break;
            default:
                System.out.println("Invalid option. Returning to main menu.");
        }
    }

    private void adminLogin() {
        Administrator admin = new Administrator();
        admin.login();
    }


    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

//code by abhigyann:)