import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * @description Creates a bomb object with bomb image
 * 
 * @author Sam H, Vern K, Noah B, Saman H 
 * @version 1.0.1
 */
public class Bomb extends JFrame implements Drop
{
    // instance variables - replace the example below with your own
    private BufferedImage bomb; //image of bomb
    private int bombX; //x cord.
    private int bombY; //y cord.

    /**
     * @ description Constructor for objects of class Bomb
     * Sets bomb to original coordinates
     */
    public Bomb(int firstX,int firstY)
    {
        // initialise instance variables
        bombX= firstX;
        bombY=firstY;
    }

    /**
     * @description Returns point value of the bomb
     */
    public int getValue()
    {
        return 0;
    }
    
    /**
     * @description Returns x- coordinate of the bomb
     */
    public int getX()
    {
        return bombX;
    }
    
    /**
     * @description Returns y- coordinate of the bomb
     */
    public int getY()
    {
        return bombY;
    }
    
    /**
     * @description Sets x- coordinate of the bomb of to new x- coordinate
     */
    public void setX(int bombXval)
    {
        bombX= bombXval;
    }

    /**
     * @description Sets y- coordinate of the bomb of to new y- coordinate
     */
    public void setY(int bombYval)
    {
        bombY= bombYval;
    }
    
    /**
     * @description Sets value of bomb image to new value
     */
    public void setBomb(BufferedImage value)
    {
        bomb= value;
    }

    /**
     * @description Loads image of bomb
     */
    public void loadImageBomb()
    {
        try{
            bomb = ImageIO.read(getClass().getResource("resources/Bomb.png"));
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @description Draws image of bomb
     */
    public void drawImageBomb(Graphics2D g)
    {
        g.drawImage(bomb, bombX, bombY, this);
    }
}