package day_15;

import util.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        int number30000000 = getNthNumber(startingNumbers, 30000000);

        System.out.println("The 2020th number spoken: " + number2020);
        System.out.println("The 2020th number spoken: " + number30000000);
    }

    /**
     * Calculate the nth number that will be spoken according to the game rules:
     * If that was the first time the number has been spoken, the current player says 0.
     * Otherwise, the number had been spoken before; the current player announces how many turns apart the number is from when it was previously spoken.
     *
     * @param startingNumbers list of starting numbers
     * @param n               number
     * @return nth number that will be spoken.
     */
    private int getNthNumber(List<Integer> startingNumbers, int n) {
        Map<Integer, Integer> occurrences = startingNumbers.stream()
                .collect(Collectors.toMap(nr -> nr, nr -> startingNumbers.indexOf(nr) + 1));

        int turn = startingNumbers.size();
        int lastSpokenNumber = startingNumbers.get(startingNumbers.size() - 1);
        while (turn < n) {
            int nextNumber = occurrences.containsKey(lastSpokenNumber) ? turn - occurrences.get(lastSpokenNumber) : 0;
            occurrences.put(lastSpokenNumber, turn++);
            lastSpokenNumber = nextNumber;
        }
        return lastSpokenNumber;
    }

    /**
     * Calculate the nth number that will be spoken according to the game rules.
     * This solution is the not the most optimized and not suitable for part 2.
     *
     * @param startingNumbers list of starting numbers
     * @param n               number
     * @return nth number that will be spoken.
     */
    private int getNthNumberWithoutMap(List<Integer> startingNumbers, int n) {
        List<Integer> spokenNumbers = new ArrayList<>(n);
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
