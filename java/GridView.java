import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.imageio.ImageIO;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

/**
* A graphical view of the simulation grid.
* The view displays a colored rectangle for each location
* representing its contents. It uses a default background color.
* Colors for each type of species can be defined using the
* setColor method.
*
* @author David J. Barnes, Michael Kölling,
       Andras Horvath (k19017476) and Jan Marczak (k19029774)
* @version 2020.02.20
*/
public class GridView extends JFrame implements SimulatorView
{
    // Define some non-default colors.
    private static final Color DARK_GREEN = new Color(140, 207, 127);
    private static final Color LIGHT_GREEN = new Color(179, 255, 172);

    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    // Define default label strings.
    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private final String WEATHER_PREFIX = "Weather: ";

    // Some JComponents used in the GUI.
    private JLabel stepLabel, population, timeLabel, dayTimeLabel, weatherLabel, authorLabel;
    private JPanel populationPane, infoPane, buttonPane, sideBar ;
    private JButton foodWebButton, graphButton, startButton;

    private FieldView fieldView;

    // A map for storing colors for participants in the simulation.
    private Map<Class<?>, Color> colors;
    // A map for storing icons for participants in the simulation.
    private Map<Class, ImageIcon> icons;
    // A map for storing JLabels for participants in the simulation.
    private Map<Class, JLabel> populationLabels;

    // The GraphView that can be opened from this JFrame.
    private GraphView graphView;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    // Images used for marking animals.
    private BufferedImage triangle;
    private BufferedImage red_triangle;
    private BufferedImage circle;
    private BufferedImage red_circle;

    /**
     * Create a view of the given width and height.
     * @param height The simulation's height.
     * @param width  The simulation's width.
     */
    public GridView(int height, int width, SimulatorView graph, Simulator simulation)
    {
        stats = new FieldStats();
        colors = new LinkedHashMap<>();
        graphView = (GraphView) graph;
        fieldView = new FieldView(height, width);

        try {
            triangle = ImageIO.read(new File("res/triangle.png"));
            circle = ImageIO.read(new File("res/circle.png"));
            red_triangle = ImageIO.read(new File("res/red_triangle.png"));
            red_circle = ImageIO.read(new File("res/red_circle.png"));
        } catch (IOException e) {
        }

        setTitle("Grassland Ecosystem Simulation");

        createComponents(simulation);

        pack();
        setVisible(true);
    }

    /**
     * Define a color to be used for a given class of animal.
     * @param animalClass The animal's Class object.
     * @param color The color to be used for the given class.
     */
    public void setColor(Class<?> animalClass, Color color)
    {
        colors.put(animalClass, color);
    }

    /**
     * @return The color to be used for a given class of animal.
     */
    private Color getColor(Class animalClass)
    {
        Color col = colors.get(animalClass);
        if(col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        }
        else {
            return col;
        }
    }

