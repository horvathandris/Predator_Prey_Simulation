import java.util.Random;

/**
 * Organism is a living object in the simulation. It can be eiter an
 * an animal or a plant. It holds the shared characteristics between them.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public abstract class Organism
{
    // Whether the organism is alive or not.
    private boolean alive;
    // The organism's field.
    private Field field;
    // The organism's position in the field.
    private Location location;

    // A shared random number generator to control breeding.
    protected static final Random rand = Randomizer.getRandom();

    /**
     * Create a new organism at location in the field.
     * @param isAnimal Boolean value of wether the organism is an animal.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Organism(boolean isAnimal, Field field, Location location)
    {
        alive = true;
        this.field = field;
        setLocation(location, isAnimal);
    }

    /**
     * Place the organism at the new location in the given field.
     * If an organism is an animal (isAnimal is true) then it is
     * placed on the top layer in the field. If not it is a plant
     * and it is placed on the bottom layer.
     * @param newLocation The organism's new location.
     * @param topObject Boolean value of wether an organism should 
     *                  be place on the top layer in the field.
     */
    protected void setLocation(Location newLocation, boolean topObject)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(topObject, this, newLocation);
    }

    /**
     * Indicate that the organism is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return whether the organism is alive or not.
     * @return true if the organism is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }

    /**
     * Return the organism's location.
     * @return The organism's location.
     */
    protected Location getLocation()
    {
        return location;
    }

    /**
     * Return the organinsm's field.
     * @return The organism's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Accessor method for getting the food value of an organism.
     * @return the food calue of an organism
     */
    protected abstract int getFoodValue();
}
