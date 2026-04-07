package main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The RegisterPanel class represents the registration screen of the "Knowledge Siege" game.
 * It provides fields for username, password, and password confirmation, along with buttons
 * to register a new user or return to the login screen. The panel uses a GridLayout to
 * organize its components.
 */
public class RegisterPanel extends JPanel {
    /** Text field for entering the new username. */
    private JTextField usernameField;

    /** Password field for entering the new password. */
    private JPasswordField passwordField;

    /** Password field for confirming the new password. */
    private JPasswordField confirmPasswordField;

    /** Button to submit registration details and create a new user. */
    private JButton registerButton;

    /** Button to return to the login screen without registering. */
    private JButton backButton;

    /** Reference to the main KnowledgeSiege game instance for navigation and user management. */
    private KnowledgeSiege game;

    /**
     * Constructs a new RegisterPanel for the Knowledge Siege game.
     * Initializes the panel with a GridLayout, adding labels and fields for username, password,
     * and password confirmation, and buttons for registration and back navigation. Each button
     * has an ActionListener to handle user input.
     *
     * @param game The main KnowledgeSiege instance, used for navigation and user management.
     */
    public RegisterPanel(KnowledgeSiege game) {
        this.game = game;
        setLayout(new GridLayout(4, 2));
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);
        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);
        add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField();
        add(confirmPasswordField);
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match");
                return;
            }
            List<User> users = User.loadUsers();
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    JOptionPane.showMessageDialog(null, "Username already exists");
                    return;
                }
            }
            User newUser = new User(username, User.hashPassword(password));
            users.add(newUser);
            User.saveUsers(users);
            game.setCurrentUser(newUser);
            game.showMainMenu();
        });
        add(registerButton);
        backButton = new JButton("Back");
        backButton.addActionListener(e -> game.showLogin());
        add(backButton);
    }
}