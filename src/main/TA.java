package main;

/**
 * The TA class represents a Teaching Assistant (TA) entity in the "Knowledge Siege" game,
 * extending the KnowledgeKeeper base class. TAs are a type of KnowledgeKeeper that appear
 * in Levels 2 and 3, with specific movement and shooting behavior. They move toward the
 * player with a moderate probability, shoot occasionally, and have an intermediate speed.
 */
public class TA extends KnowledgeKeeper {

    /**
     * Constructs a new TA with the specified name.
     * Initializes the TA with question and info files specific to Teaching Assistants.
     * 0.5 is the initialized shootQuestionProb for TAs
     * @param name The name of the TA (e.g., "vahideh_hayyolalam").
     */
    public TA(String name) {
        super(name, "data/TA_questions.txt", "data/TA_info.txt",0.5);
    }

    /**
     * Gets the movement speed of the TA.
     *
     * @return The speed, 60 pixels per second.
     */
    @Override
    public double getSpeed() { 
        return 60; 
    }

    /**
     * Gets the probability of the TA shooting a question or info per frame.
     *
     * @return The shoot probability, 0.007 (0.7% chance per frame).
     */
    @Override
    public double getShootProbability() { 
        return 0.007; 
    }

    /**
     * Gets the probability of the TA moving toward the player.
     *
     * @return The movement toward player probability, 0.4 (40% chance per frame).
     */
    @Override
    public double getMovementTowardsPlayerProb() { 
        return 0.4; 
    }

    /**
     * Gets the type of the KnowledgeKeeper.
     *
     * @return The type, "ta".
     */
    @Override
    public String getType() { 
        return "ta"; 
    }
}