package main;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Formatter;

/**
 * The User class represents a user in the "Knowledge Siege" game.
 * It stores the user's username, hashed password, and selected player image.
 * The class also provides static methods for hashing passwords and managing
 * user persistence by loading and saving users to a file (data/users.txt).
 */
public class User {
    /** The username of the user. */
    private String username;

    /** The hashed password of the user, using SHA-256. */
    private String passwordHash;

    /** The filename of the selected player image (e.g., "player1.png"), or null if not selected. */
    private String selectedPlayerImage;

    /**
     * Constructs a new User with the specified username and hashed password.
     *
     * @param username The username of the user.
     * @param passwordHash The hashed password of the user (SHA-256).
     */
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() { 
        return username; 
    }

    /**
     * Gets the hashed password of the user.
     *
     * @return The hashed password (SHA-256).
     */
    public String getPasswordHash() { 
        return passwordHash; 
    }

    /**
     * Gets the filename of the selected player image.
     *
     * @return The player image filename (e.g., "player1.png"), or null if not selected.
     */
    public String getSelectedPlayerImage() { 
        return selectedPlayerImage; 
    }

    /**
     * Sets the filename of the selected player image.
     *
     * @param selectedPlayerImage The player image filename (e.g., "player1.png").
     */
    public void setSelectedPlayerImage(String selectedPlayerImage) { 
        this.selectedPlayerImage = selectedPlayerImage; 
    }

    /**
     * Hashes a password using the SHA-256 algorithm.
     *
     * @param password The password to hash.
     * @return The hexadecimal string representation of the hashed password.
     * @throws RuntimeException If the SHA-256 algorithm is not available.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    /**
     * Loads the list of users from the file data/users.txt.
     * Each line in the file is expected to contain a username and hashed password,
     * separated by a comma (e.g., "username,hashedpassword").
     *
     * @return A list of User objects loaded from the file, or an empty list if the file
     *         does not exist or cannot be read.
     */
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File("data/users.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return users;
    }

    /**
     * Saves the list of users to the file data/users.txt.
     * Each user is written as a line containing their username and hashed password,
     * separated by a comma (e.g., "username,hashedpassword").
     *
     * @param users The list of users to save.
     */
    public static void saveUsers(List<User> users) {
        try (Formatter formatter = new Formatter(new FileWriter("data/users.txt"))) {
            for (User user : users) {
                formatter.format("%s,%s%n", user.getUsername(), user.getPasswordHash());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}