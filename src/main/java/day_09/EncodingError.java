package day_09;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import util.InputReader;

public class EncodingError {

    private static final String INPUT_FILE = "input_09.txt";

    private static final int PREAMBLE = 25;

    public static void main(String[] args) {
        new EncodingError().processNumbers();
    }

    private void processNumbers() {
        List<Long> numbers = InputReader.readInputFile(INPUT_FILE).stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        long invalidNumber = IntStream.range(PREAMBLE, numbers.size())
                .filter(index -> !isValidNumber(numbers.get(index), numbers.subList(index - PREAMBLE, index)))
                .mapToLong(numbers::get)
                .findFirst()
                .getAsLong();

        // Encryption weakness is the sum of the smallest and largest number from the contiguous set of at least two numbers that sums up to invalidNumber
        long encryptionWeakness = numbers.subList(0, numbers.indexOf(invalidNumber))
                .stream()
                .map(number -> getSumSequence(invalidNumber, numbers.subList(numbers.indexOf(number), numbers.indexOf(invalidNumber))))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::getMinAndMaxSum)
                .findFirst()
                .get();

        System.out.println("First invalid number is: " + invalidNumber);
        System.out.println("Encryption weakness is: " + encryptionWeakness);
    }

    /**
     * Determine if the given number is the sum of any 2 numbers in a given list.
     *
     * @param number   given number
     * @param previous list of numbers
     * @return true if number is the sum of any 2 numbers in previous list, false otherwise
     */
    private boolean isValidNumber(long number, List<Long> previous) {
        return previous.stream().anyMatch(nr1 -> previous.stream().anyMatch(nr2 -> nr1 + nr2 == number));
    }


    /**
     * Extract a sublist from a given sequence for which the sum of its elements is equal to a given number.
     *
     * @param number   the target sum
     * @param sequence list from which the resulting sublist has to be extracted
     * @return sublist that sums up to number
     */
    private Optional<List<Long>> getSumSequence(long number, List<Long> sequence) {
        return IntStream.range(0, sequence.size())
                .boxed()
                .collect(Collectors.toMap(index -> index,
                        index -> sequence.subList(0, index).stream().mapToLong(nr -> nr).sum()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() == number)
                .map(entry -> sequence.subList(0, entry.getKey()))
                .findFirst();
    }

    /**
     * Calculate the sum of the smallest and largest number in a list
     *
     * @param sequence given list
     * @return sum of the smallest and largest number from the list
     */
    private Long getMinAndMaxSum(List<Long> sequence) {
        return sequence.stream().min(Comparator.naturalOrder()).get() + sequence.stream().max(Comparator.naturalOrder()).get();
    }
}
