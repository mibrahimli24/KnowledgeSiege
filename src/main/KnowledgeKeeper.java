package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.List;
import java.util.Scanner;

/**
 * The KnowledgeKeeper class is an abstract base class representing entities in the "Knowledge Siege" game
 * that can shoot questions or info at the player (e.g., Section Leaders, Teaching Assistants, Professors).
 * It manages position, image, content (questions/info), and shooting behavior, with abstract methods
 * for specific implementations by subclasses.
 */
public abstract class KnowledgeKeeper {
    /** The x-coordinate of the KnowledgeKeeper. */
    private double x;

    /** The y-coordinate of the KnowledgeKeeper. */
    private double y;

    /** The image representing the KnowledgeKeeper, or null if not loaded. */
    private BufferedImage image;

    /** The width of the KnowledgeKeeper, defaults to 50 pixels (updated if image is loaded). */
    private int width = 50;

    /** The height of the KnowledgeKeeper, defaults to 50 pixels (updated if image is loaded). */
    private int height = 50;

    /** The name of the KnowledgeKeeper (e.g., "efe_degismis"). */
    private String name;

    /** The file path for loading questions (e.g., "questions.txt"). */
    private String questionFile;

    /** The file path for loading info (e.g., "info.txt"). */
    private String infoFile;

    /** The text of the last shot fired (question or info). */
    private String lastShotText;

    /** Flag to ensure image loading warnings are shown only once. */
    private static boolean imageWarningShown = false;

    /**
     * Inner class representing a content item (question or info) with text and an optional image.
     */
    private static class ContentItem {
        /** The text content (question or info). */
        String text;

        /** The image associated with the content, or null if not available. */
        BufferedImage image;

        /**
         * Constructs a new ContentItem with the specified text and image.
         *
         * @param text The text content (question or info).
         * @param image The associated image, or null if not applicable.
         */
        public ContentItem(String text, BufferedImage image) {
            this.text = text;
            this.image = image;
        }
    }

    /** List of question content items loaded from the question file. */
    private List<ContentItem> questions;

    /** List of info content items loaded from the info file. */
    private List<ContentItem> info;

    /** Probability of shooting a question (vs. info) */
    private double shootQuestionProb;

    /** Speed of the shotbox when shooting a question (pixels per second), default 300. */
    private double shotboxSpeed = 300;

    /** Damage inflicted by a question shotbox, default 10. */
    private int damage = 10;

    /** Points awarded by an info shotbox, default 10. */
    private int points = 10;

    /** Static image for question shotboxes, loaded once and reused. */
    private static BufferedImage questionImage = null;

    /** Static image for info shotboxes, loaded once and reused. */
    private static BufferedImage infoImage = null;

    /**
     * Constructs a new KnowledgeKeeper with the specified name and content file paths.
     * Initializes the image, loads questions and info, and sets up default images for shotboxes.
     *
     * @param name The name of the KnowledgeKeeper (used for image loading).
     * @param questionFile The file path for loading questions.
     * @param infoFile The file path for loading info.
     */
    public KnowledgeKeeper(String name, String questionFile, String infoFile, double shootQuestionProb) {
        this.name = name;
        this.questionFile = questionFile;
        this.infoFile = infoFile;
        this.shootQuestionProb=shootQuestionProb;
        this.image = loadImage("images/" + name + ".png");
        width = image.getWidth();
        height = image.getHeight();
        
        if (questionImage == null) {
            questionImage = loadImage("images/question.png");
            
        }
        if (infoImage == null) {
            infoImage = loadImage("images/info.png");
        }
        questions = loadContentWithImages(questionFile, true);
        info = loadContentWithImages(infoFile, false);
    }

