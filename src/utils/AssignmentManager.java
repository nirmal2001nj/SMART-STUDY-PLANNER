package utils;

import models.Assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentManager {

    private static final String FILE_NAME =
            "assignments.txt";

    // ==========================================
    // ADD ASSIGNMENT
    // ==========================================

    public static void addAssignment(
            Assignment assignment
    ) {

        try {

            FileWriter writer =
                    new FileWriter(
                            FILE_NAME,
                            true
                    );

            writer.write(
                    assignment.getUsername()
                            + "|"
                            + assignment.getSubjectName()
                            + "|"
                            + assignment.getAssignmentName()
                            + "|"
                            + assignment.getDueDate()
                            + "|"
                            + assignment.getPriority()
                            + "|"
                            + assignment.isCompleted()
                            + "\n"
            );

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // ==========================================
    // GET USER ASSIGNMENTS
    // ==========================================

    public static List<Assignment>
    getAssignmentsForUser(
            String username
    ) {

        List<Assignment> assignments =
                new ArrayList<>();

        File file =
                new File(FILE_NAME);

        if (!file.exists()) {
            return assignments;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length != 6) {
                    continue;
                }

                if (!data[0].equals(username)) {
                    continue;
                }

                Assignment assignment =
                        new Assignment(
                                data[0],
                                data[1],
                                data[2],
                                data[3],
                                data[4],
                                Boolean.parseBoolean(
                                        data[5]
                                )
                        );

                assignments.add(assignment);
            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return assignments;
    }

    // ==========================================
    // DELETE ASSIGNMENT
    // ==========================================

    public static void deleteAssignment(
            String username,
            String assignmentName
    ) {

        List<Assignment> assignments =
                getAllAssignments();

        try {

            FileWriter writer =
                    new FileWriter(FILE_NAME);

            for (Assignment assignment :
                    assignments) {

                if (assignment.getUsername()
                        .equals(username)
                        &&
                        assignment
                                .getAssignmentName()
                                .equals(assignmentName)) {

                    continue;
                }

                writeAssignment(
                        writer,
                        assignment
                );
            }

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // ==========================================
    // UPDATE ASSIGNMENT
    // ==========================================

    public static void updateAssignment(
            Assignment updatedAssignment
    ) {

        List<Assignment> assignments =
                getAllAssignments();

        try {

            FileWriter writer =
                    new FileWriter(FILE_NAME);

            for (Assignment assignment :
                    assignments) {

                if (assignment.getUsername()
                        .equals(
                                updatedAssignment
                                        .getUsername()
                        )
                        &&
                        assignment
                                .getAssignmentName()
                                .equals(
                                        updatedAssignment
                                                .getAssignmentName()
                                )) {

                    assignment =
                            updatedAssignment;
                }

                writeAssignment(
                        writer,
                        assignment
                );
            }

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // ==========================================
    // MARK COMPLETE / INCOMPLETE
    // ==========================================

    public static void toggleCompletion(
            String username,
            String assignmentName
    ) {

        List<Assignment> assignments =
                getAllAssignments();

        try {

            FileWriter writer =
                    new FileWriter(FILE_NAME);

            for (Assignment assignment :
                    assignments) {

                if (assignment.getUsername()
                        .equals(username)
                        &&
                        assignment
                                .getAssignmentName()
                                .equals(assignmentName)) {

                    assignment.setCompleted(
                            !assignment.isCompleted()
                    );
                }

                writeAssignment(
                        writer,
                        assignment
                );
            }

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // ==========================================
    // DELETE ALL ASSIGNMENTS OF ONE USER
    // ==========================================

    public static boolean deleteAllAssignmentsForUser(
            String username
    ) {

        List<Assignment> assignments =
                getAllAssignments();

        File file =
                new File(FILE_NAME);

        if (!file.exists()) {
            return true;
        }

        try {

            File tempFile =
                    new File("assignments_temp.txt");

            FileWriter writer =
                    new FileWriter(tempFile);

            for (Assignment assignment :
                    assignments) {

                if (assignment.getUsername()
                        .equals(username)) {

                    continue;
                }

                writeAssignment(
                        writer,
                        assignment
                );
            }

            writer.close();

            if (!file.delete()) {
                tempFile.delete();
                return false;
            }

            if (!tempFile.renameTo(file)) {
                return false;
            }

            return true;

        } catch (IOException e) {

            e.printStackTrace();

            return false;
        }
    }

    // ==========================================
    // WRITE ASSIGNMENT
    // ==========================================

    private static void writeAssignment(
            FileWriter writer,
            Assignment assignment
    ) throws IOException {

        writer.write(
                assignment.getUsername()
                        + "|"
                        + assignment.getSubjectName()
                        + "|"
                        + assignment.getAssignmentName()
                        + "|"
                        + assignment.getDueDate()
                        + "|"
                        + assignment.getPriority()
                        + "|"
                        + assignment.isCompleted()
                        + "\n"
        );
    }

    // ==========================================
    // GET ALL ASSIGNMENTS
    // ==========================================

    private static List<Assignment>
    getAllAssignments() {

        List<Assignment> assignments =
                new ArrayList<>();

        File file =
                new File(FILE_NAME);

        if (!file.exists()) {
            return assignments;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split("\\|");

                if (data.length != 6) {
                    continue;
                }

                assignments.add(
                        new Assignment(
                                data[0],
                                data[1],
                                data[2],
                                data[3],
                                data[4],
                                Boolean.parseBoolean(
                                        data[5]
                                )
                        )
                );
            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return assignments;
    }
}