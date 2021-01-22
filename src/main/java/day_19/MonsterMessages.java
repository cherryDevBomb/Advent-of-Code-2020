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

        initialRules.put("8", new Rule(Arrays.asList(Arrays.asList("42"), Arrays.asList("42", "8"))));
        initialRules.put("11", new Rule(Arrays.asList(Arrays.asList("42", "31"), Arrays.asList("42", "11", "31"))));
        Map<String, Rule> rulesUpdated = deriveRules(initialRules);
        long matchRuleZeroUpdated = countMatchingZero(rulesUpdated.get("0"), messages);

        System.out.println("Number of messages that completely match rule 0: " + matchRuleZero);
        System.out.println("Number of messages that completely match rule 0: " + matchRuleZeroUpdated);
    }

    private long countMatchingZero(Rule rule, List<String> messages) {
        return messages.stream()
                .filter(message -> rule.getLexeme().contains(message))
                .count();
    }

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
                        List<String> lexemes = derivedRule.stream()
                                .map(subRule -> String.join("", subRule))
                                .collect(Collectors.toList());
                        value.setLexeme(lexemes);
                        value.setFinal(true);
                    } else {
                        value.setComposedRules(derivedRule);
                    }
                }
            });
        }

        return rules;
    }
}
