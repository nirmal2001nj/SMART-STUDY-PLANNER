package panels;

import models.Assignment;
import models.Exam;
import utils.AssignmentManager;
import utils.CurrentUser;
import utils.ExamManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimetablePanel extends JPanel {

    private JPanel listPanel;

    private final Color BACKGROUND =
            new Color(245, 247, 250);

    private final Color DARK =
            new Color(31, 41, 55);

    private final Color TEXT =
            new Color(75, 85, 99);

    private final Color BORDER =
            new Color(220, 224, 230);

    public TimetablePanel() {

        setLayout(new BorderLayout());

        setBackground(BACKGROUND);

        createUI();

        loadTimetable();
    }

    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(BACKGROUND);

        mainPanel.setBorder(
                new EmptyBorder(
                        28,
                        28,
                        28,
                        28
                )
        );

        // =================================================
        // HEADER
        // =================================================

        JPanel header =
                new JPanel(new BorderLayout());

        header.setBackground(BACKGROUND);

        JPanel titlePanel =
                new JPanel();

        titlePanel.setBackground(BACKGROUND);

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel("My Timetable");

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        title.setForeground(DARK);

        JLabel subtitle =
                new JLabel(
                        "Your upcoming study tasks arranged by urgency"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        subtitle.setForeground(
                new Color(
                        107,
                        114,
                        128
                )
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        // =================================================
        // REFRESH BUTTON
        // =================================================

        JButton refreshButton =
                new JButton("↻ Refresh");

        refreshButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        refreshButton.setForeground(Color.WHITE);

        refreshButton.setBackground(DARK);

        refreshButton.setFocusPainted(false);

        refreshButton.setBorder(
                new EmptyBorder(
                        15,
                        25,
                        15,
                        25
                )
        );

        refreshButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        refreshButton.addActionListener(
                e -> loadTimetable()
        );

        header.add(
                refreshButton,
                BorderLayout.EAST
        );

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // =================================================
        // LIST
        // =================================================

        listPanel =
                new JPanel();

        listPanel.setBackground(BACKGROUND);

        listPanel.setLayout(
                new BoxLayout(
                        listPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(listPanel);

        scrollPane.setBorder(null);

        scrollPane.setBackground(BACKGROUND);

        scrollPane.getViewport().setBackground(
                BACKGROUND
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                mainPanel,
                BorderLayout.CENTER
        );
    }

    // =====================================================
    // LOAD TIMETABLE
    // =====================================================

    private void loadTimetable() {

        listPanel.removeAll();

        String username =
                CurrentUser.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            showEmptyMessage();

            return;
        }

        List<TimetableItem> items =
                new ArrayList<>();

        // =================================================
        // ASSIGNMENTS
        // =================================================

        List<Assignment> assignments =
                AssignmentManager.getAssignmentsForUser(
                        username
                );

        for (Assignment assignment :
                assignments) {

            if (!assignment.isCompleted()) {

                Date date =
                        parseDate(
                                assignment.getDueDate()
                        );

                if (date != null) {

                    items.add(
                            new TimetableItem(
                                    assignment.getAssignmentName(),
                                    assignment.getSubjectName(),
                                    assignment.getDueDate(),
                                    "Assignment",
                                    assignment.getPriority(),
                                    date
                            )
                    );
                }
            }
        }

        // =================================================
        // EXAMS
        // =================================================

        List<Exam> exams =
                ExamManager.getExamsForUser(
                        username
                );

        for (Exam exam :
                exams) {

            if (!exam.isPrepared()) {

                Date date =
                        parseDate(
                                exam.getExamDate()
                        );

                if (date != null) {

                    items.add(
                            new TimetableItem(
                                    exam.getExamName(),
                                    exam.getSubjectName(),
                                    exam.getExamDate(),
                                    "Exam",
                                    "High",
                                    date
                            )
                    );
                }
            }
        }

        // =================================================
        // SORT
        // =================================================

        items.sort(
                (a, b) ->
                        a.date.compareTo(
                                b.date
                        )
        );

        // =================================================
        // NO DATA
        // =================================================

        if (items.isEmpty()) {

            showEmptyMessage();

            return;
        }

        // =================================================
        // SUMMARY
        // =================================================

        JPanel summaryCard =
                createSummaryCard(
                        items.size()
                );

        listPanel.add(summaryCard);

        listPanel.add(
                Box.createVerticalStrut(18)
        );

        // =================================================
        // TASK CARDS
        // =================================================

        for (TimetableItem item :
                items) {

            JPanel card =
                    createTaskCard(item);

            listPanel.add(card);

            listPanel.add(
                    Box.createVerticalStrut(15)
            );
        }

        listPanel.revalidate();

        listPanel.repaint();
    }

    // =====================================================
    // SUMMARY CARD
    // =====================================================

    private JPanel createSummaryCard(
            int count
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        110
                )
        );

        JLabel brain =
                new JLabel("🧠");

        brain.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        28
                )
        );

        brain.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        brain.setPreferredSize(
                new Dimension(
                        55,
                        55
                )
        );

        JPanel textPanel =
                new JPanel();

        textPanel.setBackground(Color.WHITE);

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Smart Study Plan"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        21
                )
        );

        title.setForeground(DARK);

        JLabel pending =
                new JLabel(
                        count +
                                " pending item(s) need your attention"
                );

        pending.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        pending.setForeground(TEXT);

        textPanel.add(title);

        textPanel.add(
                Box.createVerticalStrut(8)
        );

        textPanel.add(pending);

        card.add(
                brain,
                BorderLayout.WEST
        );

        card.add(
                textPanel,
                BorderLayout.CENTER
        );

        return card;
    }

    // =====================================================
    // TASK CARD
    // =====================================================

    private JPanel createTaskCard(
            TimetableItem item
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                20,
                                0
                        )
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        140
                )
        );

        // =================================================
        // ICON
        // =================================================

        JLabel icon =
                new JLabel(
                        item.type.equals("Exam")
                                ? "📅"
                                : "📝"
                );

        icon.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        30
                )
        );

        icon.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        icon.setVerticalAlignment(
                SwingConstants.CENTER
        );

        icon.setPreferredSize(
                new Dimension(
                        55,
                        70
                )
        );

        card.add(
                icon,
                BorderLayout.WEST
        );

        // =================================================
        // CENTER INFORMATION
        // =================================================

        JPanel information =
                new JPanel();

        information.setBackground(Color.WHITE);

        information.setLayout(
                new BoxLayout(
                        information,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel name =
                new JLabel(
                        item.name
                );

        name.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        name.setForeground(DARK);

        JLabel subject =
                new JLabel(
                        "Subject: " +
                                item.subject
                );

        subject.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subject.setForeground(TEXT);

        JLabel date =
                new JLabel(
                        "Date: " +
                                item.dateText
                );

        date.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        date.setForeground(TEXT);

        information.add(name);

        information.add(
                Box.createVerticalStrut(8)
        );

        information.add(subject);

        information.add(
                Box.createVerticalStrut(5)
        );

        information.add(date);

        card.add(
                information,
                BorderLayout.CENTER
        );

        // =================================================
        // RIGHT INFORMATION
        // =================================================

        JPanel right =
                new JPanel();

        right.setBackground(Color.WHITE);

        right.setLayout(
                new BoxLayout(
                        right,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel type =
                new JLabel(
                        item.type
                );

        type.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        type.setForeground(DARK);

        JLabel remaining =
                new JLabel(
                        "⌛ " +
                                getRemainingText(
                                        item.date
                                )
                );

        remaining.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        remaining.setForeground(DARK);

        JLabel priority =
                new JLabel(
                        "Priority: " +
                                item.priority
                );

        priority.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        priority.setForeground(TEXT);

        type.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        remaining.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        priority.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        right.add(type);

        right.add(
                Box.createVerticalStrut(7)
        );

        right.add(remaining);

        right.add(
                Box.createVerticalStrut(5)
        );

        right.add(priority);

        card.add(
                right,
                BorderLayout.EAST
        );

        return card;
    }

    // =====================================================
    // REMAINING DAYS
    // =====================================================

    private String getRemainingText(
            Date targetDate
    ) {

        Calendar today =
                Calendar.getInstance();

        Calendar target =
                Calendar.getInstance();

        today.setTime(
                new Date()
        );

        target.setTime(
                targetDate
        );

        today.set(
                Calendar.HOUR_OF_DAY,
                0
        );

        today.set(
                Calendar.MINUTE,
                0
        );

        today.set(
                Calendar.SECOND,
                0
        );

        today.set(
                Calendar.MILLISECOND,
                0
        );

        target.set(
                Calendar.HOUR_OF_DAY,
                0
        );

        target.set(
                Calendar.MINUTE,
                0
        );

        target.set(
                Calendar.SECOND,
                0
        );

        target.set(
                Calendar.MILLISECOND,
                0
        );

        long difference =
                target.getTimeInMillis()
                        -
                        today.getTimeInMillis();

        long days =
                difference /
                        (
                                1000L
                                        * 60L
                                        * 60L
                                        * 24L
                        );

        if (days < 0) {

            return "Overdue";
        }

        if (days == 0) {

            return "Today";
        }

        if (days == 1) {

            return "1 day remaining";
        }

        return days +
                " days remaining";
    }

    // =====================================================
    // DATE PARSER
    // =====================================================

    private Date parseDate(
            String dateString
    ) {

        if (dateString == null ||
                dateString.trim().isEmpty()) {

            return null;
        }

        String[] formats = {

                "dd/MM/yyyy",

                "dd-MM-yyyy",

                "yyyy-MM-dd",

                "dd/MM/yyyy HH:mm",

                "dd-MM-yyyy HH:mm"
        };

        for (String format :
                formats) {

            try {

                SimpleDateFormat sdf =
                        new SimpleDateFormat(
                                format
                        );

                sdf.setLenient(false);

                return sdf.parse(
                        dateString
                );

            } catch (Exception ignored) {

            }
        }

        return null;
    }

    // =====================================================
    // EMPTY MESSAGE
    // =====================================================

    private void showEmptyMessage() {

        listPanel.removeAll();

        JPanel emptyPanel =
                new JPanel();

        emptyPanel.setBackground(
                Color.WHITE
        );

        emptyPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                50,
                                20,
                                50,
                                20
                        )
                )
        );

        emptyPanel.setLayout(
                new BoxLayout(
                        emptyPanel,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel("📚");

        icon.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        40
                )
        );

        icon.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel title =
                new JLabel(
                        "No upcoming tasks"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        title.setForeground(DARK);

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel message =
                new JLabel(
                        "Add assignments or exams to see them here."
                );

        message.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        message.setForeground(TEXT);

        message.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        emptyPanel.add(icon);

        emptyPanel.add(
                Box.createVerticalStrut(15)
        );

        emptyPanel.add(title);

        emptyPanel.add(
                Box.createVerticalStrut(8)
        );

        emptyPanel.add(message);

        listPanel.add(
                emptyPanel
        );

        listPanel.revalidate();

        listPanel.repaint();
    }

    // =====================================================
    // DATA CLASS
    // =====================================================

    private static class TimetableItem {

        String name;

        String subject;

        String dateText;

        String type;

        String priority;

        Date date;

        TimetableItem(
                String name,
                String subject,
                String dateText,
                String type,
                String priority,
                Date date
        ) {

            this.name = name;

            this.subject = subject;

            this.dateText = dateText;

            this.type = type;

            this.priority = priority;

            this.date = date;
        }
    }
}