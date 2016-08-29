import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @description Creates a grape object with grape image
 * 
 * @author Sam H, Vern K, Noah B, Saman H 
 * @version 1.0.1
 */
class Grape extends JFrame implements Drop
{
    // instance variables - replace the example below with your own
    private BufferedImage grape; //image of grape
    private int grapeX; //x cord.
    private int grapeY; //y cord.
    
    /**
     * @description Constructor for objects of class Grape
     * Sets grape to original coordinates
     */
    public Grape(int firstX, int firstY)
    {
        // initialise instance variables
        grapeX= firstX;
        grapeY= firstY;
    }
    
    /**
     * @description Returns point value of the grape
     */
    public int getValue()
    {
        return 1;
    }
    
    /**
     * @description Returns x- coordinate of the grape
     */
    public int getX()
    {
        return grapeX;
    }
    
    /**
     * @description Returns y- coordinate of the object
     */
    public int getY()
    {
        return grapeY;
    }
    
    /**
     * @description Sets x- coordinate of the grape of to new x- coordinate
     */
    public void setX(int grapeXval)
    {
        grapeX= grapeXval;
    }
    
    /**
     * @description Sets y- coordinate of the grape of to new y- coordinate
     */
    public void setY(int grapeYval)
    {
        grapeY= grapeYval;
    }
    
    /**
     * @description Sets value of grape image to new value
     */
    public void setGrape(BufferedImage value)
    {
        grape= value;
    }
    
    /**
     * @description Loads image of grape
     */
    public void loadImageGrape()
    {
        try{
            grape = ImageIO.read(getClass().getResource("resources/Grape.png"));
        }catch(IOException ex) {
            ex.printStackTrace();
        }        
    }
    
    /**
     * @description Draws image of grape
     */
    public void drawImageGrape(Graphics2D g)
    {
        g.drawImage(grape, grapeX, grapeY, this);
    }
    
}
