import javax.swing.*;
import java.awt.*;

/**
 * Created by kimyj on 2017/7/5.
 */
public class GameMenu extends JPanel implements Observer {
    private GameModel model;

    private JTextField scoreboard;
    private JButton gameFPS;
    private JButton gameScrollSpeed;
    private JButton gameSwitch;
    private JButton gameSave;
    private JButton gameLoad;
    private JButton gameBack;
    private JButton gameExit;

    public GameMenu(GameModel model, Controller controller) {
        this.setPreferredSize(new Dimension(DEFAULT.MENUVIEW_H_SIZE, DEFAULT.MENUVIEW_V_SIZE));
        this.setMaximumSize(new Dimension(DEFAULT.MENUVIEW_H_SIZE, DEFAULT.MENUVIEW_V_SIZE));

        this.model = model;
        model.addObserver(this);

        this.scoreboard = new JTextField();
        this.scoreboard.setText("Score: 0");
        this.scoreboard.setName("ScoreBoard");
        this.scoreboard.setHorizontalAlignment(SwingConstants.CENTER);
        this.scoreboard.setPreferredSize(new Dimension(150,100));
        this.scoreboard.setEditable(false);
        this.gameFPS = new JButton();
        this.gameFPS.setText("Change FPS speed");
        this.gameFPS.setName("FPSButton");
        this.gameFPS.setHorizontalAlignment(SwingConstants.CENTER);
        this.gameFPS.addMouseListener(controller);
        this.gameFPS.setPreferredSize(new Dimension(150,50));
        this.gameScrollSpeed = new JButton();
        this.gameScrollSpeed.setText("Change Scroll spd");
        this.gameScrollSpeed.setName("ScrollButton");
        this.gameScrollSpeed.setHorizontalAlignment(SwingConstants.CENTER);
        this.gameScrollSpeed.addMouseListener(controller);
        this.gameScrollSpeed.setPreferredSize(new Dimension(150,50));
        this.gameSwitch = new JButton();
        this.gameSwitch.setText("Start");
        this.gameSwitch.setName("SwitchButton");
        this.gameSwitch.setHorizontalAlignment(SwingConstants.CENTER);
        this.gameSwitch.setPreferredSize(new Dimension(150,50));
        this.gameSwitch.addMouseListener(controller);
        this.gameSave = new JButton();
        this.gameSave.setText("Save Game");
        this.gameSave.setName("SaveButton");
        this.gameSave.setHorizontalAlignment(SwingConstants.CENTER);
        this.gameSave.setPreferredSize(new Dimension(150,50));
        this.gameSave.addMouseListener(controller);
        this.gameLoad = new JButton();
        this.gameLoad.setText("Load Game");
        this.gameLoad.setName("LoadButton");
        this.gameLoad.setHorizontalAlignment(SwingConstants.CENTER);
        this.gameLoad.setPreferredSize(new Dimension(150,50));
        this.gameLoad.addMouseListener(controller);
        this.gameBack = new JButton();
        this.gameBack.setText("To Welcome Page");
        this.gameBack.setName("BackButton");
        this.gameBack.setHorizontalAlignment(SwingConstants.CENTER);
        this.gameBack.setPreferredSize(new Dimension(150,50));
        this.gameBack.addMouseListener(controller);
        this.gameExit = new JButton();
        this.gameExit.setText("Exit");
        this.gameExit.setName("ExitButton");
        this.gameExit.setHorizontalAlignment(SwingConstants.CENTER);
        this.gameExit.setPreferredSize(new Dimension(150,50));
        this.gameExit.addMouseListener(controller);

        this.add(this.scoreboard);
        this.add(this.gameFPS);
        this.add(this.gameScrollSpeed);
        this.add(this.gameSwitch);
        this.add(this.gameSave);
        this.add(this.gameLoad);
        this.add(this.gameBack);
        this.add(this.gameExit);



    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        this.model = (GameModel)observable;

        /**
         *  Update gameSwitch Button
         */
        if (!model.gameIsPaused()) {
            this.gameSwitch.setText("Pause");
        }
        else if (model.gameHasStarted()) {
            this.gameSwitch.setText("Continue");
        }
        else { // game not start, and is paused
            this.gameSwitch.setText("Start");
        }

        /**
         *  Update scoreboard Text Field
         */
        this.scoreboard.setText("Score: " + Integer.toString(model.getScore()));
    }
}
