import java.util.List;
import java.util.Random;
import java.util.Iterator;
import java.lang.Math;
import java.util.LinkedList;

/**
 * A class representing shared characteristics of animals.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public abstract class Animal extends Organism
{
    // The animal's age.
    protected int age;
    // The animal's sex. True if it's a male.
    protected boolean isMale;
    // True if the animal is meat-eater, false if it is a plant-eater.
    protected boolean isCarnivore;
    // The food level of the animal, which is increased by eating.
    protected int foodLevel;
    // Amount of steps animal needs to wait before it breads again.
    protected int stepsUntilBreed;
    // Boolean value of wether an animal is active during night.
    protected boolean isNocturnal;
    // Disaese that animal has on it.
    protected Disease disease;

    /**
     * Create a new animal at location in field.
     * @param randomAge The age of the animal.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param isCarnivore True if the animal is meat-eater,
     *                    false if it is a plant-eater.
     * @param isNocturnal True if the animal is active at night
                          and sleeps at daytime.
     */
    public Animal(boolean randomAge, Field field, Location location, boolean isCarnivore, boolean isNocturnal)
    {
        // True indicates that all animals are initialized
        // on the top level of their 2D block.
        super(true, field, location);
        isMale = rand.nextBoolean();
        this.isCarnivore = isCarnivore;
        this.isNocturnal = isNocturnal;
        disease = null;

        if (randomAge) {
            age = rand.nextInt(getMaxAvgAge());
            foodLevel = rand.nextInt(getMaxFoodLevel());
            setStepsUntilBreed();
        }
        else {
            age = 0;
            foodLevel = getMaxFoodLevel() / 2;
        }
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     * @param foodWeb The map representation of the ecosystem's food web.
     * @param rand A randomizer for calculating probabilities.
     * @param isFoggy If it is foggy predators act differently.
     * @param time
     */
     public void act(List<Animal> newAnimals, FoodWeb foodWeb, Random rand, boolean isFoggy, Time time)
     {
        incrementAge(rand);

        if (disease != null && rand.nextDouble() <= disease.getLethalProbability()) {
            // dies from disease
            setDead();
        }
        else if (disease != null && rand.nextDouble() <= disease.getHealProbability()) {
            // recoveres from disease.
            disease = null;
        }

        if (asleep(time));   //sleep
        else {
            incrementHunger();
            if (isAlive()) {
                if(canBreed()) {
                    giveBirth(newAnimals);
                }
                Location newLocation = null;
                if (isHungry()) {
                    newLocation = findFood(foodWeb, isFoggy);
                }
                if(newLocation == null) {
                    // No food found - try to move to a free location.
                    newLocation = getField().freeAdjacentLocation(getLocation());
                }
                // See if it was possible to move.
                if(newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                     // Overcrowding.
                     setDead();
                }
            }
        }
    }

    /**
     * Look for food source adjacent to the current location.
     * Only the first live food source is eaten.
     * @param foodWeb The map representation of the ecosystem's food web.
     * @param isFoggy True if it is currently foggy.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(FoodWeb foodWeb, boolean isFoggy)
    {
        Field field = getField();
        List<Location> adjacent = new LinkedList<>();

        // Removing couple of adjacent locations the predator can move to
        // when it's foggy. We remove a first half of the list since
        // the adjacent locations are already randomized.
        if (isFoggy && isCarnivore) {
            adjacent = field.adjacentLocations(getLocation());
            adjacent.subList(0, adjacent.size() / 2).clear();
        }
        else if (!isCarnivore) {
            adjacent = field.getFreeAdjacentLocations(getLocation());
            adjacent.add(0, getLocation());  // Add the location herbivorous animal stand on.
        }
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Organism organism = null;
            if (isCarnivore) {
                organism = field.getAnimalAt(where); // Get the animal
            }
            else {
                organism = field.getPlantAt(where); // Get the plant
            }
            // Check if the organism is not null and that this animal eats it.
            if (organism != null && foodWeb.getFoodSource(this).contains(organism.getClass())) {
                if (isCarnivore && organism.isAlive()) {
                    Animal prey = (Animal) organism;
                    // If the prey is infected, the disease can spread to the predator.
                    boolean getsInfected = prey.isInfected() && prey.getDisease().doesSpreadThroughFood();
                    if (getsInfected && rand.nextDouble() <= prey.getDisease().getTransmissionProbability()) {
                        this.setDisease(prey.getDisease());
                    }
                    organism.setDead(); // Prey dies.
                    foodLevel = organism.getFoodValue();
                    return where;
                }
                else if (!isCarnivore) {
                    Plant plant = (Plant) organism;
                    if (plant.isEatable()) {
                        plant.resetGrowLevel();
                        foodLevel += plant.getFoodValue();
                        return where;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this animal is to give birth at this step.
     * New births will be made into free adjacent locations.
     * This method checks only if females have a mate around them to prevent
     * "double" mating by the same pair of ainmals.
     * @param newAnimals A list to add newly born animals to.
     */
    protected void giveBirth(List<Animal> newAnimals)
    {
        // New animals are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        Animal mate = hasMateNear(getLocation());
        if(!isMale() &&  mate != null)
        {
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            if (births > 0) {
                // If a female is infected then there is a big chance her
                // mate will get her disease.
                if (isInfected() && disease.doesSpreadThroughBreeding()) {
                    if (rand.nextDouble() <= disease.getTransmissionProbability()) {
                        mate.setDisease(disease);
                    }
                }
                // If a male is infected then there is a big chance his
                // mate will get his disease.
                else if (mate.isInfected() && mate.getDisease().doesSpreadThroughBreeding()) {
                    if (rand.nextDouble() <= mate.getDisease().getTransmissionProbability()) {
                        setDisease(mate.getDisease());
                    }
                }

                setStepsUntilBreed();
            }
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Animal young = newAnimal(field, loc);
                // If female is infected the children will be infected as well.
                if (isInfected() || mate.isInfected()) {
                    young.setDisease((isInfected()) ? disease : mate.getDisease());
                }
                newAnimals.add(young);
            }
        }
    }


    /**
     * Checks and returns whether there is a mate of an opposite gender nearby.
     * @param location The location of the animal looking for a mate.
     * @return boolean - Whether there is a mate of opposite sex near.
     *                   the location provided.
     */
    public Animal hasMateNear(Location location)
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(location);
        for(Location next : adjacent) {
            Animal animal = field.getAnimalAt(next);
            if(animal != null) {
                if(animal.getClass().equals(getClass()) && ((Animal) animal).isMale() != isMale()) {
                    return animal;
                }
            }
        }
        return null;
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if (rand.nextDouble() <= getBreedingProb()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    /**
     * Seting steps until animal can bread again.
     */
    protected void setStepsUntilBreed()
    {
        stepsUntilBreed = rand.nextInt((int) Math.round(0.04 * getMaxAvgAge()));
    }

    /**
     * Increase the age. This could result in the animals's death.
     * Not all animals from the same specie live the same time.
     * The maximum age is randomized.
     * @param rand Randomizer to randomize dieAge.
     */
    protected void incrementAge(Random rand)
    {
        age++;
        stepsUntilBreed--;
        int maxAvgAge = getMaxAvgAge();
        int minDieAge = (int) Math.round(0.8 * maxAvgAge);
        int maxDieAge = (int) Math.round(1.2 * maxAvgAge);
        int dieAge = rand.nextInt((maxDieAge - minDieAge) + 1) + minDieAge;
        if(age > dieAge) {
            setDead();
        }
    }

    /**
    * Set the location of the animal to the new location provided.
    * @param newLocation The new location to be set.
    */
    protected void setLocation(Location newLocation)
    {
        super.setLocation(newLocation, true);
    }

    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Set a new disease animal has.
     * @param newDisease New disease that animal got infected with.
     */
    protected void setDisease(Disease newDisease)
    {
        disease = newDisease;
    }

    /**
     * @return boolean - Whether the animal is able to reproduce.
     */
    protected boolean canBreed()
    {
        return (age >= getBreedingAge() && stepsUntilBreed <= 0);
    }

    /**
     * Check if an animal is hungry.
     * @return True if an animal is hungry.
     */
    protected boolean isHungry()
    {
        return (foodLevel <= (0.6 * getMaxFoodLevel()));
    }

    /**
     * Check if an animal is infected.
     * @return True if an animal is infected.
     */
    protected boolean isInfected()
    {
        return disease != null;
    }

    /**
     * @return True if an animal is sleeping.
     */
    protected boolean asleep(Time time)
    {
        return (!isNocturnal || !time.isNight()) && (isNocturnal || time.isNight());
    }

    /**
     * @return A disease that animal is infected with.
     *         Null if it is not infected.
     */
    protected Disease getDisease()
    {
        return disease;
    }

    /**
     * @return Whether the animal is a male.
     */
    protected boolean isMale()
    {
        return isMale;
    }

    /**
     * @return An animal's age.
     */
    public int getAge()
    {
        return age;
    }

    /**
     * @return An animal's current food level.
     */
    public int getFoodLevel()
    {
        return foodLevel;
    }

    /**
     * Accessor method for getting the animal's maximum age.
     */
    abstract public int getMaxAvgAge();

    /**
     * Accessor method for getting the animal's breeding age.
     */
    abstract public int getBreedingAge();

    /**
     * Accessor method for getting the animal's
     * probability of breeding.
     */
    abstract public double getBreedingProb();

    /**
     * Accessor method for getting the animal's
     * maximum litter size.
     */
    abstract public int getMaxLitterSize();

    /**
     * Accessor method for getting the animal's maximum food level.
     */
    abstract public int getMaxFoodLevel();

    /**
     * Create a new Animal type object.
     * @param field - The field that the animal is created onto.
     * @param loc - Location of the animal on the field.
     * @return Animal - The Animal object that has been created.
     */
    abstract public Animal newAnimal(Field field, Location loc);
}
