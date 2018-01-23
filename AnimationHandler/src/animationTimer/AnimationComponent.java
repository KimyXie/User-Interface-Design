package animationTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

/**
 * This example shows how to create animations using Timers in Java.
 */
public class AnimationComponent extends JComponent {
    private boolean inside = false;
    private int FPS = 30;
    private LinkedList<Star> stars = new LinkedList<Star>();
    private Timer animationTimer;

    public AnimationComponent() {
        super();

        // Using Mouse Listeners to handle mouse events
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("mouse click!");
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("mouse entered!");
                AnimationComponent.this.inside = true;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("mouse exited!");
                AnimationComponent.this.inside = false;
            }
        });
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                if (AnimationComponent.this.inside) {
                    AnimationComponent.this.createStar(e.getPoint());
                }
            }
        });

        // Add a Timer to handle animation.
		// Your Timer should use this.handleAnimation() and this.repaint().

        // Using a Timer to handle animation.
        // Note that the following statement uses a lambda expression instead
        // of creating an anonymous class implementing ActionListener, but
        // both syntax are equivalent.
        this.animationTimer = new Timer(1000/this.FPS, event -> {
            this.handleAnimation();
            this.repaint(); // note that we call repaint, not paintComponent
        });
        this.animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // dBuff and gBuff are used for double buffering
        Image dBuff = new BufferedImage(this.getWidth(), this.getHeight(), TYPE_3BYTE_BGR);
        Graphics gBuff = dBuff.getGraphics();
        gBuff.setClip(0, 0, this.getWidth(), this.getHeight());
        gBuff.setColor(Color.CYAN);
        gBuff.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (Star s : this.stars) {
            s.paint(gBuff);
        }
        g.drawImage(dBuff, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    private void createStar(Point point) {
        Star star = new Star(point.x, point.y);
        this.stars.add(star);
    }

    private void handleAnimation() {
        ListIterator<Star> iter = this.stars.listIterator();
        while (iter.hasNext()) {
            Star s = iter.next();
            s.fadeOut();
            if (!s.isVisible()) {
                iter.remove();
            }
        }
    }
}