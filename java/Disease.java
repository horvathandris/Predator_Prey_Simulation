import java.util.Random;

/**
 * This class represents a disease that aninmal can be infected with.
 * Every disease has slightly different attributes that are
 * randomized when the disease is created.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.20.02
 */
public class Disease
{
    private static Random rand = Randomizer.getRandom();
    // The probability of a disease appearing on one animal each step.
    private static final double PROBABILITY = 0.9;
    // The probability of an animal dying each step.
    private double lethalProbability;
    // The probability of an animal healing from the disease each step.
    private double healProbability;
    // The probability of an animal getting a disaese from infected animal.
    private double transmissionProbability;
    // True if the disease spreads through food.
    private boolean spreadsThroughFood;
    // True if the disease spreads through breeding.
    private boolean spreadsThroughBreeding;

    /**
     * Create a new disease with randomized attributes.
     */
    public Disease()
    {
        transmissionProbability = ((double) rand.nextInt(20) + 80) / 100;
        lethalProbability = ((double) rand.nextInt(10)+5) / 100;
        healProbability = ((double) rand.nextInt(10)+5) / 100;
        // Generate number between 0 and 2 inclusive.
        int spreadType = rand.nextInt(3);
        if (spreadType == 0) {
            spreadsThroughFood = true;
            spreadsThroughBreeding = false;
        }
        else if (spreadType == 1) {
            spreadsThroughFood = false;
            spreadsThroughBreeding = true;
        }
        else {
            spreadsThroughFood = true;
            spreadsThroughBreeding = true;
        }
    }

    /**
     * @return Probability of a disease trasmiting a disease.
     */
    public double getTransmissionProbability()
    {
        return transmissionProbability;
    }

    /**
     * @return Probability of an aninmal dying.
     */
    public double getLethalProbability()
    {
        return lethalProbability;
    }

    /**
     * @return Probability of an animal getting healed.
     */
    public double getHealProbability()
    {
        return lethalProbability;
    }


    /**
     * @return Probability of getting a disease.
     */
    public static double getProbability()
    {
        return PROBABILITY;
    }

    /**
     * @return Whether it spreads through food.
     */
    public boolean doesSpreadThroughFood()
    {
        return spreadsThroughFood;
    }

    /**
     * @return Whether it spreads through breeding.
     */
    public boolean doesSpreadThroughBreeding()
    {
        return spreadsThroughBreeding;
    }
}
