import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class GameView extends JScrollPane implements Observer {

    private GameModel model;


    public GameView(GameModel model, Component map) {
        super(map);
        this.setPreferredSize(new Dimension(DEFAULT.GAMEVIEW_SIZE, DEFAULT.GAMEVIEW_SIZE));
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.setWheelScrollingEnabled(false);

        this.model = model;
        model.addObserver(this);

    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        this.model = (GameModel)observable;
        if (model.getGameViewLeftBoundary() < this.getHorizontalScrollBar().getMaximum() - DEFAULT.GAMEVIEW_SIZE) {
            this.getHorizontalScrollBar().setValue(model.getGameViewLeftBoundary());
        }
    }
}
