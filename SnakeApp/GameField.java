import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 288;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private Image background;
    private Image boost;
    private int boostX;
    private int boostY;
    private int appleX;
    private int appleY;
    private int score;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;


    GameField(){
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }

    private void restart(){
        score = 0;
        inGame = true;
        this.initGame();
    }

    private Timer getRefreshedTimer() {
        if(timer == null){
            timer = new Timer(200,this);
            return timer;
        }else {
            timer.setDelay(200);
            return timer;
        }
    }

    private void initGame(){
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i*DOT_SIZE;
            y[i] = 48;
        }
        timer = getRefreshedTimer();
        timer.start();
        createApple();
        createBoost();
    }

    private void createApple(){
        appleX = new Random().nextInt(15)*DOT_SIZE;
        appleY = new Random().nextInt(15)*DOT_SIZE;
        createBoost();
    }
    private void createBoost(){
        int boostRandom = new Random().nextInt(6);

        if(boostRandom == 3) {
            boostX = new Random().nextInt(15) * DOT_SIZE;
            boostY = new Random().nextInt(15) * DOT_SIZE;
        }else {
            boostX = 330;
            boostY = 0;
        }
    }

    private void loadImages(){
        ImageIcon targetIcon = new ImageIcon(getClass().getResource("resources/targetBox.png"));
        apple = targetIcon.getImage();
        ImageIcon snakeIcon = new ImageIcon(getClass().getResource("resources/snakeBox.png"));
        dot = snakeIcon.getImage();
        ImageIcon back = new ImageIcon(getClass().getResource("resources/background.png"));
        background = back.getImage();
        ImageIcon boostIcon = new ImageIcon(getClass().getResource("resources/boost.png"));
        boost = boostIcon.getImage();

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){
            g.drawImage(background,0,0,this);
            g.drawImage(boost,boostX,boostY,this);
            g.drawImage(boost,boostX,boostY,this);
            g.drawImage(apple,appleX,appleY,this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        }else{
            g.drawImage(background, 0, 0, this);
            g.setColor(Color.black);
            String finalScore = "Score " + score;
            String restart = "R - Restart";
            String exit = "E - Exit";
            g.setColor(Color.BLUE);
            g.setFont(new Font(finalScore, Font.BOLD, 25));
            g.drawString(finalScore, 95, SIZE - 180);
            g.drawString(restart, 95, SIZE - 100);
            g.drawString(exit, 95, SIZE - 60);
        }
    }

    private void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        }
        if(up){
            y[0] -= DOT_SIZE;
        }
        if(down){
            y[0] += DOT_SIZE;
        }
    }

    private void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            score+=10;
            createApple();
            timer.setDelay(timer.getDelay() -5);
        }
    }

    private void checkBoost(){
        if(x[0] == boostX && y[0] == boostY){
            dots = dots + 3;
            score+=40;
            timer.setDelay(timer.getDelay() -10);
            createBoost();
        }
    }

    private void checkCollisions(){
        for (int i = dots; i >0 ; i--) {
            if(i>=4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        if(x[0]>=SIZE){
            for (int i = 0; i <dots ; i++) {
                if(x[i] > SIZE){
                    x[i] = 0;
                }
            }
        }
        if(x[0]<=0){
            for (int i = 0; i <dots ; i++) {
                if(x[i] < 0){
                    x[i] = SIZE;
                }
            }
        }
        if(y[0]>SIZE){
            for (int i = 0; i <dots ; i++) {
                if(y[i] >= SIZE){
                    y[i] = 0;
                }
            }
        }
        if(y[0]<0){
            for (int i = 0; i <dots ; i++) {
                if(y[i] < 0){
                    y[i] = SIZE;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame){
            checkApple();
            checkBoost();
            checkCollisions();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_UP && !down){
                right = false;
                up = true;
                left = false;
            }
            if(key == KeyEvent.VK_DOWN && !up){
                right = false;
                down = true;
                left = false;
            }
            if(key == KeyEvent.VK_R){
                restart();
            }
            if(key == KeyEvent.VK_E){
                System.exit(0);
            }
        }
    }
}
