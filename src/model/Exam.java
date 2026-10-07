package models;

public class Exam {

    private String username;
    private String subjectName;
    private String examName;
    private String examDate;
    private boolean prepared;

    public Exam(
            String username,
            String subjectName,
            String examName,
            String examDate,
            boolean prepared
    ) {

        this.username = username;
        this.subjectName = subjectName;
        this.examName = examName;
        this.examDate = examDate;
        this.prepared = prepared;
    }

    public String getUsername() {
        return username;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getExamName() {
        return examName;
    }

    public String getExamDate() {
        return examDate;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(
            boolean prepared
    ) {

        this.prepared = prepared;
    }
}