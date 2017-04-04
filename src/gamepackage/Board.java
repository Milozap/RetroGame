package gamepackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener 
{
    protected static int width = 1000;
    protected static int height = 900;
    protected static int dotSize = 10;
    private int delay = 40;
    protected int playerWidth = 100;
    protected int playerHeight = 100;
     
    private boolean inGame = true;
    private boolean instaWin = false;
    private boolean needHelp = false;
    private boolean stopInit = true;
    private boolean stopMenu = false;
    
    private Timer timer;

    Image playerImg;
    Player player = new Player(100,100);
    ArrayList<Enemy> enemy = new ArrayList<>();
        
    public Board() 
    {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(width, height));
        loadImages();
        init();
    }

    private void loadImages() 
    {
        ImageIcon ip = new ImageIcon("player2.png");
        playerImg = ip.getImage();
    }

    private void init()
    {
        addEnemy();
        if(enemy.get(0).velX ==0 && enemy.get(0).velY ==0)
        {
            enemy.remove(0);
            addEnemy();
        }
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        
        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) 
    {
        if (inGame)
        {
            g.setFont(new Font("Helvetica", Font.BOLD, 20));
            g.setColor(Color.white);
            g.drawString("Score: " + player.score , width-100, 50);
            g.drawString("Lifes: " + player.lives, width-100, 80);
            g.drawImage(playerImg, player.x, player.y, this);
            //g.drawLine(width/2,0,width/2,height);
            //g.drawLine(0,height/2,width,height/2);
            
            for(int i=0; i <= enemy.size()-1; i++)
            {
                g.setColor(enemy.get(i).color);
                g.fillRect(enemy.get(i).x , enemy.get(i).y , dotSize, dotSize);
            }
            Toolkit.getDefaultToolkit().sync();
        }        
        else 
        {

            gameOver(g);    
            
        }
        if(instaWin)
        {
            gameWon(g);
        }  
    }

    private void gameOver(Graphics g)  
    {
        
        String msg = "Your score: " + player.score + "    Press space to reset";
        //System.out.println("sc: " + player.score);
        
        Font small = new Font("Helvetica", Font.BOLD, 24);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (width - metr.stringWidth(msg)) / 2, height / 2);
        timer.stop();     
    }

        private void gameWon(Graphics g)  
        {
                
        Font small = new Font("Helvetica", Font.BOLD, 24);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString("Congratulations!! YOU WON", (width - metr.stringWidth("Congratulations!! YOU WON")) / 2, height / 2);
        timer.stop();     
        }

    

    private void move()
    {
        if(stopInit)
        {
            introduce();
        }
        if(needHelp)
        {
            player.leftDirection = player.rightDirection = player.upDirection = player.downDirection = false;
            String intro = "Avoid flying enemies." + "\n" + "Controls: AWSD or arrows." + "\n" + "Pause: esc." + "\n" + "Help: h" + "\n" + "\n" + "Power Ups: " + "\n" + "1.Additional life (" + player.cost + " pt)" + "\n" + "2.Slower speed (10 pt)" + "\n" + "3.Instant win (100 pt) ";
            JOptionPane.showMessageDialog(this, intro);
            needHelp = false;
        }
              
        movePlayer();
        moveEnemy();
    }
    
    private void introduce()
    {
        timer.stop();
        String intro = "Avoid flying enemies." + "\n" + "Controls: AWSD or arrows." + "\n" + "Pause: esc." + "\n" + "Help: h";
        JOptionPane.showMessageDialog(this, intro);
        intro = "Power Ups: " + "\n" + "1.Additional life (" + player.cost + " pt)" + "\n" + "2.Slower speed (10 pt)" + "\n" + "3.Instant win (100 pt) ";
        JOptionPane.showMessageDialog(this, intro);
        
        timer.start();
        stopInit = false;     
    }
    private void movePlayer() 
    {
         if (player.leftDirection) 
         {
            player.x -= dotSize;
         }
        if (player.rightDirection) 
        {
            player.x += dotSize;
        }
        if (player.upDirection) 
        {
            player.y -= dotSize;
        }
        if (player.downDirection) 
        {
            player.y += dotSize;
        }
    }
    
    private void moveEnemy()
    {
        for(int i=0; i < enemy.size(); i++)
        {
            enemy.get(i).x += enemy.get(i).velX;
            enemy.get(i).y += enemy.get(i).velY;
            //System.out.println("enemy x: " + enemy.get(i).x);
            //System.out.println("enemy y: " + enemy.get(i).y);
        }        
                
    }
    
   

    private void checkCollision()
    {
        if(player.x == 0)
        {
            player.leftDirection = false;
        }      
        if(player.x + (playerWidth) >= width)
        {
            player.rightDirection = false;
        }       
        if(player.y == 0)
        {
            player.upDirection = false;
        }       
        if(player.y + (playerHeight) >= height)
        {
            player.downDirection = false;
        }
      
        for(int i = 0; i < enemy.size(); i++)
        {
            if((enemy.get(i).x <= 0) || (enemy.get(i).x >= width))
            {
                enemy.get(i).velX = -enemy.get(i).velX;
            }
            
            if((enemy.get(i).y <= 0) || (enemy.get(i).y >= height))
            {
                enemy.get(i).velY = -enemy.get(i).velY;
            }   
        }
        
        for(int i = 0; i < enemy.size(); i++)
        {
            for(int j=0; j < 10; j++)
            {
                for(int k=0; k<10; k++)
                    {
                        if(enemy.get(i).x == player.x + (k * 10) && enemy.get(i).y == player.y + (j * 10)) 
                        {
                            enemy.remove(i);
                            addEnemy();
                                                               
                            
                            while(enemy.get(i).velX == 0 && enemy.get(i).velY == 0)
                            {
                                enemy.remove(i);
                                addEnemy();
                            }

                            player.lives--;
                            if(player.lives == 0)
                            {
                                inGame = false;
                            }
                        }
                    }
            }
        }      
    }
    
    private void warning()
    {
        timer.stop();
        player.leftDirection = player.rightDirection = player.upDirection = player.downDirection = false;        
        JOptionPane.showMessageDialog(this, "Not enough points.");
        timer.start();
    }
    
    private void addEnemy()
    {
        if ((player.x+50) >= width/2 && (player.y+50) >= height/2)
        {
            enemy.add(new Enemy( ((width/10)/2), ((height/10)/2), 0, 0));
        }
        else if ((player.x+50) < width/2 && (player.y+50) < height/2)
        {
            enemy.add(new Enemy( ((width/10)/2), ((height/10)/2), (width/10)/2, (height/10)/2 ));
        }
        else if ((player.x+50) >= width/2 && (player.y+50) < height/2)
        {
            enemy.add(new Enemy( ((width/10)/2), ((height/10)/2), 0, (height/10)/2 ));                    
        }
        else if ((player.x+50) < width/2 && (player.y+50) >= height/2)
        {
        enemy.add(new Enemy( ((width/10)/2), ((height/10)/2), (width/10)/2, 0));
        }                                    
        
    }
    
    private void reset()
    {
                inGame = true;
                enemy.removeAll(enemy);
                player.score = 1;
                player.lives = 3;
                player.cost = 20;
                player.x = player.y = 100;
                addEnemy();
                if(enemy.get(0).velX ==0 && enemy.get(0).velY ==0)
                {
                    enemy.remove(0);
                    addEnemy();
                }                
                timer.start();        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {

        if (inGame) 
        {
            checkCollision();
            move();
            if(player.level != 100)
            {
                player.level++;
            }
            else
            {
                if(enemy.size() >= 20)
                {
                    timer.setDelay(--delay);
                    player.score++;
                }
                else
                {
                    addEnemy();                

                    for(int j=0; j < 10; j++)
                    {
                        for(int k=0; k<10; k++)
                        {
                            while(enemy.get(enemy.size()-1).velX == 0 && enemy.get(enemy.size()-1).velY == 0)
                            {
                            enemy.remove(enemy.size()-1);
                            addEnemy();
                            }                            
                        }
                    }                   
                }
                if(enemy.size()>2 && enemy.get(0).x == enemy.get(1).x && enemy.get(0).y == enemy.get(1).y)
                {
                    enemy.get(0).velX += 1;
                }                
                player.score++;
                player.level = 0;
            }
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter 
    {
        @Override
        public void keyPressed(KeyEvent e) 
        {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            {
                player.leftDirection = true;
            }

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) 
            {
                player.rightDirection = true;
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) 
            {
                player.upDirection = true;
            }

            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) 
            {
                player.downDirection = true;
            }
            
            if (key == KeyEvent.VK_1)
            {
               if(player.score - player.cost >= 0)
               {
                   player.score -= player.cost;
                   player.lives++;
                   player.cost += 5;
               }
               else
                   warning();
            }

            if (key == KeyEvent.VK_2)
            {
               if(player.score - 10 >= 0)
               {
                   player.score -= 10;
                   delay += 10;
               }
               else
                   warning();
            }

            if (key == KeyEvent.VK_3)
            {
                if(player.score - 100 >= 0)
                {
                    player.score -= 100;
                    instaWin = true;
                }
                else
                    warning();
            }
            
            if (key == KeyEvent.VK_H)
            {
                needHelp = true;
            }

            if (key == KeyEvent.VK_SPACE && !inGame)
            {
                reset();
            }

            if(key == KeyEvent.VK_ESCAPE) 
            {
                if(!stopMenu)
                {
                    stopMenu = true;
                    timer.stop();
                }
                else
                {
                    stopMenu = false;
                    timer.start();
                }
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) 
        {

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)
            {
                player.leftDirection = false;
            }

            if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) 
            {
                player.rightDirection = false;
            }

            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) 
            {
                player.upDirection = false;
            }

            if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) 
            {
                player.downDirection = false;
            }
        }
    }
}

