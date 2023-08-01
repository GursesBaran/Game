import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;

public class Game extends JPanel implements ActionListener {
    private int paddleX;
    private int paddleY;
    private int paddleWidth;
    private int paddleHeight;
    private ArrayList<Rock> rocks;
    private Timer timer;
    private boolean gameOver;

    private static final int PADDLE_SPEED = 12;
    private static final int PADDLE_WIDTH = 80;
    private static final int PADDLE_HEIGHT = 25;
    private static final int ROCK_SIZE = 30;
    private static final int ROCK_SPEED = 10;
    private static final int TIMER_DELAY = 10;
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 900;

    public Game() {
        paddleX = WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2;
        paddleY = WINDOW_HEIGHT - PADDLE_HEIGHT - 20;
        paddleWidth = PADDLE_WIDTH;
        paddleHeight = PADDLE_HEIGHT;
        rocks = new ArrayList<>();
        gameOver = false;

        timer = new Timer(TIMER_DELAY, this);
        timer.start();
        addKeyListener(new PaddleKeyListener());
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameOver) {
            g.setColor(Color.BLUE);
            g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);

            g.setColor(Color.RED);
            for (Rock rock : rocks) {
                g.fillRect(rock.x, rock.y, ROCK_SIZE, ROCK_SIZE);
            }
        } else {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Game Over!", WINDOW_WIDTH / 2 - 60, WINDOW_HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            createNewRock();
            moveRocks();
            checkCollision();
            repaint();
        }
    }

    private void createNewRock() {
        Random random = new Random();
        if (random.nextInt(100) < 2) {
            int rockX = random.nextInt(WINDOW_WIDTH - ROCK_SIZE);
            rocks.add(new Rock(rockX, 0));
        }
    }

    private void moveRocks() {
        for (Rock rock : rocks) {
            rock.y += ROCK_SPEED;
            if (rock.y >= WINDOW_HEIGHT) {
                rocks.remove(rock);
                break;
            }
        }
    }

    private void checkCollision() {
        Rectangle paddleRect = new Rectangle(paddleX, paddleY, paddleWidth, paddleHeight);
        for (Rock rock : rocks) {
            Rectangle rockRect = new Rectangle(rock.x, rock.y, ROCK_SIZE, ROCK_SIZE);
            if (paddleRect.intersects(rockRect)) {
                gameOver = true;
                break;
            }
        }
    }

    private class Rock {
        int x;
        int y;

        Rock(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class PaddleKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                paddleX -= PADDLE_SPEED;
                if (paddleX < 0) {
                    paddleX = 0;
                }
            } else if (key == KeyEvent.VK_RIGHT) {
                paddleX += PADDLE_SPEED;
                if (paddleX + PADDLE_WIDTH > WINDOW_WIDTH) {
                    paddleX = WINDOW_WIDTH - PADDLE_WIDTH;
                }
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Avoid the Falling Rocks Game");
        Game game = new Game();
        frame.add(game);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

