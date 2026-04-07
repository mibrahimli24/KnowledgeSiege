package main;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The ScoreboardPanel class represents the scoreboard screen of the "Knowledge Siege" game.
 * It displays a list of game scores loaded from a file (data/scoreboard.txt) in a scrollable
 * text area, sorted by score (descending), then by username, and finally by game number.
 */
public class ScoreboardPanel extends JPanel {
    /** The text area displaying the sorted list of scores. */
    private JTextArea scoreArea;

    /**
     * Constructs a new ScoreboardPanel for the Knowledge Siege game.
     * Initializes the panel with a BorderLayout, adds a scrollable text area for displaying scores,
     * and loads the scores from the scoreboard file.
     *
     * @param game The main KnowledgeSiege instance (not used directly but required for panel integration).
     */
    public ScoreboardPanel(KnowledgeSiege game) {
        setLayout(new BorderLayout());
        scoreArea = new JTextArea(20, 30);
        scoreArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(scoreArea);
        add(scrollPane, BorderLayout.CENTER);
        loadScores();
    }

    /**
     * Loads scores from the file data/scoreboard.txt and displays them in the text area.
     * Scores are sorted by score (descending), then by username (alphabetically), and finally
     * by game number (ascending). Each entry is formatted as "GameX by username (score)".
     */
    private void loadScores() {
        List<ScoreEntry> entries = new ArrayList<>();
        File file = new File("data/scoreboard.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    int gameNumber = Integer.parseInt(parts[1]);
                    int score = Integer.parseInt(parts[2]);
                    entries.add(new ScoreEntry(username, gameNumber, score));
                }
            }
        } catch (FileNotFoundException e) {
            scoreArea.setText("No scores available.");
            return;
        }

        entries.sort((a, b) -> {
            if (a.score != b.score) {
                return Integer.compare(b.score, a.score);
            } else if (!a.username.equals(b.username)) {
                return a.username.compareTo(b.username);
            } else {
                return Integer.compare(a.gameNumber, b.gameNumber);
            }
        });

        StringBuilder sb = new StringBuilder();
        for (ScoreEntry entry : entries) {
            sb.append("Game").append(entry.gameNumber).append(" by ").append(entry.username)
              .append(" (").append(entry.score).append(")\n");
        }
        scoreArea.setText(sb.toString());
    }

    /**
     * Inner class representing a single score entry with username, game number, and score.
     */
    private static class ScoreEntry {
        /** The username of the player. */
        String username;

        /** The game number for this score entry (incremented per user). */
        int gameNumber;

        /** The score achieved in the game. */
        int score;

        /**
         * Constructs a new ScoreEntry with the specified details.
         *
         * @param username The username of the player.
         * @param gameNumber The game number for this score entry.
         * @param score The score achieved in the game.
         */
        public ScoreEntry(String username, int gameNumber, int score) {
            this.username = username;
            this.gameNumber = gameNumber;
            this.score = score;
        }
    }
}