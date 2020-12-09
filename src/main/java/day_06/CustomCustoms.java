package day_06;

import org.apache.commons.lang3.StringUtils;
import util.InputReader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomCustoms {

    private static final String INPUT_FILE = "input_06.txt";

    public static void main(String[] args) {
        new CustomCustoms().processCustomsDeclarationsForms();
    }

    private void processCustomsDeclarationsForms() {
        List<String> groups = InputReader.readInputFileChunks(INPUT_FILE, " ");

        long sumSomeoneAnswered = groups.stream()
                .map(group -> group.replace(" ", ""))
                .mapToLong(group -> group.chars().distinct().count())
                .sum();

        long sumEveryoneAnswered = groups.stream()
                .mapToLong(group -> getCommonAnswersInAGroup(Arrays.asList(group.split(" "))))
                .sum();

        System.out.println("Sum of questions someone answered with yes: " + sumSomeoneAnswered);
        System.out.println("Sum of questions everyone answered with yes: " + sumEveryoneAnswered);
    }

    /**
     * Get the size of the intersection of characters in a number of lists.
     * Each String in the input represents a list of characters.
     * Example: ["ab", "abc", "ac"] should return 1, since a is the only character all 3 elements have in common.
     *
     * @param groupAnswers List of Strings that represent lists of characters
     * @return number of elements all lists of characters have in common
     */
    private long getCommonAnswersInAGroup(List<String> groupAnswers) {
        return groupAnswers.stream()
                .filter(StringUtils::isNotBlank)
                .map(individualAnswers -> individualAnswers.chars().boxed().collect(Collectors.toList()))
                .reduce((p1, p2) -> p1.stream().filter(p2::contains).collect(Collectors.toList()))
                .get()
                .size();
    }
}
