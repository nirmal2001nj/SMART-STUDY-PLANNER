package panels;

import models.Assignment;
import models.Exam;
import models.Subject;
import utils.AssignmentManager;
import utils.CurrentUser;
import utils.ExamManager;
import utils.SubjectManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardPanel extends JPanel {

    private final Color BACKGROUND = new Color(245, 247, 250);
    private final Color DARK = new Color(31, 43, 58);
    private final Color TEXT = new Color(70, 80, 95);
    private final Color SUBTEXT = new Color(100, 110, 125);
    private final Color BORDER = new Color(220, 225, 232);

    private JLabel welcomeLabel;
    private JLabel subjectCountLabel;
    private JLabel assignmentCountLabel;
    private JLabel examCountLabel;
    private JLabel progressLabel;
    private JLabel attentionLabel;

    private JProgressBar progressBar;

    private JPanel upcomingPanel;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public DashboardPanel() {

        setLayout(new BorderLayout());
        setBackground(BACKGROUND);

        createUI();
        loadDashboard();
    }

    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.setBackground(BACKGROUND);

        mainPanel.setBorder(
                new EmptyBorder(
                        30,
                        40,
                        30,
                        40
                )
        );

        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(BACKGROUND);

        JPanel titlePanel = new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setOpaque(false);

        welcomeLabel = new JLabel("Hi");

        welcomeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        38
                )
        );

        welcomeLabel.setForeground(DARK);

        JLabel subtitle = new JLabel(
                "Here is your study overview"
        );

        subtitle.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        20
                )
        );

        subtitle.setForeground(SUBTEXT);

        titlePanel.add(welcomeLabel);

        titlePanel.add(
                Box.createVerticalStrut(6)
        );

        titlePanel.add(subtitle);

        JButton refreshButton =
                new JButton("↻ Refresh");

        refreshButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        17
                )
        );

        refreshButton.setForeground(Color.WHITE);

        refreshButton.setBackground(DARK);

        refreshButton.setFocusPainted(false);

        refreshButton.setBorderPainted(false);

        refreshButton.setPreferredSize(
                new Dimension(
                        145,
                        55
                )
        );

        refreshButton.addActionListener(
                e -> loadDashboard()
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                refreshButton,
                BorderLayout.EAST
        );

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        // =====================================================
        // CONTENT
        // =====================================================

        JPanel content = new JPanel();

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        content.setBackground(BACKGROUND);

        // =====================================================
        // STATISTICS
        // =====================================================

        JPanel statistics =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                18,
                                0
                        )
                );

        statistics.setBackground(BACKGROUND);

        statistics.add(
                createStatCard(
                        "📚",
                        "Subjects",
                        "0",
                        0
                )
        );

        statistics.add(
                createStatCard(
                        "📝",
                        "Pending Assignments",
                        "0",
                        1
                )
        );

        statistics.add(
                createStatCard(
                        "📅",
                        "Upcoming Exams",
                        "0",
                        2
                )
        );

        content.add(statistics);

        content.add(
                Box.createVerticalStrut(22)
        );

        // =====================================================
        // PROGRESS
        // =====================================================

        content.add(
                createProgressCard()
        );

        content.add(
                Box.createVerticalStrut(22)
        );

        // =====================================================
        // ATTENTION
        // =====================================================

        content.add(
                createAttentionCard()
        );

        content.add(
                Box.createVerticalStrut(22)
        );

        // =====================================================
        // UPCOMING
        // =====================================================

        JLabel upcomingTitle =
                new JLabel("Upcoming");

        upcomingTitle.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        26
                )
        );

        upcomingTitle.setForeground(DARK);

        content.add(upcomingTitle);

        content.add(
                Box.createVerticalStrut(12)
        );

        upcomingPanel = new JPanel();

        upcomingPanel.setLayout(
                new BoxLayout(
                        upcomingPanel,
                        BoxLayout.Y_AXIS
                )
        );

        upcomingPanel.setBackground(BACKGROUND);

        content.add(upcomingPanel);

        JScrollPane scrollPane =
                new JScrollPane(content);

        scrollPane.setBorder(null);

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        mainPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                mainPanel,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel createStatCard(
            String icon,
            String title,
            String value,
            int type
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
                                22,
                                22,
                                22,
                                22
                        )
                )
        );

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        30
                )
        );

        iconLabel.setPreferredSize(
                new Dimension(
                        50,
                        50
                )
        );

        JPanel text = new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(false);

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        titleLabel.setForeground(SUBTEXT);

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        28
                )
        );

        valueLabel.setForeground(DARK);

        if (type == 0) {

            subjectCountLabel = valueLabel;

        } else if (type == 1) {

            assignmentCountLabel = valueLabel;

        } else {

            examCountLabel = valueLabel;
        }

        text.add(titleLabel);

        text.add(
                Box.createVerticalStrut(5)
        );

        text.add(valueLabel);

        card.add(
                iconLabel,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // PROGRESS CARD
    // =========================================================

    private JPanel createProgressCard() {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                25,
                                28,
                                25,
                                28
                        )
                )
        );

        JLabel title =
                new JLabel(
                        "📊 Overall Progress"
                );

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        23
                )
        );

        title.setForeground(DARK);

        progressLabel =
                new JLabel("0%");

        progressLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        progressLabel.setForeground(DARK);

        progressBar =
                new JProgressBar(
                        0,
                        100
                );

        progressBar.setValue(0);

        progressBar.setStringPainted(true);

        progressBar.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        28
                )
        );

        JLabel details =
                new JLabel(
                        "Complete assignments and prepare exams to increase your progress."
                );

        details.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        16
                )
        );

        details.setForeground(SUBTEXT);

        card.add(title);

        card.add(
                Box.createVerticalStrut(8)
        );

        card.add(progressLabel);

        card.add(
                Box.createVerticalStrut(12)
        );

        card.add(progressBar);

        card.add(
                Box.createVerticalStrut(10)
        );

        card.add(details);

        return card;
    }

    // =========================================================
    // ATTENTION CARD
    // =========================================================

    private JPanel createAttentionCard() {

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

        JLabel icon =
                new JLabel("⚠️");

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        28
                )
        );

        attentionLabel =
                new JLabel(
                        "Everything is under control."
                );

        attentionLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        18
                )
        );

        attentionLabel.setForeground(DARK);

        card.add(
                icon,
                BorderLayout.WEST
        );

        card.add(
                attentionLabel,
                BorderLayout.CENTER
        );

        return card;
    }

    // =========================================================
    // LOAD DASHBOARD
    // =========================================================

    public void loadDashboard() {

        String username =
                CurrentUser.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            welcomeLabel.setText("Hi");

            subjectCountLabel.setText("0");

            assignmentCountLabel.setText("0");

            examCountLabel.setText("0");

            progressLabel.setText("0%");

            progressBar.setValue(0);

            loadUpcoming(
                    null,
                    null
            );

            return;
        }

        welcomeLabel.setText(
                "Hi " + username
        );

        // =====================================================
        // GET SUBJECTS
        // =====================================================

        List<Subject> subjects =
                SubjectManager.getSubjectsForUser(
                        username
                );

        // =====================================================
        // GET ASSIGNMENTS
        // =====================================================

        List<Assignment> assignments =
                AssignmentManager.getAssignmentsForUser(
                        username
                );

        int pendingAssignments = 0;

        int completedAssignments = 0;

        for (Assignment assignment : assignments) {

            if (assignment.isCompleted()) {

                completedAssignments++;

            } else {

                pendingAssignments++;
            }
        }

        // =====================================================
        // GET EXAMS
        // =====================================================

        List<Exam> exams =
                ExamManager.getExamsForUser(
                        username
                );

        int pendingExams = 0;

        int preparedExams = 0;

        for (Exam exam : exams) {

            if (exam.isPrepared()) {

                preparedExams++;

            } else {

                pendingExams++;
            }
        }

        // =====================================================
        // PROGRESS
        // =====================================================

        int total =
                assignments.size()
                        +
                        exams.size();

        int completed =
                completedAssignments
                        +
                        preparedExams;

        int percentage = 0;

        if (total > 0) {

            percentage =
                    (completed * 100) / total;
        }

        // =====================================================
        // UPDATE DASHBOARD
        // =====================================================

        subjectCountLabel.setText(
                String.valueOf(
                        subjects.size()
                )
        );

        assignmentCountLabel.setText(
                String.valueOf(
                        pendingAssignments
                )
        );

        examCountLabel.setText(
                String.valueOf(
                        pendingExams
                )
        );

        progressLabel.setText(
                percentage + "%"
        );

        progressBar.setValue(
                percentage
        );

        progressBar.setString(
                percentage + "%"
        );

        // =====================================================
        // ATTENTION
        // =====================================================

        int attention =
                pendingAssignments
                        +
                        pendingExams;

        if (attention == 0) {

            attentionLabel.setText(
                    "🎉 Everything is under control!"
            );

        } else if (attention == 1) {

            attentionLabel.setText(
                    "⚠️ 1 item needs your attention."
            );

        } else {

            attentionLabel.setText(
                    "⚠️ " +
                            attention +
                            " items need your attention."
            );
        }

        // =====================================================
        // UPCOMING
        // =====================================================

        loadUpcoming(
                assignments,
                exams
        );

        revalidate();

        repaint();
    }

    // =========================================================
    // UPCOMING
    // =========================================================

    private void loadUpcoming(
            List<Assignment> assignments,
            List<Exam> exams
    ) {

        upcomingPanel.removeAll();

        int count = 0;

        // =====================================================
        // ASSIGNMENTS
        // =====================================================

        if (assignments != null) {

            for (Assignment assignment :
                    assignments) {

                if (assignment.isCompleted()) {

                    continue;
                }

                LocalDate date =
                        parseDate(
                                assignment.getDueDate()
                        );

                if (date == null) {

                    continue;
                }

                upcomingPanel.add(
                        createUpcomingCard(
                                "📝",
                                assignment.getAssignmentName(),
                                assignment.getSubjectName(),
                                assignment.getDueDate(),
                                "Assignment"
                        )
                );

                upcomingPanel.add(
                        Box.createVerticalStrut(10)
                );

                count++;

                if (count >= 5) {

                    break;
                }
            }
        }

        // =====================================================
        // EXAMS
        // =====================================================

        if (exams != null && count < 5) {

            for (Exam exam : exams) {

                if (exam.isPrepared()) {

                    continue;
                }

                LocalDate date =
                        parseDate(
                                exam.getExamDate()
                        );

                if (date == null) {

                    continue;
                }

                upcomingPanel.add(
                        createUpcomingCard(
                                "📅",
                                exam.getExamName(),
                                exam.getSubjectName(),
                                exam.getExamDate(),
                                "Exam"
                        )
                );

                upcomingPanel.add(
                        Box.createVerticalStrut(10)
                );

                count++;

                if (count >= 5) {

                    break;
                }
            }
        }

        // =====================================================
        // EMPTY
        // =====================================================

        if (count == 0) {

            JLabel empty =
                    new JLabel(
                            "No upcoming tasks. You are all caught up!"
                    );

            empty.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            18
                    )
            );

            empty.setForeground(SUBTEXT);

            upcomingPanel.add(empty);
        }

        upcomingPanel.revalidate();

        upcomingPanel.repaint();
    }

    // =========================================================
    // UPCOMING CARD
    // =========================================================

    private JPanel createUpcomingCard(
            String iconText,
            String name,
            String subject,
            String date,
            String type
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
                                18,
                                22,
                                18,
                                22
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        100
                )
        );

        JLabel icon =
                new JLabel(iconText);

        icon.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        28
                )
        );

        icon.setPreferredSize(
                new Dimension(
                        50,
                        50
                )
        );

        JPanel text = new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(false);

        JLabel nameLabel =
                new JLabel(name);

        nameLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        19
                )
        );

        nameLabel.setForeground(DARK);

        JLabel subjectLabel =
                new JLabel(
                        subject +
                                "  •  " +
                                type
                );

        subjectLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        15
                )
        );

        subjectLabel.setForeground(SUBTEXT);

        text.add(nameLabel);

        text.add(
                Box.createVerticalStrut(5)
        );

        text.add(subjectLabel);

        JLabel dateLabel =
                new JLabel(date);

        dateLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        dateLabel.setForeground(DARK);

        card.add(
                icon,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );

        card.add(
                dateLabel,
                BorderLayout.EAST
        );

        return card;
    }

    // =========================================================
    // DATE
    // =========================================================

    private LocalDate parseDate(
            String date
    ) {

        try {

            return LocalDate.parse(
                    date,
                    formatter
            );

        } catch (Exception e) {

            return null;
        }
    }
}