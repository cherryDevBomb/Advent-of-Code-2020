package day_23;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import util.InputReader;

public class CrabCups {

    private static final String INPUT_FILE = "input_23.txt";

    long calcFromToTime = 0;
    long rotateIfOutOfBoundsTime = 0;
    long sublistTime = 0;
    long removeTime = 0;

    public static void main(String[] args) {
        new CrabCups().playCups();
    }

    private void playCups() {
        List<Long> inputCups = InputReader.readInputFile(INPUT_FILE).get(0).chars()
                .mapToLong(c -> Long.parseLong(Character.toString((char) c))).boxed().collect(Collectors.toList());

//        List<Long> gameResult = playGame(new ArrayList<>(inputCups), 100);
//        String finalLabeling = Stream.concat(gameResult.subList(gameResult.indexOf(1L) + 1, gameResult.size()).stream(), gameResult.subList(0, gameResult.indexOf(1L)).stream())
//                .map(Object::toString)
//                .collect(Collectors.joining(""));

        List<Long> millionCups = new ArrayList<>(inputCups);
        millionCups.addAll(LongStream.rangeClosed(10, 1_000_000).boxed().collect(Collectors.toList()));
        List<Long> millionGameResult = playGame(millionCups, 10_000_000);
        long starProduct = millionGameResult.get(millionGameResult.indexOf(1L) + 1) * millionGameResult.get(millionGameResult.indexOf(1L) + 2);

//        List<Long> millionCups = new ArrayList<>(inputCups);
//        millionCups.addAll(LongStream.rangeClosed(10, 1_000_000).boxed().collect(Collectors.toList()));
//        List<Long> millionGameResult = playGame(millionCups, 100_000);
//        long starProduct = millionGameResult.get(millionGameResult.indexOf(1L) + 1) * millionGameResult.get(millionGameResult.indexOf(1L) + 2);


//        System.out.println("Cups after 100 moves: " + finalLabeling);
        System.out.println("Product of the two cups that will end up immediately after cup 1 after 10 million moves: " + starProduct);
    }

    private List<Long> playGame(List<Long> cups, long nrOfRounds) {
        int currentCupIndex = 0;
        Long currentCup = cups.get(currentCupIndex);
        long maxCup = cups.size();
        AtomicBoolean hasRotated = new AtomicBoolean();

//        long start = 0;
//        long checkpoint = 0;
//        long pickUpTime = 0;
//        long chooseDestinationTime = 0;
//        long addPickUpTime = 0;
//        long shiftCurrentCupTime = 0;

        for (long i = 0; i < nrOfRounds; i++) {
//            start = System.currentTimeMillis();

            List<Long> pickUp = pickUpCups(cups, currentCup, currentCupIndex, hasRotated);

//            checkpoint = System.currentTimeMillis();
//            pickUpTime += checkpoint - start;
//            start = System.currentTimeMillis();

            long destinationCup = currentCup - 1;
            while (pickUp.contains(destinationCup) || destinationCup == 0) {
                destinationCup--;
                if (destinationCup <= 0) {
                    destinationCup = maxCup;
                }
            }

//            checkpoint = System.currentTimeMillis();
//            chooseDestinationTime += checkpoint - start;
//            start = System.currentTimeMillis();

            int destinationCupIndex = cups.indexOf(destinationCup);
            cups.addAll(destinationCupIndex + 1, pickUp);

//            checkpoint = System.currentTimeMillis();
//            addPickUpTime += checkpoint - start;
//            start = System.currentTimeMillis();

            // calculating cups.indexOf(currentCup) is not time efficient. It is the only way to calculate next currentCupIndex when the array has rotated.
            // Otherwise, since the pick-up will always have a size of 3 elements, use destinationCupIndex to calculate next currentCupIndex:
            // if pick up was placed after the previous currentCup, currentCupIndex will shift with 1 position to the right.
            // if pick up was placed before the previous currentCup, currentCupIndex will shift with 4 position to the right.
            if (hasRotated.get()) {
                currentCupIndex = cups.indexOf(currentCup) + 1;
                hasRotated.set(false);
            } else {
                currentCupIndex = destinationCupIndex >= currentCupIndex ? currentCupIndex + 1 : currentCupIndex + 4;
            }
            if (currentCupIndex >= cups.size()) {
                currentCupIndex = 0;
            }
            currentCup = cups.get(currentCupIndex);

//            checkpoint = System.currentTimeMillis();
//            shiftCurrentCupTime += checkpoint - start;

            System.out.println("Completed round " + i);
        }

        System.out.println("Completed while with all rounds");

//        System.out.println("pickUpTime = " + pickUpTime);
//        System.out.println("chooseDestinationTime = " + chooseDestinationTime);
//        System.out.println("addPickUpTime = " + addPickUpTime);
//        System.out.println("shiftCurrentCupTime = " + shiftCurrentCupTime);
//
//        System.out.println("calcFromToTime = " + calcFromToTime);
//        System.out.println("rotateIfOutOfBoundsTime = " + rotateIfOutOfBoundsTime);
//        System.out.println("sublistTime = " + sublistTime);
//        System.out.println("removeTime = " + removeTime);

        return cups;
    }



    /**
     * Extract 3 elements that are exactly after the currentCup in the cups list and delete them from the cups list.
     * Assume list is circular meaning that the last element is followed by the first.
     *
     * @param cups       list representing all cup labels
     * @param currentCup label of current cup
     * @return list containing the labels of 3 cups following the current cup.
     */
    private List<Long> pickUpCups(List<Long> cups, Long currentCup, int currentCupIndex, AtomicBoolean hasRotated) {
//        long start = 0;
//        long checkpoint = 0;
//
//        start = System.currentTimeMillis();

        int from = currentCupIndex + 1;
        int to = from + 3;

//        checkpoint = System.currentTimeMillis();
//        calcFromToTime += checkpoint - start;
//        start = System.currentTimeMillis();

        //rotate cups if indices are out of bounds
        if (to > cups.size()) {
            List<Long> head = new ArrayList<>(cups.subList(0, 3));
//            cups.removeAll(head);
            cups.remove(0);
            cups.remove(0);
            cups.remove(0);
            cups.addAll(head);
            from = cups.indexOf(currentCup) + 1;
            to = from + 3;
            hasRotated.set(true);
        }

//        checkpoint = System.currentTimeMillis();
//        rotateIfOutOfBoundsTime += checkpoint - start;
//        start = System.currentTimeMillis();

        List<Long> pickUp = new ArrayList<>(cups.subList(from, to));

//        checkpoint = System.currentTimeMillis();
//        sublistTime += checkpoint - start;
//        start = System.currentTimeMillis();

        //this is 35x times faster than removeAll()
        cups.remove(from);
        cups.remove(from);
        cups.remove(from);

//        checkpoint = System.currentTimeMillis();
//        removeTime += checkpoint - start;

        return pickUp;
    }
}
