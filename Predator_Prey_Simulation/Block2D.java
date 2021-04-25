/**
 * This class contains information of a 2 dimensional block
 * which represents one location in the field.
 * It has a bottom and a top level, which both
 * can contain an object.
 *
 * @author Andras Horvath (k19017476) and Jan Marczak (k19029774)
 * @version 2020.02.20
 */
public class Block2D
{
    // The two objects representing the two layers
    private Object top;
    private Object bottom;

    /**
     * Create an Block2D object with empty layers.
     */
    public Block2D()
    {

    }

    /**
     * Create a Block2D object with provided
     * top and bottom objects.
     */
    public Block2D(Object topObject, Object bottomObject)
    {
        top = topObject;
        bottom = bottomObject;
    }

    /**
     * Set the top object.
     * @param object The object to be set on top.
     */
    public void setTop(Object object)
    {
        top = object;
    }

    /**
     * Set the bottom object.
     * @param object The object to be set on bottom.
     */
    public void setBottom(Object object)
    {
        bottom = object;
    }

    /**
     * Accessor method for the object on top.
     * @return The object on top.
     */
    public Object top()
    {
        return top;
    }

    /**
     * Accessor method for the object on bottom.
     * @return The object on bottom.
     */
    public Object bottom()
    {
        return bottom;
    }

    /**
     * Set the top object to null.
     */
    public void clearTop()
    {
        top = null;
    }

    /**
     * Set the bottom object to null.
     */
    public void clearBottom()
    {
        bottom = null;
    }

    /**
     * Set both object layers to null.
     */
    public void clearAll()
    {
        top = null;
        bottom = null;
    }
}
