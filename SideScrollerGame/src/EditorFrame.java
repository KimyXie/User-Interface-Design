import javax.swing.*;
import java.awt.*;

public class EditorFrame extends JFrame implements Observer {

    private EditorModel model;


    public EditorFrame(EditorModel model) {
        // Set up the window.
        this.setTitle("Hunters Level Editor");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.addObserver(this);

        setVisible(true);
    }


    @Override
    public void update(Object observable) {

    }
}
