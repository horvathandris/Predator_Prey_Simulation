/**
 * A model of a frog.
 * Frogs age, move, eat grasshoppers, breed, and die.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Frog extends Animal
{
    // Characteristics shared by all frogs (class variables).

    // The age at which a frog can start to breed.
    private static final int BREEDING_AGE = 20;
    // The age to which a frog can live.
    private static final int MAX_AVARAGE_AGE = 230;
    // The likelihood of a frog breeding.
    private static final double BREEDING_PROBABILITY = 0.6;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single frog. In effect, this is the
    // number that get's inceremented to the foodLevel of frog's predator.
    private static final int FOOD_VALUE = 8;
    // The maximum value of foodLevel that frog can have.
    private static final int MAX_FOOD_LEVEL = 70;

    /**
     * Create a frog. A frog can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * @param randomAge If true, the frog will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Frog(boolean randomAge, Field field, Location location)
    {
        super(randomAge, field, location, true, true);
    }

    /**
     * Create and return a new frog that has random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @return Frog a new frog.
     */
    public Frog newAnimal(Field field, Location loc)
    {
        return new Frog(false, field, loc);
    }

    // Accessor methods for frog's private static final fields.

    public int getMaxAvgAge()
    {
        return MAX_AVARAGE_AGE;
    }

    public int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    public double getBreedingProb()
    {
        return BREEDING_PROBABILITY;
    }

    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }

    public int getFoodValue()
    {
        return FOOD_VALUE;
    }

    public int getMaxFoodLevel()
    {
        return MAX_FOOD_LEVEL;
    }
}
