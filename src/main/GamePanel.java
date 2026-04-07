package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.Timer;

/**
 * The GamePanel class represents the main game panel for the "Knowledge Siege" game.
 * It handles the game loop, rendering, user input, and game logic, including player movement,
 * KnowledgeKeeper interactions, ShotBox handling, and logging of game events.
 * The panel is divided into a game area (left) and a side panel (right) displaying the score,
 * health bar, level, and a list of active questions/info.
 */
public class GamePanel extends JPanel implements ActionListener {
    /** The timer driving the game loop, firing at 60 FPS. */
    private Timer timer;

    /** The player controlled by the user. */
    private Player player;

    /** List of KnowledgeKeepers (SLs, TAs, Professors) currently active in the game. */
    private List<KnowledgeKeeper> knowledgeKeepers;

    /** List of ShotBoxes (questions or info) currently on the screen. */
    private List<ShotBox> shotBoxes;

    /** Current level of the game (1, 2, or 3). */
    private int level;

    /** Player's current score, increased by collecting info ShotBoxes. */
    private int score;

    /** Player's current health, decreased by question ShotBoxes, starting at 100. */
    private int health;

    /** Reference to the main KnowledgeSiege game instance for navigation and user data. */
    private KnowledgeSiege game;

    /** List of texts for active ShotBoxes, used for display in the JList. */
    private List<String> activeShotBoxTexts;

    /** Model for the JList displaying active ShotBox texts (questions/info). */
    private DefaultListModel<String> shotBoxListModel;

    /** JList displaying the current questions and info ShotBoxes on the right panel. */
    private JList<String> shotBoxList;

    /** Label displaying the current score on the side panel. */
    private JLabel scoreLabel;

    /** Label displaying the current level on the side panel. */
    private JLabel levelLabel;

    /** Progress bar displaying the player's health on the side panel. */
    private JProgressBar healthBar;

    /** Width of the game area (excluding the side panel), in pixels. */
    static final int WIDTH = 800;

    /** Height of the game area, in pixels. */
    private static final int HEIGHT = 600;

    /** Frames per second for the game loop. */
    private static final int FPS = 60;

    /** Time step per frame (1/FPS), in seconds, used for movement calculations. */
    public static final double DT = 1.0 / FPS;

    /** List of all Section Leader names for spawning in levels 1 and 2. */
    private static final List<String> allSLNames = Arrays.asList(
            "efe_degismis", "ekin_gun", "ahmet_sukru_kilic", "nazrin_mustafazadeh",
            "ozan_ozak", "abdullah_daoud", "ertugrul_recep_kocaman", "burak_gercekaslan");

    /** List of all Teaching Assistant names for spawning in levels 2 and 3. */
    private static final List<String> allTANames = Arrays.asList(
            "vahideh_hayyolalam", "abdulrezzak_zekiye", "hamza_abuzahra",
            "fatma_nur_yasar", "aylanur_erturk");

    /** List of all Professor names for spawning in level 3. */
    private static final List<String> allProfessorNames = Arrays.asList(
            "oznur_ozkasap", "attila_gursoy");

    /** Shuffled list of Section Leader names for random level assignment. */
    private List<String> shuffledSLNames;

    /** Shuffled list of Teaching Assistant names for random level assignment. */
    private List<String> shuffledTANames;

