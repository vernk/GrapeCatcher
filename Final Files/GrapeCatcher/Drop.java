/**
 * Interface of Dropable Objects for the Game
 * 
 * @author Sam H, Vern K, Noah B, Saman H 
 * @version 1.0.0
 */
public interface Drop
{
    /**
     * @Details Returns point value of the object
     */
    int getValue();
    /**
     * @Details Returns x- coordinate of the object
     */
    int getX();
    /**
     * @Details Returns y- coordinate of the object
     */
    int getY();
    /**
     * @Details Sets x- coordinate of the object of to new x- coordinate
     */
    void setX(int xValue);
    /**
     * @Details Sets y- coordinate of the object of to new y- coordinate
     */
    void setY(int yValue);
}
