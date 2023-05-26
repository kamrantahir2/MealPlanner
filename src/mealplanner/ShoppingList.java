package mealplanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShoppingList {
    UtilFunctions util = new UtilFunctions();
    File file;
    FileWriter writer;

    public ShoppingList(String fileName) throws IOException, SQLException {
        Statement statement = util.connection.createStatement();
        statement.executeUpdate("DROP TABLE IF EXISTS shopping_list");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS shopping_list (" +
                "ingredient VARCHAR(30))");
        statement.close();

        try {
            file = new File(fileName);
            writer = new FileWriter(file);
        }
        catch (IOException e) {
            System.out.println("FileNotFound. Error: " + e.getMessage());
        }

        addToShoppingList();
        checkDuplicates();
        System.out.println("Saved!");
        writer.close();
    }

    public void addToShoppingList() throws SQLException {
        Statement statement = util.connection.createStatement();
        Statement stmt = util.connection.createStatement();
        Statement s = util.connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT meal_id FROM plan");
        while (rs.next()) {
           int mealId = rs.getInt("meal_id");
           ResultSet rsStmt = stmt.executeQuery("SELECT DISTINCT ingredient from ingredients WHERE meal_id = '" + mealId + "'");
           while (rsStmt.next()) {
               String ingredient = rsStmt.getString("ingredient");
               s.executeUpdate("INSERT INTO shopping_list VALUES ('" + ingredient + "')");
           }
        }
    }


    public void checkDuplicates() throws SQLException, IOException {
        Statement statement = util.connection.createStatement();
        Statement stmt = util.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT DISTINCT ingredient FROM shopping_list");
        while (rs.next()) {
            String ingredient = rs.getString("ingredient");
            ResultSet rsDup = stmt.executeQuery("SELECT count(ingredient) AS count FROM shopping_list WHERE ingredient = '" + ingredient + "'");
            while (rsDup.next()) {
                int count = rsDup.getInt("count");
                if (count == 1) {
                    try {
                        writer.write(ingredient + "\n");
                    } catch (IOException e) {
                        System.out.printf("An exception has occurred %s" , e.getMessage());
                    }
                } else {
                    writer.write(ingredient + " x" + count + "\n");
                }
            }
        }

        rs.close();
        statement.close();
        stmt.close();
    }


}