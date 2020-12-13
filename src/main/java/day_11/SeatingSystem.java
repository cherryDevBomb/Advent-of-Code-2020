package day_11;

import org.apache.commons.lang3.tuple.Pair;
import util.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SeatingSystem {

    private static final String INPUT_FILE = "input_11.txt";

    private static final Character EMPTY = 'L';
    private static final Character OCCUPIED = '#';
    private static final Character FLOOR = '.';

    public static void main(String[] args) {
        new SeatingSystem().simulateSeatingArea();
    }

    private void simulateSeatingArea() {
        List<List<Character>> seatLayout = InputReader.readInputFile(INPUT_FILE).stream()
                .map(line -> line.chars().boxed().map(code -> (char) code.intValue()).collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<List<Character>> finalSeatLayout1 = getStaticSeatLayout(seatLayout, this::getNumberOfAdjacentOccupiedSeats, 4);
        long nrOccupied1 = getNumberOfOccupiedSeats(finalSeatLayout1);

        List<List<Character>> finalSeatLayout2 = getStaticSeatLayout(seatLayout, this::getNumberOfFirstSeenOccupiedSeats, 5);
        long nrOccupied2 = getNumberOfOccupiedSeats(finalSeatLayout2);

        System.out.println("Number of occupied seats in the final seat layout using first rules: " + nrOccupied1);
        System.out.println("Number of occupied seats in the final seat layout using second rules: " + nrOccupied2);
    }

    /**
     * Calculate the final state of the seat layout.
     * After applying enough rounds of rules, further applications of these rules cause no seats to change state.
     *
     * @param seatLayout       matrix that describes initial state of the seats
     * @param visibilityMethod specifies what method should be applied to determine which seats are adjacent
     * @param maxTolerated     minimum number of adjacent seats occupied that would cause a seat to become empty
     * @return matrix that describes final state of the seats
     */
    private List<List<Character>> getStaticSeatLayout(List<List<Character>> seatLayout, VisibilityMethod visibilityMethod, int maxTolerated) {
        List<List<Character>> newSeatLayout = performOneRound(seatLayout, visibilityMethod, maxTolerated);
        if (!seatLayoutEquals(seatLayout, newSeatLayout)) {
            return getStaticSeatLayout(newSeatLayout, visibilityMethod, maxTolerated);
        }
        return newSeatLayout;
    }

    /**
     * Compute the state of the seat layout after applying one round of rules.
     * The following rules are applied to every seat simultaneously:
     * - If a seat is empty (L) and there are no occupied seats adjacent to it according to visibility method, the seat becomes occupied.
     * - If a seat is occupied (#) and maxTolerated or more seats adjacent to it are also occupied, the seat becomes empty.
     * - Otherwise, the seat's state does not change.
     *
     * @param seatLayout       matrix that describes the current state of the seats
     * @param visibilityMethod specifies what method should be applied to determine which seats are adjacent
     * @param maxTolerated     minimum number of adjacent seats occupied that would cause a seat to become empty
     * @return matrix that describes the state of the seats after applying the rules once to each seat
     */
    private List<List<Character>> performOneRound(List<List<Character>> seatLayout, VisibilityMethod visibilityMethod, int maxTolerated) {
        return IntStream.range(0, seatLayout.size())
                .mapToObj(row -> IntStream.range(0, seatLayout.get(row).size())
                        .mapToObj(col -> {
                            Character currentState = seatLayout.get(row).get(col);
                            long adjacentOccupied = visibilityMethod.getNumberOfOccupiedSeats(seatLayout, row, col);
                            if (EMPTY.equals(currentState) && adjacentOccupied == 0) {
                                return OCCUPIED;
                            } else if (OCCUPIED.equals(currentState) && adjacentOccupied >= maxTolerated) {
                                return EMPTY;
                            }
                            return currentState;
                        }).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    /**
     * Calculate the number of occupied seats adjacent to a given seat (one of the eight positions immediately up, down, left, right, or diagonal from the seat).
     *
     * @param seatLayout matrix that describes the current state of the seats
     * @param row        row of current seat in the matrix
     * @param col        column of current seat in the matrix
     * @return number of adjacent seats that are occupied
     */
    private long getNumberOfAdjacentOccupiedSeats(List<List<Character>> seatLayout, int row, int col) {
        List<Pair<Integer, Integer>> adjacentSeats = Arrays.asList(
                Pair.of(row - 1, col - 1),
                Pair.of(row - 1, col),
                Pair.of(row - 1, col + 1),
                Pair.of(row, col - 1),
                Pair.of(row, col + 1),
                Pair.of(row + 1, col - 1),
                Pair.of(row + 1, col),
                Pair.of(row + 1, col + 1)
        );

        return getNumberOfOccupiedSeatsFromSet(seatLayout, adjacentSeats);
    }

    /**
     * Calculate the number of occupied seats first seen from a given seat in each of those eight directions(up, down, left, right or diagonal)
     *
     * @param seatLayout matrix that describes the current state of the seats
     * @param row        row of current seat in the matrix
     * @param col        column of current seat in the matrix
     * @return number of adjacent seats that are occupied
     */
    private long getNumberOfFirstSeenOccupiedSeats(List<List<Character>> seatLayout, int row, int col) {
        int upRow = row - 1;
        while (upRow >= 0 && FLOOR.equals(seatLayout.get(upRow).get(col))) {
            upRow--;
        }

        int downRow = row + 1;
        while (downRow < seatLayout.size() && FLOOR.equals(seatLayout.get(downRow).get(col))) {
            downRow++;
        }

        int leftCol = col - 1;
        while (leftCol >= 0 && FLOOR.equals(seatLayout.get(row).get(leftCol))) {
            leftCol--;
        }

        int rightCol = col + 1;
        while (rightCol < seatLayout.get(row).size() && FLOOR.equals(seatLayout.get(row).get(rightCol))) {
            rightCol++;
        }

        int upperLeftDiagonalRow = row - 1;
        int upperLeftDiagonalCol = col - 1;
        while (upperLeftDiagonalRow >= 0 && upperLeftDiagonalCol >= 0 && FLOOR.equals(seatLayout.get(upperLeftDiagonalRow).get(upperLeftDiagonalCol))) {
            upperLeftDiagonalRow--;
            upperLeftDiagonalCol--;
        }

        int upperRightDiagonalRow = row - 1;
        int upperRightDiagonalCol = col + 1;
        while (upperRightDiagonalRow >= 0 && upperRightDiagonalCol < seatLayout.get(0).size() && FLOOR.equals(seatLayout.get(upperRightDiagonalRow).get(upperRightDiagonalCol))) {
            upperRightDiagonalRow--;
            upperRightDiagonalCol++;
        }

        int lowerLeftDiagonalRow = row + 1;
        int lowerLeftDiagonalCol = col - 1;
        while (lowerLeftDiagonalRow < seatLayout.size() && lowerLeftDiagonalCol >= 0 && FLOOR.equals(seatLayout.get(lowerLeftDiagonalRow).get(lowerLeftDiagonalCol))) {
            lowerLeftDiagonalRow++;
            lowerLeftDiagonalCol--;
        }

        int lowerRightDiagonalRow = row + 1;
        int lowerRightDiagonalCol = col + 1;
        while (lowerRightDiagonalRow < seatLayout.size() && lowerRightDiagonalCol < seatLayout.get(row).size() && FLOOR.equals(seatLayout.get(lowerRightDiagonalRow).get(lowerRightDiagonalCol))) {
            lowerRightDiagonalRow++;
            lowerRightDiagonalCol++;
        }

        List<Pair<Integer, Integer>> adjacentSeats = Arrays.asList(
                Pair.of(upperLeftDiagonalRow, upperLeftDiagonalCol),
                Pair.of(upRow, col),
                Pair.of(upperRightDiagonalRow, upperRightDiagonalCol),
                Pair.of(row, leftCol),
                Pair.of(row, rightCol),
                Pair.of(lowerLeftDiagonalRow, lowerLeftDiagonalCol),
                Pair.of(downRow, col),
                Pair.of(lowerRightDiagonalRow, lowerRightDiagonalCol)
        );

        return getNumberOfOccupiedSeatsFromSet(seatLayout, adjacentSeats);
    }

    /**
     * Calculate how many from the given seats are occupied in the given seat layout
     *
     * @param seatLayout matrix that describes the current state of the seats
     * @param seats      coordinates of the analyzed seats described as pairs of row, column
     * @return number of occupied seats from the given list of seats
     */
    private long getNumberOfOccupiedSeatsFromSet(List<List<Character>> seatLayout, List<Pair<Integer, Integer>> seats) {
        return seats.stream()
                .filter(seat -> seat.getLeft() >= 0 && seat.getRight() >= 0 && seat.getLeft() < seatLayout.size() && seat.getRight() < seatLayout.get(0).size())
                .filter(seat -> OCCUPIED.equals(seatLayout.get(seat.getLeft()).get(seat.getRight())))
                .count();
    }

    /**
     * Calculate the total number of occupied seats in a given seat layout
     *
     * @param seatLayout matrix that describes the current state of the seats
     * @return total number of occupied seats
     */
    private long getNumberOfOccupiedSeats(List<List<Character>> seatLayout) {
        return seatLayout.stream()
                .mapToLong(row -> row.stream().filter(OCCUPIED::equals).count())
                .sum();
    }
    
    private boolean seatLayoutEquals(List<List<Character>> seatLayout1, List<List<Character>> seatLayout2) {
        return IntStream.range(0, seatLayout1.size())
                .allMatch(row -> IntStream.range(0, seatLayout1.get(row).size())
                        .allMatch(col -> seatLayout1.get(row).get(col).equals(seatLayout2.get(row).get(col))));
    }

    private void printSeatLayout(List<List<Character>> seatLayout) {
        seatLayout.forEach(row -> {
            row.forEach(System.out::print);
            System.out.println();
        });
    }
}
