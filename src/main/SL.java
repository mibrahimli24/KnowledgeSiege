package main;

/**
 * The SL class represents a Section Leader (SL) entity in the "Knowledge Siege" game,
 * extending the KnowledgeKeeper base class. SLs are a type of KnowledgeKeeper that appear
 * in Levels 1 and 2, with specific movement and shooting behavior. They move randomly (not
 * toward the player), shoot very infrequently, and have a slower speed compared to other
 * KnowledgeKeepers.
 */
public class SL extends KnowledgeKeeper {

    /**
     * Constructs a new SL with the specified name.
     * Initializes the SL with question and info files specific to Section Leaders.
     * 0.3 is the initialized shootQuestionProb for SLs
     * @param name The name of the SL (e.g., "efe_degismis").
     */
    public SL(String name) {
        super(name, "data/SL_questions.txt", "data/SL_info.txt",0.3);
    }

    /**
     * Gets the movement speed of the SL.
     *
     * @return The speed, 50 pixels per second.
     */
    @Override
    public double getSpeed() { 
        return 50; 
    }

    /**
     * Gets the probability of the SL shooting a question or info per frame.
     *
     * @return The shoot probability, 0.005 (0.5% chance per frame).
     */
    @Override
    public double getShootProbability() { 
        return 0.005; 
    }

    /**
     * Gets the probability of the SL moving toward the player.
     *
     * @return The movement toward player probability, 0 (always moves randomly).
     */
    @Override
    public double getMovementTowardsPlayerProb() { 
        return 0; 
    }

    /**
     * Gets the type of the KnowledgeKeeper.
     *
     * @return The type, "sl".
     */
    @Override
    public String getType() { 
        return "sl"; 
    }
}