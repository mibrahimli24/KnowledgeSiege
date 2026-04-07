package main;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The ShotBox class represents a projectile-like entity in the "Knowledge Siege" game,
 * fired by KnowledgeKeepers. It can be either a QUESTION (damages the player) or INFO
 * (awards points). The ShotBox moves downward at a specified speed and has an associated
 * image for rendering.
 */
public class ShotBox {
    /** The x-coordinate of the ShotBox. */
    private double x;

    /** The y-coordinate of the ShotBox. */
    private double y;

    /** The type of the ShotBox (QUESTION or INFO). */
    private Type type;

    /** The text associated with the ShotBox (e.g., the question or info content). */
    private String text;

    /** The vertical speed of the ShotBox (pixels per second). */
    private double speed;

    /** The value of the ShotBox (damage for QUESTION, points for INFO). */
    private int value;

    /** The image representing the ShotBox, or null if not loaded. */
    private BufferedImage image;

    /**
     * Enum defining the possible types of a ShotBox.
     */
    public enum Type {
        /** Represents a question ShotBox, which damages the player upon collision. */
        QUESTION,
        
        /** Represents an info ShotBox, which awards points to the player upon collision. */
        INFO
    }

    /**
     * Constructs a new ShotBox with the specified properties.
     *
     * @param x The initial x-coordinate of the ShotBox.
     * @param y The initial y-coordinate of the ShotBox.
     * @param type The type of the ShotBox (QUESTION or INFO).
     * @param text The text associated with the ShotBox (e.g., the question or info content).
     * @param speed The vertical speed of the ShotBox (pixels per second).
     * @param value The value (damage for QUESTION, points for INFO).
     * @param image The BufferedImage representing the ShotBox, or null if not available.
     */
    public ShotBox(double x, double y, Type type, String text, double speed, int value, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.text = text;
        this.speed = speed;
        this.value = value;
        this.image = image;
    }

    /**
     * Updates the ShotBox's position by moving it downward based on its speed.
     * Uses the time step (DT) from GamePanel for smooth movement.
     */
    public void update() {
        y += speed * GamePanel.DT;
    }

    /**
     * Gets the x-coordinate of the ShotBox.
     *
     * @return The x-coordinate.
     */
    public double getX() { 
        return x; 
    }

    /**
     * Gets the y-coordinate of the ShotBox.
     *
     * @return The y-coordinate.
     */
    public double getY() { 
        return y; 
    }

    /**
     * Gets the type of the ShotBox.
     *
     * @return The type (QUESTION or INFO).
     */
    public Type getType() { 
        return type; 
    }

    /**
     * Gets the text associated with the ShotBox.
     *
     * @return The text (e.g., the question or info content).
     */
    public String getText() { 
        return text; 
    }

    /**
     * Gets the damage value of the ShotBox if it is a QUESTION type.
     *
     * @return The damage value if type is QUESTION, 0 otherwise.
     */
    public int getDamage() { 
        return type == Type.QUESTION ? value : 0; 
    }

    /**
     * Gets the points value of the ShotBox if it is an INFO type.
     *
     * @return The points value if type is INFO, 0 otherwise.
     */
    public int getPoints() { 
        return type == Type.INFO ? value : 0; 
    }

    /**
     * Gets the bounding rectangle of the ShotBox for collision detection.
     * The rectangle is fixed at 20x20 pixels.
     *
     * @return The Rectangle representing the ShotBox's bounds.
     */
    public Rectangle getBounds() { 
        return new Rectangle((int) x, (int) y, 20, 20); 
    }

    /**
     * Gets the image representing the ShotBox.
     *
     * @return The BufferedImage, or null if no image is loaded.
     */
    public BufferedImage getImage() { 
        return image; 
    }
}