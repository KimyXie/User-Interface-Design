package animationEventQueue;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.*;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

/**
 * This example recreates Xlib's explict event loop style of programming.
 * It is for pedagogical purposes only.  DO NOT USE THIS CODE for a
 * "real" Swing application!
 */
public class AnimationComponent extends JComponent {
    private Point mouse;
    private boolean inside = false;
    private int FPS = 30;
    private LinkedList<Star> stars = new LinkedList<Star>();
    private MyEventQueue myEventQueue;

    public AnimationComponent(MyEventQueue myEventQueue) {
        super();
        this.myEventQueue = myEventQueue;
    }

	/**
	 * Handles events from the event queue
	 */
    public void eventLoop() throws InterruptedException {
		// Fix this code to handle events and animation timely.
		// Do NOT use a Timer for this implementation.
        while (true) {
            while (this.myEventQueue.hasEvents()) {
                this.handleEvent(this.myEventQueue.nextEvent());
            }
            this.handleAnimation();
            this.paintComponent(this.getGraphics());
            Thread.sleep(1000 / this.FPS);
        }
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

    private void handleEvent(AWTEvent e) {
        if (e instanceof MouseEvent) {
            this.handleMouseEvent((MouseEvent) e);
        }
    }

    private void handleMouseEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                System.out.println("mouse click!");
                break;
            case MouseEvent.MOUSE_RELEASED:
                break;
            case MouseEvent.MOUSE_DRAGGED:
                //System.out.println("mouse dragged!");
                break;
            case MouseEvent.MOUSE_MOVED:
                if (this.inside) {
                    this.createStar(e.getPoint());
                }
                break;
            case MouseEvent.MOUSE_ENTERED:
                System.out.println("mouse entered!");
                this.inside = true;
                this.mouse = e.getPoint();
                break;
            case MouseEvent.MOUSE_EXITED:
                System.out.println("mouse exited!");
                this.inside = false;
                break;
            default:
                //System.out.println("Some other event " + e.getID());
        }
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