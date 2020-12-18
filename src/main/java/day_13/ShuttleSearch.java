package day_13;

import util.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class ShuttleSearch {

    private static final String INPUT_FILE = "input_13.txt";

    public static void main(String[] args) {
        new ShuttleSearch().findBus();
    }

    private void findBus() {
        List<String> input = InputReader.readInputFile(INPUT_FILE);

        int myTimestamp = Integer.parseInt(input.get(0));
        List<Integer> availableBusses = Arrays.stream(input.get(1).split(","))
                .filter(bus -> !"x".equals(bus))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int product = findProductOfEarliestBusAndWaitingTime(myTimestamp, availableBusses);
        long earliestTimestamp = findEarliestTimestamp(Arrays.asList(input.get(1).split(",")));

        System.out.println("The ID of the earliest bus I can take multiplied by the number of minutes I'll need to wait for that bus: " + product);
        System.out.println("The earliest timestamp such that all bus IDs depart at offsets matching their positions in the list: " + earliestTimestamp);
    }

    /**
     * Find the first number greater or equal myTimestamp that is a multiple of one of the numbers in availableBusses,
     * multiplied by the difference between it and myTimestamp.
     *
     * @param myTimestamp     number from which to start
     * @param availableBusses list of integers among which one should be a divisor
     * @return first found divisor * (foundMultiple - myTimestamp)
     */
    private int findProductOfEarliestBusAndWaitingTime(int myTimestamp, List<Integer> availableBusses) {
        AtomicInteger timestamp = new AtomicInteger(myTimestamp);
        while (true) {
            timestamp.getAndIncrement();
            Optional<Integer> foundBus = availableBusses.stream()
                    .filter(bus -> timestamp.get() % bus == 0)
                    .findFirst();
            if (foundBus.isPresent()) {
                return foundBus.get() * (timestamp.get() - myTimestamp);
            }
        }
    }

    /**
     * Find the minimum number x such that for every busId in the list, x mod busId equals to the index of the bus in the list.
     *
     * @param busList list of busIds, where x represents an unknown bus with no constraints.
     * @return minimum number x
     */
    private long findEarliestTimestamp(List<String> busList) {
        Map<Long, Integer> busIndexMap = busList.stream()
                .filter(bus -> !"x".equals(bus))
                .collect(Collectors.toMap(Long::parseLong, busList::indexOf));

        long productOfBusIds = busIndexMap.keySet().stream()
                .mapToLong(id -> id)
                .reduce(1, (a, b) -> a * b);

        long sum = busIndexMap.entrySet().stream()
                .mapToLong(entry -> {
                    long remainder = entry.getValue();
                    long prodDivided = productOfBusIds / entry.getKey();
                    long inverse = getModularMultiplicativeInverse(prodDivided, entry.getKey());
                    return remainder * prodDivided * inverse;
                })
                .sum();

        long result = sum % productOfBusIds;
        long result2 = productOfBusIds - result;
        return Math.min(result, result2);
    }

    /**
     * Find the modular multiplicative inverse of a number modulo mod.
     * Assume given numbers are relatively prime.
     *
     * @param number number for which we compute the inverse
     * @param mod    modulo
     * @return modular multiplicative inverse for number modulo mod
     */
    private long getModularMultiplicativeInverse(long number, long mod) {
        return LongStream.range(0, number)
                .filter(nr -> (number * nr) % mod == 1)
                .findFirst()
                .getAsLong();
    }
}
