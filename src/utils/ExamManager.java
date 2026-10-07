package utils;

import models.Exam;

import java.util.ArrayList;
import java.util.List;

public class ExamManager {

    private static final List<Exam> exams =
            new ArrayList<>();

    // ==========================================
    // ADD EXAM
    // ==========================================

    public static void addExam(Exam exam) {

        exams.add(exam);
    }

    // ==========================================
    // GET ALL EXAMS FOR A USER
    // ==========================================

    public static List<Exam> getExamsForUser(
            String username
    ) {

        List<Exam> result =
                new ArrayList<>();

        for (Exam exam : exams) {

            if (exam.getUsername()
                    .equals(username)) {

                result.add(exam);
            }
        }

        return result;
    }

    // ==========================================
    // DELETE ONE SPECIFIC EXAM
    // ==========================================

    public static void deleteExam(
            String username,
            String subjectName,
            String examName
    ) {

        exams.removeIf(
                exam ->
                        exam.getUsername()
                                .equals(username)
                                &&
                                exam.getSubjectName()
                                        .equals(subjectName)
                                &&
                                exam.getExamName()
                                        .equals(examName)
        );
    }

    // ==========================================
    // TOGGLE PREPARED STATUS
    // ==========================================

    public static void togglePrepared(
            String username,
            String subjectName,
            String examName
    ) {

        for (Exam exam : exams) {

            if (exam.getUsername()
                    .equals(username)
                    &&
                    exam.getSubjectName()
                            .equals(subjectName)
                    &&
                    exam.getExamName()
                            .equals(examName)) {

                exam.setPrepared(
                        !exam.isPrepared()
                );

                break;
            }
        }
    }

    // ==========================================
    // DELETE ALL EXAMS OF ONE USER
    // ==========================================

    public static boolean deleteAllExamsForUser(
            String username
    ) {

        try {

            exams.removeIf(
                    exam ->
                            exam.getUsername()
                                    .equals(username)
            );

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}