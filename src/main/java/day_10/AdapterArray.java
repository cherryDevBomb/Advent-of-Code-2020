package day_10;

import util.InputReader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdapterArray {

    private static final String INPUT_FILE = "input_10.txt";

    public static void main(String[] args) {
        new AdapterArray().processCustomsDeclarationsForms();
    }

    private void processCustomsDeclarationsForms() {
        List<Integer> adapters = InputReader.readInputFile(INPUT_FILE).stream()
                .map(Integer::parseInt)
                .sorted()
                .collect(Collectors.toList());

        System.out.println("Jolts of 1 * Jolts of 3: " + calculateProductOfJolts(adapters));
        System.out.println("Adapters can be arranged in that many ways: " + getTotalNumberOfWays(adapters));
    }

    /**
     * Given a sorted list of numbers, calculate the occurrence of 2 consecutive numbers having difference 1 or 3.
     * Calculate the product of that occurrences: numberOfDifferences1 * numberOfDifferences3
     *
     * @param adapters sorted list of numbers
     * @return numberOfDifferences1 * numberOfDifferences3
     */
    private int calculateProductOfJolts(List<Integer> adapters) {
        AtomicInteger jolts1 = new AtomicInteger(0);
        AtomicInteger jolts3 = new AtomicInteger(0);

        adapters.stream().reduce(0, (adapter1, adapter2) -> {
            if (adapter2 - adapter1 == 1) {
                jolts1.getAndIncrement();
            } else if (adapter2 - adapter1 == 3) {
                jolts3.getAndIncrement();
            }
            return adapter2;
        });

        return jolts1.get() * jolts3.incrementAndGet();
    }

    /**
     * Given a sorted list of numbers, calculate in how many ways you can arrange its elements,
     * so that each 2 consecutive numbers will have a minimum difference of 1 and a maximum difference of 3.
     *
     * @param adapters sorted list of numbers
     * @return total number of ways adapters elements can be rearranged while still following the rule
     */
    private long getTotalNumberOfWays(List<Integer> adapters) {
        Map<Integer, Long> paths = adapters.stream().collect(Collectors.toMap(adapter -> adapter, adapter -> 0L));
        paths.put(adapters.get(0), 1L);

        adapters.forEach(adapter -> IntStream.rangeClosed(1, 3)
                .forEach(offset -> {
                    int offsetAdapter = adapter + offset;
                    if (adapters.contains(offsetAdapter)) {
                        paths.put(offsetAdapter, paths.get(offsetAdapter) + paths.get(adapter));
                    }
                }));

        return paths.get(adapters.get(adapters.size() - 1));
    }
}
