import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import javax.swing.*;
import javax.swing.Timer;
import sun.audio.*;

//import java.util.TimerTask;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
/**
 * The Graphic User Interface for GrapeCatcher
 * Creates Timer and Movement of Images
 * Loads and Draws Images
 * Controls Key Binding and Screen Text
 * 
 * @Authors Sam Haber, Vern Kerai, Noah Brown,
 * @Player Saman Hashemipour
 * @Version 1.2.6
 */
public class GrapeCatcherGUI extends JFrame implements KeyListener {
    //Objects for images
    public static final int SPEED = 3;
    private BufferedImage logo;
    private BufferedImage background;
    private BufferedImage instructions;

    //First coordinates of bird image
    private Duck duck= new Duck(273,607);

    //Coordinates of grapes, raisin, bomb
    private Grape grape1= new Grape((int)(525 * Math.random()), 0);//grape1
    private Grape grape2= new Grape((int)(525 * Math.random()), -125); //grape2
    private Grape grape3= new Grape((int)(525 * Math.random()), -250); //grape3
    private Raisin raisin= new Raisin((int)(525 * Math.random()), 0);  //raisin
    private Bomb bomb = new Bomb((int)(525 * Math.random()), -10);//bomb
    private Bomb bomb2= new Bomb((int)(525 * Math.random()), -25);//bomb2

    //string for version
    private final String VERSION= "1.2.6";

    //ints
    private int raisinRandom = (int)(12 * Math.random()); // random value for raisin
    private int bombRandom = (int)(5 * Math.random()); // random value for bomb
    private int bombRandom2 = (int)(5 * Math.random()); // random value for bomb
    private int bonusFifteen = 15; // bonus to add lives()
    private int speed= 28;//speed of timer at beginning
    private int vX = 0; //velocity

    //booleans
    private boolean play = false; //game has not been started
    private boolean pause = false; //pause is off
    private boolean raisinTime = false; //to activate raisin
    private boolean bombTime= false; // activiates bomb
    private boolean bombTime2= false;
    private boolean once = true; //for online duck.getScore()

    //Objects for double buffer
    private BufferStrategy myBuffer;
    /**
     * Constructor for Grape Catcher
     * @description Creates size of background, loads images, opens window, sets speed, 
     * and controls movement and painting of images
     */
    public GrapeCatcherGUI() {
        setTitle("Grape Catcher");

        //set window dimension 480x320px
        setSize(600, 700);

        //load images...
        loadImages(); 
    
        //icon
        ImageIcon img = new ImageIcon(getClass().getResource("resources/Duck.png"));
        setIconImage(img.getImage());
        
        //make window visible
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Ignore OS paint calls
        setIgnoreRepaint(true);

        //create a double buffer objects
        //NOTE: It's very important to create bufferStrategy after
        //JFrame is visible, in other case fail!
        this.createBufferStrategy(2);
        myBuffer = this.getBufferStrategy();

        //call explicit to paint first time
        repaint();

        //ensuring boundary of grapes and raisins
        if (grape1.getX() < 25)
            grape1.setX(grape1.getX() + 25);
        if (grape2.getX() < 25)
            grape2.setX(grape2.getX() + 25);
        if (grape3.getX() < 25)
            grape3.setX(grape3.getX() + 25);
        if (raisin.getX() < 25)
            raisin.setX(raisin.getX() + 25);
        if (bomb.getX() < 25)
            bomb.setX(bomb.getX() + 25);
        if (bomb2.getX() < 25)
            bomb2.setX(bomb.getX() + 25);
        
        //ensures values of randoms are greater than 0
        //raisin
        while (raisinRandom == 0)
            raisinRandom = (int)(12 * Math.random());

        //bomb1
        while (bombRandom == 0)
            bombRandom = (int)(5 * Math.random());

        //bomb2
        while (bombRandom2 == 0)
            bombRandom2 = (int)(5 * Math.random());

        ActionListener actListner = new ActionListener() {
                @
                Override
                public void actionPerformed(ActionEvent event) {
                    tick();
                }
            };
        Timer timer = new Timer(25, actListner);
        timer.start();
        //for loop to continue timer process
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                //sending the actual Thread of execution to sleep X milliseconds                        
                Thread.sleep(speed); //timer
            } catch (InterruptedException ie) {}
            tick();

