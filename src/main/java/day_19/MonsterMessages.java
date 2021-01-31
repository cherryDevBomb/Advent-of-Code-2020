package day_19;

import org.apache.commons.lang3.StringUtils;
import util.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MonsterMessages {

    private static final String INPUT_FILE = "input_19.txt";

    public static void main(String[] args) {
        new MonsterMessages().analyzeMessages();
    }

    private void analyzeMessages() {
        List<List<String>> input = InputReader.readInputFileLineGroups(INPUT_FILE);

        Map<String, Rule> initialRules = input.get(0).stream()
                .map(line -> line.split(": "))
                .collect(Collectors.toMap(line -> line[0], line -> {
                    if (line[1].contains("\"")) {
                        return new Rule(line[1].substring(1, line[1].length() - 1));
                    } else {
                        List<List<String>> composedRules = Arrays.stream(line[1].split(" \\| "))
                                .map(subRule -> Arrays.asList(subRule.split(" ")))
                                .collect(Collectors.toList());
                        return new Rule(composedRules);
                    }
                }));

        List<String> messages = input.get(1);

        Map<String, Rule> rules = deriveRules(initialRules);
        long matchRuleZero = countMatchingZero(rules.get("0"), messages);

        long matchRuleZeroUpdated = countMatchingZeroRecursion(messages, rules);

        System.out.println("Number of messages that completely match rule 0: " + matchRuleZero);
        System.out.println("Number of messages that completely match rule 0: " + matchRuleZeroUpdated);
    }

    /**
     * Count the messages that match the given rule, assuming the rules contain no recursion.
     *
     * @param rule     rule to match
     * @param messages list of strings to match
     * @return number of matching strings in messages
     */
    private long countMatchingZero(Rule rule, List<String> messages) {
        return messages.stream()
                .filter(message -> rule.getLexeme().contains(message))
                .count();
    }

    /**
     * Solution for rule "0: 8 11" after updating the following 2 rules:
     * 8: 42 | 42 8
     * 11: 42 31 | 42 11 31
     * This eventually resolves to at least one 42 followed by an equal number of 42s and 31s.
     *
     * @param messages list of strings to match
     * @param allRules list of all derived rules
     * @return number of matching strings in messages
     */
    private long countMatchingZeroRecursion(List<String> messages, Map<String, Rule> allRules) {
        return messages.parallelStream()
                .filter(message -> {
                    int index = 0;
                    int occurrences42 = 0;
                    int occurrences31 = 0;
                    boolean barrierCrossed = false;
                    while (index < message.length()) {
                        String part = message.substring(index, index + 8);
                        index += 8;
                        if (!barrierCrossed && allRules.get("42").getLexeme().contains(part)) {
                            occurrences42++;
                        } else if (!barrierCrossed && allRules.get("31").getLexeme().contains(part)) {
                            occurrences31++;
                            barrierCrossed = true;
                        } else if (barrierCrossed && allRules.get("31").getLexeme().contains(part)) {
                            occurrences31++;
                        } else {
                            return false;
                        }
                    }
                    return occurrences31 >= 1 && occurrences42 > occurrences31;
                })
                .count();
    }

    /**
     * Starting with the initial set of rules, derive them until the rule resolves to simple lexemes or until recursion occurs.
     *
     * @param rules initial set of rules
     * @return derived set of rules
     */
    private Map<String, Rule> deriveRules(Map<String, Rule> rules) {
        AtomicBoolean hasDerived = new AtomicBoolean(true);
        while (hasDerived.get()) {
            hasDerived.set(false);
            rules.forEach((key, value) -> {
                if (!value.isFinal()) {
                    hasDerived.set(true);
                    List<List<String>> derivedRule = value.getComposedRules().stream()
                            .map(subRule -> {
                                List<String> subRuleCopy = new ArrayList<>(subRule);
                                List<List<String>> derivedSubRules = new ArrayList<>();
                                AtomicBoolean hasFinalTokens = new AtomicBoolean(false);
                                subRule.stream().forEach(token -> {
                                    if (StringUtils.isNumeric(token) && rules.get(token).isFinal()) {
                                        hasFinalTokens.set(true);
                                        int tokenIndex = subRule.indexOf(token);
                                        List<List<String>> thisDerived = rules.get(token).getLexeme().stream().map(lexeme -> {
                                            subRuleCopy.set(tokenIndex, lexeme);
                                            List<String> subRuleCopyCopy = new ArrayList<>(subRuleCopy);
                                            subRuleCopyCopy.set(tokenIndex, lexeme);
                                            return subRuleCopyCopy;
                                        }).collect(Collectors.toList());
                                        derivedSubRules.addAll(thisDerived);
                                    }
                                });
                                if (!hasFinalTokens.get()) {
                                    derivedSubRules.add(subRule);
                                }
                                return derivedSubRules;
                            })
                            .flatMap(List::stream)
                            .distinct()
                            .collect(Collectors.toList());

                    // all derivations complete -> convert rule to final
                    if (derivedRule.stream().allMatch(subRule -> subRule.stream().allMatch(StringUtils::isAlpha))) {
                        value.setLexeme(deriveLexemes(derivedRule));
                        value.setFinal(true);
                    } else {
                        value.setComposedRules(derivedRule);
                        if (derivedRule.stream().allMatch(subRule -> subRule.stream().allMatch(lexeme -> StringUtils.isAlpha(lexeme) || key.equals(lexeme)))) {
                            value.setFinal(true);
                        }
                    }
                }
            });
        }

        return rules;
    }

    /**
     * Use a list of composed rule list to derive its list of lexemes.
     *
     * @param composedRules
     * @return list of lexemes for given rule
     */
    private List<String> deriveLexemes(List<List<String>> composedRules) {
        return composedRules.stream()
                .map(subRule -> String.join("", subRule))
                .collect(Collectors.toList());
    }
}
