package day_17;

import java.util.List;

public interface Coordinate {

    List<? extends Coordinate> getNeighbourCoordinates();
}