    /**
     * Creates and returns a new ShotBox (question or info) fired by the KnowledgeKeeper.
     * Randomly selects whether to shoot a question or info based on shootQuestionProb,
     * and chooses a random content item from the appropriate list.
     *
     * @return A new ShotBox instance with position, type, text, speed, value, and image.
     */
    public ShotBox shoot() {
        double shotX = x + width / 2.0 - 10;
        double shotY = y + height;
        boolean isQuestion = Math.random() < shootQuestionProb;
        List<ContentItem> contentList = isQuestion ? questions : info;
        double speed = isQuestion ? shotboxSpeed : shotboxSpeed / 2;
        int value = isQuestion ? damage : points;
        if (contentList.isEmpty()) {
            lastShotText = "No content";
            return new ShotBox(shotX, shotY, isQuestion ? ShotBox.Type.QUESTION : ShotBox.Type.INFO, lastShotText, speed, value, isQuestion ? questionImage : infoImage);
        }
        int index = (int) (Math.random() * contentList.size());
        ContentItem item = contentList.get(index);
        lastShotText = item.text;
        return new ShotBox(shotX, shotY, isQuestion ? ShotBox.Type.QUESTION : ShotBox.Type.INFO, item.text, speed, value, item.image);
    }

    /**
     * Loads content (questions or info) from a file, associating each line with an image.
     *
     * @param filePath The path to the file containing the content.
     * @param isQuestion True if loading questions, false if loading info (affects default image).
     * @return A list of ContentItem objects, or an empty list if the file cannot be read.
     */
    private List<ContentItem> loadContentWithImages(String filePath, boolean isQuestion) {
        List<ContentItem> contentList = new ArrayList<>();
        File file = new File(filePath);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty()) {
                    contentList.add(new ContentItem(line, isQuestion ? questionImage : infoImage));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return contentList;
    }

    /**
     * Gets the text of the last shot fired by the KnowledgeKeeper.
     *
     * @return The last shot text (question or info).
     */
    public String getLastShotText() { 
        return lastShotText; 
    }

    /**
     * Gets the x-coordinate of the KnowledgeKeeper.
     *
     * @return The x-coordinate.
     */
    public double getX() { 
        return x; 
    }

    /**
     * Sets the x-coordinate of the KnowledgeKeeper.
     *
     * @param x The new x-coordinate.
     */
    public void setX(double x) { 
        this.x = x; 
    }

    /**
     * Gets the y-coordinate of the KnowledgeKeeper.
     *
     * @return The y-coordinate.
     */
    public double getY() { 
        return y; 
    }

    /**
     * Sets the y-coordinate of the KnowledgeKeeper.
     *
     * @param y The new y-coordinate.
     */
    public void setY(double y) { 
        this.y = y; 
    }

    /**
     * Gets the width of the KnowledgeKeeper.
     *
     * @return The width in pixels.
     */
    public int getWidth() { 
        return width; 
    }

    /**
     * Gets the height of the KnowledgeKeeper.
     *
     * @return The height in pixels.
     */
    public int getHeight() { 
        return height; 
    }

    /**
     * Gets the bounding rectangle of the KnowledgeKeeper for collision detection.
     *
     * @return The Rectangle representing the KnowledgeKeeper's bounds.
     */
    public Rectangle getBounds() { 
        return new Rectangle((int) x, (int) y, width, height); 
    }

    /**
     * Gets the image representing the KnowledgeKeeper.
     *
     * @return The BufferedImage, or null if not loaded.
     */
    public BufferedImage getImage() { 
        return image; 
    }

    /**
     * Gets the movement speed of the KnowledgeKeeper.
     * Must be implemented by subclasses.
     *
     * @return The speed in pixels per second.
     */
    public abstract double getSpeed();

    /**
     * Gets the probability of the KnowledgeKeeper shooting per frame.
     * Must be implemented by subclasses.
     *
     * @return The shoot probability (0 to 1).
     */
    public abstract double getShootProbability();

    /**
     * Gets the probability of the KnowledgeKeeper moving toward the player.
     * Must be implemented by subclasses.
     *
     * @return The movement toward player probability (0 to 1).
     */
    public abstract double getMovementTowardsPlayerProb();

    /**
     * Gets the type of the KnowledgeKeeper (e.g., SL, TA, Professor).
     * Must be implemented by subclasses.
     *
     * @return The type as a string.
     */
    public abstract String getType();

    /**
     * Loads an image from the specified path.
     * Performs multiple checks for file existence, readability, and validity,
     * logging errors if loading fails.
     *
     * @param path The path to the image file.
     * @return The loaded BufferedImage, or null if loading fails.
     */
    private BufferedImage loadImage(String path) {
        try {
            File file = new File(path);
            BufferedImage img = ImageIO.read(file);
            return img;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}