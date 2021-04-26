import java.util.Collections;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
import java.lang.reflect.Constructor;

/**
 * A predator-prey simulator, based on a rectangular field
 * containing organinsms from a small food chain.
 * The organisms are: plants, rabbits, grasshoppers, frogs, rats,
                      coyotes and wolves.
 *
 *
 * @author  Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.19
 */
public class Simulator
{
    // Map that holds probabilities of certain animal to be created on the first populate() method.
    private static final Map<Class, Double> CREATION_PROBABILITIES = setCreationProbabilities();
    // Custom animals colors.
    private static final Color VERY_LIGHT_GREY = new Color(230, 230, 230);
    private static final Color YELLOW = new Color(253, 212, 77);
    private static final Color ORANGE = new Color(255, 148, 54);
    private static final Color PINK = new Color(255, 184, 233);

    private static final Random rand = Randomizer.getRandom();
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 90;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 70;

    // A graphical view of the simulation.
    private List<SimulatorView> views;
    // List of animal classes.
    private List<Class> animalClasses;
    // List of animals in the field.
    private List<Animal> animals;
    // List of animals in the field.
    private List<Plant> plants;
    // The food web run in this simulation.
    private FoodWeb foodWeb;
    // The Current weather in the simulation.
    private Weather weather;
    // The current state of the field.
    private Field field;
    // The current time of the simulation.
    private Time time;
    // The current step of the simulation.
    private int step;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        field = new Field(depth, width);
        plants = new ArrayList<>();
        animals = new ArrayList<>();
        time = new Time();
        weather = new Weather(time);

        createFoodWeb(); // Create connections between animals.
        animalClasses = foodWeb.getAnimalClasses();

        views = new ArrayList<>();
        // Create a view of the state of each location in the field.
        SimulatorView view = new GraphView(500, 150, 500);
        setColors(view);
        views.add(view);
        view = new GridView(depth, width, views.get(0), this);
        setColors(view);
        views.add(view);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        ((GridView) views.get(1)).oneStepButtonSetEnabled(false);
        for(int step = 1; step <= numSteps && views.get(0).isViable(field); step++) {
            simulateOneStep();
            delay(70);   // Comment this out to make simulation run faster.
        }
        ((GridView) views.get(1)).oneStepButtonSetEnabled(true);
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * orgainsm.
     */
    public void simulateOneStep()
    {
        time.incrementTime();
        weather.decrementDuration();

        step++;

        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();

        // Let all plants grow.
        for (Plant plant : plants) {
            plant.grow(weather.isRaining());
        }

        boolean isFoggy = weather.isFoggy();
        // Let all animals act.
        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals, foodWeb, rand, isFoggy, time);
            if (!animal.isAlive()) {
                it.remove();
            }
        }
        // We pick a random animal on the field and randomize
        // the chance of it getting a new disease.
        if (rand.nextDouble() <= Disease.getProbability()) {
            Animal diseasedAnimal = animals.get(rand.nextInt(animals.size()));
            diseasedAnimal.setDisease(new Disease());
        }

        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);
        updateViews();
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        animals.clear();
        time.reset();
        weather.reset();
        for (SimulatorView view : views) {
            view.reset();
        }

        populate();

        // Show the starting state in the view.
        updateViews();

    }

    /**
     * Update all existing views.
     */
    private void updateViews()
    {
        for (SimulatorView view : views) {
            view.showStatus(step, time, weather, field);
        }
    }

    /**
     * Randomly populate the field with ainmals.
     * The whole field is also covered in plants, from the start.
     */
    private void populate()
    {
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                Plant plant = new Plant(field, location);
                plants.add(plant);

                boolean isCreated = false;
                int index = 0;

                Animal animal = null;

                // Shuffle the animal classes before picking the class from which the animal will be created.
                Collections.shuffle(animalClasses, rand);

                // Try to create an animal at this location randomly.
                while (!isCreated && index < animalClasses.size()) {
                    try {
                        Class animalClass = animalClasses.get(index);
                        double creationProbability = CREATION_PROBABILITIES.get(animalClass);
                        if (rand.nextDouble() <= creationProbability) {
                            // Create a new instance of whichever animal gets chosen by the probability.
                            Constructor animalConstructor = animalClass.getConstructor(boolean.class, Field.class, Location.class);
                            animal = (Animal) animalConstructor.newInstance(true, field, location);
                            animals.add(animal);
                            isCreated = true;
                        }
                    }
                    catch (IllegalAccessException e) {
                        System.out.println(e);
                    }
                    catch (InstantiationException e) {
                        System.out.println(e);
                    }
                    catch (NoSuchMethodException e) {
                        System.out.println(e);
                    }
                    catch (java.lang.reflect.InvocationTargetException e) {
                        System.out.println(e);
                    }
                    index++;
                }
            }
        }
    }

    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }

    /**
     * Create a foodweb with organisms and add the connections between them.
     */
    private void createFoodWeb()
    {
        foodWeb = new FoodWeb();

        // The first parameter in the foodweb.add method is the
        // animal that is added to the foodweb and the classes
        // after it are organisms that it eats.

        // In this case wolves eat coyotes and rabbits.
        foodWeb.add(Wolf.class, Coyote.class, Rabbit.class);

        foodWeb.add(Coyote.class, Rabbit.class, Rat.class, Frog.class);

        foodWeb.add(Frog.class, Grasshopper.class);

        foodWeb.add(Rat.class, Grasshopper.class);

        foodWeb.add(Rabbit.class, Plant.class);

        foodWeb.add(Grasshopper.class, Plant.class);
    }

    /**
     * Set probability for an animal to be created  during the method populate().
     */
    private static Map<Class, Double> setCreationProbabilities()
    {
        Map<Class, Double> creationProbabilities = new HashMap<>();
        creationProbabilities.put(Rabbit.class, 0.065);
        creationProbabilities.put(Coyote.class, 0.04);
        creationProbabilities.put(Wolf.class, 0.024);
        creationProbabilities.put(Grasshopper.class, 0.23);
        creationProbabilities.put(Frog.class, 0.05);
        creationProbabilities.put(Rat.class, 0.05);

        return creationProbabilities;
    }

    /**
     * Set color that is displayed on the field to each animal.
     */
    private void setColors(SimulatorView view)
    {
        view.setColor(Rabbit.class, VERY_LIGHT_GREY);
        view.setColor(Coyote.class, ORANGE);
        view.setColor(Wolf.class, Color.BLACK);
        view.setColor(Rat.class, Color.GRAY);
        view.setColor(Frog.class, YELLOW);
        view.setColor(Grasshopper.class, PINK);
    }
}
