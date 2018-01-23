
import javax.swing.*;
import java.awt.*;


public class GameMap extends JPanel implements Observer {
    private GameModel model;

    Integer[][] gameMap;

    private Integer block_size;

    private Integer model_h_size;
    private Integer model_v_size;

    public GameMap(GameModel model, Controller controller) {
        this.model = model;
        this.gameMap = model.getMap();
        model.addObserver(this);

        this.model_h_size = model.getH_size();
        this.model_v_size = model.getV_size();

        this.block_size = DEFAULT.GAMEVIEW_SIZE / model_v_size;

        this.setPreferredSize(new Dimension(model_h_size * this.block_size, DEFAULT.GAMEVIEW_SIZE));
        this.addKeyListener(controller);
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // super.paintComponent(g);

        for (int i = 0; i < model_v_size; i++) {
            for (int j = 0; j < model_h_size; j++) {
                if (gameMap[i][j] == 1) {
                    g.setColor(Color.GRAY);
                    g.fillRect(j*block_size, i*block_size, block_size, block_size);
                }
                else if (gameMap[i][j] == -1) {
                    g.setColor(Color.BLACK);

                    switch(model.getPlayer().getDirection()) {
                        case 1:  // DEFAULT.DIR_TOP
                        {
                            int[] x_points = {j * block_size, (j + 1) * block_size, j * block_size + block_size / 2};
                            int[] y_points = {(i + 1) * block_size, (i + 1) * block_size, i * block_size};
                            g.fillPolygon(x_points, y_points, 3);
                        }
                            break;
                        case 2:  // DEFAULT.DIR_BOTTOM
                        {
                            int[] x_points = {j * block_size, (j + 1) * block_size, j * block_size + block_size / 2};
                            int[] y_points = {i * block_size, i * block_size, (i+1) * block_size};
                            g.fillPolygon(x_points, y_points, 3);
                        }
                            break;
                        case 3:  // DEFAULT.DIR_LEFT
                        {
                            int[] x_points = {j * block_size, (j + 1) * block_size, (j + 1) * block_size};
                            int[] y_points = {i * block_size + block_size / 2, i * block_size, (i+1) * block_size};
                            g.fillPolygon(x_points, y_points, 3);
                        }
                            break;
                        default: // DEFAULT.DIR_RIGHT
                        {
                            int[] x_points = {j * block_size, j * block_size, (j + 1) * block_size};
                            int[] y_points = {i * block_size, (i + 1) * block_size, i * block_size + block_size / 2};
                            g.fillPolygon(x_points, y_points, 3);
                        }
                            break;
                    }

                }
            }
        }

    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        this.model = (GameModel)observable;
        gameMap = model.getMap();
        this.requestFocusInWindow();
    }
}
