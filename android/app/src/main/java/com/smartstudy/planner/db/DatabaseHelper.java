package com.smartstudy.planner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smartstudy.planner.models.Assignment;
import com.smartstudy.planner.models.Exam;
import com.smartstudy.planner.models.Subject;
import com.smartstudy.planner.models.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartstudy.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "username TEXT PRIMARY KEY, " +
                "password_hash TEXT, " +
                "recovery_hash TEXT)");

        db.execSQL("CREATE TABLE subjects (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "name TEXT, " +
                "difficulty TEXT, " +
                "study_goal REAL, " +
                "weekly_hours REAL, " +
                "progress INTEGER)");

        db.execSQL("CREATE TABLE assignments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "subject_name TEXT, " +
                "title TEXT, " +
                "due_date TEXT, " +
                "priority TEXT, " +
                "completed INTEGER)");

        db.execSQL("CREATE TABLE exams (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "subject_name TEXT, " +
                "name TEXT, " +
                "exam_date TEXT, " +
                "prepared INTEGER)");

        seedDemoData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS subjects");
        db.execSQL("DROP TABLE IF EXISTS assignments");
        db.execSQL("DROP TABLE IF EXISTS exams");
        onCreate(db);
    }

    private void seedDemoData(SQLiteDatabase db) {
        // Pre-seed demo users
        String defaultPass = hash("1234");
        String defaultRecovery = hash("study");

        insertUserInternal(db, "nirmal", defaultPass, defaultRecovery);
        insertUserInternal(db, "student", defaultPass, defaultRecovery);

        // Pre-seed sample subjects for nirmal
        insertSubjectInternal(db, "nirmal", "Data Structures", "Hard", 60.0, 8.0, 45);
        insertSubjectInternal(db, "nirmal", "Database Systems", "Medium", 40.0, 5.0, 70);
        insertSubjectInternal(db, "nirmal", "Computer Networks", "Easy", 30.0, 4.0, 80);

        // Pre-seed assignments
        insertAssignmentInternal(db, "nirmal", "Data Structures", "Binary Trees Lab", "15/10/2026", "High", 0);
        insertAssignmentInternal(db, "nirmal", "Database Systems", "SQL Normalization Report", "20/10/2026", "Medium", 0);
        insertAssignmentInternal(db, "nirmal", "Computer Networks", "Subnetting Quiz Prep", "12/10/2026", "Low", 1);

        // Pre-seed exams
        insertExamInternal(db, "nirmal", "Data Structures", "Midterm Exam", "18/10/2026", 0);
        insertExamInternal(db, "nirmal", "Database Systems", "Final Exam", "28/10/2026", 0);
    }

    private void insertUserInternal(SQLiteDatabase db, String username, String passHash, String recoveryHash) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password_hash", passHash);
        cv.put("recovery_hash", recoveryHash);
        db.insertWithOnConflict("users", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void insertSubjectInternal(SQLiteDatabase db, String user, String name, String diff, double goal, double weekly, int prog) {
        ContentValues cv = new ContentValues();
        cv.put("username", user);
        cv.put("name", name);
        cv.put("difficulty", diff);
        cv.put("study_goal", goal);
        cv.put("weekly_hours", weekly);
        cv.put("progress", prog);
        db.insert("subjects", null, cv);
    }

    private void insertAssignmentInternal(SQLiteDatabase db, String user, String subject, String title, String due, String priority, int completed) {
        ContentValues cv = new ContentValues();
        cv.put("username", user);
        cv.put("subject_name", subject);
        cv.put("title", title);
        cv.put("due_date", due);
        cv.put("priority", priority);
        cv.put("completed", completed);
        db.insert("assignments", null, cv);
    }

    private void insertExamInternal(SQLiteDatabase db, String user, String subject, String name, String date, int prep) {
        ContentValues cv = new ContentValues();
        cv.put("username", user);
        cv.put("subject_name", subject);
        cv.put("name", name);
        cv.put("exam_date", date);
        cv.put("prepared", prep);
        db.insert("exams", null, cv);
    }

    // ==========================================
    // AUTHENTICATION
    // ==========================================

    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            return sb.toString();
        } catch (Exception e) {
            return input;
        }
    }

    public boolean registerUser(String username, String password, String recoveryAnswer) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        username = username.trim();
        SQLiteDatabase db = getWritableDatabase();
        if (userExists(username)) return false;

        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password_hash", hash(password));
        cv.put("recovery_hash", hash(recoveryAnswer));
        long res = db.insert("users", null, cv);
        return res != -1;
    }

    public boolean loginUser(String username, String password) {
        if (username == null || password == null) return false;
        SQLiteDatabase db = getReadableDatabase();
        String hash = hash(password);
        Cursor cursor = db.rawQuery("SELECT username FROM users WHERE username = ? AND password_hash = ?",
                new String[]{username.trim(), hash});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM users WHERE username = ?", new String[]{username.trim()});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean resetPassword(String username, String recoveryAnswer, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT username FROM users WHERE username = ? AND recovery_hash = ?",
                new String[]{username.trim(), hash(recoveryAnswer)});
        if (cursor.moveToFirst()) {
            cursor.close();
            ContentValues cv = new ContentValues();
            cv.put("password_hash", hash(newPassword));
            int rows = db.update("users", cv, "username = ?", new String[]{username.trim()});
            return rows > 0;
        }
        cursor.close();
        return false;
    }

    // ==========================================
    // SUBJECTS
    // ==========================================

    public List<Subject> getSubjectsForUser(String username) {
        List<Subject> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, username, name, difficulty, study_goal, weekly_hours, progress FROM subjects WHERE username = ? ORDER BY name ASC",
                new String[]{username});
        while (cursor.moveToNext()) {
            list.add(new Subject(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getDouble(4),
                    cursor.getDouble(5),
                    cursor.getInt(6)
            ));
        }
        cursor.close();
        return list;
    }

    public boolean addSubject(Subject s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", s.getUsername());
        cv.put("name", s.getName());
        cv.put("difficulty", s.getDifficulty());
        cv.put("study_goal", s.getStudyGoal());
        cv.put("weekly_hours", s.getWeeklyHours());
        cv.put("progress", s.getProgress());
        long id = db.insert("subjects", null, cv);
        s.setId(id);
        return id != -1;
    }

    public boolean updateSubject(Subject s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", s.getName());
        cv.put("difficulty", s.getDifficulty());
        cv.put("study_goal", s.getStudyGoal());
        cv.put("weekly_hours", s.getWeeklyHours());
        cv.put("progress", s.getProgress());
        return db.update("subjects", cv, "id = ?", new String[]{String.valueOf(s.getId())}) > 0;
    }

    public boolean deleteSubject(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("subjects", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==========================================
    // ASSIGNMENTS
    // ==========================================

    public List<Assignment> getAssignmentsForUser(String username) {
        List<Assignment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, username, subject_name, title, due_date, priority, completed FROM assignments WHERE username = ? ORDER BY completed ASC, id DESC",
                new String[]{username});
        while (cursor.moveToNext()) {
            list.add(new Assignment(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getInt(6) == 1
            ));
        }
        cursor.close();
        return list;
    }

    public boolean addAssignment(Assignment a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", a.getUsername());
        cv.put("subject_name", a.getSubjectName());
        cv.put("title", a.getAssignmentName());
        cv.put("due_date", a.getDueDate());
        cv.put("priority", a.getPriority());
        cv.put("completed", a.isCompleted() ? 1 : 0);
        long id = db.insert("assignments", null, cv);
        a.setId(id);
        return id != -1;
    }

    public boolean updateAssignmentCompleted(long id, boolean completed) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("completed", completed ? 1 : 0);
        return db.update("assignments", cv, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteAssignment(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("assignments", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    // ==========================================
    // EXAMS
    // ==========================================

    public List<Exam> getExamsForUser(String username) {
        List<Exam> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, username, subject_name, name, exam_date, prepared FROM exams WHERE username = ? ORDER BY prepared ASC, id DESC",
                new String[]{username});
        while (cursor.moveToNext()) {
            list.add(new Exam(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5) == 1
            ));
        }
        cursor.close();
        return list;
    }

    public boolean addExam(Exam e) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", e.getUsername());
        cv.put("subject_name", e.getSubjectName());
        cv.put("name", e.getExamName());
        cv.put("exam_date", e.getExamDate());
        cv.put("prepared", e.isPrepared() ? 1 : 0);
        long id = db.insert("exams", null, cv);
        e.setId(id);
        return id != -1;
    }

    public boolean updateExamPrepared(long id, boolean prepared) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("prepared", prepared ? 1 : 0);
        return db.update("exams", cv, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteExam(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("exams", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }
}
