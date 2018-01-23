package animationTimer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static java.lang.Math.*;

public interface Displayable {
    void paint(Graphics g);
}

class Star implements Displayable {
    private int x, y;
    private float alpha;

    public Star(int x, int y) {
        this.x = x;
        this.y = y;
        this.alpha = 1.0f;
    }

    public void fadeOut() {
        this.alpha -= 0.05f;
        if (this.alpha < 0.0f) {
            this.alpha = 0.0f;
        }
    }

    public boolean isVisible() {
        return this.alpha > 0.0f;
    }

    public void paint(Graphics g) {
        if (StarImage.getImage() != null) {
            Graphics2D g2 = (Graphics2D) g;
            Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
            g2.setComposite(comp);
            int width = StarImage.getImage().getWidth();
            int height = StarImage.getImage().getHeight();
            g2.drawImage(StarImage.getImage(), this.x - width / 2, this.y - height / 2, null);
        }
    }
}

class StarImage {
    private static BufferedImage theImage;

    public static BufferedImage getImage() {
        if (StarImage.theImage == null) {
            try {
                StarImage.theImage = ImageIO.read(new File(StarImage.class.getResource("/img/star.png").toURI()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return StarImage.theImage;
    }
}