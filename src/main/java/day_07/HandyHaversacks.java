package day_07;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.InputReader;

public class HandyHaversacks {

    private static final String INPUT_FILE = "input_07.txt";

    private static final String SHINY_GOLD = "shiny gold";

    public static void main(String[] args) {
        new HandyHaversacks().processLuggage();
    }

    private void processLuggage() {
        Map<String, Map<String, Integer>> rules = InputReader.readInputFile(INPUT_FILE).stream()
                .map(line -> line.split(" bags contain "))
                .collect(Collectors.toMap(line -> line[0], line -> getAllowedContent(line[1])));

        long canContainShinyGold = rules.entrySet().stream()
                .filter(entry -> flatBagCanContain(rules, entry.getKey()).anyMatch(SHINY_GOLD::equals))
                .count();

        long shinyGoldShouldContain = flatBagMustContain(rules, SHINY_GOLD).count();

        System.out.println("Number of bag colors that can eventually contain at least one shiny gold bag: " + canContainShinyGold);
        System.out.println("Total number of bags a shiny gold bag should contain: " + shinyGoldShouldContain);

    }

    /**
     * Parse rules regarding the content of a bag to create a map with color as a key and max occurrences as a value.
     * Example: given "1 bright white bag, 4 muted yellow bags." return {"bright white": 1, "muted yellow": 4}
     *
     * @param contentRules string of rules separated by comma.
     * @return map of rules with color as a key and max occurrences as a value
     */
    private Map<String, Integer> getAllowedContent(String contentRules) {
        return contentRules.contains("no other bags") ? Collections.emptyMap() :
                Arrays.stream(contentRules.replaceAll("\\.| bags| bag", "")
                        .split(", "))
                        .map(rule -> rule.split(" "))
                        .collect(Collectors.toMap(rule -> rule[1].concat(" ").concat(rule[2]), rule -> Integer.parseInt(rule[0])));
    }

    /**
     * Get the list of colors that a bag of a specified color can contain
     *
     * @param rules map that specifies a list of allowed colors and their max occurrence for each existing color
     * @param color for which the result is computed
     * @return stream of colors that a bag of a specified color can contain
     */
    private Stream<String> flatBagCanContain(Map<String, Map<String, Integer>> rules, String color) {
        return Stream.concat(rules.get(color).keySet().stream(),
                rules.get(color).keySet().stream().flatMap(value -> flatBagCanContain(rules, value)))
                .distinct();
    }

    /**
     * Get the list of all bags that a bag of a specified color must contain
     *
     * @param rules map that specifies a list of allowed colors and their max occurrence for each existing color
     * @param color for which the result is computed
     * @return stream of colors that a bag of a specified color must contain, including duplicates
     */
    private Stream<String> flatBagMustContain(Map<String, Map<String, Integer>> rules, String color) {
        List<String> directBagContent = rules.get(color).entrySet().stream()
                .map(entry -> Collections.nCopies(entry.getValue(), entry.getKey()))
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return Stream.concat(directBagContent.stream(),
                directBagContent.stream().flatMap(value -> flatBagMustContain(rules, value)));
    }
}
