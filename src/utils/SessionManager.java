package utils;

public class SessionManager {

    private static String loggedInUsername;

    public static void login(String username) {
        loggedInUsername = username;
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static void logout() {
        loggedInUsername = null;
    }
}