            //sets speed based on score
            if (duck.getScore() >= 10 && duck.getScore() < 20) {
                speed = 26;
            } else if (duck.getScore() >= 20 && duck.getScore() < 30) {
                speed = 24;
            } else if (duck.getScore() >= 30 && duck.getScore() < 40) {
                speed = 22;
            } else if (duck.getScore() >= 40 && duck.getScore() < 50) {
                speed = 19;
            } else if (duck.getScore() >= 50 && duck.getScore() < 60) {
                speed = 16;
            } else if (duck.getScore() >= 60 && duck.getScore() < 70) {
                speed = 14;
            } else if (duck.getScore() >= 70 && duck.getScore() < 80) {
                speed = 12;
            } else if (duck.getScore() >= 80 && duck.getScore() < 90) {
                speed = 11;
            } else if (duck.getScore() >= 90 && duck.getScore() < 100) {
                speed = 10;
            } else if (duck.getScore() >= 100 && duck.getScore() < 200) {
                speed = 8;
            } else if (duck.getScore() >= 200 && duck.getScore() < 300) {
                speed = 7;
            } else if (duck.getScore() >= 300 && duck.getScore() < 400) {
                speed = 6;
            } else if (duck.getScore() >= 400 && duck.getScore() < 500) {
                speed = 5;
            } else if (duck.getScore() >= 500 && duck.getScore() < 600) {
                speed = 4;
            } else if (duck.getScore() >= 600) {
                speed = 3;
            }

            //add life each 15 caught
            //only if user has lss than 15 lives
            if ((bonusFifteen== duck.getScore()) && duck.getLives() < 15) {
                bonusFifteen += 15;
                duck.gainLife();
            }

            //from right side to left
            if (duck.getX() > 530) 
                duck.setX(10);
            //go from right to left
            if (duck.getX() < 10) 
                duck.setX(530);

            //grape1    
            if (grape1.getY() < 640){ //has not hit ground yet
                if (((grape1.getY() > duck.getY() - 20) && (grape1.getY() <= duck.getY() + 20)) && ((grape1.getX() > duck.getX() - 30) && (grape1.getX() < duck.getX() + 30))) {
                    //resets loc if caught
                    grape1.setY((int)(10 * Math.random()));
                    grape1.setX((int)(525 * Math.random()));
                    
                    //ensures x val is greater than 25
                    if (grape1.getX() < 25)
                        grape1.setX(grape1.getX() + 25);

                    //draw another grape
                    duck.caught();
                } else {
                    // grape moving
                    if (duck.getLives() > 0 && pause == false && play == true)
                        grape1.setY(grape1.getY() + 3);
                        //game not over but not moving
                    else if (duck.getLives() > 0)
                        grape1.setY(grape1.getY() + 0);
                    else
                    // game over
                        grape1.setGrape(null);
                }
            } else {
                //if hits ground
                //lose life
                if (duck.getLives() > 0)
                    duck.loseLives();
                //reset if game not over yet
                if (duck.getLives() > 0) {
                    grape1.setY((int)(10 * Math.random()));
                    grape1.setX((int)(525 * Math.random()));
                    //ensure x cord is greater than 25
                    if (grape1.getX() < 25)
                        grape1.setX(grape1.getX() + 25);
                    repaint();
                } else
                    grape1.setGrape(null);
            }
            repaint();

