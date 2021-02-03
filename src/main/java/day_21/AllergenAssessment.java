package day_21;

import util.InputReader;

import java.util.*;
import java.util.stream.Collectors;

public class AllergenAssessment {

    private static final String INPUT_FILE = "input_21.txt";

    private static final String SEPARATOR = " \\(contains ";

    public static void main(String[] args) {
        new AllergenAssessment().analyzeIngredients();
    }

    private void analyzeIngredients() {
        List<Food> foods = InputReader.readInputFile(INPUT_FILE).stream()
                .map(line -> line.split(SEPARATOR))
                .map(line -> new Food(Arrays.asList(line[0].split(" ")), Arrays.asList(line[1].substring(0, line[1].length() - 1).split(", "))))
                .collect(Collectors.toList());

        Map<String, String> ingredientMap = getIngredientAllergenMap(foods);

        long nonAllergicAppearances = foods.stream()
                .map(Food::getIngredients)
                .flatMap(List::stream)
                .filter(ingredient -> !ingredientMap.containsKey(ingredient))
                .count();

        String dangerousIngredients = new ArrayList<>(ingredientMap.entrySet())
                .stream().sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));

        System.out.println("Ingredients that cannot contain any allergens appear this number of times: " + nonAllergicAppearances);
        System.out.println("Canonical list of dangerous ingredients: " + dangerousIngredients);
    }

    /**
     * Compute a Map where each ingredient is mapped to its corresponding allergen.
     * An ingredient contains at most one allergen. Ingredients that contain no allergens are ommited.
     *
     * @param foods list of Food items that contain a list of ingredients and a list of allergens
     * @return Map where each ingredient is mapped to its corresponding allergen
     */
    private Map<String, String> getIngredientAllergenMap(List<Food> foods) {
        Map<String, List<List<String>>> allergenMap = new HashMap<>();
        foods.forEach(food -> food.getAllergens().forEach(allergen -> {
            allergenMap.computeIfAbsent(allergen, x -> new ArrayList<>());
            allergenMap.get(allergen).add(food.getIngredients());
        }));

        Map<String, String> ingredientMap = new HashMap<>();
        while (ingredientMap.size() < allergenMap.size()) {
            allergenMap.forEach((key, value) -> {
                if (!ingredientMap.containsValue(key)) {
                    List<String> ingredientIntersection = value.stream()
                            .collect(() -> new ArrayList<>(value.get(0)), List::retainAll, List::retainAll);
                    ingredientIntersection.removeAll(ingredientMap.keySet());
                    if (ingredientIntersection.size() == 1) {
                        ingredientMap.put(ingredientIntersection.get(0), key);
                    }
                }
            });
        }
        return ingredientMap;
    }
}
