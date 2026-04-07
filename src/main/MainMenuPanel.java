package main;

import javax.swing.*;
import java.awt.*;

/**
 * The MainMenuPanel class represents the main menu screen of the "Knowledge Siege" game.
 * It provides buttons for starting the game (with player selection), viewing the scoreboard,
 * and logging out. The panel uses a GridLayout to arrange the buttons vertically.
 */
public class MainMenuPanel extends JPanel {
    /** Button to start a new game, prompting the user to select a player. */
    private JButton startGameButton;

    /** Button to view the scoreboard, navigating to the scoreboard panel. */
    private JButton viewScoreboardButton;

    /** Button to log out, clearing the current user and returning to the login panel. */
    private JButton logoutButton;

    /** Reference to the main KnowledgeSiege game instance for navigation and user management. */
    private KnowledgeSiege game;

    /**
     * Constructs a new MainMenuPanel for the Knowledge Siege game.
     * Initializes the panel with a GridLayout and adds three buttons: Start Game,
     * View Scoreboard, and Logout. Each button has an ActionListener to handle user interaction.
     *
     * @param game The main KnowledgeSiege instance, used for navigation and user management.
     */
    public MainMenuPanel(KnowledgeSiege game) {
        this.game = game;
        setLayout(new GridLayout(3, 1));
        startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(e -> {
            String[] players = {"Captain America", "Iron Man", "Spider Man"};
            String selectedPlayer = (String) JOptionPane.showInputDialog(
                    null, "Choose your player:", "Player Selection",
                    JOptionPane.QUESTION_MESSAGE, null, players, players[0]);
            if (selectedPlayer != null) {
                game.getCurrentUser().setSelectedPlayerImage(selectedPlayer + ".png");
                game.showGame();
            } else {
                JOptionPane.showMessageDialog(null, "Player selection cancelled.");
            }
        });
        add(startGameButton);
        viewScoreboardButton = new JButton("View Scoreboard");
        viewScoreboardButton.addActionListener(e -> game.showScoreboard());
        add(viewScoreboardButton);
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            game.setCurrentUser(null);
            game.showLogin();
        });
        add(logoutButton);
    }
}