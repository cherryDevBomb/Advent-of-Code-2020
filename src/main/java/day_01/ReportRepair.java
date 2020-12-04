package day_01;

import util.InputReader;

import java.util.List;
import java.util.stream.Collectors;

public class ReportRepair {

    private static final String INPUT_FILE = "day_01/input.txt";

    public static void main(String[] args) {
        new ReportRepair().repairReport();
    }

    private void repairReport() {
        List<Integer> numbers = readInput();
        System.out.println("Product of 2 numbers that sum to 2020: " + getResultForTwoNumbers(numbers));
        System.out.println("Product of 3 numbers that sum to 2020: " + getResultForThreeNumbers(numbers));
    }

    /***
     * Find two entries in a list that sum to 2020 and then multiply those two numbers together.
     *
     * @param numbers List of Integer
     * @return product of those two numbers
     */
    private int getResultForTwoNumbers(List<Integer> numbers) {
        return numbers.stream()
                .filter(nr1 -> numbers.stream().anyMatch(nr2 -> (nr1 + nr2 == 2020)))
                .reduce((nr1, nr2) -> nr1 * nr2)
                .get();
    }

    /***
     * Find three entries in a list that sum to 2020 and then multiply those three numbers together.
     *
     * @param numbers List of Integer
     * @return product of those three numbers
     */
    private int getResultForThreeNumbers(List<Integer> numbers) {
        return numbers.stream()
                .filter(nr1 -> numbers.stream().anyMatch(nr2 -> numbers.stream().anyMatch(nr3 -> (nr1 + nr2 + nr3 == 2020))))
                .reduce((nr1, nr2) -> nr1 * nr2)
                .get();
    }

    private List<Integer> readInput() {
        return InputReader.readInputFile(INPUT_FILE)
                .stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
