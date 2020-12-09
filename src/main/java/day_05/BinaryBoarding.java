package day_05;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import util.InputReader;

public class BinaryBoarding {

    private static final String INPUT_FILE = "input_05.txt";

    private static final char FRONT = 'F';
    private static final char BACK = 'B';
    private static final char LEFT = 'L';
    private static final char RIGHT = 'R';

    public static void main(String[] args) {
        new BinaryBoarding().processBoardingPasses();
    }

    /**
     * Find the only missing seat from a list of boarding passes.
     */
    private void processBoardingPasses() {
        List<Seat> seats = InputReader.readInputFile(INPUT_FILE)
                .stream()
                .map(this::getSeatFromPass)
                .collect(Collectors.toList());


        int maxId = seats.stream().max(Comparator.comparing(Seat::getId)).get().getId();
        int minId = seats.stream().min(Comparator.comparing(Seat::getId)).get().getId();
        int idsSum = seats.stream().mapToInt(Seat::getId).sum();

        // The sum of a continuous range of numbers is: maxId * (maxId + 1)  - (minId - 1) * minId) / 2
        int mySeatId = ((maxId * (maxId + 1) - (minId - 1) * minId) / 2) - idsSum;

        System.out.println("The highest seat ID on a boarding pass: " + maxId);
        System.out.println("My seat ID: " + mySeatId);
    }

    /**
     * Get the coordinates of a seat based on the boarding pass code.
     * The first 7 characters will either be F or B; these specify exactly one of the 128 rows on the plane (numbered 0 through 127).
     * Each letter tells you which half of a region the given seat is in: F means it is in the front (0 through 63); B means it is in the back (64 through 127)
     * Start with the whole list of rows and continue until you're left with exactly one row.
     * The last three characters specify exactly one of the 8 columns of seats on the plane (numbered 0 through 7).
     * The same process as above proceeds again: L means to keep the lower half, while R means to keep the upper half.
     *
     * @param boardingPass String composed of 7 letters F or B which specify the row and 3 letters L or R which specify the column.
     * @return seat
     */
    private Seat getSeatFromPass(String boardingPass) {
        Range rowRange = new Range(0, 127);
        Range columnRange = new Range(0, 7);

        boardingPass.chars().forEach(character -> {
            if (character == FRONT) {
                rowRange.getLowerHalf();
            } else if (character == BACK) {
                rowRange.getUpperHalf();
            } else if (character == LEFT) {
                columnRange.getLowerHalf();
            } else if (character == RIGHT) {
                columnRange.getUpperHalf();
            }
        });

        return new Seat(rowRange.getExactValue(), columnRange.getExactValue());
    }
}
