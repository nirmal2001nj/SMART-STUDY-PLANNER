package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UserManager {

    private static final String USER_FILE =
            "users.txt";

    // ==========================================
    // REGISTER
    // ==========================================

    public static boolean registerUser(
            String username,
            String password,
            String recoveryAnswer) {

        username = username.trim();

        if (username.isEmpty() ||
                password.isEmpty() ||
                recoveryAnswer.isEmpty()) {

            return false;
        }

        if (userExists(username)) {
            return false;
        }

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(
                                     USER_FILE,
                                     true
                             ))) {

            writer.write(
                    username + "|" +
                            hash(password) + "|" +
                            hash(recoveryAnswer)
            );

            writer.newLine();

            createUserFolder(username);

            return true;

        } catch (IOException e) {

            e.printStackTrace();

            return false;
        }
    }

    // ==========================================
    // LOGIN
    // ==========================================

    public static boolean loginUser(
            String username,
            String password) {

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(USER_FILE))) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] parts =
                        line.split("\\|");

                if (parts.length == 3) {

                    if (parts[0].equals(username)
                            &&
                            parts[1].equals(
                                    hash(password))) {

                        return true;
                    }
                }
            }

        } catch (FileNotFoundException e) {

            return false;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // CHECK USER
    // ==========================================

    public static boolean userExists(
            String username) {

        File file =
                new File(USER_FILE);

        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(file))) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] parts =
                        line.split("\\|");

                if (parts.length >= 1 &&
                        parts[0].equalsIgnoreCase(
                                username)) {

                    return true;
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }

        return false;
    }

    // ==========================================
    // RESET PASSWORD
    // ==========================================

    public static boolean resetPassword(
            String username,
            String recoveryAnswer,
            String newPassword) {

        File file =
                new File(USER_FILE);

        if (!file.exists()) {
            return false;
        }

        File tempFile =
                new File("users_temp.txt");

        boolean updated = false;

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new FileReader(file));

                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(tempFile))
        ) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] parts =
                        line.split("\\|");

                if (parts.length == 3 &&
                        parts[0].equalsIgnoreCase(
                                username) &&
                        parts[2].equals(
                                hash(recoveryAnswer))) {

                    writer.write(
                            parts[0] + "|" +
                                    hash(newPassword) + "|" +
                                    parts[2]
                    );

                    updated = true;

                } else {

                    writer.write(line);
                }

                writer.newLine();
            }

        } catch (IOException e) {

            e.printStackTrace();

            return false;
        }

        if (updated) {

            if (file.delete()) {

                tempFile.renameTo(file);
            }

        } else {

            tempFile.delete();
        }

        return updated;
    }

    // ==========================================
    // DELETE ACCOUNT
    // ==========================================

    public static boolean deleteAccount(
            String username
    ) {

        if (username == null ||
                username.trim().isEmpty()) {

            return false;
        }

        // --------------------------------------
        // Delete user's subjects
        // --------------------------------------

        boolean subjectsDeleted =
                SubjectManager
                        .deleteAllSubjectsForUser(
                                username
                        );

        if (!subjectsDeleted) {
            return false;
        }

        // --------------------------------------
        // Delete user's assignments
        // --------------------------------------

        boolean assignmentsDeleted =
                AssignmentManager
                        .deleteAllAssignmentsForUser(
                                username
                        );

        if (!assignmentsDeleted) {
            return false;
        }

        // --------------------------------------
        // Delete user's exams
        // --------------------------------------

        boolean examsDeleted =
                ExamManager
                        .deleteAllExamsForUser(
                                username
                        );

        if (!examsDeleted) {
            return false;
        }

        // --------------------------------------
        // Remove user from users.txt
        // --------------------------------------

        File file =
                new File(USER_FILE);

        if (file.exists()) {

            File tempFile =
                    new File("users_temp.txt");

            try (
                    BufferedReader reader =
                            new BufferedReader(
                                    new FileReader(file));

                    BufferedWriter writer =
                            new BufferedWriter(
                                    new FileWriter(tempFile))
            ) {

                String line;

                while ((line =
                        reader.readLine()) != null) {

                    String[] parts =
                            line.split("\\|");

                    if (parts.length >= 1 &&
                            parts[0].equalsIgnoreCase(
                                    username)) {

                        continue;
                    }

                    writer.write(line);
                    writer.newLine();
                }

            } catch (IOException e) {

                e.printStackTrace();

                tempFile.delete();

                return false;
            }

            if (!file.delete()) {

                tempFile.delete();

                return false;
            }

            if (!tempFile.renameTo(file)) {

                return false;
            }
        }

        // --------------------------------------
        // Delete user's folder
        // --------------------------------------

        File userFolder =
                new File(
                        "user_data",
                        username
                );

        if (userFolder.exists()) {

            if (!deleteFolder(userFolder)) {
                return false;
            }
        }

        return true;
    }

    // ==========================================
    // DELETE FOLDER
    // ==========================================

    private static boolean deleteFolder(
            File folder
    ) {

        File[] files =
                folder.listFiles();

        if (files != null) {

            for (File file : files) {

                if (file.isDirectory()) {

                    if (!deleteFolder(file)) {
                        return false;
                    }

                } else {

                    if (!file.delete()) {
                        return false;
                    }
                }
            }
        }

        return folder.delete();
    }

    // ==========================================
    // CREATE USER FOLDER
    // ==========================================

    private static void createUserFolder(
            String username) {

        File folder =
                new File(
                        "user_data",
                        username
                );

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    // ==========================================
    // GET USER FOLDER
    // ==========================================

    public static String getUserFolder(
            String username) {

        File folder =
                new File(
                        "user_data",
                        username
                );

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return folder.getPath();
    }

    // ==========================================
    // HASH
    // ==========================================

    private static String hash(
            String text) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] bytes =
                    digest.digest(
                            text.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder result =
                    new StringBuilder();

            for (byte b : bytes) {

                result.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return result.toString();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }
}