            //grape2
            //same motion fuctions as grape 1
            if (grape2.getY() < 640) {
                if (((grape2.getY() > duck.getY() - 20) && (grape2.getY() <= duck.getY() + 20)) && ((grape2.getX() > duck.getX() - 30) && (grape2.getX() < duck.getX() + 30))) {
                    grape2.setY((int)(10 * Math.random()));
                    grape2.setX((int)(525 * Math.random()));

                    if (grape2.getX() < 25)
                        grape2.setX(grape2.getX() + 25);

                    //draw another grape
                    duck.caught();
                } else {
                    if (duck.getLives() > 0 && pause == false && play == true)
                        grape2.setY(grape2.getY() + 3);
                    else if (duck.getLives() > 0)
                        grape2.setY(grape2.getY() + 0);
                    else
                        grape2.setGrape(null);
                }
            } else {
                if (duck.getLives() > 0)
                    duck.loseLives();
                if (duck.getLives() > 0) {
                    grape2.setY((int)(10 * Math.random()));
                    grape2.setX((int)(525 * Math.random()));
                    if (grape2.getX() < 25)
                        grape2.setX(grape2.getX() + 25);
                    repaint();
                } else
                    grape2.setGrape(null);
            }
            repaint();

            //grape3 
            //same motion fuctions as grape 1
            if (grape3.getY() < 640) {
                if (((grape3.getY() > duck.getY() - 20) && (grape3.getY() <= duck.getY() + 20)) && ((grape3.getX() > duck.getX() - 30) && (grape3.getX() < duck.getX() + 30))) {
                    grape3.setY((int)(10 * Math.random()));
                    grape3.setX((int)(525 * Math.random()));

                    if (grape3.getX() < 25)
                        grape3.setX(grape1.getX() + 25);

                    //draw another grape
                    duck.caught();
                } else {
                    if (duck.getLives() > 0 && pause == false && play == true)
                        grape3.setY(grape3.getY() + 3);
                    else if (duck.getLives() > 0)
                        grape3.setY(grape3.getY() + 0);
                    else
                        grape3.setGrape(null);
                }
            } else {
                if (duck.getLives() > 0)
                    duck.loseLives();
                if (duck.getLives() > 0) {
                    grape3.setY((int)(10 * Math.random()));
                    grape3.setX((int)(525 * Math.random()));
                    if (grape3.getX() < 25)
                        grape3.setX(grape3.getX() + 25);
                    repaint();
                } else
                    grape3.setGrape(null);
            }
            repaint();

            //raisin
            if (raisin.getY() < 640) { //if raisin is not on ground; in air
                //checks to see if time to drop raisin
                if (raisinRandom == duck.getScore())
                    raisinTime = true;
                //if raisin is caught
                if (((raisin.getY() > duck.getY() - 20) && (raisin.getY() <= duck.getY() + 20)) && ((raisin.getX() > duck.getX() - 30) && (raisin.getX() < duck.getX() + 30))) {
                    raisinTime = false; //turn off raisin for interim period
                    //reset raisin loc
                    raisin.setY(0);
                    raisin.setX((int)(525 * Math.random()));
                    
                    //ensures raisin x is greater 25
                    if (raisin.getX() < 25)
                        raisin.setX(raisin.getX() + 25);

                    duck.caughtRaisin(); // score increase by 5
                    
                    // increments life if user has less than 15
                    if(duck.getLives() < 15)
                        duck.gainLife(); //add life

                    bombRandom+= 5; //added to factor in shift in score
                    bonusFifteen+=5; //factor in change after catching raisin
                    
                    //create new random val for raisin
                    raisinRandom = duck.getScore() + (int)(12 * Math.random());
                    
                    //ensurancing random greater than 0
                    while (raisinRandom == duck.getScore())
                        raisinRandom = duck.getScore() + (int)(12 * Math.random());
                } else {
                    //movement of the raisin
                    if (duck.getLives() > 0 && pause == false && play == true && raisinTime == true) {
                        raisin.setY(raisin.getY() + 4);
                    }
                    //if game is paused but not over
                    else if (duck.getLives() > 0)
                    {
                        raisin.setY(raisin.getY() + 0);
                    }
                    else
                        raisin.setRaisin(null);
                }
            } else {
                //if the raisin hits the ground
                if (duck.getLives() > 0) {
                    //turn off raisin time
                    raisinTime = false;
                    //reset loc
                    raisin.setY(0);
                    raisin.setX((int)(525 * Math.random()));
                    
                    //ensure x cord greater than 25
                    if (raisin.getX() < 25)
                        raisin.setX(raisin.getX() + 25);
                    
                    //new random
                    raisinRandom = duck.getScore() + (int)(12 * Math.random());
                    //ensures random > 0
                    while (raisinRandom == duck.getScore())
                        raisinRandom = duck.getScore() + (int)(12 * Math.random());
                    repaint();
                } else
                    raisin.setRaisin(null);
            }

