
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;

public class GameFrame extends JFrame implements Observer {
    /**
     *  Model
     */
    private GameModel model;


    /**
     *  View Components
     */
    private GameView view;
    private GameMenu menu;

    public GameFrame(GameModel model, Controller controller) {
        // Set up the window.
        this.setTitle("Hunters");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(controller);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);

        /**
         *  Initialize View Components
         */
        this.menu = new GameMenu(model, controller);
        GameMap map = new GameMap(model, controller);
        this.view = new GameView(model, map);

        addKeyListener(controller);

        this.setLayout(new BorderLayout());
        this.add(this.menu, BorderLayout.LINE_START); // left
        this.add(this.view, BorderLayout.LINE_END);   // right

        this.setVisible(false);
        pack();
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        this.model = (GameModel)observable;
        if (model.isPlayerDied()) {
            JOptionPane.showOptionDialog(null,
                    "Player died, Game Over\n"
                            + "Your Score: " + Integer.toString(model.getScore()) + "\n"
                            + "The game will terminate, please restart for another round",
                    "Game Over", JOptionPane.PLAIN_MESSAGE,JOptionPane.QUESTION_MESSAGE,
                    null, DEFAULT.DIALOG_ONE_OPTION, DEFAULT.DIALOG_ONE_OPTION[0]);
            System.exit(0);
        }

        if (model.isPlayerWin()) {
            JOptionPane.showOptionDialog(null,
                    "Congratulation! You have won the game!\n"
                            + "Your Score: " + Integer.toString(model.getScore()) + "\n"
                            + "The game will terminate, please restart for another round",
                    "You Win", JOptionPane.PLAIN_MESSAGE,JOptionPane.QUESTION_MESSAGE,
                    null, DEFAULT.DIALOG_ONE_OPTION, DEFAULT.DIALOG_ONE_OPTION[0]);
            System.exit(0);
        }
    }
}
