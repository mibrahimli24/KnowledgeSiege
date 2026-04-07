package main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * The LoginPanel class represents the login screen of the "Knowledge Siege" game.
 * It provides a simple interface with fields for username and password, and buttons for
 * logging in or navigating to the registration screen. The panel uses a GridLayout to
 * organize its components.
 */
public class LoginPanel extends JPanel {
    /** Text field for entering the username. */
    private JTextField usernameField;

    /** Password field for entering the password. */
    private JPasswordField passwordField;

    /** Button to submit login credentials and authenticate the user. */
    private JButton loginButton;

    /** Button to navigate to the registration screen. */
    private JButton registerButton;

    /** Reference to the main KnowledgeSiege game instance for navigation and user management. */
    private KnowledgeSiege game;

    /**
     * Constructs a new LoginPanel for the Knowledge Siege game.
     * Initializes the panel with a GridLayout, adding labels and fields for username and password,
     * and buttons for login and registration. Each button has an ActionListener to handle user input.
     *
     * @param game The main KnowledgeSiege instance, used for navigation and user management.
     */
    public LoginPanel(KnowledgeSiege game) {
        this.game = game;
        setLayout(new GridLayout(3, 2));
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);
        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            List<User> users = User.loadUsers();
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPasswordHash().equals(User.hashPassword(password))) {
                    game.setCurrentUser(user);
                    game.showMainMenu();
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Invalid username or password");
        });
        add(loginButton);
        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> game.showRegister());
        add(registerButton);
    }
}