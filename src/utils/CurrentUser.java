package utils;

public class CurrentUser {

    private static String username;

    public static void setUsername(
            String username
    ) {
        CurrentUser.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static void clear() {
        username = null;
    }
}