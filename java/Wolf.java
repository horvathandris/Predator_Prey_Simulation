/**
 * A model of a wolf.
 * Wolf age, move, eat coyotes, rabbits, breed, and die.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Wolf extends Animal
{
    // Characteristics shared by all wolves (class variables).

    // The age at which a wolf can start to breed.
    private static final int BREEDING_AGE = 30;
    // The age to which a wolf can live.
    private static final int MAX_AVARAGE_AGE = 3000;
    // The likelihood of a wolf breeding.
    private static final double BREEDING_PROBABILITY = 0.7;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single wolf. In effect, this is the
    // number that get's inceremented to the foodLevel of wolf's predator.
    private static final int FOOD_VALUE = 50;
    // The maximum value of foodLevel that wolf can have.
    private static final int MAX_FOOD_LEVEL = 150;

    /**
     * Create a wolf. A wolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * @param randomAge If true, the wolf will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location, true, true);
    }

    /**
     * Create and return a new wolf that has random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @return Wolf a new wolf.
     */
    public Wolf newAnimal(Field field, Location loc)
    {
        return new Wolf(false, field, loc);
    }

    /**
     * @return The avarage age to which a wolf can live.
     */
    public int getMaxAvgAge()
    {
        return MAX_AVARAGE_AGE;
    }

    /**
     * @return The age at which a wolf can start to breed.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return The likelihood of a wolf breeding.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return The maximum number of wolf's births.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * @return The food value of a single wolf.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }

    /**
     * @return The maximum value of foodLevel that wolf can have.
     */
    public int getMaxFoodLevel()
    {
        return MAX_FOOD_LEVEL;
    }
}
