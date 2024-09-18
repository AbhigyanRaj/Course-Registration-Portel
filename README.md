#Project Title: University Course Management System made by abhigyann :)

___Description___

This project is a Java-based university course management system designed to facilitate course registration, assignment of professors to courses, and handling student records and complaints. It provides separate access rights for students, professors, and administrators.

Let's Start!!

___How to start___

Open the project in your chosen Java IDE.
Ensure that the src/resources/data directory is correctly set up as per the project structure for data files like students.txt, professors.txt, etc.
Run Main.java to start the application.

___what i did ?___

All necessary data files are pre-populated in the src/resources/data directory.
Professors and courses are predefined in the system with necessary details.

___OOP Principles Applied___

Encapsulation: The system uses classes to encapsulate and manage the properties and behaviors associated with the various entities such as students, professors, and administrators. Each class controls the visibility of its own data fields via private access modifiers and provides public setter and getter methods to modify and view the data.

Inheritance: The User interface is implemented by Student, Professor, and Administrator classes, allowing for shared methods like login and logout, ensuring reusability and maintainability.

Polymorphism: Polymorphism is used via method overriding. For example, each user type (Student, Professor, Administrator) has a unique implementation of the showMenu method, which defines specific functionalities available to that user type.

Abstraction: The User interface provides an abstract layer, hiding the implementation details of methods like login, logout, and showMenu from the user, allowing the system to define common protocols for all types of users.


Link to Git Repositries :


made by
Abhigyann :)

2023018
