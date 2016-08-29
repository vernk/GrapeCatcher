import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
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

public class GrapeCatcher extends JFrame implements KeyListener {
    //Objects for images
    public static final int SPEED = 3;
    private BufferedImage background;
    private BufferedImage bird;
    private BufferedImage grape1;
    private BufferedImage grape2;
    private BufferedImage grape3;
    private BufferedImage raisin;
    private BufferedImage bomb;
    private BufferedImage logo;
    private JButton restartButton;
    private JLabel restartLabel;
    //First coordinates of bird image
    private int cordX = 273;
    private int cordY = 607;
    //Coordinates of grape
    private int grapeX1 = (int)(525 * Math.random());
    private int grapeX2 = (int)(525 * Math.random());
    private int grapeX3 = (int)(525 * Math.random());
    private int raisinX = (int)(525 * Math.random());
    private int raisinY = 0;
    private static Socket socket;
    private final static int PORT = 6677;//SET A CONSTANT VARIABLE PORT
	private final static String HOST = "localhost";//SET A CONSTANT VARIABLE HOST
    private int bombX = (int)(525 * Math.random());
    private int bombY = -10;
    private int grapeY1 = 0;
    private int grapeY2 = -125;
    private int grapeY3 = -250;
    private boolean connected = false; 
    private int raisinRandom = (int)(12 * Math.random()); // random value for raisin
    private int bombRandom = (int)(5 * Math.random()); // random value for bomb
    private int score = 0;
    private int lives = 3;
    private int speed= 30;//speed of timer
    private boolean up, down, left, right;
    private int vX = 0, vY = 0;
    private boolean play = false; //game has not been started
    private boolean pause = false; //pause is off
    private boolean raisinTime = false; //to activate raisin
    private boolean bombTime= false; // activiates bomb
    private boolean once = true; //for online score

    //Objects for double buffer
    private BufferStrategy myBuffer;
    public GrapeCatcher(Socket s) {
    	socket = s;
        setTitle("Grape Catcher");
        //set window dimension 480x320px
        setSize(600, 700);
        //load images...
        loadImages();
        //  pack();      
        //make window visible
        ImageIcon img = new ImageIcon("resources/Duck.png");
        setIconImage(img.getImage());
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
        int bonusFifteen = 15; // bonus to add lives
        //ensuring boundary of grapes and raisins
        if (grapeX1 < 25)
            grapeX1 += 25;
        if (grapeX2 < 25)
            grapeX2 += 25;
        if (grapeX3 < 25)
            grapeX3 += 25;
        if (raisinX < 25)
            raisinX += 25;
        if (bombX < 25)
            bombX += 25;

        while (raisinRandom == 0)
            raisinRandom = (int)(12 * Math.random());

        while (bombRandom == 0)
            bombRandom = (int)(5 * Math.random());

        ActionListener actListner = new ActionListener() {
                @
                Override
                public void actionPerformed(ActionEvent event) {
                    tick();
                }
            };
        Timer timer = new Timer(25, actListner);
        timer.start();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                //sending the actual Thread of execution to sleep X milliseconds                        
                Thread.sleep(speed);
            } catch (InterruptedException ie) {}
            tick();
            
            if (score >= 10 && score < 20) {
                speed = 27;
            } else if (score >= 20 && score < 30) {
                speed = 24;
            } else if (score >= 30 && score < 40) {
                speed = 21;
            } else if (score >= 40 && score < 50) {
                speed = 18;
            } else if (score >= 50 && score < 60) {
            } else if (score >= 60 && score < 70) {
                speed = 13;
            } else if (score >= 70 && score < 80) {
                speed = 11;
            } else if (score >= 80 && score < 90) {
                speed = 10;
            } else if (score >= 100) {
                speed = 8;
            }

            //add life each 15 caught
            if (score == bonusFifteen) {
                lives++;
                bonusFifteen += 15;
            }
            //from right side to left
            if (cordX > 530) 
                cordX = 10;
            //go from right to left
            if (cordX < 10) 
                cordX = 530;

