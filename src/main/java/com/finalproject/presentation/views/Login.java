package com.finalproject.presentation.views;

import com.finalproject.application.dto.FindUserResponse;
import com.finalproject.configuration.DependencyInjector;
import com.finalproject.domain.exception.DomainException;
import com.finalproject.infrastructure.security.UserContext;
import com.finalproject.infrastructure.security.UserContextHolder;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {
    private JButton loginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel welcomeLabel;
    private JPanel loginPanel;

    private final DependencyInjector dependencyInjector;

    public Login(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector;
        setupUI();
    }

    private void setupUI() {
        setTitle("Login");
        setSize(400, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setContentPane(loginPanel);

        loginButton.addActionListener(e -> handleLogin());
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            FindUserResponse userResponse = dependencyInjector.getUserApplicationService()
                    .findUser(username, password);
            UserContextHolder.set(new UserContext(
                    userResponse.getId(),
                    userResponse.getUsername(),
                    userResponse.getUserType()));

            SwingUtilities.invokeLater(() -> {
                new MainPanel(dependencyInjector, userResponse);
                dispose();
            });
        } catch (DomainException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
}
