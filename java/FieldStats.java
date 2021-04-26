import java.awt.Color;
import java.util.Map;
import java.util.HashMap;

/**
 * This class collects and provides some statistical data on the state
 * of a field. It is flexible: it will create and maintain a counter
 * for any class of object that is found within the field.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class FieldStats
{
    // Counters for each type of animal in the simulation.
    private HashMap<Class, Counter> counters;
    // Whether the counters are currently up to date.
    private boolean countsValid;

    /**
     * Construct a FieldStats object.
     */
    public FieldStats()
    {
        // Set up a collection for counters for each type of animal that
        // we might find
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
     * Get the Map with populations and their 
     * number of individuals on the field.
     * @param field The field of the simulation.
     * @return A Map with population and their number on the field.
     */
    public Map<Class, String> getPopulationMap(Field field)
    {
        Map<Class, String> populations = new HashMap<>();
        generateCounts(field);
        for (Class key : counters.keySet()) {
            Counter info = counters.get(key);
            String str = " " + info.getCount();
            populations.put(key, str);
        }
        return populations;
    }

    /**
     * Get the number of individuals in the population of a given class.
     * @param field The field of the simulation.
     * @param key
     * @return An int with the number for this class.
     */
    public int getPopulationCount(Field field, Class key)
    {
        generateCounts(field);
        Counter counter = counters.get(key);
        if (counter != null) {
            return counter.getCount();
        }
        return 0;
    }

    /**
     * Invalidate the current set of statistics; reset all
     * counts to zero.
     */
    public void reset()
    {
        countsValid = false;
        for (Class key : counters.keySet()) {
            Counter count = counters.get(key);
            count.reset();
        }
    }

    /**
     * Increment the count for one class of animal.
     * @param animalClass The class of animal to increment.
     */
    public void incrementCount(Class animalClass)
    {
        Counter count = counters.get(animalClass);
        if(count == null) {
            // We do not have a counter for this species yet.
            // Create one.
            count = new Counter(animalClass.getName());
            counters.put(animalClass, count);
        }
        count.increment();
    }

    /**
     * Indicate that an animal count has been completed.
     */
    public void countFinished()
    {
        countsValid = true;
    }

    /**
     * Determine whether the simulation is still viable.
     * I.e., should it continue to run.
     * @param field The field of the simulation.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        // How many counts are non-zero.
        int nonZero = 0;
        generateCounts(field);
        for(Class key : counters.keySet()) {
            Counter info = counters.get(key);
            if(info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }

    /**
     * Generate counts of the number of foxes and rabbits.
     * These are not kept up to date as foxes and rabbits
     * are placed in the field, but only when a request
     * is made for the information.
     * @param field The field to generate the stats for.
     */
    private void generateCounts(Field field)
    {
        if (!countsValid) {
            reset();
            for(int row = 0; row < field.getDepth(); row++) {
                for(int col = 0; col < field.getWidth(); col++) {
                    Animal animal = field.getAnimalAt(row, col);
                    if(animal != null) {
                        incrementCount(animal.getClass());
                    }
                }
            }
            countsValid = true;
        }
    }
}
