package animationEventQueue;

import java.awt.*;
import java.util.*;

/**
 * This example recreates Xlib's explict event loop style of programming.
 * It is for pedagogical purposes only.  DO NOT USE THIS CODE for a
 * "real" Swing application!
 * YOU DO NOT NEED TO UNDERSTAND THE CODE IN THIS FILE.
 */

public class MyEventQueue {
    private Queue<AWTEvent> queue = new LinkedList<AWTEvent>();

    public MyEventQueue() {
        EventProcessingThread eventProcessor = new EventProcessingThread(this);
        eventProcessor.start();
    }

    public synchronized void addEvent(AWTEvent event) {
        this.queue.add(event);
    }

    public synchronized boolean hasEvents() {
        return this.queue.size() > 0;
    }

    public synchronized AWTEvent nextEvent() {
        return this.queue.poll();
    }
}

class EventProcessingThread extends Thread {
    private MyEventQueue myEventQueue;

    public EventProcessingThread(MyEventQueue myEventQueue) {
        this.myEventQueue = myEventQueue;
    }

    @Override
    public void run() {
        EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        while (true) {
            try {
                AWTEvent event = eventQueue.getNextEvent();
                this.myEventQueue.addEvent(event);
            } catch (InterruptedException e) {
            }
        }
    }
}