            if (grapeY1 < 640) {
                if (((grapeY1 > cordY - 20) && (grapeY1 <= cordY + 20)) && ((grapeX1 > cordX - 25) && (grapeX1 < cordX + 25))) {
                    //grape1=null;
                    grapeY1 = (int)(10 * Math.random());
                    grapeX1 = (int)(525 * Math.random());
                    if (grapeX1 < 25)
                        grapeX1 += 25;
                    //draw another grape
                    score++;
                } else {
                    if (lives > 0 && pause == false && play == true)
                        grapeY1 += 3;
                    else if (lives > 0)
                        grapeY1 += 0;
                    else
                        grape1 = null;
                }
            } else {
                if (lives > 0)
                    lives--;
                if (lives > 0) {
                    grapeY1 = (int)(10 * Math.random());
                    grapeX1 = (int)(525 * Math.random());
                    if (grapeX1 < 25)
                        grapeX1 += 25;
                    repaint();
                } else
                    grape1 = null;
            }
            repaint();
            if (grapeY2 < 640) {
                if (((grapeY2 > cordY - 20) && (grapeY2 <= cordY + 20)) && ((grapeX2 > cordX - 25) && (grapeX2 < cordX + 25))) {
                    grapeY2 = (int)(35 * Math.random());
                    grapeX2 = (int)(525 * Math.random());
                    if (grapeX2 < 25)
                        grapeX2 += 25;
                    //draw another grape
                    score++;
                } else {
                    if (lives > 0 && pause == false && play == true)
                        grapeY2 += 3;
                    else if (lives > 0)
                        grapeY2 += 0;
                    else
                        grape2 = null;
                }
            } else {
                if (lives > 0)
                    lives--;
                if (lives > 0) {
                    grapeY2 = (int)(35 * Math.random());
                    grapeX2 = (int)(525 * Math.random());
                    if (grapeX2 < 25)
                        grapeX2 += 25;
                    repaint();
                } else
                    grape2 = null;
            }
            repaint();
            if (grapeY3 < 640) {
                if (((grapeY3 > cordY - 20) && (grapeY3 <= cordY + 20)) && ((grapeX3 > cordX - 25) && (grapeX3 < cordX + 25))) {
                    grapeY3 = (int)(60 * Math.random());
                    grapeX3 = (int)(525 * Math.random());
                    if (grapeX3 < 25)
                        grapeX3 += 25;
                    //draw another grape
                    score++;
                } else {
                    if (lives > 0 && pause == false && play == true)
                        grapeY3 += 3;
                    else if (lives > 0)
                        grapeY3 += 0;
                    else
                        grape3 = null;
                }
            } else {
                if (lives > 0)
                    lives--;
                if (lives > 0) {
                    grapeY3 = (int)(60 * Math.random());
                    grapeX3 = (int)(525 * Math.random());
                    if (grapeX3 < 25)
                        grapeX3 += 25;
                    repaint();
                } else
                    grape3 = null;
            }
            repaint();
            if (raisinY < 640) {
                if (raisinRandom == score)
                    raisinTime = true;
                if (((raisinY > cordY - 20) && (raisinY <= cordY + 20)) && ((raisinX > cordX - 25) && (raisinX < cordX + 25))) {
                    raisinTime = false; //turn off raisin for interim period
                    raisinY = 0;
                    raisinX = (int)(525 * Math.random());
                    if (raisinX < 25)
                        raisinX += 25;
                    score += 5; // score increase by 5
                    lives++;
                    bombRandom+= 5; //added to factor in shift in score
                    
                    raisinRandom = score + (int)(12 * Math.random());
                    //ensurancing random greater than 0
                    while (raisinRandom == score)
                        raisinRandom = score + (int)(12 * Math.random());
                } else {
                    //movement of the raisin
                    if (lives > 0 && pause == false && play == true && raisinTime == true) {
                        raisinY += 4;
                    }
                    //if game is paused but not over
                    else if (lives > 0)
                    {
                        raisinY += 0;
                    }
                    else
                        raisin = null;
                }
            } else {
                //if the raisin hits the ground
                if (lives > 0) {
                    raisinTime = false;
                    raisinY = 0;
                    raisinX = (int)(525 * Math.random());
                    if (raisinX < 25)
                        raisinX += 25;
                    raisinRandom = score + (int)(12 * Math.random());
                    while (raisinRandom == score)
                        raisinRandom = score + (int)(12 * Math.random());
                    repaint();
                } else
                    raisin = null;
            }

