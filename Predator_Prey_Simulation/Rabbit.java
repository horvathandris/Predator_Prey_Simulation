/**
 * A model of a rabbit.
 * Rabbits age, move, eat plants, breed, and die.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 30;
    // The age to which a rabbit can live.
    private static final int MAX_AVARAGE_AGE = 400;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.5;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number that get's inceremented to the foodLevel of rabbit's predator.
    private static final int FOOD_VALUE = 10;
    // The maximum value of foodLevel that rabbit can have.
    private static final int MAX_FOOD_LEVEL = 25;

    // Individual characteristics (instance fields).

    /**
     * Create a rabbit. A rabbit can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * @param randomAge If true, the rabbit will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {
        // the 4th paramter is false because it is not a carnivore.
        super(randomAge, field, location, false, false);
    }

    /**
     * Create and return a new rabbit that has random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @return Rabbit a new rabbit.
     */
    public Rabbit newAnimal(Field field, Location loc)
    {
        return new Rabbit(false, field, loc);
    }

    /**
     * @return The avarage age to which a rabbit can live.
     */
    public int getMaxAvgAge()
    {
        return MAX_AVARAGE_AGE;
    }

    /**
     * @return The age at which a rabbit can start to breed.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return The likelihood of a rabbit breeding.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return The maximum number of rabit's births.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * @return The food value of a single rabbit.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }

    /**
     * @return The maximum value of foodLevel that rabbit can have.
     */
    public int getMaxFoodLevel()
    {
        return MAX_FOOD_LEVEL;
    }
}
