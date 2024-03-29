package SETA.Data;

import com.example.grpc.TaxiNetworkServiceOuterClass;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represent a position in the grid of the city
 */
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

    public Position(TaxiNetworkServiceOuterClass.Position position){
        this.x = position.getX();
        this.y = position.getY();
    }

    public Position() {}

    public boolean equals(Position pos) {
        return this.x == pos.x && this.y == pos.y;
    }

    /**
     * Obtain the district number by the position
     * @return district number of this position
     */
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

    /**
     * Calculate the distance between this position and the one specified in the param
     * @param startingPosition position from wich you want the distance
     * @return euclidean distance between the two position
     */
    public double getDistance(Position startingPosition){
        double x = startingPosition.x - this.x;
        double y = startingPosition.y - this.y;
        //System.out.println("DISTANCE (" + this.x + "," + this.y + ")-(" + startingPosition.x + "," + startingPosition.y + ") ------> " + Math.sqrt(Math.pow(x,2) + Math.pow(y,2)));
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    /**
     * Get the position of the recharge station in the current district
     * @return the position of the recharge station
     */
    public Position getRechargeStation(){
        switch (this.getDistrict()){
            case 1: //NW
                return new Position(0, 0);
            case 2: //NE
                return new Position(0, 9);
            case 3: //SW
                return new Position(9, 0);
            default:
                return new Position(9, 9);
        }
    }

    /**
     * Map this position the position class of the proto file
     * @return position of the proto file
     */
    public TaxiNetworkServiceOuterClass.Position toProtoPosition(){
        return TaxiNetworkServiceOuterClass.Position.newBuilder().setX(this.x).setY(this.y).build();
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
