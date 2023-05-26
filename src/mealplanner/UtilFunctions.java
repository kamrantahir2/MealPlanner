package mealplanner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UtilFunctions {

    private static long idCounter = 0;
    private static long ingredientCounter = 0;

    public int createID() {
        return (int) idCounter ++;
    }

    public int createIngredientID() {
        return (int) ingredientCounter ++;
    }

    final String DB_URL = "jdbc:postgresql:meals_db";
    final String USER = "postgres";
    final String PASS = "1111";

     final Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);

     Statement statement = connection.createStatement();
     Statement stmt = connection.createStatement();

    public Connection getConnection() {
        return connection;
    }

    public UtilFunctions() throws SQLException {}

    public List<String> createIngredientsList(String str) {
        String[] splitStr = str.split(",");
        List<String> ingredientsList = new ArrayList<>();
        for (String ingredient : splitStr) {
            String trimmed = ingredient.trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            ingredientsList.add(trimmed);
        }

        List<String> newList = ingredientsList;

        for (int i = 0; i < ingredientsList.size()-1; i++) {
            String temp = ingredientsList.get(i);
            for (int j = i + 1; j < newList.size(); j++) {
                if (temp.equals(ingredientsList.get(j))) {
                    newList.remove(i);
                }
            }
        }

        return newList;
    }

    public Meal createMeal(Scanner scanner) throws SQLException {
        String meal;
        String name;
        String ingredients;
        int mealId = createID();

        ResultSet rs = statement.executeQuery("SELECT meal_id FROM meals");

        // IF THE ID ALREADY EXISTS, ADD 1 UNTIL YOU REACH THE END WHERE THE ID WILL BE UNIQUE
        while (rs.next()) {
            if (mealId == rs.getInt(("meal_id"))) {
                mealId++;
            }
        }

        while (true) {
            System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
            meal = scanner.nextLine();

            if (!meal.equals("breakfast") && !meal.equals("lunch") && !meal.equals("dinner")) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                continue;
            }

            break;
        }
        while (true) {
            System.out.println("Input the meal's name:");
            name = scanner.nextLine();
            if (name.isBlank()) {
                System.out.println("Wrong format. Use letters only!");
                continue;
            }
            char[] nameCharArr = name.toCharArray();
            boolean nameHasDigit = false;
            for (int i = 0; i < nameCharArr.length; i++) {
                if (Character.isWhitespace(nameCharArr[i])) {
                    continue;
                }
                if (nameCharArr[i] == ',') {
                    continue;
                }
                if (!Character.isLetter(nameCharArr[i])) {
                    nameHasDigit = true;
                    break;
                }
            }
            if (nameHasDigit) {
                System.out.println("Wrong format. Use letters only!");
                continue;
            }
            break;
        }

        statement.executeUpdate("INSERT INTO meals (category, meal, meal_id) VALUES ('" + meal +"', '" + name + "', '" + mealId + "')");


        while (true) {
            System.out.println("Input the ingredients:");
            ingredients = scanner.nextLine();
            if (ingredients.isBlank()) {
                System.out.println("Wrong format. Use letters only!");
                continue;
            }

            String[] splitted = ingredients.split(",");
            boolean emptyIngredient = false;

            for (String str : splitted) {
                String trimmed = str.trim();
                if (trimmed.isEmpty()) {
                    emptyIngredient = true;
                }
            }

            if (emptyIngredient) {
                System.out.println("Wrong format. Use letters only!");
                continue;
            }

            char[] nameCharArr = ingredients.toCharArray();
            boolean nameHasDigit = false;
            for (int i = 0; i < nameCharArr.length; i++) {
                if (Character.isWhitespace(nameCharArr[i]) || nameCharArr[i] == ',') {
                    continue;
                }
                if (!Character.isLetter(nameCharArr[i])) {
                    nameHasDigit = true;
                    break;
                }
            }
            if (nameHasDigit) {
                System.out.println("Wrong format. Use letters only!");
                continue;
            }
            break;
        }
        List<String> ingredientsList = createIngredientsList(ingredients);

        for (String ing: ingredientsList) {
            statement.executeUpdate("INSERT INTO ingredients (ingredient, ingredient_id, meal_id) VALUES ( '" + ing + "', '" +
                    createIngredientID() + "', '" +
                    mealId + "'" +
                    ")");
        }
        return new Meal(meal, name, ingredients);
    }

    public void printAllMeals() throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM meals");
        printResults(rs);
    }

    public void printResults(ResultSet rs) throws SQLException {
        while (rs.next()) {
            System.out.println("Name: " + rs.getString("meal"));
            ResultSet rsIngredients = stmt.executeQuery("SELECT ingredient FROM ingredients WHERE meal_id = '" + rs.getInt("meal_id") + "'");
            System.out.println("Ingredients:");
            while (rsIngredients.next()) {
                System.out.println(rsIngredients.getString("ingredient"));
            }
        }
    }

    public void printByCategory(String category) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM meals WHERE category = '" + category + "'");

        // IF NO RESULTS:
        ResultSet rsCount = stmt.executeQuery("SELECT count(*) AS cat_count FROM meals WHERE category = '" + category + "'");

        if (rsCount.next()) {
            if (rsCount.getInt("cat_count") == 0) {
                System.out.println("No meals found.");
            } else {
                System.out.println("Category: " + category);
            }
        }
        printResults(rs);
    }

}