import java.lang.Math;

/**
 * A class representing shared characteristics of plants.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Plant extends Organism
{
    // Characteristics shared by all plants (class variables).

    // The rate in which the plant grows.
    private static final double GROW_RATE = 1.1;
    // The maximum growth level that a plant can reach.
    private static final int MAX_GROW_LEVEL = 30;
    // The minimum growth level from which the plant is visible.
    private static final int MIN_VISIBLE_GROW_LEVEL = 15;
    // The food value of a single plant. In effect, this is the
    // number that get's inceremented to the foodLevel of herbivorous animals.
    private static final int FOOD_VALUE = 1;
    // The current growth level of a plant.
    private double growLevel;
    // If true, the plant will be visible on the field and eatable by animals.
    private boolean isVisible;

    /**
     * Create a new Plant at location of the field.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location)
    {
        // False indicates that all plants are initialized
        // on the bottom level of their 2D block.
        super(false, field, location);
        // set random growth level
        growLevel = rand.nextInt(MAX_GROW_LEVEL+1)+1;
        checkIfVisible();
    }

    /**
     * Increase the growth level by the grow rate.
     * If it is raining the grow rate in which the plant 
     * grows is additionally increased.
     * @param isRaining true if it is currently raining
     */
    public void grow(boolean isRaining)
    {
        if (isRaining) {
            growLevel  *= 2;
        }
        growLevel *= GROW_RATE;
        // the growLevel cannot be greater than the MAX_GROW_LEVEL
        if (growLevel >= MAX_GROW_LEVEL) growLevel = MAX_GROW_LEVEL;
        checkIfVisible();
    }

    /**
     * Set the visibility of the plant according to it's growth level.
     */
    public void checkIfVisible()
    {
        if (growLevel >= MIN_VISIBLE_GROW_LEVEL) {
            isVisible = true;
        }
        else {
            isVisible = false;
        }
    }

    /**
     * Return the food value of the plant.
     * @return int - The food value of the plant.
     */
    public int getFoodValue()
    {
        return FOOD_VALUE;
    }

    /**
     * Reset the growth level of a plant (i.e. if it gets eaten).
     */
    public void resetGrowLevel()
    {
        // set growLevel to a double between 4.0 and 5.0
        growLevel = Math.round((4 + rand.nextDouble()) * 10) / 10.0;
        checkIfVisible();
    }

    /**
     * Returun whether plant is visible on the field.
     * @return boolean - Whether the plant can be eaten by an animal,
     *                   therefore should be distinct from the not 
     *                   eatable plants on the field.
     */
    public boolean isEatable()
    {
        return isVisible;
    }
}
