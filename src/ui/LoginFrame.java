package ui;

import utils.CurrentUser;
import utils.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {

        setTitle("Smart Study Planner - Login");

        setSize(600, 700);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setResizable(false);

        createUI();
    }

    private void createUI() {

        // ==========================================
        // MAIN BACKGROUND
        // ==========================================

        JPanel background =
                new JPanel(new GridBagLayout());

        background.setBackground(
                new Color(245, 247, 250)
        );

        // ==========================================
        // WHITE LOGIN CARD
        // ==========================================

        JPanel card =
                new JPanel(new GridBagLayout());

        card.setBackground(Color.WHITE);

        card.setPreferredSize(
                new Dimension(480, 600)
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 228, 233)
                        ),
                        new EmptyBorder(
                                35, 55, 35, 55
                        )
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.weightx = 1;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // ==========================================
        // LOGO
        // ==========================================

        JLabel logo =
                new JLabel("🎓");

        logo.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        45
                )
        );

        logo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy = 0;

        gbc.insets =
                new Insets(
                        0, 0, 10, 0
                );

        card.add(logo, gbc);

        // ==========================================
        // TITLE
        // ==========================================

        JLabel title =
                new JLabel(
                        "Smart Study Planner"
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

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy = 1;

        gbc.insets =
                new Insets(
                        0, 0, 5, 0
                );

        card.add(title, gbc);

        // ==========================================
        // SUBTITLE
        // ==========================================

        JLabel subtitle =
                new JLabel(
                        "Login to continue"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(
                new Color(107, 114, 128)
        );

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy = 2;

        gbc.insets =
                new Insets(
                        0, 0, 30, 0
                );

        card.add(subtitle, gbc);

        // ==========================================
        // USERNAME LABEL
        // ==========================================

        JLabel usernameLabel =
                createLabel("Username");

        gbc.gridy = 3;

        gbc.insets =
                new Insets(
                        0, 0, 7, 0
                );

        card.add(usernameLabel, gbc);

        // ==========================================
        // USERNAME FIELD
        // ==========================================

        usernameField =
                new JTextField();

        styleField(usernameField);

        gbc.gridy = 4;

        gbc.insets =
                new Insets(
                        0, 0, 18, 0
                );

        card.add(usernameField, gbc);

        // ==========================================
        // PASSWORD LABEL
        // ==========================================

        JLabel passwordLabel =
                createLabel("Password");

        gbc.gridy = 5;

        gbc.insets =
                new Insets(
                        0, 0, 7, 0
                );

        card.add(passwordLabel, gbc);

        // ==========================================
        // PASSWORD FIELD
        // ==========================================

        passwordField =
                new JPasswordField();

        styleField(passwordField);

        gbc.gridy = 6;

        gbc.insets =
                new Insets(
                        0, 0, 25, 0
                );

        card.add(passwordField, gbc);

        // ==========================================
        // LOGIN BUTTON
        // ==========================================

        JButton loginButton =
                new JButton("LOGIN");

        styleMainButton(loginButton);

        loginButton.addActionListener(
                e -> login()
        );

        gbc.gridy = 7;

        gbc.insets =
                new Insets(
                        0, 0, 15, 0
                );

        card.add(loginButton, gbc);

        // ==========================================
        // FORGOT PASSWORD
        // ==========================================

        JButton forgotButton =
                new JButton(
                        "Forgot Password?"
                );

        styleLinkButton(forgotButton);

        forgotButton.addActionListener(e -> {

            ForgotPasswordFrame frame =
                    new ForgotPasswordFrame(this);

            frame.setVisible(true);
        });

        gbc.gridy = 8;

        gbc.insets =
                new Insets(
                        0, 0, 18, 0
                );

        card.add(forgotButton, gbc);

        // ==========================================
        // REGISTER SECTION
        // ==========================================

        JPanel registerPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                5,
                                0
                        )
                );

        registerPanel.setBackground(
                Color.WHITE
        );

        JLabel registerText =
                new JLabel(
                        "Don't have an account?"
                );

        registerText.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        JButton registerButton =
                new JButton("Register");

        styleLinkButton(registerButton);

        registerButton.addActionListener(e -> {

            RegisterFrame frame =
                    new RegisterFrame(this);

            frame.setVisible(true);

            setVisible(false);
        });

        registerPanel.add(registerText);

        registerPanel.add(registerButton);

        gbc.gridy = 9;

        gbc.insets =
                new Insets(
                        0, 0, 0, 0
                );

        card.add(registerPanel, gbc);

        // ==========================================
        // ADD CARD
        // ==========================================

        background.add(card);

        add(background);
    }

    // ==========================================
    // LOGIN FUNCTION
    // ==========================================

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        // Check empty fields

        if (username.isEmpty() ||
                password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Login Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Check username and password

        if (!UserManager.loginUser(
                username,
                password
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // ==========================================
        // SAVE CURRENT LOGGED-IN USER
        // ==========================================

        CurrentUser.setUsername(username);

        // ==========================================
        // LOGIN SUCCESS POPUP
        // ==========================================

        JOptionPane.showMessageDialog(
                this,
                "Login successful!",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE
        );

        // ==========================================
        // OPEN MAIN DASHBOARD
        // ==========================================

        dispose();

        MainFrame mainFrame =
                new MainFrame();

        mainFrame.setVisible(true);
    }

    // ==========================================
    // LABEL STYLE
    // ==========================================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(
                new Color(31, 41, 55)
        );

        label.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        return label;
    }

    // ==========================================
    // TEXT FIELD STYLE
    // ==========================================

    private void styleField(
            JTextField field
    ) {

        field.setPreferredSize(
                new Dimension(
                        350,
                        42
                )
        );

        field.setMinimumSize(
                new Dimension(
                        350,
                        42
                )
        );

        field.setMaximumSize(
                new Dimension(
                        350,
                        42
                )
        );

        field.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(200, 205, 212)
                        ),
                        new EmptyBorder(
                                8, 12, 8, 12
                        )
                )
        );
    }

    // ==========================================
    // MAIN BUTTON STYLE
    // ==========================================

    private void styleMainButton(
            JButton button
    ) {

        button.setPreferredSize(
                new Dimension(
                        350,
                        45
                )
        );

        button.setMinimumSize(
                new Dimension(
                        350,
                        45
                )
        );

        button.setMaximumSize(
                new Dimension(
                        350,
                        45
                )
        );

        button.setBackground(
                new Color(31, 41, 55)
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
    // LINK BUTTON STYLE
    // ==========================================

    private void styleLinkButton(
            JButton button
    ) {

        button.setBorderPainted(false);

        button.setContentAreaFilled(false);

        button.setFocusPainted(false);

        button.setForeground(
                new Color(37, 99, 235)
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }
}