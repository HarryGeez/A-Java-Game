import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 * Created by weijiangan on 28/11/2016.
 */
public class Board extends JPanel implements ComponentListener {
    private int frameWidth, frameHeight;
    private boolean isSplash;
    private int LAND_HEIGHT = (int) (0.8 * frameHeight);
    private int WATER_HEIGHT = (int) (0.95 * frameHeight);
    private int MOUNTAIN_HEIGHT = (int) (0.82 * frameWidth);
    private int MOON_X = (int) (0.8 * frameHeight);
    private int MOON_Y = (int) (0.12 * frameWidth);
    private int PLAYER_X = (int) (0.15 * frameWidth);
    private int PLAYER_Y;
    private final int SPAWN_INTERVAL = 32;
    private int i = 0;
    private Random random;
    private Terrain cloud, ground, ground2, water, water2, mountain;
    private Player player;
    private Vector<Enemy> enemies;
    private Iterator<Enemy> iter;
    Timer timer;

    public Board(int width, int height) {
        this.frameWidth = width;
        this.frameHeight = height;
        enemies = new Vector<>();
        addComponentListener(this);
        setDoubleBuffered(true);
        random = new Random();
        cloud = new Terrain(-2, "resources/Tiles/Cloud_1.png");
        cloud.scaleSprite(0.2f);
        ground = new Terrain(-5, "resources/Tiles/grassMid.png");
        ground2 = new Terrain(-5, "resources/Tiles/grassCenter.png");
        water = new Terrain(-15, "resources/Tiles/liquidWaterTop_mid.png");
        water2 = new Terrain(-15, "resources/Tiles/liquidWater.png");
        mountain = new Terrain(-1, "resources/Background/Mountains.png");
        player = new Player();
        player.setX(30);
        timer = new Timer(25, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cloud.nextPos();
                ground.nextPos();
                ground2.nextPos();
                water.nextPos();
                water2.nextPos();
                mountain.nextPos();
                player.nextFrame();
                player.updatePos();
                if (i == SPAWN_INTERVAL) {
                    spawnEnemies();
                    i = -1;
                }
                iter = enemies.iterator();
                while (iter.hasNext()) {
                    Enemy tmp = iter.next();
                    if (tmp.getX() < 500) {
                        enemies.remove(tmp);
                        break;
                    }
                    tmp.nextFrame();
                    tmp.updatePos();
                }
                repaint();
                i++;
            }
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawSky(g);
        drawMoon(g);
        drawCloud(g);
        drawMountain(g);
        drawLand(g);
        drawWater(g);

        g.drawImage(player.getSprite(), player.getX(), player.getY(), this);

        iter = enemies.iterator();
        while (iter.hasNext()) {
            Enemy tmp = iter.next();
            g.drawImage(tmp.getSprite(), tmp.getX(), tmp.getY(), null);
        }
    }

    private void drawSky(Graphics g) {
        g.setColor(new Color(181, 229, 216));
        g.fillRect(0, 0, frameWidth, (int) (frameHeight * 0.8));
    }

    private void drawMoon(Graphics g) {
        g.setColor(new Color(255, 235, 153));
        g.fillOval(MOON_X, MOON_Y, 100, 100);
    }

    private void drawCloud(Graphics g) {
        for (int x = cloud.getInitX(); x < frameWidth; x += cloud.getW())
            g.drawImage(cloud.getSprite(), x, (int) (frameHeight * 0.1), null);
    }

    private void drawMountain(Graphics g) {
        for (int x = mountain.getInitX(); x < frameWidth; x += mountain.getW())
            g.drawImage(mountain.getSprite(), x, MOUNTAIN_HEIGHT, null);
    }

    private void drawLand(Graphics g) {
        for (int y = LAND_HEIGHT; y < frameHeight; y += ground.getH()) {
            if (y == LAND_HEIGHT) {
                for (int x = ground.getInitX(); x < frameWidth; x += ground.getW()) {
                    g.drawImage(ground.getSprite(), x, y, null);
                }
            } else {
                for (int x = ground.getInitX(); x < frameWidth; x += ground2.getW()) {
                    g.drawImage(ground2.getSprite(), x, y, null);
                }
            }
        }
    }

    private void drawWater(Graphics g) {
        for (int y = WATER_HEIGHT; y < frameHeight; y += water.getH()) {
            if (y == WATER_HEIGHT) {
                for (int x = water.getInitX(); x < frameWidth; x += water.getW())
                    g.drawImage(water.getSprite(), x, y, null);
            } else {
                for (int x = water.getInitX(); x < frameWidth; x += water2.getW())
                    g.drawImage(water2.getSprite(), x, y, null);
            }
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        timer.stop();
        frameHeight = getHeight();
        frameWidth = getWidth();
        LAND_HEIGHT = (int) (0.85 * frameHeight);
        WATER_HEIGHT = (int) (0.92 * frameHeight);
        MOUNTAIN_HEIGHT = (LAND_HEIGHT - 500);
        PLAYER_X = (int) (0.15 * frameWidth);
        PLAYER_Y = LAND_HEIGHT - player.getSprite().getHeight(null) + 5;
        player.setX(PLAYER_X);
        player.setY(PLAYER_Y);
        player.setLAND_Y(PLAYER_Y);
        MOON_X = (int) (0.8 * frameWidth);
        MOON_Y = (int) (0.08 * frameHeight);
        iter = enemies.iterator();
        while (iter.hasNext()) {
            Enemy tmp = iter.next();
            tmp.setY(frameHeight - 81 + 5);
        }
        timer.start();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}


    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            player.jump(true);
        } else if (key == KeyEvent.VK_LEFT) {
            player.setDx(-9);
        } else if (key == KeyEvent.VK_RIGHT) {
            player.setDx(9);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            player.jump(false);
        } else if (key == KeyEvent.VK_LEFT) {
            player.setDx(0);
        } else if (key == KeyEvent.VK_RIGHT) {
            player.setDx(0);
        } else if (key == KeyEvent.VK_ESCAPE) {
            if (timer.isRunning())
                timer.stop();
            else
                timer.start();
        }
    }

    private void spawnEnemies() {
        if (enemies.size() < 5) {
            if (genEnemyChance() > 8) {
                Enemy enemy = new Enemy(frameWidth + 150, LAND_HEIGHT - 81 + 5);
                enemies.add(enemy);
            }
        }
    }

    private int genEnemyChance() {
        int temp = random.nextInt(10) + 1;
        return temp;
    }
}