package ui;

import utils.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ForgotPasswordFrame extends JFrame {

    private JTextField usernameField;
    private JTextField recoveryField;
    private JPasswordField newPasswordField;

    public ForgotPasswordFrame(JFrame parent) {

        setTitle(
                "Smart Study Planner - Reset Password"
        );

        setSize(600, 600);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(parent);

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
                new Dimension(480, 500)
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
                        "Reset Password"
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
                        0, 0, 30, 0
                );

        card.add(title, gbc);

        // USERNAME

        card.add(
                createLabel("Username"),
                labelConstraints(1)
        );

        usernameField =
                new JTextField();

        styleField(usernameField);

        card.add(
                usernameField,
                fieldConstraints(2, 18)
        );

        // RECOVERY ANSWER

        card.add(
                createLabel("Recovery Answer"),
                labelConstraints(3)
        );

        recoveryField =
                new JTextField();

        styleField(recoveryField);

        card.add(
                recoveryField,
                fieldConstraints(4, 18)
        );

        // NEW PASSWORD

        card.add(
                createLabel("New Password"),
                labelConstraints(5)
        );

        newPasswordField =
                new JPasswordField();

        styleField(newPasswordField);

        card.add(
                newPasswordField,
                fieldConstraints(6, 25)
        );

        // RESET BUTTON

        JButton resetButton =
                new JButton(
                        "RESET PASSWORD"
                );

        styleMainButton(resetButton);

        resetButton.addActionListener(
                e -> resetPassword()
        );

        gbc.gridy = 7;

        gbc.insets =
                new Insets(
                        0, 0, 0, 0
                );

        card.add(resetButton, gbc);

        background.add(card);

        add(background);
    }

    private void resetPassword() {

        String username =
                usernameField.getText().trim();

        String answer =
                recoveryField.getText().trim();

        String newPassword =
                new String(
                        newPasswordField.getPassword()
                );

        if (username.isEmpty() ||
                answer.isEmpty() ||
                newPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        boolean success =
                UserManager.resetPassword(
                        username,
                        answer,
                        newPassword
                );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password reset successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Username or recovery answer is incorrect.",
                    "Reset Failed",
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
}