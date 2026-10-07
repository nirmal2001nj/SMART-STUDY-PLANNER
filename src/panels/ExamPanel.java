package panels;

import models.Exam;
import models.Subject;
import utils.CurrentUser;
import utils.ExamManager;
import utils.SubjectManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ExamPanel extends JPanel {

    private JPanel examsContainer;

    public ExamPanel() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                new Color(
                        245, 247, 250
                )
        );

        setBorder(
                new EmptyBorder(
                        30, 30, 30, 30
                )
        );

        createUI();

        loadExams();
    }

    // =====================================================
    // CREATE USER INTERFACE
    // =====================================================

    private void createUI() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(
                        245, 247, 250
                )
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
                new Color(
                        245, 247, 250
                )
        );

        JLabel title =
                new JLabel(
                        "My Exams"
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
                        31, 41, 55
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Keep track of your upcoming exams"
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
                        107, 114, 128
                )
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
                        "+ Add Exam"
                );

        styleMainButton(
                addButton
        );

        addButton.addActionListener(
                e -> showAddExamDialog()
        );

        header.add(
                addButton,
                BorderLayout.EAST
        );

        add(
                header,
                BorderLayout.NORTH
        );

        examsContainer =
                new JPanel();

        examsContainer.setLayout(
                new GridLayout(
                        0,
                        2,
                        20,
                        20
                )
        );

        examsContainer.setBackground(
                new Color(
                        245, 247, 250
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        examsContainer
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
    // LOAD EXAMS
    // =====================================================

    private void loadExams() {

        examsContainer.removeAll();

        String username =
                CurrentUser.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            return;
        }

        List<Exam> exams =
                ExamManager
                        .getExamsForUser(
                                username
                        );

        if (exams.isEmpty()) {

            examsContainer.setLayout(
                    new GridLayout(
                            1,
                            1
                    )
            );

            examsContainer.add(
                    createEmptyCard()
            );

        } else {

            examsContainer.setLayout(
                    new GridLayout(
                            0,
                            2,
                            20,
                            20
                    )
            );

            for (Exam exam : exams) {

                examsContainer.add(
                        createExamCard(
                                exam
                        )
                );
            }
        }

        examsContainer.revalidate();

        examsContainer.repaint();
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
                                        225, 228, 233
                                )
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
                new JLabel("📅");

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
                        "No exams yet"
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

        JLabel message =
                new JLabel(
                        "Add your first exam to start planning."
                );

        message.setForeground(
                new Color(
                        107, 114, 128
                )
        );

        message.setAlignmentX(
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

        card.add(message);

        return card;
    }

    // =====================================================
    // EXAM CARD
    // =====================================================

    private JPanel createExamCard(
            Exam exam
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
                                        225, 228, 233
                                )
                        ),
                        new EmptyBorder(
                                20, 20, 20, 20
                        )
                )
        );

        // -------------------------------------------------
        // TOP
        // -------------------------------------------------

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(
                Color.WHITE
        );

        JLabel name =
                new JLabel(
                        "📅 "
                                + exam.getExamName()
                );

        name.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        JLabel status =
                new JLabel();

        status.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        if (exam.isPrepared()) {

            status.setText(
                    "✓ Prepared"
            );

            status.setForeground(
                    new Color(
                            21, 128, 61
                    )
            );

        } else {

            status.setText(
                    "○ Not Prepared"
            );

            status.setForeground(
                    new Color(
                            185, 28, 28
                    )
            );
        }

        top.add(
                name,
                BorderLayout.WEST
        );

        top.add(
                status,
                BorderLayout.EAST
        );

        card.add(
                top,
                BorderLayout.NORTH
        );

        // -------------------------------------------------
        // DETAILS
        // -------------------------------------------------

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
                                + exam.getSubjectName()
                );

        JLabel date =
                new JLabel(
                        "Exam Date: "
                                + exam.getExamDate()
                );

        JLabel remaining =
                new JLabel(
                        getRemainingText(
                                exam.getExamDate()
                        )
                );

        remaining.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        details.add(subject);

        details.add(
                Box.createVerticalStrut(7)
        );

        details.add(date);

        details.add(
                Box.createVerticalStrut(10)
        );

        details.add(remaining);

        card.add(
                details,
                BorderLayout.CENTER
        );

        // -------------------------------------------------
        // BUTTONS
        // -------------------------------------------------

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

        JButton prepareButton =
                new JButton(
                        exam.isPrepared()
                                ? "Mark Not Prepared"
                                : "Mark Prepared"
                );

        JButton deleteButton =
                new JButton(
                        "Delete"
                );

        styleSmallButton(
                prepareButton
        );

        styleSmallButton(
                deleteButton
        );

        buttons.add(
                prepareButton
        );

        buttons.add(
                deleteButton
        );

        card.add(
                buttons,
                BorderLayout.SOUTH
        );

        // =================================================
        // MARK PREPARED
        // =================================================

        prepareButton.addActionListener(
                e -> {

                    boolean oldStatus =
                            exam.isPrepared();

                    int result =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    oldStatus
                                            ? "Mark this exam as not prepared?"
                                            : "Mark this exam as prepared?",
                                    "Confirm",
                                    JOptionPane.YES_NO_OPTION
                            );

                    if (result !=
                            JOptionPane.YES_OPTION) {

                        return;
                    }

                    /*
                     * IMPORTANT:
                     *
                     * We pass:
                     * username
                     * subject name
                     * exam name
                     *
                     * Therefore:
                     *
                     * Java - IA 1
                     *
                     * will not affect:
                     *
                     * DBMS - IA 1
                     */

                    ExamManager.togglePrepared(
                            CurrentUser.getUsername(),
                            exam.getSubjectName(),
                            exam.getExamName()
                    );

                    loadExams();

                    JOptionPane.showMessageDialog(
                            this,
                            oldStatus
                                    ? "Exam marked as not prepared."
                                    : "Exam marked as prepared.",
                            "Updated",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
        );

        // =================================================
        // DELETE
        // =================================================

        deleteButton.addActionListener(
                e -> {

                    int result =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "Delete \""
                                            + exam.getExamName()
                                            + "\" for subject \""
                                            + exam.getSubjectName()
                                            + "\"?",
                                    "Confirm Delete",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE
                            );

                    if (result !=
                            JOptionPane.YES_OPTION) {

                        return;
                    }

                    /*
                     * IMPORTANT:
                     *
                     * We pass:
                     * username
                     * subject name
                     * exam name
                     *
                     * Only this exact exam will be deleted.
                     */

                    ExamManager.deleteExam(
                            CurrentUser.getUsername(),
                            exam.getSubjectName(),
                            exam.getExamName()
                    );

                    loadExams();

                    JOptionPane.showMessageDialog(
                            this,
                            "Exam deleted successfully.",
                            "Deleted",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
        );

        return card;
    }

    // =====================================================
    // ADD EXAM
    // =====================================================

    private void showAddExamDialog() {

        String username =
                CurrentUser.getUsername();

        List<Subject> subjects =
                SubjectManager
                        .getSubjectsForUser(
                                username
                        );

        // -------------------------------------------------
        // EXISTING SUBJECT DROPDOWN
        // -------------------------------------------------

        JComboBox<String> subjectBox =
                new JComboBox<>();

        subjectBox.addItem(
                "-- Select Existing Subject --"
        );

        for (Subject subject : subjects) {

            subjectBox.addItem(
                    subject.getName()
            );
        }

        // -------------------------------------------------
        // NEW SUBJECT FIELD
        // -------------------------------------------------

        JTextField newSubjectField =
                new JTextField();

        // -------------------------------------------------
        // EXAM NAME
        // -------------------------------------------------

        JTextField examNameField =
                new JTextField();

        // -------------------------------------------------
        // EXAM DATE
        // -------------------------------------------------

        JTextField examDateField =
                new JTextField();

        // -------------------------------------------------
        // KEEP FORM OPEN WHEN VALIDATION FAILS
        // -------------------------------------------------

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
                            "Select Existing Subject"
                    )
            );

            panel.add(
                    subjectBox
            );

            panel.add(
                    new JLabel(
                            "OR Enter New Subject Name"
                    )
            );

            panel.add(
                    newSubjectField
            );

            panel.add(
                    new JLabel(
                            "Exam Name"
                    )
            );

            panel.add(
                    examNameField
            );

            panel.add(
                    new JLabel(
                            "Exam Date (DD/MM/YYYY)"
                    )
            );

            panel.add(
                    examDateField
            );

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "Add New Exam",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result !=
                    JOptionPane.OK_OPTION) {

                return;
            }

            // -------------------------------------------------
            // GET VALUES
            // -------------------------------------------------

            String selectedSubject =
                    String.valueOf(
                            subjectBox
                                    .getSelectedItem()
                    );

            String newSubject =
                    newSubjectField
                            .getText()
                            .trim();

            String examName =
                    examNameField
                            .getText()
                            .trim();

            String examDate =
                    examDateField
                            .getText()
                            .trim();

            boolean existingSelected =
                    !selectedSubject.equals(
                            "-- Select Existing Subject --"
                    );

            boolean newSubjectEntered =
                    !newSubject.isEmpty();

            // -------------------------------------------------
            // SUBJECT REQUIRED
            // -------------------------------------------------

            if (!existingSelected &&
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

            // -------------------------------------------------
            // BOTH SUBJECT OPTIONS USED
            // -------------------------------------------------

            if (existingSelected &&
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

            // -------------------------------------------------
            // SELECT SUBJECT NAME
            // -------------------------------------------------

            String subjectName;

            if (newSubjectEntered) {

                subjectName =
                        newSubject;

            } else {

                subjectName =
                        selectedSubject;
            }

            // -------------------------------------------------
            // CHECK OTHER FIELDS
            // -------------------------------------------------

            if (examName.isEmpty()
                    || examDate.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            // -------------------------------------------------
            // CHECK DATE
            // -------------------------------------------------

            if (!isValidDate(
                    examDate
            )) {

                JOptionPane.showMessageDialog(
                        this,
                        "Enter a valid date.\n"
                                +
                                "Example: 25/08/2026",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            // -------------------------------------------------
            // CONFIRM ADD
            // -------------------------------------------------

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Add this exam?\n\n"
                                    +
                                    "Subject: "
                                    + subjectName
                                    + "\n"
                                    +
                                    "Exam: "
                                    + examName
                                    + "\n"
                                    +
                                    "Date: "
                                    + examDate,
                            "Confirm Exam",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

            if (confirm !=
                    JOptionPane.YES_OPTION) {

                continue;
            }

            // -------------------------------------------------
            // CREATE EXAM
            // -------------------------------------------------

            Exam exam =
                    new Exam(
                            username,
                            subjectName,
                            examName,
                            examDate,
                            false
                    );

            // -------------------------------------------------
            // SAVE EXAM
            // -------------------------------------------------

            ExamManager.addExam(
                    exam
            );

            // -------------------------------------------------
            // REFRESH SCREEN
            // -------------------------------------------------

            loadExams();

            // -------------------------------------------------
            // SUCCESS MESSAGE
            // -------------------------------------------------

            JOptionPane.showMessageDialog(
                    this,
                    "Exam added successfully!",
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
    // REMAINING DAYS
    // =====================================================

    private String getRemainingText(
            String examDate
    ) {

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                );

        try {

            LocalDate exam =
                    LocalDate.parse(
                            examDate,
                            formatter
                    );

            LocalDate today =
                    LocalDate.now();

            long days =
                    ChronoUnit.DAYS.between(
                            today,
                            exam
                    );

            if (days > 1) {

                return "⏳ "
                        + days
                        + " days remaining";

            } else if (days == 1) {

                return "⏳ 1 day remaining";

            } else if (days == 0) {

                return "🔔 Exam is today!";

            } else {

                return "⚠ Exam date has passed";
            }

        } catch (
                DateTimeParseException e
        ) {

            return "Date unavailable";
        }
    }

    // =====================================================
    // MAIN BUTTON STYLE
    // =====================================================

    private void styleMainButton(
            JButton button
    ) {

        button.setPreferredSize(
                new Dimension(
                        160,
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

        button.setOpaque(true);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // =====================================================
    // SMALL BUTTON STYLE
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