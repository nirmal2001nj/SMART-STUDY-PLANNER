package com.smartstudy.planner.models;

public class Exam {
    private long id;
    private String username;
    private String subjectName;
    private String examName;
    private String examDate;
    private boolean prepared;

    public Exam(long id, String username, String subjectName, String examName, String examDate, boolean prepared) {
        this.id = id;
        this.username = username;
        this.subjectName = subjectName;
        this.examName = examName;
        this.examDate = examDate;
        this.prepared = prepared;
    }

    public Exam(String username, String subjectName, String examName, String examDate, boolean prepared) {
        this(-1, username, subjectName, examName, examDate, prepared);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }
    public String getSubjectName() { return subjectName; }
    public String getExamName() { return examName; }
    public String getExamDate() { return examDate; }
    public boolean isPrepared() { return prepared; }

    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public void setExamName(String examName) { this.examName = examName; }
    public void setExamDate(String examDate) { this.examDate = examDate; }
    public void setPrepared(boolean prepared) { this.prepared = prepared; }
}
