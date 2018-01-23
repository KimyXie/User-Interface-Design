
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class GameModel extends Observable {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;


    private Player player;


    /** Game world                V  *  H
     *  for integers inside: -1 for player, 0 for space, 1 for obstacles
     */
    private Integer[][] map;
    private Integer v_size;
    private Integer h_size;

    private Integer block_size;

    private Integer distanceReached;

    public Boolean inGame;  // whether game has started or not

    private Boolean isPaused;

    private Boolean playerIsDied;
    private Boolean playerWon;


    /**
     *  For game scroll use
     */
    private Integer gameFPS;
    private Integer gameScrollSpeed;
    private Double scrollPixelPerFrame;
    private Integer gameLeftBoundary;       // indices, increment by 1
    private Integer gameLeftBoundaryMax;    // maximum index that could be scrolled
    private Integer gameRightBoundary;
    private Integer gameRightBoundaryMax;
    private Integer gameTopBoundary;        // fixed index
    private Integer gameBottomBoundary;     // fixed index
    private Integer gameViewLeftBoundary;   // pixel position, increment by swing Timer
    private Timer scrollTimer;



    /**
     * Create a new model.
     */
    public GameModel() {

        this.observers = new ArrayList<>();
        player = new Player(DEFAULT.PLAYER_INITIAL_H_LOCATION, DEFAULT.PLAYER_INITIAL_V_LOCATION);

        this.h_size = DEFAULT.HORIZONTAL_SIZE;
        this.v_size = DEFAULT.VERTICAL_SIZE;
        map = new Integer[this.v_size][this.h_size];

        this.block_size = DEFAULT.GAMEVIEW_SIZE / this.v_size;

        loadDefaultMap();

        distanceReached = 0;
        this.inGame = false;
        this.isPaused = true;


        this.gameFPS = DEFAULT.FPS;
        this.gameScrollSpeed = DEFAULT.SCROLL_SPEED;
        this.scrollPixelPerFrame = this.gameScrollSpeed.doubleValue() * block_size / this.gameFPS;
        this.gameLeftBoundary = 0;
        this.gameLeftBoundaryMax = this.h_size - this.v_size - 1;
        this.gameViewLeftBoundary = 0;


        this.gameRightBoundary = this.v_size - 1;
        this.gameRightBoundaryMax = this.h_size - 1;
        this.gameTopBoundary = 0;
        this.gameBottomBoundary = this.v_size - 1;

        playerIsDied = false;
        playerWon = false;

    }

    public GameModel(String filename) {
        this.observers = new ArrayList();
        loadMap(filename);
        distanceReached = 0;
        this.inGame = false;
    }

    private void loadDefaultMap() {
        BufferedReader read;

        try {
            read = new BufferedReader(new FileReader(DEFAULT.MAPFILE));

            for (int i = 0; i < this.v_size; i++) {
                String[] numarr = read.readLine().split(" ");
                for (int j = 0; j < this.h_size; j++) {
                    map[i][j] = Integer.parseInt(numarr[j]);
                }
            }
        }
        catch (Exception e) {
            System.err.println("ERROR: Exception on loadDefaultMap");
            System.err.println(e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void loadMap(String filename) {
        BufferedReader read;

        try {
            read = new BufferedReader(new FileReader(filename));

            String[] numarr = read.readLine().split(" ");
            this.v_size = Integer.parseInt(numarr[0]);
            this.h_size = Integer.parseInt(numarr[1]);
            map = new Integer[this.v_size][this.h_size];
            if (numarr.length == 4) {
                Integer p_v_axis = Integer.parseInt(numarr[2]);
                Integer p_h_axis = Integer.parseInt(numarr[3]);
                this.player = new Player(p_h_axis, p_v_axis);
            }
            else {
                this.player = new Player(DEFAULT.PLAYER_INITIAL_H_LOCATION, this.v_size / 2);
            }

            for (int i = 0; i < this.v_size; i++) {
                numarr = read.readLine().split(" ");
                for (int j = 0; j < this.h_size; j++) {
                    map[i][j] = Integer.parseInt(numarr[j]);
                }
            }
        }
        catch (Exception e) {
            System.err.println("ERROR: Exception on loadMap");
            System.err.println(e.toString());
            e.printStackTrace();
            System.exit(1);
        }

    }

    private Boolean isCollision(Integer h, Integer v) {
        if (h < this.gameLeftBoundary) {
            return true;
        }

        if (this.map[v][h] == 1) {
            return true;
        }

        return false;
    }

    private void playerDeath() {
        this.inGame = false;
        pauseGame();
        playerIsDied = true;
        System.out.println("Player died");
        notifyObservers();
    }

    private void playerWin() {
        this.inGame = false;
        pauseGame();
        playerWon = true;
        System.out.println("Player win");
        notifyObservers();
    }

    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    public Integer[][] getMap() {
        return this.map;
    }

    public Integer getH_size() {
        return this.h_size;
    }

    public Integer getV_size() {
        return this.v_size;
    }

    public void playerMove(Integer direction) {
        Integer player_h = player.getH();
        Integer player_v = player.getV();
        Integer player_h_new = 0;
        Integer player_v_new = 0;

        System.err.println(direction);

        switch (direction) {
            case 1: // TOP
                if (player_v <= gameTopBoundary) {
                    player_h_new = player_h;
                    player_v_new = player_v;
                }
                else {
                    if (isCollision(player_h, player_v - 1)) {
                        playerDeath();
                    }
                    else {
                        player_h_new = player_h;
                        player_v_new = player_v - 1;
                    }
                }

                break;
            case 2: // BOTTOM
                if (player_v >= gameBottomBoundary) {
                    player_h_new = player_h;
                    player_v_new = player_v;
                }
                else {
                    if (isCollision(player_h, player_v + 1)) {
                        playerDeath();
                    }
                    else {
                        player_h_new = player_h;
                        player_v_new = player_v + 1;
                    }
                }
                break;
            case 3: // LEFT
                if (isCollision(player_h - 1, player_v)) {
                    playerDeath();
                }
                else {
                    player_h_new = player_h - 1;
                    player_v_new = player_v;
                }
                break;
            default: // RIGHT
                if (player_h >= gameRightBoundary) {
                    player_h_new = player_h;
                    player_v_new = player_v;
                }
                else {
                    if (isCollision(player_h+1, player_v)) {
                        playerDeath();
                    }
                    else {
                        player_h_new = player_h + 1;
                        player_v_new = player_v;
                        if (this.distanceReached < player_h_new) {
                            this.distanceReached = player_h_new;
                            if (DEFAULT.DEBUG) {
                                System.out.printf("DistanceReached changed to %d\n",this.distanceReached);
                            }

                            if (this.distanceReached >= gameRightBoundaryMax) {
                                playerWin();
                            }
                        }
                    }
                }
                break;
        }

        System.err.println(inGame);

        if (inGame) {
            map[player_v][player_h] = 0;
            map[player_v_new][player_h_new] = -1;
            player.move(player_h_new, player_v_new, direction);
            if (DEFAULT.DEBUG) {
                System.out.printf("Player Move from v=%d,h=%d to v=%d,h=%d\n",player_v, player_h, player_v_new, player_h_new);
            }
        }

    }

    public Integer getScore() {
        return this.distanceReached;
    }

    public Boolean gameIsPaused() {
        return this.isPaused;
    }

    public Boolean gameHasStarted() {
        return this.inGame;
    }

    public void pauseGame() {
        isPaused = true;
        stopScrollTimer();
    }

    public void continueGame() {
        inGame = true;
        isPaused = false;
        startScrollTimer();
    }

    public void savingGame() {

    }

    public void loadingGame() {

    }

    public Integer getGameFPS() {
        return gameFPS;
    }

    public Integer getGameScrollSpeed() {
        return gameScrollSpeed;
    }

    public void setGameFPS(Integer gameFPS) {
        this.gameFPS = gameFPS;
        this.scrollPixelPerFrame = this.gameScrollSpeed.doubleValue() * block_size / this.gameFPS;
    }

    public void setGameScrollSpeed(Integer gameScrollSpeed) {
        this.gameScrollSpeed = gameScrollSpeed;
        this.scrollPixelPerFrame = this.gameScrollSpeed.doubleValue() * block_size / this.gameFPS;

        stopScrollTimer();
        startScrollTimer();
    }

    public Player getPlayer() {
        return player;
    }

    public void startScrollTimer() {
        this.scrollTimer = new Timer();
        this.scrollTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused && gameLeftBoundary < gameLeftBoundaryMax && gameRightBoundary < gameRightBoundaryMax) {
                    gameLeftBoundary++;
                    gameRightBoundary++;

                    if (isCollision(player.h_axis, player.v_axis)) {
                        playerDeath();
                    }
                }

            }
        }, 0, DEFAULT.ONESECOND / gameScrollSpeed);
    }

    public void stopScrollTimer() {
        if (this.scrollTimer != null) {
            this.scrollTimer.cancel();
        }
    }

    public void incrementGameViewLeftBoundary() {
        gameViewLeftBoundary += scrollPixelPerFrame.intValue();
    }

    public Integer getGameViewLeftBoundary() {
        return gameViewLeftBoundary;
    }

    public Boolean isPlayerDied() {
        return playerIsDied;
    }

    public Boolean isPlayerWin() {
        return playerWon;
    }
}
