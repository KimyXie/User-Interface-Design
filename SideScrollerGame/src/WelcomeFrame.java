import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WelcomeFrame extends JFrame {

    private Controller controller;

    private JPanel instructionPanel;
    private JPanel directionPanel;

    public WelcomeFrame(Controller controller) {
        // Set up the window.
        this.setTitle("Hunters Welcome Page");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(controller);

        this.controller = controller;

        this.instructionPanel = new JPanel();
        this.instructionPanel.setName("InstructionPanel");
        this.instructionPanel.setPreferredSize(new Dimension(800, 400));

        JTextArea instruction = new JTextArea();
        instruction.setEditable(false);
        instruction.setPreferredSize(new Dimension(800, 400));
        instruction.setText("The Hunter - Side Scrolling Game\n\n"
                + "Control your flying aircraft (identified as inverted triangle) to reach the destination.\n\n"
                + "Players need to control aircraft to avoid obstacles (identified as grey square).\n\n"
                + "Directions controlled by keyboard inputs:\n"
                + "        W -- UP\n"
                + "        S -- DOWN\n"
                + "        A -- LEFT\n"
                + "        D -- RIGHT\n\n"
                + "How to win: reach the right end of the map without collision with obstacles or falling back.");
        
        this.instructionPanel.add(instruction);

        this.directionPanel = new JPanel();
        this.directionPanel.setName("DirectionPanel");
        this.directionPanel.setPreferredSize(new Dimension(800, 200));

        JPanel directionLeftPanel = new JPanel();
        directionLeftPanel.setPreferredSize(new Dimension(150, 200));
        JButton gameButton = new JButton();
        gameButton.setPreferredSize(new Dimension(150, 100));
        gameButton.setText("Go To Game Page");
        gameButton.setName("welcomeGameButton");
        gameButton.setHorizontalAlignment(SwingConstants.CENTER);
        gameButton.addMouseListener(controller);
        directionLeftPanel.add(gameButton, BorderLayout.CENTER);

        JPanel directionCentrePanel = new JPanel();
        directionCentrePanel.setPreferredSize(new Dimension(150, 200));
        JButton editorButton = new JButton();
        editorButton.setPreferredSize(new Dimension(150, 100));
        editorButton.setText("Go To Level Editor");
        editorButton.setName("welcomeEditorButton");
        editorButton.setHorizontalAlignment(SwingConstants.CENTER);
        editorButton.addMouseListener(controller);
        directionCentrePanel.add(editorButton, BorderLayout.CENTER);

        JPanel directionRightPanel = new JPanel();
        directionRightPanel.setPreferredSize(new Dimension(100, 200));
        JButton exitButton = new JButton();
        exitButton.setPreferredSize(new Dimension(100, 100));
        exitButton.setText("Exit");
        exitButton.setName("welcomeExitButton");
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.addMouseListener(controller);
        directionRightPanel.add(exitButton, BorderLayout.CENTER);

        this.directionPanel.add(directionLeftPanel);
        this.directionPanel.add(directionCentrePanel);
        this.directionPanel.add(directionRightPanel);
        this.setVisible(false);
    }

    public void packFrame() {

        setLayout(new BorderLayout());
        add(instructionPanel, BorderLayout.NORTH);
        add(directionPanel, BorderLayout.SOUTH);

        pack();
    }

}
