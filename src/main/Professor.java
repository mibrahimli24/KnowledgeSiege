package main;

/**
 * The Professor class represents a Professor entity in the "Knowledge Siege" game,
 * extending the KnowledgeKeeper base class. Professors are a type of KnowledgeKeeper
 * that appear in Level 3, with specific movement and shooting behavior.
 * They always move toward the player, shoot infrequently, and have a moderate speed.
 */
public class Professor extends KnowledgeKeeper {

    /**
     * Constructs a new Professor with the specified name.
     * Initializes the Professor with question and info files specific to Professors.
     * 0.7 is the initialized shootQuestionProb for Professors
     * @param name The name of the Professor (e.g., "oznur_ozkasap").
     */
    public Professor(String name) {
        super(name, "data/Professor_questions.txt", "data/Professor_info.txt",0.7);
    }

    /**
     * Gets the movement speed of the Professor.
     *
     * @return The speed, 70 pixels per second.
     */
    @Override
    public double getSpeed() { 
        return 70; 
    }

    /**
     * Gets the probability of the Professor shooting a question or info per frame.
     *
     * @return The shoot probability, 0.01 (1% chance per frame).
     */
    @Override
    public double getShootProbability() { 
        return 0.01; 
    }

    /**
     * Gets the probability of the Professor moving toward the player.
     *
     * @return The movement toward player probability, 1 (always moves toward the player).
     */
    @Override
    public double getMovementTowardsPlayerProb() { 
        return 1; 
    }

    /**
     * Gets the type of the KnowledgeKeeper.
     *
     * @return The type, "professor".
     */
    @Override
    public String getType() { 
        return "professor"; 
    }
}