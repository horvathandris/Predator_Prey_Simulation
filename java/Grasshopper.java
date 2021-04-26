/**
 * A model of a grasshopper.
 * Grasshopper age, move, eat plants, breed, and die.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Grasshopper extends Animal
{
    // Characteristics shared by all grasshoppers (class variables).

    // The age at which a grasshopper can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a grasshopper can live.
    private static final int MAX_AVARAGE_AGE = 60;
    // The likelihood of a grasshopper breeding.
    private static final double BREEDING_PROBABILITY = 0.5;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 4;
    // The food value of a single grasshopper. In effect, this is the
    // number that get's inceremented to the foodLevel of grasshopper's predator.
    private static final int FOOD_VALUE = 4;
    // The maximum value of foodLevel that grasshopper can have.
    private static final int MAX_FOOD_LEVEL = 15;

    /**
     * Create a grasshopper. A grasshopper can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * @param randomAge If true, the grasshopper will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Grasshopper(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location, false, false);
    }

    /**
     * Create and return a new grasshopper that has random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @return Grasshopper a new grasshopper.
     */
    public Grasshopper newAnimal(Field field, Location loc)
    {
        return new Grasshopper(false, field, loc);
    }

    /**
     * @return The avarage age to which a grasshopper can live.
     */
    public int getMaxAvgAge()
    {
        return MAX_AVARAGE_AGE;
    }

    /**
     * @return The age at which a grasshopper can start to breed.
     */
    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * @return The likelihood of a grasshopper breeding.
     */
    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }

    /**
     * @return The maximum number of grasshopper's births.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    /**
     * @return The food value of a single grasshopper.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }

    /**
     * @return The maximum value of foodLevel that grasshopper can have.
     */
    public int getMaxFoodLevel()
    {
        return MAX_FOOD_LEVEL;
    }
}
