import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * This class is a representation of how organisms depend on
 * each other as food sources.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.19
 */
public class FoodWeb
{
    // A map that holds an aninmal as key and the list of
    // the organisms that they eat as value.
    private Map<Class, List<Class>> foodSources;

    /**
     * Create the map of food sources.
     */
    public FoodWeb()
    {
        foodSources = new HashMap<>();
    }

    /**
     * Return organisms that the animal eats.
     * @param animal An animal
     * @return The list of classes that the animal eats.
     */
    public List getFoodSource(Animal animal)
    {
        return foodSources.get(animal.getClass());
    }

    /**
     * Add the connection between organisms.
     * @param aniaml An animal.
     * @param foods Organisms that are food source for animal.
     */
    public void add(Class animal, Class... foods)
    {
        foodSources.put(animal, new ArrayList<>());
        for (Class food : foods) {
            foodSources.get(animal).add(food);
        }
    }

    /**
     * Return the map of food sources.
     * @return the map of food sources.
     */
    public Map<Class, List<Class>> getMap()
    {
        return foodSources;
    }

    /**
     * Return the list of all animals in the foodweb.
     * @return the list of all animals in the foodweb.
     */
    public List<Class> getAnimalClasses()
    {
        return new ArrayList<>(foodSources.keySet());
    }
}
