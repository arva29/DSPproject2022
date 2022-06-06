package SETA.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Position {
    public static final int FIRST_CELL_INDEX = 0;
    public static final int LAST_CELL_INDEX = 9;
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {}

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

    public double getDistance(Position startingPosition){
        double x = startingPosition.x - this.x;
        double y = startingPosition.y - this.y;
        return Math.sqrt(Math.exp(x) + Math.exp(y));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
