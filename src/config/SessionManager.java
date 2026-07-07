package config;

public class SessionManager {
    private static int currentUser = -1;

    public static void login(int userId) {
        currentUser = userId;
    }

    public static int getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = -1;  
    }
    
    public static boolean isLoggedIn() {
        return currentUser != -1;
    }
}