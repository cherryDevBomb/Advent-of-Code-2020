package day_03;

import util.InputReader;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TobogganTrajectory {

    private static final String INPUT_FILE = "input_03.txt";

    private static final char TREE = '#';

    public static void main(String[] args) {
        new TobogganTrajectory().countTrees();
    }

    private void countTrees() {
        List<List<Character>> map = readInput();

        long treesBySlopeOne = countTreesBySlope(map, 3);
        long treesBySlopeTwo = countTreesBySlope(map, 1);
        long treesBySlopeThree = countTreesBySlope(map, 5);
        long treesBySlopeFour = countTreesBySlope(map, 7);

        //create map for traversing 2 positions down on each iteration
        List<List<Character>> filteredMap = IntStream.range(0, map.size())
                .filter(i -> i % 2 == 0)
                .mapToObj(map::get)
                .collect(Collectors.toList());

        long treesBySlopeFive = countTreesBySlope(filteredMap, 1);

        System.out.println("Number of trees encountered using first slope: " + treesBySlopeOne);

        long result = treesBySlopeOne * treesBySlopeTwo * treesBySlopeThree * treesBySlopeFour * treesBySlopeFive;
        System.out.println("Product of number of trees encountered using all slopes: " + result);
    }

    /**
     * Traverse a matrix starting in the top-left corner and moving each time offsetValue positions to the right, 1 position down.
     * Count all the trees (marked '#' in the matrix) you would encounter for the slope.
     *
     * @param map         a matrix of characters
     * @param offsetValue number of positions to move to the right at each iteration
     * @return number of trees encountered
     */
    private long countTreesBySlope(List<List<Character>> map, final int offsetValue) {
        AtomicInteger trees = new AtomicInteger();
        AtomicInteger offset = new AtomicInteger();
        int sequenceLength = map.get(0).size();

        map.stream().skip(1).forEach(line -> {
            char location = line.get(offset.addAndGet(offsetValue) % sequenceLength);
            if (location == TREE) {
                trees.getAndIncrement();
            }
        });

        return trees.get();
    }

    private List<List<Character>> readInput() {
        return InputReader.readInputFile(INPUT_FILE)
                .stream()
                .map(line -> line.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