            repaint();
            //bomb1
            //always in play
            if (bomb.getY() < 640) { //if bomb is not on ground
                //checks if time to drop bomb
                if (bombRandom == duck.getScore())
                    bombTime = true;
                //if bomb is caught    
                if (((bomb.getY() > duck.getY() - 20) && (bomb.getY() <= duck.getY() + 20)) && ((bomb.getX() > duck.getX() - 30) && (bomb.getX() < duck.getX() + 30))) {
                    bombTime = false; //turn off raisin for interim period
                    //resets loc of bomb after being caught
                    bomb.setY(-10);
                    bomb.setX((int)(525 * Math.random()));
                    
                    //ensure x cord is greater than 25
                    if (bomb.getX() < 25)
                        bomb.setX(bomb.getX() + 25);

                    //lose two duck.getLives()    
                    duck.setLives(duck.getLives() - 3);

                    //reset random
                    bombRandom = duck.getScore() + (int)(5 * Math.random());                    
                    //ensurancing random greater than 0
                    while (bombRandom == duck.getScore())
                        bombRandom = duck.getScore() + (int)(5 * Math.random());

                } else {
                    //movement of the raisin
                    if (duck.getLives() > 0 && pause == false && play == true && bombTime == true) {
                        bomb.setY(bomb.getY() + 4);
                    }
                    //if game is paused but not over
                    else if (duck.getLives() > 0)
                        bomb.setY(bomb.getY() + 0);
                        //if game is over
                    else
                        bomb.setBomb(null);
                }
            } else {
                //if the bomb hits the ground
                if (duck.getLives() > 0) {
                    bombTime = false;// turns of bomb until time to drop again
                    
                    //resets x and y cords
                    bomb.setY(-10);

                    //starting point > 25
                    bomb.setX((int)(525 * Math.random()));
                    if (bomb.getX() < 25)
                        bomb.setX(bomb.getX() + 25);

                    //reset and ensure random is greater than 0    
                    bombRandom = duck.getScore() + (int)(5 * Math.random());
                    while (bombRandom == duck.getScore())
                        bombRandom = duck.getScore() + (int)(5 * Math.random());

                    repaint();
                } else
                    bomb.setBomb(null);
            }
            repaint();

