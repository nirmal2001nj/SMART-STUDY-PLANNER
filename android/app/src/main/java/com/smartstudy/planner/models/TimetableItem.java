package com.smartstudy.planner.models;

import java.util.Date;

public class TimetableItem implements Comparable<TimetableItem> {
    private String title;
    private String subject;
    private String dateStr;
    private String type; // "Assignment" or "Exam"
    private String priority;
    private Date parsedDate;

    public TimetableItem(String title, String subject, String dateStr, String type, String priority, Date parsedDate) {
        this.title = title;
        this.subject = subject;
        this.dateStr = dateStr;
        this.type = type;
        this.priority = priority;
        this.parsedDate = parsedDate;
    }

    public String getTitle() { return title; }
    public String getSubject() { return subject; }
    public String getDateStr() { return dateStr; }
    public String getType() { return type; }
    public String getPriority() { return priority; }
    public Date getParsedDate() { return parsedDate; }

    @Override
    public int compareTo(TimetableItem o) {
        if (this.parsedDate == null && o.parsedDate == null) return 0;
        if (this.parsedDate == null) return 1;
        if (o.parsedDate == null) return -1;
        return this.parsedDate.compareTo(o.parsedDate);
    }
}
