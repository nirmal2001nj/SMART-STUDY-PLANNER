package models;

public class Assignment {

    private String username;
    private String subjectName;
    private String assignmentName;
    private String dueDate;
    private String priority;
    private boolean completed;

    public Assignment(
            String username,
            String subjectName,
            String assignmentName,
            String dueDate,
            String priority,
            boolean completed
    ) {

        this.username = username;
        this.subjectName = subjectName;
        this.assignmentName = assignmentName;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    public String getUsername() {
        return username;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setAssignmentName(
            String assignmentName
    ) {
        this.assignmentName = assignmentName;
    }

    public void setDueDate(
            String dueDate
    ) {
        this.dueDate = dueDate;
    }

    public void setPriority(
            String priority
    ) {
        this.priority = priority;
    }

    public void setCompleted(
            boolean completed
    ) {
        this.completed = completed;
    }
}