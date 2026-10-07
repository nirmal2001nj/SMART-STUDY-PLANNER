package utils;

import models.Subject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SubjectManager {

    private static final String FILE_NAME =
            "subjects.txt";

    public static void addSubject(
            Subject subject
    ) {

        try {

            FileWriter writer =
                    new FileWriter(
                            FILE_NAME,
                            true
                    );

            writer.write(
                    subject.getUsername()
                            + "|"
                            + subject.getName()
                            + "|"
                            + subject.getDifficulty()
                            + "|"
                            + subject.getStudyGoal()
                            + "|"
                            + subject.getWeeklyHours()
                            + "|"
                            + subject.getProgress()
                            + "\n"
            );

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static List<Subject> getSubjectsForUser(
            String username
    ) {

        List<Subject> subjects =
                new ArrayList<>();

        File file =
                new File(FILE_NAME);

        if (!file.exists()) {
            return subjects;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line = reader.readLine())
                    != null) {

                String[] data =
                        line.split("\\|");

                if (data.length != 6) {
                    continue;
                }

                if (!data[0].equals(username)) {
                    continue;
                }

                Subject subject =
                        new Subject(
                                data[0],
                                data[1],
                                data[2],
                                Double.parseDouble(data[3]),
                                Double.parseDouble(data[4]),
                                Integer.parseInt(data[5])
                        );

                subjects.add(subject);
            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return subjects;
    }

    public static void deleteSubject(
            String username,
            String subjectName
    ) {

        List<Subject> allSubjects =
                getAllSubjects();

        try {

            FileWriter writer =
                    new FileWriter(FILE_NAME);

            for (Subject subject :
                    allSubjects) {

                if (subject.getUsername()
                        .equals(username)
                        &&
                        subject.getName()
                                .equals(subjectName)) {

                    continue;
                }

                writeSubject(writer, subject);
            }

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void updateSubject(
            Subject updatedSubject
    ) {

        List<Subject> allSubjects =
                getAllSubjects();

        try {

            FileWriter writer =
                    new FileWriter(FILE_NAME);

            for (Subject subject :
                    allSubjects) {

                if (subject.getUsername()
                        .equals(
                                updatedSubject.getUsername()
                        )
                        &&
                        subject.getName()
                                .equals(
                                        updatedSubject.getName()
                                )) {

                    subject = updatedSubject;
                }

                writeSubject(writer, subject);
            }

            writer.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // ==========================================
    // DELETE ALL SUBJECTS OF ONE USER
    // ==========================================

    public static boolean deleteAllSubjectsForUser(
            String username
    ) {

        List<Subject> allSubjects =
                getAllSubjects();

        File file =
                new File(FILE_NAME);

        if (!file.exists()) {
            return true;
        }

        try {

            File tempFile =
                    new File("subjects_temp.txt");

            FileWriter writer =
                    new FileWriter(tempFile);

            for (Subject subject :
                    allSubjects) {

                if (subject.getUsername()
                        .equals(username)) {

                    continue;
                }

                writeSubject(writer, subject);
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

    private static void writeSubject(
            FileWriter writer,
            Subject subject
    ) throws IOException {

        writer.write(
                subject.getUsername()
                        + "|"
                        + subject.getName()
                        + "|"
                        + subject.getDifficulty()
                        + "|"
                        + subject.getStudyGoal()
                        + "|"
                        + subject.getWeeklyHours()
                        + "|"
                        + subject.getProgress()
                        + "\n"
        );
    }

    private static List<Subject> getAllSubjects() {

        List<Subject> subjects =
                new ArrayList<>();

        File file =
                new File(FILE_NAME);

        if (!file.exists()) {
            return subjects;
        }

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new FileReader(file)
                    );

            String line;

            while ((line = reader.readLine())
                    != null) {

                String[] data =
                        line.split("\\|");

                if (data.length != 6) {
                    continue;
                }

                subjects.add(
                        new Subject(
                                data[0],
                                data[1],
                                data[2],
                                Double.parseDouble(data[3]),
                                Double.parseDouble(data[4]),
                                Integer.parseInt(data[5])
                        )
                );
            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return subjects;
    }
}