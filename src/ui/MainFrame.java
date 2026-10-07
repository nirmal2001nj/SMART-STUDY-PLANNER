package ui;

import panels.AssignmentsPanel;
import panels.DashboardPanel;
import panels.ExamPanel;
import panels.ProgressPanel;
import panels.SubjectsPanel;
import panels.TimetablePanel;
import utils.CurrentUser;
import utils.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel contentPanel;

    public MainFrame() {

        setTitle("Smart Study Planner");

        setSize(
                1200,
                750
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setMinimumSize(
                new Dimension(
                        1000,
                        650
                )
        );

        createUI();
    }

    // =====================================================
    // CREATE MAIN UI
    // =====================================================

    private void createUI() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        // =================================================
        // SIDEBAR
        // =================================================

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(
                        230,
                        750
                )
        );

        sidebar.setBackground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        // =================================================
        // LOGO
        // =================================================

        JLabel logo =
                new JLabel(
                        "🎓 Smart Study"
                );

        logo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        logo.setForeground(
                Color.WHITE
        );

        logo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        logo.setBorder(
                new EmptyBorder(
                        30,
                        10,
                        35,
                        10
                )
        );

        sidebar.add(logo);

        // =================================================
        // MENU BUTTONS
        // =================================================

        JButton dashboardButton =
                createMenuButton(
                        "🏠  Dashboard"
                );

        JButton subjectsButton =
                createMenuButton(
                        "📚  Subjects"
                );

        JButton assignmentsButton =
                createMenuButton(
                        "📝  Assignments"
                );

        JButton examsButton =
                createMenuButton(
                        "📅  Exams"
                );

        JButton timetableButton =
                createMenuButton(
                        "🗓  Timetable"
                );

        JButton progressButton =
                createMenuButton(
                        "📊  Progress"
                );

        JButton aboutButton =
                createMenuButton(
                        "ℹ  About"
                );

        sidebar.add(
                dashboardButton
        );

        sidebar.add(
                subjectsButton
        );

        sidebar.add(
                assignmentsButton
        );

        sidebar.add(
                examsButton
        );

        sidebar.add(
                timetableButton
        );

        sidebar.add(
                progressButton
        );

        // =================================================
        // SPACE
        // =================================================

        sidebar.add(
                Box.createVerticalGlue()
        );

        // =================================================
        // ABOUT
        // =================================================

        sidebar.add(
                aboutButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        20
                )
        );

        // =================================================
        // LOGOUT
        // =================================================

        JButton logoutButton =
                createLogoutButton();

        sidebar.add(
                logoutButton
        );

        sidebar.add(
                Box.createVerticalStrut(
                        25
                )
        );

        // =================================================
        // TOP BAR
        // =================================================

        JPanel topBar =
                new JPanel(
                        new BorderLayout()
                );

        topBar.setBackground(
                Color.WHITE
        );

        topBar.setBorder(
                new EmptyBorder(
                        18,
                        25,
                        18,
                        25
                )
        );

        // =================================================
        // USER NAME
        // =================================================

        String username =
                CurrentUser.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            username = "User";
        }

        JLabel welcomeLabel =
                new JLabel(
                        "Hi, " + username
                );

        welcomeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        welcomeLabel.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        topBar.add(
                welcomeLabel,
                BorderLayout.WEST
        );

        // =================================================
        // PLANNER NAME
        // =================================================

        JLabel plannerLabel =
                new JLabel(
                        "Smart Study Planner"
                );

        plannerLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        plannerLabel.setForeground(
                new Color(
                        107,
                        114,
                        128
                )
        );

        topBar.add(
                plannerLabel,
                BorderLayout.EAST
        );

        // =================================================
        // CONTENT PANEL
        // =================================================

        contentPanel =
                new JPanel(
                        new BorderLayout()
                );

        contentPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        // =================================================
        // BUTTON ACTIONS
        // =================================================

        dashboardButton.addActionListener(
                e -> showDashboard()
        );

        subjectsButton.addActionListener(
                e -> showSubjects()
        );

        assignmentsButton.addActionListener(
                e -> showAssignments()
        );

        examsButton.addActionListener(
                e -> showExams()
        );

        timetableButton.addActionListener(
                e -> showTimetable()
        );

        progressButton.addActionListener(
                e -> showProgress()
        );

        aboutButton.addActionListener(
                e -> showAbout()
        );

        // =================================================
        // DEFAULT PAGE
        // =================================================

        showDashboard();

        // =================================================
        // ADD SIDEBAR
        // =================================================

        mainPanel.add(
                sidebar,
                BorderLayout.WEST
        );

        // =================================================
        // RIGHT SIDE
        // =================================================

        JPanel rightPanel =
                new JPanel(
                        new BorderLayout()
                );

        rightPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        rightPanel.add(
                topBar,
                BorderLayout.NORTH
        );

        rightPanel.add(
                contentPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                rightPanel,
                BorderLayout.CENTER
        );

        add(mainPanel);
    }

    // =====================================================
    // DASHBOARD
    // =====================================================

    private void showDashboard() {

        contentPanel.removeAll();

        DashboardPanel dashboardPanel =
                new DashboardPanel();

        contentPanel.add(
                dashboardPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =====================================================
    // SUBJECTS
    // =====================================================

    private void showSubjects() {

        contentPanel.removeAll();

        SubjectsPanel subjectsPanel =
                new SubjectsPanel();

        contentPanel.add(
                subjectsPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =====================================================
    // ASSIGNMENTS
    // =====================================================

    private void showAssignments() {

        contentPanel.removeAll();

        AssignmentsPanel assignmentsPanel =
                new AssignmentsPanel();

        contentPanel.add(
                assignmentsPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =====================================================
    // EXAMS
    // =====================================================

    private void showExams() {

        contentPanel.removeAll();

        ExamPanel examPanel =
                new ExamPanel();

        contentPanel.add(
                examPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =====================================================
    // TIMETABLE
    // =====================================================

    private void showTimetable() {

        contentPanel.removeAll();

        TimetablePanel timetablePanel =
                new TimetablePanel();

        contentPanel.add(
                timetablePanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =====================================================
    // PROGRESS
    // =====================================================

    private void showProgress() {

        contentPanel.removeAll();

        ProgressPanel progressPanel =
                new ProgressPanel();

        contentPanel.add(
                progressPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =====================================================
    // ABOUT
    // =====================================================

    private void showAbout() {

        contentPanel.removeAll();

        JPanel aboutPanel =
                new JPanel();

        aboutPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        aboutPanel.setBorder(
                new EmptyBorder(
                        40,
                        40,
                        40,
                        40
                )
        );

        aboutPanel.setLayout(
                new BoxLayout(
                        aboutPanel,
                        BoxLayout.Y_AXIS
                )
        );

        // =================================================
        // TITLE
        // =================================================

        JLabel title =
                new JLabel(
                        "ℹ About Smart Study Planner"
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

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // =================================================
        // DESCRIPTION
        // =================================================

        JLabel description =
                new JLabel(
                        "<html>" +
                                "Smart Study Planner is a personalized " +
                                "study management application." +
                                "<br><br>" +
                                "It helps students manage subjects, " +
                                "assignments, exams, timetable and progress." +
                                "</html>"
                );

        description.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        description.setForeground(
                new Color(
                        75,
                        85,
                        99
                )
        );

        description.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        aboutPanel.add(title);

        aboutPanel.add(
                Box.createVerticalStrut(
                        20
                )
        );

        aboutPanel.add(description);

        aboutPanel.add(
                Box.createVerticalStrut(
                        35
                )
        );

        // =================================================
        // ACCOUNT SECTION
        // =================================================

        JLabel accountTitle =
                new JLabel(
                        "Account Settings"
                );

        accountTitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        accountTitle.setForeground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        accountTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        aboutPanel.add(accountTitle);

        aboutPanel.add(
                Box.createVerticalStrut(
                        15
                )
        );

        // =================================================
        // DELETE ACCOUNT BUTTON
        // =================================================

        JButton deleteAccountButton =
                new JButton(
                        "🗑  Delete Account"
                );

        deleteAccountButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        deleteAccountButton.setForeground(
                Color.WHITE
        );

        deleteAccountButton.setBackground(
                new Color(
                        185,
                        28,
                        28
                )
        );

        deleteAccountButton.setMaximumSize(
                new Dimension(
                        220,
                        45
                )
        );

        deleteAccountButton.setPreferredSize(
                new Dimension(
                        220,
                        45
                )
        );

        deleteAccountButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        deleteAccountButton.setFocusPainted(
                false
        );

        deleteAccountButton.setOpaque(
                true
        );

        deleteAccountButton.setContentAreaFilled(
                true
        );

        deleteAccountButton.setBorderPainted(
                false
        );

        deleteAccountButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        deleteAccountButton.addActionListener(
                e -> deleteCurrentAccount()
        );

        aboutPanel.add(
                deleteAccountButton
        );

        aboutPanel.add(
                Box.createVerticalStrut(
                        12
                )
        );

        // =================================================
        // WARNING TEXT
        // =================================================

        JLabel warningLabel =
                new JLabel(
                        "<html>" +
                                "<font color='#6B7280'>" +
                                "This will permanently delete your account " +
                                "and all your study data." +
                                "</font>" +
                                "</html>"
                );

        warningLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        warningLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        aboutPanel.add(
                warningLabel
        );

        contentPanel.add(
                aboutPanel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // =====================================================
    // DELETE CURRENT ACCOUNT
    // =====================================================

    private void deleteCurrentAccount() {

        String username =
                CurrentUser.getUsername();

        if (username == null ||
                username.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No logged-in account found.",
                    "Delete Account",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =================================================
        // FIRST CONFIRMATION
        // =================================================

        int firstConfirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "<html>" +
                                "<b>Delete your account?</b>" +
                                "<br><br>" +
                                "This will permanently delete:" +
                                "<br>• Your account" +
                                "<br>• Your subjects" +
                                "<br>• Your assignments" +
                                "<br>• Your exams" +
                                "<br>• Your study data" +
                                "<br><br>" +
                                "<b>This action cannot be undone.</b>" +
                                "</html>",
                        "Confirm Account Deletion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (firstConfirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        // =================================================
        // SECOND CONFIRMATION
        // =================================================

        int secondConfirmation =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you absolutely sure you want to delete your account?",
                        "Final Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE
                );

        if (secondConfirmation !=
                JOptionPane.YES_OPTION) {

            return;
        }

        // =================================================
        // DELETE ACCOUNT
        // =================================================

        boolean deleted =
                UserManager.deleteAccount(
                        username
                );

        // =================================================
        // SUCCESS
        // =================================================

        if (deleted) {

            JOptionPane.showMessageDialog(
                    this,
                    "Your account and all your study data have been deleted successfully.",
                    "Account Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );

            CurrentUser.setUsername(null);

            dispose();

            LoginFrame loginFrame =
                    new LoginFrame();

            loginFrame.setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "The account could not be completely deleted.\nPlease try again.",
                    "Deletion Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================================
    // MENU BUTTON
    // =====================================================

    private JButton createMenuButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        210,
                        48
                )
        );

        button.setPreferredSize(
                new Dimension(
                        210,
                        48
                )
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                new Color(
                        31,
                        41,
                        55
                )
        );

        button.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        10
                )
        );

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setContentAreaFilled(true);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // =====================================================
    // LOGOUT BUTTON
    // =====================================================

    private JButton createLogoutButton() {

        JButton button =
                new JButton(
                        "🚪  Logout"
                );

        button.setMaximumSize(
                new Dimension(
                        210,
                        48
                )
        );

        button.setPreferredSize(
                new Dimension(
                        210,
                        48
                )
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                new Color(
                        127,
                        29,
                        29
                )
        );

        button.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        10
                )
        );

        button.setFocusPainted(false);

        button.setOpaque(true);

        button.setContentAreaFilled(true);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addActionListener(
                e -> {

                    int result =
                            JOptionPane.showConfirmDialog(
                                    this,
                                    "Are you sure you want to logout?",
                                    "Confirm Logout",
                                    JOptionPane.YES_NO_OPTION
                            );

                    if (result ==
                            JOptionPane.YES_OPTION) {

                        CurrentUser.setUsername(null);

                        dispose();

                        LoginFrame loginFrame =
                                new LoginFrame();

                        loginFrame.setVisible(true);
                    }
                }
        );

        return button;
    }
}