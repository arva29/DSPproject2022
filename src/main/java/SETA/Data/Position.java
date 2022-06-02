package SETA.Data;

public class Position {
    public static final int FIRST_CELL_INDEX = 0;
    public static final int LAST_CELL_INDEX = 9;
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Position pos) {
        return this.x == pos.x && this.y == pos.y;
    }

    public int getDistrict() {
        if(x < 5 && y < 5) {
            return 1;
        } else if(x < 5){
            return 2;
        } else if(y < 5){
            return 3;
        } else {
            return 4;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
