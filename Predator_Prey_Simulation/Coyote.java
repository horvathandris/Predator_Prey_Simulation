/**
 * A model of a coyote.
 * coyotes age, move, eat frogs, rabbits and rats, breed and die.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Coyote extends Animal
{
    // Characteristics shared by all coyotes (class variables).

    // The age at which a coyote can start to breed.
    private static final int BREEDING_AGE = 30;
    // The age to which a coyote can live.
    private static final int MAX_AVARAGE_AGE = 2000;
    // The likelihood of a coyote breeding.
    private static final double BREEDING_PROBABILITY = 0.6;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single coyote. In effect, this is the
    // number that get's inceremented to the foodLevel of coyote's predator.
    private static final int FOOD_VALUE = 35;
    // The maximum value of foodLevel that coyote can have.
    private static final int MAX_FOOD_LEVEL = 100;

    /**
     * Create a coyote. A coyote can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * @param randomAge If true, the coyote will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Coyote(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location, true, true);
    }

    /**
     * Create and return a new coyote that has random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @return coyote a new coyote.
     */
    public Coyote newAnimal(Field field, Location loc)
    {
        return new Coyote(false, field, loc);
    }

    /**
     * @return The avarage age to which a coyote can live.
     */
    public int getMaxAvgAge()
    {
        return MAX_AVARAGE_AGE;
    }

    /**
     * @return The age at which a coyote can start to breed.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return The likelihood of a coyote breeding.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return The maximum number of coyote's births.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * @return The food value of a single coyote.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }

    /**
     * @return The maximum value of foodLevel that coyote can have.
     */
    public int getMaxFoodLevel()
    {
        return MAX_FOOD_LEVEL;
    }
}
