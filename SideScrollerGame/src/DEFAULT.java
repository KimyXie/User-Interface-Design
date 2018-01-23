
public final class DEFAULT {
    public static final Integer ONESECOND = 1000;
    public static final Integer HORIZONTAL_SIZE = 100;
    public static final Integer VERTICAL_SIZE = 10;
    public static final Integer SCROLL_SPEED = 1; // 1 block per second
    public static final Integer FPS = 30; // 30 frame per second
    public static final Integer DIR_TOP = 1;
    public static final Integer DIR_BOTTOM = 2;
    public static final Integer DIR_LEFT = 3;
    public static final Integer DIR_RIGHT = 4;
    public static final Integer PLAYER_INITIAL_H_LOCATION = 1; // the second column block
    public static final Integer PLAYER_INITIAL_V_LOCATION = 4; // middle of rows
    public static final Integer PLAYER_INITIAL_DIRECTION = DIR_RIGHT;
    public static final String  MAPFILE = "defaultmap.txt";
    public static final Integer GAMEVIEW_SIZE = 600;
    public static final Integer MENUVIEW_H_SIZE = 200;
    public static final Integer MENUVIEW_V_SIZE = 600;

    public static final Object[] DIALOG_ONE_OPTION = {"OK"};


    public static final Boolean DEBUG = true;

    public void DBprint(String str) {
        if (DEBUG) {
            System.err.println(str);
        }
    }

}