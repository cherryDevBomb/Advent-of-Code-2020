package day_15;

import util.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RambunctiousRecitation {

    private static final String INPUT_FILE = "input_15.txt";

    public static void main(String[] args) {
        new RambunctiousRecitation().playMemoryGame();
    }

    private void playMemoryGame() {
        List<Integer> startingNumbers = InputReader.readInputFile(INPUT_FILE)
                .stream()
                .map(sequence -> Arrays.asList(sequence.split(",")))
                .flatMap(List::stream)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int number2020 = getNthNumber(startingNumbers, 2020);

        System.out.println("The 2020th number spoken: " + number2020);
    }

    /**
     * Calculate the nth number that will be spoken according to the game rules.
     *
     * @param spokenNumbers list of starting numbers
     * @param n             number
     * @return nth number that will be spoken.
     */
    private int getNthNumber(List<Integer> spokenNumbers, int n) {
        while (spokenNumbers.size() < n) {
            spokenNumbers.add(getNextNumber(spokenNumbers));
        }
        return spokenNumbers.get(spokenNumbers.size() - 1);
    }

    /**
     * Given the list of previously spoken numbers, get the number that should be spoken at the next turn, based on the number spoken in the last turn:
     * If that was the first time the number has been spoken, the current player says 0.
     * Otherwise, the number had been spoken before; the current player announces how many turns apart the number is from when it was previously spoken.
     *
     * @param spokenNumbers list of previously spoken numbers
     * @return next number that should be spoken
     */
    private int getNextNumber(List<Integer> spokenNumbers) {
        List<Integer> numberOccurrences = IntStream.range(0, spokenNumbers.size())
                .filter(turn -> spokenNumbers.get(turn).equals(spokenNumbers.get(spokenNumbers.size() - 1)))
                .boxed()
                .collect(Collectors.toList());

        if (numberOccurrences.size() > 1) {
            return numberOccurrences.get(numberOccurrences.size() - 1) - numberOccurrences.get(numberOccurrences.size() - 2);
        }
        return 0;
    }
}
