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
import java.util.List;

public class ProgressPanel extends JPanel {

    private JLabel overallProgressLabel;
    private JProgressBar overallProgressBar;

    private JLabel totalSubjectsLabel;

    private JLabel totalAssignmentsLabel;
    private JLabel completedAssignmentsLabel;
    private JLabel pendingAssignmentsLabel;

    private JLabel totalExamsLabel;
    private JLabel preparedExamsLabel;
    private JLabel notPreparedExamsLabel;

    public ProgressPanel() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        setBorder(
                new EmptyBorder(
                        30,
                        30,
                        30,
                        30
                )
        );

        createUI();

        refreshProgress();
    }

    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel();

        mainPanel.setLayout(
                new BoxLayout(
                        mainPanel,
                        BoxLayout.Y_AXIS
                )
        );

        mainPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        // =================================================
        // HEADER
        // =================================================

        JPanel heading =
                new JPanel();

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        heading.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        heading.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        80
                )
        );

        JLabel title =
                new JLabel(
                        "My Progress"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        // IMPORTANT: CENTER TITLE
        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Track your study progress automatically"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                new Color(
                        107,
                        114,
                        128
                )
        );

        // IMPORTANT: CENTER SUBTITLE
        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        heading.add(
                title
        );

        heading.add(
                Box.createVerticalStrut(
                        5
                )
        );

        heading.add(
                subtitle
        );

        mainPanel.add(
                heading
        );

        mainPanel.add(
                Box.createVerticalStrut(
                        25
                )
        );

        // =================================================
        // OVERALL PROGRESS CARD
        // =================================================

        JPanel progressCard =
                new JPanel();

        progressCard.setLayout(
                new BoxLayout(
                        progressCard,
                        BoxLayout.Y_AXIS
                )
        );

        progressCard.setBackground(
                Color.WHITE
        );

        progressCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        228,
                                        233
                                )
                        ),
                        new EmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        progressCard.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        180
                )
        );

        JLabel progressTitle =
                new JLabel(
                        "Overall Progress"
                );

        progressTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        progressTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        overallProgressLabel =
                new JLabel(
                        "0%"
                );

        overallProgressLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        36
                )
        );

        overallProgressLabel.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        overallProgressLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        overallProgressBar =
                new JProgressBar(
                        0,
                        100
                );

        overallProgressBar.setValue(
                0
        );

        overallProgressBar.setStringPainted(
                true
        );

        overallProgressBar.setPreferredSize(
                new Dimension(
                        500,
                        25
                )
        );

        overallProgressBar.setMaximumSize(
                new Dimension(
                        700,
                        25
                )
        );

        overallProgressBar.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        progressCard.add(
                progressTitle
        );

        progressCard.add(
                Box.createVerticalStrut(
                        12
                )
        );

        progressCard.add(
                overallProgressLabel
        );

        progressCard.add(
                Box.createVerticalStrut(
                        15
                )
        );

        progressCard.add(
                overallProgressBar
        );

        mainPanel.add(
                progressCard
        );

        mainPanel.add(
                Box.createVerticalStrut(
                        25
                )
        );

        // =================================================
        // STATISTICS PANEL
        // =================================================

        JPanel statisticsPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                20,
                                0
                        )
                );

        statisticsPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        statisticsPanel.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        500
                )
        );

        // =================================================
        // SUBJECT CARD
        // =================================================

        JPanel subjectCard =
                createStatisticsCard(
                        "📚",
                        "Subjects"
                );

        totalSubjectsLabel =
                createValueLabel();

        subjectCard.add(
                totalSubjectsLabel
        );

        statisticsPanel.add(
                subjectCard
        );

        // =================================================
        // ASSIGNMENT CARD
        // =================================================

        JPanel assignmentCard =
                createStatisticsCard(
                        "📝",
                        "Assignments"
                );

        totalAssignmentsLabel =
                createValueLabel();

        completedAssignmentsLabel =
                createSmallLabel();

        pendingAssignmentsLabel =
                createSmallLabel();

        assignmentCard.add(
                totalAssignmentsLabel
        );

        assignmentCard.add(
                Box.createVerticalStrut(
                        8
                )
        );

        assignmentCard.add(
                completedAssignmentsLabel
        );

        assignmentCard.add(
                pendingAssignmentsLabel
        );

        statisticsPanel.add(
                assignmentCard
        );

        // =================================================
        // EXAM CARD
        // =================================================

        JPanel examCard =
                createStatisticsCard(
                        "📅",
                        "Exams"
                );

        totalExamsLabel =
                createValueLabel();

        preparedExamsLabel =
                createSmallLabel();

        notPreparedExamsLabel =
                createSmallLabel();

        examCard.add(
                totalExamsLabel
        );

        examCard.add(
                Box.createVerticalStrut(
                        8
                )
        );

        examCard.add(
                preparedExamsLabel
        );

        examCard.add(
                notPreparedExamsLabel
        );

        statisticsPanel.add(
                examCard
        );

        mainPanel.add(
                statisticsPanel
        );

        mainPanel.add(
                Box.createVerticalStrut(
                        25
                )
        );

        // =================================================
        // REFRESH BUTTON
        // =================================================

        JButton refreshButton =
                new JButton(
                        "↻ Refresh Progress"
                );

        refreshButton.setPreferredSize(
                new Dimension(
                        180,
                        40
                )
        );

        refreshButton.setMaximumSize(
                new Dimension(
                        180,
                        40
                )
        );

        refreshButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        refreshButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        refreshButton.setFocusPainted(
                false
        );

        refreshButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        refreshButton.addActionListener(
                e -> refreshProgress()
        );

        mainPanel.add(
                refreshButton
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        mainPanel
                );

        scrollPane.setBorder(
                null
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }

    // =====================================================
    // CREATE STATISTICS CARD
    // =====================================================

    private JPanel createStatisticsCard(
            String icon,
            String title
    ) {

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        225,
                                        228,
                                        233
                                )
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        JLabel iconLabel =
                new JLabel(
                        icon
                );

        iconLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        28
                )
        );

        iconLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel titleLabel =
                new JLabel(
                        title
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(
                iconLabel
        );

        card.add(
                Box.createVerticalStrut(
                        8
                )
        );

        card.add(
                titleLabel
        );

        card.add(
                Box.createVerticalStrut(
                        15
                )
        );

        return card;
    }

    // =====================================================
    // VALUE LABEL
    // =====================================================

    private JLabel createValueLabel() {

        JLabel label =
                new JLabel(
                        "0"
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        label.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return label;
    }

    // =====================================================
    // SMALL LABEL
    // =====================================================

    private JLabel createSmallLabel() {

        JLabel label =
                new JLabel(
                        ""
                );

        label.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        label.setForeground(
                new Color(
                        75,
                        85,
                        99
                )
        );

        label.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        return label;
    }

    // =====================================================
    // CALCULATE PROGRESS
    // =====================================================

    public void refreshProgress() {

        String username =
                CurrentUser.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            return;
        }

        // =================================================
        // SUBJECTS
        // =================================================

        List<Subject> subjects =
                SubjectManager
                        .getSubjectsForUser(
                                username
                        );

        int totalSubjects =
                subjects.size();

        totalSubjectsLabel.setText(
                String.valueOf(
                        totalSubjects
                )
        );

        // =================================================
        // ASSIGNMENTS
        // =================================================

        List<Assignment> assignments =
                AssignmentManager
                        .getAssignmentsForUser(
                                username
                        );

        int totalAssignments =
                assignments.size();

        int completedAssignments = 0;

        for (Assignment assignment :
                assignments) {

            if (assignment.isCompleted()) {

                completedAssignments++;
            }
        }

        int pendingAssignments =
                totalAssignments
                        - completedAssignments;

        totalAssignmentsLabel.setText(
                "Total: "
                        + totalAssignments
        );

        completedAssignmentsLabel.setText(
                "✓ Completed: "
                        + completedAssignments
        );

        pendingAssignmentsLabel.setText(
                "○ Pending: "
                        + pendingAssignments
        );

        // =================================================
        // EXAMS
        // =================================================

        List<Exam> exams =
                ExamManager
                        .getExamsForUser(
                                username
                        );

        int totalExams =
                exams.size();

        int preparedExams = 0;

        for (Exam exam :
                exams) {

            if (exam.isPrepared()) {

                preparedExams++;
            }
        }

        int notPreparedExams =
                totalExams
                        - preparedExams;

        totalExamsLabel.setText(
                "Total: "
                        + totalExams
        );

        preparedExamsLabel.setText(
                "✓ Prepared: "
                        + preparedExams
        );

        notPreparedExamsLabel.setText(
                "○ Not Prepared: "
                        + notPreparedExams
        );

        // =================================================
        // OVERALL PROGRESS
        // =================================================

        double assignmentProgress = 0;

        if (totalAssignments > 0) {

            assignmentProgress =
                    (
                            (double)
                                    completedAssignments
                                    /
                                    totalAssignments
                    ) * 100;
        }

        double examProgress = 0;

        if (totalExams > 0) {

            examProgress =
                    (
                            (double)
                                    preparedExams
                                    /
                                    totalExams
                    ) * 100;
        }

        double overallProgress;

        if (totalAssignments == 0 &&
                totalExams == 0) {

            overallProgress = 0;

        } else if (totalAssignments == 0) {

            overallProgress =
                    examProgress;

        } else if (totalExams == 0) {

            overallProgress =
                    assignmentProgress;

        } else {

            overallProgress =
                    (
                            assignmentProgress
                                    +
                                    examProgress
                    ) / 2;
        }

        int progress =
                (int) Math.round(
                        overallProgress
                );

        overallProgressLabel.setText(
                progress + "%"
        );

        overallProgressBar.setValue(
                progress
        );

        overallProgressBar.setString(
                progress + "%"
        );
    }
}