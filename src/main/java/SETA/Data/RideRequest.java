package SETA.Data;

import java.util.Random;

public class RideRequest {
    private int id;
    private Position startPosition;
    private Position endPosition;

    public RideRequest(int id) {
        this.id = id;
        Random rn = new Random();

        this.startPosition = new Position(rn.nextInt((Position.LAST_CELL_INDEX - Position.FIRST_CELL_INDEX + 1) + Position.FIRST_CELL_INDEX),
                rn.nextInt((Position.LAST_CELL_INDEX - Position.FIRST_CELL_INDEX + 1) + Position.FIRST_CELL_INDEX));

        //this.startPosition = new Position(2,3);

        this.endPosition = new Position(rn.nextInt((Position.LAST_CELL_INDEX - Position.FIRST_CELL_INDEX + 1) + Position.FIRST_CELL_INDEX),
                rn.nextInt((Position.LAST_CELL_INDEX - Position.FIRST_CELL_INDEX + 1) + Position.FIRST_CELL_INDEX));

        //this.endPosition = new Position(4,1);

        while(this.startPosition.equals(this.endPosition)){
            this.endPosition = new Position(rn.nextInt((Position.LAST_CELL_INDEX - Position.FIRST_CELL_INDEX + 1) + Position.FIRST_CELL_INDEX),
                    rn.nextInt((Position.LAST_CELL_INDEX - Position.FIRST_CELL_INDEX + 1) + Position.FIRST_CELL_INDEX));
        }
    }

    public int getDistrict() {
        if(this.startPosition.getX() < 5 && this.startPosition.getY() < 5) {
            return 1;
        } else if(this.startPosition.getX() < 5 && this.startPosition.getY() >= 5){
            return 2;
        } else if(this.startPosition.getX() >= 5 && this.startPosition.getY() < 5){
            return 3;
        } else {
            return 4;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }
}
