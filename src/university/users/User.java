package university.users;

public interface User {
    void login();
    void logout();
    boolean isAuthenticated();
    void signUp();  // Add for Student/Professor only
}
