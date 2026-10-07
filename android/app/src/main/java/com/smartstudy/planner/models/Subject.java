package com.smartstudy.planner.models;

public class Subject {
    private long id;
    private String username;
    private String name;
    private String difficulty;
    private double studyGoal;
    private double weeklyHours;
    private int progress;

    public Subject(long id, String username, String name, String difficulty, double studyGoal, double weeklyHours, int progress) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.difficulty = difficulty;
        this.studyGoal = studyGoal;
        this.weeklyHours = weeklyHours;
        this.progress = progress;
    }

    public Subject(String username, String name, String difficulty, double studyGoal, double weeklyHours, int progress) {
        this(-1, username, name, difficulty, studyGoal, weeklyHours, progress);
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getDifficulty() { return difficulty; }
    public double getStudyGoal() { return studyGoal; }
    public double getWeeklyHours() { return weeklyHours; }
    public int getProgress() { return progress; }

    public void setName(String name) { this.name = name; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setStudyGoal(double studyGoal) { this.studyGoal = studyGoal; }
    public void setWeeklyHours(double weeklyHours) { this.weeklyHours = weeklyHours; }
    public void setProgress(int progress) { this.progress = progress; }
}