    /** Formatter for logging timestamps in the format "yyyy-MM-dd HH:mm". */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Constructs a new GamePanel for the Knowledge Siege game.
     * Initializes the game components, including the player, side panel (with health bar,
     * score, level, and JList for questions/info), and starts the game loop.
     *
     * @param game The main KnowledgeSiege instance, used for navigation and user data.
     * @throws IllegalStateException if the player's image is not selected.
     */
    public GamePanel(KnowledgeSiege game) {
        this.game = game;
        setPreferredSize(new Dimension(WIDTH + 200, HEIGHT));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.setDx(-player.getSpeed());
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setDx(player.getSpeed());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setDx(0);
                }
            }
        });

        String playerImage = game.getCurrentUser().getSelectedPlayerImage();
        if (playerImage == null) {
            JOptionPane.showMessageDialog(null, "Player image not selected. Please select a player.");
            throw new IllegalStateException("Player image not selected");
        }
        BufferedImage image = loadImage("images/" + playerImage);
        player = new Player(375, 550, image);
        knowledgeKeepers = new ArrayList<>();
        shotBoxes = new ArrayList<>();
        activeShotBoxTexts = new ArrayList<>();
        shotBoxListModel = new DefaultListModel<>();
        shotBoxList = new JList<>(shotBoxListModel);
        shotBoxList.setFixedCellWidth(180);
        shotBoxList.setVisibleRowCount(10);
        score = 0;
        health = 100;

        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, HEIGHT));
        sidePanel.add(new JScrollPane(shotBoxList), BorderLayout.CENTER);
        JPanel statsPanel = new JPanel(new GridLayout(3, 1));
        scoreLabel = new JLabel("Score: 0");
        healthBar = new JProgressBar(0, 100);
        healthBar.setValue(health);
        healthBar.setStringPainted(true);
        levelLabel = new JLabel("Level: 1");
        statsPanel.add(scoreLabel);
        statsPanel.add(healthBar);
        statsPanel.add(levelLabel);
        sidePanel.add(statsPanel, BorderLayout.NORTH);
        setLayout(new BorderLayout());
        add(sidePanel, BorderLayout.EAST);

        timer = new Timer((int) (DT * 1000), this);
        timer.start();

        shuffledSLNames = new ArrayList<>(allSLNames);
        Collections.shuffle(shuffledSLNames);
        shuffledTANames = new ArrayList<>(allTANames);
        Collections.shuffle(shuffledTANames);

        startLevel(1);
    }

    /**
     * Renders the game components, including the background, player, KnowledgeKeepers,
     * and ShotBoxes, on the panel.
     *
     * @param g The Graphics object used for rendering.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        if (player.getImage() != null) {
            g2d.drawImage(player.getImage(), (int) player.getX(), (int) player.getY(), null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect((int) player.getX(), (int) player.getY(), 50, 50);
        }
        for (KnowledgeKeeper kk : knowledgeKeepers) {
            if (kk.getImage() != null) {
                g2d.drawImage(kk.getImage(), (int) kk.getX(), (int) kk.getY(), null);
            } else {
                g.setColor(Color.YELLOW);
                g.fillRect((int) kk.getX(), (int) kk.getY(), 50, 50);
            }
        }
        for (ShotBox sb : shotBoxes) {
            if (sb.getImage() != null) {
                g2d.drawImage(sb.getImage(), (int) sb.getX(), (int) sb.getY(), null);
            } else {
                g.setColor(sb.getType() == ShotBox.Type.QUESTION ? Color.RED : Color.GREEN);
                g.fillRect((int) sb.getX(), (int) sb.getY(), 20, 20);
            }
        }
    }

    /**
     * Handles the game loop, updating the game state each frame (at 60 FPS).
     * Updates the player, KnowledgeKeepers, and ShotBoxes, checks for collisions,
     * manages level transitions, and logs game events.
     *
     * @param e The ActionEvent triggered by the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        player.update();
        for (KnowledgeKeeper kk : knowledgeKeepers) {
            double newX = calculateNewX(kk);
            if (!wouldOverlap(kk, newX)) {
                kk.setX(newX);
            }
            if (Math.random() < kk.getShootProbability()) {
                shotBoxes.add(kk.shoot());
                activeShotBoxTexts.add(kk.getLastShotText());
                shotBoxListModel.addElement(kk.getLastShotText());
            }
        }
        Iterator<ShotBox> sbIterator = shotBoxes.iterator();
        while (sbIterator.hasNext()) {
            ShotBox sb = sbIterator.next();
            sb.update();
            if (sb.getBounds().intersects(player.getBounds())) {
                if (sb.getType() == ShotBox.Type.QUESTION) {
                    int previousHealth = health;
                    health -= sb.getDamage();
                    healthBar.setValue(health);
                    logEvent("Player " + game.getCurrentUser().getUsername() + " hit by " + sb.getType() + ": -" + sb.getDamage() + " health (Health: " + previousHealth + " -> " + health + ")");
                    if (health <= 0) {
                        timer.stop();
                        logEvent("Game Over for " + game.getCurrentUser().getUsername() + " with final score: " + score);
                        JOptionPane.showMessageDialog(null, "Game Over! Your score: " + score);
                        saveScore();
                        game.showMainMenu();
                    }
                } else {
                    int previousScore = score;
                    score += sb.getPoints();
                    scoreLabel.setText("Score: " + score);
                    logEvent("Player " + game.getCurrentUser().getUsername() + " collected info from " + sb.getType() + ": +" + sb.getPoints() + " points (Score: " + previousScore + " -> " + score + ")");
                }
                sbIterator.remove();
                activeShotBoxTexts.remove(sb.getText());
                shotBoxListModel.removeElement(sb.getText());
            } else if (sb.getY() > HEIGHT) {
                sbIterator.remove();
                activeShotBoxTexts.remove(sb.getText());
                shotBoxListModel.removeElement(sb.getText());
            }
        }
        if (level == 1 && score >= 50) {
            startLevel(2);
        } else if (level == 2 && score >= 100) {
            startLevel(3);
        } else if (level == 3 && score >= 150) {
            timer.stop();
            logEvent("Game Completed by " + game.getCurrentUser().getUsername() + " with final score: " + score);
            JOptionPane.showMessageDialog(null, "Congratulations! You won!");
            saveScore();
            score = 0;
            game.showMainMenu();
        }
        repaint();
    }

    /**
     * Calculates the new x-coordinate for a KnowledgeKeeper based on its movement behavior.
     * The KnowledgeKeeper either moves toward the player or randomly left/right, constrained
     * within the game area bounds.
     *
     * @param kk The KnowledgeKeeper to move.
     * @return The new x-coordinate for the KnowledgeKeeper.
     */
    private double calculateNewX(KnowledgeKeeper kk) {
        double playerX = player.getX();
        double dx;
        if (Math.random() < kk.getMovementTowardsPlayerProb()) {
            dx = playerX > kk.getX() ? kk.getSpeed() : -kk.getSpeed();
        } else {
            dx = Math.random() < 0.5 ? kk.getSpeed() : -kk.getSpeed();
        }
        double newX = kk.getX() + dx * DT;
        if (newX < 0) newX = 0;
        if (newX > WIDTH - kk.getWidth()) newX = WIDTH - kk.getWidth();
        return newX;
    }

    /**
     * Checks if moving a KnowledgeKeeper to a new x-coordinate would cause it to overlap
     * with another KnowledgeKeeper.
     *
     * @param current The KnowledgeKeeper to check.
     * @param newX The proposed new x-coordinate.
     * @return true if the move would cause an overlap, false otherwise.
     */
    private boolean wouldOverlap(KnowledgeKeeper current, double newX) {
        Rectangle newBounds = new Rectangle((int) newX, (int) current.getY(), current.getWidth(), current.getHeight());
        for (KnowledgeKeeper other : knowledgeKeepers) {
            if (other != current && newBounds.intersects(other.getBounds())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes a new level by clearing the current game state and spawning
     * KnowledgeKeepers appropriate for the level. Logs the level transition.
     *
     * @param level The level to start (1, 2, or 3).
     */
    private void startLevel(int level) {
        this.level = level;
        knowledgeKeepers.clear();
        shotBoxes.clear();
        activeShotBoxTexts.clear();
        shotBoxListModel.clear();

        logEvent("Player " + game.getCurrentUser().getUsername() + " started Level " + level);

        List<KnowledgeKeeper> newKnowledgeKeepers = new ArrayList<>();
        if (level == 1) {
            List<String> slsForLevel1 = shuffledSLNames.subList(0, 4);
            for (String name : slsForLevel1) {
                SL sl = new SL(name);
                placeKnowledgeKeeper(sl, newKnowledgeKeepers);
            }
        } else if (level == 2) {
            List<String> slsForLevel2 = shuffledSLNames.subList(4, 8);
            for (String name : slsForLevel2) {
                SL sl = new SL(name);
                placeKnowledgeKeeper(sl, newKnowledgeKeepers);
            }
            List<String> tasForLevel2 = shuffledTANames.subList(0, 2);
            for (String name : tasForLevel2) {
                TA ta = new TA(name);
                placeKnowledgeKeeper(ta, newKnowledgeKeepers);
            }
        } else if (level == 3) {
            List<String> tasForLevel3 = shuffledTANames.subList(2, 5);
            for (String name : tasForLevel3) {
                TA ta = new TA(name);
                placeKnowledgeKeeper(ta, newKnowledgeKeepers);
            }
            for (String name : allProfessorNames) {
                Professor prof = new Professor(name);
                placeKnowledgeKeeper(prof, newKnowledgeKeepers);
            }
        }
        knowledgeKeepers.addAll(newKnowledgeKeepers);
        levelLabel.setText("Level: " + level);
    }

    /**
     * Places a KnowledgeKeeper at a non-overlapping position on the game area.
     * Tries up to 100 times to find a non-overlapping x-coordinate.
     *
     * @param kk The KnowledgeKeeper to place.
     * @param placedKeepers List of already placed KnowledgeKeepers to check for overlaps.
     */
    private void placeKnowledgeKeeper(KnowledgeKeeper kk, List<KnowledgeKeeper> placedKeepers) {
        int maxAttempts = 100;
        int attempts = 0;
        double x;
        do {
            x = Math.random() * (WIDTH - kk.getWidth());
            attempts++;
            if (attempts > maxAttempts) {
                System.err.println("Warning: Could not find non-overlapping position for " + kk.getClass().getSimpleName() + " after " + maxAttempts + " attempts.");
                break;
            }
        } while (wouldOverlapAtPosition(kk, x, placedKeepers));
        kk.setX(x);
        kk.setY(50);
        placedKeepers.add(kk);
    }

    /**
     * Checks if placing a KnowledgeKeeper at a specific x-coordinate (at y=50) would
     * cause it to overlap with any already placed KnowledgeKeepers.
     *
     * @param current The KnowledgeKeeper to check.
     * @param newX The proposed x-coordinate.
     * @param others List of already placed KnowledgeKeepers.
     * @return true if the position would cause an overlap, false otherwise.
     */
    private boolean wouldOverlapAtPosition(KnowledgeKeeper current, double newX, List<KnowledgeKeeper> others) {
        Rectangle newBounds = new Rectangle((int) newX, 50, current.getWidth(), current.getHeight());
        for (KnowledgeKeeper other : others) {
            Rectangle otherBounds = new Rectangle((int) other.getX(), (int) other.getY(), other.getWidth(), other.getHeight());
            if (newBounds.intersects(otherBounds)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Saves the player's score to the scoreboard file (data/scoreboard.txt).
     * Increments the game number for the user and appends the score entry.
     * Logs any IOExceptions to the console.
     */
    private void saveScore() {
        String username = game.getCurrentUser().getUsername();
        int maxGameNumber = 0;
        File scoreFile = new File("data/scoreboard.txt");
        try (Scanner scanner = new Scanner(scoreFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 3 && parts[0].equals(username)) {
                    int num = Integer.parseInt(parts[1]);
                    if (num > maxGameNumber) {
                        maxGameNumber = num;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        int gameNumber = maxGameNumber + 1;
        try (Formatter formatter = new Formatter(new FileWriter("data/scoreboard.txt", true))) {
            formatter.format("%s,%d,%d%n", username, gameNumber, score);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Loads an image from the specified path.
     * If the image cannot be loaded, logs an error and returns null, triggering
     * a fallback rendering (e.g., a colored rectangle).
     *
     * @param path The path to the image file (relative to the project directory).
     * @return The loaded BufferedImage, or null if loading fails.
     */
    private BufferedImage loadImage(String path) {
        try {
            File file = new File(path);
            return ImageIO.read(file);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Logs a game event to the log file (logs/game_log.txt) with a timestamp.
     * Events include score gains, health losses, level transitions, and game completion.
     *
     * @param event The event message to log.
     */
    private void logEvent(String event) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(formatter);
        String logMessage = "[" + timestamp + "] - " + event;
        try (Formatter formatter = new Formatter(new FileWriter("logs/game_log.txt", true))) {
            formatter.format("%s%n", logMessage);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}