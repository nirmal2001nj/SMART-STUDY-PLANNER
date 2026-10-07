package panels;

import models.Assignment;
import models.Subject;
import utils.AssignmentManager;
import utils.CurrentUser;
import utils.SubjectManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AssignmentsPanel extends JPanel {

    private JPanel assignmentsContainer;

    public AssignmentsPanel() {

        setLayout(new BorderLayout());

        setBackground(
                new Color(245, 247, 250)
        );

        setBorder(
                new EmptyBorder(
                        30, 30, 30, 30
                )
        );

        createUI();

        loadAssignments();
    }

    // =====================================================
    // CREATE UI
    // =====================================================

    private void createUI() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(245, 247, 250)
        );

        JPanel heading =
                new JPanel();

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.setBackground(
                new Color(245, 247, 250)
        );

        JLabel title =
                new JLabel(
                        "My Assignments"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setForeground(
                new Color(31, 41, 55)
        );

        JLabel subtitle =
                new JLabel(
                        "Organize your tasks and stay on track"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setForeground(
                new Color(107, 114, 128)
        );

        heading.add(title);

        heading.add(
                Box.createVerticalStrut(5)
        );

        heading.add(subtitle);

        header.add(
                heading,
                BorderLayout.WEST
        );

        JButton addButton =
                new JButton(
                        "+ Add Assignment"
                );

        styleMainButton(addButton);

        addButton.addActionListener(
                e -> showAddAssignmentDialog()
        );

        header.add(
                addButton,
                BorderLayout.EAST
        );

        add(
                header,
                BorderLayout.NORTH
        );

        // =================================================
        // ASSIGNMENT CONTAINER
        // =================================================

        assignmentsContainer =
                new JPanel();

        assignmentsContainer.setLayout(
                new GridLayout(
                        0,
                        2,
                        20,
                        20
                )
        );

        assignmentsContainer.setBackground(
                new Color(245, 247, 250)
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        assignmentsContainer
                );

        scrollPane.setBorder(null);

        scrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }

    // =====================================================
    // LOAD ASSIGNMENTS
    // =====================================================

    private void loadAssignments() {

        assignmentsContainer.removeAll();

        String username =
                CurrentUser.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            return;
        }

        List<Assignment> assignments =
                AssignmentManager
                        .getAssignmentsForUser(
                                username
                        );

        if (assignments.isEmpty()) {

            assignmentsContainer.setLayout(
                    new GridLayout(
                            1,
                            1
                    )
            );

            assignmentsContainer.add(
                    createEmptyCard()
            );

        } else {

            assignmentsContainer.setLayout(
                    new GridLayout(
                            0,
                            2,
                            20,
                            20
                    )
            );

            for (Assignment assignment :
                    assignments) {

                assignmentsContainer.add(
                        createAssignmentCard(
                                assignment
                        )
                );
            }
        }

        assignmentsContainer.revalidate();

        assignmentsContainer.repaint();
    }

    // =====================================================
    // EMPTY CARD
    // =====================================================

    private JPanel createEmptyCard() {

        JPanel card =
                new JPanel();

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
                                40,
                                40,
                                40,
                                40
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel icon =
                new JLabel("📝");

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
                        "No assignments yet"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel text =
                new JLabel(
                        "Add your first assignment to get started."
                );

        text.setForeground(
                new Color(
                        107,
                        114,
                        128
                )
        );

        text.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        card.add(icon);

        card.add(
                Box.createVerticalStrut(10)
        );

        card.add(title);

        card.add(
                Box.createVerticalStrut(8)
        );

        card.add(text);

        return card;
    }

    // =====================================================
    // ASSIGNMENT CARD
    // =====================================================

    private JPanel createAssignmentCard(
            Assignment assignment
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
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

        // =================================================
        // TOP
        // =================================================

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(
                Color.WHITE
        );

        JLabel name =
                new JLabel(
                        "📝 "
                                + assignment
                                .getAssignmentName()
                );

        name.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        JLabel priority =
                new JLabel(
                        assignment.getPriority()
                );

        priority.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        priority.setForeground(
                getPriorityColor(
                        assignment.getPriority()
                )
        );

        top.add(
                name,
                BorderLayout.WEST
        );

        top.add(
                priority,
                BorderLayout.EAST
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        // =================================================
        // DETAILS
        // =================================================

        JPanel details =
                new JPanel();

        details.setBackground(
                Color.WHITE
        );

        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel subject =
                new JLabel(
                        "Subject: "
                                + assignment
                                .getSubjectName()
                );

        JLabel dueDate =
                new JLabel(
                        "Due Date: "
                                + assignment
                                .getDueDate()
                );

        // =================================================
        // REMAINING DAYS
        // =================================================

        JLabel remaining =
                new JLabel(
                        getRemainingText(
                                assignment
                                        .getDueDate()
                        )
                );

        remaining.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        // =================================================
        // STATUS
        // =================================================

        JLabel status =
                new JLabel(
                        assignment.isCompleted()
                                ? "🟢 Completed"
                                : "🔴 Pending"
                );

        status.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        details.add(subject);

        details.add(
                Box.createVerticalStrut(6)
        );

        details.add(dueDate);

        details.add(
                Box.createVerticalStrut(8)
        );

        details.add(remaining);

        details.add(
                Box.createVerticalStrut(8)
        );

        details.add(status);

        card.add(
                details,
                BorderLayout.CENTER
        );

        // =================================================
        // BUTTONS
        // =================================================

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                5,
                                0
                        )
                );

        buttons.setBackground(
                Color.WHITE
        );

        JButton completeButton =
                new JButton(
                        assignment.isCompleted()
                                ? "Mark Pending"
                                : "Mark Completed"
                );

        JButton deleteButton =
                new JButton(
                        "Delete"
                );

        styleSmallButton(
                completeButton
        );

        styleSmallButton(
                deleteButton
        );

        buttons.add(
                completeButton
        );

        buttons.add(
                deleteButton
        );

        card.add(
                buttons,
                BorderLayout.SOUTH
        );

        // =================================================
        // COMPLETE BUTTON
        // =================================================

        completeButton.addActionListener(
                e -> {

                    int result =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    assignment.isCompleted()
                                            ? "Mark this assignment as pending?"
                                            : "Mark this assignment as completed?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION
                            );

                    if (result ==
                            JOptionPane.YES_OPTION) {

                        AssignmentManager
                                .toggleCompletion(
                                        CurrentUser
                                                .getUsername(),
                                        assignment
                                                .getAssignmentName()
                                );

                        loadAssignments();
                    }
                }
        );

        // =================================================
        // DELETE BUTTON
        // =================================================

        deleteButton.addActionListener(
                e -> {

                    int result =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "Delete \""
                                            + assignment
                                            .getAssignmentName()
                                            + "\"?",
                                    "Confirm Delete",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE
                            );

                    if (result !=
                            JOptionPane.YES_OPTION) {

                        return;
                    }

                    AssignmentManager
                            .deleteAssignment(
                                    CurrentUser
                                            .getUsername(),
                                    assignment
                                            .getAssignmentName()
                            );

                    loadAssignments();

                    JOptionPane.showMessageDialog(
                            this,
                            "Assignment deleted successfully.",
                            "Deleted",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
        );

        return card;
    }

    // =====================================================
    // REMAINING DAYS
    // =====================================================

    private String getRemainingText(
            String assignmentDate
    ) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        try {

            LocalDate dueDate =
                    LocalDate.parse(
                            assignmentDate,
                            formatter
                    );

            LocalDate today =
                    LocalDate.now();

            long days =
                    ChronoUnit.DAYS.between(
                            today,
                            dueDate
                    );

            if (days > 1) {

                return "⏳ "
                        + days
                        + " days remaining";

            } else if (days == 1) {

                return "⏳ 1 day remaining";

            } else if (days == 0) {

                return "🔔 Assignment is due today!";

            } else {

                return "⚠ Assignment date has passed";
            }

        } catch (
                DateTimeParseException e
        ) {

            return "Date unavailable";
        }
    }

    // =====================================================
    // ADD ASSIGNMENT
    // =====================================================

    private void showAddAssignmentDialog() {

        List<Subject> subjects =
                SubjectManager
                        .getSubjectsForUser(
                                CurrentUser.getUsername()
                        );

        JComboBox<String> subjectBox =
                new JComboBox<>();

        for (Subject subject :
                subjects) {

            subjectBox.addItem(
                    subject.getName()
            );
        }

        JTextField newSubjectField =
                new JTextField();

        JTextField assignmentNameField =
                new JTextField();

        JTextField dueDateField =
                new JTextField();

        JComboBox<String> priorityBox =
                new JComboBox<>(
                        new String[]{
                                "Low",
                                "Medium",
                                "High"
                        }
                );

        while (true) {

            JPanel panel =
                    new JPanel(
                            new GridLayout(
                                    0,
                                    1,
                                    8,
                                    8
                            )
                    );

            panel.setBorder(
                    new EmptyBorder(
                            15,
                            15,
                            15,
                            15
                    )
            );

            panel.add(
                    new JLabel(
                            "Choose Existing Subject"
                    )
            );

            panel.add(
                    subjectBox
            );

            panel.add(
                    new JLabel(
                            "OR Enter New Subject"
                    )
            );

            panel.add(
                    newSubjectField
            );

            panel.add(
                    new JLabel(
                            "Assignment Name"
                    )
            );

            panel.add(
                    assignmentNameField
            );

            panel.add(
                    new JLabel(
                            "Due Date (DD/MM/YYYY)"
                    )
            );

            panel.add(
                    dueDateField
            );

            panel.add(
                    new JLabel(
                            "Priority"
                    )
            );

            panel.add(
                    priorityBox
            );

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Add New Assignment",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result !=
                    JOptionPane.OK_OPTION) {

                return;
            }

            String selectedSubject =
                    subjectBox
                            .getSelectedItem() == null
                            ? ""
                            : subjectBox
                            .getSelectedItem()
                            .toString();

            String newSubject =
                    newSubjectField
                            .getText()
                            .trim();

            String assignmentName =
                    assignmentNameField
                            .getText()
                            .trim();

            String dueDate =
                    dueDateField
                            .getText()
                            .trim();

            String priority =
                    priorityBox
                            .getSelectedItem()
                            .toString();

            boolean newSubjectEntered =
                    !newSubject.isEmpty();

            boolean existingSubjectSelected =
                    !selectedSubject.isEmpty();

            // =================================================
            // SUBJECT CHECK
            // =================================================

            if (existingSubjectSelected &&
                    newSubjectEntered) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please use either an existing subject\n"
                                +
                                "OR enter a new subject.",
                        "Choose One",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            if (!existingSubjectSelected &&
                    !newSubjectEntered) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select an existing subject\n"
                                +
                                "or enter a new subject name.",
                        "Subject Required",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            String subjectName;

            if (newSubjectEntered) {

                subjectName =
                        newSubject;

            } else {

                subjectName =
                        selectedSubject;
            }

            // =================================================
            // OTHER FIELD CHECK
            // =================================================

            if (assignmentName.isEmpty()
                    || dueDate.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            // =================================================
            // DATE VALIDATION
            // =================================================

            if (!isValidDate(dueDate)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid date.\n"
                                +
                                "Example: 15/08/2026",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            // =================================================
            // CONFIRM
            // =================================================

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Add \""
                                    + assignmentName
                                    + "\"?",
                            "Confirm Assignment",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (confirm !=
                    JOptionPane.YES_OPTION) {

                continue;
            }

            // =================================================
            // CREATE
            // =================================================

            Assignment assignment =
                    new Assignment(
                            CurrentUser.getUsername(),
                            subjectName,
                            assignmentName,
                            dueDate,
                            priority,
                            false
                    );

            // =================================================
            // SAVE
            // =================================================

            AssignmentManager
                    .addAssignment(
                            assignment
                    );

            // =================================================
            // REFRESH FIRST
            // =================================================

            loadAssignments();

            // =================================================
            // SUCCESS AFTER REFRESH
            // =================================================

            JOptionPane.showMessageDialog(
                    this,
                    "Assignment added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            break;
        }
    }

    // =====================================================
    // DATE VALIDATION
    // =====================================================

    private boolean isValidDate(
            String date
    ) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        try {

            LocalDate.parse(
                    date,
                    formatter
            );

            return true;

        } catch (
                DateTimeParseException e
        ) {

            return false;
        }
    }

    // =====================================================
    // PRIORITY COLOR
    // =====================================================

    private Color getPriorityColor(
            String priority
    ) {

        if (priority.equals("High")) {

            return new Color(
                    185,
                    28,
                    28
            );
        }

        if (priority.equals("Medium")) {

            return new Color(
                    180,
                    83,
                    9
            );
        }

        return new Color(
                21,
                128,
                61
        );
    }

    // =====================================================
    // MAIN BUTTON
    // =====================================================

    private void styleMainButton(
            JButton button
    ) {

        button.setPreferredSize(
                new Dimension(
                        170,
                        42
                )
        );

        button.setBackground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =====================================================
    // SMALL BUTTON
    // =====================================================

    private void styleSmallButton(
            JButton button
    ) {

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }
}