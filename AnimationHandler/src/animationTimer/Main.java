package animationTimer;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        AnimationComponent animationComponent = new AnimationComponent();
        JFrame frame = new JFrame("Animation with Timer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1,1));
        frame.setContentPane(animationComponent);
        frame.setSize(600, 500);
        frame.setVisible(true);
    }

}
