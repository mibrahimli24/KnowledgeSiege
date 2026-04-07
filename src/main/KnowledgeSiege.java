package main;

import javax.swing.*;
import java.awt.*;

/**
 * The KnowledgeSiege class represents the main frame of the "Knowledge Siege" game.
 * It manages the overall application window, navigation between different panels (login, register,
 * main menu, game, and scoreboard), and the current user state. The game uses a CardLayout to switch
 * between panels, providing a seamless user experience.
 */
public class KnowledgeSiege extends JFrame {
    /** The CardLayout used to switch between different panels (e.g., login, game). */
    private CardLayout cardLayout;

    /** The main panel containing all sub-panels, managed by the CardLayout. */
    private JPanel mainPanel;

    /** The panel for user login. */
    private LoginPanel loginPanel;

    /** The panel for user registration. */
    private RegisterPanel registerPanel;

    /** The panel for the main menu, where users can start the game or view the scoreboard. */
    private MainMenuPanel mainMenuPanel;

    /** The panel for the actual game, initialized when the game is first started. */
    private GamePanel gamePanel;

    /** The panel for displaying the scoreboard. */
    private ScoreboardPanel scoreboardPanel;

    /** The currently logged-in user, or null if no user is logged in. */
    private User currentUser;

    /**
     * Constructs a new KnowledgeSiege frame, setting up the main window and initializing
     * all panels except the game panel (which is lazily initialized). The frame starts by
     * displaying the login panel.
     */
    public KnowledgeSiege() {
        setTitle("Knowledge Siege");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        mainMenuPanel = new MainMenuPanel(this);
        scoreboardPanel = new ScoreboardPanel(this);
        // gamePanel is initialized in showGame()

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(mainMenuPanel, "mainMenu");
        mainPanel.add(scoreboardPanel, "scoreboard");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    /**
     * Displays the login panel, allowing the user to log in with their credentials.
     */
    public void showLogin() { 
        cardLayout.show(mainPanel, "login"); 
    }

    /**
     * Displays the register panel, allowing the user to create a new account.
     */
    public void showRegister() { 
        cardLayout.show(mainPanel, "register"); 
    }

    /**
     * Displays the main menu panel, where the user can start the game, view the scoreboard,
     * or log out.
     */
    public void showMainMenu() { 
        cardLayout.show(mainPanel, "mainMenu"); 
    }

    /**
     * Displays the game panel, starting the game if the user is logged in and has selected
     * a player image. If conditions are not met, redirects to the appropriate panel (login or main menu)
     * with an error message. Lazily initializes the game panel on first call.
     */
    public void showGame() {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "Please log in or register first.");
            cardLayout.show(mainPanel, "login");
            return;
        }
        if (currentUser.getSelectedPlayerImage() == null) {
            JOptionPane.showMessageDialog(null, "Please select a player image.");
            cardLayout.show(mainPanel, "mainMenu");
            return;
        }
        if (gamePanel == null) {
            gamePanel = new GamePanel(this);
            mainPanel.add(gamePanel, "game");
        }
        cardLayout.show(mainPanel, "game");
    }

    /**
     * Displays the scoreboard panel, showing the user's past game scores.
     */
    public void showScoreboard() { 
        cardLayout.show(mainPanel, "scoreboard"); 
    }

    /**
     * Sets the currently logged-in user.
     *
     * @param user The user to set as the current user, or null to indicate no user is logged in.
     */
    public void setCurrentUser(User user) { 
        this.currentUser = user; 
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return The current user, or null if no user is logged in.
     */
    public User getCurrentUser() { 
        return currentUser; 
    }

    /**
     * The main entry point for the Knowledge Siege game.
     * Launches the game on the Event Dispatch Thread (EDT) to ensure thread safety with Swing components.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        /* *********** Pledge of Honor *************************** *
        I hereby certify that I have completed this lab assignment on my own
        without any help from anyone else. I understand that the only sources of authorized
        information in this lab assignment are (1) the course textbook, (2) the
        materials posted at the course website and (3) any study notes handwritten by myself.
        I have not used, accessed or received any information from any other unauthorized
        source in taking this lab assignment. The effort in the assignment thus belongs
        completely to me.

        READ AND SIGN BY WRITING YOUR NAME SURNAME AND STUDENT ID
        SIGNATURE: Mahammad Ibrahimli, 88371

        **********************************************************/
        SwingUtilities.invokeLater(() -> {
            KnowledgeSiege game = new KnowledgeSiege();
            game.setVisible(true);
        });
    }
}