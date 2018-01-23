import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller implements ActionListener,
                                   MouseListener,
                                   KeyListener,
                                   WindowListener {

    /**
     *  Welcome part
     */
    Boolean inWelcomePage;
    WelcomeFrame welcomeframe;


    /**
     *  Game part
     */
    Boolean inGamePage;
    GameFrame gameframe;
    GameModel gamemodel;


    /**
     *  Level Editor part
     */
    Boolean inEditorPage;
    EditorFrame editorframe;
    EditorModel editormodel;

    /**
     *  Common used part
     */
    private Timer gameTimer;
    private Timer editorTimer;
    private Integer fps;




    public Controller() {
        this.gamemodel = new GameModel();
        this.editormodel = new EditorModel();

        this.welcomeframe = new WelcomeFrame(this);
        this.welcomeframe.packFrame();
        this.gameframe = new GameFrame(gamemodel, this);
        this.editorframe = new EditorFrame(editormodel);

        inGamePage = false;
        this.gameframe.setVisible(false);
        inEditorPage = false;
        this.editorframe.setVisible(false);
        inWelcomePage = false;
        this.welcomeframe.setVisible(false);

        this.fps = DEFAULT.FPS;
        this.gameTimer = new Timer(DEFAULT.ONESECOND / fps, this);
        this.editorTimer = new Timer(DEFAULT.ONESECOND / fps, this);

    }

    /**
     *  Only called by main() to start the program
     */
    public void programStart() {
        this.welcomeframe.setVisible(true);
        inWelcomePage = true;
    }

    /**
     *  called in various situations, program only exit in this function
     */
    public void programTerminate() {
        System.err.println("Program terminating");
        System.exit(0);
    }


    /**
     *  switch methods between frames
     */

    public void gameToWelcome() {
        inGamePage = false;
        gamemodel.pauseGame();

        gameframe.setVisible(false);
        gameTimer.stop();

        welcomeframe.setVisible(true);
        inWelcomePage = true;
    }

    public void editorToWelcome() {
        inEditorPage = false;
        editorframe.setVisible(false);
        editorTimer.stop();
        welcomeframe.setVisible(true);
        inWelcomePage = true;
    }

    public void welcomeToGame() {
        inWelcomePage = false;
        welcomeframe.setVisible(false);
        gameframe.setVisible(true);
        inGamePage = true;
        gameTimer.start();
    }

    public void welcomeToEditor() {
        inWelcomePage = false;
        welcomeframe.setVisible(false);
        editorframe.setVisible(true);
        inEditorPage = true;
        editorTimer.start();
    }



    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGamePage) {
            if (!gamemodel.gameIsPaused()) {
                gamemodel.incrementGameViewLeftBoundary();
                gamemodel.notifyObservers();
            }

            gameframe.repaint();
        }
        else if (inEditorPage) {
            gameframe.repaint();
        }
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        // System.err.println(e.toString());
        if (inGamePage) {
            if (!gamemodel.gameIsPaused()) {
                Character key = e.getKeyChar();

                if (e.getKeyCode() == KeyEvent.VK_W) {
                    gamemodel.playerMove(DEFAULT.DIR_TOP);
                }
                else if (e.getKeyCode() == KeyEvent.VK_S) {
                    gamemodel.playerMove(DEFAULT.DIR_BOTTOM);
                }
                else if (e.getKeyCode() == KeyEvent.VK_A) {
                    gamemodel.playerMove(DEFAULT.DIR_LEFT);
                }
                else if (e.getKeyCode() == KeyEvent.VK_D) {
                    gamemodel.playerMove(DEFAULT.DIR_RIGHT);
                }

                gamemodel.notifyObservers();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        Component cmpt = (Component)e.getSource();
        if (inWelcomePage) {
            if (cmpt.getName() == "welcomeGameButton") {
                welcomeToGame();
            }
            else if (cmpt.getName() == "welcomeEditorButton") {
                welcomeToEditor();
            }
            else if (cmpt.getName() == "welcomeExitButton") {
                int confirm = JOptionPane.showOptionDialog(null,
                        "Do you wanna exit the game?\n" +
                                "Any unsaved work will be lost.",
                        "Exit Game Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, null, null);
                if (confirm == 0) {
                    this.programTerminate();
                }
            }
        }
        else if (inGamePage) {
            if (cmpt.getName() == "SaveButton") {
                gamemodel.pauseGame();
                gamemodel.notifyObservers();
                gamemodel.savingGame();
            }
            else if (cmpt.getName() == "LoadButton") {
                gamemodel.pauseGame();
                int confirm = JOptionPane.showOptionDialog(null,
                        "Do you wanna load game from file?\n" +
                                "Any unsaved work will be lost.",
                        "Load Game Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, null, null);
                if (confirm == 0) {
                    gamemodel.loadingGame();
                }
            }
            else if (cmpt.getName() == "BackButton") {
                gamemodel.pauseGame();
                gamemodel.notifyObservers();
                this.gameToWelcome();
            }
            else if (cmpt.getName() == "ExitButton") {
                gamemodel.pauseGame();
                gamemodel.notifyObservers();
                int confirm = JOptionPane.showOptionDialog(null,
                        "Do you wanna exit the game?\n" +
                                "Any unsaved work will be lost.",
                        "Exit Game Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, null, null);
                if (confirm == 0) {
                    this.programTerminate();
                }
            }
            else if (cmpt.getName() == "SwitchButton") {
                if (gamemodel.gameIsPaused()) {
                    gamemodel.continueGame();
                }
                else {
                    gamemodel.pauseGame();
                }
            }
            else if (cmpt.getName() == "FPSButton") {
                gamemodel.pauseGame();
                gamemodel.notifyObservers();
                String s = (String)JOptionPane.showInputDialog(null,
                                                                "Change FPS speed from " +
                                                                        Integer.toString(gamemodel.getGameFPS()) +
                                                                        " to:\n" +
                                                                        "(Integer range [1, 100])",
                        "Change FPS speed", JOptionPane.PLAIN_MESSAGE,
                        null, null, null
                        );
                if (s != null) {
                    Integer newFPS = Integer.parseInt(s);
                    if (newFPS < 1 || newFPS > 100) {
                        JOptionPane.showOptionDialog(null,
                                "ERROR: inputted FPS value invalid,\n" +
                                        "expected Integer in range [1,100], saw " + s,
                                "Input Error",  JOptionPane.PLAIN_MESSAGE,JOptionPane.QUESTION_MESSAGE,
                                null, DEFAULT.DIALOG_ONE_OPTION, DEFAULT.DIALOG_ONE_OPTION[0]);
                    }
                    else {
                        gamemodel.setGameFPS(newFPS);
                        this.fps = newFPS;
                    }
                }
            }
            else if (cmpt.getName() == "ScrollButton") {
                gamemodel.pauseGame();
                gamemodel.notifyObservers();
                String s = (String)JOptionPane.showInputDialog(null,
                        "Change Scroll speed from " +
                                Integer.toString(gamemodel.getGameScrollSpeed()) +
                                " to:\n" +
                                "(Integer range [1, 100])",
                        "Change Scroll speed", JOptionPane.PLAIN_MESSAGE,
                        null, null, null
                );
                if (s != null) {
                    Integer newscrollspeed = Integer.parseInt(s);
                    if (newscrollspeed < 1 || newscrollspeed > 100) {
                        JOptionPane.showOptionDialog(null,
                                "ERROR: inputted Scroll speed value invalid,\n" +
                                        "expected Integer in range [1,100], saw " + s,
                                "Input Error",  JOptionPane.PLAIN_MESSAGE,JOptionPane.QUESTION_MESSAGE,
                                null, DEFAULT.DIALOG_ONE_OPTION, DEFAULT.DIALOG_ONE_OPTION[0]);;
                    }
                    else {
                        gamemodel.setGameScrollSpeed(newscrollspeed);
                    }
                }
            }

            gamemodel.notifyObservers();
        }
        else if (inEditorPage) {

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}



    /**
     * Invoked when the user attempts to close the window
     * from the window's system menu.
     *
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        if (inWelcomePage) {
            int confirm = JOptionPane.showOptionDialog(null,
                    "Do you wanna exit the game?",
                    "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, null, null);
            if (confirm == 0) {
                programTerminate();
            }
        }
        else if (inGamePage) {
            int confirm = JOptionPane.showOptionDialog(null,
                    "Do you wanna force quit the game?\n" +
                            "Yes for force quitting,\n" +
                            "No for pausing and returning to Welcome Page.",
                    "Force Quit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, null, null);
            if (confirm == 0) {
                this.programTerminate();
            }
            else {
                if (!gamemodel.gameIsPaused()) {
                    gamemodel.pauseGame();
                }
                this.gameToWelcome();
            }
        }
        else if (inEditorPage) {

        }

    }

    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