    /**
     * Show the current status of the field.
     * @param step Which iteration step it is.
     * @param time The current time in the simulation.
     * @param weather The weather of the simulation.
     * @param field The field whose status is to be displayed.
     */
    public void showStatus(int step, Time time, Weather weather, Field field)
    {
        if(!isVisible()) {
            setVisible(true);
        }

        stepLabel.setText(center(STEP_PREFIX + step));
        timeLabel.setText(center(time.getDisplayTime()));
        dayTimeLabel.setText(center(time.timeOfDayString()));
        weatherLabel.setIcon(weather.getIcon());
        stats.reset();

        fieldView.preparePaint();

        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {

                Animal animal = field.getAnimalAt(row, col);
                Plant plant = field.getPlantAt(row, col);

                if (plant != null) {
                    if (((Plant) plant).isEatable()) {
                        fieldView.drawBaseMark(col, row, DARK_GREEN);
                    }
                    else {
                        fieldView.drawBaseMark(col, row, LIGHT_GREEN);
                    }
                }

                if(animal != null) {
                    stats.incrementCount(animal.getClass());

                    Color color = getColor(animal.getClass());
                    Image img = null;
                    if (animal.isInfected()) {
                        img = ((Animal) animal).isMale() ? red_triangle : red_circle;
                        fieldView.drawMark(col, row, color, img);
                        fieldView.drawInfectedBorder(col, row);
                    }
                    else {
                        img = ((Animal) animal).isMale() ? triangle : circle;
                        fieldView.drawMark(col, row, color, img);
                    }
                }
                if (animal == null && plant == null) {
                    fieldView.drawBaseMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        if (icons == null) {
            createIcons();
        }
        if (populationLabels == null){
             createPopulationLabels();
        }

        Map<Class, String> populationMap = stats.getPopulationMap(field);
        for (Class key : populationMap.keySet()) {
            String string = populationLabels.get(key).getText();
            string = string.split(" ")[0]; // Get the name of the animal.
            populationLabels.get(key).setText(string + populationMap.get(key));
        }
        fieldView.repaint();

        // Update the graph view as well.
        graphView.showStatus(step, time, weather, field);
    }

    /**
     * Determine whether the simulation should continue to run.
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field)
    {
        return stats.isViable(field);
    }

    /**
     * Prepare for a new run.
     */
    public void reset()
    {
        stats.reset();
    }

    /**
     * Create the map of the population labels.
     */
    private void createPopulationLabels()
    {
        populationLabels = new HashMap<>();
        for (Class key : colors.keySet()) {
            JLabel label = new JLabel(key.getName() + ":");
            label.setIcon(icons.get(key));
            label.setBorder(new EmptyBorder(10,0,10,10));
            populationLabels.put(key, label);
            populationPane.add(label);
        }
    }

    /**
     * Create the map of the icons for each participant.
     */
    private void createIcons()
    {
        icons = new HashMap<>();
        for (Class key : colors.keySet()) {
            BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();

            // Fill a rectangle with the participant's color.
            graphics.setPaint(getColor(key));
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

            ImageIcon icon = new ImageIcon(image);
            icons.put(key, icon);
        }
    }

    /**
     * Center a string inside of a JLabel.
     * @param String The string to be centered.
     * @return The centered string.
     */
    private String center(String string)
    {
        return "<html><div style='text-align: center;'>" + string + "</html></div>";
    }

    /**
     * Set wether the one step button is enabled.
     * @param boolean Enabled or not.
     */
    public void oneStepButtonSetEnabled(boolean enabled)
    {
        startButton.setEnabled(enabled);
    }

    /**
     * Provide a graphical view to show the food web image
     * of the ecosystem.
     */
    private class FoodWebPanel extends JPanel
    {
        private Image img;

        /**
         * Create a FoodWebPanel object and load the image to be shown.
         */
        public FoodWebPanel()
        {
            try {
                img = ImageIO.read(new File("res/foodweb.jpg"));
                if(img == null || (img.getWidth(null) < 0)) {
                    // we could not load the image - probably invalid file format
                }
            }
            catch(IOException e) {
            }

            Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
            setPreferredSize(size);
            //setMinimumSize(size);
            setMaximumSize(size);
            setSize(size);
            setLayout(null);
        }

        /**
         * Override the paintComponent method of JPanel.
         * Draw the image loaded into img.
         * @param Graphics The graphics of the JPanel.
         */
        @Override
        public void paintComponent(Graphics g)
        {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        }
    }

    /**
     * Provide a graphical view of a rectangular field.
     */
    private class FieldView extends JPanel
    {
        private final int GRID_VIEW_SCALING_FACTOR = 60;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width)
        {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);

        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize()
        {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                                 gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component
         * may be resized, compute the scaling factor again.
         */
        public void preparePaint()
        {
            if(! size.equals(getSize())) {  // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if(xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if(yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Paint a plant on grid location on this field in a given color.
         */
        public void drawBaseMark(int x, int y, Color color)
        {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale, yScale);
        }

        /**
         * Paint an animal on grid location on this field in a given color
         * with an appropriate gender image.
         */
        public void drawMark(int x, int y, Color color, Image img)
        {
            g.drawImage(img, x * xScale + 1, y * yScale + 1, xScale - 2, yScale - 2, color, null);
        }

        /**
         * Draw a red border around an infected animal.
         */
         public void drawInfectedBorder(int x, int y)
         {
             Graphics2D g2 = (Graphics2D) g;
             g2.setColor(Color.RED);
             float thickness = 2;
             Stroke oldStroke = g2.getStroke();
             g2.setStroke(new BasicStroke(thickness));
             g2.drawRect(x * xScale + 1, y * yScale + 1, xScale - 2, yScale - 2);
             g2.setStroke(oldStroke);
         }

        /**
         * The field view component needs to be redisplayed. Copy the
         * internal image to screen.
         */
        public void paintComponent(Graphics g)
        {
            if(fieldImage != null) {
                Dimension currentSize = getSize();
                if(size.equals(currentSize)) {
                    g.drawImage(fieldImage, 0, 0, null);
                }
                else {
                    // Rescale the previous image.
                    g.drawImage(fieldImage, 0, 0, currentSize.width, currentSize.height, null);
                }
            }
        }
    }

    /**
     * Create the components used in the GUI of the simulation.
     * @param Simulator The current simulation.
     */
    private void createComponents(Simulator simulation)
    {
        // Font for headings.
        Font heading = new Font("Arial", Font.PLAIN, 24);

        // Initialise labels.
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        stepLabel.setFont(heading);
        stepLabel.setBorder(new EmptyBorder(20,0,20,0));

        timeLabel = new JLabel("", JLabel.CENTER);
        timeLabel.setFont(heading);

        population = new JLabel(center(POPULATION_PREFIX), JLabel.CENTER);
        population.setFont(heading);

        dayTimeLabel = new JLabel("", JLabel.CENTER);
        dayTimeLabel.setFont(heading);
        dayTimeLabel.setBorder(new EmptyBorder(18,0,20,0));

        weatherLabel = new JLabel("", JLabel.CENTER);
        weatherLabel.setBorder(new EmptyBorder(20,50,0,0));

        // Button for simulating one step.
        startButton = new JButton("Simulate one step");
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulation.simulateOneStep();
            }
        });

        // Button for opening the graph's frame.
        graphButton = new JButton("Show graph");
        graphButton.setPreferredSize(new Dimension(200, 50));
        graphButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // show the graph view
                graphView.show();
            }
        });

        // Frame for displaying the food web of the ecosystem.
        JFrame foodWebFrame = new JFrame("Food web");

        JPanel foodWebPanel = new FoodWebPanel();
        foodWebPanel.setLayout(new BorderLayout(20, 20));

        Container foodWebContents = foodWebFrame.getContentPane();
        foodWebContents.add(foodWebPanel, BorderLayout.CENTER);

        foodWebButton = new JButton("Show food web");
        foodWebButton.setPreferredSize(new Dimension(200, 50));
        foodWebButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open a new frame i.e window
                foodWebFrame.pack();
                foodWebFrame.setVisible(true);
            }
        });

        // Organise components into panels.

        infoPane = new JPanel();
        infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.Y_AXIS));
        infoPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            infoPane.add(stepLabel);
            infoPane.add(timeLabel);
            infoPane.add(weatherLabel);
            infoPane.add(dayTimeLabel);

        populationPane = new JPanel();
        populationPane.setLayout(new BoxLayout(populationPane, BoxLayout.Y_AXIS));
        populationPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        populationPane.setBorder(new EmptyBorder(0,0,10,0));
            populationPane.add(population);

        buttonPane = new JPanel(new GridLayout(3,1));
            buttonPane.add(startButton);
            buttonPane.add(foodWebButton);
            buttonPane.add(graphButton);

        authorLabel = new JLabel("Authors: Andras Horvath & Jan Marczak");
        authorLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        authorLabel.setBorder(new EmptyBorder(7, 0, 7, 0));

        sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
            sideBar.add(infoPane);
            sideBar.add(populationPane);
            sideBar.add(buttonPane);
            sideBar.add(authorLabel);
        sideBar.setBorder(new EmptyBorder(0, 10, 0, 10));

        Container contents = getContentPane();
            contents.add(sideBar, BorderLayout.WEST);
            contents.add(fieldView, BorderLayout.CENTER);
    }
}
