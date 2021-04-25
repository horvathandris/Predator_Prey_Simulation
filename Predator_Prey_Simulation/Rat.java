/**
 * A model of a rat.
 * Rats age, move, eat grasshoppers, breed, and die.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Rat extends Animal
{
    // Characteristics shared by all rats (class variables).

    // The age at which a rat can start to breed.
    private static final int BREEDING_AGE = 30;
    // The age to which a rat can live.
    private static final int MAX_AVARAGE_AGE = 300;
    // The likelihood of a rat breeding.
    private static final double BREEDING_PROBABILITY = 0.7;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single rat. In effect, this is the
    // number that get's inceremented to the foodLevel of rat's predator.
    private static final int FOOD_VALUE = 8;
    // The maximum value of foodLevel that rat can have.
    private static final int MAX_FOOD_LEVEL = 70;

    // Individual characteristics (instance fields).
    /**
     * Create a rat. A rat can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * @param randomAge If true, the rat will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rat(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location, true, false);
    }

    /**
     * Create and return a new rat that has random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @return Rat a new rat.
     */
    public Rat newAnimal(Field field, Location loc)
    {
        return new Rat(false, field, loc);
    }

    /**
     * @return The avarage age to which a rat can live.
     */
    public int getMaxAvgAge()
    {
        return MAX_AVARAGE_AGE;
    }

    /**
     * @return The age at which a rat can start to breed.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return The likelihood of a rat breeding.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return The maximum number of rat's births.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * @return The food value of a single rat.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }

    /**
     * @return The maximum value of foodLevel that rat can have.
     */
    public int getMaxFoodLevel()
    {
        return MAX_FOOD_LEVEL;
    }
}
