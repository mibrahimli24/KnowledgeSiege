package main;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Player class represents the controllable player in the "Knowledge Siege" game.
 * It manages the player's position, movement speed, and image, with methods for updating
 * position and detecting collisions. The player's movement is constrained within the
 * game panel's boundaries, using the time step (DT) from GamePanel.
 */
public class Player {
    /** The x-coordinate of the player. */
    private double x;

    /** The y-coordinate of the player. */
    private double y;

    /** The horizontal speed of the player (pixels per second). */
    private double dx;

    /** The image representing the player, or null if not loaded. */
    private BufferedImage image;

    /** The constant speed of the player, set to 200 pixels per second. */
    private static final double SPEED = 200;

    /**
     * Constructs a new Player at the specified position with the given image.
     *
     * @param x The initial x-coordinate of the player.
     * @param y The initial y-coordinate of the player.
     * @param image The BufferedImage representing the player, or null if not available.
     */
    public Player(double x, double y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    /**
     * Updates the player's position based on its horizontal speed, constrained within
     * the game panel's boundaries. Uses the time step (DT) from GamePanel for smooth movement.
     */
    public void update() {
        x += dx * GamePanel.DT;
        if (x < 0) x = 0;
        if (image != null) {
            if (x > GamePanel.WIDTH - image.getWidth()) x = GamePanel.WIDTH - image.getWidth();
        } else {
            if (x > GamePanel.WIDTH - 50) x = GamePanel.WIDTH - 50;
        }
    }

    /**
     * Gets the x-coordinate of the player.
     *
     * @return The x-coordinate.
     */
    public double getX() { 
        return x; 
    }

    /**
     * Gets the y-coordinate of the player.
     *
     * @return The y-coordinate.
     */
    public double getY() { 
        return y; 
    }

    /**
     * Gets the image representing the player.
     *
     * @return The BufferedImage of the player, or null if no image is loaded.
     */
    public BufferedImage getImage() { 
        return image; 
    }

    /**
     * Sets the player's horizontal speed.
     *
     * @param dx The horizontal speed (pixels per second), positive for right, negative for left.
     */
    public void setDx(double dx) { 
        this.dx = dx; 
    }

    /**
     * Gets the player's constant speed.
     *
     * @return The speed value (200 pixels per second).
     */
    public double getSpeed() { 
        return SPEED; 
    }

    /**
     * Gets the player's bounding rectangle for collision detection.
     * Uses the image dimensions if available, otherwise defaults to 50x50 pixels.
     *
     * @return The Rectangle representing the player's bounds.
     */
    public Rectangle getBounds() {
        int w = image != null ? image.getWidth() : 50;
        int h = image != null ? image.getHeight() : 50;
        return new Rectangle((int) x, (int) y, w, h);
    }
}