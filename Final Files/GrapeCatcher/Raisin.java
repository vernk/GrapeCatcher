import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * @description Creates a raisin object with raisin image
 * 
 * @author Sam H, Vern K, Noah B, Saman H 
 * @version 1.0.1
 */
public class Raisin extends JFrame implements Drop
{
    // instance variables - replace the example below with your own
    private BufferedImage raisin; // image of raisin
    private int raisinX; //x cord. 
    private int raisinY; //y cord.
    
    /**
     * @description Constructor for objects of class Raisin
     * Sets raisin to original coordinates
     */
    public Raisin(int firstX,int firstY)
    {
        // initialise instance variables
        raisinX= firstX;
        raisinY=firstY;
    }
    
    /**
     * @description Returns point value of the raisin
     */
    public int getValue()
    {
        return 5;
    }
    
    /**
     * @description Returns x- coordinate of the raisin
     */
    public int getX()
    {
        return raisinX;
    }
    
    /**
     * @description Returns y- coordinate of the grape
     */
    public int getY()
    {
        return raisinY;
    }
    
    /**
     * @description Sets x- coordinate of the grape of to new x- coordinate
     */
    public void setX(int raisinXval)
    {
        raisinX= raisinXval;
    }
    
    /**
     * @description Sets y- coordinate of the grape of to new y- coordinate
     */
    public void setY(int raisinYval)
    {
        raisinY= raisinYval;
    }
    /**
     * @description Sets value of raisin image to new value
     */
    public void setRaisin(BufferedImage value)
    {
        raisin= value;
    }
    
    /**
     * @description Loads image of raisin
     */
    public void loadImageRaisin()
    {
        try{
            raisin = ImageIO.read(getClass().getResource("resources/RaisinPro.png"));
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @description Draws image of raisin
     */
    public void drawImageRaisin(Graphics2D g)
    {
        g.drawImage(raisin, raisinX, raisinY, this);
    }
}