            //bomb2
            //same motion style as bomb 1
            //only exists after score >= 200
            if (bomb2.getY() < 640) {
                if (bombRandom2 == duck.getScore() && duck.getScore() >= 200)
                    bombTime2 = true;
                if (((bomb2.getY() > duck.getY() - 20) && (bomb2.getY() <= bomb2.getY() + 20)) && ((bomb2.getX() > duck.getX() - 30) && (bomb2.getX() < duck.getX() + 30))) {
                    bombTime2 = false; //turn off raisin for interim period
                    bomb2.setY(-10);
                    bomb2.setX((int)(525 * Math.random()));
                    if (bomb2.getX() < 25)
                        bomb2.setX(bomb2.getX() + 25);

                    //lose two duck.getLives()    
                    duck.setLives(duck.getLives() - 3);

                    //reset random
                    bombRandom2 = duck.getScore() + (int)(5 * Math.random());                    
                    //ensurancing random greater than 0
                    while (bombRandom2 == duck.getScore())
                        bombRandom2 = duck.getScore() + (int)(5 * Math.random());

                } else {
                    //movement of the raisin
                    if (duck.getLives() > 0 && pause == false && play == true && bombTime2 == true) {
                        bomb2.setY(bomb2.getY() + 4);
                    }
                    //if game is paused but not over
                    else if (duck.getLives() > 0)
                        bomb2.setY(bomb2.getY() + 0);
                    else
                        bomb2.setBomb(null);
                }
            } else {
                //if the bomb hits the ground
                if (duck.getLives() > 0) {
                    bombTime2 = false;
                    bomb2.setY(-10);

                    //starting point > 25
                    bomb2.setX((int)(525 * Math.random()));
                    if (bomb2.getX() < 25)
                        bomb2.setX(bomb2.getX() + 25);

                    //ensure random is greater than 0    
                    bombRandom2 = duck.getScore() + (int)(5 * Math.random());
                    while (bombRandom2 == duck.getScore())
                        bombRandom2 = duck.getScore() + (int)(5 * Math.random());

                    repaint();
                } else
                    bomb2.setBomb(null);
            }
            repaint();
        }
    }

    /**
     * @description Loads Images from PNG Files
     */
    public void loadImages() {
        try {
            //path for bg image file
            background = ImageIO.read(getClass().getResource("resources/Game.png"));

            //load image for opening page
            logo= ImageIO.read(getClass().getResource("resources/Logo.png"));

            //load instructions images
            instructions= ImageIO.read(getClass().getResource("resources/Instructions.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //load images of duck
        duck.loadImageDuck();

        //load images of grapes, raisin, bomb
        grape1.loadImageGrape();
        grape2.loadImageGrape();
        grape3.loadImageGrape();
        raisin.loadImageRaisin();
        bomb.loadImageBomb();
        bomb2.loadImageBomb();

        //associate the keyboard listener with this JFrame
        addKeyListener(this);
    }

    /**
     * @description Draws Loaded Images on Screen;
     * Draws Test and Prompts on Screem
     */
    public void drawImages(Graphics2D g) {
        //draw brackground image (first image)
        g.drawImage(background, 0, 0, this);

        //draw bird image (second image, in this order)
        //draw grapes, raisin, bomb
        duck.drawImageDuck(g);
        grape1.drawImageGrape(g);
        grape2.drawImageGrape(g);
        grape3.drawImageGrape(g);
        raisin.drawImageRaisin(g);
        bomb.drawImageBomb(g);
        bomb2.drawImageBomb(g);
        
        //score and lives at top left of screen during game play only
        if (play == true && duck.getLives() > 0) {
            //duck.getScore() label
            g.setFont(new Font("default", Font.BOLD, 32));
            g.setColor(Color.YELLOW);
            g.drawString("Score: " + duck.getScore(), 10, 75);
            //duck.getLives() label
            Color pink = new Color(0xFF99FF, false);
            g.setColor(pink);
            String liveHearts = generateHearts(duck.getLives());
            g.drawString("Lives: " + duck.getLives() + " " + liveHearts, 10, 110);
        }        
        //ask user to begin game
        if (play == false) {
            g.setFont(new Font("default", Font.BOLD, 32));
            g.setColor(Color.BLUE);
            g.drawImage(logo, 90, 115, this);

            //instructions    
            g.drawImage(instructions, 75, 190, this);            
        }
        //to pause game label
        if (pause == true && duck.getLives() > 0) {
            g.setFont(new Font("default", Font.BOLD, 40));
            g.setColor(Color.RED);
            g.drawString("GAME PAUSED!", 150, 260);
            g.setFont(new Font("default", Font.BOLD, 24));
            g.setColor(Color.BLACK);
            g.drawString("Press the Space Bar to Resume", 125, 310);
            g.drawString("Press Q to Quit", 215, 340);
        }
        //YOU LOSE label and new game option
        if (duck.getLives() <= 0) {
            g.setFont(new Font("default", Font.BOLD, 40));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 180, 265);
            g.setFont(new Font("default", Font.BOLD, 24));
            g.setColor(Color.YELLOW);
            g.drawString("Score: " + duck.getScore(), 255, 300);
            g.setColor(Color.BLUE);
            g.setFont(new Font("default", Font.BOLD, 20));
            g.drawString("Go to www.grapecatcher.com to see your ranking", 65, 400);

            //restart button            
            g.setColor(Color.DARK_GRAY);
            g.drawString("Would you like to play again?", 165, 335);
            g.drawString("Press ENTER to restart", 190, 365);
            /*
             * ----------------------ALL THIS IS TO PUT SCORE ONLINE------------------
             * 
             */
            if (once) {
                InetAddress ip; // ip
                String ipstring;
                try {
                    ip = InetAddress.getLocalHost();
                    //hostname = ip.getHostName();
                    ipstring = ip.getHostName();
                    ipstring = ipstring.replace(' ', '-');
                    InputStream is;
                    try {
                        URL url = new URL("http://grapecatcher.vernk.me/api.php?ip=" + ipstring + "&score=" + duck.getScore()+ "&version=" + VERSION); //sends info to server
                        is = url.openStream();
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                once = false;
            }
            /*
             * ----------------------------END PUTTING SCORE ONLINE--------------------------
             */
            //set bird to null
            duck.setDuck(null);
        }
    }

    /**
     * Paints the Images on the Screen
     */
    public void paint(Graphics g) {
        Graphics2D g2 = null;
        if (myBuffer != null) {
            try {
                //get the graphics2d object of the buffer
                g2 = (Graphics2D) myBuffer.getDrawGraphics();
                //draw images in buffer
                drawImages(g2);
            } finally {
                g2.dispose();
            }
            //draw the content of buffer in the screen
            myBuffer.show();
        }
    }

    /**
     * @description Increments the X-axis according to keys clicked
     * Called by the timer to update coordinate
     */
    public void tick() {
        duck.setX(duck.getX() + vX);
        // etc...
    }

    //While a key is pressed
    /**
     * @description Key Bindings
     * @Right To move Right
     * @Left To move Left
     * @A To move Left
     * @D To move Right
     * @Enter to Restart
     * @Space to start
     * @Escape to pause
     * @Q to Quit
     */
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            //right option1
            //if the right arrow in keyboard is pressed...
            //x velocity increased to 5
            case KeyEvent.VK_RIGHT:
            {
                if (duck.getX() <= 545 && pause == false && play == true)
                    vX = 5;
            }
            break;

            //left option1
            //if the left arrow in keyboard is pressed...
            //x velocity increased to -5
            case KeyEvent.VK_LEFT:
            {
                if (duck.getX() >= 10 && pause == false && play == true)
                    vX = -5;                
            }
            break;

            //right option2
            //if the A in keyboard is pressed...
            //x velocity increased to 5
            case KeyEvent.VK_D:
            {
                if (duck.getX() <= 545 && pause == false && play == true)
                    vX = 5;
            }
            break;

            //left option2
            //if the D in keyboard is pressed...
            //x velocity increased to -5
            case KeyEvent.VK_A:
            {
                if (duck.getX() >= 10 && pause == false && play == true)
                    vX = -5;                
            }
            break;

            //restart
            case KeyEvent.VK_ENTER:
            {
                //call restart method
                if (duck.getLives() == 0) {
                    reset();
                    pause=false;
                }
            }
            break;

            //start and unpause
            case KeyEvent.VK_SPACE:
            {
                if (duck.getLives() > 0) {
                    play = true;
                    pause = false;
                }
            }
            break;

            //pause
            case KeyEvent.VK_ESCAPE:
            {
                if (duck.getLives() > 0 && play == true) {
                    pause = true;
                }
            }
            break;

            //life hack for testing and fun:) 
            //will remain commented but can be commented if needed for later versions
            /*
            case KeyEvent.VK_L:
            {
            if (play== true && pause == false)
            duck.setLives(999);
            }
            break;

            //score hack for testing
            case KeyEvent.VK_S:
            {
            if (play== true && pause == false)
            duck.setScore(duck.getScore() + 100);
            }
            break;
             */

            //quit option
            case KeyEvent.VK_Q:
            {
                if (play == true && pause == true && duck.getLives() > 0) {
                    duck.setLives(0);
                }
            }
            break;           

        }
        //call explicit to paint
        repaint();
    }
    //When a key is typed (once)
    /**
     * @description Reacts to the touch of a key
     */
    public void keyTyped(KeyEvent ke) {}
    //When a key is released (typed or pressed)
    /**
     * @description Notes when a key has been released
     * A new key can then be pressed
     */
    public void keyReleased(KeyEvent ke) {
        //connects each key to the velocity for smoother moving
        switch (ke.getKeyCode()) {            
            case KeyEvent.VK_LEFT:
            vX = 0;
            break;
            case KeyEvent.VK_RIGHT:
            vX = 0;
            break;
            case KeyEvent.VK_A:
            vX = 0;
            break;
            case KeyEvent.VK_D:
            vX = 0;
            break;
        }
        //update();
    }
    //to play again after losing
    /**
     * @description Resets the Game after being called by ENTER
     */
    private void reset() {
        //sets all coordinates of objects back to original
        grape1.setY(0);
        grape2.setY(-125);
        grape3.setY(-250);
        grape1.setX((int)(525 * Math.random()));
        grape2.setX((int)(525 * Math.random()));
        grape3.setX((int)(525 * Math.random()));
        raisin.setX((int)(525 * Math.random()));
        bomb.setX((int)(525 * Math.random()));
        bomb2.setX((int)(525 * Math.random()));
        grape1.setY(0);
        grape2.setY(-125);
        grape3.setY(-250);
        raisin.setY(0);
        bomb.setY(-10);
        bomb.setY(-25);
        duck.setX(273);

        raisinRandom = (int)(12 * Math.random()); // random value for raisin
        bombRandom = (int)(5 * Math.random()); // random value for bomb

        //ensure grapes fall within bounds
        //ensuring boundary of grapes and raisins
        if (grape1.getX() < 25)
            grape1.setX(grape1.getX() + 25);
        if (grape2.getX() < 25)
            grape2.setX(grape2.getX() + 25);
        if (grape3.getX() < 25)
            grape3.setX(grape3.getX() + 25);
        if (raisin.getX() < 25)
            raisin.setX(raisin.getX() + 25);
        if (bomb.getX() < 25)
            bomb.setX(bomb.getX() + 25);
        if (bomb2.getX() < 25)
            bomb2.setX(bomb.getX() + 25);

        //ensure randoms are not 0
        while (raisinRandom == 0)
            raisinRandom = (int)(12 * Math.random());

        while (bombRandom == 0)
            bombRandom = (int)(5 * Math.random());

        //reset duck.getScore() and duck.getLives()    
        duck.setScore(0); //resets score
        duck.setLives(3); //resets lives
        once = true; //resets server send
        speed= 30; //reset speed

        //reload background
        loadImages();        
    }

    /**
     * @description Presents the Lives in Picture form at top
     */
    public String generateHearts(int livesin) {
        if (livesin == 1) return ("♥ ");
        if (livesin == 2) return ("♥ ♥ ");
        if (livesin == 3) return ("♥ ♥ ♥ ");
        if (livesin == 4) return ("♥ ♥ ♥ ♥ ");
        if (livesin == 5) return ("♥ ♥ ♥ ♥ ♥ ");
        if (livesin == 6) return ("♥ ♥ ♥ ♥ ♥ ♥ ");
        if (livesin == 7) return ("♥ ♥ ♥ ♥ ♥ ♥ ♥ ");
        if (livesin == 8) return ("♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ");
        if (livesin == 9) return ("♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ");
        if (livesin > 9) return ("♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥ ♥...");
        return "";
    }
}