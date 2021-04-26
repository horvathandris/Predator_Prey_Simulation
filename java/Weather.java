import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * Simulates a weather in the simulation. The types of weather are:
 * Rainy, foggy, sunny and cloudy (sunny or cloudy is always active, 
 * depending on the time of the day). The simulation allwos for maximum
 * two weather types to be active at the same time (plus sunny and cloudy).
 * The possible weather combinations are stored in a HashMap called 
 * weatherCombinatons, so there can sunny and rainy at the same time
 * (assuming it is daytime). The weather has also an effect on the whole 
 * simulation e.g.when it rains the grass grows faster and when 
 * it's foggy the predators cannot move as freely as before.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Weather
{
    private static final Random rand = Randomizer.getRandom();

    // The weather types are stored in a HashMap, where a key specifies the name of it 
    // and the value tells if it is currently active (true) or not (false).
    private Map<String, Boolean> weatherTypes;
    // The possible weather combinnations that can occur in a simulation.
    private Map<String, String[]> weatherCombinations;
    // Some weather types are dependent on the time therefore we pass it to this class.
    private Time time; 
    // True if there are two active weather types
    private boolean areTwoActive;
    private int duration;
    // ImageIcons that represent the current weather and are displayed on the sidebar.
    private ImageIcon sunnyIcon, rainyIcon, foggyIcon, rainyAndFoggyIcon, sunnyAndRainyIcon, cloudyIcon;

    /**
     * Creates possible weather types and combinations between them. 
     * @param t Time in the simulation.
     */
    public Weather(Time t)
    {
        weatherTypes = new HashMap<>();
        time = t;                   
        setWeatherTypes();          // Set all possible weather types.
        areTwoActive = false;       // Deafult weather is sunny (in daytime) or cloudy (at night).
        setWeatherCombinations();   // Set all possible weather combinations
        loadIcons();                // Load weather icons displayed
    }
    
    /**
     * Set all possible weather types that can occur in the simulation.
     */
    private void setWeatherTypes()
    {
        weatherTypes.put("sunny", false);
        weatherTypes.put("cloudy", false);
        weatherTypes.put("rainy", false);
        weatherTypes.put("foggy", false);
    }
    
    /**
     * Set the weather types that the weather can combine with at the same time.
     */
    private void setWeatherCombinations()
    {
        // Here rainy can combine with sunny or with foggy.
        weatherCombinations = new HashMap<>();
        weatherCombinations.put("rainy", new String[] {"sunny", "foggy"});
        weatherCombinations.put("foggy", new String[] {"rainy"});
        weatherCombinations.put("sunny", new String[] {"rainy"});
        weatherCombinations.put("cloudy", new String[] {"rainy", "foggy"});
    }
    
    /**
     * Set the first active weather in the simulation. It first removes
     * "deafult" type weather such as sunny and cloudy and then randomly
     * gives a chance for other weather types to be set by calling the setActive()
     * method. If nothing is set there we call the setDeafultWeather().
     */
    private void setFirstActive() {
        Map<String, Boolean> weathers = weatherTypes;
        weathers.remove("sunny");
        weathers.remove("cloudy");
        String firstActive = setActive(weathers.keySet());
        if (firstActive != null) {
            weatherTypes.put(firstActive, true);
        }
        else {
            setDeafultWeather();
        }
        setDuration(); // Set the duration for new weather type.
    }

    /**
     * Set the second active weather in the simulation by first getting the
     * current first active weather. Then it shuffles the list of all possible 
     * combinations that the active weather can be combined with and call the
     * setActive() method to randomly check if that indeed happens.
     */
    private void setSecondActive() {
        String activeWeather = whichIsActive(); // get the first active weather.
        List<String> weatherCombinationsList= Arrays.asList(weatherCombinations.get(activeWeather));
        Collections.shuffle(weatherCombinationsList);
        String secondActive = setActive(weatherCombinationsList);
        if (secondActive != null) {
            weatherTypes.put(secondActive, true);
            areTwoActive = true;
        }
        setDeafultWeather();
    }

    /**
     * Get the iterable collection as a parameter and checks if its 
     * one element will be set to active by counting the probability.
     * @param collectionn The collection that the method iterates over.
     * @return The weather type to be set to active or null if that is
     *         not the case.
     */
    private String setActive(Iterable<String> collection)
    {
        for (String key : collection) {
            if (rand.nextDouble() <= 0.3) {
                return key;
            }
        }
        return null;
    }

    /**
     * @return The currently active weather.
     */
    private String whichIsActive()
    {
        for (String key : weatherTypes.keySet()) {
            if (weatherTypes.get(key) == true) {
                return key;
            }
        }
        return null;
    }

    /**
     * Set the deafult weather. Sunny if it is daytime,
     * cloudy when it is night.
     */
    private void setDeafultWeather()
    {
        if (!time.isNight()) {
            weatherTypes.put("sunny", true);
            weatherTypes.put("cloudy", false);
        }
        else {
            weatherTypes.put("cloudy", true);
            weatherTypes.put("sunny",  false);
        }
    }

    /**
     * Decrease the duration of the current weather and sets the
     * deafult weather. When the time changes from daytime to night-time
     * the weather is automaticly uploaded. When the duration is set 
     * to zero it calls the setWeatherTypes() that resets the weather 
     * types by dropping their prior boolean values and replacing them with 
     * deafult ones. Then the first and second active weather is set.
     */
     public void decrementDuration()
     {
         duration--;
         setDeafultWeather(); 
         if (duration <= 0) {
             setWeatherTypes();
             setFirstActive();
             setSecondActive();
         }
     }

    /**
     * Randomize and set the duration to value between 15 annd 30.
     */
    private void setDuration()
    {
        duration = rand.nextInt(15) + 15;
    }

    /**
     * Reset the weather types and duration to deafult values.
     */
     public void reset()
     {
         setWeatherTypes();
         setDeafultWeather();
         setDuration();
     } 

    /**
     * Load the icons for the GUI.
     */
    private void loadIcons()
    {
        try {
            sunnyIcon = new ImageIcon(ImageIO.read(new File("res/sunny.png")));
            rainyIcon = new ImageIcon(ImageIO.read(new File("res/rainy.png")));
            foggyIcon = new ImageIcon(ImageIO.read(new File("res/foggy.png")));
            rainyAndFoggyIcon = new ImageIcon(ImageIO.read(new File("res/foggy_and_rainy.png")));
            sunnyAndRainyIcon = new ImageIcon(ImageIO.read(new File("res/sunny_and_rainy.png")));
            cloudyIcon = new ImageIcon(ImageIO.read(new File("res/cloudy.png")));
        } catch (IOException e) {
        }
    }
    
    /**
     * Check and return the icon that represents the weather
     * that is currently active on the field.
     * @return ImageIcon that represents weather.
     */
     public ImageIcon getIcon()
    {
         if (isRaining() && isFoggy()) {
             return rainyAndFoggyIcon;
         }
         else if (isRaining() && isSunny()) {
             return sunnyAndRainyIcon;
         }
         else if (isRaining()) {
             return rainyIcon;
         }
         else if (isFoggy()) {
             return foggyIcon;
         }
         else if (isSunny()) {
             return sunnyIcon;
         }
         else if (isCloudy()) {
             return cloudyIcon;
         }
         return null;
    }

    /**
     * @return true if it is raining.
     */
     public boolean isRaining()
     {
         return weatherTypes.get("rainy");
     }

    /**
     * @return true if it is foggy.
     */
     public boolean isFoggy()
     {
         return weatherTypes.get("foggy");
     }

     /**
      * @return true if it is sunny.
      */
     public boolean isSunny()
     {
         return weatherTypes.get("sunny");
     }

     /**
      * @return true if it is cloudy.
      */
     public boolean isCloudy()
     {
         return weatherTypes.get("cloudy");
     }
}
