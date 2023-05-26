package mealplanner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Meal {

    UtilFunctions util = new UtilFunctions();

    private String category;
    private String name;
    private List<String> ingredients;

    public static List<Meal> getHistory() {
        return history;
    }

    private static List<Meal> history = new ArrayList<>();

    public Meal(String category, String name, String ingredientsString) throws SQLException {
        this.category = category;
        this.name = name;
        this.ingredients = util.createIngredientsList(ingredientsString);
        System.out.println("The meal has been added!");
        history.add(this);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void printIngredients() {
        for (int i = 0; i < this.ingredients.size(); i++) {
            System.out.println(this.ingredients.get(i));
        }
    }

    public void printAll() {
        System.out.println();
        System.out.println("Category: " + this.category);
        System.out.println("Name: " + this.name);
        System.out.println("Ingredients:");
        printIngredients();
    }
}