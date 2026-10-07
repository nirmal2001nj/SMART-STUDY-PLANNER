package panels;

import models.Subject;
import utils.CurrentUser;
import utils.SubjectManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SubjectsPanel extends JPanel {

    private JPanel subjectsContainer;

    public SubjectsPanel() {

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

        loadSubjects();
    }

    private void createUI() {

        // ==========================================
        // HEADER
        // ==========================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(245, 247, 250)
        );

        JLabel title =
                new JLabel(
                        "My Subjects"
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
                        "Manage your subjects and track your study goals"
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
                        "+ Add Subject"
                );

        styleMainButton(addButton);

        addButton.addActionListener(
                e -> showAddSubjectDialog()
        );

        header.add(
                addButton,
                BorderLayout.EAST
        );

        add(
                header,
                BorderLayout.NORTH
        );

        // ==========================================
        // SUBJECT CONTAINER
        // ==========================================

        subjectsContainer =
                new JPanel();

        subjectsContainer.setLayout(
                new GridLayout(
                        0,
                        2,
                        20,
                        20
                )
        );

        subjectsContainer.setBackground(
                new Color(245, 247, 250)
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        subjectsContainer
                );

        scrollPane.setBorder(null);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }

    // ==========================================
    // LOAD SUBJECTS
    // ==========================================

    private void loadSubjects() {

        subjectsContainer.removeAll();

        String username =
                CurrentUser.getUsername();

        if (username == null) {
            return;
        }

        List<Subject> subjects =
                SubjectManager
                        .getSubjectsForUser(
                                username
                        );

        if (subjects.isEmpty()) {

            JPanel emptyCard =
                    createEmptyCard();

            subjectsContainer.setLayout(
                    new GridLayout(
                            1,
                            1
                    )
            );

            subjectsContainer.add(
                    emptyCard
            );

        } else {

            subjectsContainer.setLayout(
                    new GridLayout(
                            0,
                            2,
                            20,
                            20
                    )
            );

            for (Subject subject :
                    subjects) {

                subjectsContainer.add(
                        createSubjectCard(
                                subject
                        )
                );
            }
        }

        subjectsContainer.revalidate();

        subjectsContainer.repaint();
    }

    // ==========================================
    // EMPTY CARD
    // ==========================================

    private JPanel createEmptyCard() {

        JPanel card =
                new JPanel();

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 228, 233)
                        ),
                        new EmptyBorder(
                                40, 40, 40, 40
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
                        "No subjects added yet"
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
                        "Click + Add Subject to begin."
                );

        text.setForeground(
                new Color(107, 114, 128)
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

    // ==========================================
    // SUBJECT CARD
    // ==========================================

    private JPanel createSubjectCard(
            Subject subject
    ) {

        JPanel card =
                new JPanel();

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 228, 233)
                        ),
                        new EmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        card.setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        // ==========================================
        // TOP
        // ==========================================

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(Color.WHITE);

        JLabel name =
                new JLabel(
                        "📚 " + subject.getName()
                );

        name.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        JLabel difficulty =
                new JLabel(
                        subject.getDifficulty()
                );

        difficulty.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        difficulty.setForeground(
                getDifficultyColor(
                        subject.getDifficulty()
                )
        );

        top.add(
                name,
                BorderLayout.WEST
        );

        top.add(
                difficulty,
                BorderLayout.EAST
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        // ==========================================
        // DETAILS
        // ==========================================

        JPanel details =
                new JPanel();

        details.setBackground(Color.WHITE);

        details.setLayout(
                new BoxLayout(
                        details,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel goal =
                new JLabel(
                        "Study Goal: "
                                +
                                subject.getStudyGoal()
                                +
                                "%"
                );

        JLabel hours =
                new JLabel(
                        "Weekly Study Hours: "
                                +
                                subject.getWeeklyHours()
                );

        JLabel progressText =
                new JLabel(
                        "Progress: "
                                +
                                subject.getProgress()
                                +
                                "%"
                );

        details.add(goal);

        details.add(
                Box.createVerticalStrut(6)
        );

        details.add(hours);

        details.add(
                Box.createVerticalStrut(12)
        );

        details.add(progressText);

        JProgressBar progressBar =
                new JProgressBar(
                        0,
                        100
                );

        progressBar.setValue(
                subject.getProgress()
        );

        progressBar.setStringPainted(true);

        progressBar.setPreferredSize(
                new Dimension(
                        300,
                        20
                )
        );

        details.add(
                Box.createVerticalStrut(5)
        );

        details.add(progressBar);

        JLabel status =
                new JLabel(
                        subject.getProgress() >= 100
                                ? "🟢 Completed"
                                : "🔴 Incomplete"
                );

        status.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        details.add(
                Box.createVerticalStrut(8)
        );

        details.add(status);

        card.add(
                details,
                BorderLayout.CENTER
        );

        // ==========================================
        // BUTTONS
        // ==========================================

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                5,
                                0
                        )
                );

        buttons.setBackground(Color.WHITE);

        JButton editButton =
                new JButton("Edit");

        JButton deleteButton =
                new JButton("Delete");

        styleSmallButton(editButton);

        styleSmallButton(deleteButton);

        buttons.add(editButton);

        buttons.add(deleteButton);

        card.add(
                buttons,
                BorderLayout.SOUTH
        );

        // ==========================================
        // DELETE
        // ==========================================

        deleteButton.addActionListener(e -> {

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Delete \""
                                    + subject.getName()
                                    + "\"?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (result != JOptionPane.YES_OPTION) {
                return;
            }

            // Delete first
            SubjectManager.deleteSubject(
                    CurrentUser.getUsername(),
                    subject.getName()
            );

            // Refresh screen first
            loadSubjects();

            // Then show success
            JOptionPane.showMessageDialog(
                    this,
                    "Subject deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        // ==========================================
        // EDIT
        // ==========================================

        editButton.addActionListener(e -> {

            showEditSubjectDialog(
                    subject
            );
        });

        return card;
    }

    // ==========================================
    // ADD SUBJECT
    // ==========================================

    private void showAddSubjectDialog() {

        JTextField nameField =
                new JTextField();

        JComboBox<String> difficultyBox =
                new JComboBox<>(
                        new String[]{
                                "Easy",
                                "Medium",
                                "Hard"
                        }
                );

        JTextField goalField =
                new JTextField();

        JTextField hoursField =
                new JTextField();

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
                    new JLabel("Subject Name")
            );

            panel.add(nameField);

            panel.add(
                    new JLabel("Difficulty")
            );

            panel.add(difficultyBox);

            panel.add(
                    new JLabel("Study Goal (%)")
            );

            panel.add(goalField);

            panel.add(
                    new JLabel("Weekly Study Hours")
            );

            panel.add(hoursField);

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Add New Subject",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            // User clicked Cancel
            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            String name =
                    nameField
                            .getText()
                            .trim();

            String goalText =
                    goalField
                            .getText()
                            .trim();

            String hoursText =
                    hoursField
                            .getText()
                            .trim();

            // ==========================================
            // VALIDATE EMPTY FIELDS
            // ==========================================

            if (name.isEmpty()
                    || goalText.isEmpty()
                    || hoursText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                // IMPORTANT:
                // Continue keeps the user in the form
                continue;
            }

            // ==========================================
            // VALIDATE NUMBERS
            // ==========================================

            double goal;
            double hours;

            try {

                goal =
                        Double.parseDouble(
                                goalText
                        );

                hours =
                        Double.parseDouble(
                                hoursText
                        );

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid numbers.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            // ==========================================
            // VALIDATE STUDY GOAL
            // ==========================================

            if (goal < 0 || goal > 100) {

                JOptionPane.showMessageDialog(
                        this,
                        "Study Goal must be between 0 and 100.",
                        "Invalid Study Goal",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            // ==========================================
            // VALIDATE HOURS
            // ==========================================

            if (hours < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Weekly Study Hours cannot be negative.",
                        "Invalid Hours",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            String difficulty =
                    difficultyBox
                            .getSelectedItem()
                            .toString();

            // ==========================================
            // CONFIRM
            // ==========================================

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Add \""
                                    + name
                                    + "\" to your subjects?",
                            "Confirm Subject",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (confirm != JOptionPane.YES_OPTION) {

                continue;
            }

            // ==========================================
            // SAVE
            // ==========================================

            Subject subject =
                    new Subject(
                            CurrentUser.getUsername(),
                            name,
                            difficulty,
                            goal,
                            hours,
                            0
                    );

            SubjectManager.addSubject(
                    subject
            );

            // ==========================================
            // FIRST SHOW THE NEW CARD
            // ==========================================

            loadSubjects();

            // ==========================================
            // THEN SHOW SUCCESS
            // ==========================================

            JOptionPane.showMessageDialog(
                    this,
                    "Subject added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            break;
        }
    }
    // ==========================================
    // EDIT SUBJECT
    // ==========================================

    private void showEditSubjectDialog(
            Subject subject
    ) {

        JTextField nameField =
                new JTextField(
                        subject.getName()
                );

        JComboBox<String> difficultyBox =
                new JComboBox<>(
                        new String[]{
                                "Easy",
                                "Medium",
                                "Hard"
                        }
                );

        difficultyBox.setSelectedItem(
                subject.getDifficulty()
        );

        JTextField goalField =
                new JTextField(
                        String.valueOf(
                                subject.getStudyGoal()
                        )
                );

        JTextField hoursField =
                new JTextField(
                        String.valueOf(
                                subject.getWeeklyHours()
                        )
                );

        JPanel panel =
                createFormPanel(
                        nameField,
                        difficultyBox,
                        goalField,
                        hoursField
                );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Edit Subject",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result !=
                JOptionPane.OK_OPTION) {

            return;
        }

        try {

            String name =
                    nameField.getText().trim();

            String difficulty =
                    difficultyBox
                            .getSelectedItem()
                            .toString();

            double goal =
                    Double.parseDouble(
                            goalField.getText()
                                    .trim()
                    );

            double hours =
                    Double.parseDouble(
                            hoursField.getText()
                                    .trim()
                    );

            if (name.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Subject name cannot be empty.",
                        "Invalid",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (goal < 0 || goal > 100) {

                JOptionPane.showMessageDialog(
                        this,
                        "Study Goal must be between 0 and 100.",
                        "Invalid",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (hours < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Weekly Study Hours cannot be negative.",
                        "Invalid",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            Subject updatedSubject =
                    new Subject(
                            CurrentUser.getUsername(),
                            name,
                            difficulty,
                            goal,
                            hours,
                            subject.getProgress()
                    );

            SubjectManager.updateSubject(
                    updatedSubject
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Subject updated successfully.",
                    "Updated",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadSubjects();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // ==========================================
    // FORM
    // ==========================================

    private JPanel createFormPanel(
            JTextField nameField,
            JComboBox<String> difficultyBox,
            JTextField goalField,
            JTextField hoursField
    ) {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                0,
                                1,
                                6,
                                6
                        )
                );

        panel.setBorder(
                new EmptyBorder(
                        10, 10, 10, 10
                )
        );

        panel.add(
                new JLabel(
                        "Subject Name"
                )
        );

        panel.add(nameField);

        panel.add(
                new JLabel(
                        "Difficulty"
                )
        );

        panel.add(difficultyBox);

        panel.add(
                new JLabel(
                        "Study Goal (%)"
                )
        );

        panel.add(goalField);

        panel.add(
                new JLabel(
                        "Weekly Study Hours"
                )
        );

        panel.add(hoursField);

        return panel;
    }

    // ==========================================
    // DIFFICULTY COLOR
    // ==========================================

    private Color getDifficultyColor(
            String difficulty
    ) {

        if (difficulty.equals("Hard")) {

            return new Color(
                    185, 28, 28
            );
        }

        if (difficulty.equals("Medium")) {

            return new Color(
                    180, 83, 9
            );
        }

        return new Color(
                21, 128, 61
        );
    }

    // ==========================================
    // MAIN BUTTON
    // ==========================================

    private void styleMainButton(
            JButton button
    ) {

        button.setPreferredSize(
                new Dimension(
                        150,
                        42
                )
        );

        button.setBackground(
                new Color(
                        31, 41, 55
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

        button.setContentAreaFilled(true);

        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // ==========================================
    // SMALL BUTTON
    // ==========================================

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