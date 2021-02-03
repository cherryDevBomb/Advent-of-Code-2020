package day_21;

import java.util.List;

public class Food {

    private List<String> ingredients;
    private List<String> allergens;

    public Food(List<String> ingredients, List<String> allergens) {
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<String> allergens) {
        this.allergens = allergens;
    }
}