class Player
{
    protected int x;
    protected int y;
    protected int level = 0;
    protected int lives =3;
    protected int score=1;
    protected int cost = 20;
    protected boolean leftDirection = false;
    protected boolean rightDirection = false;
    protected boolean upDirection = false;
    protected boolean downDirection = false;
    
    public Player(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
}

class Enemy
{
    protected int x;
    protected int y;
    protected int velX;
    protected int velY;
    protected int velSum;
    protected Color color;
    Random random = new Random();
    
    public Enemy()
    {
        x = Board.dotSize * random.nextInt(Board.width/10);
        y = Board.dotSize * random.nextInt(Board.height/10);

        if(x==0)
        {
            x += 10;
        }
        else if(x==Board.width)
        {
            x -=10;
        }
        if(y==0)
        {
            y += 10;
        }
        else if(y==Board.width)
        {
            y -=10;
        }
        velX = Board.dotSize * (random.nextInt(4)-2);
        velY = Board.dotSize *(random.nextInt(4)-2);
        velSum = Math.abs(velX) + Math.abs(velY);
        switch(velSum)
        {
            case 10:
                color= Color.white;
                break;
            case 20:
                color = Color.blue;
                break;
            case 30: 
                color = Color.magenta;
                break;
            case 40:
                color = Color.red;
                break;
        }        
    }
    public Enemy(int placeX, int placeY, int stepX, int stepY)
    {
        x = Board.dotSize * (random.nextInt(placeX) + stepX);
        y = Board.dotSize * (random.nextInt(placeY) + stepY);

        if(x==0)
        {
            x += 10;
        }
        else if(x==Board.width)
        {
            x -=10;
        }
        if(y==0)
        {
            y += 10;
        }
        else if(y==Board.width)
        {
            y -=10;
        }
        
        velX = Board.dotSize * (random.nextInt(4)-2);
        velY = Board.dotSize *(random.nextInt(4)-2);
        velSum = Math.abs(velX) + Math.abs(velY);
        switch(velSum)
        {
            case 10:
                color= Color.white;
                break;
            case 20:
                color = Color.blue;
                break;
            case 30: 
                color = Color.magenta;
                break;
            case 40:
                color = Color.red;
                break;
        }        
    }
    
}
