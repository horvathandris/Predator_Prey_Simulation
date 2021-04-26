/**
 * This class is responsible for keeping track of time during
 * the simulation.
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.19
 */
public class Time
{
    // Deafult time (in minutes) that will get incremented with each step.
    private static final int TIME_INCREMENT = 30;
    // Deafult start time of the day.
    private static final int DAY_TIME_START_HOUR = 7;
    // Deafult end time of the day.
    private static final int DAY_TIME_END_HOUR = 22;

    private int day;
    private int hour;
    private int minute;
    private boolean isNight;

    /**
     * Create a deafult time that is set to 12:00.
     */
    public Time()
    {
        hour = 12;
        minute = 0;
        day = 1;
    }

    /**
     * Create a time that is set by providinng parameters.
     * @param int The hours of current day
     * @param int The minutes of current hour
     */
    public Time(int hour, int minute)
    {
        this();
        if (!(hour > 23 || hour < 0 || minute > 60 || minute < 0)) {
            this.hour = hour;
            this.minute = minute;
        }
    }

    /**
     * Increment the current time by the default increment (in minutes).
     */
    public void incrementTime()
    {
        minute += TIME_INCREMENT;
        if (minute >= 60) {
            minute %= 60;
            ++hour;
            if (hour >= 24) {
                hour %= 24;
                ++day;
            }
        }
    }

    /**
     * Creates and returns the string with hours and minutes
     * in the form of "12:00".
     * @return The string with time.
     */
    private String getTimeString()
    {
        StringBuilder builder = new StringBuilder();
        if (hour < 10) {
            builder.append("0");
        }
        builder.append(hour + ":");
        if (minute < 10) {
            builder.append("0");
        }
        builder.append(minute);

        return builder.toString();
    }

    /**
     * Creates and returns the string with a day number
     * @return The string with day number.
     */
    private String getDayString()
    {
        return "Day " + day + ", ";
    }

    /**
     * Check if it is day time or night time.
     * @return true if it is day time and false if it is night time.
     */
    public boolean isNight()
    {
        isNight = !(hour >= DAY_TIME_START_HOUR && hour < DAY_TIME_END_HOUR);
        return isNight;
    }

    /**
     * Combine the day and time string togeter in the form of "Day 1, 12:00".
     * @return The string with a day number and the time.
     */
    public String getDisplayTime()
    {
        return getDayString() + getTimeString();
    }

    /**
     * Return string to be displayed, which says if it is daytime or nightime.
     * @return The string indicating if it's daytime or night-time.
     */
    public String timeOfDayString()
    {
        if (isNight) {
            return "It is night-time.";
        }
        return "It is daytime.";
    }

    public void reset()
    {
        hour = 12;
        minute = 0;
        day = 1;
    }
}
