package mealplanner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Plan {
    UtilFunctions util = new UtilFunctions();
    Statement statement = util.connection.createStatement();
    Statement stmt = util.connection.createStatement();

    public Plan() throws SQLException {}

    public void planPrintByCategory(String category) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM meals WHERE category = '" + category + "' GROUP BY category, meal, meal_id ORDER BY meal ASC");

        // IF NO RESULTS:
        ResultSet rsCount = stmt.executeQuery("SELECT count(*) AS cat_count FROM meals WHERE category = '" + category + "'");

        if (rsCount.next()) {
            if (rsCount.getInt("cat_count") == 0) {
                System.out.println("No meals found.");
            } else {
                System.out.println("Category: " + category);
                showAvailableMeals(category);
            }
        }

    }

    public void addMealToPlan(String day, String category, String meal) throws SQLException {
        String mealName = "";
        int mealId = 0;

        ResultSet rs = statement.executeQuery("SELECT DISTINCT * FROM meals WHERE meal = " + "'" + meal + "'");

        if (!rs.next()) {
            System.out.println("This meal doesn’t exist. Choose a meal from the list above.");

            while (!rs.next()) {
                Scanner scanner = new Scanner(System.in);
                meal = scanner.nextLine();
                rs = statement.executeQuery("SELECT DISTINCT * FROM meals WHERE meal = " + "'" + meal + "'");
            }
        }
        mealName = rs.getString("meal");
        mealId = rs.getInt("meal_id");

        statement.executeUpdate("INSERT INTO plan (day, category, meal, meal_id) " +
                "VALUES ('" + day +"', '" + category + "'," +
                "'" + mealName + "', " +
                "'" + mealId + "')");

    }

    public void showAvailableMeals(String category) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT DISTINCT meal FROM meals WHERE category = '" + category + "' ORDER BY meal");

        while (rs.next()) {
            System.out.println(rs.getString("meal"));
        }
    }

    public void planByDay(String day) throws SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println(day);

        // Breakfast
        addByCategory(sc, day, "breakfast");

        // Lunch
        addByCategory(sc, day, "lunch");

        // Dinner
        addByCategory(sc, day, "dinner");

        System.out.println("Yeah! We planned the meals for " + day + ".");

    }

    public void addByCategory(Scanner sc, String day ,String category) throws SQLException {
        showAvailableMeals(category);
        System.out.println("Choose the " + category + " for " + day + " from the list above:");
        String selection  = sc.nextLine();
        addMealToPlan(day, category, selection);
    }

    public void printPlanForDay(String day) throws SQLException {
        System.out.println(day);
        selectMealByCategoryPerDay(day, "breakfast");
        selectMealByCategoryPerDay(day, "lunch");
        selectMealByCategoryPerDay(day, "dinner");
        System.out.println();
    }


    public void selectMealByCategoryPerDay(String day, String category) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM plan WHERE day = '" + day + "' AND category = '" + category + "'");
        while (rs.next()) {
            System.out.println(rs.getString("category") + ": " + rs.getString("meal"));
        }

    }

    public void printFullPlan() throws SQLException {
        printPlanForDay("Monday");
        printPlanForDay("Tuesday");
        printPlanForDay("Wednesday");
        printPlanForDay("Thursday");
        printPlanForDay("Friday");
        printPlanForDay("Saturday");
        printPlanForDay("Sunday");
    }



}