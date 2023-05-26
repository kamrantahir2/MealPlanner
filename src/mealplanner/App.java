package mealplanner;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class App {
    static Scanner scanner = new Scanner(System.in);

    public App() throws SQLException {
    }

    public static void main(String[] args) throws SQLException, IOException {
        UtilFunctions util = new UtilFunctions();

        Connection connection = util.getConnection();
        connection.setAutoCommit(true);

        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS meals " +
                "(category VARCHAR(30), " +
                "meal VARCHAR(30), " +
                "meal_id INTEGER NOT NULL" +
                ")");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS ingredients " +
                "(ingredient VARCHAR(30), " +
                "ingredient_id INTEGER, " +
                "meal_id INTEGER)");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS plan" +
                "(day VARCHAR(30)," +
                "category VARCHAR(30)," +
                "meal VARCHAR(30)," +
                "meal_id INTEGER)");

        while (true) {
            boolean finished = false;
            System.out.println("What would you like to do (add, show, plan, save, exit)?");
            String choice = scanner.nextLine();
            switch (choice) {
                case "add":
                    addMeal();
                    break;
                case "show":
                    promptForCategory();
                    break;
                case "plan":
                    plan();
                    break;
                case "save":
                    save();
                    break;
                case "exit":
                    finished = true;
                    break;
            }
            if (finished) {
                System.out.println("Bye!");
                break;
            }
        }

    }

    public static void addMeal() throws SQLException {
        UtilFunctions util = new UtilFunctions();
        Meal food = util.createMeal(scanner);
    }

    public static void promptForCategory() throws SQLException {
        UtilFunctions util = new UtilFunctions();
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        String category = scanner.nextLine().trim();
        while (true) {
            if (category.equals("breakfast") || category.equals("lunch") || category.equals("dinner")) {
                util.printByCategory(category);
                break;
            } else {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                category = scanner.nextLine().trim();
            }
        }
    }

    public static void plan() throws SQLException {
        Plan plan = new Plan();
        plan.planByDay("Monday");
        plan.planByDay("Tuesday");
        plan.planByDay("Wednesday");
        plan.planByDay("Thursday");
        plan.planByDay("Friday");
        plan.planByDay("Saturday");
        plan.planByDay("Sunday");
        plan.printFullPlan();

    }

    public static void save() throws SQLException, IOException {
        UtilFunctions util = new UtilFunctions();
        Statement statement = util.connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT count(meal) AS meal_count from meals");

        if (rs.next()) {
            if (rs.getInt("meal_count") == 0) {
                System.out.println("Unable to save. Plan your meals first.");
            }
            else {
                System.out.println("Input a filename:");
                String fileName = scanner.nextLine();
                ShoppingList sp = new ShoppingList(fileName);
            }
        }
    }



}