            repaint();

            if (bombY < 640) {
                if (bombRandom == score)
                    bombTime = true;
                if (((bombY > cordY - 20) && (bombY <= bombY + 20)) && ((bombX > cordX - 25) && (bombX < cordX + 25))) {
                    bombTime = false; //turn off raisin for interim period
                    bombY = -10;
                    bombX = (int)(525 * Math.random());
                    if (bombX < 25)
                        bombX += 25;

                    //lose two lives    
                    lives-= 3;

                    //reset random
                    bombRandom = score + (int)(5 * Math.random());                    
                    //ensurancing random greater than 0
                    while (bombRandom == score)
                        bombRandom = score + (int)(5 * Math.random());

                } else {
                    //movement of the raisin
                    if (lives > 0 && pause == false && play == true && bombTime == true) {
                        bombY += 4;
                    }
                    //if game is paused but not over
                    else if (lives > 0)
                        bombY += 0;
                    else
                        bomb = null;
                }
            } else {
                //if the bomb hits the ground
                if (lives > 0) {
                    bombTime = false;
                    bombY = -10;

                    //starting point > 25
                    bombX = (int)(525 * Math.random());
                    if (bombX < 25)
                        bombX += 25;

                    //ensure random is greater than 0    
                    bombRandom = score + (int)(5 * Math.random());
                    while (bombRandom == score)
                        bombRandom = score + (int)(5 * Math.random());
                        
                    repaint();
                } else
                    bomb = null;
            }
            repaint();
        }
    }

    public void loadImages() {
        try {
            //path for image file
            String pathBackground = "Game.png";
            background = ImageIO.read(new File(pathBackground));
            String pathBird = "Duck.png";
            bird = ImageIO.read(new File(pathBird));
            grape1 = ImageIO.read(new File("Grape.png"));
            grape2 = ImageIO.read(new File("Grape.png"));
            grape3 = ImageIO.read(new File("Grape.png"));
            raisin = ImageIO.read(new File("RaisinPro.png"));
            bomb = ImageIO.read(new File("Bomb.png"));
            logo= ImageIO.read(new File("logo.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        //associate the keyboard listener with this JFrame
        addKeyListener(this);
    }
   

    public void drawImages(Graphics2D g) {
        //draw brackground image (first image)
        g.drawImage(background, 0, 0, this);
        //g.(button, 300,300,this);
        //draw bird image (second image, in this order)
        g.drawImage(bird, cordX, cordY, this);
        g.drawImage(grape1, grapeX1, grapeY1, this);
        g.drawImage(grape2, grapeX2, grapeY2, this);
        g.drawImage(grape3, grapeX3, grapeY3, this);
        g.drawImage(raisin, raisinX, raisinY, this);
        g.drawImage(bomb, bombX, bombY, this);

        if (play == true && lives > 0) {
            //score label
            g.setFont(new Font("default", Font.BOLD, 32));
            g.setColor(Color.YELLOW);
            g.drawString("Score: " + score, 10, 75);
            //lives label
            Color pink = new Color(0xFF99FF, false);
            g.setColor(pink);
            String liveHearts = generateHearts(lives);
            g.drawString("Lives: " + lives + " " + liveHearts, 10, 110);
        }        
        //ask user to begin game
        
        if (play == false) {
            g.setFont(new Font("default", Font.BOLD, 32));
            g.setColor(Color.BLUE);
            g.drawImage(logo, 90, 130, this);
            //instructions
            g.setFont(new Font("default", Font.BOLD, 24));
            g.setColor(Color.DARK_GRAY);
            g.drawString("Instructions:", 225, 205);
            g.setFont(new Font("default", Font.BOLD, 16));
            g.drawString("Grapes are 1 Point and Raisins are 5 Points plus an Extra Life", 70, 225);
            g.drawString("Missing a Grape will Cost You a Life, but Raisins are Free...", 85, 245);
            g.drawString("So Choose Wisely... and Bombs are Worth Extra, too ;)", 100, 265);
            g.drawString("Every 15 Points will Earn you an Extra Life", 140, 285);
            g.drawString("Press or Hold the Left Arrow Key to Move left", 135, 305);
            g.drawString("Press or Hold the Right Arrow Key to Move Right", 125, 325);
            g.drawString("Press the Escape Key to Pause the Game", 145, 345);
            g.drawString("Press Q to Quit the Game During Play", 160, 365);
            //begin
            g.setColor(Color.RED);
            g.setFont(new Font("default", Font.BOLD, 32));
            g.drawString("Shall we play a game?", 125, 450);
            g.setFont(new Font("default", Font.BOLD, 24));
            if (connected) {
            	g.setColor(Color.BLUE);
            	g.drawString("Connected to server successfully", 100, 480);
            	g.setColor(Color.RED);
            g.drawString("Press Space Bar to Begin!", 150, 520);
        }
            
           if (connected==false && play==false) {
            try 
    		{
    			
    			Socket s = new Socket(HOST, PORT);//CONNECT TO THE SERVER
    			
    			//System.out.println("You connected to " + HOST);//IF CONNECTED THEN PRINT IT OUT
    			 g.drawString("Connected to server!\nPress Space Bar to Begin!", 150, 480);
    			Client client = new Client(s);//START NEW CLIENT OBJECT
    			
    			Thread t = new Thread(client);//INITIATE NEW THREAD
    			t.start();//START THREAD
    			connected=true;
    			
    		} 
    		catch (Exception noServer)//IF DIDNT CONNECT PRINT THAT THEY DIDNT
    		{
    			 System.exit(0);
    			 connected=true;
    		}
           }
            
        }
        //to pause game label
        if (pause == true) {
            g.setFont(new Font("default", Font.BOLD, 40));
            g.setColor(Color.RED);
            g.drawString("GAME PAUSED!", 150, 260);
            g.setFont(new Font("default", Font.BOLD, 24));
            g.setColor(Color.BLACK);
            g.drawString("Press the Space Bar to Resume", 125, 300);
        }
        //YOU LOSE label and new game option
        if (lives == 0) {
            g.setFont(new Font("default", Font.BOLD, 40));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 160, 260);
            g.setFont(new Font("default", Font.BOLD, 24));
            g.setColor(Color.YELLOW);
            g.drawString("Score: " + score, 240, 300);
            g.setColor(Color.BLUE);
            g.setFont(new Font("default", Font.BOLD, 20));
            g.drawString("Go to www.grapecatcher.vernk.me to see your ranking", 40, 400);
            //restart button
            g.setColor(Color.DARK_GRAY);
            g.drawString("Would you like to play again?", 155, 335);
            g.drawString("Press ENTER to restart", 190, 360);
            /*
             * ----------------------ALL THIS IS TO PUT SCORE ONLINE------------------
             * 
             */
            if (once) {
                InetAddress ip;
                String ipstring;
                try {
                    ip = InetAddress.getLocalHost();
                    //hostname = ip.getHostName();
                    ipstring = ip.getHostName();
                    if (cordY < 607) {
                        ipstring += "%20(Fly%20hack%20on)";
                    }
                    ipstring = ipstring.replace(' ', '-');
                    InputStream is;
                    try {
                        URL url = new URL("http://grapecatcher.vernk.me/api.php?ip=" + ipstring + "&score=" + score + "&endlives=" + lives);
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
            bird = null;
        }
    }

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

    public static void main(String[] args) {
        new GrapeCatcher(socket);
        try
		{
			Scanner chat = new Scanner(System.in);//GET THE INPUT FROM THE CMD
			Scanner in = new Scanner(socket.getInputStream());//GET THE CLIENTS INPUT STREAM (USED TO READ DATA SENT FROM THE SERVER)
			PrintWriter out = new PrintWriter(socket.getOutputStream());//GET THE CLIENTS OUTPUT STREAM (USED TO SEND DATA TO THE SERVER)
			
			while (true)//WHILE THE PROGRAM IS RUNNING
			{						
				String input = chat.nextLine();	//SET NEW VARIABLE input TO THE VALUE OF WHAT THE CLIENT TYPED IN
				out.println(input);//SEND IT TO THE SERVER
				out.flush();//FLUSH THE STREAM
				
				if(in.hasNext())//IF THE SERVER SENT US SOMETHING
					System.out.println(in.nextLine());//PRINT IT OUT
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();//MOST LIKELY WONT BE AN ERROR, GOOD PRACTICE TO CATCH THOUGH
		} 
        //timer.schedule(new SayHello(), 0, 100);
    }

    public void tick() {
        cordX += vX;
        // etc...
    }

    private void update() {
        vX = 0;
        vY = 0;

        if (down) vY = SPEED;
        if (up) vY = -SPEED;
        if (left) vX = -SPEED;
        if (right) vX = SPEED;
    }

    //While a key is pressed
    public void keyPressed(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            //if the right arrow in keyboard is pressed...
            case KeyEvent.VK_RIGHT:
            {
                if (cordX <= 545 && pause == false && play == true)
                // cordX +=13;
                //right=true;
                    vX = 5;
            }
            break;
            //if the left arrow in keyboard is pressed...
            case KeyEvent.VK_LEFT:
            {
                if (cordX >= 10 && pause == false && play == true)
                //cordX -=13;
                //left= true;
                    vX = -5;                
            }
            break;
            //if the down arrow in keyboard is pressed...
            case KeyEvent.VK_DOWN:
            {
                if (cordY < 607 && pause == false && play == true)
                //cordY+=15;
                    vY = 5;
            }
            break;

            case KeyEvent.VK_ENTER:
            {
                //call restart method
                if (lives == 0) {
                    reset();
                }
            }
            break;

            //start
            case KeyEvent.VK_SPACE:
            {
                if (lives > 0) {
                    play = true;
                    pause = false;
                }
            }
            break;

            //pause and unpause
            case KeyEvent.VK_ESCAPE:
            {
                if (lives > 0 && play == true) {
                    pause = true;
                }
            }
            break;

            //life hack for testing and fun:)
            case KeyEvent.VK_L:
            {
                if (pause == false)
                    lives = 999;
            }
            break;
            //quit option
            case KeyEvent.VK_Q:
            {
                if (play == true && pause == false && lives > 0) {
                    lives = 0;
                }
            }
            break;           

        }
        //call explicit to paint
        repaint();
    }
    //When a key is typed (once)
    public void keyTyped(KeyEvent ke) {}
    //When a key is released (typed or pressed)
    public void keyReleased(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_DOWN:
            vY = 0;
            break;
            case KeyEvent.VK_UP:
            vY = 0;
            break;
            case KeyEvent.VK_LEFT:
            vX = 0;
            break;
            case KeyEvent.VK_RIGHT:
            vX = 0;
            break;
        }
        //update();
    }
    //to play again after losing
    private void reset() {
        //loadImages();
        grapeY1 = 0;
        grapeY2 = -125;
        grapeY3 = -250;
        grapeX1 = (int)(525 * Math.random());
        grapeX2 = (int)(525 * Math.random());
        grapeX3 = (int)(525 * Math.random());
        raisinX = (int)(525 * Math.random());
        bombX = (int)(525 * Math.random());
        grapeY1 = 0;
        grapeY2 = -125;
        grapeY3 = -250;
        raisinY = 0;
        bombY = -10;

        raisinRandom = (int)(12 * Math.random()); // random value for raisin
        bombRandom = (int)(5 * Math.random()); // random value for bomb

        //ensure grapes fall within bounds
        if (grapeX1 < 25)
            grapeX1 += 25;
        if (grapeX2 < 25)
            grapeX2 += 25;
        if (grapeX3 < 25)
            grapeX3 += 25;
        if (raisinX < 25)
            raisinX += 25;
        if (bombX < 25)
            bombX += 25;

        //ensure randoms are not 0
        while (raisinRandom == 0)
            raisinRandom = (int)(12 * Math.random());

        while (bombRandom == 0)
            bombRandom = (int)(5 * Math.random());

        //reset score and lives    
        score = 0;
        lives = 3;
        once = true;
        speed= 30; //reset speed
        //reload grapes, raisins, bombs
        loadImages();
    }
    public int pingHost(String host){
        Long start = System.currentTimeMillis();
        try {
			if (!InetAddress.getByName(host).isReachable(2000)) return -1;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return (int) (System.currentTimeMillis()-start);
    }
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