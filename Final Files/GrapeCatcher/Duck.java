import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @description Creates a duck object with duck image
 * 
 * @author Sam H, Vern K, Noah B, Saman H 
 * @version 1.0.1
 */
public class Duck extends JFrame
{
    // instance variables - replace the example below with your own
    private int score= 0; //sets original score to 0
    private int lives= 3; //sets lives originally to 3
    private BufferedImage duck;// image of duck
    //First coordinates of bird image
    private int cordX, cordY; //x and y cords.

    /**
     *  @descriptions Constructor for objects of class Duck
     *  Sets duck to original coordinates
     */
    public Duck(int xLoc, int yLoc)
    {
        // initialise instance variables
        cordX= xLoc;
        cordY= yLoc;
    }
    /**
     * @descriptions Returns score
     */
    public int getScore()
    {
        return score;
    }
    
    /**
     * @descriptions Sets score to new score
     */
    public void setScore(int scoreVal)
    {
        score= scoreVal;
    }

    /**
     * @description Returns x- coordinate of the duck
     */
    public int getX()
    {
        return cordX;
    }

    /**
     * @description Returns y- coordinate of the duck
     */
    public int getY()
    {
        return cordY;
    }
    
    /**
     * @description Sets x- coordinate of the bomb of to new x- coordinate
     */
    public void setX(int cordXval)
    {
        cordX= cordXval;
    }
    
    /**
     * @description Sets y- coordinate of the bomb of to new y- coordinate
     */
    public void setY(int cordYval)
    {
        cordY= cordYval;
    }

    /**
     * @description Adds 1 to score when grape is caught
     */
    public void caught()
    {
        score++;
    }

    /**
     * @description Adds 5 to score when raisin is caught
     */
    public void caughtRaisin()
    {
        score +=5;
    }

    /**
     * @description Returns number of lives
     */
    public int getLives()
    {
        return lives;
    }

    /**
     * @description Decrements lives by one
     */
    public void loseLives()
    {
        if(lives > 0)
            lives--;
    }

    /**
     * @description Increments lives by one
     */
    public void gainLife()
    {
        lives++;
    }    

    /**
     * @description Sets lives to new lives value
     */
    public void setLives(int newLives)
    {
        lives= newLives;
    }

    /**
     * @description Set value of duck image
     */
    public void setDuck(BufferedImage value)
    {
        duck= value;
    }

    /**
     * @description Loads image of duck
     */
    public void loadImageDuck()
    {
        try{
            duck = ImageIO.read(getClass().getResource("resources/Duck.png"));
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @description Draws image of duck
     */
    public void drawImageDuck(Graphics2D g)
    {
        //draw bird image (second image, in this order)
        g.drawImage(duck, cordX, cordY, this);
    }

}
