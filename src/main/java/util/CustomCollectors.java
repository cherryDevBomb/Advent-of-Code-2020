package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CustomCollectors {

    /**
     * Create a Collector for Stream<String> that combines elements into chunks splitting by a separator.
     * Elements that belong into a chunk are combined into a single element using a StringBuilder.
     * Example: For initial input ["a", "b", null, "c", "d"] and null as separator, we will get ["a b", "c d"]
     *
     * @param separator Predicate used to determine split elements
     * @param unifier   String used to separate the lines in a chunk in the resulting String
     * @return List of String elements, one element per chunk
     */
    public static Collector<String, List<StringBuilder>, List<String>> collectChunks(Predicate<String> separator, String unifier) {
        return Collector.of(() -> new ArrayList<>(Arrays.asList(new StringBuilder())),
                (acc, elem) -> {
                    if (separator.test(elem)) {
                        acc.add(new StringBuilder());
                    } else {
                        acc.get(acc.size() - 1).append(unifier).append(elem);
                    }
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                },
                elem -> elem.stream().map(StringBuilder::toString).collect(Collectors.toList()));
    }

    /**
     * Create a Collector for Stream<String> that groups strings into lists splitting them by a separator.
     * Example: For initial input ["a", "b", "", "c", "d"] and the empty string as separator, we will get [["a", "b"], ["c", "d"]]
     *
     * @param separator Predicate used to determine split elements
     * @return List of Lists of String elements grouped
     */
    public static Collector<String, List<List<String>>, List<List<String>>> collectLineGroups(Predicate<String> separator) {
        return Collector.of(() -> {
                    List<List<String>> acc = new ArrayList<>();
                    acc.add(new ArrayList<>());
                    return acc;
                },
                (acc, elem) -> {
                    if (separator.test(elem)) {
                        acc.add(new ArrayList<>());
                    } else {
                        acc.get(acc.size() - 1).add(elem);
                    }
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                });
    }
}
