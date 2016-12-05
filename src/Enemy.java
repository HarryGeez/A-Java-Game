import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by weijiangan on 04/12/2016.
 */
public class Enemy {
    private final int FRAMES = 2;
    private int x, y;
    private int curFrame;
    private int speed;
    private Image sprite[] = new Image[FRAMES];

    public Enemy(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        for (int i = 0; i < FRAMES; i++) {
            sprite[i] = new ImageIcon(this.getClass().getResource("resources/Enemies/snailWalk" + (i+1) + ".png")).getImage();
        }
        curFrame = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Image getSprite() {
        return sprite[curFrame];
    }

    public void updatePos() {
        x += speed;
    }

    public void nextFrame() {
        if (curFrame == (FRAMES-1))
            curFrame = 0;
        else
            curFrame++;
    }

    public Rectangle getBounds() {
        return (new Rectangle(x, y, sprite[curFrame].getWidth(null), sprite[curFrame].getHeight(null)));
    }

    public BufferedImage getBI() {
        BufferedImage bi = new BufferedImage(sprite[curFrame].getWidth(null), sprite[curFrame].getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.getGraphics();
        g.drawImage(sprite[curFrame], 0, 0, null);
        g.dispose();
        return bi;
    }
}
