package day_11;

import java.util.List;

@FunctionalInterface
public interface VisibilityMethod {

    long getNumberOfOccupiedSeats(List<List<Character>> seatLayout, int row, int col);
}
