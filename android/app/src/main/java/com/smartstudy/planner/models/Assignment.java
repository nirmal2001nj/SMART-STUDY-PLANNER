package com.smartstudy.planner.models;

public class Assignment {
    private long id;
    private String username;
    private String subjectName;
    private String assignmentName;
    private String dueDate;
    private String priority;
    private boolean completed;

    public Assignment(long id, String username, String subjectName, String assignmentName, String dueDate, String priority, boolean completed) {
        this.id = id;
        this.username = username;
        this.subjectName = subjectName;
        this.assignmentName = assignmentName;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    public Assignment(String username, String subjectName, String assignmentName, String dueDate, String priority, boolean completed) {
        this(-1, username, subjectName, assignmentName, dueDate, priority, completed);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }
    public String getSubjectName() { return subjectName; }
    public String getAssignmentName() { return assignmentName; }
    public String getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public void setAssignmentName(String assignmentName) { this.assignmentName = assignmentName; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
