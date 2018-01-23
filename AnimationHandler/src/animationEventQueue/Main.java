package animationEventQueue;

import java.awt.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyEventQueue myEventQueue = new MyEventQueue();
        AnimationComponent animationComponent = new AnimationComponent(myEventQueue);
        JFrame frame = new JFrame("Animation with Event Queue");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1,1));
        frame.setContentPane(animationComponent);
        frame.setSize(600, 500);
        frame.setVisible(true);
        animationComponent.eventLoop();
    }

}
