public class Player {
    Integer h_axis;
    Integer v_axis;
    Integer direction;

    Player(Integer h, Integer v) {
        this.h_axis = h;
        this.v_axis = v;
        this.direction = DEFAULT.PLAYER_INITIAL_DIRECTION;
    }

    public void move(Integer h, Integer v, Integer dir) {
        this.h_axis = h;
        this.v_axis = v;
        this.direction = dir;
    }

    public Integer getH() {
        return this.h_axis;
    }

    public Integer getV() {
        return this.v_axis;
    }

    public Integer getDirection() {
        return this.direction;
    }
}