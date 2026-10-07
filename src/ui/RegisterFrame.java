package ui;

import utils.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField recoveryField;

    private JFrame loginFrame;

    public RegisterFrame(JFrame loginFrame) {

        this.loginFrame = loginFrame;

        setTitle(
                "Smart Study Planner - Register"
        );

        setSize(600, 720);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(loginFrame);

        setResizable(false);

        createUI();
    }

    private void createUI() {

        JPanel background =
                new JPanel(new GridBagLayout());

        background.setBackground(
                new Color(245, 247, 250)
        );

        JPanel card =
                new JPanel(
                        new GridBagLayout()
                );

        card.setBackground(Color.WHITE);

        card.setPreferredSize(
                new Dimension(480, 620)
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

        // TITLE

        JLabel title =
                new JLabel(
                        "Create Account"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        gbc.gridy = 0;

        gbc.insets =
                new Insets(
                        0, 0, 5, 0
                );

        card.add(title, gbc);

        // SUBTITLE

        JLabel subtitle =
                new JLabel(
                        "Create your personal study account"
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

        gbc.gridy = 1;

        gbc.insets =
                new Insets(
                        0, 0, 25, 0
                );

        card.add(subtitle, gbc);

        // USERNAME

        card.add(
                createLabel("Username"),
                labelConstraints(2)
        );

        usernameField =
                new JTextField();

        styleField(usernameField);

        card.add(
                usernameField,
                fieldConstraints(3, 15)
        );

        // PASSWORD

        card.add(
                createLabel("Password"),
                labelConstraints(4)
        );

        passwordField =
                new JPasswordField();

        styleField(passwordField);

        card.add(
                passwordField,
                fieldConstraints(5, 15)
        );

        // CONFIRM PASSWORD

        card.add(
                createLabel("Confirm Password"),
                labelConstraints(6)
        );

        confirmPasswordField =
                new JPasswordField();

        styleField(confirmPasswordField);

        card.add(
                confirmPasswordField,
                fieldConstraints(7, 15)
        );

        // RECOVERY

        card.add(
                createLabel("Recovery Answer"),
                labelConstraints(8)
        );

        recoveryField =
                new JTextField();

        styleField(recoveryField);

        card.add(
                recoveryField,
                fieldConstraints(9, 25)
        );

        // CREATE ACCOUNT

        JButton createButton =
                new JButton(
                        "CREATE ACCOUNT"
                );

        styleMainButton(createButton);

        createButton.addActionListener(
                e -> register()
        );

        gbc = new GridBagConstraints();

        gbc.gridx = 0;

        gbc.gridy = 10;

        gbc.weightx = 1;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(
                        0, 0, 15, 0
                );

        card.add(createButton, gbc);

        // BACK TO LOGIN

        JButton loginButton =
                new JButton(
                        "Already have an account? Login"
                );

        styleLinkButton(loginButton);

        loginButton.addActionListener(e -> {

            dispose();

            loginFrame.setVisible(true);
        });

        gbc.gridy = 11;

        gbc.fill =
                GridBagConstraints.NONE;

        gbc.anchor =
                GridBagConstraints.CENTER;

        gbc.insets =
                new Insets(0, 0, 0, 0);

        card.add(loginButton, gbc);

        background.add(card);

        add(background);
    }

    private void register() {

        String username =
                usernameField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField
                                .getPassword()
                );

        String recoveryAnswer =
                recoveryField.getText().trim();

        if (username.isEmpty() ||
                password.isEmpty() ||
                confirmPassword.isEmpty() ||
                recoveryAnswer.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Registration Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!password.equals(
                confirmPassword
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (UserManager.userExists(
                username
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Username already exists.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        boolean created =
                UserManager.registerUser(
                        username,
                        password,
                        recoveryAnswer
                );

        if (created) {

            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            loginFrame.setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to create account.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

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

        return label;
    }

    private GridBagConstraints labelConstraints(
            int row
    ) {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.gridy = row;

        gbc.weightx = 1;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(
                        0, 0, 7, 0
                );

        return gbc;
    }

    private GridBagConstraints fieldConstraints(
            int row,
            int bottom
    ) {

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.gridy = row;

        gbc.weightx = 1;

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(
                        0, 0, bottom, 0
                );

        return gbc;
    }

    private void styleField(
            JTextField field
    ) {

        field.setPreferredSize(
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