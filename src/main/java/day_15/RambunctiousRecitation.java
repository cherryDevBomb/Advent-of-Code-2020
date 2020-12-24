package day_15;

import util.InputReader;

import java.util.ArrayList;
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

        List<Integer> spokenNumbers = new ArrayList<>(startingNumbers);
        while (spokenNumbers.size() < 2020) {
            spokenNumbers.add(getNextNumber(spokenNumbers));
        }

        System.out.println("The 2020th number spoken: " + spokenNumbers.get(spokenNumbers.size() - 1));
    }

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
